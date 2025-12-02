package cn.hex.ddp.manufacture.domain.technique.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 砂类型枚举
 *
 * @author Huhaisen
 * @date 2024/05/04
 */
@Getter
@AllArgsConstructor
public enum SandTypeEnum {

    /**
     * 普通砂
     */
    NORMAL_SAND(0),

    /**
     * 树脂砂
     */
    RESIN_SAND(1),
    ;

    @EnumValue
    private final int code;

}
