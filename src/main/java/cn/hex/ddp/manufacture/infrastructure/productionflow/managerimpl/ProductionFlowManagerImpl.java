package cn.hex.ddp.manufacture.infrastructure.productionflow.managerimpl;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.productionflow.rest.req.CreateOrUpdateProductionFlowDetailReq;
import cn.hex.ddp.manufacture.api.productionflow.rest.req.CreateOrUpdateProductionFlowReq;
import cn.hex.ddp.manufacture.api.productionflow.rest.req.QueryProductionFlowByPageReq;
import cn.hex.ddp.manufacture.domain.productionflow.manager.ProductionFlowManager;
import cn.hex.ddp.manufacture.domain.productionflow.model.ProductionFlow;
import cn.hex.ddp.manufacture.infrastructure.productionflow.managerimpl.converter.ProductionFlowInfraConverter;
import cn.hex.ddp.manufacture.infrastructure.productionflow.persistence.po.ProductionFlowDetailPO;
import cn.hex.ddp.manufacture.infrastructure.productionflow.persistence.po.ProductionFlowPO;
import cn.hex.ddp.manufacture.infrastructure.productionflow.persistence.postgresql.repository.ProductionFlowDetailPORepository;
import cn.hex.ddp.manufacture.infrastructure.productionflow.persistence.postgresql.repository.ProductionFlowPORepository;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/**
 * 生产流程管理实现类
 *
 * @author Huhaisen
 * @date 2024/05/22
 */
@Repository
public class ProductionFlowManagerImpl implements ProductionFlowManager {

    @Autowired
    private ProductionFlowPORepository productionFlowRepository;

    @Autowired
    private ProductionFlowDetailPORepository productionFlowDetailRepository;

    @Autowired
    private ProductionFlowInfraConverter productionFlowConverter;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createProductionFlow(CreateOrUpdateProductionFlowReq request) {
        // 保存生产流程
        ProductionFlowPO productionFlowPO = productionFlowConverter.toProductionFlowPO(request);
        productionFlowRepository.save(productionFlowPO);

        // 保存生产流程明细
        ArrayList<ProductionFlowDetailPO> productionFlowDetailPOS = new ArrayList<>();
        for (CreateOrUpdateProductionFlowDetailReq flowDetail : request.getFlowDetails()) {
            ProductionFlowDetailPO productionFlowDetailPO = productionFlowConverter.toProductionFlowDetailPO(
                    flowDetail, productionFlowPO.getId()
            );
            productionFlowDetailPOS.add(productionFlowDetailPO);
        }
        productionFlowDetailRepository.saveBatch(productionFlowDetailPOS);
    }

    @Override
    public PageResult<ProductionFlow> queryProductionFlowByPage(QueryProductionFlowByPageReq request) {
        // 连表查询生产流程及其明细
        Page<ProductionFlowPO> productionFlowPOPage = productionFlowRepository.lambdaQueryPlus()
                .like(StringUtils.isNotEmpty(request.getName()), ProductionFlowPO::getName, request.getName())
                .like(StringUtils.isNotEmpty(request.getDescription()), ProductionFlowPO::getDescription,
                        request.getDescription())
                .orderByDesc(ProductionFlowPO::getId)
                .bindPage(request.toPage(), ProductionFlowPO::getFlowDetails);
        if (productionFlowPOPage.getRecords().isEmpty()) {
            return PageResult.emptyResult();
        }
        return PageResult.of(productionFlowPOPage, productionFlowConverter::toProductionFlowList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProductionFlow(Long id, CreateOrUpdateProductionFlowReq request) {
        // 更新生产流程
        ProductionFlowPO productionFlowPO = productionFlowConverter.toProductionFlowPO(id, request);
        productionFlowRepository.updateById(productionFlowPO);

        // 删除原有明细
        productionFlowDetailRepository.remove(Wrappers.lambdaQuery(ProductionFlowDetailPO.class)
                .eq(ProductionFlowDetailPO::getProductionFlowId, id));

        // 保存新明细
        ArrayList<ProductionFlowDetailPO> productionFlowDetailPOS = new ArrayList<>();
        for (CreateOrUpdateProductionFlowDetailReq flowDetail : request.getFlowDetails()) {
            ProductionFlowDetailPO productionFlowDetailPO = productionFlowConverter.toProductionFlowDetailPO(
                    flowDetail, id
            );
            productionFlowDetailPOS.add(productionFlowDetailPO);
        }
        productionFlowDetailRepository.saveBatch(productionFlowDetailPOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProductionFlow(Long id) {
        // 删除生产流程
        productionFlowRepository.removeById(id);
        // 删除生产流程明细
        productionFlowDetailRepository.remove(Wrappers.lambdaQuery(ProductionFlowDetailPO.class)
               .eq(ProductionFlowDetailPO::getProductionFlowId, id));
    }
}
