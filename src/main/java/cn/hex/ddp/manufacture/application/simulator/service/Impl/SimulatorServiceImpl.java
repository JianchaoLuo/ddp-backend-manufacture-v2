package cn.hex.ddp.manufacture.application.simulator.service.Impl;

import cn.hex.ddp.manufacture.application.simulator.service.SimulatorService;
import cn.hex.ddp.manufacture.domain.simulator.manager.*;
import cn.hex.ddp.manufacture.domain.simulator.model.*;
import cn.hex.ddp.manufacture.domain.simulator.model.simulation.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 仿真服务实现类
 * @Author: FengZebang
 * @Date: 2025/4/17
 */
@Slf4j
@Service
public class SimulatorServiceImpl implements SimulatorService {

    @Resource
    private CarSimulatorManager carSimulatorManager;

    @Resource
    private EquipmentSimulatorManager equipmentSimulatorManager;

    @Resource
    private PathSimulatorManager pathSimulatorManager;

    @Resource
    private PositonSimulatorManager positionSimulatorManager;

    @Resource
    private ProductSimulatorManager productSimulatorManager;

    @Resource
    private WorkStationSimilatorManager workStationSimilatorManager;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

   /**
     * 保存仿真数据, 包含车辆、设备、路径、位置、工位、产品等
     * 这里用到@Transactional，应该还有优化的地方。
     * @param currentTick
     * @param missionId
     * @param cars
     * @param equipments
     * @param positions
     * @param workstations
     * @param paths
     * @param products
    */
    @Override
    //@Transactional(rollbackFor = Exception.class)
    public void saveSimulatorData(Long currentTick,
                                  Long missionId,
                                  List<Car> cars,
                                  List<Equipment> equipments,
                                  Collection<PositionRealTime> positions,
                                  Collection<WorkstationRealTime> workstations,
                                  Collection<Path> paths,
                                  Collection<Product> products)

//    {
//        LocalDateTime now = LocalDateTime.now();
//        //一帧中车辆数据
//        try{
//            ArrayList<CarVaryData> carVaryDatas = new ArrayList<>();
//            for (Car car : cars) {
//                CarVaryData carVaryData = new CarVaryData();
//                carVaryData.setId(car.getId());
//                carVaryData.setCarOperationStatus(car.getRealTime().getOperationStatus());
//                carVaryData.setCurrentCoordinate(car.getRealTime().getCurrentCoordinate().scaleConversionToString());
//                carVaryData.setTargetCoordinate(car.getRealTime().getTargetCoordinate().scaleConversionToString());
//                carVaryData.setCurrentAction(car.getRealTime().getCurrentAction());
//                carVaryDatas.add(carVaryData);
//            }
//            CarSimulatorData carSimulatorData = new CarSimulatorData();
//            carSimulatorData.setMission(missionId);
//            carSimulatorData.setTick(currentTick);
//            carSimulatorData.setSimulationTime(LocalDateTime.now());
//            carSimulatorData.setData(OBJECT_MAPPER.writeValueAsString(carVaryDatas));
//            carSimulatorManager.saveCarRealTime(carSimulatorData);
//        } catch (JsonProcessingException e) {
//            log.warn("missionId:{}, currentTick:{}, 数据帧中车辆数据存储异常:{}", missionId, currentTick, e.getMessage());
//        }
//
//        //一帧中所有设备数据
//        try{
//            ArrayList<EquipmentVaryData> equipmentVaryDatas = new ArrayList<>();
//            for (Equipment equipment : equipments) {
//                EquipmentVaryData equipmentVaryData = new EquipmentVaryData();
//                equipmentVaryData.setId(equipment.getId());
//                equipmentVaryData.setOperationStatus(equipment.getRealTime().getOperationStatus());
//                equipmentVaryData.setMoltenIronPouringQuantity(equipment.getRealTime().getMoltenIronQuantity());
//                equipmentVaryDatas.add(equipmentVaryData);
//            }
//            EquipmentSimulatorData equipmentSimulatorData = new EquipmentSimulatorData();
//            equipmentSimulatorData.setMission(missionId);
//            equipmentSimulatorData.setTick(currentTick);
//            equipmentSimulatorData.setSimulationTime(now);
//            equipmentSimulatorData.setData(OBJECT_MAPPER.writeValueAsString(equipmentVaryDatas));
//            equipmentSimulatorManager.saveEquipmentRealTime(equipmentSimulatorData);
//        } catch (JsonProcessingException e) {
//            log.warn("missionId:{}, currentTick:{}, 数据帧中设备数据存储异常:{}", missionId, currentTick, e.getMessage());
//        }
//
//        //一帧中所有路径数据
//        try {
//            List<PathVaryData> pathVaryDatas = new ArrayList<>();
//            for (Path path : paths) {
//                PathVaryData pathVaryData = new PathVaryData();
//                pathVaryData.setId(path.getId());
//                pathVaryData.setName(path.getName());
//                pathVaryData.setLength(path.getLength());
//                pathVaryData.setStartCoordinate(path.getStartCoordinate().scaleConversionToString());
//                pathVaryData.setEndCoordinate(path.getEndCoordinate().scaleConversionToString());
//                pathVaryData.setRailDirection(path.getDirection());
//                pathVaryData.setStatus(path.getStatus());
//                pathVaryDatas.add(pathVaryData);
//            }
//            PathSimulatorData pathSimulatorData = new PathSimulatorData();
//            pathSimulatorData.setMission(missionId);
//            pathSimulatorData.setTick(currentTick);
//            pathSimulatorData.setSimulationTime(now);
//            pathSimulatorData.setData(OBJECT_MAPPER.writeValueAsString(pathVaryDatas));
//            pathSimulatorManager.savePathRealTime(pathSimulatorData);
//        } catch (JsonProcessingException e) {
//            log.warn("missionId:{}, currentTick:{}, 数据帧中设备路径存储异常:{}", missionId, currentTick, e.getMessage());
//        }
//
//        //一帧中所有位置数据
//        try {
//            List<PositionVaryData> positionVaryDatas = new ArrayList<>();
//            for (PositionRealTime position : positions) {
//                PositionVaryData positionVaryData = new PositionVaryData();
//                positionVaryData.setId(position.getId());
//                positionVaryData.setCurrentStatus(position.getStatus().name());
//                positionVaryData.setCurrentAction(position.getCurrentAction());
//                positionVaryDatas.add(positionVaryData);
//            }
//            PositionSimulatorData positionSimulatorData = new PositionSimulatorData();
//            positionSimulatorData.setMission(missionId);
//            positionSimulatorData.setTick(currentTick);
//            positionSimulatorData.setSimulationTime(now);
//            positionSimulatorData.setData(OBJECT_MAPPER.writeValueAsString(positionVaryDatas));
//            positionSimulatorManager.savePositionRealTime(positionSimulatorData);
//        } catch (JsonProcessingException e) {
//            log.warn("missionId:{}, currentTick:{}, 数据帧中设备位置存储异常:{}", missionId, currentTick, e.getMessage());
//        }
//
//        //一帧中所有工位数据
//        try {
//            List<WorkstationVaryData> workstationVaryDatas = new ArrayList<>();
//            for (WorkstationRealTime workstation : workstations) {
//                WorkstationVaryData workstationVaryData = new WorkstationVaryData();
//                workstationVaryData.setName(workstation.getName());
//                workstationVaryData.setStatus(workstation.getStatus());
//                workstationVaryData.setCurrentAction(workstation.getCurrentAction());
//                workstationVaryDatas.add(workstationVaryData);
//            }
//            WorkStationSimulatorData workStationSimulatorData = new WorkStationSimulatorData();
//            workStationSimulatorData.setMission(missionId);
//            workStationSimulatorData.setTick(currentTick);
//            workStationSimulatorData.setSimulationTime(now);
//            workStationSimulatorData.setData(OBJECT_MAPPER.writeValueAsString(workstationVaryDatas));
//            workStationSimilatorManager.saveWorkStationRealTime(workStationSimulatorData);
//        } catch (JsonProcessingException e) {
//            log.warn("missionId:{}, currentTick:{}, 数据帧中工位数据数据存储异常:{}", missionId, currentTick, e.getMessage());
//        }
//
//        //一帧中所有产品数据
//        try {
//            List<ProductVaryData> productVaryDatas = new ArrayList<>();
//            for (Product product : products) {
//                ProductVaryData productVaryData = new ProductVaryData();
//                productVaryData.setName(product.getName());
//                productVaryData.setProductType(product.getProductType());
//                productVaryData.setProductAfoot(product.getProductAfoot());
//                productVaryDatas.add(productVaryData);
//            }
//            ProductSimulatorData productSimulatorData = new ProductSimulatorData();
//            productSimulatorData.setMission(missionId);
//            productSimulatorData.setTick(currentTick);
//            productSimulatorData.setSimulationTime(now);
//            productSimulatorData.setData(OBJECT_MAPPER.writeValueAsString(productVaryDatas));
//            productSimulatorManager.saveProductSimulatorData(productSimulatorData);
//        } catch (JsonProcessingException e) {
//            log.warn("missionId:{}, currentTick:{}, 数据帧中产品数据存储异常:{}", missionId, currentTick, e.getMessage());
//        }
//    }
    //并行流
    {
        LocalDateTime now = LocalDateTime.now();

        // 一帧中车辆数据
        try {
            List<CarVaryData> carVaryDatas = cars.parallelStream()
                    .map(car -> {
                        CarVaryData carVaryData = new CarVaryData();
                        carVaryData.setId(car.getId());
                        carVaryData.setCarOperationStatus(car.getRealTime().getOperationStatus());
                        carVaryData.setCurrentCoordinate(car.getRealTime().getCurrentCoordinate().scaleConversionToString());
                        carVaryData.setTargetCoordinate(car.getRealTime().getTargetCoordinate().scaleConversionToString());
                        carVaryData.setCurrentAction(car.getRealTime().getCurrentAction());
                        return carVaryData;
                    })
                    .collect(Collectors.toList());

            CarSimulatorData carSimulatorData = new CarSimulatorData();
            carSimulatorData.setMission(missionId);
            carSimulatorData.setTick(currentTick);
            carSimulatorData.setSimulationTime(LocalDateTime.now());
            carSimulatorData.setData(OBJECT_MAPPER.writeValueAsString(carVaryDatas));
            carSimulatorManager.saveCarRealTime(carSimulatorData);
        } catch (JsonProcessingException e) {
            log.warn("missionId:{}, currentTick:{}, 数据帧中车辆数据存储异常:{}", missionId, currentTick, e.getMessage());
        }

        // 一帧中所有设备数据
        try {
            List<EquipmentVaryData> equipmentVaryDatas = equipments.parallelStream()
                    .map(equipment -> {
                        EquipmentVaryData equipmentVaryData = new EquipmentVaryData();
                        equipmentVaryData.setId(equipment.getId());
                        equipmentVaryData.setOperationStatus(equipment.getRealTime().getOperationStatus());
                        equipmentVaryData.setMoltenIronPouringQuantity(equipment.getRealTime().getMoltenIronQuantity());
                        return equipmentVaryData;
                    })
                    .collect(Collectors.toList());

            EquipmentSimulatorData equipmentSimulatorData = new EquipmentSimulatorData();
            equipmentSimulatorData.setMission(missionId);
            equipmentSimulatorData.setTick(currentTick);
            equipmentSimulatorData.setSimulationTime(now);
            equipmentSimulatorData.setData(OBJECT_MAPPER.writeValueAsString(equipmentVaryDatas));
            equipmentSimulatorManager.saveEquipmentRealTime(equipmentSimulatorData);
        } catch (JsonProcessingException e) {
            log.warn("missionId:{}, currentTick:{}, 数据帧中设备数据存储异常:{}", missionId, currentTick, e.getMessage());
        }

        // 一帧中所有路径数据
        try {
            List<PathVaryData> pathVaryDatas = paths.parallelStream()
                    .map(path -> {
                        PathVaryData pathVaryData = new PathVaryData();
                        pathVaryData.setId(path.getId());
                        pathVaryData.setName(path.getName());
                        pathVaryData.setLength(path.getLength());
                        pathVaryData.setStartCoordinate(path.getStartCoordinate().scaleConversionToString());
                        pathVaryData.setEndCoordinate(path.getEndCoordinate().scaleConversionToString());
                        pathVaryData.setRailDirection(path.getDirection());
                        pathVaryData.setStatus(path.getStatus());
                        return pathVaryData;
                    })
                    .collect(Collectors.toList());

            PathSimulatorData pathSimulatorData = new PathSimulatorData();
            pathSimulatorData.setMission(missionId);
            pathSimulatorData.setTick(currentTick);
            pathSimulatorData.setSimulationTime(now);
            pathSimulatorData.setData(OBJECT_MAPPER.writeValueAsString(pathVaryDatas));
            pathSimulatorManager.savePathRealTime(pathSimulatorData);
        } catch (JsonProcessingException e) {
            log.warn("missionId:{}, currentTick:{}, 数据帧中设备路径存储异常:{}", missionId, currentTick, e.getMessage());
        }

        // 一帧中所有位置数据
        try {
            List<PositionVaryData> positionVaryDatas = positions.parallelStream()
                    .map(position -> {
                        PositionVaryData positionVaryData = new PositionVaryData();
                        positionVaryData.setId(position.getId());
                        positionVaryData.setCurrentStatus(position.getStatus().name());
                        positionVaryData.setCurrentAction(position.getCurrentAction());
                        return positionVaryData;
                    })
                    .collect(Collectors.toList());

            PositionSimulatorData positionSimulatorData = new PositionSimulatorData();
            positionSimulatorData.setMission(missionId);
            positionSimulatorData.setTick(currentTick);
            positionSimulatorData.setSimulationTime(now);
            positionSimulatorData.setData(OBJECT_MAPPER.writeValueAsString(positionVaryDatas));
            positionSimulatorManager.savePositionRealTime(positionSimulatorData);
        } catch (JsonProcessingException e) {
            log.warn("missionId:{}, currentTick:{}, 数据帧中设备位置存储异常:{}", missionId, currentTick, e.getMessage());
        }

        // 一帧中所有工位数据
        try {
            List<WorkstationVaryData> workstationVaryDatas = workstations.parallelStream()
                    .map(workstation -> {
                        WorkstationVaryData workstationVaryData = new WorkstationVaryData();
                        workstationVaryData.setName(workstation.getName());
                        workstationVaryData.setStatus(workstation.getStatus());
                        workstationVaryData.setCurrentAction(workstation.getCurrentAction());
                        return workstationVaryData;
                    })
                    .collect(Collectors.toList());

            WorkStationSimulatorData workStationSimulatorData = new WorkStationSimulatorData();
            workStationSimulatorData.setMission(missionId);
            workStationSimulatorData.setTick(currentTick);
            workStationSimulatorData.setSimulationTime(now);
            workStationSimulatorData.setData(OBJECT_MAPPER.writeValueAsString(workstationVaryDatas));
            workStationSimilatorManager.saveWorkStationRealTime(workStationSimulatorData);
        } catch (JsonProcessingException e) {
            log.warn("missionId:{}, currentTick:{}, 数据帧中工位数据数据存储异常:{}", missionId, currentTick, e.getMessage());
        }

        // 一帧中所有产品数据
        try {
            List<ProductVaryData> productVaryDatas = products.parallelStream()
                    .map(product -> {
                        ProductVaryData productVaryData = new ProductVaryData();
                        productVaryData.setName(product.getName());
                        productVaryData.setProductType(product.getProductType());
                        productVaryData.setProductAfoot(product.getProductAfoot());
                        return productVaryData;
                    })
                    .collect(Collectors.toList());

            ProductSimulatorData productSimulatorData = new ProductSimulatorData();
            productSimulatorData.setMission(missionId);
            productSimulatorData.setTick(currentTick);
            productSimulatorData.setSimulationTime(now);
            productSimulatorData.setData(OBJECT_MAPPER.writeValueAsString(productVaryDatas));
            productSimulatorManager.saveProductSimulatorData(productSimulatorData);
        } catch (JsonProcessingException e) {
            log.warn("missionId:{}, currentTick:{}, 数据帧中产品数据存储异常:{}", missionId, currentTick, e.getMessage());
        }
    }
}
