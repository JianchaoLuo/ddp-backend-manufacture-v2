package cn.hex.ddp.manufacture.domain.productionflow.manager;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.productionflow.rest.req.CreateOrUpdateProductionFlowReq;
import cn.hex.ddp.manufacture.api.productionflow.rest.req.QueryProductionFlowByPageReq;
import cn.hex.ddp.manufacture.domain.productionflow.model.ProductionFlow;

/**
 * 生成流程管理器接口
 *
 * @author Huhaisen
 * @date 2024/05/22
 */
public interface ProductionFlowManager {
    /**
     * 创建生产流程
     * @param request 创建生产流程请求
     */
    void createProductionFlow(CreateOrUpdateProductionFlowReq request);

    /**
     * 获得生产流程分页
     * @param request 分页请求
     * @return {@link PageResult }<{@link ProductionFlow }>
     */
    PageResult<ProductionFlow> queryProductionFlowByPage(QueryProductionFlowByPageReq request);

    /**
     * 更新生产流程
     * @param id 生产流程ID
     * @param request 更新生产流程请求
     */
    void updateProductionFlow(Long id, CreateOrUpdateProductionFlowReq request);

    /**
     * 删除生产流程
     * @param id 生产流程ID
     */
    void deleteProductionFlow(Long id);
}
