package cn.hex.ddp.manufacture.infrastructure.simulator.persistence.managerimpl.converter;

import cn.hex.ddp.manufacture.domain.simulator.model.EquipmentSimulatorData;
import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.po.EquipmentSimulatorPO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 设备仿真数据转换器
 * @Author: FengZebang
 * @Date: 2025/3/10
 */
@Mapper(componentModel = "spring")
public interface EquipmentSimulatorInfraConverter {
    EquipmentSimulatorPO toEquipmentRealTimePO(EquipmentSimulatorData equipmentSimulatorData);

    List<EquipmentSimulatorData> toEquipmentRealTimeList(List<EquipmentSimulatorPO> equipmentSimulatorPOList);
}
