package cn.hex.ddp.manufacture.infrastructure.simulator.persistence.managerimpl;
import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.domain.simulator.manager.WorkStationSimilatorManager;
import cn.hex.ddp.manufacture.domain.simulator.model.WorkStationSimulatorData;
import cn.hex.ddp.manufacture.infrastructure.common.dynamic.DynamicTableNameContext;
import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.managerimpl.converter.WorkStationSimulatorInfraConverter;
import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.po.WorkStationSimulatorDataPO;
import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.postgresql.repository.WorkStationSimulatorDataPORepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
/**
 * 工位仿真管理器实现类
 * @Author: FengZebang
 * @Date: 2025/3/5
 */
@Slf4j
@Repository
public class WorkStationSimilatorManagerImpl implements WorkStationSimilatorManager {
    @Autowired
    private WorkStationSimulatorDataPORepository workStationSimulatorDataRepository;

    @Autowired
    private WorkStationSimulatorInfraConverter workStationSimulatorInfraConverter;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveWorkStationRealTime(WorkStationSimulatorData workStationSimulatorData) {
        //校验MissionId不空
        if(Optional.ofNullable(workStationSimulatorData.getMission()).isEmpty()){
            log.warn("[WorkStationSimilatorManagerImpl.saveWorkStationRealTime] 任务编号不能为空");
            throw new BusinessException(BssExType.SIMULATOR_MISSION_VALIDATE_ERROR, "任务编号不能为空");
        }
        WorkStationSimulatorDataPO workStationSimulatorDataPO = workStationSimulatorInfraConverter.toWorkStationRealTimePO(workStationSimulatorData);
        try {
            DynamicTableNameContext.setMissionId(workStationSimulatorDataPO.getMission());
            workStationSimulatorDataRepository.save(workStationSimulatorDataPO);
        }finally {
            DynamicTableNameContext.clear();
        }
    }

    @Override
    public List<WorkStationSimulatorData> getWorkStationRealTimes(Long mission, Long startTick, Long endTick) {
        //校验MissionId不空
        if(Optional.ofNullable(mission).isEmpty()){
            log.warn("[WorkStationSimilatorManagerImpl.saveWorkStationRealTime] 任务编号不能为空");
            throw new BusinessException(BssExType.SIMULATOR_MISSION_VALIDATE_ERROR, "任务编号不能为空");
        }
        //校验查询帧的合法性
        if (startTick == null || endTick == null || startTick > endTick){
            log.warn("[WorkStationSimilatorManagerImpl.getWorkStationRealTimes] 帧不能为空，且开始帧不能大于结束帧");
            throw new BusinessException(BssExType.TICK_VALIDATE_ERROR, "帧不能为空，且开始帧不能大于结束帧");
        }
        try {
            DynamicTableNameContext.setMissionId(mission);
            List<WorkStationSimulatorDataPO> workStationSimulatorDataPOList = workStationSimulatorDataRepository.lambdaQuery()
                    .eq(WorkStationSimulatorDataPO::getMission, mission)
                    .ge(WorkStationSimulatorDataPO::getTick, startTick)
                    .le(WorkStationSimulatorDataPO::getTick, endTick)
                    .list();
            return workStationSimulatorInfraConverter.toWorkStationRealTimeList(workStationSimulatorDataPOList);
        }finally {
            DynamicTableNameContext.clear();
        }
    }
}