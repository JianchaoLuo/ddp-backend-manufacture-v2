package cn.hex.ddp.manufacture.infrastructure.productionflow.persistence.po;

import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.tangzc.autotable.annotation.ColumnComment;
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
 * 生产流程 PO
 *
 * @author Huhaisen
 * @date 2024/05/05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@Table(schema = "ddp_manufacture_service_v2", value = "production_flow", comment = "生产流程表")
public class ProductionFlowPO extends BaseDatabasePO {

    /**
     * 生产流程名称
     */
    @ColumnComment(value = "生产流程名称")
    @Column(length = 32, notNull = true, defaultValue = "''")
    private String name;

    /**
     * 生产流程描述
     */
    @ColumnComment(value = "生产流程描述")
    @Column(length = 512, notNull = true, defaultValue = "''")
    private String description;

    /**
     * 生产流程明细，连表查询
     */
    @BindEntity(conditions = @JoinCondition(selfField = ProductionFlowPODefine.id, joinField = ProductionFlowDetailPODefine.productionFlowId))
    private List<ProductionFlowDetailPO> flowDetails;
}
