package cn.hex.ddp.manufacture.domain.order.model;

import cn.hex.ddp.manufacture.domain.order.enums.OrderStatusEnum;
import cn.hex.ddp.manufacture.domain.product.model.ScheduledProduct;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 排产订单类,主要目的配合排产计划补充排产计划信息
 * 如果订单通过算法计算计入排产计划，这张表需要更新
 * todo：排产算法更新后，需要在订单表中更新订单生产情况
 * @Author: FengZebang
 * @Date: 2025/5/27
 */
@Data
@AllArgsConstructor
public class ScheduledOrder {

    /**
     * 订单ID
     * 订单数据库ID，注意算法中使用的是订单这个ID
     */
    private Long orderId;

    /**
     * 预计完成时间
     * 排产算法计算得出的时间，算法结果需要更新的不的部分
     */
    private LocalDateTime expectedCompletionTime;

    /**
     * 订单
     * 关联查询
     */
    private Order order;
}
