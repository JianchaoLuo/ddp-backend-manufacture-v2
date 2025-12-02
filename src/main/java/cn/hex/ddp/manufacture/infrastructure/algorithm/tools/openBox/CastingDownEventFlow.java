package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.openBox;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
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
 * @Created: 2024/8/1 3:05
 */
@NoArgsConstructor
public class CastingDownEventFlow {
    /**
     * 仿真总工具类
     */
    private final SimulationTool allTool = new SimulationTool();

    public AfterClosingBoxAllLink EmptyCastingDownGoPositionEventCreate(AfterClosingBoxAllLink simulationOutput) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生空载子车前往点位装载待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getCastingDownSubCar().getLocationCoordinate().getX() - input.getCastingDownPosition().getCoordinate().getX())
                / input.getCastingDownSubCar().getEmptySpeed();
        double executionTime_2 = input.getCastingDownSubCar().getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getCastingDownSubCar().getSubCarState());
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
        pathIds.add(input.getCastingDownPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getCastingDownPosition().getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100741", "ELCDSCGP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2,
                input.getTotalTime() - executionTime_2, input.getCastingDownSubCar().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, input.getCastingDownPosition().getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getCastingDownPosition().getId(), startPositionStatus, startPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingDownSubCar().getId(), SUB_CAR, RUNNING, input.getCastingDownPosition().getCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingDownSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100742", "ELCDSCLIP", executionTime_2, input.getTotalTime() - executionTime_2,
                input.getTotalTime(), input.getCastingDownSubCar().getId(), startSubCarStatus, true, input.getCastingDownPosition().getCoordinate(), endSubCarStatus,
                true, input.getCastingDownPosition().getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getCastingDownPosition().getId(), startPositionStatus, endPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingDownSubCar().getId(), SUB_CAR, RISING, null, product);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingDownSubCarInGanttIndex()).getEvents().add(event_2);

        //改变车辆状态和时间
        input.getCastingDownSubCar().setSubCarState(endSubCarStatus);
        input.getCastingDownSubCar().setLocationCoordinate(input.getCastingDownPosition().getCoordinate());
        input.getCastingDownPosition().setStatus(UNOCCUPIED);

        input.getCastingDownSubCar().setProduct(product);
        input.getCastingDownPosition().setProduct(null);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink EmptyCastingDownGoWaitPositionEventCreate(AfterClosingBoxAllLink simulationOutput, Coordinate waitCoordinate) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生空载子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getCastingDownSubCar().getLocationCoordinate().getX() - waitCoordinate.getX())
                / input.getCastingDownSubCar().getEmptySpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getCastingDownSubCar().getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getCastingDownPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = allTool.createEvent("100743", "ELCDSCGWP", executionTime, input.getTotalTime() - executionTime,
                input.getTotalTime(), input.getCastingDownSubCar().getId(), startSubCarStatus, false, null,
                endSubCarStatus, true, waitCoordinate, null, null, false, null,
                null, false, null, null, null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingDownSubCar().getId(), SUB_CAR, RUNNING, waitCoordinate, null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingDownSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getCastingDownSubCar().setSubCarState(endSubCarStatus);
        input.getCastingDownSubCar().setLocationCoordinate(waitCoordinate);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink FullCastingDownGoPositionEventCreate(AfterClosingBoxAllLink simulationOutput) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生满载子车前往点位卸载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getCastingDownSubCar().getLocationCoordinate().getX() - input.getCastingShippingPosition().getCoordinate().getX())
                / input.getCastingDownSubCar().getFullSpeed();
        double executionTime_2 = input.getCastingDownSubCar().getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getCastingDownSubCar().getSubCarState());
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
        pathIds.add(input.getCastingDownPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getCastingDownSubCar().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.REFLUXING);
        product.setProductFinish(ProductFinishEnum.FINISH_UNBOXING);

        //生成事件
        Event event_1 = allTool.createEvent("100744", "FLCDSCGP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2,
                input.getTotalTime() - executionTime_2, input.getCastingDownSubCar().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, input.getCastingShippingPosition().getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getCastingShippingPosition().getId(), startPositionStatus, startPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingDownSubCar().getId(), SUB_CAR, RUNNING, input.getCastingShippingPosition().getCoordinate(), product);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingDownSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100745", "FLCDSCLIP", executionTime_2, input.getTotalTime() - executionTime_2,
                input.getTotalTime(), input.getCastingDownSubCar().getId(), startSubCarStatus, true, input.getCastingShippingPosition().getCoordinate(),
                endSubCarStatus, true, input.getCastingShippingPosition().getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getCastingShippingPosition().getId(), startPositionStatus, endPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingDownSubCar().getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingDownSubCarInGanttIndex()).getEvents().add(event_2);

        //改变车辆状态和时间
        input.getCastingDownSubCar().setSubCarState(endSubCarStatus);
        input.getCastingDownSubCar().setLocationCoordinate(input.getCastingShippingPosition().getCoordinate());
        input.getCastingShippingPosition().setStatus(OCCUPIED);

        input.getCastingShippingPosition().setProduct(product);
        input.getCastingDownSubCar().setProduct(null);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink FullCastingDownGoWaitPositionEventCreate(AfterClosingBoxAllLink simulationOutput, Coordinate waitCoordinate) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生满载子车前往轨道等待的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getCastingDownSubCar().getLocationCoordinate().getX() - waitCoordinate.getX())
                / input.getCastingDownSubCar().getFullSpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>();
        startSubCarStatus.add(FULL_LOAD);
        startSubCarStatus.add(TOP_ROD_RAISED);
        startSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getCastingDownPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = allTool.createEvent("100746", "FLCDSCGWP", executionTime, input.getTotalTime() - executionTime,
                input.getTotalTime(), input.getCastingDownSubCar().getId(), startSubCarStatus, false, null,
                endSubCarStatus, true, waitCoordinate, null, null, false, null,
                null, false, null, null, null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingDownSubCar().getId(), SUB_CAR, RUNNING, waitCoordinate,
                input.getCastingDownSubCar().getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingDownSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getCastingDownSubCar().setSubCarState(endSubCarStatus);
        input.getCastingDownSubCar().setLocationCoordinate(waitCoordinate);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink FullCastingDownGoWaitPositionEventCreate_2(AfterClosingBoxAllLink simulationOutput, Integer workStationIndex, Coordinate waitCoordinate) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生满载子车前往轨道等待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getCastingDownSubCar().getLocationCoordinate().getX() -
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX()) / input.getCastingDownSubCar().getFullSpeed();

        Coordinate aimCoordinate = new Coordinate(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX(),
                input.getCastingDownSubCar().getLocationCoordinate().getY());

        /*
        注意：这里是需要表示一下车辆等待零件搬运上车的时间，因为没有相应参数，所以选择用车辆顶杆升降时间代替
         */
        double executionTime_2 = input.getCastingDownSubCar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() - waitCoordinate.getX())
                / input.getCastingDownSubCar().getFullSpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>();
        startSubCarStatus.add(FULL_LOAD);
        startSubCarStatus.add(TOP_ROD_RAISED);
        startSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getCastingDownPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event_1 = allTool.createEvent("100766", "FLCDSCGWP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_2 - executionTime_3, input.getCastingDownSubCar().getId(), startSubCarStatus, false,
                null, startSubCarStatus, true, aimCoordinate, null, null, false,
                null, null, false, null, null, null,
                null, null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingDownSubCar().getId(), SUB_CAR, RUNNING, aimCoordinate,
                input.getCastingDownSubCar().getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingDownSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100763", "FLCUSCGWP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_3, input.getCastingDownSubCar().getId(), startSubCarStatus, true,
                aimCoordinate, startSubCarStatus, true, aimCoordinate, null, null, false,
                null, null, false, null, null, null,
                null, null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingDownSubCar().getId(), SUB_CAR, CAR_WAITING, waitCoordinate,
                input.getCastingDownSubCar().getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingDownSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100764", "FLCUSCGWP", executionTime_3, input.getTotalTime() - executionTime_3,
                input.getTotalTime(), input.getCastingDownSubCar().getId(), startSubCarStatus, false, null,
                endSubCarStatus, true, waitCoordinate, null, null, false, null,
                null, false, null, null, null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getCastingDownSubCar().getId(), SUB_CAR, RUNNING, waitCoordinate,
                input.getCastingDownSubCar().getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getCastingDownSubCarInGanttIndex()).getEvents().add(event_3);

        //改变车辆状态和时间
        input.getCastingDownSubCar().setSubCarState(endSubCarStatus);
        input.getCastingDownSubCar().setLocationCoordinate(waitCoordinate);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }
}
