package cn.hex.ddp.manufacture.domain.plc.enums;

/**
 * 枚举了一些常用API操作码
 * @author 冯泽邦
 * @date 2025/10/20
 */
public enum ResultEnum {
    /** 正确 **/
    SUCCESS(200,"正确"),
    /** 默认业务错误 **/
    DEFAULT_ERROR(-1,"业务错误"),
    /** 参数错误 **/
    PARAM_ERROR(400,"参数错误"),
    /** token 过期 **/
    TOKEN_TIMEOUT(401,"token过期"),
    /** 禁止访问 **/
    NO_AUTH(402,"禁止访问"),
    /** 资源没找到 **/
    NOT_FOUND(404,"资源没找到"),
    /** 服务器错误 **/
    SERVER_ERROR(500,"服务器错误"),

    NETWORK_DESERTED_PLEASE_TRY_AGAIN_LATER(600,"网络开小差了，请稍后再试。"),
    /** 与其他系统交互异常 **/
    SYSTEM_NOTICE_EXCEPTION(501,"与其他系统交互异常");

    private int code;
    private String msg;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private ResultEnum(int code) {
        this.code = code;
    }

    private ResultEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ResultEnum{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
