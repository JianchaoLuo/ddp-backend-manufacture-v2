package cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.tools;

import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.util.table.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassDescription: 调度仿真的工具类，部分功能实现的函数集合
 * @Author: KangHong
 * @Created: 2024/11/17 20:25
 */
@Data
public class DispatchTool {
    /**
     * 查看事件请求表中的事件
     * 将能发生的事件加入平台执行事件表中
     * 注意避免对象的浅拷贝问题
     * @param eventRequestTable 事件请求表
     * @param resourceTable 资源状态表
     * @param productEventExecuteTable 更新前的平台执行事件表
     */
    public void findExecuteEvent(EventRequestTable eventRequestTable, ResourceTable resourceTable, ProductEventExecuteTable productEventExecuteTable){

    }

    /**
     * 将平台执行事件表中未开始执行的事件状态设置为正在执行
     * 并且更新对应的甘特图
     * 将执行的事件加入相应的甘特图中
     * @param productEventExecuteTable 平台执行事件表
     * @param ganttCharts 全局甘特图
     */
    public void startExecuteEvent(ProductEventExecuteTable productEventExecuteTable, List<GanttChart> ganttCharts){

    }

    /**
     * 根据系统时间判断完成的事件
     * 如果有完成的事件，更新事件执行表
     * 更改资源状态，更新资源状态表
     * 更新产品事件表
     * 如果需要则更新车辆执行表（并且更新甘特图）
     * @param systemTime 系统动态时间
     * @param productEventExecuteTable 事件执行表
     * @param resourceTable 资源状态表
     * @param productTable 产品事件表
     * @param carExecuteTable 车辆执行表
     * @param ganttCharts 全局甘特图
     */
    public void judgeFinishEvent(Double systemTime, ProductEventExecuteTable productEventExecuteTable, ResourceTable resourceTable,
                                 ProductTable productTable, CarExecuteTable carExecuteTable, List<GanttChart> ganttCharts) {

    }

    /**
     * 根据系统时间完成车辆执行表中的车辆，更改车辆状态
     * @param systemTime 系统动态时间
     * @param carExecuteTable 车辆执行表
     * @param resourceTable 资源状态表
     */
    public void judgeCarFinishInstruction(Double systemTime, CarExecuteTable carExecuteTable, ResourceTable resourceTable) {

    }

    /**
     * 更新请求事件表
     * 根据产品事件表和资源状态更新需要请求的事件
     * @param eventRequestTable 请求事件表
     * @param productTable 产品事件表
     * @param resourceTable 资源状态表
     */
    public void updateEventRequestTable(EventRequestTable eventRequestTable, ProductTable productTable, ResourceTable resourceTable) {

    }

    /**
     * 根据事件执行表和车辆执行表找出最短时间
     * 将系统时间更改为最短时间
     * @param systemTime 系统动态时间
     * @param carExecuteTable 车辆执行表
     * @param productEventExecuteTable 事件执行表
     * @return
     */
    public Double updateSystemTime(Double systemTime, CarExecuteTable carExecuteTable, ProductEventExecuteTable productEventExecuteTable) {

        return null;
    }

    /**
     * 调度结束判断
     * @param productTable 产品事件表
     * @return true表示执行完成，false表示未执行完成
     */
    public boolean judgeFinishAllEvent(ProductTable productTable) {

        return false;
    }

    /**
     * 更新计算所有订单的完成时间
     * @param orderTimes 订单完成时间的Map
     * @param productTable 产品事件表
     * @param input 仿真输入
     */
    public void updateOrderTimes(Map<Long, Double> orderTimes, ProductTable productTable, SimulateInput input) {

    }
}
