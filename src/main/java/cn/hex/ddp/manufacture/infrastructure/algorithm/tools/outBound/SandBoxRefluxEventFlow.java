package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.OutBoundSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;

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
 * @Created: 2024/8/4 17:32
 */
public class SandBoxRefluxEventFlow {
    /**
     * 仿真总工具类
     */
    private final SimulationTool allTool = new SimulationTool();

    public OutBoundGanttChartLinkInput EmptySandBoxRefluxGoHACPositionEventCreate(OutBoundGanttChartLinkInput simulationOutput, Coordinate refluxWaitCoordinate) {
        OutBoundSimulationInput input = simulationOutput.getInput();
        //发生空载子车前往点位装载待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getLocationCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getX())
                / input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getEmptySpeed();
        double executionTime_2 = input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getX() -
                refluxWaitCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getFullSpeed();
                //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getSubCarState());
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
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getSandBoxRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event_1 = allTool.createEvent("100901", "ELSBRSCGHACP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_2 - executionTime_3, input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getId(),
                startSubCarStatus, false, null, startSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate(), null, null,
                false, null, null, false, null, null,
                null, null, input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getId(),
                startPositionStatus, startPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getId(), SUB_CAR, RUNNING,
                input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100902", "ELSBRSCLIHACP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_3, input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getId(), startSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate(), endSubCarStatus,
                true, input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate(), null,
                null, false, null, null, false, null, null,
                null, null, input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getId(),
                startPositionStatus, endPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getId(), SUB_CAR, RISING, null,
                input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100903", "FLSBRSCGWPIHAC", executionTime_2, input.getTotalTime() - executionTime_3,
                input.getTotalTime(), input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getId(), endSubCarStatus,
                true, input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate(), endSubCarStatus,
                true, refluxWaitCoordinate, null, null, false, null, null,
                false, null, null, null, null, null, null,
                null, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getId(), SUB_CAR, RUNNING, refluxWaitCoordinate,
                input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarInGanttIndex()).getEvents().add(event_3);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).setLocationCoordinate(refluxWaitCoordinate);
        input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).setStatus(UNOCCUPIED);
        Product product = input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getProduct().productDeepCopy();
        input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).setProduct(product);
        input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).setProduct(null);

        simulationOutput.setInput(input);

        return simulationOutput;
    }

    public OutBoundGanttChartLinkInput EmptySandBoxRefluxGoHotPositionEventCreate(OutBoundGanttChartLinkInput simulationOutput, Coordinate refluxWaitCoordinate) {
        OutBoundSimulationInput input = simulationOutput.getInput();
        //发生空载子车前往点位装载待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getLocationCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getX())
                / input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getEmptySpeed();
        double executionTime_2 = input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getX() -
                refluxWaitCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getFullSpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getSubCarState());
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
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getSandBoxRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event_1 = allTool.createEvent("100904", "ELSBRSCGHP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_2 - executionTime_3, input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getId(),
                startSubCarStatus, false, null, startSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate(), null, null,
                false, null, null, false, null, null,
                null, null, input.getPositions().get(input.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getId(),
                startPositionStatus, startPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getId(), SUB_CAR, RUNNING,
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100905", "ELSBRSCLIHP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_3, input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getId(), startSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate(), endSubCarStatus,
                true, input.getPositions().get(input.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate(), null,
                null, false, null, null, false, null, null,
                null, null, input.getPositions().get(input.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getId(),
                startPositionStatus, endPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getId(), SUB_CAR, RISING, null,
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100906", "FLSBRSCGWPIH", executionTime_2, input.getTotalTime() - executionTime_3,
                input.getTotalTime(), input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getId(), endSubCarStatus,
                true, input.getPositions().get(input.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate(), endSubCarStatus,
                true, refluxWaitCoordinate, null, null, false, null, null,
                false, null, null, null, null, null, null,
                null, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getId(), SUB_CAR, RUNNING, refluxWaitCoordinate,
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarInGanttIndex()).getEvents().add(event_3);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).setLocationCoordinate(refluxWaitCoordinate);
        input.getPositions().get(input.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).setStatus(UNOCCUPIED);
        Product product = input.getPositions().get(input.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getProduct().productDeepCopy();
        input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).setProduct(product);
        input.getPositions().get(input.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).setProduct(null);

        simulationOutput.setInput(input);

        return simulationOutput;
    }

    public OutBoundGanttChartLinkInput EmptySandBoxRefluxGoWaitPositionEventCreate(OutBoundGanttChartLinkInput simulationOutput, Coordinate refluxWaitCoordinate) {
        OutBoundSimulationInput input = simulationOutput.getInput();
        //发生空载子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getLocationCoordinate().getX() -
                refluxWaitCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getEmptySpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getSandBoxRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = allTool.createEvent("100907", "ELSBRSCGWP", executionTime, input.getTotalTime() - executionTime,
                input.getTotalTime(), input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getId(), startSubCarStatus,
                false, null, endSubCarStatus, true, refluxWaitCoordinate, null, null,
                false, null, null, false, null, null,
                null, null, null, null, null, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getId(), SUB_CAR, RUNNING, refluxWaitCoordinate, null);

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).setLocationCoordinate(refluxWaitCoordinate);

        simulationOutput.setInput(input);

        return simulationOutput;
    }

    public OutBoundGanttChartLinkInput FullSandBoxRefluxGoHotPositionEventCreate(OutBoundGanttChartLinkInput simulationOutput) {
        OutBoundSimulationInput input = simulationOutput.getInput();
        //发生满载子车前往点位卸载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getLocationCoordinate().getX() -
                input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).getCoordinate().getX())
                / input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getFullSpeed();
        double executionTime_2 = input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getSubCarState());
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
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getSandBoxRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event_1 = allTool.createEvent("100908", "FLSBRSCGHP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2,
                input.getTotalTime() - executionTime_2, input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getId(),
                startSubCarStatus, false, null, startSubCarStatus, true,
                input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).getId(), startPositionStatus, startPositionStatus, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getId(), SUB_CAR, RUNNING,
                input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).getCoordinate(),
                input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100909", "FLSBRSCLIHP", executionTime_2, input.getTotalTime() - executionTime_2,
                input.getTotalTime(), input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getId(), startSubCarStatus,
                true, input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).getCoordinate(),
                endSubCarStatus, true, input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).getCoordinate(),
                null, null, false, null, null, false, null,
                null, null, null, input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).getId(),
                startPositionStatus, endPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarInGanttIndex()).getEvents().add(event_2);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).setLocationCoordinate(input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).getCoordinate());
        input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).setStatus(OCCUPIED);
        Product product = input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getProduct().productDeepCopy();
        input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).setProduct(product);
        input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).setProduct(null);

        simulationOutput.setInput(input);

        return simulationOutput;
    }

    public OutBoundGanttChartLinkInput FullSandBoxRefluxGoWaitPositionEventCreate(OutBoundGanttChartLinkInput simulationOutput, Coordinate layWaitCoordinate) {
        OutBoundSimulationInput input = simulationOutput.getInput();
        //发生满载子车前往轨道等待的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getLocationCoordinate().getX() -
                layWaitCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getFullSpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getSandBoxRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = allTool.createEvent("100910", "FLSBRSCGWP", executionTime, input.getTotalTime() - executionTime,
                input.getTotalTime(), input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getId(), startSubCarStatus,
                false, null, endSubCarStatus, true, layWaitCoordinate, null,
                null, false, null, null, false, null,
                null, null, null, null, null, null,
                null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getId(), SUB_CAR, RUNNING, layWaitCoordinate,
                input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).setLocationCoordinate(layWaitCoordinate);

        simulationOutput.setInput(input);

        return simulationOutput;
    }

    public OutBoundGanttChartLinkInput FullSandBoxRefluxGoColdPositionEventCreate(OutBoundGanttChartLinkInput simulationOutput) {
        OutBoundSimulationInput input = simulationOutput.getInput();
        //发生满载子车前往点位卸载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getLocationCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).getCoordinate().getX())
                / input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getFullSpeed();
        double executionTime_2 = input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getSubCarState());
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
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getSandBoxRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event_1 = allTool.createEvent("100911", "FLSBRSCGCP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2,
                input.getTotalTime() - executionTime_2, input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getId(),
                startSubCarStatus, false, null, startSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).getId(), startPositionStatus, startPositionStatus, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getId(), SUB_CAR, RUNNING,
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).getCoordinate(),
                input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100912", "FLSBRSCLIHP", executionTime_2, input.getTotalTime() - executionTime_2,
                input.getTotalTime(), input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getId(), startSubCarStatus,
                true, input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).getCoordinate(),
                endSubCarStatus, true, input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).getCoordinate(),
                null, null, false, null, null, false, null,
                null, null, null, input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).getId(),
                startPositionStatus, endPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarInGanttIndex()).getEvents().add(event_2);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).setLocationCoordinate(input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).getCoordinate());
        input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).setStatus(OCCUPIED);
        Product product = input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getProduct().productDeepCopy();
        input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).setProduct(product);
        input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).setProduct(null);

        simulationOutput.setInput(input);

        return simulationOutput;
    }

    public void emptySubCarGoHotMoldingPositionLoad(OutBoundGanttChartLinkInput simulationOutput) {
        OutBoundSimulationInput input = simulationOutput.getInput();
        //发生子车前往热砂砂箱合模放置点位搬运要合模的砂箱事件，并且搬运后子车向前移动到等待点，防止后续发生车辆堵塞
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getSandBoxMoldingSubCar().getLocationCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate().getX())
                / input.getSandBoxMoldingSubCar().getEmptySpeed();
        double executionTime_2 = input.getSandBoxMoldingSubCar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getSandBoxMoldingSubCar().getLocationCoordinate().getX() -
                input.getNewPositionMap().get(PositionNameEnum.HOT_SAND_MOLD_CLOSING_WORKSTATION_ACQUISITION_POSITION_5).getCoordinate().getX())
                / input.getSandBoxMoldingSubCar().getFullSpeed();

                //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSandBoxMoldingSubCar().getSubCarState());
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
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getSandBoxRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100915", "FLSBRSCGCP", executionTime_1, input.getTotalTime() -
                        executionTime_1 - executionTime_2 - executionTime_3, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getSandBoxMoldingSubCar().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate(),
                null, null, false, null, null, false,
                null, null, null, null,
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getId(), startPositionStatus, startPositionStatus,
                null, null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getSandBoxMoldingSubCar().getId(),
                SUB_CAR, RUNNING, input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getHotSandMoldingSubcarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100916", "FLSBRSCLIHP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_3, input.getSandBoxMoldingSubCar().getId(), startSubCarStatus,
                true, input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate(),
                endSubCarStatus, true, input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate(),
                null, null, false, null, null, false, null,
                null, null, null, input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getId(),
                startPositionStatus, endPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSandBoxMoldingSubCar().getId(), SUB_CAR, RISING, null, product);

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getHotSandMoldingSubcarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100917", "FLSBRSCLIHP", executionTime_2, input.getTotalTime() - executionTime_3,
                input.getTotalTime(), input.getSandBoxMoldingSubCar().getId(), endSubCarStatus, false, null,
                endSubCarStatus, true, input.getNewPositionMap().get(PositionNameEnum.HOT_SAND_MOLD_CLOSING_WORKSTATION_ACQUISITION_POSITION_5).getCoordinate(),
                null, null, false, null, null, false, null,
                null, null, null, null, null, null, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getSandBoxMoldingSubCar().getId(),
                SUB_CAR, RUNNING, input.getNewPositionMap().get(PositionNameEnum.HOT_SAND_MOLD_CLOSING_WORKSTATION_ACQUISITION_POSITION_5).getCoordinate(), product);

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getHotSandMoldingSubcarInGanttIndex()).getEvents().add(event_3);

        //改变车辆状态和时间
        input.getSandBoxMoldingSubCar().setSubCarState(endSubCarStatus);
        input.getSandBoxMoldingSubCar().setLocationCoordinate(input.getNewPositionMap().get(PositionNameEnum.HOT_SAND_MOLD_CLOSING_WORKSTATION_ACQUISITION_POSITION_5).getCoordinate());
        input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).setStatus(UNOCCUPIED);
        input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).setProduct(null);
        input.getSandBoxMoldingSubCar().setProduct(product);

        simulationOutput.setInput(input);
    }

    public void emptySubCarGoSandBoxRefluxLayPositionLoad(OutBoundGanttChartLinkInput simulationOutput) {
        OutBoundSimulationInput input = simulationOutput.getInput();
        //发生子车前往砂箱回流点位搬运回流砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getSandBoxMoldingSubCar().getLocationCoordinate().getX() -
                input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).getCoordinate().getX())
                / input.getSandBoxMoldingSubCar().getEmptySpeed();
        double executionTime_2 = input.getSandBoxMoldingSubCar().getTopRodRaiseOrFallTime();

        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSandBoxMoldingSubCar().getSubCarState());
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
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getSandBoxRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100918", "FLSBRSCGCP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2,
                input.getTotalTime() - executionTime_2, input.getSandBoxMoldingSubCar().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).getCoordinate(),
                null, null, false, null, null, false,
                null, null, null, null,
                input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).getId(), startPositionStatus, startPositionStatus,
                null, null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getSandBoxMoldingSubCar().getId(),
                SUB_CAR, RUNNING, input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).getCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getHotSandMoldingSubcarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100919", "FLSBRSCLIHP", executionTime_2, input.getTotalTime() - executionTime_2,
                input.getTotalTime(), input.getSandBoxMoldingSubCar().getId(), startSubCarStatus, true,
                input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).getCoordinate(),
                endSubCarStatus, true, input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).getCoordinate(),
                null, null, false, null, null, false, null,
                null, null, null, input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).getId(),
                startPositionStatus, endPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSandBoxMoldingSubCar().getId(), SUB_CAR, RISING, null, product);

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getHotSandMoldingSubcarInGanttIndex()).getEvents().add(event_2);

        //改变车辆状态和时间
        input.getSandBoxMoldingSubCar().setSubCarState(endSubCarStatus);
        input.getSandBoxMoldingSubCar().setLocationCoordinate(input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).getCoordinate());
        input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).setStatus(UNOCCUPIED);
        input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).setProduct(null);
        input.getSandBoxMoldingSubCar().setProduct(product);

        simulationOutput.setInput(input);
    }

    public void emptySubCarGoMidPositionWait(OutBoundGanttChartLinkInput simulationOutput, Coordinate midCoordinate) {
        OutBoundSimulationInput input = simulationOutput.getInput();
        //发生子车空载前往中点等待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getSandBoxMoldingSubCar().getLocationCoordinate().getX() - midCoordinate.getX())
                / input.getSandBoxMoldingSubCar().getEmptySpeed();

        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSandBoxMoldingSubCar().getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);

        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getSandBoxRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        //生成事件
        Event event_1 = allTool.createEvent("100920", "FLSBRSCGCP", executionTime_1, input.getTotalTime() - executionTime_1,
                input.getTotalTime(), input.getSandBoxMoldingSubCar().getId(), startSubCarStatus, false, null,
                endSubCarStatus, true, midCoordinate, null, null, false, null,
                null, false, null, null, null, null,
                null, null, null, null, null, null, pathIds,
                pathStatusEnums, pathStatusEnums, input.getSandBoxMoldingSubCar().getId(), SUB_CAR, RUNNING, midCoordinate, null);

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getHotSandMoldingSubcarInGanttIndex()).getEvents().add(event_1);

        //改变车辆状态和时间
        input.getSandBoxMoldingSubCar().setSubCarState(endSubCarStatus);
        input.getSandBoxMoldingSubCar().setLocationCoordinate(midCoordinate);

        simulationOutput.setInput(input);
    }

    public void fullSubCarGoHotMoldingPositionLand(OutBoundGanttChartLinkInput simulationOutput, PositionNameEnum goPositionEnum) {
        OutBoundSimulationInput input = simulationOutput.getInput();
        //发生子车满载前往相应热砂砂箱合模位置的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getSandBoxMoldingSubCar().getLocationCoordinate().getX() -
                input.getNewPositionMap().get(goPositionEnum).getCoordinate().getX()) /
                input.getSandBoxMoldingSubCar().getFullSpeed();
        double executionTime_2 = input.getSandBoxMoldingSubCar().getTopRodRaiseOrFallTime();

        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSandBoxMoldingSubCar().getSubCarState());
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
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getSandBoxRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getSandBoxMoldingSubCar().getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100921", "FLSBRSCGCP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2,
                input.getTotalTime() - executionTime_2, input.getSandBoxMoldingSubCar().getId(), startSubCarStatus, false,
                null, startSubCarStatus, true, input.getNewPositionMap().get(goPositionEnum).getCoordinate(),
                null, null, false, null, null, false,
                null, null, null, null,
                input.getNewPositionMap().get(goPositionEnum).getId(), startPositionStatus, startPositionStatus, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getSandBoxMoldingSubCar().getId(),
                SUB_CAR, RUNNING, input.getNewPositionMap().get(goPositionEnum).getCoordinate(), product);

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getHotSandMoldingSubcarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100922", "FLSBRSCLIHP", executionTime_2, input.getTotalTime() - executionTime_2,
                input.getTotalTime(), input.getSandBoxMoldingSubCar().getId(), startSubCarStatus, true,
                input.getNewPositionMap().get(goPositionEnum).getCoordinate(), endSubCarStatus, true,
                input.getNewPositionMap().get(goPositionEnum).getCoordinate(), null, null, false,
                null, null, false, null, null,
                null, null, input.getNewPositionMap().get(goPositionEnum).getId(), startPositionStatus,
                endPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSandBoxMoldingSubCar().getId(), SUB_CAR, LANDING, null, product);

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getHotSandMoldingSubcarInGanttIndex()).getEvents().add(event_2);

        //改变车辆状态和时间
        input.getSandBoxMoldingSubCar().setSubCarState(endSubCarStatus);
        input.getSandBoxMoldingSubCar().setLocationCoordinate(input.getNewPositionMap().get(goPositionEnum).getCoordinate());
        input.getNewPositionMap().get(goPositionEnum).setStatus(OCCUPIED);
        input.getNewPositionMap().get(goPositionEnum).setProduct(product);
        input.getSandBoxMoldingSubCar().setProduct(null);

        simulationOutput.setInput(input);
    }

    public void fullSubCarGoHotSandRefluxPositionLand(OutBoundGanttChartLinkInput simulationOutput) {
        OutBoundSimulationInput input = simulationOutput.getInput();
        //发生子车前往砂箱回流点位放置砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getSandBoxMoldingSubCar().getLocationCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate().getX()) /
                input.getSandBoxMoldingSubCar().getFullSpeed();
        double executionTime_2 = input.getSandBoxMoldingSubCar().getTopRodRaiseOrFallTime();

        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSandBoxMoldingSubCar().getSubCarState());
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
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getSandBoxRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getSandBoxMoldingSubCar().getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100923", "FLSBRSCGCP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2,
                input.getTotalTime() - executionTime_2, input.getSandBoxMoldingSubCar().getId(), startSubCarStatus, 
                false, null, startSubCarStatus, true, 
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate(),
                null, null, false, null, null, 
                false, null, null, null, null,
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getId(), startPositionStatus, 
                startPositionStatus, null, null, null, pathIds, 
                pathStatusEnums, pathStatusEnums, input.getSandBoxMoldingSubCar().getId(), SUB_CAR, RUNNING, 
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate(), product);

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getHotSandMoldingSubcarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100924", "FLSBRSCLIHP", executionTime_2, input.getTotalTime() - executionTime_2,
                input.getTotalTime(), input.getSandBoxMoldingSubCar().getId(), startSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate(), endSubCarStatus, 
                true, input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate(), 
                null, null, false, null, null, false, 
                null, null, null, null, 
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getId(), startPositionStatus,
                endPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSandBoxMoldingSubCar().getId(), SUB_CAR, LANDING, null, product);

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getHotSandMoldingSubcarInGanttIndex()).getEvents().add(event_2);

        //改变车辆状态和时间
        input.getSandBoxMoldingSubCar().setSubCarState(endSubCarStatus);
        input.getSandBoxMoldingSubCar().setLocationCoordinate(input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate());
        input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).setStatus(OCCUPIED);
        input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).setProduct(product);
        input.getSandBoxMoldingSubCar().setProduct(null);

        simulationOutput.setInput(input);
    }

    public void fullSubCarGoHotSandWaitRefluxPosition(OutBoundGanttChartLinkInput simulationOutput) {
        OutBoundSimulationInput input = simulationOutput.getInput();
        //发生子车前往回流砂箱放置等待点位的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getSandBoxMoldingSubCar().getLocationCoordinate().getX() -
                input.getNewPositionMap().get(PositionNameEnum.HOT_SAND_MOLD_CLOSING_WORKSTATION_ACQUISITION_POSITION_5).getCoordinate().getX()) /
                input.getSandBoxMoldingSubCar().getFullSpeed();

        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSandBoxMoldingSubCar().getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);

        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getSandBoxRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getSandBoxMoldingSubCar().getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100925", "FLSBRSCGCP", executionTime_1, input.getTotalTime() - executionTime_1,
                input.getTotalTime(), input.getSandBoxMoldingSubCar().getId(), startSubCarStatus, false, null,
                endSubCarStatus, true, input.getNewPositionMap().get(PositionNameEnum.HOT_SAND_MOLD_CLOSING_WORKSTATION_ACQUISITION_POSITION_5).getCoordinate(),
                null, null, false, null, null, false,
                null, null, null, null, null, null,
                null, null, null, null, pathIds, pathStatusEnums,
                pathStatusEnums, input.getSandBoxMoldingSubCar().getId(), SUB_CAR, RUNNING,
                input.getNewPositionMap().get(PositionNameEnum.HOT_SAND_MOLD_CLOSING_WORKSTATION_ACQUISITION_POSITION_5).getCoordinate(), product);

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getHotSandMoldingSubcarInGanttIndex()).getEvents().add(event_1);

        //改变车辆状态和时间
        input.getSandBoxMoldingSubCar().setSubCarState(endSubCarStatus);
        input.getSandBoxMoldingSubCar().setLocationCoordinate(input.getNewPositionMap().get(PositionNameEnum.HOT_SAND_MOLD_CLOSING_WORKSTATION_ACQUISITION_POSITION_5).getCoordinate());

        simulationOutput.setInput(input);
    }

    public void fullSubCarGoColdSandRefluxPositionLand(OutBoundGanttChartLinkInput simulationOutput) {
        OutBoundSimulationInput input = simulationOutput.getInput();
        //发生前往冷砂回流轨道的行车交互点放置冷砂回流砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getSandBoxMoldingSubCar().getLocationCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).getCoordinate().getX())
                / input.getSandBoxMoldingSubCar().getFullSpeed();
        double executionTime_2 = input.getSandBoxMoldingSubCar().getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSandBoxMoldingSubCar().getSubCarState());
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
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getSandBoxRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getSandBoxMoldingSubCar().getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100926", "FLSBRSCGCP", executionTime_1, input.getTotalTime() -
                        executionTime_1 - executionTime_2, input.getTotalTime() - executionTime_2, input.getSandBoxMoldingSubCar().getId(),
                startSubCarStatus, false, null, startSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).getCoordinate(), null, null,
                false, null, null, false, null, null,
                null, null, input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).getId(),
                startPositionStatus, startPositionStatus, null, null, null, pathIds, pathStatusEnums,
                pathStatusEnums, input.getSandBoxMoldingSubCar().getId(), SUB_CAR, RUNNING,
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).getCoordinate(), product);

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getHotSandMoldingSubcarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100927", "FLSBRSCLIHP", executionTime_2, input.getTotalTime() - executionTime_2,
                input.getTotalTime(), input.getSandBoxMoldingSubCar().getId(), startSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).getCoordinate(),
                endSubCarStatus, true, input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).getCoordinate(),
                null, null, false, null, null, false, null,
                null, null, null, input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).getId(),
                startPositionStatus, endPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSandBoxMoldingSubCar().getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getHotSandMoldingSubcarInGanttIndex()).getEvents().add(event_2);

        //改变车辆状态和时间
        input.getSandBoxMoldingSubCar().setSubCarState(endSubCarStatus);
        input.getSandBoxMoldingSubCar().setLocationCoordinate(input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).getCoordinate());
        input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).setStatus(OCCUPIED);
        input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).setProduct(product);
        input.getSandBoxMoldingSubCar().setProduct(null);

        simulationOutput.setInput(input);
    }
}
