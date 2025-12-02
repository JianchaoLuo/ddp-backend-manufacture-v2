package cn.hex.ddp.manufacture.api.task.rest.vo;

import cn.hex.ddp.manufacture.domain.order.model.Order;
import cn.hex.ddp.manufacture.domain.product.model.Product;
import lombok.Data;

/**
 * 仿真任务项VO
 *
 * @author Huhaisen
 * @date 2024/09/21
 */
@Data
public class TaskItemVO {
    /**
     * 任务项ID(数据库主键ID)
     */
    private Long id;

    /**
     * 仿真任务表ID
     */
    private Long taskId;

    /**
     * 订单
     */
    private Order order;

    /**
     * 生产数量
     */
    private Integer count;

    /**
     * 产品
     */
    private Product product;
}
