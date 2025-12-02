package cn.hex.ddp.manufacture.domain.technique.model;

import lombok.Data;

import java.util.List;

/**
 * 铁水配方模型
 *
 * @author Huhaisen
 * @date 2024/05/04
 */
@Data
public class MoltenIronFormula {
    /**
     * 铁水配方ID(数据库ID)
     */
    private Long id;

    /**
     * 铁水配方型号
     */
    private String model;

    /**
     * 铁水配方名称
     */
    private String name;

    /**
     * 铁水配方构成明细
     */
    private List<FormulaDetail> formulaDetail;

    /**
     * 材质包名称
     */
    private String materialPackName;

    /**
     * 单位量的材质所需要的准备烧铁水的时间
     */
    private Double materialTime;

}
