package cn.hex.ddp.manufacture.api.user.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import cn.hex.ddp.manufacture.domain.user.enums.UserPostEnum;
import cn.hex.ddp.manufacture.domain.user.enums.UserRoleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取用户列表请求参数
 *
 * @author Huhaisen
 * @date 2024/05/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetUserListReq extends PageReq {

    /**
     * 姓名(模糊查询, 不传不搜索)
     */
    private String name;

    /**
     * 手机号(模糊查询, 不传不搜索)
     */
    private String phone;

    /**
     * 工号(模糊查询, 不传不搜索)
     */
    private String workerNo;

    /**
     * 角色(精确查询, 不传不搜索)
     */
    private UserRoleEnum role;

    /**
     * 岗位(精确查询, 不传不搜索)
     */
    private UserPostEnum post;
}
