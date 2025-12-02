package cn.hex.ddp.manufacture.application.scheduling.util;

import cn.hex.ddp.manufacture.api.equipment.rest.vo.EquipmentVO;
import cn.hex.ddp.manufacture.api.workstation.rest.vo.WorkstationVO;
import cn.hex.ddp.manufacture.application.car.dto.out.CarDetailDTO;
import cn.hex.ddp.manufacture.application.car.service.CarService;
import cn.hex.ddp.manufacture.application.configuration.dto.out.PositionDTO;
import cn.hex.ddp.manufacture.application.configuration.service.ConfigurationService;
import cn.hex.ddp.manufacture.application.equipment.service.EquipmentService;
import cn.hex.ddp.manufacture.application.order.service.OrderService;
import cn.hex.ddp.manufacture.application.path.dto.out.PathDTO;
import cn.hex.ddp.manufacture.application.path.service.PathService;
import cn.hex.ddp.manufacture.application.workstation.service.WorkstationService;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.order.manager.OrderManager;
import cn.hex.ddp.manufacture.domain.order.model.Order;
import cn.hex.ddp.manufacture.domain.product.manager.ProductManager;
import cn.hex.ddp.manufacture.domain.product.model.ProductionItem;
import cn.hex.ddp.manufacture.domain.scheduling.enums.PLanType;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.AllocationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.OrderInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule.ScheduleOrderInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule.ScheduleOrderItem;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule.ScheduleProductInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.AnalogInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.util.ScheduleDateConverter;
import cn.hex.ddp.manufacture.infrastructure.common.converter.AlgorithmDataConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 排产算法输入构造器
 * @author 冯泽邦
 * @date 2025/11/25
 */
@Slf4j
@Service
public class SchedulingInput {

    @Autowired
    private CarService carService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private WorkstationService workstationService;

    @Autowired
    private OrderManager orderManager;

    @Autowired
    private ProductManager productManager;

    @Autowired
    private PathService pathService;

    @Autowired
    private OrderService orderService;


    /**
     * 构造SimulateInput
     * 逻辑与SimulateService中prepareSimulateContext函数相同
     */
    public SimulateInput buildSimulationInput(List<Long> orderIdList){
        ArrayList<AllocationInput> allocationInputs = new ArrayList<>();

        //转换子车与摆渡车
        List<CarDetailDTO> allCar = carService.getAllCar();
        List<SubCar> subCars = AlgorithmDataConverter.carDetailDTOsToSubCars(allCar);
        Map<AreaEnum, List<SubCar>> subCarsMap = subCars
                .stream()
                .collect(Collectors.groupingBy(subCar -> subCar.getNameEnum().getArea()));
        List<Ferry> ferries = AlgorithmDataConverter.carDetailDTOsToFerries(allCar);
        Map<AreaEnum, List<Ferry>> ferryMap = ferries
                .stream()
                .collect(Collectors.groupingBy(ferry -> ferry.getNameEnum().getArea()));

        //转换设备列表
        List<EquipmentVO> allEquipment = equipmentService.getAllEquipment();
        List<Equipment> equipments = AlgorithmDataConverter.equipmentVOToEquipments(allEquipment);
        Map<AreaEnum, List<Equipment>> equipmentMap = equipments
                .stream()
                .collect(Collectors.groupingBy(equipment -> equipment.getNameEnum().getArea()));

        //转换点位列表
        List<PositionDTO> allPosition = configurationService.getAllPosition();
        List<Position> positions = AlgorithmDataConverter.positionDTOsToPositions(allPosition);
        Map<AreaEnum, List<Position>> positionMap = positions
                .stream()
                .collect(Collectors.groupingBy(position -> position.getNameEnum().getArea()));

        //转换公岗列表
        List<WorkstationVO> allWorkstation = workstationService.getAllWorkstation();
        List<WorkStation> workStations = AlgorithmDataConverter.workstationVOsToWorkStations(allWorkstation);
        Map<AreaEnum, List<WorkStation>> workStationMap = workStations
                .stream()
                .collect(Collectors.groupingBy(workStation -> workStation.getNameEnum().getArea()));

        //转换路径列表
        List<PathDTO> allPath = pathService.getAllPath();
        List<Path> paths = AlgorithmDataConverter.pathDTOsToPaths(allPath);
        Map<AreaEnum, List<Path>> pathMap = paths
                .stream()
                .collect(Collectors.groupingBy(path -> path.getNameEnum().getArea()));

        List<Map<AreaEnum, ? >> maps = Arrays.asList(
                subCarsMap,
                ferryMap,
                equipmentMap,
                positionMap,
                workStationMap,
                pathMap
        );
        //构建工厂的基础配置信息
        for(AreaEnum area : AreaEnum.values()){
            //判断是否有任何类型数据
            boolean hasData = false;
            for(Map<AreaEnum, ?> map : maps){
                if(map.containsKey(area)){
                    hasData = true;
                    break;
                }
            }
            //没有数据则跳过
            if(!hasData) continue;

            //为当前区域构建AllocationInput对象
            AllocationInput allocationInput = new AllocationInput(
                    subCarsMap.getOrDefault(area, Collections.emptyList()),
                    ferryMap.getOrDefault(area, Collections.emptyList()),
                    equipmentMap.getOrDefault(area, Collections.emptyList()),
                    positionMap.getOrDefault(area, Collections.emptyList()),
                    workStationMap.getOrDefault(area, Collections.emptyList()),
                    pathMap.getOrDefault(area, Collections.emptyList()),
                    area
            );
            allocationInputs.add(allocationInput);
        }

        ArrayList<OrderInput> orderInputs = new ArrayList<>();

        for(Long orderId : orderIdList){
            OrderInput orderInput = AlgorithmDataConverter.orderDTOToOrderInput(
                    orderService.getOrderDetailById(orderId)
            );
            orderInputs.add(orderInput);
        }

//        // 转换订单和产品信息
//        Map<Order, List<TaskItemDTO>> taskItemsMap = Optional.ofNullable(taskDTO)
//                .map(TaskDTO::getTaskItems)
//                .orElse(Collections.emptyList())
//                .stream()
//                .collect(Collectors.groupingBy(
//                        // 处理 getOrder() 为 null 的情况，这里假设 Order 有 UNKNOWN 常量作为默认值
//                        TaskItemDTO::getOrder,
//                        HashMap::new, // 显式指定 Map 类型
//                        Collectors.toList()
//                ));
//
//        for(Map.Entry<Order, List<TaskItemDTO> > entry : taskItemsMap.entrySet()){
//            Order order = entry.getKey();
//            List<TaskItemDTO> taskItemDTOList = entry.getValue();
//            OrderInput orderInput = AlgorithmDataConverter.toOrderInput(order, taskItemDTOList);
//            orderInputs.add(orderInput);
//        }

        AnalogInput analogInput = new AnalogInput(
                subCars.stream().filter(car -> car.getId() == 1806898891212197890L).findFirst().orElse(null),
                subCars.stream().filter(car -> car.getId() == 1817456665160683522L).findFirst().orElse(null),
                subCars.stream().filter(car -> car.getId() == 1817456988612825089L).findFirst().orElse(null),
                subCars.stream().filter(car -> car.getId() == 1817886619438518273L).findFirst().orElse(null),
                subCars.stream().filter(car -> car.getId() == 1806899642168774658L).findFirst().orElse(null),
                subCars.stream().filter(car -> car.getId() == 1806898464978636801L).findFirst().orElse(null),
                subCars.stream().filter(car -> car.getId() == 1813500565670334466L).findFirst().orElse(null),

                equipments.stream().filter(eq -> eq.getId() == 1796862790497665025L).findFirst().orElse(null),
                equipments.stream().filter(eq -> eq.getId() == 1796862790678020098L).findFirst().orElse(null),
                equipments.stream().filter(eq -> eq.getId() == 1796862790610911234L).findFirst().orElse(null),
                equipments.stream().filter(eq -> eq.getId() == 1796862790812237825L).findFirst().orElse(null),
                equipments.stream().filter(eq -> eq.getId() == 1796862790547996675L).findFirst().orElse(null),
                equipments.stream().filter(eq -> eq.getId() == 1796862790745128961L).findFirst().orElse(null)
        );

        return new SimulateInput(
                allocationInputs,
                orderInputs,
                analogInput
        );
    }

    /**
     * 构造ScheduleOrderInput
     * @param orderIdList 订单ID列表,planType 计划类型
     */
    public ScheduleOrderInput buildScheduleOrderInput (List<Long> orderIdList, PLanType planType){
        //简单校验，注意校验逻辑赢该写在业务中
        if (orderIdList.isEmpty()) {
            log.warn("构建ScheduleOrderInput——orderIdList订单列表为空，清空排产计划");
            return new ScheduleOrderInput(new ArrayList<>());
        }

        // 构建订单项列表
        List<ScheduleOrderItem> items = orderIdList.stream()
                .filter(Objects::nonNull)
                .map(orderId -> this.buildScheduleOrderItem(orderId, planType))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new ScheduleOrderInput(items);
    }

    /**
     * 构建单个ScheduleOrderItem
     * @param orderId 订单ID,planType 计划类型
     * @return ScheduleOrderItem对象
     */
    private ScheduleOrderItem buildScheduleOrderItem(Long orderId, PLanType planType) {
        // 获取订单详情
        Order order = orderManager.getOrderById(orderId);
        if (order == null) {
            log.warn("订单不存在，orderId: {}", orderId);
            return null;
        }

        // 根据计划类型获取订单关联的排产产品实例列表
        List<ProductionItem> productionItems = productManager.getProductionItemsByOrderId(orderId, planType);

        // 构建排产产品输入列表
        List<ScheduleProductInput> productInputs = productionItems.stream()
                .filter(Objects::nonNull)
                .map(item -> new ScheduleProductInput(
                        item.getId(),           // 排产产品ID（来自ProductionItem）
                        item.getProductId(),    // 产品型号ID
                        orderId                 // 订单ID
                ))
                .collect(Collectors.toList());

        // 获取交付时间并转换为分钟
        double deliveryTime = ScheduleDateConverter.localDateTime2Minute(
                order.getDeliveryDeadline()
        );

        return new ScheduleOrderItem(orderId, productInputs, deliveryTime);
    }

//    /**
//     * 根据计划类型初始化排产产品实例
//     * 注意：这里没有对订单状态的维护
//     */
//    private void initProductionItems(List<Long> orderIdList, PLanType planType){
//        //根据计划类型删除排产产品实例
//        productManager.deleteProductionItemByPlanType(planType);
//
//        //根据订单ID查询订单
//        List<Order> orders = orderManager.getOrderByIds(orderIdList);
//
//        //根据订单列表初始化排产产品实例
//        productManager.initProductionItemByOrderIds(orders);
//    }
}
