package cn.hex.ddp.manufacture.api.mold.rest.req;

import cn.hex.ddp.manufacture.domain.mold.enums.MoldTypeEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class CreateMoldGroupReq {
    /**
     * 模具组合型号名称
     */
    @NotEmpty(message = "模具组合型号名称不能为空")
    @Length(max = 32, message = "模具组合型号名称长度不能超过32")
    private String name;

    /**
     * 模具组合类型
     */
    @NotNull(message = "模具组合类型不能为空")
    private MoldTypeEnum type;

    /**
     * 模具组明细
     */
    @NotEmpty(message = "模具组明细不能为空")
    private List<AddMoldBatchToGroupReq> groupItems;
}
