package cn.hex.ddp.manufacture.api.task.rest.vo;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.task.enums.ResourceTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 仿真故障VO
 *
 * @author zhanganqi
 * @date 2025/06/12
 */
@Data
public class FaultVO {
    /**
     * 故障ID(数据库ID)
     */
    private Long id;

    /**
     * 任务ID(关联的仿真任务ID)
     */
    private Long taskId;

    /**
     * 故障区域
     */
    private AreaEnum area;

    /**
     * 故障资源类型
     */
    private ResourceTypeEnum resourceType;

    /**
     * 故障资源ID
     */
    private Long resourceId; // 具体资源ID

    /**
     * 故障开始时间
     */
    private LocalDateTime startTime;

    /**
     * 故障持续时间
     */
    private Long duration; // 持续时间(秒)
}
