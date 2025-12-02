package cn.hex.ddp.manufacture.domain.sandbox.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 砂箱类型枚举
 *
 * @author Huhaisen
 * @date 2024/05/03
 */
@Getter
@AllArgsConstructor
public enum SandboxTypeEnum {

    /**
     * 冷砂
     */
    COLD_SAND(0),

    /**
     * 热砂
     */
    HOT_SAND(1),

    /**
     * 冷热都可
     */
    COLD_OR_HOT(2),
    ;

    @EnumValue
    private final int code;

}
