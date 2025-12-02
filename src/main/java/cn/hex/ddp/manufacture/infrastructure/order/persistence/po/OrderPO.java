package cn.hex.ddp.manufacture.infrastructure.order.persistence.po;

import cn.hex.ddp.manufacture.domain.order.enums.OrderStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.tangzc.autotable.annotation.ColumnComment;
import com.tangzc.autotable.annotation.ColumnDefault;
import com.tangzc.autotable.annotation.ColumnNotNull;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.autotable.annotation.UniqueIndex;
import com.tangzc.mpe.bind.metadata.annotation.BindEntity;
import com.tangzc.mpe.bind.metadata.annotation.JoinCondition;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单 PO
 *
 * @author Huhaisen
 * @date 2024/05/05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@Table(schema = "ddp_manufacture_service_v2", value = "order", comment = "订单表")
public class OrderPO extends BaseDatabasePO {

    /**
     * 销售订单编号
     */
    @Column(length = 63, comment = "销售订单编号", notNull = true, defaultValue = "''")
    @UniqueIndex
    private String orderNo;

    /**
     * 产品数量
     */
    @ColumnComment(value = "产品数量")
    @ColumnNotNull
    @ColumnDefault("0")
    private Integer productCount;


    /**
     * 订单信息(订单描述)
     */
    @Column(length = 512, comment = "订单信息", notNull = true, defaultValue = "''")
    private String orderInfo;

    /**
     * 订单状态
     */
    @ColumnComment(value = "订单状态")
    @ColumnNotNull
    @ColumnDefault("4")
    private OrderStatusEnum orderStatus;

    /**
     * 预计投产时间
     */
    @ColumnComment(value = "预计投产时间")
    //@ColumnNotNull
    //@ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime expectedProductionTime;

    /**
     * 预计交付时间
     */
    @ColumnComment(value = "预计交付时间")
    //@ColumnNotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime expectedDeliveryTime;

    /**
     * 交付截止时间
     */
    @ColumnComment(value = "交付截止时间")
    @ColumnNotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime deliveryDeadline;

    /**
     * 订单优先级
     */
    @ColumnComment(value = "订单优先级")
    @ColumnNotNull
    @ColumnDefault("0")
    private Integer priority;


    /**
     * 订单项列表，连表查询
     */
    @BindEntity(conditions = @JoinCondition(selfField = OrderPODefine.id, joinField = OrderItemPODefine.orderId))
    private List<OrderItemPO> orderItems;
}
