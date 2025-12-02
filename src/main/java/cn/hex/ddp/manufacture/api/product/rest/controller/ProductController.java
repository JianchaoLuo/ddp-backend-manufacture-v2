package cn.hex.ddp.manufacture.api.product.rest.controller;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.product.rest.req.*;
import cn.hex.ddp.manufacture.api.product.rest.vo.ProductDetailVO;
import cn.hex.ddp.manufacture.api.product.rest.vo.ProductModelVO;
import cn.hex.ddp.manufacture.api.product.rest.vo.ProductVO;
import cn.hex.ddp.manufacture.application.product.service.ProductService;
import cn.hex.ddp.manufacture.domain.product.model.Product;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品Controller
 *
 * @author Huhaisen
 * @date 2024/05/22
 */
@Validated
@RestController
@RequestMapping("/api/v2/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 产品型号/创建产品型号
     * @param createProductModelReq 产品型号创建请求参数
     */
    @PostMapping("/model")
    public void createProductModel(@RequestBody @Valid CreateProductModelReq createProductModelReq) {
        productService.createProductModel(createProductModelReq);
    }

    /**
     * 产品型号/获取产品型号分页列表
     * @param getProductModelPageReq 产品型号分页查询请求参数
     * @return {@link PageResult }<{@link ProductModelVO }>
     */
    @GetMapping("/model/page")
    public PageResult<ProductModelVO> getProductModelPage(@Valid GetProductModelPageReq getProductModelPageReq) {
        return productService.getProductModelPage(getProductModelPageReq);
    }

    /**
     * 产品型号/更新产品型号
     * @param id 产品型号id
     * @param updateProductModelReq 产品型号更新请求参数
     */
    @PutMapping("/model/{id}")
    public void updateProductModel(@PathVariable("id") @Valid @NotNull(message = "产品型号id不能为空") Long id,
                                   @RequestBody @Valid UpdateProductModelReq updateProductModelReq) {
        productService.updateProductModel(id, updateProductModelReq);
    }

    /**
     * 产品型号/删除产品型号
     * @param id 产品型号id
     */
    @DeleteMapping("/model/{id}")
    public void deleteProductModel(@PathVariable("id") @Valid @NotNull(message = "产品型号id不能为空") Long id) {
        productService.deleteProductModel(id);
    }


    /**
     * 产品/创建产品
     * @param createProductReq 产品创建请求参数
     */
    @PostMapping("")
    public void createProduct(@RequestBody @Valid CreateProductReq createProductReq) {
        productService.createProduct(createProductReq);
    }

    /**
     * 产品/获取产品分页列表
     * @param getProductPageReq 产品分页查询请求参数
     * @return {@link PageResult }<{@link Product }>
     */
    @GetMapping("/page")
    public PageResult<ProductVO> getProductPage(@Valid GetProductPageReq getProductPageReq) {
        return productService.getProductPage(getProductPageReq);
    }

    /**
     * 产品/更新产品
     * @param id 产品id
     * @param updateProductReq 产品更新请求参数
     */
    @PutMapping("/{id}")
    public void updateProduct(@PathVariable("id") @Valid @NotNull(message = "产品id不能为空") Long id,
                              @RequestBody @Valid UpdateProductReq updateProductReq) {
        productService.updateProduct(id, updateProductReq);
    }

    /**
     * 产品/删除产品
     * @param id 产品id
     */
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable("id") @Valid @NotNull(message = "产品id不能为空") Long id) {
        productService.deleteProduct(id);
    }

}
