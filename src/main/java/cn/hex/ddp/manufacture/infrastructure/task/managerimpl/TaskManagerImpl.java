package cn.hex.ddp.manufacture.infrastructure.task.managerimpl;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.task.rest.req.GetTaskPageReq;
import cn.hex.ddp.manufacture.application.task.dto.out.FaultDTO;
import cn.hex.ddp.manufacture.domain.task.manager.TaskManager;
import cn.hex.ddp.manufacture.domain.task.model.Task;
import cn.hex.ddp.manufacture.domain.task.model.TaskItem;
import cn.hex.ddp.manufacture.infrastructure.task.managerimpl.converter.TaskInfraConverter;
import cn.hex.ddp.manufacture.infrastructure.task.persistence.po.FaultPO;
import cn.hex.ddp.manufacture.infrastructure.task.persistence.po.TaskItemPO;
import cn.hex.ddp.manufacture.infrastructure.task.persistence.po.TaskPO;
import cn.hex.ddp.manufacture.infrastructure.task.persistence.postgresql.repository.FaultPORepository;
import cn.hex.ddp.manufacture.infrastructure.task.persistence.postgresql.repository.TaskItemPORepository;
import cn.hex.ddp.manufacture.infrastructure.task.persistence.postgresql.repository.TaskPORepository;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 仿真任务管理器实现类
 *
 * @author Huhaisen
 * @date 2024/09/21
 */
@Repository
public class TaskManagerImpl implements TaskManager {
    @Autowired
    private TaskPORepository taskRepository;

    @Autowired
    private FaultPORepository faultRepository;

    @Autowired
    private TaskItemPORepository taskItemRepository;

    @Autowired
    private TaskInfraConverter taskConverter;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTask(Task task) {
        TaskPO taskPO = taskConverter.toTaskPO(task);
        taskRepository.save(taskPO);
        task.setId(taskPO.getId());
    }

    @Override
    public PageResult<Task> getTaskPage(GetTaskPageReq getTaskPageReq) {
        Page<TaskPO> taskPOPage = taskRepository.lambdaQueryPlus()
                .eq(ObjectUtils.isNotNull(getTaskPageReq.getSimulateStatus()), TaskPO::getSimulateStatus,
                        getTaskPageReq.getSimulateStatus())
                .like(StringUtils.isNotEmpty(getTaskPageReq.getName()), TaskPO::getName, getTaskPageReq.getName())
                .orderByDesc(TaskPO::getId)
                .bindPage(getTaskPageReq.toPage(), TaskPO::getTaskItems);
        return PageResult.of(taskPOPage, taskConverter::toTasks);
    }

    @Override
    public Task getTaskById(Long taskId) {
        TaskPO taskPO = taskRepository.lambdaQueryPlus().eq(TaskPO::getId, taskId)
                .bindOne(TaskPO::getTaskItems);
        return taskConverter.toTask(taskPO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTask(Task task) {
        taskRepository.updateById(taskConverter.toTaskPO(task));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTaskById(Long taskId) {
        taskRepository.removeById(taskId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTaskItems(List<TaskItem> taskItems) {
        List<TaskItemPO> taskItemPOs = taskConverter.toTaskItemPOs(taskItems);
        taskItemRepository.saveBatch(taskItemPOs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTaskItemsByTaskId(Long taskId) {
        taskItemRepository.remove(Wrappers.lambdaQuery(TaskItemPO.class).eq(TaskItemPO::getTaskId, taskId));
    }

    @Override
    public Task getTaskByMissionId(Long missionId) {
        TaskPO taskPO = taskRepository.lambdaQueryPlus().eq(TaskPO::getMissionId, missionId)
                .bindOne(TaskPO::getTaskItems);
        return taskConverter.toTask(taskPO);
    }

    @Override
    public List<TaskItem> getTaskItemsByOrderId(Long orderId) {
        List<TaskItemPO> taskItemPOList = taskItemRepository.lambdaQuery()
                .eq(ObjectUtils.isNotNull(orderId), TaskItemPO::getOrderId, orderId)
                .list();

        return taskConverter.toTaskItems(taskItemPOList);
    }

    @Override
    public List<TaskItem> getTaskItemsByProductId(Long productId) {
        List<TaskItemPO> taskItemPOList = taskItemRepository.lambdaQuery()
                .eq(ObjectUtils.isNotNull(productId), TaskItemPO::getProductId, productId)
                .list();

        return taskConverter.toTaskItems(taskItemPOList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTaskFaults(List<FaultDTO> faultDTOList) {
        List<FaultPO> faultPOList = faultDTOList.stream()
                .map(faultDTO -> taskConverter.toFaultPO(faultDTO))
                .toList();
        faultRepository.saveOrUpdateBatch(faultPOList);
    }

    @Override
    public List<FaultDTO> getTaskFaults(Long taskId) {
        List<FaultPO> faultPOList = faultRepository.lambdaQuery()
                .eq(ObjectUtils.isNotNull(taskId), FaultPO::getTaskId, taskId)
                .list();
        return taskConverter.toFaultDTOs(faultPOList);
    }

    @Override
    public List<FaultDTO> getTaskFaultsByTaskId(Long taskId) {
        List<FaultPO> faultPOList = faultRepository.lambdaQuery()
                .eq(ObjectUtils.isNotNull(taskId), FaultPO::getTaskId, taskId)
                .list();
        return taskConverter.toFaultDTOs(faultPOList);
    }
}
