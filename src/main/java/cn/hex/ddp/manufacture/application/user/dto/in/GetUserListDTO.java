package cn.hex.ddp.manufacture.application.user.dto.in;

import cn.hex.ddp.manufacture.domain.user.enums.UserPostEnum;
import cn.hex.ddp.manufacture.domain.user.enums.UserRoleEnum;
import lombok.Data;

/**
 * 获取用户列表入参
 *
 * @author Huhaisen
 * @date 2024/05/10
 */
@Data
public class GetUserListDTO {

    private Integer current;

    private Integer pageSize;

    /**
     * 姓名
     */
    private String name;

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
