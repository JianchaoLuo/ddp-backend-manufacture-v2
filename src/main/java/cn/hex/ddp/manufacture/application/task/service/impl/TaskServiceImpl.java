package cn.hex.ddp.manufacture.application.task.service.impl;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.task.rest.req.*;
import cn.hex.ddp.manufacture.api.task.rest.vo.RandomFaultVO;
import cn.hex.ddp.manufacture.api.task.rest.vo.TaskVO;
import cn.hex.ddp.manufacture.application.product.service.ProductComponentBuilder;
import cn.hex.ddp.manufacture.application.simulator.SimulateService;
import cn.hex.ddp.manufacture.application.task.converter.TaskAppConverter;
import cn.hex.ddp.manufacture.application.task.dto.out.FaultDTO;
import cn.hex.ddp.manufacture.application.task.dto.out.TaskDTO;
import cn.hex.ddp.manufacture.application.task.dto.out.TaskItemDTO;
import cn.hex.ddp.manufacture.application.task.service.TaskService;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.common.exception.NotFoundException;
import cn.hex.ddp.manufacture.domain.order.manager.OrderManager;
import cn.hex.ddp.manufacture.domain.order.model.Order;
import cn.hex.ddp.manufacture.domain.product.manager.ProductManager;
import cn.hex.ddp.manufacture.domain.product.model.Product;
import cn.hex.ddp.manufacture.domain.task.enums.ResourceTypeEnum;
import cn.hex.ddp.manufacture.domain.task.manager.TaskManager;
import cn.hex.ddp.manufacture.domain.task.model.Task;
import cn.hex.ddp.manufacture.domain.task.model.TaskItem;
import cn.hex.ddp.manufacture.infrastructure.common.redis.RedisUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 仿真任务服务实现类
 *
 * @author Huhaisen
 * @date 2024/09/21
 */
@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskManager taskManager;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private ProductManager productManager;

    @Autowired
    private TaskAppConverter taskAppConverter;

    @Autowired
    private SimulateService simulateService;

    @Autowired
    private ProductComponentBuilder productComponentBuilder;

    @Override
    public void createTask(CreateTaskReq createTaskReq) {
        Task task = taskAppConverter.toTask(createTaskReq);
        // 在这里设置missionId
        task.setMissionId(getNewMissionId());
        // 校验任务项中订单和产品是否存在
        for (AddTaskItemReq taskItem : createTaskReq.getTaskItems()) {
            Optional.ofNullable(orderManager.getOrderById(taskItem.getOrderId())).orElseThrow(() ->
                    new NotFoundException("订单不存在，订单ID：" + taskItem.getOrderId()));
            Optional.ofNullable(productManager.getProductById(taskItem.getProductId())).orElseThrow(() ->
                    new NotFoundException("产品不存在，产品ID：" + taskItem.getProductId()));
        }
        //校验资源ID有效性,资源与分区的组合一共有45种组合，对于资源ID由前端来保证有效性ID

        // 设置产品数量
        int productCount = createTaskReq.getTaskItems().stream()
                .mapToInt(AddTaskItemReq::getCount)
                .sum();
        task.setProductCount(productCount);

        // 保存任务
        taskManager.saveTask(task);

        // 保存任务项
        List<TaskItem> taskItems = createTaskReq.getTaskItems().stream()
                .map(item -> taskAppConverter.toTaskItem(task.getId(), item))
                .toList();
        taskManager.saveTaskItems(taskItems);

        //保存故障模拟列表
        if (Optional.ofNullable(createTaskReq.getTaskFaults()).isPresent() && !createTaskReq.getTaskFaults().isEmpty()) {
            List<FaultDTO> faultDTOList = createTaskReq.getTaskFaults().stream()
                    .map(taskFault -> taskAppConverter.toFaultDTO(task.getId(), taskFault))
                    .toList();
            taskManager.saveTaskFaults(faultDTOList);
        }

        // 进行仿真
        simulateService.runSimulate(getTaskDetailById(task.getId()));
    }

    @Override
    public void createTaskByOrders(CreateTaskByOrdersReq createTaskByOrdersReq) {
        Task task = taskAppConverter.toTaskwithOrder(createTaskByOrdersReq);
        // 设置missionId
        task.setMissionId(getNewMissionId());
        //设置产品数量
        List<Long> orderIdList = createTaskByOrdersReq.getOrderIds();
        List<Order> orderList = orderManager.getOrderByIds(orderIdList);
        if (CollectionUtils.isEmpty(orderList)) {
            throw new NotFoundException("订单不存在，订单ID列表：" + orderIdList);
        }
        // 统计所有orderItem的count总和
        int totalCount = orderList.stream()
                // 处理可能为null的orderItems，避免空指针
                .flatMap(order -> Optional.ofNullable(order.getOrderItems()).orElse(Collections.emptyList()).stream())
                // 累加每个orderItem的count（若count可能为null，需设置默认值0）
                .mapToInt(orderItem -> Optional.ofNullable(orderItem.getCount()).orElse(0))
                .sum();
        task.setProductCount(totalCount);
        // 保存任务
        taskManager.saveTask(task);
        // 保存任务项
        List<TaskItem> taskItems = orderList.stream().flatMap(order -> order.getOrderItems().stream())
                .map(orderItem -> taskAppConverter.toTaskItem(task.getId(), new AddTaskItemReq(
                        orderItem.getOrderId(),
                        orderItem.getCount(),
                        orderItem.getProductId()
                )))
                .toList();

        taskManager.saveTaskItems(taskItems);

        //保存故障模拟列表
        if (Optional.ofNullable(createTaskByOrdersReq.getTaskFaults()).isPresent() && !createTaskByOrdersReq.getTaskFaults().isEmpty()) {
            List<FaultDTO> faultDTOList = createTaskByOrdersReq.getTaskFaults().stream()
                    .map(taskFault -> taskAppConverter.toFaultDTO(task.getId(), taskFault))
                    .toList();
            taskManager.saveTaskFaults(faultDTOList);
        }

        // 进行仿真
        simulateService.runSimulate(getTaskDetailById(task.getId()));
    }

    @Override
    public PageResult<TaskVO> getTaskPage(GetTaskPageReq getTaskPageReq) {
        PageResult<Task> taskPage = taskManager.getTaskPage(getTaskPageReq);
        // 获得订单信息Map
        List<Long> orderIds = taskPage.getList().stream().flatMap(task -> task.getTaskItems().stream())
                .map(TaskItem::getOrderId).toList();
        Map<Long, Order> orderMap = orderManager.getOrderMapByIds(orderIds);
        // 获得产品信息Map
        List<Long> productIds = taskPage.getList().stream().flatMap(task -> task.getTaskItems().stream())
                .map(TaskItem::getProductId).toList();
        Map<Long, Product> productMap = productManager.getProductMapByIds(productIds);
        // 组装订单和产品信息并返回
        return PageResult.of(taskPage, taskAppConverter.toTaskVOs(taskPage.getList(), orderMap, productMap));
    }

    @Override
    public void updateTask(Long taskId, UpdateTaskReq updateTaskReq) {
        Task task = taskManager.getTaskById(taskId);
        if (task == null) {
            throw new NotFoundException("任务不存在，任务ID：" + taskId);
        }
        // 更新任务并设置产品数量
        taskAppConverter.updateTask(task, updateTaskReq);
        if (CollectionUtils.isNotEmpty(updateTaskReq.getTaskItems())) {
            int productCount = updateTaskReq.getTaskItems().stream().mapToInt(AddTaskItemReq::getCount).sum();
            task.setProductCount(productCount);
        }
        taskManager.updateTask(task);
        // 若任务项列表不为空则更新任务项
        if (CollectionUtils.isNotEmpty(updateTaskReq.getTaskItems())) {
            // 校验任务项中订单和产品是否存在
            for (AddTaskItemReq taskItem : updateTaskReq.getTaskItems()) {
                Optional.ofNullable(orderManager.getOrderById(taskItem.getOrderId())).orElseThrow(() ->
                        new NotFoundException("订单不存在，订单ID：" + taskItem.getOrderId()));
                Optional.ofNullable(productManager.getProductById(taskItem.getProductId())).orElseThrow(() ->
                        new NotFoundException("产品不存在，产品ID：" + taskItem.getProductId()));
            }
            // 先删除原来任务项
            taskManager.deleteTaskItemsByTaskId(taskId);
            // 再添加新任务项
            List<TaskItem> taskItems = updateTaskReq.getTaskItems().stream().map(
                    item -> taskAppConverter.toTaskItem(taskId, item)
            ).toList();
            taskManager.saveTaskItems(taskItems);
            // TODO：修改完仿真任务后（主要是任务项变化），需要考虑重新进行仿真
        }
    }

    @Override
    public void deleteTask(Long taskId) {
        // 删除任务
        taskManager.deleteTaskById(taskId);
        // 删除任务项
        taskManager.deleteTaskItemsByTaskId(taskId);
    }

    @Override
    public TaskDTO getTaskDetailById(Long taskId) {
        Task task = taskManager.getTaskById(taskId);
        if (task == null) {
            throw new NotFoundException("任务不存在，任务ID：" + taskId);
        }
        TaskDTO taskDTO = taskAppConverter.toTaskDTO(task);
        // 获得订单信息Map
        List<Long> orderIds = task.getTaskItems().stream().map(TaskItem::getOrderId).toList();
        Map<Long, Order> orderMap = orderManager.getOrderMapByIds(orderIds);
        List<TaskItemDTO> taskItemDTOS = task.getTaskItems().stream().map(item -> new TaskItemDTO(
                item.getId(),
                item.getTaskId(),
                orderMap.get(item.getOrderId()),
                item.getCount(),
                productComponentBuilder.getProductDTO(item.getProductId())
        )).toList();
        taskDTO.setTaskItems(taskItemDTOS);

        // 组装故障信息：查询并转换为领域Fault列表，设置到TaskDTO
        List<FaultDTO> faultDTOs = taskManager.getTaskFaultsByTaskId(taskId);
        if (faultDTOs != null && !faultDTOs.isEmpty()) {
            taskDTO.setFaults(taskAppConverter.toFaults(faultDTOs));
        } else {
            taskDTO.setFaults(Collections.emptyList());
        }

        return taskDTO;
    }

    private Long getNewMissionId() {
        return RedisUtil.StringOps.incrBy("MISSION_ID", 1);
    }

    @Override
    public RandomFaultVO randomFault() {
        RandomFaultVO randomFaultVO = new RandomFaultVO();
        randomFaultVO.setArea(AreaEnum.values()[new Random().nextInt(AreaEnum.values().length)]);
        randomFaultVO.setResourceType(ResourceTypeEnum.values()[new Random().nextInt(ResourceTypeEnum.values().length)]);
        randomFaultVO.setStartTime(LocalDateTime.now().plusMinutes(new Random().nextInt(60)));
        randomFaultVO.setEndTime(randomFaultVO.getStartTime().plusMinutes(new Random().nextInt(60)));
        return randomFaultVO;
    }
}
