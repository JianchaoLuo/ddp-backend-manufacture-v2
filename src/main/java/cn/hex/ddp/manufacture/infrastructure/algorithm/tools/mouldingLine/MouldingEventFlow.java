package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.mouldingLine;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductAfootEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductFinishEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
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
 * @Created: 2024/7/9 15:11
 */
public class MouldingEventFlow {
    /**
     * 仿真总工具类
     */
    private SimulationTool allTool = new SimulationTool();
    /**
     * 空载前往回流等待点时事件流生成
     * @param input
     * @return
     */
    public OutBoundMouldingAndClosingBox EmptyGoRefluxWaitPositionEventCreate(OutBoundMouldingAndClosingBox input, Coordinate refluxWaitCoordinate){
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getLocationCoordinate().getY() - refluxWaitCoordinate.getY())
                / input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getEmptySpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getMouldingInput().getInput().getMoldRefluxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = allTool.createEvent("100201", "ELHACMRSCGRWP", executionTime, input.getMouldingInput().getInput().getTotalTime() - executionTime,
                input.getMouldingInput().getInput().getTotalTime(), input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(), startSubCarStatus,
                false, null, endSubCarStatus, true, refluxWaitCoordinate, null, null, false,
                null, null, false, null, null, null, null,
                null, null, null, null, null, null, pathIds, pathStatusEnums,
                pathStatusEnums, input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(), SUB_CAR, RUNNING, refluxWaitCoordinate, null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getMoldRefluxSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().setSubCarState(endSubCarStatus);
        input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().setLocationCoordinate(refluxWaitCoordinate);

        OutBoundMouldingAndClosingBox outBoundMouldingAndClosingBox = input;

        return outBoundMouldingAndClosingBox;
    }

    /**
     * 空载前往回流等待点时事件流生成
     * @param input
     * @return
     */
    public OutBoundMouldingAndClosingBox EmptyCoreMoldOpeningPositionEventCreate(OutBoundMouldingAndClosingBox input){
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getLocationCoordinate().getY() -
                input.getMouldingInput().getInput().getCoreMoldOpeningPosition().getCoordinate().getY()) / input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getEmptySpeed();
        double executionTime_2 = input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getSubCarState());
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
        pathIds.add(input.getMouldingInput().getInput().getMoldRefluxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getMouldingInput().getInput().getCoreMoldOpeningPosition().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.REFLUXING);
        product.setProductFinish(ProductFinishEnum.FINISH_MOLD_OPENING);

        //生成事件
        Event event = allTool.createEvent("100202", "ELHACMRSCLGCMRP", executionTime_1, input.getMouldingInput().getInput().getTotalTime() - executionTime_1 - executionTime_2,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_2, input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(), startSubCarStatus,
                false, null, startSubCarStatus, true, input.getMouldingInput().getInput().getCoreMoldOpeningPosition().getCoordinate(),
                null, null, false, null, null, false, null, null,
                null, null, input.getMouldingInput().getInput().getCoreMoldOpeningPosition().getId(), startPositionStatus, startPositionStatus, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(),
                SUB_CAR, RUNNING, input.getMouldingInput().getInput().getCoreMoldOpeningPosition().getCoordinate(), null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getMoldRefluxSubCarInGanttIndex()).getEvents().add(event);

        //生成事件
        Event event1 = allTool.createEvent("100203", "ELHACMRSCLICMRP", executionTime_2, input.getMouldingInput().getInput().getTotalTime() - executionTime_2,
                input.getMouldingInput().getInput().getTotalTime(), input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(), startSubCarStatus,
                true, input.getMouldingInput().getInput().getCoreMoldOpeningPosition().getCoordinate(), endSubCarStatus, true, input.getMouldingInput().getInput().getCoreMoldOpeningPosition().getCoordinate(),
                null, null, false, null, null, false, null, null,
                null, null, input.getMouldingInput().getInput().getCoreMoldOpeningPosition().getId(), startPositionStatus, endPositionStatus, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(),
                SUB_CAR, RISING, null, product);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getMoldRefluxSubCarInGanttIndex()).getEvents().add(event1);

        //改变车辆状态和时间
        input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().setSubCarState(endSubCarStatus);
        input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().setLocationCoordinate(input.getMouldingInput().getInput().getCoreMoldOpeningPosition().getCoordinate());
        input.getMouldingInput().getInput().getCoreMoldOpeningPosition().setStatus(UNOCCUPIED);

        input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().setProduct(product);
        input.getMouldingInput().getInput().getCoreMoldOpeningPosition().setProduct(null);

        return input;
    }

    /**
     * 空载前往回流等待点时事件流生成
     * @param input
     * @return
     */
    public OutBoundMouldingAndClosingBox EmptyHotMoldOpeningPositionEventCreate(OutBoundMouldingAndClosingBox input){
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getLocationCoordinate().getY() -
                input.getMouldingInput().getInput().getHotMoldOpeningPosition().getCoordinate().getY()) / input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getEmptySpeed();
        double executionTime_2 = input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getSubCarState());
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
        pathIds.add(input.getMouldingInput().getInput().getMoldRefluxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getMouldingInput().getInput().getHotMoldOpeningPosition().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.REFLUXING);
        product.setProductFinish(ProductFinishEnum.FINISH_MOLD_OPENING);

        //生成事件
        Event event = allTool.createEvent("100204", "ELHACMRSCLGHMRP", executionTime_1, input.getMouldingInput().getInput().getTotalTime() - executionTime_1 - executionTime_2,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_2, input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(), startSubCarStatus,
                false, null, startSubCarStatus, true, input.getMouldingInput().getInput().getHotMoldOpeningPosition().getCoordinate(),
                null, null, false, null, null, false, null, null,
                null, null, input.getMouldingInput().getInput().getHotMoldOpeningPosition().getId(), startPositionStatus, startPositionStatus, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(),
                SUB_CAR, RUNNING, input.getMouldingInput().getInput().getHotMoldOpeningPosition().getCoordinate(), null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getMoldRefluxSubCarInGanttIndex()).getEvents().add(event);

        //生成事件
        Event event1 = allTool.createEvent("100205", "ELHACMRSCLIHMRP", executionTime_2, input.getMouldingInput().getInput().getTotalTime() - executionTime_2,
                input.getMouldingInput().getInput().getTotalTime(), input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(), startSubCarStatus,
                true, input.getMouldingInput().getInput().getHotMoldOpeningPosition().getCoordinate(), endSubCarStatus, true, input.getMouldingInput().getInput().getHotMoldOpeningPosition().getCoordinate(),
                null, null, false, null, null, false, null, null,
                null, null, input.getMouldingInput().getInput().getHotMoldOpeningPosition().getId(), startPositionStatus, endPositionStatus, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(),
                SUB_CAR, RISING, null, product);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getMoldRefluxSubCarInGanttIndex()).getEvents().add(event1);

        //改变车辆状态和时间
        input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().setSubCarState(endSubCarStatus);
        input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().setLocationCoordinate(input.getMouldingInput().getInput().getHotMoldOpeningPosition().getCoordinate());
        input.getMouldingInput().getInput().getHotMoldOpeningPosition().setStatus(UNOCCUPIED);

        input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().setProduct(product);
        input.getMouldingInput().getInput().getHotMoldOpeningPosition().setProduct(null);

        OutBoundMouldingAndClosingBox outBoundMouldingAndClosingBox = input;

        return outBoundMouldingAndClosingBox;
    }

    /**
     * 空载前往回流等待点时事件流生成
     * @param input
     * @return
     */
    public OutBoundMouldingAndClosingBox FullMoldRefluxLayPositionEventCreate(OutBoundMouldingAndClosingBox input, Coordinate waitLayCoordinate){
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getOutBoundInput().getInput().getPositions().get(input.getOutBoundInput().getInput().getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate().getY()
                - input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getLocationCoordinate().getY()) / input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getFullSpeed();
        double executionTime_2 = input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getTopRodRaiseOrFallTime();
        double executionTime_3 = Math.abs(input.getOutBoundInput().getInput().getPositions().get(input.getOutBoundInput().getInput().getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate().getY() -
                waitLayCoordinate.getY()) / input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getEmptySpeed();
                //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getSubCarState());
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
        pathIds.add(input.getMouldingInput().getInput().getMoldRefluxPath().getId());
        pathIds.add(input.getOutBoundInput().getInput().getPaths().get(input.getOutBoundInput().getInput().getOutBoundIndexInput().getMoldRefluxPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getProduct().productDeepCopy();

        //生成事件
        Event event = allTool.createEvent("100206", "FLHACMRSCGMRP", executionTime_1, input.getMouldingInput().getInput().getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_2 - executionTime_3, input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(),
                startSubCarStatus, false, null, startSubCarStatus, true,
                input.getOutBoundInput().getInput().getPositions().get(input.getOutBoundInput().getInput().getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate(),
                null, null, false, null, null, false, null, null,
                null, null, input.getOutBoundInput().getInput().getPositions().get(input.getOutBoundInput().getInput().getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getId(),
                startPositionStatus, startPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(), SUB_CAR, RUNNING,
                input.getOutBoundInput().getInput().getPositions().get(input.getOutBoundInput().getInput().getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate(), product);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getMoldRefluxSubCarInGanttIndex()).getEvents().add(event);

        //生成事件
        Event event1 = allTool.createEvent("100207", "FLHACMRSCULIMRP", executionTime_2, input.getMouldingInput().getInput().getTotalTime() - executionTime_2 - executionTime_3,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_3, input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(), startSubCarStatus,
                true, input.getOutBoundInput().getInput().getPositions().get(input.getOutBoundInput().getInput().getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate(),
                endSubCarStatus, true, input.getOutBoundInput().getInput().getPositions().get(input.getOutBoundInput().getInput().getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate(),
                null, null, false, null, null, false, null, null, null,
                null, input.getOutBoundInput().getInput().getPositions().get(input.getOutBoundInput().getInput().getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getId(),
                startPositionStatus, endPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getMoldRefluxSubCarInGanttIndex()).getEvents().add(event1);

        //生成事件
        Event event2 = allTool.createEvent("100208", "ELHACMRSCGRWP", executionTime_3, input.getMouldingInput().getInput().getTotalTime() - executionTime_3,
                input.getMouldingInput().getInput().getTotalTime(), input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(), endSubCarStatus, false,
                null, endSubCarStatus, true, waitLayCoordinate, null, null, false, null,
                null, false, null, null, null, null, null, null,
                null, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(), SUB_CAR, RUNNING, waitLayCoordinate, null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getMoldRefluxSubCarInGanttIndex()).getEvents().add(event2);

        //改变车辆状态和时间
        input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().setSubCarState(endSubCarStatus);
        input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().setLocationCoordinate(waitLayCoordinate);
        input.getOutBoundInput().getInput().getPositions().get(input.getOutBoundInput().getInput().getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).setStatus(OCCUPIED);

        input.getOutBoundInput().getInput().getPositions().get(input.getOutBoundInput().getInput().getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).setProduct(product);
        input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().setProduct(null);

        OutBoundMouldingAndClosingBox outBoundMouldingAndClosingBox = input;

        return outBoundMouldingAndClosingBox;
    }

    /**
     * 空载前往回流等待点时事件流生成
     * @param input
     * @return
     */
    public OutBoundMouldingAndClosingBox FullGoRefluxWaitPositionEventCreate(OutBoundMouldingAndClosingBox input, Coordinate waitLayCoordinate){
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getLocationCoordinate().getY() - waitLayCoordinate.getY())
                / input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getFullSpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getMouldingInput().getInput().getMoldRefluxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = allTool.createEvent("100209", "FLHACMRSCGRWP", executionTime, input.getMouldingInput().getInput().getTotalTime() - executionTime,
                input.getMouldingInput().getInput().getTotalTime(), input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(), startSubCarStatus,
                false, null, endSubCarStatus, true, waitLayCoordinate, null, null,
                false, null, null, false, null, null, null,
                null, null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(), SUB_CAR, RUNNING, waitLayCoordinate,
                input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getProduct().productDeepCopy());

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getMoldRefluxSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().setSubCarState(endSubCarStatus);
        input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().setLocationCoordinate(waitLayCoordinate);

        return input;
    }

    /**
     * 冷砂回流子车空载前往回流等待点时事件流生成
     * @param input
     * @return
     */
    public OutBoundMouldingAndClosingBox EmptyColdGoRefluxWaitPositionEventCreate(OutBoundMouldingAndClosingBox input, Coordinate coldMoldRefluxWaitCoordinate){
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getMouldingInput().getInput().getColdMoldRefluxSubCar().getLocationCoordinate().getY() - coldMoldRefluxWaitCoordinate.getY())
                / input.getMouldingInput().getInput().getColdMoldRefluxSubCar().getEmptySpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getMouldingInput().getInput().getColdMoldRefluxSubCar().getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getMouldingInput().getInput().getColdMoldRefluxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = allTool.createEvent("100210", "ELCMRSCGRWP", executionTime, input.getMouldingInput().getInput().getTotalTime() - executionTime,
                input.getMouldingInput().getInput().getTotalTime(), input.getMouldingInput().getInput().getColdMoldRefluxSubCar().getId(), startSubCarStatus,
                false, null, endSubCarStatus, true, coldMoldRefluxWaitCoordinate, null, null,
                false, null, null, false, null, null, null,
                null, null, null, null, null, null, null, pathIds,
                pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getColdMoldRefluxSubCar().getId(), SUB_CAR, RUNNING, coldMoldRefluxWaitCoordinate, null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getColdMoldRefluxSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getMouldingInput().getInput().getColdMoldRefluxSubCar().setSubCarState(endSubCarStatus);
        input.getMouldingInput().getInput().getColdMoldRefluxSubCar().setLocationCoordinate(coldMoldRefluxWaitCoordinate);

        OutBoundMouldingAndClosingBox outBoundMouldingAndClosingBox = input;

        return outBoundMouldingAndClosingBox;
    }

    /**
     * 冷砂回流子车空载前往回流等待点时事件流生成
     * @param input
     * @return
     */
    public OutBoundMouldingAndClosingBox EmptyColdGoRefluxPositionEventCreate(OutBoundMouldingAndClosingBox input){
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getMouldingInput().getInput().getColdMoldRefluxSubCar().getLocationCoordinate().getY() - input.getMouldingInput().getInput().getColdMoldOpeningPosition().getCoordinate().getY())
                / input.getMouldingInput().getInput().getColdMoldRefluxSubCar().getEmptySpeed();
        double executionTime_2 = input.getMouldingInput().getInput().getColdMoldRefluxSubCar().getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getMouldingInput().getInput().getColdMoldRefluxSubCar().getSubCarState());
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
        pathIds.add(input.getMouldingInput().getInput().getColdMoldRefluxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getMouldingInput().getInput().getColdMoldOpeningPosition().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.REFLUXING);
        product.setProductFinish(ProductFinishEnum.FINISH_MOLD_OPENING);

        //生成事件
        Event event1 = allTool.createEvent("100211", "ELCMRSCGRWP", executionTime_1, input.getMouldingInput().getInput().getTotalTime() - executionTime_1 - executionTime_2,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_2, input.getMouldingInput().getInput().getColdMoldRefluxSubCar().getId(), startSubCarStatus,
                false, null, startSubCarStatus, true, input.getMouldingInput().getInput().getColdMoldOpeningPosition().getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getMouldingInput().getInput().getColdMoldOpeningPosition().getId(), startPositionStatus, startPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getColdMoldRefluxSubCar().getId(), SUB_CAR, RUNNING,
                input.getMouldingInput().getInput().getColdMoldOpeningPosition().getCoordinate(), null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getColdMoldRefluxSubCarInGanttIndex()).getEvents().add(event1);

        //生成事件
        Event event2 = allTool.createEvent("100212", "ELCMRSCLIRWP", executionTime_2, input.getMouldingInput().getInput().getTotalTime() - executionTime_2,
                input.getMouldingInput().getInput().getTotalTime(), input.getMouldingInput().getInput().getColdMoldRefluxSubCar().getId(), startSubCarStatus,
                true, input.getMouldingInput().getInput().getColdMoldOpeningPosition().getCoordinate(), endSubCarStatus, true, input.getMouldingInput().getInput().getColdMoldOpeningPosition().getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getMouldingInput().getInput().getColdMoldOpeningPosition().getId(), startPositionStatus, endPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getColdMoldRefluxSubCar().getId(), SUB_CAR, RISING, null, product);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getColdMoldRefluxSubCarInGanttIndex()).getEvents().add(event2);

        //改变车辆状态和时间
        input.getMouldingInput().getInput().getColdMoldRefluxSubCar().setSubCarState(endSubCarStatus);
        input.getMouldingInput().getInput().getColdMoldRefluxSubCar().setLocationCoordinate(input.getMouldingInput().getInput().getColdMoldOpeningPosition().getCoordinate());
        input.getMouldingInput().getInput().getColdMoldOpeningPosition().setStatus(UNOCCUPIED);

        input.getMouldingInput().getInput().getColdMoldRefluxSubCar().setProduct(product);
        input.getMouldingInput().getInput().getColdMoldOpeningPosition().setProduct(null);

        OutBoundMouldingAndClosingBox outBoundMouldingAndClosingBox = input;

        return outBoundMouldingAndClosingBox;
    }

    /**
     * 冷砂回流子车空载前往回流等待点时事件流生成
     * @param input
     * @return
     */
    public OutBoundMouldingAndClosingBox FullColdGoRefluxWaitPositionEventCreate(OutBoundMouldingAndClosingBox input, Coordinate coldMoldRefluxWaitCoordinate){
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getLocationCoordinate().getY() -
                coldMoldRefluxWaitCoordinate.getY()) / input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getFullSpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getMouldingInput().getInput().getMoldRefluxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = allTool.createEvent("100213", "FLCMRSCGRWP", executionTime, input.getMouldingInput().getInput().getTotalTime() - executionTime,
                input.getMouldingInput().getInput().getTotalTime(), input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(), startSubCarStatus, false,
                null, endSubCarStatus, true, coldMoldRefluxWaitCoordinate, null, null, false,
                null, null, false, null, null, null, null,
                null, null, null, null, null, null, pathIds, pathStatusEnums,
                pathStatusEnums, input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(), SUB_CAR, RUNNING, coldMoldRefluxWaitCoordinate,
                input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getProduct().productDeepCopy());

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getMoldRefluxSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().setSubCarState(endSubCarStatus);
        input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().setLocationCoordinate(coldMoldRefluxWaitCoordinate);

        OutBoundMouldingAndClosingBox outBoundMouldingAndClosingBox = input;

        return outBoundMouldingAndClosingBox;
    }

    /**
     * 冷砂回流子车空载前往回流等待点时事件流生成
     * @param input
     * @return
     */
    public OutBoundMouldingAndClosingBox FullColdGoRefluxPositionEventCreate(OutBoundMouldingAndClosingBox input){
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getLocationCoordinate().getY() -
                input.getOutBoundInput().getInput().getPositions().get(input.getOutBoundInput().getInput().getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate().getY())
                / input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getFullSpeed();
        double executionTime_2 = input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getSubCarState());
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
        pathIds.add(input.getMouldingInput().getInput().getMoldRefluxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getProduct().productDeepCopy();

        //生成事件
        Event event1 = allTool.createEvent("100214", "FLCMRSCGRWP", executionTime_1, input.getMouldingInput().getInput().getTotalTime() - executionTime_1 - executionTime_2,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_2, input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(), startSubCarStatus,
                false, null, startSubCarStatus, true,
                input.getOutBoundInput().getInput().getPositions().get(input.getOutBoundInput().getInput().getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate(),
                null, null, false, null, null, false, null, null, null,
                null, input.getOutBoundInput().getInput().getPositions().get(input.getOutBoundInput().getInput().getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getId(),
                startPositionStatus, startPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(), SUB_CAR, RUNNING,
                input.getOutBoundInput().getInput().getPositions().get(input.getOutBoundInput().getInput().getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate(), product);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getMoldRefluxSubCarInGanttIndex()).getEvents().add(event1);

        //生成事件
        Event event2 = allTool.createEvent("100215", "FLCMRSCLIRWP", executionTime_2, input.getMouldingInput().getInput().getTotalTime() - executionTime_2,
                input.getMouldingInput().getInput().getTotalTime(), input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(), startSubCarStatus, true,
                input.getOutBoundInput().getInput().getPositions().get(input.getOutBoundInput().getInput().getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate(), endSubCarStatus,
                true, input.getOutBoundInput().getInput().getPositions().get(input.getOutBoundInput().getInput().getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate(),
                null, null, false, null, null, false, null, null, null, null,
                input.getOutBoundInput().getInput().getPositions().get(input.getOutBoundInput().getInput().getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getId(),
                startPositionStatus, endPositionStatus, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getMoldRefluxSubCarInGanttIndex()).getEvents().add(event2);

        //改变车辆状态和时间
        input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().setSubCarState(endSubCarStatus);
        input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().setLocationCoordinate(input.getOutBoundInput().
                getInput().getPositions().get(input.getOutBoundInput().getInput().getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate());
        input.getOutBoundInput().getInput().getPositions().get(input.getOutBoundInput().getInput().getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).setStatus(OCCUPIED);

        input.getOutBoundInput().getInput().getPositions().get(input.getOutBoundInput().getInput().getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).setProduct(product);
        input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().setProduct(null);

        return input;
    }

    /**
     * 冷砂回流子车空载前往回流等待点时事件流生成
     * @param input
     * @return
     */
    public OutBoundMouldingAndClosingBox EmptyHotSprayGoWaitPositionEventCreate(OutBoundMouldingAndClosingBox input, Coordinate hotSprayWaitCoordinate){
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().getLocationCoordinate().getY() - hotSprayWaitCoordinate.getY())
                / input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().getEmptySpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getMouldingInput().getInput().getHotSpraySandBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = allTool.createEvent("100216", "ELHSSSCGRWP", executionTime, input.getMouldingInput().getInput().getTotalTime() - executionTime,
                input.getMouldingInput().getInput().getTotalTime(), input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().getId(), startSubCarStatus,
                false, null, endSubCarStatus, true, hotSprayWaitCoordinate, null, null, false,
                null, null, false, null, null, null, null,
                null, null, null, null, null, null, pathIds, pathStatusEnums,
                pathStatusEnums, input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().getId(), SUB_CAR, RUNNING, hotSprayWaitCoordinate, null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getHotSpraySandBoxSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().setSubCarState(endSubCarStatus);
        input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().setLocationCoordinate(hotSprayWaitCoordinate);

        OutBoundMouldingAndClosingBox outBoundMouldingAndClosingBox = input;

        return outBoundMouldingAndClosingBox;
    }

    /**
     * 冷砂回流子车空载前往回流等待点时事件流生成
     * @param input
     * @return
     */
    public OutBoundMouldingAndClosingBox EmptyHotSprayGoPositionEventCreate(OutBoundMouldingAndClosingBox input){
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().getLocationCoordinate().getY() -
                input.getMouldingInput().getInput().getHotSandBoxOpeningPosition_2().getCoordinate().getY())
                / input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().getFullSpeed();
        double executionTime_2 = input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().getSubCarState());
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
        pathIds.add(input.getMouldingInput().getInput().getHotSpraySandBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getMouldingInput().getInput().getHotSandBoxOpeningPosition_2().getProduct().productDeepCopy();

        //生成事件
        Event event1 = allTool.createEvent("100217", "ELHSSSCGSOP", executionTime_1, input.getMouldingInput().getInput().getTotalTime() - executionTime_1 - executionTime_2,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_2, input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().getId(), startSubCarStatus,
                false, null, startSubCarStatus, true, input.getMouldingInput().getInput().getHotSandBoxOpeningPosition_2().getCoordinate(),
                null, null, false, null, null, false, null, null,
                null, null, input.getMouldingInput().getInput().getHotSandBoxOpeningPosition_2().getId(), startPositionStatus, startPositionStatus,
                null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().getId(), SUB_CAR, RUNNING,
                input.getMouldingInput().getInput().getHotSandBoxOpeningPosition_2().getCoordinate(), null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getHotSpraySandBoxSubCarInGanttIndex()).getEvents().add(event1);

        //生成事件
        Event event2 = allTool.createEvent("100218", "ELHSSSCLISOP", executionTime_2, input.getMouldingInput().getInput().getTotalTime() - executionTime_2,
                input.getMouldingInput().getInput().getTotalTime(), input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().getId(), startSubCarStatus,
                true, input.getMouldingInput().getInput().getHotSandBoxOpeningPosition_2().getCoordinate(), endSubCarStatus, true,
                input.getMouldingInput().getInput().getHotSandBoxOpeningPosition_2().getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getMouldingInput().getInput().getHotSandBoxOpeningPosition_2().getId(), startPositionStatus, endPositionStatus, null, null,
                null, pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().getId(), SUB_CAR, RISING,
                null, product);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getHotSpraySandBoxSubCarInGanttIndex()).getEvents().add(event2);

        //改变车辆状态和时间
        input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().setSubCarState(endSubCarStatus);
        input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().setLocationCoordinate(input.getMouldingInput().getInput().getHotSandBoxOpeningPosition_2().getCoordinate());
        input.getMouldingInput().getInput().getHotSandBoxOpeningPosition_2().setStatus(UNOCCUPIED);

        input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().setProduct(product);
        input.getMouldingInput().getInput().getHotSandBoxOpeningPosition_2().setProduct(null);

        OutBoundMouldingAndClosingBox outBoundMouldingAndClosingBox = input;

        return outBoundMouldingAndClosingBox;
    }

    /**
     * 冷砂回流子车空载前往回流等待点时事件流生成
     * @param input
     * @return
     */
    public OutBoundMouldingAndClosingBox FullHoSprayGoDownPositionEventCreate(OutBoundMouldingAndClosingBox input){
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().getLocationCoordinate().getY() -
                input.getMouldingInput().getInput().getHotSandBoxSprayDownPosition().getCoordinate().getY())
                / input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().getFullSpeed();
        double executionTime_2 = input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().getSubCarState());
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
        pathIds.add(input.getMouldingInput().getInput().getHotSpraySandBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().getProduct().productDeepCopy();

        //生成事件
        Event event1 = allTool.createEvent("100219", "FLHSSSCGSDP", executionTime_1, input.getMouldingInput().getInput().getTotalTime() - executionTime_1 - executionTime_2,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_2, input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().getId(), startSubCarStatus,
                false, null, startSubCarStatus, true, input.getMouldingInput().getInput().getHotSandBoxSprayDownPosition().getCoordinate(),
                null, null, false, null, null, false, null, null,
                null, null, input.getMouldingInput().getInput().getHotSandBoxSprayDownPosition().getId(), startPositionStatus, startPositionStatus,
                null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().getId(), SUB_CAR, RUNNING,
                input.getMouldingInput().getInput().getHotSandBoxSprayDownPosition().getCoordinate(), product);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getHotSpraySandBoxSubCarInGanttIndex()).getEvents().add(event1);

        //生成事件
        Event event2 = allTool.createEvent("100220", "FLHSSSCLISDP", executionTime_2, input.getMouldingInput().getInput().getTotalTime() - executionTime_2,
                input.getMouldingInput().getInput().getTotalTime(), input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().getId(), startSubCarStatus,
                true, input.getMouldingInput().getInput().getHotSandBoxSprayDownPosition().getCoordinate(), endSubCarStatus, true,
                input.getMouldingInput().getInput().getHotSandBoxSprayDownPosition().getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getMouldingInput().getInput().getHotSandBoxSprayDownPosition().getId(), startPositionStatus, endPositionStatus, null, null,
                null, pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().getId(), SUB_CAR, LANDING,
                null, null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getHotSpraySandBoxSubCarInGanttIndex()).getEvents().add(event2);

        //改变车辆状态和时间
        input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().setSubCarState(endSubCarStatus);
        input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().setLocationCoordinate(input.getMouldingInput().getInput().getHotSandBoxSprayDownPosition().getCoordinate());
        input.getMouldingInput().getInput().getHotSandBoxSprayDownPosition().setStatus(OCCUPIED);

        input.getMouldingInput().getInput().getHotSandBoxSprayDownPosition().setProduct(product);
        input.getMouldingInput().getInput().getHotSpraySandBoxSubCar().setProduct(null);

        OutBoundMouldingAndClosingBox outBoundMouldingAndClosingBox = input;

        return outBoundMouldingAndClosingBox;
    }

    /**
     * 冷砂回流子车空载前往回流等待点时事件流生成
     * @param input
     * @return
     */
    public OutBoundMouldingAndClosingBox EmptyColdSprayGoWaitPositionEventCreate(OutBoundMouldingAndClosingBox input, Coordinate coldSprayWaitCoordinate){
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getMouldingInput().getInput().getColdSandBoxSubCar().getLocationCoordinate().getY() - coldSprayWaitCoordinate.getY())
                / input.getMouldingInput().getInput().getColdSandBoxSubCar().getEmptySpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getMouldingInput().getInput().getColdSandBoxSubCar().getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(EMPTY_LOAD);
        endSubCarStatus.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getMouldingInput().getInput().getColdSandBoxGoSprayPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = allTool.createEvent("100221", "ELCSSSCGRWP", executionTime, input.getMouldingInput().getInput().getTotalTime() - executionTime,
                input.getMouldingInput().getInput().getTotalTime(), input.getMouldingInput().getInput().getColdSandBoxSubCar().getId(), startSubCarStatus,
                false, null, endSubCarStatus, true, coldSprayWaitCoordinate, null, null,
                false, null, null, false, null, null, null,
                null, null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getColdSandBoxSubCar().getId(), SUB_CAR, RUNNING, coldSprayWaitCoordinate, null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getColdSandBoxSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getMouldingInput().getInput().getColdSandBoxSubCar().setSubCarState(endSubCarStatus);
        input.getMouldingInput().getInput().getColdSandBoxSubCar().setLocationCoordinate(coldSprayWaitCoordinate);

        OutBoundMouldingAndClosingBox outBoundMouldingAndClosingBox = input;

        return outBoundMouldingAndClosingBox;
    }

    /**
     * 冷砂回流子车空载前往回流等待点时事件流生成
     * @param input
     * @return
     */
    public OutBoundMouldingAndClosingBox EmptyColdSprayGoPositionEventCreate(OutBoundMouldingAndClosingBox input){
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getMouldingInput().getInput().getColdSandBoxSubCar().getLocationCoordinate().getY() -
                input.getMouldingInput().getInput().getColdSandBoxOpeningPosition().getCoordinate().getY())
                / input.getMouldingInput().getInput().getColdSandBoxSubCar().getEmptySpeed();
        double executionTime_2 = input.getMouldingInput().getInput().getColdSandBoxSubCar().getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getMouldingInput().getInput().getColdSandBoxSubCar().getSubCarState());
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
        pathIds.add(input.getMouldingInput().getInput().getColdSandBoxGoSprayPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getMouldingInput().getInput().getColdSandBoxOpeningPosition().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.GOING_SPRAY);
        product.setProductFinish(ProductFinishEnum.FINISH_MOLD_OPENING);

        //生成事件
        Event event1 = allTool.createEvent("100222", "ELCSSSCGSOP", executionTime_1, input.getMouldingInput().getInput().getTotalTime() - executionTime_1 - executionTime_2,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_2, input.getMouldingInput().getInput().getColdSandBoxSubCar().getId(), startSubCarStatus,
                false, null, startSubCarStatus, true, input.getMouldingInput().getInput().getColdSandBoxOpeningPosition().getCoordinate(),
                null, null, false, null, null, false, null, null,
                null, null, input.getMouldingInput().getInput().getColdSandBoxOpeningPosition().getId(), startPositionStatus, startPositionStatus,
                null, null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getColdSandBoxSubCar().getId(),
                SUB_CAR, RUNNING, input.getMouldingInput().getInput().getColdSandBoxOpeningPosition().getCoordinate(), null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getColdSandBoxSubCarInGanttIndex()).getEvents().add(event1);

        //生成事件
        Event event2 = allTool.createEvent("100223", "ELCSSSCLISOP", executionTime_2, input.getMouldingInput().getInput().getTotalTime() - executionTime_2,
                input.getMouldingInput().getInput().getTotalTime(), input.getMouldingInput().getInput().getColdSandBoxSubCar().getId(), startSubCarStatus, true,
                input.getMouldingInput().getInput().getColdSandBoxOpeningPosition().getCoordinate(), endSubCarStatus, true,
                input.getMouldingInput().getInput().getColdSandBoxOpeningPosition().getCoordinate(), null, null, false, null,
                null, false, null, null, null, null,
                input.getMouldingInput().getInput().getColdSandBoxOpeningPosition().getId(), startPositionStatus, endPositionStatus, null, null,
                null, pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getColdSandBoxSubCar().getId(), SUB_CAR, RISING,
                null, product);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getColdSandBoxSubCarInGanttIndex()).getEvents().add(event2);

        //改变车辆状态和时间
        input.getMouldingInput().getInput().getColdSandBoxSubCar().setSubCarState(endSubCarStatus);
        input.getMouldingInput().getInput().getColdSandBoxSubCar().setLocationCoordinate(input.getMouldingInput().getInput().getColdSandBoxOpeningPosition().getCoordinate());
        input.getMouldingInput().getInput().getColdSandBoxOpeningPosition().setStatus(UNOCCUPIED);

        input.getMouldingInput().getInput().getColdSandBoxSubCar().setProduct(product);
        input.getMouldingInput().getInput().getColdSandBoxOpeningPosition().setProduct(null);

        OutBoundMouldingAndClosingBox outBoundMouldingAndClosingBox = input;

        return outBoundMouldingAndClosingBox;
    }

    /**
     * 冷砂回流子车空载前往回流等待点时事件流生成
     * @param input
     * @return
     */
    public OutBoundMouldingAndClosingBox FullColdSprayGoDownPositionEventCreate(OutBoundMouldingAndClosingBox input){
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getMouldingInput().getInput().getColdSandBoxSubCar().getLocationCoordinate().getY() -
                input.getMouldingInput().getInput().getColdSandBoxSprayPosition_2().getCoordinate().getY())
                / input.getMouldingInput().getInput().getColdSandBoxSubCar().getFullSpeed();
        double executionTime_2 = input.getMouldingInput().getInput().getColdSandBoxSubCar().getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getMouldingInput().getInput().getColdSandBoxSubCar().getSubCarState());
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
        pathIds.add(input.getMouldingInput().getInput().getColdSandBoxGoSprayPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getMouldingInput().getInput().getColdSandBoxSubCar().getProduct().productDeepCopy();

        //生成事件
        Event event1 = allTool.createEvent("100224", "ELCSSSCGSDP", executionTime_1, input.getMouldingInput().getInput().getTotalTime() - executionTime_1 - executionTime_2,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_2, input.getMouldingInput().getInput().getColdSandBoxSubCar().getId(), startSubCarStatus,
                false, null, startSubCarStatus, true, input.getMouldingInput().getInput().getColdSandBoxSprayPosition_2().getCoordinate(),
                null, null, false, null, null, false, null, null,
                null, null, input.getMouldingInput().getInput().getColdSandBoxSprayPosition_2().getId(), startPositionStatus, startPositionStatus,
                null, null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getColdSandBoxSubCar().getId(),
                SUB_CAR, RUNNING, input.getMouldingInput().getInput().getColdSandBoxSprayPosition_2().getCoordinate(), product);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getColdSandBoxSubCarInGanttIndex()).getEvents().add(event1);

        //生成事件
        Event event2 = allTool.createEvent("100225", "ELCSSSCLISDP", executionTime_2, input.getMouldingInput().getInput().getTotalTime() - executionTime_2,
                input.getMouldingInput().getInput().getTotalTime(), input.getMouldingInput().getInput().getColdSandBoxSubCar().getId(), startSubCarStatus,
                true, input.getMouldingInput().getInput().getColdSandBoxSprayPosition_2().getCoordinate(), endSubCarStatus, true,
                input.getMouldingInput().getInput().getColdSandBoxSprayPosition_2().getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getMouldingInput().getInput().getColdSandBoxSprayPosition_2().getId(), startPositionStatus, endPositionStatus, null, null,
                null, pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getColdSandBoxSubCar().getId(), SUB_CAR, LANDING,
                null, null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getColdSandBoxSubCarInGanttIndex()).getEvents().add(event2);

        //改变车辆状态和时间
        input.getMouldingInput().getInput().getColdSandBoxSubCar().setSubCarState(endSubCarStatus);
        input.getMouldingInput().getInput().getColdSandBoxSubCar().setLocationCoordinate(input.getMouldingInput().getInput().getColdSandBoxSprayPosition_2().getCoordinate());
        input.getMouldingInput().getInput().getColdSandBoxSprayPosition_2().setStatus(OCCUPIED);

        input.getMouldingInput().getInput().getColdSandBoxSprayPosition_2().setProduct(product);
        input.getMouldingInput().getInput().getColdSandBoxSubCar().setProduct(null);

        return input;
    }

    /**
     * 冷砂回流子车空载前往回流等待点时事件流生成
     * @param input
     * @return
     */
    public OutBoundMouldingAndClosingBox FullColdSprayGoPositionEventCreate(OutBoundMouldingAndClosingBox input){
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getMouldingInput().getInput().getColdSandBoxSubCar().getLocationCoordinate().getY() -
                input.getMouldingInput().getInput().getColdSandBoxSprayPosition_1().getCoordinate().getY())
                / input.getMouldingInput().getInput().getColdSandBoxSubCar().getFullSpeed();
        double executionTime_2 = input.getMouldingInput().getInput().getColdSandBoxSubCar().getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getMouldingInput().getInput().getColdSandBoxSubCar().getSubCarState());
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
        pathIds.add(input.getMouldingInput().getInput().getColdSandBoxGoSprayPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getMouldingInput().getInput().getColdSandBoxSubCar().getProduct().productDeepCopy();

        //生成事件
        Event event1 = allTool.createEvent("100226", "ELCSSSCGSUP", executionTime_1, input.getMouldingInput().getInput().getTotalTime() -
                        executionTime_1 - executionTime_2, input.getMouldingInput().getInput().getTotalTime() - executionTime_2,
                input.getMouldingInput().getInput().getColdSandBoxSubCar().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, input.getMouldingInput().getInput().getColdSandBoxSprayPosition_1().getCoordinate(), null, null,
                false, null, null, false, null, null, null,
                null, input.getMouldingInput().getInput().getColdSandBoxSprayPosition_1().getId(), startPositionStatus, startPositionStatus, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getColdSandBoxSubCar().getId(),
                SUB_CAR, RUNNING, input.getMouldingInput().getInput().getColdSandBoxSprayPosition_1().getCoordinate(), product);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getColdSandBoxSubCarInGanttIndex()).getEvents().add(event1);

        //生成事件
        Event event2 = allTool.createEvent("100227", "ELCSSSCLISUP", executionTime_2, input.getMouldingInput().getInput().getTotalTime() - executionTime_2,
                input.getMouldingInput().getInput().getTotalTime(), input.getMouldingInput().getInput().getColdSandBoxSubCar().getId(), startSubCarStatus,
                true, input.getMouldingInput().getInput().getColdSandBoxSprayPosition_1().getCoordinate(), endSubCarStatus, true,
                input.getMouldingInput().getInput().getColdSandBoxSprayPosition_1().getCoordinate(), null, null, false,
                null, null, false, null, null, null, null,
                input.getMouldingInput().getInput().getColdSandBoxSprayPosition_1().getId(), startPositionStatus, endPositionStatus, null, null,
                null, pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getColdSandBoxSubCar().getId(), SUB_CAR, LANDING,
                null, null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getColdSandBoxSubCarInGanttIndex()).getEvents().add(event2);

        //改变车辆状态和时间
        input.getMouldingInput().getInput().getColdSandBoxSubCar().setSubCarState(endSubCarStatus);
        input.getMouldingInput().getInput().getColdSandBoxSubCar().setLocationCoordinate(input.getMouldingInput().getInput().getColdSandBoxSprayPosition_1().getCoordinate());
        input.getMouldingInput().getInput().getColdSandBoxSprayPosition_1().setStatus(OCCUPIED);

        input.getMouldingInput().getInput().getColdSandBoxSprayPosition_1().setProduct(product);
        input.getMouldingInput().getInput().getColdSandBoxSubCar().setProduct(null);

        return input;
    }

    /**
     * 空载前往回流等待点时事件流生成
     * @param input
     * @return
     */
    public OutBoundMouldingAndClosingBox EmptyColdMoldOpeningPositionEventCreate(OutBoundMouldingAndClosingBox input) {
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getLocationCoordinate().getY() -
                input.getMouldingInput().getInput().getColdMoldOpeningPosition().getCoordinate().getY()) /
                input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getEmptySpeed();
        double executionTime_2 = input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getTopRodRaiseOrFallTime();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getSubCarState());
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
        pathIds.add(input.getMouldingInput().getInput().getMoldRefluxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getMouldingInput().getInput().getColdMoldOpeningPosition().getProduct().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.REFLUXING);
        product.setProductFinish(ProductFinishEnum.FINISH_MOLD_OPENING);

        //生成事件
        Event event = allTool.createEvent("100254", "ELHACMRSCLGHMRP", executionTime_1, input.getMouldingInput().getInput().getTotalTime() - executionTime_1 - executionTime_2,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_2, input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(), startSubCarStatus,
                false, null, startSubCarStatus, true, input.getMouldingInput().getInput().getColdMoldOpeningPosition().getCoordinate(),
                null, null, false, null, null, false, null, null,
                null, null, input.getMouldingInput().getInput().getColdMoldOpeningPosition().getId(), startPositionStatus, startPositionStatus, null,
                null, null, pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(),
                SUB_CAR, RUNNING, input.getMouldingInput().getInput().getColdMoldOpeningPosition().getCoordinate(), null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getMoldRefluxSubCarInGanttIndex()).getEvents().add(event);

        //生成事件
        Event event1 = allTool.createEvent("100255", "ELHACMRSCLIHMRP", executionTime_2, input.getMouldingInput().getInput().getTotalTime() - executionTime_2,
                input.getMouldingInput().getInput().getTotalTime(), input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(), startSubCarStatus,
                true, input.getMouldingInput().getInput().getColdMoldOpeningPosition().getCoordinate(), endSubCarStatus, true,
                input.getMouldingInput().getInput().getColdMoldOpeningPosition().getCoordinate(), null, null, false, null,
                null, false, null, null, null, null,
                input.getMouldingInput().getInput().getColdMoldOpeningPosition().getId(), startPositionStatus, endPositionStatus, null, null,
                null, pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().getId(),
                SUB_CAR, RISING, null, product);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getMoldRefluxSubCarInGanttIndex()).getEvents().add(event1);

        //改变车辆状态和时间
        input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().setSubCarState(endSubCarStatus);
        input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().setLocationCoordinate(input.getMouldingInput().getInput().getColdMoldOpeningPosition().getCoordinate());
        input.getMouldingInput().getInput().getColdMoldOpeningPosition().setStatus(UNOCCUPIED);

        input.getMouldingInput().getInput().getHotAndCoreMoldRefluxSubCar().setProduct(product);
        input.getMouldingInput().getInput().getColdMoldOpeningPosition().setProduct(null);

        return input;
    }

    public OutBoundMouldingAndClosingBox FullColdSprayGoWaitPositionEventCreate(OutBoundMouldingAndClosingBox input, Coordinate coldSprayWaitCoordinate) {
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime = Math.abs(input.getMouldingInput().getInput().getColdSandBoxSubCar().getLocationCoordinate().getY() - coldSprayWaitCoordinate.getY())
                / input.getMouldingInput().getInput().getColdSandBoxSubCar().getFullSpeed();
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getMouldingInput().getInput().getColdSandBoxSubCar().getSubCarState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        endSubCarStatus.add(FULL_LOAD);
        endSubCarStatus.add(TOP_ROD_RAISED);
        endSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getMouldingInput().getInput().getColdSandBoxGoSprayPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);
        //生成事件
        Event event = allTool.createEvent("100226", "ELCSSSCGRWP", executionTime, input.getMouldingInput().getInput().getTotalTime() - executionTime,
                input.getMouldingInput().getInput().getTotalTime(), input.getMouldingInput().getInput().getColdSandBoxSubCar().getId(), startSubCarStatus,
                false, null, endSubCarStatus, true, coldSprayWaitCoordinate, null, null,
                false, null, null, false, null, null, null,
                null, null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getColdSandBoxSubCar().getId(), SUB_CAR, RUNNING, coldSprayWaitCoordinate, null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getColdSandBoxSubCarInGanttIndex()).getEvents().add(event);

        //改变车辆状态和时间
        input.getMouldingInput().getInput().getColdSandBoxSubCar().setSubCarState(endSubCarStatus);
        input.getMouldingInput().getInput().getColdSandBoxSubCar().setLocationCoordinate(coldSprayWaitCoordinate);

        return input;
    }
}
