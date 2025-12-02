package cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool;

import cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum;
import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentNameEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathNameEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationNameEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.AllocationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.OrderInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.OrderProduct;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.OrderProductTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums.ResourceState;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.event.NodeEventTable;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.input.ResourceInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.resource.Resource;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.tree.Node;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.tree.NodeTree;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassDescription: 工具类
 * @Author: KangHong
 * @Created: 2024/12/7 22:49
 */
@Data
public class SimulationTool {
    /**
     * 根据仿真输入初始化仿真属性工具类
     * @param input 仿真输入
     * @return 仿真属性工具类
     */
    public NodeAttributeTool initialNodeAttribute(SimulateInput input) {
        NodeAttributeTool nodeAttributeTool = new NodeAttributeTool();
        //初始化当前选择的事件
        nodeAttributeTool.setSelectEvent(null);
        //初始化仿真总时间
        nodeAttributeTool.setTotalTime(0.0);
        //初始化事件最小完成时间
        nodeAttributeTool.setEventMinTime(0.0);
        //计算热砂出库队列和冷砂出库队列
        Deque<Product> hotProducts = new ArrayDeque<>();
        Deque<Product> coldProducts = new ArrayDeque<>();
        // 遍历订单
        for (OrderInput orderInput : input.getOrderInputs()){
            // 遍历订单产品列表
            for (OrderProduct orderProduct : orderInput.getOrderProducts()){
                //出库产品为生产一个铸件需要的所有产品 * 铸件数量
                int number = orderProduct.getNumber();
                //订单为热砂订单
                if(orderProduct.getProductType().equals(OrderProductTypeEnum.HOT_SAND)){
                    for(int i = 0;i < number;i++){
                        //遍历产品
                        for(Product product : orderProduct.getProducts()){
                            //产品不为模具和砂芯模具
                            if(!product.getProductType().equals(ProductTypeEnum.MOULD) &&
                                    !product.getProductType().equals(ProductTypeEnum.SAND_MOULD)){
                                hotProducts.add(product);
                            }
                        }
                    }
                    //订单为冷砂订单
                }else if(orderProduct.getProductType().equals(OrderProductTypeEnum.COLD_SAND)){
                    for(int i = 0;i < number;i++){
                        //遍历产品
                        for(Product product : orderProduct.getProducts()){
                            //产品不为模具和砂芯模具
                            if(!product.getProductType().equals(ProductTypeEnum.MOULD) &&
                                    !product.getProductType().equals(ProductTypeEnum.SAND_MOULD)){
                                coldProducts.add(product);
                            }
                        }
                    }
                }
            }
        }
        //初始化热砂出库产品队列
        nodeAttributeTool.setHotProducts(hotProducts);
        //初始化冷砂出库产品队列
        nodeAttributeTool.setColdProducts(coldProducts);
        //初始化开始浇筑的标识
        nodeAttributeTool.setWaitingPourFlag(false);
        //初始化待浇筑区的产品数目
        nodeAttributeTool.setWaitingPourCount(0);
        //初始化铸件计数
        nodeAttributeTool.setCastingCount(0);
        //初始化完成数量
        nodeAttributeTool.setFinishedCount(0);
        //计算订单数量列表，key为订单id，value为铸件数量
        Map<Long, Integer> orderNums = new HashMap<>();
        // 遍历订单
        for(OrderInput orderInput : input.getOrderInputs()){
            long id = orderInput.getId();
            int number = 0;
            // 遍历订单产品列表
            for(OrderProduct orderProduct : orderInput.getOrderProducts()){
                number = number + orderProduct.getNumber();
            }
            orderNums.put(id, number);
        }
        //初始化订单数量列表
        nodeAttributeTool.setOrderNums(orderNums);
        //初始化铁水用量
        nodeAttributeTool.setUsageDoubleList(null);
        return nodeAttributeTool;
    }

    /**
     * 根据仿真输入初始化仿真资源Input类
     * @param input 仿真输入
     * @return 仿真资源Input类
     */
    public ResourceInput initialRescourceInput(SimulateInput input) {
        ResourceInput resourceInput = new ResourceInput();
        List<AllocationInput> allocationInputs = input.getAllocationInputs();

        // 将每个区域AllocationInput中的List<SubCar>拼接成一个大的List<SubCar>
        List<SubCar> allSubCars = allocationInputs.stream()
                .flatMap(allocationInput -> Optional.ofNullable(allocationInput.getSubCars())
                        .orElse(Collections.emptyList()).stream())
                .collect(Collectors.toList());
        // 初始化SubCars
        resourceInput.setSubCars(allSubCars);

        // 将每个区域AllocationInput中的List<Ferry>拼接成一个大的List<Ferry>
        List<Ferry> allFerries = allocationInputs.stream()
                .flatMap(allocationInput -> Optional.ofNullable(allocationInput.getFerries())
                        .orElse(Collections.emptyList()).stream())
                .collect(Collectors.toList());
        // 初始化Ferries
        resourceInput.setFerries(allFerries);

        // 将每个区域AllocationInput中的List<Equipment>拼接成一个大的List<Equipment>
        List<Equipment> allEquipments = allocationInputs.stream()
                .flatMap(allocationInput -> Optional.ofNullable(allocationInput.getEquipments())
                        .orElse(Collections.emptyList()).stream())
                .collect(Collectors.toList());
        // 初始化Equipments
        resourceInput.setEquipments(allEquipments);

        // 将每个区域AllocationInput中的List<Position>拼接成一个大的List<Position>
        List<Position> allPositions = allocationInputs.stream()
                .flatMap(allocationInput -> Optional.ofNullable(allocationInput.getPositions())
                        .orElse(Collections.emptyList()).stream())
                .collect(Collectors.toList());
        // 初始化Positions
        resourceInput.setPositions(allPositions);

        // 将每个区域AllocationInput中的List<WorkStation>拼接成一个大的List<WorkStation>
        List<WorkStation> allWorkStations = allocationInputs.stream()
                .flatMap(allocationInput -> Optional.ofNullable(allocationInput.getWorkStations())
                        .orElse(Collections.emptyList()).stream())
                .collect(Collectors.toList());
        // 初始化WorkStations
        resourceInput.setWorkStations(allWorkStations);

        // 将每个区域AllocationInput中的List<Path>拼接成一个大的List<Path>
        List<Path> allPaths = allocationInputs.stream()
                .flatMap(allocationInput -> Optional.ofNullable(allocationInput.getPaths())
                        .orElse(Collections.emptyList()).stream())
                .collect(Collectors.toList());
        // 初始化Paths
        resourceInput.setPaths(allPaths);
        return resourceInput;
    }

    /**
     * 根据仿真资源Input类初始化生成S（节点中的资源集合）
     * @param resourceInput 仿真资源Input类
     * @return S（节点中的资源集合）
     */
    public Resource initialRescource(ResourceInput resourceInput) {
        // 初始化 Rescource 的 Map
        Map<CarNameEnum, ResourceState> carResMap = new HashMap<>();
        Map<PositionNameEnum, ResourceState> positionResMap = new HashMap<>();
        Map<WorkstationNameEnum, ResourceState> workstationResMap = new HashMap<>();
        Map<EquipmentNameEnum, ResourceState> equipmentResMap = new HashMap<>();
        Map<PathNameEnum, ResourceState> pathResMap = new HashMap<>();

        // 填充 carResMap
        for (SubCar subCar : resourceInput.getSubCars()) {
            carResMap.put(subCar.getNameEnum(), ResourceState.IDLE); // 假设初始状态为 IDLE
        }
        for (Ferry ferry : resourceInput.getFerries()) {
            carResMap.put(ferry.getNameEnum(), ResourceState.IDLE); // 假设初始状态为 IDLE
        }

        // 填充 positionResMap
        for (Position position : resourceInput.getPositions()) {
            positionResMap.put(position.getNameEnum(), ResourceState.IDLE); // 假设初始状态为 IDLE
        }

        // 填充 workstationResMap
        for (WorkStation workStation : resourceInput.getWorkStations()) {
            workstationResMap.put(workStation.getNameEnum(), ResourceState.IDLE); // 假设初始状态为 IDLE
        }

        // 填充 equipmentResMap
        for (Equipment equipment : resourceInput.getEquipments()) {
            equipmentResMap.put(equipment.getNameEnum(), ResourceState.IDLE); // 假设初始状态为 IDLE
        }

        // 填充 pathResMap
        for (Path path : resourceInput.getPaths()) {
            pathResMap.put(path.getNameEnum(), ResourceState.IDLE); // 假设初始状态为 IDLE
        }

        // 创建并返回 Rescource 对象
        return new Resource(carResMap, positionResMap, workstationResMap, equipmentResMap, pathResMap);
    }

    /**
     * 初始化根节点
     * @param resource 资源状态
     * @return 根节点
     */
    public Node initialRootNode(Resource resource) {

        return null;
    }

    /**
     * 初始化事件列表
     * @return NodeEventTable
     */
    public NodeEventTable initialNodeEventTable() {
        // 创建一个空的 NodeEventTable 对象
        NodeEventTable nodeEventTable = new NodeEventTable();
        // 初始化事件列表
        nodeEventTable.setNodeLinkEvents(new ArrayList<>());
        // 返回初始化的 NodeEventTable 对象
        return nodeEventTable;
    }

    /**
     * 初始化最终的甘特图
     * 根据每个区域的每个资源，创建相应的甘特图
     * 注意：如果是子母车！！！只创建一个资源对象，子车和母车的事件全部写入同一个List中
     * 因为子母车本质是串行，分开写会导致发指令的时候造成母车提前离开
     * @param resourceInput 全部区域的资源输入
     * @return 初始化的甘特图
     */
    public List<GanttChart> initialGanttCharts(ResourceInput resourceInput) {

        return null;
    }

    /**
     * 初始化订单结束事件Map
     * @param input 仿真输入
     * @return 初始化的Map
     */
    public Map<Long, Double> initialOrderTimes(SimulateInput input) {

        return null;
    }

    /**
     * 结束判断函数
     *
     * @param tree              节点树
     * @param nodeAttributeTool 属性工具类
     * @return true为完成所有应该结束，false表示不应该结束
     */
    public boolean finishJudge(NodeTree tree, NodeAttributeTool nodeAttributeTool) {
        return false;
    }
}
