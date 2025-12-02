package cn.hex.ddp.manufacture.infrastructure.algorithm_2.simulation.dispatch;

import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateResult;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.NodeAttributeTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.SimulationTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.dispatch.HandleTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.dispatch.NodeDispatchTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.event.NodeEventTable;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.input.ResourceInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.resource.Resource;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.tree.Node;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.tree.NodeTree;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/12/8 15:28
 */
@Data
public class NodeSimulation {
    /**
     * 算法函数启动
     * @param input 仿真输入
     * @return 仿真结果输出
     */
    public SimulateResult simulationRun(SimulateInput input){
        //仿真用到的工具类
        SimulationTool simulationTool = new SimulationTool();
        //仿真需要的部分属性的工具类
        NodeAttributeTool nodeAttributeTool;
        //处理仿真的工具类
        HandleTool handleTool = new HandleTool();
        //调度需要用到的工具类
        NodeDispatchTool dispatchTool = new NodeDispatchTool();

        //初始化属性工具类
        nodeAttributeTool = simulationTool.initialNodeAttribute(input);
        //初始化资源Input
        ResourceInput resourceInput = simulationTool.initialRescourceInput(input);
        //初始化资源集合S
        Resource resource = simulationTool.initialRescource(resourceInput);
        //初始化所有事件列表
        NodeEventTable nodeEventTable = simulationTool.initialNodeEventTable();
        //初始化根节点
        Node root = simulationTool.initialRootNode(resource);
        //根据S生成E，根据资源生成可执行事件（只生成执行事件，不生成结束事件）
        root.setEvents(handleTool.generateResEventByRescource(root.getResource(), resourceInput));
//        //扩展初始化节点（扩展节点方法中不需要将子节点的S生成E）
//        handleTool.extensionNode(root, rescourceInput);
        //初始化节点树，将初节点加入节点树中
        NodeTree tree = new NodeTree(root);
        //初始化甘特图
        List<GanttChart> ganttCharts = simulationTool.initialGanttCharts(resourceInput);
        //初始化订单结束时间Map
        Map<Long, Double> orderTimes = simulationTool.initialOrderTimes(input);

        do {
            //选择算法，选择树中当前节点的E中的一个事件，更新到当前执行事件Event（在属性工具类中），判断当前资源是否有可以发生的事件，如果所有资源的事件都不可发生，则更新为null
            dispatchTool.selectEvent(tree, nodeAttributeTool);
            //将选择的事件Event更新到NodeEventTable中，注意在NodeEventTable中只更新完成事件，当前事件为null，则在NodeEventTable中选择最短时间的完成事件更新到当前事件中
            handleTool.updateNodeEventTable(nodeEventTable, nodeAttributeTool);
            //更新当前资源状态
            handleTool.updateRescourceState(tree, resourceInput, nodeAttributeTool);
            //将当前Event更新到甘特图中（只更新状态为完成事件的事件到甘特图中）
            handleTool.updateGanttCharts(ganttCharts, orderTimes, nodeAttributeTool);
            //更新当前系统时间（如果当前事件为完成事件，将完成的事件从事件表中删除），并且更新其他完成事件的时间
            handleTool.updateOtherEventTime(nodeEventTable, nodeAttributeTool);
            //根据S生成E（如果为叶子节点则生成为null）
            tree.getNode().setEvents(handleTool.generateResEventByRescource(tree.getNode().getResource(), resourceInput));

//            //扩展当前树的节点（如果为叶子节点则扩展为null）
//            handleTool.extensionNode(tree.getNode(), rescourceInput);
//            //将其子节点更改到树中
//            handleTool.updateNodeTree(tree, nodeAttributeTool);
        }while (!simulationTool.finishJudge(tree, nodeAttributeTool));   //结束判断

        return new SimulateResult(ganttCharts, orderTimes);
    }
}
