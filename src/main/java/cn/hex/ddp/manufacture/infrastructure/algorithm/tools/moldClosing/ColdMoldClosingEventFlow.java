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
public class ColdMoldClosingEventFlow {
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
    public OutBoundANDMoldClosing ColdMoleEmptyLoadEventCreate(MoldClosingSimulationInput input, OutBoundGanttChartLinkInput outBoundInput,
                                                               GanttChart moldClosingGanttChart, int finishedCount){
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getColdMoldClosingPosition().getCoordinate().getY() -
                input.getColdMoldClosingFerry().getLocationCoordinate().getY()) / input.getColdMoldClosingSubcar().getEmptySpeed();
        double executionTime_2 = input.getColdMoldClosingSubcar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getColdMoldClosingPosition().getCoordinate().getY() -
                input.getColdMoldClosingFerry().getLocationCoordinate().getY()) / input.getColdMoldClosingSubcar().getFullSpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getColdMoldClosingSubcar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getColdMoldClosingFerry().getFerryState());
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
        //母车结束状态  ?
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
        pathIds.add(input.getColdMoldClosingPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = tools.createEvent("100121", "ELMCSCGICMP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_2 - executionTime_3, input.getColdMoldClosingSubcar().getId(), startSubCarStatus_1,true,
                input.getColdMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), endSubCarStatus_1, true, input.getColdMoldClosingPosition().getCoordinate(),
                input.getColdMoldClosingFerry().getId(), startFerryStatus_1,true, input.getColdMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(),
                endFerryStatus_1, true, input.getColdMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), null,null,
                null, input.getColdMoldClosingPosition().getId(), startPositionStatus, startPositionStatus, null,null,
                null, pathIds, pathStatusEnums, pathStatusEnums, input.getColdMoldClosingSubcar().getId(), SUB_CAR, RUNNING,
                input.getColdMoldClosingPosition().getCoordinate(), null);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getColdMoldClosingSubCarInGanttIndex()).getEvents().add(event);

        //生成事件
        event = tools.createEvent("100122", "ELMCSCICMP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_3, input.getColdMoldClosingSubcar().getId(), endSubCarStatus_1,true,
                input.getColdMoldClosingPosition().getCoordinate(), endSubCarStatus_2, true, input.getColdMoldClosingPosition().getCoordinate(),
                input.getColdMoldClosingFerry().getId(), endFerryStatus_1,true, input.getColdMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(),
                endFerryStatus_1, true, input.getColdMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), null,null,
                null, input.getColdMoldClosingPosition().getId(), startPositionStatus, endPositionStatus, null,null,
                null, pathIds, pathStatusEnums, pathStatusEnums, input.getColdMoldClosingSubcar().getId(), SUB_CAR, RISING, null,
                input.getColdMoldClosingPosition().getProduct().productDeepCopy());

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getColdMoldClosingSubCarInGanttIndex()).getEvents().add(event);

        //生成事件
        event = tools.createEvent("100123", "FLMCSCGBTF", executionTime_3, input.getTotalTime() - executionTime_3, input.getTotalTime(),
                input.getColdMoldClosingSubcar().getId(), endSubCarStatus_2,true, input.getColdMoldClosingPosition().getCoordinate(), endSubCarStatus_3,
                true, input.getColdMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), input.getColdMoldClosingFerry().getId(), endFerryStatus_1,
                true, input.getColdMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_2, true,
                input.getColdMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null, null,
                null, null, null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getColdMoldClosingSubcar().getId(), SUB_CAR, RUNNING, input.getColdMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(),
                input.getColdMoldClosingPosition().getProduct().productDeepCopy());

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getColdMoldClosingSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getColdMoldClosingSubcar().setSubCarState(endSubCarStatus_3);
        input.getColdMoldClosingFerry().setFerryState(endFerryStatus_2);
        input.getColdMoldClosingPosition().setStatus(UNOCCUPIED);
        outBoundInput.getInput().getPositions().get(outBoundInput.getInput().getOutBoundIndexInput().getColdSandMoldingPositionIndex()).setStatus(UNOCCUPIED);
        Product product = input.getColdMoldClosingPosition().getProduct().productDeepCopy();
        input.getColdMoldClosingSubcar().setProduct(product);
        outBoundInput.getInput().getPositions().get(outBoundInput.getInput().getOutBoundIndexInput().getColdSandMoldingPositionIndex()).setProduct(null);
        input.getColdMoldClosingPosition().setProduct(null);

        MoldClosingGanttChartLinkInput moldClosingGanttChartLinkInput = new MoldClosingGanttChartLinkInput(moldClosingGanttChart, input);

        return new OutBoundANDMoldClosing(outBoundInput, moldClosingGanttChartLinkInput, finishedCount);
    }

    /**
     * 在工岗装载事件生成
     * @param input
     * @param moldClosingGanttChart
     * @return
     */
    public MoldClosingGanttChartLinkInput ColdMoleEmptyLoadInWorkStationEventCreate(MoldClosingSimulationInput input, GanttChart moldClosingGanttChart, Integer workStationIndex) throws SimulationException {
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getWorkStations().get(input.getMoldClosingIndexInput().getColdMoldClosingWorkStationIndex_1()).getWorkPositionCoordinate().getY() -
                input.getColdMoldClosingFerry().getLocationCoordinate().getY()) / input.getColdMoldClosingSubcar().getEmptySpeed();
        double executionTime_2 = input.getColdMoldClosingSubcar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getWorkStations().get(input.getMoldClosingIndexInput().getColdMoldClosingWorkStationIndex_1()).getWorkPositionCoordinate().getY() -
                input.getColdMoldClosingFerry().getLocationCoordinate().getY()) / input.getColdMoldClosingSubcar().getFullSpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getColdMoldClosingSubcar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getColdMoldClosingFerry().getFerryState());
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
        pathIds.add(input.getColdMoldClosingPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        List<Product> products = new ArrayList<>(input.getWorkStations().get(workStationIndex).getProduct());
        Product product = tools.createMoldClosingProduct(products);

        //生成事件
        Event event = tools.createEvent("100124", "ELMCSCGICWSP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_2 - executionTime_3, input.getColdMoldClosingSubcar().getId(), startSubCarStatus_1,true,
                input.getColdMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), endSubCarStatus_1, true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(),
                input.getColdMoldClosingFerry().getId(), startFerryStatus_1,true, input.getColdMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(),
                endFerryStatus_1, true, input.getColdMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), null,null,
                null, null, null, null, input.getWorkStations().get(workStationIndex).getId(),startWorkstationStatus,
                startWorkstationStatus, pathIds, pathStatusEnums, pathStatusEnums, input.getColdMoldClosingSubcar().getId(), SUB_CAR, RUNNING,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), null);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getColdMoldClosingSubCarInGanttIndex()).getEvents().add(event);

        //生成事件
        event = tools.createEvent("100125", "ELMCSCICWSP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3, input.getTotalTime() - executionTime_3,
                input.getColdMoldClosingSubcar().getId(), endSubCarStatus_1,true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_2, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getColdMoldClosingFerry().getId(), endFerryStatus_1,true, input.getColdMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(),
                endFerryStatus_1, true, input.getColdMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, endWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdMoldClosingSubcar().getId(), SUB_CAR, RISING, null, product);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getColdMoldClosingSubCarInGanttIndex()).getEvents().add(event);

        //生成事件
        event = tools.createEvent("100126", "FLMCSCGBTFCWS", executionTime_3, input.getTotalTime() - executionTime_3, input.getTotalTime(),
                input.getColdMoldClosingSubcar().getId(), endSubCarStatus_2,true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_3, true,
                input.getColdMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), input.getColdMoldClosingFerry().getId(), endFerryStatus_1,true, input.getColdMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(),
                endFerryStatus_2, true, input.getColdMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdMoldClosingSubcar().getId(), SUB_CAR, RUNNING,
                input.getColdMoldClosingFerry().getLocationCoordinate().coordinateDeepCopy(), product);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getColdMoldClosingSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getColdMoldClosingSubcar().setSubCarState(endSubCarStatus_3);
        input.getColdMoldClosingFerry().setFerryState(endFerryStatus_2);
        input.getWorkStations().get(workStationIndex).setStatus(FREE);

        input.getColdMoldClosingSubcar().setProduct(product);
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
    public MoldClosingGanttChartLinkInput ColdMoleEmptyGoWorkStationEventCreate(MoldClosingSimulationInput input, GanttChart moldClosingGanttChart, Integer workStationIndex){
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                input.getColdMoldClosingFerry().getLocationCoordinate().getX()) / input.getColdMoldClosingFerry().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX(),
                input.getColdMoldClosingPath().getStartCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getColdMoldClosingSubcar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getColdMoldClosingFerry().getFerryState());
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
        pathIds.add(input.getColdMoldClosingPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);


        //生成事件
        Event event = tools.createEvent("100127", "EGMCSCGICWSP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getColdMoldClosingSubcar().getId(), startSubCarStatus_1,false, null, endSubCarStatus, true,
                aimCoordinate, input.getColdMoldClosingFerry().getId(), startFerryStatus_1,false, null,
                endFerryStatus, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(),startWorkstationStatus, endWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdMoldClosingFerry().getId(), FERRY, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getColdMoldClosingSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getColdMoldClosingSubcar().setSubCarState(endSubCarStatus);
        input.getColdMoldClosingFerry().setFerryState(endFerryStatus);
        input.getColdMoldClosingSubcar().setLocationCoordinate(aimCoordinate);
        input.getColdMoldClosingFerry().setLocationCoordinate(aimCoordinate);

        MoldClosingGanttChartLinkInput moldClosingGanttChartLinkInput = new MoldClosingGanttChartLinkInput(moldClosingGanttChart, input);

        return moldClosingGanttChartLinkInput;
    }

    /**
     * 前往点位装载事件生成
     * @param input
     * @param moldClosingGanttChart
     * @return
     */
    public MoldClosingGanttChartLinkInput ColdMoleEmptyGoColdPosotionEventCreate(MoldClosingSimulationInput input, GanttChart moldClosingGanttChart){
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getColdMoldClosingPosition().getCoordinate().getX() -
                input.getColdMoldClosingFerry().getLocationCoordinate().getX()) / input.getColdMoldClosingFerry().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getColdMoldClosingPosition().getCoordinate().getX(), input.getColdMoldClosingPath().getStartCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getColdMoldClosingSubcar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getColdMoldClosingFerry().getFerryState());
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
        pathIds.add(input.getColdMoldClosingPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);


        //生成事件
        Event event = tools.createEvent("100128", "EGMCSCGICMP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getColdMoldClosingSubcar().getId(), startSubCarStatus_1,false, null, endSubCarStatus, true,
                aimCoordinate, input.getColdMoldClosingFerry().getId(), startFerryStatus_1,false, null,
                endFerryStatus, true, aimCoordinate, null,null, null,
                input.getColdMoldClosingPosition().getId(), startPositionStatus, endPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdMoldClosingFerry().getId(), FERRY, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getColdMoldClosingSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getColdMoldClosingSubcar().setSubCarState(endSubCarStatus);
        input.getColdMoldClosingFerry().setFerryState(endFerryStatus);
        input.getColdMoldClosingSubcar().setLocationCoordinate(aimCoordinate);
        input.getColdMoldClosingFerry().setLocationCoordinate(aimCoordinate);

        MoldClosingGanttChartLinkInput moldClosingGanttChartLinkInput = new MoldClosingGanttChartLinkInput(moldClosingGanttChart, input);

        return moldClosingGanttChartLinkInput;
    }

    /**
     * 前往工岗卸载砂箱事件生成
     * @param input
     * @param moldClosingGanttChart
     * @return
     */
    public MoldClosingGanttChartLinkInput ColdMoleFullGoWorkStationEventCreate(MoldClosingSimulationInput input, GanttChart moldClosingGanttChart, Integer workStationIndex){
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                input.getColdMoldClosingFerry().getLocationCoordinate().getX()) / input.getColdMoldClosingFerry().getFullSpeed();
        double executionTime_2 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getColdMoldClosingFerry().getLocationCoordinate().getY()) / input.getColdMoldClosingSubcar().getFullSpeed();
        double executionTime_3 = input.getColdMoldClosingSubcar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getColdMoldClosingFerry().getLocationCoordinate().getY()) / input.getColdMoldClosingSubcar().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX(),
                input.getColdMoldClosingPath().getStartCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getColdMoldClosingSubcar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getColdMoldClosingFerry().getFerryState());
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
        pathIds.add(input.getColdMoldClosingPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getColdMoldClosingSubcar().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.DOING_MOLD_CLOSING);

        //生成事件
        Event event = tools.createEvent("100129", "FGMCSCGICWSP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getColdMoldClosingSubcar().getId(), startSubCarStatus_1,false,
                null, startSubCarStatus_1, true, aimCoordinate, input.getColdMoldClosingFerry().getId(), startFerryStatus_1,false,
                null, endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, startWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdMoldClosingFerry().getId(), FERRY, RUNNING, aimCoordinate, product);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getColdMoldClosingSubCarInGanttIndex()).getEvents().add(event);

        //生成事件
        event = tools.createEvent("100130", "FGMCSCICWSP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_3 - executionTime_4, input.getColdMoldClosingSubcar().getId(), startSubCarStatus_1,true, aimCoordinate,
                endSubCarStatus_1, true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getColdMoldClosingFerry().getId(),
                startFerryStatus_1,true, aimCoordinate, endFerryStatus_2, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, startWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdMoldClosingSubcar().getId(), SUB_CAR, RUNNING,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), product);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getColdMoldClosingSubCarInGanttIndex()).getEvents().add(event);

        //生成事件
        event = tools.createEvent("100131", "FGMCSCULIWSP", executionTime_3, input.getTotalTime() - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_4, input.getColdMoldClosingSubcar().getId(), endSubCarStatus_1,true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_2, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getColdMoldClosingFerry().getId(), endFerryStatus_2,true,
                aimCoordinate, endFerryStatus_2, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdMoldClosingSubcar().getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getColdMoldClosingSubCarInGanttIndex()).getEvents().add(event);

        //生成事件
        event = tools.createEvent("100132", "FGMCSCBFICWS", executionTime_4, input.getTotalTime() - executionTime_4, input.getTotalTime(),
                input.getColdMoldClosingSubcar().getId(), endSubCarStatus_2,true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(),
                endSubCarStatus_3, true, aimCoordinate, input.getColdMoldClosingFerry().getId(), endFerryStatus_2,true, aimCoordinate,
                endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdMoldClosingSubcar().getId(), SUB_CAR, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getColdMoldClosingSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getColdMoldClosingSubcar().setSubCarState(endSubCarStatus_3);
        input.getColdMoldClosingFerry().setFerryState(endFerryStatus_1);
        input.getColdMoldClosingSubcar().setLocationCoordinate(aimCoordinate);
        input.getColdMoldClosingFerry().setLocationCoordinate(aimCoordinate);
        input.getWorkStations().get(workStationIndex).setStatus(WAIT_PAIR);

        List<Product> products = new ArrayList<>();
        products.add(product);
        input.getWorkStations().get(workStationIndex).setProduct(products);
        input.getColdMoldClosingSubcar().setProduct(null);

        MoldClosingGanttChartLinkInput moldClosingGanttChartLinkInput = new MoldClosingGanttChartLinkInput(moldClosingGanttChart, input);

        return moldClosingGanttChartLinkInput;
    }

    /**
     * 前往工岗卸载模具事件生成
     * @param input
     * @param moldClosingGanttChart
     * @return
     */
    public MoldClosingGanttChartLinkInput ColdMoleFullGoPairWorkStationEventCreate(MoldClosingSimulationInput input, GanttChart moldClosingGanttChart, Integer workStationIndex){
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                input.getColdMoldClosingFerry().getLocationCoordinate().getX()) / input.getColdMoldClosingFerry().getFullSpeed();
        double executionTime_2 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getColdMoldClosingFerry().getLocationCoordinate().getY()) / input.getColdMoldClosingSubcar().getFullSpeed();
        double executionTime_3 = input.getColdMoldClosingSubcar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getColdMoldClosingFerry().getLocationCoordinate().getY()) / input.getColdMoldClosingSubcar().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX(),
                input.getColdMoldClosingPath().getStartCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getColdMoldClosingSubcar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getColdMoldClosingFerry().getFerryState());
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
        pathIds.add(input.getColdMoldClosingPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getColdMoldClosingSubcar().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.DOING_MOLD_CLOSING);

        //生成事件
        Event event = tools.createEvent("100133", "FGMCSCGICWSPP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4, input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getColdMoldClosingSubcar().getId(), startSubCarStatus_1,false, null, startSubCarStatus_1, true,
                aimCoordinate, input.getColdMoldClosingFerry().getId(), startFerryStatus_1,false, null,
                endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, startWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdMoldClosingFerry().getId(), FERRY, RUNNING, aimCoordinate, product);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getColdMoldClosingSubCarInGanttIndex()).getEvents().add(event);

        //生成事件
        event = tools.createEvent("100134", "FGMCSCICWSPP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getTotalTime() - executionTime_3 - executionTime_4,
                input.getColdMoldClosingSubcar().getId(), startSubCarStatus_1,true, aimCoordinate, endSubCarStatus_1, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getColdMoldClosingFerry().getId(), startFerryStatus_1,true, aimCoordinate,
                endFerryStatus_2, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, startWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdMoldClosingSubcar().getId(), SUB_CAR, RUNNING,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), product);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getColdMoldClosingSubCarInGanttIndex()).getEvents().add(event);

        //生成事件
        event = tools.createEvent("100135", "FGMCSCULICWSPP", executionTime_3, input.getTotalTime() - executionTime_3 - executionTime_4, input.getTotalTime() - executionTime_4,
                input.getColdMoldClosingSubcar().getId(), endSubCarStatus_1,true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_2, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getColdMoldClosingFerry().getId(), endFerryStatus_2,true, aimCoordinate,
                endFerryStatus_2, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdMoldClosingSubcar().getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getColdMoldClosingSubCarInGanttIndex()).getEvents().add(event);

        //生成事件
        event = tools.createEvent("100136", "FGMCSCBFICWSP", executionTime_4, input.getTotalTime() - executionTime_4, input.getTotalTime(),
                input.getColdMoldClosingSubcar().getId(), endSubCarStatus_2,true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_3, true,
                aimCoordinate, input.getColdMoldClosingFerry().getId(), endFerryStatus_2,true, aimCoordinate,
                endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdMoldClosingSubcar().getId(), SUB_CAR, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getColdMoldClosingSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getColdMoldClosingSubcar().setSubCarState(endSubCarStatus_3);
        input.getColdMoldClosingFerry().setFerryState(endFerryStatus_1);
        input.getColdMoldClosingSubcar().setLocationCoordinate(aimCoordinate);
        input.getColdMoldClosingFerry().setLocationCoordinate(aimCoordinate);
        input.getWorkStations().get(workStationIndex).setStatus(WORKING);

        input.getWorkStations().get(workStationIndex).getProduct().add(product);
        input.getColdMoldClosingSubcar().setProduct(null);

        MoldClosingGanttChartLinkInput moldClosingGanttChartLinkInput = new MoldClosingGanttChartLinkInput(moldClosingGanttChart, input);

        return moldClosingGanttChartLinkInput;
    }

    /**
     * 前往点位卸载事件生成
     * @param input
     * @param moldClosingGanttChart
     * @return
     */
    public MoldClosingGanttChartLinkInput ColdMoleFullGoLayPositionEventCreate(MoldClosingSimulationInput input, GanttChart moldClosingGanttChart){
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getColdMoldClosingLayPosition().getCoordinate().getX() -
                input.getColdMoldClosingFerry().getLocationCoordinate().getX()) / input.getColdMoldClosingFerry().getFullSpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getColdMoldClosingLayPosition().getCoordinate().getX(),
                input.getColdMoldClosingPath().getStartCoordinate().getY());
//        //判断母车所在工岗的位置标识
//        int workStationIndex = tools.judgeColdFerryInWhichWorkstation(input.getColdMoldClosingFerry(), input.getWorkStations(),
//                input.getMoldClosingIndexInput());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getColdMoldClosingSubcar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getColdMoldClosingFerry().getFerryState());
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
        pathIds.add(input.getColdMoldClosingPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);


        //生成事件
        Event event = tools.createEvent("100137", "FGMCSCGICLP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getColdMoldClosingSubcar().getId(), startSubCarStatus_1,false, null, endSubCarStatus, true,
                aimCoordinate, input.getColdMoldClosingFerry().getId(), startFerryStatus_1,false, null, endFerryStatus,
                true, aimCoordinate, null,null, null, null, null,
                null, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getColdMoldClosingFerry().getId(), FERRY, RUNNING, aimCoordinate, input.getColdMoldClosingSubcar().getProduct().productDeepCopy());

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getColdMoldClosingSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getColdMoldClosingSubcar().setSubCarState(endSubCarStatus);
        input.getColdMoldClosingFerry().setFerryState(endFerryStatus);
        input.getColdMoldClosingSubcar().setLocationCoordinate(aimCoordinate);
        input.getColdMoldClosingFerry().setLocationCoordinate(aimCoordinate);

        MoldClosingGanttChartLinkInput moldClosingGanttChartLinkInput = new MoldClosingGanttChartLinkInput(moldClosingGanttChart, input);

        return moldClosingGanttChartLinkInput;
    }

    /**
     * 前往点位卸载事件生成
     * @param input
     * @param moldClosingGanttChart
     * @return
     */
    public MoldClosingGanttChartLinkInput ColdMoleFullUnLoadLayPositionEventCreate(MoldClosingSimulationInput input, GanttChart moldClosingGanttChart){
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getColdMoldClosingLayPosition().getCoordinate().getY() -
                input.getColdMoldClosingFerry().getLocationCoordinate().getY()) / input.getColdMoldClosingSubcar().getFullSpeed();
        double executionTime_2 = input.getColdMoldClosingSubcar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getColdMoldClosingLayPosition().getCoordinate().getY() -
                input.getColdMoldClosingFerry().getLocationCoordinate().getY()) / input.getColdMoldClosingSubcar().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getColdMoldClosingLayPosition().getCoordinate().getX(),
                input.getColdMoldClosingPath().getStartCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getColdMoldClosingSubcar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getColdMoldClosingFerry().getFerryState());
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
        pathIds.add(input.getColdMoldClosingPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getColdMoldClosingSubcar().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.GOING_SANDBLASTING);
        product.setProductFinish(ProductFinishEnum.FINISH_MOLD_CLOSING);

        //生成事件
        Event event = tools.createEvent("100138", "FGMCSCICLP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getColdMoldClosingSubcar().getId(), startSubCarStatus_1,true, aimCoordinate, endSubCarStatus_1, true,
                input.getColdMoldClosingLayPosition().getCoordinate(), input.getColdMoldClosingFerry().getId(), startFerryStatus_1,true, aimCoordinate,
                endFerryStatus_2, true, aimCoordinate, null,null, null,
                input.getColdMoldClosingLayPosition().getId(), startPositionStatus, startPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdMoldClosingSubcar().getId(), SUB_CAR, RUNNING, input.getColdMoldClosingLayPosition().getCoordinate(), product);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getColdMoldClosingSubCarInGanttIndex()).getEvents().add(event);

        //生成事件
        event = tools.createEvent("100139", "FGMCSCULICLP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3, input.getTotalTime() - executionTime_3,
                input.getColdMoldClosingSubcar().getId(), endSubCarStatus_1,true, input.getColdMoldClosingLayPosition().getCoordinate(), endSubCarStatus_2, true,
                input.getColdMoldClosingLayPosition().getCoordinate(), input.getColdMoldClosingFerry().getId(), endFerryStatus_2,true, aimCoordinate,
                endFerryStatus_2, true, aimCoordinate, null,null, null,
                input.getColdMoldClosingLayPosition().getId(), startPositionStatus, endPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdMoldClosingSubcar().getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getColdMoldClosingSubCarInGanttIndex()).getEvents().add(event);

        //生成事件
        event = tools.createEvent("100140", "FGMCSCBFICLP", executionTime_3, input.getTotalTime() - executionTime_3, input.getTotalTime(),
                input.getColdMoldClosingSubcar().getId(), endSubCarStatus_2,true, input.getColdMoldClosingLayPosition().getCoordinate(), endSubCarStatus_3, true,
                aimCoordinate, input.getColdMoldClosingFerry().getId(), endFerryStatus_2,true, aimCoordinate,
                endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdMoldClosingSubcar().getId(), SUB_CAR, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(input.getMoldClosingIndexInput().getColdMoldClosingSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getColdMoldClosingSubcar().setSubCarState(endSubCarStatus_3);
        input.getColdMoldClosingFerry().setFerryState(endFerryStatus_1);
        input.getColdMoldClosingSubcar().setLocationCoordinate(aimCoordinate);
        input.getColdMoldClosingFerry().setLocationCoordinate(aimCoordinate);
        input.getColdMoldClosingLayPosition().setStatus(OCCUPIED);

        input.getColdMoldClosingLayPosition().setProduct(product);
        input.getColdMoldClosingSubcar().setProduct(null);

        MoldClosingGanttChartLinkInput moldClosingGanttChartLinkInput = new MoldClosingGanttChartLinkInput(moldClosingGanttChart, input);

        return moldClosingGanttChartLinkInput;
    }
}
