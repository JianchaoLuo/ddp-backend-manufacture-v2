package cn.hex.ddp.manufacture.infrastructure.mold.persistence.po;

import cn.hex.ddp.manufacture.domain.mold.enums.MoldTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.tangzc.autotable.annotation.ColumnComment;
import com.tangzc.autotable.annotation.ColumnDefault;
import com.tangzc.autotable.annotation.ColumnNotNull;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.bind.metadata.annotation.BindEntity;
import com.tangzc.mpe.bind.metadata.annotation.JoinCondition;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 模具组 PO
 *
 * @author Huhaisen
 * @date 2024/05/04
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@Table(schema = "ddp_manufacture_service_v2", value = "mold_group", comment = "模具组")
public class MoldGroupPO extends BaseDatabasePO {

    /**
     * 模具组合型号名称
     */
    @Column(comment = "模具组合型号名称", length = 32, notNull = true, defaultValue = "''")
    private String name;

    /**
     * 模具组合类型
     */
    @ColumnComment(value = "模具组合类型")
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private MoldTypeEnum type;

    /**
     * 模具组合长
     */
    @ColumnComment(value = "模具组合长")
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private Float length;

    /**
     * 模具组合宽
     */
    @ColumnComment(value = "模具组合宽")
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private Float width;

    /**
     * 模具组合高
     */
    @ColumnComment(value = "模具组合高")
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private Float height;

    /**
     * 模具组合重量
     */
    @ColumnComment(value = "模具组合重量")
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private Float weight;

    /**
     * 模具组合总层数
     */
    @ColumnComment(value = "模具组合总层数")
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private Integer layerCount;

    /**
     * 模具组模具列表, 连表查询
     */
    @BindEntity(conditions = @JoinCondition(selfField = MoldGroupPODefine.id, joinField = MoldGroupMoldPODefine.moldGroupId))
    private List<MoldGroupMoldPO> moldGroupMoldList;
}
