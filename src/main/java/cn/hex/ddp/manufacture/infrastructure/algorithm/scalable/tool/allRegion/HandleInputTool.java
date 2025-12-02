package cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.tool.allRegion;

import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.util.allRegion.AllRegionInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.util.allRegion.EventExecuteTable;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.util.allRegion.PerformabilityTable;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import lombok.Data;

import java.util.List;
import java.util.Map;

;

/**
 * @ClassDescription: 处理仿真输入的工具类
 * @Author: KangHong
 * @Created: 2024/11/27 20:24
 */
@Data
public class HandleInputTool {
    /**
     * 初始化处理仿真输入
     * @param input 仿真输入
     * @param allRegionInput 各个区域的仿真输入
     */
    public void handleAllInput(SimulateInput input, AllRegionInput allRegionInput) {
    }

    /**
     * 初始化事件执行表
     * @return 事件执行表对象
     */
    public EventExecuteTable initialEventExecuteTable() {

        return null;
    }

    /**
     * 初始化事件可执行表
     * @return 事件可执行表对象
     */
    public PerformabilityTable initialperformabilityTable() {

        return null;
    }

    /**
     * 初始化最终的甘特图
     * 根据每个区域的每个资源，创建相应的甘特图
     * 注意：如果是子母车！！！只创建一个资源对象，子车和母车的事件全部写入同一个List中
     * 因为子母车本质是串行，分开写会导致发指令的时候造成母车提前离开
     * @param allRegionInput 全部区域的资源输入
     * @return 初始化的甘特图
     */
    public List<GanttChart> initialGanttCharts(AllRegionInput allRegionInput) {

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
     * 算法的结束判断
     * @param tool 仿真的属性工具类
     * @return true表示算法执行完毕，应结束；false表示算法未执行完毕，应继续执行
     */
    public boolean allProductFinish(AttributeTool tool) {
        return false;
    }

    /**
     * 判断每个订单是否结束，结束后更新订单时间的Map
     * @param tool 仿真的属性工具类
     * @param orderTimes 订单结束时间Map
     */
    public void judgeOrderTimeMap(AttributeTool tool, Map<Long, Double> orderTimes) {
    }

}
