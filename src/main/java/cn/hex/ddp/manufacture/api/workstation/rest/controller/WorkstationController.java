package cn.hex.ddp.manufacture.api.workstation.rest.controller;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.workstation.rest.converter.WorkstationApiConverter;
import cn.hex.ddp.manufacture.api.workstation.rest.req.CreateWorkstationReq;
import cn.hex.ddp.manufacture.api.workstation.rest.req.ListWorkstationReq;
import cn.hex.ddp.manufacture.api.workstation.rest.vo.WorkstationVO;
import cn.hex.ddp.manufacture.application.workstation.dto.WorkstationDTO;
import cn.hex.ddp.manufacture.application.workstation.service.WorkstationService;
import cn.hex.ddp.manufacture.domain.car.model.Car;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工位管理
 */
@Validated
@RestController
@RequestMapping("/api/v2/workstation")
public class WorkstationController {
    @Autowired
    private WorkstationService workstationService;
    @Autowired
    private WorkstationApiConverter converter;

    /**
     * 获取工位列表
     *
     * @return
     */
    @GetMapping("/page")
    public PageResult<WorkstationVO> listWorkstation(@Valid ListWorkstationReq listWorkstationReq) {
        return workstationService.listWorkstation(listWorkstationReq);
    }

    /**
     * 添加工位
     */
    @PostMapping("")
    public void addWorkstation(@RequestBody CreateWorkstationReq createWorkstationReq) {
        workstationService.addWorkstation(createWorkstationReq);
    }

    /**
     * 删除指定工位
     */
    @DeleteMapping("/{id}")
    public void deleteWorkstation(@PathVariable Long id) {
        workstationService.deleteWorkstation(id);
    }

    /**
     * 更新工位信息
     *
     * @param id
     * @param createWorkstationReq
     */
    @PutMapping("/{id}")
    public void updateWorkstation(@PathVariable Long id, @RequestBody CreateWorkstationReq createWorkstationReq) {
        workstationService.updateWorkstation(id, createWorkstationReq);
    }

    /**
     * 根据区域获取工位列表
     *
     * @return List<WorkstationVO>
     */
    @GetMapping("/list/{area}")
    public List<WorkstationVO> getWorkstationListByArea(@PathVariable("area") @Valid @NotNull(message = "area不能为空") AreaEnum area){
        List<WorkstationDTO> workstationDTOS = workstationService.getListByArea(area);
        if (workstationDTOS == null || workstationDTOS.isEmpty()) {
            return List.of();
        }
        return converter.toWorkstationVOList(workstationDTOS);
    }

    /**
     * 根据工位id获取工位信息
     */
    @GetMapping("/{id}")
    public WorkstationVO getWorkstation(@PathVariable("id") @Valid @NotNull(message = "id不能为空") Long id){
        return converter.toWorkstationVO(workstationService.getWorkstation(id));
    }
}
