package cn.hex.ddp.manufacture.infrastructure.task.persistence.po;

import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import cn.hex.ddp.manufacture.infrastructure.order.persistence.po.OrderPO;
import cn.hex.ddp.manufacture.infrastructure.order.persistence.po.OrderPODefine;
import cn.hex.ddp.manufacture.infrastructure.product.persistence.po.ProductPO;
import cn.hex.ddp.manufacture.infrastructure.product.persistence.po.ProductPODefine;
import com.tangzc.autotable.annotation.Index;
import com.tangzc.autotable.annotation.TableIndex;
import com.tangzc.autotable.annotation.enums.IndexTypeEnum;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.bind.metadata.annotation.BindEntity;
import com.tangzc.mpe.bind.metadata.annotation.JoinCondition;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 任务项 PO
 *
 * @author Huhaisen
 * @date 2024/09/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@TableIndex(name = "task_item_unique", fields = {TaskItemPODefine.taskId, TaskItemPODefine.orderId,
        TaskItemPODefine.productId}, type = IndexTypeEnum.UNIQUE)
@Table(schema = "ddp_manufacture_service_v2", value = "task_item", comment = "任务项")
public class TaskItemPO extends BaseDatabasePO {
    /**
     * 仿真任务表ID
     */
    @Column(comment = "仿真任务表ID", notNull = true, defaultValue = "0")
    @Index
    private Long taskId;

    /**
     * 订单ID
     */
    @Column(comment = "订单ID", notNull = true, defaultValue = "0")
    private Long orderId;

    /**
     * 生产数量
     */
    @Column(comment = "生产数量", notNull = true, defaultValue = "0")
    private Integer count;

    /**
     * 产品ID
     */
    @Column(comment = "产品ID", notNull = true, defaultValue = "0")
    private Long productId;
}
