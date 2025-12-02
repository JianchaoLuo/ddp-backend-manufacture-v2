package cn.hex.ddp.manufacture.domain.scheduling.model;

import cn.hex.ddp.manufacture.domain.scheduling.enums.PLanType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *  排产计划
 * @author 冯泽邦
 * @date 2025/11/25
 */
@Data
public class SchedulingPlan {
    /**
     * 排产计划ID
     */
    private Long id;

    /**
     * 排产日期
     */
    private LocalDateTime scheduleDate;

    /**
     * 排产产品总量
     */
    private Integer totalProductCount;

    /**
     * 计划类型
     */
    PLanType planType;
}
