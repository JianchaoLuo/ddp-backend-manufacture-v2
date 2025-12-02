package cn.hex.ddp.manufacture.application.plc.converter;

import cn.hex.ddp.manufacture.api.plc.rest.req.UpdatePLCReq;
import cn.hex.ddp.manufacture.api.plc.rest.vo.PLCSummaryVO;
import cn.hex.ddp.manufacture.api.plc.rest.vo.PLCVO;
import cn.hex.ddp.manufacture.api.plc.rest.vo.PLCResourceVO;
import cn.hex.ddp.manufacture.api.plc.rest.vo.PLCWithResourcesVO;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.equipment.model.Equipment;
import cn.hex.ddp.manufacture.domain.plc.enums.PLCTypeEnum;
import cn.hex.ddp.manufacture.domain.plc.model.PLC;
import cn.hex.ddp.manufacture.domain.plc.model.ResourceSummary;
import cn.hex.ddp.manufacture.domain.plc.model.PlcWithResources;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = {PLCTypeEnum.class, AreaEnum.class})
public interface PLCServiceConverter {

    @Mapping(target = "id", ignore = true)
    PLC toPLC(UpdatePLCReq updatePLCReq);

    PLCVO toPLCVO(PLC plc);

    default List<PLCVO> toPLCVO(List<PLC> list, Map<Long, List<Equipment>> equipmentsMap) {
        return list.stream().map(plc -> {
            PLCVO plcVO = toPLCVO(plc);
            plcVO.setEquipmentList(equipmentsMap.getOrDefault(plc.getId(), List.of()));
            return plcVO;
        }).toList();
    }

    PLCSummaryVO toPLCSummaryVO(PLC plc);

    default PLCResourceVO toPLCResourceVO(ResourceSummary summary) {
        if (summary == null) return null;
        PLCResourceVO vo = new PLCResourceVO();
        vo.setResourceId(summary.getResourceId());
        vo.setResourceType(summary.getResourceType());
        vo.setResourceName(summary.getResourceName());
        return vo;
    }

    default List<PLCResourceVO> toPLCResourceVOList(List<ResourceSummary> list) {
        return list == null ? List.of() : list.stream().map(this::toPLCResourceVO).collect(Collectors.toList());
    }

    default List<PLCWithResourcesVO> toPLCWithResourcesVOList(Map<Long, List<ResourceSummary>> grouped) {
        if (grouped == null || grouped.isEmpty()) return List.of();
        return grouped.entrySet().stream().map(e -> {
            PLCWithResourcesVO vo = new PLCWithResourcesVO();
            vo.setId(e.getKey());
            vo.setResources(toPLCResourceVOList(e.getValue()));
            return vo;
        }).collect(Collectors.toList());
    }

    default PLCWithResourcesVO toPLCWithResourcesVO(PlcWithResources agg) {
        if (agg == null) return null;
        PLCWithResourcesVO vo = new PLCWithResourcesVO();
        vo.setId(agg.getId());
        vo.setIp(agg.getIp());
        vo.setType(agg.getType());
        vo.setArea(agg.getArea());
        vo.setResources(toPLCResourceVOList(agg.getResources()));
        return vo;
    }

    default List<PLCWithResourcesVO> toPLCWithResourcesVOList(List<PlcWithResources> list) {
        return list == null ? List.of() : list.stream().map(this::toPLCWithResourcesVO).collect(Collectors.toList());
    }
}
