package cn.hex.ddp.manufacture.domain.simulator.model;
import lombok.Data;
import java.time.LocalDateTime;
/**
 * 工位仿真数据
 * @Author: FengZebang
 * @Date: 2025/3/5
 */
@Data
public class WorkStationSimulatorData {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 仿真任务编号
     */
    private Long mission;
    /**
     * 当前帧
     */
    private Long tick;
    /**
     * 当前仿真时间
     */
    private LocalDateTime simulationTime;
    /**
     * 当前帧所有工位数据
     */
    private String data;
}