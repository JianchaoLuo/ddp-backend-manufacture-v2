package cn.hex.ddp.manufacture.infrastructure.user.managerimpl.converter;

import cn.hex.ddp.manufacture.domain.user.model.User;
import cn.hex.ddp.manufacture.infrastructure.user.persistence.po.UserPO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;

/**
 * 用户信息转换器接口
 *
 * @author Huhaisen
 * @date 2024/05/06
 */
@Mapper(componentModel = "spring")
public interface UserInfraConverter {
    UserPO toUserPo(User user);

    User toUser(UserPO userPo);

    Page<User> toUserPage(Page<UserPO> userPoPage);
}
