package cn.hex.ddp.manufacture.domain.order.manager;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.order.rest.req.GetOrderByStatusReq;
import cn.hex.ddp.manufacture.api.order.rest.req.GetOrderOperationHistoryPageReq;
import cn.hex.ddp.manufacture.api.order.rest.req.GetOrderPageReq;
import cn.hex.ddp.manufacture.domain.order.enums.OrderStatusEnum;
import cn.hex.ddp.manufacture.domain.order.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 订单管理器接口
 *
 * @author Huhaisen
 * @date 2024/05/29
 */
public interface OrderManager {
    void createOrder(Order order);

    Order getOrderById(Long orderId);

    Order getOrderByNo(String orderNo);

    PageResult<Order> getOrderPage(GetOrderPageReq getOrderPageReq);

    void updateOrderById(Order order);

    void deleteOrderById(Long orderId);

    PageResult<OrderOperationHistory> getOperationHistoryPage(GetOrderOperationHistoryPageReq req);

    /**
     * 获得所有待生产订单ID
     * @return {@link List }<{@link Long }>
     */
    List<Long> getAllPendingOrderIds();

    /**
     * 根据订单状态查找订单
     */
    List<Long> getOrderByStatus(OrderStatusEnum orderStatus);

    /**
     * 根据订单id获取订单中的产品id
     * @return {@link List }<{@link Long }>
     */
    List<Long> getProductIdsByOrderId(Long orderId);

    /**
     * 根据订单ID获得订单详情
     * @param orderId 订单ID
     * @return {@link Order }
     */
    Order getOrderDetailById(Long orderId);

    /**
     * 添加订单甘特图数据（仿真任务产生的数据）
     *
     * @param missionId  仿真任务ID
     * @param startTime  开始时间
     * @param orderTimes 订单甘特图数据
     */
    void addOrderGanttChart(Long missionId, LocalDateTime startTime, Map<Long, Double> orderTimes);

    /**
     * 获得订单甘特图数据
     */
    List<OrderGanttChart> getOrderGanttChart(Long missionId);

    /**
     * 添加多个订单项
     */
    void addOrderItems(List<OrderItem> orderItems);

    /**
     * 根据订单ID删除订单项
     */
    void deleteOrderItemByOrderId(Long orderId);

    /**
     * 根据订单ID列表获得订单Map
     */
    Map<Long, Order> getOrderMapByIds(List<Long> orderIds);

    /**
     * 根据产品ID获得订单项列表
     */
    List<OrderItem> getOrderItemsByProductId(Long productId);


    /**
     * 通过订单id与产品id获取某订单中具体产品型号生产数目
     */
    Integer getTotalNnmByOrderIdAndProductId(Long orderId, Long productId);

    /**
     * 通过订单ID列表获得订单列表
     */
    List<Order> getOrderByIds(List<Long> orderIdList);

    /**
     * 获得所有订单ID
     */
    List<Long> getAllOrderId();

    /**
     * 获取当前未完成的订单
     */
    List<Order> getOrderStatus();

    /**
     * 更新订单状态
     */
    void updateOrderStatus(List<Long> orderIdList, OrderStatusEnum orderStatus);

    /**
     * 批量更新订单状态
     */
    void updateOrderStatusBatch(List<Long> orderIdList,  OrderStatusEnum orderStatus);

    /**
     * 批量更新订单预期完成时间
     */
    void updateOrderExpectedDeliveryTimeBatch(Map<Long, LocalDateTime> orderFinishTime);

    PageResult<Order> getOrderByStatusPage(GetOrderByStatusReq req);

    void deleteOrderByIds(List<Long> list);
}
