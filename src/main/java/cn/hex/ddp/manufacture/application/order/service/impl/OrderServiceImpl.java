package cn.hex.ddp.manufacture.application.order.service.impl;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.order.rest.req.*;
import cn.hex.ddp.manufacture.api.order.rest.vo.OrderSummaryVO;
import cn.hex.ddp.manufacture.application.order.converter.OrderAppConverter;
import cn.hex.ddp.manufacture.application.order.dto.out.OrderDTO;
import cn.hex.ddp.manufacture.application.order.dto.out.OrderItemDTO;
import cn.hex.ddp.manufacture.application.order.service.OrderService;
import cn.hex.ddp.manufacture.application.product.service.ProductComponentBuilder;
import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.domain.common.exception.NotFoundException;
import cn.hex.ddp.manufacture.domain.order.manager.OrderManager;
import cn.hex.ddp.manufacture.domain.order.model.*;
import cn.hex.ddp.manufacture.domain.product.manager.ProductManager;
import cn.hex.ddp.manufacture.domain.product.model.Product;
import cn.hex.ddp.manufacture.domain.task.manager.TaskManager;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 订单服务实现类
 *
 * @author Huhaisen
 * @date 2024/05/29
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private ProductManager productManager;

    @Autowired
    private OrderAppConverter orderAppConverter;

    @Autowired
    private ProductComponentBuilder productComponentBuilder;

    @Autowired
    private TaskManager taskManager;

    @Override
    public void createOrder(CreateOrderReq createOrderReq) {
//        //校验时间是否合理
//        if (!createOrderReq.getExpectedProductionTime().isBefore(createOrderReq.getDeliveryDeadline())) {
//            log.warn("[OrderServiceImpl.createOrder] 交付截止时间不能早于预计交单时间");
//            throw new BusinessException(BssExType.PARAM_VALIDATE_FAILED, "预计投产时间不能早于预计交单时间");
//        }
        // 校验订单编号是否重复
        Optional.ofNullable(orderManager.getOrderByNo(createOrderReq.getOrderNo())).ifPresent(order -> {
            log.warn("[OrderServiceImpl.createOrder] 订单编号已存在，订单编号：{}", createOrderReq.getOrderNo());
            throw new BusinessException(BssExType.ORDER_NO_REPEAT, createOrderReq.getOrderNo());
        });
        // 校验产品是否存在
        for (AddOrderItemReq addOrderItemReq : createOrderReq.getOrderItems()) {
            Optional.ofNullable(productManager.getProductById(addOrderItemReq.getProductId())).orElseThrow(() ->
                    new NotFoundException("产品不存在，产品ID：" + addOrderItemReq.getProductId())
            );
        }
        // 设置订单产品数量
        Order order = orderAppConverter.toOrder(createOrderReq);
        int count = createOrderReq.getOrderItems().stream().mapToInt(AddOrderItemReq::getCount).sum();
        order.setProductCount(count);
        // 保存订单
        orderManager.createOrder(order);
        // 保存订单项
        List<OrderItem> orderItems = createOrderReq.getOrderItems().stream().map(
                item -> orderAppConverter.toOrderItem(order.getId(), item)
        ).toList();
        orderManager.addOrderItems(orderItems);
    }

    @Override
    public PageResult<OrderSummaryVO> getOrderPage(GetOrderPageReq getOrderPageReq) {
        PageResult<Order> orderPage = orderManager.getOrderPage(getOrderPageReq);
        List<Long> productIds = orderPage.getList().stream().flatMap(order -> order.getOrderItems().stream())
                .map(OrderItem::getProductId).toList();
        Map<Long, Product> productMap = productManager.getProductMapByIds(productIds);
        return PageResult.of(orderPage, orderAppConverter.toOrderSummaryVOs(orderPage.getList(), productMap));
    }

    @Override
    public void updateOrder(Long orderId, UpdateOrderReq updateOrderReq) {
        Order order = orderManager.getOrderById(orderId);
        // 校验订单是否存在
        if (order == null) {
            log.warn("[OrderServiceImpl.updateOrder] 订单不存在，订单ID：{}", orderId);
            throw new BusinessException(BssExType.ORDER_NOT_EXIST);
        }
        // 校验订单编号是否重复
        if (StringUtils.isNotEmpty(updateOrderReq.getOrderNo())) {
            Optional.ofNullable(orderManager.getOrderByNo(updateOrderReq.getOrderNo())).ifPresent(it -> {
                if (!it.getId().equals(orderId)) {
                    log.warn("[OrderServiceImpl.updateOrder] 订单编号已存在，订单编号：{}", updateOrderReq.getOrderNo());
                    throw new BusinessException(BssExType.ORDER_NO_REPEAT, updateOrderReq.getOrderNo());
                }
            });
        }
        // 更新订单并设置订单产品数量
        orderAppConverter.updateOrder(order, updateOrderReq);
        if (CollectionUtils.isNotEmpty(updateOrderReq.getOrderItems())) {
            int count = updateOrderReq.getOrderItems().stream().mapToInt(AddOrderItemReq::getCount).sum();
            order.setProductCount(count);
        }
        orderManager.updateOrderById(order);
        // 若updateOrderReq中订单项列表不为空，则更新订单项
        if (CollectionUtils.isNotEmpty(updateOrderReq.getOrderItems())) {
            // 校验产品是否存在
            for (AddOrderItemReq addOrderItemReq : updateOrderReq.getOrderItems()) {
                Optional.ofNullable(productManager.getProductById(addOrderItemReq.getProductId())).orElseThrow(() ->
                        new NotFoundException("产品不存在，产品ID：" + addOrderItemReq.getProductId())
                );
            }
            // 先删除原来订单项
            orderManager.deleteOrderItemByOrderId(orderId);
            // 再添加新订单项
            List<OrderItem> orderItems = updateOrderReq.getOrderItems().stream().map(
                    item -> orderAppConverter.toOrderItem(orderId, item)
            ).toList();
            orderManager.addOrderItems(orderItems);
        }
    }

    @Override
    public void deleteOrder(Long orderId) {
        // 校验订单是否存在
        Order order = orderManager.getOrderById(orderId);
        if (order == null) {
            log.warn("[OrderServiceImpl.deleteOrder] 订单不存在，订单ID：{}", orderId);
            throw new BusinessException(BssExType.ORDER_NOT_EXIST);
        }
        if(CollectionUtils.isNotEmpty(taskManager.getTaskItemsByOrderId(orderId))){
            log.warn("[OrderServiceImpl.deleteOrder] 订单被占用");
            throw new BusinessException(BssExType.ORDER_BE_OCCUPIED);
        }
        orderManager.deleteOrderById(orderId);
        // 删除订单项
        orderManager.deleteOrderItemByOrderId(orderId);
    }

    @Override
    public PageResult<OrderOperationHistory> getOperationHistoryPage(GetOrderOperationHistoryPageReq req) {
        return orderManager.getOperationHistoryPage(req);
    }

    @Override
    public OrderDTO getOrderDetailById(Long orderId) {
        Order order = orderManager.getOrderDetailById(orderId);
        if (order == null) {
            log.info("[OrderServiceImpl.getOrderDetailById] 订单不存在，订单ID：{}", orderId);
            throw new BusinessException(BssExType.ORDER_NOT_EXIST);
        }
        OrderDTO orderDTO = orderAppConverter.toOrderDTO(order);
        // 获得订单项列表DTO
        List<OrderItemDTO> orderItemDTOS = order.getOrderItems().stream().map(item -> new OrderItemDTO(
                item.getId(),
                item.getOrderId(),
                item.getCount(),
                productComponentBuilder.getProductDTO(item.getProductId())
        )).toList();
        // 填充订单项列表DTO
        orderDTO.setOrderItems(orderItemDTOS);
        return orderDTO;
    }

    @Override
    public List<Long> getAllPendingOrderIds() {
        return orderManager.getAllPendingOrderIds();
    }

    @Override
    public List<OrderGanttChart> getOrderGanttChart(Long missionId) {
        return orderManager.getOrderGanttChart(missionId);
    }

    @Override
    public List<Product> getProductsByOrderId(Long orderId) {
        // 校验订单是否存在
        Order order = orderManager.getOrderById(orderId);
        if (order == null) {
            log.info("[OrderServiceImpl.getProductsByOrderId] 订单不存在，订单ID：{}", orderId);
            throw new BusinessException(BssExType.ORDER_NOT_EXIST);
        }
        List<Long> productIds = orderManager.getProductIdsByOrderId(orderId);
        List<Product> products = productManager.getProductListByIds(productIds);
        return products;
    }

    @Override
    public Long getmissionIdBytaskId(Long taskId) {
        //获取taskId对应task的对象,再返回其对应的missionId
        return taskManager.getTaskById(taskId).getMissionId();
    }

    @Override
    public PageResult<OrderSummaryVO> getOrderByStatus(GetOrderByStatusReq req) {
        PageResult<Order> orderPage = orderManager.getOrderByStatusPage(req);
        List<Long> productIds = orderPage.getList().stream().flatMap(order -> order.getOrderItems().stream())
                .map(OrderItem::getProductId).toList();
        Map<Long, Product> productMap = productManager.getProductMapByIds(productIds);
        return PageResult.of(orderPage, orderAppConverter.toOrderSummaryVOs(orderPage.getList(), productMap));
    }
}
