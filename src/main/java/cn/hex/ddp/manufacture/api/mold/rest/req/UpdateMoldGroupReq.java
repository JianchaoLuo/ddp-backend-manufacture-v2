package cn.hex.ddp.manufacture.api.mold.rest.req;

import cn.hex.ddp.manufacture.domain.mold.enums.MoldTypeEnum;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class UpdateMoldGroupReq {
    /**
     * 模具组合型号名称
     */
    @Length(max = 32, message = "模具组合型号名称长度不能超过32")
    private String name;

    /**
     * 模具组合类型
     */
    private MoldTypeEnum type;

    /**
     * 模具组合长
     */
    private Float length;

    /**
     * 模具组合宽
     */
    private Float width;

    /**
     * 模具组合高
     */
    private Float height;

    /**
     * 模具组合重量
     */
    private Float weight;

    /**
     * 模具组合总层数
     */
    private Integer layerCount;

    /**
     * 模具组明细
     */
    @Size(min = 1, message = "模具组明细数量不能小于1")
    private List<AddMoldBatchToGroupReq> groupItems;
}
