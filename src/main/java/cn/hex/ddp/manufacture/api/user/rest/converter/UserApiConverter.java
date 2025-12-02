package cn.hex.ddp.manufacture.api.user.rest.converter;

import cn.hex.ddp.manufacture.api.user.rest.req.GetUserListReq;
import cn.hex.ddp.manufacture.api.user.rest.req.RegisterReq;
import cn.hex.ddp.manufacture.api.user.rest.req.UpdateUserReq;
import cn.hex.ddp.manufacture.api.user.rest.vo.UserVO;
import cn.hex.ddp.manufacture.application.user.dto.in.GetUserListDTO;
import cn.hex.ddp.manufacture.application.user.dto.in.RegisterDTO;
import cn.hex.ddp.manufacture.application.user.dto.in.UpdateUserDTO;
import cn.hex.ddp.manufacture.application.user.dto.out.UserDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;

/**
 * 用户API转换器接口
 *
 * @author Huhaisen
 * @date 2024/05/06
 */
@Mapper(componentModel = "spring")
public interface UserApiConverter {
    RegisterDTO toRegisterDto(RegisterReq registerReq);

    UserVO toUserVo(UserDTO userDto);

    GetUserListDTO toGetUserListDto(GetUserListReq getUserListReq);

    Page<UserVO> toUserVoPage(Page<UserDTO> userDtoPage);

    UpdateUserDTO toUpdateUserDto(Long id, UpdateUserReq updateUserReq);
}
