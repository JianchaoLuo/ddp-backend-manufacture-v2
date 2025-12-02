package cn.hex.ddp.manufacture.domain.product.model;

import cn.hex.ddp.manufacture.domain.product.enums.ProductionProgressStatusEnum;
import lombok.Data;

/**
 * 历史排产产品表
 *
 * @Author: FengZebang
 * @Date: 2025/7/4
 */
@Data
public class HistoricalProductDetail {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 历史排产订单ID
     * 历史排产订单与历史排产产品是一对多关系
     */
    private Long HistoricalOrderDetailId;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 产品
     * 可以抽象理解为产品型号
     */
    private Product product;

    /**
     * 产品当日生产进度
     */
    private ProductionProgressStatusEnum productionProgressStatus;
}
