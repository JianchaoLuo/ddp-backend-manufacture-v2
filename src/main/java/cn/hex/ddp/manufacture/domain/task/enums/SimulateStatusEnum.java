package cn.hex.ddp.manufacture.domain.task.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 仿真状态枚举
 *
 * @author Huhaisen
 * @date 2024/09/21
 */
@Getter
@AllArgsConstructor
public enum SimulateStatusEnum {
    /**
     * 未开始
     */
    NOT_STARTED(0),

    /**
     * 进行中
     */
    PROGRESSING(1),

    /**
     * 已完成
     */
    FINISHED(2),

    /**
     * 失败
     */
    FAILED(3),
    ;

    @EnumValue
    private final int code;
}
