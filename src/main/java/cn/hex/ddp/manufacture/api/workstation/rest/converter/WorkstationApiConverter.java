package cn.hex.ddp.manufacture.api.workstation.rest.converter;

import cn.hex.ddp.manufacture.api.workstation.rest.vo.WorkstationVO;
import cn.hex.ddp.manufacture.application.workstation.dto.WorkstationDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkstationApiConverter {
    WorkstationVO toWorkstationVO(WorkstationDTO workstationDTO);

    List<WorkstationVO> toWorkstationVOList(List<WorkstationDTO> workstationDTOS);
}
