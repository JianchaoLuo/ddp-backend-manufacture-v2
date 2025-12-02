package cn.hex.ddp.manufacture.application.productionflow.service.impl;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.productionflow.rest.req.CreateOrUpdateProductionFlowReq;
import cn.hex.ddp.manufacture.api.productionflow.rest.req.QueryProductionFlowByPageReq;
import cn.hex.ddp.manufacture.application.productionflow.service.ProductionFlowService;
import cn.hex.ddp.manufacture.domain.productionflow.manager.ProductionFlowManager;
import cn.hex.ddp.manufacture.domain.productionflow.model.ProductionFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 生成流程服务实现类
 *
 * @author Huhaisen
 * @date 2024/05/22
 */
@Service
public class ProductionFlowServiceImpl implements ProductionFlowService {

    @Autowired
    private ProductionFlowManager productionFlowManager;

    @Override
    public void createProductionFlow(CreateOrUpdateProductionFlowReq request) {
        productionFlowManager.createProductionFlow(request);
    }

    @Override
    public PageResult<ProductionFlow> queryProductionFlowByPage(QueryProductionFlowByPageReq request) {
        return productionFlowManager.queryProductionFlowByPage(request);
    }

    @Override
    public void updateProductionFlow(Long id, CreateOrUpdateProductionFlowReq request) {
        productionFlowManager.updateProductionFlow(id, request);
    }

    @Override
    public void deleteProductionFlow(Long id) {
        productionFlowManager.deleteProductionFlow(id);
    }
}
