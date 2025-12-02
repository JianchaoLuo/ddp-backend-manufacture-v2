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
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.OutBoundSimulationInput;
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
 * @Created: 2024/8/1 3:02
 */
@NoArgsConstructor
@Slf4j
public class HotOpenBoxEventFlow {
    /**
     * 仿真总工具类
     */
    private SimulationTool allTool = new SimulationTool();
    /**
     * 开箱区的仿真工具类
     */
    private OpenBoxSimulationTools tools = new OpenBoxSimulationTools();

    public AfterClosingBoxAllLink HotEmptyLoadInPositionEventCreate(AfterClosingBoxAllLink simulationOutput) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getHotOpenBoxPosition().getCoordinate().getY() -
                input.getHotFerry().getLocationCoordinate().getY()) / input.getHotSubCar().getEmptySpeed();
        double executionTime_2 = input.getHotSubCar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getHotOpenBoxPosition().getCoordinate().getY() -
                input.getHotFerry().getLocationCoordinate().getY()) / input.getHotSubCar().getFullSpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getHotSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getHotFerry().getFerryState());
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
        pathIds.add(input.getHotOpenBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getHotOpenBoxPosition().getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100701", "ELOBSCGIHOBP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_2 - executionTime_3, input.getHotSubCar().getId(), startSubCarStatus_1, true,
                input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(), endSubCarStatus_1, true, input.getHotOpenBoxPosition().getCoordinate(),
                input.getHotFerry().getId(), startFerryStatus_1,true, input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_1,
                true, input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null,
                input.getHotOpenBoxPosition().getId(), startPositionStatus, startPositionStatus, null,null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotSubCar().getId(), SUB_CAR, RUNNING, input.getHotOpenBoxPosition().getCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getHotSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100702", "ELOBSCLIHOBP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_3, input.getHotSubCar().getId(), endSubCarStatus_1, true,
                input.getHotOpenBoxPosition().getCoordinate(), endSubCarStatus_2, true, input.getHotOpenBoxPosition().getCoordinate(), input.getHotFerry().getId(),
                endFerryStatus_1,true, input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_1, true,
                input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null,
                input.getHotOpenBoxPosition().getId(), startPositionStatus, endPositionStatus, null,null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotSubCar().getId(), SUB_CAR, RISING, null, product);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getHotSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100703", "FLOBSCGBTFIH", executionTime_3, input.getTotalTime() - executionTime_3,
                input.getTotalTime(), input.getHotSubCar().getId(), endSubCarStatus_2,true, input.getHotOpenBoxPosition().getCoordinate(),
                endSubCarStatus_3, true, input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(), input.getHotFerry().getId(),
                endFerryStatus_1,true, input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_2, true,
                input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(), null,null, null, null,
                null, null, null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getHotSubCar().getId(), SUB_CAR, RUNNING, input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(), product);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getHotSubCarInGanttIndex()).getEvents().add(event_3);

        //改变车辆状态和时间
        input.getHotSubCar().setSubCarState(endSubCarStatus_3);
        input.getHotFerry().setFerryState(endFerryStatus_2);
        input.getHotOpenBoxPosition().setStatus(UNOCCUPIED);


        input.getHotSubCar().setProduct(product);
        input.getHotOpenBoxPosition().setProduct(null);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink HotOpenBoxEmptyLoadInWorkStationEventCreate(AfterClosingBoxAllLink simulationOutput, int workStationIndex, Deque<Product> downProducts) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getHotFerry().getLocationCoordinate().getY()) / input.getHotSubCar().getEmptySpeed();
        double executionTime_2 = input.getHotSubCar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getHotFerry().getLocationCoordinate().getY()) / input.getHotSubCar().getFullSpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getHotSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getHotFerry().getFerryState());
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
        input.getHotSubCar().setSubCarState(endSubCarStatus_3);
        input.getHotFerry().setFerryState(endFerryStatus_2);
        //工岗点状态列表
        List<WorkstationStatusEnum> startWorkstationStatus = new ArrayList<>();
        List<WorkstationStatusEnum> endWorkstationStatus = new ArrayList<>();
        startWorkstationStatus.add(WAIT_TRANSPORT);

        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getHotOpenBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        //根据工岗的产品判断工岗的结束状态
        if (input.getWorkStations().get(workStationIndex).getProduct().size() > 1){
            Product product = input.getWorkStations().get(workStationIndex).getProduct().getFirst().productDeepCopy();
            input.getWorkStations().get(workStationIndex).getProduct().removeFirst();
            input.getHotSubCar().setProduct(product);
            endWorkstationStatus.add(WAIT_TRANSPORT);
            input.getWorkStations().get(workStationIndex).setStatus(WAIT_TRANSPORT);
        }else if (input.getWorkStations().get(workStationIndex).getProduct().size() == 1){
            Product product = input.getWorkStations().get(workStationIndex).getProduct().getFirst().productDeepCopy();
            input.getWorkStations().get(workStationIndex).setProduct(new ArrayList<>());
            input.getHotSubCar().setProduct(product);
            endWorkstationStatus.add(FREE);
            input.getWorkStations().get(workStationIndex).setStatus(FREE);
            Product castingProduct = tools.createCastingProduct(product.getOrderProductType());
            //加入铸件队列
            downProducts.add(castingProduct);
        }else {
            log.info("ERROR: 开箱区工岗的产品列表出错");
            endWorkstationStatus.add(FREE);
        }
        input.getHotSubCar().setSubCarState(endSubCarStatus_3);

        Product product = input.getHotSubCar().getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("100704", "ELOBSCGIWSP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_2 - executionTime_3, input.getHotSubCar().getId(), startSubCarStatus_1,true,
                input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(), endSubCarStatus_1, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getHotFerry().getId(), startFerryStatus_1,true,
                input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_1, true, input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(),
                null,null, null, null, null, null,
                input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getHotSubCar().getId(), SUB_CAR, RUNNING, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getHotSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100705", "ELOBSCIWSP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_3, input.getHotSubCar().getId(), endSubCarStatus_1,true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_2, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getHotFerry().getId(), endFerryStatus_1,true,
                input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_1, true, input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(),
                null,null, null, null, null, null,
                input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, endWorkstationStatus, pathIds, pathStatusEnums, pathStatusEnums,
                input.getHotSubCar().getId(), SUB_CAR, RISING, null, product);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getHotSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100706", "FLHOBSCGBTFWS", executionTime_3, input.getTotalTime() - executionTime_3, input.getTotalTime(),
                input.getHotSubCar().getId(), endSubCarStatus_2,true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_3,
                true, input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(), input.getHotFerry().getId(), endFerryStatus_1,true,
                input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_2, true, input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(),
                null,null, null, null, null, null, null, null,
                null, pathIds, pathStatusEnums, pathStatusEnums, input.getHotSubCar().getId(), SUB_CAR, RUNNING,
                input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(), product);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getHotSubCarInGanttIndex()).getEvents().add(event_3);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink HotOpenBoxEmptyGoWorkStationEventCreate(AfterClosingBoxAllLink simulationOutput, int workStationIndex) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                input.getHotFerry().getLocationCoordinate().getX()) / input.getHotFerry().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX(),
                input.getHotFerry().getLocationCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getHotSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getHotFerry().getFerryState());
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
        pathIds.add(input.getHotOpenBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);


        //生成事件
        Event event = allTool.createEvent("100707", "EGOBSCGIWSP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getHotSubCar().getId(), startSubCarStatus_1,false, null, endSubCarStatus, true,
                aimCoordinate, input.getHotFerry().getId(), startFerryStatus_1,false, null,
                endFerryStatus, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, endWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotFerry().getId(), FERRY, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getHotSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getHotSubCar().setSubCarState(endSubCarStatus);
        input.getHotFerry().setFerryState(endFerryStatus);
        input.getHotSubCar().setLocationCoordinate(aimCoordinate);
        input.getHotFerry().setLocationCoordinate(aimCoordinate);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink HotOpenBoxEmptyGoHotPosotionEventCreate(AfterClosingBoxAllLink simulationOutput) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getHotOpenBoxPosition().getCoordinate().getX() -
                input.getHotFerry().getLocationCoordinate().getX()) / input.getHotFerry().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getHotOpenBoxPosition().getCoordinate().getX(), input.getHotFerry().getLocationCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getHotSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getHotFerry().getFerryState());
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
        pathIds.add(input.getHotOpenBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);


        //生成事件
        Event event = allTool.createEvent("100708", "EOBSCGIHMP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getHotSubCar().getId(), startSubCarStatus_1,false, null, endSubCarStatus, true,
                aimCoordinate, input.getHotFerry().getId(), startFerryStatus_1, false, null,
                endFerryStatus, true, aimCoordinate, null,null, null,
                input.getHotOpenBoxPosition().getId(), startPositionStatus, endPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotFerry().getId(), FERRY, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getHotSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getHotSubCar().setSubCarState(endSubCarStatus);
        input.getHotFerry().setFerryState(endFerryStatus);
        input.getHotSubCar().setLocationCoordinate(aimCoordinate);
        input.getHotFerry().setLocationCoordinate(aimCoordinate);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink HotOpenBoxFullGoWorkStationEventCreate(AfterClosingBoxAllLink simulationOutput, int workStationIndex) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                input.getHotFerry().getLocationCoordinate().getX()) / input.getHotFerry().getFullSpeed();
        double executionTime_2 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getHotFerry().getLocationCoordinate().getY()) / input.getHotSubCar().getFullSpeed();
        double executionTime_3 = input.getHotSubCar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getHotFerry().getLocationCoordinate().getY()) / input.getHotSubCar().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX(),
                input.getHotFerry().getLocationCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getHotSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getHotFerry().getFerryState());
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
        pathIds.add(input.getHotOpenBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getHotSubCar().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.DOING_UNBOXING);

        //生成事件
        Event event_1 = allTool.createEvent("100709", "FGHOBSCGIWSP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getHotSubCar().getId(), startSubCarStatus_1,false,
                null, startSubCarStatus_1, true, aimCoordinate, input.getHotFerry().getId(), startFerryStatus_1,false,
                null, endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, startWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotFerry().getId(), FERRY, RUNNING, aimCoordinate, product);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getHotSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100710", "FGHOBSCIWSP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getTotalTime() - executionTime_3 - executionTime_4, input.getHotSubCar().getId(), startSubCarStatus_1,true, aimCoordinate, endSubCarStatus_1,
                true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getHotFerry().getId(), startFerryStatus_1,true, aimCoordinate,
                endFerryStatus_2, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, startWorkstationStatus,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotSubCar().getId(), SUB_CAR, RUNNING, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), product);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getHotSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100711", "FGHOBSCULIWSP", executionTime_3, input.getTotalTime() - executionTime_3 - executionTime_4, input.getTotalTime() - executionTime_4,
                input.getHotSubCar().getId(), endSubCarStatus_1,true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_2, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getHotFerry().getId(), endFerryStatus_2,true, aimCoordinate,
                endFerryStatus_2, true, aimCoordinate, null,null, null,
                null, null, null, input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotSubCar().getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getHotSubCarInGanttIndex()).getEvents().add(event_3);

        //生成事件
        Event event_4 = allTool.createEvent("100712", "FGHOBSCBFIWS", executionTime_4, input.getTotalTime() - executionTime_4, input.getTotalTime(),
                input.getHotSubCar().getId(), endSubCarStatus_2,true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(),
                endSubCarStatus_3, true, aimCoordinate, input.getHotFerry().getId(), endFerryStatus_2,true, aimCoordinate,
                endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotSubCar().getId(), SUB_CAR, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getHotSubCarInGanttIndex()).getEvents().add(event_4);

        //改变车辆状态和时间
        input.getHotSubCar().setSubCarState(endSubCarStatus_3);
        input.getHotFerry().setFerryState(endFerryStatus_1);
        input.getHotSubCar().setLocationCoordinate(aimCoordinate);
        input.getHotFerry().setLocationCoordinate(aimCoordinate);
        input.getWorkStations().get(workStationIndex).setStatus(WORKING);

        List<Product> products = new ArrayList<>(product.getProducts());
        input.getWorkStations().get(workStationIndex).setProduct(products);
        input.getHotSubCar().setProduct(null);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink HotOpenBoxFullUnLoadPositionEventCreate(AfterClosingBoxAllLink simulationOutput) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        OutBoundSimulationInput outBoundInput = simulationOutput.getOutBoundInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getHotFerry().getLocationCoordinate().getY() -
                outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getY()) /
                input.getHotSubCar().getFullSpeed();
        double executionTime_2 = input.getHotSubCar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getHotFerry().getLocationCoordinate().getY() -
                outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getY()) /
                input.getHotSubCar().getEmptySpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getX(),
                input.getHotFerry().getLocationCoordinate().getY());
        Coordinate positionCoordinate = outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().coordinateDeepCopy();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getHotSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getHotFerry().getFerryState());
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
        pathIds.add(input.getHotOpenBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getHotSubCar().getProduct().productDeepCopy();
        product.setRefluxFlag(true);
        product.setProductAfoot(ProductAfootEnum.REFLUXING);
        product.setProductFinish(ProductFinishEnum.FINISH_UNBOXING);

        //生成事件
        Event event_1 = allTool.createEvent("100713", "FGHOBSCILP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_2 - executionTime_3, input.getHotSubCar().getId(), startSubCarStatus_1,true, aimCoordinate,
                endSubCarStatus_1, true, positionCoordinate, input.getHotFerry().getId(), startFerryStatus_1,
                true, aimCoordinate, endFerryStatus_2, true, aimCoordinate, null,null, null,
                outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getId(), startPositionStatus, startPositionStatus,
                null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getHotSubCar().getId(), SUB_CAR, RUNNING, positionCoordinate, product);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getHotSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100714", "FGHOBSCULILP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_3, input.getHotSubCar().getId(), endSubCarStatus_1,true, positionCoordinate,
                endSubCarStatus_2, true, positionCoordinate, input.getHotFerry().getId(), endFerryStatus_2,
                true, aimCoordinate, endFerryStatus_2, true, aimCoordinate, null,null, null,
                outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getId(), startPositionStatus, endPositionStatus,
                null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getHotSubCar().getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getHotSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100715", "FGHOBSCBFILP", executionTime_3, input.getTotalTime() - executionTime_3, input.getTotalTime(),
                input.getHotSubCar().getId(), endSubCarStatus_2,true, positionCoordinate, endSubCarStatus_3, true,
                aimCoordinate, input.getHotFerry().getId(), endFerryStatus_2,true, aimCoordinate,
                endFerryStatus_1, true, aimCoordinate, null,null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotSubCar().getId(), SUB_CAR, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getHotSubCarInGanttIndex()).getEvents().add(event_3);

        //改变车辆状态和时间
        input.getHotSubCar().setSubCarState(endSubCarStatus_3);
        input.getHotFerry().setFerryState(endFerryStatus_1);
        input.getHotSubCar().setLocationCoordinate(aimCoordinate);
        input.getHotFerry().setLocationCoordinate(aimCoordinate);
        outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).setStatus(OCCUPIED);


        outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).setProduct(product);
        input.getHotSubCar().setProduct(null);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink HotOpenBoxFullGoPositionEventCreate(AfterClosingBoxAllLink simulationOutput) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        OutBoundSimulationInput outBoundInput = simulationOutput.getOutBoundInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getHotFerry().getLocationCoordinate().getX() -
                outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getX()) /
                input.getHotFerry().getFullSpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getX(),
                input.getHotFerry().getLocationCoordinate().getY());
        //判断母车所在工岗的位置标识
        int workStationIndex = tools.judgeHotFerryInWhichWorkstation(input.getHotFerry(), input.getWorkStations(), input.getOpenBoxIndexInput());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getHotSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getHotFerry().getFerryState());
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
        pathIds.add(input.getHotOpenBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);


        //生成事件
        Event event = allTool.createEvent("100716", "FGHOBSCGILP", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                input.getHotSubCar().getId(), startSubCarStatus_1,false, null, endSubCarStatus, true,
                aimCoordinate, input.getHotFerry().getId(), startFerryStatus_1,false, null,
                endFerryStatus, true, aimCoordinate, null,null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getHotFerry().getId(), FERRY, RUNNING, aimCoordinate, input.getHotSubCar().getProduct().productDeepCopy());

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getHotSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getHotSubCar().setSubCarState(endSubCarStatus);
        input.getHotFerry().setFerryState(endFerryStatus);
        input.getHotSubCar().setLocationCoordinate(aimCoordinate);
        input.getHotFerry().setLocationCoordinate(aimCoordinate);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }

    public AfterClosingBoxAllLink HotOpenBoxEmptyLoadInWorkStationEventCreate_2(AfterClosingBoxAllLink simulationOutput, int workStationIndex,
                                                                                Deque<Product> downProducts, Deque<Integer> downWorkStationIndex) {
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getHotFerry().getLocationCoordinate().getY()) / input.getHotSubCar().getEmptySpeed();
        double executionTime_2 = input.getHotSubCar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                input.getHotFerry().getLocationCoordinate().getY()) / input.getHotSubCar().getFullSpeed();
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(input.getHotSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(input.getHotFerry().getFerryState());
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
        input.getHotSubCar().setSubCarState(endSubCarStatus_3);
        input.getHotFerry().setFerryState(endFerryStatus_2);
        //工岗点状态列表
        List<WorkstationStatusEnum> startWorkstationStatus = new ArrayList<>();
        List<WorkstationStatusEnum> endWorkstationStatus = new ArrayList<>();
        startWorkstationStatus.add(WAIT_TRANSPORT);
        endWorkstationStatus.add(FREE);

        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getHotOpenBoxPath().getId());
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
        downProducts.add(castingProduct);
        downWorkStationIndex.add(workStationIndex);

        input.getHotSubCar().setSubCarState(endSubCarStatus_3);
        input.getHotSubCar().setProduct(product);
        input.getWorkStations().get(workStationIndex).setStatus(FREE);
        input.getHotSubCar().setSubCarState(endSubCarStatus_3);

        //生成事件
        Event event_1 = allTool.createEvent("100704", "ELOBSCGIWSP", executionTime_1, input.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_2 - executionTime_3, input.getHotSubCar().getId(), startSubCarStatus_1,true,
                input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(), endSubCarStatus_1, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getHotFerry().getId(), startFerryStatus_1,true,
                input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_1, true, input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(),
                null,null, null, null, null, null,
                input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getHotSubCar().getId(), SUB_CAR, RUNNING, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), null);

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getHotSubCarInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("100705", "ELOBSCIWSP", executionTime_2, input.getTotalTime() - executionTime_2 - executionTime_3,
                input.getTotalTime() - executionTime_3, input.getHotSubCar().getId(), endSubCarStatus_1,true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_2, true,
                input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), input.getHotFerry().getId(), endFerryStatus_1,true,
                input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_1, true, input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(),
                null,null, null, null, null, null,
                input.getWorkStations().get(workStationIndex).getId(), startWorkstationStatus, endWorkstationStatus, pathIds, pathStatusEnums, pathStatusEnums,
                input.getHotSubCar().getId(), SUB_CAR, RISING, null, product.productDeepCopy());

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getHotSubCarInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("100706", "FLHOBSCGBTFWS", executionTime_3, input.getTotalTime() - executionTime_3, input.getTotalTime(),
                input.getHotSubCar().getId(), endSubCarStatus_2,true, input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate(), endSubCarStatus_3,
                true, input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(), input.getHotFerry().getId(), endFerryStatus_1,true,
                input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(), endFerryStatus_2, true, input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(),
                null,null, null, null, null, null, null, null,
                null, pathIds, pathStatusEnums, pathStatusEnums, input.getHotSubCar().getId(), SUB_CAR, RUNNING,
                input.getHotFerry().getLocationCoordinate().coordinateDeepCopy(), product.productDeepCopy());

        //事件加入甘特图
        simulationOutput.getOpenBoxInput().getGanttChart().getEventLinkGantts().get(input.getOpenBoxIndexInput().getHotSubCarInGanttIndex()).getEvents().add(event_3);

        simulationOutput.getOpenBoxInput().setInput(input);

        return simulationOutput;
    }
}

