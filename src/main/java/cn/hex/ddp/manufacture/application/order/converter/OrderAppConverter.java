package cn.hex.ddp.manufacture.application.order.converter;

import cn.hex.ddp.manufacture.api.order.rest.req.AddOrderItemReq;
import cn.hex.ddp.manufacture.api.order.rest.req.CreateOrderReq;
import cn.hex.ddp.manufacture.api.order.rest.req.UpdateOrderReq;
import cn.hex.ddp.manufacture.api.order.rest.vo.OrderItemVO;
import cn.hex.ddp.manufacture.api.order.rest.vo.OrderSummaryVO;
import cn.hex.ddp.manufacture.api.scheduling.rest.req.OrderInfoReq;
import cn.hex.ddp.manufacture.api.scheduling.rest.req.OrderItemReq;
import cn.hex.ddp.manufacture.api.scheduling.rest.vo.OrderVO;
import cn.hex.ddp.manufacture.application.order.dto.out.OrderDTO;
import cn.hex.ddp.manufacture.domain.order.model.Order;
import cn.hex.ddp.manufacture.domain.order.model.OrderItem;
import cn.hex.ddp.manufacture.domain.product.model.Product;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 订单应用层转换器接口
 *
 * @author Huhaisen
 * @date 2024/05/29
 */
@Mapper(componentModel = "spring")
public interface OrderAppConverter {
    Order toOrder(CreateOrderReq createOrderReq);

    void updateOrder(@MappingTarget Order order, UpdateOrderReq updateOrderReq);

    //默认初始化已完成数量为0
    @Mapping(target = "completedCount", constant = "0")
    OrderItem toOrderItem(Long orderId, AddOrderItemReq addOrderItemReq);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "completedCount", constant = "0")
    @Mapping(target = "orderId", source = "orderId")
    OrderItem OrderItemReqtoOrderItem(Long orderId , OrderItemReq orderItemReq);

    OrderDTO toOrderDTO(Order order);

    default List<OrderSummaryVO> toOrderSummaryVOs(List<Order> orders, Map<Long, Product> productMap) {
        if (orders == null || orders.isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList<OrderSummaryVO> orderSummaryVOS = new ArrayList<>(orders.size());
        // 转换订单列表
        for (Order order : orders) {
            OrderSummaryVO orderSummaryVO = toOrderSummaryVO(order);
            // 转换订单项列表
            List<OrderItem> orderItems = order.getOrderItems();
            if (orderItems == null || orderItems.isEmpty()) {
                orderSummaryVO.setOrderItems(Collections.emptyList());
            } else {
                ArrayList<OrderItemVO> orderItemVOS = new ArrayList<>(orderItems.size());
                // 拼接订单项中产品信息
                for (OrderItem orderItem : orderItems) {
                    OrderItemVO orderItemVO = toOrderItemVO(orderItem);
                    orderItemVO.setProduct(productMap.get(orderItem.getProductId()));
                    orderItemVOS.add(orderItemVO);
                }
                orderSummaryVO.setOrderItems(orderItemVOS);
            }
            orderSummaryVOS.add(orderSummaryVO);
        }
        return orderSummaryVOS;
    }

    OrderItemVO toOrderItemVO(OrderItem orderItem);

    @Mapping(target = "orderItems", ignore = true)
    OrderSummaryVO toOrderSummaryVO(Order order);

    OrderVO newToOrderVO(Order order);

    Order orderInfoReqtoOrder(OrderInfoReq orderInfoReq);

    List<Order> orderInfoReqsToOrders(List<OrderInfoReq> orderInfoReqs);
}
