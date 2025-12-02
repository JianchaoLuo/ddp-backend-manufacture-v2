package cn.hex.ddp.manufacture.application.plc.service.impl;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.plc.rest.req.ListPLCReq;
import cn.hex.ddp.manufacture.api.plc.rest.req.UpdatePLCReq;
import cn.hex.ddp.manufacture.api.plc.rest.vo.PLCResourceVO;
import cn.hex.ddp.manufacture.api.plc.rest.vo.PLCVO;
import cn.hex.ddp.manufacture.api.plc.rest.vo.PLCWithResourcesVO;
import cn.hex.ddp.manufacture.application.plc.converter.PLCServiceConverter;
import cn.hex.ddp.manufacture.application.plc.service.PLCService;
import cn.hex.ddp.manufacture.domain.car.manager.CarManager;
import cn.hex.ddp.manufacture.domain.car.model.Car;
import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.domain.common.exception.NotFoundException;
import cn.hex.ddp.manufacture.domain.equipment.manager.EquipmentManager;
import cn.hex.ddp.manufacture.domain.equipment.model.Equipment;
import cn.hex.ddp.manufacture.domain.plc.manager.PLCManager;
import cn.hex.ddp.manufacture.domain.plc.model.PLC;
import cn.hex.ddp.manufacture.domain.plc.model.PlcWithResources;
import cn.hex.ddp.manufacture.domain.plc.model.ResourceSummary;
import cn.hex.ddp.manufacture.domain.workstation.manager.WorkstationManager;
import cn.hex.ddp.manufacture.domain.workstation.model.Workstation;
import cn.hex.ddp.manufacture.infrastructure.plc.persistence.po.PLCResourcePO;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class PLCServiceImpl implements PLCService {

    @Autowired
    private PLCManager plcManager;

    @Autowired
    private EquipmentManager equipmentManager;

    @Autowired
    private CarManager carManager;

    @Autowired
    private WorkstationManager workstationManager;

    @Autowired
    private PLCServiceConverter plcServiceConverter;

    @Override
    public PageResult<PLCVO> listPLC(ListPLCReq listPlcReq) {
        PageResult<PLC> plcs = plcManager.listPLC(listPlcReq);
        if (plcs.isEmpty()) {
            return PageResult.emptyResult();
        }
        List<Long> plcIds = plcs.getList().stream().map(PLC::getId).toList();
        Map<Long, List<Equipment>> equipmentsMap =  equipmentManager.getEquipmentListByPLCIds(plcIds);
        return PageResult.of(plcs,plcServiceConverter.toPLCVO(plcs.getList(), equipmentsMap));
    }

    @Override
    public void createPLC(UpdatePLCReq updatePLCReq) {
        //插入PLC表中
        PLC plc = plcServiceConverter.toPLC(updatePLCReq);
        plcManager.createPLC(plc);
        //判断是否同时传入需要绑定的资源ID
        if (updatePLCReq.getResourceId() != null) {
            //校验资源ID对应资源是否存在
            Long resourceId = updatePLCReq.getResourceId();
            switch (updatePLCReq.getType()){
                case EQUIPMENT_PLC :
                    Equipment equipment = equipmentManager.getEquipment(resourceId);
                    if(equipment.getId() == null){
                        log.warn("[PLCServiceImpl.updatePLCResource] 资源不存在，resourceId={}", resourceId);
                        throw new NotFoundException("资源不存在");
                    }
                    break;
                case CAR_PLC:
                    Car car = carManager.getCarById(resourceId);
                    if(car.getId() == null){
                        log.warn("[PLCServiceImpl.updatePLCResource] 车辆不存在，resourceId={}", resourceId);
                        throw new NotFoundException("车辆不存在");
                    }
                    break;
                case WORKSTATION_PLC:
                    Workstation workstation = workstationManager.getWorkstationById(resourceId);
                    if(workstation.getId() == null){
                        log.warn("[PLCServiceImpl.updatePLCResource] 工位不存在，resourceId={}", resourceId);
                        throw new NotFoundException("工位不存在");
                    }
                    break;
                default:
                    log.warn("资源类型无效，resourceId={}", resourceId);
                    throw new BusinessException(BssExType.INVALID_RESOURCE_TYPE);
            }
            //插入PLC资源表中
            plcManager.insertPLCResource(plc.getId(), resourceId);
        }
    }

    @Override
    public void updatePLC(Long id, UpdatePLCReq updatePLCReq) {
        PLC plc = plcServiceConverter.toPLC(updatePLCReq);
        plcManager.updatePLC(id, plc);
    }

    @Override
    public void deletePLC(Long id) {
        PLC plc = plcManager.getPLCbyId(id);
        if (plc == null) {
            log.warn("[PLCServiceImpl.deletePLC] PLC不存在，id={}", id);
            throw new NotFoundException("PLC不存在");
        }
        if(CollectionUtils.isNotEmpty(equipmentManager.getEquipmentsByPlcId(id))){
            log.warn("[PLCServiceImpl.deletePLC] PLC被占用");
            throw new BusinessException(BssExType.PLC_BE_OCCUPIED);
        }
        plcManager.deletePLC(id);
    }

    @Override
    public void addPLCResource(Long PLCId, Long resourceId) {
        //校验PLCId是否存在
        PLC plc = plcManager.getPLCbyId(PLCId);
        if(Optional.ofNullable(plc).isEmpty()){
            log.warn("[PLCServiceImpl.addPLCResource] PLC不存在，PLCId={}", PLCId);
            throw new NotFoundException("PLC不存在");
        }
        //根据plc类型校验资源Id是否存在
        boolean isResourceIdExist = false;
        switch (plc.getType()){
            case EQUIPMENT_PLC :
                Equipment equipment = equipmentManager.getEquipment(resourceId);
                if(equipment.getId() == null){
                    log.warn("[PLCServiceImpl.addPLCResource] 资源不存在，resourceId={}", resourceId);
                    throw new NotFoundException("资源不存在");
                }
                isResourceIdExist = true;
                break;
            case CAR_PLC:
                Car car = carManager.getCarById(resourceId);
                if(car.getId() == null){
                    log.warn("[PLCServiceImpl.addPLCResource] 车辆不存在，resourceId={}", resourceId);
                    throw new NotFoundException("车辆不存在");
                }
                isResourceIdExist = true;
                break;
            case WORKSTATION_PLC:
                Workstation workstation = workstationManager.getWorkstationById(resourceId);
                if(workstation.getId() == null){
                    log.warn("[PLCServiceImpl.addPLCResource] 工位不存在，resourceId={}", resourceId);
                    throw new NotFoundException("工位不存在");
                }
                isResourceIdExist = true;
                break;
            default:
                log.warn("资源类型无效，resourceId={}", PLCId);
                throw new BusinessException(BssExType.INVALID_RESOURCE_TYPE);
        }
        //插入关联表
        if(isResourceIdExist){
            plcManager.insertPLCResource(PLCId, resourceId);
        }
    }

    @Override
    public void updatePLCResource(Long plcId, Long resourceId) {

        //检查输入的plc类型
        PLC plc = plcManager.getPLCbyId(plcId);
        if(Optional.ofNullable(plc).isEmpty()){
            log.warn("[PLCServiceImpl.updatePLCResource] PLC不存在，plcId={}", plcId);
            throw new NotFoundException("PLC不存在");
        }

        //检查plc_resource表中是否存在该plcId的记录
        PLCResourcePO plcResourcePO = plcManager.getPLCResourceByPlcId(plcId);
        if(Optional.ofNullable(plcResourcePO).isEmpty()){
            log.warn("[PLCServiceImpl.updatePLCResource] PLC资源关联记录不存在，plcId={}", plcId);
            throw new NotFoundException("PLC资源关联记录不存在");
        }

        //根据plc类型校验资源Id是否存在
        switch (plc.getType()){
            case EQUIPMENT_PLC :
                Equipment equipment = equipmentManager.getEquipment(resourceId);
                if(equipment.getId() == null){
                    log.warn("[PLCServiceImpl.updatePLCResource] 资源不存在，resourceId={}", resourceId);
                    throw new NotFoundException("资源不存在");
                }
                break;
            case CAR_PLC:
                Car car = carManager.getCarById(resourceId);
                if(car.getId() == null){
                    log.warn("[PLCServiceImpl.updatePLCResource] 车辆不存在，resourceId={}", resourceId);
                    throw new NotFoundException("车辆不存在");
                }
                break;
            case WORKSTATION_PLC:
                Workstation workstation = workstationManager.getWorkstationById(resourceId);
                if(workstation.getId() == null){
                    log.warn("[PLCServiceImpl.updatePLCResource] 工位不存在，resourceId={}", resourceId);
                    throw new NotFoundException("工位不存在");
                }
                break;
            default:
                log.warn("资源类型无效，resourceId={}", resourceId);
                throw new BusinessException(BssExType.INVALID_RESOURCE_TYPE);
        }

        //修改关联表
        plcManager.updatePLCResource(plcId, resourceId);
    }

    // 按 PLCID 获取资源
    @Override
    @Transactional(readOnly = true)
    public List<PLCResourceVO> getResourcesByPlcId(Long id) {
        List<ResourceSummary> list = plcManager.getResourcesByPlcId(id);
        return plcServiceConverter.toPLCResourceVOList(list);
    }

    // 获取所有 PLC 及其关联资源
    @Override
    @Transactional(readOnly = true)
    public List<PLCWithResourcesVO> listAllPlcWithResources() {
        // Map<Long, List<ResourceSummary>> grouped = plcManager.getResourcesGroupedByPlc();
        // return plcServiceConverter.toPLCWithResourcesVOList(grouped);

        List<PlcWithResources> aggs = plcManager.listPlcWithResources();
        return plcServiceConverter.toPLCWithResourcesVOList(aggs);
    }

    // 新增：分页查询 PLC + 资源聚合
    @Override
    @Transactional(readOnly = true)
    public PageResult<PLCWithResourcesVO> pagePlcWithResources(ListPLCReq req) {
        PageResult<PlcWithResources> pageAgg = plcManager.listPlcWithResources(req);
        if (pageAgg.isEmpty()) {
            return PageResult.emptyResult();
        }
        List<PLCWithResourcesVO> voList = plcServiceConverter.toPLCWithResourcesVOList(pageAgg.getList());
        return PageResult.of(pageAgg, voList);
    }

    // 删除 PLC 关联资源
    @Override
    public void deletePLCResourcesByPLCId(Long plcId) {
        //校验PLCId是否存在
        PLC plc = plcManager.getPLCbyId(plcId);
        if(Optional.ofNullable(plc).isEmpty()){
            log.warn("[PLCServiceImpl.deletePLCResourcesByPLCId] PLC不存在，PLCId={}", plcId);
            throw new NotFoundException("PLC不存在");
        }
        //检查PLC资源关联记录是否存在
        PLCResourcePO plcResourcePO = plcManager.getPLCResourceByPlcId(plcId);
        if(Optional.ofNullable(plcResourcePO).isEmpty()){
            log.warn("[PLCServiceImpl.deletePLCResourcesByPLCId] PLC资源关联记录不存在，PLCId={}", plcId);
            throw new NotFoundException("PLC资源关联记录不存在");
        }
        // 删除关联关系
        plcManager.deletePLCResourcesByPLCId(plcId);
    }

}
