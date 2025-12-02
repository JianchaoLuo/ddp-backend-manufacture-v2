package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.closingBox;

import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Ferry;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.SubCar;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.ClosingBoxSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.mouldingLine.OutBoundMouldingAndClosingBox;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum.WAIT_TRANSPORT;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum.WORK_STATION;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum.NOTICE;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/21 5:20
 */
@NoArgsConstructor
public class ClosingBoxWorkStationEventFlow {
    /**
     * 仿真总工具类
     */
    private SimulationTool allTool = new SimulationTool();

    /**
     * 工岗通知事件的生成
     * @param simulationOutput 仿真的输入
     * @param subCar 子车
     * @param ferry 母车
     * @param workStationId 工岗在输入中的标识
     * @param workStationGanttIndex 工岗在甘特图中的标识
     * @return 仿真的输出
     */
    public OutBoundMouldingAndClosingBox ClosingBoxWorkStationWorkingEventCreate(OutBoundMouldingAndClosingBox simulationOutput, SubCar subCar, Ferry ferry,
                                                                                 Integer workStationId, Integer workStationGanttIndex) {
        ClosingBoxSimulationInput input = simulationOutput.getClosingBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = input.getWorkStations().get(workStationId).getProduct().getFirst().getClosingBoxTime();

        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(subCar.getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(ferry.getFerryState());
        Coordinate ferryCoordinate = new Coordinate(ferry.getLocationCoordinate().getX(), ferry.getLocationCoordinate().getY());
        List<WorkstationStatusEnum> startWorkStationStatus = new ArrayList<>();
        startWorkStationStatus.add(WorkstationStatusEnum.WORKING);
        List<WorkstationStatusEnum> endWorkStationStatus = new ArrayList<>();
        endWorkStationStatus.add(WorkstationStatusEnum.WAIT_TRANSPORT);

        Product product = input.getWorkStations().get(workStationId).getProduct().getFirst().productDeepCopy();
        ArrayList<Product> products = new ArrayList<>();
        for (int i = 1; i < input.getWorkStations().get(workStationId).getProduct().size(); i++) {
            products.add(input.getWorkStations().get(workStationId).getProduct().get(i).productDeepCopy());
            //product.getProducts().add(input.getWorkStations().get(workStationId).getProduct().get(i).productDeepCopy());
        }

        product.setProducts(products);

        //生成事件
        Event event = allTool.createEvent("100341", "CBWSW", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                null, null, false, null, null, false, null,
                null, null, false, null, null, false, null, null,
                null, null, null, null, null, input.getWorkStations().get(workStationId).getId(),
                startWorkStationStatus, endWorkStationStatus, null, null, null, input.getWorkStations().get(workStationId).getId(),
                WORK_STATION, NOTICE, null, product);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(workStationGanttIndex).getEvents().add(event);

        //改变车辆状态和时间
        input.getWorkStations().get(workStationId).setStatus(WAIT_TRANSPORT);

        simulationOutput.getClosingBoxInput().setInput(input);

        return simulationOutput;
    }
}
