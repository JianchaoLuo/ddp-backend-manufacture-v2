package cn.hex.ddp.manufacture.domain.common.exception;

import cn.hex.ddp.manufacture.domain.plc.enums.ResultEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 冯泽邦
 * @date 2025/10/20
 */
@Slf4j
public class ApiException extends RuntimeException {

    private final int code;

    private static final long serialVersionUID = -7219505447612569684L;

    public ApiException(ResultEnum resultEnum){
        super(resultEnum.getMsg());
        log.info(resultEnum.toString());
        this.code = resultEnum.getCode();
    }

    public ApiException(String message, int code) {
        super(message);
        this.code = code;
    }

    public ApiException(String message) {
        this(message, ResultEnum.DEFAULT_ERROR.getCode());
    }

    public static void isTrue(boolean expression, String message) {
        if (expression) {
            throw new ApiException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (object == null) {
            throw new ApiException(message);
        }
    }

    public static void isNull(Object obj) {
        isNull(obj, "参数不能为空");
    }

    public static void isBlank(String text, String message) {
        if (StringUtils.isBlank(text)) {
            throw new ApiException(message);
        }
    }

    public static void isBlank(String text) {
        isBlank(text, "参数不能为空");
    }
}