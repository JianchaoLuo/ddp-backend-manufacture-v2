package cn.hex.ddp.manufacture.api.sandbox.rest.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class CreateSandboxGroupReq {
    /**
     * 砂箱组型号名称
     */
    @NotEmpty(message = "砂箱组型号名称不能为空")
    @Length(max = 32, message = "砂箱组型号名称长度不能超过32")
    private String name;

    /**
     * 砂箱组明细
     */
    @NotEmpty(message = "砂箱组明细不能为空")
    @Size(min = 2, message = "砂箱组的数量不能小于2")
    private List<AddSandboxBatchToGroupReq> groupItems;
}
