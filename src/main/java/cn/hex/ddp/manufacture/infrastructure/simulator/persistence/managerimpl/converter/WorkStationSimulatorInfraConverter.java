
package cn.hex.ddp.manufacture.infrastructure.simulator.persistence.managerimpl.converter;
import cn.hex.ddp.manufacture.domain.simulator.model.WorkStationSimulatorData;
import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.po.WorkStationSimulatorDataPO;
import org.mapstruct.Mapper;
import java.util.List;
/**
 * 工位仿真数据转化器
 * @Author: FengZebang
 * @Date: 2025/3/5
 */
@Mapper(componentModel = "spring")
public interface WorkStationSimulatorInfraConverter {
    WorkStationSimulatorDataPO toWorkStationRealTimePO(WorkStationSimulatorData workStationSimulatorData);
    List<WorkStationSimulatorData> toWorkStationRealTimeList(List<WorkStationSimulatorDataPO> workStationSimulatorDataPOList);
}