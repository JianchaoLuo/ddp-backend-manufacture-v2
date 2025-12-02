package cn.hex.ddp.manufacture.application.user.service;

import cn.hex.ddp.manufacture.application.user.dto.in.GetUserListDTO;
import cn.hex.ddp.manufacture.application.user.dto.in.RegisterDTO;
import cn.hex.ddp.manufacture.application.user.dto.in.UpdateUserDTO;
import cn.hex.ddp.manufacture.application.user.dto.out.UserDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 用户服务接口
 *
 * @author Huhaisen
 * @date 2024/05/06
 */
public interface UserService {

    /**
     * 用户注册
     *
     * @param registerDto 注册参数
     */
    void register(RegisterDTO registerDto);

    /**
     * 手机号密码登录
     * 返回用户token
     *
     * @param phone 手机号
     * @param password 密码
     * @return {@link String}
     */
    UserDTO loginByPassword(String phone, String password);

    /**
     * 根据id获取用户信息
     *
     * @param id 用户ID
     * @return {@link UserDTO}
     */
    UserDTO getUserById(Long id);

    /**
     * 分页获取用户列表
     *
     * @param getUserListDto 请求参数
     * @return {@link Page}<{@link UserDTO}>
     */
    Page<UserDTO> getUserInfoPage(GetUserListDTO getUserListDto);

    UserDTO developerLogin(String phone);


    void updateUser(UpdateUserDTO updateUserDto);

    void deleteUserById(Long id);
}
