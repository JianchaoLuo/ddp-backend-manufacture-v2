package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.mouldingLine;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductAfootEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductFinishEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
 * @Created: 2024/7/13 13:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SprayGoClosingEventFlow {
    /**
     * 仿真总工具类
     */
    private SimulationTool allTool = new SimulationTool();

    /**
     * 热砂喷涂到合箱空载前往喷涂点时事件流生成
     * @param input
     * @return
     */
    public OutBoundMouldingAndClosingBox EmptyHotSprayGoClosingBoxEventCreate(OutBoundMouldingAndClosingBox input){
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getLocationCoordinate().getY() -
                input.getMouldingInput().getInput().getHotSpray_3().getEquipmentCoordinate().getY()) / input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getEmptySpeed();
        double executionTime_2 = Math.abs(input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getLocationCoordinate().getX() -
                input.getMouldingInput().getInput().getHotSpray_3().getEquipmentCoordinate().getX()) / input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getEmptySpeed();
        double executionTime_3 = input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getLocationCoordinate().getX() -
                input.getMouldingInput().getInput().getHotSpray_3().getEquipmentCoordinate().getX()) / input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getFullSpeed();
        Coordinate aimcoordinate = new Coordinate(input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getLocationCoordinate().getX(),
                input.getMouldingInput().getInput().getHotSpray_3().getEquipmentCoordinate().getY());
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus_1 = new ArrayList<>();
        endSubCarStatus_1.add(EMPTY_LOAD);
        endSubCarStatus_1.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_1.add(SUB_CAR_IN_FERRY);
        List<SubCarStatusEnum> endSubCarStatus_2 = new ArrayList<>();
        endSubCarStatus_2.add(EMPTY_LOAD);
        endSubCarStatus_2.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_2.add(SUB_CAR_NOT_IN_FERRY);
        List<SubCarStatusEnum> endSubCarStatus_3 = new ArrayList<>();
        endSubCarStatus_3.add(FULL_LOAD);
        endSubCarStatus_3.add(TOP_ROD_RAISED);
        endSubCarStatus_3.add(SUB_CAR_NOT_IN_FERRY);
        List<SubCarStatusEnum> endSubCarStatus_4 = new ArrayList<>();
        endSubCarStatus_4.add(FULL_LOAD);
        endSubCarStatus_4.add(TOP_ROD_RAISED);
        endSubCarStatus_4.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus_1 = new ArrayList<>();
        List<FerryStatusEnum> endFerryStatus_2 = new ArrayList<>();
        endFerryStatus_1.add(NOT_CARRY_SUB_CAR);
        endFerryStatus_2.add(CARRY_SUB_CAR);
        //点位状态列表
        List<EquipmentOperationStatusEnum> startEquipmentStatus = new ArrayList<>();
        List<EquipmentOperationStatusEnum> endEquipmentStatus = new ArrayList<>();
        startEquipmentStatus.add(EquipmentOperationStatusEnum.FINISHED);
        endEquipmentStatus.add(EquipmentOperationStatusEnum.STANDBY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getMouldingInput().getInput().getHotSparyGoClosingBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getMouldingInput().getInput().getHotSpray_3().getProducts().getFirst().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.GOING_CLOSING_BOX);
        product.setProductFinish(ProductFinishEnum.FINISH_SPRAY);

        //生成事件
        Event event1 = allTool.createEvent("100227", "ELSGCFGSE", executionTime_1, input.getMouldingInput().getInput().getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getId(), startSubCarStatus,
                false, null, endSubCarStatus_1, true, aimcoordinate, input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getId(), startFerryStatus,
                false, null, endFerryStatus_2, true, aimcoordinate, null, null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getId(), FERRY, RUNNING, aimcoordinate, null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getHotSprayGoClosingBoxSubCarInGanttIndex()).getEvents().add(event1);

        //生成事件
        Event event2 = allTool.createEvent("100228", "ELCSGCSCGSE", executionTime_2, input.getMouldingInput().getInput().getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_3 - executionTime_4, input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getId(), endSubCarStatus_1,
                true, aimcoordinate, endSubCarStatus_2, true, input.getMouldingInput().getInput().getHotSpray_3().getEquipmentCoordinate(), input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getId(), endFerryStatus_2,
                true, aimcoordinate, endFerryStatus_1, true, aimcoordinate, input.getMouldingInput().getInput().getHotSpray_3().getId(), startEquipmentStatus, startEquipmentStatus,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getId(), SUB_CAR, RUNNING,
                input.getMouldingInput().getInput().getHotSpray_3().getEquipmentCoordinate(), null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getHotSprayGoClosingBoxSubCarInGanttIndex()).getEvents().add(event2);

        //生成事件
        Event event3 = allTool.createEvent("100229", "ELCSGCSCLISE", executionTime_3, input.getMouldingInput().getInput().getTotalTime() - executionTime_3 - executionTime_4,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_4, input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getId(), endSubCarStatus_2,
                true, input.getMouldingInput().getInput().getHotSpray_3().getEquipmentCoordinate(), endSubCarStatus_3, true, input.getMouldingInput().getInput().getHotSpray_3().getEquipmentCoordinate(),
                input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getId(), endFerryStatus_1, true, aimcoordinate, endFerryStatus_1, true, aimcoordinate,
                input.getMouldingInput().getInput().getHotSpray_3().getId(), startEquipmentStatus, endEquipmentStatus, null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getId(), SUB_CAR, RISING, null, product);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getHotSprayGoClosingBoxSubCarInGanttIndex()).getEvents().add(event3);

        //生成事件
        Event event4 = allTool.createEvent("100230", "FLCSGCSCGF", executionTime_4, input.getMouldingInput().getInput().getTotalTime()- executionTime_4,
                input.getMouldingInput().getInput().getTotalTime(), input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getId(), endSubCarStatus_3,
                false, null, endSubCarStatus_4, true, aimcoordinate, input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getId(), endFerryStatus_1,
                true, aimcoordinate, endFerryStatus_2, true, aimcoordinate, null, null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getId(), SUB_CAR, RUNNING, aimcoordinate, product);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getHotSprayGoClosingBoxSubCarInGanttIndex()).getEvents().add(event4);

        //改变车辆状态和时间
        input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().setSubCarState(endSubCarStatus_4);
        input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().setLocationCoordinate(aimcoordinate);
        input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().setFerryState(endFerryStatus_2);
        input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().setLocationCoordinate(aimcoordinate);
        input.getMouldingInput().getInput().getHotSpray_3().setEquipmentStatus(EquipmentOperationStatusEnum.STANDBY);

        input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().setProduct(product);
        input.getMouldingInput().getInput().getHotSpray_3().setProducts(null);

        return input;
    }

    /**
     * 热砂喷涂到合箱空载前往喷涂点时事件流生成
     * @param input
     * @return
     */
    public OutBoundMouldingAndClosingBox EmptyHotSprayGoClosingBoxDownEventCreate(OutBoundMouldingAndClosingBox input){
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getLocationCoordinate().getY() -
                input.getMouldingInput().getInput().getHotSpray_3().getEquipmentCoordinate().getY()) / input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getEmptySpeed();
        double executionTime_2 = Math.abs(input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getLocationCoordinate().getX() -
                input.getMouldingInput().getInput().getHotSpray_3().getEquipmentCoordinate().getX()) / input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getEmptySpeed();
        double executionTime_3 = input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getLocationCoordinate().getX() -
                input.getMouldingInput().getInput().getHotSpray_3().getEquipmentCoordinate().getX()) / input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getFullSpeed();
        Coordinate aimcoordinate = new Coordinate(input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getLocationCoordinate().getX(), input.getMouldingInput().getInput().getHotSpray_3().getEquipmentCoordinate().getY());
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus_1 = new ArrayList<>();
        endSubCarStatus_1.add(EMPTY_LOAD);
        endSubCarStatus_1.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_1.add(SUB_CAR_IN_FERRY);
        List<SubCarStatusEnum> endSubCarStatus_2 = new ArrayList<>();
        endSubCarStatus_2.add(EMPTY_LOAD);
        endSubCarStatus_2.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_2.add(SUB_CAR_NOT_IN_FERRY);
        List<SubCarStatusEnum> endSubCarStatus_3 = new ArrayList<>();
        endSubCarStatus_3.add(FULL_LOAD);
        endSubCarStatus_3.add(TOP_ROD_RAISED);
        endSubCarStatus_3.add(SUB_CAR_NOT_IN_FERRY);
        List<SubCarStatusEnum> endSubCarStatus_4 = new ArrayList<>();
        endSubCarStatus_4.add(FULL_LOAD);
        endSubCarStatus_4.add(TOP_ROD_RAISED);
        endSubCarStatus_4.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus_1 = new ArrayList<>();
        List<FerryStatusEnum> endFerryStatus_2 = new ArrayList<>();
        endFerryStatus_1.add(NOT_CARRY_SUB_CAR);
        endFerryStatus_2.add(CARRY_SUB_CAR);
        //点位状态列表
        List<EquipmentOperationStatusEnum> startEquipmentStatus = new ArrayList<>();
        List<EquipmentOperationStatusEnum> endEquipmentStatus = new ArrayList<>();
        startEquipmentStatus.add(EquipmentOperationStatusEnum.FINISHED);
        endEquipmentStatus.add(EquipmentOperationStatusEnum.STANDBY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getMouldingInput().getInput().getHotSparyGoClosingBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getMouldingInput().getInput().getHotSpray_3().getProducts().getFirst().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.GOING_CLOSING_BOX);
        product.setProductFinish(ProductFinishEnum.FINISH_SPRAY);

        //生成事件
        Event event1 = allTool.createEvent("100231", "ELSGCFGSE", executionTime_1, input.getMouldingInput().getInput().getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getId(), startSubCarStatus,
                false, null, endSubCarStatus_1, true, aimcoordinate, input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getId(), startFerryStatus,
                false, null, endFerryStatus_2, true, aimcoordinate, null, null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getId(), FERRY, RUNNING, aimcoordinate, null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getHotSprayGoClosingBoxSubCarInGanttIndex()).getEvents().add(event1);

        //生成事件
        Event event2 = allTool.createEvent("100232", "ELCSGCSCGSE", executionTime_2, input.getMouldingInput().getInput().getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_3 - executionTime_4, input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getId(), endSubCarStatus_1,
                true, aimcoordinate, endSubCarStatus_2, true, input.getMouldingInput().getInput().getHotSpray_3().getEquipmentCoordinate(), input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getId(), endFerryStatus_2,
                true, aimcoordinate, endFerryStatus_1, true, aimcoordinate, input.getMouldingInput().getInput().getHotSpray_3().getId(), startEquipmentStatus, startEquipmentStatus,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getId(), SUB_CAR, RUNNING,
                input.getMouldingInput().getInput().getHotSpray_3().getEquipmentCoordinate(), null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getHotSprayGoClosingBoxSubCarInGanttIndex()).getEvents().add(event2);

        //生成事件
        Event event3 = allTool.createEvent("100233", "ELCSGCSCLISE", executionTime_3, input.getMouldingInput().getInput().getTotalTime() - executionTime_3 - executionTime_4,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_4, input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getId(), endSubCarStatus_2,
                true, input.getMouldingInput().getInput().getHotSpray_3().getEquipmentCoordinate(), endSubCarStatus_3, true, input.getMouldingInput().getInput().getHotSpray_3().getEquipmentCoordinate(),
                input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getId(), endFerryStatus_1, true, aimcoordinate, endFerryStatus_1, true, aimcoordinate,
                input.getMouldingInput().getInput().getHotSpray_3().getId(), startEquipmentStatus, endEquipmentStatus, null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getId(), SUB_CAR, RISING, null, product);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getHotSprayGoClosingBoxSubCarInGanttIndex()).getEvents().add(event3);

        //生成事件
        Event event4 = allTool.createEvent("100234", "FLCSGCSCGF", executionTime_4, input.getMouldingInput().getInput().getTotalTime()- executionTime_4,
                input.getMouldingInput().getInput().getTotalTime(), input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getId(), endSubCarStatus_3,
                false, null, endSubCarStatus_4, true, aimcoordinate, input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getId(), endFerryStatus_1,
                true, aimcoordinate, endFerryStatus_2, true, aimcoordinate, null, null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getId(), SUB_CAR, RUNNING, aimcoordinate, product);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getHotSprayGoClosingBoxSubCarInGanttIndex()).getEvents().add(event4);

        //改变车辆状态和时间
        input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().setSubCarState(endSubCarStatus_4);
        input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().setLocationCoordinate(aimcoordinate);
        input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().setFerryState(endFerryStatus_2);
        input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().setLocationCoordinate(aimcoordinate);
        input.getMouldingInput().getInput().getHotSpray_3().setEquipmentStatus(EquipmentOperationStatusEnum.STANDBY);

        input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().setProduct(product);
        input.getMouldingInput().getInput().getHotSpray_3().setProducts(null);

        OutBoundMouldingAndClosingBox outBoundMouldingAndClosingBox = input;

        return outBoundMouldingAndClosingBox;
    }

    /**
     * 热砂喷涂到合箱空载前往合箱点时事件流生成
     * @param input
     * @return
     */
    public OutBoundMouldingAndClosingBox FullHotSprayGoClosingBoxEventCreate(OutBoundMouldingAndClosingBox input){
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getLocationCoordinate().getY() -
                input.getClosingBoxInput().getInput().getHotClosingBoxPosition().getCoordinate().getY()) / input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getFullSpeed();
        double executionTime_2 = Math.abs(input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getLocationCoordinate().getX() -
                input.getClosingBoxInput().getInput().getHotClosingBoxPosition().getCoordinate().getX()) / input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getFullSpeed();
        double executionTime_3 = input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getLocationCoordinate().getX() -
                input.getClosingBoxInput().getInput().getHotClosingBoxPosition().getCoordinate().getX()) / input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getEmptySpeed();
        Coordinate aimcoordinate = new Coordinate(input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getLocationCoordinate().getX(), input.getClosingBoxInput().getInput().getHotClosingBoxPosition().getCoordinate().getY());
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus_1 = new ArrayList<>();
        endSubCarStatus_1.add(FULL_LOAD);
        endSubCarStatus_1.add(TOP_ROD_RAISED);
        endSubCarStatus_1.add(SUB_CAR_IN_FERRY);
        List<SubCarStatusEnum> endSubCarStatus_2 = new ArrayList<>();
        endSubCarStatus_2.add(FULL_LOAD);
        endSubCarStatus_2.add(TOP_ROD_RAISED);
        endSubCarStatus_2.add(SUB_CAR_NOT_IN_FERRY);
        List<SubCarStatusEnum> endSubCarStatus_3 = new ArrayList<>();
        endSubCarStatus_3.add(EMPTY_LOAD);
        endSubCarStatus_3.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_3.add(SUB_CAR_NOT_IN_FERRY);
        List<SubCarStatusEnum> endSubCarStatus_4 = new ArrayList<>();
        endSubCarStatus_4.add(EMPTY_LOAD);
        endSubCarStatus_4.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_4.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus_1 = new ArrayList<>();
        List<FerryStatusEnum> endFerryStatus_2 = new ArrayList<>();
        endFerryStatus_1.add(NOT_CARRY_SUB_CAR);
        endFerryStatus_2.add(CARRY_SUB_CAR);
        //点位状态列表
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        startPositionStatus.add(PositionStatusEnum.UNOCCUPIED);
        endPositionStatus.add(PositionStatusEnum.OCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getMouldingInput().getInput().getHotSparyGoClosingBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getProduct().productDeepCopy();

        //生成事件
        Event event1 = allTool.createEvent("100235", "FLSGCFGCP", executionTime_1, input.getMouldingInput().getInput().getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getId(), startSubCarStatus,
                false, null, endSubCarStatus_1, true, aimcoordinate, input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getId(), startFerryStatus,
                false, null, endFerryStatus_2, true, aimcoordinate, null, null, null,
                null, null, null, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getId(), FERRY, RUNNING, aimcoordinate, product);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getHotSprayGoClosingBoxSubCarInGanttIndex()).getEvents().add(event1);

        //生成事件
        Event event2 = allTool.createEvent("100236", "FLCSGCSCGCP", executionTime_2, input.getMouldingInput().getInput().getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_3 - executionTime_4, input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getId(), endSubCarStatus_1,
                true, aimcoordinate, endSubCarStatus_2, true, input.getClosingBoxInput().getInput().getHotClosingBoxPosition().getCoordinate(), input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getId(), endFerryStatus_2,
                true, aimcoordinate, endFerryStatus_1, true, aimcoordinate, null, null, null,
                input.getClosingBoxInput().getInput().getHotClosingBoxPosition().getId(), startPositionStatus, startPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getId(), SUB_CAR, RUNNING,
                input.getClosingBoxInput().getInput().getHotClosingBoxPosition().getCoordinate(), product);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getHotSprayGoClosingBoxSubCarInGanttIndex()).getEvents().add(event2);

        //生成事件
        Event event3 = allTool.createEvent("100237", "FLCSGCSCLICP", executionTime_3, input.getMouldingInput().getInput().getTotalTime() - executionTime_3 - executionTime_4,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_4, input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getId(), endSubCarStatus_2,
                true, input.getClosingBoxInput().getInput().getHotClosingBoxPosition().getCoordinate(), endSubCarStatus_3, true, input.getClosingBoxInput().getInput().getHotClosingBoxPosition().getCoordinate(),
                input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getId(), endFerryStatus_1, true, aimcoordinate, endFerryStatus_1, true, aimcoordinate,
                null, null, null, input.getClosingBoxInput().getInput().getHotClosingBoxPosition().getId(), startPositionStatus, endPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getHotSprayGoClosingBoxSubCarInGanttIndex()).getEvents().add(event3);

        //生成事件
        Event event4 = allTool.createEvent("100238", "ELCSGCSCGF", executionTime_4, input.getMouldingInput().getInput().getTotalTime()- executionTime_4,
                input.getMouldingInput().getInput().getTotalTime(), input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getId(), endSubCarStatus_3,
                false, null, endSubCarStatus_4, true, aimcoordinate, input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().getId(), endFerryStatus_1,
                true, aimcoordinate, endFerryStatus_2, true, aimcoordinate, null, null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().getId(), SUB_CAR, RUNNING, aimcoordinate, null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getHotSprayGoClosingBoxSubCarInGanttIndex()).getEvents().add(event4);

        //改变车辆状态和时间
        input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().setSubCarState(endSubCarStatus_4);
        input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().setLocationCoordinate(aimcoordinate);
        input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().setFerryState(endFerryStatus_2);
        input.getMouldingInput().getInput().getHotSprayGoClosingBoxferry().setLocationCoordinate(aimcoordinate);
        input.getClosingBoxInput().getInput().getHotClosingBoxPosition().setStatus(PositionStatusEnum.OCCUPIED);

        input.getClosingBoxInput().getInput().getHotClosingBoxPosition().setProduct(product);
        input.getMouldingInput().getInput().getHotSprayGoClosingBoxSubCar().setProduct(null);

        OutBoundMouldingAndClosingBox outBoundMouldingAndClosingBox = input;

        return outBoundMouldingAndClosingBox;
    }
    /**
     * 冷砂喷涂到合箱空载前往喷涂点时事件流生成
     * @param input
     * @return
     */
    public OutBoundMouldingAndClosingBox EmptyColdSprayGoClosingBoxUpEventCreate(OutBoundMouldingAndClosingBox input){
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getLocationCoordinate().getY() -
                input.getMouldingInput().getInput().getColdSpray_3().getEquipmentCoordinate().getY()) / input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getEmptySpeed();
        double executionTime_2 = Math.abs(input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getLocationCoordinate().getX() -
                input.getMouldingInput().getInput().getColdSpray_3().getEquipmentCoordinate().getX()) / input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getEmptySpeed();
        double executionTime_3 = input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getLocationCoordinate().getX() -
                input.getMouldingInput().getInput().getColdSpray_3().getEquipmentCoordinate().getX()) / input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getFullSpeed();
        Coordinate aimcoordinate = new Coordinate(input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getLocationCoordinate().getX(),
                input.getMouldingInput().getInput().getColdSpray_3().getEquipmentCoordinate().getY());
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus_1 = new ArrayList<>();
        endSubCarStatus_1.add(EMPTY_LOAD);
        endSubCarStatus_1.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_1.add(SUB_CAR_IN_FERRY);
        List<SubCarStatusEnum> endSubCarStatus_2 = new ArrayList<>();
        endSubCarStatus_2.add(EMPTY_LOAD);
        endSubCarStatus_2.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_2.add(SUB_CAR_NOT_IN_FERRY);
        List<SubCarStatusEnum> endSubCarStatus_3 = new ArrayList<>();
        endSubCarStatus_3.add(FULL_LOAD);
        endSubCarStatus_3.add(TOP_ROD_RAISED);
        endSubCarStatus_3.add(SUB_CAR_NOT_IN_FERRY);
        List<SubCarStatusEnum> endSubCarStatus_4 = new ArrayList<>();
        endSubCarStatus_4.add(FULL_LOAD);
        endSubCarStatus_4.add(TOP_ROD_RAISED);
        endSubCarStatus_4.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus_1 = new ArrayList<>();
        List<FerryStatusEnum> endFerryStatus_2 = new ArrayList<>();
        endFerryStatus_1.add(NOT_CARRY_SUB_CAR);
        endFerryStatus_2.add(CARRY_SUB_CAR);
        //点位状态列表
        List<EquipmentOperationStatusEnum> startEquipmentStatus = new ArrayList<>();
        List<EquipmentOperationStatusEnum> endEquipmentStatus = new ArrayList<>();
        startEquipmentStatus.add(EquipmentOperationStatusEnum.FINISHED);
        endEquipmentStatus.add(EquipmentOperationStatusEnum.STANDBY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getMouldingInput().getInput().getColdSparyGoClosingBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getMouldingInput().getInput().getColdSpray_3().getProducts().getFirst().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.GOING_CLOSING_BOX);
        product.setProductFinish(ProductFinishEnum.FINISH_SPRAY);

        //生成事件
        Event event1 = allTool.createEvent("100239", "ELSGCFGCSE", executionTime_1, input.getMouldingInput().getInput().getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getId(), startSubCarStatus,
                false, null, endSubCarStatus_1, true, aimcoordinate, input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getId(), startFerryStatus,
                false, null, endFerryStatus_2, true, aimcoordinate, null, null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getId(), FERRY, RUNNING, aimcoordinate, null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getColdSprayGoClosingBoxSubCarInGanttIndex()).getEvents().add(event1);

        //生成事件
        Event event2 = allTool.createEvent("100240", "ELCSGCSCGCSE", executionTime_2, input.getMouldingInput().getInput().getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_3 - executionTime_4, input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getId(), endSubCarStatus_1,
                true, aimcoordinate, endSubCarStatus_2, true, input.getMouldingInput().getInput().getColdSpray_3().getEquipmentCoordinate(), input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getId(), endFerryStatus_2,
                true, aimcoordinate, endFerryStatus_1, true, aimcoordinate, input.getMouldingInput().getInput().getColdSpray_3().getId(), startEquipmentStatus, startEquipmentStatus,
                null, null, null, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getId(), SUB_CAR, RUNNING, input.getMouldingInput().getInput().getColdSpray_3().getEquipmentCoordinate(), null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getColdSprayGoClosingBoxSubCarInGanttIndex()).getEvents().add(event2);

        //生成事件
        Event event3 = allTool.createEvent("100241", "ELCSGCSCLICSE", executionTime_3, input.getMouldingInput().getInput().getTotalTime() - executionTime_3 - executionTime_4,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_4, input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getId(), endSubCarStatus_2,
                true, input.getMouldingInput().getInput().getColdSpray_3().getEquipmentCoordinate(), endSubCarStatus_3, true, input.getMouldingInput().getInput().getColdSpray_3().getEquipmentCoordinate(),
                input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getId(), endFerryStatus_1, true, aimcoordinate, endFerryStatus_1, true, aimcoordinate,
                input.getMouldingInput().getInput().getColdSpray_3().getId(), startEquipmentStatus, endEquipmentStatus, null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getId(), SUB_CAR, RISING, null, product);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getColdSprayGoClosingBoxSubCarInGanttIndex()).getEvents().add(event3);

        //生成事件
        Event event4 = allTool.createEvent("100242", "FLCSGCSCGCF", executionTime_4, input.getMouldingInput().getInput().getTotalTime()- executionTime_4,
                input.getMouldingInput().getInput().getTotalTime(), input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getId(), endSubCarStatus_3,
                false, null, endSubCarStatus_4, true, aimcoordinate, input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getId(), endFerryStatus_1,
                true, aimcoordinate, endFerryStatus_2, true, aimcoordinate, null, null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getId(), SUB_CAR, RUNNING, aimcoordinate, product);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getColdSprayGoClosingBoxSubCarInGanttIndex()).getEvents().add(event4);

        //改变车辆状态和时间
        input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().setSubCarState(endSubCarStatus_4);
        input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().setLocationCoordinate(aimcoordinate);
        input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().setFerryState(endFerryStatus_2);
        input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().setLocationCoordinate(aimcoordinate);
        input.getMouldingInput().getInput().getColdSpray_3().setEquipmentStatus(EquipmentOperationStatusEnum.STANDBY);

        input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().setProduct(product);
        input.getMouldingInput().getInput().getColdSpray_3().setProducts(null);

        return input;
    }

    /**
     * 冷砂喷涂到合箱空载前往喷涂点时事件流生成
     * @param input
     * @return
     */
    public OutBoundMouldingAndClosingBox EmptyColdSprayGoClosingBoxDownEventCreate(OutBoundMouldingAndClosingBox input){
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getLocationCoordinate().getY() -
                input.getMouldingInput().getInput().getColdSpray_3().getEquipmentCoordinate().getY()) / input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getEmptySpeed();
        double executionTime_2 = Math.abs(input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getLocationCoordinate().getX() -
                input.getMouldingInput().getInput().getColdSpray_3().getEquipmentCoordinate().getX()) / input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getEmptySpeed();
        double executionTime_3 = input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getLocationCoordinate().getX() -
                input.getMouldingInput().getInput().getColdSpray_3().getEquipmentCoordinate().getX()) / input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getFullSpeed();
        Coordinate aimcoordinate = new Coordinate(input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getLocationCoordinate().getX(), input.getMouldingInput().getInput().getColdSpray_3().getEquipmentCoordinate().getY());
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus_1 = new ArrayList<>();
        endSubCarStatus_1.add(EMPTY_LOAD);
        endSubCarStatus_1.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_1.add(SUB_CAR_IN_FERRY);
        List<SubCarStatusEnum> endSubCarStatus_2 = new ArrayList<>();
        endSubCarStatus_2.add(EMPTY_LOAD);
        endSubCarStatus_2.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_2.add(SUB_CAR_NOT_IN_FERRY);
        List<SubCarStatusEnum> endSubCarStatus_3 = new ArrayList<>();
        endSubCarStatus_3.add(FULL_LOAD);
        endSubCarStatus_3.add(TOP_ROD_RAISED);
        endSubCarStatus_3.add(SUB_CAR_NOT_IN_FERRY);
        List<SubCarStatusEnum> endSubCarStatus_4 = new ArrayList<>();
        endSubCarStatus_4.add(FULL_LOAD);
        endSubCarStatus_4.add(TOP_ROD_RAISED);
        endSubCarStatus_4.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus_1 = new ArrayList<>();
        List<FerryStatusEnum> endFerryStatus_2 = new ArrayList<>();
        endFerryStatus_1.add(NOT_CARRY_SUB_CAR);
        endFerryStatus_2.add(CARRY_SUB_CAR);
        //点位状态列表
        List<EquipmentOperationStatusEnum> startEquipmentStatus = new ArrayList<>();
        List<EquipmentOperationStatusEnum> endEquipmentStatus = new ArrayList<>();
        startEquipmentStatus.add(EquipmentOperationStatusEnum.FINISHED);
        endEquipmentStatus.add(EquipmentOperationStatusEnum.STANDBY);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getMouldingInput().getInput().getColdSparyGoClosingBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getMouldingInput().getInput().getColdSpray_3().getProducts().getFirst().productDeepCopy();
        product.setProductAfoot(ProductAfootEnum.GOING_CLOSING_BOX);
        product.setProductFinish(ProductFinishEnum.FINISH_SPRAY);

        //生成事件
        Event event1 = allTool.createEvent("100243", "ELSGCFGSCE", executionTime_1, input.getMouldingInput().getInput().getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getId(), startSubCarStatus,
                false, null, endSubCarStatus_1, true, aimcoordinate, input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getId(), startFerryStatus,
                false, null, endFerryStatus_2, true, aimcoordinate, null, null, null,
                null, null, null, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getId(), FERRY, RUNNING, aimcoordinate, null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getColdSprayGoClosingBoxSubCarInGanttIndex()).getEvents().add(event1);

        //生成事件
        Event event2 = allTool.createEvent("100244", "ELCSGCSCGSCE", executionTime_2, input.getMouldingInput().getInput().getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_3 - executionTime_4, input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getId(), endSubCarStatus_1,
                true, aimcoordinate, endSubCarStatus_2, true, input.getMouldingInput().getInput().getColdSpray_3().getEquipmentCoordinate(), input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getId(), endFerryStatus_2,
                true, aimcoordinate, endFerryStatus_1, true, aimcoordinate, input.getMouldingInput().getInput().getColdSpray_3().getId(), startEquipmentStatus, startEquipmentStatus,
                null, null, null, null, null, null, pathIds, pathStatusEnums, pathStatusEnums,
                input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getId(), SUB_CAR, RUNNING, input.getMouldingInput().getInput().getColdSpray_3().getEquipmentCoordinate(), null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getColdSprayGoClosingBoxSubCarInGanttIndex()).getEvents().add(event2);

        //生成事件
        Event event3 = allTool.createEvent("100245", "ELCSGCSCLISCE", executionTime_3, input.getMouldingInput().getInput().getTotalTime() - executionTime_3 - executionTime_4,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_4, input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getId(), endSubCarStatus_2,
                true, input.getMouldingInput().getInput().getColdSpray_3().getEquipmentCoordinate(), endSubCarStatus_3, true, input.getMouldingInput().getInput().getColdSpray_3().getEquipmentCoordinate(),
                input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getId(), endFerryStatus_1, true, aimcoordinate, endFerryStatus_1, true, aimcoordinate,
                input.getMouldingInput().getInput().getColdSpray_3().getId(), startEquipmentStatus, endEquipmentStatus, null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getId(), SUB_CAR, RISING, null, product);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getColdSprayGoClosingBoxSubCarInGanttIndex()).getEvents().add(event3);

        //生成事件
        Event event4 = allTool.createEvent("100246", "FLCSGCSCGCF", executionTime_4, input.getMouldingInput().getInput().getTotalTime()- executionTime_4,
                input.getMouldingInput().getInput().getTotalTime(), input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getId(), endSubCarStatus_3,
                false, null, endSubCarStatus_4, true, aimcoordinate, input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getId(), endFerryStatus_1,
                true, aimcoordinate, endFerryStatus_2, true, aimcoordinate, null, null, null,
                null, null, null, null, null, null, pathIds, pathStatusEnums,
                pathStatusEnums, input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getId(), SUB_CAR, RUNNING, aimcoordinate, product);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getColdSprayGoClosingBoxSubCarInGanttIndex()).getEvents().add(event4);

        //改变车辆状态和时间
        input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().setSubCarState(endSubCarStatus_4);
        input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().setLocationCoordinate(aimcoordinate);
        input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().setFerryState(endFerryStatus_2);
        input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().setLocationCoordinate(aimcoordinate);
        input.getMouldingInput().getInput().getColdSpray_3().setEquipmentStatus(EquipmentOperationStatusEnum.STANDBY);

        input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().setProduct(product);
        input.getMouldingInput().getInput().getColdSpray_3().setProducts(null);

        OutBoundMouldingAndClosingBox outBoundMouldingAndClosingBox = input;

        return outBoundMouldingAndClosingBox;
    }

    /**
     * 冷砂喷涂到合箱空载前往合箱点时事件流生成
     * @param input
     * @return
     */
    public OutBoundMouldingAndClosingBox FullColdSprayGoClosingBoxEventCreate(OutBoundMouldingAndClosingBox input){
        //发生子车前往轨道回流等待的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getLocationCoordinate().getY() -
                input.getClosingBoxInput().getInput().getColdClosingBoxPosition().getCoordinate().getY()) / input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getFullSpeed();
        double executionTime_2 = Math.abs(input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getLocationCoordinate().getX() -
                input.getClosingBoxInput().getInput().getColdClosingBoxPosition().getCoordinate().getX()) / input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getFullSpeed();
        double executionTime_3 = input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getLocationCoordinate().getX() -
                input.getClosingBoxInput().getInput().getColdClosingBoxPosition().getCoordinate().getX()) / input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getEmptySpeed();
        Coordinate aimcoordinate = new Coordinate(input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getLocationCoordinate().getX(), input.getClosingBoxInput().getInput().getColdClosingBoxPosition().getCoordinate().getY());
        //子车开始状态，防止链表的地址引用造成数据出错
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus_1 = new ArrayList<>();
        endSubCarStatus_1.add(FULL_LOAD);
        endSubCarStatus_1.add(TOP_ROD_RAISED);
        endSubCarStatus_1.add(SUB_CAR_IN_FERRY);
        List<SubCarStatusEnum> endSubCarStatus_2 = new ArrayList<>();
        endSubCarStatus_2.add(FULL_LOAD);
        endSubCarStatus_2.add(TOP_ROD_RAISED);
        endSubCarStatus_2.add(SUB_CAR_NOT_IN_FERRY);
        List<SubCarStatusEnum> endSubCarStatus_3 = new ArrayList<>();
        endSubCarStatus_3.add(EMPTY_LOAD);
        endSubCarStatus_3.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_3.add(SUB_CAR_NOT_IN_FERRY);
        List<SubCarStatusEnum> endSubCarStatus_4 = new ArrayList<>();
        endSubCarStatus_4.add(EMPTY_LOAD);
        endSubCarStatus_4.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_4.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus_1 = new ArrayList<>();
        List<FerryStatusEnum> endFerryStatus_2 = new ArrayList<>();
        endFerryStatus_1.add(NOT_CARRY_SUB_CAR);
        endFerryStatus_2.add(CARRY_SUB_CAR);
        //点位状态列表
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        startPositionStatus.add(PositionStatusEnum.UNOCCUPIED);
        endPositionStatus.add(PositionStatusEnum.OCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(input.getMouldingInput().getInput().getColdSparyGoClosingBoxPath().getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getProduct().productDeepCopy();

        //生成事件
        Event event1 = allTool.createEvent("100247", "FLSGCFGCCP", executionTime_1, input.getMouldingInput().getInput().getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4, input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getId(), startSubCarStatus,
                false, null, endSubCarStatus_1, true, aimcoordinate, input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getId(), startFerryStatus,
                false, null, endFerryStatus_2, true, aimcoordinate, null, null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getId(), FERRY, RUNNING, aimcoordinate, product);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getColdSprayGoClosingBoxSubCarInGanttIndex()).getEvents().add(event1);

        //生成事件
        Event event2 = allTool.createEvent("100248", "FLCSGCSCGCCP", executionTime_2, input.getMouldingInput().getInput().getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_3 - executionTime_4, input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getId(), endSubCarStatus_1,
                true, aimcoordinate, endSubCarStatus_2, true, input.getClosingBoxInput().getInput().getColdClosingBoxPosition().getCoordinate(), input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getId(), endFerryStatus_2,
                true, aimcoordinate, endFerryStatus_1, true, aimcoordinate, null, null, null,
                input.getClosingBoxInput().getInput().getColdClosingBoxPosition().getId(), startPositionStatus, startPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getId(), SUB_CAR, RUNNING,
                input.getClosingBoxInput().getInput().getColdClosingBoxPosition().getCoordinate(), product);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getColdSprayGoClosingBoxSubCarInGanttIndex()).getEvents().add(event2);

        //生成事件
        Event event3 = allTool.createEvent("100249", "FLCSGCSCLICCP", executionTime_3, input.getMouldingInput().getInput().getTotalTime() - executionTime_3 - executionTime_4,
                input.getMouldingInput().getInput().getTotalTime() - executionTime_4, input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getId(), endSubCarStatus_2,
                true, input.getClosingBoxInput().getInput().getColdClosingBoxPosition().getCoordinate(), endSubCarStatus_3, true, input.getClosingBoxInput().getInput().getColdClosingBoxPosition().getCoordinate(),
                input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getId(), endFerryStatus_1, true, aimcoordinate, endFerryStatus_1, true, aimcoordinate,
                null, null, null, input.getClosingBoxInput().getInput().getColdClosingBoxPosition().getId(), startPositionStatus, endPositionStatus, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getId(), SUB_CAR, LANDING, null, null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getColdSprayGoClosingBoxSubCarInGanttIndex()).getEvents().add(event3);

        //生成事件
        Event event4 = allTool.createEvent("100250", "ELCSGCSCGCF", executionTime_4, input.getMouldingInput().getInput().getTotalTime()- executionTime_4,
                input.getMouldingInput().getInput().getTotalTime(), input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getId(), endSubCarStatus_3,
                false, null, endSubCarStatus_4, true, aimcoordinate, input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().getId(), endFerryStatus_1,
                true, aimcoordinate, endFerryStatus_2, true, aimcoordinate, null, null, null,
                null, null, null, null, null, null,
                pathIds, pathStatusEnums, pathStatusEnums, input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().getId(), SUB_CAR, RUNNING, aimcoordinate, null);

        //事件加入甘特图
        input.getMouldingInput().getGanttChart().getEventLinkGantts().get(input.getMouldingInput().getInput().getMouldingIndexInput().getColdSprayGoClosingBoxSubCarInGanttIndex()).getEvents().add(event4);

        //改变车辆状态和时间
        input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().setSubCarState(endSubCarStatus_4);
        input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().setLocationCoordinate(aimcoordinate);
        input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().setFerryState(endFerryStatus_2);
        input.getMouldingInput().getInput().getColdSprayGoClosingBoxferry().setLocationCoordinate(aimcoordinate);
        input.getClosingBoxInput().getInput().getColdClosingBoxPosition().setStatus(PositionStatusEnum.OCCUPIED);

        input.getClosingBoxInput().getInput().getColdClosingBoxPosition().setProduct(product);
        input.getMouldingInput().getInput().getColdSprayGoClosingBoxSubCar().setProduct(null);

        OutBoundMouldingAndClosingBox outBoundMouldingAndClosingBox = input;

        return outBoundMouldingAndClosingBox;
    }
}
