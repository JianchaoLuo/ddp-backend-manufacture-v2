package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.moldClosing;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Ferry;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.SubCar;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.WorkStation;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationException;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SnowflakeIdGenerator;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.*;

import java.util.ArrayList;
import java.util.List;

import static cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum.*;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductAfootEnum.GOING_SANDBLASTING;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductFinishEnum.FINISH_MOLD_CLOSING;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum.*;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum.EMPTY_LOAD;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum.SUB_CAR_IN_FERRY;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/6/23 16:04
 */
public class MoldClosingSimulationTools {

    /**
     * 雪花函数生成id
     */
    private SnowflakeIdGenerator idWorker = new SnowflakeIdGenerator(2, 2);

    /**
     * 判断当前子车是否是空载状态
     * @param subCar 需要判断的子车
     * @return true表示空载，false表示满载
     */
    public boolean judgeSubCarIsEmpty(SubCar subCar){
        for (int i = 0; i < subCar.getSubCarState().size(); i++) {
            if (subCar.getSubCarState().get(i).equals(EMPTY_LOAD)){

                return true;
            }
        }

        return false;
    }

    /**
     * 判断当前子车是否是完全在母车上
     * @param subCar 需要判断的子车
     * @return true表示子车完全在母车上，false相反
     */
    public boolean judgeSubCarIsInFerry(SubCar subCar){
        for (int i = 0; i < subCar.getSubCarState().size(); i++) {
            if (subCar.getSubCarState().get(i).equals(SUB_CAR_IN_FERRY)){

                return true;
            }
        }

        return false;
    }

    /**
     * 创造事件的函数
     * @param id 事件id
     * @param eventName 事件名称
     * @param executionTime 事件执行预计需要用到的时间
     * @param startTime 事件发生的时间
     * @param endTime 事件结束的时间
     * @param subCarId 事件开始前，参与子车的id
     * @param startSubCarStatus 事件开始前，参与子车的状态
     * @param startSubCarCpFlag 事件开始前，参与子车的状态，表示是否判断位置
     * @param startSubCarTargetCoordinate 若判断位置，目标位置
     * @param endSubCarStatus 事件结束后，参与子车的状态
     * @param endSubCarCpFlag 事件结束后，参与子车的状态，表示是否判断位置
     * @param endSubCarTargetCoordinate 若判断位置，目标位置
     * @param ferryId 事件开始前，参与母车的id
     * @param startFerryStatus 事件开始前，参与母车的状态
     * @param startFerryCpFlag 事件开始前，参与母车的状态，表示是否判断位置
     * @param startFerryTargetCoordinate 若判断位置，目标位置
     * @param endFerryStatus 事件结束后，参与母车的状态
     * @param endFerryCpFlag 事件结束后，参与母车的状态，表示是否判断位置
     * @param endFerryTargetCoordinate 若判断位置，目标位置
     * @param equipmentId 事件开始前，参与设备的id
     * @param startEquipmentStatus 事件开始前，参与设备的状态
     * @param endEquipmentStatus 事件结束后，参与设备的状态
     * @param positionId 事件开始前，参与点位的id
     * @param startPositionStatus 事件开始前，参与点位的状态
     * @param endPositionStatus 事件结束后，参与点位的状态
     * @param workStationId 事件开始前，参与工岗的id
     * @param startWorkStationStatus 事件开始前，参与工岗的状态
     * @param endWorkStationStatus 事件结束后，参与工岗的状态
     * @param pathIds 事件开始前，参与道路的id
     * @param startPathStatus 事件开始前，参与道路的状态
     * @param endPathstatus 事件结束后，参与道路的状态
     * @param instructionExecuteId 事件中指令的id
     * @param executeType 事件中指令执行者的类型
     * @param instructions 事件中执行的指令
     * @param runAimCoordinate 如有需要，目标点位
     * @return
     */
    public Event createEvent(String id, String eventName, double executionTime, double startTime, double endTime, Long subCarId,
                             List<SubCarStatusEnum> startSubCarStatus, boolean startSubCarCpFlag, Coordinate startSubCarTargetCoordinate,
                             List<SubCarStatusEnum> endSubCarStatus, boolean endSubCarCpFlag, Coordinate endSubCarTargetCoordinate,
                             Long ferryId, List<FerryStatusEnum> startFerryStatus, boolean startFerryCpFlag, Coordinate startFerryTargetCoordinate,
                             List<FerryStatusEnum> endFerryStatus, boolean endFerryCpFlag, Coordinate endFerryTargetCoordinate,
                             Long equipmentId, List<EquipmentOperationStatusEnum> startEquipmentStatus, List<EquipmentOperationStatusEnum> endEquipmentStatus,
                             Long positionId, List<PositionStatusEnum> startPositionStatus, List<PositionStatusEnum> endPositionStatus,
                             Long workStationId, List<WorkstationStatusEnum> startWorkStationStatus, List<WorkstationStatusEnum> endWorkStationStatus,
                             List<Long> pathIds, List<PathStatusEnum> startPathStatus, List<PathStatusEnum> endPathstatus, Long instructionExecuteId,
                             ExecuteTypeEnum executeType, InstructionsEnum instructions, Coordinate runAimCoordinate, Product product){

        SubCarLinkEvent startSubCarEvent = new SubCarLinkEvent(subCarId, startSubCarStatus, startSubCarCpFlag, startSubCarTargetCoordinate);
        SubCarLinkEvent endSubCarEvent = new SubCarLinkEvent(subCarId, endSubCarStatus, endSubCarCpFlag, endSubCarTargetCoordinate);
        FerryLinkEvent startFerryEvent = new FerryLinkEvent(ferryId, startFerryStatus, startFerryCpFlag, startFerryTargetCoordinate);
        FerryLinkEvent endFerryEvent = new FerryLinkEvent(ferryId, endFerryStatus, endFerryCpFlag, endFerryTargetCoordinate);
        EquipmentLinkEvent startEquipmentEvent = new EquipmentLinkEvent(equipmentId, startEquipmentStatus);
        EquipmentLinkEvent endEquipmentEvent = new EquipmentLinkEvent(equipmentId, endEquipmentStatus);
        PositionLinkEvent startPositionEvent = new PositionLinkEvent(positionId, startPositionStatus);
        PositionLinkEvent endPositionEvent = new PositionLinkEvent(positionId, endPositionStatus);
        WorkStationLinkEvent startWorkStationEvent = new WorkStationLinkEvent(workStationId, startWorkStationStatus);
        WorkStationLinkEvent endWorkStationEvent = new WorkStationLinkEvent(workStationId, endWorkStationStatus);

        List<PathLinkEvent> startPathEvents = new ArrayList<>();
        List<PathLinkEvent> endPathLinkEvent = new ArrayList<>();
        int startPathStatusSize = startPathStatus == null ? 0 : startPathStatus.size();
        for (int i = 0; i < startPathStatusSize; i++) {
            startPathEvents.add(new PathLinkEvent(pathIds.get(i), startPathStatus.get(i)));
            endPathLinkEvent.add(new PathLinkEvent(pathIds.get(i), endPathstatus.get(i)));
        }

        Instruction instruction = new Instruction(instructionExecuteId, executeType, instructions, runAimCoordinate);
        Event event = new Event(id, eventName, executionTime, startTime, endTime, startSubCarEvent, startFerryEvent,
                startEquipmentEvent, startPositionEvent, startWorkStationEvent, startPathEvents, endSubCarEvent, endFerryEvent,
                endEquipmentEvent, endPositionEvent, endWorkStationEvent, endPathLinkEvent, instruction, false, false, product);

        return event;
    }

    /**
     * 判断热砂母车是否到达工岗位置且工岗处于待搬运
     * @return 0表示没有，1-4表示1-4号工岗
     */
    public int judgeHotFerryInWorkstation(Ferry ferry, List<WorkStation> workstations, MoldClosingIndexInput input){
        if (ferry.getLocationCoordinate().getX() == workstations.get(input.getHotMoldClosingWorkStationIndex_1()).getWorkPositionCoordinate().getX()){
            if (workstations.get(input.getHotMoldClosingWorkStationIndex_1()).getStatus().equals(WAIT_TRANSPORT))
                return 1;
        }else if (ferry.getLocationCoordinate().getX() == workstations.get(input.getHotMoldClosingWorkStationIndex_2()).getWorkPositionCoordinate().getX()){
            if (workstations.get(input.getHotMoldClosingWorkStationIndex_2()).getStatus().equals(WAIT_TRANSPORT))
                return 2;
        }else if (ferry.getLocationCoordinate().getX() == workstations.get(input.getHotMoldClosingWorkStationIndex_3()).getWorkPositionCoordinate().getX()){
            if (workstations.get(input.getHotMoldClosingWorkStationIndex_3()).getStatus().equals(WAIT_TRANSPORT))
                return 3;
        }else if (ferry.getLocationCoordinate().getX() == workstations.get(input.getHotMoldClosingWorkStationIndex_4()).getWorkPositionCoordinate().getX()){
            if (workstations.get(input.getHotMoldClosingWorkStationIndex_4()).getStatus().equals(WAIT_TRANSPORT))
                return 4;
        }

        return 0;
    }

    /**
     * 判断冷砂母车是否到达工岗位置且工岗处于待搬运
     * @return 0表示没有，1-4表示1-4号工岗
     */
    public int judgeColdFerryInWorkstation(Ferry ferry, List<WorkStation> workstations, MoldClosingIndexInput input){
        if (ferry.getLocationCoordinate().getX() == workstations.get(input.getColdMoldClosingWorkStationIndex_1()).getWorkPositionCoordinate().getX()){
            if (workstations.get(input.getColdMoldClosingWorkStationIndex_1()).getStatus().equals(WAIT_TRANSPORT))
                return 1;
        }else if (ferry.getLocationCoordinate().getX() == workstations.get(input.getColdMoldClosingWorkStationIndex_2()).getWorkPositionCoordinate().getX()){
            if (workstations.get(input.getColdMoldClosingWorkStationIndex_2()).getStatus().equals(WAIT_TRANSPORT))
                return 2;
        }else if (ferry.getLocationCoordinate().getX() == workstations.get(input.getColdMoldClosingWorkStationIndex_3()).getWorkPositionCoordinate().getX()){
            if (workstations.get(input.getColdMoldClosingWorkStationIndex_3()).getStatus().equals(WAIT_TRANSPORT))
                return 3;
        }else if (ferry.getLocationCoordinate().getX() == workstations.get(input.getColdMoldClosingWorkStationIndex_4()).getWorkPositionCoordinate().getX()){
            if (workstations.get(input.getColdMoldClosingWorkStationIndex_4()).getStatus().equals(WAIT_TRANSPORT))
                return 4;
        }

        return 0;
    }

    /**
     * 判断当前热砂工岗是否存在待搬运的产品
     * @param workstations
     * @param input
     * @return 返回待搬运的工岗标识，-1表示不存在待搬运的工岗
     */
    public Integer judgeHotWorkStationIsWaitingTransprot(List<WorkStation> workstations, MoldClosingIndexInput input){
        if (workstations.get(input.getHotMoldClosingWorkStationIndex_1()).getStatus().equals(WAIT_TRANSPORT)){
            return input.getHotMoldClosingWorkStationIndex_1();
        }else if (workstations.get(input.getHotMoldClosingWorkStationIndex_2()).getStatus().equals(WAIT_TRANSPORT)){
            return input.getHotMoldClosingWorkStationIndex_2();
        }else if (workstations.get(input.getHotMoldClosingWorkStationIndex_3()).getStatus().equals(WAIT_TRANSPORT)){
            return input.getHotMoldClosingWorkStationIndex_3();
        }else if (workstations.get(input.getHotMoldClosingWorkStationIndex_4()).getStatus().equals(WAIT_TRANSPORT)){
            return input.getHotMoldClosingWorkStationIndex_4();
        }

        return -1;
    }

    /**
     * 判断当前冷砂工岗是否存在待搬运的产品
     * @param workstations
     * @param input
     * @return 返回待搬运的工岗标识，-1表示不存在待搬运的工岗
     */
    public Integer judgeColdWorkStationIsWaitingTransprot(List<WorkStation> workstations, MoldClosingIndexInput input){
        if (workstations.get(input.getColdMoldClosingWorkStationIndex_1()).getStatus().equals(WAIT_TRANSPORT)){
            return input.getColdMoldClosingWorkStationIndex_1();
        }else if (workstations.get(input.getColdMoldClosingWorkStationIndex_2()).getStatus().equals(WAIT_TRANSPORT)){
            return input.getColdMoldClosingWorkStationIndex_2();
        }else if (workstations.get(input.getColdMoldClosingWorkStationIndex_3()).getStatus().equals(WAIT_TRANSPORT)){
            return input.getColdMoldClosingWorkStationIndex_3();
        }else if (workstations.get(input.getColdMoldClosingWorkStationIndex_4()).getStatus().equals(WAIT_TRANSPORT)){
            return input.getColdMoldClosingWorkStationIndex_4();
        }

        return -1;
    }

    /**
     * 判断当前子车装载的产品类型
     * @param product 判断的产品
     * @return 1表示产品为上，中或下的砂箱；2表示模具；3表示合模完成的砂箱
     */
    public int judgeProductType(Product product){
        if (product.getProductType().equals(UPPER_BOX) || product.getProductType().equals(MIDDLE_BOX) || product.getProductType().equals(LOWER_BOX)){
            return 1;
        }
        if (product.getProductType().equals(MOULD)){
            return 2;
        }
        return 3;
    }

    /**
     * 判断热砂工岗是否空闲
     * @param workstations 工岗列表
     * @param input 输入的标识
     * @return 1-4标识对应的热砂工岗，-1表示无工岗空闲
     */
    public Integer judgeWhichHotWorkStationFree(List<WorkStation> workstations, MoldClosingIndexInput input){
        if (workstations.get(input.getHotMoldClosingWorkStationIndex_1()).getStatus().equals(FREE)){
            return input.getHotMoldClosingWorkStationIndex_1();
        }else if (workstations.get(input.getHotMoldClosingWorkStationIndex_2()).getStatus().equals(FREE)){
            return input.getHotMoldClosingWorkStationIndex_2();
        }else if (workstations.get(input.getHotMoldClosingWorkStationIndex_3()).getStatus().equals(FREE)){
            return input.getHotMoldClosingWorkStationIndex_3();
        }else if (workstations.get(input.getHotMoldClosingWorkStationIndex_4()).getStatus().equals(FREE)){
            return input.getHotMoldClosingWorkStationIndex_4();
        }

        return -1;
    }

    /**
     * 判断冷砂工岗是否空闲
     * @param workstations 工岗列表
     * @param input 输入的标识
     * @return 1-4标识对应的热砂工岗，-1表示无工岗空闲
     */
    public Integer judgeWhichColdWorkStationFree(List<WorkStation> workstations, MoldClosingIndexInput input){
        if (workstations.get(input.getColdMoldClosingWorkStationIndex_1()).getStatus().equals(FREE)){
            return input.getColdMoldClosingWorkStationIndex_1();
        }else if (workstations.get(input.getColdMoldClosingWorkStationIndex_2()).getStatus().equals(FREE)){
            return input.getColdMoldClosingWorkStationIndex_2();
        }else if (workstations.get(input.getColdMoldClosingWorkStationIndex_3()).getStatus().equals(FREE)){
            return input.getColdMoldClosingWorkStationIndex_3();
        }else if (workstations.get(input.getColdMoldClosingWorkStationIndex_4()).getStatus().equals(FREE)){
            return input.getColdMoldClosingWorkStationIndex_4();
        }

        return -1;
    }

    /**
     * 判断哪个热砂工岗待配对
     * @param workstations  工岗列表
     * @param input 输入的标识
     * @return 1-4标识对应的热砂工岗，-1表示无工岗待配对
     */
    public Integer findPairHotWorkStation(List<WorkStation> workstations, MoldClosingIndexInput input){
        if (workstations.get(input.getHotMoldClosingWorkStationIndex_1()).getStatus().equals(WAIT_PAIR)){
            return input.getHotMoldClosingWorkStationIndex_1();
        }else if (workstations.get(input.getHotMoldClosingWorkStationIndex_2()).getStatus().equals(WAIT_PAIR)){
            return input.getHotMoldClosingWorkStationIndex_2();
        }else if (workstations.get(input.getHotMoldClosingWorkStationIndex_3()).getStatus().equals(WAIT_PAIR)){
            return input.getHotMoldClosingWorkStationIndex_3();
        }else if (workstations.get(input.getHotMoldClosingWorkStationIndex_4()).getStatus().equals(WAIT_PAIR)){
            return input.getHotMoldClosingWorkStationIndex_4();
        }

        return -1;
    }

    /**
     * 判断哪个冷砂工岗待配对
     * @param workstations  工岗列表
     * @param input 输入的标识
     * @return 1-4标识对应的热砂工岗，-1表示无工岗待配对
     */
    public Integer findPairColdWorkStation(List<WorkStation> workstations, MoldClosingIndexInput input){
        if (workstations.get(input.getColdMoldClosingWorkStationIndex_1()).getStatus().equals(WAIT_PAIR)){
            return input.getColdMoldClosingWorkStationIndex_1();
        }else if (workstations.get(input.getColdMoldClosingWorkStationIndex_2()).getStatus().equals(WAIT_PAIR)){
            return input.getColdMoldClosingWorkStationIndex_2();
        }else if (workstations.get(input.getColdMoldClosingWorkStationIndex_3()).getStatus().equals(WAIT_PAIR)){
            return input.getColdMoldClosingWorkStationIndex_3();
        }else if (workstations.get(input.getColdMoldClosingWorkStationIndex_4()).getStatus().equals(WAIT_PAIR)){
            return input.getColdMoldClosingWorkStationIndex_4();
        }

        return -1;
    }

    /**
     * 生成合模后的产品
     * @param products 合模的产品列表
     * @return 合模后的产品
     */
    public Product createMoldClosingProduct(List<Product> products) throws SimulationException {
        /**
         * 生成雪花id
         */
        Long id = idWorker.nextId();
        ProductTypeEnum productType = null;
        String name = "合模后的砂壳箱";

        for (Product product : products){
            if (product.getProductType().equals(UPPER_BOX)){
                productType = ProductTypeEnum.UPPER_SAND_SHELL_BOX;
                name = "合模后的上砂壳箱";
            }else if (product.getProductType().equals(LOWER_BOX)){
                productType = ProductTypeEnum.LOWER_SAND_SHELL_BOX;
                name = "合模后的下砂壳箱";
            }
        }
        if (productType == null)
            throw new SimulationException("合模完成的产品列表中无上或下砂箱，There are no upper or lower sand boxes in the list of products completed by mold closing");
        /**
         * 生成合模后的产品
         */
        Product product = new Product(id, name, products.getFirst().getLevel(), productType, FINISH_MOLD_CLOSING, GOING_SANDBLASTING,
                products.getFirst().getOrderProductType(), false, 0.0, products.getFirst().getOutBoundTime(), products.getFirst().getMoldClosingTime(),
                products.getFirst().getSandblastingTime(), products.getFirst().getMoldOpeningTime(), products.getFirst().getSprayTime(), products.getFirst().getClosingBoxTime(),
                products.getFirst().getPouringTime(), products.getFirst().getCoolingTime(), products.getFirst().getUnboxingTime(), products);

        return product;
    }

    /**
     * 判断热砂母车在哪个工岗
     * @param ferry
     * @param workstations
     * @param input
     * @return
     */
    public int judgeHotFerryInWhichWorkstation(Ferry ferry, List<WorkStation> workstations, MoldClosingIndexInput input){
        if (ferry.getLocationCoordinate().getX() ==
                workstations.get(input.getHotMoldClosingWorkStationIndex_1()).getWorkPositionCoordinate().getX()){
            return input.getHotMoldClosingWorkStationIndex_1();
        }else if (ferry.getLocationCoordinate().getX() ==
                workstations.get(input.getHotMoldClosingWorkStationIndex_2()).getWorkPositionCoordinate().getX()) {
            return input.getHotMoldClosingWorkStationIndex_2();
        }else if (ferry.getLocationCoordinate().getX() ==
                workstations.get(input.getHotMoldClosingWorkStationIndex_3()).getWorkPositionCoordinate().getX()) {
            return input.getHotMoldClosingWorkStationIndex_3();
        }else if (ferry.getLocationCoordinate().getX() ==
                workstations.get(input.getHotMoldClosingWorkStationIndex_4()).getWorkPositionCoordinate().getX()) {
            return input.getHotMoldClosingWorkStationIndex_4();
        }

        return -1;
    }

    /**
     * 判断热砂母车在哪个工岗
     * @param ferry
     * @param workstations
     * @param input
     * @return
     */
    public int judgeColdFerryInWhichWorkstation(Ferry ferry, List<WorkStation> workstations, MoldClosingIndexInput input){
        if (ferry.getLocationCoordinate().getX() ==
                workstations.get(input.getColdMoldClosingWorkStationIndex_1()).getWorkPositionCoordinate().getX()){

            return input.getColdMoldClosingWorkStationIndex_1();
        }else if (ferry.getLocationCoordinate().getX() ==
                workstations.get(input.getColdMoldClosingWorkStationIndex_2()).getWorkPositionCoordinate().getX()) {

            return input.getColdMoldClosingWorkStationIndex_2();
        }else if (ferry.getLocationCoordinate().getX() ==
                workstations.get(input.getColdMoldClosingWorkStationIndex_3()).getWorkPositionCoordinate().getX()) {

            return input.getColdMoldClosingWorkStationIndex_3();
        }else if (ferry.getLocationCoordinate().getX() ==
                workstations.get(input.getColdMoldClosingWorkStationIndex_4()).getWorkPositionCoordinate().getX()) {

            return input.getColdMoldClosingWorkStationIndex_4();
        }

        return -1;
    }

    /**
     * 判断热砂合模区域的所有工岗是否处于工作状态
     * @param workStations 热砂合模区域的工岗集合
     * @param input 热砂合模区域的仿真输入
     * @return true表示所有的工岗都在工作中，false表示有工岗处于空闲状态
     */
    public boolean judgeHotMoldClosingWorkStationAllInWorking(List<WorkStation> workStations, MoldClosingIndexInput input) {
        if (workStations.get(input.getHotMoldClosingWorkStationIndex_1()).getStatus().equals(WorkstationStatusEnum.FREE) ||
                workStations.get(input.getHotMoldClosingWorkStationIndex_1()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){

            return false;
        }else if (workStations.get(input.getHotMoldClosingWorkStationIndex_2()).getStatus().equals(WorkstationStatusEnum.FREE) ||
                workStations.get(input.getHotMoldClosingWorkStationIndex_2()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){

            return false;
        }else if (workStations.get(input.getHotMoldClosingWorkStationIndex_3()).getStatus().equals(WorkstationStatusEnum.FREE) ||
                workStations.get(input.getHotMoldClosingWorkStationIndex_3()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){

            return false;
        }else if (workStations.get(input.getHotMoldClosingWorkStationIndex_4()).getStatus().equals(WorkstationStatusEnum.FREE) ||
                workStations.get(input.getHotMoldClosingWorkStationIndex_4()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){

            return false;
        }

        return true;
    }

    /**
     * 判断冷砂合模区域的所有工岗是否处于工作状态
     * @param workStations 冷砂合模区域的工岗集合
     * @param input 冷砂合模区域的仿真输入
     * @return true表示所有的工岗都在工作中，false表示有工岗处于空闲状态
     */
    public boolean judgeColdMoldClosingWorkStationAllInWorking(List<WorkStation> workStations, MoldClosingIndexInput input) {
        if (workStations.get(input.getColdMoldClosingWorkStationIndex_1()).getStatus().equals(WorkstationStatusEnum.FREE) ||
                workStations.get(input.getColdMoldClosingWorkStationIndex_1()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){

            return false;
        }else if (workStations.get(input.getColdMoldClosingWorkStationIndex_2()).getStatus().equals(WorkstationStatusEnum.FREE) ||
                workStations.get(input.getColdMoldClosingWorkStationIndex_2()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){

            return false;
        }else if (workStations.get(input.getColdMoldClosingWorkStationIndex_3()).getStatus().equals(WorkstationStatusEnum.FREE) ||
                workStations.get(input.getColdMoldClosingWorkStationIndex_3()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){

            return false;
        }else if (workStations.get(input.getColdMoldClosingWorkStationIndex_4()).getStatus().equals(WorkstationStatusEnum.FREE) ||
                workStations.get(input.getColdMoldClosingWorkStationIndex_4()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){

            return false;
        }

        return true;
    }
}
