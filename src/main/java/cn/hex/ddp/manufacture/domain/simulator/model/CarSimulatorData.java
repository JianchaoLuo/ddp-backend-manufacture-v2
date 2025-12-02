package cn.hex.ddp.manufacture.domain.simulator.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 车辆仿真数据
 *
 * @author Huhaisen
 * @date 2024/12/26
 */
@Data
public class CarSimulatorData {

    /**
     * 主键
     */
    protected Long id;

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
     * 当前帧所有车辆数据
     */
    private String data;
}
