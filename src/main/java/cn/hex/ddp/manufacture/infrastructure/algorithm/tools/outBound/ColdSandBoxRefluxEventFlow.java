package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound;

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
 * @Created: 2024/8/4 14:37
 */
public class ColdSandBoxRefluxEventFlow {
    /**
     * 仿真总工具类
     */
    private final SimulationTool allTool = new SimulationTool();

    public OutBoundGanttChartLinkInput EmptyColdSandBoxGoPositionEventCreate(OutBoundGanttChartLinkInput simulationOutput) {
        OutBoundSimulationInput input = simulationOutput.getInput();
        //发生空载子车前往点位装载待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getLocationCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getColdSandRowCarPositionIndex()).getCoordinate().getX())
                / input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getEmptySpeed();
        double executionTime_2 = input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getSubCarState());
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
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getColdSandBoxRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event_1 = allTool.createEvent("100801", "ELCSBRSCGP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2,
                input.getTotalTime() - executionTime_2, input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getId(),
                startSubCarStatus, false, null, startSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getColdSandRowCarPositionIndex()).getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getPositions().get(input.getOutBoundIndexInput().getColdSandRowCarPositionIndex()).getId(), startPositionStatus, startPositionStatus, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getId(),
                SUB_CAR, RUNNING, input.getPositions().get(input.getOutBoundIndexInput().getColdSandRowCarPositionIndex()).getCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getColdRefluxSubcarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100802", "ELCSBRSCLIP", executionTime_2, input.getTotalTime() - executionTime_2,
                input.getTotalTime(), input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getId(), startSubCarStatus,
                true, input.getPositions().get(input.getOutBoundIndexInput().getColdSandRowCarPositionIndex()).getCoordinate(), endSubCarStatus,
                true, input.getPositions().get(input.getOutBoundIndexInput().getColdSandRowCarPositionIndex()).getCoordinate(), null,
                null, false, null, null, false, null, null,
                null, null, input.getPositions().get(input.getOutBoundIndexInput().getColdSandRowCarPositionIndex()).getId(),
                startPositionStatus, endPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getId(), SUB_CAR, RISING, null,
                input.getPositions().get(input.getOutBoundIndexInput().getColdSandRowCarPositionIndex()).getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getColdRefluxSubcarInGanttIndex()).getEvents().add(event_2);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).setLocationCoordinate(
                input.getPositions().get(input.getOutBoundIndexInput().getColdSandRowCarPositionIndex()).getCoordinate());
        input.getPositions().get(input.getOutBoundIndexInput().getColdSandRowCarPositionIndex()).setStatus(UNOCCUPIED);
        Product product = input.getPositions().get(input.getOutBoundIndexInput().getColdSandRowCarPositionIndex()).getProduct().productDeepCopy();
        input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).setProduct(product);
        input.getPositions().get(input.getOutBoundIndexInput().getColdSandRowCarPositionIndex()).setProduct(null);

        simulationOutput.setInput(input);

        return simulationOutput;
    }

    public OutBoundGanttChartLinkInput EmptyColdSandBoxGoWaitPositionEventCreate(OutBoundGanttChartLinkInput simulationOutput, Coordinate midCoordinate) {
        OutBoundSimulationInput input = simulationOutput.getInput();
        //发生空载子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getLocationCoordinate().getX() -
                midCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getEmptySpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getColdSandBoxRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = allTool.createEvent("100803", "ELCSBRSCGWP", executionTime, input.getTotalTime() - executionTime,
                input.getTotalTime(), input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getId(), startSubCarStatus,
                false, null, endSubCarStatus, true, midCoordinate,
                null, null, false, null, null, false,
                null, null, null, null, null, null,
                null, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getId(), SUB_CAR, RUNNING, midCoordinate, null);

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getColdRefluxSubcarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).setLocationCoordinate(midCoordinate);

        simulationOutput.setInput(input);

        return simulationOutput;
    }

    public OutBoundGanttChartLinkInput FullColdSandBoxGoPositionEventCreate(OutBoundGanttChartLinkInput simulationOutput) {
        OutBoundSimulationInput input = simulationOutput.getInput();
        //发生满载子车前往点位卸载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getLocationCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate().getX())
                / input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getFullSpeed();
        double executionTime_2 = input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getSubCarState());
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
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getColdSandBoxRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event_1 = allTool.createEvent("100804", "FLCSBRSCGP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2,
                input.getTotalTime() - executionTime_2, input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getId(), startSubCarStatus,
                false, null, startSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getId(), startPositionStatus, startPositionStatus, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getId(), SUB_CAR, RUNNING,
                input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate(),
                input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getColdRefluxSubcarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100805", "FLCSBRSCLIP", executionTime_2, input.getTotalTime() - executionTime_2,
                input.getTotalTime(), input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getId(), startSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate(), endSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getId(), startPositionStatus, endPositionStatus, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getColdRefluxSubcarInGanttIndex()).getEvents().add(event_2);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).setLocationCoordinate(input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate());
        input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).setStatus(OCCUPIED);
        Product product = input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getProduct().productDeepCopy();
        input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).setProduct(product);
        input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).setProduct(null);

        simulationOutput.setInput(input);

        return simulationOutput;
    }

    public OutBoundGanttChartLinkInput FullColdSandBoxGoWaitPositionEventCreate(OutBoundGanttChartLinkInput simulationOutput, Coordinate midCoordinate) {
        OutBoundSimulationInput input = simulationOutput.getInput();
        //发生满载子车前往轨道等待的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getLocationCoordinate().getX() -
                midCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getFullSpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getColdSandBoxRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = allTool.createEvent("100806", "FLCSBRSCGWP", executionTime, input.getTotalTime() - executionTime,
                input.getTotalTime(), input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getId(), startSubCarStatus,
                false, null, endSubCarStatus, true, midCoordinate, null,
                null, false, null, null, false, null,
                null, null, null, null, null, null,
                null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getId(), SUB_CAR, RUNNING, midCoordinate,
                input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getColdRefluxSubcarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).setLocationCoordinate(midCoordinate);

        simulationOutput.setInput(input);

        return simulationOutput;
    }

    public void emptySubCarGoColdMoldingPositionLoad(OutBoundGanttChartLinkInput simulationOutput) {
        OutBoundSimulationInput input = simulationOutput.getInput();
        //发生子车前往热砂砂箱合模放置点位搬运要合模的砂箱事件，并且搬运后子车向前移动到等待点，防止后续发生车辆堵塞
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getLocationCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate().getX())
                / input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getEmptySpeed();
        double executionTime_2 = input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getTopRodRaiseOrFallTime();

        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getSubCarState());
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
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getColdSandBoxRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100815", "FLSBRSCGCP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2,
                input.getTotalTime() - executionTime_2, input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getId(),
                startSubCarStatus, false, null, startSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate(), null, null,
                false, null, null, false, null, null,
                null, null, input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getId(),
                startPositionStatus, startPositionStatus, null, null, null, pathIds, pathStatusEnums,
                pathStatusEnums, input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getId(), SUB_CAR, RUNNING,
                input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getColdRefluxSubcarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100816", "FLSBRSCLIHP", executionTime_2, input.getTotalTime() - executionTime_2,
                input.getTotalTime(), input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getId(), startSubCarStatus,
                true, input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate(),
                endSubCarStatus, true, input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate(),
                null, null, false, null, null, false, null,
                null, null, null, input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getId(),
                startPositionStatus, endPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getId(), SUB_CAR, RISING, null, product);

        //事件加入甘特图
        simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getColdRefluxSubcarInGanttIndex()).getEvents().add(event_2);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).setLocationCoordinate
                (input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate());
        input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).setStatus(UNOCCUPIED);
        input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).setProduct(null);
        input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).setProduct(product);

        simulationOutput.setInput(input);
    }
}
