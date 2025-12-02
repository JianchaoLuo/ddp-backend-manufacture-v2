package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.openBox;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.OpenBoxSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.OutBoundSimulationInput;
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
 * @Created: 2024/8/1 3:06
 */
@NoArgsConstructor
public class SandBoxRefluxEventFlow {
    /**
     * 仿真总工具类
     */
    private final SimulationTool allTool = new SimulationTool();

    public AfterClosingBoxAllLink EmptySandBoxRefluxGoPositionEventCreate(AfterClosingBoxAllLink simulationOutput) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生空载子车前往点位装载待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getSandBoxRefluxRightSubCar().getLocationCoordinate().getY() - input.getColdAndHotRefluxPosition().getCoordinate().getY())
                / input.getSandBoxRefluxRightSubCar().getEmptySpeed();
        double executionTime_2 = input.getSandBoxRefluxRightSubCar().getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSandBoxRefluxRightSubCar().getSubCarState());
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
        pathIds.add(input.getSandBoxRefluxRightPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getColdAndHotRefluxPosition().getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100753", "ELSBRSCGP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2,
                input.getTotalTime() - executionTime_2, input.getSandBoxRefluxRightSubCar().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, input.getColdAndHotRefluxPosition().getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getColdAndHotRefluxPosition().getId(), startPositionStatus, startPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getSandBoxRefluxRightSubCar().getId(), SUB_CAR, RUNNING, input.getColdAndHotRefluxPosition().getCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getSandBoxRefluxRightSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100754", "ELSBRSCLIP", executionTime_2, input.getTotalTime() - executionTime_2,
                input.getTotalTime(), input.getSandBoxRefluxRightSubCar().getId(), startSubCarStatus, true, input.getColdAndHotRefluxPosition().getCoordinate(), endSubCarStatus,
                true, input.getColdAndHotRefluxPosition().getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getColdAndHotRefluxPosition().getId(), startPositionStatus, endPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getSandBoxRefluxRightSubCar().getId(), SUB_CAR, RISING, null, product);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getSandBoxRefluxRightSubCarInGanttIndex()).getEvents().add(event_2);

        //改变车辆状态和时间
        input.getSandBoxRefluxRightSubCar().setSubCarState(endSubCarStatus);
        input.getSandBoxRefluxRightSubCar().setLocationCoordinate(input.getColdAndHotRefluxPosition().getCoordinate());
        input.getColdAndHotRefluxPosition().setStatus(UNOCCUPIED);

        input.getSandBoxRefluxRightSubCar().setProduct(product);
        input.getColdAndHotRefluxPosition().setProduct(null);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink EmptySandBoxRefluxGoWaitPositionEventCreate(AfterClosingBoxAllLink simulationOutput, Coordinate waitCoordinate) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生空载子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getSandBoxRefluxRightSubCar().getLocationCoordinate().getY() - waitCoordinate.getY())
                / input.getSandBoxRefluxRightSubCar().getEmptySpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSandBoxRefluxRightSubCar().getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getSandBoxRefluxRightPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = allTool.createEvent("100755", "ELSBRSCGWP", executionTime, input.getTotalTime() - executionTime,
                input.getTotalTime(), input.getSandBoxRefluxRightSubCar().getId(), startSubCarStatus, false, null,
                endSubCarStatus, true, waitCoordinate, null, null, false, null,
                null, false, null, null, null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getSandBoxRefluxRightSubCar().getId(), SUB_CAR, RUNNING, waitCoordinate, null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getSandBoxRefluxRightSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSandBoxRefluxRightSubCar().setSubCarState(endSubCarStatus);
        input.getSandBoxRefluxRightSubCar().setLocationCoordinate(waitCoordinate);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink FullSandBoxRefluxGoPositionEventCreate(AfterClosingBoxAllLink simulationOutput) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        OutBoundSimulationInput outBoundInput = simulationOutput.getOutBoundInput().getInput();
        //发生满载子车前往点位卸载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getSandBoxRefluxRightSubCar().getLocationCoordinate().getY() -
                outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getY())
                / input.getSandBoxRefluxRightSubCar().getFullSpeed();
        double executionTime_2 = input.getSandBoxRefluxRightSubCar().getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSandBoxRefluxRightSubCar().getSubCarState());
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
        pathIds.add(input.getSandBoxRefluxRightPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getSandBoxRefluxRightSubCar().getProduct().productDeepCopy();
        product.setRefluxFlag(true);

        //生成事件
        Event event_1 = allTool.createEvent("100756", "FLSBRSCGP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2,
                input.getTotalTime() - executionTime_2, input.getSandBoxRefluxRightSubCar().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate(),
                null, null, false, null, null, false, null, null,
                null, null, outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getId(),
                startPositionStatus, startPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSandBoxRefluxRightSubCar().getId(), SUB_CAR, RUNNING,
                outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate(), product);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getSandBoxRefluxRightSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100757", "FLSBRSCLIP", executionTime_2, input.getTotalTime() - executionTime_2,
                input.getTotalTime(), input.getSandBoxRefluxRightSubCar().getId(), startSubCarStatus, true,
                outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate(),
                endSubCarStatus, true, outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate(),
                null, null, false, null, null, false, null,
                null, null, null,
                outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getId(),
                startPositionStatus, endPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getSandBoxRefluxRightSubCar().getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getSandBoxRefluxRightSubCarInGanttIndex()).getEvents().add(event_2);

        //改变车辆状态和时间
        input.getSandBoxRefluxRightSubCar().setSubCarState(endSubCarStatus);
        input.getSandBoxRefluxRightSubCar().setLocationCoordinate(outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate());
        outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).setStatus(OCCUPIED);

        outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).setProduct(product);
        input.getSandBoxRefluxRightSubCar().setProduct(null);

        simulationOutput.getOpenBoxInput().setInput(input);
        simulationOutput.getOutBoundInput().setInput(outBoundInput);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink FullSandBoxRefluxGoWaitPositionEventCreate(AfterClosingBoxAllLink simulationOutput, Coordinate waitCoordinate) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生满载子车前往轨道等待的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getSandBoxRefluxRightSubCar().getLocationCoordinate().getY() - waitCoordinate.getY())
                / input.getSandBoxRefluxRightSubCar().getFullSpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSandBoxRefluxRightSubCar().getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getSandBoxRefluxRightPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = allTool.createEvent("100758", "FLSBRSCGWP", executionTime, input.getTotalTime() - executionTime,
                input.getTotalTime(), input.getSandBoxRefluxRightSubCar().getId(), startSubCarStatus, false, null,
                endSubCarStatus, true, waitCoordinate, null, null, false, null,
                null, false, null, null, null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getSandBoxRefluxRightSubCar().getId(), SUB_CAR, RUNNING, waitCoordinate, input.getSandBoxRefluxRightSubCar().getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getSandBoxRefluxRightSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSandBoxRefluxRightSubCar().setSubCarState(endSubCarStatus);
        input.getSandBoxRefluxRightSubCar().setLocationCoordinate(waitCoordinate);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }
}
