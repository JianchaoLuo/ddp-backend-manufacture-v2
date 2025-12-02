package cn.hex.ddp.manufacture.infrastructure.algorithm.multitruck;

import cn.hex.ddp.manufacture.infrastructure.algorithm.multitruck.input.OutBoundSimulationMultiCarInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.multitruck.multiCarTools.outBound.OutBoundANDMoldClosingMultiCar;
import cn.hex.ddp.manufacture.infrastructure.algorithm.multitruck.multiCarTools.outBound.OutBoundMuktiCarEventFlow;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.Stereoscopic;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.moldClosing.MoldClosingGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound.OutBoundSimulationTools;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound.multiCar.ColdSandBoxRefulxSimulationMultiCar;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound.multiCar.CoreMakingSimulationMultiCar;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound.multiCar.MoldRefulxSimulationMultiCar;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound.multiCar.SandBoxRefulxSimulationMultiCar;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import lombok.Data;

import java.util.Deque;
import java.util.LinkedList;

import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.OCCUPIED;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/10/29 16:08
 */
@Data
public class OutBoundSimulationMultiCar {
    /**
     * 区域所处的时间
     * 出库轨道车辆的时间
     */
    private double outBoundTime_Hot = 0.0;
    /**
     * 区域所处的时间
     * 出库轨道车辆的时间
     */
    private double outBoundTime_Cold = 0.0;
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean hotStateChange = false;
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean coldStateChange = false;
    /**
     * 出库区仿真的工具类
     */
    private OutBoundSimulationTools tools = new OutBoundSimulationTools();
    /**
     * 出库区模拟的立体库
     */
    private Stereoscopic stereoscopic = new Stereoscopic();

    /**
     * 制芯轨道仿真
     */
    private CoreMakingSimulationMultiCar coreMakingSimulationMultiCar = new CoreMakingSimulationMultiCar();
    /**
     * 模具回流轨道仿真
     */
    private MoldRefulxSimulationMultiCar moldRefulxSimulationMultiCar = new MoldRefulxSimulationMultiCar();
    /**
     * 砂箱回流轨道仿真
     */
    private SandBoxRefulxSimulationMultiCar sandBoxRefulxSimulationMultiCar = new SandBoxRefulxSimulationMultiCar();
    /**
     * 冷砂回流轨道仿真
     */
    private ColdSandBoxRefulxSimulationMultiCar coldSandBoxRefulxSimulationMultiCar =  new ColdSandBoxRefulxSimulationMultiCar();

    /**
     * 创建事件的类
     */
    private final OutBoundMuktiCarEventFlow outBoundMuktiCarEventFlow = new OutBoundMuktiCarEventFlow();

    /**
     * 回流产品列表，简单测试使用，后期会更改
     */
    private Deque<Product> refluxProducts = new LinkedList<>();

    private SimulationTool simulationTool = new SimulationTool();

    private double eventTime = 0.0;

    public OutBoundANDMoldClosingMultiCar OutBoundMultiCarSimulationRun(OutBoundANDMoldClosingMultiCar input,
                                                                        GanttChart outBoundGanttChart, int finishedCount, Double[] timeList) {
        OutBoundSimulationMultiCarInput outBoundInput = input.getOutBoundMultiCarGanttLinkInput().getInput();
        MoldClosingGanttChartLinkInput moldInput = input.getMoldClosingGanttChartLinkInput();

        //判断时间总时间线是否到达
        //总时间线未到区域时间，故此时间内正在进行事件，不发生其他事件
        if (outBoundInput.getTotalTime() >= outBoundTime_Hot){
            //判断子车是否空载
            if (tools.judgeSubCarIsEmpty(outBoundInput.getSubCars().get(outBoundInput.getOutBoundIndexInputMultiCar().getOutBoundSubcarHotIndex()))){
                //判断砂箱回流点是否有带搬运的回流砂箱
                if (outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInputMultiCar().getSandBoxRefluxPositionIndex()).getStatus().equals(OCCUPIED)){
                    //子母车前往砂箱回流点搬运回流的砂箱
                    if (hotStateChange) {

                        input = outBoundMuktiCarEventFlow.HotoutBoundEmptyGoRefluxEventCreate(input, finishedCount);
                        outBoundInput = input.getOutBoundMultiCarGanttLinkInput().getInput();
                        moldInput = input.getMoldClosingGanttChartLinkInput();

                        hotStateChange = false;

                    }else {
                        double executionTime = (Math.abs(outBoundInput.getOutboundFerryHot().getLocationCoordinate().getY() -
                                outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInputMultiCar().getSandBoxRefluxPositionIndex()).getCoordinate().getY())
                                / outBoundInput.getOutboundFerryHot().getEmptySpeed()) +
                                (Math.abs(outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInputMultiCar().getSandBoxRefluxPositionIndex()).getCoordinate().getX() -
                                        outBoundInput.getOutboundFerryHot().getLocationCoordinate().getX()) /
                                        outBoundInput.getSubCars().get(outBoundInput.getOutBoundIndexInputMultiCar().getOutBoundSubcarHotIndex()).getEmptySpeed()) +
                                (outBoundInput.getSubCars().get(outBoundInput.getOutBoundIndexInputMultiCar().getOutBoundSubcarHotIndex()).getTopRodRaiseOrFallTime()) +
                                (Math.abs(outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInputMultiCar().getSandBoxRefluxPositionIndex()).getCoordinate().getX() -
                                        outBoundInput.getOutboundFerryHot().getLocationCoordinate().getX()) /
                                        outBoundInput.getSubCars().get(outBoundInput.getOutBoundIndexInputMultiCar().getOutBoundSubcarHotIndex()).getFullSpeed());

                        outBoundTime_Hot = outBoundInput.getTotalTime() + executionTime;
                        hotStateChange = true;

                        eventTime = executionTime;
                    }

                    //判断模具回流点是否有带搬运的回流砂箱
                }else if (outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInputMultiCar().getMoldRefluxPositionIndex()).getStatus().equals(OCCUPIED)){
                    //子母车前往模具回流点搬运回流的模具
                }

                //子车满载
            }else {

            }
        }

        return null;
    }
}
