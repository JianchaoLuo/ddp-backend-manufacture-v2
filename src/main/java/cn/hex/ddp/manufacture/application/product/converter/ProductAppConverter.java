package cn.hex.ddp.manufacture.application.product.converter;

import cn.hex.ddp.manufacture.api.product.rest.vo.ProductDetailVO;
import cn.hex.ddp.manufacture.api.product.rest.vo.ProductModelVO;
import cn.hex.ddp.manufacture.api.product.rest.vo.ProductVO;
import cn.hex.ddp.manufacture.api.technique.rest.vo.TechniqueSummaryVO;
import cn.hex.ddp.manufacture.application.product.dto.out.ProductDTO;
import cn.hex.ddp.manufacture.application.product.dto.out.ProductModelDTO;
import cn.hex.ddp.manufacture.application.technique.dto.out.TechniqueDTO;
import cn.hex.ddp.manufacture.domain.product.model.Product;
import cn.hex.ddp.manufacture.domain.product.model.ProductModel;
import cn.hex.ddp.manufacture.domain.technique.model.Technique;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.jmx.export.annotation.ManagedOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 产品应用层转换器接口
 *
 * @author Huhaisen
 * @date 2024/05/22
 */
@Mapper(componentModel = "spring")
public interface ProductAppConverter {
    default List<ProductModelVO> toProductModelVOList(List<ProductModel> list,
                                                      Map<Long, TechniqueSummaryVO> techniqueSummaryVOMap) {
        ArrayList<ProductModelVO> productModelVOS = new ArrayList<>(list.size());
        for (ProductModel productModel : list) {
            ProductModelVO productModelVO = toProductModelVO(productModel);
            productModelVO.setTechnique(techniqueSummaryVOMap.getOrDefault(
                    productModel.getTechniqueId(), null
            ));
            productModelVOS.add(productModelVO);
        }
        return productModelVOS;
    }

    ProductModelVO toProductModelVO(ProductModel productModel);

    ProductModelDTO convertProductModelToDTO(ProductModel productModel);

    TechniqueDTO convertTechniqueToDTO(Technique technique);

    default List<ProductVO> toProductVOList(List<Product> list, Map<Long, TechniqueSummaryVO> techniqueSummaryVOMap) {
        ArrayList<ProductVO> productVOS = new ArrayList<>(list.size());
        for (Product product : list) {
            ProductVO productVO = toProductVO(product);
            productVO.setTechnique(techniqueSummaryVOMap.getOrDefault(
                    product.getTechniqueId(), null
            ));
            productVOS.add(productVO);
        }
        return productVOS;
    }

    ProductVO toProductVO(Product product);

    @Mapping(target ="id", source = "product.id")
    @Mapping(target = "name", source = "product.name")
    @Mapping(target = "model", source = "product.model")
    ProductDetailVO toProductDetailVO(Product product);

    List<ProductDetailVO> toProductDetailVOList(List<Product> products);

    ProductDTO convertProductToDTO(Product product);
}
