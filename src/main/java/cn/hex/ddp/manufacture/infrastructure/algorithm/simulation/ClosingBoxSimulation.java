package cn.hex.ddp.manufacture.infrastructure.algorithm.simulation;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.ClosingBoxSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationException;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.closingBox.ClosingBoxSimulationTools;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.closingBox.ClosingBoxWorkStationEventFlow;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.closingBox.ColdClosingBoxEventFlow;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.closingBox.HotClosingBoxEventFlow;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.mouldingLine.OutBoundMouldingAndClosingBox;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;

import static cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum.WORKING;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/20 20:40
 */
@Slf4j
public class ClosingBoxSimulation {
    /**
     * 热砂合箱时间
     */
    private double hotClosingTime = 0.0;
    /**
     * 冷砂合箱时间
     */
    private double coldClosingTime = 0.0;
    /**
     * 热砂合箱工岗1时间
     */
    private double hotWorkStationTime_1 = 0.0;
    /**
     * 热砂合箱工岗2时间
     */
    private double hotWorkStationTime_2 = 0.0;
    /**
     * 热砂合箱工岗3时间
     */
    private double hotWorkStationTime_3 = 0.0;
    /**
     * 热砂合箱工岗4时间
     */
    private double hotWorkStationTime_4 = 0.0;
    /**
     * 冷砂合箱工岗1时间
     */
    private double coldWorkStationTime_1 = 0.0;
    /**
     * 冷砂合箱工岗2时间
     */
    private double coldWorkStationTime_2 = 0.0;
    /**
     * 冷砂合箱工岗3时间
     */
    private double coldWorkStationTime_3 = 0.0;
    /**
     * 冷砂合箱工岗4时间
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
     * 仿真总工具类
     */
    private final SimulationTool allTool = new SimulationTool();
    /**
     * 合箱区放置的工具类
     */
    private final ClosingBoxSimulationTools tools = new ClosingBoxSimulationTools();
    /**
     * 创建事件的类
     */
    private final HotClosingBoxEventFlow hotClosingBoxEventFlow = new HotClosingBoxEventFlow();
    /**
     * 创建事件的类
     */
    private final ColdClosingBoxEventFlow coldClosingBoxEventFlow = new ColdClosingBoxEventFlow();
    /**
     * 创建事件的类
     */
    private final ClosingBoxWorkStationEventFlow closingBoxWorkStationEventFlow = new ClosingBoxWorkStationEventFlow();

    private double eventTime = 0.0;

    public OutBoundMouldingAndClosingBox ClosingBoxSimulationRun(OutBoundMouldingAndClosingBox simulationInput, Double[] timeList) throws SimulationException {
        //创建仿真结果的新地址，防止出现拷贝问题
        OutBoundMouldingAndClosingBox simulationOutput = SerializationUtils.clone(simulationInput);
        //获取合箱区的仿真输入
        ClosingBoxSimulationInput input = simulationOutput.getClosingBoxInput().getInput();

        //判断热砂合箱轨道是否可以发生事件
        if (input.getTotalTime() >= hotClosingTime) {
            //判断子车是否满载
            if (allTool.judgeSubCarIsEmpty(input.getHotClosingBoxSubCar())){
                //判断母车是否到达热砂合箱的交互点并判断点位状态是否被占用
                if (input.getHotClosingBoxFerry().getLocationCoordinate().getX() == input.getHotClosingBoxPosition().getCoordinate().getX() &&
                        input.getHotClosingBoxPosition().getStatus().equals(PositionStatusEnum.OCCUPIED)){
                    //进行装载的事件流
                    if (hotStateChange){
                        simulationOutput = hotClosingBoxEventFlow.HotEmptyLoadInPositionEventCreate(simulationOutput);

                        hotStateChange = false;
                    }else {
                        double executionTime = (Math.abs(input.getHotClosingBoxPosition().getCoordinate().getY() -
                                input.getHotClosingBoxFerry().getLocationCoordinate().getY()) / input.getHotClosingBoxSubCar().getEmptySpeed()) +
                                (input.getHotClosingBoxSubCar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getHotClosingBoxPosition().getCoordinate().getY() -
                                input.getHotClosingBoxFerry().getLocationCoordinate().getY()) / input.getHotClosingBoxSubCar().getFullSpeed());
                        hotClosingTime = input.getTotalTime() + executionTime;
                        hotStateChange = true;

                        eventTime = executionTime;
                    }
                    log.info("发生热砂子车前往Hot点位装载事件" + hotClosingTime + " 产品类型：" + input.getHotClosingBoxPosition().getProduct());

                    //判断母车是否到达工岗并判断工岗状态是否为待搬运
                    //进行装载的事件流
                }else if (tools.judgeHotClosingBoxFerryInWorkstation(input.getHotClosingBoxFerry(), input.getWorkStations(), input.getClosingBoxIndexInput()) != 0){
                    int index = tools.judgeHotClosingBoxFerryInWorkstation(input.getHotClosingBoxFerry(), input.getWorkStations(), input.getClosingBoxIndexInput());
                    //发生装载事件
                    if (hotStateChange){
                        Integer workStationIndex;
                        if (index == 1){
                            workStationIndex = input.getClosingBoxIndexInput().getHotClosingBoxWorkStationIndex_1();
                        }else if (index == 2){
                            workStationIndex = input.getClosingBoxIndexInput().getHotClosingBoxWorkStationIndex_2();
                        }else if (index == 3){
                            workStationIndex = input.getClosingBoxIndexInput().getHotClosingBoxWorkStationIndex_3();
                        }else {
                            workStationIndex = input.getClosingBoxIndexInput().getHotClosingBoxWorkStationIndex_4();
                        }

                        simulationOutput = hotClosingBoxEventFlow.HotClosingBoxEmptyLoadInWorkStationEventCreate(simulationOutput, workStationIndex);

                        hotStateChange = false;

                    }else {
                        double executionTime = (Math.abs(input.getWorkStations().get(input.getClosingBoxIndexInput().getHotClosingBoxWorkStationIndex_1()).getWorkPositionCoordinate().getY() -
                                input.getHotClosingBoxFerry().getLocationCoordinate().getY()) / input.getHotClosingBoxSubCar().getEmptySpeed()) +
                                (input.getHotClosingBoxSubCar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getWorkStations().get(input.getClosingBoxIndexInput().getHotClosingBoxWorkStationIndex_1()).getWorkPositionCoordinate().getY() -
                                input.getHotClosingBoxFerry().getLocationCoordinate().getY()) / input.getHotClosingBoxSubCar().getFullSpeed());
                        hotClosingTime = input.getTotalTime() + executionTime;
                        hotStateChange = true;

                        eventTime = executionTime;

                    }
//                    log.info("发生子车前往工岗装载待搬运事件" + hotClosingTime);

                    //判断工岗是否处于待搬运状态
                    //如果有，前往装载
                }else {
                    int workStationIndex = tools.judgeHotClosingBoxWorkStationIsWaitingTransprot(input.getWorkStations(), input.getClosingBoxIndexInput());
                    //判断是否有工岗待搬运
                    if (workStationIndex != -1){
                        //发生前往工岗的事件
                        if (hotStateChange){
                            simulationOutput = hotClosingBoxEventFlow.HotClosingBoxEmptyGoWorkStationEventCreate(simulationOutput, workStationIndex);

                            hotStateChange = false;
                        }else {
                            double executionTime = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                                    input.getHotClosingBoxFerry().getLocationCoordinate().getX()) / input.getHotClosingBoxFerry().getEmptySpeed();
                            hotClosingTime = input.getTotalTime() + executionTime;
                            hotStateChange = true;

                            eventTime = executionTime;
                        }

//                        log.info("发生母车前往工岗搬运待搬运砂箱事件" + hotClosingTime);

                        //没有待搬运的工岗
                    }else {
                        //判断4个工岗是否全部被占用，正等待工作完成，避免车辆在搬运热砂合箱点位的砂箱后整个开箱区堵塞（因为工岗被占用）
                        if (!tools.judgeHotClosingBoxWorkStationAllInWorking(input.getWorkStations(), input.getClosingBoxIndexInput())){
                            //判断热砂合箱点位是否有待搬运的产品
                            //如果有，前往热砂合箱点位
                            if (input.getHotClosingBoxPosition().getStatus().equals(PositionStatusEnum.OCCUPIED)){
                                //发生前往热砂合箱点位的装载事件
                                if (hotStateChange){
                                    simulationOutput = hotClosingBoxEventFlow.HotClosingBoxEmptyGoHotPosotionEventCreate(simulationOutput);

                                    hotStateChange = false;
                                }else {
                                    double executionTime = Math.abs(input.getHotClosingBoxPosition().getCoordinate().getX() -
                                            input.getHotClosingBoxFerry().getLocationCoordinate().getX()) / input.getHotClosingBoxFerry().getEmptySpeed();
                                    hotClosingTime = input.getTotalTime() + executionTime;
                                    hotStateChange = true;

                                    eventTime = executionTime;
//                                log.info(executionTime);
                                }

//                            log.info("发生母车前往点位搬运砂箱事件" + hotClosingTime + "??" + input.getTotalTime());

                            }
                        }
                    }
                }

                //子车满载
            }else {
                //判断车辆装载的产品是否是合箱完成的砂箱产品
                if (input.getHotClosingBoxSubCar().getProduct().getProductType().equals(ProductTypeEnum.WHOLE_SAND_SHELL_BOX)){
                    //判断母车是否到达合箱完成前往浇筑的点位处
                    if (input.getHotClosingBoxFerry().getLocationCoordinate().getX() == input.getHotClosingBoxGoPourPosition().getCoordinate().getX()){
                        //判断点位是否被占用
                        if (input.getHotClosingBoxGoPourPosition().getStatus().equals(PositionStatusEnum.UNOCCUPIED)){
                            //发生子车前往点位卸载砂箱的事件
                            if (hotStateChange){
                                simulationOutput = hotClosingBoxEventFlow.HotClosingBoxFullUnLoadPositionEventCreate(simulationOutput);

                                hotStateChange = false;

                            }else {
                                double executionTime = (Math.abs(input.getHotClosingBoxGoPourPosition().getCoordinate().getY() -
                                        input.getHotClosingBoxFerry().getLocationCoordinate().getY()) / input.getHotClosingBoxSubCar().getFullSpeed()) +
                                        (input.getHotClosingBoxSubCar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getHotClosingBoxGoPourPosition().getCoordinate().getY() -
                                        input.getHotClosingBoxFerry().getLocationCoordinate().getY()) / input.getHotClosingBoxSubCar().getEmptySpeed());
                                hotClosingTime = input.getTotalTime() + executionTime;
                                hotStateChange = true;

                                eventTime = executionTime;
                            }

//                            log.info("发生子车在点位卸载砂箱事件" + hotClosingTime);
                        }

                        //没到点位处
                    }else {
                        //发生前往点位的事件
                        if (hotStateChange){
                            simulationOutput = hotClosingBoxEventFlow.HotClosingBoxFullGoPositionEventCreate(simulationOutput);

                            hotStateChange = false;

                        }else {
                            double executionTime = Math.abs(input.getHotClosingBoxGoPourPosition().getCoordinate().getX() -
                                    input.getHotClosingBoxFerry().getLocationCoordinate().getX()) / input.getHotClosingBoxFerry().getFullSpeed();
                            hotClosingTime = input.getTotalTime() + executionTime;
                            hotStateChange = true;

                            eventTime = executionTime;
                        }

//                        log.info("发生母车前往点位卸载砂箱事件" + hotClosingTime);
                    }

                    //车辆装载的产品需要前往工岗处
                }else {
                    //判断是否需要搬运前往待配对的工岗
                    if (tools.judgeHotClosingBoxCarIsGoPairWorkStation(input.getHotClosingBoxSubCar(), input.getWorkStations(), input.getClosingBoxIndexInput()) != -1){
                        int workStationIndex = tools.judgeHotClosingBoxCarIsGoPairWorkStation(input.getHotClosingBoxSubCar(), input.getWorkStations(), input.getClosingBoxIndexInput());
                        //发生前往工岗卸载砂箱的指令
                        if (hotStateChange){
                            simulationOutput = hotClosingBoxEventFlow.HotClosingBoxFullGoPairWorkStationEventCreate(simulationOutput, workStationIndex);

                            hotStateChange = false;

                        }else {
                            double executionTime = (Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                                    input.getHotClosingBoxFerry().getLocationCoordinate().getX()) / input.getHotClosingBoxFerry().getFullSpeed()) +
                                    (Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                                            input.getHotClosingBoxFerry().getLocationCoordinate().getY()) / input.getHotClosingBoxSubCar().getFullSpeed()) +
                                    (input.getHotClosingBoxSubCar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                                    input.getHotClosingBoxFerry().getLocationCoordinate().getY()) / input.getHotClosingBoxSubCar().getEmptySpeed());
                            hotClosingTime = input.getTotalTime() + executionTime;
                            hotStateChange = true;

                            eventTime = executionTime;
                        }

//                        log.info("发生母车前往待配对工岗卸载事件" + hotClosingTime);

                        //前往空闲的工岗
                    }else {
                        int workStationIndex = tools.judgeHotClosingBoxCarGoWhichWorkStation(input.getWorkStations(), input.getClosingBoxIndexInput());
                        //判断全部工岗是否已满
                        if (workStationIndex != -1){
                            //发生前往工岗卸载砂箱的指令
                            if (hotStateChange){
                                simulationOutput = hotClosingBoxEventFlow.HotClosingBoxFullGoWorkStationEventCreate(simulationOutput, workStationIndex);

                                hotStateChange = false;

                            }else {
                                double executionTime = (Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                                        input.getHotClosingBoxFerry().getLocationCoordinate().getX()) / input.getHotClosingBoxFerry().getFullSpeed()) +
                                        (Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                                                input.getHotClosingBoxFerry().getLocationCoordinate().getY()) / input.getHotClosingBoxSubCar().getFullSpeed()) +
                                        (input.getHotClosingBoxSubCar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                                        input.getHotClosingBoxFerry().getLocationCoordinate().getY()) / input.getHotClosingBoxSubCar().getEmptySpeed());
                                hotClosingTime = input.getTotalTime() + executionTime;
                                hotStateChange = true;

                                eventTime = executionTime;
                            }

//                            log.info("发生母车前往空闲工岗卸载事件" + hotClosingTime);
                        }
                    }
                }
            }
        }

        if (eventTime != 0.0){
            timeList[21] = eventTime;
            eventTime = 0.0;
        }

        //判断冷砂合箱轨道是否可以发生事件
        if (input.getTotalTime() >= coldClosingTime) {
            //判断子车是否满载
            if (allTool.judgeSubCarIsEmpty(input.getColdClosingBoxSubCar())){
                //判断母车是否到达热砂合箱的交互点并判断点位状态是否被占用
                //进行装载的事件流
                if (input.getColdClosingBoxFerry().getLocationCoordinate().getX() == input.getColdClosingBoxPosition().getCoordinate().getX() &&
                        input.getColdClosingBoxPosition().getStatus().equals(PositionStatusEnum.OCCUPIED)){
                    if (coldStateChange){
                        simulationOutput = coldClosingBoxEventFlow.ColdEmptyLoadInPositionEventCreate(simulationOutput);

                        coldStateChange = false;
                    }else {
                        double executionTime = (Math.abs(input.getColdClosingBoxPosition().getCoordinate().getY() -
                                input.getColdClosingBoxFerry().getLocationCoordinate().getY()) / input.getColdClosingBoxSubCar().getEmptySpeed()) +
                                (input.getColdClosingBoxSubCar().getTopRodRaiseOrFallTime()) + ((Math.abs(input.getColdClosingBoxPosition().getCoordinate().getY() -
                                input.getColdClosingBoxFerry().getLocationCoordinate().getY()) / input.getColdClosingBoxSubCar().getFullSpeed()));
                        coldClosingTime = input.getTotalTime() + executionTime;
                        coldStateChange = true;

                        eventTime = executionTime;
                    }

                    log.info("发生冷砂子车前往Cold点位装载事件" + coldClosingTime + " 产品类型：" + input.getColdClosingBoxPosition().getProduct());

                    //判断母车是否到达工岗并判断工岗状态是否为待搬运
                    //进行装载的事件流
                }else if (tools.judgeColdClosingBoxFerryInWorkstation(input.getColdClosingBoxFerry(), input.getWorkStations(), input.getClosingBoxIndexInput()) != 0){
                    int index = tools.judgeColdClosingBoxFerryInWorkstation(input.getColdClosingBoxFerry(), input.getWorkStations(), input.getClosingBoxIndexInput());
                    //发生装载事件
                    if (coldStateChange){
                        Integer workStationIndex;
                        if (index == 1){
                            workStationIndex = input.getClosingBoxIndexInput().getColdClosingBoxWorkStationIndex_1();
                        }else if (index == 2){
                            workStationIndex = input.getClosingBoxIndexInput().getColdClosingBoxWorkStationIndex_2();
                        }else if (index == 3){
                            workStationIndex = input.getClosingBoxIndexInput().getColdClosingBoxWorkStationIndex_3();
                        }else {
                            workStationIndex = input.getClosingBoxIndexInput().getColdClosingBoxWorkStationIndex_4();
                        }

                        simulationOutput = coldClosingBoxEventFlow.ColdClosingBoxEmptyLoadInWorkStationEventCreate(simulationOutput, workStationIndex);

                        coldStateChange = false;

                    }else {
                        double executionTime = (Math.abs(input.getWorkStations().get(input.getClosingBoxIndexInput().getColdClosingBoxWorkStationIndex_1()).getWorkPositionCoordinate().getY() -
                                input.getColdClosingBoxFerry().getLocationCoordinate().getY()) / input.getColdClosingBoxSubCar().getEmptySpeed()) +
                                (input.getColdClosingBoxSubCar().getTopRodRaiseOrFallTime()) + ((Math.abs(input.getWorkStations().get(input.getClosingBoxIndexInput().getColdClosingBoxWorkStationIndex_1()).getWorkPositionCoordinate().getY() -
                                input.getColdClosingBoxFerry().getLocationCoordinate().getY()) / input.getColdClosingBoxSubCar().getFullSpeed()));
                        coldClosingTime = input.getTotalTime() + executionTime;
                        coldStateChange = true;

                        eventTime = executionTime;

                    }

//                    log.info("发生冷砂子车前往点位装载事件" + coldClosingTime);

                    //判断工岗是否处于待搬运状态
                    //如果有，前往装载
                }else {
                    int workStationIndex = tools.judgeColdClosingBoxWorkStationIsWaitingTransprot(input.getWorkStations(), input.getClosingBoxIndexInput());
                    //判断是否有工岗待搬运
                    if (workStationIndex != -1){
                        //发生前往工岗的事件
                        if (coldStateChange){
                            simulationOutput = coldClosingBoxEventFlow.ColdClosingBoxEmptyGoWorkStationEventCreate(simulationOutput, workStationIndex);

                            coldStateChange = false;

                        }else {
                            double executionTime = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                                    input.getColdClosingBoxFerry().getLocationCoordinate().getX()) / input.getColdClosingBoxFerry().getEmptySpeed();
                            coldClosingTime = input.getTotalTime() + executionTime;
                            coldStateChange = true;

                            eventTime = executionTime;
                        }

                        //没有待搬运的工岗
                    }else {
                        //判断4个工岗是否全部被占用，正等待工作完成，避免车辆在搬运冷砂合箱点位的砂箱后整个开箱区堵塞（因为工岗被占用）
                        if (!tools.judgeColdClosingBoxWorkStationAllInWorking(input.getWorkStations(), input.getClosingBoxIndexInput())){
                            //判断热砂合箱点位是否有待搬运的产品
                            //如果有，前往热砂合箱点位
                            if (input.getColdClosingBoxPosition().getStatus().equals(PositionStatusEnum.OCCUPIED)){
                                //发生前往热砂合箱点位的装载事件
                                if (coldStateChange){
                                    simulationOutput = coldClosingBoxEventFlow.ColdClosingBoxEmptyGoColdPosotionEventCreate(simulationOutput);

                                    coldStateChange = false;
                                }else {
                                    double executionTime = Math.abs(input.getColdClosingBoxPosition().getCoordinate().getX() -
                                            input.getColdClosingBoxFerry().getLocationCoordinate().getX()) / input.getColdClosingBoxFerry().getEmptySpeed();
                                    coldClosingTime = input.getTotalTime() + executionTime;
                                    coldStateChange = true;

                                    eventTime = executionTime;
                                }
                            }
                        }
                    }
                }

                //子车满载
            }else {
                //判断车辆装载的产品是否是合箱完成的砂箱产品
                if (input.getColdClosingBoxSubCar().getProduct().getProductType().equals(ProductTypeEnum.WHOLE_SAND_SHELL_BOX)){
                    //判断母车是否到达合箱完成前往浇筑的点位处
                    if (input.getColdClosingBoxFerry().getLocationCoordinate().getX() == input.getColdClosingBoxGoPourPosition().getCoordinate().getX()){
                        //判断点位是否被占用
                        if (input.getColdClosingBoxGoPourPosition().getStatus().equals(PositionStatusEnum.UNOCCUPIED)){
                            //发生子车前往点位卸载砂箱的事件
                            if (coldStateChange){
                                simulationOutput = coldClosingBoxEventFlow.ColdClosingBoxFullUnLoadPositionEventCreate(simulationOutput);

                                coldStateChange = false;

                            }else {
                                double executionTime = (Math.abs(input.getColdClosingBoxGoPourPosition().getCoordinate().getY() -
                                        input.getColdClosingBoxFerry().getLocationCoordinate().getY()) / input.getColdClosingBoxSubCar().getFullSpeed()) +
                                        (input.getColdClosingBoxSubCar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getColdClosingBoxGoPourPosition().getCoordinate().getY() -
                                        input.getColdClosingBoxFerry().getLocationCoordinate().getY()) / input.getColdClosingBoxSubCar().getEmptySpeed());
                                coldClosingTime = input.getTotalTime() + executionTime;
                                coldStateChange = true;

                                eventTime = executionTime;
                            }
                        }

                        //没到点位处
                    }else {
                        //发生前往点位的事件
                        if (coldStateChange){
                            simulationOutput = coldClosingBoxEventFlow.ColdClosingBoxFullGoPositionEventCreate(simulationOutput);

                            coldStateChange = false;

                        }else {
                            double executionTime = Math.abs(input.getColdClosingBoxGoPourPosition().getCoordinate().getX() -
                                    input.getColdClosingBoxFerry().getLocationCoordinate().getX()) / input.getColdClosingBoxFerry().getFullSpeed();
                            coldClosingTime = input.getTotalTime() + executionTime;
                            coldStateChange = true;

                            eventTime = executionTime;
                        }
                    }

                    //车辆装载的产品需要前往工岗处
                }else {
                    //判断是否需要搬运前往待配对的工岗
                    if (tools.judgeColdClosingBoxCarIsGoPairWorkStation(input.getColdClosingBoxSubCar(), input.getWorkStations(), input.getClosingBoxIndexInput()) != -1){
                        int workStationIndex = tools.judgeColdClosingBoxCarIsGoPairWorkStation(input.getColdClosingBoxSubCar(), input.getWorkStations(), input.getClosingBoxIndexInput());
                        //发生前往工岗卸载砂箱的指令
                        if (coldStateChange){
                            simulationOutput = coldClosingBoxEventFlow.ColdClosingBoxFullGoPairWorkStationEventCreate(simulationOutput, workStationIndex);

                            coldStateChange = false;

                        }else {
                            double executionTime = (Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                                    input.getColdClosingBoxFerry().getLocationCoordinate().getX()) / input.getColdClosingBoxFerry().getFullSpeed()) +
                                    (Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                                            input.getColdClosingBoxFerry().getLocationCoordinate().getY()) / input.getColdClosingBoxSubCar().getFullSpeed()) +
                                    (input.getColdClosingBoxSubCar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                                    input.getColdClosingBoxFerry().getLocationCoordinate().getY()) / input.getColdClosingBoxSubCar().getEmptySpeed());
                            coldClosingTime = input.getTotalTime() + executionTime;
                            coldStateChange = true;

                            eventTime = executionTime;
                        }

                        //前往空闲的工岗
                    }else {
                        int workStationIndex = tools.judgeColdClosingBoxCarGoWhichWorkStation(input.getWorkStations(), input.getClosingBoxIndexInput());
                        //判断全部工岗是否已满
                        if (workStationIndex != -1){
                            //发生前往工岗卸载砂箱的指令
                            if (coldStateChange){
                                simulationOutput = coldClosingBoxEventFlow.ColdClosingBoxFullGoWorkStationEventCreate(simulationOutput, workStationIndex);

                                coldStateChange = false;

                            }else {
                                double executionTime = (Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                                        input.getColdClosingBoxFerry().getLocationCoordinate().getX()) / input.getColdClosingBoxFerry().getFullSpeed()) +
                                        (Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                                                input.getColdClosingBoxFerry().getLocationCoordinate().getY()) / input.getColdClosingBoxSubCar().getFullSpeed()) +
                                        (input.getColdClosingBoxSubCar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                                        input.getColdClosingBoxFerry().getLocationCoordinate().getY()) / input.getColdClosingBoxSubCar().getEmptySpeed());
                                coldClosingTime = input.getTotalTime() + executionTime;
                                coldStateChange = true;

                                eventTime = executionTime;
                            }
                        }
                    }
                }
            }
        }

        if (eventTime != 0.0){
            timeList[22] = eventTime;
            eventTime = 0.0;
        }

        //判断热砂合箱工岗1是否可以发生事件
        if (input.getTotalTime() >= hotWorkStationTime_1) {
            if (input.getWorkStations().get(input.getClosingBoxIndexInput().getHotClosingBoxWorkStationIndex_1()).getStatus().equals(WORKING)){
                if (hotWorkStationStateChange_1){
                    simulationOutput = closingBoxWorkStationEventFlow.ClosingBoxWorkStationWorkingEventCreate(simulationOutput, input.getHotClosingBoxSubCar(), input.getHotClosingBoxFerry(),
                            input.getClosingBoxIndexInput().getHotClosingBoxWorkStationIndex_1(), input.getClosingBoxIndexInput().getHotClosingBoxWorkStationInGanttIndex_1());

                    hotWorkStationStateChange_1 = false;

                }else {
                    double executionTime = input.getWorkStations().get(input.getClosingBoxIndexInput().getHotClosingBoxWorkStationIndex_1()).getProduct().getFirst().getClosingBoxTime();

                    hotWorkStationTime_1 = input.getTotalTime() + executionTime;
                    hotWorkStationStateChange_1 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[23] = eventTime;
            eventTime = 0.0;
        }

        //判断热砂合箱工岗2是否可以发生事件
        if (input.getTotalTime() >= hotWorkStationTime_2) {
            if (input.getWorkStations().get(input.getClosingBoxIndexInput().getHotClosingBoxWorkStationIndex_2()).getStatus().equals(WORKING)){
                if (hotWorkStationStateChange_2){
                    simulationOutput = closingBoxWorkStationEventFlow.ClosingBoxWorkStationWorkingEventCreate(simulationOutput, input.getHotClosingBoxSubCar(), input.getHotClosingBoxFerry(),
                            input.getClosingBoxIndexInput().getHotClosingBoxWorkStationIndex_2(), input.getClosingBoxIndexInput().getHotClosingBoxWorkStationInGanttIndex_2());

                    hotWorkStationStateChange_2 = false;

                }else {
                    double executionTime = input.getWorkStations().get(input.getClosingBoxIndexInput().getHotClosingBoxWorkStationIndex_2()).getProduct().getFirst().getClosingBoxTime();

                    hotWorkStationTime_2 = input.getTotalTime() + executionTime;
                    hotWorkStationStateChange_2 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[24] = eventTime;
            eventTime = 0.0;
        }

        //判断热砂合箱工岗3是否可以发生事件
        if (input.getTotalTime() >= hotWorkStationTime_3) {
            if (input.getWorkStations().get(input.getClosingBoxIndexInput().getHotClosingBoxWorkStationIndex_3()).getStatus().equals(WORKING)){
                if (hotWorkStationStateChange_3){
                    simulationOutput = closingBoxWorkStationEventFlow.ClosingBoxWorkStationWorkingEventCreate(simulationOutput, input.getHotClosingBoxSubCar(), input.getHotClosingBoxFerry(),
                            input.getClosingBoxIndexInput().getHotClosingBoxWorkStationIndex_3(), input.getClosingBoxIndexInput().getHotClosingBoxWorkStationInGanttIndex_3());

                    hotWorkStationStateChange_3 = false;

                }else {
                    double executionTime = input.getWorkStations().get(input.getClosingBoxIndexInput().getHotClosingBoxWorkStationIndex_3()).getProduct().getFirst().getClosingBoxTime();

                    hotWorkStationTime_3 = input.getTotalTime() + executionTime;
                    hotWorkStationStateChange_3 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[25] = eventTime;
            eventTime = 0.0;
        }

        //判断热砂合箱工岗4是否可以发生事件
        if (input.getTotalTime() >= hotWorkStationTime_4) {
            if (input.getWorkStations().get(input.getClosingBoxIndexInput().getHotClosingBoxWorkStationIndex_4()).getStatus().equals(WORKING)){
                if (hotWorkStationStateChange_4){
                    simulationOutput = closingBoxWorkStationEventFlow.ClosingBoxWorkStationWorkingEventCreate(simulationOutput, input.getHotClosingBoxSubCar(), input.getHotClosingBoxFerry(),
                            input.getClosingBoxIndexInput().getHotClosingBoxWorkStationIndex_4(), input.getClosingBoxIndexInput().getHotClosingBoxWorkStationInGanttIndex_4());

                    hotWorkStationStateChange_4 = false;

                }else {
                    double executionTime = input.getWorkStations().get(input.getClosingBoxIndexInput().getHotClosingBoxWorkStationIndex_4()).getProduct().getFirst().getClosingBoxTime();

                    hotWorkStationTime_4 = input.getTotalTime() + executionTime;
                    hotWorkStationStateChange_4 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[26] = eventTime;
            eventTime = 0.0;
        }

        //判断冷砂合箱工岗1是否可以发生事件
        if (input.getTotalTime() >= coldWorkStationTime_1) {
            if (input.getWorkStations().get(input.getClosingBoxIndexInput().getColdClosingBoxWorkStationIndex_1()).getStatus().equals(WORKING)){
                if (coldWorkStationStateChange_1){
                    simulationOutput = closingBoxWorkStationEventFlow.ClosingBoxWorkStationWorkingEventCreate(simulationOutput, input.getColdClosingBoxSubCar(), input.getColdClosingBoxFerry(),
                            input.getClosingBoxIndexInput().getColdClosingBoxWorkStationIndex_1(), input.getClosingBoxIndexInput().getColdClosingBoxWorkStationInGanttIndex_1());

                    coldWorkStationStateChange_1 = false;

                }else {
                    double executionTime = input.getWorkStations().get(input.getClosingBoxIndexInput().getColdClosingBoxWorkStationIndex_1()).getProduct().getFirst().getClosingBoxTime();

                    coldWorkStationTime_1 = input.getTotalTime() + executionTime;
                    coldWorkStationStateChange_1 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[27] = eventTime;
            eventTime = 0.0;
        }

        //判断冷砂合箱工岗2是否可以发生事件
        if (input.getTotalTime() >= coldWorkStationTime_2) {
            if (input.getWorkStations().get(input.getClosingBoxIndexInput().getColdClosingBoxWorkStationIndex_2()).getStatus().equals(WORKING)){
                if (coldWorkStationStateChange_2){
                    simulationOutput = closingBoxWorkStationEventFlow.ClosingBoxWorkStationWorkingEventCreate(simulationOutput, input.getColdClosingBoxSubCar(), input.getColdClosingBoxFerry(),
                            input.getClosingBoxIndexInput().getColdClosingBoxWorkStationIndex_2(), input.getClosingBoxIndexInput().getColdClosingBoxWorkStationInGanttIndex_2());

                    coldWorkStationStateChange_2 = false;

                }else {
                    double executionTime = input.getWorkStations().get(input.getClosingBoxIndexInput().getColdClosingBoxWorkStationIndex_2()).getProduct().getFirst().getClosingBoxTime();

                    coldWorkStationTime_2 = input.getTotalTime() + executionTime;
                    coldWorkStationStateChange_2 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[28] = eventTime;
            eventTime = 0.0;
        }

        //判断冷砂合箱工岗3是否可以发生事件
        if (input.getTotalTime() >= coldWorkStationTime_3) {
            if (input.getWorkStations().get(input.getClosingBoxIndexInput().getColdClosingBoxWorkStationIndex_3()).getStatus().equals(WORKING)){
                if (coldWorkStationStateChange_3){
                    simulationOutput = closingBoxWorkStationEventFlow.ClosingBoxWorkStationWorkingEventCreate(simulationOutput, input.getColdClosingBoxSubCar(), input.getColdClosingBoxFerry(),
                            input.getClosingBoxIndexInput().getColdClosingBoxWorkStationIndex_3(), input.getClosingBoxIndexInput().getColdClosingBoxWorkStationInGanttIndex_3());

                    coldWorkStationStateChange_3 = false;

                }else {
                    double executionTime = input.getWorkStations().get(input.getClosingBoxIndexInput().getColdClosingBoxWorkStationIndex_3()).getProduct().getFirst().getClosingBoxTime();

                    coldWorkStationTime_3 = input.getTotalTime() + executionTime;
                    coldWorkStationStateChange_3 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[29] = eventTime;
            eventTime = 0.0;
        }

        //判断冷砂合箱工岗4是否可以发生事件
        if (input.getTotalTime() >= coldWorkStationTime_4) {
            if (input.getWorkStations().get(input.getClosingBoxIndexInput().getColdClosingBoxWorkStationIndex_4()).getStatus().equals(WORKING)){
                if (coldWorkStationStateChange_4){
                    simulationOutput = closingBoxWorkStationEventFlow.ClosingBoxWorkStationWorkingEventCreate(simulationOutput, input.getColdClosingBoxSubCar(), input.getColdClosingBoxFerry(),
                            input.getClosingBoxIndexInput().getColdClosingBoxWorkStationIndex_4(), input.getClosingBoxIndexInput().getColdClosingBoxWorkStationInGanttIndex_4());

                    coldWorkStationStateChange_4 = false;

                }else {
                    double executionTime = input.getWorkStations().get(input.getClosingBoxIndexInput().getColdClosingBoxWorkStationIndex_4()).getProduct().getFirst().getClosingBoxTime();

                    coldWorkStationTime_4 = input.getTotalTime() + executionTime;
                    coldWorkStationStateChange_4 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[30] = eventTime;
            eventTime = 0.0;
        }

        return simulationOutput;
    }
}
