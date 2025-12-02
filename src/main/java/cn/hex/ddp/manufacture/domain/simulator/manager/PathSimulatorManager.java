package cn.hex.ddp.manufacture.domain.simulator.manager;

import cn.hex.ddp.manufacture.domain.simulator.model.PathSimulatorData;

import java.util.List;

/**
 * 路径仿真管理器接口
 * @Author: FengZebang
 * @Date: 2025/3/12
 */
public interface PathSimulatorManager {
    /**
     * 保存路径实时数据
     * @param pathSimulatorData
     */
    void savePathRealTime(PathSimulatorData pathSimulatorData);

    /**
     * 获取路径实时数据
     * @param mission
     * @param startTick
     * @param endTick
     * @return
     */
    List<PathSimulatorData> getPathRealTimes(Long mission, Long startTick, Long endTick);
}
