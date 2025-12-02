package cn.hex.ddp.manufacture.application.workstation.service.impl;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.workstation.rest.req.CreateWorkstationReq;
import cn.hex.ddp.manufacture.api.workstation.rest.req.ListWorkstationReq;
import cn.hex.ddp.manufacture.api.workstation.rest.vo.WorkstationVO;
import cn.hex.ddp.manufacture.application.workstation.converter.WorkstationAppConverter;
import cn.hex.ddp.manufacture.application.workstation.dto.WorkstationDTO;
import cn.hex.ddp.manufacture.application.workstation.service.WorkstationService;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.domain.common.exception.NotFoundException;
import cn.hex.ddp.manufacture.domain.configuration.manager.ConfigurationManager;
import cn.hex.ddp.manufacture.domain.configuration.model.Coordinate;
import cn.hex.ddp.manufacture.domain.workstation.manager.WorkstationManager;
import cn.hex.ddp.manufacture.domain.workstation.model.Workstation;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class WorkstationServiceImpl implements WorkstationService {

    @Autowired
    private WorkstationManager workstationManager;
    @Autowired
    private ConfigurationManager configurationManager;
    @Autowired
    private WorkstationAppConverter workstationAppConverter;

    @Override
    public PageResult<WorkstationVO> listWorkstation(ListWorkstationReq listWorkstationReq) {
        PageResult<Workstation> results = workstationManager.listWorkstation(listWorkstationReq);
        if (results.isEmpty()) {
            return PageResult.emptyResult();
        }
        Map<Long, Coordinate> coordinateMapByIds =
                configurationManager.getCoordinateMapByIds(
                        results.getList()
                                .stream()
                                .map(Workstation::getCoordinateId)
                                .toList());
        return PageResult.of(results, workstationAppConverter.toWorkstationVO(results.getList(), coordinateMapByIds));
    }

    @Override
    public void addWorkstation(CreateWorkstationReq createWorkstationReq) {
        if (StringUtils.isNotBlank(createWorkstationReq.getStationNo())) {
            Workstation workstationByNo = workstationManager.getWorkstationByNo(createWorkstationReq.getStationNo());
            if (workstationByNo!= null) {
                throw new BusinessException(BssExType.WORKSTATION_ALREADY_EXISTS, "工位编号已存在");
            }
        }

        Workstation workstation = workstationAppConverter.toWorkstation(createWorkstationReq);
        workstationManager.addWorkstation(workstation);
    }

    @Override
    public void deleteWorkstation(Long id) {
        workstationManager.deleteWorkstation(id);
    }

    @Override
    public void updateWorkstation(Long id, CreateWorkstationReq createWorkstationReq) {
        if (StringUtils.isNotBlank(createWorkstationReq.getStationNo())) {
            Workstation workstationByNo = workstationManager.getWorkstationByNo(createWorkstationReq.getStationNo());
            if (workstationByNo!= null && !workstationByNo.getStationNo().equals(createWorkstationReq.getStationNo())) {
                throw new BusinessException(BssExType.WORKSTATION_ALREADY_EXISTS, "工位编号已存在");
            }
        }

        Workstation workstation = workstationAppConverter.toWorkstation(createWorkstationReq);
        workstation.setId(id);
        workstationManager.updateWorkstation(workstation);
    }

    @Override
    public List<WorkstationVO> getAllWorkstation() {
        List<Workstation> workstations = workstationManager.getAllWorkstation();
        if (workstations.isEmpty()) {
            return List.of();
        }
        Map<Long, Coordinate> coordinateMapByIds =
                configurationManager.getCoordinateMapByIds(
                        workstations.stream()
                                .map(Workstation::getCoordinateId)
                                .toList());
        return workstationAppConverter.toWorkstationVO(workstations, coordinateMapByIds);
    }
    @Override
    public List<WorkstationDTO> getListByArea(AreaEnum area) {
        List<Workstation> workstations = workstationManager.getListByArea(area);
        if (workstations.isEmpty()) {
            return List.of();
        }
        return workstationAppConverter.toWorkstationDTOList(workstations);
    }

    @Override
    public WorkstationDTO getWorkstation(Long id) {
        Workstation workstation = workstationManager.getWorkstationById(id);

        if(workstation == null){
            log.warn("[WorkstationServiceImpl.getWorkstation] 工位不存在，id={}", id);
            throw new NotFoundException("工位不存在");
        }

        return workstationAppConverter.toWorkstationDTO(workstation);
    }
}
