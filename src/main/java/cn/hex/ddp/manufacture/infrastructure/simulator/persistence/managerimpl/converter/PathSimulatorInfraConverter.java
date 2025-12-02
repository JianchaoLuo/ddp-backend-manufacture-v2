package cn.hex.ddp.manufacture.infrastructure.simulator.persistence.managerimpl.converter;

import cn.hex.ddp.manufacture.domain.simulator.model.PathSimulatorData;
import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.po.PathSimulatorDataPO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 路径仿真数据基础转换器
 * @Author: FengZebang
 * @Date: 2025/3/12
 */
@Mapper(componentModel = "spring")
public interface PathSimulatorInfraConverter {
    PathSimulatorDataPO toPathSimulatorDataPO(PathSimulatorData pathSimulatorData);

    List<PathSimulatorData> toPathSimulatorDataList(List<PathSimulatorDataPO> pathSimulatorDataPOList);
}
