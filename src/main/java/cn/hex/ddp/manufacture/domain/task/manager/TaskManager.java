package cn.hex.ddp.manufacture.domain.task.manager;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.task.rest.req.GetTaskPageReq;
import cn.hex.ddp.manufacture.application.task.dto.out.FaultDTO;
import cn.hex.ddp.manufacture.domain.task.model.Task;
import cn.hex.ddp.manufacture.domain.task.model.TaskItem;

import java.util.List;

/**
 * 仿真任务管理接口
 *
 * @author Huhaisen
 * @date 2024/09/21
 */
public interface TaskManager {
    void saveTask(Task task);

    PageResult<Task> getTaskPage(GetTaskPageReq getTaskPageReq);

    Task getTaskById(Long taskId);

    void updateTask(Task task);

    void deleteTaskById(Long taskId);

    void saveTaskItems(List<TaskItem> taskItems);

    void deleteTaskItemsByTaskId(Long taskId);

    Task getTaskByMissionId(Long missionId);

    List<TaskItem> getTaskItemsByOrderId(Long orderId);

    List<TaskItem> getTaskItemsByProductId(Long productId);

    void saveTaskFaults(List<FaultDTO> faultDTOList);

    List<FaultDTO> getTaskFaults(Long taskId);

    List<FaultDTO> getTaskFaultsByTaskId(Long taskId);
}
