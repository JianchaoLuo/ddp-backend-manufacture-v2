package cn.hex.ddp.manufacture.infrastructure.plc.managerimpl.converter;

import cn.hex.ddp.manufacture.api.plc.rest.vo.PLCSummaryVO;
import cn.hex.ddp.manufacture.domain.plc.model.PLC;
import cn.hex.ddp.manufacture.infrastructure.plc.persistence.po.PLCPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PLCInfrastructureConverter {


    PLCPO toPLCPO(PLC plc);

    @Mapping(target = "equipmentIdList", ignore = true)
    List<PLC> poToPLC(List<PLCPO> records);

    PLCSummaryVO toPLCSummaryVO(PLCPO plcpo);

    PLC poToPLC(PLCPO plcPO);
}
