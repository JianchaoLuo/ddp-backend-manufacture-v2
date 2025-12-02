package cn.hex.ddp.manufacture.infrastructure.simulator.persistence.managerimpl;

import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.domain.simulator.manager.PositonSimulatorManager;
import cn.hex.ddp.manufacture.domain.simulator.model.PositionSimulatorData;
import cn.hex.ddp.manufacture.infrastructure.common.dynamic.DynamicTableNameContext;
import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.managerimpl.converter.PositionSimulatorInfraConverter;
import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.po.PositionSimulatorDataPO;
import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.postgresql.repository.PositionSimulatorDataPORepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 点位仿真数据管理器实现
 * @Author: FengZebang
 * @Date: 2025/3/11
 */
@Slf4j
@Repository
public class PositonSimulatorManagerImpl implements PositonSimulatorManager {
    @Autowired
    private PositionSimulatorInfraConverter positionSimulatorInfraConverter;
    
    @Autowired
    private PositionSimulatorDataPORepository positionSimulatorDataRepository;
    
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePositionRealTime(PositionSimulatorData positionSimulatorData) {
        if(Optional.ofNullable(positionSimulatorData.getMission()).isEmpty()){
            log.warn("[PositonSimulatorManagerImpl.savePositionRealTime] 任务编号不能为空");
            throw new BusinessException(BssExType.SIMULATOR_MISSION_VALIDATE_ERROR, "任务编号不能为空");
        }
        PositionSimulatorDataPO positionSimulatorDataPO = positionSimulatorInfraConverter.toPositionRealTimePO(positionSimulatorData);
        try {
            DynamicTableNameContext.setMissionId(positionSimulatorDataPO.getMission());
            positionSimulatorDataRepository.save(positionSimulatorDataPO);
        }finally {
            DynamicTableNameContext.clear();
        }
    }

    @Override
    public List<PositionSimulatorData> getPositionRealTimes(Long mission, Long startTick, Long endTick) {
        if (Optional.ofNullable(mission).isEmpty()){
            log.warn("[PositonSimulatorManagerImpl.getPositionRealTimes] 任务编号不能为空");
            throw new BusinessException(BssExType.SIMULATOR_MISSION_VALIDATE_ERROR, "任务编号不能为空");
        }

        if(startTick == null || endTick == null || startTick > endTick){
            log.warn("[PositonSimulatorManagerImpl.getPositionRealTimes] 帧不能为空，且开始帧不能大于结束帧");
            throw new BusinessException(BssExType.TICK_VALIDATE_ERROR, "帧不能为空，且开始帧不能大于结束帧");
        }
        try {
            DynamicTableNameContext.setMissionId(mission);
            List<PositionSimulatorDataPO> positionSimulatorDataPOList = positionSimulatorDataRepository.lambdaQuery()
                    .eq(PositionSimulatorDataPO::getMission, mission)
                    .ge(PositionSimulatorDataPO::getTick, startTick)
                    .le(PositionSimulatorDataPO::getTick, endTick)
                    .list();
            return positionSimulatorInfraConverter.toPositionRealTimeList(positionSimulatorDataPOList);
        }finally {
            DynamicTableNameContext.clear();
        }
    }
}
