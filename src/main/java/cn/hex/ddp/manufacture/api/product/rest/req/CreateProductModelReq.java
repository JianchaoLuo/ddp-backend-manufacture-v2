package cn.hex.ddp.manufacture.api.product.rest.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CreateProductModelReq {
    /**
     * 产品型号名称
     */
    @NotEmpty(message = "产品型号名称不能为空")
    @Length(max = 32, message = "产品型号名称长度不能超过32")
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
    @Length(max = 32, message = "产品材质编号长度不能超过32")
    private String materialNo;

    /**
     * 工艺ID
     */
    @NotNull(message = "工艺ID不能为空")
    private Long techniqueId;

}
