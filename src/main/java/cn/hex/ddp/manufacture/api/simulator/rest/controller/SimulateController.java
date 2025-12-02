package cn.hex.ddp.manufacture.api.simulator.rest.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hex.ddp.manufacture.api.simulator.rest.req.StartSimulateReq;
import cn.hex.ddp.manufacture.api.simulator.rest.vo.SimulateStatusVO;
import cn.hex.ddp.manufacture.application.simulator.SimulateService;
import cn.hex.ddp.manufacture.application.task.dto.out.TaskDTO;
import cn.hex.ddp.manufacture.application.task.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 仿真器
 */
@Validated
@RestController
@RequestMapping("/api/v2/simulate")
public class SimulateController {
    @Autowired
    private SimulateService simulateService;

    @Autowired
    private TaskService taskService;

    /**
     * 开始仿真
     *
     * @param startSimulateReq
     */
    @SaIgnore
    @PostMapping("/prepare")
    public void prepareSimulate(@RequestBody @Valid StartSimulateReq startSimulateReq) {
        TaskDTO taskDTO = taskService.getTaskDetailById(startSimulateReq.getTaskId());
        simulateService.runSimulate(taskDTO);
    }

    /**
     * 获取仿真状态
     *
     * @return
     */
    @SaIgnore
    @GetMapping("/status")
    public SimulateStatusVO getSimulateStatus() {
        return simulateService.getSimulateStatus();
    }


}
