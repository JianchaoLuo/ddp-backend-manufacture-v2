package cn.hex.ddp.manufacture.infrastructure.simulator.persistence.managerimpl.converter;

import cn.hex.ddp.manufacture.domain.simulator.model.CarSimulatorData;
import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.po.CarSimulatorDataPO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 仿真数据基础转换器
 *
 * @author Huhaisen
 * @date 2024/12/26
 */
@Mapper(componentModel = "spring")
public interface CarSimulatorInfraConverter {
    CarSimulatorDataPO toCarRealTimePO(CarSimulatorData carSimulatorData);

    List<CarSimulatorData> toCarRealTimeList(List<CarSimulatorDataPO> carSimulatorDataPOList);
}
