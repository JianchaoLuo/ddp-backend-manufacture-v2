package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.moldClosing;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductAfootEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductFinishEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.MoldClosingSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.test.OutBoundANDMoldClosing;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationException;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound.OutBoundGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;

import java.util.ArrayList;
import java.util.List;

import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.OCCUPIED;
import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.UNOCCUPIED;
import static cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum.PATH_UNOCCUPIED;
import static cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum.*;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum.FERRY;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum.SUB_CAR;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum.CARRY_SUB_CAR;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum.NOT_CARRY_SUB_CAR;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum.*;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum.*;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/6/23 18:15
 */
public class HotMoldClosingEventFlow {
    /**
     * 合模区仿真
     */
    private MoldClosingSimulationTools tools = new MoldClosingSimulationTools();

    /**
     * 装载事件生成
     * @param input
     * @param moldClosingGanttChart
     * @return
     */
    public OutBoundANDMoldClosing HotMoleEmptyLoadEventCreate(MoldClosingSimulationInput input, OutBoundGanttChartLinkInput outBoundInput,
                                                              GanttChart moldClosingGanttChart, int finishedCount){
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getHotMoldClosingPosition().getCoordinate().getY() -
                input.getHotMoldClosingFerry().getLocationCoordinate().getY()) / input.getHotMoldClosingSubcar().getEmptySpeed();
        double executionTime_2 = input.getHotMoldClosingSubcar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getHotMoldClosingPosition().getCoordinate().getY() -
                input.getHotMoldClosingFerry().getLocationCoordinate().getY()) / input.getHotMoldClosingSubcar().getFullSpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getHotMoldClosingSubcar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getHotMoldClosingFerry().getFerryState());
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
        startPositionStatus.add(OCCUPIED);
        endPositionStatus.add(UNOCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getHotMoldClosingPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event_1 = tools.createEvent("100101", "ELMCSCGIHMP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_2 - executionTime_3, input.getHotMoldClosingSubcar().getId(), startSubCarStatus_1,true,
                input.getHotMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), endSubCarStatus_1, true, input.getHotMoldClosingPosition().getCoordinate(),
                input.getHotMoldClosingFerry().getId(), startFerryStatus_1,true, input.getHotMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(),
                endFerryStatus_1, true, input.getHotMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null,
                input.getHotMoldClosingPosition().getId(), startPositionStatus, startPositionStatus, null,null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotMoldClosingSubcar().getId(), SUB_CAR, RUNNING, input.getHotMoldClosingPosition().getCoordinate(), null);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getHotMoldClosingSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = tools.createEvent("100102", "ELMCSCIHMP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3, input.getTotalTime() - executionTime_3,
                input.getHotMoldClosingSubcar().getId(), endSubCarStatus_1,true, input.getHotMoldClosingPosition().getCoordinate(), endSubCarStatus_2, true,
                input.getHotMoldClosingPosition().getCoordinate(), input.getHotMoldClosingFerry().getId(), endFerryStatus_1,true, input.getHotMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(),
                endFerryStatus_1, true, input.getHotMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), null,null,
                null, input.getHotMoldClosingPosition().getId(), startPositionStatus, endPositionStatus, null,null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotMoldClosingSubcar().getId(), SUB_CAR, RISING, null, input.getHotMoldClosingPosition().getProduct().productDeepCopy());

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getHotMoldClosingSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = tools.createEvent("100103", "FLMCSCGBTF", executionTime_3, input.getTotalTime() - executionTime_3, input.getTotalTime(),
                input.getHotMoldClosingSubcar().getId(), endSubCarStatus_2,true, input.getHotMoldClosingPosition().getCoordinate(), endSubCarStatus_3, true,
                input.getHotMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), input.getHotMoldClosingFerry().getId(), endFerryStatus_1,true,
                input.getHotMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_2, true,
                input.getHotMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null, null,
                null, null, null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getHotMoldClosingSubcar().getId(), SUB_CAR, RUNNING, input.getHotMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), input.getHotMoldClosingPosition().getProduct().productDeepCopy());

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getHotMoldClosingSubCarInGanttIndex()).getEvents().add(event_3);

        //改变车辆状态和时间
        input.getHotMoldClosingSubcar().setSubCarState(endSubCarStatus_3);
        input.getHotMoldClosingFerry().setFerryState(endFerryStatus_2);
        input.getHotMoldClosingPosition().setStatus(UNOCCUPIED);
        outBoundInput.getInput().getPositions().get(outBoundInput.getInput().getOutBoundIndexInput().getHotSandMoldingPositionIndex()).setStatus(UNOCCUPIED);

        Product product = input.getHotMoldClosingPosition().getProduct().productDeepCopy();
        input.getHotMoldClosingSubcar().setProduct(product);

        outBoundInput.getInput().getPositions().get(outBoundInput.getInput().getOutBoundIndexInput().getHotSandMoldingPositionIndex()).setProduct(null);
        input.getHotMoldClosingPosition().setProduct(null);

        MoldClosingGanttChartLinkInput moldClosingGanttChartLinkInput = new MoldClosingGanttChartLinkInput(moldClosingGanttChart, input);

        return new OutBoundANDMoldClosing(outBoundInput, moldClosingGanttChartLinkInput, finishedCount);
    }

    /**
     * 在工岗装载事件生成
     * @param input
     * @param moldClosingGanttChart
     * @return
     */
    public MoldClosingGanttChartLinkInput HotMoleEmptyLoadInWorkStationEventCreate(MoldClosingSimulationInput input, GanttChart moldClosingGanttChart, Integer workStationIndex) throws SimulationException {
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_1()).getWorkPositionCoordinate().getY() -
                input.getHotMoldClosingFerry().getLocationCoordinate().getY()) / input.getHotMoldClosingSubcar().getEmptySpeed();
        double executionTime_2 = input.getHotMoldClosingSubcar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_1()).getWorkPositionCoordinate().getY() -
                input.getHotMoldClosingFerry().getLocationCoordinate().getY()) / input.getHotMoldClosingSubcar().getFullSpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getHotMoldClosingSubcar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getHotMoldClosingFerry().getFerryState());
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
        //工岗点状态列表
        List<WorkstationStatusEnum> startWorkstationStatus = new ArrayList<>();
        List<WorkstationStatusEnum> endWorkstationStatus = new ArrayList<>();
        startWorkstationStatus.add(WAIT_TRANSPORT);
        endWorkstationStatus.add(FREE);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getHotMoldClosingPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        List<Product> products = new ArrayList<>(input.getWorkStations().get(workStationIndex).getProduct());
        Product product = tools.createMoldClosingProduct(products);

        //生成事件
        Event event = tools.createEvent("100104", "ELMCSCGIWSP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_2 - executionTime_3, input.getHotMoldClosingSubcar().getId(), startSubCarStatus_1,true,
                input.getHotMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), endSubCarStatus_1, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getHotMoldClosingFerry().getId(), startFerryStatus_1,true,
                input.getHotMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_1, true,
                input.getHotMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, startWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotMoldClosingSubcar().getId(), SUB_CAR, RUNNING,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), null);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getHotMoldClosingSubCarInGanttIndex()).getEvents().add(event);

        //生成事件
        Event event_2 = tools.createEvent("100105", "ELMCSCIWSP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_3, input.getHotMoldClosingSubcar().getId(), endSubCarStatus_1,true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_2, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getHotMoldClosingFerry().getId(), endFerryStatus_1,true,
                input.getHotMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_1, true,
                input.getHotMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, endWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotMoldClosingSubcar().getId(), SUB_CAR, RISING, null, product);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getHotMoldClosingSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = tools.createEvent("100106", "FLMCSCGBTFWS", executionTime_3, input.getTotalTime() - executionTime_3, input.getTotalTime(),
                input.getHotMoldClosingSubcar().getId(), endSubCarStatus_2,true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(),
                endSubCarStatus_3, true, input.getHotMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), input.getHotMoldClosingFerry().getId(),
                endFerryStatus_1,true, input.getHotMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_2, true,
                input.getHotMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null, null,
                null, null, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getHotMoldClosingSubcar().getId(), SUB_CAR, RUNNING, input.getHotMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), product);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getHotMoldClosingSubCarInGanttIndex()).getEvents().add(event_3);

        //改变车辆状态和时间
        input.getHotMoldClosingSubcar().setSubCarState(endSubCarStatus_3);
        input.getHotMoldClosingFerry().setFerryState(endFerryStatus_2);
        input.getWorkStations().get(workStationIndex).setStatus(FREE);

        input.getHotMoldClosingSubcar().setProduct(product);
        input.getWorkStations().get(workStationIndex).setProduct(new ArrayList<>());

        MoldClosingGanttChartLinkInput moldClosingGanttChartLinkInput = new MoldClosingGanttChartLinkInput(moldClosingGanttChart, input);

        return moldClosingGanttChartLinkInput;
    }

    /**
     * 前往工岗装载事件生成
     * @param input
     * @param moldClosingGanttChart
     * @return
     */
    public MoldClosingGanttChartLinkInput HotMoleEmptyGoWorkStationEventCreate(MoldClosingSimulationInput input, GanttChart moldClosingGanttChart, Integer workStationIndex){
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                input.getHotMoldClosingFerry().getLocationCoordinate().getX()) / input.getHotMoldClosingFerry().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX(),
                input.getHotMoldClosingFerry().getLocationCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getHotMoldClosingSubcar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getHotMoldClosingFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(CARRY_SUB_CAR);
        //工岗点状态列表
        List<WorkstationStatusEnum> startWorkstationStatus = new ArrayList<>();
        List<WorkstationStatusEnum> endWorkstationStatus = new ArrayList<>();
        startWorkstationStatus.add(WAIT_TRANSPORT);
        endWorkstationStatus.add(WAIT_TRANSPORT);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getHotMoldClosingPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);


        //生成事件
        Event event = tools.createEvent("100107", "EGMCSCGIWSP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getHotMoldClosingSubcar().getId(), startSubCarStatus_1,false, null, endSubCarStatus, true,
                aimCoordinate, input.getHotMoldClosingFerry().getId(), startFerryStatus_1,false, null,
                endFerryStatus, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(),startWorkstationStatus, endWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotMoldClosingFerry().getId(), FERRY, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getHotMoldClosingSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getHotMoldClosingSubcar().setSubCarState(endSubCarStatus);
        input.getHotMoldClosingFerry().setFerryState(endFerryStatus);
        input.getHotMoldClosingSubcar().setLocationCoordinate(aimCoordinate);
        input.getHotMoldClosingFerry().setLocationCoordinate(aimCoordinate);

        MoldClosingGanttChartLinkInput moldClosingGanttChartLinkInput = new MoldClosingGanttChartLinkInput(moldClosingGanttChart, input);

        return moldClosingGanttChartLinkInput;
    }

    /**
     * 前往点位装载事件生成
     * @param input
     * @param moldClosingGanttChart
     * @return
     */
    public MoldClosingGanttChartLinkInput HotMoleEmptyGoHotPosotionEventCreate(MoldClosingSimulationInput input, GanttChart moldClosingGanttChart){
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getHotMoldClosingPosition().getCoordinate().getX() -
                input.getHotMoldClosingFerry().getLocationCoordinate().getX()) / input.getHotMoldClosingFerry().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getHotMoldClosingPosition().getCoordinate().getX(), input.getHotMoldClosingFerry().getLocationCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getHotMoldClosingSubcar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getHotMoldClosingFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(CARRY_SUB_CAR);
        //点位状态列表
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        startPositionStatus.add(OCCUPIED);
        endPositionStatus.add(OCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getHotMoldClosingPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);


        //生成事件
        Event event = tools.createEvent("100108", "EGMCSCGIHMP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getHotMoldClosingSubcar().getId(), startSubCarStatus_1,false, null, endSubCarStatus, true,
                aimCoordinate, input.getHotMoldClosingFerry().getId(), startFerryStatus_1,false, null, endFerryStatus,
                true, aimCoordinate, null,null, null, input.getHotMoldClosingPosition().getId(),
                startPositionStatus, endPositionStatus, null, null, null, pathIds, pathStatusEnums,
                pathStatusEnums, input.getHotMoldClosingFerry().getId(), FERRY, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getHotMoldClosingSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getHotMoldClosingSubcar().setSubCarState(endSubCarStatus);
        input.getHotMoldClosingFerry().setFerryState(endFerryStatus);
        input.getHotMoldClosingSubcar().setLocationCoordinate(aimCoordinate);
        input.getHotMoldClosingFerry().setLocationCoordinate(aimCoordinate);

        MoldClosingGanttChartLinkInput moldClosingGanttChartLinkInput = new MoldClosingGanttChartLinkInput(moldClosingGanttChart, input);

        return moldClosingGanttChartLinkInput;
    }

    /**
     * 前往工岗卸载砂箱事件生成
     * @param input
     * @param moldClosingGanttChart
     * @return
     */
    public MoldClosingGanttChartLinkInput HotMoleFullGoWorkStationEventCreate(MoldClosingSimulationInput input, GanttChart moldClosingGanttChart, Integer workStationIndex){
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                input.getHotMoldClosingFerry().getLocationCoordinate().getX()) / input.getHotMoldClosingFerry().getFullSpeed();
        double executionTime_2 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getHotMoldClosingFerry().getLocationCoordinate().getY()) / input.getHotMoldClosingSubcar().getFullSpeed();
        double executionTime_3 = input.getHotMoldClosingSubcar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getHotMoldClosingFerry().getLocationCoordinate().getY()) / input.getHotMoldClosingSubcar().getEmptySpeed();
                //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX(),
                input.getHotMoldClosingFerry().getLocationCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getHotMoldClosingSubcar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getHotMoldClosingFerry().getFerryState());
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
        endFerryStatus_1.add(CARRY_SUB_CAR);
        endFerryStatus_2.add(NOT_CARRY_SUB_CAR);
        //工岗点状态列表
        List<WorkstationStatusEnum> startWorkstationStatus = new ArrayList<>();
        List<WorkstationStatusEnum> endWorkstationStatus = new ArrayList<>();
        startWorkstationStatus.add(FREE);
        endWorkstationStatus.add(WAIT_PAIR);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getHotMoldClosingPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getHotMoldClosingSubcar().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.DOING_MOLD_CLOSING);

        //生成事件
        Event event_1 = tools.createEvent("100109", "FGMCSCGIWSP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getHotMoldClosingSubcar().getId(), startSubCarStatus_1,false,
                null, startSubCarStatus_1, true, aimCoordinate, input.getHotMoldClosingFerry().getId(), startFerryStatus_1,false,
                null, endFerryStatus_1, true, aimCoordinate, null,null, null, null,
                null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, startWorkstationStatus, pathIds,
                pathStatusEnums, pathStatusEnums, input.getHotMoldClosingFerry().getId(), FERRY, RUNNING, aimCoordinate, product);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getHotMoldClosingSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = tools.createEvent("100110", "FGMCSCIWSP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_3 - executionTime_4, input.getHotMoldClosingSubcar().getId(), startSubCarStatus_1,true, aimCoordinate,
                endSubCarStatus_1, true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getHotMoldClosingFerry().getId(),
                startFerryStatus_1,true, aimCoordinate, endFerryStatus_2, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, startWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotMoldClosingSubcar().getId(), SUB_CAR, RUNNING, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), product);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getHotMoldClosingSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = tools.createEvent("100111", "FGMCSCULIWSP", executionTime_3, input.getTotalTime() - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_4, input.getHotMoldClosingSubcar().getId(), endSubCarStatus_1,true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_2, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getHotMoldClosingFerry().getId(), endFerryStatus_2,true,
                aimCoordinate, endFerryStatus_2, true, aimCoordinate, null,null, null, null,
                null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotMoldClosingSubcar().getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getHotMoldClosingSubCarInGanttIndex()).getEvents().add(event_3);

        //生成事件
        Event event_4 = tools.createEvent("100112", "FGMCSCBFIWS", executionTime_4, input.getTotalTime() - executionTime_4, input.getTotalTime(),
                input.getHotMoldClosingSubcar().getId(), endSubCarStatus_2,true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(),
                endSubCarStatus_3, true, aimCoordinate, input.getHotMoldClosingFerry().getId(), endFerryStatus_2,true, aimCoordinate,
                endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotMoldClosingSubcar().getId(), SUB_CAR, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getHotMoldClosingSubCarInGanttIndex()).getEvents().add(event_4);

        //改变车辆状态和时间
        input.getHotMoldClosingSubcar().setSubCarState(endSubCarStatus_3);
        input.getHotMoldClosingFerry().setFerryState(endFerryStatus_1);
        input.getHotMoldClosingSubcar().setLocationCoordinate(aimCoordinate);
        input.getHotMoldClosingFerry().setLocationCoordinate(aimCoordinate);
        input.getWorkStations().get(workStationIndex).setStatus(WAIT_PAIR);

        List<Product> products = new ArrayList<>();
        products.add(product);
        input.getWorkStations().get(workStationIndex).setProduct(products);
        input.getHotMoldClosingSubcar().setProduct(null);

        MoldClosingGanttChartLinkInput moldClosingGanttChartLinkInput = new MoldClosingGanttChartLinkInput(moldClosingGanttChart, input);

        return moldClosingGanttChartLinkInput;
    }

    /**
     * 前往工岗卸载模具事件生成
     * @param input
     * @param moldClosingGanttChart
     * @return
     */
    public MoldClosingGanttChartLinkInput HotMoleFullGoPairWorkStationEventCreate(MoldClosingSimulationInput input, GanttChart moldClosingGanttChart, Integer workStationIndex){
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                input.getHotMoldClosingFerry().getLocationCoordinate().getX()) / input.getHotMoldClosingFerry().getFullSpeed();
        double executionTime_2 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getHotMoldClosingFerry().getLocationCoordinate().getY()) / input.getHotMoldClosingSubcar().getFullSpeed();
        double executionTime_3 = input.getHotMoldClosingSubcar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getHotMoldClosingFerry().getLocationCoordinate().getY()) / input.getHotMoldClosingSubcar().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX(),
                input.getHotMoldClosingFerry().getLocationCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getHotMoldClosingSubcar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getHotMoldClosingFerry().getFerryState());
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
        endFerryStatus_1.add(CARRY_SUB_CAR);
        endFerryStatus_2.add(NOT_CARRY_SUB_CAR);
        //工岗点状态列表
        List<WorkstationStatusEnum> startWorkstationStatus = new ArrayList<>();
        List<WorkstationStatusEnum> endWorkstationStatus = new ArrayList<>();
        startWorkstationStatus.add(WAIT_PAIR);
        endWorkstationStatus.add(OCCUPANCY);
        endWorkstationStatus.add(WORKING);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getHotMoldClosingPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getHotMoldClosingSubcar().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.DOING_MOLD_CLOSING);

        //生成事件
        Event event_1 = tools.createEvent("100113", "FGMCSCGIWSPP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getHotMoldClosingSubcar().getId(), startSubCarStatus_1,false,
                null, startSubCarStatus_1, true, aimCoordinate, input.getHotMoldClosingFerry().getId(), startFerryStatus_1,false,
                null, endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, startWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotMoldClosingFerry().getId(), FERRY, RUNNING, aimCoordinate, product);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getHotMoldClosingSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = tools.createEvent("100114", "FGMCSCIWSPP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_3 - executionTime_4, input.getHotMoldClosingSubcar().getId(), startSubCarStatus_1,true, aimCoordinate,
                endSubCarStatus_1, true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getHotMoldClosingFerry().getId(),
                startFerryStatus_1,true, aimCoordinate, endFerryStatus_2, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, startWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotMoldClosingSubcar().getId(), SUB_CAR, RUNNING,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), product);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getHotMoldClosingSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = tools.createEvent("100115", "FGMCSCULIWSPP", executionTime_3, input.getTotalTime() - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_4, input.getHotMoldClosingSubcar().getId(), endSubCarStatus_1,true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_2, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getHotMoldClosingFerry().getId(), endFerryStatus_2,true,
                aimCoordinate, endFerryStatus_2, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus,
                null, pathIds, pathStatusEnums, pathStatusEnums, input.getHotMoldClosingSubcar().getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getHotMoldClosingSubCarInGanttIndex()).getEvents().add(event_3);

        //生成事件
        Event event_4 = tools.createEvent("100116", "FGMCSCBFIWSP", executionTime_4, input.getTotalTime() - executionTime_4, input.getTotalTime(),
                input.getHotMoldClosingSubcar().getId(), endSubCarStatus_2,true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(),
                endSubCarStatus_3, true, aimCoordinate, input.getHotMoldClosingFerry().getId(), endFerryStatus_2,true, aimCoordinate,
                endFerryStatus_1, true, aimCoordinate, null,null, null, null,
                null, null, null, null, null, pathIds, pathStatusEnums,
                pathStatusEnums, input.getHotMoldClosingSubcar().getId(), SUB_CAR, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getHotMoldClosingSubCarInGanttIndex()).getEvents().add(event_4);

        //改变车辆状态和时间
        input.getHotMoldClosingSubcar().setSubCarState(endSubCarStatus_3);
        input.getHotMoldClosingFerry().setFerryState(endFerryStatus_1);
        input.getHotMoldClosingSubcar().setLocationCoordinate(aimCoordinate);
        input.getHotMoldClosingFerry().setLocationCoordinate(aimCoordinate);
        input.getWorkStations().get(workStationIndex).setStatus(WORKING);

        input.getWorkStations().get(workStationIndex).getProduct().add(product);
        input.getHotMoldClosingSubcar().setProduct(null);

        MoldClosingGanttChartLinkInput moldClosingGanttChartLinkInput = new MoldClosingGanttChartLinkInput(moldClosingGanttChart, input);

        return moldClosingGanttChartLinkInput;
    }

    /**
     * 前往点位卸载事件生成
     * @param input
     * @param moldClosingGanttChart
     * @return
     */
    public MoldClosingGanttChartLinkInput HotMoleFullGoLayPositionEventCreate(MoldClosingSimulationInput input, GanttChart moldClosingGanttChart){
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getHotMoldClosingLayPosition().getCoordinate().getX() -
                input.getHotMoldClosingFerry().getLocationCoordinate().getX()) / input.getHotMoldClosingFerry().getFullSpeed();
                //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getHotMoldClosingLayPosition().getCoordinate().getX(),
                input.getHotMoldClosingFerry().getLocationCoordinate().getY());
        //判断母车所在工岗的位置标识
//        int workStationIndex = tools.judgeHotFerryInWhichWorkstation(input.getHotMoldClosingFerry(), input.getWorkStations(),
//                input.getMoldClosingIndexInput());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getHotMoldClosingSubcar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getHotMoldClosingFerry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus = new ArrayList<>();
        endFerryStatus.add(CARRY_SUB_CAR);
        //工岗状态列表
        List<WorkstationStatusEnum> startWorkstationStatus = new ArrayList<>();
        startWorkstationStatus.add(WorkstationStatusEnum.FREE);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getHotMoldClosingPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        //生成事件
        Event event = tools.createEvent("100117", "FGMCSCGILP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getHotMoldClosingSubcar().getId(), startSubCarStatus_1,false, null, endSubCarStatus,
                true, aimCoordinate, input.getHotMoldClosingFerry().getId(), startFerryStatus_1,false, null,
                endFerryStatus, true, aimCoordinate, null,null, null, null,
                null, null, null, null, null, pathIds, pathStatusEnums,
                pathStatusEnums, input.getHotMoldClosingFerry().getId(), FERRY, RUNNING, aimCoordinate, input.getHotMoldClosingSubcar().getProduct().productDeepCopy());

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getHotMoldClosingSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getHotMoldClosingSubcar().setSubCarState(endSubCarStatus);
        input.getHotMoldClosingFerry().setFerryState(endFerryStatus);
        input.getHotMoldClosingSubcar().setLocationCoordinate(aimCoordinate);
        input.getHotMoldClosingFerry().setLocationCoordinate(aimCoordinate);

        MoldClosingGanttChartLinkInput moldClosingGanttChartLinkInput = new MoldClosingGanttChartLinkInput(moldClosingGanttChart, input);

        return moldClosingGanttChartLinkInput;
    }

    /**
     * 前往点位卸载事件生成
     * @param input
     * @param moldClosingGanttChart
     * @return
     */
    public MoldClosingGanttChartLinkInput HotMoleFullUnLoadLayPositionEventCreate(MoldClosingSimulationInput input, GanttChart moldClosingGanttChart){
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getHotMoldClosingLayPosition().getCoordinate().getY() -
                input.getHotMoldClosingFerry().getLocationCoordinate().getY()) / input.getHotMoldClosingSubcar().getFullSpeed();
        double executionTime_2 = input.getHotMoldClosingSubcar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getHotMoldClosingLayPosition().getCoordinate().getY() -
                input.getHotMoldClosingFerry().getLocationCoordinate().getY()) / input.getHotMoldClosingSubcar().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getHotMoldClosingLayPosition().getCoordinate().getX(),
                input.getHotMoldClosingFerry().getLocationCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getHotMoldClosingSubcar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getHotMoldClosingFerry().getFerryState());
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
        endFerryStatus_1.add(CARRY_SUB_CAR);
        endFerryStatus_2.add(NOT_CARRY_SUB_CAR);
        //点位状态列表
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        startPositionStatus.add(UNOCCUPIED);
        endPositionStatus.add(OCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getHotMoldClosingPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getHotMoldClosingSubcar().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.GOING_SANDBLASTING);
        product.setProductFinish(ProductFinishEnum.FINISH_MOLD_CLOSING);

        //生成事件
        Event event_1 = tools.createEvent("100118", "FGMCSCILP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_2 - executionTime_3, input.getHotMoldClosingSubcar().getId(), startSubCarStatus_1,true, aimCoordinate,
                endSubCarStatus_1, true, input.getHotMoldClosingLayPosition().getCoordinate(), input.getHotMoldClosingFerry().getId(), startFerryStatus_1,
                true, aimCoordinate, endFerryStatus_2, true, aimCoordinate, null,null, null,
                input.getHotMoldClosingLayPosition().getId(), startPositionStatus, startPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotMoldClosingSubcar().getId(), SUB_CAR, RUNNING, input.getHotMoldClosingLayPosition().getCoordinate(), product);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getHotMoldClosingSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = tools.createEvent("100119", "FGMCSCULILP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_3, input.getHotMoldClosingSubcar().getId(), endSubCarStatus_1,true,
                input.getHotMoldClosingLayPosition().getCoordinate(), endSubCarStatus_2, true,
                input.getHotMoldClosingLayPosition().getCoordinate(), input.getHotMoldClosingFerry().getId(), endFerryStatus_2,true,
                aimCoordinate, endFerryStatus_2, true, aimCoordinate, null,null, null,
                input.getHotMoldClosingLayPosition().getId(), startPositionStatus, endPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotMoldClosingSubcar().getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getHotMoldClosingSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = tools.createEvent("100120", "FGMCSCBFILP", executionTime_3, input.getTotalTime() - executionTime_3, input.getTotalTime(),
                input.getHotMoldClosingSubcar().getId(), endSubCarStatus_2,true, input.getHotMoldClosingLayPosition().getCoordinate(), endSubCarStatus_3,
                true, aimCoordinate, input.getHotMoldClosingFerry().getId(), endFerryStatus_2,true, aimCoordinate,
                endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotMoldClosingSubcar().getId(), SUB_CAR, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getHotMoldClosingSubCarInGanttIndex()).getEvents().add(event_3);

        //改变车辆状态和时间
        input.getHotMoldClosingSubcar().setSubCarState(endSubCarStatus_3);
        input.getHotMoldClosingFerry().setFerryState(endFerryStatus_1);
        input.getHotMoldClosingSubcar().setLocationCoordinate(aimCoordinate);
        input.getHotMoldClosingFerry().setLocationCoordinate(aimCoordinate);
        input.getHotMoldClosingLayPosition().setStatus(OCCUPIED);

        input.getHotMoldClosingLayPosition().setProduct(product);
        input.getHotMoldClosingSubcar().setProduct(null);

        MoldClosingGanttChartLinkInput moldClosingGanttChartLinkInput = new MoldClosingGanttChartLinkInput(moldClosingGanttChart, input);

        return moldClosingGanttChartLinkInput;
    }
}
