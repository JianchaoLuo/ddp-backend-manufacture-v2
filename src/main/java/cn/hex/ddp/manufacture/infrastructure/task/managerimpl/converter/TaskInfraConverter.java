package cn.hex.ddp.manufacture.infrastructure.task.managerimpl.converter;

import cn.hex.ddp.manufacture.application.task.dto.out.FaultDTO;
import cn.hex.ddp.manufacture.domain.task.model.Task;
import cn.hex.ddp.manufacture.domain.task.model.TaskItem;
import cn.hex.ddp.manufacture.infrastructure.task.persistence.po.FaultPO;
import cn.hex.ddp.manufacture.infrastructure.task.persistence.po.TaskItemPO;
import cn.hex.ddp.manufacture.infrastructure.task.persistence.po.TaskPO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 仿真任务基础转换器
 *
 * @author Huhaisen
 * @date 2024/09/21
 */
@Mapper(componentModel = "spring")
public interface TaskInfraConverter {
    TaskPO toTaskPO(Task task);

    List<TaskItemPO> toTaskItemPOs(List<TaskItem> taskItems);

    List<Task> toTasks(List<TaskPO> taskPOS);

    Task toTask(TaskPO taskPO);

    List<TaskItem> toTaskItems(List<TaskItemPO> taskItemPOList);

    FaultPO toFaultPO(FaultDTO faultDTO);

    List<FaultDTO> toFaultDTOs(List<FaultPO> faultPOList);
}
