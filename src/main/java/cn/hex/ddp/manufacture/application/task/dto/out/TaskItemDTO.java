package cn.hex.ddp.manufacture.application.task.dto.out;

import cn.hex.ddp.manufacture.application.product.dto.out.ProductDTO;
import cn.hex.ddp.manufacture.domain.order.model.Order;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 仿真任务项DTO
 *
 * @author Huhaisen
 * @date 2024/09/21
 */
@Data
@AllArgsConstructor
public class TaskItemDTO {
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
    private ProductDTO product;
}
