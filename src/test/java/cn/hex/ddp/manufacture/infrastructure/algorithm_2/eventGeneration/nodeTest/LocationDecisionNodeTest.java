//package cn.hex.ddp.manufacture.infrastructure.algorithm_2.eventGeneration.nodeTest;
//
//import cn.hex.ddp.manufacture.domain.path.enums.PathNameEnum;
//import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.AllocationInput;
//import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.OrderInput;
//import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.OrderProduct;
//import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput;
//import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.*;
//import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
//import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.SimulationTool;
//import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.eventGeneration.PredictInfo;
//import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.input.ResourceInput;
//import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.resource.Resource;
//
//import java.util.List;
//
//import static cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum.HOT_CLOSING_BOX_FERRY;
//import static cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum.HOT_CLOSING_BOX_SUB_CAR;
//import static cn.hex.ddp.manufacture.domain.common.enums.AreaEnum.SOUTH_MOULDING;
//import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum.HOT_SAND_BOX_CLOSING_BOX_GO_POUR_POSITION;
//import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum.HOT_SAND_BOX_SPRAY_GO_CLOSING_BOX_POSITION;
//import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.UNOCCUPIED;
//import static cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentNameEnum.HOT_SAND_BLASTING;
//import static cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum.STANDBY;
//import static cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum.PATH_UNOCCUPIED;
//import static cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationNameEnum.*;
//import static cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum.FREE;
//import static cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum.OCCUPANCY;
//import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum.CARRY_SUB_CAR;
//import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.OrderProductTypeEnum.HOT_SAND;
//import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductAfootEnum.GOING_MOLD_CLOSING;
//import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductFinishEnum.FINISH_MOLD_CLOSING;
//import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum.UPPER_BOX;
//import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum.FULL_LOAD;
//import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum.SUB_CAR_IN_FERRY;
//
///**
// * @Author: bobo
// * @Description: 根据多个地点的状态决策地点的节点
// * @DateTime: 2025/3/24 10:59
// **/
//public class LocationDecisionNodeTest {
//
//    //仿真输入
//    SimulateInput simulateInput = new SimulateInput();
//    //仿真工具类
//    SimulationTool simulationTool = new SimulationTool();
//    //仿真维护的资源列表
//    ResourceInput resourceInput;
//    //维护资源的忙闲状态
//    Resource resource;
//    //预测信息
//    PredictInfo predictInfo;
//    //mook一个输入
//    {
//        //mook输入信息
//        Coordinate start = new Coordinate(0, 0);
//        Coordinate end = new Coordinate(5, 0);
//
//        Coordinate workStation_1_Coordinate = new Coordinate(1, 1);
//        Coordinate workStation_2_Coordinate = new Coordinate(2, 1);
//        Coordinate workStation_3_Coordinate = new Coordinate(3, 1);
//        Coordinate workStation_4_Coordinate = new Coordinate(4, 1);
//
//        Coordinate position_1_Coordinate = new Coordinate(0, 1);
//        Coordinate position_2_Coordinate = new Coordinate(5, 1);
//
//        Coordinate subCarCoordinate = new Coordinate(5, 0);
//        Coordinate ferryCoordinate = new Coordinate(5, 0);
//
//        Path path = new Path(1L,"path", PathNameEnum.HOT_CLOSING_BOX_PATH,start,end, List.of(start,end),PATH_UNOCCUPIED);
//
//        Product product = new Product(1L,"product",1,UPPER_BOX,FINISH_MOLD_CLOSING,GOING_MOLD_CLOSING,HOT_SAND,true,5,5,5,5,5,5,5,5,5,5,null);
//
//        Tray tray = new Tray(1L,"1",product);
//
//        Ferry ferry = new Ferry(1L,"ferry",HOT_CLOSING_BOX_FERRY,5,5,List.of(CARRY_SUB_CAR),path,1L,ferryCoordinate);
//
//        SubCar subCar = new SubCar(1L,"subCar",HOT_CLOSING_BOX_SUB_CAR,5,5,5,List.of(FULL_LOAD,SUB_CAR_IN_FERRY),path, 1L, subCarCoordinate, product, tray);
//
//        Equipment equipment = new Equipment(1L,"equipment",start,STANDBY,null,0,0,HOT_SAND_BLASTING);
//
//        Position position_1 = new Position(1L,"position_1",HOT_SAND_BOX_SPRAY_GO_CLOSING_BOX_POSITION,position_1_Coordinate,UNOCCUPIED,null,null);
//        Position position_2 = new Position(2L,"position_2",HOT_SAND_BOX_CLOSING_BOX_GO_POUR_POSITION,position_2_Coordinate,UNOCCUPIED,null,null);
//
//        WorkStation workStation_1 = new WorkStation(1L,"workPosition_1",HOT_CLOSING_BOX_WORKSTATION_1,workStation_1_Coordinate,OCCUPANCY,null,null);
//        WorkStation workStation_2 = new WorkStation(2L,"workPosition_2",HOT_CLOSING_BOX_WORKSTATION_2,workStation_2_Coordinate,FREE,null,null);
//        WorkStation workStation_3 = new WorkStation(3L,"workPosition_3",HOT_CLOSING_BOX_WORKSTATION_3,workStation_3_Coordinate,FREE,null,null);
//        WorkStation workStation_4 = new WorkStation(4L,"workPosition_4",HOT_CLOSING_BOX_WORKSTATION_4,workStation_4_Coordinate,OCCUPANCY,null,null);
//
//        OrderProduct orderProduct = new OrderProduct(List.of(product),5,"icon",HOT_SAND,2,2,0,0,null,"");
//
//        //创建一个全区域输入
//        AllocationInput allocationInput = new AllocationInput(
//                List.of(subCar),List.of(ferry),List.of(equipment),List.of(position_1,position_2),List.of(workStation_1,workStation_2,workStation_3,workStation_4)
//                ,List.of(path),SOUTH_MOULDING);
//        simulateInput.setAllocationInputs(List.of(allocationInput));
//        //创建一个订单输入
//        OrderInput orderInput = new OrderInput(1L,List.of(orderProduct),1,1);
//        simulateInput.setOrderInputs(List.of(orderInput));
//        //创建一个仿真时维护的资源列表
//        resourceInput = simulationTool.initialRescourceInput(simulateInput);
//        //维护资源的忙闲状态
//        resource = simulationTool.initialRescource(resourceInput);
//    }
//
////    //测试创建根据地点状态决策地点的节点
////    @Test
////    void newLocationDecisionTreeNodeTest(){
////
////        //构建热砂合箱母车决策需要的预测信息
////        List<Enum<?>> relatedResources = new ArrayList<>(
////                List.of(HOT_CLOSING_BOX_FERRY, HOT_CLOSING_BOX_SUB_CAR, HOT_SAND_BOX_SPRAY_GO_CLOSING_BOX_POSITION,HOT_SAND_BOX_CLOSING_BOX_GO_POUR_POSITION,
////                        HOT_CLOSING_BOX_WORKSTATION_1, HOT_CLOSING_BOX_WORKSTATION_2, HOT_CLOSING_BOX_WORKSTATION_3, HOT_CLOSING_BOX_WORKSTATION_4)
////        );
////        predictInfo = new PredictInfo(HOT_CLOSING_BOX_FERRY, relatedResources, resourceInput, resource);
////        System.out.println(predictInfo);
////
////        // 用于判断的地点列表
////        List<WorkstationNameEnum> workstationNameEnumList = List.of(
////                HOT_CLOSING_BOX_WORKSTATION_1, HOT_CLOSING_BOX_WORKSTATION_2,
////                HOT_CLOSING_BOX_WORKSTATION_3, HOT_CLOSING_BOX_WORKSTATION_4
////        );
////        DecisionTreeNode<WorkstationNameEnum, WorkstationStatusEnum> testNode1 = new DecisionTreeNode<>(
////                workstationNameEnumList, FREE, WorkstationStatusEnum.class,
////                ClosingBoxEventName.SUB_CAR_FULL_GO_HOT_SAND_CLOSING_BOX_WORK_POSITION
////        );
////        System.out.println(testNode1);
////        System.out.println(testNode1.getKeyResource(predictInfo));
////    }
//}
