package cn.hex.ddp.manufacture.domain.simulator.manager;
import cn.hex.ddp.manufacture.domain.simulator.model.WorkStationSimulatorData;
import java.util.List;
/**
 * 工位仿真管理器接口
 * @Author: FengZebang
 * @Date: 2025/3/5
 */
public interface WorkStationSimilatorManager {
    /**
     * 保存单挑工位仿真数据
     * @param workStationSimulatorData
     */
    void saveWorkStationRealTime(WorkStationSimulatorData workStationSimulatorData);
    List<WorkStationSimulatorData> getWorkStationRealTimes(Long mission, Long startTick, Long endTick);
}