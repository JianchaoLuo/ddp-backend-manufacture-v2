package cn.hex.ddp.manufacture.application.product.service;

import cn.hex.ddp.manufacture.application.product.converter.ProductAppConverter;
import cn.hex.ddp.manufacture.application.product.dto.out.ProductDTO;
import cn.hex.ddp.manufacture.application.product.dto.out.ProductModelDTO;
import cn.hex.ddp.manufacture.application.technique.converter.TechniqueServiceConverter;
import cn.hex.ddp.manufacture.application.technique.dto.out.TechniqueDTO;
import cn.hex.ddp.manufacture.domain.mold.manager.MoldManager;
import cn.hex.ddp.manufacture.domain.product.manager.ProductManager;
import cn.hex.ddp.manufacture.domain.product.model.Product;
import cn.hex.ddp.manufacture.domain.product.model.ProductModel;
import cn.hex.ddp.manufacture.domain.sandbox.manager.SandboxManager;
import cn.hex.ddp.manufacture.domain.technique.manager.TechniqueManager;
import cn.hex.ddp.manufacture.domain.technique.model.Technique;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 产品领域组件构建器
 * 用来组装产品相关的各种信息，包括产品型号，工艺，砂箱组，模具组等等
 *
 * @author Huhaisen
 * @date 2024/06/24
 */
@Component
public class ProductComponentBuilder {

    @Autowired
    private ProductManager productManager;

    @Autowired
    private TechniqueManager techniqueManager;

    @Autowired
    private ProductAppConverter productConverter;

    @Autowired
    private SandboxManager sandboxManager;

    @Autowired
    private MoldManager moldManager;

    @Autowired
    private TechniqueServiceConverter converter;

    /**
     * 根据产品型号ID，获取产品型号DTO（组装工艺，砂箱组，模具组等信息）
     *
     * @param productModelId 产品型号ID
     * @return {@link ProductModelDTO }
     */
    public ProductModelDTO getProductModelDTO(Long productModelId) {
        // 获得产品型号
        ProductModel productModel = productManager.getProductModelById(productModelId);
        ProductModelDTO productModelDTO = productConverter.convertProductModelToDTO(productModel);
        // 获得工艺信息
        Technique technique = techniqueManager.getTechnique(productModel.getTechniqueId());
        TechniqueDTO techniqueDTO = productConverter.convertTechniqueToDTO(technique);

        // 工艺中组装 砂箱组 砂箱模具组 砂芯模具组
        techniqueDTO.setSandboxGroup(sandboxManager.getSandboxGroupById(technique.getSandboxGroupId()));
        techniqueDTO.setSandboxMoldGroup(moldManager.getMoldGroupDetailById(technique.getSandboxMoldGroupId()));
        techniqueDTO.setSandCoreMoldGroup(moldManager.getMoldGroupDetailById(technique.getSandCoreMoldGroupId()));
        // 产品型号中组装工艺信息
        productModelDTO.setTechnique(techniqueDTO);
        return productModelDTO;
    }

    /**
     * 根据产品ID，获取产品DTO（组装工艺，砂箱组，模具组等信息）
     *
     * @param productId 产品ID
     * @return {@link ProductDTO }
     */
    public ProductDTO getProductDTO(Long productId) {
        // 获得产品
        Product product = productManager.getProductById(productId);
        ProductDTO productDTO = productConverter.convertProductToDTO(product);
        // 获得工艺信息
        Technique technique = techniqueManager.getTechnique(product.getTechniqueId());
        TechniqueDTO techniqueDTO = productConverter.convertTechniqueToDTO(technique);
        // 工艺中组装 砂箱组 砂箱模具组 砂芯模具组
        techniqueDTO.setSandboxGroup(sandboxManager.getSandboxGroupById(technique.getSandboxGroupId()));
        techniqueDTO.setSandboxMoldGroup(moldManager.getMoldGroupDetailById(technique.getSandboxMoldGroupId()));
        techniqueDTO.setSandCoreMoldGroup(moldManager.getMoldGroupDetailById(technique.getSandCoreMoldGroupId()));
        // 产品中组装工艺信息
        productDTO.setTechnique(techniqueDTO);
        return productDTO;
    }

}
