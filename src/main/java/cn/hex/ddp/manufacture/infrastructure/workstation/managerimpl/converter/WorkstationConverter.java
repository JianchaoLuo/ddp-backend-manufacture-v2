package cn.hex.ddp.manufacture.infrastructure.workstation.managerimpl.converter;

import cn.hex.ddp.manufacture.domain.workstation.model.Workstation;
import cn.hex.ddp.manufacture.infrastructure.workstation.persistence.po.WorkstationPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkstationConverter {
    List<Workstation> toWorkstation(List<WorkstationPO> workstationPOS);

    Workstation toWorkstation(WorkstationPO workstationPO);

    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    WorkstationPO toWorkstationPO(Workstation workstation);

    List<Workstation> toWorkstationList(List<WorkstationPO> workstationPOS);
}
