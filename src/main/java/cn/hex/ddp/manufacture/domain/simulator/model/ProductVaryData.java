package cn.hex.ddp.manufacture.domain.simulator.model;

import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductAfootEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum;
import lombok.Data;

/**
 * 产品中可能变换的数据，用于仿真
 * @Author: FengZebang
 * @Date: 2025/3/12
 */
@Data
public class ProductVaryData {
    /**
     * 数据库中的主键
     */
    private Long id;

    /**
     * 产品名称
     */
    private String name;

    /**
     * 产品类型
     */
    private ProductTypeEnum productType;

    /**
     * 产品正在进行的加工
     */
    private ProductAfootEnum productAfoot;
}
