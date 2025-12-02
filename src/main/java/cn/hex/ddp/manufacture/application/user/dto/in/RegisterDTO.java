package cn.hex.ddp.manufacture.application.user.dto.in;

import cn.hex.ddp.manufacture.domain.user.enums.UserPostEnum;
import cn.hex.ddp.manufacture.domain.user.enums.UserRoleEnum;
import lombok.Data;

/**
 * 用户注册DTO
 *
 * @author Huhaisen
 * @date 2024/05/06
 */
@Data
public class RegisterDTO {

    /**
     * 姓名
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 工号
     */
    private String workerNo;

    /**
     * 角色
     */
    private UserRoleEnum role;

    /**
     * 岗位
     */
    private UserPostEnum post;

}
