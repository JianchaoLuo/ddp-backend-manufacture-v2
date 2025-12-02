package cn.hex.ddp.manufacture.domain.user.manager;

import cn.hex.ddp.manufacture.domain.user.enums.UserPostEnum;
import cn.hex.ddp.manufacture.domain.user.enums.UserRoleEnum;
import cn.hex.ddp.manufacture.domain.user.model.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * 用户聚合管理接口
 *
 * @author Huhaisen
 * @date 2024/05/06
 */
public interface UserManager {
    /**
     * 用户注册
     *
     * @param user 用户实体
     */
    void register(User user);

    /**
     * 根据手机号获取用户信息
     *
     * @param phone 手机号
     * @return {@link User}
     */
    User getUserByPhone(String phone);

    /**
     * 根据工号获取用户信息
     *
     * @param workerNo 工号
     * @return {@link User}
     */
    User getUserByWorkerNo(String workerNo);

    /**
     * 根据ID获取用户信息
     *
     * @param id ID
     * @return {@link User}
     */
    User getUserById(Long id);

    Page<User> getUserInfoPage(GetUserInfoListParam param);

    void updateById(User user);

    void deleteUserById(Long id);


    @Data
    class GetUserInfoListParam {
        private Integer current;
        private Integer pageSize;
        private String name;
        private String phone;
        private String workerNo;
        private UserRoleEnum role;
        private UserPostEnum post;
    }
}
