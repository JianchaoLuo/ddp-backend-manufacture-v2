package cn.hex.ddp.manufacture.api.user.rest.req;

import cn.hex.ddp.manufacture.domain.user.enums.UserPostEnum;
import cn.hex.ddp.manufacture.domain.user.enums.UserRoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 用户更新请求参数
 *
 * 更新用户信息请求参数类
 *
 * @date 2024/05/16
 */
@Data
public class UpdateUserReq {

    /**
     * 姓名
     */
    @Length(min = 2, max = 20, message = "姓名长度必须在2-20个字符之间")
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    @Length(min = 11, max = 11, message = "手机号长度必须为11位")
    private String phone;

    /**
     * 角色
     */
    private UserRoleEnum role;

    /**
     * 岗位
     */
    private UserPostEnum post;
}
