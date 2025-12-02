package cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.dispatch;

import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateResult;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.tool.allRegion.AttributeTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.util.allRegion.AllRegionInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.util.allRegion.EventExecuteTable;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.util.allRegion.PerformabilityTable;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;

import java.util.List;
import java.util.Map;

/**
 * @ClassDescription: 仿真算法的启动类
 * @Author: KangHong
 * @Created: 2024/11/27 20:06
 */
public class AllRegionSimulation {
    /**
     * 仿真的工具类，所有仿真用到的属性存放在工具类中
     */
    private AttributeTool tool;

    /**
     * 算法函数启动
     * @param input 仿真输入
     * @return 仿真结果输出
     */
    public SimulateResult simulationRun(SimulateInput input){
        //初始化仿真工具类
        tool = initialAllAttribute(input);
        //初始化全部区域的仿真输入类
        AllRegionInput allRegionInput = new AllRegionInput();

        //处理仿真输入，将输入拆分为各区域的资源输入
        tool.getHandleInputTool().handleAllInput(input, allRegionInput);
        //初始化事件可执行表
        PerformabilityTable performabilityTable = tool.getHandleInputTool().initialperformabilityTable();
        //初始化事件执行表
        EventExecuteTable eventExecuteTable = tool.getHandleInputTool().initialEventExecuteTable();
        //初始化甘特图
        List<GanttChart> ganttCharts = tool.getHandleInputTool().initialGanttCharts(allRegionInput);
        //初始化订单结束时间Map
        Map<Long, Double> orderTimes = tool.getHandleInputTool().initialOrderTimes(input);

        do {
            //根据出库区的输入，生成出库区的事件列表
            tool.getDispatchTool().generateOutBoundEvents(performabilityTable, allRegionInput, tool);
            if (!tool.isWaitingPourFlag()){
                //生成合模区的事件列表
                tool.getDispatchTool().generateMoldClosingEvents(performabilityTable, allRegionInput, tool);
                //生成造型线的事件列表
                tool.getDispatchTool().generateMouldingEvents(performabilityTable, allRegionInput, tool);
                //生成合箱区的事件列表
                tool.getDispatchTool().generateClosingBoxEvents(performabilityTable, allRegionInput, tool);
                //生成待浇筑区的事件列表
                tool.getDispatchTool().generateWaitingPourEvents(performabilityTable, allRegionInput, tool);
            }else {
                //生成待浇筑区的事件列表
                tool.getDispatchTool().generateWaitingPourEvents(performabilityTable, allRegionInput, tool);
                //生成浇筑区的事件列表
                tool.getDispatchTool().generatePouringEvents(performabilityTable, allRegionInput, tool);
                //生成冷却区的事件列表
                tool.getDispatchTool().generateCoolingEvents(performabilityTable, allRegionInput, tool);
                //生成开箱区的事件列表
                tool.getDispatchTool().generateOpenBoxEvents(performabilityTable, allRegionInput, tool);
            }

            //搜索选择算法，选择每个资源应执行的事件，将其加入正在执行的事件列表中
            tool.getDispatchTool().selectResEvent(performabilityTable, eventExecuteTable, tool);

            //选择执行事件表中，最短完成时间的事件，更改系统时间
            tool.getDispatchTool().updateSystemTime(eventExecuteTable, tool);

            //判读事件执行表中的事件，将执行完成的事件删除，更改资源状态，将完成的事件加入甘特图
            tool.getDispatchTool().updateEventExecuteTable(eventExecuteTable, allRegionInput, tool, ganttCharts);

            //判断每个订单是否结束，结束后更新订单时间的Map
            tool.getHandleInputTool().judgeOrderTimeMap(tool, orderTimes);

        }while (!tool.getHandleInputTool().allProductFinish(tool));   //算法结束判断

        return null;
    }

    /**
     * 初始化函数，初始化属性工具类
     * @param input 仿真输入
     * @return AttributeTool
     */
    public static AttributeTool initialAllAttribute(SimulateInput input){

        return null;
    };
}
