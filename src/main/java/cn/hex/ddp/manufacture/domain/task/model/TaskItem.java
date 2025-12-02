package cn.hex.ddp.manufacture.domain.task.model;

import lombok.Data;

/**
 * 仿真任务项模型
 *
 * @author Huhaisen
 * @date 2024/09/21
 */
@Data
public class TaskItem {
    /**
     * 任务项ID(数据库主键ID)
     */
    private Long id;

    /**
     * 仿真任务表ID
     */
    private Long taskId;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 生产数量
     */
    private Integer count;

    /**
     * 产品ID
     */
    private Long productId;
}
