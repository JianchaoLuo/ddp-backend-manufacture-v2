package cn.hex.ddp.manufacture.application.equipment.converter;

import cn.hex.ddp.manufacture.api.common.config.JacksonConfig;
import cn.hex.ddp.manufacture.api.equipment.rest.req.CreateEquipmentParameterReq;
import cn.hex.ddp.manufacture.api.equipment.rest.req.CreateEquipmentReq;
import cn.hex.ddp.manufacture.api.equipment.rest.vo.EquipmentVO;
import cn.hex.ddp.manufacture.api.plc.rest.vo.PLCSummaryVO;
import cn.hex.ddp.manufacture.application.equipment.dto.EquipmentSummaryDTO;
import cn.hex.ddp.manufacture.domain.configuration.model.Coordinate;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum;
import cn.hex.ddp.manufacture.domain.equipment.model.Equipment;
import cn.hex.ddp.manufacture.domain.equipment.model.EquipmentParameter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;

/**
 * @author zhaolin
 * @date 2024/5/16
 */
@Mapper(componentModel = "spring", imports = {EquipmentOperationStatusEnum.class, JacksonConfig.class})
public interface EquipmentServiceConverter {


    @Mapping(target = "coordinate", ignore = true)
    @Mapping(target = "plc", ignore = true)
    EquipmentVO toEquipmentVO(Equipment equipment);

    @Mapping(target = "name", source = "equipment.name")
    @Mapping(target = "id", source = "equipment.id")
    @Mapping(target = "plc", ignore = true)
    EquipmentVO toEquipmentVO(Equipment equipment, Coordinate coordinate);

    default List<EquipmentVO> toEquipmentVO(List<Equipment> list, Map<Long, Coordinate> coordinateMap,
                                            Map<Long, PLCSummaryVO> plcSummaryVOMap) {
        return list.stream().map(equipment -> {
            EquipmentVO equipmentVO = toEquipmentVO(equipment);
            equipmentVO.setCoordinate(coordinateMap.get(equipment.getCoordinateId()));
            equipmentVO.setPlc(plcSummaryVOMap.get(equipment.getPlcId()));
            return equipmentVO;
        }).toList();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "operationStatus", expression = "java(EquipmentOperationStatusEnum.STANDBY)")
    @Mapping(target = "displayStyle", expression = "java(JacksonConfig.objToString(createEquipmentReq.getDisplayStyle" +
            "()))")
    Equipment toEquipment(CreateEquipmentReq createEquipmentReq);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "equipmentId", ignore = true)
    EquipmentParameter toEquipmentParameter(CreateEquipmentParameterReq createEquipmentParameterReq);

    EquipmentSummaryDTO toEquipmentSummaryDTO(Equipment equipment);

    List<EquipmentSummaryDTO> toEquipmentSummaryDTOList(List<Equipment> equipments);
}
