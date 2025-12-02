package cn.hex.ddp.manufacture.api.sandbox.rest.req;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class UpdateSandboxGroupReq {
    /**
     * 砂箱组型号名称
     */
    @Length(max = 32, message = "砂箱组型号名称长度不能超过32")
    private String name;

    /**
     * 砂箱组长度
     */
    private Float length;

    /**
     * 砂箱组宽度
     */
    private Float width;

    /**
     * 砂箱组高度
     */
    private Float height;

    /**
     * 砂箱组空重
     */
    private Float emptyWeight;

    /**
     * 砂箱组满重
     */
    private Float fullWeight;

    /**
     * 砂箱组总层数
     */
    private Integer layerCount;

    /**
     * 砂箱组明细
     */
    @Size(min = 2, message = "砂箱组的数量不能小于2")
    private List<AddSandboxBatchToGroupReq> groupItems;
}
