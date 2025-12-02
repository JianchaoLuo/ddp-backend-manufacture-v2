package cn.hex.ddp.manufacture.application.product.service.impl;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.product.rest.req.*;
import cn.hex.ddp.manufacture.api.product.rest.vo.ProductModelVO;
import cn.hex.ddp.manufacture.api.product.rest.vo.ProductVO;
import cn.hex.ddp.manufacture.api.technique.rest.vo.TechniqueSummaryVO;
import cn.hex.ddp.manufacture.application.product.converter.ProductAppConverter;
import cn.hex.ddp.manufacture.application.product.service.ProductService;
import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.domain.common.exception.NotFoundException;
import cn.hex.ddp.manufacture.domain.order.manager.OrderManager;
import cn.hex.ddp.manufacture.domain.product.manager.ProductManager;
import cn.hex.ddp.manufacture.domain.product.model.Product;
import cn.hex.ddp.manufacture.domain.product.model.ProductModel;
import cn.hex.ddp.manufacture.domain.task.manager.TaskManager;
import cn.hex.ddp.manufacture.domain.technique.manager.TechniqueManager;
import cn.hex.ddp.manufacture.domain.technique.model.Technique;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 产品服务实现类
 *
 * @author Huhaisen
 * @date 2024/05/22
 */
@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductManager productManager;

    @Autowired
    private TechniqueManager techniqueManager;

    @Autowired
    private ProductAppConverter productConverter;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private TaskManager taskManager;

    @Override
    public void createProductModel(CreateProductModelReq createProductModelReq) {
        // 校验工艺ID对应的工艺是否存在
        Technique technique = techniqueManager.getTechnique(createProductModelReq.getTechniqueId());
        if (technique == null) {
            throw new BusinessException(BssExType.TECHNIQUE_NOT_FOUND,
                    "techniqueId:%s".formatted(createProductModelReq.getTechniqueId()));
        }

        productManager.createProductModel(createProductModelReq);
    }

    @Override
    public PageResult<ProductModelVO> getProductModelPage(GetProductModelPageReq getProductModelPageReq) {
        PageResult<ProductModel> productModelPage = productManager.getProductModelPage(getProductModelPageReq);
        // 获取工艺摘要信息
        List<Long> techniqueIds = productModelPage.getList().stream().map(ProductModel::getTechniqueId).toList();
        Map<Long, TechniqueSummaryVO> techniqueSummaryVOMap = techniqueManager.getTechniqueSummaryVOMap(techniqueIds);
        return PageResult.of(productModelPage, productConverter.toProductModelVOList(
                productModelPage.getList(), techniqueSummaryVOMap
        ));
    }

    @Override
    public void updateProductModel(Long id, UpdateProductModelReq updateProductModelReq) {
        ProductModel productModel = productManager.getProductModelById(id);
        if (productModel == null) {
            throw new BusinessException(BssExType.PRODUCT_MODEL_NOT_FOUND, "productModelId:%s".formatted(id));
        }

        if (updateProductModelReq.getTechniqueId() != null) {
            // 校验工艺ID对应的工艺是否存在
            Technique technique = techniqueManager.getTechnique(updateProductModelReq.getTechniqueId());
            if (technique == null) {
                throw new BusinessException(BssExType.TECHNIQUE_NOT_FOUND,
                        "techniqueId:%s".formatted(updateProductModelReq.getTechniqueId()));
            }
        }

        productManager.updateProductModel(id, updateProductModelReq);
    }

    @Override
    public void deleteProductModel(Long id) {

        productManager.deleteProductModelById(id);
    }

    @Override
    public void createProduct(CreateProductReq createProductReq) {
        // 校验产品名称和型号是否重复
        Optional.ofNullable(productManager.getProductByName(createProductReq.getName())).ifPresent(product -> {
            log.warn("[ProductServiceImpl.createProduct] 产品名称已存在, name:{}", createProductReq.getName());
            throw new BusinessException(BssExType.PRODUCT_NAME_REPEAT, createProductReq.getName());
        });
        Optional.ofNullable(productManager.getProductByModel(createProductReq.getModel())).ifPresent(product -> {
            log.warn("[ProductServiceImpl.createProduct] 产品型号已存在, model:{}", createProductReq.getModel());
            throw new BusinessException(BssExType.PRODUCT_MODEL_REPEAT, createProductReq.getModel());
        });
        // 校验工艺ID对应的工艺是否存在
        Optional.ofNullable(techniqueManager.getTechnique(createProductReq.getTechniqueId())).orElseThrow(() ->
                new BusinessException(BssExType.TECHNIQUE_NOT_FOUND,
                        "techniqueId:%s".formatted(createProductReq.getTechniqueId()))
        );
        productManager.createProduct(createProductReq);
    }

    @Override
    public PageResult<ProductVO> getProductPage(GetProductPageReq getProductPageReq) {
        PageResult<Product> productPage = productManager.getProductPage(getProductPageReq);
        if (productPage.getList().isEmpty()) {
            return PageResult.emptyResult();
        }
        // 获取工艺摘要信息
        List<Long> techniqueIds = productPage.getList().stream().map(Product::getTechniqueId).toList();
        Map<Long, TechniqueSummaryVO> techniqueSummaryVOMap = techniqueManager.getTechniqueSummaryVOMap(techniqueIds);
        return PageResult.of(productPage, productConverter.toProductVOList(
                productPage.getList(), techniqueSummaryVOMap
        ));
    }

    @Override
    public void updateProduct(Long id, UpdateProductReq updateProductReq) {
        // 校验产品名称和型号是否重复
        if (StringUtils.isNotEmpty(updateProductReq.getName())) {
            Optional.ofNullable(productManager.getProductByName(updateProductReq.getName())).ifPresent(product -> {
                if (!product.getId().equals(id)) {
                    log.warn("[ProductServiceImpl.updateProduct] 产品名称已存在, name:{}", updateProductReq.getName());
                    throw new BusinessException(BssExType.PRODUCT_NAME_REPEAT, updateProductReq.getName());
                }
            });
        }
        if (StringUtils.isNotEmpty(updateProductReq.getModel())) {
            Optional.ofNullable(productManager.getProductByModel(updateProductReq.getModel())).ifPresent(product -> {
                if (!product.getId().equals(id)) {
                    log.warn("[ProductServiceImpl.updateProduct] 产品型号已存在, model:{}", updateProductReq.getModel());
                    throw new BusinessException(BssExType.PRODUCT_MODEL_REPEAT, updateProductReq.getModel());
                }
            });
        }
        // 校验工艺ID对应的工艺是否存在
        if (updateProductReq.getTechniqueId() != null) {
            Optional.ofNullable(techniqueManager.getTechnique(updateProductReq.getTechniqueId())).orElseThrow(() ->
                    new BusinessException(BssExType.TECHNIQUE_NOT_FOUND,
                            "techniqueId:%s".formatted(updateProductReq.getTechniqueId()))
            );
        }
        productManager.updateProduct(id, updateProductReq);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productManager.getProductById(id);
        if (product == null) {
            log.warn("[ProductServiceImpl.deleteProduct] 产品不存在，id={}", id);
            throw new NotFoundException("产品不存在");
        }
        if(CollectionUtils.isNotEmpty(orderManager.getOrderItemsByProductId(id))){
            log.warn("[ProductServiceImpl.deleteProduct] 产品被订单占用");
            throw new BusinessException(BssExType.PRODUCT_BE_OCCUPIED);
        }
        if(CollectionUtils.isNotEmpty(taskManager.getTaskItemsByProductId(id))){
            log.warn("[ProductServiceImpl.deleteProduct] 产品被任务项占用");
            throw new BusinessException(BssExType.PRODUCT_BE_OCCUPIED_BY_TASK);
        }
        productManager.deleteProductById(id);
    }

}
