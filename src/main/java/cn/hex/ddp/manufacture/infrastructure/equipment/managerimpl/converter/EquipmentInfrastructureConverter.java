package cn.hex.ddp.manufacture.infrastructure.equipment.managerimpl.converter;

import cn.hex.ddp.manufacture.domain.equipment.model.Equipment;
import cn.hex.ddp.manufacture.domain.equipment.model.EquipmentParameter;
import cn.hex.ddp.manufacture.infrastructure.equipment.persistence.po.EquipmentPO;
import cn.hex.ddp.manufacture.infrastructure.equipment.persistence.po.EquipmentParameterPO;
import cn.hutool.core.collection.CollUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author zhaolin
 * @date 2024/5/16
 */
@Mapper(componentModel = "spring")
public interface EquipmentInfrastructureConverter {
    // 添加坐标ID到坐标对象的映射
    @Mapping(target = "coordinate", ignore = true) // 忽略坐标对象，由上层处理
    @Mapping(source = "coordinateId", target = "coordinateId") // 保留坐标ID
    @Mapping(target = "parameters", ignore = true)
    Equipment toEquipment(EquipmentPO equipment);

    Equipment toEquipment(EquipmentPO equipment, List<EquipmentParameterPO> parameters);

    List<Equipment> toEquipments(List<EquipmentPO> equipmentPOS);

    EquipmentParameter toEquipmentParameter(EquipmentParameterPO equipmentParameter);

    List<EquipmentParameter> toEquipmentParameter(List<EquipmentParameterPO> equipmentParameterPOs);

    default List<Equipment> toEquipment(List<EquipmentPO> records,
                                        Map<Long, List<EquipmentParameterPO>> equipmentParameterMap) {
        return records.stream().map(equipmentPO -> {
            Equipment equipment = toEquipment(equipmentPO);
            equipment.setParameters(toEquipmentParameter(equipmentParameterMap.get(equipmentPO.getId())));
            return equipment;
        }).toList();
    }

    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    EquipmentPO toEquipmentPO(Equipment equipment);

    default List<EquipmentParameterPO> toEquipmentParameterPO(List<EquipmentParameter> equipmentParameterReq,
                                                              Long equipmentId) {
        if (CollUtil.isEmpty(equipmentParameterReq)) {
            return Collections.emptyList();
        }
        return equipmentParameterReq.stream().map(equipmentParameter -> {
            EquipmentParameterPO equipmentParameterPO = toEquipmentParameterPO(equipmentParameter);
            equipmentParameterPO.setEquipmentId(equipmentId);
            return equipmentParameterPO;
        }).toList();
    }

    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "equipmentId", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    EquipmentParameterPO toEquipmentParameterPO(EquipmentParameter equipmentParameterReq);
}
