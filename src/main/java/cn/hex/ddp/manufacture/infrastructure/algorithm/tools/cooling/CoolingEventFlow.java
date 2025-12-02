package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.cooling;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.CoolingSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.OpenBoxSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.waitingPour.AfterClosingBoxAllLink;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;
import lombok.NoArgsConstructor;

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
 * @Created: 2024/8/1 0:05
 */
@NoArgsConstructor
public class CoolingEventFlow {
    /**
     * 仿真总工具类
     */
    private SimulationTool allTool = new SimulationTool();

    public AfterClosingBoxAllLink emptyGoUpCoolingPositionEventCreate(AfterClosingBoxAllLink simulationOutput, int pathIndex, int positionIndex) {
        CoolingSimulationInput input = simulationOutput.getCoolingInput().getInput();
        //母车前往的目标地点坐标
        Coordinate aimCoordinate = new Coordinate(input.getCoolingFerry().getLocationCoordinate().getX(),
                input.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate().getY());
        //发生母车前往热砂合箱点位，子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getCoolingFerry().getLocationCoordinate().getY() -
                aimCoordinate.getY()) / input.getCoolingFerry().getEmptySpeed();
        double executionTime_2 = Math.abs(input.getCoolingSubCar().getLocationCoordinate().getX() -
                input.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getCoolingSubCar().getEmptySpeed();
        double executionTime_3 = input.getCoolingSubCar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getCoolingSubCar().getLocationCoordinate().getX() -
                input.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getCoolingSubCar().getFullSpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getCoolingSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getCoolingFerry().getFerryState());
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
        startPositionStatus.add(PositionStatusEnum.FINISH_COOLING);
        endPositionStatus.add(UNOCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getCoolingGoOpenBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getUpPositions().get(pathIndex).get(positionIndex).getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100601", "ELCGOFGIUCP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getCoolingSubCar().getId(), startSubCarStatus_1,
                false, null, startSubCarStatus_1, true, aimCoordinate, input.getCoolingFerry().getId(),
                startFerryStatus_1,false, null, endFerryStatus_2, true, aimCoordinate, null,null,
                null, input.getUpPositions().get(pathIndex).get(positionIndex).getId(), startPositionStatus, startPositionStatus, null,null,
                null, pathIds, pathStatusEnums, pathStatusEnums, input.getCoolingFerry().getId(), ExecuteTypeEnum.FERRY, InstructionsEnum.RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getCoolingInput().getGanttChart().getEventLinkGantts().get(input.getCoolingIndexInput().getCoolingGoOpenBoxSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100602", "ELCGOSCGIUCP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_3 - executionTime_4, input.getCoolingSubCar().getId(), startSubCarStatus_1, true, aimCoordinate,
                endSubCarStatus_1, true, input.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate(), input.getCoolingFerry().getId(), endFerryStatus_2,
                true, aimCoordinate, endFerryStatus_1, true, aimCoordinate, null,null, null,
                input.getUpPositions().get(pathIndex).get(positionIndex).getId(), startPositionStatus, startPositionStatus, null,null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCoolingSubCar().getId(), ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, input.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getCoolingInput().getGanttChart().getEventLinkGantts().get(input.getCoolingIndexInput().getCoolingGoOpenBoxSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100603", "ELCGOCSULIUCP", executionTime_3, input.getTotalTime() - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_4, input.getCoolingSubCar().getId(), endSubCarStatus_1, true,
                input.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate(), endSubCarStatus_2, true,
                input.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate(), input.getCoolingFerry().getId(), endFerryStatus_1,true,
                aimCoordinate, endFerryStatus_1, true, aimCoordinate, null, null, null,
                input.getUpPositions().get(pathIndex).get(positionIndex).getId(), startPositionStatus, endPositionStatus,
                null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getCoolingSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.RISING, null, product);

        //事件加入甘特图
        simulationOutput.getCoolingInput().getGanttChart().getEventLinkGantts().get(input.getCoolingIndexInput().getCoolingGoOpenBoxSubCarInGanttIndex()).getEvents().add(event_3);

        //生成事件
        Event event_4 = allTool.createEvent("100604", "FLCGOSCGBTFIU", executionTime_3, input.getTotalTime() - executionTime_4,
                input.getTotalTime(), input.getCoolingSubCar().getId(), endSubCarStatus_2,true, input.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate(),
                endSubCarStatus_3, true, aimCoordinate, input.getCoolingFerry().getId(), endFerryStatus_1,true, aimCoordinate, endFerryStatus_2,
                true, aimCoordinate, null,null, null, null, null, null,
                null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getCoolingSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.RUNNING, aimCoordinate, product);

        //事件加入甘特图
        simulationOutput.getCoolingInput().getGanttChart().getEventLinkGantts().get(input.getCoolingIndexInput().getCoolingGoOpenBoxSubCarInGanttIndex()).getEvents().add(event_4);

        //改变车辆状态和时间
        input.getCoolingSubCar().setSubCarState(endSubCarStatus_3);
        input.getCoolingFerry().setFerryState(endFerryStatus_2);
        input.getCoolingSubCar().setLocationCoordinate(aimCoordinate);
        input.getCoolingFerry().setLocationCoordinate(aimCoordinate);
        input.getUpPositions().get(pathIndex).get(positionIndex).setStatus(PositionStatusEnum.UNOCCUPIED);


        input.getUpPositions().get(pathIndex).get(positionIndex).setProduct(null);
        input.getCoolingSubCar().setProduct(product);

        simulationOutput.getCoolingInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink emptyGoDownCoolingPositionEventCreate(AfterClosingBoxAllLink simulationOutput, int pathIndex, int positionIndex) {
        CoolingSimulationInput input = simulationOutput.getCoolingInput().getInput();
        //母车前往的目标地点坐标
        Coordinate aimCoordinate = new Coordinate(input.getCoolingFerry().getLocationCoordinate().getX(),
                input.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate().getY());
        //发生母车前往热砂合箱点位，子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getCoolingFerry().getLocationCoordinate().getY() -
                aimCoordinate.getY()) / input.getCoolingFerry().getEmptySpeed();
        double executionTime_2 = Math.abs(input.getCoolingSubCar().getLocationCoordinate().getX() -
                input.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getCoolingSubCar().getEmptySpeed();
        double executionTime_3 = input.getCoolingSubCar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getCoolingSubCar().getLocationCoordinate().getX() -
                input.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getCoolingSubCar().getFullSpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getCoolingSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getCoolingFerry().getFerryState());
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
        startPositionStatus.add(PositionStatusEnum.FINISH_COOLING);
        endPositionStatus.add(UNOCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getCoolingGoOpenBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getDownPositions().get(pathIndex).get(positionIndex).getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100601", "ELCGOFGIUCP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getCoolingSubCar().getId(), startSubCarStatus_1,
                false, null, startSubCarStatus_1, true, aimCoordinate, input.getCoolingFerry().getId(),
                startFerryStatus_1,false, null, endFerryStatus_2, true, aimCoordinate, null,null,
                null, input.getDownPositions().get(pathIndex).get(positionIndex).getId(), startPositionStatus, startPositionStatus, null,null,
                null, pathIds, pathStatusEnums, pathStatusEnums, input.getCoolingFerry().getId(), ExecuteTypeEnum.FERRY, InstructionsEnum.RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getCoolingInput().getGanttChart().getEventLinkGantts().get(input.getCoolingIndexInput().getCoolingGoOpenBoxSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100602", "ELCGOSCGIUCP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_3 - executionTime_4, input.getCoolingSubCar().getId(), startSubCarStatus_1, true, aimCoordinate,
                endSubCarStatus_1, true, input.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate(), input.getCoolingFerry().getId(), endFerryStatus_2,
                true, aimCoordinate, endFerryStatus_1, true, aimCoordinate, null,null, null,
                input.getDownPositions().get(pathIndex).get(positionIndex).getId(), startPositionStatus, startPositionStatus, null,null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCoolingSubCar().getId(), ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, input.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getCoolingInput().getGanttChart().getEventLinkGantts().get(input.getCoolingIndexInput().getCoolingGoOpenBoxSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100603", "ELCGOCSULIUCP", executionTime_3, input.getTotalTime() - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_4, input.getCoolingSubCar().getId(), endSubCarStatus_1, true,
                input.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate(), endSubCarStatus_2, true,
                input.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate(), input.getCoolingFerry().getId(), endFerryStatus_1,true,
                aimCoordinate, endFerryStatus_1, true, aimCoordinate, null, null, null,
                input.getDownPositions().get(pathIndex).get(positionIndex).getId(), startPositionStatus, endPositionStatus,
                null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getCoolingSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.RISING, null, product);

        //事件加入甘特图
        simulationOutput.getCoolingInput().getGanttChart().getEventLinkGantts().get(input.getCoolingIndexInput().getCoolingGoOpenBoxSubCarInGanttIndex()).getEvents().add(event_3);

        //生成事件
        Event event_4 = allTool.createEvent("100604", "FLCGOSCGBTFIU", executionTime_3, input.getTotalTime() - executionTime_4,
                input.getTotalTime(), input.getCoolingSubCar().getId(), endSubCarStatus_2,true, input.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate(),
                endSubCarStatus_3, true, aimCoordinate, input.getCoolingFerry().getId(), endFerryStatus_1,true, aimCoordinate, endFerryStatus_2,
                true, aimCoordinate, null,null, null, null, null, null,
                null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getCoolingSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.RUNNING, aimCoordinate, product);

        //事件加入甘特图
        simulationOutput.getCoolingInput().getGanttChart().getEventLinkGantts().get(input.getCoolingIndexInput().getCoolingGoOpenBoxSubCarInGanttIndex()).getEvents().add(event_4);

        //改变车辆状态和时间
        input.getCoolingSubCar().setSubCarState(endSubCarStatus_3);
        input.getCoolingFerry().setFerryState(endFerryStatus_2);
        input.getCoolingSubCar().setLocationCoordinate(aimCoordinate);
        input.getCoolingFerry().setLocationCoordinate(aimCoordinate);
        input.getDownPositions().get(pathIndex).get(positionIndex).setStatus(PositionStatusEnum.UNOCCUPIED);


        input.getDownPositions().get(pathIndex).get(positionIndex).setProduct(null);
        input.getCoolingSubCar().setProduct(product);

        simulationOutput.getCoolingInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink fullSubCarGoHotOpenBoxPositionEventCreate(AfterClosingBoxAllLink simulationOutput) {
        CoolingSimulationInput input = simulationOutput.getCoolingInput().getInput();
        OpenBoxSimulationInput openBoxInput = simulationOutput.getOpenBoxInput().getInput();
        //发生母车前往热砂合箱点位，子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getCoolingSubCar().getLocationCoordinate().getX() -
                openBoxInput.getHotOpenBoxPosition().getCoordinate().getX()) / input.getCoolingSubCar().getFullSpeed();
        double executionTime_2 = input.getCoolingSubCar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getCoolingSubCar().getLocationCoordinate().getX() -
                openBoxInput.getHotOpenBoxPosition().getCoordinate().getX()) / input.getCoolingSubCar().getEmptySpeed();
        Coordinate ferryCoordinate = input.getCoolingFerry().getLocationCoordinate().coordinateDeepCopy();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getCoolingSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getCoolingFerry().getFerryState());
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
        pathIds.add(input.getCoolingGoOpenBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getCoolingSubCar().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.GOING_UNBOXING);
        product.setProductFinish(ProductFinishEnum.FINISH_COOLING);

        //生成事件
        Event event_1 = allTool.createEvent("100606", "FLCGOSCGIHOBP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_2 - executionTime_3, input.getCoolingSubCar().getId(), startSubCarStatus_1, true, ferryCoordinate,
                endSubCarStatus_1, true, openBoxInput.getHotOpenBoxPosition().getCoordinate(), input.getCoolingFerry().getId(), endFerryStatus_2,
                true, ferryCoordinate, endFerryStatus_1, true, ferryCoordinate, null,null, null,
                openBoxInput.getHotOpenBoxPosition().getId(), startPositionStatus, startPositionStatus, null,null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCoolingSubCar().getId(), ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, openBoxInput.getHotOpenBoxPosition().getCoordinate(), product);

        //事件加入甘特图
        simulationOutput.getCoolingInput().getGanttChart().getEventLinkGantts().get(input.getCoolingIndexInput().getCoolingGoOpenBoxSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100607", "FLCGOCSLIHOBP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime()- executionTime_3, input.getCoolingSubCar().getId(), endSubCarStatus_1, true,
                openBoxInput.getHotOpenBoxPosition().getCoordinate(), endSubCarStatus_2, true, openBoxInput.getHotOpenBoxPosition().getCoordinate(),
                input.getCoolingFerry().getId(), endFerryStatus_1,true, ferryCoordinate, endFerryStatus_1, true, ferryCoordinate, null,
                null, null, openBoxInput.getHotOpenBoxPosition().getId(), startPositionStatus, endPositionStatus,
                null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getCoolingSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getCoolingInput().getGanttChart().getEventLinkGantts().get(input.getCoolingIndexInput().getCoolingGoOpenBoxSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100608", "ELCGOSCGBTFH", executionTime_3, input.getTotalTime() - executionTime_3,
                input.getTotalTime(), input.getCoolingSubCar().getId(), endSubCarStatus_2,true, openBoxInput.getHotOpenBoxPosition().getCoordinate(),
                endSubCarStatus_3, true, ferryCoordinate, input.getCoolingFerry().getId(), endFerryStatus_1,true, ferryCoordinate, endFerryStatus_2,
                true, ferryCoordinate, null,null, null, null, null, null,
                null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getCoolingSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.RUNNING, ferryCoordinate, null);

        //事件加入甘特图
        simulationOutput.getCoolingInput().getGanttChart().getEventLinkGantts().get(input.getCoolingIndexInput().getCoolingGoOpenBoxSubCarInGanttIndex()).getEvents().add(event_3);

        //改变车辆状态和时间
        input.getCoolingSubCar().setSubCarState(endSubCarStatus_3);
        input.getCoolingFerry().setFerryState(endFerryStatus_2);
        openBoxInput.getHotOpenBoxPosition().setStatus(PositionStatusEnum.OCCUPIED);


        openBoxInput.getHotOpenBoxPosition().setProduct(product);
        input.getCoolingSubCar().setProduct(null);

        simulationOutput.getCoolingInput().setInput(input);
        simulationOutput.getOpenBoxInput().setInput(openBoxInput);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink fullGoHotOpenBoxPositionEventCreate(AfterClosingBoxAllLink simulationOutput) {
        CoolingSimulationInput input = simulationOutput.getCoolingInput().getInput();
        OpenBoxSimulationInput openBoxInput = simulationOutput.getOpenBoxInput().getInput();
        //发生母车前往热砂合箱点位，子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getCoolingFerry().getLocationCoordinate().getY() -
                openBoxInput.getHotOpenBoxPosition().getCoordinate().getY()) / input.getCoolingFerry().getFullSpeed();
        Coordinate aimCoordinate = new Coordinate(input.getCoolingFerry().getLocationCoordinate().getX(),
                openBoxInput.getHotOpenBoxPosition().getCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getCoolingSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getCoolingFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(CARRY_SUB_CAR);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getCoolingGoOpenBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        //生成事件
        Event event_1 = allTool.createEvent("100605", "FLCGOFGHOBP", executionTime, input.getTotalTime() - executionTime,
                input.getTotalTime(), input.getCoolingSubCar().getId(), startSubCarStatus, false, null,
                endSubCarStatus, true, aimCoordinate, input.getCoolingFerry().getId(), startFerryStatus, false,
                null, endFerryStatus, true, aimCoordinate, null,null,
                null, null, null, null, null,null,
                null, pathIds, pathStatusEnums, pathStatusEnums, input.getCoolingFerry().getId(), ExecuteTypeEnum.FERRY,
                InstructionsEnum.RUNNING, aimCoordinate, input.getCoolingSubCar().getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getCoolingInput().getGanttChart().getEventLinkGantts().get(input.getCoolingIndexInput().getCoolingGoOpenBoxSubCarInGanttIndex()).getEvents().add(event_1);

        //改变车辆状态和时间
        input.getCoolingSubCar().setSubCarState(endSubCarStatus);
        input.getCoolingFerry().setFerryState(endFerryStatus);
        input.getCoolingSubCar().setLocationCoordinate(aimCoordinate);
        input.getCoolingFerry().setLocationCoordinate(aimCoordinate);

        simulationOutput.getCoolingInput().setInput(input);
        simulationOutput.getOpenBoxInput().setInput(openBoxInput);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink fullSubCarGoColdAndHotOpenBoxPositionEventCreate(AfterClosingBoxAllLink simulationOutput) {
        CoolingSimulationInput input = simulationOutput.getCoolingInput().getInput();
        OpenBoxSimulationInput openBoxInput = simulationOutput.getOpenBoxInput().getInput();
        //发生母车前往热砂合箱点位，子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getCoolingSubCar().getLocationCoordinate().getX() -
                openBoxInput.getColdAndHotOpenBoxPosition().getCoordinate().getX()) / input.getCoolingSubCar().getFullSpeed();
        double executionTime_2 = input.getCoolingSubCar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getCoolingSubCar().getLocationCoordinate().getX() -
                openBoxInput.getColdAndHotOpenBoxPosition().getCoordinate().getX()) / input.getCoolingSubCar().getEmptySpeed();
        Coordinate ferryCoordinate = input.getCoolingFerry().getLocationCoordinate().coordinateDeepCopy();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getCoolingSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getCoolingFerry().getFerryState());
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
        pathIds.add(input.getCoolingGoOpenBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getCoolingSubCar().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.GOING_UNBOXING);
        product.setProductFinish(ProductFinishEnum.FINISH_COOLING);

        //生成事件
        Event event_1 = allTool.createEvent("1006010", "FLCGOSCGICAHOBP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_2 - executionTime_3, input.getCoolingSubCar().getId(), startSubCarStatus_1, true, ferryCoordinate,
                endSubCarStatus_1, true, openBoxInput.getColdAndHotOpenBoxPosition().getCoordinate(), input.getCoolingFerry().getId(), endFerryStatus_2,
                true, ferryCoordinate, endFerryStatus_1, true, ferryCoordinate, null,null, null,
                openBoxInput.getColdAndHotOpenBoxPosition().getId(), startPositionStatus, startPositionStatus, null,null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCoolingSubCar().getId(), ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, openBoxInput.getColdAndHotOpenBoxPosition().getCoordinate(), product);

        //事件加入甘特图
        simulationOutput.getCoolingInput().getGanttChart().getEventLinkGantts().get(input.getCoolingIndexInput().getCoolingGoOpenBoxSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("1006011", "FLCGOCSLICAHOBP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime()- executionTime_3, input.getCoolingSubCar().getId(), endSubCarStatus_1, true,
                openBoxInput.getColdAndHotOpenBoxPosition().getCoordinate(), endSubCarStatus_2, true, openBoxInput.getColdAndHotOpenBoxPosition().getCoordinate(),
                input.getCoolingFerry().getId(), endFerryStatus_1,true, ferryCoordinate, endFerryStatus_1, true, ferryCoordinate, null,
                null, null, openBoxInput.getColdAndHotOpenBoxPosition().getId(), startPositionStatus, endPositionStatus,
                null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getCoolingSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getCoolingInput().getGanttChart().getEventLinkGantts().get(input.getCoolingIndexInput().getCoolingGoOpenBoxSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("1006012", "ELCGOSCGBTFCAH", executionTime_3, input.getTotalTime() - executionTime_3,
                input.getTotalTime(), input.getCoolingSubCar().getId(), endSubCarStatus_2,true, openBoxInput.getColdAndHotOpenBoxPosition().getCoordinate(),
                endSubCarStatus_3, true, ferryCoordinate, input.getCoolingFerry().getId(), endFerryStatus_1,true, ferryCoordinate, endFerryStatus_2,
                true, ferryCoordinate, null,null, null, null, null, null,
                null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getCoolingSubCar().getId(), ExecuteTypeEnum.SUB_CAR, InstructionsEnum.RUNNING, ferryCoordinate, null);

        //事件加入甘特图
        simulationOutput.getCoolingInput().getGanttChart().getEventLinkGantts().get(input.getCoolingIndexInput().getCoolingGoOpenBoxSubCarInGanttIndex()).getEvents().add(event_3);

        //改变车辆状态和时间
        input.getCoolingSubCar().setSubCarState(endSubCarStatus_3);
        input.getCoolingFerry().setFerryState(endFerryStatus_2);
        openBoxInput.getColdAndHotOpenBoxPosition().setStatus(PositionStatusEnum.OCCUPIED);


        openBoxInput.getColdAndHotOpenBoxPosition().setProduct(product);
        input.getCoolingSubCar().setProduct(null);

        simulationOutput.getCoolingInput().setInput(input);
        simulationOutput.getOpenBoxInput().setInput(openBoxInput);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink fullGoColdAndHotOpenBoxPositionEventCreate(AfterClosingBoxAllLink simulationOutput) {
        CoolingSimulationInput input = simulationOutput.getCoolingInput().getInput();
        OpenBoxSimulationInput openBoxInput = simulationOutput.getOpenBoxInput().getInput();
        //发生母车前往热砂合箱点位，子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getCoolingFerry().getLocationCoordinate().getY() -
                openBoxInput.getColdAndHotOpenBoxPosition().getCoordinate().getY()) / input.getCoolingFerry().getFullSpeed();
        Coordinate aimCoordinate = new Coordinate(input.getCoolingFerry().getLocationCoordinate().getX(),
                openBoxInput.getColdAndHotOpenBoxPosition().getCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getCoolingSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getCoolingFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(CARRY_SUB_CAR);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getCoolingGoOpenBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        //生成事件
        Event event_1 = allTool.createEvent("100609", "FLCGOFGHOBP", executionTime, input.getTotalTime() - executionTime,
                input.getTotalTime(), input.getCoolingSubCar().getId(), startSubCarStatus, false, null,
                endSubCarStatus, true, aimCoordinate, input.getCoolingFerry().getId(), startFerryStatus, false,
                null, endFerryStatus, true, aimCoordinate, null,null,
                null, null, null, null, null,null,
                null, pathIds, pathStatusEnums, pathStatusEnums, input.getCoolingFerry().getId(), ExecuteTypeEnum.FERRY,
                InstructionsEnum.RUNNING, aimCoordinate, input.getCoolingSubCar().getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getCoolingInput().getGanttChart().getEventLinkGantts().get(input.getCoolingIndexInput().getCoolingGoOpenBoxSubCarInGanttIndex()).getEvents().add(event_1);

        //改变车辆状态和时间
        input.getCoolingSubCar().setSubCarState(endSubCarStatus);
        input.getCoolingFerry().setFerryState(endFerryStatus);
        input.getCoolingSubCar().setLocationCoordinate(aimCoordinate);
        input.getCoolingFerry().setLocationCoordinate(aimCoordinate);

        simulationOutput.getCoolingInput().setInput(input);
        simulationOutput.getOpenBoxInput().setInput(openBoxInput);

        return simulationOutput;
    }
}
