package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.pouring;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.CoolingSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.PouringSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.WaitingPourSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.waitingPour.AfterClosingBoxAllLink;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.*;
import static cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum.PATH_UNOCCUPIED;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum.CARRY_SUB_CAR;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum.NOT_CARRY_SUB_CAR;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum.*;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/31 15:18
 */
@NoArgsConstructor
public class PouringEventFlow {
    /**
     * 仿真总工具类
     */
    private SimulationTool allTool = new SimulationTool();

    public AfterClosingBoxAllLink emptyGoUpWaitingPourPositionEventCreate(AfterClosingBoxAllLink simulationOutput, int pathIndex, int positionIndex) {
        PouringSimulationInput input = simulationOutput.getPouringInput().getInput();
        WaitingPourSimulationInput waitingPourInput = simulationOutput.getWaitingPourInput().getInput();
        //母车前往的目标地点坐标
        Coordinate aimCoordinate = new Coordinate(input.getPouringFerry().getLocationCoordinate().getX(),
                waitingPourInput.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate().getY());
        //发生母车前往热砂合箱点位，子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getPouringFerry().getLocationCoordinate().getY() -
                aimCoordinate.getY()) / input.getPouringFerry().getEmptySpeed();
        double executionTime_2 = Math.abs(input.getPouringSubCar().getLocationCoordinate().getX() -
                waitingPourInput.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getPouringSubCar().getEmptySpeed();
        double executionTime_3 = input.getPouringSubCar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getPouringSubCar().getLocationCoordinate().getX() -
                waitingPourInput.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getPouringSubCar().getFullSpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getPouringSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getPouringFerry().getFerryState());
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
        pathIds.add(input.getPouringPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = waitingPourInput.getUpPositions().get(pathIndex).get(positionIndex).getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100501", "ELPFGIUWPP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getPouringSubCar().getId(), startSubCarStatus_1,
                false, null, startSubCarStatus_1, true, aimCoordinate, input.getPouringFerry().getId(),
                startFerryStatus_1,false, null, endFerryStatus_2, true, aimCoordinate, null,null,
                null, waitingPourInput.getUpPositions().get(pathIndex).get(positionIndex).getId(), startPositionStatus, startPositionStatus, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getPouringFerry().getId(), ExecuteTypeEnum.FERRY,
                InstructionsEnum.RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getPouringInput().getGanttChart().getEventLinkGantts().get(input.getPouringIndexInput().getPouringSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100502", "ELPSCGIUWPP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_3 - executionTime_4, input.getPouringSubCar().getId(), startSubCarStatus_1, true, aimCoordinate,
                endSubCarStatus_1, true, waitingPourInput.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate(), input.getPouringFerry().getId(),
                endFerryStatus_2, true, aimCoordinate, endFerryStatus_1, true, aimCoordinate, null,null, null,
                waitingPourInput.getUpPositions().get(pathIndex).get(positionIndex).getId(), startPositionStatus, startPositionStatus, null,null,
                null, pathIds, pathStatusEnums, pathStatusEnums, input.getPouringSubCar().getId(), ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, waitingPourInput.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getPouringInput().getGanttChart().getEventLinkGantts().get(input.getPouringIndexInput().getPouringSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100503", "ELPCSULIUWPP", executionTime_3, input.getTotalTime() - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_4, input.getPouringSubCar().getId(), endSubCarStatus_1, true,
                waitingPourInput.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate(), endSubCarStatus_2, true,
                waitingPourInput.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate(), input.getPouringFerry().getId(), endFerryStatus_1,
                true, aimCoordinate, endFerryStatus_1, true, aimCoordinate, null, null,
                null, waitingPourInput.getUpPositions().get(pathIndex).get(positionIndex).getId(), startPositionStatus, endPositionStatus,
                null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getPouringSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.RISING, null, product);

        //事件加入甘特图
        simulationOutput.getPouringInput().getGanttChart().getEventLinkGantts().get(input.getPouringIndexInput().getPouringSubCarInGanttIndex()).getEvents().add(event_3);

        //生成事件
        Event event_4 = allTool.createEvent("100504", "FLPSCGBTFIU", executionTime_3, input.getTotalTime() - executionTime_4,
                input.getTotalTime(), input.getPouringSubCar().getId(), endSubCarStatus_2,true,
                waitingPourInput.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate(), endSubCarStatus_3, true, aimCoordinate,
                input.getPouringFerry().getId(), endFerryStatus_1,true, aimCoordinate, endFerryStatus_2, true, aimCoordinate,
                null,null, null, null, null, null,
                null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getPouringSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.RUNNING, aimCoordinate, product);

        //事件加入甘特图
        simulationOutput.getPouringInput().getGanttChart().getEventLinkGantts().get(input.getPouringIndexInput().getPouringSubCarInGanttIndex()).getEvents().add(event_4);

        //改变车辆状态和时间
        input.getPouringSubCar().setSubCarState(endSubCarStatus_3);
        input.getPouringFerry().setFerryState(endFerryStatus_2);
        input.getPouringSubCar().setLocationCoordinate(aimCoordinate);
        input.getPouringFerry().setLocationCoordinate(aimCoordinate);
        waitingPourInput.getUpPositions().get(pathIndex).get(positionIndex).setStatus(PositionStatusEnum.UNOCCUPIED);


        waitingPourInput.getUpPositions().get(pathIndex).get(positionIndex).setProduct(null);
        input.getPouringSubCar().setProduct(product);

        simulationOutput.getWaitingPourInput().setInput(waitingPourInput);
        simulationOutput.getPouringInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink emptyGoDownWaitingPourPositionEventCreate(AfterClosingBoxAllLink simulationOutput, int pathIndex, int positionIndex) {
        PouringSimulationInput input = simulationOutput.getPouringInput().getInput();
        WaitingPourSimulationInput waitingPourInput = simulationOutput.getWaitingPourInput().getInput();
        //母车前往的目标地点坐标
        Coordinate aimCoordinate = new Coordinate(input.getPouringFerry().getLocationCoordinate().getX(),
                waitingPourInput.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate().getY());
        //发生母车前往热砂合箱点位，子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getPouringFerry().getLocationCoordinate().getY() -
                aimCoordinate.getY()) / input.getPouringFerry().getEmptySpeed();
        double executionTime_2 = Math.abs(input.getPouringSubCar().getLocationCoordinate().getX() -
                waitingPourInput.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getPouringSubCar().getEmptySpeed();
        double executionTime_3 = input.getPouringSubCar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getPouringSubCar().getLocationCoordinate().getX() -
                waitingPourInput.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getPouringSubCar().getFullSpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getPouringSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getPouringFerry().getFerryState());
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
        pathIds.add(input.getPouringPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = waitingPourInput.getDownPositions().get(pathIndex).get(positionIndex).getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100505", "ELPFGIDWPP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getPouringSubCar().getId(), startSubCarStatus_1,
                false, null, startSubCarStatus_1, true, aimCoordinate, input.getPouringFerry().getId(),
                startFerryStatus_1,false, null, endFerryStatus_2, true, aimCoordinate, null,null,
                null, waitingPourInput.getDownPositions().get(pathIndex).get(positionIndex).getId(), startPositionStatus, startPositionStatus, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getPouringFerry().getId(), ExecuteTypeEnum.FERRY,
                InstructionsEnum.RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getPouringInput().getGanttChart().getEventLinkGantts().get(input.getPouringIndexInput().getPouringSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100506", "ELPSCGIDWPP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_3 - executionTime_4, input.getPouringSubCar().getId(), startSubCarStatus_1, true, aimCoordinate,
                endSubCarStatus_1, true, waitingPourInput.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate(), input.getPouringFerry().getId(),
                endFerryStatus_2, true, aimCoordinate, endFerryStatus_1, true, aimCoordinate, null,null, null,
                waitingPourInput.getDownPositions().get(pathIndex).get(positionIndex).getId(), startPositionStatus, startPositionStatus, null,null,
                null, pathIds, pathStatusEnums, pathStatusEnums, input.getPouringSubCar().getId(), ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, waitingPourInput.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getPouringInput().getGanttChart().getEventLinkGantts().get(input.getPouringIndexInput().getPouringSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100507", "ELPCSULIDWPP", executionTime_3, input.getTotalTime() - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_4, input.getPouringSubCar().getId(), endSubCarStatus_1, true,
                waitingPourInput.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate(), endSubCarStatus_2, true,
                waitingPourInput.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate(), input.getPouringFerry().getId(), endFerryStatus_1,
                true, aimCoordinate, endFerryStatus_1, true, aimCoordinate, null, null,
                null, waitingPourInput.getDownPositions().get(pathIndex).get(positionIndex).getId(), startPositionStatus, endPositionStatus,
                null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getPouringSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.RISING, null, product);

        //事件加入甘特图
        simulationOutput.getPouringInput().getGanttChart().getEventLinkGantts().get(input.getPouringIndexInput().getPouringSubCarInGanttIndex()).getEvents().add(event_3);

        //生成事件
        Event event_4 = allTool.createEvent("100508", "FLPSCGBTFID", executionTime_3, input.getTotalTime() - executionTime_4,
                input.getTotalTime(), input.getPouringSubCar().getId(), endSubCarStatus_2,true,
                waitingPourInput.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate(), endSubCarStatus_3, true,
                aimCoordinate, input.getPouringFerry().getId(), endFerryStatus_1,true, aimCoordinate, endFerryStatus_2, true,
                aimCoordinate, null,null, null, null, null,
                null, null,null, null, pathIds, pathStatusEnums,
                pathStatusEnums, input.getPouringSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.RUNNING, aimCoordinate, product);

        //事件加入甘特图
        simulationOutput.getPouringInput().getGanttChart().getEventLinkGantts().get(input.getPouringIndexInput().getPouringSubCarInGanttIndex()).getEvents().add(event_4);

        //改变车辆状态和时间
        input.getPouringSubCar().setSubCarState(endSubCarStatus_3);
        input.getPouringFerry().setFerryState(endFerryStatus_2);
        input.getPouringSubCar().setLocationCoordinate(aimCoordinate);
        input.getPouringFerry().setLocationCoordinate(aimCoordinate);
        waitingPourInput.getDownPositions().get(pathIndex).get(positionIndex).setStatus(PositionStatusEnum.UNOCCUPIED);


        waitingPourInput.getDownPositions().get(pathIndex).get(positionIndex).setProduct(null);
        input.getPouringSubCar().setProduct(product);

        simulationOutput.getWaitingPourInput().setInput(waitingPourInput);
        simulationOutput.getPouringInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink emptyGoWorkStationLoadEventCreate(AfterClosingBoxAllLink simulationOutput, int workStationIndex) {
        PouringSimulationInput input = simulationOutput.getPouringInput().getInput();
        //母车前往的目标地点坐标
        Coordinate aimCoordinate = new Coordinate(input.getPouringFerry().getLocationCoordinate().getX(),
                input.getWorkingStations().get(workStationIndex).getWorkPositionCoordinate().getY());
        //发生母车前往热砂合箱点位，子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getPouringFerry().getLocationCoordinate().getY() -
                input.getWorkingStations().get(workStationIndex).getWorkPositionCoordinate().getY()) / input.getPouringFerry().getEmptySpeed();
        double executionTime_2 = Math.abs(input.getPouringSubCar().getLocationCoordinate().getX() -
                input.getWorkingStations().get(workStationIndex).getWorkPositionCoordinate().getX()) / input.getPouringSubCar().getEmptySpeed();
        double executionTime_3 = input.getPouringSubCar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getPouringSubCar().getLocationCoordinate().getX() -
                input.getWorkingStations().get(workStationIndex).getWorkPositionCoordinate().getX()) / input.getPouringSubCar().getFullSpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getPouringSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getPouringFerry().getFerryState());
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
        //工岗状态列表
        List<WorkstationStatusEnum> startWorkstationStatus = new ArrayList<>();
        List<WorkstationStatusEnum> endWorkstationStatus = new ArrayList<>();
        startWorkstationStatus.add(WorkstationStatusEnum.WAIT_TRANSPORT);
        endWorkstationStatus.add(WorkstationStatusEnum.FREE);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPouringPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getWorkingStations().get(workStationIndex).getProduct().getFirst().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100509", "ELPFGIPWS", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getPouringSubCar().getId(), startSubCarStatus_1,
                false, null, startSubCarStatus_1, true, aimCoordinate, input.getPouringFerry().getId(),
                startFerryStatus_1,false, null, endFerryStatus_2, true, aimCoordinate, null,null,
                null, null, null, null, input.getWorkingStations().get(workStationIndex).getId(), startWorkstationStatus,
                startWorkstationStatus, pathIds, pathStatusEnums, pathStatusEnums, input.getPouringFerry().getId(), ExecuteTypeEnum.FERRY, InstructionsEnum.RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getPouringInput().getGanttChart().getEventLinkGantts().get(input.getPouringIndexInput().getPouringSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100510", "ELPSCGIPWS", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_3 - executionTime_4, input.getPouringSubCar().getId(), startSubCarStatus_1, true, aimCoordinate,
                endSubCarStatus_1, true, input.getWorkingStations().get(workStationIndex).getWorkPositionCoordinate(), input.getPouringFerry().getId(), endFerryStatus_2,
                true, aimCoordinate, endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkingStations().get(workStationIndex).getId(), startWorkstationStatus, startWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getPouringSubCar().getId(), ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, input.getWorkingStations().get(workStationIndex).getWorkPositionCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getPouringInput().getGanttChart().getEventLinkGantts().get(input.getPouringIndexInput().getPouringSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100511", "ELPCSULIPWS", executionTime_3, input.getTotalTime() - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_4, input.getPouringSubCar().getId(), endSubCarStatus_1, true,
                input.getWorkingStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_2, true,
                input.getWorkingStations().get(workStationIndex).getWorkPositionCoordinate(), input.getPouringFerry().getId(), endFerryStatus_1,true,
                aimCoordinate, endFerryStatus_1, true, aimCoordinate, null, null, null, null,
                null, null, input.getWorkingStations().get(workStationIndex).getId(), startWorkstationStatus, endWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getPouringSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.RISING, null, product);

        //事件加入甘特图
        simulationOutput.getPouringInput().getGanttChart().getEventLinkGantts().get(input.getPouringIndexInput().getPouringSubCarInGanttIndex()).getEvents().add(event_3);

        //生成事件
        Event event_4 = allTool.createEvent("100512", "FLPSCGBTFIPWS", executionTime_3, input.getTotalTime() - executionTime_4,
                input.getTotalTime(), input.getPouringSubCar().getId(), endSubCarStatus_2,true, input.getWorkingStations().get(workStationIndex).getWorkPositionCoordinate(),
                endSubCarStatus_3, true, aimCoordinate, input.getPouringFerry().getId(), endFerryStatus_1,true, aimCoordinate, endFerryStatus_2,
                true, aimCoordinate, null,null, null, null, null, null,
                null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getPouringSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.RUNNING, aimCoordinate, product);

        //事件加入甘特图
        simulationOutput.getPouringInput().getGanttChart().getEventLinkGantts().get(input.getPouringIndexInput().getPouringSubCarInGanttIndex()).getEvents().add(event_4);

        //改变车辆状态和时间
        input.getPouringSubCar().setSubCarState(endSubCarStatus_3);
        input.getPouringFerry().setFerryState(endFerryStatus_2);
        input.getPouringSubCar().setLocationCoordinate(aimCoordinate);
        input.getPouringFerry().setLocationCoordinate(aimCoordinate);
        input.getWorkingStations().get(workStationIndex).setStatus(WorkstationStatusEnum.FREE);


        input.getWorkingStations().get(workStationIndex).setProduct(new ArrayList<>());
        input.getPouringSubCar().setProduct(product);

        simulationOutput.getPouringInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink fullGoUpCoolingPositionEventCreate(AfterClosingBoxAllLink simulationOutput, int pathIndex, int positionIndex) {
        PouringSimulationInput input = simulationOutput.getPouringInput().getInput();
        CoolingSimulationInput coolingInput = simulationOutput.getCoolingInput().getInput();
        //母车前往的目标地点坐标
        Coordinate aimCoordinate = new Coordinate(input.getPouringFerry().getLocationCoordinate().getX(),
                coolingInput.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate().getY());
        //发生母车前往热砂合箱点位，子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getPouringFerry().getLocationCoordinate().getY() -
                aimCoordinate.getY()) / input.getPouringFerry().getFullSpeed();
        double executionTime_2 = Math.abs(input.getPouringSubCar().getLocationCoordinate().getX() -
                coolingInput.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getPouringSubCar().getFullSpeed();
        double executionTime_3 = input.getPouringSubCar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getPouringSubCar().getLocationCoordinate().getX() -
                coolingInput.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getPouringSubCar().getEmptySpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getPouringSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getPouringFerry().getFerryState());
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
        endFerryStatus_1.add(NOT_CARRY_SUB_CAR);
        endFerryStatus_2.add(CARRY_SUB_CAR);
        //点位状态列表
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus_1 = new ArrayList<>();
        startPositionStatus.add(UNOCCUPIED);
        endPositionStatus.add(OCCUPIED);
        endPositionStatus_1.add(FINISH_COOLING);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(coolingInput.getCoolingGoOpenBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getPouringSubCar().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.GOING_COOLING);
        product.setProductFinish(ProductFinishEnum.FINISH_POURING);

        //生成事件
        Event event_1 = allTool.createEvent("100513", "FLUCFGIUCP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getPouringSubCar().getId(), startSubCarStatus_1, false,
                null, startSubCarStatus_1, true, aimCoordinate, input.getPouringFerry().getId(), startFerryStatus_1,false,
                null, endFerryStatus_2, true, aimCoordinate, null,null, null,
                coolingInput.getUpPositions().get(pathIndex).get(positionIndex).getId(), startPositionStatus, startPositionStatus, null,null,
                null, pathIds, pathStatusEnums, pathStatusEnums, input.getPouringFerry().getId(), ExecuteTypeEnum.FERRY, InstructionsEnum.RUNNING, aimCoordinate, product);

        //事件加入甘特图
        simulationOutput.getPouringInput().getGanttChart().getEventLinkGantts().get(input.getPouringIndexInput().getPouringSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100514", "FLUCSCGIUCP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_3 - executionTime_4, input.getPouringSubCar().getId(), startSubCarStatus_1, true, aimCoordinate,
                endSubCarStatus_1, true, coolingInput.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate(), input.getPouringFerry().getId(),
                endFerryStatus_2, true, aimCoordinate, endFerryStatus_1, true, aimCoordinate, null,null, null,
                coolingInput.getUpPositions().get(pathIndex).get(positionIndex).getId(), startPositionStatus, startPositionStatus, null,null,
                null, pathIds, pathStatusEnums, pathStatusEnums, input.getPouringSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.RUNNING,
                coolingInput.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate(), product);

        //事件加入甘特图
        simulationOutput.getPouringInput().getGanttChart().getEventLinkGantts().get(input.getPouringIndexInput().getPouringSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100515", "FLUCCSULIUCP", executionTime_3, input.getTotalTime() - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_4, input.getPouringSubCar().getId(), endSubCarStatus_1, true,
                coolingInput.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate(), endSubCarStatus_2, true,
                coolingInput.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate(), input.getPouringFerry().getId(), endFerryStatus_1,
                true, aimCoordinate, endFerryStatus_1, true, aimCoordinate, null, null,
                null, coolingInput.getUpPositions().get(pathIndex).get(positionIndex).getId(), startPositionStatus, null,
                null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getPouringSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getPouringInput().getGanttChart().getEventLinkGantts().get(input.getPouringIndexInput().getPouringSubCarInGanttIndex()).getEvents().add(event_3);

        //生成事件
        Event event_4 = allTool.createEvent("100516", "ELUCSCGBTFIU", executionTime_3, input.getTotalTime() - executionTime_4,
                input.getTotalTime(), input.getPouringSubCar().getId(), endSubCarStatus_2,true,
                coolingInput.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate(), endSubCarStatus_3, true, aimCoordinate,
                input.getPouringFerry().getId(), endFerryStatus_1,true, aimCoordinate, endFerryStatus_2,
                true, aimCoordinate, null,null, null, null, null,
                null, null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getPouringSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getPouringInput().getGanttChart().getEventLinkGantts().get(input.getPouringIndexInput().getPouringSubCarInGanttIndex()).getEvents().add(event_4);

        //生成点位的通知事件，写在这里方便查找冷却的点位
        Event event_5 = allTool.createEvent("100613", "NUCPCP", input.getPouringSubCar().getProduct().getCoolingTime(),
                input.getTotalTime() - input.getPouringSubCar().getProduct().getCoolingTime(), input.getTotalTime(), null, null,
                false, null, null, false, null, null, null,
                false, null, null, false, null, null, null,
                null, coolingInput.getUpPositions().get(pathIndex).get(positionIndex).getId(), endPositionStatus, endPositionStatus_1, null,
                null, null, null, null, null,
                coolingInput.getUpPositions().get(pathIndex).get(positionIndex).getId(), ExecuteTypeEnum.POSITION, InstructionsEnum.NOTICE, null, null);

        simulationOutput.getCoolingInput().getGanttChart().getEventLinkGantts().get(1 + (pathIndex * 10 + (positionIndex + 1))).getEvents().add(event_5);

        //改变车辆状态和时间
        input.getPouringSubCar().setSubCarState(endSubCarStatus_3);
        input.getPouringFerry().setFerryState(endFerryStatus_2);
        input.getPouringSubCar().setLocationCoordinate(aimCoordinate);
        input.getPouringFerry().setLocationCoordinate(aimCoordinate);
        coolingInput.getUpPositions().get(pathIndex).get(positionIndex).setStatus(PositionStatusEnum.OCCUPIED);


        input.getPouringSubCar().setProduct(null);
        coolingInput.getUpPositions().get(pathIndex).get(positionIndex).setProduct(product);

        simulationOutput.getUpImitates().get(pathIndex).get(positionIndex).setCoolingTime(product.getCoolingTime());
        simulationOutput.getUpImitates().get(pathIndex).get(positionIndex).setTotalTime(input.getTotalTime() - executionTime_4);

        simulationOutput.getPouringInput().setInput(input);
        simulationOutput.getCoolingInput().setInput(coolingInput);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink fullGoDownCoolingPositionEventCreate(AfterClosingBoxAllLink simulationOutput, int pathIndex, int positionIndex) {
        PouringSimulationInput input = simulationOutput.getPouringInput().getInput();
        CoolingSimulationInput coolingInput = simulationOutput.getCoolingInput().getInput();
        //母车前往的目标地点坐标
        Coordinate aimCoordinate = new Coordinate(input.getPouringFerry().getLocationCoordinate().getX(),
                coolingInput.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate().getY());
        //发生母车前往热砂合箱点位，子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getPouringFerry().getLocationCoordinate().getY() -
                aimCoordinate.getY()) / input.getPouringFerry().getFullSpeed();
        double executionTime_2 = Math.abs(input.getPouringSubCar().getLocationCoordinate().getX() -
                coolingInput.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getPouringSubCar().getFullSpeed();
        double executionTime_3 = input.getPouringSubCar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getPouringSubCar().getLocationCoordinate().getX() -
                coolingInput.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getPouringSubCar().getEmptySpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getPouringSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getPouringFerry().getFerryState());
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
        endFerryStatus_1.add(NOT_CARRY_SUB_CAR);
        endFerryStatus_2.add(CARRY_SUB_CAR);
        //点位状态列表
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus_1 = new ArrayList<>();
        startPositionStatus.add(UNOCCUPIED);
        endPositionStatus.add(OCCUPIED);
        endPositionStatus_1.add(FINISH_COOLING);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(coolingInput.getCoolingGoOpenBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getPouringSubCar().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.GOING_COOLING);
        product.setProductFinish(ProductFinishEnum.FINISH_POURING);

        //生成事件
        Event event_1 = allTool.createEvent("100517", "FLDCFGIDCP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getPouringSubCar().getId(), startSubCarStatus_1,
                false, null, startSubCarStatus_1, true, aimCoordinate, input.getPouringFerry().getId(),
                startFerryStatus_1,false, null, endFerryStatus_2, true, aimCoordinate, null,null,
                null, coolingInput.getDownPositions().get(pathIndex).get(positionIndex).getId(), startPositionStatus, startPositionStatus, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getPouringFerry().getId(), ExecuteTypeEnum.FERRY,
                InstructionsEnum.RUNNING, aimCoordinate, product);

        //事件加入甘特图
        simulationOutput.getPouringInput().getGanttChart().getEventLinkGantts().get(input.getPouringIndexInput().getPouringSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100518", "FLDCSCGIDCP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_3 - executionTime_4, input.getPouringSubCar().getId(), startSubCarStatus_1, true, aimCoordinate,
                endSubCarStatus_1, true, coolingInput.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate(), input.getPouringFerry().getId(),
                endFerryStatus_2, true, aimCoordinate, endFerryStatus_1, true, aimCoordinate, null,null, null,
                coolingInput.getDownPositions().get(pathIndex).get(positionIndex).getId(), startPositionStatus, startPositionStatus, null,null,
                null, pathIds, pathStatusEnums, pathStatusEnums, input.getPouringSubCar().getId(), ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, coolingInput.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate(), product);

        //事件加入甘特图
        simulationOutput.getPouringInput().getGanttChart().getEventLinkGantts().get(input.getPouringIndexInput().getPouringSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100519", "FLDCCSULIDCP", executionTime_3, input.getTotalTime() - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_4, input.getPouringSubCar().getId(), endSubCarStatus_1, true,
                coolingInput.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate(), endSubCarStatus_2, true,
                coolingInput.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate(), input.getPouringFerry().getId(), endFerryStatus_1,
                true, aimCoordinate, endFerryStatus_1, true, aimCoordinate, null, null,
                null, coolingInput.getDownPositions().get(pathIndex).get(positionIndex).getId(), startPositionStatus, null,
                null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getPouringSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getPouringInput().getGanttChart().getEventLinkGantts().get(input.getPouringIndexInput().getPouringSubCarInGanttIndex()).getEvents().add(event_3);

        //生成事件
        Event event_4 = allTool.createEvent("100520", "ELDCSCGBTFID", executionTime_3, input.getTotalTime() - executionTime_4,
                input.getTotalTime(), input.getPouringSubCar().getId(), endSubCarStatus_2,true,
                coolingInput.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate(), endSubCarStatus_3, true, aimCoordinate,
                input.getPouringFerry().getId(), endFerryStatus_1,true, aimCoordinate, endFerryStatus_2,
                true, aimCoordinate, null,null, null, null, null,
                null, null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getPouringSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getPouringInput().getGanttChart().getEventLinkGantts().get(input.getPouringIndexInput().getPouringSubCarInGanttIndex()).getEvents().add(event_4);

        //生成点位的通知事件，写在这里方便查找冷却的点位
        Event event_5 = allTool.createEvent("100614", "NDCPCP", input.getPouringSubCar().getProduct().getCoolingTime(),
                input.getTotalTime() - input.getPouringSubCar().getProduct().getCoolingTime(), input.getTotalTime(), null, null,
                false, null, null, false, null, null,
                null, false, null, null, false, null,
                null, null, null, coolingInput.getDownPositions().get(pathIndex).get(positionIndex).getId(),
                endPositionStatus, endPositionStatus_1, null, null, null, null, null,
                null, coolingInput.getDownPositions().get(pathIndex).get(positionIndex).getId(), ExecuteTypeEnum.POSITION, InstructionsEnum.NOTICE,
                null, null);

        simulationOutput.getCoolingInput().getGanttChart().getEventLinkGantts().get(51 + (pathIndex * 10 + (positionIndex + 1))).getEvents().add(event_5);

        //改变车辆状态和时间
        input.getPouringSubCar().setSubCarState(endSubCarStatus_3);
        input.getPouringFerry().setFerryState(endFerryStatus_2);
        input.getPouringSubCar().setLocationCoordinate(aimCoordinate);
        input.getPouringFerry().setLocationCoordinate(aimCoordinate);
        coolingInput.getDownPositions().get(pathIndex).get(positionIndex).setStatus(PositionStatusEnum.OCCUPIED);


        input.getPouringSubCar().setProduct(null);
        coolingInput.getDownPositions().get(pathIndex).get(positionIndex).setProduct(product);

        simulationOutput.getDownImitates().get(pathIndex).get(positionIndex).setCoolingTime(product.getCoolingTime());
        simulationOutput.getDownImitates().get(pathIndex).get(positionIndex).setTotalTime(input.getTotalTime() - executionTime_4);

        simulationOutput.getPouringInput().setInput(input);
        simulationOutput.getCoolingInput().setInput(coolingInput);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink fullGoWorkStationUnLoadEventCreate(AfterClosingBoxAllLink simulationOutput, int workStationIndex) {
        PouringSimulationInput input = simulationOutput.getPouringInput().getInput();
        //母车前往的目标地点坐标
        Coordinate aimCoordinate = new Coordinate(input.getPouringFerry().getLocationCoordinate().getX(),
                input.getWorkingStations().get(workStationIndex).getWorkPositionCoordinate().getY());
        //发生母车前往热砂合箱点位，子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getPouringFerry().getLocationCoordinate().getY() -
                input.getWorkingStations().get(workStationIndex).getWorkPositionCoordinate().getY()) / input.getPouringFerry().getFullSpeed();
        double executionTime_2 = Math.abs(input.getPouringSubCar().getLocationCoordinate().getX() -
                input.getWorkingStations().get(workStationIndex).getWorkPositionCoordinate().getX()) / input.getPouringSubCar().getFullSpeed();
        double executionTime_3 = input.getPouringSubCar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getPouringSubCar().getLocationCoordinate().getX() -
                input.getWorkingStations().get(workStationIndex).getWorkPositionCoordinate().getX()) / input.getPouringSubCar().getEmptySpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getPouringSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getPouringFerry().getFerryState());
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
        endFerryStatus_1.add(NOT_CARRY_SUB_CAR);
        endFerryStatus_2.add(CARRY_SUB_CAR);
        //工岗状态列表
        List<WorkstationStatusEnum> startWorkstationStatus = new ArrayList<>();
        List<WorkstationStatusEnum> endWorkstationStatus = new ArrayList<>();
        startWorkstationStatus.add(WorkstationStatusEnum.FREE);
        endWorkstationStatus.add(WorkstationStatusEnum.WORKING);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPouringPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getPouringSubCar().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.DOING_POURING);

        //生成事件
        Event event_1 = allTool.createEvent("100521", "FLPFGIPWS", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getPouringSubCar().getId(), startSubCarStatus_1,
                false, null, startSubCarStatus_1, true, aimCoordinate, input.getPouringFerry().getId(),
                startFerryStatus_1,false, null, endFerryStatus_2, true, aimCoordinate, null,null,
                null, null, null, null, input.getWorkingStations().get(workStationIndex).getId(), startWorkstationStatus,
                startWorkstationStatus, pathIds, pathStatusEnums, pathStatusEnums, input.getPouringFerry().getId(), ExecuteTypeEnum.FERRY, InstructionsEnum.RUNNING, aimCoordinate, product);

        //事件加入甘特图
        simulationOutput.getPouringInput().getGanttChart().getEventLinkGantts().get(input.getPouringIndexInput().getPouringSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100522", "FLPSCGIPWS", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_3 - executionTime_4, input.getPouringSubCar().getId(), startSubCarStatus_1, true, aimCoordinate,
                endSubCarStatus_1, true, input.getWorkingStations().get(workStationIndex).getWorkPositionCoordinate(), input.getPouringFerry().getId(), endFerryStatus_2,
                true, aimCoordinate, endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkingStations().get(workStationIndex).getId(), startWorkstationStatus, startWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getPouringSubCar().getId(), ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, input.getWorkingStations().get(workStationIndex).getWorkPositionCoordinate(), product);

        //事件加入甘特图
        simulationOutput.getPouringInput().getGanttChart().getEventLinkGantts().get(input.getPouringIndexInput().getPouringSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100523", "FLPCSULIPWS", executionTime_3, input.getTotalTime() - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_4, input.getPouringSubCar().getId(), endSubCarStatus_1, true,
                input.getWorkingStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_2, true,
                input.getWorkingStations().get(workStationIndex).getWorkPositionCoordinate(), input.getPouringFerry().getId(), endFerryStatus_1,true,
                aimCoordinate, endFerryStatus_1, true, aimCoordinate, null, null, null, null,
                null, null, input.getWorkingStations().get(workStationIndex).getId(), startWorkstationStatus, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getPouringSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getPouringInput().getGanttChart().getEventLinkGantts().get(input.getPouringIndexInput().getPouringSubCarInGanttIndex()).getEvents().add(event_3);

        //生成事件
        Event event_4 = allTool.createEvent("100524", "ELPSCGBTFIPWS", executionTime_3, input.getTotalTime() - executionTime_4,
                input.getTotalTime(), input.getPouringSubCar().getId(), endSubCarStatus_2,true, input.getWorkingStations().get(workStationIndex).getWorkPositionCoordinate(),
                endSubCarStatus_3, true, aimCoordinate, input.getPouringFerry().getId(), endFerryStatus_1,true, aimCoordinate, endFerryStatus_2,
                true, aimCoordinate, null,null, null, null, null, null,
                null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getPouringSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getPouringInput().getGanttChart().getEventLinkGantts().get(input.getPouringIndexInput().getPouringSubCarInGanttIndex()).getEvents().add(event_4);

        //改变车辆状态和时间
        input.getPouringSubCar().setSubCarState(endSubCarStatus_3);
        input.getPouringFerry().setFerryState(endFerryStatus_2);
        input.getPouringSubCar().setLocationCoordinate(aimCoordinate);
        input.getPouringFerry().setLocationCoordinate(aimCoordinate);
        input.getWorkingStations().get(workStationIndex).setStatus(WorkstationStatusEnum.WORKING);


        input.getPouringSubCar().setProduct(null);
        List<Product> products = new ArrayList<>();
        products.add(product);
        input.getWorkingStations().get(workStationIndex).setProduct(products);

        simulationOutput.getPouringInput().setInput(input);

        return simulationOutput;
    }
}
