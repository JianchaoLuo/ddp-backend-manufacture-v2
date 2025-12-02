package cn.hex.ddp.manufacture.domain.simulator.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 设备仿真数据
 * @Author: FengZebang
 * @Date: 2025/3/10
 */
@Data
public class EquipmentSimulatorData {
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
     * 当前帧所有设备数据
     */
    private String data;
}
