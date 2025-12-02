package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.closingBox;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductAfootEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductFinishEnum;
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
 * @Created: 2024/7/21 5:19
 */
public class HotClosingBoxEventFlow {
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
    public OutBoundMouldingAndClosingBox HotEmptyLoadInPositionEventCreate(OutBoundMouldingAndClosingBox input) {
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getClosingBoxInput().getInput().getHotClosingBoxPosition().getCoordinate().getY() -
                input.getClosingBoxInput().getInput().getHotClosingBoxFerry().getLocationCoordinate().getY()) /
                input.getClosingBoxInput().getInput().getHotClosingBoxSubCar().getEmptySpeed();
        double executionTime_2 = input.getClosingBoxInput().getInput().getHotClosingBoxSubCar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getClosingBoxInput().getInput().getHotClosingBoxPosition().getCoordinate().getY() -
                input.getClosingBoxInput().getInput().getHotClosingBoxFerry().getLocationCoordinate().getY()) /
                input.getClosingBoxInput().getInput().getHotClosingBoxSubCar().getFullSpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getClosingBoxInput().getInput().getHotClosingBoxSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getClosingBoxInput().getInput().getHotClosingBoxFerry().getFerryState());
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
        pathIds.add(input.getClosingBoxInput().getInput().getHotClosingBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getClosingBoxInput().getInput().getHotClosingBoxPosition().getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100301", "ELCSSCGIHCSP", executionTime_1, input.getClosingBoxInput().getInput().getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getClosingBoxInput().getInput().getTotalTime() - executionTime_2 - executionTime_3, input.getClosingBoxInput().getInput().getHotClosingBoxSubCar().getId(), startSubCarStatus_1,
                true, input.getClosingBoxInput().getInput().getHotClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), endSubCarStatus_1, true,
                input.getClosingBoxInput().getInput().getHotClosingBoxPosition().getCoordinate(), input.getClosingBoxInput().getInput().getHotClosingBoxFerry().getId(), startFerryStatus_1,true,
                input.getClosingBoxInput().getInput().getHotClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_1, true,
                input.getClosingBoxInput().getInput().getHotClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null,
                input.getClosingBoxInput().getInput().getHotClosingBoxPosition().getId(), startPositionStatus, startPositionStatus, null,null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getClosingBoxInput().getInput().getHotClosingBoxSubCar().getId(), SUB_CAR, RUNNING,
                input.getClosingBoxInput().getInput().getHotClosingBoxPosition().getCoordinate(), null);

        //事件加入甘特图
        input.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxInput().getInput().getClosingBoxIndexInput().getHotClosingBoxSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100302", "ELCSCSCIHCSP", executionTime_2, input.getClosingBoxInput().getInput().getTotalTime() - executionTime_2 - executionTime_3,
                input.getClosingBoxInput().getInput().getTotalTime() - executionTime_3, input.getClosingBoxInput().getInput().getHotClosingBoxSubCar().getId(), endSubCarStatus_1,
                true, input.getClosingBoxInput().getInput().getHotClosingBoxPosition().getCoordinate(), endSubCarStatus_2, true,
                input.getClosingBoxInput().getInput().getHotClosingBoxPosition().getCoordinate(), input.getClosingBoxInput().getInput().getHotClosingBoxFerry().getId(),
                endFerryStatus_1,true, input.getClosingBoxInput().getInput().getHotClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(),
                endFerryStatus_1, true, input.getClosingBoxInput().getInput().getHotClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(),
                null,null, null, input.getClosingBoxInput().getInput().getHotClosingBoxPosition().getId(), startPositionStatus, endPositionStatus,
                null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getClosingBoxInput().getInput().getHotClosingBoxSubCar().getId(), SUB_CAR, RISING, null, product);

        //事件加入甘特图
        input.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxInput().getInput().getClosingBoxIndexInput().getHotClosingBoxSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100303", "FLCSCSCGBTF", executionTime_3, input.getClosingBoxInput().getInput().getTotalTime() - executionTime_3,
                input.getClosingBoxInput().getInput().getTotalTime(), input.getClosingBoxInput().getInput().getHotClosingBoxSubCar().getId(), endSubCarStatus_2,true,
                input.getClosingBoxInput().getInput().getHotClosingBoxPosition().getCoordinate(), endSubCarStatus_3, true,
                input.getClosingBoxInput().getInput().getHotClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), input.getClosingBoxInput().getInput().getHotClosingBoxFerry().getId(),
                endFerryStatus_1,true, input.getClosingBoxInput().getInput().getHotClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(),
                endFerryStatus_2, true, input.getClosingBoxInput().getInput().getHotClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(),
                null,null, null, null, null, null, null,null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getClosingBoxInput().getInput().getHotClosingBoxSubCar().getId(), SUB_CAR, RUNNING,
                input.getClosingBoxInput().getInput().getHotClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), product);

        //事件加入甘特图
        input.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxInput().getInput().getClosingBoxIndexInput().getHotClosingBoxSubCarInGanttIndex()).getEvents().add(event_3);

        //改变车辆状态和时间
        input.getClosingBoxInput().getInput().getHotClosingBoxSubCar().setSubCarState(endSubCarStatus_3);
        input.getClosingBoxInput().getInput().getHotClosingBoxFerry().setFerryState(endFerryStatus_2);
        input.getClosingBoxInput().getInput().getHotClosingBoxPosition().setStatus(UNOCCUPIED);


        input.getClosingBoxInput().getInput().getHotClosingBoxSubCar().setProduct(product);

        input.getClosingBoxInput().getInput().getHotClosingBoxPosition().setProduct(null);

        return input;
    }

    /**
     * 在工岗装载事件生成
     * @param simulationOutput
     * @param workStationIndex
     * @return
     */
    public OutBoundMouldingAndClosingBox HotClosingBoxEmptyLoadInWorkStationEventCreate(OutBoundMouldingAndClosingBox simulationOutput, Integer workStationIndex) throws SimulationException {
        ClosingBoxSimulationInput input = simulationOutput.getClosingBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getWorkStations().get(input.getClosingBoxIndexInput().getHotClosingBoxWorkStationIndex_1()).getWorkPositionCoordinate().getY() -
                input.getHotClosingBoxFerry().getLocationCoordinate().getY()) / input.getHotClosingBoxSubCar().getEmptySpeed();
        double executionTime_2 = input.getHotClosingBoxSubCar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getWorkStations().get(input.getClosingBoxIndexInput().getHotClosingBoxWorkStationIndex_1()).getWorkPositionCoordinate().getY() -
                input.getHotClosingBoxFerry().getLocationCoordinate().getY()) / input.getHotClosingBoxSubCar().getFullSpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getHotClosingBoxSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getHotClosingBoxFerry().getFerryState());
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
        pathIds.add(input.getHotClosingBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        List<Product> products = new ArrayList<>(input.getWorkStations().get(workStationIndex).getProduct());
        Product product = tools.createClosingBoxProduct(products);

        //生成事件
        Event event_1 = allTool.createEvent("100304", "ELCSCSCGIWSP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_2 - executionTime_3, input.getHotClosingBoxSubCar().getId(), startSubCarStatus_1,true,
                input.getHotClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), endSubCarStatus_1, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getHotClosingBoxFerry().getId(), startFerryStatus_1,true,
                input.getHotClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_1, true,
                input.getHotClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, startWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotClosingBoxSubCar().getId(), SUB_CAR, RUNNING,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getHotClosingBoxSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100305", "ELCSCSCIWSP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_3, input.getHotClosingBoxSubCar().getId(), endSubCarStatus_1,true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_2, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getHotClosingBoxFerry().getId(), endFerryStatus_1,true,
                input.getHotClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_1, true,
                input.getHotClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, endWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotClosingBoxSubCar().getId(), SUB_CAR, RISING, null, product);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getHotClosingBoxSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100306", "FLHCSCGBTFWS", executionTime_3, input.getTotalTime() - executionTime_3, input.getTotalTime(),
                input.getHotClosingBoxSubCar().getId(), endSubCarStatus_2,true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(),
                endSubCarStatus_3, true, input.getHotClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), input.getHotClosingBoxFerry().getId(),
                endFerryStatus_1,true, input.getHotClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_2, true,
                input.getHotClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotClosingBoxSubCar().getId(), SUB_CAR, RUNNING,
                input.getHotClosingBoxFerry().getLocationCoordinate().coordinateDeepCopy(), product);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getHotClosingBoxSubCarInGanttIndex()).getEvents().add(event_3);

        //改变车辆状态和时间
        input.getHotClosingBoxSubCar().setSubCarState(endSubCarStatus_3);
        input.getHotClosingBoxFerry().setFerryState(endFerryStatus_2);
        input.getWorkStations().get(workStationIndex).setStatus(FREE);

        input.getHotClosingBoxSubCar().setProduct(product);
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
    public OutBoundMouldingAndClosingBox HotClosingBoxEmptyGoWorkStationEventCreate(OutBoundMouldingAndClosingBox simulationOutput, int workStationIndex) {
        ClosingBoxSimulationInput input = simulationOutput.getClosingBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                input.getHotClosingBoxFerry().getLocationCoordinate().getX()) / input.getHotClosingBoxFerry().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX(),
                input.getHotClosingBoxFerry().getLocationCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getHotClosingBoxSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>();
        startFerryStatus_1.addAll(input.getHotClosingBoxFerry().getFerryState());
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
        pathIds.add(input.getHotClosingBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);


        //生成事件
        Event event = allTool.createEvent("100307", "EGCSCSCGIWSP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getHotClosingBoxSubCar().getId(), startSubCarStatus_1,false, null, endSubCarStatus, true,
                aimCoordinate, input.getHotClosingBoxFerry().getId(), startFerryStatus_1,false, null, endFerryStatus,
                true, aimCoordinate, null,null, null, null, null,
                null, input.getWorkStations().get(workStationIndex).getId(),startWorkstationStatus, endWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotClosingBoxFerry().getId(), FERRY, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getHotClosingBoxSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getHotClosingBoxSubCar().setSubCarState(endSubCarStatus);
        input.getHotClosingBoxFerry().setFerryState(endFerryStatus);
        input.getHotClosingBoxSubCar().setLocationCoordinate(aimCoordinate);
        input.getHotClosingBoxFerry().setLocationCoordinate(aimCoordinate);

        simulationOutput.getClosingBoxInput().setInput(input);

        return simulationOutput;
    }

    /**
     * 前往点位装载事件生成
     * @param simulationOutput
     * @return
     */
    public OutBoundMouldingAndClosingBox HotClosingBoxEmptyGoHotPosotionEventCreate(OutBoundMouldingAndClosingBox simulationOutput) {
        ClosingBoxSimulationInput input = simulationOutput.getClosingBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getHotClosingBoxPosition().getCoordinate().getX() -
                input.getHotClosingBoxFerry().getLocationCoordinate().getX()) / input.getHotClosingBoxFerry().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getHotClosingBoxPosition().getCoordinate().getX(), input.getHotClosingBoxFerry().getLocationCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getHotClosingBoxSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getHotClosingBoxFerry().getFerryState());
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
        pathIds.add(input.getHotClosingBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);


        //生成事件
        Event event = allTool.createEvent("100308", "EGCSCSCGIHMP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getHotClosingBoxSubCar().getId(), startSubCarStatus_1,false, null, endSubCarStatus, true,
                aimCoordinate, input.getHotClosingBoxFerry().getId(), startFerryStatus_1, false, null, endFerryStatus,
                true, aimCoordinate, null,null, null, input.getHotClosingBoxPosition().getId(),
                startPositionStatus, endPositionStatus, null, null, null, pathIds, pathStatusEnums,
                pathStatusEnums, input.getHotClosingBoxFerry().getId(), FERRY, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getHotClosingBoxSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getHotClosingBoxSubCar().setSubCarState(endSubCarStatus);
        input.getHotClosingBoxFerry().setFerryState(endFerryStatus);
        input.getHotClosingBoxSubCar().setLocationCoordinate(aimCoordinate);
        input.getHotClosingBoxFerry().setLocationCoordinate(aimCoordinate);

        simulationOutput.getClosingBoxInput().setInput(input);

        return simulationOutput;
    }

    /**
     * 前往合箱完成前往浇筑点位卸载事件生成
     * @param simulationOutput
     * @return
     */
    public OutBoundMouldingAndClosingBox HotClosingBoxFullUnLoadPositionEventCreate(OutBoundMouldingAndClosingBox simulationOutput) {
        ClosingBoxSimulationInput input = simulationOutput.getClosingBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getHotClosingBoxGoPourPosition().getCoordinate().getY() -
                input.getHotClosingBoxFerry().getLocationCoordinate().getY()) / input.getHotClosingBoxSubCar().getFullSpeed();
        double executionTime_2 = input.getHotClosingBoxSubCar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getHotClosingBoxGoPourPosition().getCoordinate().getY() -
                input.getHotClosingBoxFerry().getLocationCoordinate().getY()) / input.getHotClosingBoxSubCar().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getHotClosingBoxGoPourPosition().getCoordinate().getX(),
                input.getHotClosingBoxFerry().getLocationCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getHotClosingBoxSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getHotClosingBoxFerry().getFerryState());
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
        pathIds.add(input.getHotClosingBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getHotClosingBoxSubCar().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.GOING_POURING);
        product.setProductFinish(ProductFinishEnum.FINISH_CLOSING_BOX);

        //生成事件
        Event event_1 = allTool.createEvent("100318", "FGHCSCSCILP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_2 - executionTime_3, input.getHotClosingBoxSubCar().getId(), startSubCarStatus_1,true, aimCoordinate,
                endSubCarStatus_1, true, input.getHotClosingBoxGoPourPosition().getCoordinate(), input.getHotClosingBoxFerry().getId(), startFerryStatus_1,
                true, aimCoordinate, endFerryStatus_2, true, aimCoordinate, null,null, null,
                input.getHotClosingBoxGoPourPosition().getId(), startPositionStatus, startPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotClosingBoxSubCar().getId(), SUB_CAR, RUNNING, input.getHotClosingBoxGoPourPosition().getCoordinate(), product);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getHotClosingBoxSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100319", "FGCSCSCULILP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_3, input.getHotClosingBoxSubCar().getId(), endSubCarStatus_1,true, input.getHotClosingBoxGoPourPosition().getCoordinate(),
                endSubCarStatus_2, true, input.getHotClosingBoxGoPourPosition().getCoordinate(), input.getHotClosingBoxFerry().getId(), endFerryStatus_2,
                true, aimCoordinate, endFerryStatus_2, true, aimCoordinate, null,null, null,
                input.getHotClosingBoxGoPourPosition().getId(), startPositionStatus, endPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotClosingBoxSubCar().getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getHotClosingBoxSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100320", "FGHCSCBFILP", executionTime_3, input.getTotalTime() - executionTime_3, input.getTotalTime(),
                input.getHotClosingBoxSubCar().getId(), endSubCarStatus_2,true, input.getHotClosingBoxGoPourPosition().getCoordinate(), endSubCarStatus_3,
                true, aimCoordinate, input.getHotClosingBoxFerry().getId(), endFerryStatus_2,true, aimCoordinate,
                endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotClosingBoxSubCar().getId(), SUB_CAR, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getHotClosingBoxSubCarInGanttIndex()).getEvents().add(event_3);

        //改变车辆状态和时间
        input.getHotClosingBoxSubCar().setSubCarState(endSubCarStatus_3);
        input.getHotClosingBoxFerry().setFerryState(endFerryStatus_1);
        input.getHotClosingBoxSubCar().setLocationCoordinate(aimCoordinate);
        input.getHotClosingBoxFerry().setLocationCoordinate(aimCoordinate);
        input.getHotClosingBoxGoPourPosition().setStatus(OCCUPIED);

        input.getHotClosingBoxGoPourPosition().setProduct(product);
        input.getHotClosingBoxSubCar().setProduct(null);

        simulationOutput.getClosingBoxInput().setInput(input);

        return simulationOutput;
    }

    /**
     * 前往合箱完成前往浇筑点位卸载事件生成
     * @param simulationOutput
     * @return
     */
    public OutBoundMouldingAndClosingBox HotClosingBoxFullGoPositionEventCreate(OutBoundMouldingAndClosingBox simulationOutput) {
        ClosingBoxSimulationInput input = simulationOutput.getClosingBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getHotClosingBoxGoPourPosition().getCoordinate().getX() -
                input.getHotClosingBoxFerry().getLocationCoordinate().getX()) / input.getHotClosingBoxFerry().getFullSpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getHotClosingBoxGoPourPosition().getCoordinate().getX(),
                input.getHotClosingBoxFerry().getLocationCoordinate().getY());
        //判断母车所在工岗的位置标识
//        int workStationIndex = tools.judgeHotFerryInWhichWorkstation(input.getHotClosingBoxFerry(), input.getWorkStations(),
//                input.getClosingBoxIndexInput());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getHotClosingBoxSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getHotClosingBoxFerry().getFerryState());
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
        pathIds.add(input.getHotClosingBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);


        //生成事件
        Event event = allTool.createEvent("100317", "FGHCSCGILP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getHotClosingBoxSubCar().getId(), startSubCarStatus_1,false, null, endSubCarStatus, true,
                aimCoordinate, input.getHotClosingBoxFerry().getId(), startFerryStatus_1,false, null,
                endFerryStatus, true, aimCoordinate, null,null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotClosingBoxFerry().getId(), FERRY, RUNNING, aimCoordinate,
                input.getHotClosingBoxSubCar().getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getHotClosingBoxSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getHotClosingBoxSubCar().setSubCarState(endSubCarStatus);
        input.getHotClosingBoxFerry().setFerryState(endFerryStatus);
        input.getHotClosingBoxSubCar().setLocationCoordinate(aimCoordinate);
        input.getHotClosingBoxFerry().setLocationCoordinate(aimCoordinate);

        simulationOutput.getClosingBoxInput().setInput(input);

        return simulationOutput;
    }

    /**
     * 前往待配对工岗卸载砂箱事件生成
     * @param simulationOutput
     * @param workStationIndex
     * @return
     */
    public OutBoundMouldingAndClosingBox HotClosingBoxFullGoPairWorkStationEventCreate(OutBoundMouldingAndClosingBox simulationOutput, int workStationIndex) {
        ClosingBoxSimulationInput input = simulationOutput.getClosingBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                input.getHotClosingBoxFerry().getLocationCoordinate().getX()) / input.getHotClosingBoxFerry().getFullSpeed();
        double executionTime_2 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getHotClosingBoxFerry().getLocationCoordinate().getY()) / input.getHotClosingBoxSubCar().getFullSpeed();
        double executionTime_3 = input.getHotClosingBoxSubCar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getHotClosingBoxFerry().getLocationCoordinate().getY()) / input.getHotClosingBoxSubCar().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX(),
                input.getHotClosingBoxFerry().getLocationCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getHotClosingBoxSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getHotClosingBoxFerry().getFerryState());
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
        pathIds.add(input.getHotClosingBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getHotClosingBoxSubCar().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.DOING_CLOSING_BOX);

        //生成事件
        Event event_1 = allTool.createEvent("100313", "FGCSCSCGIWSPP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getHotClosingBoxSubCar().getId(), startSubCarStatus_1,false,
                null, startSubCarStatus_1, true, aimCoordinate, input.getHotClosingBoxFerry().getId(), startFerryStatus_1,false,
                null, endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, startWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotClosingBoxFerry().getId(), FERRY, RUNNING, aimCoordinate, product);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getHotClosingBoxSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100314", "FGHCSCIWSPP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_3 - executionTime_4, input.getHotClosingBoxSubCar().getId(), startSubCarStatus_1,true, aimCoordinate,
                endSubCarStatus_1, true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getHotClosingBoxFerry().getId(),
                startFerryStatus_1,true, aimCoordinate, endFerryStatus_2, true, aimCoordinate, null,null,
                null, null, null, null, input.getWorkStations().get(workStationIndex).getId(),
                startWorkstationStatus, startWorkstationStatus, pathIds, pathStatusEnums, pathStatusEnums, input.getHotClosingBoxSubCar().getId(), SUB_CAR, RUNNING,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), product);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getHotClosingBoxSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100315", "FGHCSCULIWSPP", executionTime_3, input.getTotalTime() - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_4, input.getHotClosingBoxSubCar().getId(), endSubCarStatus_1,true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_2, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getHotClosingBoxFerry().getId(), endFerryStatus_2,true,
                aimCoordinate, endFerryStatus_2, true, aimCoordinate, null,null, null, null,
                null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotClosingBoxSubCar().getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getHotClosingBoxSubCarInGanttIndex()).getEvents().add(event_3);

        //生成事件
        Event event_4 = allTool.createEvent("100316", "FGHCSCBFIWSP", executionTime_4, input.getTotalTime() - executionTime_4, input.getTotalTime(),
                input.getHotClosingBoxSubCar().getId(), endSubCarStatus_2,true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(),
                endSubCarStatus_3, true, aimCoordinate, input.getHotClosingBoxFerry().getId(), endFerryStatus_2,true, aimCoordinate,
                endFerryStatus_1, true, aimCoordinate, null,null, null, null, null,
                null, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getHotClosingBoxSubCar().getId(), SUB_CAR, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getHotClosingBoxSubCarInGanttIndex()).getEvents().add(event_4);

        //改变车辆状态和时间
        input.getHotClosingBoxSubCar().setSubCarState(endSubCarStatus_3);
        input.getHotClosingBoxFerry().setFerryState(endFerryStatus_1);
        input.getHotClosingBoxSubCar().setLocationCoordinate(aimCoordinate);
        input.getHotClosingBoxFerry().setLocationCoordinate(aimCoordinate);
        input.getWorkStations().get(workStationIndex).setStatus(WORKING);

        input.getWorkStations().get(workStationIndex).getProduct().add(product);
        input.getHotClosingBoxSubCar().setProduct(null);

        simulationOutput.getClosingBoxInput().setInput(input);

        return simulationOutput;
    }

    /**
     * 前往空闲工岗卸载砂箱事件生成
     * @param simulationOutput
     * @param workStationIndex
     * @return
     */
    public OutBoundMouldingAndClosingBox HotClosingBoxFullGoWorkStationEventCreate(OutBoundMouldingAndClosingBox simulationOutput, int workStationIndex) {
        ClosingBoxSimulationInput input = simulationOutput.getClosingBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                input.getHotClosingBoxFerry().getLocationCoordinate().getX()) / input.getHotClosingBoxFerry().getFullSpeed();
        double executionTime_2 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getHotClosingBoxFerry().getLocationCoordinate().getY()) / input.getHotClosingBoxSubCar().getFullSpeed();
        double executionTime_3 = input.getHotClosingBoxSubCar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getHotClosingBoxFerry().getLocationCoordinate().getY()) / input.getHotClosingBoxSubCar().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX(),
                input.getHotClosingBoxFerry().getLocationCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getHotClosingBoxSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getHotClosingBoxFerry().getFerryState());
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
        pathIds.add(input.getHotClosingBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getHotClosingBoxSubCar().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.DOING_CLOSING_BOX);

        //生成事件
        Event event_1 = allTool.createEvent("100309", "FGHCSCGIWSP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getHotClosingBoxSubCar().getId(), startSubCarStatus_1,false,
                null, startSubCarStatus_1, true, aimCoordinate, input.getHotClosingBoxFerry().getId(), startFerryStatus_1,false,
                null, endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, startWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotClosingBoxFerry().getId(), FERRY, RUNNING, aimCoordinate, product);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getHotClosingBoxSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100310", "FGHCSCIWSP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_3 - executionTime_4, input.getHotClosingBoxSubCar().getId(), startSubCarStatus_1,true, aimCoordinate,
                endSubCarStatus_1, true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getHotClosingBoxFerry().getId(),
                startFerryStatus_1,true, aimCoordinate, endFerryStatus_2, true, aimCoordinate, null,null,
                null, null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus,
                startWorkstationStatus, pathIds, pathStatusEnums, pathStatusEnums, input.getHotClosingBoxSubCar().getId(), SUB_CAR, RUNNING,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), product);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getHotClosingBoxSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100311", "FGHCSCULIWSP", executionTime_3, input.getTotalTime() - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_4, input.getHotClosingBoxSubCar().getId(), endSubCarStatus_1,true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_2, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getHotClosingBoxFerry().getId(), endFerryStatus_2,true,
                aimCoordinate, endFerryStatus_2, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus,
                null, pathIds, pathStatusEnums, pathStatusEnums, input.getHotClosingBoxSubCar().getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getHotClosingBoxSubCarInGanttIndex()).getEvents().add(event_3);

        //生成事件
        Event event_4 = allTool.createEvent("100312", "FGHCSCBFIWS", executionTime_4, input.getTotalTime() - executionTime_4, input.getTotalTime(),
                input.getHotClosingBoxSubCar().getId(), endSubCarStatus_2,true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(),
                endSubCarStatus_3, true, aimCoordinate, input.getHotClosingBoxFerry().getId(), endFerryStatus_2,true, aimCoordinate,
                endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotClosingBoxSubCar().getId(), SUB_CAR, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getClosingBoxInput().getGanttChart().getEventLinkGantts().get(input.getClosingBoxIndexInput().getHotClosingBoxSubCarInGanttIndex()).getEvents().add(event_4);

        //改变车辆状态和时间
        input.getHotClosingBoxSubCar().setSubCarState(endSubCarStatus_3);
        input.getHotClosingBoxFerry().setFerryState(endFerryStatus_1);
        input.getHotClosingBoxSubCar().setLocationCoordinate(aimCoordinate);
        input.getHotClosingBoxFerry().setLocationCoordinate(aimCoordinate);
        input.getWorkStations().get(workStationIndex).setStatus(WAIT_PAIR);

        List<Product> products = new ArrayList<>();
        products.add(product);
        input.getWorkStations().get(workStationIndex).setProduct(products);
        input.getHotClosingBoxSubCar().setProduct(null);

        simulationOutput.getClosingBoxInput().setInput(input);

        return simulationOutput;
    }
}
