package cn.hex.ddp.manufacture.api.productionflow.rest.req;

import cn.hex.ddp.manufacture.domain.common.enums.ProcessEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrUpdateProductionFlowDetailReq {
    /**
     * 子流程ID
     */
    private Long childFlowId;

    /**
     * 工序
     */
    @NotNull(message = "工序不能为空")
    private ProcessEnum process;

    /**
     * 工序顺序
     */
    @NotNull(message = "工序顺序不能为空")
    private Integer processSequence;
}
