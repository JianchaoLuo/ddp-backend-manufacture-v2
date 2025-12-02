package cn.hex.ddp.manufacture.infrastructure.order.managerimpl.converter;

import cn.hex.ddp.manufacture.domain.order.model.Order;
import cn.hex.ddp.manufacture.domain.order.model.OrderItem;
import cn.hex.ddp.manufacture.domain.order.model.OrderOperationHistory;
import cn.hex.ddp.manufacture.domain.order.model.ScheduledOrder;
import cn.hex.ddp.manufacture.infrastructure.order.persistence.po.OrderItemPO;
import cn.hex.ddp.manufacture.infrastructure.order.persistence.po.OrderOperationHistoryPO;
import cn.hex.ddp.manufacture.infrastructure.order.persistence.po.OrderPO;
import org.mapstruct.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * 订单基础转换器
 *
 * @author Huhaisen
 * @date 2024/05/29
 */
@Mapper(componentModel = "spring")
public interface OrderInfraConverter {
    OrderPO toOrderPO(Order order);

    Order toOrder(OrderPO orderPO);

    OrderItemPO toOrderItemPO(OrderItem orderItem);

    OrderItem toOrderItem(OrderItemPO orderItemPO);

    List<Order> toOrderList(List<OrderPO> orderPOS);

    List<OrderOperationHistory> toOperationHistoryList(List<OrderOperationHistoryPO> orderOperationHistoryPOS);

    List<OrderItem> toOrderItemList(List<OrderItemPO> orderItemPOS);

    Collection<OrderItemPO> toOrderItemPOList(List<OrderItem> orderItems);
}
