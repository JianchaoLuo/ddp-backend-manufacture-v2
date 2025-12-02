package cn.hex.ddp.manufacture.infrastructure.task.persistence.po;

import cn.hex.ddp.manufacture.domain.task.enums.SimulateStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.autotable.annotation.UniqueIndex;
import com.tangzc.mpe.bind.metadata.annotation.BindEntity;
import com.tangzc.mpe.bind.metadata.annotation.JoinCondition;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * 仿真任务PO
 *
 * @author Huhaisen
 * @date 2024/09/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@Table(schema = "ddp_manufacture_service_v2", value = "task", comment = "仿真任务表")
public class TaskPO extends BaseDatabasePO {
    /**
     * 仿真任务编号(模拟器生成的ID)
     */
    @Column(comment = "仿真任务编号", notNull = true, defaultValue = "0")
    @UniqueIndex
    private Long missionId;

    /**
     * 最大帧数(模拟器生成)
     */
    @Column(comment = "最大帧数", notNull = true, defaultValue = "0")
    private Long maxTick;

    /**
     * 仿真任务名称
     */
    @Column(comment = "仿真任务名称",length = 63, notNull = true, defaultValue = "''")
    private String name;

    /**
     * 计划生产日期
     */
    @Column(comment = "计划生产日期", notNull = true, defaultValue = "CURRENT_TIMESTAMP")
    private LocalDateTime plannedProductionDate;

    /**
     * 产品数量
     */
    @Column(comment = "产品数量", notNull = true, defaultValue = "0")
    private Integer productCount;

    /**
     * 仿真状态
     */
    @Column(comment = "仿真状态", notNull = true, defaultValue = "0")
    private SimulateStatusEnum simulateStatus;

    /**
     * 仿真进度
     */
    @Column(comment = "仿真进度", notNull = true, defaultValue = "0")
    private Float simulateProgress;

    /**
     * 预计开始时间
     */
    @Column(comment = "预计开始时间", notNull = true, defaultValue = "CURRENT_TIMESTAMP")
    private LocalDateTime expectedStartTime;

    /**
     * 预计结束时间
     */
    @Column(comment = "预计结束时间", notNull = true, defaultValue = "CURRENT_TIMESTAMP")
    private LocalDateTime expectedEndTime;

    /**
     * 预计生产耗时
     */
    @Column(comment = "预计生产耗时", notNull = true, defaultValue = "CURRENT_TIME")
    private LocalTime expectedProductionTime;

    /**
     * 任务项列表, 连表查询
     */
    @BindEntity(conditions = @JoinCondition(selfField = TaskPODefine.id, joinField = TaskItemPODefine.taskId))
    private List<TaskItemPO> taskItems;

    /**
     * 故障列表, 连表查询
     */
    @BindEntity(conditions = @JoinCondition(selfField = TaskPODefine.id, joinField = FaultPODefine.taskId))
    private List<FaultPO> taskFaults;
}
