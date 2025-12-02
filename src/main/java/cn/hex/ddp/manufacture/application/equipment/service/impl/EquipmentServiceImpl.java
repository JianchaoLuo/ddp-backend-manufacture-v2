package cn.hex.ddp.manufacture.application.equipment.service.impl;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.equipment.rest.req.CreateEquipmentReq;
import cn.hex.ddp.manufacture.api.equipment.rest.req.ListEquipmentReq;
import cn.hex.ddp.manufacture.api.equipment.rest.vo.EquipmentVO;
import cn.hex.ddp.manufacture.api.plc.rest.vo.PLCSummaryVO;
import cn.hex.ddp.manufacture.application.equipment.converter.EquipmentServiceConverter;
import cn.hex.ddp.manufacture.application.equipment.dto.EquipmentSummaryDTO;
import cn.hex.ddp.manufacture.application.equipment.service.EquipmentService;
import cn.hex.ddp.manufacture.application.plc.converter.PLCServiceConverter;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.domain.configuration.manager.ConfigurationManager;
import cn.hex.ddp.manufacture.domain.configuration.model.Coordinate;
import cn.hex.ddp.manufacture.domain.equipment.manager.EquipmentManager;
import cn.hex.ddp.manufacture.domain.equipment.model.Equipment;
import cn.hex.ddp.manufacture.domain.plc.manager.PLCManager;
import cn.hex.ddp.manufacture.domain.plc.model.PLC;
import cn.hex.ddp.manufacture.domain.workstation.manager.WorkstationManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author zhaolin
 * @date 2024/5/15
 */
@Slf4j
@Service
public class EquipmentServiceImpl implements EquipmentService {
    @Autowired
    private EquipmentManager equipmentManager;
    @Autowired
    private WorkstationManager workstationManager;
    @Autowired
    private ConfigurationManager configurationManager;
    @Autowired
    private PLCManager plcManager;

    @Autowired
    private EquipmentServiceConverter equipmentServiceConverter;
    @Autowired
    private PLCServiceConverter plcConverter;

    @Override
    public PageResult<EquipmentVO> listEquipment(ListEquipmentReq listEquipmentReq) {
        PageResult<Equipment> equipments = equipmentManager.listEquipment(listEquipmentReq);
        if (equipments.isEmpty()) {
            return PageResult.emptyResult();
        }
        List<Long> coordinateIds = equipments.getList().stream().map(Equipment::getCoordinateId).toList();
        Map<Long, Coordinate> coordinateMap = configurationManager.getCoordinateMapByIds(coordinateIds);
        //  获取设备对应的PLC信息
        List<Long> plcIds = equipments.getList().stream().map(Equipment::getPlcId).toList();
        Map<Long, PLCSummaryVO> plcSummaryVOMap = plcManager.getPLCSummaryVOMap(plcIds);
        return PageResult.of(
                equipments,
                equipmentServiceConverter.toEquipmentVO(equipments.getList(), coordinateMap, plcSummaryVOMap)
        );
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createEquipment(CreateEquipmentReq createEquipmentReq) {
        Equipment equipmentByNo = equipmentManager.getEquipmentByNo(createEquipmentReq.getEquipmentNo());
        if (equipmentByNo != null) {
            log.error("[EquipmentServiceImpl.createEquipment] 设备唯一编号:{} 已存在", createEquipmentReq.getEquipmentNo());
            throw new BusinessException(BssExType.EQUIPMENT_NO_REPEAT_ERROR, createEquipmentReq.getEquipmentNo());
        }
        Equipment equipment = equipmentServiceConverter.toEquipment(createEquipmentReq);
        equipmentManager.createEquipment(equipment);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteEquipment(Long id) {
        equipmentManager.deleteEquipment(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateEquipment(Long id, CreateEquipmentReq createEquipmentReq) {
        Equipment equipmentByNo = equipmentManager.getEquipmentByNo(createEquipmentReq.getEquipmentNo());
        if (equipmentByNo != null && !equipmentByNo.getId().equals(id)) {
            log.error("[EquipmentServiceImpl.updateEquipment] 设备唯一编号:{} 已存在", createEquipmentReq.getEquipmentNo());
            throw new BusinessException(BssExType.EQUIPMENT_NO_REPEAT_ERROR, createEquipmentReq.getEquipmentNo());
        }
        Equipment equipment = equipmentServiceConverter.toEquipment(createEquipmentReq);
        equipmentManager.updateEquipment(id, equipment);
    }

    @Override
    public EquipmentVO getEquipment(Long id) {
        Equipment equipment = equipmentManager.getEquipment(id);
        Coordinate coordinate = configurationManager.getCoordinateById(equipment.getCoordinateId());
        EquipmentVO equipmentVO = equipmentServiceConverter.toEquipmentVO(equipment, coordinate);
        //  plc组装
        PLC plCbyId = plcManager.getPLCbyId(equipment.getPlcId());
        equipmentVO.setPlc(plcConverter.toPLCSummaryVO(plCbyId));
        return equipmentVO;
    }

    @Override
    public List<EquipmentVO> getAllEquipment() {
        List<Equipment> equipments = equipmentManager.getAllEquipment();
        if (equipments.isEmpty()) {
            return List.of();
        }
        List<Long> coordinateIds = equipments.stream().map(Equipment::getCoordinateId).toList();
        Map<Long, Coordinate> coordinateMap = configurationManager.getCoordinateMapByIds(coordinateIds);
        //  获取设备对应的PLC信息
        List<Long> plcIds = equipments.stream().map(Equipment::getPlcId).toList();
        Map<Long, PLCSummaryVO> plcSummaryVOMap = plcManager.getPLCSummaryVOMap(plcIds);
        return equipmentServiceConverter.toEquipmentVO(equipments, coordinateMap, plcSummaryVOMap);
    }

    @Override
    public List<EquipmentSummaryDTO> getListByArea(AreaEnum area) {
        List<Equipment> equipments = equipmentManager.getListByArea(area);
        if (equipments.isEmpty()) {
            return List.of();
        }
        // 确保坐标信息已被加载（Manager层已处理）
        return equipmentServiceConverter.toEquipmentSummaryDTOList(equipments);
    }
}
