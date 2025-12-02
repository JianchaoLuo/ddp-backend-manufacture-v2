package cn.hex.ddp.manufacture.application.scheduling.converter;

import cn.hex.ddp.manufacture.api.order.rest.vo.OrderItemVO;
import cn.hex.ddp.manufacture.api.scheduling.rest.vo.SchedulingOrderVO;
import cn.hex.ddp.manufacture.api.scheduling.rest.vo.SchedulingProductVO;
import cn.hex.ddp.manufacture.api.scheduling.rest.vo.SchedulingSummaryVO;
import cn.hex.ddp.manufacture.domain.order.model.Order;
import cn.hex.ddp.manufacture.domain.product.model.Product;
import cn.hex.ddp.manufacture.domain.scheduling.model.SchedulingPlan;
import cn.hex.ddp.manufacture.infrastructure.scheduling.persistence.po.SchedulingOrderDetailPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 排产应用转换器接口
 * @author 冯泽邦
 * @date 2025/11/25
 */
@Mapper(componentModel = "spring")
public interface SchedulingAppConverter {
    @Mapping(target = "orderItems", ignore = true)
    SchedulingOrderVO toSchedulingOrderVO(Order order);

    /**
     * 批量转换Order到SchedulingOrderVO
     */
    default List<SchedulingOrderVO> toSchedulingOrderVOs(List<Order> orders, Map<Long, Product> productMap) {
        if (orders == null || orders.isEmpty()) {
            return Collections.emptyList();
        }

        List<SchedulingOrderVO> result = new ArrayList<>();
        for (Order order : orders) {
            SchedulingOrderVO vo = toSchedulingOrderVO(order);

            // 转换订单项
            if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                List<OrderItemVO> itemVOs = order.getOrderItems().stream()
                        .map(item -> {
                            OrderItemVO itemVO = new OrderItemVO();
                            itemVO.setId(item.getId());
                            itemVO.setOrderId(item.getOrderId());
                            itemVO.setCount(item.getCount());
                            itemVO.setProduct(productMap.get(item.getProductId()));
                            return itemVO;
                        })
                        .toList();
                vo.setOrderItems(itemVOs);
            }
            result.add(vo);
        }
        return result;
    }

    @Mapping(source = "id", target = "schedulingId")
    @Mapping(source = "scheduleDate", target = "schedulingDate", dateFormat = "yyyy-MM-dd")
    SchedulingSummaryVO toSchedulingSummaryVO(SchedulingPlan plan);

    List<SchedulingSummaryVO> toSchedulingSummaryVOList(List<SchedulingPlan> plans);

    default List<SchedulingProductVO> toSchedulingProductVOList(List<SchedulingOrderDetailPO> list, Map<Long, Product> productMap, Map<Long, Order> orderMap){
        ArrayList<SchedulingProductVO> schedulingProductVOS = new ArrayList<>(list.size());
        for (SchedulingOrderDetailPO schedulingOrderDetailPO : list) {
            SchedulingProductVO schedulingProductVO = new SchedulingProductVO();
            schedulingProductVO.setProduct(productMap.getOrDefault(schedulingOrderDetailPO.getProductId(), null));
            schedulingProductVO.setOrder(orderMap.getOrDefault(schedulingOrderDetailPO.getOrderId(), null));
            schedulingProductVO.setId(schedulingOrderDetailPO.getId());
            schedulingProductVO.setProductionCount(schedulingOrderDetailPO.getProductionCount());
            schedulingProductVOS.add(schedulingProductVO);
        }
        return schedulingProductVOS;
    }
}
