package cn.hex.ddp.manufacture.infrastructure.mold.persistence.po;

import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.tangzc.autotable.annotation.ColumnComment;
import com.tangzc.autotable.annotation.ColumnDefault;
import com.tangzc.autotable.annotation.ColumnNotNull;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.autotable.annotation.UniqueIndex;
import com.tangzc.mpe.bind.metadata.annotation.BindEntity;
import com.tangzc.mpe.bind.metadata.annotation.JoinCondition;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 模具 PO
 *
 * @author Huhaisen
 * @date 2024/05/04
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@Table(schema = "ddp_manufacture_service_v2", value = "mold", comment = "模具表")
public class MoldPO extends BaseDatabasePO {

    /**
     * 模具编号
     * 每个模具实体的唯一编号
     */
    @Column(comment = "模具编号", length = 32, notNull = true, defaultValue = "''")
    @UniqueIndex
    private String moldNo;

    /**
     * 模具名称
     */
    @Column(comment = "模具名称", length = 32, notNull = true, defaultValue = "''")
    private String name;

    /**
     * 模具型号ID
     */
    @ColumnComment(value = "模具型号ID")
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private Long moldModelId;

    /**
     * 模具状态
     * 有没有被使用过之类的，是否可用...
     */
    @Column(comment = "模具状态", length = 512, notNull = true, defaultValue = "''")
    private String status;

    /**
     * 模具型号, 连表查询
     */
    @BindEntity(conditions = @JoinCondition(selfField = MoldPODefine.moldModelId, joinField = MoldModelPODefine.id))
    private MoldModelPO moldModel;
}
