package cn.hex.ddp.manufacture.infrastructure.order.persistence.po;

import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.tangzc.autotable.annotation.*;
import com.tangzc.autotable.annotation.enums.IndexTypeEnum;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 订单项 PO
 *
 * @author Huhaisen
 * @date 2024/05/05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@TableIndex(name = "order_item_unique", fields = {OrderItemPODefine.orderId, OrderItemPODefine.productId}, type = IndexTypeEnum.UNIQUE)
@Table(schema = "ddp_manufacture_service_v2", value = "order_item", comment = "订单项")
public class OrderItemPO extends BaseDatabasePO {

    /**
     * 订单ID
     */
    @ColumnComment(value = "订单ID")
    @ColumnNotNull
    @ColumnDefault("0")
    @Index
    private Long orderId;

    /**
     * 生产数量
     */
    @ColumnComment(value = "生产数量")
    @ColumnNotNull
    @ColumnDefault("0")
    private Integer count;

    /**
     * 产品ID
     */
    @ColumnComment(value = "产品ID")
    @ColumnNotNull
    @ColumnDefault("0")
    private Long productId;

    /**
     * 已完成生产数量
     */
    @ColumnComment(value = "已完成生产数量")
    @ColumnNotNull
    @ColumnDefault("0")
    private Integer completedCount;
}
