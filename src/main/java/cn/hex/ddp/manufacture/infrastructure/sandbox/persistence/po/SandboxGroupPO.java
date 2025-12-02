package cn.hex.ddp.manufacture.infrastructure.sandbox.persistence.po;

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
 * 砂箱组 PO
 *
 * @author Huhaisen
 * @date 2024/05/04
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@Table(schema = "ddp_manufacture_service_v2", value = "sandbox_group", comment = "砂箱组")
public class SandboxGroupPO extends BaseDatabasePO {

    /**
     * 砂箱组型号名称
     */
    @Column(comment = "砂箱组型号名称", length = 32, notNull = true, defaultValue = "''")
    private String name;

    /**
     * 砂箱组长度
     */
    @ColumnComment(value = "砂箱组长度")
    @ColumnNotNull
    @ColumnDefault("0")
    private Float length;

    /**
     * 砂箱组宽度
     */
    @ColumnComment(value = "砂箱组宽度")
    @ColumnNotNull
    @ColumnDefault("0")
    private Float width;

    /**
     * 砂箱组高度
     */
    @ColumnComment(value = "砂箱组高度")
    @ColumnNotNull
    @ColumnDefault("0")
    private Float height;

    /**
     * 砂箱组空重
     */
    @ColumnComment(value = "砂箱组空重")
    @ColumnNotNull
    @ColumnDefault("0")
    private Float emptyWeight;

    /**
     * 砂箱组满重
     */
    @ColumnComment(value = "砂箱组满重")
    @ColumnNotNull
    @ColumnDefault("0")
    private Float fullWeight;

    /**
     * 砂箱组总层数
     */
    @ColumnComment(value = "砂箱组总层数")
    @ColumnNotNull
    @ColumnDefault("0")
    private Integer layerCount;

    /**
     * 砂箱组砂箱列表, 连表查询
     */
    @BindEntity(conditions = @JoinCondition(selfField = SandboxGroupPODefine.id, joinField = SandboxGroupSandboxPODefine.sandboxGroupId))
    private List<SandboxGroupSandboxPO> sandboxGroupSandboxList;

}
