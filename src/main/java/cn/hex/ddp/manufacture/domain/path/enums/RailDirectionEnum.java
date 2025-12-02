package cn.hex.ddp.manufacture.domain.path.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 轨道方向类型枚举
 *
 * @author Huhaisen
 * @date 2024/04/28
 */
@Getter
@AllArgsConstructor
public enum RailDirectionEnum {

    /**
     * 从上到下
     */
    TOP_TO_BOTTOM(0),

    /**
     * 从左到右
     */
    LEFT_TO_RIGHT(1),

    /**
     * 从下到上
     */
    BOTTOM_TO_TOP(2),

    /**
     * 从右到左
     */
    RIGHT_TO_LEFT(3);

    @EnumValue
    private final int code;

}
