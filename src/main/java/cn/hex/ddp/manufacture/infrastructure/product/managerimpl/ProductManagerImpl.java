package cn.hex.ddp.manufacture.infrastructure.product.managerimpl;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.product.rest.req.*;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.domain.order.model.Order;
import cn.hex.ddp.manufacture.domain.order.model.OrderItem;
import cn.hex.ddp.manufacture.domain.product.manager.ProductManager;
import cn.hex.ddp.manufacture.domain.product.model.Product;
import cn.hex.ddp.manufacture.domain.product.model.ProductModel;
import cn.hex.ddp.manufacture.domain.product.model.ProductionItem;
import cn.hex.ddp.manufacture.domain.scheduling.enums.PLanType;
import cn.hex.ddp.manufacture.infrastructure.product.managerimpl.converter.ProductInfraConverter;
import cn.hex.ddp.manufacture.infrastructure.product.persistence.po.*;
import cn.hex.ddp.manufacture.infrastructure.product.persistence.postgresql.repository.*;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 产品管理实现类
 *
 * @author Huhaisen
 * @date 2024/05/22
 */
@Repository
public class ProductManagerImpl implements ProductManager {

    @Autowired
    private ProductModelPORepository productModelRepository;

    @Autowired
    private ProductPORepository productRepository;

    @Autowired
    private ProductionItemPORepository productionItemRepository;

    @Autowired
    private ProductInfraConverter productInfraConverter;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createProductModel(CreateProductModelReq createProductModelReq) {
        productModelRepository.save(productInfraConverter.toProductModelPO(createProductModelReq));
    }

    @Override
    public PageResult<ProductModel> getProductModelPage(GetProductModelPageReq getProductModelPageReq) {
        Page<ProductModelPO> productModelPOPage = productModelRepository.lambdaQuery()
                .like(StringUtils.isNotEmpty(getProductModelPageReq.getName()), ProductModelPO::getName,
                        getProductModelPageReq.getName())
                .orderByDesc(ProductModelPO::getId)
                .page(getProductModelPageReq.toPage());
        if (productModelPOPage.getRecords().isEmpty()) {
            return PageResult.emptyResult();
        }

        return PageResult.of(productModelPOPage, productInfraConverter.toProductModelList(productModelPOPage.getRecords()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProductModel(Long id, UpdateProductModelReq updateProductModelReq) {
        ProductModelPO productModelPO = productInfraConverter.toProductModelPO(id, updateProductModelReq);
        productModelRepository.updateById(productModelPO);
    }

    @Override
    public ProductModel getProductModelById(Long id) {
        ProductModelPO productModelPO = productModelRepository.getById(id);
        return productInfraConverter.toProductModel(productModelPO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProductModelById(Long id) {
        productModelRepository.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createProduct(CreateProductReq createProductReq) {
        productRepository.save(productInfraConverter.toProductPO(createProductReq));
    }

    @Override
    public PageResult<Product> getProductPage(GetProductPageReq getProductPageReq) {
        Page<ProductPO> productPOPage = productRepository.lambdaQueryPlus()
                .like(StringUtils.isNotEmpty(getProductPageReq.getName()), ProductPO::getName,
                        getProductPageReq.getName())
                .like(StringUtils.isNotEmpty(getProductPageReq.getModel()), ProductPO::getModel,
                        getProductPageReq.getModel())
                .like(StringUtils.isNotEmpty(getProductPageReq.getMaterialNo()), ProductPO::getMaterialNo,
                        getProductPageReq.getMaterialNo())
                .orderByDesc(ProductPO::getId)
                .bindPage(getProductPageReq.toPage());
        if (productPOPage.getRecords().isEmpty()) {
            return PageResult.emptyResult();
        }

        return PageResult.of(productPOPage, productInfraConverter::toProductList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProduct(Long id, UpdateProductReq updateProductReq) {
        ProductPO productPO = productInfraConverter.toProductPO(id, updateProductReq);
        productRepository.updateById(productPO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProductById(Long id) {
        productRepository.removeById(id);
    }

    @Override
    public Product getProductById(Long productId) {
        ProductPO productPO = productRepository.getById(productId);
        return productInfraConverter.toProduct(productPO);
    }

    @Override
    public List<Product> getProductListByIds(List<Long> productIds) {
        if(productIds == null || productIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<ProductPO> productPOS = productRepository.listByIds(productIds);
        List<Product> productList = productPOS.stream().map(productInfraConverter::toProduct).collect(Collectors.toList());
        return productList;
    }

    @Override
    public Product getProductByName(String name) {
        ProductPO productPO = productRepository.lambdaQuery().eq(ProductPO::getName, name).one();
        return productInfraConverter.toProduct(productPO);
    }

    @Override
    public Product getProductByModel(String model) {
        ProductPO productPO = productRepository.lambdaQuery().eq(ProductPO::getModel, model).one();
        return productInfraConverter.toProduct(productPO);
    }

    @Override
    public List<Product> getProductsByTechniqueId(Long techniqueId) {
        List<ProductPO> productPOS = productRepository.lambdaQuery()
                .eq(ObjectUtils.isNotNull(techniqueId), ProductPO::getTechniqueId, techniqueId).list();
        return productInfraConverter.toProductList(productPOS);
    }

    @Override
    public Map<Long, Product> getProductMapByIds(List<Long> productIds) {
        if (CollectionUtils.isEmpty(productIds)) {
            return Collections.emptyMap();
        }
        List<ProductPO> productPOS = productRepository.listByIds(productIds);
        return productPOS.stream().collect(
                Collectors.toMap(ProductPO::getId, productInfraConverter::toProduct, (k1, k2) -> k1)
        );
    }


    @Override
    public List<Product> getProductsByIds(List<Long> productIdList) {
        List<ProductPO> productPOS = productRepository.listByIds(productIdList);
        return productPOS.stream()
                .map(productInfraConverter::toProduct)
                .collect(Collectors.toList());
    }

    @Override
    public void initProductionItemByOrderIds(List<Order> orders, PLanType planType) {
        // 参数校验
        if (CollectionUtils.isEmpty(orders)) {
            return;
        }

        // 过滤掉空的订单
        List<Order> validOrders = orders.stream()
                .filter(Objects::nonNull)
                .filter(order -> order.getId() != null)
                .toList();

        if (validOrders.isEmpty()) {
            return;
        }

        // 构建需要初始化的ProductionItem列表
        List<ProductionItem> productionItems = new ArrayList<>();

        for (Order order : validOrders) {
            Long orderId = order.getId();
            List<OrderItem> orderItems = order.getOrderItems();

            if (CollectionUtils.isEmpty(orderItems)) {
                continue;
            }

            // 为每个订单项创建对应的ProductionItem
            for (OrderItem item : orderItems) {
                Long productId = item.getProductId();
                Integer count = item.getCount();

                // 创建指定数量的ProductionItem实例
                for (int i = 0; i < count; i++) {
                    ProductionItem productionItem = new ProductionItem();
                    productionItem.setProductId(productId);
                    productionItem.setOrderId(orderId);
                    productionItem.setPlanType(planType);
                    // 设置其他必要属性
                    productionItems.add(productionItem);
                }
            }
        }

        // 批量保存ProductionItem
        if (!productionItems.isEmpty()) {
            productionItemRepository.saveBatch(
                    productInfraConverter.toProductionItemPOList(productionItems)
            );
        }
    }

    @Override
    public List<ProductionItem> deleteAndBackupProductionItemByPlanType(PLanType pLanType) {
        // 参数校验
        if (pLanType == null) {
            throw new BusinessException("计划类型不能为空");
        }

        // 查询所有与指定计划类型关联的排产产品关系记录用于备份
        List<ProductionItemPO> backupItems = productionItemRepository
                .lambdaQuery()
                .eq(ProductionItemPO::getPlanType, pLanType)
                .list();

        if (CollectionUtils.isEmpty(backupItems)) {
            // 如果没有关联记录，直接返回空列表
            return Collections.emptyList();
        }

        // 转换为ProductionItem列表用于返回
        List<ProductionItem> backupResult = productInfraConverter.toProductionItemList(backupItems);

        // 获取需要删除的排产产品ID列表
        List<Long> productionItemIds = backupItems.stream()
                .map(ProductionItemPO::getId)
                .distinct()
                .toList();

        // 删除排产产品记录(ProductionItem)
        if (!productionItemIds.isEmpty()) {
            productionItemRepository.removeByIds(productionItemIds);
        }

        return backupResult;
    }

    @Override
    public List<ProductionItem> getProductionItemsByOrderId(Long orderId, PLanType planType) {
        // 参数校验
        if (orderId == null) {
            return Collections.emptyList();
        }

        // 查询指定订单ID的ScheduledProductPO记录
        List<ProductionItemPO> productionItemPOS = productionItemRepository
                .lambdaQuery()
                .eq(ProductionItemPO::getOrderId, orderId)
                .list();

        // 转换为ProductionItem列表
        return productInfraConverter.toProductionItemList(productionItemPOS);
    }

    @Override
    public void saveProductionItemsBatch(List<ProductionItem> backupProductionItems) {
        // 参数校验
        if (CollectionUtils.isEmpty(backupProductionItems)) {
            return;
        }

        // 过滤掉空的对象
        List<ProductionItem> validItems = backupProductionItems.stream()
                .filter(Objects::nonNull)
                .toList();

        if (validItems.isEmpty()) {
            return;
        }

        // 转换为PO对象并批量保存
        List<ProductionItemPO> productionItemPOs = productInfraConverter.toProductionItemPOList(validItems);
        if (!productionItemPOs.isEmpty()) {
            productionItemRepository.saveBatch(productionItemPOs);
        }
    }
}
