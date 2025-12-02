package cn.hex.ddp.manufacture.infrastructure.simulator.persistence.managerimpl;

import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.domain.simulator.manager.EquipmentSimulatorManager;
import cn.hex.ddp.manufacture.domain.simulator.model.EquipmentSimulatorData;
import cn.hex.ddp.manufacture.infrastructure.common.dynamic.DynamicTableNameContext;
import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.managerimpl.converter.EquipmentSimulatorInfraConverter;
import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.po.EquipmentSimulatorPO;

import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.postgresql.repository.EquipmentSimulatorPORepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 设备仿真数据管理器实现
 * @Author: FengZebang
 * @Date: 2025/3/10
 */
@Slf4j
@Repository
public class EquipmentSimulatorManagerImpl implements EquipmentSimulatorManager {
    @Autowired
    private EquipmentSimulatorPORepository equipmentSimulatorRepository;

    @Autowired
    private EquipmentSimulatorInfraConverter equipmentSimulatorInfraConverter;

    /**
     * 保存设备仿真数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEquipmentRealTime(EquipmentSimulatorData equipmentSimulatorData) {
        //先验证任务编号不能为空
        if(Optional.ofNullable(equipmentSimulatorData.getMission()).isEmpty()){
            log.warn("[EquipmentManagerImpl.saveEquipmentRealTime] 任务编号不能为空");
            throw new BusinessException(BssExType.SIMULATOR_MISSION_VALIDATE_ERROR, "任务编号不能为空");
        }
        //转换数据格式
        EquipmentSimulatorPO equipmentSimulatorPO = equipmentSimulatorInfraConverter.toEquipmentRealTimePO(equipmentSimulatorData);

        try {
            DynamicTableNameContext.setMissionId(equipmentSimulatorData.getMission());
            equipmentSimulatorRepository.save(equipmentSimulatorPO);
        }finally {
            DynamicTableNameContext.clear();
        }
    }

    @Override
    public List<EquipmentSimulatorData> getEquipmentRealTimes(Long mission, Long startTick, Long endTick) {
        if (Optional.ofNullable(mission).isEmpty()){
            log.warn("[EquipmentManagerImpl.getEquipmentRealTimes] 任务编号不能为空");
            throw new BusinessException(BssExType.SIMULATOR_MISSION_VALIDATE_ERROR, "任务编号不能为空");
        }

        //校验帧数合法性
        if (startTick == null || endTick == null || startTick > endTick){
            log.warn("[EquipmentManagerImpl.getEquipmentRealTimes] 帧不能为空，且开始帧不能大于结束帧");
            throw new BusinessException(BssExType.TICK_VALIDATE_ERROR, "帧不能为空，且开始帧不能大于结束帧");
        }
        try {
            DynamicTableNameContext.setMissionId(mission);
            List<EquipmentSimulatorPO> equipmentSimulatorPOList = equipmentSimulatorRepository.lambdaQuery()
                    .eq(EquipmentSimulatorPO::getMission, mission)
                    .ge(EquipmentSimulatorPO::getTick, startTick)
                    .le(EquipmentSimulatorPO::getTick, endTick)
                    .list();
            return equipmentSimulatorInfraConverter.toEquipmentRealTimeList(equipmentSimulatorPOList);
        }finally {
            DynamicTableNameContext.clear();
        }
    }
}
