package cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.eventGeneration;

import cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum;
import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentNameEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathNameEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationNameEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.resource.Resource;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums.ResourceState;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.input.ResourceInput;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum.CARRY_SUB_CAR;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum.NOT_CARRY_SUB_CAR;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum.*;

/**
 * @Author: bobo
 * @Description: 用决策树进行预测需要的信息
 * @DateTime: 2025/3/19 15:20
 **/

@Data
@Slf4j
public class PredictInfo {

    /*
     * features存储决策树进行决策需要的状态信息
     * MAP的key为一个资源的名字枚举,比如合箱区子车
     * MAP的value为一个信息的列表,比如[满载枚举,产品对象,坐标对象]
     */
    private Map<Enum<?>, List<?>> features = new HashMap<>();

    // 与车辆名字枚举关联
    private CarNameEnum relatedCar;

    //该车生成事件需要的全部资源枚举
    private List<Enum<?>> relatedResource;

    public PredictInfo(CarNameEnum relatedCar, List<Enum<?>> relatedResource,
                       ResourceInput resourceInput, Resource resource) {
        this.relatedCar = relatedCar;
        this.relatedResource = relatedResource;
        this.features = this.inputToFeatures(resourceInput,resource);
    }

    // 获取目标资源的特征信息列表
    public List<?> getKeyResourceFeatures(Enum<?> keyResouceEnum) {
        return this.features.get(keyResouceEnum);
    }

    //将当前资源转换为决策需要的状态信息
    public Map<Enum<?>, List<?>> inputToFeatures(ResourceInput resourceInput, Resource resource) {
        Map<Enum<?>, List<?>> resultFeatures = new HashMap<>();
        //遍历资源枚举列表中的资源枚举
        for(Enum<?> resourceEnum : relatedResource) {
            // 如果名字枚举为车的名字，将resource赋给carEnum
            if(resourceEnum instanceof CarNameEnum carEnum) {
                // 获取车辆空闲/忙碌状态
                ResourceState carState = resource.getCarResMap().get(carEnum);
                // 如果资源为子车
                if(resourceInput.isSubCarByName(carEnum)){
                    SubCar subCar = resourceInput.getSubCarByName(carEnum);
                    List<Object> subCarFeatures = new ArrayList<>();
                    // 添加子车忙闲状态
                    subCarFeatures.add(carState);
                    // 添加子车的坐标
                    subCarFeatures.add(subCar.getLocationCoordinate());
                    // 添加子车负载类型,这个信息和子车上的物品重复了,决策时用不到
                    List<SubCarStatusEnum> statusEnums = subCar.getSubCarState();
//                    if(statusEnums.contains(EMPTY_LOAD)){
//                        subCarFeatures.add(EMPTY_LOAD);
//                    }else if(statusEnums.contains(FULL_LOAD)){
//                        subCarFeatures.add(FULL_LOAD);
//                    }else {
//                        log.error("子车{}状态中没有存负载情况", carEnum.name());
//                    }
                    // 添加子母车关系
                    if(statusEnums.contains(SUB_CAR_IN_FERRY)){
                        subCarFeatures.add(SUB_CAR_IN_FERRY);
                    }else if(statusEnums.contains(SUB_CAR_NOT_IN_FERRY)){
                        subCarFeatures.add(SUB_CAR_NOT_IN_FERRY);
                    }
                    // 添加子车的物品类型
                    // 如果没有物品，设置物品类型为空
                    if(subCar.getProduct() == null){
                        subCarFeatures.add(ProductTypeEnum.EMPTY);
                    }else {
                        ProductTypeEnum productTypeEnum = subCar.getProduct().getProductType();
                        if (productTypeEnum != null) {
                            subCarFeatures.add(productTypeEnum);
                        }
                    }
                    // 将子车的相关特征放入特征列表中
                    resultFeatures.put(resourceEnum, subCarFeatures);
                }
                // 如果资源为母车
                else {
                    Ferry ferry = resourceInput.getFerryByName(carEnum);
                    List<Object> ferryFeatures = new ArrayList<>();
                    // 添加子车忙闲状态
                    ferryFeatures.add(carState);
                    // 添加母车的坐标
                    ferryFeatures.add(ferry.getLocationCoordinate());
                    // 添加母车是否搭载子车的状态
                    List<FerryStatusEnum> statusEnums = ferry.getFerryState();
                    if(statusEnums.contains(CARRY_SUB_CAR)){
                        ferryFeatures.add(CARRY_SUB_CAR);
                    }else if(statusEnums.contains(NOT_CARRY_SUB_CAR)){
                        ferryFeatures.add(NOT_CARRY_SUB_CAR);
                    }else {
                        log.error("母车{}状态中没有存是否搭载子车", carEnum.name());
                    }
                    // 将母车的相关特征放入特征列表中
                    resultFeatures.put(resourceEnum, ferryFeatures);
                }
            }
            // 如果名字枚举为点位的名字
            else if (resourceEnum instanceof PositionNameEnum positionNameEnum) {
                Position position = resourceInput.getPositionByName(positionNameEnum);
                List<Object> positionFeatures = new ArrayList<>();
                // 添加点位状态
                positionFeatures.add(position.getStatus());
                // 将点位的相关特征放入特征列表中
                resultFeatures.put(resourceEnum, positionFeatures);
            }
            // 如果名字枚举为工岗的名字
            else if (resourceEnum instanceof WorkstationNameEnum workstationNameEnum) {
                WorkStation workStation = resourceInput.getWorkStationByName(workstationNameEnum);
                List<Object> workStationFeatures = new ArrayList<>();
                // 获取工岗空闲/忙碌状态
                ResourceState workStationState = resource.getWorkstationResMap().get(workstationNameEnum);
                // 添加工岗空闲/忙碌状态
                workStationFeatures.add(workStationState);
                // 添加工岗状态
                workStationFeatures.add(workStation.getStatus());
                // 将工岗的相关特征放入特征列表中
                resultFeatures.put(resourceEnum, workStationFeatures);
            }
            // 如果名字枚举为设备的名字
            else if (resourceEnum instanceof EquipmentNameEnum equipmentNameEnum) {
                Equipment equipment = resourceInput.getEquipmentByName(equipmentNameEnum);
                List<Object> equipmentFeatures = new ArrayList<>();
                // 获取工岗空闲/忙碌状态
                ResourceState equipmentState = resource.getWorkstationResMap().get(equipmentNameEnum);
                // 添加工岗空闲/忙碌状态
                equipmentFeatures.add(equipmentState);
                // 添加设备状态
                equipmentFeatures.add(equipment.getEquipmentStatus());
                // 将设备的相关特征放入特征列表
                resultFeatures.put(resourceEnum, equipmentFeatures);
            }
            // 如果名字枚举为道路的名字
            else if (resourceEnum instanceof PathNameEnum pathNameEnum) {
                Path path = resourceInput.getPathByName(pathNameEnum);
                List<Object> pathFeatures = new ArrayList<>();
                // 添加道路状态
                pathFeatures.add(path.getStatus());
                // 将道路的相关特征放入特征列表
                resultFeatures.put(resourceEnum, pathFeatures);
            }
        }
        return resultFeatures;
    }

    @Override
    public String toString() {
        return "PredictInfo{" +
                "\nfeatures=" + features +
                "\nrelatedCar=" + relatedCar +
                "\nrelatedResource=" + relatedResource +
                "\n}";
    }
}