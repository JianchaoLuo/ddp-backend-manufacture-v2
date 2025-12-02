package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.closingBox;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductAfootEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.ClosingBoxSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationException;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.mouldingLine.OutBoundMouldingAndClosingBox;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;

import java.util.ArrayList;
import java.util.List;

import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.OCCUPIED;
import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.UNOCCUPIED;
import static cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum.PATH_UNOCCUPIED;
import static cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum.*;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum.FERRY;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum.SUB_CAR;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum.CARRY_SUB_CAR;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum.NOT_CARRY_SUB_CAR;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum.*;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum.*;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/21 5:20
 */
public class ColdClosingBoxEventFlow {
    /**
     * 仿真总工具类
     */
    private SimulationTool allTool = new SimulationTool();
    /**
     * 合箱区放置的工具类
     */
    private ClosingBoxSimulationTools tools = new ClosingBoxSimulationTools();

    /**
     * 装载事件生成
     * @param input
     * @return
     */
    public OutBoundMouldingAndClosingBox ColdEmptyLoadInPositionEventCreate(OutBoundMouldingAndClosingBox input) {
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getClosingBoxInput().getInput().getColdClosingBoxPosition().getCoordinate().getY() -
                input.getClosingBoxInput().getInput().getColdClosingBoxFerry().getLocationCoordinate().getY()) /
                input.getClosingBoxInput().getInput().getColdClosingBoxSubCar().getEmptySpeed();
        double executionTime_2 = input.getClosingBoxInput().getInput().getColdClosingBoxSubCar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getClosingBoxInput().getInput().getColdClosingBoxPosition().getCoordinate().getY() -
                input.getClosingBoxInput().getInput().getColdClosingBoxFerry().getLocationCoordinate().getY()) /
                input.getClosingBoxInput().getInput().getColdClosingBoxSubCar().getFullSpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getClosingBoxInput().getInput().getColdClosingBoxSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getClosingBoxInput().getInput().getColdClosingBoxFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus_1 = new ArrayList<>();
        List<SubCarStatusEnum> endSubCarStatus_2 = new ArrayList<>();
        List<SubCarStatusEnum> endSubCarStatus_3 = new ArrayList<>();
        endSubCarStatus_1.add(EMPTY_LOAD);
        endSubCarStatus_1.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_1.add(SUB_CAR_NOT_IN_FERRY);
        endSubCarStatus_2.add(FULL_LOAD);
        endSubCarStatus_2.add(TOP_ROD_RAISED);
        endSubCarStatus_2.add(SUB_CAR_NOT_IN_FERRY);
        endSubCarStatus_3.add(FULL_LOAD);
        endSubCarStatus_3.add(TOP_ROD_RAISED);
        endSubCarStatus_3.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus_1 = new ArrayList<>();
        List<FerryStatusEnum> endFerryStatus_2 = new ArrayList<>();
        endFerryStatus_1.add(NOT_CARRY_SUB_CAR);
        endFerryStatus_2.add(CARRY_SUB_CAR);
        //点位状态列表
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        startPositionStatus.add(OCCUPIED);
        endPositionStatus.add(UNOCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getClosingBoxInput().getInput().getColdClosingBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getClosingBoxInput().getInput().getColdClosingBoxPosition().getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100321", "ELCSSCGICCSP", executionTime_1, input.getClosingBoxInput().getInput().getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getClosingBoxInput().getInput().getTotalTime() - executionTime_2 - executionTime_3, input.getClosingBoxInput().getInput().getColdClosingBoxSubCar().getId(),
                startSubCarStatus_1, true, input.getClosingBoxInput().getInput().getColdClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(),
                endSubCarStatus_1, true, input.getClosingBoxInput().getInput().getColdClosingBoxPosition().getCoordinate(),
                input.getClosingBoxInput().getInput().getColdClosingBoxFerry().getId(), startFerryStatus_1,true,
                input.getClosingBoxInput().getInput().getColdClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_1, true,
                input.getClosingBoxInput().getInput().getColdClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), null,null,
                null, input.getClosingBoxInput().getInput().getColdClosingBoxPosition().getId(), startPositionStatus, startPositionStatus, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getClosingBoxInput().getInput().getColdClosingBoxSubCar().getId(),
                SUB_CAR, RUNNING, input.getClosingBoxInput().getInput().getColdClosingBoxPosition().getCoordinate(), null);

        //事件加入甘特图
        input.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxInput().getInput().getClosingBoxIndexInput().getColdClosingBoxSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100322", "ELCSCSCICCSP", executionTime_2, input.getClosingBoxInput().getInput().getTotalTime() - executionTime_2 - executionTime_3,
                input.getClosingBoxInput().getInput().getTotalTime() - executionTime_3, input.getClosingBoxInput().getInput().getColdClosingBoxSubCar().getId(), endSubCarStatus_1,
                true, input.getClosingBoxInput().getInput().getColdClosingBoxPosition().getCoordinate(), endSubCarStatus_2, true,
                input.getClosingBoxInput().getInput().getColdClosingBoxPosition().getCoordinate(), input.getClosingBoxInput().getInput().getColdClosingBoxFerry().getId(),
                endFerryStatus_1,true, input.getClosingBoxInput().getInput().getColdClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(),
                endFerryStatus_1, true, input.getClosingBoxInput().getInput().getColdClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(),
                null,null, null, input.getClosingBoxInput().getInput().getColdClosingBoxPosition().getId(), startPositionStatus,
                endPositionStatus, null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getClosingBoxInput().getInput().getColdClosingBoxSubCar().getId(), SUB_CAR, RISING, null, product);

        //事件加入甘特图
        input.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxInput().getInput().getClosingBoxIndexInput().getColdClosingBoxSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100323", "FLCCSCSCGBTF", executionTime_3, input.getClosingBoxInput().getInput().getTotalTime() - executionTime_3,
                input.getClosingBoxInput().getInput().getTotalTime(), input.getClosingBoxInput().getInput().getColdClosingBoxSubCar().getId(), endSubCarStatus_2,true,
                input.getClosingBoxInput().getInput().getColdClosingBoxPosition().getCoordinate(), endSubCarStatus_3, true,
                input.getClosingBoxInput().getInput().getColdClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(),
                input.getClosingBoxInput().getInput().getColdClosingBoxFerry().getId(), endFerryStatus_1,true,
                input.getClosingBoxInput().getInput().getColdClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_2,
                true, input.getClosingBoxInput().getInput().getColdClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(),
                null,null, null, null, null, null, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getClosingBoxInput().getInput().getColdClosingBoxSubCar().getId(),
                SUB_CAR, RUNNING, input.getClosingBoxInput().getInput().getColdClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), product);

        //事件加入甘特图
        input.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxInput().getInput().getClosingBoxIndexInput().getColdClosingBoxSubCarInGanttIndex()).getEvents().add(event_3);

        //改变车辆状态和时间
        input.getClosingBoxInput().getInput().getColdClosingBoxSubCar().setSubCarState(endSubCarStatus_3);
        input.getClosingBoxInput().getInput().getColdClosingBoxFerry().setFerryState(endFerryStatus_2);
        input.getClosingBoxInput().getInput().getColdClosingBoxPosition().setStatus(UNOCCUPIED);


        input.getClosingBoxInput().getInput().getColdClosingBoxSubCar().setProduct(product);

        input.getClosingBoxInput().getInput().getColdClosingBoxPosition().setProduct(null);

        return input;
    }

    /**
     * 在工岗装载事件生成
     * @param simulationOutput
     * @param workStationIndex
     * @return
     */
    public OutBoundMouldingAndClosingBox ColdClosingBoxEmptyLoadInWorkStationEventCreate(OutBoundMouldingAndClosingBox simulationOutput, Integer workStationIndex) throws SimulationException {
        ClosingBoxSimulationInput input = simulationOutput.getClosingBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getWorkStations().get(input.getClosingBoxIndexInput().getColdClosingBoxWorkStationIndex_1()).getWorkPositionCoordinate().getY() -
                input.getColdClosingBoxFerry().getLocationCoordinate().getY()) / input.getColdClosingBoxSubCar().getEmptySpeed();
        double executionTime_2 = input.getColdClosingBoxSubCar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getWorkStations().get(input.getClosingBoxIndexInput().getColdClosingBoxWorkStationIndex_1()).getWorkPositionCoordinate().getY() -
                input.getColdClosingBoxFerry().getLocationCoordinate().getY()) / input.getColdClosingBoxSubCar().getFullSpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getColdClosingBoxSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getColdClosingBoxFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus_1 = new ArrayList<>();
        List<SubCarStatusEnum> endSubCarStatus_2 = new ArrayList<>();
        List<SubCarStatusEnum> endSubCarStatus_3 = new ArrayList<>();
        endSubCarStatus_1.add(EMPTY_LOAD);
        endSubCarStatus_1.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_1.add(SUB_CAR_NOT_IN_FERRY);
        endSubCarStatus_2.add(FULL_LOAD);
        endSubCarStatus_2.add(TOP_ROD_RAISED);
        endSubCarStatus_2.add(SUB_CAR_NOT_IN_FERRY);
        endSubCarStatus_3.add(FULL_LOAD);
        endSubCarStatus_3.add(TOP_ROD_RAISED);
        endSubCarStatus_3.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus_1 = new ArrayList<>();
        List<FerryStatusEnum> endFerryStatus_2 = new ArrayList<>();
        endFerryStatus_1.add(NOT_CARRY_SUB_CAR);
        endFerryStatus_2.add(CARRY_SUB_CAR);
        //工岗点状态列表
        List<WorkstationStatusEnum> startWorkstationStatus = new ArrayList<>();
        List<WorkstationStatusEnum> endWorkstationStatus = new ArrayList<>();
        startWorkstationStatus.add(WAIT_TRANSPORT);
        endWorkstationStatus.add(FREE);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getColdClosingBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        List<Product> products = new ArrayList<>(input.getWorkStations().get(workStationIndex).getProduct());
        Product product = tools.createClosingBoxProduct(products);

        //生成事件
        Event event_1 = allTool.createEvent("100324", "ELCCSCSCGIWSP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_2 - executionTime_3, input.getColdClosingBoxSubCar().getId(), startSubCarStatus_1,true,
                input.getColdClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), endSubCarStatus_1, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getColdClosingBoxFerry().getId(), startFerryStatus_1,
                true, input.getColdClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_1, true,
                input.getColdClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus,
                startWorkstationStatus, pathIds, pathStatusEnums, pathStatusEnums, input.getColdClosingBoxSubCar().getId(), SUB_CAR, RUNNING,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getColdClosingBoxSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100325", "ELCCSCSCIWSP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_3, input.getColdClosingBoxSubCar().getId(), endSubCarStatus_1,true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_2, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getColdClosingBoxFerry().getId(), endFerryStatus_1,
                true, input.getColdClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_1, true,
                input.getColdClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus,
                endWorkstationStatus, pathIds, pathStatusEnums, pathStatusEnums, input.getColdClosingBoxSubCar().getId(), SUB_CAR, RISING, null, product);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getColdClosingBoxSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100326", "FLCSCSCGBTFWS", executionTime_3, input.getTotalTime() - executionTime_3, input.getTotalTime(),
                input.getColdClosingBoxSubCar().getId(), endSubCarStatus_2,true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(),
                endSubCarStatus_3, true, input.getColdClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), input.getColdClosingBoxFerry().getId(),
                endFerryStatus_1,true, input.getColdClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_2, true,
                input.getColdClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null, null,
                null, null, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getColdClosingBoxSubCar().getId(), SUB_CAR, RUNNING, input.getColdClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), product);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getColdClosingBoxSubCarInGanttIndex()).getEvents().add(event_3);

        //改变车辆状态和时间
        input.getColdClosingBoxSubCar().setSubCarState(endSubCarStatus_3);
        input.getColdClosingBoxFerry().setFerryState(endFerryStatus_2);
        input.getWorkStations().get(workStationIndex).setStatus(FREE);

        input.getColdClosingBoxSubCar().setProduct(product);
        input.getWorkStations().get(workStationIndex).setProduct(new ArrayList<>());

        simulationOutput.getClosingBoxInput().setInput(input);

        return simulationOutput;
    }

    /**
     * 前往工岗装载事件生成
     * @param simulationOutput
     * @param workStationIndex
     * @return
     */
    public OutBoundMouldingAndClosingBox ColdClosingBoxEmptyGoWorkStationEventCreate(OutBoundMouldingAndClosingBox simulationOutput, int workStationIndex) {
        ClosingBoxSimulationInput input = simulationOutput.getClosingBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                input.getColdClosingBoxFerry().getLocationCoordinate().getX()) / input.getColdClosingBoxFerry().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX(),
                input.getColdClosingBoxFerry().getLocationCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getColdClosingBoxSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getColdClosingBoxFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(CARRY_SUB_CAR);
        //工岗点状态列表
        List<WorkstationStatusEnum> startWorkstationStatus = new ArrayList<>();
        List<WorkstationStatusEnum> endWorkstationStatus = new ArrayList<>();
        startWorkstationStatus.add(WAIT_TRANSPORT);
        endWorkstationStatus.add(WAIT_TRANSPORT);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getColdClosingBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);


        //生成事件
        Event event = allTool.createEvent("100327", "EGCCSCSCGIWSP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getColdClosingBoxSubCar().getId(), startSubCarStatus_1,false, null, endSubCarStatus, true,
                aimCoordinate, input.getColdClosingBoxFerry().getId(), startFerryStatus_1,false, null, endFerryStatus,
                true, aimCoordinate, null,null, null, null, null,
                null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, endWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdClosingBoxFerry().getId(), FERRY, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getColdClosingBoxSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getColdClosingBoxSubCar().setSubCarState(endSubCarStatus);
        input.getColdClosingBoxFerry().setFerryState(endFerryStatus);
        input.getColdClosingBoxSubCar().setLocationCoordinate(aimCoordinate);
        input.getColdClosingBoxFerry().setLocationCoordinate(aimCoordinate);

        simulationOutput.getClosingBoxInput().setInput(input);

        return simulationOutput;
    }

    /**
     * 前往点位装载事件生成
     * @param simulationOutput
     * @return
     */
    public OutBoundMouldingAndClosingBox ColdClosingBoxEmptyGoColdPosotionEventCreate(OutBoundMouldingAndClosingBox simulationOutput) {
        ClosingBoxSimulationInput input = simulationOutput.getClosingBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getColdClosingBoxPosition().getCoordinate().getX() -
                input.getColdClosingBoxFerry().getLocationCoordinate().getX()) / input.getColdClosingBoxFerry().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getColdClosingBoxPosition().getCoordinate().getX(), input.getColdClosingBoxFerry().getLocationCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getColdClosingBoxSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getColdClosingBoxFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(CARRY_SUB_CAR);
        //点位状态列表
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        startPositionStatus.add(OCCUPIED);
        endPositionStatus.add(OCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getColdClosingBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);


        //生成事件
        Event event = allTool.createEvent("100328", "EGCCSCSCGICMP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getColdClosingBoxSubCar().getId(), startSubCarStatus_1,false, null, endSubCarStatus, true,
                aimCoordinate, input.getColdClosingBoxFerry().getId(), startFerryStatus_1, false, null, endFerryStatus,
                true, aimCoordinate, null,null, null, input.getColdClosingBoxPosition().getId(),
                startPositionStatus, endPositionStatus, null, null, null, pathIds, pathStatusEnums,
                pathStatusEnums, input.getColdClosingBoxFerry().getId(), FERRY, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getColdClosingBoxSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getColdClosingBoxSubCar().setSubCarState(endSubCarStatus);
        input.getColdClosingBoxFerry().setFerryState(endFerryStatus);
        input.getColdClosingBoxSubCar().setLocationCoordinate(aimCoordinate);
        input.getColdClosingBoxFerry().setLocationCoordinate(aimCoordinate);

        simulationOutput.getClosingBoxInput().setInput(input);

        return simulationOutput;
    }

    /**
     * 前往合箱完成前往浇筑点位卸载事件生成
     * @param simulationOutput
     * @return
     */
    public OutBoundMouldingAndClosingBox ColdClosingBoxFullUnLoadPositionEventCreate(OutBoundMouldingAndClosingBox simulationOutput) {
        ClosingBoxSimulationInput input = simulationOutput.getClosingBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getColdClosingBoxGoPourPosition().getCoordinate().getY() -
                input.getColdClosingBoxFerry().getLocationCoordinate().getY()) / input.getColdClosingBoxSubCar().getFullSpeed();
        double executionTime_2 = input.getColdClosingBoxSubCar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getColdClosingBoxGoPourPosition().getCoordinate().getY() -
                input.getColdClosingBoxFerry().getLocationCoordinate().getY()) / input.getColdClosingBoxSubCar().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getColdClosingBoxGoPourPosition().getCoordinate().getX(),
                input.getColdClosingBoxFerry().getLocationCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getColdClosingBoxSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getColdClosingBoxFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus_1 = new ArrayList<>();
        List<SubCarStatusEnum> endSubCarStatus_2 = new ArrayList<>();
        List<SubCarStatusEnum> endSubCarStatus_3 = new ArrayList<>();
        endSubCarStatus_1.add(FULL_LOAD);
        endSubCarStatus_1.add(TOP_ROD_RAISED);
        endSubCarStatus_1.add(SUB_CAR_NOT_IN_FERRY);
        endSubCarStatus_2.add(EMPTY_LOAD);
        endSubCarStatus_2.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_2.add(SUB_CAR_NOT_IN_FERRY);
        endSubCarStatus_3.add(EMPTY_LOAD);
        endSubCarStatus_3.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_3.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus_1 = new ArrayList<>();
        List<FerryStatusEnum> endFerryStatus_2 = new ArrayList<>();
        endFerryStatus_1.add(CARRY_SUB_CAR);
        endFerryStatus_2.add(NOT_CARRY_SUB_CAR);
        //点位状态列表
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        startPositionStatus.add(UNOCCUPIED);
        endPositionStatus.add(OCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getColdClosingBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getColdClosingBoxSubCar().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.DOING_CLOSING_BOX);

        //生成事件
        Event event_1 = allTool.createEvent("100338", "FGCCSCSCILP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_2 - executionTime_3, input.getColdClosingBoxSubCar().getId(), startSubCarStatus_1,true, aimCoordinate,
                endSubCarStatus_1, true, input.getColdClosingBoxGoPourPosition().getCoordinate(), input.getColdClosingBoxFerry().getId(), startFerryStatus_1,
                true, aimCoordinate, endFerryStatus_2, true, aimCoordinate, null,null, null,
                input.getColdClosingBoxGoPourPosition().getId(), startPositionStatus, startPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdClosingBoxSubCar().getId(), SUB_CAR, RUNNING,
                input.getColdClosingBoxGoPourPosition().getCoordinate(), product);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getColdClosingBoxSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100339", "FCGCSCSCULILP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_3, input.getColdClosingBoxSubCar().getId(), endSubCarStatus_1,true,
                input.getColdClosingBoxGoPourPosition().getCoordinate(), endSubCarStatus_2, true, input.getColdClosingBoxGoPourPosition().getCoordinate(),
                input.getColdClosingBoxFerry().getId(), endFerryStatus_2, true, aimCoordinate, endFerryStatus_2, true, aimCoordinate,
                null,null, null, input.getColdClosingBoxGoPourPosition().getId(), startPositionStatus, endPositionStatus,
                null, null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getColdClosingBoxSubCar().getId(),
                SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getColdClosingBoxSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100340", "FGCSCBFILP", executionTime_3, input.getTotalTime() - executionTime_3, input.getTotalTime(),
                input.getColdClosingBoxSubCar().getId(), endSubCarStatus_2,true, input.getColdClosingBoxGoPourPosition().getCoordinate(), endSubCarStatus_3,
                true, aimCoordinate, input.getColdClosingBoxFerry().getId(), endFerryStatus_2,true, aimCoordinate,
                endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdClosingBoxSubCar().getId(), SUB_CAR, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getColdClosingBoxSubCarInGanttIndex()).getEvents().add(event_3);

        //改变车辆状态和时间
        input.getColdClosingBoxSubCar().setSubCarState(endSubCarStatus_3);
        input.getColdClosingBoxFerry().setFerryState(endFerryStatus_1);
        input.getColdClosingBoxSubCar().setLocationCoordinate(aimCoordinate);
        input.getColdClosingBoxFerry().setLocationCoordinate(aimCoordinate);
        input.getColdClosingBoxGoPourPosition().setStatus(OCCUPIED);

        input.getColdClosingBoxGoPourPosition().setProduct(product);
        input.getColdClosingBoxSubCar().setProduct(null);

        simulationOutput.getClosingBoxInput().setInput(input);

        return simulationOutput;
    }

    /**
     * 前往合箱完成前往浇筑点位卸载事件生成
     * @param simulationOutput
     * @return
     */
    public OutBoundMouldingAndClosingBox ColdClosingBoxFullGoPositionEventCreate(OutBoundMouldingAndClosingBox simulationOutput) {
        ClosingBoxSimulationInput input = simulationOutput.getClosingBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getColdClosingBoxGoPourPosition().getCoordinate().getX() -
                input.getColdClosingBoxFerry().getLocationCoordinate().getX()) / input.getColdClosingBoxFerry().getFullSpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getColdClosingBoxGoPourPosition().getCoordinate().getX(),
                input.getColdClosingBoxFerry().getLocationCoordinate().getY());
        //判断母车所在工岗的位置标识
//        int workStationIndex = tools.judgeColdFerryInWhichWorkstation(input.getColdClosingBoxFerry(), input.getWorkStations(),
//                input.getClosingBoxIndexInput());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getColdClosingBoxSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getColdClosingBoxFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(CARRY_SUB_CAR);
        //工岗状态列表
        List<WorkstationStatusEnum> startWorkstationStatus = new ArrayList<>();
        startWorkstationStatus.add(WorkstationStatusEnum.FREE);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getColdClosingBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);


        //生成事件
        Event event = allTool.createEvent("100337", "FGCCSCGILP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getColdClosingBoxSubCar().getId(), startSubCarStatus_1,false, null, endSubCarStatus, true,
                aimCoordinate, input.getColdClosingBoxFerry().getId(), startFerryStatus_1,false, null, endFerryStatus,
                true, aimCoordinate, null,null, null, null, null,
                null, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getColdClosingBoxFerry().getId(), FERRY, RUNNING, aimCoordinate, input.getColdClosingBoxSubCar().getProduct());

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getColdClosingBoxSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getColdClosingBoxSubCar().setSubCarState(endSubCarStatus);
        input.getColdClosingBoxFerry().setFerryState(endFerryStatus);
        input.getColdClosingBoxSubCar().setLocationCoordinate(aimCoordinate);
        input.getColdClosingBoxFerry().setLocationCoordinate(aimCoordinate);

        simulationOutput.getClosingBoxInput().setInput(input);

        return simulationOutput;
    }

    /**
     * 前往待配对工岗卸载砂箱事件生成
     * @param simulationOutput
     * @param workStationIndex
     * @return
     */
    public OutBoundMouldingAndClosingBox ColdClosingBoxFullGoPairWorkStationEventCreate(OutBoundMouldingAndClosingBox simulationOutput, int workStationIndex) {
        ClosingBoxSimulationInput input = simulationOutput.getClosingBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                input.getColdClosingBoxFerry().getLocationCoordinate().getX()) / input.getColdClosingBoxFerry().getFullSpeed();
        double executionTime_2 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getColdClosingBoxFerry().getLocationCoordinate().getY()) / input.getColdClosingBoxSubCar().getFullSpeed();
        double executionTime_3 = input.getColdClosingBoxSubCar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getColdClosingBoxFerry().getLocationCoordinate().getY()) / input.getColdClosingBoxSubCar().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX(),
                input.getColdClosingBoxFerry().getLocationCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getColdClosingBoxSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getColdClosingBoxFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus_1 = new ArrayList<>();
        List<SubCarStatusEnum> endSubCarStatus_2 = new ArrayList<>();
        List<SubCarStatusEnum> endSubCarStatus_3 = new ArrayList<>();
        endSubCarStatus_1.add(FULL_LOAD);
        endSubCarStatus_1.add(TOP_ROD_RAISED);
        endSubCarStatus_1.add(SUB_CAR_NOT_IN_FERRY);
        endSubCarStatus_2.add(EMPTY_LOAD);
        endSubCarStatus_2.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_2.add(SUB_CAR_NOT_IN_FERRY);
        endSubCarStatus_3.add(EMPTY_LOAD);
        endSubCarStatus_3.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_3.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus_1 = new ArrayList<>();
        List<FerryStatusEnum> endFerryStatus_2 = new ArrayList<>();
        endFerryStatus_1.add(CARRY_SUB_CAR);
        endFerryStatus_2.add(NOT_CARRY_SUB_CAR);
        //工岗点状态列表
        List<WorkstationStatusEnum> startWorkstationStatus = new ArrayList<>();
        List<WorkstationStatusEnum> endWorkstationStatus = new ArrayList<>();
        startWorkstationStatus.add(WAIT_PAIR);
        endWorkstationStatus.add(OCCUPANCY);
        endWorkstationStatus.add(WORKING);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getColdClosingBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getColdClosingBoxSubCar().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.DOING_CLOSING_BOX);

        //生成事件
        Event event_1 = allTool.createEvent("100333", "FGCCSCSCGIWSPP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getColdClosingBoxSubCar().getId(), startSubCarStatus_1,false,
                null, startSubCarStatus_1, true, aimCoordinate, input.getColdClosingBoxFerry().getId(), startFerryStatus_1,false,
                null, endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, startWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdClosingBoxFerry().getId(), FERRY, RUNNING, aimCoordinate, product);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getColdClosingBoxSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100334", "FGCCSCIWSPP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_3 - executionTime_4, input.getColdClosingBoxSubCar().getId(), startSubCarStatus_1,true, aimCoordinate,
                endSubCarStatus_1, true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getColdClosingBoxFerry().getId(),
                startFerryStatus_1,true, aimCoordinate, endFerryStatus_2, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, startWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdClosingBoxSubCar().getId(), SUB_CAR, RUNNING,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), product);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getColdClosingBoxSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100335", "FGCCSCULIWSPP", executionTime_3, input.getTotalTime() - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_4, input.getColdClosingBoxSubCar().getId(), endSubCarStatus_1,true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_2, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getColdClosingBoxFerry().getId(), endFerryStatus_2,true,
                aimCoordinate, endFerryStatus_2, true, aimCoordinate, null,null, null, null,
                null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdClosingBoxSubCar().getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getColdClosingBoxSubCarInGanttIndex()).getEvents().add(event_3);

        //生成事件
        Event event_4 = allTool.createEvent("100336", "FGCCSCBFIWSP", executionTime_4, input.getTotalTime() - executionTime_4, input.getTotalTime(),
                input.getColdClosingBoxSubCar().getId(), endSubCarStatus_2,true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(),
                endSubCarStatus_3, true, aimCoordinate, input.getColdClosingBoxFerry().getId(), endFerryStatus_2,true, aimCoordinate,
                endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdClosingBoxSubCar().getId(), SUB_CAR, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getColdClosingBoxSubCarInGanttIndex()).getEvents().add(event_4);

        //改变车辆状态和时间
        input.getColdClosingBoxSubCar().setSubCarState(endSubCarStatus_3);
        input.getColdClosingBoxFerry().setFerryState(endFerryStatus_1);
        input.getColdClosingBoxSubCar().setLocationCoordinate(aimCoordinate);
        input.getColdClosingBoxFerry().setLocationCoordinate(aimCoordinate);
        input.getWorkStations().get(workStationIndex).setStatus(WORKING);

        input.getWorkStations().get(workStationIndex).getProduct().add(product);
        input.getColdClosingBoxSubCar().setProduct(null);

        simulationOutput.getClosingBoxInput().setInput(input);

        return simulationOutput;
    }

    /**
     * 前往空闲工岗卸载砂箱事件生成
     * @param simulationOutput
     * @param workStationIndex
     * @return
     */
    public OutBoundMouldingAndClosingBox ColdClosingBoxFullGoWorkStationEventCreate(OutBoundMouldingAndClosingBox simulationOutput, int workStationIndex) {
        ClosingBoxSimulationInput input = simulationOutput.getClosingBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                input.getColdClosingBoxFerry().getLocationCoordinate().getX()) / input.getColdClosingBoxFerry().getFullSpeed();
        double executionTime_2 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getColdClosingBoxFerry().getLocationCoordinate().getY()) / input.getColdClosingBoxSubCar().getFullSpeed();
        double executionTime_3 = input.getColdClosingBoxSubCar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getColdClosingBoxFerry().getLocationCoordinate().getY()) / input.getColdClosingBoxSubCar().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX(),
                input.getColdClosingBoxFerry().getLocationCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getColdClosingBoxSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getColdClosingBoxFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus_1 = new ArrayList<>();
        List<SubCarStatusEnum> endSubCarStatus_2 = new ArrayList<>();
        List<SubCarStatusEnum> endSubCarStatus_3 = new ArrayList<>();
        endSubCarStatus_1.add(FULL_LOAD);
        endSubCarStatus_1.add(TOP_ROD_RAISED);
        endSubCarStatus_1.add(SUB_CAR_NOT_IN_FERRY);
        endSubCarStatus_2.add(EMPTY_LOAD);
        endSubCarStatus_2.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_2.add(SUB_CAR_NOT_IN_FERRY);
        endSubCarStatus_3.add(EMPTY_LOAD);
        endSubCarStatus_3.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_3.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus_1 = new ArrayList<>();
        List<FerryStatusEnum> endFerryStatus_2 = new ArrayList<>();
        endFerryStatus_1.add(CARRY_SUB_CAR);
        endFerryStatus_2.add(NOT_CARRY_SUB_CAR);
        //工岗点状态列表
        List<WorkstationStatusEnum> startWorkstationStatus = new ArrayList<>();
        List<WorkstationStatusEnum> endWorkstationStatus = new ArrayList<>();
        startWorkstationStatus.add(FREE);
        endWorkstationStatus.add(WAIT_PAIR);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getColdClosingBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getColdClosingBoxSubCar().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.DOING_CLOSING_BOX);

        //生成事件
        Event event_1 = allTool.createEvent("100329", "FGCCSCGIWSP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getColdClosingBoxSubCar().getId(), startSubCarStatus_1,false,
                null, startSubCarStatus_1, true, aimCoordinate, input.getColdClosingBoxFerry().getId(), startFerryStatus_1,false,
                null, endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, startWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdClosingBoxFerry().getId(), FERRY, RUNNING, aimCoordinate, product);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getColdClosingBoxSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100330", "FGCCSCIWSP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_3 - executionTime_4, input.getColdClosingBoxSubCar().getId(), startSubCarStatus_1,true, aimCoordinate,
                endSubCarStatus_1, true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getColdClosingBoxFerry().getId(),
                startFerryStatus_1,true, aimCoordinate, endFerryStatus_2, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, startWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdClosingBoxSubCar().getId(), SUB_CAR, RUNNING,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), product);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getColdClosingBoxSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100331", "FGCCSCULIWSP", executionTime_3, input.getTotalTime() - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_4, input.getColdClosingBoxSubCar().getId(), endSubCarStatus_1,true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_2, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getColdClosingBoxFerry().getId(), endFerryStatus_2,true,
                aimCoordinate, endFerryStatus_2, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdClosingBoxSubCar().getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getColdClosingBoxSubCarInGanttIndex()).getEvents().add(event_3);

        //生成事件
        Event event_4 = allTool.createEvent("100332", "FGCCSCBFIWS", executionTime_4, input.getTotalTime() - executionTime_4, input.getTotalTime(),
                input.getColdClosingBoxSubCar().getId(), endSubCarStatus_2,true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(),
                endSubCarStatus_3, true, aimCoordinate, input.getColdClosingBoxFerry().getId(), endFerryStatus_2,true, aimCoordinate,
                endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdClosingBoxSubCar().getId(), SUB_CAR, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getColdClosingBoxSubCarInGanttIndex()).getEvents().add(event_4);

        //改变车辆状态和时间
        input.getColdClosingBoxSubCar().setSubCarState(endSubCarStatus_3);
        input.getColdClosingBoxFerry().setFerryState(endFerryStatus_1);
        input.getColdClosingBoxSubCar().setLocationCoordinate(aimCoordinate);
        input.getColdClosingBoxFerry().setLocationCoordinate(aimCoordinate);
        input.getWorkStations().get(workStationIndex).setStatus(WAIT_PAIR);

        List<Product> products = new ArrayList<>();
        products.add(product);
        input.getWorkStations().get(workStationIndex).setProduct(products);
        input.getColdClosingBoxSubCar().setProduct(null);

        simulationOutput.getClosingBoxInput().setInput(input);

        return simulationOutput;
    }
}
