package cn.hex.ddp.manufacture.domain.mold.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 模具类型枚举
 *
 * @author Huhaisen
 * @date 2024/05/04
 */
@Getter
@AllArgsConstructor
public enum MoldTypeEnum {

    /**
     * 砂箱模具
     */
    SANDBOX_MOLD(0),

    /**
     * 砂芯模具
     */
    SAND_CORE_MOLD(1),
    ;

    @EnumValue
    private final int code;

}
