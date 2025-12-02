package cn.hex.ddp.manufacture.api.task.rest.req;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 更新仿真任务请求参数
 *
 * @author Huhaisen
 * @date 2024/09/21
 */
@Data
public class UpdateTaskReq {
    /**
     * 仿真任务名称
     */
    @Length(max = 63, message = "任务名称长度不能超过63")
    private String name;

    /**
     * 计划生产日期
     */
    private LocalDateTime plannedProductionDate;

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
     * 仿真任务项列表
     */
    private List<AddTaskItemReq> taskItems;

    /**
     * 仿真故障模拟列表
     */
    @NotEmpty(message = "故障模拟列表不能为空")
    private List<AddTaskFaultReq> taskFaults;
}
