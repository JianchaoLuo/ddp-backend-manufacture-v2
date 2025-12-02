package cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.dispatch;

import cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentNameEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationNameEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums.ResourceState;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.NodeAttributeTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.closingBox.ClosingBoxDispatchTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.cooling.CoolingDispatchTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.moulding.MouldingDispatchTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.openBox.OpenBoxDispatchTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.pouring.PouringDispatchTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.waitingPour.WaitingPourDispatchTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.EventName;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.event.NodeEventTable;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.input.ResourceInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.resource.ResEvents;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.resource.Resource;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.tree.Node;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.tree.NodeTree;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/12/8 15:43
 */
@Data
public class HandleTool {
    /**
     * 造型线仿真工具类
     */
    MouldingDispatchTool mouldingDispatchTool = new MouldingDispatchTool();
    /**
     * 合箱区仿真工具类
     */
    ClosingBoxDispatchTool closingBoxDispatchTool = new ClosingBoxDispatchTool();
    /**
     * 待浇筑区仿真工具类
     */
    WaitingPourDispatchTool waitingPourDispatchTool = new WaitingPourDispatchTool();
    /**
     * 浇筑区仿真工具类
     */
    PouringDispatchTool pouringDispatchTool = new PouringDispatchTool();
    /**
     * 冷却区仿真工具类
     */
    CoolingDispatchTool coolingDispatchTool = new CoolingDispatchTool();
    /**
     * 开箱区仿真工具类
     */
    OpenBoxDispatchTool openBoxDispatchTool = new OpenBoxDispatchTool();

    /**
     * 通过S生成E；通过当前资源集合生成可执行事件
     *
     * @param resource      资源集合
     * @param resourceInput 资源查询的Input
     * @return 可执行事件
     */
    public ResEvents generateResEventByRescource(Resource resource, ResourceInput resourceInput){
        // 初始化一个空的事件列表
        List<EventName> events = new ArrayList<EventName>();

        // 遍历车辆资源Map
        for (Map.Entry<CarNameEnum, ResourceState> entry : resource.getCarResMap().entrySet()) {
            CarNameEnum carNameEnum = entry.getKey(); // 车辆名称枚举
            ResourceState carState = entry.getValue(); // 车辆状态

            if (carState.equals(ResourceState.IDLE)){
                switch (carNameEnum.getArea()){
                    case AreaEnum.SOUTH_MOULDING:

                        // 根据枚举判断，由造型线生成车辆事件函数
                        mouldingDispatchTool.generateResEventByCar(carNameEnum, resourceInput, events);
                        break;
                    case AreaEnum.SOUTH_COMBINE_BOX:

                        // 根据枚举判断，由合箱区生成车辆事件函数
                        closingBoxDispatchTool.generateResEventByCar(carNameEnum, resourceInput, events);
                        break;
                    case AreaEnum.SOUTH_WAITING_POURING:

                        // 根据枚举判断，由待浇筑区生成车辆事件函数
                        waitingPourDispatchTool.generateResEventByCar(carNameEnum, resourceInput, events);
                    case AreaEnum.SOUTH_POURING:

                        // 根据枚举判断，由浇筑区生成车辆事件函数
                        pouringDispatchTool.generateResEventByCar(carNameEnum, resourceInput, events);
                    case AreaEnum.SOUTH_COOLING:

                        // 根据枚举判断，由浇筑区生成车辆事件函数
                        coolingDispatchTool.generateResEventByCar(carNameEnum, resourceInput, events);
                    case AreaEnum.SOUTH_OPEN_BOX:

                        // 根据枚举判断，由浇筑区生成车辆事件函数
                        openBoxDispatchTool.generateResEventByCar(carNameEnum, resourceInput, events);
                }
            }

        }

        // 遍历工岗资源Map
        for (Map.Entry<WorkstationNameEnum, ResourceState> entry : resource.getWorkstationResMap().entrySet()) {
            WorkstationNameEnum workstationNameEnum = entry.getKey(); // 工岗名称枚举
            ResourceState workstationState = entry.getValue(); // 工岗状态

            if (workstationState.equals(ResourceState.IDLE)){
                switch (workstationNameEnum.getArea()){
                    case AreaEnum.SOUTH_MOULDING:

                        // 根据枚举判断，由造型线生成工岗事件函数
                        mouldingDispatchTool.generateResEventByWorkstation(workstationNameEnum, resourceInput, events);
                        break;
                    case AreaEnum.SOUTH_COMBINE_BOX:

                        // 根据枚举判断，由合箱区生成工岗事件函数
                        closingBoxDispatchTool.generateResEventByWorkstation(workstationNameEnum, resourceInput, events);
                    case AreaEnum.SOUTH_OPEN_BOX:

                        // 根据枚举判断，由开箱区生成工岗事件函数
                        openBoxDispatchTool.generateResEventByWorkstation(workstationNameEnum, resourceInput, events);
                }
            }

        }

        // 遍历设备资源Map
        for (Map.Entry<EquipmentNameEnum, ResourceState> entry : resource.getEquipmentResMap().entrySet()) {
            EquipmentNameEnum equipmentNameEnum = entry.getKey(); // 设备名称枚举
            ResourceState equipmentState = entry.getValue(); // 设备状态

            if (equipmentState.equals(ResourceState.IDLE)){
                if (Objects.requireNonNull(equipmentNameEnum.getArea()) == AreaEnum.SOUTH_POURING) {// 根据枚举判断，由造型线生成工岗事件函数
                    mouldingDispatchTool.generateResEventByEquipment(equipmentNameEnum, resourceInput, events);
                }
            }

        }

        return new ResEvents(events);
    }

    /**
     * 扩展节点方法
     * @param node 树的节点
     * @param resourceInput 资源查询的Input
     */
    public void extensionNode(Node node, ResourceInput resourceInput) {
    }

    /**
     * 将选择的事件Event更新到NodeEventTable中，如果已有相同名称的事件，则将其覆盖
     * @param nodeEventTable 事件存储列表
     * @param nodeAttributeTool 属性工具类
     */
    public void updateNodeEventTable(NodeEventTable nodeEventTable, NodeAttributeTool nodeAttributeTool) {
    }

    /**
     * 更新当前的节点树，将子节点更改到树中
     * @param tree 树
     * @param nodeAttributeTool 属性工具类
     */
    public void updateNodeTree(NodeTree tree, NodeAttributeTool nodeAttributeTool) {
    }

    /**
     * 将当前Event更新到甘特图中（只更新状态为完成事件的事件到甘特图中）
     * 并且更新系统时间
     * 判断完成事件后是否完成一个订单，如果完成则更新订单时间Map<orderTime>
     * @param ganttCharts 最终甘特图
     * @param orderTimes 订单时间Map<orderTime>
     * @param nodeAttributeTool 属性工具类
     */
    public void updateGanttCharts(List<GanttChart> ganttCharts, Map<Long, Double> orderTimes, NodeAttributeTool nodeAttributeTool) {
    }


    /**
     * 更新当前系统时间
     * （如果当前事件为完成事件，将完成的事件从事件表中删除）
     * 并且更新其他完成事件的时间
     * @param nodeEventTable 事件存储列表
     * @param nodeAttributeTool 属性工具类
     */
    public void updateOtherEventTime(NodeEventTable nodeEventTable, NodeAttributeTool nodeAttributeTool) {
    }

    /**
     * 通过当前事件更新资源状态
     *
     * @param tree 当前节点树（只有当前节点）
     * @param resourceInput    资源集合
     * @param nodeAttributeTool 属性工具类
     */
    public void updateRescourceState(NodeTree tree, ResourceInput resourceInput, NodeAttributeTool nodeAttributeTool) {
    }
}
