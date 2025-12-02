package cn.hex.ddp.manufacture.domain.productionflow.model;

import lombok.Data;

import java.util.List;

/**
 * 生产流程聚合模型
 *
 * @author Huhaisen
 * @date 2024/05/05
 */
@Data
public class ProductionFlow {
    /**
     * 生产流程ID(数据库ID)
     */
    private Long id;

    /**
     * 生产流程名称
     */
    private String name;

    /**
     * 生产流程描述
     */
    private String description;

    /**
     * 生产流程详情
     */
    private List<ProductionFlowDetail> flowDetails;

}
