package cn.hex.ddp.manufacture.infrastructure.technique.persistence.po;

import cn.hex.ddp.manufacture.domain.technique.enums.SandLineTypeEnum;
import cn.hex.ddp.manufacture.domain.technique.enums.SandTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.tangzc.autotable.annotation.ColumnComment;
import com.tangzc.autotable.annotation.ColumnDefault;
import com.tangzc.autotable.annotation.ColumnNotNull;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.autotable.annotation.UniqueIndex;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 工艺 Po
 *
 * @author Huhaisen
 * @date 2024/05/04
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@Table(schema = "ddp_manufacture_service_v2", value = "technique", comment = "工艺表")
public class TechniquePO extends BaseDatabasePO {

    /**
     * 工艺编号
     */
    @ColumnComment(value = "工艺编号")
    @Column(length = 63, notNull = true, defaultValue = "''")
    @UniqueIndex
    private String techniqueNo;

    /**
     * 工艺名称
     */
    @ColumnComment(value = "工艺名称")
    @Column(length = 63, notNull = true, defaultValue = "''")
    private String name;

    /**
     * 砂类型
     */
    @ColumnComment(value = "砂类型")
    @ColumnNotNull
    @ColumnDefault("0")
    private SandTypeEnum sandType;

    /**
     * 砂线类型
     */
    @ColumnComment(value = "砂线类型")
    @ColumnNotNull
    @ColumnDefault("0")
    private SandLineTypeEnum sandLineType;


    /**
     * 砂箱组ID
     */
    @ColumnComment(value = "砂箱组ID")
    @ColumnNotNull
    @ColumnDefault("0")
    private Long sandboxGroupId;

    /**
     * 砂箱是否需要加热
     */
    @ColumnComment(value = "砂箱是否需要加热")
    @ColumnNotNull
    @ColumnDefault("false")
    private Boolean sandboxNeedHeat;

    /**
     * 砂箱加热温度
     * 根据砂箱是否加热判断是否需要此值
     */
    @ColumnComment(value = "砂箱加热温度")
    @ColumnNotNull
    @ColumnDefault("0")
    private Float sandboxHeatTemperature;


    /**
     * 砂箱模具组ID
     */
    @ColumnComment(value = "砂箱模具组ID")
    @ColumnNotNull
    @ColumnDefault("0")
    private Long sandboxMoldGroupId;

    /**
     * 砂箱模具是否需要加热
     */
    @ColumnComment(value = "砂箱模具是否需要加热")
    @ColumnNotNull
    @ColumnDefault("false")
    private Boolean sandboxMoldNeedHeat;

    /**
     * 砂箱模具加热温度
     * 根据砂箱模具是否加热判断是否需要此值
     */
    @ColumnComment(value = "砂箱模具加热温度")
    @ColumnNotNull
    @ColumnDefault("0")
    private Float sandboxMoldHeatTemperature;


    /**
     * 砂芯模具组ID
     */
    @ColumnComment(value = "砂芯模具组ID")
    @ColumnNotNull
    @ColumnDefault("0")
    private Long sandCoreMoldGroupId;

    /**
     * 砂芯模具是否需要加热
     */
    @ColumnComment(value = "砂芯模具是否需要加热")
    @ColumnNotNull
    @ColumnDefault("false")
    private Boolean sandCoreMoldNeedHeat;

    /**
     * 砂芯模具加热温度
     * 根据砂芯模具是否加热判断是否需要此值
     */
    @ColumnComment(value = "砂芯模具加热温度")
    @ColumnNotNull
    @ColumnDefault("0")
    private Float sandCoreMoldHeatTemperature;


    /**
     * 砂配方ID
     */
    @ColumnComment(value = "砂配方ID")
    @ColumnNotNull
    @ColumnDefault("0")
    private Long sandFormulaId;

    /**
     * 铁水配方ID
     */
    @ColumnComment(value = "铁水配方ID")
    @ColumnNotNull
    @ColumnDefault("0")
    private Long moltenIronFormulaId;

    /**
     * 铁水浇筑用量
     */
    @ColumnComment(value = "铁水浇筑用量")
    @ColumnNotNull
    @ColumnDefault("0")
    private Float moltenIronPouringQuantity;


    /**
     * 产品出库时间
     */
    @Column(comment = "产品出库时间", notNull = true, defaultValue = "0")
    private Double outBoundTime;
    /**
     * 产品的合模时间
     */
    @Column(comment = "产品的合模时间", notNull = true, defaultValue = "0")
    private Double moldClosingTime;
    /**
     * 产品的射砂时间
     */
    @Column(comment = "产品的射砂时间", notNull = true, defaultValue = "0")
    private Double sandblastingTime;
    /**
     * 产品的开模时间
     */
    @Column(comment = "产品的开模时间", notNull = true, defaultValue = "0")
    private Double moldOpeningTime;
    /**
     * 产品的喷涂时间
     */
    @Column(comment = "产品的喷涂时间", notNull = true, defaultValue = "0")
    private Double sprayTime;
    /**
     * 产品的合箱时间
     */
    @Column(comment = "产品的合箱时间", notNull = true, defaultValue = "0")
    private Double closingBoxTime;
    /**
     * 产品的浇筑时间
     */
    @Column(comment = "产品的浇筑时间", notNull = true, defaultValue = "0")
    private Double pouringTime;
    /**
     * 产品的冷却时间
     */
    @Column(comment = "产品的冷却时间", notNull = true, defaultValue = "0")
    private Double coolingTime;
    /**
     * 产品的开箱时间
     */
    @Column(comment = "产品的开箱时间", notNull = true, defaultValue = "0")
    private Double unboxingTime;

}
