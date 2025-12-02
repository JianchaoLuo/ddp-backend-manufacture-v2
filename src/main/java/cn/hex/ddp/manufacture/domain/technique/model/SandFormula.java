package cn.hex.ddp.manufacture.domain.technique.model;

import lombok.Data;

import java.util.List;

/**
 * 砂配方模型
 *
 * @author Huhaisen
 * @date 2024/05/04
 */
@Data
public class SandFormula {
    /**
     * 砂配方ID(数据库ID)
     */
    private Long id;

    /**
     * 砂配方型号
     */
    private String model;

    /**
     * 砂配方名称
     */
    private String name;

    /**
     * 砂配方构成明细
     */
    private List<FormulaDetail> formulaDetail;

}
