package cn.hex.ddp.manufacture.infrastructure.order.persistence.po;

import cn.hex.ddp.manufacture.domain.order.enums.OrderOperationTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.tangzc.autotable.annotation.ColumnComment;
import com.tangzc.autotable.annotation.ColumnDefault;
import com.tangzc.autotable.annotation.ColumnNotNull;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.bind.metadata.annotation.BindEntity;
import com.tangzc.mpe.bind.metadata.annotation.JoinCondition;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订单操作历史 PO
 *
 * @author Huhaisen
 * @date 2024/05/05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@Table(schema = "ddp_manufacture_service_v2", value = "order_operation_history", comment = "订单操作历史")
public class OrderOperationHistoryPO extends BaseDatabasePO {

    /**
     * 订单ID
     */
    @ColumnComment(value = "订单ID")
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private Long orderId;

    /**
     * 订单操作类型
     */
    @ColumnComment(value = "订单操作类型")
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private OrderOperationTypeEnum operationType;

    /**
     * 订单变更明细
     */
    @Column(length = 2000, comment = "订单变更明细", notNull = true, defaultValue = "''")
    private String changeDetail;

    /**
     * 订单信息，连表查询
     */
    @BindEntity(conditions = @JoinCondition(selfField = OrderOperationHistoryPODefine.orderId))
    private OrderPO order;

}
