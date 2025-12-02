package cn.hex.ddp.manufacture.application.order.service;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.order.rest.req.*;
import cn.hex.ddp.manufacture.api.order.rest.vo.OrderSummaryVO;
import cn.hex.ddp.manufacture.application.order.dto.out.OrderDTO;
import cn.hex.ddp.manufacture.domain.order.model.OrderGanttChart;
import cn.hex.ddp.manufacture.domain.order.model.OrderOperationHistory;
import cn.hex.ddp.manufacture.domain.product.model.Product;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 订单服务接口
 *
 * @author Huhaisen
 * @date 2024/05/29
 */
public interface OrderService {
    void createOrder(CreateOrderReq createOrderReq);

    PageResult<OrderSummaryVO> getOrderPage(GetOrderPageReq getOrderPageReq);

    void updateOrder(Long orderId, UpdateOrderReq updateOrderReq);

    void deleteOrder(Long orderId);

    PageResult<OrderOperationHistory> getOperationHistoryPage(GetOrderOperationHistoryPageReq req);

    /**
     * 根据订单ID获取订单详情
     * @param orderId 订单ID
     * @return {@link OrderDTO }
     */
    OrderDTO getOrderDetailById(Long orderId);

    /**
     * 获得所有待生产订单ID
     * @return {@link List }<{@link Long }>
     */
    List<Long> getAllPendingOrderIds();

    /**
     * 获取订单甘特图数据
     * @param missionId 仿真任务ID
     */
    List<OrderGanttChart> getOrderGanttChart(Long missionId);

    /**
     * 根据订单id查询订单中所有产品的信息
     * @param orderId 订单id
     *
     */
    List<Product> getProductsByOrderId(Long orderId);

    /**
     * 根据taskId查询订单mission_id
     */
    Long getmissionIdBytaskId(Long taskId);

    /**
     * 根据订单状态查询订单
     */
    PageResult<OrderSummaryVO> getOrderByStatus(@Valid GetOrderByStatusReq req);
}
