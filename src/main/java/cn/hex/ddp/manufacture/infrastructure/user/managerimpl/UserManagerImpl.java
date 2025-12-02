package cn.hex.ddp.manufacture.infrastructure.user.managerimpl;

import cn.hex.ddp.manufacture.domain.user.manager.UserManager;
import cn.hex.ddp.manufacture.domain.user.model.User;
import cn.hex.ddp.manufacture.infrastructure.user.managerimpl.converter.UserInfraConverter;
import cn.hex.ddp.manufacture.infrastructure.user.persistence.po.UserPO;
import cn.hex.ddp.manufacture.infrastructure.user.persistence.postgresql.repository.UserPORepository;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户聚合管理实现类
 *
 * @author Huhaisen
 * @date 2024/05/06
 */
@Repository
public class UserManagerImpl implements UserManager {

    @Autowired
    private UserPORepository userRepository;

    @Autowired
    private UserInfraConverter userCvt;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(User user) {
        userRepository.save(userCvt.toUserPo(user));
    }

    @Override
    public User getUserByPhone(String phone) {
        UserPO userPo = userRepository.getOne(Wrappers.lambdaQuery(UserPO.class)
                .eq(StringUtils.isNotBlank(phone), UserPO::getPhone, phone)
        );
        return userCvt.toUser(userPo);
    }

    @Override
    public User getUserByWorkerNo(String workerNo) {
        UserPO userPo = userRepository.getOne(Wrappers.lambdaQuery(UserPO.class)
                .eq(StringUtils.isNotBlank(workerNo), UserPO::getWorkerNo, workerNo)
        );
        return userCvt.toUser(userPo);
    }

    @Override
    public User getUserById(Long id) {
        UserPO userPo = userRepository.getById(id);
        return userCvt.toUser(userPo);
    }

    @Override
    public Page<User> getUserInfoPage(GetUserInfoListParam param) {
        Page<UserPO> userPoPage = userRepository.page(
                new Page<>(param.getCurrent(), param.getPageSize()),
                Wrappers.lambdaQuery(UserPO.class)
                        .like(ObjectUtils.isNotEmpty(param.getName()), UserPO::getName, param.getName())
                        .like(ObjectUtils.isNotEmpty(param.getPhone()), UserPO::getPhone, param.getPhone())
                        .like(ObjectUtils.isNotEmpty(param.getWorkerNo()), UserPO::getWorkerNo, param.getWorkerNo())
                        .eq(ObjectUtils.isNotNull(param.getRole()), UserPO::getRole, param.getRole())
                        .eq(ObjectUtils.isNotNull(param.getPost()), UserPO::getPost, param.getPost())
                        .orderByDesc(UserPO::getId)
        );
        return userCvt.toUserPage(userPoPage);
    }

    @Override
    public void updateById(User user) {
        userRepository.updateById(userCvt.toUserPo(user));
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.removeById(id);
    }
}
