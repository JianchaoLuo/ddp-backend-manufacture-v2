package cn.hex.ddp.manufacture.domain.product.model;

import lombok.Data;

/**
 * 产品型号
 *
 * @author Huhaisen
 * @date 2024/05/05
 */
@Data
public class ProductModel {
    /**
     * 产品型号ID(数据库ID)
     */
    private Long id;

    /**
     * 产品型号名称
     */
    private String name;

    /**
     * 长
     */
    private Float length;

    /**
     * 宽
     */
    private Float width;

    /**
     * 高
     */
    private Float height;

    /**
     * 直径
     */
    private Float diameter;

    /**
     * 内径
     */
    private Float innerDiameter;

    /**
     * 产品重量
     */
    private Float weight;

    /**
     * 产品材质编号
     */
    private String materialNo;

    /**
     * 工艺ID
     */
    private Long techniqueId;

}
