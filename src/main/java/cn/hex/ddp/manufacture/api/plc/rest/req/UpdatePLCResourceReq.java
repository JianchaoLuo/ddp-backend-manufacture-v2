package cn.hex.ddp.manufacture.api.plc.rest.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @Description 更新PLC与资源绑定参数
 * @Author QSQ
 * @createTime: 2025/11/5 - 13:56
 */
@Data
public class UpdatePLCResourceReq {

    /**
     * 资源id
     */
    @NotNull
    private Long resourceId;

}
