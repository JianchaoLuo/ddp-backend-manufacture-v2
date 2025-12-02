package cn.hex.ddp.manufacture.application.product.service;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.product.rest.req.*;
import cn.hex.ddp.manufacture.api.product.rest.vo.ProductDetailVO;
import cn.hex.ddp.manufacture.api.product.rest.vo.ProductModelVO;
import cn.hex.ddp.manufacture.api.product.rest.vo.ProductVO;
import cn.hex.ddp.manufacture.domain.product.model.Product;

import java.util.List;

/**
 * 产品服务接口
 *
 * @author Huhaisen
 * @date 2024/05/22
 */
public interface ProductService {
    /**
     * 创建产品型号
     * @param createProductModelReq 产品型号创建请求
     */
    void createProductModel(CreateProductModelReq createProductModelReq);

    /**
     * 获取产品型号分页数据
     * @param getProductModelPageReq 产品型号分页请求
     * @return {@link PageResult }<{@link ProductModelVO }>
     */
    PageResult<ProductModelVO> getProductModelPage(GetProductModelPageReq getProductModelPageReq);

    /**
     * 更新产品型号
     * @param id 产品型号ID
     * @param updateProductModelReq 产品型号更新请求
     */
    void updateProductModel(Long id, UpdateProductModelReq updateProductModelReq);

    /**
     * 删除产品型号
     * @param id
     */
    void deleteProductModel(Long id);

    /**
     * 创建产品
     * @param createProductReq 产品创建请求
     */
    void createProduct(CreateProductReq createProductReq);

    /**
     * 获取产品分页数据
     * @param getProductPageReq 产品分页请求
     * @return {@link PageResult }<{@link Product }>
     */
    PageResult<ProductVO> getProductPage(GetProductPageReq getProductPageReq);

    /**
     * 更新产品
     * @param id 产品ID
     * @param updateProductReq 产品更新请求
     */
    void updateProduct(Long id, UpdateProductReq updateProductReq);

    /**
     * 删除产品
     * @param id 产品ID
     */
    void deleteProduct(Long id);

}
