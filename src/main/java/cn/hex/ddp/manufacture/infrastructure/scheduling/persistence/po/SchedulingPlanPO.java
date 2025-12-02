package cn.hex.ddp.manufacture.infrastructure.scheduling.persistence.po;

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
import org.checkerframework.common.aliasing.qual.Unique;

import java.time.LocalDateTime;

/**
 * 排产计划主表
 * @author 冯泽邦
 * @date 2025/11/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@Table(schema = "ddp_manufacture_service_v2", value = "scheduling_plan", comment = "排产计划")
public class SchedulingPlanPO extends BaseDatabasePO {
    /**
     * 排产日期
     */
    @ColumnComment(value = "排产日期")
    @ColumnNotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Unique
    private LocalDateTime scheduleDate;

    /**
     * 排产产品总量
     */
    @ColumnComment(value = "排产产品总量")
    @ColumnNotNull
    @ColumnDefault("0")
    private Integer totalProductCount;

    /**
     * 计划类型
     */
    @ColumnComment(value = "计划类型")
    PLanType planType;
}
