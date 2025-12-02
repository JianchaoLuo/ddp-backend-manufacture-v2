package cn.hex.ddp.manufacture.api.order.rest.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.order.rest.req.*;
import cn.hex.ddp.manufacture.api.order.rest.vo.OrderSummaryVO;
import cn.hex.ddp.manufacture.application.order.service.OrderService;
import cn.hex.ddp.manufacture.domain.order.model.Order;
import cn.hex.ddp.manufacture.domain.order.model.OrderGanttChart;
import cn.hex.ddp.manufacture.domain.order.model.OrderOperationHistory;
import cn.hex.ddp.manufacture.domain.product.model.Product;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * 订单Controller
 *
 * @author Huhaisen
 * @date 2024/05/29
 */
@Validated
@RestController
@RequestMapping("/api/v2/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单/创建订单
     * @param createOrderReq 订单创建请求参数
     */
    @PostMapping("")
    public void createOrder(@RequestBody @Valid CreateOrderReq createOrderReq) {
        orderService.createOrder(createOrderReq);
    }

    /**
     * 订单/分页查询订单
     * @param getOrderPageReq 分页查询订单请求参数
     * @return {@link PageResult }<{@link Order }>
     */
    @GetMapping("/page")
    public PageResult<OrderSummaryVO> getOrderPage(@Valid GetOrderPageReq getOrderPageReq) {
        return orderService.getOrderPage(getOrderPageReq);
    }

    /**
     * 根据订单id查询订单中所有产品的信息
     * @param orderId 订单id
     */
    @GetMapping("/{orderId}/products")
    public List<Product> pageProductsById(@Valid @PathVariable Long orderId) {
        return orderService.getProductsByOrderId(orderId);
    }

    /**
     * 订单/修改订单信息
     * @param orderId 订单ID
     * @param updateOrderReq 修改订单信息请求参数
     */
    @PutMapping("/{orderId}")
    public void updateOrder(@PathVariable @Valid @NotNull(message = "订单ID不能为空") Long orderId,
                            @RequestBody @Valid UpdateOrderReq updateOrderReq) {
        orderService.updateOrder(orderId, updateOrderReq);
    }

    /**
     * 订单/删除订单
     * @param orderId 订单ID
     */
    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable @Valid @NotNull(message = "订单ID不能为空") Long orderId) {
        orderService.deleteOrder(orderId);
    }

    /**
     * 订单操作历史/分页查询订单操作历史
     * @param req 分页查询订单操作历史请求参数
     * @return {@link PageResult }<{@link OrderOperationHistory }>
     */
    @GetMapping("/history/page")
    public PageResult<OrderOperationHistory> getOperationHistoryPage(@Valid GetOrderOperationHistoryPageReq req) {
        return orderService.getOperationHistoryPage(req);
    }

    /**
     * 订单甘特图/获取订单甘特图数据
     */
    @SaIgnore
    @GetMapping("/gantt-chart/{taskId}")
    public List<OrderGanttChart> getOrderGanttChart(@PathVariable Long taskId) {
        //根据taskId查询对应的missionId
        Long missionId = orderService.getmissionIdBytaskId(taskId);
        if (missionId == null) {
            return Collections.emptyList();
        }
        return orderService.getOrderGanttChart(missionId);
    }

    /**
     * 根据订单状态查询订单
     */
    @GetMapping("/status/page")
    public PageResult<OrderSummaryVO> getOrderByStatus(@Valid GetOrderByStatusReq req){
        return orderService.getOrderByStatus(req);
    }
}
