package cn.hex.ddp.manufacture.api.task.rest.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 以订单为单位创建仿真任务请求参数
 *
 * @author 冯泽邦
 * @date 2025/7/24
 */
@Data
public class CreateTaskByOrdersReq {
    /**
     * 仿真任务名称
     */
    @NotEmpty(message = "任务名称不能为空")
    @Length(max = 63, message = "任务名称长度不能超过63")
    private String name;

    /**
     * 计划生产日期
     */
    @NotNull(message = "计划生产日期不能为空")
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
    @Valid
    @NotEmpty(message = "仿真任务订单列表不能为空")
    private List<Long> orderIds;

    /**
     * 仿真故障模拟列表
     */
    @Valid
    private List<AddTaskFaultReq> taskFaults;
}