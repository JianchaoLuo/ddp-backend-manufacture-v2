package cn.hex.ddp.manufacture.application.workstation.converter;

import cn.hex.ddp.manufacture.api.workstation.rest.req.CreateWorkstationReq;
import cn.hex.ddp.manufacture.api.workstation.rest.vo.WorkstationVO;
import cn.hex.ddp.manufacture.application.workstation.dto.WorkstationDTO;
import cn.hex.ddp.manufacture.domain.configuration.model.Coordinate;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
import cn.hex.ddp.manufacture.domain.workstation.model.Workstation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;
@Mapper(componentModel = "spring",imports = {WorkstationStatusEnum.class})
public interface WorkstationAppConverter {
    @Mapping(target = "coordinate", ignore = true)
    WorkstationVO toWorkstationVO(Workstation workstation);

    List<WorkstationVO> toWorkstationVO(List<Workstation> list);

    default List<WorkstationVO> toWorkstationVO(List<Workstation> list, Map<Long, Coordinate> coordinateMapByIds) {
        return list.stream().map(workstation -> {
            WorkstationVO workstationVO = toWorkstationVO(workstation);
            workstationVO.setCoordinate(coordinateMapByIds.get(workstation.getCoordinateId()));
            return workstationVO;
        }).toList();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", expression = "java(WorkstationStatusEnum.FREE)")
    Workstation toWorkstation(CreateWorkstationReq createWorkstationReq);

    @Mapping(target = "id")
    WorkstationDTO toWorkstationDTO(Workstation workstation);

    List<WorkstationDTO> toWorkstationDTOList(List<Workstation> workstations);
}
