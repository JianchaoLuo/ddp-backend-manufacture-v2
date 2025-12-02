package cn.hex.ddp.manufacture.api.product.rest.vo;

import lombok.Data;

/**
 * 产品细节VO
 * @author 冯泽邦
 * @date 2025/7/16
 */
@Data
public class ProductDetailVO {
    /**
     * 产品ID(数据库ID)
     */
    private Long id;

    /**
     * 产品名称
     */
    private String name;

    /**
     * 产品型号
     */
    private String model;
}
