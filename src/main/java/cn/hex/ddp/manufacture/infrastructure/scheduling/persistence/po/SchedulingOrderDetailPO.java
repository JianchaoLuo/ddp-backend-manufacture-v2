package cn.hex.ddp.manufacture.infrastructure.scheduling.persistence.po;

import cn.hex.ddp.manufacture.domain.product.enums.ProductionProgressStatusEnum;
import cn.hex.ddp.manufacture.domain.scheduling.enums.PLanType;
import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 排产计划订单详情PO
 * @author 冯泽邦
 * @date 2025/11/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@Table(schema = "ddp_manufacture_service_v2", value = "scheduling_order_detail", comment = "排产计划订单详情")
public class SchedulingOrderDetailPO extends BaseDatabasePO {
    /**
     * 排产计划ID
     */
    private Long schedulingPlanId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 排产产品id
     */
    private Long productionItemId;

    /**
     * 生产数量
     */
    private Integer productionCount;

    /**
     * 订单项状态
     */
    private ProductionProgressStatusEnum productionProgress;

    /**
     * 计划类型
     */
    private PLanType planType;
}
