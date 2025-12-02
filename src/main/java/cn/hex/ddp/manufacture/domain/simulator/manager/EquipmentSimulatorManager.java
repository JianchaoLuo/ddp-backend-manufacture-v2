package cn.hex.ddp.manufacture.domain.simulator.manager;

import cn.hex.ddp.manufacture.domain.simulator.model.EquipmentSimulatorData;

import java.util.List;

/**
 * 设备仿真管理器
 * @Author: FengZebang
 * @Date: 2025/3/10
 */
public interface EquipmentSimulatorManager {
    /**
     * 保存单条设备实时数据
     * @param equipmentSimulatorData
     */
    void saveEquipmentRealTime(EquipmentSimulatorData equipmentSimulatorData);

    /**
     * 获取设备实时数据
     * @param mission
     * @param startTick
     * @param endTick
     * @return
     */
    List<EquipmentSimulatorData> getEquipmentRealTimes(Long mission, Long startTick, Long endTick);
}
