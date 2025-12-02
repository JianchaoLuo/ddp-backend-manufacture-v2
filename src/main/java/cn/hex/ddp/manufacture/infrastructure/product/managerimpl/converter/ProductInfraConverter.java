package cn.hex.ddp.manufacture.infrastructure.product.managerimpl.converter;

import cn.hex.ddp.manufacture.api.product.rest.req.CreateProductModelReq;
import cn.hex.ddp.manufacture.api.product.rest.req.CreateProductReq;
import cn.hex.ddp.manufacture.api.product.rest.req.UpdateProductModelReq;
import cn.hex.ddp.manufacture.api.product.rest.req.UpdateProductReq;
import cn.hex.ddp.manufacture.domain.product.model.Product;
import cn.hex.ddp.manufacture.domain.product.model.ProductModel;
import cn.hex.ddp.manufacture.domain.product.model.ProductionItem;
import cn.hex.ddp.manufacture.infrastructure.product.persistence.po.ProductModelPO;
import cn.hex.ddp.manufacture.infrastructure.product.persistence.po.ProductPO;
import cn.hex.ddp.manufacture.infrastructure.product.persistence.po.ProductionItemPO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 产品信息转换器接口
 *
 * @author Huhaisen
 * @date 2024/05/22
 */
@Mapper(componentModel = "spring")
public interface ProductInfraConverter {
    ProductModelPO toProductModelPO(CreateProductModelReq createProductModelReq);

    ProductModelPO toProductModelPO(Long id, UpdateProductModelReq updateProductModelReq);

    List<ProductModel> toProductModelList(List<ProductModelPO> productModelPOList);

    ProductModel toProductModel(ProductModelPO productModelPO);

    ProductPO toProductPO(CreateProductReq createProductReq);

    ProductPO toProductPO(Long id, UpdateProductReq updateProductReq);

    List<Product> toProductList(List<ProductPO> productPOS);

    Product toProduct(ProductPO productPO);

    ProductionItemPO toProductionItemPO(ProductionItem productionItem);

    List<ProductionItemPO> toProductionItemPOList(List<ProductionItem> productionItems);

    ProductionItem toProductionItem(ProductionItemPO productionItemPO);

    List<ProductionItem> toProductionItemList(List<ProductionItemPO> productionItemPOS);
}
