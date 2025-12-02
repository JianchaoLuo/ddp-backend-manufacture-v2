package cn.hex.ddp.manufacture.domain.productionflow.model;

import cn.hex.ddp.manufacture.domain.common.enums.ProcessEnum;
import lombok.Data;

/**
 * 生产流程明细模型
 *
 * @author Huhaisen
 * @date 2024/05/05
 */
@Data
public class ProductionFlowDetail {
    /**
     * 生产流程明细ID(数据库ID)
     */
    private Long id;

    /**
     * 生产流程ID
     */
    private Long productionFlowId;

    /**
     * 子流程ID
     */
    private Long childFlowId;

    /**
     * 工序
     */
    private ProcessEnum process;

    /**
     * 工序顺序
     */
    private Integer processSequence;

}
