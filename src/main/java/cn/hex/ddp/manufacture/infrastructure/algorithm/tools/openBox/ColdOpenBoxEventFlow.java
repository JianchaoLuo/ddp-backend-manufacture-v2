package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.openBox;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum;
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
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Deque;
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
 * @Created: 2024/8/1 3:03
 */
@NoArgsConstructor
@Slf4j
public class ColdOpenBoxEventFlow {
    /**
     * 仿真总工具类
     */
    private SimulationTool allTool = new SimulationTool();
    /**
     * 开箱区的仿真工具类
     */
    private OpenBoxSimulationTools tools = new OpenBoxSimulationTools();

    public AfterClosingBoxAllLink ColdAndHotEmptyLoadInPositionEventCreate(AfterClosingBoxAllLink simulationOutput) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getColdAndHotOpenBoxPosition().getCoordinate().getY() -
                input.getColdAndHotFerry().getLocationCoordinate().getY()) / input.getColdAndHotSubCar().getEmptySpeed();
        double executionTime_2 = input.getColdAndHotSubCar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getColdAndHotOpenBoxPosition().getCoordinate().getY() -
                input.getColdAndHotFerry().getLocationCoordinate().getY()) / input.getColdAndHotSubCar().getFullSpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getColdAndHotSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getColdAndHotFerry().getFerryState());
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
        pathIds.add(input.getColdAndHotOpenBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getColdAndHotOpenBoxPosition().getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100717", "ELOBSCGICAHOBP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_2 - executionTime_3, input.getColdAndHotSubCar().getId(), startSubCarStatus_1, true,
                input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), endSubCarStatus_1, true, input.getColdAndHotOpenBoxPosition().getCoordinate(),
                input.getColdAndHotFerry().getId(), startFerryStatus_1,true, input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_1,
                true, input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null,
                input.getColdAndHotOpenBoxPosition().getId(), startPositionStatus, startPositionStatus, null,null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdAndHotSubCar().getId(), SUB_CAR, RUNNING, input.getColdAndHotOpenBoxPosition().getCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getColdAndHotSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100718", "ELOBSCLICAHOBP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_3, input.getColdAndHotSubCar().getId(), endSubCarStatus_1, true,
                input.getColdAndHotOpenBoxPosition().getCoordinate(), endSubCarStatus_2, true, input.getColdAndHotOpenBoxPosition().getCoordinate(),
                input.getColdAndHotFerry().getId(), endFerryStatus_1,true, input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_1,
                true, input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null,
                input.getColdAndHotOpenBoxPosition().getId(), startPositionStatus, endPositionStatus, null,null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdAndHotSubCar().getId(), SUB_CAR, RISING, null, product);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getColdAndHotSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100719", "FLOBSCGBTFICAH", executionTime_3, input.getTotalTime() - executionTime_3,
                input.getTotalTime(), input.getColdAndHotSubCar().getId(), endSubCarStatus_2,true, input.getColdAndHotOpenBoxPosition().getCoordinate(),
                endSubCarStatus_3, true, input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), input.getColdAndHotFerry().getId(),
                endFerryStatus_1,true, input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_2, true,
                input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null, null,
                null, null, null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getColdAndHotSubCar().getId(), SUB_CAR, RUNNING, input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), product);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getColdAndHotSubCarInGanttIndex()).getEvents().add(event_3);

        //改变车辆状态和时间
        input.getColdAndHotSubCar().setSubCarState(endSubCarStatus_3);
        input.getColdAndHotFerry().setFerryState(endFerryStatus_2);
        input.getColdAndHotOpenBoxPosition().setStatus(UNOCCUPIED);


        input.getColdAndHotSubCar().setProduct(product);
        input.getColdAndHotOpenBoxPosition().setProduct(null);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink ColdAndHotOpenBoxEmptyLoadInWorkStationEventCreate(AfterClosingBoxAllLink simulationOutput, int workStationIndex, Deque<Product> downProducts) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getColdAndHotFerry().getLocationCoordinate().getY()) / input.getColdAndHotSubCar().getEmptySpeed();
        double executionTime_2 = input.getColdAndHotSubCar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getColdAndHotFerry().getLocationCoordinate().getY()) / input.getColdAndHotSubCar().getFullSpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getColdAndHotSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getColdAndHotFerry().getFerryState());
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
        //改变车辆状态和时间
        input.getColdAndHotSubCar().setSubCarState(endSubCarStatus_3);
        input.getColdAndHotFerry().setFerryState(endFerryStatus_2);
        //工岗点状态列表
        List<WorkstationStatusEnum> startWorkstationStatus = new ArrayList<>();
        List<WorkstationStatusEnum> endWorkstationStatus = new ArrayList<>();
        startWorkstationStatus.add(WAIT_TRANSPORT);

        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getColdAndHotOpenBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        //根据工岗的产品判断工岗的结束状态
        if (input.getWorkStations().get(workStationIndex).getProduct().size() > 1){
            Product product = input.getWorkStations().get(workStationIndex).getProduct().getFirst().productDeepCopy();
            input.getWorkStations().get(workStationIndex).getProduct().removeFirst();
            input.getColdAndHotSubCar().setProduct(product);
            endWorkstationStatus.add(WAIT_TRANSPORT);
            input.getWorkStations().get(workStationIndex).setStatus(WAIT_TRANSPORT);
        }else if (input.getWorkStations().get(workStationIndex).getProduct().size() == 1){
            Product product = input.getWorkStations().get(workStationIndex).getProduct().getFirst().productDeepCopy();
            input.getWorkStations().get(workStationIndex).setProduct(new ArrayList<>());
            input.getColdAndHotSubCar().setProduct(product);
            endWorkstationStatus.add(FREE);
            input.getWorkStations().get(workStationIndex).setStatus(FREE);
            Product castingProduct = tools.createCastingProduct(product.getOrderProductType());
            //加入铸件队列
            downProducts.add(castingProduct);
        }else {
            log.info("ERROR: 开箱区工岗的产品列表出错");
            endWorkstationStatus.add(FREE);
        }
        input.getColdAndHotSubCar().setSubCarState(endSubCarStatus_3);

        Product product = input.getColdAndHotSubCar().getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100720", "ELCAHOBSCGIWSP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_2 - executionTime_3, input.getColdAndHotSubCar().getId(), startSubCarStatus_1,true,
                input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), endSubCarStatus_1, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getColdAndHotFerry().getId(), startFerryStatus_1,true,
                input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_1, true,
                input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null, null,
                null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, startWorkstationStatus, pathIds,
                pathStatusEnums, pathStatusEnums, input.getColdAndHotSubCar().getId(), SUB_CAR, RUNNING, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getColdAndHotSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100721", "ELCAHOBSCIWSP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_3, input.getColdAndHotSubCar().getId(), endSubCarStatus_1,true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_2, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getColdAndHotFerry().getId(), endFerryStatus_1,true,
                input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_1, true, input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(),
                null,null, null, null, null, null,
                input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, endWorkstationStatus, pathIds, pathStatusEnums, pathStatusEnums,
                input.getColdAndHotSubCar().getId(), SUB_CAR, RISING, null, product);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getColdAndHotSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100722", "FLCAHOBSCGBTFWS", executionTime_3, input.getTotalTime() - executionTime_3, input.getTotalTime(),
                input.getColdAndHotSubCar().getId(), endSubCarStatus_2,true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_3,
                true, input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), input.getColdAndHotFerry().getId(), endFerryStatus_1,true,
                input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_2, true,
                input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null, null,
                null, null, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getColdAndHotSubCar().getId(), SUB_CAR, RUNNING, input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), product);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getColdAndHotSubCarInGanttIndex()).getEvents().add(event_3);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink ColdAndHotOpenBoxEmptyGoWorkStationEventCreate(AfterClosingBoxAllLink simulationOutput, int workStationIndex) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                input.getColdAndHotFerry().getLocationCoordinate().getX()) / input.getColdAndHotFerry().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX(),
                input.getColdAndHotFerry().getLocationCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getColdAndHotSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getColdAndHotFerry().getFerryState());
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
        pathIds.add(input.getColdAndHotOpenBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);


        //生成事件
        Event event = allTool.createEvent("100723", "EGCAHOBSCGIWSP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getColdAndHotSubCar().getId(), startSubCarStatus_1,false, null, endSubCarStatus, true,
                aimCoordinate, input.getColdAndHotFerry().getId(), startFerryStatus_1,false, null,
                endFerryStatus, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(),startWorkstationStatus, endWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdAndHotFerry().getId(), FERRY, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getColdAndHotSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getColdAndHotSubCar().setSubCarState(endSubCarStatus);
        input.getColdAndHotFerry().setFerryState(endFerryStatus);
        input.getColdAndHotSubCar().setLocationCoordinate(aimCoordinate);
        input.getColdAndHotFerry().setLocationCoordinate(aimCoordinate);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink ColdAndHotOpenBoxEmptyGoColdAndHotPosotionEventCreate(AfterClosingBoxAllLink simulationOutput) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getColdAndHotOpenBoxPosition().getCoordinate().getX() -
                input.getColdAndHotFerry().getLocationCoordinate().getX()) / input.getColdAndHotFerry().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getColdAndHotOpenBoxPosition().getCoordinate().getX(), input.getColdAndHotFerry().getLocationCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getColdAndHotSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getColdAndHotFerry().getFerryState());
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
        pathIds.add(input.getColdAndHotOpenBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);


        //生成事件
        Event event = allTool.createEvent("100724", "EOBSCGICAHMP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getColdAndHotSubCar().getId(), startSubCarStatus_1,false, null, endSubCarStatus, true,
                aimCoordinate, input.getColdAndHotFerry().getId(), startFerryStatus_1, false, null,
                endFerryStatus, true, aimCoordinate, null,null, null,
                input.getColdAndHotOpenBoxPosition().getId(), startPositionStatus, endPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdAndHotFerry().getId(), FERRY, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getColdAndHotSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getColdAndHotSubCar().setSubCarState(endSubCarStatus);
        input.getColdAndHotFerry().setFerryState(endFerryStatus);
        input.getColdAndHotSubCar().setLocationCoordinate(aimCoordinate);
        input.getColdAndHotFerry().setLocationCoordinate(aimCoordinate);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink ColdAndHotOpenBoxFullGoWorkStationEventCreate(AfterClosingBoxAllLink simulationOutput, int workStationIndex) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                input.getColdAndHotFerry().getLocationCoordinate().getX()) / input.getColdAndHotFerry().getFullSpeed();
        double executionTime_2 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getColdAndHotFerry().getLocationCoordinate().getY()) / input.getColdAndHotSubCar().getFullSpeed();
        double executionTime_3 = input.getColdAndHotSubCar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getColdAndHotFerry().getLocationCoordinate().getY()) / input.getColdAndHotSubCar().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX(),
                input.getColdAndHotFerry().getLocationCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getColdAndHotSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getColdAndHotFerry().getFerryState());
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
        endWorkstationStatus.add(WORKING);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getColdAndHotOpenBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getColdAndHotSubCar().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.DOING_UNBOXING);

        //生成事件
        Event event_1 = allTool.createEvent("100725", "FGCAHOBSCGIWSP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getColdAndHotSubCar().getId(), startSubCarStatus_1,false,
                null, startSubCarStatus_1, true, aimCoordinate, input.getColdAndHotFerry().getId(), startFerryStatus_1,false,
                null, endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, startWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdAndHotFerry().getId(), FERRY, RUNNING, aimCoordinate, product);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getColdAndHotSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100726", "FGCAHOBSCIWSP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_3 - executionTime_4, input.getColdAndHotSubCar().getId(), startSubCarStatus_1,true, aimCoordinate, endSubCarStatus_1,
                true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getColdAndHotFerry().getId(), startFerryStatus_1,true, aimCoordinate,
                endFerryStatus_2, true, aimCoordinate, null,null, null, null, null,
                null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, startWorkstationStatus, pathIds, pathStatusEnums,
                pathStatusEnums, input.getColdAndHotSubCar().getId(), SUB_CAR, RUNNING, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), product);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getColdAndHotSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100727", "FGCAHOBSCULIWSP", executionTime_3, input.getTotalTime() - executionTime_3 - executionTime_4, input.getTotalTime() - executionTime_4,
                input.getColdAndHotSubCar().getId(), endSubCarStatus_1,true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_2, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getColdAndHotFerry().getId(), endFerryStatus_2,true, aimCoordinate,
                endFerryStatus_2, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdAndHotSubCar().getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getColdAndHotSubCarInGanttIndex()).getEvents().add(event_3);

        //生成事件
        Event event_4 = allTool.createEvent("100728", "FGCAHOBSCBFIWS", executionTime_4, input.getTotalTime() - executionTime_4, input.getTotalTime(),
                input.getColdAndHotSubCar().getId(), endSubCarStatus_2,true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(),
                endSubCarStatus_3, true, aimCoordinate, input.getColdAndHotFerry().getId(), endFerryStatus_2,true, aimCoordinate,
                endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdAndHotSubCar().getId(), SUB_CAR, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getColdAndHotSubCarInGanttIndex()).getEvents().add(event_4);

        //改变车辆状态和时间
        input.getColdAndHotSubCar().setSubCarState(endSubCarStatus_3);
        input.getColdAndHotFerry().setFerryState(endFerryStatus_1);
        input.getColdAndHotSubCar().setLocationCoordinate(aimCoordinate);
        input.getColdAndHotFerry().setLocationCoordinate(aimCoordinate);
        input.getWorkStations().get(workStationIndex).setStatus(WORKING);

        List<Product> products = new ArrayList<>(product.getProducts());
        input.getWorkStations().get(workStationIndex).setProduct(products);
        input.getColdAndHotSubCar().setProduct(null);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink ColdAndHotOpenBoxFullUnLoadPositionEventCreate(AfterClosingBoxAllLink simulationOutput) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getColdAndHotFerry().getLocationCoordinate().getY() -
                input.getColdAndHotRefluxPosition().getCoordinate().getY()) / input.getColdAndHotSubCar().getFullSpeed();
        double executionTime_2 = input.getColdAndHotSubCar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getColdAndHotFerry().getLocationCoordinate().getY() -
                input.getColdAndHotRefluxPosition().getCoordinate().getY()) / input.getColdAndHotSubCar().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getColdAndHotRefluxPosition().getCoordinate().getX(), input.getColdAndHotFerry().getLocationCoordinate().getY());
        Coordinate positionCoordinate = input.getColdAndHotRefluxPosition().getCoordinate().coordinateDeepCopy();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getColdAndHotSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getColdAndHotFerry().getFerryState());
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
        pathIds.add(input.getColdAndHotOpenBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getColdAndHotSubCar().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.REFLUXING);
        product.setProductFinish(ProductFinishEnum.FINISH_UNBOXING);

        //生成事件
        Event event_1 = allTool.createEvent("100729", "FGCAHOBSCILP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_2 - executionTime_3, input.getColdAndHotSubCar().getId(), startSubCarStatus_1,true, aimCoordinate,
                endSubCarStatus_1, true, positionCoordinate, input.getColdAndHotFerry().getId(), startFerryStatus_1, true, aimCoordinate,
                endFerryStatus_2, true, aimCoordinate, null,null, null, input.getColdAndHotRefluxPosition().getId(),
                startPositionStatus, startPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getColdAndHotSubCar().getId(), SUB_CAR, RUNNING, positionCoordinate, product);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getColdAndHotSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100730", "FGCAHOBSCULILP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_3, input.getColdAndHotSubCar().getId(), endSubCarStatus_1,true, positionCoordinate,
                endSubCarStatus_2, true, positionCoordinate, input.getColdAndHotFerry().getId(), endFerryStatus_2, true, aimCoordinate,
                endFerryStatus_2, true, aimCoordinate, null,null, null, input.getColdAndHotRefluxPosition().getId(),
                startPositionStatus, endPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getColdAndHotSubCar().getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getColdAndHotSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100732", "FGCAHOBSCBFILP", executionTime_3, input.getTotalTime() - executionTime_3, input.getTotalTime(),
                input.getColdAndHotSubCar().getId(), endSubCarStatus_2,true, positionCoordinate, endSubCarStatus_3, true,
                aimCoordinate, input.getColdAndHotFerry().getId(), endFerryStatus_2,true, aimCoordinate,
                endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdAndHotSubCar().getId(), SUB_CAR, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getColdAndHotSubCarInGanttIndex()).getEvents().add(event_3);

        //改变车辆状态和时间
        input.getColdAndHotSubCar().setSubCarState(endSubCarStatus_3);
        input.getColdAndHotFerry().setFerryState(endFerryStatus_1);
        input.getColdAndHotSubCar().setLocationCoordinate(aimCoordinate);
        input.getColdAndHotFerry().setLocationCoordinate(aimCoordinate);
        input.getColdAndHotRefluxPosition().setStatus(OCCUPIED);

        input.getColdAndHotRefluxPosition().setProduct(product);
        input.getColdAndHotSubCar().setProduct(null);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink ColdAndHotOpenBoxFullGoPositionEventCreate(AfterClosingBoxAllLink simulationOutput) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getColdAndHotFerry().getLocationCoordinate().getX() -
                input.getColdAndHotRefluxPosition().getCoordinate().getX()) / input.getColdAndHotFerry().getFullSpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getColdAndHotRefluxPosition().getCoordinate().getX(),
                input.getColdAndHotFerry().getLocationCoordinate().getY());
        //判断母车所在工岗的位置标识
        int workStationIndex = tools.judgeColdAndHotFerryInWhichWorkstation(input.getColdAndHotFerry(), input.getWorkStations(), input.getOpenBoxIndexInput());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getColdAndHotSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getColdAndHotFerry().getFerryState());
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
        pathIds.add(input.getColdAndHotOpenBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);


        //生成事件
        Event event = allTool.createEvent("100733", "FGCAHOBSCGILP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getColdAndHotSubCar().getId(), startSubCarStatus_1,false, null, endSubCarStatus, true,
                aimCoordinate, input.getColdAndHotFerry().getId(), startFerryStatus_1,false, null,
                endFerryStatus, true, aimCoordinate, null,null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getColdAndHotFerry().getId(), FERRY, RUNNING, aimCoordinate, input.getColdAndHotSubCar().getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getColdAndHotSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getColdAndHotSubCar().setSubCarState(endSubCarStatus);
        input.getColdAndHotFerry().setFerryState(endFerryStatus);
        input.getColdAndHotSubCar().setLocationCoordinate(aimCoordinate);
        input.getColdAndHotFerry().setLocationCoordinate(aimCoordinate);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink ColdAndHotOpenBoxEmptyLoadInWorkStationEventCreate_2(AfterClosingBoxAllLink simulationOutput, int workStationIndex,
                                                                                       Deque<Product> upProducts, Deque<Integer> upWorkStations) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getColdAndHotFerry().getLocationCoordinate().getY()) / input.getColdAndHotSubCar().getEmptySpeed();
        double executionTime_2 = input.getColdAndHotSubCar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getColdAndHotFerry().getLocationCoordinate().getY()) / input.getColdAndHotSubCar().getFullSpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getColdAndHotSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getColdAndHotFerry().getFerryState());
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
        //改变车辆状态和时间
        input.getColdAndHotSubCar().setSubCarState(endSubCarStatus_3);
        input.getColdAndHotFerry().setFerryState(endFerryStatus_2);
        //工岗点状态列表
        List<WorkstationStatusEnum> startWorkstationStatus = new ArrayList<>();
        List<WorkstationStatusEnum> endWorkstationStatus = new ArrayList<>();
        startWorkstationStatus.add(WAIT_TRANSPORT);
        endWorkstationStatus.add(FREE);

        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getColdAndHotOpenBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        //获取工岗中的产品
        Product product = input.getWorkStations().get(workStationIndex).getProduct().getFirst().productDeepCopy();
        input.getWorkStations().get(workStationIndex).getProduct().removeFirst();
        product.setProducts(new ArrayList<>());
        product.getProducts().addAll(input.getWorkStations().get(workStationIndex).getProduct());
        product.setRefluxFlag(true);
        //生成铸件产品
        Product castingProduct = tools.createCastingProduct(product.getOrderProductType());
        //加入铸件队列
        upProducts.add(castingProduct);
        upWorkStations.add(workStationIndex);

        input.getColdAndHotSubCar().setSubCarState(endSubCarStatus_3);
        input.getColdAndHotSubCar().setProduct(product);
        input.getWorkStations().get(workStationIndex).setStatus(FREE);

        //生成事件
        Event event_1 = allTool.createEvent("100720", "ELCAHOBSCGIWSP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_2 - executionTime_3, input.getColdAndHotSubCar().getId(), startSubCarStatus_1,true,
                input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), endSubCarStatus_1, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getColdAndHotFerry().getId(), startFerryStatus_1,true,
                input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_1, true,
                input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null, null,
                null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, startWorkstationStatus, pathIds,
                pathStatusEnums, pathStatusEnums, input.getColdAndHotSubCar().getId(), SUB_CAR, RUNNING, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(),
                null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getColdAndHotSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100721", "ELCAHOBSCIWSP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_3, input.getColdAndHotSubCar().getId(), endSubCarStatus_1,true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_2, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getColdAndHotFerry().getId(), endFerryStatus_1,true,
                input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_1, true,
                input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null, null,
                null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, endWorkstationStatus, pathIds,
                pathStatusEnums, pathStatusEnums, input.getColdAndHotSubCar().getId(), SUB_CAR, RISING, null, product.productDeepCopy());

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getColdAndHotSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100722", "FLCAHOBSCGBTFWS", executionTime_3, input.getTotalTime() - executionTime_3, input.getTotalTime(),
                input.getColdAndHotSubCar().getId(), endSubCarStatus_2,true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_3,
                true, input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), input.getColdAndHotFerry().getId(), endFerryStatus_1,true,
                input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_2, true,
                input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null, null,
                null, null, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getColdAndHotSubCar().getId(), SUB_CAR, RUNNING, input.getColdAndHotFerry().getLocationCoordinate().coordinateDeepCopy(), product.productDeepCopy());

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getColdAndHotSubCarInGanttIndex()).getEvents().add(event_3);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }
}
