package cn.hex.ddp.manufacture.infrastructure.order.managerimpl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.order.rest.req.GetOrderByStatusReq;
import cn.hex.ddp.manufacture.api.order.rest.req.GetOrderOperationHistoryPageReq;
import cn.hex.ddp.manufacture.api.order.rest.req.GetOrderPageReq;
import cn.hex.ddp.manufacture.domain.order.enums.OrderOperationTypeEnum;
import cn.hex.ddp.manufacture.domain.order.enums.OrderStatusEnum;
import cn.hex.ddp.manufacture.domain.order.manager.OrderManager;
import cn.hex.ddp.manufacture.domain.order.model.*;
import cn.hex.ddp.manufacture.domain.product.model.ProductionItem;
import cn.hex.ddp.manufacture.infrastructure.common.redis.RedisUtil;
import cn.hex.ddp.manufacture.infrastructure.order.managerimpl.converter.OrderInfraConverter;
import cn.hex.ddp.manufacture.infrastructure.order.persistence.po.*;
import cn.hex.ddp.manufacture.infrastructure.order.persistence.postgresql.repository.*;
import cn.hex.ddp.manufacture.infrastructure.product.persistence.po.ProductionItemPO;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单管理实现类
 *
 * @author Huhaisen
 * @date 2024/05/29
 */
@Slf4j
@Repository
public class OrderManagerImpl implements OrderManager {
    // 存Redis中订单甘特图的key
    private static final String ORDER_GANTT_CHART_KEY = "ORDER_GANTT_CHART";
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private OrderPORepository orderRepository;

    @Autowired
    private OrderItemPORepository orderItemRepository;

    @Autowired
    private OrderOperationHistoryPORepository orderOperationHistoryRepository;

    @Autowired
    private OrderInfraConverter orderInfraConverter;

    @PostConstruct
    public void init() {
        // 为了让jackson支持 LocalDateTime 类型，注册时间模块
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createOrder(Order order) {
        // 保存订单信息
        OrderPO orderPO = orderInfraConverter.toOrderPO(order);
        orderRepository.save(orderPO);
        order.setId(orderPO.getId());

        log.info("方法：createOrder订单创建成功，订单id：{}", orderPO.getId());

        // 保存订单操作历史
        OrderOperationHistoryPO orderOperationHistoryPO = new OrderOperationHistoryPO();
        orderOperationHistoryPO.setOrderId(orderPO.getId());
        orderOperationHistoryPO.setOperationType(OrderOperationTypeEnum.CREATE_ORDER);
        orderOperationHistoryPO.setChangeDetail("ID为：%s的用户创建了订单，订单信息：%s".formatted(
                StpUtil.getLoginIdAsString(), orderPO.toString()
        ));
        orderOperationHistoryRepository.save(orderOperationHistoryPO);
    }

    @Override
    public Order getOrderById(Long orderId) {
        OrderPO orderPO = orderRepository.getById(orderId);
        return orderInfraConverter.toOrder(orderPO);
    }

    @Override
    public Order getOrderByNo(String orderNo) {
        OrderPO orderPO = orderRepository.lambdaQuery().eq(OrderPO::getOrderNo, orderNo).one();
        return orderInfraConverter.toOrder(orderPO);
    }

    @Override
    public PageResult<Order> getOrderPage(GetOrderPageReq getOrderPageReq) {
        Page<OrderPO> orderPOPage = orderRepository.lambdaQueryPlus()
                .eq(ObjectUtils.isNotNull(getOrderPageReq.getOrderStatus()), OrderPO::getOrderStatus,
                        getOrderPageReq.getOrderStatus())
                .like(StringUtils.isNotEmpty(getOrderPageReq.getOrderNo()), OrderPO::getOrderNo,
                        getOrderPageReq.getOrderNo())
                .like(StringUtils.isNotEmpty(getOrderPageReq.getOrderInfo()), OrderPO::getOrderInfo,
                        getOrderPageReq.getOrderInfo())
                .orderByDesc(OrderPO::getId)
                .bindPage(getOrderPageReq.toPage(), OrderPO::getOrderItems);
        return PageResult.of(orderPOPage, orderInfraConverter::toOrderList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderById(Order order) {
        // 更新订单信息
        orderRepository.updateById(orderInfraConverter.toOrderPO(order));
        // 保存订单操作历史
        OrderOperationHistoryPO orderOperationHistoryPO = new OrderOperationHistoryPO();
        orderOperationHistoryPO.setOrderId(order.getId());
        orderOperationHistoryPO.setOperationType(OrderOperationTypeEnum.MODIFY_ORDER);
        orderOperationHistoryPO.setChangeDetail("ID为：%s的用户更新了订单信息，订单信息：%s".formatted(
                StpUtil.getLoginIdAsString(), order.toString()
        ));
        orderOperationHistoryRepository.save(orderOperationHistoryPO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrderById(Long orderId) {
        orderRepository.removeById(orderId);
        // 保存订单操作历史
        OrderOperationHistoryPO orderOperationHistoryPO = new OrderOperationHistoryPO();
        orderOperationHistoryPO.setOrderId(orderId);
        orderOperationHistoryPO.setOperationType(OrderOperationTypeEnum.CANCEL_ORDER);
        orderOperationHistoryPO.setChangeDetail("ID为：%s的用户删除了订单，订单ID：%s".formatted(
                StpUtil.getLoginIdAsString(), orderId
        ));
        orderOperationHistoryRepository.save(orderOperationHistoryPO);
    }

    @Override
    public PageResult<OrderOperationHistory> getOperationHistoryPage(GetOrderOperationHistoryPageReq req) {
        Page<OrderOperationHistoryPO> orderOperationHistoryPOPage = orderOperationHistoryRepository.lambdaQueryPlus()
                .eq(ObjectUtils.isNotNull(req.getOrderId()), OrderOperationHistoryPO::getOrderId, req.getOrderId())
                .eq(ObjectUtils.isNotNull(req.getOperationType()), OrderOperationHistoryPO::getOperationType,
                        req.getOperationType())
                .orderByDesc(OrderOperationHistoryPO::getId)
                .bindPage(req.toPage(), OrderOperationHistoryPO::getOrder);
        return PageResult.of(orderOperationHistoryPOPage, orderInfraConverter::toOperationHistoryList);
    }

    @Override
    public List<Long> getAllPendingOrderIds() {
        return orderRepository.list(Wrappers.lambdaQuery(OrderPO.class)
                .select(OrderPO::getId)
                .eq(OrderPO::getOrderStatus, OrderStatusEnum.PENDING)
        ).stream().map(OrderPO::getId).toList();
    }

    @Override
    public List<Long> getOrderByStatus(OrderStatusEnum orderStatus) {
        return orderRepository.list(Wrappers.lambdaQuery(OrderPO.class)
                .select(OrderPO::getId)
                .eq(OrderPO::getOrderStatus, orderStatus)
        ).stream().map(OrderPO::getId).toList();
    }

    @Override
    public List<Long> getProductIdsByOrderId(Long orderId) {
        return orderItemRepository.list(Wrappers.lambdaQuery(OrderItemPO.class)
                .select(OrderItemPO::getProductId)
                .eq(OrderItemPO::getOrderId,orderId)
        ).stream().map(OrderItemPO::getProductId).toList();
    }

    @Override
    public Order getOrderDetailById(Long orderId) {
        OrderPO orderPO = orderRepository.lambdaQueryPlus().eq(OrderPO::getId, orderId)
                .bindOne(OrderPO::getOrderItems);
        return orderInfraConverter.toOrder(orderPO);
    }

    @Override
    public void addOrderGanttChart(Long missionId, LocalDateTime startTime, Map<Long, Double> orderTimes) {
        // 甘特图列表
        ArrayList<OrderGanttChart> ganttCharts = new ArrayList<>();
        // 开始时间添加一个扰动，使其看起来更有随机性
        Double disturb = Math.random() * 30 + 30;
        int index = 0;
        for (Long orderId : orderTimes.keySet()) {
            OrderGanttChart orderGanttChart = new OrderGanttChart();
            orderGanttChart.setOrderId(orderId);
            orderGanttChart.setStartTime(startTime.plusMinutes(index * disturb.longValue()));
            orderGanttChart.setEndTime(startTime.plusSeconds(orderTimes.get(orderId).longValue()));
            ganttCharts.add(orderGanttChart);
            index++;
        }
        // 将甘特图列表保存到Redis中
        try {
            String json = objectMapper.writeValueAsString(ganttCharts);
            RedisUtil.HashOps.hPut(ORDER_GANTT_CHART_KEY, String.valueOf(missionId), json);
        } catch (JsonProcessingException e) {
            log.error("OrderManagerImpl.addOrderGanttChart中,订单甘特图数据序列化失败，missionId:{}", missionId, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<OrderGanttChart> getOrderGanttChart(Long missionId) {
        Object ganttChart = RedisUtil.HashOps.hGet(ORDER_GANTT_CHART_KEY, String.valueOf(missionId));
        // 若Redis中没有订单甘特图数据，则返回空列表
        if (ganttChart == null) {
            return Collections.emptyList();
        }
        // 反序列化
        try {
            return objectMapper.readValue(ganttChart.toString(),
                    objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, OrderGanttChart.class));
        } catch (JsonProcessingException e) {
            log.error("OrderManagerImpl.getOrderGanttChart中,订单甘特图数据反序列化失败, missionId:{}", missionId, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrderItems(List<OrderItem> orderItems) {
        orderItemRepository.saveBatch(orderInfraConverter.toOrderItemPOList(orderItems));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOrderItemByOrderId(Long orderId) {
        orderItemRepository.remove(Wrappers.lambdaQuery(OrderItemPO.class).eq(OrderItemPO::getOrderId, orderId));
    }

    @Override
    public Map<Long, Order> getOrderMapByIds(List<Long> orderIds) {
        if (CollectionUtils.isEmpty(orderIds)) {
            return Collections.emptyMap();
        }
        List<OrderPO> orderPOS = orderRepository.listByIds(orderIds);
        return orderPOS.stream().collect(
                Collectors.toMap(OrderPO::getId, orderInfraConverter::toOrder, (o1, o2) -> o1)
        );
    }

    @Override
    public List<OrderItem> getOrderItemsByProductId(Long productId) {
        List<OrderItemPO> orderItemPOs = orderItemRepository.lambdaQuery()
                .eq(ObjectUtils.isNotNull(productId), OrderItemPO::getProductId, productId)
                .list();
        return orderInfraConverter.toOrderItemList(orderItemPOs);
    }

    @Override
    public Integer getTotalNnmByOrderIdAndProductId(Long orderId, Long productId) {
        if (orderId == null || productId == null) {
            return null;
        }

        OrderPO orderPO = orderRepository.lambdaQueryPlus()
                .eq(OrderPO::getId, orderId)
                .bindOne(OrderPO::getOrderItems);

        if (orderPO == null) {
            return null;
        }

        List<OrderItemPO> orderItems = orderPO.getOrderItems();
        if (CollectionUtils.isEmpty(orderItems)) {
            return null;
        }

        for (OrderItemPO orderItemPO : orderItems) {
            if (orderItemPO != null && productId.equals(orderItemPO.getProductId())) {
                return orderItemPO.getCount();
            }
        }

        return null;
    }

    @Override
    public List<Order> getOrderByIds(List<Long> orderIdList) {
        if(CollectionUtils.isEmpty(orderIdList)){
            return Collections.emptyList();
        }

        return orderRepository.lambdaQueryPlus()
                .in(OrderPO::getId, orderIdList)
                .bindList(OrderPO::getOrderItems)
                .stream()
                .map(orderInfraConverter::toOrder)
                .collect(Collectors.toList());
    }


    @Override
    public List<Long> getAllOrderId() {
        return orderRepository.lambdaQuery()
                .list()
                .stream()
                .map(OrderPO::getId)
                .toList();
    }

    @Override
    public List<Order> getOrderStatus() {
        return orderInfraConverter.toOrderList(orderRepository.lambdaQuery()
                .list()
                .stream()
                .filter(orderPO -> orderPO.getOrderStatus() != OrderStatusEnum.FINISHED)
                .toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderStatus(List<Long> orderIdList, OrderStatusEnum orderStatus) {
        // 批量更新订单状态
        if (CollectionUtils.isEmpty(orderIdList) || orderStatus == null) {
            return;
        }

        // 构建需要更新的订单PO列表
        List<OrderPO> orderPOList = orderIdList.stream()
                .filter(Objects::nonNull)
                .map(orderId -> {
                    OrderPO orderPO = new OrderPO();
                    orderPO.setId(orderId);
                    orderPO.setOrderStatus(orderStatus);
                    orderPO.setUpdateTime(LocalDateTime.now());
                    return orderPO;
                })
                .toList();

        // 批量更新订单状态
        orderRepository.updateBatchById(orderPOList);
    }

    @Override
    public void updateOrderStatusBatch(List<Long> orderIdList, OrderStatusEnum orderStatus) {
        // 参数校验
        if (CollectionUtils.isEmpty(orderIdList) || orderStatus == null) {
            log.warn("更新订单状态参数为空，orderIdList: {}, orderStatus: {}", orderIdList, orderStatus);
            return;
        }

        // 过滤掉空的订单ID
        List<Long> validOrderIds = orderIdList.stream()
                .filter(Objects::nonNull)
                .toList();

        if (validOrderIds.isEmpty()) {
            log.warn("有效的订单ID列表为空");
            return;
        }

        // 构建需要更新的订单PO列表
        List<OrderPO> orderPOList = validOrderIds.stream()
                .map(orderId -> {
                    OrderPO orderPO = new OrderPO();
                    orderPO.setId(orderId);
                    orderPO.setOrderStatus(orderStatus);
                    orderPO.setUpdateTime(LocalDateTime.now());
                    return orderPO;
                })
                .toList();

        // 批量更新订单状态
        boolean success = orderRepository.updateBatchById(orderPOList);

        if (success) {
            log.info("批量更新订单状态成功，订单数量: {}，目标状态: {}", validOrderIds.size(), orderStatus);
        } else {
            log.error("批量更新订单状态失败，订单IDs: {}", validOrderIds);
            throw new RuntimeException("批量更新订单状态失败");
        }
    }

    @Override
    public void updateOrderExpectedDeliveryTimeBatch(Map<Long, LocalDateTime> orderFinishTime) {
        // 参数校验
        if (CollectionUtils.isEmpty(orderFinishTime)) {
            log.warn("更新订单预期交付时间参数为空");
            return;
        }

        // 过滤掉空的订单ID和时间
        Map<Long, LocalDateTime> validOrderFinishTime = orderFinishTime.entrySet().stream()
                .filter(entry -> entry.getKey() != null && entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (validOrderFinishTime.isEmpty()) {
            log.warn("有效的订单ID和交付时间映射为空：排产算法结果有问题");
            return;
        }

        // 构建需要更新的订单PO列表
        List<OrderPO> orderPOList = validOrderFinishTime.entrySet().stream()
                .map(entry -> {
                    OrderPO orderPO = new OrderPO();
                    orderPO.setId(entry.getKey());
                    orderPO.setExpectedDeliveryTime(entry.getValue());
                    return orderPO;
                })
                .toList();

        // 批量更新订单预期交付时间
        boolean success = orderRepository.updateBatchById(orderPOList);

        if (success) {
            log.info("批量更新订单预期交付时间成功，订单数量: {}", validOrderFinishTime.size());
        } else {
            log.error("批量更新订单预期交付时间失败，订单IDs: {}", validOrderFinishTime.keySet());
            throw new RuntimeException("批量更新订单预期交付时间失败");
        }
    }

    @Override
    public PageResult<Order> getOrderByStatusPage(GetOrderByStatusReq req) {
        Page<OrderPO> orderPOPage = orderRepository.lambdaQueryPlus()
                .eq(ObjectUtils.isNotNull(req.getOrderStatus()), OrderPO::getOrderStatus
                        , req.getOrderStatus())
                .orderByDesc(OrderPO::getUpdateTime)
                .bindPage(req.toPage(), OrderPO::getOrderItems);
        return PageResult.of(orderPOPage, orderInfraConverter::toOrderList);
    }

    @Override
    public void deleteOrderByIds(List<Long> list) {
        for(Long orderId:list){
            orderRepository.removeById(orderId);
            orderItemRepository.remove(
                    Wrappers.lambdaQuery(OrderItemPO.class)
                            .eq(OrderItemPO::getOrderId, orderId)
            );
        }
    }


}
