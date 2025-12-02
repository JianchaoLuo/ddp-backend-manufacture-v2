package cn.hex.ddp.manufacture.domain.simulator.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 点位仿真数据
 * @Author: FengZebang
 * @Date: 2025/3/11
 */
@Data
public class PositionSimulatorData {
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
     * 仿真当前时间
     */
    private LocalDateTime simulationTime;
    /**
     * 当前帧所有数据
     */
    private String data;
}
