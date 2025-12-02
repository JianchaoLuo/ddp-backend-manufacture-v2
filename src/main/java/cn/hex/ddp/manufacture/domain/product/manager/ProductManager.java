package cn.hex.ddp.manufacture.domain.product.manager;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.product.rest.req.*;
import cn.hex.ddp.manufacture.domain.order.model.Order;
import cn.hex.ddp.manufacture.domain.product.model.Product;
import cn.hex.ddp.manufacture.domain.product.model.ProductModel;
import cn.hex.ddp.manufacture.domain.product.model.ProductionItem;
import cn.hex.ddp.manufacture.domain.product.model.ScheduledProduct;
import cn.hex.ddp.manufacture.domain.scheduling.enums.PLanType;

import java.util.List;
import java.util.Map;

/**
 * 产品管理接口
 * @author Huhaisen
 * @date 2024/05/22
 */
public interface ProductManager {
    /**
     * 创建产品型号
     * @param createProductModelReq 产品型号创建请求参数
     */
    void createProductModel(CreateProductModelReq createProductModelReq);

    /**
     * 获取产品型号分页数据
     * @param getProductModelPageReq 产品型号分页请求参数
     * @return {@link PageResult }<{@link ProductModel }>
     */
    PageResult<ProductModel> getProductModelPage(GetProductModelPageReq getProductModelPageReq);

    /**
     * 更新产品型号
     * @param id 产品型号ID
     * @param updateProductModelReq 产品型号更新请求参数
     */
    void updateProductModel(Long id, UpdateProductModelReq updateProductModelReq);

    /**
     * 根据ID获取产品型号
     * @param id 产品型号ID
     * @return {@link ProductModel }
     */
    ProductModel getProductModelById(Long id);

    /**
     * 根据ID删除产品型号
     * @param id 产品型号ID
     */
    void deleteProductModelById(Long id);

    /**
     * 创建产品
     * @param createProductReq 产品创建请求参数
     */
    void createProduct(CreateProductReq createProductReq);

    /**
     * 获得产品分页数据
     * @param getProductPageReq 产品分页请求参数
     * @return {@link PageResult }<{@link Product }>
     */
    PageResult<Product> getProductPage(GetProductPageReq getProductPageReq);

    /**
     * 跟新产品
     * @param id 产品ID
     * @param updateProductReq 产品更新请求参数
     */
    void updateProduct(Long id, UpdateProductReq updateProductReq);

    /**
     * 根据IDs删除产品
     * @param id 产品ID
     */
    void deleteProductById(Long id);

    Product getProductById(Long productId);

    List<Product> getProductListByIds(List<Long> productIds);

    Product getProductByName(String name);

    Product getProductByModel(String model);

    List<Product> getProductsByTechniqueId(Long techniqueId);

    Map<Long, Product> getProductMapByIds(List<Long> productIds);

    /**
     * 根据产品ID获取产品
     */
    List<Product> getProductsByIds(List<Long> productIdList);



    /**
     * 根据订单列表初始化排产产品实例
     */
    void initProductionItemByOrderIds(List<Order> orders, PLanType  planType);

    /**
     * 根据计划类型删除排产实例
     */
    List<ProductionItem> deleteAndBackupProductionItemByPlanType(PLanType pLanType);

    /**
     * 根据订单id获取涉及到的所有产品实例
     */
    List<ProductionItem> getProductionItemsByOrderId(Long orderId, PLanType planType);

    void saveProductionItemsBatch(List<ProductionItem> backupProductionItems);
}
