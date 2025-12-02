package cn.hex.ddp.manufacture.domain.common.exception;

import lombok.Getter;

/**
 * 自定义业务异常类
 * 会返回HTTP 200,但是会在response的body部分标注请求失败及失败原因
 *
 * @author Huhaisen
 * @date 2024/05/07
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(BssExType type) {
        super(type.getMsg());
        this.code = type.getCode();
    }

    public BusinessException(String msg) {
        super(msg);
        this.code = 10000;
    }

    public BusinessException(BssExType type, String msg) {
        super(type.getMsg() + ": " + msg);
        this.code = type.getCode();
    }

}
