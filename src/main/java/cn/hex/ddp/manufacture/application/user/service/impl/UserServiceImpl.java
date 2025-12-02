package cn.hex.ddp.manufacture.application.user.service.impl;

import cn.hex.ddp.manufacture.application.user.converter.UserAppConverter;
import cn.hex.ddp.manufacture.application.user.dto.in.GetUserListDTO;
import cn.hex.ddp.manufacture.application.user.dto.in.RegisterDTO;
import cn.hex.ddp.manufacture.application.user.dto.in.UpdateUserDTO;
import cn.hex.ddp.manufacture.application.user.dto.out.UserDTO;
import cn.hex.ddp.manufacture.application.user.service.UserService;
import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.domain.common.exception.NotFoundException;
import cn.hex.ddp.manufacture.domain.user.manager.UserManager;
import cn.hex.ddp.manufacture.domain.user.model.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 用户服务实现类
 *
 * @author Huhaisen
 * @date 2024/05/06
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserManager userManager;

    @Autowired
    private UserAppConverter userCvt;

    @Override
    public void register(RegisterDTO registerDto) {
        User newUser = userCvt.toUser(registerDto);

        // 校验手机号是否已注册
        Optional.ofNullable(userManager.getUserByPhone(newUser.getPhone()))
                .ifPresent(user -> user.assertCanRegister(newUser));
        // 校验工号是否已注册
        Optional.ofNullable(userManager.getUserByWorkerNo(newUser.getWorkerNo()))
                .ifPresent(user -> user.assertCanRegister(newUser));

        // 加密密码
        newUser.encryptPwd();
        userManager.register(newUser);
    }

    @Override
    public UserDTO loginByPassword(String phone, String password) {
        User user = userManager.getUserByPhone(phone);
        if (user == null) {
            throw new BusinessException(BssExType.USERNAME_OR_PASSWORD_ERROR);
        }
        // 验证密码是否正确
        user.assertPassword(password);
        return userCvt.toUserDto(user);
    }

    @Override
    public UserDTO developerLogin(String phone) {
        User user = userManager.getUserByPhone(phone);
        if (user == null) {
            log.warn("[UserServiceImpl.loginByPassword] 用户不存在, phone：{}", phone);
            throw new NotFoundException("用户不存在");
        }
        return userCvt.toUserDto(user);
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userManager.getUserById(id);
        if (user == null) {
            log.warn("[UserServiceImpl.getUserById] 用户不存在, id：{}", id);
            throw new NotFoundException("用户不存在");
        }
        return userCvt.toUserDto(user);
    }

    @Override
    public Page<UserDTO> getUserInfoPage(GetUserListDTO getUserListDto) {
        Page<User> userPage = userManager.getUserInfoPage(userCvt.toGetUserInfoListParam(getUserListDto));
        return userCvt.toUserDtoPage(userPage);
    }

    @Override
    public void updateUser(UpdateUserDTO updateUserDto) {
        // 获取现有的用户信息
        User existingUser = userManager.getUserById(updateUserDto.getId());

        if (existingUser == null) {
            throw new NotFoundException("用户不存在");
        }

        User updateUser = userCvt.toUpdateUser(updateUserDto);

        if (updateUser.getPhone() != null) {
            // 校验手机号是否已注册
            Optional.ofNullable(userManager.getUserByPhone(updateUser.getPhone()))
                    .ifPresent(user -> {
                        if (!user.getId().equals(existingUser.getId())) {
                            user.assertCanRegister(existingUser);
                        }
                    });
        }

        // 加密密码
        updateUser.encryptPwd();

        // 更新数据库中的用户信息
        userManager.updateById(updateUser);
    }

    @Override
    public void deleteUserById(Long id) {
        User user = userManager.getUserById(id);
        if (user == null) {
            throw new NotFoundException("用户不存在");
        }
        userManager.deleteUserById(id);
    }

}
