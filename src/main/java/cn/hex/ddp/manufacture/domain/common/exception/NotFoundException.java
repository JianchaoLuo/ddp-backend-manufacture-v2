package cn.hex.ddp.manufacture.domain.common.exception;

/**
 * 资源不存在异常
 *
 * @author Huhaisen
 * @date 2024/05/07
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

}
