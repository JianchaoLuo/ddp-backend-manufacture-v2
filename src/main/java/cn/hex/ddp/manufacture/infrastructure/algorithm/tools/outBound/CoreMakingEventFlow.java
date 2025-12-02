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
 * @Created: 2024/6/21 22:31
 */
public class CoreMakingEventFlow {
    /**
     * 出库区仿真的工具类
     */
    private final OutBoundSimulationTools tools = new OutBoundSimulationTools();

    /**
     * 装载事件生成
     */
    public OutBoundGanttChartLinkInput EmptyLoadEventCreate(OutBoundSimulationInput input, GanttChart outBoundGanttChart){
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getSubCarState());
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
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getCoreMakingPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = tools.createEvent("100045", "ELCMSCLICMP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getId(), startSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getCoordinate(), endSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getCoordinate(), null, null,
                false, null, null, false, null, null,
                null, null, input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getId(),
                startPositionStatus, endPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getId(), SUB_CAR, RISING, null,
                input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getProduct().productDeepCopy());

        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getCoreMakingSubcarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).setStatus(UNOCCUPIED);
        Product product = input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getProduct().productDeepCopy();
        input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).setProduct(product);
        input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).setProduct(null);

        return new OutBoundGanttChartLinkInput(outBoundGanttChart, input);
    }

    /**
     * 空载前往交互点时事件流生成
     */
    public OutBoundGanttChartLinkInput EmptyGoPositionEventCreate(OutBoundSimulationInput input, GanttChart outBoundGanttChart){
        //发生子车前往装载点的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getCoordinate().getX() -
                input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getLocationCoordinate().getX()) /
                input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getEmptySpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //点位状态列表
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        startPositionStatus.add(OCCUPIED);
        endPositionStatus.add(OCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getCoreMakingPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = tools.createEvent("100046", "ELCMSCGCMP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getId(), startSubCarStatus, false, null,
                endSubCarStatus, true, input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getCoordinate(), null,
                null, false, null, null, false, null, null,
                null, null, input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getId(), startPositionStatus,
                endPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getId(), SUB_CAR, RUNNING,
                input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getCoordinate(), null);

        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getCoreMakingSubcarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).setLocationCoordinate(input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getCoordinate().coordinateDeepCopy());

        return new OutBoundGanttChartLinkInput(outBoundGanttChart, input);
    }

    /**
     * 空载前往中点时事件流生成
     */
    public OutBoundGanttChartLinkInput EmptyGoMidPositionEventCreate(OutBoundSimulationInput input, GanttChart outBoundGanttChart, Coordinate midCoordinate){
        //发生子车前往轨道中点的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getLocationCoordinate().getX() -
                midCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getEmptySpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getCoreMakingPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = tools.createEvent("100047", "ELCMSCGMP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getId(), startSubCarStatus,
                false, null, endSubCarStatus, true, midCoordinate, null, null, false,
                null, null, false, null, null, null, null,
                null, null, null, null, null, null, pathIds, pathStatusEnums,
                pathStatusEnums, input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getId(), SUB_CAR, RUNNING, midCoordinate, null);

        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getCoreMakingSubcarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).setLocationCoordinate(midCoordinate);

        return new OutBoundGanttChartLinkInput(outBoundGanttChart, input);
    }

    /**
     * 满载前往中点时事件流生成
     */
    public OutBoundGanttChartLinkInput FullGoMidPositionEventCreate(OutBoundSimulationInput input, GanttChart outBoundGanttChart, Coordinate midCoordinate){
        //发生子车前往轨道中点的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getLocationCoordinate().getX() -
                midCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getFullSpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getCoreMakingPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = tools.createEvent("100048", "ELCMSCGMP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getId(), startSubCarStatus,
                false, null, endSubCarStatus, true, midCoordinate, null, null, false,
                null, null, false, null, null, null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getId(), SUB_CAR, RUNNING, midCoordinate,
                input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getProduct().productDeepCopy());

        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getCoreMakingSubcarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).setLocationCoordinate(midCoordinate);

        return new OutBoundGanttChartLinkInput(outBoundGanttChart, input);
    }

    /**
     * 满载前往交互点时事件流生成
     */
    public OutBoundGanttChartLinkInput FullGoLayPositionEventCreate(OutBoundSimulationInput input, GanttChart outBoundGanttChart){
        //发生子车前往卸载点的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingLayPositionIndex()).getCoordinate().getX() -
                input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getLocationCoordinate().getX()) /
                input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getFullSpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //点位状态列表
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        startPositionStatus.add(UNOCCUPIED);
        endPositionStatus.add(UNOCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getCoreMakingPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = tools.createEvent("100049", "FLCMSCGCMLP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getId(), startSubCarStatus, false, null,
                endSubCarStatus, true, input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingLayPositionIndex()).getCoordinate(), null,
                null, false, null, null, false, null, null,
                null, null, input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingLayPositionIndex()).getId(), startPositionStatus,
                endPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getId(), SUB_CAR, RUNNING,
                input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingLayPositionIndex()).getCoordinate(),
                input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getProduct().productDeepCopy());

        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getCoreMakingSubcarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).setLocationCoordinate(input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingLayPositionIndex()).getCoordinate().coordinateDeepCopy());

        return new OutBoundGanttChartLinkInput(outBoundGanttChart, input);
    }

    /**
     * 卸载事件生成
     */
    public OutBoundGanttChartLinkInput FullLoadEventCreate(OutBoundSimulationInput input, GanttChart outBoundGanttChart){
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getSubCarState());
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
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getCoreMakingPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = tools.createEvent("100050", "FLCMSCLICMP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getId(), startSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingLayPositionIndex()).getCoordinate(), endSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingLayPositionIndex()).getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingLayPositionIndex()).getId(), startPositionStatus, endPositionStatus, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getId(),
                SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getCoreMakingSubcarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingLayPositionIndex()).setStatus(OCCUPIED);
        Product product = input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getProduct().productDeepCopy();
        input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingLayPositionIndex()).setProduct(product);
        input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).setProduct(null);

        return new OutBoundGanttChartLinkInput(outBoundGanttChart, input);
    }
}
