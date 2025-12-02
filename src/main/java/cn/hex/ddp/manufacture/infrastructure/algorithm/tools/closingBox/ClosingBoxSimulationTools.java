package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.closingBox;

import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductAfootEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductFinishEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Ferry;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.SubCar;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.WorkStation;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SnowflakeIdGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum.WAIT_TRANSPORT;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/21 5:17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClosingBoxSimulationTools {
    /**
     * 雪花函数生成id
     */
    private SnowflakeIdGenerator idWorker = new SnowflakeIdGenerator(3, 3);

    /**
     * 判断热砂合箱母车是否到达工岗位置且工岗处于待搬运
     * @return 0表示没有，1-4表示1-4号工岗
     */
    public int judgeHotClosingBoxFerryInWorkstation(Ferry ferry, List<WorkStation> workstations, ClosingBoxIndexInput input){
        if (ferry.getLocationCoordinate().getX() == workstations.get(input.getHotClosingBoxWorkStationIndex_1()).getWorkPositionCoordinate().getX()){
            if (workstations.get(input.getHotClosingBoxWorkStationIndex_1()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT))
                return 1;

        }else if (ferry.getLocationCoordinate().getX() == workstations.get(input.getHotClosingBoxWorkStationIndex_2()).getWorkPositionCoordinate().getX()){
            if (workstations.get(input.getHotClosingBoxWorkStationIndex_2()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT))
                return 2;

        }else if (ferry.getLocationCoordinate().getX() == workstations.get(input.getHotClosingBoxWorkStationIndex_3()).getWorkPositionCoordinate().getX()){
            if (workstations.get(input.getHotClosingBoxWorkStationIndex_3()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT))
                return 3;

        }else if (ferry.getLocationCoordinate().getX() == workstations.get(input.getHotClosingBoxWorkStationIndex_4()).getWorkPositionCoordinate().getX()){
            if (workstations.get(input.getHotClosingBoxWorkStationIndex_4()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT))
                return 4;

        }

        return 0;
    }

    /**
     * 判断当前热砂工岗是否存在待搬运的产品
     * @param workstations 工岗列表
     * @param input 合箱区仿真输入的标识类
     * @return 返回待搬运的工岗标识，-1表示不存在待搬运的工岗
     */
    public int judgeHotClosingBoxWorkStationIsWaitingTransprot(List<WorkStation> workstations, ClosingBoxIndexInput input){
        if (workstations.get(input.getHotClosingBoxWorkStationIndex_1()).getStatus().equals(WAIT_TRANSPORT)){

            return input.getHotClosingBoxWorkStationIndex_1();
        }else if (workstations.get(input.getHotClosingBoxWorkStationIndex_2()).getStatus().equals(WAIT_TRANSPORT)){

            return input.getHotClosingBoxWorkStationIndex_2();
        }else if (workstations.get(input.getHotClosingBoxWorkStationIndex_3()).getStatus().equals(WAIT_TRANSPORT)){

            return input.getHotClosingBoxWorkStationIndex_3();
        }else if (workstations.get(input.getHotClosingBoxWorkStationIndex_4()).getStatus().equals(WAIT_TRANSPORT)){

            return input.getHotClosingBoxWorkStationIndex_4();
        }

        return -1;
    }

    /**
     * 判断合箱区工岗是否待配对并且车辆的产品是否与其配对
     * @param subCar 子车
     * @param workstations 工岗列表
     * @param input 合箱区仿真输入的标识
     * @return 需要前往的待配对的工岗的标识,-1表示不需要，需要前往空闲工岗
     */
    public int judgeHotClosingBoxCarIsGoPairWorkStation(SubCar subCar, List<WorkStation> workstations, ClosingBoxIndexInput input){
        if (workstations.get(input.getHotClosingBoxWorkStationIndex_1()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){
            if (!workstations.get(input.getHotClosingBoxWorkStationIndex_1()).getProduct().getFirst().getProductType().equals
                    (subCar.getProduct().getProductType())){

                return input.getHotClosingBoxWorkStationIndex_1();
            }
        }else if (workstations.get(input.getHotClosingBoxWorkStationIndex_2()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){
            if (!workstations.get(input.getHotClosingBoxWorkStationIndex_2()).getProduct().getFirst().getProductType().equals
                    (subCar.getProduct().getProductType())){

                return input.getHotClosingBoxWorkStationIndex_2();
            }
        }else if (workstations.get(input.getHotClosingBoxWorkStationIndex_3()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){
            if (!workstations.get(input.getHotClosingBoxWorkStationIndex_3()).getProduct().getFirst().getProductType().equals
                    (subCar.getProduct().getProductType())){

                return input.getHotClosingBoxWorkStationIndex_3();
            }
        }else if (workstations.get(input.getHotClosingBoxWorkStationIndex_4()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){
            if (!workstations.get(input.getHotClosingBoxWorkStationIndex_4()).getProduct().getFirst().getProductType().equals
                    (subCar.getProduct().getProductType())){

                return input.getHotClosingBoxWorkStationIndex_4();
            }
        }

        return -1;
    }

    /**
     * 判断合箱区子车应前往哪个工岗
     * @param workstations 工岗列表
     * @param input 合箱区仿真输入的标识
     * @return 需要前往的待配对的工岗的标识，-1代表所有工岗被占用
     */
    public int judgeHotClosingBoxCarGoWhichWorkStation(List<WorkStation> workstations, ClosingBoxIndexInput input){
        if (workstations.get(input.getHotClosingBoxWorkStationIndex_1()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getHotClosingBoxWorkStationIndex_1();
        }else if (workstations.get(input.getHotClosingBoxWorkStationIndex_2()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getHotClosingBoxWorkStationIndex_2();
        }else if (workstations.get(input.getHotClosingBoxWorkStationIndex_3()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getHotClosingBoxWorkStationIndex_3();
        }else if (workstations.get(input.getHotClosingBoxWorkStationIndex_4()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getHotClosingBoxWorkStationIndex_4();
        }

        return -1;
    }

    /**
     * 生成合箱后的产品
     * @param products 合箱的产品列表
     * @return 合箱后的产品
     */
    public Product createClosingBoxProduct(List<Product> products) {
        /**
         * 生成雪花id
         */
        Long id = idWorker.nextId();
        ProductTypeEnum productType = ProductTypeEnum.WHOLE_SAND_SHELL_BOX;

        /**
         * 生成合箱后的产品
         */
        Product product = new Product(id, "合箱完成的砂箱", products.getFirst().getLevel(), productType, ProductFinishEnum.FINISH_CLOSING_BOX, ProductAfootEnum.GOING_POURING,
                products.getFirst().getOrderProductType(), false, 0.0, products.getFirst().getOutBoundTime(), products.getFirst().getMoldClosingTime(),
                products.getFirst().getSandblastingTime(), products.getFirst().getMoldOpeningTime(), products.getFirst().getSprayTime(), products.getFirst().getClosingBoxTime(),
                products.getFirst().getPouringTime(), products.getFirst().getCoolingTime(), products.getFirst().getUnboxingTime(), products);

        return product;
    }

    /**
     * 判断热砂合箱母车是否到达工岗位置且工岗处于待搬运
     * @return 0表示没有，1-5表示1-5号工岗
     */
    public int judgeColdClosingBoxFerryInWorkstation(Ferry ferry, List<WorkStation> workstations, ClosingBoxIndexInput input){
        if (ferry.getLocationCoordinate().getX() == workstations.get(input.getColdClosingBoxWorkStationIndex_1()).getWorkPositionCoordinate().getX()){
            if (workstations.get(input.getColdClosingBoxWorkStationIndex_1()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT))
                return 1;

        }else if (ferry.getLocationCoordinate().getX() == workstations.get(input.getColdClosingBoxWorkStationIndex_2()).getWorkPositionCoordinate().getX()){
            if (workstations.get(input.getColdClosingBoxWorkStationIndex_2()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT))
                return 2;

        }else if (ferry.getLocationCoordinate().getX() == workstations.get(input.getColdClosingBoxWorkStationIndex_3()).getWorkPositionCoordinate().getX()){
            if (workstations.get(input.getColdClosingBoxWorkStationIndex_3()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT))
                return 3;

        }else if (ferry.getLocationCoordinate().getX() == workstations.get(input.getColdClosingBoxWorkStationIndex_4()).getWorkPositionCoordinate().getX()){
            if (workstations.get(input.getColdClosingBoxWorkStationIndex_4()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT))
                return 4;

        }

        return 0;
    }

    /**
     * 判断当前热砂工岗是否存在待搬运的产品
     * @param workstations 工岗列表
     * @param input 合箱区仿真输入的标识类
     * @return 返回待搬运的工岗标识，-1表示不存在待搬运的工岗
     */
    public int judgeColdClosingBoxWorkStationIsWaitingTransprot(List<WorkStation> workstations, ClosingBoxIndexInput input){
        if (workstations.get(input.getColdClosingBoxWorkStationIndex_1()).getStatus().equals(WAIT_TRANSPORT)){

            return input.getColdClosingBoxWorkStationIndex_1();
        }else if (workstations.get(input.getColdClosingBoxWorkStationIndex_2()).getStatus().equals(WAIT_TRANSPORT)){

            return input.getColdClosingBoxWorkStationIndex_2();
        }else if (workstations.get(input.getColdClosingBoxWorkStationIndex_3()).getStatus().equals(WAIT_TRANSPORT)){

            return input.getColdClosingBoxWorkStationIndex_3();
        }else if (workstations.get(input.getColdClosingBoxWorkStationIndex_4()).getStatus().equals(WAIT_TRANSPORT)){

            return input.getColdClosingBoxWorkStationIndex_4();
        }

        return -1;
    }

    /**
     * 判断合箱区工岗是否待配对并且车辆的产品是否与其配对
     * @param subCar 子车
     * @param workstations 工岗列表
     * @param input 合箱区仿真输入的标识
     * @return 需要前往的待配对的工岗的标识,-1表示不需要，需要前往空闲工岗
     */
    public int judgeColdClosingBoxCarIsGoPairWorkStation(SubCar subCar, List<WorkStation> workstations, ClosingBoxIndexInput input){
        if (workstations.get(input.getColdClosingBoxWorkStationIndex_1()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){
            if (!workstations.get(input.getColdClosingBoxWorkStationIndex_1()).getProduct().getFirst().getProductType().equals
                    (subCar.getProduct().getProductType())){

                return input.getColdClosingBoxWorkStationIndex_1();
            }
        }else if (workstations.get(input.getColdClosingBoxWorkStationIndex_2()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){
            if (!workstations.get(input.getColdClosingBoxWorkStationIndex_2()).getProduct().getFirst().getProductType().equals
                    (subCar.getProduct().getProductType())){

                return input.getColdClosingBoxWorkStationIndex_2();
            }
        }else if (workstations.get(input.getColdClosingBoxWorkStationIndex_3()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){
            if (!workstations.get(input.getColdClosingBoxWorkStationIndex_3()).getProduct().getFirst().getProductType().equals
                    (subCar.getProduct().getProductType())){

                return input.getColdClosingBoxWorkStationIndex_3();
            }
        }else if (workstations.get(input.getColdClosingBoxWorkStationIndex_4()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){
            if (!workstations.get(input.getColdClosingBoxWorkStationIndex_4()).getProduct().getFirst().getProductType().equals
                    (subCar.getProduct().getProductType())){

                return input.getColdClosingBoxWorkStationIndex_4();
            }
        }

        return -1;
    }

    /**
     * 判断合箱区子车应前往哪个工岗
     * @param workstations 工岗列表
     * @param input 合箱区仿真输入的标识
     * @return 需要前往的待配对的工岗的标识，-1代表所有工岗被占用
     */
    public int judgeColdClosingBoxCarGoWhichWorkStation(List<WorkStation> workstations, ClosingBoxIndexInput input){
        if (workstations.get(input.getColdClosingBoxWorkStationIndex_1()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getColdClosingBoxWorkStationIndex_1();
        }else if (workstations.get(input.getColdClosingBoxWorkStationIndex_2()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getColdClosingBoxWorkStationIndex_2();
        }else if (workstations.get(input.getColdClosingBoxWorkStationIndex_3()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getColdClosingBoxWorkStationIndex_3();
        }else if (workstations.get(input.getColdClosingBoxWorkStationIndex_4()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getColdClosingBoxWorkStationIndex_4();
        }

        return -1;
    }

    /**
     * 判断热砂母车在哪个工岗
     * @param ferry
     * @param workStations
     * @param input
     * @return
     */
    public int judgeHotFerryInWhichWorkstation(Ferry ferry, List<WorkStation> workStations, ClosingBoxIndexInput input) {
        if (ferry.getLocationCoordinate().getX() ==
                workStations.get(input.getHotClosingBoxWorkStationIndex_1()).getWorkPositionCoordinate().getX()){

            return input.getHotClosingBoxWorkStationIndex_1();
        }else if (ferry.getLocationCoordinate().getX() ==
                workStations.get(input.getHotClosingBoxWorkStationIndex_2()).getWorkPositionCoordinate().getX()) {

            return input.getHotClosingBoxWorkStationIndex_2();
        }else if (ferry.getLocationCoordinate().getX() ==
                workStations.get(input.getHotClosingBoxWorkStationIndex_3()).getWorkPositionCoordinate().getX()) {

            return input.getHotClosingBoxWorkStationIndex_3();
        }else if (ferry.getLocationCoordinate().getX() ==
                workStations.get(input.getHotClosingBoxWorkStationIndex_4()).getWorkPositionCoordinate().getX()) {

            return input.getHotClosingBoxWorkStationIndex_4();
        }

        return -1;
    }

    /**
     * 判断热砂母车在哪个工岗
     * @param ferry
     * @param workStations
     * @param input
     * @return
     */
    public int judgeColdFerryInWhichWorkstation(Ferry ferry, List<WorkStation> workStations, ClosingBoxIndexInput input) {
        if (ferry.getLocationCoordinate().getX() ==
                workStations.get(input.getColdClosingBoxWorkStationIndex_1()).getWorkPositionCoordinate().getX()){

            return input.getColdClosingBoxWorkStationIndex_1();
        }else if (ferry.getLocationCoordinate().getX() ==
                workStations.get(input.getColdClosingBoxWorkStationIndex_2()).getWorkPositionCoordinate().getX()) {

            return input.getColdClosingBoxWorkStationIndex_2();
        }else if (ferry.getLocationCoordinate().getX() ==
                workStations.get(input.getColdClosingBoxWorkStationIndex_3()).getWorkPositionCoordinate().getX()) {

            return input.getColdClosingBoxWorkStationIndex_3();
        }else if (ferry.getLocationCoordinate().getX() ==
                workStations.get(input.getColdClosingBoxWorkStationIndex_4()).getWorkPositionCoordinate().getX()) {

            return input.getColdClosingBoxWorkStationIndex_4();
        }

        return -1;
    }

    /**
     * 判断热砂合箱区域的所有工岗是否处于工作状态
     * @param workStations 热砂合箱区域的工岗集合
     * @param input 热砂合箱区域的仿真输入
     * @return true表示所有的工岗都在工作中，false表示有工岗处于空闲状态
     */
    public boolean judgeHotClosingBoxWorkStationAllInWorking(List<WorkStation> workStations, ClosingBoxIndexInput input) {
        if (workStations.get(input.getHotClosingBoxWorkStationIndex_1()).getStatus().equals(WorkstationStatusEnum.FREE) ||
                workStations.get(input.getHotClosingBoxWorkStationIndex_1()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){

            return false;
        }else if (workStations.get(input.getHotClosingBoxWorkStationIndex_2()).getStatus().equals(WorkstationStatusEnum.FREE) ||
                workStations.get(input.getHotClosingBoxWorkStationIndex_2()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){

            return false;
        }else if (workStations.get(input.getHotClosingBoxWorkStationIndex_3()).getStatus().equals(WorkstationStatusEnum.FREE) ||
                workStations.get(input.getHotClosingBoxWorkStationIndex_3()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){

            return false;
        }else if (workStations.get(input.getHotClosingBoxWorkStationIndex_4()).getStatus().equals(WorkstationStatusEnum.FREE) ||
                workStations.get(input.getHotClosingBoxWorkStationIndex_4()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){

            return false;
        }

        return true;
    }

    /**
     * 判断冷砂合箱区域的所有工岗是否处于工作状态
     * @param workStations 冷砂合箱区域的工岗集合
     * @param input 冷砂合箱区域的仿真输入
     * @return true表示所有的工岗都在工作中，false表示有工岗处于空闲状态
     */
    public boolean judgeColdClosingBoxWorkStationAllInWorking(List<WorkStation> workStations, ClosingBoxIndexInput input) {
        if (workStations.get(input.getColdClosingBoxWorkStationIndex_1()).getStatus().equals(WorkstationStatusEnum.FREE) ||
                workStations.get(input.getColdClosingBoxWorkStationIndex_1()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){

            return false;
        }else if (workStations.get(input.getColdClosingBoxWorkStationIndex_2()).getStatus().equals(WorkstationStatusEnum.FREE) ||
                workStations.get(input.getColdClosingBoxWorkStationIndex_2()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){

            return false;
        }else if (workStations.get(input.getColdClosingBoxWorkStationIndex_3()).getStatus().equals(WorkstationStatusEnum.FREE) ||
                workStations.get(input.getColdClosingBoxWorkStationIndex_3()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){

            return false;
        }else if (workStations.get(input.getColdClosingBoxWorkStationIndex_4()).getStatus().equals(WorkstationStatusEnum.FREE) ||
                workStations.get(input.getColdClosingBoxWorkStationIndex_4()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){

            return false;
        }

        return true;
    }
}
