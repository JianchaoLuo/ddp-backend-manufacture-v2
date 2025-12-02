package cn.hex.ddp.manufacture.domain.workstation.manager;


import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.workstation.rest.req.ListWorkstationReq;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.workstation.model.Workstation;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

public interface WorkstationManager {
    PageResult<Workstation> listWorkstation(ListWorkstationReq listWorkstationReq);

    void addWorkstation(Workstation workstation);

    void deleteWorkstation(Long id);

    void updateWorkstation(Workstation workstation);

    Map<Long, Workstation> listWorkstationMapByIds(List<Long> workstationIds);

    Workstation getWorkstationById(Long workstationId);

    List<Workstation> getAllWorkstation();

    Workstation getWorkstationByNo(String stationNo);

    List<Workstation> getListByArea(AreaEnum area);

    List<Workstation> getNearWorkStation(@NotNull(message = "横坐标不能为空") Float x, @NotNull(message = "纵坐标不能为空") Float y);
}
