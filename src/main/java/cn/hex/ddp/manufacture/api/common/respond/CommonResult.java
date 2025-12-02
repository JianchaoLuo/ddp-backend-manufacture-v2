package cn.hex.ddp.manufacture.api.common.respond;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通用返回结果类型
 *
 * @author Huhaisen
 * @date 2024/05/07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult {

    private static final Integer SUCCESS_CODE = 200;
    private static final Integer FAILURE_CODE = 500;
    private static final String SUCCESS_MESSAGE = "操作成功";
    private static final Object EMPTY_OBJECT = new Object();

    /**
     * 是否成功（必填）
     */
    private Boolean success;

    /**
     * 业务状态码（必填）
     */
    private Integer code;

    /**
     * 对业务状态码的解释（必填）
     */
    private String message;

    /**
     * 返回数据（选题）
     */
    private Object data;

    public static CommonResult success() {
        return success(EMPTY_OBJECT);
    }

    public static CommonResult success(Object data) {
        return new CommonResult(true, SUCCESS_CODE, SUCCESS_MESSAGE, data);
    }

    public static CommonResult failure(String message){
        return failure(FAILURE_CODE, message);
    }

    public static CommonResult failure(Integer code, String message) {
        return new CommonResult(false, code, message, EMPTY_OBJECT);
    }

}
