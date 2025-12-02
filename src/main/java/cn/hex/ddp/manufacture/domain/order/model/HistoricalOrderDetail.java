package cn.hex.ddp.manufacture.domain.order.model;

import cn.hex.ddp.manufacture.domain.order.enums.OrderStatusEnum;
import cn.hex.ddp.manufacture.domain.product.model.HistoricalProductDetail;
import lombok.Data;

import java.util.List;

/**
 * 历史排产订单生产信息
 *
 * @Author: FengZebang
 * @Date: 2025/7/2
 */
@Data
public class HistoricalOrderDetail {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 历史排产计划ID
     */
    private Long ProductionScheduleHistoryId;

    /**
     * 订单Id
     */
    private Long orderId;

    /**
     * 当日订单状态
     */
    private OrderStatusEnum orderStatus;

    /**
     * 当日生产产品列表
     */
    private List<HistoricalProductDetail> historicalProductDetailList;

}
