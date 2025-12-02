package cn.hex.ddp.manufacture.application.plc.service;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.plc.rest.req.ListPLCReq;
import cn.hex.ddp.manufacture.api.plc.rest.req.UpdatePLCReq;
import cn.hex.ddp.manufacture.api.plc.rest.vo.PLCResourceVO;
import cn.hex.ddp.manufacture.api.plc.rest.vo.PLCVO;
import cn.hex.ddp.manufacture.api.plc.rest.vo.PLCWithResourcesVO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface PLCService {

    PageResult<PLCVO> listPLC(ListPLCReq listPlcReq);

    void createPLC(UpdatePLCReq updatePLCReq);

    void updatePLC(Long id, UpdatePLCReq updatePLCReq);

    void deletePLC(Long id);

    void addPLCResource(Long PLCId, Long resourceId);

    List<PLCResourceVO> getResourcesByPlcId(Long id);

    List<PLCWithResourcesVO> listAllPlcWithResources();

    PageResult<PLCWithResourcesVO> pagePlcWithResources(ListPLCReq req);

    void deletePLCResourcesByPLCId(Long PLCId);

    void updatePLCResource(Long plcId, @NotNull Long resourceId);
}
