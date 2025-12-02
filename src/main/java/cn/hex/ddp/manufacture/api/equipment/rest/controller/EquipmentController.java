package cn.hex.ddp.manufacture.api.equipment.rest.controller;

import cn.hex.ddp.manufacture.api.car.rest.vo.CarSummaryVO;
import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.equipment.converter.EquipmentApiConverter;
import cn.hex.ddp.manufacture.api.equipment.rest.req.CreateEquipmentReq;
import cn.hex.ddp.manufacture.api.equipment.rest.req.ListEquipmentReq;
import cn.hex.ddp.manufacture.api.equipment.rest.vo.EquipmentSummaryVO;
import cn.hex.ddp.manufacture.api.equipment.rest.vo.EquipmentVO;
import cn.hex.ddp.manufacture.application.car.dto.out.CarSummaryDTO;
import cn.hex.ddp.manufacture.application.equipment.dto.EquipmentSummaryDTO;
import cn.hex.ddp.manufacture.application.equipment.service.EquipmentService;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备管理
 *
 * @author zhaolin
 * @date 2024/5/16
 */
@Validated
@RestController
@RequestMapping("/api/v2/equipment")
public class EquipmentController {
    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private EquipmentApiConverter equipmentCvt;

    /**
     * 获取设备列表
     *
     * @param listEquipmentReq
     * @return
     */
    @GetMapping("/page")
    public PageResult<EquipmentVO> listEquipment(@Valid ListEquipmentReq listEquipmentReq) {
        return equipmentService.listEquipment(listEquipmentReq);
    }

    /**
     * 创建设备
     */
    @PostMapping("")
    public void createEquipment(@Valid @RequestBody CreateEquipmentReq createEquipmentReq) {
        equipmentService.createEquipment(createEquipmentReq);
    }

    /**
     * 删除设备
     */
    @DeleteMapping("/{id}")
    public void deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
    }

    /**
     * 全量更新设备
     *
     * @param id
     * @param createEquipmentReq
     */
    @PutMapping("/{id}")
    public void updateEquipment(@PathVariable Long id, @RequestBody CreateEquipmentReq createEquipmentReq) {
        equipmentService.updateEquipment(id, createEquipmentReq);
    }

    /**
     * 获取设备详情
     */
    @GetMapping("/{id}")
    public EquipmentVO getEquipment(@PathVariable Long id) {
        return equipmentService.getEquipment(id);
    }

    /**
     * 根据区域获取设备列表
     *
     * @return List<EquipmentSummaryVO>
     */
    @GetMapping("/list/{area}")
    public List<EquipmentSummaryVO> getEquipmentListByArea(@PathVariable("area") @Valid @NotNull(message = "area不能为空") AreaEnum area) {
        List<EquipmentSummaryDTO> equipmentSummaryDTOS = equipmentService.getListByArea(area);
//        // 打印DTO内容，检查是否有数据
//        System.out.println("equipmentSummaryDTOS = " + equipmentSummaryDTOS);
        if (equipmentSummaryDTOS.isEmpty()) {
            return List.of();
        }
        return equipmentCvt.toEquipmentSummaryVOList(equipmentSummaryDTOS);
    }
}
