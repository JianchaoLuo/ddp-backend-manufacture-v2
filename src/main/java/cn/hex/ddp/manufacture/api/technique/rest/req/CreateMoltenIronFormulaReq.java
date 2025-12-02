package cn.hex.ddp.manufacture.api.technique.rest.req;

import cn.hex.ddp.manufacture.domain.technique.model.FormulaDetail;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class CreateMoltenIronFormulaReq {

    /**
     * 铁水配方型号
     */
    @NotEmpty(message = "铁水配方型号不能为空")
    @Length(max = 63, message = "铁水配方型号长度不能超过63")
    private String model;

    /**
     * 铁水配方名称
     */
    @NotEmpty(message = "铁水配方名称不能为空")
    @Length(max = 63, message = "铁水配方名称长度不能超过63")
    private String name;

    /**
     * 铁水配方构成明细
     */
    private List<FormulaDetail> formulaDetail;

    /**
     * 材质包名称
     */
    @Length(max = 63, message = "材质包名称长度不能超过63")
    private String materialPackName;

    /**
     * 单位量的材质所需要的准备烧铁水的时间
     */
    private Double materialTime;

}
