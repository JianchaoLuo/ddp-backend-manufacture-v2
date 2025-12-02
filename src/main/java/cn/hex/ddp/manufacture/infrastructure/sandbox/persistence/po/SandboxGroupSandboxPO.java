package cn.hex.ddp.manufacture.infrastructure.sandbox.persistence.po;

import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.tangzc.autotable.annotation.*;
import com.tangzc.autotable.annotation.enums.IndexTypeEnum;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 砂箱组与砂箱关系模型 PO
 *
 * @author Huhaisen
 * @date 2024/05/04
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@TableIndexes({
        @TableIndex(name = "sandboxGroupId_sandboxId_uni_index",
                fields = {SandboxGroupSandboxPODefine.sandboxGroupId, SandboxGroupSandboxPODefine.sandboxId},
                type = IndexTypeEnum.UNIQUE
        )
})
@Table(schema = "ddp_manufacture_service_v2", value = "sandbox_group_sandbox", comment = "砂箱组与砂箱关系表")
public class SandboxGroupSandboxPO extends BaseDatabasePO {

    /**
     * 砂箱组ID
     */
    @ColumnComment(value = "砂箱组ID")
    @ColumnNotNull
    @ColumnDefault("0")
    private Long sandboxGroupId;

    /**
     * 砂箱ID
     */
    @ColumnComment(value = "砂箱ID")
    @Index
    @ColumnNotNull
    @ColumnDefault("0")
    private Long sandboxId;

    /**
     * 砂箱层级
     * 1代表最上层，2代表第二层,..
     */
    @ColumnComment(value = "砂箱层级")
    @ColumnNotNull
    @ColumnDefault("0")
    private Integer layer;

    /**
     * 优先级
     */
    @ColumnComment(value = "优先级")
    @ColumnNotNull
    @ColumnDefault("0")
    private Integer priority;


}
