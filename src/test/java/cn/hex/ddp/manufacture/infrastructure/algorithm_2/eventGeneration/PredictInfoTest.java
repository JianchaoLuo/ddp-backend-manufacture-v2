//package cn.hex.ddp.manufacture.infrastructure.algorithm_2.eventGeneration;
//
//import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.AllocationInput;
//import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.OrderInput;
//import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.OrderProduct;
//import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput;
//import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.*;
//import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
//import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.SimulationTool;
//import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.eventGeneration.PredictInfo;
//import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.input.ResourceInput;
//
//import java.util.List;
//
//import static cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum.OUT_BOUND_FERRY;
//import static cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum.OUT_BOUND_SUB_CAR;
//import static cn.hex.ddp.manufacture.domain.common.enums.AreaEnum.SOUTH_MOULDING;
//import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum.HOT_SAND_MOLDING_POSITION;
//import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.UNOCCUPIED;
//import static cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentNameEnum.HOT_SAND_BLASTING;
//import static cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum.STANDBY;
//import static cn.hex.ddp.manufacture.domain.path.enums.PathNameEnum.OUT_BOUND_PATH;
//import static cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum.PATH_UNOCCUPIED;
//import static cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationNameEnum.HOT_MOLD_CLOSING_WORKSTATION_1;
//import static cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum.FREE;
//import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum.NOT_CARRY_SUB_CAR;
//import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.OrderProductTypeEnum.HOT_SAND;
//import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductAfootEnum.GOING_MOLD_CLOSING;
//import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductFinishEnum.FINISH_MOLD_CLOSING;
//import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum.UPPER_BOX;
//import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum.EMPTY_LOAD;
//
//
///**
// * @Author: bobo
// * @Description: 特征容器是否可用的测试
// * @DateTime: 2025/3/19 10:48
// **/
//public class PredictInfoTest {
//
//    //仿真输入
//    SimulateInput simulateInput = new SimulateInput();
//    //仿真工具类
//    SimulationTool simulationTool = new SimulationTool();
//    //仿真维护的资源列表
//    ResourceInput resourceInput;
//    //mook一个输入
//    {
//        //mook输入信息
//        Coordinate start = new Coordinate(0, 0);
//        Coordinate end = new Coordinate(0, 1);
//        Path path = new Path(1L,"path",OUT_BOUND_PATH,start,end, List.of(start,end),PATH_UNOCCUPIED);
//        Product product = new Product(1L,"product",1,UPPER_BOX,FINISH_MOLD_CLOSING,GOING_MOLD_CLOSING,HOT_SAND,true,5,5,5,5,5,5,5,5,5,5,null);
//        Tray tray = new Tray(1L,"1",product);
//        Ferry ferry = new Ferry(1L,"ferry",OUT_BOUND_FERRY,5,5,List.of(NOT_CARRY_SUB_CAR),path,1L,start);
//        SubCar subCar = new SubCar(1L,"subcar",OUT_BOUND_SUB_CAR,5,5,5,List.of(EMPTY_LOAD),path, 1L,start,product,tray);
//        Equipment equipment = new Equipment(1L,"equipment",start,STANDBY,null,0,0,HOT_SAND_BLASTING);
//        Position position = new Position(1L,"position",HOT_SAND_MOLDING_POSITION,start,UNOCCUPIED,product,tray);
//        WorkStation workStation = new WorkStation(1L,"workPosition",HOT_MOLD_CLOSING_WORKSTATION_1,start,FREE,List.of(product),null);
//        OrderProduct orderProduct = new OrderProduct(List.of(product),5,"icon",HOT_SAND,2,2,0,0,null,"");
//
//        //创建一个全区域输入
//        AllocationInput allocationInput = new AllocationInput(List.of(subCar),List.of(ferry),List.of(equipment),List.of(position),List.of(workStation),List.of(path),SOUTH_MOULDING);
//        simulateInput.setAllocationInputs(List.of(allocationInput));
//        //创建一个订单输入
//        OrderInput orderInput = new OrderInput(1L,List.of(orderProduct),1,1);
//        simulateInput.setOrderInputs(List.of(orderInput));
//        //创建一个仿真时维护的资源列表
//        resourceInput = simulationTool.initialRescourceInput(simulateInput);
//    }
//
//    // 构造一个特征容器
//    PredictInfo predictInfo;
//
//    // 测试将资源列表转为决策特征
////    @Test
////    void setRelatedResources() {
////        List<Enum<?>> relatedResources = new ArrayList<>(
////                List.of(OUT_BOUND_PATH, OUT_BOUND_FERRY, OUT_BOUND_SUB_CAR, HOT_SAND_BLASTING, HOT_SAND_MOLDING_POSITION, HOT_MOLD_CLOSING_WORKSTATION_1)
////        );
////        predictInfo = new PredictInfo(OUT_BOUND_SUB_CAR, relatedResources,resourceInput );
////        System.out.println(predictInfo);
////    }
//
//
//
//}
