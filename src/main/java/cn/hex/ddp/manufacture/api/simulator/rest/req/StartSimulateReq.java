package cn.hex.ddp.manufacture.api.simulator.rest.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StartSimulateReq {
    /**
     * 仿真任务ID
     */
    @NotNull(message = "taskId不能为空")
    private Long taskId;
}
