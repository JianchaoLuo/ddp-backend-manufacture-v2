package cn.hex.ddp.manufacture.api.user.rest.req;

import cn.hex.ddp.manufacture.domain.user.enums.UserPostEnum;
import cn.hex.ddp.manufacture.domain.user.enums.UserRoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 用户注册请求参数
 *
 * @author Huhaisen
 * @date 2024/05/06
 */
@Data
public class RegisterReq {

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    @Length(min = 2, max = 20, message = "姓名长度必须在2-20个字符之间")
    private String name;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Length(min = 11, max = 11, message = "手机号长度必须为11位")
    private String phone;

    /**
     * 工号
     */
    @NotBlank(message = "工号不能为空")
    private String workerNo;

    /**
     * 角色
     */
    @NotNull(message = "角色不能为空")
    private UserRoleEnum role;

    /**
     * 岗位
     */
    private UserPostEnum post;

}
