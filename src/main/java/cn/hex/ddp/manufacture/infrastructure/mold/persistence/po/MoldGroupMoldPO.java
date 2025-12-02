package cn.hex.ddp.manufacture.infrastructure.mold.persistence.po;

import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.tangzc.autotable.annotation.*;
import com.tangzc.autotable.annotation.enums.IndexTypeEnum;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.bind.metadata.annotation.BindEntity;
import com.tangzc.mpe.bind.metadata.annotation.JoinCondition;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 模具组与模具的关联模型 PO
 *
 * @author Huhaisen
 * @date 2024/05/04
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@TableIndexes({
        @TableIndex(name = "moldGroupId_moldId_uni_index",
                fields = {MoldGroupMoldPODefine.moldGroupId, MoldGroupMoldPODefine.moldId},
                type = IndexTypeEnum.UNIQUE
        )
})
@Table(schema = "ddp_manufacture_service_v2", value = "mold_group_mold", comment = "模具组与模具关联表")
public class MoldGroupMoldPO extends BaseDatabasePO {

    /**
     * 模具组ID
     */
    @ColumnComment(value = "模具组ID")
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private Long moldGroupId;

    /**
     * 模具ID
     */
    @ColumnComment(value = "模具ID")
    @Index
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private Long moldId;

    /**
     * 层级
     * 1代表最上层，2代表第二层,..
     */
    @ColumnComment(value = "层级")
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private Integer layer;

    /**
     * 优先级
     */
    @ColumnComment(value = "优先级")
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private Integer priority;

    /**
     * 模具, 连表查询
     */
    @BindEntity(conditions = @JoinCondition(selfField = MoldGroupMoldPODefine.moldId, joinField = MoldPODefine.id))
    private MoldPO mold;
}
