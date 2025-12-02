package cn.hex.ddp.manufacture.domain.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户角色枚举
 *
 * @author Huhaisen
 * @date 2024/04/27
 */
@Getter
@AllArgsConstructor
public enum UserRoleEnum {

    /**
     * 普通用户
     */
    USER(0),

    /**
     * 管理员
     */
    ADMIN(1),

    /**
     * 超级管理员
     */
    SUPER_ADMIN(2);

    @EnumValue
    private final int code;

}
