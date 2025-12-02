package cn.hex.ddp.manufacture.api.technique.rest.req;

import cn.hex.ddp.manufacture.domain.technique.model.FormulaDetail;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class CreateSandFormulaReq {

    /**
     * 砂配方型号
     */
    @NotEmpty(message = "砂配方型号不能为空")
    @Length(max = 63, message = "砂配方型号长度不能超过63")
    private String model;

    /**
     * 砂配方名称
     */
    @NotEmpty(message = "砂配方名称不能为空")
    @Length(max = 63, message = "砂配方名称长度不能超过63")
    private String name;

    /**
     * 砂配方构成明细
     */
    private List<FormulaDetail> formulaDetail;
}
