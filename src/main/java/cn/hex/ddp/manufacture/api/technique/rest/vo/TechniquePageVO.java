package cn.hex.ddp.manufacture.api.technique.rest.vo;

import cn.hex.ddp.manufacture.api.mold.rest.vo.MoldGroupSummaryVO;
import cn.hex.ddp.manufacture.api.sandbox.rest.vo.SandboxGroupSummaryVO;
import cn.hex.ddp.manufacture.domain.technique.enums.SandLineTypeEnum;
import cn.hex.ddp.manufacture.domain.technique.enums.SandTypeEnum;
import cn.hex.ddp.manufacture.domain.technique.model.MoltenIronFormula;
import cn.hex.ddp.manufacture.domain.technique.model.SandFormula;
import lombok.Data;

@Data
public class TechniquePageVO {
    /**
     * 工艺ID(数据库ID)
     */
    private Long id;

    /**
     * 工艺编号
     */
    private String techniqueNo;

    /**
     * 工艺名称
     */
    private String name;

    /**
     * 砂类型
     */
    private SandTypeEnum sandType;

    /**
     * 砂线类型
     */
    private SandLineTypeEnum sandLineType;


    /**
     * 砂箱组
     */
    private SandboxGroupSummaryVO sandboxGroup;

    /**
     * 砂箱是否需要加热
     */
    private Boolean sandboxNeedHeat;

    /**
     * 砂箱加热温度
     * 根据砂箱是否加热判断是否需要此值
     */
    private Float sandboxHeatTemperature;


    /**
     * 砂箱模具组
     */
    private MoldGroupSummaryVO sandboxMoldGroup;

    /**
     * 砂箱模具是否需要加热
     */
    private Boolean sandboxMoldNeedHeat;

    /**
     * 砂箱模具加热温度
     * 根据砂箱模具是否加热判断是否需要此值
     */
    private Float sandboxMoldHeatTemperature;


    /**
     * 砂芯模具组
     */
    private MoldGroupSummaryVO sandCoreMoldGroup;

    /**
     * 砂芯模具是否需要加热
     */
    private Boolean sandCoreMoldNeedHeat;

    /**
     * 砂芯模具加热温度
     * 根据砂芯模具是否加热判断是否需要此值
     */
    private Float sandCoreMoldHeatTemperature;


    /**
     * 砂配方
     */
    private SandFormula sandFormula;

    /**
     * 铁水配方
     */
    private MoltenIronFormula moltenIronFormula;

    /**
     * 铁水浇筑用量
     */
    private Float moltenIronPouringQuantity;


    /**
     * 产品出库时间
     */
    private Double outBoundTime;
    /**
     * 产品的合模时间
     */
    private Double moldClosingTime;
    /**
     * 产品的射砂时间
     */
    private Double sandblastingTime;
    /**
     * 产品的开模时间
     */
    private Double moldOpeningTime;
    /**
     * 产品的喷涂时间
     */
    private Double sprayTime;
    /**
     * 产品的合箱时间
     */
    private Double closingBoxTime;
    /**
     * 产品的浇筑时间
     */
    private Double pouringTime;
    /**
     * 产品的冷却时间
     */
    private Double coolingTime;
    /**
     * 产品的开箱时间
     */
    private Double unboxingTime;

}
