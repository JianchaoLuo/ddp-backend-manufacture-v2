package cn.hex.ddp.manufacture.application.task.converter;

import cn.hex.ddp.manufacture.api.task.rest.req.*;
import cn.hex.ddp.manufacture.api.task.rest.vo.TaskItemVO;
import cn.hex.ddp.manufacture.api.task.rest.vo.TaskVO;
import cn.hex.ddp.manufacture.application.task.dto.out.FaultDTO;
import cn.hex.ddp.manufacture.application.task.dto.out.TaskDTO;
import cn.hex.ddp.manufacture.domain.order.model.Order;
import cn.hex.ddp.manufacture.domain.product.model.Product;
import cn.hex.ddp.manufacture.domain.task.model.Fault;
import cn.hex.ddp.manufacture.domain.task.model.Task;
import cn.hex.ddp.manufacture.domain.task.model.TaskItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 仿真任务应用层转换器接口
 *
 * @author Huhaisen
 * @date 2024/09/21
 */
@Mapper(componentModel = "spring")
public interface TaskAppConverter {
    Task toTask(CreateTaskReq createTaskReq);

    Task toTaskwithOrder(CreateTaskByOrdersReq createTaskByOrdersReq);

    TaskItem toTaskItem(Long taskId, AddTaskItemReq addTaskItemReq);

    default List<TaskVO> toTaskVOs(List<Task> tasks, Map<Long, Order> orderMap, Map<Long, Product> productMap) {
        if (tasks == null || tasks.isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList<TaskVO> taskVOS = new ArrayList<>(tasks.size());
        // 转换任务列表
        for (Task task : tasks) {
            TaskVO taskVO = toTaskVO(task);
            // 转换任务项列表
            List<TaskItem> taskItems = task.getTaskItems();
            if (taskItems == null || taskItems.isEmpty()) {
                taskVO.setTaskItems(Collections.emptyList());
            } else {
                ArrayList<TaskItemVO> taskItemVOS = new ArrayList<>(taskItems.size());
                // 拼接任务项中订单和产品信息
                for (TaskItem taskItem : taskItems) {
                    TaskItemVO taskItemVO = toTaskItemVO(taskItem);
                    taskItemVO.setOrder(orderMap.get(taskItem.getOrderId()));
                    taskItemVO.setProduct(productMap.get(taskItem.getProductId()));
                    taskItemVOS.add(taskItemVO);
                }
                taskVO.setTaskItems(taskItemVOS);
            }
            taskVOS.add(taskVO);
        }
        return taskVOS;
    }

    TaskItemVO toTaskItemVO(TaskItem taskItem);

    @Mapping(target = "taskItems", ignore = true)
    TaskVO toTaskVO(Task task);

    void updateTask(@MappingTarget Task task, UpdateTaskReq updateTaskReq);

    TaskDTO toTaskDTO(Task task);

    FaultDTO toFaultDTO(Long taskId, AddTaskFaultReq taskFault);

    Fault toFault(FaultDTO faultDTO);

    List<Fault> toFaults(List<FaultDTO> faultDTOs);
}
