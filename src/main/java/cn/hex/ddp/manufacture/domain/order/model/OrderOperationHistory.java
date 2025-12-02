package cn.hex.ddp.manufacture.domain.order.model;

import cn.hex.ddp.manufacture.domain.order.enums.OrderOperationTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 订单操作历史模型
 *
 * @author Huhaisen
 * @date 2024/05/05
 */
@Data
public class OrderOperationHistory {
    /**
     * 订单操作历史ID(数据库ID)
     */
    private Long id;

    /**
     * 订单
     */
    private Order order;

    /**
     * 订单操作类型
     */
    private OrderOperationTypeEnum operationType;

    /**
     * 订单变更明细
     */
    private String changeDetail;


    /**
     * 创建人员编号
     */
    private Long createBy;

    /**
     * 操作时间
     */
    private LocalDateTime createTime;

}
