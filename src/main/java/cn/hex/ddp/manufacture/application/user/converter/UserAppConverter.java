package cn.hex.ddp.manufacture.application.user.converter;

import cn.hex.ddp.manufacture.application.user.dto.in.GetUserListDTO;
import cn.hex.ddp.manufacture.application.user.dto.in.RegisterDTO;
import cn.hex.ddp.manufacture.application.user.dto.in.UpdateUserDTO;
import cn.hex.ddp.manufacture.application.user.dto.out.UserDTO;
import cn.hex.ddp.manufacture.domain.user.manager.UserManager;
import cn.hex.ddp.manufacture.domain.user.model.User;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;

/**
 * 用户应用层转换器接口
 *
 * @author Huhaisen
 * @date 2024/05/06
 */
@Mapper(componentModel = "spring")
public interface UserAppConverter {
    User toUser(RegisterDTO registerDto);

    UserDTO toUserDto(User user);

    UserManager.GetUserInfoListParam toGetUserInfoListParam(GetUserListDTO getUserListDto);

    Page<UserDTO> toUserDtoPage(Page<User> userPage);

    User toUpdateUser(UpdateUserDTO updateUserDTO);

}
