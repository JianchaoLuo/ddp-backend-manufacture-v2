package cn.hex.ddp.manufacture.application.scheduling.service.impl;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.product.rest.vo.ProductVO;
import cn.hex.ddp.manufacture.api.scheduling.rest.req.*;
import cn.hex.ddp.manufacture.api.scheduling.rest.vo.*;
import cn.hex.ddp.manufacture.application.order.converter.OrderAppConverter;
import cn.hex.ddp.manufacture.application.scheduling.converter.SchedulingAppConverter;
import cn.hex.ddp.manufacture.application.scheduling.service.SchedulingService;
import cn.hex.ddp.manufacture.application.scheduling.util.SchedulingInput;
import cn.hex.ddp.manufacture.application.scheduling.util.SchedulingOutput;
import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.domain.common.exception.NotFoundException;
import cn.hex.ddp.manufacture.domain.order.enums.OrderStatusEnum;
import cn.hex.ddp.manufacture.domain.order.manager.OrderManager;
import cn.hex.ddp.manufacture.domain.order.model.Order;
import cn.hex.ddp.manufacture.domain.order.model.OrderItem;
import cn.hex.ddp.manufacture.domain.product.manager.ProductManager;
import cn.hex.ddp.manufacture.domain.product.model.Product;
import cn.hex.ddp.manufacture.domain.product.model.ProductionItem;
import cn.hex.ddp.manufacture.domain.scheduling.enums.PLanType;
import cn.hex.ddp.manufacture.domain.scheduling.manager.SchedulingManager;
import cn.hex.ddp.manufacture.domain.scheduling.model.SchedulingOrderDetail;
import cn.hex.ddp.manufacture.domain.scheduling.model.SchedulingPlan;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule.ScheduleOrderInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.ProductionScheduling;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.output.SchedulingOutPut;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.output.SchedulingResult;
import cn.hex.ddp.manufacture.infrastructure.scheduling.persistence.po.SchedulingOrderDetailPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 排产服务接口实现类
 *
 * @author 冯泽邦
 * @date 2025/11/25
 */
@Slf4j
@Service
public class SchedulingServiceImpl implements SchedulingService {

    @Autowired
    private SchedulingManager schedulingManager;
    @Autowired
    private SchedulingAppConverter schedulingAppConverter;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private ProductManager productManager;

    @Autowired
    private SchedulingInput schedulingInput;

    @Autowired
    private SchedulingOutput schedulingOutput;

    @Autowired
    private OrderAppConverter orderAppConverter;

    /**
     * 更新排产计划
     */
    @Override
    public SchedulingResultVO updateSchedule(UpdateSchedulingReq updateSchedulingReq) {
        // 参数校验
        if (updateSchedulingReq == null) {
            log.error("参数错误：UpdateSchedulingReq 不能为空");
            throw new BusinessException(BssExType.PARAM_VALIDATE_FAILED);
        }

        List<Long> orderList = updateSchedulingReq.getOrderList();

        // 处理清空排产计划的情况（传入空列表）
        if (CollectionUtils.isEmpty(orderList)) {
            log.info("[SchedulingServiceImpl.updateSchedule] 传入订单列表为空，执行清空排产计划操作");
            return clearSchedulingPlan();
        }

        // 根据订单ID查询订单
        List<Order> orders = orderManager.getOrderByIds(orderList);

        // 校验订单交付截止时间
        List<FailedOrderInfoVO> deadlineExceededOrders = validateDeliveryDeadlines(orders);
        if (!deadlineExceededOrders.isEmpty()) {
            log.warn("[SchedulingServiceImpl.updateSchedule] 存在交付截止时间已过期的订单");
            return SchedulingResultVO.failure("[更新排产计划]失败！：存在交付截止时间已过期的订单", deadlineExceededOrders);
        }

        // 查询当前排产计划涉及到的订单ID（修复：只获取当前排产计划的订单）
        List<Long> currentSchedulingOrderIds = schedulingManager.getSchedulingOrderIds(null);

        // 初始化排产计划主表和排产订单详情表
        List<SchedulingPlan> schedulingPlans = Collections.emptyList();
        List<SchedulingOrderDetail> schedulingOrderDetails = Collections.emptyList();
        List<ProductionItem> productionItems = Collections.emptyList();

        try {
            // 清除并备份排产计划主表
            schedulingPlans = schedulingManager.deleteAndBackupSchedulingPlan(PLanType.SCHEDULE_PLAN);
            // 清除备份排产订单详情记录
            schedulingOrderDetails = schedulingManager.deleteAndBackupSchedulingOrderDetail(PLanType.SCHEDULE_PLAN);
            // 删除原有排产产品实例数据
            productionItems = productManager.deleteAndBackupProductionItemByPlanType(PLanType.SCHEDULE_PLAN);

            // 修改当前排产计划涉及的订单状态为待排产
            if (!CollectionUtils.isEmpty(currentSchedulingOrderIds)) {
                orderManager.updateOrderStatusBatch(currentSchedulingOrderIds, OrderStatusEnum.PENDING_SCHEDULING);
            }

            // 根据订单列表初始化排产产品实例
            productManager.initProductionItemByOrderIds(orders, PLanType.SCHEDULE_PLAN);

            // 算法数据准备
            SimulateInput simulateInput = schedulingInput.buildSimulationInput(orderList);
            ScheduleOrderInput scheduleOrderInput = schedulingInput.buildScheduleOrderInput(orderList, PLanType.SCHEDULE_PLAN);

            // 调用算法
            SchedulingResult schedulingResult = ProductionScheduling.preDoScheduling(simulateInput, scheduleOrderInput);

            // 处理算法结果
            if (schedulingResult.isSuccess()) {
                log.info("[SchedulingServiceImpl.updateSchedule] 排产算法调用成功");

                // 存储排产结果
                SchedulingOutPut successOutput = schedulingResult.getSuccessOutput();
                schedulingOutput.updateOrderExpectedCompletionTime(successOutput.getOrderFinishTime());
                schedulingOutput.updatePlan(successOutput.getProductionPlan(), PLanType.SCHEDULE_PLAN);
                orderManager.updateOrderStatusBatch(orderList, OrderStatusEnum.PENDING_PRODUCTION);

                return SchedulingResultVO.success("[更新排产计划]成功！：排产成功完成，共处理 " + orderList.size() + " 个订单");
            } else {
                log.error("[SchedulingServiceImpl.updateSchedule] 排产算法调用失败");

                rollbackSchedulingChanges(currentSchedulingOrderIds, schedulingPlans, schedulingOrderDetails, productionItems);

                List<SchedulingResult.FailedOrderInfo> failedOrders = schedulingResult.getFailedOrders();
                List<FailedOrderInfoVO> failedOrderInfoVOs = convertToFailedOrderInfoVO(failedOrders);

                return SchedulingResultVO.failure("[更新排产计划]失败！：排产失败", failedOrderInfoVOs);
            }
        } catch (Exception e) {
            log.error("[SchedulingServiceImpl.updateSchedule] 排产过程发生异常，执行回滚操作", e);

            try {
                rollbackSchedulingChanges(currentSchedulingOrderIds, schedulingPlans, schedulingOrderDetails, productionItems);
            } catch (Exception rollbackException) {
                log.error("回滚操作失败", rollbackException);
            }

            throw e;
        }
    }

    private List<FailedOrderInfoVO> validateDeliveryDeadlines(List<Order> orders) {
        List<FailedOrderInfoVO> deadlineExceededOrders = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Order order : orders) {
            if (order.getDeliveryDeadline() != null && order.getDeliveryDeadline().isBefore(now)) {
                OrderVO orderVO = orderAppConverter.newToOrderVO(order);
                FailedOrderInfoVO failedOrderInfoVO = new FailedOrderInfoVO(orderVO, "订单交付截止时间已过期");
                deadlineExceededOrders.add(failedOrderInfoVO);
            }
        }
        return deadlineExceededOrders;
    }

    private List<FailedOrderInfoVO> convertToFailedOrderInfoVO(List<SchedulingResult.FailedOrderInfo> failedOrders) {
        List<FailedOrderInfoVO> failedOrderInfoVOs = new ArrayList<>();
        for (SchedulingResult.FailedOrderInfo failedOrder : failedOrders) {
            Order order = orderManager.getOrderById(failedOrder.getOrderId());
            OrderVO orderVO;
            if (order != null) {
                orderVO = orderAppConverter.newToOrderVO(order);
            } else {
                orderVO = new OrderVO();
                orderVO.setId(failedOrder.getOrderId());
            }
            FailedOrderInfoVO failedOrderInfoVO = new FailedOrderInfoVO(orderVO, failedOrder.getReason());
            failedOrderInfoVOs.add(failedOrderInfoVO);
        }
        return failedOrderInfoVOs;
    }

    /**
     * 清空排产计划操作
     */
    private SchedulingResultVO clearSchedulingPlan() {
        try {
            // 先获取当前排产计划涉及的订单ID，用于恢复订单状态
            List<Long> currentSchedulingOrderIds = schedulingManager.getSchedulingOrderIds(null);

            // 备份当前排产计划数据
            List<SchedulingPlan> schedulingPlans = schedulingManager.deleteAndBackupSchedulingPlan(PLanType.SCHEDULE_PLAN);
            List<SchedulingOrderDetail> schedulingOrderDetails = schedulingManager.deleteAndBackupSchedulingOrderDetail(PLanType.SCHEDULE_PLAN);
            List<ProductionItem> productionItems = productManager.deleteAndBackupProductionItemByPlanType(PLanType.SCHEDULE_PLAN);

            // 恢复订单状态到待生产状态（而不是待排产状态）
            if (!CollectionUtils.isEmpty(currentSchedulingOrderIds)) {
                orderManager.updateOrderStatusBatch(currentSchedulingOrderIds, OrderStatusEnum.PENDING_SCHEDULING);
            }

            log.info("[SchedulingServiceImpl.clearSchedulingPlan] 成功清空排产计划");
            return SchedulingResultVO.success("[清空排产计划]成功！：已清空所有排产计划");

        } catch (Exception e) {
            log.error("[SchedulingServiceImpl.clearSchedulingPlan] 清空排产计划失败", e);
            throw new BusinessException(BssExType.SERVER_BUSY_ERROR, "清空排产计划失败");
        }
    }

    /**
     * 回滚排产变更操作
     */
    private void rollbackSchedulingChanges(
            List<Long> currentSchedulingOrderIds,
            List<SchedulingPlan> backupPlans,
            List<SchedulingOrderDetail> backupOrderDetails,
            List<ProductionItem> backupProductionItems) {

        try {
            // 1. 恢复订单状态
            if (!CollectionUtils.isEmpty(currentSchedulingOrderIds)) {
                orderManager.updateOrderStatusBatch(currentSchedulingOrderIds, OrderStatusEnum.PENDING_PRODUCTION);
            }

            // 2. 恢复排产计划主表数据
            if (!CollectionUtils.isEmpty(backupPlans)) {
                for (SchedulingPlan plan : backupPlans) {
                    schedulingManager.saveSchedulingPlan(plan);
                }
            }

            // 3. 恢复排产订单详情数据
            if (!CollectionUtils.isEmpty(backupOrderDetails)) {
                for (SchedulingOrderDetail detail : backupOrderDetails) {
                    schedulingManager.saveSchedulingOrderDetail(detail);
                }
            }

            // 4. 恢复排产产品实例数据
            if (!CollectionUtils.isEmpty(backupProductionItems)) {
                // 需要在ProductManager中添加批量保存方法
                productManager.saveProductionItemsBatch(backupProductionItems);
            }

            log.info("[SchedulingServiceImpl.rollbackSchedulingChanges] 排产变更回滚成功");
        } catch (Exception rollbackException) {
            log.error("[SchedulingServiceImpl.rollbackSchedulingChanges] 回滚操作失败", rollbackException);
        }
    }

    @Override
    public PageResult<SchedulingSummaryVO> getSchedulingPage(GetSchedulingPageReq req) {
        PageResult<SchedulingPlan> schedulingPage = schedulingManager.getSchedulingPage(req);
        return PageResult.of(schedulingPage, schedulingAppConverter::toSchedulingSummaryVOList);
    }

    @Override
    public PageResult<SchedulingOrderVO> getSchedulingOrdersPage(SchedulingDetailReq req) {
        // 1. 获取所有在排产计划中的订单ID
        List<Long> orderIds = schedulingManager.getSchedulingOrderIds(null);
        if (CollectionUtils.isEmpty(orderIds)) {
            return PageResult.emptyResult();
        }

        // 2. 计算分页参数
        int total = orderIds.size();
        int pageSize = req.getPageSize();
        int pageNum = req.getCurrent();

        // 3. 计算分页起始和结束位置
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, total);

        // 4. 检查分页参数有效性
        if (startIndex >= total) {
            return PageResult.emptyResult();
        }

        // 5. 分页截取订单ID
        List<Long> pagedOrderIds = orderIds.subList(startIndex, endIndex);

        // 6. 批量查询订单详情（包含订单项）
        List<Order> orders = orderManager.getOrderByIds(pagedOrderIds);
        if (CollectionUtils.isEmpty(orders)) {
            return PageResult.emptyResult();
        }

        // 7. 获取所有涉及的产品ID，并批量查询产品信息
        List<Long> productIds = orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .map(OrderItem::getProductId)
                .distinct()
                .toList();
        Map<Long, Product> productMap = productManager.getProductMapByIds(productIds);

        // 8. 转换为VO
        List<SchedulingOrderVO> voList = schedulingAppConverter.toSchedulingOrderVOs(orders, productMap);

        // 9. 返回分页结果
        return PageResult.of(total, pageNum, pageSize, voList);
    }

    @Override
    public SchedulingResultVO makeSchedulingDecision(DecisionReq req) {
        List<OrderInfoReq> newOrders = req.getNewOrders();
        // 参数校验
        // 校验每一订单信息
        for (OrderInfoReq orderInfoReq : newOrders) {
            // 校验订单编号是否重复
            Optional.ofNullable(orderManager.getOrderByNo(orderInfoReq.getOrderNo())).ifPresent(newOrder -> {
                log.warn("[OrderServiceImpl.createOrder] 订单编号已存在，订单编号：{}", newOrder.getOrderNo());
                throw new BusinessException(BssExType.ORDER_NO_REPEAT, newOrder.getOrderNo());
            });
            // 校验产品是否存在
            for (OrderItemReq addOrderItem : orderInfoReq.getOrderItems()) {
                Optional.ofNullable(productManager.getProductById(addOrderItem.getProductId())).orElseThrow(() ->
                        new NotFoundException("产品不存在，产品ID：" + addOrderItem.getProductId())
                );
            }
        }

        // 校验新订单的交付截止时间
        List<Order> newOrderList = new ArrayList<>();
        for (OrderInfoReq orderInfoReq : newOrders) {
            Order newOrder = orderAppConverter.orderInfoReqtoOrder(orderInfoReq);
            newOrderList.add(newOrder);
        }

        List<FailedOrderInfoVO> newOrderDeadlineExceeded = validateDeliveryDeadlines(newOrderList);
        if (!newOrderDeadlineExceeded.isEmpty()) {
            return SchedulingResultVO.failure("[制定排产决策]失败！：存在交付截止时间已过期的新订单", newOrderDeadlineExceeded);
        }

        // 包装并存储新订单（这里订单状态不需要维护，这些订单代码运行完毕后会删除）
        // 记录临时订单
        List<Order> temporaryOrder = new ArrayList<>();
        try {
            for (OrderInfoReq orderInfoReq : newOrders) {
                Order newOrder = orderAppConverter.orderInfoReqtoOrder(orderInfoReq);
                int count = orderInfoReq.getOrderItems().stream().mapToInt(OrderItemReq::getCount).sum();
                newOrder.setProductCount(count);
                // 保存订单
                orderManager.createOrder(newOrder);
                temporaryOrder.add(newOrder);
                // 保存订单项
                List<OrderItem> orderItems = orderInfoReq.getOrderItems().stream().map(
                        item -> orderAppConverter.OrderItemReqtoOrderItem(newOrder.getId(), item)
                ).toList();
                orderManager.addOrderItems(orderItems);
            }

            // 初始化排产计划主表和排产订单详情表
            // 删除排产计划原有排产计划数据（一次操作删除了两张表中的数据）
            schedulingManager.deleteSchedulingPlan(PLanType.DECISION_PLAN);
            // 删除原有排产产品实例数据
            productManager.deleteAndBackupProductionItemByPlanType(PLanType.DECISION_PLAN);
            // 查询现有排产订单id列表
            List<Long> orderIdList = orderManager.getOrderByStatus(OrderStatusEnum.PENDING_PRODUCTION);
            List<Order> orders = orderManager.getOrderByIds(orderIdList);

            // 校验现有订单交付截止时间
            List<FailedOrderInfoVO> deadlineExceededOrders = validateDeliveryDeadlines(orders);
            if (!deadlineExceededOrders.isEmpty()) {
                log.warn("[SchedulingServiceImpl.makeSchedulingDecision] 存在交付截止时间已过期的订单");
                return SchedulingResultVO.failure("[制定排产决策]失败！：存在交付截止时间已过期的现有订单", deadlineExceededOrders);
            }

            // 合并排产订单和临时订单
            orders.addAll(temporaryOrder);

            // 校验合并后所有订单的交付截止时间
            List<FailedOrderInfoVO> allOrdersDeadlineExceeded = validateDeliveryDeadlines(orders);
            if (!allOrdersDeadlineExceeded.isEmpty()) {
                log.warn("[SchedulingServiceImpl.makeSchedulingDecision] 存在交付截止时间已过期的订单");
                return SchedulingResultVO.failure("[制定排产决策]失败！：存在交付截止时间已过期的订单", allOrdersDeadlineExceeded);
            }

            // 根据订单列表初始化排产产品实例
            productManager.initProductionItemByOrderIds(orders, PLanType.DECISION_PLAN);
            // 获取订单id列表
            List<Long> orderIds = orders.stream().map(Order::getId).toList();

            // 数据准备
            SimulateInput simulateInput = schedulingInput.buildSimulationInput(orderIds);
            ScheduleOrderInput scheduleOrderInput = schedulingInput.buildScheduleOrderInput(orderIds, PLanType.DECISION_PLAN);
            // 调用算法
            SchedulingResult schedulingResult = ProductionScheduling.preDoScheduling(simulateInput, scheduleOrderInput);
            // 算法结果处理
            // 决策结果显示成功
            if (schedulingResult.isSuccess()) {
                log.info("[SchedulingServiceImpl.makeSchedulingDecision] 决策成功");
                return SchedulingResultVO.success("排产成功！：决策成功完成，共处理 " + orderIds.size() + " 个订单");
            }
            // 决策结果显示失败
            else {
                log.info("[SchedulingServiceImpl.makeSchedulingDecision] 决策失败");
                return SchedulingResultVO.failure("排产失败！：决策失败", FailedOrderInfoVO.fromList(schedulingResult.getFailedOrders()));
            }
        } finally {
            // 删除临时订单
            if (!temporaryOrder.isEmpty()) {
                orderManager.deleteOrderByIds(temporaryOrder.stream().map(Order::getId).toList());
            }
        }
    }

    @Override
    public PageResult<SchedulingProductVO> getSchedulingProductsPage(Long scheduleId, ProductsDetailReq req) {
        // 校验排产计划ID
        SchedulingPlan schedulingPlan = schedulingManager.getSchedulingPlanById(scheduleId);
        if (schedulingPlan == null) {
            log.warn("[SchedulingServiceImpl.getSchedulingProductsPage] 排产计划不存在，排产计划ID：{}", scheduleId);
            return PageResult.emptyResult();
        }

        //分页查询排产计划id涉及到的排产计划订单详情表
        PageResult<SchedulingOrderDetailPO> page = schedulingManager.getSchedulingOrderDetailPage(scheduleId, req);
        if (page.getList().isEmpty()){
            return PageResult.emptyResult();
        }

        //获取产品信息
        List<Long> productIds = page.getList().stream().map(SchedulingOrderDetailPO::getProductId).toList();
        Map<Long, Product> productMap = productManager.getProductMapByIds( productIds);
        //获取订单信息
        List<Long> orderIds = page.getList().stream().map(SchedulingOrderDetailPO::getOrderId).toList();
        Map<Long, Order> orderMap = orderManager.getOrderMapByIds(orderIds);
        return PageResult.of(
                page,
                schedulingAppConverter.toSchedulingProductVOList(
                        page.getList(),
                        productMap,
                        orderMap
                )
        );
    }

    @Override
    public PageResult<SchedulingOrderVO> getSchedulingOrderPage(Long scheduleId, OrdersDetailReq req) {
        // 校验排产计划ID
        SchedulingPlan schedulingPlan = schedulingManager.getSchedulingPlanById(scheduleId);
        if (schedulingPlan == null) {
            log.warn("[SchedulingServiceImpl.getSchedulingProductsPage] 排产计划不存在，排产计划ID：{}", scheduleId);
            return PageResult.emptyResult();
        }

        //查询排产计划涉及到所有的订单id
        List<Long> orderIds = schedulingManager.getSchedulingOrderIds(scheduleId);
        if (CollectionUtils.isEmpty(orderIds)){
            return PageResult.emptyResult();
        }

        //计算分页参数
        int total = orderIds.size();
        int pageSize = req.getPageSize();
        int pageNum = req.getCurrent();

        //计算分页起始和结束位置
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, total);

        //检查分页参数有效性
        if (startIndex >= total) {
            return PageResult.emptyResult();
        }

        //分页截取订单ID
        List<Long> pagedOrderIds = orderIds.subList(startIndex, endIndex);

        //批量查询订单详情（包含订单项）
        List<Order> orders = orderManager.getOrderByIds(pagedOrderIds);
        if (CollectionUtils.isEmpty(orders)) {
            return PageResult.emptyResult();
        }

        //获取所有涉及的产品ID，并批量查询产品信息
        List<Long> productIds = orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .map(OrderItem::getProductId)
                .distinct()
                .toList();
        Map<Long, Product> productMap = productManager.getProductMapByIds(productIds);

        //转换为VO
        List<SchedulingOrderVO> voList = schedulingAppConverter.toSchedulingOrderVOs(orders, productMap);

        //返回分页结果
        return PageResult.of(total, pageNum, pageSize, voList);
    }
}
