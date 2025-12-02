package cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.tool.allRegion;

import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.tool.closingBox.ClosingBoxTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.tool.cooling.CoolingTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.tool.moldClosing.MoldClosingTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.tool.moulding.MouldingTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.tool.outBound.OutBoundTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.tool.pouring.PouringTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.tool.waitingPour.WaitingPourTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.util.allRegion.AllRegionInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.util.allRegion.EventExecuteTable;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.util.allRegion.PerformabilityTable;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.util.openBox.OpenBoxInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import lombok.Data;

import java.util.List;

/**
 * @ClassDescription: 调度需要用到的工具类
 * @Author: KangHong
 * @Created: 2024/11/27 20:33
 */
@Data
public class DispatchTool {
    /**
     * 出库区工具类
     */
    OutBoundTool outBoundTool;
    /**
     * 合箱区工具类
     */
    MoldClosingTool moldClosingTool;
    /**
     * 造型线工具类
     */
    MouldingTool mouldingTool;
    /**
     * 合箱区工具类
     */
    ClosingBoxTool closingBoxTool;
    /**
     * 待浇筑区工具类
     */
    WaitingPourTool waitingPourTool;
    /**
     * 浇筑区工具类
     */
    PouringTool pouringTool;
    /**
     * 冷却区工具类
     */
    CoolingTool coolingTool;
    /**
     * 开箱区工具类
     */
    OpenBoxInput openBoxInput;

    /**
     * 生成出库区的事件列表
     * 根据出库区的状态，生成出库区可执行的全部事件
     * 事件存放在事件可执行表中
     * @param performabilityTable 事件可执行表
     * @param allRegionInput 全部区域的仿真输入类
     * @param tool 仿真的属性工具类
     */
    public void generateOutBoundEvents(PerformabilityTable performabilityTable, AllRegionInput allRegionInput, AttributeTool tool) {
    }

    /**
     * 生成合模区的事件列表
     * 根据合模区的状态，生成合模区可执行的全部事件
     * 事件存放在事件可执行表中
     * @param performabilityTable 事件可执行表
     * @param allRegionInput 全部区域的仿真输入类
     * @param tool 仿真的属性工具类
     */
    public void generateMoldClosingEvents(PerformabilityTable performabilityTable, AllRegionInput allRegionInput, AttributeTool tool) {
    }

    /**
     * 生成造型线的事件列表
     * 根据造型线的状态，生成造型线可执行的全部事件
     * 事件存放在事件可执行表中
     * @param performabilityTable 事件可执行表
     * @param allRegionInput 全部区域的仿真输入类
     * @param tool 仿真的属性工具类
     */
    public void generateMouldingEvents(PerformabilityTable performabilityTable, AllRegionInput allRegionInput, AttributeTool tool) {
    }

    /**
     * 生成合箱区的事件列表
     * 根据合箱区的状态，生成合箱区可执行的全部事件
     * 事件存放在事件可执行表中
     * @param performabilityTable 事件可执行表
     * @param allRegionInput 全部区域的仿真输入类
     * @param tool 仿真的属性工具类
     */
    public void generateClosingBoxEvents(PerformabilityTable performabilityTable, AllRegionInput allRegionInput, AttributeTool tool) {
    }

    /**
     * 生成待浇筑区的事件列表
     * 根据待浇筑区的状态，生成待浇筑区可执行的全部事件
     * 事件存放在事件可执行表中
     * @param performabilityTable 事件可执行表
     * @param allRegionInput 全部区域的仿真输入类
     * @param tool 仿真的属性工具类
     */
    public void generateWaitingPourEvents(PerformabilityTable performabilityTable, AllRegionInput allRegionInput, AttributeTool tool) {
    }

    /**
     * 生成浇筑区的事件列表
     * 根据浇筑区的状态，生成浇筑区可执行的全部事件
     * 事件存放在事件可执行表中
     * @param performabilityTable 事件可执行表
     * @param allRegionInput 全部区域的仿真输入类
     * @param tool 仿真的属性工具类
     */
    public void generatePouringEvents(PerformabilityTable performabilityTable, AllRegionInput allRegionInput, AttributeTool tool) {
    }

    /**
     * 生成冷却区的事件列表
     * 根据冷却区的状态，生成冷却区可执行的全部事件
     * 事件存放在事件可执行表中
     * @param performabilityTable 事件可执行表
     * @param allRegionInput 全部区域的仿真输入类
     * @param tool 仿真的属性工具类
     */
    public void generateCoolingEvents(PerformabilityTable performabilityTable, AllRegionInput allRegionInput, AttributeTool tool) {
    }

    /**
     * 生成开箱区的事件列表
     * 根据开箱区的状态，生成开箱区可执行的全部事件
     * 事件存放在事件可执行表中
     * @param performabilityTable 事件可执行表
     * @param allRegionInput 全部区域的仿真输入类
     * @param tool 仿真的属性工具类
     */
    public void generateOpenBoxEvents(PerformabilityTable performabilityTable, AllRegionInput allRegionInput, AttributeTool tool) {
    }

    /**
     * 搜索选择算法，选择每个资源应执行的事件，将其加入正在执行的事件列表中
     * @param performabilityTable 事件可执行表
     * @param eventExecuteTable 事件正在执行表
     * @param tool 仿真的属性工具类
     */
    public void selectResEvent(PerformabilityTable performabilityTable, EventExecuteTable eventExecuteTable, AttributeTool tool) {
    }

    /**
     * 选择执行事件表中，最短完成时间的事件，更改系统时间
     * @param eventExecuteTable 事件正在执行表
     * @param tool 仿真的属性工具类
     */
    public void updateSystemTime(EventExecuteTable eventExecuteTable, AttributeTool tool) {
    }

    /**
     * 判读事件执行表中的事件，将执行完成的事件删除，更改资源状态，将完成的事件加入甘特图
     *
     * @param eventExecuteTable 事件正在执行表
     * @param allRegionInput
     * @param tool              仿真的属性工具类
     * @param ganttCharts       甘特图
     */
    public void updateEventExecuteTable(EventExecuteTable eventExecuteTable, AllRegionInput allRegionInput, AttributeTool tool, List<GanttChart> ganttCharts) {
    }
}
