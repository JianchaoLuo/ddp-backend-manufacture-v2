package cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.simulation_v2;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.outbound.ColdSandMoldingEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.OutBoundSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound.ColdSandBoxRefluxEventFlow;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound.OutBoundGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2025/1/4 22:15
 */
public class ColdSandBoxRefluxSimulation_2 {
    /**
     * 冷砂回流轨道车辆时间
     */
    private double coldSandBoxRefluxTime = 0.0;
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean coldSandBoxStateChange = false;
    /**
     * 创建事件的类
     */
    private final ColdSandBoxRefluxEventFlow eventFlow = new ColdSandBoxRefluxEventFlow();
    /**
     * 仿真总工具类
     */
    private final SimulationTool allTool = new SimulationTool();

    /**
     * 枚举类，用于查找当前执行的事件，从而进行当前事件的资源状态的改变，避免出错
     */
    private ColdSandMoldingEnum carEventEnum;

    private double eventTime = 0.0;

    public OutBoundGanttChartLinkInput ColdSandBoxRefluxSimulationRun(OutBoundSimulationInput input, GanttChart outBoundGanttChart, Double[] timeList){
        OutBoundGanttChartLinkInput simulationOutput = new OutBoundGanttChartLinkInput(outBoundGanttChart, input);

        Coordinate midCoordinate = new Coordinate((input.getPositions().get(input.getOutBoundIndexInput().getColdSandRowCarPositionIndex()).getCoordinate().getX() +
                input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate().getX()) / 2,
                input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getLocationCoordinate().getY());

        if (input.getTotalTime() >= coldSandBoxRefluxTime){
            if (!coldSandBoxStateChange){
                if (allTool.judgeSubCarIsEmpty(input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()))){
                    //判断冷砂回流轨道点位上是否有产品
                    if (input.getPositions().get(input.getOutBoundIndexInput().getColdSandRowCarPositionIndex()).getStatus().equals(PositionStatusEnum.OCCUPIED)){
                        //发生前往点位装载砂箱的事件
                        double executionTime = (Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getLocationCoordinate().getX() -
                                input.getPositions().get(input.getOutBoundIndexInput().getColdSandRowCarPositionIndex()).getCoordinate().getX())
                                / input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getEmptySpeed()) +
                                input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getTopRodRaiseOrFallTime();

                        coldSandBoxRefluxTime = input.getTotalTime() + executionTime;

                        coldSandBoxStateChange = true;
                        carEventEnum = ColdSandMoldingEnum.EMPTY_SUB_CAR_GO_COLD_SAND_REFLUX_LAY_POSITION_LOAD;
                        eventTime = executionTime;

                    }else {
                        //判断车辆是否到达中间点位
                        if (!input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getLocationCoordinate().equals(midCoordinate)){
                            //发生前往中间点位的事件
                            double executionTime = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getLocationCoordinate().getX() -
                                    midCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getEmptySpeed();

                            coldSandBoxRefluxTime = input.getTotalTime() + executionTime;
                            coldSandBoxStateChange = true;
                            carEventEnum = ColdSandMoldingEnum.EMPTY_SUB_CAR_GO_COLD_MID_POSITION_WAIT;
                            eventTime = executionTime;

                        }
                    }

                    //子车满载
                }else {
                    //判断冷砂回流放置点位上是否空载
                    if (input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getStatus().equals(PositionStatusEnum.UNOCCUPIED)){
                        //发生前往放置点位卸载砂箱的事件
                        double executionTime = (Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getLocationCoordinate().getX() -
                                input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate().getX())
                                / input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getFullSpeed()) +
                                input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getTopRodRaiseOrFallTime();

                        coldSandBoxRefluxTime = input.getTotalTime() + executionTime;

                        coldSandBoxStateChange = true;
                        carEventEnum = ColdSandMoldingEnum.FULL_SUB_CAR_GO_COLD_SAND_REFLUX_POSITION_LAND;
                        eventTime = executionTime;

                    }else {
                        //判断车辆是否到达中间点位
                        if (!input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getLocationCoordinate().equals(midCoordinate)){
                            //发生前往中间点位的事件
                            double executionTime = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getLocationCoordinate().getX() -
                                    midCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getColdRefluxSubcarIndex()).getFullSpeed();

                            coldSandBoxRefluxTime = input.getTotalTime() + executionTime;
                            coldSandBoxStateChange = true;
                            carEventEnum = ColdSandMoldingEnum.FULL_SUB_CAR_GO_COLD_SAND_WAIT_REFLUX_POSITION;
                            eventTime = executionTime;

                        }
                    }
                }
            }else {
                switch (carEventEnum){
                    case EMPTY_SUB_CAR_GO_COLD_SAND_REFLUX_LAY_POSITION_LOAD:
                        //前往开模点装载的事件
                        simulationOutput = eventFlow.EmptyColdSandBoxGoPositionEventCreate(simulationOutput);

                        coldSandBoxStateChange = false;
                        carEventEnum = null;
                        break;
                    case EMPTY_SUB_CAR_GO_COLD_MID_POSITION_WAIT:
                        //前往回流点前的等待位置处
                        simulationOutput = eventFlow.EmptyColdSandBoxGoWaitPositionEventCreate(simulationOutput, midCoordinate);

                        coldSandBoxStateChange = false;
                        carEventEnum = null;
                        break;
                    case FULL_SUB_CAR_GO_COLD_SAND_REFLUX_POSITION_LAND:
                        //前往回流点前的等待位置处
                        simulationOutput = eventFlow.FullColdSandBoxGoPositionEventCreate(simulationOutput);

                        coldSandBoxStateChange = false;
                        carEventEnum = null;
                        break;
                    case FULL_SUB_CAR_GO_COLD_SAND_WAIT_REFLUX_POSITION:
                        //前往回流点前的等待位置处
                        simulationOutput = eventFlow.FullColdSandBoxGoWaitPositionEventCreate(simulationOutput, midCoordinate);

                        coldSandBoxStateChange = false;
                        carEventEnum = null;
                        break;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[3] = eventTime;
            eventTime = 0.0;
        }

        return simulationOutput;
    }
}
