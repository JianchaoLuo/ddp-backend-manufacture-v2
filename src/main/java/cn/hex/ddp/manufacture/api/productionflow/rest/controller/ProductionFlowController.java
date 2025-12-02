package cn.hex.ddp.manufacture.api.productionflow.rest.controller;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.productionflow.rest.req.CreateOrUpdateProductionFlowReq;
import cn.hex.ddp.manufacture.api.productionflow.rest.req.QueryProductionFlowByPageReq;
import cn.hex.ddp.manufacture.application.productionflow.service.ProductionFlowService;
import cn.hex.ddp.manufacture.domain.productionflow.model.ProductionFlow;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 生产流程Controller
 *
 * @author Huhaisen
 * @date 2024/05/22
 */
@Validated
@RestController
@RequestMapping("/api/v2/production-flow")
public class ProductionFlowController {

    @Autowired
    private ProductionFlowService productionFlowService;

    /**
     * 创建生成流程(包含流程明细)
     * @param request 请求参数
     */
    @PostMapping("")
    public void createProductionFlow(@RequestBody @Valid CreateOrUpdateProductionFlowReq request) {
        productionFlowService.createProductionFlow(request);
    }

    /**
     * 获得生产流程分页
     * @param request 请求参数
     * @return {@link PageResult }<{@link ProductionFlow }>
     */
    @GetMapping("/page")
    public PageResult<ProductionFlow> queryProductionFlowByPage(@Valid QueryProductionFlowByPageReq request) {
        return productionFlowService.queryProductionFlowByPage(request);
    }

    /**
     * 全量更新生产流程(包含流程明细)
     * @param id 生产流程ID
     * @param request 请求参数
     */
    @PutMapping("/{id}")
    public void updateProductionFlow(@PathVariable("id") @Valid @NotNull(message = "id不能为空") Long id,
                                     @RequestBody @Valid CreateOrUpdateProductionFlowReq request) {
        productionFlowService.updateProductionFlow(id, request);
    }

    /**
     * 删除生产流程(包含流程明细)
     * @param id 生产流程ID
     */
    @DeleteMapping("/{id}")
    public void deleteProductionFlow(@PathVariable("id") @Valid @NotNull(message = "id不能为空") Long id) {
        productionFlowService.deleteProductionFlow(id);
    }
}
