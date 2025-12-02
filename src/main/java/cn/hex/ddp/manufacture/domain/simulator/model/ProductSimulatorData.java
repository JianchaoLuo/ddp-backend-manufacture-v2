package cn.hex.ddp.manufacture.domain.simulator.model;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 产品仿真数据
 * @Author: FengZebang
 * @Date: 2025/3/12
 */
@Data
public class ProductSimulatorData {
    /**
     * 主键
     */
    private Long id;

    /**
     * 仿真任务编号
     */
    private Long mission;

    /**
     * 仿真帧
     */
    private Long tick;

    /**
     * 当前仿真时间
     */
    private LocalDateTime simulationTime;

    /**
     * 仿真数据
     */
    private String data;
}
