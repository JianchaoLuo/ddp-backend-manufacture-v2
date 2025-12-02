package cn.hex.ddp.manufacture.api.user.rest.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.user.rest.converter.UserApiConverter;
import cn.hex.ddp.manufacture.api.user.rest.req.*;
import cn.hex.ddp.manufacture.api.user.rest.vo.UserVO;
import cn.hex.ddp.manufacture.application.user.dto.out.UserDTO;
import cn.hex.ddp.manufacture.application.user.service.UserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户相关
 *
 * @author Huhaisen
 * @date 2024/05/06
 */
@Validated
@RestController
@RequestMapping("/api/v2/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserApiConverter userCvt;

    /**
     * 用户注册
     *
     * @param registerReq
     */
    @PostMapping("/register")
    public void register(@RequestBody @Valid RegisterReq registerReq) {
        userService.register(userCvt.toRegisterDto(registerReq));
    }

    /**
     * 手机号密码登录
     *
     * @param loginByPasswordReq
     * @return
     */
    @PostMapping("/login/password")
    public String loginByPassword(@RequestBody @Valid LoginByPasswordReq loginByPasswordReq) {
        UserDTO userDto = userService.loginByPassword(loginByPasswordReq.getPhone(), loginByPasswordReq.getPassword());

        // 登录成功后，基于sa-token保持用户的登录状态，并返回token值
        StpUtil.login(userDto.getId());
        return StpUtil.getTokenValue();
    }

    /**
     * 开发者登录，填写手机号一键登录
     *
     * @return
     */
    @PostMapping("/login/dev")
    public String developerLogin(@RequestBody DeveloperLoginReq developerLoginReq) {
        UserDTO userDto = userService.developerLogin(developerLoginReq.getPhone());

        // 登录成功后，基于sa-token保持用户的登录状态，并返回token值
        StpUtil.login(userDto.getId());
        return StpUtil.getTokenValue();
    }

    /**
     * 获取个人信息
     *
     * @return
     */
    @GetMapping("/own")
    public UserVO getOwnInfo() {
        UserDTO userDto = userService.getUserById(StpUtil.getLoginIdAsLong());
        return userCvt.toUserVo(userDto);
    }

    /**
     * 获取指定用户信息
     *
     * @param userId
     * @return
     */
    @GetMapping("/{userId}")
    public UserVO getUserInfo(@PathVariable("userId") @Valid @NotNull(message = "用户ID不能为空") Long userId) {
        UserDTO userDto = userService.getUserById(userId);
        return userCvt.toUserVo(userDto);
    }

    /**
     * 获取用户列表
     *
     * @param getUserListReq
     * @return
     */
    @GetMapping()
    public PageResult<UserVO> getUserInfoPage(@Valid GetUserListReq getUserListReq) {
        Page<UserDTO> userDtoPage = userService.getUserInfoPage(userCvt.toGetUserListDto(getUserListReq));
        Page<UserVO> userVoPage = userCvt.toUserVoPage(userDtoPage);
        return PageResult.of(userVoPage);
    }

    /**
     * 更新用户信息
     *
     * @return
     */
    @PutMapping("/{id}")
    public void updateUser(@PathVariable("id") @Valid @NotNull(message = "用户id不能为空") Long id, @RequestBody @Valid UpdateUserReq updateUserReq) {
        userService.updateUser(userCvt.toUpdateUserDto(id, updateUserReq));
    }


    /**
     * 删除用户
     *
     * @return
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
    }

}
