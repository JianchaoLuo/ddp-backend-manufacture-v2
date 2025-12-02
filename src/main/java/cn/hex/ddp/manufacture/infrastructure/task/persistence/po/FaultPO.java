package cn.hex.ddp.manufacture.infrastructure.task.persistence.po;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.task.enums.ResourceTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 仿真故障PO
 *
 * @author zhanganqi
 * @date 2025/06/13
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@Table(schema = "ddp_manufacture_service_v2", value = "fault", comment = "故障表")
public class FaultPO extends BaseDatabasePO {
    @Column(comment = "任务ID", notNull = true)
    private Long taskId;

    @Column(comment = "区域", notNull = true)
    private AreaEnum area;

    @Column(comment = "资源类型", notNull = true)
    private ResourceTypeEnum resourceType;

    @Column(comment = "资源ID",  notNull = true)
    private Long resourceId;

    @Column(comment = "故障开始时间", notNull = true)
    private LocalDateTime startTime;

    @Column(comment = "故障持续时间(秒)", notNull = true)
    private Long duration;
}
