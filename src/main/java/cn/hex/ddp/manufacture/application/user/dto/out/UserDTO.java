package cn.hex.ddp.manufacture.application.user.dto.out;

import cn.hex.ddp.manufacture.domain.user.enums.UserPostEnum;
import cn.hex.ddp.manufacture.domain.user.enums.UserRoleEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 返回用户信息Dto
 *
 * @author Huhaisen
 * @date 2024/05/08
 */
@Data
public class UserDTO {

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

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;
}
