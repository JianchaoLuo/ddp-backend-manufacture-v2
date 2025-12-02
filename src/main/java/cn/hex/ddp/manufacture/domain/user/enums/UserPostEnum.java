package cn.hex.ddp.manufacture.domain.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户岗位枚举
 *
 * @author Huhaisen
 * @date 2024/04/27
 */
@Getter
@AllArgsConstructor
public enum UserPostEnum {

    // TODO: 待完善具体枚举值

    /**
     * 默认值
     */
    DEFAULT(0),

    /**
     * 造型
     */
    MODELLING(1);

    @EnumValue
    private final int code;
}
