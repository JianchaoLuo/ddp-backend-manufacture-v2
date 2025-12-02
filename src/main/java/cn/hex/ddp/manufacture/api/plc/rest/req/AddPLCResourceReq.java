package cn.hex.ddp.manufacture.api.plc.rest.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 添加PLC与资源绑定参数
 * @author 冯泽邦
 * @date 2025/11/4
 */
@Data
public class AddPLCResourceReq {
    /**
     * 资源id
     */
    @NotNull
    private Long resourceId;

    /**
     * PLC_ID
     */
    @NotNull
    private Long plcId;
}
