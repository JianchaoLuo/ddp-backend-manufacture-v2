package cn.hex.ddp.manufacture.infrastructure.simulator.persistence.managerimpl;

import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.domain.simulator.manager.CarSimulatorManager;
import cn.hex.ddp.manufacture.domain.simulator.model.CarSimulatorData;
import cn.hex.ddp.manufacture.infrastructure.common.dynamic.DynamicTableNameContext;
import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.managerimpl.converter.CarSimulatorInfraConverter;
import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.po.CarSimulatorDataPO;
import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.postgresql.repository.CarSimulatorDataPORepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 车辆仿真管理器实现类
 *
 * @author Huhaisen
 * @date 2024/12/26
 */
@Slf4j
@Repository
public class CarSimulatorManagerImpl implements CarSimulatorManager {

    @Autowired
    private CarSimulatorDataPORepository carSimulatorDataPORepository;

    @Autowired
    private CarSimulatorInfraConverter carSimulatorInfraConverter;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCarRealTime(CarSimulatorData carSimulatorData) {
        // 校验missionId不能为空
        if (carSimulatorData.getMission() == null) {
            log.warn("[CarSimulatorManagerImpl.saveCarRealTime] 任务编号不能为空");
            throw new BusinessException(BssExType.SIMULATOR_MISSION_VALIDATE_ERROR, "任务编号不能为空");
        }

        CarSimulatorDataPO carSimulatorDataPO = carSimulatorInfraConverter.toCarRealTimePO(carSimulatorData);
        try {
            // 设置全局的 missionId
            DynamicTableNameContext.setMissionId(carSimulatorDataPO.getMission());
            // 保存车辆仿真数据
            carSimulatorDataPORepository.save(carSimulatorDataPO);
        } finally {
            // 清理上下文
            DynamicTableNameContext.clear();
        }
    }

    @Override
    public List<CarSimulatorData> getCarRealTimes(Long mission, Long startTick, Long endTick) {
        // 校验missionId不能为空
        if (mission == null) {
            log.warn("[CarSimulatorManagerImpl.getCarRealTimes] 任务编号不能为空");
            throw new BusinessException(BssExType.SIMULATOR_MISSION_VALIDATE_ERROR, "任务编号不能为空");
        }
        // 校验tick范围是否合法
        if (startTick == null || endTick == null || startTick > endTick) {
            log.warn("[CarSimulatorManagerImpl.getCarRealTimes] 帧不能为空，且开始帧不能大于结束帧");
            throw new BusinessException(BssExType.TICK_VALIDATE_ERROR, "帧不能为空，且开始帧不能大于结束帧");
        }

        try {
            // 设置全局的 missionId
            DynamicTableNameContext.setMissionId(mission);
            // 查询车辆仿真数据
            List<CarSimulatorDataPO> carSimulatorDataPOList = carSimulatorDataPORepository.lambdaQuery()
                    .eq(CarSimulatorDataPO::getMission, mission)
                    .ge(CarSimulatorDataPO::getTick, startTick)
                    .le(CarSimulatorDataPO::getTick, endTick)
                    .list();
            // 转换为实体类
            return carSimulatorInfraConverter.toCarRealTimeList(carSimulatorDataPOList);
        } finally {
            // 清理上下文
            DynamicTableNameContext.clear();
        }
    }
}
