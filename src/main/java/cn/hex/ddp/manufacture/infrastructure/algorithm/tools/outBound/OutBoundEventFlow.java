package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductAfootEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.OutBoundSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.Stereoscopic;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.moldClosing.MoldClosingGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.OCCUPIED;
import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.UNOCCUPIED;
import static cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum.PATH_UNOCCUPIED;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum.FERRY;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum.SUB_CAR;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum.CARRY_SUB_CAR;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum.NOT_CARRY_SUB_CAR;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum.*;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum.*;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/12/28 20:15
 */
public class OutBoundEventFlow {
    /**
     * 仿真总工具类
     */
    private final SimulationTool tools = new SimulationTool();
    /**
     * 出库区仿真的工具类
     */
    private OutBoundSimulationTools outBoundtools = new OutBoundSimulationTools();
    /**
     * 出库区模拟的立体库
     */
    private Stereoscopic stereoscopic = new Stereoscopic();

    public void EmptySubCarGoHotSandRefluxPosition(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生子车前往回流点位的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate().getX()) /
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();
        //子母车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(NOT_CARRY_SUB_CAR);
        //点位状态列表
        //因为事件发生前后点位状态一致，所以只用一个表达
        List<PositionStatusEnum> positionStatus = new ArrayList<>();
        positionStatus.add(OCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getSandBoxRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = tools.createEvent("100012", "ELOBSCGSBRP", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus,
                true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate(), input.getOutboundFerry().getId(), startFerryStatus,
                true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null,
                null, null, input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getId(), positionStatus, positionStatus, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(),
                SUB_CAR, RUNNING, input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate(), null);
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate().coordinateDeepCopy());
        input.getOutboundFerry().setFerryState(endFerryStatus);
    }

    public void EmptyFerryGoHotSandRefluxPosition(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生母车空载前往回流点位的事件
        //计算发生事件的时间
        double executionTime = Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate().getY() -
                input.getOutboundFerry().getLocationCoordinate().getY()) / input.getOutboundFerry().getEmptySpeed();
        //子车，母车，点位结束的状态不改变，只改变位置，不涉及，设备，工岗
        Coordinate aimCoordinate = new Coordinate(input.getOutboundFerry().getLocationCoordinate().getX(), input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //点位状态
        List<PositionStatusEnum> positionStatus = new ArrayList<>();
        positionStatus.add(OCCUPIED);
        //轨道状态前后不改变，写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = tools.createEvent("100013", "ELOBFGSBRP", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, aimCoordinate, input.getOutboundFerry().getId(), startFerryStatus, false, null,
                startFerryStatus, true, aimCoordinate, null, null, null,
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getId(), positionStatus, positionStatus, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getOutboundFerry().getId(), FERRY, RUNNING, aimCoordinate, null);
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(aimCoordinate);
        input.getOutboundFerry().setLocationCoordinate(aimCoordinate);
    }

    public void EmptySubCarGoMoldRefluxPosition(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生子车前往模具回流点位的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getCoordinate().getX()) /
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(NOT_CARRY_SUB_CAR);
        //点位状态列表
        //因为事件发生前后点位状态一致，所以只用一个表达
        List<PositionStatusEnum> positionStatus = new ArrayList<>();
        positionStatus.add(OCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getMoldRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        //生成事件
        Event event = tools.createEvent("100014", "ELOBSCGMRP", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getCoordinate(), input.getOutboundFerry().getId(), startFerryStatus,
                true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null,
                null, null, input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getId(), positionStatus, positionStatus, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(),
                SUB_CAR, RUNNING, input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getCoordinate(), null);
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getCoordinate().coordinateDeepCopy());
        input.getOutboundFerry().setFerryState(endFerryStatus);
    }

    public void EmptyFerryGoMoldRefluxPosition(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生母车空载前往模具回流点位的事件
        //计算发生事件的时间
        double executionTime = Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getCoordinate().getY() -
                input.getOutboundFerry().getLocationCoordinate().getY()) / input.getOutboundFerry().getEmptySpeed();
        //子车，母车，点位结束的状态不改变，只改变位置，不涉及，设备，工岗
        Coordinate aimCoordinate = new Coordinate(input.getOutboundFerry().getLocationCoordinate().getX(), input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //点位状态
        List<PositionStatusEnum> positionStatus = new ArrayList<>();
        positionStatus.add(OCCUPIED);
        //轨道状态前后不改变，写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = tools.createEvent("100015", "ELOBFGMRP", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, false, null, startSubCarStatus, true,
                aimCoordinate, input.getOutboundFerry().getId(), startFerryStatus, false, null, startFerryStatus,
                true, aimCoordinate, null, null, null, input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getId(),
                positionStatus, positionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getOutboundFerry().getId(), FERRY, RUNNING, aimCoordinate, null);
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(aimCoordinate);
        input.getOutboundFerry().setLocationCoordinate(aimCoordinate);
    }

    public void EmptySubCarGoColdSandRefluxPosition(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生子车前往冷砂回流点位的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate().getX()) /
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(NOT_CARRY_SUB_CAR);
        //点位状态列表
        //因为事件发生前后点位状态一致，所以只用一个表达
        List<PositionStatusEnum> positionStatus = new ArrayList<>();
        positionStatus.add(OCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getColdSandBoxRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        //生成事件
        Event event = tools.createEvent("100016", "ELOBSCGCSBRP", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate(), input.getOutboundFerry().getId(), startFerryStatus,
                true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null,
                null, null, input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getId(), positionStatus, positionStatus, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(),
                SUB_CAR, RUNNING, input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate(), null);
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate().coordinateDeepCopy());
        input.getOutboundFerry().setFerryState(endFerryStatus);
    }

    public void EmptyFerryGoColdSandRefluxPosition(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生母车空载前往冷砂回流点位的事件
        //计算发生事件的时间
        double executionTime = Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate().getY() -
                input.getOutboundFerry().getLocationCoordinate().getY()) / input.getOutboundFerry().getEmptySpeed();
        //子车，母车，点位结束的状态不改变，只改变位置，不涉及，设备，工岗
        Coordinate aimCoordinate = new Coordinate(input.getOutboundFerry().getLocationCoordinate().getX(),
                input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //点位状态
        List<PositionStatusEnum> positionStatus = new ArrayList<>();
        positionStatus.add(OCCUPIED);
        //轨道状态前后不改变，写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = tools.createEvent("100017", "ELOBFGCSBRP", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, false,
                null, startSubCarStatus, true, aimCoordinate, input.getOutboundFerry().getId(), startFerryStatus,
                false, null, startFerryStatus, true, aimCoordinate, null, null,
                null, input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getId(), positionStatus, positionStatus,
                null, null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getOutboundFerry().getId(),
                FERRY, RUNNING, aimCoordinate, null);
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(aimCoordinate);
        input.getOutboundFerry().setLocationCoordinate(aimCoordinate);
    }

    public void EmptySubCarOutBoundInOutPosition_1(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生子车在出库点1进行出库的事件，由于不确定完全的出库流程，考虑让子车向出库点移动一段距离，然后进行出库，先暂时使用交互点和轨道之间的距离替代
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getCoordinate().getX()) /
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(NOT_CARRY_SUB_CAR);
        //目标坐标，考虑让子车向出库点移动一段距离，然后进行出库，先暂时使用交互点和轨道之间的距离替代
        Coordinate aimCoordinate = new Coordinate(Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getCoordinate().getX()) +
                input.getOutboundFerry().getLocationCoordinate().getX(), input.getOutboundFerry().getLocationCoordinate().getY());
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        //生成事件
        Event event = tools.createEvent("100018", "ELOBSCIOB_1", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, true,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endSubCarStatus, true, aimCoordinate, input.getOutboundFerry().getId(),
                startFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(),
                null, null,null, null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), SUB_CAR, RUNNING, aimCoordinate, null);
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(aimCoordinate);
        input.getOutboundFerry().setFerryState(endFerryStatus);
    }

    public void EmptyFerryOutBoundGoOutPosition_1(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生母车空载前往出库点1，上方的事件
        //计算发生事件的时间
        double executionTime = Math.abs(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getNodeCoordinate().getFirst().getY() -
                input.getOutboundFerry().getLocationCoordinate().getY()) / input.getOutboundFerry().getEmptySpeed();
        //子车，母车结束的状态不改变，只改变位置，不涉及，点位，设备，工岗
        Coordinate aimCoordinate = new Coordinate(input.getOutboundFerry().getLocationCoordinate().getX(),
                input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getNodeCoordinate().getFirst().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //轨道状态前后不改变，写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = tools.createEvent("100019", "ELOBFGOB_1", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, aimCoordinate, input.getOutboundFerry().getId(), startFerryStatus, false, null,
                startFerryStatus, true, aimCoordinate, null, null, null, null, null,
                null, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getOutboundFerry().getId(), FERRY, RUNNING, aimCoordinate, null);
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(aimCoordinate);
        input.getOutboundFerry().setLocationCoordinate(aimCoordinate);
    }

    public void EmptySubCarOutBoundInOutPosition_2(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生子车在出库点2进行出库的事件，由于不确定完全的出库流程，考虑让子车向出库点移动一段距离，然后进行出库，先暂时使用交互点和轨道之间的距离替代
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getCoordinate().getX()) /
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(NOT_CARRY_SUB_CAR);
        //目标坐标，考虑让子车向出库点移动一段距离，然后进行出库，先暂时使用交互点和轨道之间的距离替代
        Coordinate aimCoordinate = new Coordinate(Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getCoordinate().getX()) +
                input.getOutboundFerry().getLocationCoordinate().getX(), input.getOutboundFerry().getLocationCoordinate().getY());
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        //生成事件
        Event event = tools.createEvent("100020", "ELOBSCIOB_2", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(),
                endSubCarStatus, true, aimCoordinate, input.getOutboundFerry().getId(), startFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(),
                endFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null, null,null, null,
                null, null, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), SUB_CAR, RUNNING, aimCoordinate, null);
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(aimCoordinate);
        input.getOutboundFerry().setFerryState(endFerryStatus);
    }

    public void EmptyFerryOutBoundGoOutPosition_2(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生母车空载前往出库点2，下方的事件
        //计算发生事件的时间
        double executionTime = Math.abs(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getNodeCoordinate().get(1).getY() -
                input.getOutboundFerry().getLocationCoordinate().getY()) / input.getOutboundFerry().getEmptySpeed();
        //子车，母车结束的状态不改变，只改变位置，不涉及，点位，设备，工岗
        Coordinate aimCoordinate = new Coordinate(input.getOutboundFerry().getLocationCoordinate().getX(), input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getNodeCoordinate().get(1).getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //轨道状态前后不改变，写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = tools.createEvent("100021", "ELOBFGOB_2", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, aimCoordinate, input.getOutboundFerry().getId(), startFerryStatus, false, null, startFerryStatus,
                true, aimCoordinate, null, null, null, null, null, null,
                null, null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getOutboundFerry().getId(), FERRY, RUNNING, aimCoordinate, null);
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(aimCoordinate);
        input.getOutboundFerry().setLocationCoordinate(aimCoordinate);
    }

    public void EmptySubCarInHotSandRefluxPositionLoad(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生子车在回流砂箱轨道交互点进行装载产品的事件
        //计算事件发生事件的时间
        double executionTime = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getTopRodRaiseOrFallTime();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(NOT_CARRY_SUB_CAR);
        //目标坐标，回流砂箱的点位坐标或者子车坐标不改变
        Coordinate aimCoordinate = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate();
        //点位状态列表
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        startPositionStatus.add(OCCUPIED);
        endPositionStatus.add(UNOCCUPIED);

        //生成事件
        Event event = tools.createEvent("100022", "ELOBSCISBRPL", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus,
                true, aimCoordinate, endSubCarStatus, true, aimCoordinate, input.getOutboundFerry().getId(), startFerryStatus,
                true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(),
                null, null,null, input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getId(),
                startPositionStatus, endPositionStatus, null, null, null, null, null, null,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), SUB_CAR, RISING, null,
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getProduct().productDeepCopy());
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态，点位的状态，时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).setStatus(UNOCCUPIED);
        input.getOutboundFerry().setFerryState(endFerryStatus);

        //产品进行转移
        Product product = input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getProduct().productDeepCopy();
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setProduct(product);
        input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).setProduct(null);
    }

    public void EmptySubCarInMoldRefluxPositionLoad(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生子车在模具回流轨道交互点进行装载产品的事件
        //计算事件发生事件的时间
        double executionTime = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getTopRodRaiseOrFallTime();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(NOT_CARRY_SUB_CAR);
        //目标坐标，模具回流的点位坐标或者子车坐标不改变
        Coordinate aimCoordinate = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate();
        //点位状态列表
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        startPositionStatus.add(OCCUPIED);
        endPositionStatus.add(UNOCCUPIED);

        //生成事件
        Event event = tools.createEvent("100023", "ELOBSCIMRPL", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus,
                true, aimCoordinate, endSubCarStatus, true, aimCoordinate, input.getOutboundFerry().getId(), startFerryStatus,
                true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(),
                null, null,null, input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getId(),
                startPositionStatus, endPositionStatus, null, null, null, null, null, null,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), SUB_CAR, RISING, null,
                input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getProduct().productDeepCopy());
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态，点位的状态，时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).setStatus(UNOCCUPIED);
        input.getOutboundFerry().setFerryState(endFerryStatus);

        //产品进行转移
        Product product = input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getProduct().productDeepCopy();
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setProduct(product);
        input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).setProduct(null);
    }

    public void EmptySubCarInColdSandRefluxPositionLoad(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生子车在冷砂回流轨道交互点进行装载产品的事件
        //计算事件发生事件的时间
        double executionTime = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getTopRodRaiseOrFallTime();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(NOT_CARRY_SUB_CAR);
        //目标坐标，冷砂回流的点位坐标或者子车坐标不改变
        Coordinate aimCoordinate = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate();
        //点位状态列表
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        startPositionStatus.add(OCCUPIED);
        endPositionStatus.add(UNOCCUPIED);

        //生成事件
        Event event = tools.createEvent("100024", "ELOBSCICSBRPL", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, true, aimCoordinate, endSubCarStatus,
                true, aimCoordinate, input.getOutboundFerry().getId(), startFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(),
                endFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null, null,null,
                input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getId(), startPositionStatus, endPositionStatus, null,
                null, null, null, null, null,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), SUB_CAR, RISING, null,
                input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getProduct().productDeepCopy());
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态，点位的状态，时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).setStatus(UNOCCUPIED);
        input.getOutboundFerry().setFerryState(endFerryStatus);

        //产品进行转移
        Product product = input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getProduct().productDeepCopy();
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setProduct(product);
        input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).setProduct(null);
    }

    public void EmptySubCarInOutBoundPositionLoad_1(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生子车在出库点1进行出库产品的事件
        //计算事件发生事件的时间
        assert input.getProductDeque().peek() != null;
        double executionTime = input.getProductDeque().peek().getOutBoundTime();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(NOT_CARRY_SUB_CAR);
        //目标坐标，立体库不受控制，所以不关注车辆的位置，出库不涉及点位

        //改变车辆状态，装载的产品，时间，更新出库队列
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setProduct(stereoscopic.outBoundProduct(Objects.requireNonNull(input.getProductDeque().poll())));
        input.getOutboundFerry().setFerryState(endFerryStatus);

        //生成事件
        Event event = tools.createEvent("100025", "ELOBSCIOBL_1", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, false, null,
                endSubCarStatus, false, null, input.getOutboundFerry().getId(), startFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(),
                endFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null, null,null, null,
                null, null, null, null, null, null, null, null,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), SUB_CAR, RISING, null,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().productDeepCopy());
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);
    }

    public void EmptySubCarBackFerryInOutBoundPosition_1(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生子车在出库点1回流砂箱完成，返回母车的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate().getX()) /
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(CARRY_SUB_CAR);

        //生成事件
        Event event = tools.createEvent("100026", "ELOBSCIOBBF_1", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, false, null,
                endSubCarStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), input.getOutboundFerry().getId(), startFerryStatus, true,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null,
                null,null, null, null, null, null, null, null,
                null, null, null, input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), SUB_CAR, RUNNING,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null);
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy());
        input.getOutboundFerry().setFerryState(endFerryStatus);
    }

    public void EmptySubCarInOutBoundPositionLoad_2(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生子车在出库点1进行出库产品的事件
        //计算事件发生事件的时间
        assert input.getProductDeque().peek() != null;
        double executionTime_2 = input.getProductDeque().peek().getOutBoundTime();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_2 = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus_2 = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus_2 = new ArrayList<>();
        endSubCarStatus_2.add(FULL_LOAD);
        endSubCarStatus_2.add(TOP_ROD_RAISED);
        endSubCarStatus_2.add(SUB_CAR_NOT_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus_2 = new ArrayList<>();
        endFerryStatus_2.add(NOT_CARRY_SUB_CAR);
        //目标坐标，立体库不受控制，所以不关注车辆的位置，出库不涉及点位

        //改变车辆状态，装载的产品，时间，更新出库队列
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus_2);
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setProduct(stereoscopic.outBoundProduct(Objects.requireNonNull(input.getProductDeque().poll())));
        input.getOutboundFerry().setFerryState(endFerryStatus_2);

        //生成事件
        Event event_2 = tools.createEvent("100027", "ELOBSCIOBL_2", executionTime_2, input.getTotalTime(), executionTime_2 + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus_2, false, null,
                endSubCarStatus_2, false, null, input.getOutboundFerry().getId(), startFerryStatus_2, true,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_2, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null,
                null,null, null, null, null, null, null, null,
                null, null, null, input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), SUB_CAR, RISING, null,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().productDeepCopy());
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event_2);
    }

    public void EmptySubCarBackFerryInOutBoundPosition_2(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生子车在出库点1回流砂箱完成，返回母车的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate().getX()) /
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(CARRY_SUB_CAR);

        //生成事件
        Event event = tools.createEvent("100028", "ELOBSCIOBBF_2", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, false, null,
                endSubCarStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), input.getOutboundFerry().getId(), startFerryStatus, true,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(),
                null, null,null, null, null, null, null, null, null,
                null, null, null, input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), SUB_CAR, RUNNING,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null);
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy());
        input.getOutboundFerry().setFerryState(endFerryStatus);
    }

    public void EmptySubCarBackFerryInSandRefluxPosition(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //子车在热砂或冷砂合模交互点，放置砂箱后返回母车的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate().getX()) /
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(CARRY_SUB_CAR);

        //生成事件
        Event event = tools.createEvent("100029", "ELOBSCIHOCSBBF", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, false, null,
                endSubCarStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), input.getOutboundFerry().getId(), startFerryStatus, true,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null,
                null,null, null, null, null, null, null, null,
                null, null, null, input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), SUB_CAR, RUNNING,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null);
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy());
        input.getOutboundFerry().setFerryState(endFerryStatus);
    }

    public void EmptySubCarBackFerryInMoldRefluxPosition(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //子车在热砂或冷砂合模交互点，放置砂箱后返回母车的事件
        //计算事件发生事件的时间
        double executionTime_2 = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate().getX()) /
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_2 = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus_2 = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus_2 = new ArrayList<>();
        endSubCarStatus_2.add(EMPTY_LOAD);
        endSubCarStatus_2.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_2.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus_2 = new ArrayList<>();
        endFerryStatus_2.add(CARRY_SUB_CAR);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getCoreMakingPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        //生成事件
        Event event_2 = tools.createEvent("100030", "ELOBSCIHOCSBBF", executionTime_2, input.getTotalTime(), executionTime_2 + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus_2, false, null,
                endSubCarStatus_2, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), input.getOutboundFerry().getId(), startFerryStatus_2, true,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_2, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null,
                null,null, null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), SUB_CAR, RUNNING,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null);
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event_2);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus_2);
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy());
        input.getOutboundFerry().setFerryState(endFerryStatus_2);
    }

    public void FullSubCarGoHotMoldingPosition(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生子车前往热砂模具交互点位的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getCoordinate().getX()) /
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(NOT_CARRY_SUB_CAR);
        //点位状态列表
        //因为事件发生前后点位状态一致，所以只用一个表达
        List<PositionStatusEnum> positionStatus = new ArrayList<>();
        positionStatus.add(UNOCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = tools.createEvent("100031", "FLOBSCGHSMP", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, true,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getCoordinate(), input.getOutboundFerry().getId(),
                startFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus, true,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null, null, null,
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getId(), positionStatus, positionStatus,
                null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), SUB_CAR, RUNNING,
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getCoordinate(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().productDeepCopy());
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getCoordinate().coordinateDeepCopy());
        input.getOutboundFerry().setFerryState(endFerryStatus);
    }

    public void FullFerryGoHotMoldingPosition(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生母车满载前往热砂模具交互点的事件
        //计算发生事件的时间
        double executionTime = Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getCoordinate().getY() -
                input.getOutboundFerry().getLocationCoordinate().getY()) / input.getOutboundFerry().getEmptySpeed();
        //子车，母车，点位结束的状态不改变，只改变位置，不涉及，设备，工岗
        Coordinate aimCoordinate = new Coordinate(input.getOutboundFerry().getLocationCoordinate().getX(), input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //点位状态
        List<PositionStatusEnum> positionStatus = new ArrayList<>();
        positionStatus.add(UNOCCUPIED);
        positionStatus.add(OCCUPIED);
        //轨道状态前后不改变，写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = tools.createEvent("100032", "FLOBFGHSMP", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, false,
                null, startSubCarStatus, true, aimCoordinate, input.getOutboundFerry().getId(), startFerryStatus,
                false, null, startFerryStatus, true, aimCoordinate, null, null,
                null, input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getId(), positionStatus,
                positionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getOutboundFerry().getId(), FERRY, RUNNING, aimCoordinate,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().productDeepCopy());
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(aimCoordinate);
        input.getOutboundFerry().setLocationCoordinate(aimCoordinate);
    }

    public void FullSubCarGoColdMoldingPosition(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生子车前往冷砂模具交互点位的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldingPositionIndex()).getCoordinate().getX()) /
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(NOT_CARRY_SUB_CAR);
        //点位状态列表
        //因为事件发生前后点位状态一致，所以只用一个表达
        List<PositionStatusEnum> positionStatus = new ArrayList<>();
        positionStatus.add(UNOCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = tools.createEvent("100033", "FLOBSCGCSMP", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, true,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldingPositionIndex()).getCoordinate(), input.getOutboundFerry().getId(),
                startFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus, true,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null, null, null,
                input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldingPositionIndex()).getId(), positionStatus, positionStatus,
                null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), SUB_CAR, RUNNING,
                input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldingPositionIndex()).getCoordinate(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().productDeepCopy());
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldingPositionIndex()).getCoordinate().coordinateDeepCopy());
        input.getOutboundFerry().setFerryState(endFerryStatus);
    }

    public void FullFerryGoColdMoldingPosition(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生母车满载前往冷砂模具交互点的事件
        //计算发生事件的时间
        double executionTime = Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldingPositionIndex()).getCoordinate().getY() -
                input.getOutboundFerry().getLocationCoordinate().getY()) / input.getOutboundFerry().getEmptySpeed();
        //子车，母车，点位结束的状态不改变，只改变位置，不涉及，设备，工岗
        Coordinate aimCoordinate = new Coordinate(input.getOutboundFerry().getLocationCoordinate().getX(), input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldingPositionIndex()).getCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //点位状态
        List<PositionStatusEnum> positionStatus = new ArrayList<>();
        positionStatus.add(UNOCCUPIED);
        positionStatus.add(OCCUPIED);
        //轨道状态前后不改变，写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = tools.createEvent("100034", "FLOBFGCSMP", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, false,
                null, startSubCarStatus, true, aimCoordinate, input.getOutboundFerry().getId(), startFerryStatus,
                false, null, startFerryStatus, true, aimCoordinate, null, null,
                null, input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldingPositionIndex()).getId(), positionStatus,
                positionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getOutboundFerry().getId(), FERRY, RUNNING, aimCoordinate,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().productDeepCopy());
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(aimCoordinate);
        input.getOutboundFerry().setLocationCoordinate(aimCoordinate);
    }

    public void FullSubCarGoCoreMakingPosition(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生子车前往制芯点的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getCoordinate().getX()) /
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(NOT_CARRY_SUB_CAR);
        //点位状态列表
        //因为事件发生前后点位状态一致，所以只用一个表达
        List<PositionStatusEnum> positionStatus = new ArrayList<>();
        positionStatus.add(UNOCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getCoreMakingPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = tools.createEvent("100035", "FLOBSCGCMP", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, true,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getCoordinate(), input.getOutboundFerry().getId(),
                startFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus, true,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null, null, null,
                input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getId(), positionStatus, positionStatus, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(),
                SUB_CAR, RUNNING, input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getCoordinate(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().productDeepCopy());
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getCoordinate().coordinateDeepCopy());
        input.getOutboundFerry().setFerryState(endFerryStatus);
    }

    public void FullFerryGoCoreMakingPosition(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生母车满载前往冷砂模具交互点的事件
        //计算发生事件的时间
        double executionTime = Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getCoordinate().getY() -
                input.getOutboundFerry().getLocationCoordinate().getY()) / input.getOutboundFerry().getEmptySpeed();
        //子车，母车，点位结束的状态不改变，只改变位置，不涉及，设备，工岗
        Coordinate aimCoordinate = new Coordinate(input.getOutboundFerry().getLocationCoordinate().getX(), input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //点位状态
        List<PositionStatusEnum> positionStatus = new ArrayList<>();
        positionStatus.add(UNOCCUPIED);
        positionStatus.add(OCCUPIED);
        //轨道状态前后不改变，写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = tools.createEvent("100036", "FLOBFGCMP", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, false,
                null, startSubCarStatus, true, aimCoordinate, input.getOutboundFerry().getId(), startFerryStatus,
                false, null, startFerryStatus, true, aimCoordinate, null, null,
                null, input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getId(), positionStatus,
                positionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getOutboundFerry().getId(), FERRY, RUNNING, aimCoordinate,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().productDeepCopy());
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(aimCoordinate);
        input.getOutboundFerry().setLocationCoordinate(aimCoordinate);
    }

    public void FullSubCarGoOutBoundPositionLand(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生子车前往出库点的事件，同理进入出库点一段距离，思路同上空载情况
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getCoordinate().getX()) * 2 /
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(NOT_CARRY_SUB_CAR);
        //目标坐标，同空载相同
        Coordinate aimCoordinate = new Coordinate(Math.abs(input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getCoordinate().getX()) * 2 +
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy().getX(), input.getOutboundFerry().getLocationCoordinate().getY());

        //生成事件
        Event event = tools.createEvent("100037", "FLOBSCGIOBR", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, true,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endSubCarStatus, true, aimCoordinate,
                input.getOutboundFerry().getId(), startFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(),
                endFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null, null,
                null, null, null, null, null, null, null,
                null, null, null, input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(),
                SUB_CAR, RUNNING, aimCoordinate, input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().productDeepCopy());
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(aimCoordinate);
        input.getOutboundFerry().setFerryState(endFerryStatus);
    }

    public void FullFerryGoOutBoundPositionLand(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生母车满载前往出库点的事件，根据就近原则决定去哪个出库点
        int obIndex = outBoundtools.judgeGoWhichOutBound(input.getOutboundFerry(), input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()));
        //计算发生事件的时间
        double executionTime = Math.abs(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getNodeCoordinate().get(obIndex).getY() -
                input.getOutboundFerry().getLocationCoordinate().getY()) / input.getOutboundFerry().getEmptySpeed();
        //子车，母车，点位结束的状态不改变，只改变位置，不涉及，设备，工岗
        Coordinate aimCoordinate = new Coordinate(input.getOutboundFerry().getLocationCoordinate().getX(),
                input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getNodeCoordinate().get(obIndex).getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //轨道状态前后不改变，写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = tools.createEvent("100038", "FLOBFGOBP", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, false,
                null, startSubCarStatus, true, aimCoordinate, input.getOutboundFerry().getId(), startFerryStatus,
                false, null, startFerryStatus, true, aimCoordinate, null, null,
                null, null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getOutboundFerry().getId(), FERRY, RUNNING, aimCoordinate,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().productDeepCopy());
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(aimCoordinate);
        input.getOutboundFerry().setLocationCoordinate(aimCoordinate);
    }

    public void FullSubCarGoBackFerry(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //子车装载砂箱后返回母车的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate().getX()) /
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(CARRY_SUB_CAR);
        //轨道状态前后不改变，写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(outBoundtools.judgeSubCarGoBackFerryInWhichPath(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()),
                input.getPaths().get(input.getOutBoundIndexInput().getSandBoxRefluxPathIndex()), input.getPaths().get(input.getOutBoundIndexInput().getMoldRefluxPathIndex()),
                input.getPaths().get(input.getOutBoundIndexInput().getColdSandBoxRefluxPathIndex())));
        pathStatusEnums.add(PATH_UNOCCUPIED);

        //生成事件
        Event event = tools.createEvent("100039", "FLOBSCIHSBRBF", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, false,
                null, endSubCarStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(),
                input.getOutboundFerry().getId(), startFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(),
                endFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null, null,
                null, null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), SUB_CAR, RUNNING,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().productDeepCopy());
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus);
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy());
        input.getOutboundFerry().setFerryState(endFerryStatus);
    }

    public void FullSubCarInOutBoundPositionLand(OutBoundSimulationInput input, GanttChart outBoundGanttChart, Deque<Product> refluxProducts) {
        //发生子车在出库点1进行回流产品的事件
        //计算事件发生事件的时间
        double executionTime_1 = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().getOutBoundTime();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus_1 = new ArrayList<>();
        endSubCarStatus_1.add(EMPTY_LOAD);
        endSubCarStatus_1.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_1.add(SUB_CAR_NOT_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus_1 = new ArrayList<>();
        endFerryStatus_1.add(NOT_CARRY_SUB_CAR);
        //目标坐标，立体库不受控制，所以不关注车辆的位置，出库不涉及点位

        //生成事件
        Event event_1 = tools.createEvent("100040", "FLOBSCIOBUL", executionTime_1, input.getTotalTime(), executionTime_1 + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus_1, false,
                null, endSubCarStatus_1, false, null, input.getOutboundFerry().getId(),
                startFerryStatus_1, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_1,
                true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null, null,
                null, null, null, null, null, null,
                null, null, null, null,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), SUB_CAR, LANDING, null, null);
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event_1);

        //前期测试，更新回流队列，用于判断结束，后期会更改
        refluxProducts.add(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().productDeepCopy());

        //改变车辆状态，装载的产品，时间，更新出库队列
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus_1);
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setProduct(null);
        input.getOutboundFerry().setFerryState(endFerryStatus_1);
    }

    public void FullSubCarBackFerryInOutBoundPosition(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生子车在出库点1出库产品完成，返回母车的事件
        //计算事件发生事件的时间
        double executionTime_2 = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate().getX()) /
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_2 = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus_2 = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus_2 = new ArrayList<>();
        endSubCarStatus_2.add(FULL_LOAD);
        endSubCarStatus_2.add(TOP_ROD_RAISED);
        endSubCarStatus_2.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus_2 = new ArrayList<>();
        endFerryStatus_2.add(CARRY_SUB_CAR);

        //生成事件
        Event event_2 = tools.createEvent("100041", "FLOBSCIOBBF", executionTime_2, input.getTotalTime(), executionTime_2 + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus_2, false,
                null, endSubCarStatus_2, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(),
                input.getOutboundFerry().getId(), startFerryStatus_2, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(),
                endFerryStatus_2, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null, null,
                null, null, null, null, null, null, null,
                null, null, null, input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(),
                SUB_CAR, RUNNING, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().productDeepCopy());
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event_2);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus_2);
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy());
        input.getOutboundFerry().setFerryState(endFerryStatus_2);
    }

    public void FullSubCarLandInHotMoldingPosition(OutBoundSimulationInput input, GanttChart outBoundGanttChart, MoldClosingGanttChartLinkInput moldInput) {
        //发生子车在热砂合模交互点进行卸载产品的事件
        //计算事件发生事件的时间
        double executionTime_1 = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getTopRodRaiseOrFallTime();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus_1 = new ArrayList<>();
        endSubCarStatus_1.add(EMPTY_LOAD);
        endSubCarStatus_1.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_1.add(SUB_CAR_NOT_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus_1 = new ArrayList<>();
        endFerryStatus_1.add(NOT_CARRY_SUB_CAR);
        //目标坐标，冷砂回流的点位坐标或者子车坐标不改变
        Coordinate aimCoordinate = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate();
        //点位状态列表
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        startPositionStatus.add(UNOCCUPIED);
        endPositionStatus.add(OCCUPIED);

        //生成事件
        Event event_1 = tools.createEvent("100042", "FLOBSCIHSMPUL", executionTime_1, input.getTotalTime(), executionTime_1 + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus_1, true, aimCoordinate,
                endSubCarStatus_1, true, aimCoordinate, input.getOutboundFerry().getId(), startFerryStatus_1, true,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_1, true,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null, null,null,
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getId(), startPositionStatus, endPositionStatus,
                null, null, null, null, null, null,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), SUB_CAR, LANDING, null, null);
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event_1);

        //改变车辆状态，点位的状态，时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus_1);
        input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).setStatus(OCCUPIED);
        moldInput.getInput().getHotMoldClosingPosition().setStatus(OCCUPIED);
        input.getOutboundFerry().setFerryState(endFerryStatus_1);
        //产品进行转移
        Product product = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().productDeepCopy();


        //测试
        Product productTest = product.productDeepCopy();

        product.setProductAfoot(ProductAfootEnum.GOING_MOLD_CLOSING);
        productTest.setProductAfoot(ProductAfootEnum.GOING_MOLD_CLOSING);

        input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).setProduct(product);
        moldInput.getInput().getHotMoldClosingPosition().setProduct(productTest);
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setProduct(null);
    }

    public void FullSubCarLandInColdMoldingPosition(OutBoundSimulationInput input, GanttChart outBoundGanttChart, MoldClosingGanttChartLinkInput moldInput) {
        //发生子车在冷砂合模交互点进行卸载产品的事件
        //计算事件发生事件的时间
        double executionTime_1 = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getTopRodRaiseOrFallTime();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus_1 = new ArrayList<>();
        endSubCarStatus_1.add(EMPTY_LOAD);
        endSubCarStatus_1.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_1.add(SUB_CAR_NOT_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus_1 = new ArrayList<>();
        endFerryStatus_1.add(NOT_CARRY_SUB_CAR);
        //目标坐标，冷砂回流的点位坐标或者子车坐标不改变
        Coordinate aimCoordinate = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate();
        //点位状态列表
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        startPositionStatus.add(UNOCCUPIED);
        endPositionStatus.add(OCCUPIED);

        //生成事件
        Event event_1 = tools.createEvent("100043", "FLOBSCICSMPUL", executionTime_1, input.getTotalTime(), executionTime_1 + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus_1, true, aimCoordinate,
                endSubCarStatus_1, true, aimCoordinate, input.getOutboundFerry().getId(), startFerryStatus_1, true,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_1, true,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null, null,null,
                input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldingPositionIndex()).getId(), startPositionStatus, endPositionStatus,
                null, null, null, null, null, null,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), SUB_CAR, LANDING, null, null);
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event_1);

        //改变车辆状态，点位的状态，时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus_1);
        input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldingPositionIndex()).setStatus(OCCUPIED);
        moldInput.getInput().getColdMoldClosingPosition().setStatus(OCCUPIED);
        input.getOutboundFerry().setFerryState(endFerryStatus_1);

        //产品进行转移
        Product product = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().productDeepCopy();
        Product productTest = product.productDeepCopy();

        product.setProductAfoot(ProductAfootEnum.GOING_MOLD_CLOSING);
        productTest.setProductAfoot(ProductAfootEnum.GOING_MOLD_CLOSING);

        input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldingPositionIndex()).setProduct(product);
        moldInput.getInput().getColdMoldClosingPosition().setProduct(productTest);
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setProduct(null);
    }

    public void FullSubCarLandInCoreMakingPosition(OutBoundSimulationInput input, GanttChart outBoundGanttChart, MoldClosingGanttChartLinkInput moldInput) {
        //发生子车在制芯交互点进行卸载产品的事件
        //计算事件发生事件的时间
        double executionTime_1 = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getTopRodRaiseOrFallTime();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus_1 = new ArrayList<>();
        endSubCarStatus_1.add(EMPTY_LOAD);
        endSubCarStatus_1.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_1.add(SUB_CAR_NOT_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus_1 = new ArrayList<>();
        endFerryStatus_1.add(NOT_CARRY_SUB_CAR);
        //目标坐标，冷砂回流的点位坐标或者子车坐标不改变
        Coordinate aimCoordinate = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate();
        //点位状态列表
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        startPositionStatus.add(UNOCCUPIED);
        endPositionStatus.add(OCCUPIED);

        //生成事件
        Event event_1 = tools.createEvent("100044", "FLOBSCICMPUL", executionTime_1, input.getTotalTime(), executionTime_1 + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus_1, true, aimCoordinate,
                endSubCarStatus_1, true, aimCoordinate, input.getOutboundFerry().getId(), startFerryStatus_1, true,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_1, true,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null, null,null,
                input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getId(), startPositionStatus, endPositionStatus,
                null, null, null, null, null, null,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), SUB_CAR, LANDING, null, null);
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event_1);

        //改变车辆状态，点位的状态，时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus_1);
        input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).setStatus(OCCUPIED);
        input.getOutboundFerry().setFerryState(endFerryStatus_1);

        //产品进行转移
        Product product = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().productDeepCopy();

        product.setProductAfoot(ProductAfootEnum.GOING_SANDBLASTING);

        input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).setProduct(product);
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setProduct(null);
    }

    public void FullFerryGoHotSandMoldingPosition(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生母车满载前往热砂模具交互点的事件
        //计算发生事件的时间
        double executionTime = Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate().getY() -
                input.getOutboundFerry().getLocationCoordinate().getY()) / input.getOutboundFerry().getEmptySpeed();
        //子车，母车，点位结束的状态不改变，只改变位置，不涉及，设备，工岗
        Coordinate aimCoordinate = new Coordinate(input.getOutboundFerry().getLocationCoordinate().getX(), input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //点位状态
        List<PositionStatusEnum> positionStatus = new ArrayList<>();
        positionStatus.add(UNOCCUPIED);
        positionStatus.add(OCCUPIED);
        //轨道状态前后不改变，写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = tools.createEvent("100032", "FLOBFGHSMP", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, false,
                null, startSubCarStatus, true, aimCoordinate, input.getOutboundFerry().getId(), startFerryStatus,
                false, null, startFerryStatus, true, aimCoordinate, null, null,
                null, input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getId(), positionStatus,
                positionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getOutboundFerry().getId(), FERRY, RUNNING, aimCoordinate,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().productDeepCopy());
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(aimCoordinate);
        input.getOutboundFerry().setLocationCoordinate(aimCoordinate);
    }

    public void FullSubCarGoHotSandMoldingPositionLand(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生子车前往热砂模具交互点位的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate().getX()) /
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getFullSpeed();
        double executionTime_2 = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                        input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate().getX()) /
                        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus_1 = new ArrayList<>();
        endSubCarStatus_1.add(EMPTY_LOAD);
        endSubCarStatus_1.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_1.add(SUB_CAR_NOT_IN_FERRY);
        List<SubCarStatusEnum> endSubCarStatus_2 = new ArrayList<>();
        endSubCarStatus_2.add(EMPTY_LOAD);
        endSubCarStatus_2.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_2.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(CARRY_SUB_CAR);
        //点位状态列表
        //因为事件发生前后点位状态一致，所以只用一个表达
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        startPositionStatus.add(UNOCCUPIED);
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        endPositionStatus.add(OCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().productDeepCopy();
        //生成事件
        Event event_1 = tools.createEvent("100031", "FLOBSCGHSMP", executionTime_1, input.getTotalTime() - executionTime_1 -
                        executionTime_2 + executionTime_3, input.getTotalTime() + executionTime_2 + executionTime_3,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, true,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), startSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate(), input.getOutboundFerry().getId(),
                startFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), startFerryStatus, true,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null, null, null,
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getId(), startPositionStatus, startPositionStatus,
                null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), SUB_CAR, RUNNING,
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate(), product);
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = tools.createEvent("100061", "FLOBSCGHSMP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime() + executionTime_3, input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(),
                startSubCarStatus, true, input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate(),
                endSubCarStatus_1, true, input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate(),
                input.getOutboundFerry().getId(), startFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(),
                startFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null,
                null, null, input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getId(),
                startPositionStatus, endPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), SUB_CAR, LANDING, null, product);
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = tools.createEvent("100062", "FLOBSCGHSMP", executionTime_3, input.getTotalTime() - executionTime_3,
                input.getTotalTime(), input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(),
                endSubCarStatus_1, true, input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate(),
                endSubCarStatus_2, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(),
                input.getOutboundFerry().getId(), startFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(),
                endFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null,
                null, null, null, null, null, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), SUB_CAR, RUNNING,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null);
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event_3);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus_2);
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy());
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setProduct(null);
        input.getOutboundFerry().setFerryState(endFerryStatus);
        input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).setStatus(OCCUPIED);
        input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).setProduct(product);
    }

    public void FullFerryGoColdSandMoldingPosition(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生母车满载前往热砂模具交互点的事件
        //计算发生事件的时间
        double executionTime = Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate().getY() -
                input.getOutboundFerry().getLocationCoordinate().getY()) / input.getOutboundFerry().getEmptySpeed();
        //子车，母车，点位结束的状态不改变，只改变位置，不涉及，设备，工岗
        Coordinate aimCoordinate = new Coordinate(input.getOutboundFerry().getLocationCoordinate().getX(), input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //点位状态
        List<PositionStatusEnum> positionStatus = new ArrayList<>();
        positionStatus.add(UNOCCUPIED);
        positionStatus.add(OCCUPIED);
        //轨道状态前后不改变，写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = tools.createEvent("100032", "FLOBFGHSMP", executionTime, input.getTotalTime(), executionTime + input.getTotalTime(),
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, false,
                null, startSubCarStatus, true, aimCoordinate, input.getOutboundFerry().getId(), startFerryStatus,
                false, null, startFerryStatus, true, aimCoordinate, null, null,
                null, input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getId(), positionStatus,
                positionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getOutboundFerry().getId(), FERRY, RUNNING, aimCoordinate,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().productDeepCopy());
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(aimCoordinate);
        input.getOutboundFerry().setLocationCoordinate(aimCoordinate);
    }

    public void FullSubCarGoColdSandMoldingPositionLand(OutBoundSimulationInput input, GanttChart outBoundGanttChart) {
        //发生子车前往热砂模具交互点位的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate().getX()) /
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getFullSpeed();
        double executionTime_2 = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate().getX()) /
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getOutboundFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus_1 = new ArrayList<>();
        endSubCarStatus_1.add(EMPTY_LOAD);
        endSubCarStatus_1.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_1.add(SUB_CAR_NOT_IN_FERRY);
        List<SubCarStatusEnum> endSubCarStatus_2 = new ArrayList<>();
        endSubCarStatus_2.add(EMPTY_LOAD);
        endSubCarStatus_2.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_2.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(CARRY_SUB_CAR);
        //点位状态列表
        //因为事件发生前后点位状态一致，所以只用一个表达
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        startPositionStatus.add(UNOCCUPIED);
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        endPositionStatus.add(OCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().productDeepCopy();
        //生成事件
        Event event_1 = tools.createEvent("100031", "FLOBSCGHSMP", executionTime_1, input.getTotalTime() - executionTime_1 -
                        executionTime_2 + executionTime_3, input.getTotalTime() + executionTime_2 + executionTime_3,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), startSubCarStatus, true,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), startSubCarStatus, true,
                input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate(), input.getOutboundFerry().getId(),
                startFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), startFerryStatus, true,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null, null, null,
                input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getId(), startPositionStatus, startPositionStatus,
                null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), SUB_CAR, RUNNING,
                input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate(), product);
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = tools.createEvent("100063", "FLOBSCGHSMP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime() + executionTime_3, input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(),
                startSubCarStatus, true, input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate(),
                endSubCarStatus_1, true, input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate(),
                input.getOutboundFerry().getId(), startFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(),
                startFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null,
                null, null, input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getId(),
                startPositionStatus, endPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), SUB_CAR, LANDING, null, product);
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = tools.createEvent("100064", "FLOBSCGHSMP", executionTime_3, input.getTotalTime() - executionTime_3,
                input.getTotalTime(), input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(),
                endSubCarStatus_1, true, input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate(),
                endSubCarStatus_2, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(),
                input.getOutboundFerry().getId(), startFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(),
                endFerryStatus, true, input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null,
                null, null, null, null, null, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getId(), SUB_CAR, RUNNING,
                input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy(), null);
        //事件加入甘特图
        outBoundGanttChart.getEventLinkGantts().get(input.getOutBoundIndexInput().getOutBoundSubCarInGanttIndex()).getEvents().add(event_3);

        //改变车辆状态和时间
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setSubCarState(endSubCarStatus_2);
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setLocationCoordinate(input.getOutboundFerry().getLocationCoordinate().coordinateDeepCopy());
        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).setProduct(null);
        input.getOutboundFerry().setFerryState(endFerryStatus);
        input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).setStatus(OCCUPIED);
        input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).setProduct(product);
    }
}
