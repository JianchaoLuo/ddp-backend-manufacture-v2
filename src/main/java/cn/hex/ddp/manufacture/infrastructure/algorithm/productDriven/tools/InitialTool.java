package cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.tools;

import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.util.table.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassDescription: 初始化工具类，初始化函数的集合
 * @Author: KangHong
 * @Created: 2024/11/20 17:18
 */
@Data
public class InitialTool {
    /**
     * 初始化产品表
     * 通过仿真的输入，初始化生成要生产的所有产品的事件列表
     * @param input 仿真的输入
     * @return 产品事件列表
     */
    public ProductTable initialProductEventTable(SimulateInput input){

        return null;
    }

    /**
     * 初始化资源状态表
     * 通过仿真的输入，初始化生成要生产的所有资源的状态集合表
     * @param input 仿真的输入
     * @return 所有资源的状态集合表
     */
    public ResourceTable initialResourceTable(SimulateInput input){

        return null;
    }

    /**
     * 初始化事件执行表
     * 创建一个空对象即可
     * @return 初始化执行的事件执行表
     */
    public ProductEventExecuteTable initialEventExecuteTable() {

        return null;
    }

    /**
     * 初始化事件请求表
     * 将所有产品的第一个事件加入到事件请求表中
     * @param productTable 产品表
     * @return 初始化请求执行的事件执行表
     */
    public EventRequestTable initialEventRequestTable(ProductTable productTable) {

        return null;
    }

    /**
     * 初始化车辆执行表
     * @return 车辆执行表
     */
    public CarExecuteTable initialCarExecuteTable() {

        return null;
    }

    /**
     * 初始化甘特图
     * @param input 仿真输入
     * @return 全局甘特图
     */
    public List<GanttChart> initialGanttCharts(SimulateInput input) {

        return null;
    }

    /**
     * 初始化订单结束时间Map
     * @param input 仿真输入
     * @return 订单结束时间Map
     */
    public Map<Long, Double> initialOrderTimes(SimulateInput input) {

        return null;
    }
}
