package cn.hex.ddp.manufacture.application.product.dto.out;

import cn.hex.ddp.manufacture.application.technique.dto.out.TechniqueDTO;
import lombok.Data;

/**
 * 产品型号DTO
 *
 * @author Huhaisen
 * @date 2024/06/24
 */
@Data
public class ProductModelDTO {
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
     * 工艺
     */
    private TechniqueDTO technique;
}
