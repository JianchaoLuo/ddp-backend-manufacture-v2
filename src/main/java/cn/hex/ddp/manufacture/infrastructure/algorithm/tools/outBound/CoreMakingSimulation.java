package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound;

import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.OutBoundSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.OCCUPIED;
import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.UNOCCUPIED;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/6/21 21:37
 */
@Slf4j
public class CoreMakingSimulation {
    /**
     * 制芯轨道车辆的时间
     */
    private double coreMakeTime = 0.0;
    /**
     * 出库区仿真的工具类
     */
    private final OutBoundSimulationTools tools = new OutBoundSimulationTools();
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean stateChange = false;
    /**
     * 创建事件的类
     */
    private final CoreMakingEventFlow coreMakingEventFlow = new CoreMakingEventFlow();

    private double eventTime = 0.0;

    public OutBoundGanttChartLinkInput CoreMakingSimulationRun(@NotNull OutBoundSimulationInput input, GanttChart outBoundGanttChart, Double[] timeList){
        // 轨道的中点坐标
        Coordinate midCoordinate = new Coordinate((input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getCoordinate().getX() +
                input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingLayPositionIndex()).getCoordinate().getX()) / 2,
                input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getCoordinate().getY());

        OutBoundGanttChartLinkInput outBoundGanttChartLinkInput = new OutBoundGanttChartLinkInput();
        outBoundGanttChartLinkInput.setInput(input);
        outBoundGanttChartLinkInput.setGanttChart(outBoundGanttChart);
        if (input.getTotalTime() >= coreMakeTime){
            //判断制芯轨道车辆是否空载
            if (tools.judgeSubCarIsEmpty(input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()))){
                //判断出库轨道交互点处是否有产品
                if (input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getStatus().equals(OCCUPIED)){
                    //判断车辆是否到达交互点
                    if (input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getLocationCoordinate().equals(
                            input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getCoordinate())){
                        //发生装载事件
                        if (stateChange){
                            outBoundGanttChartLinkInput = coreMakingEventFlow.EmptyLoadEventCreate(outBoundGanttChartLinkInput.getInput(), outBoundGanttChartLinkInput.getGanttChart());

                            stateChange = false;
                        }else {
                            double executionTime = input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getTopRodRaiseOrFallTime();

                            coreMakeTime = input.getTotalTime() + executionTime;
                            stateChange = true;

                            eventTime = executionTime;
                        }
                        //发生前往交互点的事件
                    }else {
                        if (stateChange){
                            outBoundGanttChartLinkInput = coreMakingEventFlow.EmptyGoPositionEventCreate(outBoundGanttChartLinkInput.getInput(), outBoundGanttChartLinkInput.getGanttChart());

                            stateChange = false;
                        }else {
                            double executionTime = Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getCoordinate().getX() -
                                    input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getLocationCoordinate().getX()) /
                                    input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getEmptySpeed();

                            coreMakeTime = input.getTotalTime() + executionTime;
                            stateChange = true;

                            eventTime = executionTime;
                        }
                    }
                }else {
                    //判断车辆是否到达中点位置处
                    if (!input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getLocationCoordinate().equals(midCoordinate)){
                        if (stateChange){
                            outBoundGanttChartLinkInput = coreMakingEventFlow.EmptyGoMidPositionEventCreate(outBoundGanttChartLinkInput.getInput(), outBoundGanttChartLinkInput.getGanttChart(), midCoordinate);

                            stateChange = false;
                        }else {
                            double executionTime = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getLocationCoordinate().getX() -
                                    midCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getEmptySpeed();

                            coreMakeTime = input.getTotalTime() + executionTime;
                            stateChange = true;

                            eventTime = executionTime;
                        }
                    }
                }

                //如果装载
            }else {
                //判断车辆是否到达中点位置
                if ((!input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getLocationCoordinate().equals(midCoordinate)) &&
                        !(input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getLocationCoordinate().equals
                                (input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingLayPositionIndex()).getCoordinate()))){
                    if (stateChange){
                        outBoundGanttChartLinkInput = coreMakingEventFlow.FullGoMidPositionEventCreate(outBoundGanttChartLinkInput.getInput(), outBoundGanttChartLinkInput.getGanttChart(), midCoordinate);

                        stateChange = false;
                    }else {
                        double executionTime = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getLocationCoordinate().getX() -
                                midCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getFullSpeed();

                        coreMakeTime = input.getTotalTime() + executionTime;
                        stateChange = true;

                        eventTime = executionTime;
                    }
                    //车辆到达中点位置
                }else {
                    //如果点位未被占用
                    if (input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingLayPositionIndex()).getStatus().equals(UNOCCUPIED)){
                        //判断车辆是否到放置达交互点
                        if (input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getLocationCoordinate().equals(
                                input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingLayPositionIndex()).getCoordinate())){
                            if (stateChange){
                                outBoundGanttChartLinkInput = coreMakingEventFlow.FullLoadEventCreate(outBoundGanttChartLinkInput.getInput(), outBoundGanttChartLinkInput.getGanttChart());

                                stateChange = false;
                            }else {
                                double executionTime = input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getTopRodRaiseOrFallTime();

                                coreMakeTime = input.getTotalTime() + executionTime;
                                stateChange = true;

                                eventTime = executionTime;
                            }

                            log.info("Mold Lay!");
                        }else {
                            if (stateChange){
                                outBoundGanttChartLinkInput = coreMakingEventFlow.FullGoLayPositionEventCreate(outBoundGanttChartLinkInput.getInput(), outBoundGanttChartLinkInput.getGanttChart());

                                stateChange = false;
                            }else {
                                double executionTime = Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingLayPositionIndex()).getCoordinate().getX() -
                                        input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getLocationCoordinate().getX()) /
                                        input.getSubCars().get(input.getOutBoundIndexInput().getCoreMakingSubcarIndex()).getFullSpeed();

                                coreMakeTime = input.getTotalTime() + executionTime;
                                stateChange = true;

                                eventTime = executionTime;
                            }
                        }
                    }
                }
            }
        }

        if (eventTime != 0.0){
            timeList[1] = eventTime;
            eventTime = 0.0;
        }

        return outBoundGanttChartLinkInput;
    }
}
