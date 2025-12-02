package cn.hex.ddp.manufacture.domain.technique.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 砂线类型枚举
 *
 * @author Huhaisen
 * @date 2024/05/04
 */
@Getter
@AllArgsConstructor
public enum SandLineTypeEnum {

    /**
     * 冷砂线
     */
    COLD_LINE(0),

    /**
     * 热砂线
     */
    HOT_LINE(1),
    ;

    @EnumValue
    private final int code;

}
