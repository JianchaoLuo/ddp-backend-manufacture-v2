package cn.hex.ddp.manufacture.infrastructure.common.handlers;

import cn.dev33.satoken.stp.StpUtil;
import com.tangzc.mpe.annotation.handler.IOptionByAutoFillHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * 全局获取用户ID
 *
 * @author Huhaisen
 */
@Component
public class UserIdAutoFillHandler implements IOptionByAutoFillHandler<Long> {

    @Override
    public Long getVal(Object object, Class<?> clazz, Field field) {
        if (StpUtil.isLogin()) {
            return StpUtil.getLoginIdAsLong();
        } else {
            return -1L;
        }
    }
}
