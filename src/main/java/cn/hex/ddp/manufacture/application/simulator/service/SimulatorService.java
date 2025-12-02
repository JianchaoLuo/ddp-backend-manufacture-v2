package cn.hex.ddp.manufacture.application.simulator.service;

import cn.hex.ddp.manufacture.domain.simulator.model.*;
import cn.hex.ddp.manufacture.domain.simulator.model.simulation.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;

import java.util.Collection;
import java.util.List;

/**
 * 仿真服务接口
 * @Author: FengZebang
 * @Date: 2025/4/17
 */
public interface SimulatorService {

    /**
     * 记录帧
     */
    public void saveSimulatorData(Long currentTick,
                                  Long missionId,
                                  List<Car> cars,
                                  List<Equipment> equipments,
                                  Collection<PositionRealTime> positions,
                                  Collection<WorkstationRealTime> workstations,
                                  Collection<Path> paths,
                                  Collection<Product> products);

    //updateTickData();
    //getTickData();
}
