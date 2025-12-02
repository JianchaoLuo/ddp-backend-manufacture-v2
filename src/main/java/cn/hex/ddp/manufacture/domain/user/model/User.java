package cn.hex.ddp.manufacture.domain.user.model;

import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.domain.user.enums.UserPostEnum;
import cn.hex.ddp.manufacture.domain.user.enums.UserRoleEnum;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.Data;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

/**
 * 用户模型聚合类
 *
 * @author Huhaisen
 * @date 2024/04/27
 */
@Data
public class User {
    /**
     * 用户ID(数据库ID)
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 密码
     */
    private String password;

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


    /**
     * 验证注册人手机号，工号是否已经存在
     *
     * @param newUser 新注册用户信息
     */
    public void assertCanRegister(User newUser) {
        if (Objects.equals(getPhone(), newUser.getPhone())) {
            throw new BusinessException(BssExType.PHONE_ALREADY_EXIST);
        }
        if (Objects.equals(getWorkerNo(), newUser.getWorkerNo())) {
            throw new BusinessException(BssExType.WORKER_NO_ALREADY_EXIST);
        }
    }

    /**
     * 验证账号密码
     * @param pwd 输入的密码
     */
    public void assertPassword(String pwd) {
        if (pwd == null) {
            throw new BusinessException(BssExType.USERNAME_OR_PASSWORD_ERROR);
        }
        byte[] resultPwd = Base64.getDecoder().decode(password.getBytes(StandardCharsets.UTF_8));
        byte[] randomSalt = Arrays.copyOf(resultPwd, 32);
        byte[] randomSaltHashPwd = Arrays.copyOfRange(resultPwd, 32, resultPwd.length);
        byte[] loginHashPwd = SecureUtil.sha256().digest(ArrayUtil.addAll(randomSalt, pwd.getBytes(StandardCharsets.UTF_8)));
        if (!Arrays.equals(loginHashPwd, randomSaltHashPwd)) {
            throw new BusinessException(BssExType.USERNAME_OR_PASSWORD_ERROR);
        }
    }

    /**
     * 加密当前密码
     */
    public void encryptPwd() {
        password = encryptPwd(password);
    }

    private String encryptPwd(String passwd) {
        byte[] randomSalt = RandomUtil.randomBytes(32);
        byte[] randomSaltHashPwd = SecureUtil.sha256().digest(ArrayUtil.addAll(randomSalt, passwd.getBytes(StandardCharsets.UTF_8)));
        byte[] resultPwd = ArrayUtil.addAll(randomSalt, randomSaltHashPwd);
        return new String(Base64.getEncoder().encode(resultPwd), StandardCharsets.UTF_8);
    }

}
