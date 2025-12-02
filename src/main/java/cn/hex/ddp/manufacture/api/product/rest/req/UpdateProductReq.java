package cn.hex.ddp.manufacture.api.product.rest.req;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UpdateProductReq {
    /**
     * 产品名称
     */
    @Length(max = 63, message = "产品名称长度不能超过63")
    private String name;

    /**
     * 产品型号
     */
    @Length(max = 63, message = "产品型号长度不能超过63")
    private String model;

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
    @Length(max = 63, message = "产品材质编号长度不能超过63")
    private String materialNo;

    /**
     * 工艺ID
     */
    private Long techniqueId;
}
