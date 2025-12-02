package cn.hex.ddp.manufacture.api.productionflow.rest.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class CreateOrUpdateProductionFlowReq {
    /**
     * 生产流程名称
     */
    @NotEmpty(message = "生产流程名称不能为空")
    @Length(max = 32, message = "生产流程名称长度不能超过32")
    private String name;

    /**
     * 生产流程描述
     */
    @NotEmpty(message = "生产流程描述不能为空")
    @Length(max = 512, message = "生产流程描述长度不能超过512")
    private String description;

    /**
     * 生产流程详情
     */
    @NotEmpty(message = "生产流程详情不能为空")
    private List<CreateOrUpdateProductionFlowDetailReq> flowDetails;
}
