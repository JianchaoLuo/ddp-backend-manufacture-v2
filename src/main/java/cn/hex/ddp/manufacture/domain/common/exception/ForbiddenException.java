package cn.hex.ddp.manufacture.domain.common.exception;

/**
 * 非法操作异常
 * 会返回HTTP 403
 *
 * @author 廖菁璞
 */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
