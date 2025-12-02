package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound;

import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.OutBoundSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;

import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.OCCUPIED;
import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.UNOCCUPIED;
import static cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum.PATH_OCCUPIED;
import static cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum.PATH_UNOCCUPIED;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/6/21 21:41
 */
public class MoldRefulxSimulation {
    /**
     * 模具回流轨道车辆时间
     */
    private double moldRefluxTime = 0.0;
    /**
     * 出库区仿真的工具类
     */
    private OutBoundSimulationTools tools = new OutBoundSimulationTools();
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean stateChange = false;
    /**
     * 轨道的回流等待点位坐标坐标
     */
    private Coordinate refluxWaitCoordinate;
    /**
     * 轨道的放置等待点位坐标
     */
    private Coordinate layWaitCoordinate;
    /**
     * 创建事件的类
     */
    private MoldRefluxEventFlow moldRefluxEventFlow = new MoldRefluxEventFlow();

    private double eventTime = 0.0;

    public OutBoundGanttChartLinkInput MoldRefulxSimulationRun(OutBoundSimulationInput input, GanttChart outBoundGanttChart, Double[] timeList){
        layWaitCoordinate = new Coordinate(input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getCoordinate().getX() -
                Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldRefluxPositionIndex()).getCoordinate().getX()),
                input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getCoordinate().getY());
        refluxWaitCoordinate = new Coordinate(input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate().getX() +
                Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate().getX() -
                        input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldRefluxPositionIndex()).getCoordinate().getX()),
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate().getY());
        OutBoundGanttChartLinkInput outBoundGanttChartLinkInput = new OutBoundGanttChartLinkInput();
        outBoundGanttChartLinkInput.setInput(input);
        outBoundGanttChartLinkInput.setGanttChart(outBoundGanttChart);
        if (input.getTotalTime() >= moldRefluxTime){
            //判断模具回流轨道车辆是否装载砂箱
            if (tools.judgeSubCarIsEmpty(input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()))){
                //判断车辆是否到达等待点
                if (input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getLocationCoordinate().equals(refluxWaitCoordinate)){
                    if (input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getStatus().equals(OCCUPIED)){
                        if (stateChange){
                            outBoundGanttChartLinkInput = moldRefluxEventFlow.EmptyLoadHotMoldEventCreate(outBoundGanttChartLinkInput.getInput(), outBoundGanttChartLinkInput.getGanttChart(), refluxWaitCoordinate);

//                            log.info("前往热砂模具搬运回流的模具！");
                            stateChange = false;
                        }else {
                            double executionTime = (Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate().getX() -
                                    refluxWaitCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getEmptySpeed()) +
                                    input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getTopRodRaiseOrFallTime() +
                                    (Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldRefluxPositionIndex()).getCoordinate().getX() -
                                    refluxWaitCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getFullSpeed());

                            moldRefluxTime = input.getTotalTime() + executionTime;
                            stateChange = true;

                            eventTime = executionTime;

//                            log.info("前往热砂模具搬运回流的模具？");
                        }
                    }else if (input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldRefluxPositionIndex()).getStatus().equals(OCCUPIED)){
                        if (stateChange){
                            outBoundGanttChartLinkInput = moldRefluxEventFlow.EmptyLoadColdMoldEventCreate(outBoundGanttChartLinkInput.getInput(), outBoundGanttChartLinkInput.getGanttChart(), refluxWaitCoordinate);

                            stateChange = false;
                            //恢复占用的轨道
                            input.getPaths().get(input.getOutBoundIndexInput().getMoldRefluxPathIndex()).setStatus(PATH_UNOCCUPIED);

//                            log.info("前往冷砂模具搬运回流的模具！");
                        }else {
                            double executionTime = (Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldRefluxPositionIndex()).getCoordinate().getX() -
                                    refluxWaitCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getEmptySpeed()) +
                                    input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getTopRodRaiseOrFallTime();

                            moldRefluxTime = input.getTotalTime() + executionTime;
                            stateChange = true;
                            //占用轨道，防止其他车辆进入
                            input.getPaths().get(input.getOutBoundIndexInput().getMoldRefluxPathIndex()).setStatus(PATH_OCCUPIED);

                            eventTime = executionTime;

//                            log.info("前往冷砂模具搬运回流的模具？");
                        }
                    }

//                    log.info("?" + moldRefluxTime + "!!" + input.getTotalTime());
                }else {
                    if (stateChange){
                        outBoundGanttChartLinkInput = moldRefluxEventFlow.EmptyGoRefluxWaitPositionEventCreate(outBoundGanttChartLinkInput.getInput(), outBoundGanttChartLinkInput.getGanttChart(), refluxWaitCoordinate);

                        stateChange = false;

//                        log.info("前往模具搬运回流的等待点！");
                    }else {
                        double executionTime = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getLocationCoordinate().getX() -
                                refluxWaitCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getEmptySpeed();
                        moldRefluxTime = input.getTotalTime() + executionTime;
                        stateChange = true;

                        eventTime = executionTime;
//                        log.info("前往模具搬运回流的等待点？");
                    }
                }

                //如果满载
            }else {
                //判断模具回流轨道车辆是否到达放置等待点位
                if (input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getLocationCoordinate().equals(layWaitCoordinate)){
                    if (input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getStatus().equals(UNOCCUPIED)){
                        if (stateChange){
                            outBoundGanttChartLinkInput = moldRefluxEventFlow.FullLoadMoldEventCreate(outBoundGanttChartLinkInput.getInput(), outBoundGanttChartLinkInput.getGanttChart(), layWaitCoordinate);

                            stateChange = false;

//                            log.info("前往模具回流点卸载模具！");
                        }else {
                            double executionTime = (Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getCoordinate().getX() -
                                    layWaitCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getFullSpeed()) +
                                    input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getTopRodRaiseOrFallTime();

                            moldRefluxTime = input.getTotalTime() + executionTime;
                            stateChange = true;

                            eventTime = executionTime;

//                            log.info("前往模具回流点卸载模具？");
                        }
                    }
                }else {
                    if (stateChange){
                        outBoundGanttChartLinkInput = moldRefluxEventFlow.FullGoRefluxWaitPositionEventCreate(outBoundGanttChartLinkInput.getInput(), outBoundGanttChartLinkInput.getGanttChart(), layWaitCoordinate);

                        stateChange = false;

//                        log.info("前往模具回流等待卸载点！");
                    }else {
                        double executionTime = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getLocationCoordinate().getX() -
                                layWaitCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getMoldRefluxSubcarIndex()).getFullSpeed();
                        moldRefluxTime = input.getTotalTime() + executionTime;
                        stateChange = true;

                        eventTime = executionTime;
//                        log.info("前往模具回流等待卸载点？");
                    }
                }
            }
        }

        if (eventTime != 0.0){
            timeList[4] = eventTime;
            eventTime = 0.0;
        }

        return outBoundGanttChartLinkInput;
    }
}
