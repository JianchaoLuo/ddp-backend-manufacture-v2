package cn.hex.ddp.manufacture.domain.path.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 路径类型枚举
 *
 * @author Huhaisen
 * @date 2024/04/28
 */
@Getter
@AllArgsConstructor
public enum PathTypeEnum {

    /**
     * 仅摆渡车能走
     */
    ONLY_FERRY_CAR(0),

    /**
     * 仅小车能走
     */
    ONLY_SUB_CAR(1),

    /**
     * 行车走的
     */
    TRAVELLING_CRANE(2),
    ;

    @EnumValue
    private final int code;

}
