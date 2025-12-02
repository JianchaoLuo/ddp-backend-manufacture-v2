package cn.hex.ddp.manufacture.application.user.dto.in;

import cn.hex.ddp.manufacture.domain.user.enums.UserPostEnum;
import cn.hex.ddp.manufacture.domain.user.enums.UserRoleEnum;
import lombok.Data;

@Data
public class UpdateUserDTO {
    /**
     * 用户ID
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 密码
     */
    private String password;

    /**
     * 角色
     */
    private UserRoleEnum role;

    /**
     * 岗位
     */
    private UserPostEnum post;
}
