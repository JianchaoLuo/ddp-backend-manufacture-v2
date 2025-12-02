package cn.hex.ddp.manufacture.domain.task.model;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.task.enums.ResourceTypeEnum;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 仿真故障模型
 *
 * @author zhanganqi
 * @date 2025/06/13
 */
@Data
public class Fault {
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

    /**
     * 根据任务开始时间计算故障开始的tick
     */
    public long getStartTick(LocalDateTime taskStartTime) {
        if (startTime == null || taskStartTime == null) {
            return -1;
        }
        // 修改为每500ms一个tick
        return Duration.between(taskStartTime, startTime).toMillis() / 500;
    }

    /**
     * 根据任务开始时间计算故障结束的tick
     */
    public long getEndTick(LocalDateTime taskStartTime) {
        if (startTime == null || duration == null || taskStartTime == null) {
            return -1;
        }
        long startTick = getStartTick(taskStartTime);
        // duration是秒，转换为tick（每500ms一个tick）
        return startTick + (duration * 1000 / 500);
    }

    /**
     * 检查指定tick是否在故障期间
     */
    public boolean isInFaultPeriod(long currentTick, LocalDateTime taskStartTime) {
        long startTick = getStartTick(taskStartTime);
        long endTick = getEndTick(taskStartTime);
        return startTick != -1 && endTick != -1 &&
                currentTick >= startTick && currentTick < endTick;
    }


}
