package cn.hex.ddp.manufacture.application.task.service;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.task.rest.req.CreateTaskByOrdersReq;
import cn.hex.ddp.manufacture.api.task.rest.vo.RandomFaultVO;
import cn.hex.ddp.manufacture.api.task.rest.req.CreateTaskReq;
import cn.hex.ddp.manufacture.api.task.rest.req.GetTaskPageReq;
import cn.hex.ddp.manufacture.api.task.rest.req.UpdateTaskReq;
import cn.hex.ddp.manufacture.api.task.rest.vo.TaskVO;
import cn.hex.ddp.manufacture.application.task.dto.out.TaskDTO;
import jakarta.validation.Valid;

/**
 * 仿真任务服务接口
 *
 * @author Huhaisen
 * @date 2024/09/21
 */
public interface TaskService {
    /**
     * 创建仿真任务
     *
     * @param createTaskReq 创建任务参数
     */
    void createTask(CreateTaskReq createTaskReq);

    void createTaskByOrders(@Valid CreateTaskByOrdersReq createTaskByOrdersReq);

    PageResult<TaskVO> getTaskPage(GetTaskPageReq getTaskPageReq);

    void updateTask(Long taskId, UpdateTaskReq updateTaskReq);

    void deleteTask(Long taskId);

    TaskDTO getTaskDetailById(Long taskId);

    /**
     * 返回随机故障
     */
    RandomFaultVO randomFault();

}
