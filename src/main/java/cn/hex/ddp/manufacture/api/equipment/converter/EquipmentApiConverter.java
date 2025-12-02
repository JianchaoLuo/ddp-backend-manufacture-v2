package cn.hex.ddp.manufacture.api.equipment.converter;

import cn.hex.ddp.manufacture.api.car.rest.vo.CarSummaryVO;
import cn.hex.ddp.manufacture.api.equipment.rest.vo.EquipmentSummaryVO;
import cn.hex.ddp.manufacture.api.equipment.rest.vo.EquipmentVO;
import cn.hex.ddp.manufacture.application.car.dto.out.CarSummaryDTO;
import cn.hex.ddp.manufacture.application.equipment.dto.EquipmentSummaryDTO;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Equipment;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author zhanganqi
 * @date 2025/06/10
 */
@Mapper(componentModel = "spring")
public interface EquipmentApiConverter {
    @BeanMapping(ignoreByDefault = true)
    @Mappings({
            @Mapping(source = "id", target = "id"),
            @Mapping(source = "equipmentNo", target = "equipmentNo"),
            @Mapping(source = "model", target = "model"),
            @Mapping(source = "name", target = "name"),
            @Mapping(source = "operationStatus", target = "operationStatus"),
            @Mapping(source = "type", target = "type"),
            @Mapping(source = "coordinate", target = "coordinate"),
            @Mapping(source = "nameEnum", target = "nameEnum")
    })
    EquipmentSummaryVO toEquipmentSummaryVO(EquipmentSummaryDTO dto);

    List<EquipmentSummaryVO> toEquipmentSummaryVOList(List<EquipmentSummaryDTO> dtos);
}

