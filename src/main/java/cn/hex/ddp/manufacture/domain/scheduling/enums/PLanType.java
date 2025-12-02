package cn.hex.ddp.manufacture.domain.scheduling.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 计划类型
 * 用于区分实是排产计划还是决策
 * @author 冯泽邦
 * @date 2025/11/25
 */
@Getter
@AllArgsConstructor
public enum PLanType {
    /**
     * 排产计划
     */
    SCHEDULE_PLAN(1),
    /**
     * 决策计划
     */
    DECISION_PLAN(2);

    @EnumValue
    private final Integer code;
}
