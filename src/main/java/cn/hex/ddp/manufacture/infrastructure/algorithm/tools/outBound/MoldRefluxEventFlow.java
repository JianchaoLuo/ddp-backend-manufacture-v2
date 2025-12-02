package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.OutBoundSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;

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
 * @Created: 2024/6/22 22:00
 */
public class MoldRefluxEventFlow {
    /**
     * 出库区仿真的工具类
     */
    private final OutBoundSimulationTools tools = new OutBoundSimulationTools();
    /**
     * 空载前往回流等待点时事件流生成
     * @param input
     * @param outBoundGanttChart
     * @return
     */
    public OutBoundGanttChartLinkInput EmptyGoRefluxWaitPositionEventCreate(OutBoundSimulationInput input, GanttChart outBoundGanttChart, Coordinate refluxWaitCoordinate){
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getLocationCoordinate().getX() -
                refluxWaitCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getEmptySpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getMoldRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = tools.createEvent("100051", "ELMRSCGRWP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getId(), startSubCarStatus, false, null,
                endSubCarStatus, true, refluxWaitCoordinate, null, null, false, null, null,
                false, null, null, null, null, null, null,
                null, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getId(), SUB_CAR, RUNNING, refluxWaitCoordinate, null);

        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getMoldRefluxSubcarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).setLocationCoordinate(refluxWaitCoordinate);

        return new OutBoundGanttChartLinkInput(outBoundGanttChart, input);
    }

    /**
     * 回流热砂模具事件生成
     * @param input
     * @param outBoundGanttChart
     * @return
     */
    public OutBoundGanttChartLinkInput EmptyLoadHotMoldEventCreate(OutBoundSimulationInput input, GanttChart outBoundGanttChart,  Coordinate refluxWaitCoordinate){
        //发生子车装载回流模具的事件
        //计算事件发生事件的时间
        double executionTime_1 = (Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate().getX() -
                refluxWaitCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getEmptySpeed());
        double executionTime_2 = input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getSubCarState());
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
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getMoldRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成前往事件
        Event event = tools.createEvent("100052", "ELMRSCLGHMRP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2,
                input.getTotalTime() - executionTime_2, input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getId(), startSubCarStatus,
                false, null, startSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate(), null, null,
                false, null, null, false, null, null,
                null, null, input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getId(),
                startPositionStatus, startPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getId(), SUB_CAR, RUNNING,
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate(), null);

        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getMoldRefluxSubcarInGanttIndex()).getEvents().add(event);

        //生成装载事件
        event = tools.createEvent("100053", "ELMRSCLIHMRP", executionTime_2, input.getTotalTime() - executionTime_2, input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getId(), startSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate(), endSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate(), null, null,
                false, null, null, false, null, null, null,
                null, input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getId(), startPositionStatus, endPositionStatus,
                null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getId(), SUB_CAR, RISING, null,
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getProduct().productDeepCopy());

        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getMoldRefluxSubcarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).setLocationCoordinate(input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate());
        input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).setStatus(UNOCCUPIED);
        Product product = input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getProduct();
        input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).setProduct(product);
        input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).setProduct(null);

        return new OutBoundGanttChartLinkInput(outBoundGanttChart, input);
    }

    /**
     * 回流冷砂模具事件生成
     * @param input
     * @param outBoundGanttChart
     * @return
     */
    public OutBoundGanttChartLinkInput EmptyLoadColdMoldEventCreate(OutBoundSimulationInput input, GanttChart outBoundGanttChart,  Coordinate refluxWaitCoordinate){
        //发生子车装载回流模具的事件
        //计算事件发生事件的时间
        double executionTime_1 = (Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldRefluxPositionIndex()).getCoordinate().getX() -
                refluxWaitCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getEmptySpeed());
        double executionTime_2 = input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getTopRodRaiseOrFallTime();
        double executionTime_3 = (Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldRefluxPositionIndex()).getCoordinate().getX() -
                refluxWaitCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getFullSpeed());
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getSubCarState());
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
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getMoldRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成前往事件
        Event event = tools.createEvent("100054", "ELMRSCLGCMRP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_2 - executionTime_3, input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getId(),
                startSubCarStatus, false, null, startSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldRefluxPositionIndex()).getCoordinate(), null, null,
                false, null, null, false, null, null,
                null, null, input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldRefluxPositionIndex()).getId(),
                startPositionStatus, startPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getId(), SUB_CAR, RUNNING,
                input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldRefluxPositionIndex()).getCoordinate(), null);

        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getMoldRefluxSubcarInGanttIndex()).getEvents().add(event);

        //生成装载事件
        event = tools.createEvent("100055", "ELMRSCLICMRP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_3, input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getId(), startSubCarStatus,
                true, input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldRefluxPositionIndex()).getCoordinate(), endSubCarStatus,
                true, input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldRefluxPositionIndex()).getCoordinate(), null, null,
                false, null, null, false, null, null, null,
                null, input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldRefluxPositionIndex()).getId(), startPositionStatus, endPositionStatus,
                null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getId(), SUB_CAR, RISING, null,
                input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldRefluxPositionIndex()).getProduct().productDeepCopy());

        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getMoldRefluxSubcarInGanttIndex()).getEvents().add(event);

        //生成前往事件
        event = tools.createEvent("100056", "ELMRSCLGWMRP", executionTime_3, input.getTotalTime() - executionTime_3, input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getId(), endSubCarStatus, false, null,
                endSubCarStatus, true, refluxWaitCoordinate, null, null, false, null, null,
                false, null, null, null, null,
                input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldRefluxPositionIndex()).getId(), endPositionStatus, endPositionStatus, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getId(), SUB_CAR, RUNNING, refluxWaitCoordinate,
                input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldRefluxPositionIndex()).getProduct().productDeepCopy());

        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getMoldRefluxSubcarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).setLocationCoordinate(refluxWaitCoordinate);
        input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldRefluxPositionIndex()).setStatus(UNOCCUPIED);
        Product product = input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldRefluxPositionIndex()).getProduct().productDeepCopy();
        input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).setProduct(product);
        input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldRefluxPositionIndex()).setProduct(null);

        return new OutBoundGanttChartLinkInput(outBoundGanttChart, input);
    }

    /**
     * 空载前往回流等待点时事件流生成
     * @param input
     * @param outBoundGanttChart
     * @return
     */
    public OutBoundGanttChartLinkInput FullGoRefluxWaitPositionEventCreate(OutBoundSimulationInput input, GanttChart outBoundGanttChart, Coordinate layWaitCoordinate){
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getLocationCoordinate().getX() -
                layWaitCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getFullSpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getMoldRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = tools.createEvent("100057", "FLMRSCGRWP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getId(), startSubCarStatus, false, null,
                endSubCarStatus, true, layWaitCoordinate, null, null, false, null, null,
                false, null, null, null, null, null, null,
                null, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getId(), SUB_CAR, RUNNING, layWaitCoordinate,
                input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getProduct().productDeepCopy());

        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getMoldRefluxSubcarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).setLocationCoordinate(layWaitCoordinate);

        return new OutBoundGanttChartLinkInput(outBoundGanttChart, input);
    }

    /**
     * 回流热砂模具事件生成
     * @param input
     * @param outBoundGanttChart
     * @return
     */
    public OutBoundGanttChartLinkInput FullLoadMoldEventCreate(OutBoundSimulationInput input, GanttChart outBoundGanttChart,  Coordinate layWaitCoordinate){
        //发生子车装载回流模具的事件
        //计算事件发生事件的时间
        double executionTime_1 = (Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate().getX() -
                layWaitCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getFullSpeed());
        double executionTime_2 = input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getSubCarState());
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
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getMoldRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成前往事件
        Event event = tools.createEvent("100052", "FLMRSCLGMRP", executionTime_1, input.getTotalTime() - executionTime_1, input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getCoordinate(), null,
                null, false, null, null, false, null, null,
                null, null, input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getId(), startPositionStatus,
                startPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getId(), SUB_CAR, RUNNING,
                input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getCoordinate(),
                input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getProduct().productDeepCopy());

        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getMoldRefluxSubcarInGanttIndex()).getEvents().add(event);

        //生成装载事件
        event = tools.createEvent("100053", "ELMRSCLIHMRP", executionTime_2, input.getTotalTime() - executionTime_2, input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getId(), startSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getCoordinate(), endSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getCoordinate(), null, null,
                false, null, null, false, null, null,
                null, null, input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getId(), startPositionStatus,
                endPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getMoldRefluxSubcarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).setLocationCoordinate(input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getCoordinate());
        input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).setStatus(OCCUPIED);
        Product product = input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getProduct();
        input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).setProduct(product);
        input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).setProduct(null);

        return new OutBoundGanttChartLinkInput(outBoundGanttChart, input);
    }
}
