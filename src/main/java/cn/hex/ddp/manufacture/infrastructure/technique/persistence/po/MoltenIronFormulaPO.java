package cn.hex.ddp.manufacture.infrastructure.technique.persistence.po;

import cn.hex.ddp.manufacture.domain.technique.model.FormulaDetail;
import cn.hex.ddp.manufacture.infrastructure.common.handlers.JsonListFormulaDetailTypeHandler;
import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangzc.autotable.annotation.ColumnComment;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.autotable.annotation.UniqueIndex;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 铁水配方 PO
 *
 * @author Huhaisen
 * @date 2024/05/04
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@TableName(autoResultMap = true)
@Table(schema = "ddp_manufacture_service_v2", value = "molten_iron_formula", comment = "铁水配方")
public class MoltenIronFormulaPO extends BaseDatabasePO {

    /**
     * 铁水配方型号
     */
    @ColumnComment(value = "铁水配方型号")
    @Column(length = 63, notNull = true, defaultValue = "''")
    @UniqueIndex
    private String model;

    /**
     * 铁水配方名称
     */
    @ColumnComment(value = "铁水配方名称")
    @Column(length = 63, notNull = true, defaultValue = "''")
    private String name;

    /**
     * 铁水配方构成明细（JSON格式）
     */
    @TableField(typeHandler = JsonListFormulaDetailTypeHandler.class)
    @ColumnComment(value = "铁水配方构成明细")
    @Column(type = "JSONB", notNull = true, defaultValue = "'[]'")
    private List<FormulaDetail> formulaDetail;

    /**
     * 材质包名称
     */
    @ColumnComment(value = "材质包名称")
    @Column(length = 63, notNull = true, defaultValue = "''")
    private String materialPackName;

    /**
     * 单位量的材质所需要的准备烧铁水的时间
     */
    @Column(comment = "单位量的材质所需要的准备烧铁水的时间", notNull = true, defaultValue = "0")
    private Double materialTime;

}
