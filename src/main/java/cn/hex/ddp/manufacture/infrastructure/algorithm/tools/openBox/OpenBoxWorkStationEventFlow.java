package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.openBox;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Ferry;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.SubCar;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.OpenBoxSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.waitingPour.AfterClosingBoxAllLink;
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
 * @Created: 2024/8/1 3:03
 */
@NoArgsConstructor
public class OpenBoxWorkStationEventFlow {
    /**
     * 仿真总工具类
     */
    private SimulationTool allTool = new SimulationTool();

    /**
     * 开箱区工岗仿真
     * @param simulationOutput 仿真的输入
     * @param subCar 开箱区子车
     * @param ferry 开箱区母车
     * @param workStationId 工岗在列表中的标识
     * @param workStationGanttIndex 工岗在甘特图中的标识
     * @param upDownFlag 是上或者下开箱，true为上，false为下
     * @return
     */
    public AfterClosingBoxAllLink OpenBoxWorkStationWorkingEventCreate(AfterClosingBoxAllLink simulationOutput, SubCar subCar, Ferry ferry,
                                                                       Integer workStationId, Integer workStationGanttIndex, boolean upDownFlag) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = input.getWorkStations().get(workStationId).getProduct().getFirst().getUnboxingTime();

        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(subCar.getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(ferry.getFerryState());
        Coordinate ferryCoordinate = new Coordinate(ferry.getLocationCoordinate().getX(), ferry.getLocationCoordinate().getY());
        List<WorkstationStatusEnum> startWorkStationStatus = new ArrayList<>();
        startWorkStationStatus.add(WorkstationStatusEnum.WORKING);
        List<WorkstationStatusEnum> endWorkStationStatus = new ArrayList<>();
        endWorkStationStatus.add(WorkstationStatusEnum.WAIT_TRANSPORT);

        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        startPositionStatus.add(PositionStatusEnum.UNOCCUPIED);
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        endPositionStatus.add(PositionStatusEnum.OCCUPIED);

        //生成事件
        Event event = allTool.createEvent("100734", "OBWSW", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                null, null, false, null, null, false, null,
                null, null, false, null, null, false, null, null,
                null, null, null, null, null, input.getWorkStations().get(workStationId).getId(),
                startWorkStationStatus, endWorkStationStatus, null, null, null, input.getWorkStations().get(workStationId).getId(),
                WORK_STATION, NOTICE, null, input.getWorkStations().get(workStationId).getProduct().getFirst().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(workStationGanttIndex).getEvents().add(event);

        if (upDownFlag){
            Event event_1 = allTool.createEvent("100735", "OBUPN", executionTime, input.getTotalTime(), input.getTotalTime() + executionTime,
                    null, null, false, null, null, false, null,
                    null, null, true, null, null, false, null,
                    null, null, null, input.getCastingUpPosition().getId(), startPositionStatus, endPositionStatus, null,
                    null, null, null, null, null, input.getCastingUpPosition().getId(),
                    ExecuteTypeEnum.POSITION, NOTICE, null, null);

            simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(workStationGanttIndex).getEvents().add(event_1);
        }else {
            Event event_2 = allTool.createEvent("100735", "OBDPN", executionTime, input.getTotalTime(), input.getTotalTime() + executionTime,
                    null, null, false, null, null, false, null,
                    null, null, true, null, null, false, null,
                    null, null, null, input.getCastingDownPosition().getId(), startPositionStatus, endPositionStatus, null,
                    null, null, null, null, null, input.getCastingDownPosition().getId(),
                    ExecuteTypeEnum.POSITION, NOTICE, null, null);

            simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(workStationGanttIndex).getEvents().add(event_2);
        }


        //改变车辆状态和时间
        input.getWorkStations().get(workStationId).setStatus(WAIT_TRANSPORT);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public void openBoxWorkStationWorkingEventCreate_2(AfterClosingBoxAllLink simulationOutput, SubCar subCar, Ferry ferry,
                                                       Integer downWorkStationId, Integer downWorkStationGanttIndex, Integer upWorkStationId,
                                                       Integer upWorkStationGanttIndex, boolean upDownFlag) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = input.getWorkStations().get(downWorkStationId).getProduct().getFirst().getUnboxingTime();

        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(subCar.getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(ferry.getFerryState());
        Coordinate ferryCoordinate = new Coordinate(ferry.getLocationCoordinate().getX(), ferry.getLocationCoordinate().getY());
        List<WorkstationStatusEnum> startWorkStationStatus = new ArrayList<>();
        startWorkStationStatus.add(WorkstationStatusEnum.WORKING);
        List<WorkstationStatusEnum> endWorkStationStatus = new ArrayList<>();
        endWorkStationStatus.add(WorkstationStatusEnum.WAIT_TRANSPORT);

        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        startPositionStatus.add(PositionStatusEnum.UNOCCUPIED);
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        endPositionStatus.add(PositionStatusEnum.OCCUPIED);

        //生成事件
        Event event = allTool.createEvent("100734", "OBWSW", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                null, null, false, null, null, false, null,
                null, null, false, null, null, false, null, null,
                null, null, null, null, null, input.getWorkStations().get(downWorkStationId).getId(),
                startWorkStationStatus, endWorkStationStatus, null, null, null, input.getWorkStations().get(downWorkStationId).getId(),
                WORK_STATION, NOTICE, null, input.getWorkStations().get(downWorkStationId).getProduct().getFirst().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(downWorkStationGanttIndex).getEvents().add(event);

        if (upDownFlag){
            Event event_1 = allTool.createEvent("100735", "OBUPN", executionTime, input.getTotalTime(), input.getTotalTime() + executionTime,
                    null, null, false, null, null, false, null,
                    null, null, true, null, null, false, null,
                    null, null, null, input.getCastingUpPosition().getId(), startPositionStatus, endPositionStatus, null,
                    null, null, null, null, null, input.getCastingUpPosition().getId(),
                    ExecuteTypeEnum.POSITION, NOTICE, null, null);

            simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(downWorkStationGanttIndex).getEvents().add(event_1);
        }else {
            Event event_2 = allTool.createEvent("100735", "OBDPN", executionTime, input.getTotalTime(), input.getTotalTime() + executionTime,
                    null, null, false, null, null, false, null,
                    null, null, true, null, null, false, null,
                    null, null, null, input.getCastingDownPosition().getId(), startPositionStatus, endPositionStatus, null,
                    null, null, null, null, null, input.getCastingDownPosition().getId(),
                    ExecuteTypeEnum.POSITION, NOTICE, null, null);

            simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(downWorkStationGanttIndex).getEvents().add(event_2);
        }


        //改变车辆状态和时间
        input.getWorkStations().get(downWorkStationId).setStatus(WAIT_TRANSPORT);

        simulationOutput.getOpenBoxInput().setInput(input);
    }
}
