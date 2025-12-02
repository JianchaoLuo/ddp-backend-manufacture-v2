package cn.hex.ddp.manufacture.application.workstation.service;


import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.workstation.rest.req.CreateWorkstationReq;
import cn.hex.ddp.manufacture.api.workstation.rest.req.ListWorkstationReq;
import cn.hex.ddp.manufacture.api.workstation.rest.vo.WorkstationVO;
import cn.hex.ddp.manufacture.application.workstation.dto.WorkstationDTO;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface WorkstationService {
    PageResult<WorkstationVO> listWorkstation(ListWorkstationReq listWorkstationReq);

    void addWorkstation(CreateWorkstationReq createWorkstationReq);

    void deleteWorkstation(Long id);

    void updateWorkstation(Long id, CreateWorkstationReq createWorkstationReq);

    List<WorkstationVO> getAllWorkstation();

    List<WorkstationDTO> getListByArea(AreaEnum area);

    WorkstationDTO getWorkstation(Long id);
}
