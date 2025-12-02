package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.openBox;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
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
 * @Created: 2024/8/1 3:05
 */
@NoArgsConstructor
public class CastingVerticalEventFlow {
    /**
     * 仿真总工具类
     */
    private SimulationTool allTool = new SimulationTool();

    public AfterClosingBoxAllLink EmptyCastingVerticalGoPositionEventCreate(AfterClosingBoxAllLink simulationOutput) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生空载子车前往点位装载待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getCastingVerticalSubCar().getLocationCoordinate().getY() - input.getCastingVerticalPosition().getCoordinate().getY())
                / input.getCastingVerticalSubCar().getEmptySpeed();
        double executionTime_2 = input.getCastingVerticalSubCar().getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>();
        startSubCarStatus.addAll(input.getCastingVerticalSubCar().getSubCarState());
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
        pathIds.add(input.getCastingVerticalPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getCastingVerticalPosition().getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100747", "ELCVSCGP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2,
                input.getTotalTime() - executionTime_2, input.getCastingVerticalSubCar().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, input.getCastingVerticalPosition().getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getCastingVerticalPosition().getId(), startPositionStatus, startPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingVerticalSubCar().getId(), SUB_CAR, RUNNING, input.getCastingVerticalPosition().getCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingVerticalSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100748", "ELCVSCLIP", executionTime_2, input.getTotalTime() - executionTime_2,
                input.getTotalTime(), input.getCastingVerticalSubCar().getId(), startSubCarStatus, true, input.getCastingVerticalPosition().getCoordinate(), endSubCarStatus,
                true, input.getCastingVerticalPosition().getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getCastingVerticalPosition().getId(), startPositionStatus, endPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingVerticalSubCar().getId(), SUB_CAR, RISING, null, product);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingVerticalSubCarInGanttIndex()).getEvents().add(event_2);

        //改变车辆状态和时间
        input.getCastingVerticalSubCar().setSubCarState(endSubCarStatus);
        input.getCastingVerticalSubCar().setLocationCoordinate(input.getCastingVerticalPosition().getCoordinate());
        input.getCastingVerticalPosition().setStatus(UNOCCUPIED);

        input.getCastingVerticalSubCar().setProduct(product);
        input.getCastingVerticalPosition().setProduct(null);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink EmptyCastingVerticalGoWaitPositionEventCreate(AfterClosingBoxAllLink simulationOutput, Coordinate waitCoordinate) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生空载子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getCastingVerticalSubCar().getLocationCoordinate().getY() - waitCoordinate.getY())
                / input.getCastingVerticalSubCar().getEmptySpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getCastingVerticalSubCar().getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getCastingVerticalPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = allTool.createEvent("100749", "ELCVSCGWP", executionTime, input.getTotalTime() - executionTime,
                input.getTotalTime(), input.getCastingVerticalSubCar().getId(), startSubCarStatus, false, null,
                endSubCarStatus, true, waitCoordinate, null, null, false, null,
                null, false, null, null, null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingVerticalSubCar().getId(), SUB_CAR, RUNNING, waitCoordinate, null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingVerticalSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getCastingVerticalSubCar().setSubCarState(endSubCarStatus);
        input.getCastingVerticalSubCar().setLocationCoordinate(waitCoordinate);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink FullCastingVerticalGoPositionEventCreate(AfterClosingBoxAllLink simulationOutput) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生满载子车前往点位卸载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getCastingVerticalSubCar().getLocationCoordinate().getY() - input.getCastingShippingPosition().getCoordinate().getY())
                / input.getCastingVerticalSubCar().getFullSpeed();
        double executionTime_2 = input.getCastingVerticalSubCar().getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>();
        startSubCarStatus.addAll(input.getCastingVerticalSubCar().getSubCarState());
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
        pathIds.add(input.getCastingVerticalPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getCastingVerticalSubCar().getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100750", "FLCVSCGP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2,
                input.getTotalTime() - executionTime_2, input.getCastingVerticalSubCar().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, input.getCastingShippingPosition().getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getCastingShippingPosition().getId(), startPositionStatus, startPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingVerticalSubCar().getId(), SUB_CAR, RUNNING, input.getCastingShippingPosition().getCoordinate(), product);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingVerticalSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100751", "FLCVSCLIP", executionTime_2, input.getTotalTime() - executionTime_2,
                input.getTotalTime(), input.getCastingVerticalSubCar().getId(), startSubCarStatus, true, input.getCastingShippingPosition().getCoordinate(),
                endSubCarStatus, true, input.getCastingShippingPosition().getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getCastingShippingPosition().getId(), startPositionStatus, endPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingVerticalSubCar().getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingVerticalSubCarInGanttIndex()).getEvents().add(event_2);

        //改变车辆状态和时间
        input.getCastingVerticalSubCar().setSubCarState(endSubCarStatus);
        input.getCastingVerticalSubCar().setLocationCoordinate(input.getCastingShippingPosition().getCoordinate());
        input.getCastingShippingPosition().setStatus(OCCUPIED);

        input.getCastingShippingPosition().setProduct(product);
        input.getCastingVerticalSubCar().setProduct(null);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink FullCastingVerticalGoWaitPositionEventCreate(AfterClosingBoxAllLink simulationOutput, Coordinate waitCoordinate) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生满载子车前往轨道等待的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getCastingVerticalSubCar().getLocationCoordinate().getY() - waitCoordinate.getY())
                / input.getCastingVerticalSubCar().getFullSpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>();
        startSubCarStatus.addAll(input.getCastingVerticalSubCar().getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getCastingVerticalPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = allTool.createEvent("100752", "FLCVSCGWP", executionTime, input.getTotalTime() - executionTime,
                input.getTotalTime(), input.getCastingVerticalSubCar().getId(), startSubCarStatus, false, null,
                endSubCarStatus, true, waitCoordinate, null, null, false, null,
                null, false, null, null, null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingVerticalSubCar().getId(), SUB_CAR, RUNNING, waitCoordinate,
                input.getCastingVerticalSubCar().getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingVerticalSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getCastingVerticalSubCar().setSubCarState(endSubCarStatus);
        input.getCastingVerticalSubCar().setLocationCoordinate(waitCoordinate);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }
}
