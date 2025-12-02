package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.openBox;

import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.OrderProductTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductAfootEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductFinishEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Ferry;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.WorkStation;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SnowflakeIdGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum.WAIT_TRANSPORT;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/8/1 16:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenBoxSimulationTools {
    /**
     * 雪花函数生成id
     */
    private SnowflakeIdGenerator idWorker = new SnowflakeIdGenerator(6, 6);

    /**
     * 判断热砂开箱母车是否到达工岗位置且工岗处于待搬运
     * @param ferry 热砂母车
     * @param workStations 热砂工岗
     * @param input 开箱区仿真输入标识
     * @return -1表示没有，0-3表示1-4号工岗的标识
     */
    public int judgeHotOpenBoxFerryInWorkstation(Ferry ferry, List<WorkStation> workStations, OpenBoxIndexInput input) {
        if (ferry.getLocationCoordinate().getX() == workStations.get(input.getHotWorkStation_1()).getWorkPositionCoordinate().getX()){
            if (workStations.get(input.getHotWorkStation_1()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT))
                return input.getHotWorkStation_1();

        }else if (ferry.getLocationCoordinate().getX() == workStations.get(input.getHotWorkStation_2()).getWorkPositionCoordinate().getX()){
            if (workStations.get(input.getHotWorkStation_2()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT))
                return input.getHotWorkStation_2();

        }else if (ferry.getLocationCoordinate().getX() == workStations.get(input.getHotWorkStation_3()).getWorkPositionCoordinate().getX()){
            if (workStations.get(input.getHotWorkStation_3()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT))
                return input.getHotWorkStation_3();

        }else if (ferry.getLocationCoordinate().getX() == workStations.get(input.getHotWorkStation_4()).getWorkPositionCoordinate().getX()){
            if (workStations.get(input.getHotWorkStation_4()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT))
                return input.getHotWorkStation_4();

        }

        return -1;
    }

    /**
     * 生成铸件产品
     * @param orderProductType 产品的类型：热砂或冷砂
     * @return 生成的铸件产品
     */
    public Product createCastingProduct(OrderProductTypeEnum orderProductType) {
        /**
         * 生成雪花id
         */
        Long id = idWorker.nextId();

        /**
         * 生成合箱后的产品
         */
        Product product = new Product(id, "铸件产品", null, ProductTypeEnum.CASTING, ProductFinishEnum.FINISH_UNBOXING, ProductAfootEnum.DOING_UNBOXING,
                orderProductType, false, 0.0, 0.0, 0.0, 0.0, 0.0,
                0.0, 0.0, 0.0, 0.0, 0.0, new ArrayList<>());

        return product;
    }

    /**
     * 判断当前热砂开箱工岗是否存在待搬运的产品
     * @param workStations 工岗列表
     * @param input 开箱区仿真输入的标识类
     * @return 返回待搬运的工岗标识，-1表示不存在待搬运的工岗
     */
    public int judgeHotOpenBoxWorkStationIsWaitingTransprot(List<WorkStation> workStations, OpenBoxIndexInput input) {
        if (workStations.get(input.getHotWorkStation_1()).getStatus().equals(WAIT_TRANSPORT)){

            return input.getHotWorkStation_1();
        }else if (workStations.get(input.getHotWorkStation_2()).getStatus().equals(WAIT_TRANSPORT)){

            return input.getHotWorkStation_2();
        }else if (workStations.get(input.getHotWorkStation_3()).getStatus().equals(WAIT_TRANSPORT)){

            return input.getHotWorkStation_3();
        }else if (workStations.get(input.getHotWorkStation_4()).getStatus().equals(WAIT_TRANSPORT)){

            return input.getHotWorkStation_4();
        }

        return -1;
    }

    /**
     * 判断热砂开箱车辆前往哪个工岗
     * @param workStations 热砂开箱工岗列表
     * @param input 开箱区仿真输入标识
     * @return 工岗的标识，-1表示出错
     */
    public int judgeHotOpenBoxCarGoWhichWorkStation(List<WorkStation> workStations, OpenBoxIndexInput input) {
        if (workStations.get(input.getHotWorkStation_1()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getHotWorkStation_1();
        }else if (workStations.get(input.getHotWorkStation_2()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getHotWorkStation_2();
        }else if (workStations.get(input.getHotWorkStation_3()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getHotWorkStation_3();
        }else if (workStations.get(input.getHotWorkStation_4()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getHotWorkStation_4();
        }

        return -1;
    }

    /**
     * 判断热砂母车在哪个工岗
     * @param ferry 热砂开箱母车
     * @param workStations 热砂开箱工岗列表
     * @param input 开箱区仿真输入标识
     * @return 母车所在的工岗标识，-1表示出错
     */
    public int judgeHotFerryInWhichWorkstation(Ferry ferry, List<WorkStation> workStations, OpenBoxIndexInput input) {
        if (ferry.getLocationCoordinate().getX() ==
                workStations.get(input.getHotWorkStation_1()).getWorkPositionCoordinate().getX()){

            return input.getHotWorkStation_1();
        }else if (ferry.getLocationCoordinate().getX() ==
                workStations.get(input.getHotWorkStation_2()).getWorkPositionCoordinate().getX()) {

            return input.getHotWorkStation_2();
        }else if (ferry.getLocationCoordinate().getX() ==
                workStations.get(input.getHotWorkStation_3()).getWorkPositionCoordinate().getX()) {

            return input.getHotWorkStation_3();
        }else if (ferry.getLocationCoordinate().getX() ==
                workStations.get(input.getHotWorkStation_4()).getWorkPositionCoordinate().getX()) {

            return input.getHotWorkStation_4();
        }

        return -1;
    }

    /**
     * 判断冷热混合砂箱开箱母车是否到达工岗位置且工岗处于待搬运
     * @param ferry 冷热混合砂箱母车
     * @param workStations 冷热混合砂箱工岗
     * @param input 开箱区仿真输入标识
     * @return -1表示没有，0-3表示1-4号工岗的标识
     */
    public int judgeColdAndHotOpenBoxFerryInWorkstation(Ferry ferry, List<WorkStation> workStations, OpenBoxIndexInput input) {
        if (ferry.getLocationCoordinate().getX() == workStations.get(input.getColdAndHotWorkStation_1()).getWorkPositionCoordinate().getX()){
            if (workStations.get(input.getColdAndHotWorkStation_1()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT))
                return input.getColdAndHotWorkStation_1();

        }else if (ferry.getLocationCoordinate().getX() == workStations.get(input.getColdAndHotWorkStation_2()).getWorkPositionCoordinate().getX()){
            if (workStations.get(input.getColdAndHotWorkStation_2()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT))
                return input.getColdAndHotWorkStation_2();

        }else if (ferry.getLocationCoordinate().getX() == workStations.get(input.getColdAndHotWorkStation_3()).getWorkPositionCoordinate().getX()){
            if (workStations.get(input.getColdAndHotWorkStation_3()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT))
                return input.getColdAndHotWorkStation_3();

        }else if (ferry.getLocationCoordinate().getX() == workStations.get(input.getColdAndHotWorkStation_4()).getWorkPositionCoordinate().getX()){
            if (workStations.get(input.getColdAndHotWorkStation_4()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT))
                return input.getColdAndHotWorkStation_4();

        }

        return -1;
    }

    /**
     * 判断当前冷热混合砂箱开箱工岗是否存在待搬运的产品
     * @param workStations 工岗列表
     * @param input 开箱区仿真输入的标识类
     * @return 返回待搬运的工岗标识，-1表示不存在待搬运的工岗
     */
    public int judgeColdAndHotOpenBoxWorkStationIsWaitingTransprot(List<WorkStation> workStations, OpenBoxIndexInput input) {
        if (workStations.get(input.getColdAndHotWorkStation_1()).getStatus().equals(WAIT_TRANSPORT)){

            return input.getColdAndHotWorkStation_1();
        }else if (workStations.get(input.getColdAndHotWorkStation_2()).getStatus().equals(WAIT_TRANSPORT)){

            return input.getColdAndHotWorkStation_2();
        }else if (workStations.get(input.getColdAndHotWorkStation_3()).getStatus().equals(WAIT_TRANSPORT)){

            return input.getColdAndHotWorkStation_3();
        }else if (workStations.get(input.getColdAndHotWorkStation_4()).getStatus().equals(WAIT_TRANSPORT)){

            return input.getColdAndHotWorkStation_4();
        }

        return -1;
    }

    /**
     * 判断冷热混合砂箱开箱车辆前往哪个工岗
     * @param workStations 冷热混合砂箱开箱工岗列表
     * @param input 开箱区仿真输入标识
     * @return 工岗的标识，-1表示出错
     */
    public int judgeColdAndHotOpenBoxCarGoWhichWorkStation(List<WorkStation> workStations, OpenBoxIndexInput input) {
        if (workStations.get(input.getColdAndHotWorkStation_1()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getColdAndHotWorkStation_1();
        }else if (workStations.get(input.getColdAndHotWorkStation_2()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getColdAndHotWorkStation_2();
        }else if (workStations.get(input.getColdAndHotWorkStation_3()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getColdAndHotWorkStation_3();
        }else if (workStations.get(input.getColdAndHotWorkStation_4()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getColdAndHotWorkStation_4();
        }

        return -1;
    }

    /**
     * 判断冷热混合砂箱母车在哪个工岗
     * @param ferry 冷热混合砂箱开箱母车
     * @param workStations 冷热混合砂箱开箱工岗列表
     * @param input 开箱区仿真输入标识
     * @return 母车所在的工岗标识，-1表示出错
     */
    public int judgeColdAndHotFerryInWhichWorkstation(Ferry ferry, List<WorkStation> workStations, OpenBoxIndexInput input) {
        if (ferry.getLocationCoordinate().getX() ==
                workStations.get(input.getColdAndHotWorkStation_1()).getWorkPositionCoordinate().getX()){

            return input.getColdAndHotWorkStation_1();
        }else if (ferry.getLocationCoordinate().getX() ==
                workStations.get(input.getColdAndHotWorkStation_2()).getWorkPositionCoordinate().getX()) {

            return input.getColdAndHotWorkStation_2();
        }else if (ferry.getLocationCoordinate().getX() ==
                workStations.get(input.getColdAndHotWorkStation_3()).getWorkPositionCoordinate().getX()) {

            return input.getColdAndHotWorkStation_3();
        }else if (ferry.getLocationCoordinate().getX() ==
                workStations.get(input.getColdAndHotWorkStation_4()).getWorkPositionCoordinate().getX()) {

            return input.getColdAndHotWorkStation_4();
        }

        return -1;
    }

    /**
     * 判断热砂开箱区域的所有工岗是否处于工作状态
     * @param workStations 热砂开箱区域的工岗集合
     * @param input 热砂开箱区域的仿真输入
     * @return true表示所有的工岗都在工作中，false表示有工岗处于空闲状态
     */
    public boolean judgeHotOpenBoxWorkStationAllInWorking(List<WorkStation> workStations, OpenBoxIndexInput input) {
        if (workStations.get(input.getHotWorkStation_1()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return false;
        }else if (workStations.get(input.getHotWorkStation_2()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return false;
        }else if (workStations.get(input.getHotWorkStation_3()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return false;
        }else if (workStations.get(input.getHotWorkStation_4()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return false;
        }

        return true;
    }

    /**
     * 判断冷热混合开箱区域的所有工岗是否处于工作状态
     * @param workStations 冷热混合开箱区域的工岗集合
     * @param input 冷热混合开箱区域的仿真输入
     * @return true表示所有的工岗都在工作中，false表示有工岗处于空闲状态
     */
    public boolean judgeColdOpenBoxWorkStationAllInWorking(List<WorkStation> workStations, OpenBoxIndexInput input) {
        if (workStations.get(input.getColdAndHotWorkStation_1()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return false;
        }else if (workStations.get(input.getColdAndHotWorkStation_2()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return false;
        }else if (workStations.get(input.getColdAndHotWorkStation_3()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return false;
        }else if (workStations.get(input.getColdAndHotWorkStation_4()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return false;
        }

        return true;
    }
}
