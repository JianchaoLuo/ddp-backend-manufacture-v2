package cn.hex.ddp.manufacture.domain.simulator.manager;

import cn.hex.ddp.manufacture.domain.simulator.model.PositionSimulatorData;

import java.util.List;

/**
 * 点位仿真管理器
 * @Author: FengZebang
 * @Date: 2025/3/11
 */
public interface PositonSimulatorManager {
    /**
     * 保存单条点位实时数据
     * @param positionSimulatorData
     */
    void savePositionRealTime(PositionSimulatorData positionSimulatorData);

    /**
     * 批量获取点位实时数据
     * @param mission
     * @param startTick
     * @param endTick
     * @return
     */
    List<PositionSimulatorData> getPositionRealTimes(Long mission, Long startTick, Long endTick);
}
