package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.waitingPour;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.ClosingBoxSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.WaitingPourSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;

import java.util.ArrayList;
import java.util.List;

import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.OCCUPIED;
import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.UNOCCUPIED;
import static cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum.PATH_UNOCCUPIED;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum.CARRY_SUB_CAR;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum.NOT_CARRY_SUB_CAR;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum.*;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/25 16:42
 */
public class WaitingPourEventFlow {
    /**
     * 仿真总工具类
     */
    private SimulationTool allTool = new SimulationTool();

    public AfterClosingBoxAllLink emptyGoHotClosingPositionEventCreate(AfterClosingBoxAllLink simulationOutput) {
        ClosingBoxSimulationInput closingBoxInput = simulationOutput.getClosingBoxInput().getInput();
        WaitingPourSimulationInput input = simulationOutput.getWaitingPourInput().getInput();
        //发生母车前往热砂合箱点位，子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getClosingGoPourFerry().getLocationCoordinate().getY() -
                closingBoxInput.getHotClosingBoxGoPourPosition().getCoordinate().getY()) / input.getClosingGoPourFerry().getEmptySpeed();
        double executionTime_2 = Math.abs(input.getClosingGoPourSubCar().getLocationCoordinate().getX() -
                closingBoxInput.getHotClosingBoxGoPourPosition().getCoordinate().getX()) / input.getClosingGoPourSubCar().getEmptySpeed();
        double executionTime_3 = input.getClosingGoPourSubCar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getClosingGoPourSubCar().getLocationCoordinate().getX() -
                closingBoxInput.getHotClosingBoxGoPourPosition().getCoordinate().getX()) / input.getClosingGoPourSubCar().getFullSpeed();
        //母车前往的目标地点坐标
        Coordinate aimCoordinate = new Coordinate(input.getClosingGoPourFerry().getLocationCoordinate().getX(),
                closingBoxInput.getHotClosingBoxGoPourPosition().getCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getClosingGoPourSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getClosingGoPourFerry().getFerryState());
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
        pathIds.add(input.getClosingGoPourPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = closingBoxInput.getHotClosingBoxGoPourPosition().getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100401", "ELWPFGIHCBP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getClosingGoPourSubCar().getId(), startSubCarStatus_1, false,
                null, startSubCarStatus_1, true, aimCoordinate, input.getClosingGoPourFerry().getId(), startFerryStatus_1, false,
                null, endFerryStatus_2, true, aimCoordinate, null,null, null,
                closingBoxInput.getHotClosingBoxGoPourPosition().getId(), startPositionStatus, startPositionStatus, null,null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getClosingGoPourFerry().getId(), ExecuteTypeEnum.FERRY, InstructionsEnum.RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getWaitingPourInput().getGanttChart().getEventLinkGantts().get(input.getWaitingPourIndexInput().getClosingBoxGoPourSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100402", "ELWPSCGIHCBP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_3 - executionTime_4, input.getClosingGoPourSubCar().getId(), startSubCarStatus_1, true, aimCoordinate,
                endSubCarStatus_1, true, closingBoxInput.getHotClosingBoxGoPourPosition().getCoordinate(), input.getClosingGoPourFerry().getId(), endFerryStatus_2,
                true, aimCoordinate, endFerryStatus_1, true, aimCoordinate, null,null, null,
                closingBoxInput.getHotClosingBoxGoPourPosition().getId(), startPositionStatus, startPositionStatus, null,null,
                null, pathIds, pathStatusEnums, pathStatusEnums, input.getClosingGoPourSubCar().getId(), ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, closingBoxInput.getHotClosingBoxGoPourPosition().getCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getWaitingPourInput().getGanttChart().getEventLinkGantts().get(input.getWaitingPourIndexInput().getClosingBoxGoPourSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100403", "ELWPCSLIHCBP", executionTime_3, input.getTotalTime() - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_4, input.getClosingGoPourSubCar().getId(), endSubCarStatus_1, true,
                closingBoxInput.getHotClosingBoxGoPourPosition().getCoordinate(), endSubCarStatus_2, true, closingBoxInput.getHotClosingBoxGoPourPosition().getCoordinate(),
                input.getClosingGoPourFerry().getId(), endFerryStatus_1,true, aimCoordinate, endFerryStatus_1, true, aimCoordinate, null,
                null, null, closingBoxInput.getHotClosingBoxGoPourPosition().getId(), startPositionStatus, endPositionStatus,
                null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getClosingGoPourSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.RISING, null, product);

        //事件加入甘特图
        simulationOutput.getWaitingPourInput().getGanttChart().getEventLinkGantts().get(input.getWaitingPourIndexInput().getClosingBoxGoPourSubCarInGanttIndex()).getEvents().add(event_3);

        //生成事件
        Event event_4 = allTool.createEvent("100404", "FLWPSCGBTFH", executionTime_3, input.getTotalTime() - executionTime_4,
                input.getTotalTime(), input.getClosingGoPourSubCar().getId(), endSubCarStatus_2,true, closingBoxInput.getHotClosingBoxGoPourPosition().getCoordinate(),
                endSubCarStatus_3, true, aimCoordinate, input.getClosingGoPourFerry().getId(), endFerryStatus_1,true, aimCoordinate, endFerryStatus_2,
                true, aimCoordinate, null,null, null, null, null, null,
                null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getClosingGoPourSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.RUNNING, aimCoordinate, product);

        //事件加入甘特图
        simulationOutput.getWaitingPourInput().getGanttChart().getEventLinkGantts().get(input.getWaitingPourIndexInput().getClosingBoxGoPourSubCarInGanttIndex()).getEvents().add(event_4);

        //改变车辆状态和时间
        input.getClosingGoPourSubCar().setSubCarState(endSubCarStatus_3);
        input.getClosingGoPourFerry().setFerryState(endFerryStatus_2);
        input.getClosingGoPourSubCar().setLocationCoordinate(aimCoordinate);
        input.getClosingGoPourFerry().setLocationCoordinate(aimCoordinate);
        closingBoxInput.getHotClosingBoxGoPourPosition().setStatus(PositionStatusEnum.UNOCCUPIED);


        input.getClosingGoPourSubCar().setProduct(product);
        closingBoxInput.getHotClosingBoxGoPourPosition().setProduct(null);

        simulationOutput.getClosingBoxInput().setInput(closingBoxInput);
        simulationOutput.getWaitingPourInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink emptyGoColdClosingPositionEventCreate(AfterClosingBoxAllLink simulationOutput) {
        ClosingBoxSimulationInput closingBoxInput = simulationOutput.getClosingBoxInput().getInput();
        WaitingPourSimulationInput input = simulationOutput.getWaitingPourInput().getInput();
        //发生母车前往热砂合箱点位，子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getClosingGoPourFerry().getLocationCoordinate().getY() -
                closingBoxInput.getColdClosingBoxGoPourPosition().getCoordinate().getY()) / input.getClosingGoPourFerry().getEmptySpeed();
        double executionTime_2 = Math.abs(input.getClosingGoPourSubCar().getLocationCoordinate().getX() -
                closingBoxInput.getColdClosingBoxGoPourPosition().getCoordinate().getX()) / input.getClosingGoPourSubCar().getEmptySpeed();
        double executionTime_3 = input.getClosingGoPourSubCar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getClosingGoPourSubCar().getLocationCoordinate().getX() -
                closingBoxInput.getColdClosingBoxGoPourPosition().getCoordinate().getX()) / input.getClosingGoPourSubCar().getFullSpeed();
        //母车前往的目标地点坐标
        Coordinate aimCoordinate = new Coordinate(input.getClosingGoPourFerry().getLocationCoordinate().getX(),
                closingBoxInput.getColdClosingBoxGoPourPosition().getCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getClosingGoPourSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getClosingGoPourFerry().getFerryState());
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
        pathIds.add(input.getClosingGoPourPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = closingBoxInput.getColdClosingBoxGoPourPosition().getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100405", "ELUWPFGICCBP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getClosingGoPourSubCar().getId(), startSubCarStatus_1, false,
                null, startSubCarStatus_1, true, aimCoordinate, input.getClosingGoPourFerry().getId(), startFerryStatus_1,false,
                null, endFerryStatus_2, true, aimCoordinate, null,null, null,
                closingBoxInput.getColdClosingBoxGoPourPosition().getId(), startPositionStatus, startPositionStatus, null,null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getClosingGoPourFerry().getId(), ExecuteTypeEnum.FERRY, InstructionsEnum.RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getWaitingPourInput().getGanttChart().getEventLinkGantts().get(input.getWaitingPourIndexInput().getClosingBoxGoPourSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100406", "ELUWPSCGICCBP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_3 - executionTime_4, input.getClosingGoPourSubCar().getId(), startSubCarStatus_1, true, aimCoordinate,
                endSubCarStatus_1, true, closingBoxInput.getColdClosingBoxGoPourPosition().getCoordinate(), input.getClosingGoPourFerry().getId(), endFerryStatus_2,
                true, aimCoordinate, endFerryStatus_1, true, aimCoordinate, null,null, null,
                closingBoxInput.getColdClosingBoxGoPourPosition().getId(), startPositionStatus, startPositionStatus, null,null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getClosingGoPourSubCar().getId(), ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, closingBoxInput.getColdClosingBoxGoPourPosition().getCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getWaitingPourInput().getGanttChart().getEventLinkGantts().get(input.getWaitingPourIndexInput().getClosingBoxGoPourSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100407", "ELUWPCSLICCBP", executionTime_3, input.getTotalTime() - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_4, input.getClosingGoPourSubCar().getId(), endSubCarStatus_1, true,
                closingBoxInput.getColdClosingBoxGoPourPosition().getCoordinate(), endSubCarStatus_2, true, closingBoxInput.getColdClosingBoxGoPourPosition().getCoordinate(),
                input.getClosingGoPourFerry().getId(), endFerryStatus_1,true, aimCoordinate, endFerryStatus_1, true, aimCoordinate, null,
                null, null, closingBoxInput.getColdClosingBoxGoPourPosition().getId(), startPositionStatus, endPositionStatus,
                null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getClosingGoPourSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.RISING, null, product);

        //事件加入甘特图
        simulationOutput.getWaitingPourInput().getGanttChart().getEventLinkGantts().get(input.getWaitingPourIndexInput().getClosingBoxGoPourSubCarInGanttIndex()).getEvents().add(event_3);

        //生成事件
        Event event_4 = allTool.createEvent("100408", "FLUWPSCGBTFC", executionTime_3, input.getTotalTime() - executionTime_4,
                input.getTotalTime(), input.getClosingGoPourSubCar().getId(), endSubCarStatus_2,true, closingBoxInput.getColdClosingBoxGoPourPosition().getCoordinate(),
                endSubCarStatus_3, true, aimCoordinate, input.getClosingGoPourFerry().getId(), endFerryStatus_1,true, aimCoordinate, endFerryStatus_2,
                true, aimCoordinate, null,null, null, null, null, null,
                null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getClosingGoPourSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.RUNNING, aimCoordinate, product);

        //事件加入甘特图
        simulationOutput.getWaitingPourInput().getGanttChart().getEventLinkGantts().get(input.getWaitingPourIndexInput().getClosingBoxGoPourSubCarInGanttIndex()).getEvents().add(event_4);

        //改变车辆状态和时间
        input.getClosingGoPourSubCar().setSubCarState(endSubCarStatus_3);
        input.getClosingGoPourFerry().setFerryState(endFerryStatus_2);
        input.getClosingGoPourSubCar().setLocationCoordinate(aimCoordinate);
        input.getClosingGoPourFerry().setLocationCoordinate(aimCoordinate);
        closingBoxInput.getColdClosingBoxGoPourPosition().setStatus(PositionStatusEnum.UNOCCUPIED);


        input.getClosingGoPourSubCar().setProduct(product);
        closingBoxInput.getColdClosingBoxGoPourPosition().setProduct(null);

        simulationOutput.getClosingBoxInput().setInput(closingBoxInput);
        simulationOutput.getWaitingPourInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink fullGoUpWaitingPourPositionEventCreate(AfterClosingBoxAllLink simulationOutput, int pathIndex, int positionIndex) {
        WaitingPourSimulationInput input = simulationOutput.getWaitingPourInput().getInput();
        //母车前往的目标地点坐标
        Coordinate aimCoordinate = new Coordinate(input.getClosingGoPourFerry().getLocationCoordinate().getX(),
                input.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate().getY());
        //发生母车前往热砂合箱点位，子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getClosingGoPourFerry().getLocationCoordinate().getY() -
                aimCoordinate.getY()) / input.getClosingGoPourFerry().getEmptySpeed();
        double executionTime_2 = Math.abs(input.getClosingGoPourSubCar().getLocationCoordinate().getX() -
                input.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getClosingGoPourSubCar().getEmptySpeed();
        double executionTime_3 = input.getClosingGoPourSubCar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getClosingGoPourSubCar().getLocationCoordinate().getX() -
                input.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getClosingGoPourSubCar().getFullSpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getClosingGoPourSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getClosingGoPourFerry().getFerryState());
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
        startPositionStatus.add(UNOCCUPIED);
        endPositionStatus.add(OCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getClosingGoPourPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getClosingGoPourSubCar().getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100413", "FLUWPFGIUWPP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getClosingGoPourSubCar().getId(), startSubCarStatus_1,
                false, null, startSubCarStatus_1, true, aimCoordinate, input.getClosingGoPourFerry().getId(), startFerryStatus_1,
                false, null, endFerryStatus_2, true, aimCoordinate, null,null, null,
                input.getUpPositions().get(pathIndex).get(positionIndex).getId(), startPositionStatus, startPositionStatus, null,null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getClosingGoPourFerry().getId(), ExecuteTypeEnum.FERRY, InstructionsEnum.RUNNING, aimCoordinate, product);

        //事件加入甘特图
        simulationOutput.getWaitingPourInput().getGanttChart().getEventLinkGantts().get(input.getWaitingPourIndexInput().getClosingBoxGoPourSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100414", "FLUWPSCGIUWPP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_3 - executionTime_4, input.getClosingGoPourSubCar().getId(), startSubCarStatus_1, true, aimCoordinate,
                endSubCarStatus_1, true, input.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate(), input.getClosingGoPourFerry().getId(), endFerryStatus_2,
                true, aimCoordinate, endFerryStatus_1, true, aimCoordinate, null,null, null,
                input.getUpPositions().get(pathIndex).get(positionIndex).getId(), startPositionStatus, startPositionStatus, null,null,
                null, pathIds, pathStatusEnums, pathStatusEnums, input.getClosingGoPourSubCar().getId(), ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, input.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate(), product);

        //事件加入甘特图
        simulationOutput.getWaitingPourInput().getGanttChart().getEventLinkGantts().get(input.getWaitingPourIndexInput().getClosingBoxGoPourSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100415", "FLUWPCSULIUWPP", executionTime_3, input.getTotalTime() - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_4, input.getClosingGoPourSubCar().getId(), endSubCarStatus_1, true,
                input.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate(), endSubCarStatus_2, true,
                input.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate(), input.getClosingGoPourFerry().getId(), endFerryStatus_1,true,
                aimCoordinate, endFerryStatus_1, true, aimCoordinate, null, null, null,
                input.getUpPositions().get(pathIndex).get(positionIndex).getId(), startPositionStatus, endPositionStatus,
                null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getClosingGoPourSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getWaitingPourInput().getGanttChart().getEventLinkGantts().get(input.getWaitingPourIndexInput().getClosingBoxGoPourSubCarInGanttIndex()).getEvents().add(event_3);

        //生成事件
        Event event_4 = allTool.createEvent("100416", "ELUWPSCGBTFIU", executionTime_3, input.getTotalTime() - executionTime_4,
                input.getTotalTime(), input.getClosingGoPourSubCar().getId(), endSubCarStatus_2,true, input.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate(),
                endSubCarStatus_3, true, aimCoordinate, input.getClosingGoPourFerry().getId(), endFerryStatus_1,true, aimCoordinate, endFerryStatus_2,
                true, aimCoordinate, null,null, null, null, null, null,
                null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getClosingGoPourSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getWaitingPourInput().getGanttChart().getEventLinkGantts().get(input.getWaitingPourIndexInput().getClosingBoxGoPourSubCarInGanttIndex()).getEvents().add(event_4);

        //改变车辆状态和时间
        input.getClosingGoPourSubCar().setSubCarState(endSubCarStatus_3);
        input.getClosingGoPourFerry().setFerryState(endFerryStatus_2);
        input.getClosingGoPourSubCar().setLocationCoordinate(aimCoordinate);
        input.getClosingGoPourFerry().setLocationCoordinate(aimCoordinate);
        input.getUpPositions().get(pathIndex).get(positionIndex).setStatus(PositionStatusEnum.OCCUPIED);

        input.getClosingGoPourSubCar().setProduct(null);
        input.getUpPositions().get(pathIndex).get(positionIndex).setProduct(product);

        simulationOutput.setWaitingPourCount(simulationOutput.getWaitingPourCount() + 1);
        simulationOutput.getWaitingPourInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink fullGoDownWaitingPourPositionEventCreate(AfterClosingBoxAllLink simulationOutput, int pathIndex, int positionIndex) {
        WaitingPourSimulationInput input = simulationOutput.getWaitingPourInput().getInput();
        //母车前往的目标地点坐标
        Coordinate aimCoordinate = new Coordinate(input.getClosingGoPourFerry().getLocationCoordinate().getX(),
                input.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate().getY());
        //发生母车前往热砂合箱点位，子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getClosingGoPourFerry().getLocationCoordinate().getY() -
                aimCoordinate.getY()) / input.getClosingGoPourFerry().getEmptySpeed();
        double executionTime_2 = Math.abs(input.getClosingGoPourSubCar().getLocationCoordinate().getX() -
                input.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getClosingGoPourSubCar().getEmptySpeed();
        double executionTime_3 = input.getClosingGoPourSubCar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getClosingGoPourSubCar().getLocationCoordinate().getX() -
                input.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getClosingGoPourSubCar().getFullSpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getClosingGoPourSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getClosingGoPourFerry().getFerryState());
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
        startPositionStatus.add(UNOCCUPIED);
        endPositionStatus.add(OCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getClosingGoPourPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getClosingGoPourSubCar().getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100409", "FLDWPFGIDWPP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getClosingGoPourSubCar().getId(), startSubCarStatus_1, false,
                null, startSubCarStatus_1, true, aimCoordinate, input.getClosingGoPourFerry().getId(), startFerryStatus_1,false,
                null, endFerryStatus_2, true, aimCoordinate, null,null, null,
                input.getDownPositions().get(pathIndex).get(positionIndex).getId(), startPositionStatus, startPositionStatus, null,null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getClosingGoPourFerry().getId(), ExecuteTypeEnum.FERRY, InstructionsEnum.RUNNING, aimCoordinate, product);

        //事件加入甘特图
        simulationOutput.getWaitingPourInput().getGanttChart().getEventLinkGantts().get(input.getWaitingPourIndexInput().getClosingBoxGoPourSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100410", "FLDWPSCGIDWPP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_3 - executionTime_4, input.getClosingGoPourSubCar().getId(), startSubCarStatus_1, true, aimCoordinate,
                endSubCarStatus_1, true, input.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate(), input.getClosingGoPourFerry().getId(), endFerryStatus_2,
                true, aimCoordinate, endFerryStatus_1, true, aimCoordinate, null,null, null,
                input.getDownPositions().get(pathIndex).get(positionIndex).getId(), startPositionStatus, startPositionStatus, null,null,
                null, pathIds, pathStatusEnums, pathStatusEnums, input.getClosingGoPourSubCar().getId(), ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, input.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate(), product);

        //事件加入甘特图
        simulationOutput.getWaitingPourInput().getGanttChart().getEventLinkGantts().get(input.getWaitingPourIndexInput().getClosingBoxGoPourSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100411", "FLDWPCSULIDWPP", executionTime_3, input.getTotalTime() - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_4, input.getClosingGoPourSubCar().getId(), endSubCarStatus_1, true,
                input.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate(), endSubCarStatus_2, true, input.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate(),
                input.getClosingGoPourFerry().getId(), endFerryStatus_1,true, aimCoordinate, endFerryStatus_1, true, aimCoordinate, null,
                null, null, input.getDownPositions().get(pathIndex).get(positionIndex).getId(), startPositionStatus, endPositionStatus,
                null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getClosingGoPourSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getWaitingPourInput().getGanttChart().getEventLinkGantts().get(input.getWaitingPourIndexInput().getClosingBoxGoPourSubCarInGanttIndex()).getEvents().add(event_3);

        //生成事件
        Event event_4 = allTool.createEvent("100412", "ELDWPSCGBTFID", executionTime_3, input.getTotalTime() - executionTime_4,
                input.getTotalTime(), input.getClosingGoPourSubCar().getId(), endSubCarStatus_2,true, input.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate(),
                endSubCarStatus_3, true, aimCoordinate, input.getClosingGoPourFerry().getId(), endFerryStatus_1,true, aimCoordinate, endFerryStatus_2,
                true, aimCoordinate, null,null, null, null, null, null,
                null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getClosingGoPourSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getWaitingPourInput().getGanttChart().getEventLinkGantts().get(input.getWaitingPourIndexInput().getClosingBoxGoPourSubCarInGanttIndex()).getEvents().add(event_4);

        //改变车辆状态和时间
        input.getClosingGoPourSubCar().setSubCarState(endSubCarStatus_3);
        input.getClosingGoPourFerry().setFerryState(endFerryStatus_2);
        input.getClosingGoPourSubCar().setLocationCoordinate(aimCoordinate);
        input.getClosingGoPourFerry().setLocationCoordinate(aimCoordinate);
        input.getDownPositions().get(pathIndex).get(positionIndex).setStatus(PositionStatusEnum.OCCUPIED);


        input.getClosingGoPourSubCar().setProduct(null);
        input.getDownPositions().get(pathIndex).get(positionIndex).setProduct(product);

        simulationOutput.setWaitingPourCount(simulationOutput.getWaitingPourCount() + 1);
        simulationOutput.getWaitingPourInput().setInput(input);

        return simulationOutput;
    }
}
