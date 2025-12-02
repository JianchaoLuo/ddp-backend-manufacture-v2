package cn.hex.ddp.manufacture.infrastructure.simulator.persistence.managerimpl;

import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.domain.simulator.manager.PathSimulatorManager;
import cn.hex.ddp.manufacture.domain.simulator.model.PathSimulatorData;
import cn.hex.ddp.manufacture.infrastructure.common.dynamic.DynamicTableNameContext;
import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.managerimpl.converter.PathSimulatorInfraConverter;
import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.po.PathSimulatorDataPO;
import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.postgresql.repository.PathSimulatorDataPORepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 路径仿真管理器实现类
 * @Author: FengZebang
 * @Date: 2025/3/12
 */

@Slf4j
@Repository
public class PathSimulatorManagerImpl implements PathSimulatorManager {

    @Autowired
    private PathSimulatorDataPORepository pathSimulatorDataPORepository;

    @Autowired
    private PathSimulatorInfraConverter pathSimulatorInfraConverter;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePathRealTime(PathSimulatorData pathSimulatorData) {
        //校验missionid
        if(Optional.ofNullable(pathSimulatorData.getMission()).isEmpty()){
            log.warn("[PathSimulatorManagerImpl.savePathRealTime]任务编号不能为空");
            throw new BusinessException(BssExType.SIMULATOR_MISSION_VALIDATE_ERROR, "任务编号不能为空");
        }
        PathSimulatorDataPO pathSimulatorDataPO = pathSimulatorInfraConverter.toPathSimulatorDataPO(pathSimulatorData);
        try {
            DynamicTableNameContext.setMissionId(pathSimulatorDataPO.getMission());
            pathSimulatorDataPORepository.save(pathSimulatorDataPO);
        }finally {
            DynamicTableNameContext.clear();
        }
    }

    @Override
    public List<PathSimulatorData> getPathRealTimes(Long mission, Long startTick, Long endTick) {
        if(Optional.ofNullable(mission).isEmpty()){
            log.warn("[PathSimulatorManagerImpl.getPathRealTimes]任务编号不能为空");
            throw new BusinessException(BssExType.SIMULATOR_MISSION_VALIDATE_ERROR, "任务编号不能为空");
        }

        if(startTick == null || endTick == null || startTick > endTick){
            log.warn("[PathSimulatorManagerImpl.getPathRealTimes]帧不能为空，且开始帧不能大于结束帧");
            throw new BusinessException(BssExType.TICK_VALIDATE_ERROR, "帧不能为空，且开始帧不能大于结束帧");
        }
        try {
            DynamicTableNameContext.setMissionId(mission);
            List<PathSimulatorDataPO> pathSimulatorDataPOList = pathSimulatorDataPORepository.lambdaQuery()
                    .eq(PathSimulatorDataPO::getMission, mission)
                    .ge(PathSimulatorDataPO::getTick, startTick)
                    .le(PathSimulatorDataPO::getTick, endTick)
                    .list();
            return pathSimulatorInfraConverter.toPathSimulatorDataList(pathSimulatorDataPOList);
        }finally {
            DynamicTableNameContext.clear();
        }
    }
}
