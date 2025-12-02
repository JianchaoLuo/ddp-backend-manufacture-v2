package cn.hex.ddp.manufacture.domain.plc.manager;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.plc.rest.req.ListPLCReq;
import cn.hex.ddp.manufacture.api.plc.rest.vo.PLCSummaryVO;
import cn.hex.ddp.manufacture.domain.plc.model.PLC;
import cn.hex.ddp.manufacture.infrastructure.plc.persistence.po.PLCResourcePO;
import cn.hex.ddp.manufacture.domain.plc.model.ResourceSummary;
import cn.hex.ddp.manufacture.domain.plc.model.PlcWithResources;

import java.util.List;
import java.util.Map;

public interface PLCManager {
    PageResult<PLC> listPLC(ListPLCReq listPlcReq);

    void createPLC(PLC plc);

    void updatePLC(Long id, PLC plc);

    void deletePLC(Long id);

    Map<Long, PLCSummaryVO> getPLCSummaryVOMap(List<Long> plcIdList);

    PLC getPLCbyId(Long plcId);

    List<ResourceSummary> getResourcesByPlcId(Long id);

    List<PlcWithResources> listPlcWithResources();

    PageResult<PlcWithResources> listPlcWithResources(ListPLCReq req);

    void insertPLCResource(Long plcId, Long resourceId);

    PLCResourcePO getPLCResourceByPlcId(Long plcId);

    void deletePLCResourcesByPLCId(Long plcId);

    void updatePLCResource(Long plcId, Long resourceId);

}
