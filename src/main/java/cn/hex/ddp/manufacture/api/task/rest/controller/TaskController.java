package cn.hex.ddp.manufacture.api.task.rest.controller;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.task.rest.req.CreateTaskByOrdersReq;
import cn.hex.ddp.manufacture.api.task.rest.vo.RandomFaultVO;
import cn.hex.ddp.manufacture.api.task.rest.req.CreateTaskReq;
import cn.hex.ddp.manufacture.api.task.rest.req.GetTaskPageReq;
import cn.hex.ddp.manufacture.api.task.rest.req.UpdateTaskReq;
import cn.hex.ddp.manufacture.api.task.rest.vo.TaskVO;
import cn.hex.ddp.manufacture.application.task.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 仿真任务管理
 *
 * @author Huhaisen
 * @date 2024/09/21
 */
@Validated
@RestController
@RequestMapping("/api/v2/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * 创建仿真任务（以产品为单位，很麻烦）
     */
    @PostMapping("")
    public void createTask(@RequestBody @Valid CreateTaskReq createTaskReq) {
        taskService.createTask(createTaskReq);
    }

    /**
     * 创建仿真任务（以订单为单位）
     */
    @PostMapping("/orders")
    public void createTaskByOrder(@RequestBody @Valid CreateTaskByOrdersReq createTaskByOrdersReq) {
        taskService.createTaskByOrders(createTaskByOrdersReq);
    }

    /**
     * 分页查询仿真任务
     */
    @GetMapping("/page")
    public PageResult<TaskVO> getTaskPage(@Valid GetTaskPageReq getTaskPageReq) {
        return taskService.getTaskPage(getTaskPageReq);
    }

    /**
     * 修改仿真任务
     */
    @PutMapping("/{taskId}")
    public void updateTask(@PathVariable @Valid @NotNull(message = "任务Id不能为空") Long taskId,
                           @RequestBody @Valid UpdateTaskReq updateTaskReq) {
        taskService.updateTask(taskId, updateTaskReq);
    }

    /**
     * 删除仿真任务
     */
    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable @Valid @NotNull(message = "任务Id不能为空") Long taskId) {
        taskService.deleteTask(taskId);
    }

    /**
     * 随机生成故障
     */
    public RandomFaultVO randomFault() {
        return taskService.randomFault();
    }
}
