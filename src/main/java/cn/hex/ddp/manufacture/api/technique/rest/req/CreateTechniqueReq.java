package cn.hex.ddp.manufacture.api.technique.rest.req;

import cn.hex.ddp.manufacture.domain.technique.enums.SandLineTypeEnum;
import cn.hex.ddp.manufacture.domain.technique.enums.SandTypeEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CreateTechniqueReq {
    /**
     * 工艺编号
     */
    @NotEmpty(message = "工艺编号不能为空")
    @Length(max = 63, message = "工艺编号长度不能超过63")
    private String techniqueNo;

    /**
     * 工艺名称
     */
    @NotEmpty(message = "工艺名称不能为空")
    @Length(max = 63, message = "工艺名称长度不能超过63")
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
     * 砂箱组ID
     */
    @NotNull(message = "砂箱组ID不能为空")
    private Long sandboxGroupId;

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
     * 砂箱模具组ID
     */
    @NotNull(message = "砂箱模具组ID不能为空")
    private Long sandboxMoldGroupId;

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
     * 砂芯模具组ID
     */
    @NotNull(message = "砂芯模具组ID不能为空")
    private Long sandCoreMoldGroupId;

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
    @NotNull(message = "砂配方不能为空")
    private Long sandFormulaId;

    /**
     * 铁水配方
     */
    @NotNull(message = "铁水配方不能为空")
    private Long moltenIronFormulaId;

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
