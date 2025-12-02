package cn.hex.ddp.manufacture.api.user.rest.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 密码登录请求参数
 *
 * @author Huhaisen
 * @date 2024/05/08
 */
@Data
public class LoginByPasswordReq {

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

}
