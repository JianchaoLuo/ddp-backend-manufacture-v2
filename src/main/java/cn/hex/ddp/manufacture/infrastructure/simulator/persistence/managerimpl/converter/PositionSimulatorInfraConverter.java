package cn.hex.ddp.manufacture.infrastructure.simulator.persistence.managerimpl.converter;

import cn.hex.ddp.manufacture.domain.simulator.model.PositionSimulatorData;
import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.po.PositionSimulatorDataPO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 点位仿真数据基础转换器
 * @Author: FengZebang
 * @Date: 2025/3/11
 */
@Mapper(componentModel = "spring")
public interface PositionSimulatorInfraConverter {
    PositionSimulatorDataPO toPositionRealTimePO(PositionSimulatorData positionSimulatorData);

    List<PositionSimulatorData> toPositionRealTimeList(List<PositionSimulatorDataPO> positionSimulatorDataPOList);
}
