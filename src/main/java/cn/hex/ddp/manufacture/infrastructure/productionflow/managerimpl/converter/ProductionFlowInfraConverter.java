package cn.hex.ddp.manufacture.infrastructure.productionflow.managerimpl.converter;

import cn.hex.ddp.manufacture.api.productionflow.rest.req.CreateOrUpdateProductionFlowDetailReq;
import cn.hex.ddp.manufacture.api.productionflow.rest.req.CreateOrUpdateProductionFlowReq;
import cn.hex.ddp.manufacture.domain.productionflow.model.ProductionFlow;
import cn.hex.ddp.manufacture.infrastructure.productionflow.persistence.po.ProductionFlowDetailPO;
import cn.hex.ddp.manufacture.infrastructure.productionflow.persistence.po.ProductionFlowPO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 生产流程管理-基础设施转换器
 *
 * @author Huhaisen
 * @date 2024/05/22
 */
@Mapper(componentModel = "spring")
public interface ProductionFlowInfraConverter {
    ProductionFlowPO toProductionFlowPO(CreateOrUpdateProductionFlowReq request);

    ProductionFlowPO toProductionFlowPO(Long id, CreateOrUpdateProductionFlowReq request);

    ProductionFlowDetailPO toProductionFlowDetailPO(CreateOrUpdateProductionFlowDetailReq flowDetail, Long productionFlowId);

    List<ProductionFlow> toProductionFlowList(List<ProductionFlowPO> productionFlowPOS);
}
