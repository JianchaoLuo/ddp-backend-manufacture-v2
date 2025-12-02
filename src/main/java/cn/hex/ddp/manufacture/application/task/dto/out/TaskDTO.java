package cn.hex.ddp.manufacture.application.task.dto.out;

import cn.hex.ddp.manufacture.domain.task.enums.SimulateStatusEnum;
import cn.hex.ddp.manufacture.domain.task.model.Fault;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 仿真任务DTO
 *
 * @author Huhaisen
 * @date 2024/09/21
 */
@Data
public class TaskDTO {
    /**
     * 任务ID(数据库ID)
     */
    private Long id;

    /**
     * 仿真任务编号(模拟器生成的ID)
     */
    private Long missionId;

    /**
     * 最大帧数(模拟器生成)
     */
    private Long maxTick;

    /**
     * 仿真任务名称
     */
    private String name;

    /**
     * 计划生产日期
     */
    private LocalDateTime plannedProductionDate;

    /**
     * 产品数量
     */
    private Integer productCount;

    /**
     * 仿真状态
     */
    private SimulateStatusEnum simulateStatus;

    /**
     * 仿真进度
     */
    private Float simulateProgress;

    /**
     * 预计开始时间
     */
    private LocalDateTime expectedStartTime;

    /**
     * 预计结束时间
     */
    private LocalDateTime expectedEndTime;

    /**
     * 预计生产耗时
     */
    private LocalTime expectedProductionTime;

    /**
     * 任务项列表
     */
    private List<TaskItemDTO> taskItems;

//    /**
//     * 故障模拟列表
//     */
//    private List<FaultDTO> taskFaults;

    /**
     * 故障列表
     */
    private List<Fault> faults;
}
