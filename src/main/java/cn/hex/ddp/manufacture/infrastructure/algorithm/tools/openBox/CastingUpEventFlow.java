package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.openBox;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductAfootEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductFinishEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
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
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum.SUB_CAR;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum.*;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum.*;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/8/1 3:04
 */
@NoArgsConstructor
public class CastingUpEventFlow {
    /**
     * 仿真总工具类
     */
    private SimulationTool allTool = new SimulationTool();

    public AfterClosingBoxAllLink EmptyCastingUpGoPositionEventCreate(AfterClosingBoxAllLink simulationOutput) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生空载子车前往点位装载待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getCastingUpSubCar().getLocationCoordinate().getY() - input.getCastingUpPosition().getCoordinate().getY())
                / input.getCastingUpSubCar().getEmptySpeed();
        double executionTime_2 = input.getCastingUpSubCar().getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>();
        startSubCarStatus.addAll(input.getCastingUpSubCar().getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //点位状态列表
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        startPositionStatus.add(OCCUPIED);
        endPositionStatus.add(UNOCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getCastingUpPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getCastingUpPosition().getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100735", "ELCUSCGP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2,
                input.getTotalTime() - executionTime_2, input.getCastingUpSubCar().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, input.getCastingUpPosition().getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getCastingUpPosition().getId(), startPositionStatus, startPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingUpSubCar().getId(), SUB_CAR, RUNNING, input.getCastingUpPosition().getCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingUpSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100736", "ELCUSCLIP", executionTime_2, input.getTotalTime() - executionTime_2,
                input.getTotalTime(), input.getCastingUpSubCar().getId(), startSubCarStatus, true, input.getCastingUpPosition().getCoordinate(), endSubCarStatus,
                true, input.getCastingUpPosition().getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getCastingUpPosition().getId(), startPositionStatus, endPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingUpSubCar().getId(), SUB_CAR, RISING, null, product);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingUpSubCarInGanttIndex()).getEvents().add(event_2);

        //改变车辆状态和时间
        input.getCastingUpSubCar().setSubCarState(endSubCarStatus);
        input.getCastingUpSubCar().setLocationCoordinate(input.getCastingUpPosition().getCoordinate());
        input.getCastingUpPosition().setStatus(UNOCCUPIED);

        input.getCastingUpSubCar().setProduct(product);
        input.getCastingUpPosition().setProduct(null);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink EmptyCastingUpGoWaitPositionEventCreate(AfterClosingBoxAllLink simulationOutput, Coordinate waitCoordinate) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生空载子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getCastingUpSubCar().getLocationCoordinate().getX() - waitCoordinate.getX())
                / input.getCastingUpSubCar().getEmptySpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>();
        startSubCarStatus.addAll(input.getCastingUpSubCar().getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getCastingUpPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = allTool.createEvent("100737", "ELCUSCGWP", executionTime, input.getTotalTime() - executionTime,
                input.getTotalTime(), input.getCastingUpSubCar().getId(), startSubCarStatus, false, null,
                endSubCarStatus, true, waitCoordinate, null, null, false, null,
                null, false, null, null, null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingUpSubCar().getId(), SUB_CAR, RUNNING, waitCoordinate, null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingUpSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getCastingUpSubCar().setSubCarState(endSubCarStatus);
        input.getCastingUpSubCar().setLocationCoordinate(waitCoordinate);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink FullCastingUpGoPositionEventCreate(AfterClosingBoxAllLink simulationOutput) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生满载子车前往点位卸载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getCastingUpSubCar().getLocationCoordinate().getX() - input.getCastingVerticalPosition().getCoordinate().getX())
                / input.getCastingUpSubCar().getFullSpeed();
        double executionTime_2 = input.getCastingUpSubCar().getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>();
        startSubCarStatus.addAll(input.getCastingUpSubCar().getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //点位状态列表
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        startPositionStatus.add(UNOCCUPIED);
        endPositionStatus.add(OCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getCastingUpPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getCastingUpSubCar().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.REFLUXING);
        product.setProductFinish(ProductFinishEnum.FINISH_UNBOXING);

        //生成事件
        Event event_1 = allTool.createEvent("100738", "FLCUSCGP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2,
                input.getTotalTime() - executionTime_2, input.getCastingUpSubCar().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, input.getCastingVerticalPosition().getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getCastingVerticalPosition().getId(), startPositionStatus, startPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingUpSubCar().getId(), SUB_CAR, RUNNING, input.getCastingVerticalPosition().getCoordinate(), product);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingUpSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100739", "FLCUSCLIP", executionTime_2, input.getTotalTime() - executionTime_2,
                input.getTotalTime(), input.getCastingUpSubCar().getId(), startSubCarStatus, true, input.getCastingVerticalPosition().getCoordinate(),
                endSubCarStatus, true, input.getCastingVerticalPosition().getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getCastingVerticalPosition().getId(), startPositionStatus, endPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingUpSubCar().getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingUpSubCarInGanttIndex()).getEvents().add(event_2);

        //改变车辆状态和时间
        input.getCastingUpSubCar().setSubCarState(endSubCarStatus);
        input.getCastingUpSubCar().setLocationCoordinate(input.getCastingVerticalPosition().getCoordinate());
        input.getCastingVerticalPosition().setStatus(OCCUPIED);

        input.getCastingVerticalPosition().setProduct(product);
        input.getCastingUpSubCar().setProduct(null);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink FullCastingUpGoWaitPositionEventCreate(AfterClosingBoxAllLink simulationOutput, Coordinate waitCoordinate) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生满载子车前往轨道等待的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getCastingUpSubCar().getLocationCoordinate().getX() - waitCoordinate.getX())
                / input.getCastingUpSubCar().getFullSpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getCastingUpSubCar().getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getCastingUpPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = allTool.createEvent("100740", "FLCUSCGWP", executionTime, input.getTotalTime() - executionTime,
                input.getTotalTime(), input.getCastingUpSubCar().getId(), startSubCarStatus, false, null,
                endSubCarStatus, true, waitCoordinate, null, null, false, null,
                null, false, null, null, null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingUpSubCar().getId(), SUB_CAR, RUNNING, waitCoordinate, input.getCastingUpSubCar().getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingUpSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getCastingUpSubCar().setSubCarState(endSubCarStatus);
        input.getCastingUpSubCar().setLocationCoordinate(waitCoordinate);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public void emptyCastingUpGoPositionLoadTrayEventCreate(AfterClosingBoxAllLink simulationOutput) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生空载子车前往点位装载待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getCastingUpSubCar().getLocationCoordinate().getY() - input.getCastingUpPosition().getCoordinate().getY())
                / input.getCastingUpSubCar().getEmptySpeed();
        double executionTime_2 = input.getCastingUpSubCar().getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getCastingUpSubCar().getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(HALF_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getCastingUpPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        //生成事件
        Event event_1 = allTool.createEvent("100765", "ELCUSCGP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2,
                input.getTotalTime() - executionTime_2, input.getCastingUpSubCar().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, input.getCastingUpPosition().getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingUpSubCar().getId(), SUB_CAR, RUNNING, input.getCastingUpPosition().getCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingUpSubCarInGanttIndex()).getEvents().add(event_1);

        //改变车辆状态和时间
        input.getCastingUpSubCar().setSubCarState(endSubCarStatus);
        input.getCastingUpSubCar().setLocationCoordinate(input.getCastingUpPosition().getCoordinate());

        simulationOutput.getOpenBoxInput().setInput(input);
    }

    public void halfSubCarGoWorkStationLoadCasting(AfterClosingBoxAllLink simulationOutput, int upWorkStationIndex) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生空载子车前往点位装载待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getCastingUpSubCar().getLocationCoordinate().getX() -
                input.getWorkStations().get(upWorkStationIndex).getWorkPositionCoordinate().getX()) / input.getCastingUpSubCar().getEmptySpeed();
        double executionTime_2 = input.getCastingUpSubCar().getTopRodRaiseOrFallTime();

        Coordinate aimCoordinate = new Coordinate(input.getWorkStations().get(upWorkStationIndex).getWorkPositionCoordinate().getX(),
                input.getCastingUpSubCar().getLocationCoordinate().getY());

        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>();
        startSubCarStatus.add(EMPTY_LOAD);
        startSubCarStatus.add(TOP_ROD_FALL_DOWN);
        startSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //点位状态列表
        List<WorkstationStatusEnum> startPositionStatus = new ArrayList<>();
        List<WorkstationStatusEnum> endPositionStatus = new ArrayList<>();
        startPositionStatus.add(WorkstationStatusEnum.WAIT_TRANSPORT);
        endPositionStatus.add(WorkstationStatusEnum.FREE);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getCastingUpPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getWorkStations().get(upWorkStationIndex).getProduct().getFirst().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100767", "ELCUSCGP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2,
                input.getTotalTime() - executionTime_2, input.getCastingUpSubCar().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, aimCoordinate, null, null, false, null, null,
                false, null, null, null, null, null, null,
                null, input.getWorkStations().get(upWorkStationIndex).getId(), startPositionStatus, startPositionStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingUpSubCar().getId(), SUB_CAR, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingUpSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100768", "ELCUSCLIP", executionTime_2, input.getTotalTime() - executionTime_2,
                input.getTotalTime(), input.getCastingUpSubCar().getId(), startSubCarStatus, true, aimCoordinate, endSubCarStatus,
                true, aimCoordinate, null, null, false, null, null,
                false, null, null, null, null, null,
                null, null, input.getWorkStations().get(upWorkStationIndex).getId(), startPositionStatus, endPositionStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingUpSubCar().getId(), SUB_CAR, RISING, null, product);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingUpSubCarInGanttIndex()).getEvents().add(event_2);

        //改变车辆状态和时间
        input.getCastingUpSubCar().setSubCarState(endSubCarStatus);
        input.getCastingUpSubCar().setLocationCoordinate(input.getCastingUpPosition().getCoordinate());
        input.getCastingUpPosition().setStatus(UNOCCUPIED);

        input.getCastingUpSubCar().setProduct(product);
        input.getCastingUpPosition().setProduct(null);

        simulationOutput.getOpenBoxInput().setInput(input);
    }

    public AfterClosingBoxAllLink FullCastingUpGoWaitPositionEventCreate_2(AfterClosingBoxAllLink simulationOutput, Integer workStationIndex, Coordinate waitCoordinate) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生满载子车前往轨道等待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getCastingUpSubCar().getLocationCoordinate().getX() -
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX()) / input.getCastingUpSubCar().getFullSpeed();
        /*
        注意：这里是需要表示一下车辆等待零件搬运上车的时间，因为没有相应参数，所以选择用车辆顶杆升降时间代替
         */
        double executionTime_2 = input.getCastingUpSubCar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() - waitCoordinate.getX())
                / input.getCastingUpSubCar().getFullSpeed();

        Coordinate aimCoordinate = new Coordinate(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX(),
                input.getCastingUpSubCar().getLocationCoordinate().getY());

        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getCastingUpSubCar().getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getCastingUpPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        //生成事件
        Event event_1 = allTool.createEvent("100760", "FLCUSCGWP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_2 - executionTime_3, input.getCastingUpSubCar().getId(), startSubCarStatus, false,
                null, startSubCarStatus, true, aimCoordinate, null, null, false,
                null, null, false, null, null, null,
                null, null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingUpSubCar().getId(), SUB_CAR, RUNNING, aimCoordinate, input.getCastingUpSubCar().getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingUpSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100761", "FLCUSCGWP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_3, input.getCastingUpSubCar().getId(), startSubCarStatus, true,
                aimCoordinate, startSubCarStatus, true, aimCoordinate, null, null, false,
                null, null, false, null, null, null,
                null, null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingUpSubCar().getId(), SUB_CAR, CAR_WAITING, waitCoordinate,
                input.getCastingUpSubCar().getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingUpSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100762", "FLCUSCGWP", executionTime_3, input.getTotalTime() - executionTime_3,
                input.getTotalTime(), input.getCastingUpSubCar().getId(), startSubCarStatus, false, null,
                endSubCarStatus, true, waitCoordinate, null, null, false, null,
                null, false, null, null, null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingUpSubCar().getId(), SUB_CAR, RUNNING, waitCoordinate,
                input.getCastingUpSubCar().getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingUpSubCarInGanttIndex()).getEvents().add(event_3);

        //改变车辆状态和时间
        input.getCastingUpSubCar().setSubCarState(endSubCarStatus);
        input.getCastingUpSubCar().setLocationCoordinate(waitCoordinate);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }
}
