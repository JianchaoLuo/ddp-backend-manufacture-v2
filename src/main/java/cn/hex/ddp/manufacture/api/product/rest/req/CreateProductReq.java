package cn.hex.ddp.manufacture.api.product.rest.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CreateProductReq {
    /**
     * 产品名称
     */
    @NotEmpty(message = "产品名称不能为空")
    @Length(max = 63, message = "产品名称长度不能超过63")
    private String name;

    /**
     * 产品型号
     */
    @NotEmpty(message = "产品型号不能为空")
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
    @NotNull(message = "工艺ID不能为空")
    private Long techniqueId;
}
