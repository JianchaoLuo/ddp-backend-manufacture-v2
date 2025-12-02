package cn.hex.ddp.manufacture.infrastructure.productionflow.persistence.po;

import cn.hex.ddp.manufacture.domain.common.enums.ProcessEnum;
import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.tangzc.autotable.annotation.ColumnComment;
import com.tangzc.autotable.annotation.ColumnDefault;
import com.tangzc.autotable.annotation.ColumnNotNull;
import com.tangzc.autotable.annotation.Index;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 生产流程明细 PO
 *
 * @author Huhaisen
 * @date 2024/05/05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@Table(schema = "ddp_manufacture_service_v2", value = "production_flow_detail", comment = "生产流程明细")
public class ProductionFlowDetailPO extends BaseDatabasePO {

    /**
     * 生产流程ID
     */
    @ColumnComment(value = "生产流程ID")
    @Index
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private Long productionFlowId;

    /**
     * 子流程ID
     */
    @ColumnComment(value = "子流程ID")
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private Long childFlowId;

    /**
     * 工序
     */
    @ColumnComment(value = "工序")
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private ProcessEnum process;

    /**
     * 工序顺序
     */
    @ColumnComment(value = "工序顺序")
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private Integer processSequence;

}
