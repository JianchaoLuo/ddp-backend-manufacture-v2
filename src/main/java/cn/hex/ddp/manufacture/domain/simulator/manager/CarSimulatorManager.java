package cn.hex.ddp.manufacture.domain.simulator.manager;

import cn.hex.ddp.manufacture.domain.simulator.model.CarSimulatorData;

import java.util.List;

/**
 * 车辆仿真管理器接口
 *
 * @author Huhaisen
 * @date 2024/12/26
 */
public interface CarSimulatorManager {
    /**
     * 保存单条车辆仿真数据
     *
     * @param carSimulatorData 车辆仿真数据
     */
    void saveCarRealTime(CarSimulatorData carSimulatorData);

    /**
     * 获取多条车辆仿真数据
     *
     * @param mission 任务任务编号
     * @param startTick 开始帧
     * @param endTick 结束帧
     * @return {@link List }<{@link CarSimulatorData }>
     */
    List<CarSimulatorData> getCarRealTimes(Long mission, Long startTick, Long endTick);
}
