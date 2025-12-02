package cn.hex.ddp.manufacture.infrastructure.algorithm.simulation;

import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.MoldClosingSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.test.OutBoundANDMoldClosing;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationException;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.moldClosing.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound.OutBoundGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import lombok.extern.slf4j.Slf4j;

import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.OCCUPIED;
import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.UNOCCUPIED;
import static cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum.WORKING;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/6/23 14:43
 */
@Slf4j
public class MoldClosingSimulation {
    /**
     * 热砂合模时间
     */
    private double hotMoldTime = 0.0;
    /**
     * 冷砂合模时间
     */
    private double coldMoldTime = 0.0;
    /**
     * 热砂合模工岗1时间
     */
    private double hotWorkStationTime_1 = 0.0;
    /**
     * 热砂合模工岗2时间
     */
    private double hotWorkStationTime_2 = 0.0;
    /**
     * 热砂合模工岗3时间
     */
    private double hotWorkStationTime_3 = 0.0;
    /**
     * 热砂合模工岗4时间
     */
    private double hotWorkStationTime_4 = 0.0;
    /**
     * 冷砂合模工岗1时间
     */
    private double coldWorkStationTime_1 = 0.0;
    /**
     * 冷砂合模工岗2时间
     */
    private double coldWorkStationTime_2 = 0.0;
    /**
     * 冷砂合模工岗3时间
     */
    private double coldWorkStationTime_3 = 0.0;
    /**
     * 冷砂合模工岗4时间
     */
    private double coldWorkStationTime_4 = 0.0;
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
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean hotWorkStationStateChange_1 = false;
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean hotWorkStationStateChange_2 = false;
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean hotWorkStationStateChange_3 = false;
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean hotWorkStationStateChange_4 = false;
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean coldWorkStationStateChange_1 = false;
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean coldWorkStationStateChange_2 = false;
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean coldWorkStationStateChange_3 = false;
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean coldWorkStationStateChange_4 = false;
    /**
     * 合模区仿真
     */
    private final MoldClosingSimulationTools tools = new MoldClosingSimulationTools();
    /**
     * 创建事件的类
     */
    private final HotMoldClosingEventFlow hotMoldClosingEventFlow = new HotMoldClosingEventFlow();
    /**
     * 创建事件的类
     */
    private final ColdMoldClosingEventFlow coldMoldClosingEventFlow = new ColdMoldClosingEventFlow();
    /**
     * 创建事件的类
     */
    private final WorkStationEventFlow workStationEventFlow = new WorkStationEventFlow();

    private double eventTime = 0.0;

    public OutBoundANDMoldClosing MoldClosingSimulationRun(MoldClosingSimulationInput input, OutBoundGanttChartLinkInput outBoundInput,
                                                           GanttChart moldClosingGanttChart, int finishedCount, Double[] timeList) throws SimulationException {
        MoldClosingGanttChartLinkInput moldClosingGanttChartLinkInput = new MoldClosingGanttChartLinkInput();
        moldClosingGanttChartLinkInput.setInput(input);
        moldClosingGanttChartLinkInput.setGanttChart(moldClosingGanttChart);

        if (input.getTotalTime() >= hotMoldTime){
            //判断子车是否装载
            if (tools.judgeSubCarIsEmpty(input.getHotMoldClosingSubcar())){
                //判断母车是否到达热砂点
                //进行装载事件流
                if (input.getHotMoldClosingFerry().getLocationCoordinate().getX() == input.getHotMoldClosingPosition().getCoordinate().getX() &&
                        input.getHotMoldClosingPosition().getStatus().equals(OCCUPIED)){
                    //发生装载事件
                    if (hotStateChange){

                        OutBoundANDMoldClosing outBoundANDMoldClosing = hotMoldClosingEventFlow.HotMoleEmptyLoadEventCreate(moldClosingGanttChartLinkInput.getInput(),
                                outBoundInput, moldClosingGanttChartLinkInput.getGanttChart(), finishedCount);
                        moldClosingGanttChartLinkInput = outBoundANDMoldClosing.getMoldClosingGanttChartLinkInput();
                        outBoundInput = outBoundANDMoldClosing.getOutBoundGanttChartLinkInput();

                        hotStateChange = false;

                    }else {
                        double executionTime = (Math.abs(input.getHotMoldClosingPosition().getCoordinate().getY() -
                                input.getHotMoldClosingFerry().getLocationCoordinate().getY()) / input.getHotMoldClosingSubcar().getEmptySpeed()) +
                                (input.getHotMoldClosingSubcar().getTopRodRaiseOrFallTime()) + ((Math.abs(input.getHotMoldClosingPosition().getCoordinate().getY() -
                                input.getHotMoldClosingFerry().getLocationCoordinate().getY()) / input.getHotMoldClosingSubcar().getFullSpeed()));
                        hotMoldTime = input.getTotalTime() + executionTime;
                        hotStateChange = true;

                        eventTime = executionTime;

                    }

                    log.info("热砂合模！！！" + hotMoldTime + " 产品类型：" + input.getHotMoldClosingPosition().getProduct());

                    //判断母车是否到达工岗
                    //进行装载事件流
                }else if (tools.judgeHotFerryInWorkstation(input.getHotMoldClosingFerry(), input.getWorkStations(), input.getMoldClosingIndexInput()) != 0){
                    int index = tools.judgeHotFerryInWorkstation(input.getHotMoldClosingFerry(), input.getWorkStations(), input.getMoldClosingIndexInput());
                    //发生装载事件
                    if (hotStateChange){
                        Integer workStationIndex;
                        if (index == 1){
                            workStationIndex = input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_1();
                        }else if (index == 2){
                            workStationIndex = input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_2();
                        }else if (index == 3){
                            workStationIndex = input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_3();
                        }else {
                            workStationIndex = input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_4();
                        }
                        moldClosingGanttChartLinkInput = hotMoldClosingEventFlow.HotMoleEmptyLoadInWorkStationEventCreate(moldClosingGanttChartLinkInput.getInput(), moldClosingGanttChartLinkInput.getGanttChart(), workStationIndex);

                        hotStateChange = false;

                    }else {
                        double executionTime = (Math.abs(input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_1()).getWorkPositionCoordinate().getY() -
                                input.getHotMoldClosingFerry().getLocationCoordinate().getY()) / input.getHotMoldClosingSubcar().getEmptySpeed()) +
                                (input.getHotMoldClosingSubcar().getTopRodRaiseOrFallTime()) + ((Math.abs(input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_1()).getWorkPositionCoordinate().getY() -
                                input.getHotMoldClosingFerry().getLocationCoordinate().getY()) / input.getHotMoldClosingSubcar().getFullSpeed()));
                        hotMoldTime = input.getTotalTime() + executionTime;
                        hotStateChange = true;

                        eventTime = executionTime;
                    }

                    //判断工岗是否处于待搬运状态
                    //如果有，前往装载
                }else {
                    Integer workStationIndex = tools.judgeHotWorkStationIsWaitingTransprot(input.getWorkStations(), input.getMoldClosingIndexInput());
                    if (workStationIndex != -1){
                        //发生前往工岗装载事件
                        if (hotStateChange){
                            moldClosingGanttChartLinkInput = hotMoldClosingEventFlow.HotMoleEmptyGoWorkStationEventCreate(moldClosingGanttChartLinkInput.getInput(), moldClosingGanttChartLinkInput.getGanttChart(), workStationIndex);

                            hotStateChange = false;

                        }else {
                            double executionTime = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                                    input.getHotMoldClosingFerry().getLocationCoordinate().getX()) / input.getHotMoldClosingFerry().getEmptySpeed();
                            hotMoldTime = input.getTotalTime() + executionTime;
                            hotStateChange = true;

                            eventTime = executionTime;
                        }
                    }else {
                        //判断4个工岗是否全部被占用，正等待工作完成，避免车辆在搬运热砂合模点位的砂箱后整个开箱区堵塞（因为工岗被占用）
                        if (!tools.judgeHotMoldClosingWorkStationAllInWorking(input.getWorkStations(), input.getMoldClosingIndexInput())){
                            //判断点位是否有待搬运的状态
                            //如果有，前往装载
                            if (input.getHotMoldClosingPosition().getStatus().equals(OCCUPIED)){
                                //发生前往点位装载事件
                                if (hotStateChange){
                                    moldClosingGanttChartLinkInput = hotMoldClosingEventFlow.HotMoleEmptyGoHotPosotionEventCreate(moldClosingGanttChartLinkInput.getInput(), moldClosingGanttChartLinkInput.getGanttChart());

                                    hotStateChange = false;
                                }else {
                                    double executionTime = Math.abs(input.getHotMoldClosingPosition().getCoordinate().getX() -
                                            input.getHotMoldClosingFerry().getLocationCoordinate().getX()) / input.getHotMoldClosingFerry().getEmptySpeed();
                                    hotMoldTime = input.getTotalTime() + executionTime;
                                    hotStateChange = true;

                                    eventTime = executionTime;
                                }
                            }
                        }
                    }
                }

                //子车满载
            }else {
                switch (tools.judgeProductType(input.getHotMoldClosingSubcar().getProduct())){
                    case 1:
                        //判断装载的产品，如果是上砂箱或下砂箱
                        //前往最近的空的工岗
                        Integer workStationIndex_1 = tools.judgeWhichHotWorkStationFree(input.getWorkStations(), input.getMoldClosingIndexInput());
                        if (workStationIndex_1 != -1){
                            //发生前往工岗的卸载载事件
                            if (hotStateChange){
                                moldClosingGanttChartLinkInput = hotMoldClosingEventFlow.HotMoleFullGoWorkStationEventCreate(moldClosingGanttChartLinkInput.getInput(), moldClosingGanttChartLinkInput.getGanttChart(), workStationIndex_1);

                                hotStateChange = false;

                            }else {
                                double executionTime = (Math.abs(input.getWorkStations().get(workStationIndex_1).getWorkPositionCoordinate().getX() -
                                        input.getHotMoldClosingFerry().getLocationCoordinate().getX()) / input.getHotMoldClosingFerry().getFullSpeed()) +
                                        (Math.abs(input.getWorkStations().get(workStationIndex_1).getWorkPositionCoordinate().getY() -
                                                input.getHotMoldClosingFerry().getLocationCoordinate().getY()) / input.getHotMoldClosingSubcar().getFullSpeed()) +
                                        (input.getHotMoldClosingSubcar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getWorkStations().get(workStationIndex_1).getWorkPositionCoordinate().getY() -
                                        input.getHotMoldClosingFerry().getLocationCoordinate().getY()) / input.getHotMoldClosingSubcar().getEmptySpeed());
                                hotMoldTime = input.getTotalTime() + executionTime;
                                hotStateChange = true;

                                eventTime = executionTime;
                            }
                        }
                        break;
                    case 2:
                        //判断装载的产品，如果是模具
                        //前往待匹配的工岗
                        Integer workStationIndex_2 = tools.findPairHotWorkStation(input.getWorkStations(), input.getMoldClosingIndexInput());
                        if (workStationIndex_2 != -1){
                            //发生前往工岗的卸载载事件
                            if (hotStateChange){
                                moldClosingGanttChartLinkInput = hotMoldClosingEventFlow.HotMoleFullGoPairWorkStationEventCreate(moldClosingGanttChartLinkInput.getInput(), moldClosingGanttChartLinkInput.getGanttChart(), workStationIndex_2);

                                hotStateChange = false;

                            }else {
                                double executionTime = (Math.abs(input.getWorkStations().get(workStationIndex_2).getWorkPositionCoordinate().getX() -
                                        input.getHotMoldClosingFerry().getLocationCoordinate().getX()) / input.getHotMoldClosingFerry().getFullSpeed()) +
                                        (Math.abs(input.getWorkStations().get(workStationIndex_2).getWorkPositionCoordinate().getY() -
                                                input.getHotMoldClosingFerry().getLocationCoordinate().getY()) / input.getHotMoldClosingSubcar().getFullSpeed()) +
                                        (input.getHotMoldClosingSubcar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getWorkStations().get(workStationIndex_2).getWorkPositionCoordinate().getY() -
                                        input.getHotMoldClosingFerry().getLocationCoordinate().getY()) / input.getHotMoldClosingSubcar().getEmptySpeed());
                                hotMoldTime = input.getTotalTime() + executionTime;
                                hotStateChange = true;

                                eventTime = executionTime;
                            }
                        }
                        break;
                    case 3:
                        //判断装载的产品，如果是合模完成的砂箱
                        //前往待放置的点位
                        if (input.getHotMoldClosingFerry().getLocationCoordinate().getX() == input.getHotMoldClosingLayPosition().getCoordinate().getX()){
                            if (input.getHotMoldClosingLayPosition().getStatus().equals(UNOCCUPIED)){
                                //发生前往放置热砂点位的卸载事件
                                if (hotStateChange){
                                moldClosingGanttChartLinkInput = hotMoldClosingEventFlow.HotMoleFullUnLoadLayPositionEventCreate(moldClosingGanttChartLinkInput.getInput(), moldClosingGanttChartLinkInput.getGanttChart());

                                    hotStateChange = false;

                                }else {
                                    double executionTime = (Math.abs(input.getHotMoldClosingLayPosition().getCoordinate().getY() -
                                                input.getHotMoldClosingFerry().getLocationCoordinate().getY()) / input.getHotMoldClosingSubcar().getFullSpeed()) +
                                        (input.getHotMoldClosingSubcar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getHotMoldClosingLayPosition().getCoordinate().getY() -
                                        input.getHotMoldClosingFerry().getLocationCoordinate().getY()) / input.getHotMoldClosingSubcar().getEmptySpeed());
                                    hotMoldTime = input.getTotalTime() + executionTime;
                                    hotStateChange = true;

                                    eventTime = executionTime;
                                }
                            }
                        }else {
                            //发生前往放置热砂点位的事件
                            if (hotStateChange){
                                moldClosingGanttChartLinkInput = hotMoldClosingEventFlow.HotMoleFullGoLayPositionEventCreate(moldClosingGanttChartLinkInput.getInput(), moldClosingGanttChartLinkInput.getGanttChart());

                                hotStateChange = false;

                            }else {
                                double executionTime = Math.abs(input.getHotMoldClosingLayPosition().getCoordinate().getX() -
                                        input.getHotMoldClosingFerry().getLocationCoordinate().getX()) / input.getHotMoldClosingFerry().getFullSpeed();
                                hotMoldTime = input.getTotalTime() + executionTime;
                                hotStateChange = true;

                                eventTime = executionTime;
                            }
                        }
                        break;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[5] = eventTime;
            eventTime = 0.0;
        }

        if (input.getTotalTime() >= coldMoldTime){
            //判断子车是否装载
            if (tools.judgeSubCarIsEmpty(input.getColdMoldClosingSubcar())){
                //判断母车是否到达热砂点
                //进行装载事件流
                if (input.getColdMoldClosingFerry().getLocationCoordinate().getX() == input.getColdMoldClosingPosition().getCoordinate().getX() &&
                        input.getColdMoldClosingPosition().getStatus().equals(OCCUPIED)){
                    //发生装载事件
                    if (coldStateChange){
                        OutBoundANDMoldClosing outBoundANDMoldClosing = coldMoldClosingEventFlow.ColdMoleEmptyLoadEventCreate(moldClosingGanttChartLinkInput.getInput(),
                                outBoundInput, moldClosingGanttChartLinkInput.getGanttChart(), finishedCount);

                        moldClosingGanttChartLinkInput = outBoundANDMoldClosing.getMoldClosingGanttChartLinkInput();
                        outBoundInput = outBoundANDMoldClosing.getOutBoundGanttChartLinkInput();

                        coldStateChange = false;

                    }else {
                        double executionTime = (Math.abs(input.getColdMoldClosingPosition().getCoordinate().getY() -
                                input.getColdMoldClosingFerry().getLocationCoordinate().getY()) / input.getColdMoldClosingSubcar().getEmptySpeed()) +
                                (input.getColdMoldClosingSubcar().getTopRodRaiseOrFallTime()) + ((Math.abs(input.getColdMoldClosingPosition().getCoordinate().getY() -
                                input.getColdMoldClosingFerry().getLocationCoordinate().getY()) / input.getColdMoldClosingSubcar().getFullSpeed()));
                        coldMoldTime = input.getTotalTime() + executionTime;

                        coldStateChange = true;

                        eventTime = executionTime;
                    }

                    log.info("冷砂合模！！！" + hotMoldTime + " 产品类型：" + input.getColdMoldClosingPosition().getProduct());
                    //判断母车是否到达工岗
                    //进行装载事件流
                }else if (tools.judgeColdFerryInWorkstation(input.getColdMoldClosingFerry(), input.getWorkStations(), input.getMoldClosingIndexInput()) != 0){
                    int index = tools.judgeColdFerryInWorkstation(input.getColdMoldClosingFerry(), input.getWorkStations(), input.getMoldClosingIndexInput());
                    //发生装载事件
                    if (coldStateChange){
                        Integer workStationIndex;
                        if (index == 1){
                            workStationIndex = input.getMoldClosingIndexInput().getColdMoldClosingWorkStationIndex_1();
                        }else if (index == 2){
                            workStationIndex = input.getMoldClosingIndexInput().getColdMoldClosingWorkStationIndex_2();
                        }else if (index == 3){
                            workStationIndex = input.getMoldClosingIndexInput().getColdMoldClosingWorkStationIndex_3();
                        }else {
                            workStationIndex = input.getMoldClosingIndexInput().getColdMoldClosingWorkStationIndex_4();
                        }
                        moldClosingGanttChartLinkInput = coldMoldClosingEventFlow.ColdMoleEmptyLoadInWorkStationEventCreate(moldClosingGanttChartLinkInput.getInput(), moldClosingGanttChartLinkInput.getGanttChart(), workStationIndex);

                        coldStateChange = false;
                    }else {
                        double executionTime = (Math.abs(input.getWorkStations().get(input.getMoldClosingIndexInput().getColdMoldClosingWorkStationIndex_1()).getWorkPositionCoordinate().getY() -
                                input.getColdMoldClosingFerry().getLocationCoordinate().getY()) / input.getColdMoldClosingSubcar().getEmptySpeed()) +
                                (input.getColdMoldClosingSubcar().getTopRodRaiseOrFallTime()) + ((Math.abs(input.getWorkStations().get(input.getMoldClosingIndexInput().getColdMoldClosingWorkStationIndex_1()).getWorkPositionCoordinate().getY() -
                                input.getColdMoldClosingFerry().getLocationCoordinate().getY()) / input.getColdMoldClosingSubcar().getFullSpeed()));
                        coldMoldTime = input.getTotalTime() + executionTime;
                        coldStateChange = true;

                        eventTime = executionTime;
                    }

                    //判断工岗是否处于待搬运状态
                    //如果有，前往装载
                }else {
                    Integer workStationIndex = tools.judgeColdWorkStationIsWaitingTransprot(input.getWorkStations(), input.getMoldClosingIndexInput());
                    if (workStationIndex != -1){
                        //发生前往工岗装载事件
                        if (coldStateChange){
                            moldClosingGanttChartLinkInput = coldMoldClosingEventFlow.ColdMoleEmptyGoWorkStationEventCreate(moldClosingGanttChartLinkInput.getInput(), moldClosingGanttChartLinkInput.getGanttChart(), workStationIndex);

                            coldStateChange = false;
                        }else {
                            double executionTime = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                                    input.getColdMoldClosingFerry().getLocationCoordinate().getX()) / input.getColdMoldClosingFerry().getEmptySpeed();
                            coldMoldTime = input.getTotalTime() + executionTime;
                            coldStateChange = true;

                            eventTime = executionTime;
                        }
                    }else {
                        //判断4个工岗是否全部被占用，正等待工作完成，避免车辆在搬运冷砂合模点位的砂箱后整个开箱区堵塞（因为工岗被占用）
                        if (!tools.judgeColdMoldClosingWorkStationAllInWorking(input.getWorkStations(), input.getMoldClosingIndexInput())){
                            //判断点位是否有待搬运的状态
                            //如果有，前往装载
                            if (input.getColdMoldClosingPosition().getStatus().equals(OCCUPIED)){
                                //发生前往点位装载事件
                                if (coldStateChange){
                                    moldClosingGanttChartLinkInput = coldMoldClosingEventFlow.ColdMoleEmptyGoColdPosotionEventCreate(moldClosingGanttChartLinkInput.getInput(), moldClosingGanttChartLinkInput.getGanttChart());

                                    coldStateChange = false;
                                }else {
                                    double executionTime = Math.abs(input.getColdMoldClosingPosition().getCoordinate().getX() -
                                            input.getColdMoldClosingFerry().getLocationCoordinate().getX()) / input.getColdMoldClosingFerry().getEmptySpeed();
                                    coldMoldTime = input.getTotalTime() + executionTime;
                                    coldStateChange = true;

                                    eventTime = executionTime;
                                }
                            }
                        }
                    }
                }

                //子车满载
            }else {
                switch (tools.judgeProductType(input.getColdMoldClosingSubcar().getProduct())){
                    case 1:
                        //判断装载的产品，如果是上砂箱或下砂箱
                        //前往最近的空的工岗
                        Integer workStationIndex_1 = tools.judgeWhichColdWorkStationFree(input.getWorkStations(), input.getMoldClosingIndexInput());
                        if (workStationIndex_1 != -1){
                            //发生前往工岗的卸载载事件
                            if (coldStateChange){
                                moldClosingGanttChartLinkInput = coldMoldClosingEventFlow.ColdMoleFullGoWorkStationEventCreate(moldClosingGanttChartLinkInput.getInput(), moldClosingGanttChartLinkInput.getGanttChart(), workStationIndex_1);

                                coldStateChange = false;
                            }else {
                                double executionTime = (Math.abs(input.getWorkStations().get(workStationIndex_1).getWorkPositionCoordinate().getX() -
                                        input.getColdMoldClosingFerry().getLocationCoordinate().getX()) / input.getColdMoldClosingFerry().getFullSpeed()) +
                                        (Math.abs(input.getWorkStations().get(workStationIndex_1).getWorkPositionCoordinate().getY() -
                                                input.getColdMoldClosingFerry().getLocationCoordinate().getY()) / input.getColdMoldClosingSubcar().getFullSpeed()) +
                                        (input.getColdMoldClosingSubcar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getWorkStations().get(workStationIndex_1).getWorkPositionCoordinate().getY() -
                                        input.getColdMoldClosingFerry().getLocationCoordinate().getY()) / input.getColdMoldClosingSubcar().getEmptySpeed());
                                coldMoldTime = input.getTotalTime() + executionTime;
                                coldStateChange = true;

                                eventTime = executionTime;
                            }
                        }
                        break;
                    case 2:
                        //判断装载的产品，如果是模具
                        //前往待匹配的工岗
                        Integer workStationIndex_2 = tools.findPairColdWorkStation(input.getWorkStations(), input.getMoldClosingIndexInput());
                        if (workStationIndex_2 != -1){
                            //发生前往工岗的卸载载事件
                            if (coldStateChange){
                                moldClosingGanttChartLinkInput = coldMoldClosingEventFlow.ColdMoleFullGoPairWorkStationEventCreate(moldClosingGanttChartLinkInput.getInput(), moldClosingGanttChartLinkInput.getGanttChart(), workStationIndex_2);

                                coldStateChange = false;
                            }else {
                                double executionTime = (Math.abs(input.getWorkStations().get(workStationIndex_2).getWorkPositionCoordinate().getX() -
                                        input.getColdMoldClosingFerry().getLocationCoordinate().getX()) / input.getColdMoldClosingFerry().getFullSpeed()) +
                                        (Math.abs(input.getWorkStations().get(workStationIndex_2).getWorkPositionCoordinate().getY() -
                                                input.getColdMoldClosingFerry().getLocationCoordinate().getY()) / input.getColdMoldClosingSubcar().getFullSpeed()) +
                                        (input.getColdMoldClosingSubcar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getWorkStations().get(workStationIndex_2).getWorkPositionCoordinate().getY() -
                                        input.getColdMoldClosingFerry().getLocationCoordinate().getY()) / input.getColdMoldClosingSubcar().getEmptySpeed());
                                coldMoldTime = input.getTotalTime() + executionTime;
                                coldStateChange = true;

                                eventTime = executionTime;
                            }
                        }
                        break;
                    case 3:
                        //判断装载的产品，如果是合模完成的砂箱
                        //前往待放置的点位
                        if (input.getColdMoldClosingFerry().getLocationCoordinate().getX() == input.getColdMoldClosingLayPosition().getCoordinate().getX()){
                            if (input.getColdMoldClosingLayPosition().getStatus().equals(UNOCCUPIED)){
                                //发生前往放置热砂点位的卸载事件
                                if (coldStateChange){
                                    moldClosingGanttChartLinkInput = coldMoldClosingEventFlow.ColdMoleFullUnLoadLayPositionEventCreate(moldClosingGanttChartLinkInput.getInput(), moldClosingGanttChartLinkInput.getGanttChart());

                                    coldStateChange = false;

                                }else {
                                    double executionTime = (Math.abs(input.getColdMoldClosingLayPosition().getCoordinate().getY() -
                                            input.getColdMoldClosingFerry().getLocationCoordinate().getY()) / input.getColdMoldClosingSubcar().getFullSpeed()) +
                                            (input.getColdMoldClosingSubcar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getColdMoldClosingLayPosition().getCoordinate().getY() -
                                            input.getColdMoldClosingFerry().getLocationCoordinate().getY()) / input.getColdMoldClosingSubcar().getEmptySpeed());
                                    coldMoldTime = input.getTotalTime() + executionTime;
                                    coldStateChange = true;

                                    eventTime = executionTime;
                                }
                            }
                        }else {
                            //发生前往放置热砂点位的事件
                            if (coldStateChange){
                                moldClosingGanttChartLinkInput = coldMoldClosingEventFlow.ColdMoleFullGoLayPositionEventCreate(moldClosingGanttChartLinkInput.getInput(), moldClosingGanttChartLinkInput.getGanttChart());

                                coldStateChange = false;
                            }else {
                                double executionTime = Math.abs(input.getColdMoldClosingLayPosition().getCoordinate().getX() -
                                        input.getColdMoldClosingFerry().getLocationCoordinate().getX()) / input.getColdMoldClosingFerry().getFullSpeed();
                                coldMoldTime = input.getTotalTime() + executionTime;
                                coldStateChange = true;

                                eventTime = executionTime;
                            }
                        }
                        break;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[6] = eventTime;
            eventTime = 0.0;
        }

        //判断热砂工岗
        if (input.getTotalTime() >= hotWorkStationTime_1){
            if (input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_1()).getStatus().equals(WORKING)){
                if (hotWorkStationStateChange_1){
                    moldClosingGanttChartLinkInput = workStationEventFlow.MoleClosingWorkStationWorkingEventCreate(moldClosingGanttChartLinkInput.getInput(), input.getHotMoldClosingSubcar(),
                            input.getHotMoldClosingFerry(), moldClosingGanttChartLinkInput.getGanttChart(), input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_1(),
                            input.getMoldClosingIndexInput().getHotMoldWorkStationInGanttIndex_1());

                    hotWorkStationStateChange_1 = false;

                }else {
                    double executionTime = input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_1()).getProduct().getFirst().getMoldClosingTime();

                    hotWorkStationTime_1 = input.getTotalTime() + executionTime;
                    hotWorkStationStateChange_1 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[7] = eventTime;
            eventTime = 0.0;
        }

        if (input.getTotalTime() >= hotWorkStationTime_2){
            if (input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_2()).getStatus().equals(WORKING)){
                if (hotWorkStationStateChange_2){
                    moldClosingGanttChartLinkInput = workStationEventFlow.MoleClosingWorkStationWorkingEventCreate(moldClosingGanttChartLinkInput.getInput(), input.getHotMoldClosingSubcar(),
                            input.getHotMoldClosingFerry(), moldClosingGanttChartLinkInput.getGanttChart(), input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_2(),
                            input.getMoldClosingIndexInput().getHotMoldWorkStationInGanttIndex_2());

                    hotWorkStationStateChange_2 = false;
                }else {
                    double executionTime = input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_2()).getProduct().getFirst().getMoldClosingTime();
                    hotWorkStationTime_2 = input.getTotalTime() + executionTime;
                    hotWorkStationStateChange_2 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[8] = eventTime;
            eventTime = 0.0;
        }

        if (input.getTotalTime() >= hotWorkStationTime_3){
            if (input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_3()).getStatus().equals(WORKING)){
                if (hotWorkStationStateChange_3){
                    moldClosingGanttChartLinkInput = workStationEventFlow.MoleClosingWorkStationWorkingEventCreate(moldClosingGanttChartLinkInput.getInput(), input.getHotMoldClosingSubcar(),
                            input.getHotMoldClosingFerry(), moldClosingGanttChartLinkInput.getGanttChart(), input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_3(),
                            input.getMoldClosingIndexInput().getHotMoldWorkStationInGanttIndex_3());

                    hotWorkStationStateChange_3 = false;
                }else {
                    double executionTime = input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_3()).getProduct().getFirst().getMoldClosingTime();
                    hotWorkStationTime_3 = input.getTotalTime() + executionTime;
                    hotWorkStationStateChange_3 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[9] = eventTime;
            eventTime = 0.0;
        }

        if (input.getTotalTime() >= hotWorkStationTime_4){
            if (input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_4()).getStatus().equals(WORKING)){
                if (hotWorkStationStateChange_4){
                    moldClosingGanttChartLinkInput = workStationEventFlow.MoleClosingWorkStationWorkingEventCreate(moldClosingGanttChartLinkInput.getInput(), input.getHotMoldClosingSubcar(),
                            input.getHotMoldClosingFerry(), moldClosingGanttChartLinkInput.getGanttChart(), input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_4(),
                            input.getMoldClosingIndexInput().getHotMoldWorkStationInGanttIndex_4());

                    hotWorkStationStateChange_4 = false;
                }else {
                    double executionTime = input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_4()).getProduct().getFirst().getMoldClosingTime();
                    hotWorkStationTime_4 = input.getTotalTime() + executionTime;
                    hotWorkStationStateChange_4 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[10] = eventTime;
            eventTime = 0.0;
        }

        //判断冷砂工岗
        if (input.getTotalTime() >= coldWorkStationTime_1){
            if (input.getWorkStations().get(input.getMoldClosingIndexInput().getColdMoldClosingWorkStationIndex_1()).getStatus().equals(WORKING)){
                if (coldWorkStationStateChange_1){
                    moldClosingGanttChartLinkInput = workStationEventFlow.MoleClosingWorkStationWorkingEventCreate(moldClosingGanttChartLinkInput.getInput(), input.getColdMoldClosingSubcar(),
                            input.getColdMoldClosingFerry(), moldClosingGanttChartLinkInput.getGanttChart(), input.getMoldClosingIndexInput().getColdMoldClosingWorkStationIndex_1(),
                            input.getMoldClosingIndexInput().getColdMoldWorkStationInGanttIndex_1());

                    coldWorkStationStateChange_1 = false;
                }else {
                    double executionTime = input.getWorkStations().get(input.getMoldClosingIndexInput().getColdMoldClosingWorkStationIndex_1()).getProduct().getFirst().getMoldClosingTime();
                    coldWorkStationTime_1 = input.getTotalTime() + executionTime;
                    coldWorkStationStateChange_1 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[11] = eventTime;
            eventTime = 0.0;
        }

        if (input.getTotalTime() >= coldWorkStationTime_2){
            if (input.getWorkStations().get(input.getMoldClosingIndexInput().getColdMoldClosingWorkStationIndex_2()).getStatus().equals(WORKING)){
                if (coldWorkStationStateChange_2){
                    moldClosingGanttChartLinkInput = workStationEventFlow.MoleClosingWorkStationWorkingEventCreate(moldClosingGanttChartLinkInput.getInput(), input.getColdMoldClosingSubcar(),
                            input.getColdMoldClosingFerry(), moldClosingGanttChartLinkInput.getGanttChart(), input.getMoldClosingIndexInput().getColdMoldClosingWorkStationIndex_2(),
                            input.getMoldClosingIndexInput().getColdMoldWorkStationInGanttIndex_2());

                    coldWorkStationStateChange_2 = false;
                }else {
                    double executionTime = input.getWorkStations().get(input.getMoldClosingIndexInput().getColdMoldClosingWorkStationIndex_2()).getProduct().getFirst().getMoldClosingTime();
                    coldWorkStationTime_2 = input.getTotalTime() + executionTime;
                    coldWorkStationStateChange_2 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[12] = eventTime;
            eventTime = 0.0;
        }

        if (input.getTotalTime() >= coldWorkStationTime_3){
            if (input.getWorkStations().get(input.getMoldClosingIndexInput().getColdMoldClosingWorkStationIndex_3()).getStatus().equals(WORKING)){
                if (coldWorkStationStateChange_3){
                    moldClosingGanttChartLinkInput = workStationEventFlow.MoleClosingWorkStationWorkingEventCreate(moldClosingGanttChartLinkInput.getInput(), input.getColdMoldClosingSubcar(),
                            input.getColdMoldClosingFerry(), moldClosingGanttChartLinkInput.getGanttChart(), input.getMoldClosingIndexInput().getColdMoldClosingWorkStationIndex_3(),
                            input.getMoldClosingIndexInput().getColdMoldWorkStationInGanttIndex_3());

                    coldWorkStationStateChange_3 = false;
                }else {
                    double executionTime = input.getWorkStations().get(input.getMoldClosingIndexInput().getColdMoldClosingWorkStationIndex_3()).getProduct().getFirst().getMoldClosingTime();
                    coldWorkStationTime_3 = input.getTotalTime() + executionTime;
                    coldWorkStationStateChange_3 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[13] = eventTime;
            eventTime = 0.0;
        }

        if (input.getTotalTime() >= coldWorkStationTime_4){
            if (input.getWorkStations().get(input.getMoldClosingIndexInput().getColdMoldClosingWorkStationIndex_4()).getStatus().equals(WORKING)){
                if (coldWorkStationStateChange_4){
                    moldClosingGanttChartLinkInput = workStationEventFlow.MoleClosingWorkStationWorkingEventCreate(moldClosingGanttChartLinkInput.getInput(), input.getColdMoldClosingSubcar(),
                            input.getColdMoldClosingFerry(), moldClosingGanttChartLinkInput.getGanttChart(), input.getMoldClosingIndexInput().getColdMoldClosingWorkStationIndex_4(),
                            input.getMoldClosingIndexInput().getColdMoldWorkStationInGanttIndex_4());

                    coldWorkStationStateChange_4 = false;
                }else {
                    double executionTime = input.getWorkStations().get(input.getMoldClosingIndexInput().getColdMoldClosingWorkStationIndex_4()).getProduct().getFirst().getMoldClosingTime();
                    coldWorkStationTime_4 = input.getTotalTime() + executionTime;
                    coldWorkStationStateChange_4 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[14] = eventTime;
            eventTime = 0.0;
        }

        return new OutBoundANDMoldClosing(outBoundInput, moldClosingGanttChartLinkInput, finishedCount);
    }
}
