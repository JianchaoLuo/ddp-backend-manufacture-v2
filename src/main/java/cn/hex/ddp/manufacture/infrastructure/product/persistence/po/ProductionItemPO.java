package cn.hex.ddp.manufacture.infrastructure.product.persistence.po;

import cn.hex.ddp.manufacture.domain.scheduling.enums.PLanType;
import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.tangzc.autotable.annotation.ColumnComment;
import com.tangzc.autotable.annotation.ColumnDefault;
import com.tangzc.autotable.annotation.ColumnNotNull;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 排产产品实例po
 * @author 冯泽邦
 * @date 2025/11/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@Table(schema = "ddp_manufacture_service_v2", value = "production_item", comment = "排产产品实例")
public class ProductionItemPO extends BaseDatabasePO {
    /**
     * 产品id
     */
    @ColumnComment("产品id")
    @ColumnNotNull
    private Long productId;

    /**
     * 订单id
     */
    @ColumnComment("订单id")
    @ColumnNotNull
    private Long orderId;

    /**
     * 生产数量
     */
    @ColumnComment("生产数量")
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private Integer productionCount;

    /**
     * 计划类型
     */
    @ColumnComment("计划类型")
    @ColumnNotNull
    PLanType planType;
}
