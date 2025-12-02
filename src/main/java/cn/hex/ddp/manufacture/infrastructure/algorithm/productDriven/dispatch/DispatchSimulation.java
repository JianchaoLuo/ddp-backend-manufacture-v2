package cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.dispatch;

import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateResult;
import cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.tools.DispatchTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.tools.InitialTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.util.table.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassDescription: 调度资源仿真类
 * @Author: KangHong
 * @Created: 2024/11/17 20:13
 */
@Data
public class DispatchSimulation {
    /**
     * 工具类
     */
    DispatchTool tool;
    /**
     * 初始化工具类
     */
    InitialTool initialTool;
    /**
     * 系统仿真时间
     * 用于实现离散事件动态系统的时间推进
     * 每次的系统时间应更新为最短事件完成时间或车辆指令最短完成时间
     */
    Double systemTime;

    public SimulateResult simulationRun(SimulateInput input){
        //初始化产品表
        ProductTable productTable = initialTool.initialProductEventTable(input);
        //初始化资源状态表
        ResourceTable resourceTable = initialTool.initialResourceTable(input);
        //初始化平台执行事件表
        ProductEventExecuteTable eventExecuteTable = initialTool.initialEventExecuteTable();
        //初始化请求事件表
        EventRequestTable eventRequestTable = initialTool.initialEventRequestTable(productTable);
        //初始化车辆执行表
        CarExecuteTable carExecuteTable = initialTool.initialCarExecuteTable();
        //初始化全局甘特图
        List<GanttChart> ganttCharts = initialTool.initialGanttCharts(input);
        //初始化订单完成时间Map，具体功能为仿真输出的属性
        Map<Long, Double> orderTimes = initialTool.initialOrderTimes(input);
        //初始化系统时间
        systemTime = 0.0;

        do {
            //根据系统时间判断完成的事件，更改资源状态，更新产品表，更新车辆执行表（更新甘特图）
            tool.judgeFinishEvent(systemTime, eventExecuteTable, resourceTable, productTable, carExecuteTable, ganttCharts);
            //根据系统时间完成车辆执行表中的车辆，更改车辆状态
            tool.judgeCarFinishInstruction(systemTime, carExecuteTable, resourceTable);
            //查看事件请求表中的事件，将能发生的事件加入平台执行事件表中
            tool.findExecuteEvent(eventRequestTable, resourceTable, eventExecuteTable);
            //将加入事件开始执行（改变资源状态），并且更新甘特图
            tool.startExecuteEvent(eventExecuteTable, ganttCharts);
            //更新请求事件表
            tool.updateEventRequestTable(eventRequestTable, productTable, resourceTable);
            //更新系统时间
            systemTime = tool.updateSystemTime(systemTime, carExecuteTable, eventExecuteTable);

            //结束判断
        } while (!tool.judgeFinishAllEvent(productTable));

        //更新所有订单的完成时间
        tool.updateOrderTimes(orderTimes, productTable, input);

        return new SimulateResult(ganttCharts, orderTimes);
    }
}
