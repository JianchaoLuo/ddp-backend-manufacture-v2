//package cn.hex.ddp.manufacture.infrastructure.algorithm_2.eventGeneration.predictTest;
//
//import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
//import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentNameEnum;
//import cn.hex.ddp.manufacture.domain.path.enums.PathNameEnum;
//import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationNameEnum;
//import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
//import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.AllocationInput;
//import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.OrderInput;
//import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.OrderProduct;
//import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput;
//import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum;
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
//import static cn.hex.ddp.manufacture.domain.path.enums.PathNameEnum.HOT_CLOSING_BOX_PATH;
//import static cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum.PATH_UNOCCUPIED;
//import static cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationNameEnum.*;
//import static cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum.FREE;
//import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum.CARRY_SUB_CAR;
//import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.OrderProductTypeEnum.HOT_SAND;
//import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductAfootEnum.GOING_MOLD_CLOSING;
//import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductFinishEnum.FINISH_MOLD_CLOSING;
//import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum.FULL_LOAD;
//import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum.SUB_CAR_IN_FERRY;
//
///**
// * @Author: bobo
// * @Description: 对于热合箱母车车的相关事件预测
// * @DateTime: 2025/3/24 13:24
// **/
//public class HotClosingBoxFerryEventGenerationTest {
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
//    //mock一个输入
//    {
//        //mock输入信息
//        Coordinate start = new Coordinate(0, 0);
//        Coordinate end = new Coordinate(5, 0);
//
//        Coordinate workStation_1_Coordinate = new Coordinate(1, 1);
//        Coordinate workStation_2_Coordinate = new Coordinate(2, 1);
//        Coordinate workStation_3_Coordinate = new Coordinate(3, 1);
//        Coordinate workStation_4_Coordinate = new Coordinate(4, 1);
//
//        Coordinate position_1_Coordinate = new Coordinate(5, 1);
//        Coordinate position_2_Coordinate = new Coordinate(0, 1);
//
//        Coordinate subCarCoordinate = new Coordinate(5, 0);
//        Coordinate ferryCoordinate = new Coordinate(5, 0);
//
//        Path path = new Path(1L,"path", HOT_CLOSING_BOX_PATH,start,end, List.of(start,end),PATH_UNOCCUPIED);
//
//        Product product = new Product(1L,"product",1,ProductTypeEnum.LOWER_SAND_SHELL_BOX,FINISH_MOLD_CLOSING,GOING_MOLD_CLOSING,HOT_SAND,true,5,5,5,5,5,5,5,5,5,5,null);
//
//        Tray tray = new Tray(1L,"1",product);
//
//        Ferry ferry = new Ferry(1L,"ferry",HOT_CLOSING_BOX_FERRY,5,5,List.of(CARRY_SUB_CAR),path,1L,ferryCoordinate);
//
//        SubCar subCar = new SubCar(1L,"subCar",HOT_CLOSING_BOX_SUB_CAR,5,5,5,List.of(FULL_LOAD,SUB_CAR_IN_FERRY),path, 1L, subCarCoordinate,null,tray);
//
//        Equipment equipment = new Equipment(1L,"equipment",start,STANDBY,null,0,0,HOT_SAND_BLASTING);
//
//        Position position_1 = new Position(1L,"position_1",HOT_SAND_BOX_SPRAY_GO_CLOSING_BOX_POSITION,position_1_Coordinate,UNOCCUPIED,null,null);
//        Position position_2 = new Position(2L,"position_2",HOT_SAND_BOX_CLOSING_BOX_GO_POUR_POSITION,position_2_Coordinate,UNOCCUPIED,null,null);
//
//        WorkStation workStation_1 = new WorkStation(1L,"workPosition_1",HOT_CLOSING_BOX_WORKSTATION_1,workStation_1_Coordinate,WorkstationStatusEnum.WORKING,null,null);
//        WorkStation workStation_2 = new WorkStation(2L,"workPosition_2",HOT_CLOSING_BOX_WORKSTATION_2,workStation_2_Coordinate,FREE,null,null);
//        WorkStation workStation_3 = new WorkStation(3L,"workPosition_3",HOT_CLOSING_BOX_WORKSTATION_3,workStation_3_Coordinate,WorkstationStatusEnum.WORKING,null,null);
//        WorkStation workStation_4 = new WorkStation(4L,"workPosition_4",HOT_CLOSING_BOX_WORKSTATION_4,workStation_4_Coordinate,WorkstationStatusEnum.WORKING,null,null);
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
//    //定义一个计算路口坐标的方法
//    public Coordinate entranceCoordinate(PathNameEnum pathNameEnum, Enum<?> locationEnum, ResourceInput resourceInput){
//        Coordinate pathStartCoordinate = resourceInput.getPathByName(pathNameEnum).getStartCoordinate();
//        Coordinate pathEndCoordinate = resourceInput.getPathByName(pathNameEnum).getEndCoordinate();
//        // 地点的原始坐标
//        Coordinate originalCoordinate;
//        // 返回的坐标
//        Coordinate entranceCoordinate = new Coordinate();
//        switch (locationEnum) {
//            // 传入的是点位枚举
//            case PositionNameEnum positionNameEnum ->
//                    originalCoordinate = resourceInput.getPositionByName(positionNameEnum).getCoordinate();
//
//            // 传入的是工岗枚举
//            case WorkstationNameEnum workstationNameEnum ->
//                    originalCoordinate = resourceInput.getWorkStationByName(workstationNameEnum).getWorkPositionCoordinate();
//
//            // 传入的是设备枚举
//            case EquipmentNameEnum equipmentNameEnum ->
//                    originalCoordinate = resourceInput.getEquipmentByName(equipmentNameEnum).getEquipmentCoordinate();
//            case null, default -> {
//                return null;
//            }
//        }
//        // 如果路的方向平行于x轴
//        if(pathStartCoordinate.getY() == pathEndCoordinate.getY()){
//            entranceCoordinate.setX(originalCoordinate.getX());
//            double deltaY = originalCoordinate.getY() - pathStartCoordinate.getY();
//            entranceCoordinate.setY(originalCoordinate.getY() - deltaY);
//        }
//        // 如果路的方向平行于y轴
//        else {
//            double deltaX = originalCoordinate.getX() - pathStartCoordinate.getX();
//            entranceCoordinate.setX(originalCoordinate.getX() - deltaX);
//            entranceCoordinate.setY(originalCoordinate.getY());
//        }
//        return entranceCoordinate;
//    }
//
//    //测试预测函数
////    @Test
////    void predictTest(){
////
////        //构建热砂合箱子车决策需要的预测信息
////        List<Enum<?>> relatedResources = new ArrayList<>(
////                List.of(HOT_CLOSING_BOX_FERRY, HOT_CLOSING_BOX_SUB_CAR, HOT_SAND_BOX_SPRAY_GO_CLOSING_BOX_POSITION,HOT_SAND_BOX_CLOSING_BOX_GO_POUR_POSITION,
////                        HOT_CLOSING_BOX_WORKSTATION_1, HOT_CLOSING_BOX_WORKSTATION_2, HOT_CLOSING_BOX_WORKSTATION_3, HOT_CLOSING_BOX_WORKSTATION_4)
////        );
////        predictInfo = new PredictInfo(HOT_CLOSING_BOX_SUB_CAR, relatedResources,resourceInput,resource);
////        System.out.println(predictInfo);
////
////        //计算路口坐标
////        Coordinate position1EntranceCoordinate =
////                this.entranceCoordinate(HOT_CLOSING_BOX_PATH,HOT_SAND_BOX_SPRAY_GO_CLOSING_BOX_POSITION,resourceInput);
////        Coordinate position2EntranceCoordinate =
////                this.entranceCoordinate(HOT_CLOSING_BOX_PATH,HOT_SAND_BOX_CLOSING_BOX_GO_POUR_POSITION,resourceInput);
////        Coordinate workStation1EntranceCoordinate =
////                this.entranceCoordinate(HOT_CLOSING_BOX_PATH,HOT_CLOSING_BOX_WORKSTATION_1,resourceInput);
////        Coordinate workStation2EntranceCoordinate =
////                this.entranceCoordinate(HOT_CLOSING_BOX_PATH,HOT_CLOSING_BOX_WORKSTATION_2,resourceInput);
////        Coordinate workStation3EntranceCoordinate =
////                this.entranceCoordinate(HOT_CLOSING_BOX_PATH,HOT_CLOSING_BOX_WORKSTATION_3,resourceInput);
////        Coordinate workStation4EntranceCoordinate =
////                this.entranceCoordinate(HOT_CLOSING_BOX_PATH,HOT_CLOSING_BOX_WORKSTATION_4,resourceInput);
////
////        // 构建根节点，判断子车是否在母车上
////        DecisionTreeNode<CarNameEnum, FerryStatusEnum> node1 = new DecisionTreeNode<>(
////                HOT_CLOSING_BOX_FERRY, FerryStatusEnum.class, null,
////        );
////
////        //构造第二层节点，判断子车上物品
////        DecisionTreeNode<CarNameEnum, ProductTypeEnum> node2 = new DecisionTreeNode<>(
////                HOT_CLOSING_BOX_SUB_CAR, ProductTypeEnum.class, null
////        );
////
////        // 构造完第二层节点，加入第一层
////        node1.addChild(CARRY_SUB_CAR,node2);
////
////        // 创建第三层节点，判断工岗/点位状态
////        DecisionTreeNode<WorkstationNameEnum, WorkstationStatusEnum> node3 = new DecisionTreeNode<>(
////                List.of(HOT_CLOSING_BOX_WORKSTATION_1,HOT_CLOSING_BOX_WORKSTATION_2,HOT_CLOSING_BOX_WORKSTATION_3,HOT_CLOSING_BOX_WORKSTATION_4),
////                WorkstationStatusEnum.WAIT_TRANSPORT, WorkstationStatusEnum.class, null);
////
////        DecisionTreeNode<WorkstationNameEnum, WorkstationStatusEnum> node4 = new DecisionTreeNode<>(
////                List.of(HOT_CLOSING_BOX_WORKSTATION_1,HOT_CLOSING_BOX_WORKSTATION_2,HOT_CLOSING_BOX_WORKSTATION_3,HOT_CLOSING_BOX_WORKSTATION_4),
////                FREE, WorkstationStatusEnum.class, null);
////
////        DecisionTreeNode<WorkstationNameEnum, WorkstationStatusEnum> node5 = new DecisionTreeNode<>(
////                List.of(HOT_CLOSING_BOX_WORKSTATION_1,HOT_CLOSING_BOX_WORKSTATION_2,HOT_CLOSING_BOX_WORKSTATION_3,HOT_CLOSING_BOX_WORKSTATION_4),
////                WorkstationStatusEnum.WAIT_PAIR, WorkstationStatusEnum.class, null);
////
////        DecisionTreeNode<PositionNameEnum, PositionStatusEnum> node6 = new DecisionTreeNode<>(
////                List.of(HOT_SAND_BOX_CLOSING_BOX_GO_POUR_POSITION), UNOCCUPIED, PositionStatusEnum.class, null);
////
////        // 构造完第三层，加入第二层
////        node2.addChild(ProductTypeEnum.EMPTY, node3);
////        node2.addChild(ProductTypeEnum.LOWER_SAND_SHELL_BOX, node4);
////        node2.addChild(ProductTypeEnum.MIDDLE_SAND_SHELL_BOX, node5);
////        node2.addChild(ProductTypeEnum.UPPER_SAND_SHELL_BOX, node5);
////        node2.addChild(ProductTypeEnum.WHOLE_SAND_SHELL_BOX, node6);
////
////        // 构造第四层节点，判断地点状态
////        DecisionTreeNode<PositionNameEnum, PositionStatusEnum> node7 = new DecisionTreeNode<>(
////                List.of(HOT_SAND_BOX_SPRAY_GO_CLOSING_BOX_POSITION), OCCUPIED, PositionStatusEnum.class, null);
////
////        // 构建完第四层,加入第三层
////        node3.addChild(FREE,node7);
////        node3.addChild(WorkstationStatusEnum.WORKING,node7);
////        node3.addChild(WorkstationStatusEnum.WAIT_PAIR,node7);
////        node3.addChild(WorkstationStatusEnum.UNAVAILABLE,node7);
////        node3.addChild(WorkstationStatusEnum.PREPARING,node7);
////
////        // 构造叶子结点，类型已经不重要了
////        DecisionTreeNode<WorkstationNameEnum, WorkstationStatusEnum> leaf1 = new DecisionTreeNode<>(
////                WorkstationStatusEnum.class, FERRY_AND_SUB_CAR_EMPTY_GO_HOT_SAND_CLOSING_BOX_WORK_POSITION);
////        DecisionTreeNode<WorkstationNameEnum, WorkstationStatusEnum> leaf2 = new DecisionTreeNode<>(
////                WorkstationStatusEnum.class, FERRY_AND_SUB_CAR_EMPTY_GO_HOT_SAND_CLOSING_BOX_POSITION);
////        DecisionTreeNode<WorkstationNameEnum, WorkstationStatusEnum> leaf3 = new DecisionTreeNode<>(
////                WorkstationStatusEnum.class, FERRY_AND_SUB_CAR_FULL_GO_HOT_SAND_CLOSING_BOX_WORK_POSITION);
////        DecisionTreeNode<WorkstationNameEnum, WorkstationStatusEnum> leaf4 = new DecisionTreeNode<>(
////                WorkstationStatusEnum.class, FERRY_AND_SUB_CAR_FULL_GO_HOT_SAND_CLOSING_BOX_WORK_POSITION);
////        DecisionTreeNode<WorkstationNameEnum, WorkstationStatusEnum> leaf5 = new DecisionTreeNode<>(
////                WorkstationStatusEnum.class, FERRY_AND_SUB_CAR_FULL_GO_WAIT_POURING_UP_POSITION);
////
////        // 构造完叶子结点，加入第三/四层
////        node3.addChild(WorkstationStatusEnum.WAIT_TRANSPORT,leaf1);
////        node7.addChild(OCCUPIED,leaf2);
////        node4.addChild(FREE,leaf3);
////        node5.addChild(WorkstationStatusEnum.WAIT_PAIR,leaf4);
////        node6.addChild(UNOCCUPIED,leaf5);
////
////        // 开始预测
////        DecisionTreePredictor<CarNameEnum,FerryStatusEnum> decisionTreePredictor = new DecisionTreePredictor<>(node1);
////        System.out.println(decisionTreePredictor.predict(predictInfo));
////    }
//}
