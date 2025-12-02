package cn.hex.ddp.manufacture.infrastructure.algorithm.simulation_2;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.OpenBoxSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.OutBoundSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.openBox.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.waitingPour.AfterClosingBoxAllLink;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayDeque;
import java.util.Deque;

import static cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum.WORKING;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/12/23 18:09
 */
@Slf4j
public class OpenBoxSimulation_2 {
    /**
     * 热砂开箱时间
     */
    private double hotOpenTime = 0.0;
    /**
     * 冷砂开箱时间
     */
    private double coldOpenTime = 0.0;
    /**
     * 热砂开箱工岗1时间
     */
    private double hotOpenWorkStationTime_1 = 0.0;
    /**
     * 热砂开箱工岗2时间
     */
    private double hotOpenWorkStationTime_2 = 0.0;
    /**
     * 热砂开箱工岗3时间
     */
    private double hotOpenWorkStationTime_3 = 0.0;
    /**
     * 热砂开箱工岗4时间
     */
    private double hotOpenWorkStationTime_4 = 0.0;
    /**
     * 冷砂开箱工岗1时间
     */
    private double coldOpenWorkStationTime_1 = 0.0;
    /**
     * 冷砂开箱工岗2时间
     */
    private double coldOpenWorkStationTime_2 = 0.0;
    /**
     * 冷砂开箱工岗3时间
     */
    private double coldOpenWorkStationTime_3 = 0.0;
    /**
     * 冷砂开箱工岗4时间
     */
    private double coldOpenWorkStationTime_4 = 0.0;
    /**
     * 铸件上轨道时间
     */
    private double castingUpTime = 0.0;
    /**
     * 铸件下轨道时间
     */
    private double castingDownTime = 0.0;
    /**
     * 铸件垂直轨道时间
     */
    private double castingVerticalTime = 0.0;
    /**
     * 右侧砂箱回流轨道时间
     */
    private double sandBoxRefluxTime = 0.0;
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean hotOpenStateChange = false;
    private boolean coldOpenStateChange = false;
    private boolean hotOpenWorkStationStateChange_1 = false;
    private boolean hotOpenWorkStationStateChange_2 = false;
    private boolean hotOpenWorkStationStateChange_3 = false;
    private boolean hotOpenWorkStationStateChange_4 = false;
    private boolean coldOpenWorkStationStateChange_1 = false;
    private boolean coldOpenWorkStationStateChange_2 = false;
    private boolean coldOpenWorkStationStateChange_3 = false;
    private boolean coldOpenWorkStationStateChange_4 = false;
    private boolean castingUpStateChange = false;
    private boolean castingDownStateChange = false;
    private boolean castingVerticalStateChange = false;
    private boolean sandBoxRefluxStateChange = false;
    /**
     * 仿真总工具类
     */
    private final SimulationTool allTool = new SimulationTool();
    /**
     * 开箱区放置的工具类
     */
    private final OpenBoxSimulationTools tools = new OpenBoxSimulationTools();
    /**
     * 创建事件的类
     */
    private final HotOpenBoxEventFlow hotOpenBoxEventFlow = new HotOpenBoxEventFlow();
    private final ColdOpenBoxEventFlow coldOpenBoxEventFlow = new ColdOpenBoxEventFlow();
    private final OpenBoxWorkStationEventFlow openBoxWorkStationEventFlow = new OpenBoxWorkStationEventFlow();
    private final CastingUpEventFlow castingUpEventFlow = new CastingUpEventFlow();
    private final CastingDownEventFlow castingDownEventFlow = new CastingDownEventFlow();
    private final CastingVerticalEventFlow castingVerticalEventFlow = new CastingVerticalEventFlow();
    private final SandBoxRefluxEventFlow sandBoxRefluxEventFlow = new SandBoxRefluxEventFlow();

    /**
     * 铸件区的铸件队列
     */
    private final Deque<Product> upProducts = new ArrayDeque<>();
    private final Deque<Product> downProducts = new ArrayDeque<>();

    private double eventTime = 0.0;

    public AfterClosingBoxAllLink OpenBoxSimulationRun(AfterClosingBoxAllLink afterClosingBoxAllLink, Double[] timeList){
        AfterClosingBoxAllLink simulationOutput = SerializationUtils.clone(afterClosingBoxAllLink);
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        OutBoundSimulationInput outBoundInput = simulationOutput.getOutBoundInput().getInput();

        //判断热砂开箱是否可以发生事件
        if (input.getTotalTime() >= hotOpenTime){
            //判断热砂开箱轨道子车是否满载
            if (allTool.judgeSubCarIsEmpty(input.getHotSubCar())){
                //参考合箱，注意开箱后产品有多个，注意热砂是下轨道
                //判断母车是否到达热砂开箱的交互点并判断点位状态是否被占用
                if(input.getHotFerry().getLocationCoordinate().getX() == input.getHotOpenBoxPosition().getCoordinate().getX() &&
                        input.getHotOpenBoxPosition().getStatus().equals(PositionStatusEnum.OCCUPIED)){
                    //进行装载的事件流
                    if (hotOpenStateChange){
                        simulationOutput = hotOpenBoxEventFlow.HotEmptyLoadInPositionEventCreate(simulationOutput);

                        hotOpenStateChange = false;
                    }else {
                        double executionTime = (Math.abs(input.getHotOpenBoxPosition().getCoordinate().getY() -
                                input.getHotFerry().getLocationCoordinate().getY()) / input.getHotSubCar().getEmptySpeed()) +
                                (input.getHotSubCar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getHotOpenBoxPosition().getCoordinate().getY() -
                                input.getHotFerry().getLocationCoordinate().getY()) / input.getHotSubCar().getFullSpeed());
                        hotOpenTime = input.getTotalTime() + executionTime;
                        hotOpenStateChange = true;

                        eventTime = executionTime;
                    }

                    log.info("开箱：在热砂点位装载产品" + hotOpenTime);

                }else if (tools.judgeHotOpenBoxFerryInWorkstation(input.getHotFerry(), input.getWorkStations(), input.getOpenBoxIndexInput()) != -1){
                    int workStationIndex = tools.judgeHotOpenBoxFerryInWorkstation(input.getHotFerry(), input.getWorkStations(), input.getOpenBoxIndexInput());
                    //发生装载事件
                    if (hotOpenStateChange){
                        simulationOutput = hotOpenBoxEventFlow.HotOpenBoxEmptyLoadInWorkStationEventCreate(simulationOutput, workStationIndex, downProducts);

                        hotOpenStateChange = false;

                    }else {
                        double executionTime = (Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                                input.getHotFerry().getLocationCoordinate().getY()) / input.getHotSubCar().getEmptySpeed()) +
                                (input.getHotSubCar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                                input.getHotFerry().getLocationCoordinate().getY()) / input.getHotSubCar().getFullSpeed());

                        hotOpenTime = input.getTotalTime() + executionTime;
                        hotOpenStateChange = true;

                        eventTime = executionTime;
                    }

//                    log.info("开箱：在工岗装载产品" + hotOpenTime);
                }else {
                    int workStationIndex = tools.judgeHotOpenBoxWorkStationIsWaitingTransprot(input.getWorkStations(), input.getOpenBoxIndexInput());
                    //判断是否有工岗待搬运
                    if (workStationIndex != -1){
                        //发生前往工岗的事件
                        if (hotOpenStateChange){
                            simulationOutput = hotOpenBoxEventFlow.HotOpenBoxEmptyGoWorkStationEventCreate(simulationOutput, workStationIndex);

                            hotOpenStateChange = false;

                        }else {
                            double executionTime = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                                    input.getHotFerry().getLocationCoordinate().getX()) / input.getHotFerry().getEmptySpeed();

                            hotOpenTime = input.getTotalTime() + executionTime;
                            hotOpenStateChange = true;

                            eventTime = executionTime;
                        }

                        log.info("开箱：前往工岗装载" + hotOpenTime);

                        //没有待搬运的工岗
                    }else {
                        //判断4个工岗是否全部被占用，正等待工作完成，避免车辆在搬运热砂开箱点位的砂箱后整个开箱区堵塞（因为工岗被占用）
                        if (!tools.judgeHotOpenBoxWorkStationAllInWorking(input.getWorkStations(), input.getOpenBoxIndexInput())){
                            //判断热砂合箱点位是否有待搬运的产品
                            //如果有，前往热砂开箱点位
                            if (input.getHotOpenBoxPosition().getStatus().equals(PositionStatusEnum.OCCUPIED)){
                                //发生前往热砂合箱点位的装载事件
                                if (hotOpenStateChange){
                                    simulationOutput = hotOpenBoxEventFlow.HotOpenBoxEmptyGoHotPosotionEventCreate(simulationOutput);

                                    hotOpenStateChange = false;
                                }else {
                                    double executionTime = Math.abs(input.getHotOpenBoxPosition().getCoordinate().getX() -
                                            input.getHotFerry().getLocationCoordinate().getX()) / input.getHotFerry().getEmptySpeed();

                                    hotOpenTime = input.getTotalTime() + executionTime;
                                    hotOpenStateChange = true;

                                    eventTime = executionTime;
                                }

//                            log.info("开箱：前往热砂点位装载" + hotOpenTime);
                            }
                        }
                    }
                }

                //子车满载
            }else {
                //判断车辆装载的产品是否是合箱完成的砂箱产品
                if (input.getHotSubCar().getProduct().getProductType().equals(ProductTypeEnum.WHOLE_SAND_SHELL_BOX)){
                    int workStationIndex = tools.judgeHotOpenBoxCarGoWhichWorkStation(input.getWorkStations(), input.getOpenBoxIndexInput());
                    //判断全部工岗是否已满
                    if (workStationIndex != -1){
                        //发生前往工岗卸载砂箱的指令
                        if (hotOpenStateChange){
                            simulationOutput = hotOpenBoxEventFlow.HotOpenBoxFullGoWorkStationEventCreate(simulationOutput, workStationIndex);

                            hotOpenStateChange = false;

                        }else {
                            double executionTime = (Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                                    input.getHotFerry().getLocationCoordinate().getX()) / input.getHotFerry().getFullSpeed()) +
                                    (Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                                            input.getHotFerry().getLocationCoordinate().getY()) / input.getHotSubCar().getFullSpeed()) +
                                    (input.getHotSubCar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                                    input.getHotFerry().getLocationCoordinate().getY()) / input.getHotSubCar().getEmptySpeed());
                            hotOpenTime = input.getTotalTime() + executionTime;
                            hotOpenStateChange = true;

                            eventTime = executionTime;
                        }

                        log.info("开箱：前往工岗卸载" + hotOpenTime);
                    }

                    //前往砂箱回流
                }else {
                    //判断母车是否到达砂箱回流的点位处
                    if (input.getHotFerry().getLocationCoordinate().getX() ==
                            outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getX()){
                        //判断点位是否被占用
                        if (outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getStatus().equals(PositionStatusEnum.UNOCCUPIED)){
                            //发生子车前往点位卸载砂箱的事件
                            if (hotOpenStateChange){
                                simulationOutput = hotOpenBoxEventFlow.HotOpenBoxFullUnLoadPositionEventCreate(simulationOutput);

                                hotOpenStateChange = false;

                            }else {
                                double executionTime = (Math.abs(input.getHotFerry().getLocationCoordinate().getY() -
                                        outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getY()) /
                                        input.getHotSubCar().getFullSpeed()) + (input.getHotSubCar().getTopRodRaiseOrFallTime()) +
                                        (Math.abs(outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getY() -
                                                input.getHotFerry().getLocationCoordinate().getY()) / input.getHotSubCar().getEmptySpeed());

                                hotOpenTime = input.getTotalTime() + executionTime;
                                hotOpenStateChange = true;

                                eventTime = executionTime;
                            }

                            log.info("开箱：在点位卸载" + hotOpenTime);
                        }

                        //前往热砂砂箱回流点位
                    }else {
                        //发生前往点位的事件
                        if (hotOpenStateChange){
                            simulationOutput = hotOpenBoxEventFlow.HotOpenBoxFullGoPositionEventCreate(simulationOutput);

                            hotOpenStateChange = false;

                        }else {
                            double executionTime = Math.abs(input.getHotFerry().getLocationCoordinate().getX() -
                                    outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getX()) /
                                    input.getHotFerry().getFullSpeed();

                            hotOpenTime = input.getTotalTime() + executionTime;
                            hotOpenStateChange = true;

                            eventTime = executionTime;
                        }

                        log.info("开箱：前往点位卸载" + hotOpenTime);
                    }
                }
            }
        }

        if (eventTime != 0.0){
            timeList[46] = eventTime;
            eventTime = 0.0;
        }

        //判断冷砂开箱是否可以发生事件
        if (input.getTotalTime() >= coldOpenTime){
            //参考热砂
            //判断冷砂开箱轨道子车是否满载
            if (allTool.judgeSubCarIsEmpty(input.getColdAndHotSubCar())){
                //参考合箱，注意开箱后产品有多个，注意热砂是下轨道
                //判断母车是否到达热砂开箱的交互点并判断点位状态是否被占用
                if(input.getColdAndHotFerry().getLocationCoordinate().getX() == input.getColdAndHotOpenBoxPosition().getCoordinate().getX() &&
                        input.getColdAndHotOpenBoxPosition().getStatus().equals(PositionStatusEnum.OCCUPIED)){
                    //进行装载的事件流
                    if (coldOpenStateChange){
                        simulationOutput = coldOpenBoxEventFlow.ColdAndHotEmptyLoadInPositionEventCreate(simulationOutput);

                        coldOpenStateChange = false;
                    }else {
                        double executionTime = (Math.abs(input.getColdAndHotOpenBoxPosition().getCoordinate().getY() -
                                input.getColdAndHotFerry().getLocationCoordinate().getY()) / input.getColdAndHotSubCar().getEmptySpeed()) +
                                (input.getColdAndHotSubCar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getColdAndHotOpenBoxPosition().getCoordinate().getY() -
                                input.getColdAndHotFerry().getLocationCoordinate().getY()) / input.getColdAndHotSubCar().getFullSpeed());
                        coldOpenTime = input.getTotalTime() + executionTime;
                        coldOpenStateChange = true;

                        eventTime = executionTime;
                    }

                    log.info("开箱：在冷砂点位装载产品" + coldOpenTime);

                }else if (tools.judgeColdAndHotOpenBoxFerryInWorkstation(input.getColdAndHotFerry(), input.getWorkStations(), input.getOpenBoxIndexInput()) != -1){
                    int workStationIndex = tools.judgeColdAndHotOpenBoxFerryInWorkstation(input.getColdAndHotFerry(), input.getWorkStations(), input.getOpenBoxIndexInput());
                    //发生装载事件
                    if (coldOpenStateChange){
                        simulationOutput = coldOpenBoxEventFlow.ColdAndHotOpenBoxEmptyLoadInWorkStationEventCreate(simulationOutput, workStationIndex, upProducts);

                        coldOpenStateChange = false;

                    }else {
                        double executionTime = (Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                                input.getColdAndHotFerry().getLocationCoordinate().getY()) / input.getColdAndHotSubCar().getEmptySpeed()) +
                                (input.getColdAndHotSubCar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                                input.getColdAndHotFerry().getLocationCoordinate().getY()) / input.getColdAndHotSubCar().getFullSpeed());

                        coldOpenTime = input.getTotalTime() + executionTime;
                        coldOpenStateChange = true;

                        eventTime = executionTime;
                    }

//                    log.info("开箱cold：在工岗装载产品" + coldOpenTime);
                }else {
                    int workStationIndex = tools.judgeColdAndHotOpenBoxWorkStationIsWaitingTransprot(input.getWorkStations(), input.getOpenBoxIndexInput());
                    //判断是否有工岗待搬运
                    if (workStationIndex != -1){
                        //发生前往工岗的事件
                        if (coldOpenStateChange){
                            simulationOutput = coldOpenBoxEventFlow.ColdAndHotOpenBoxEmptyGoWorkStationEventCreate(simulationOutput, workStationIndex);

                            coldOpenStateChange = false;

                        }else {
                            double executionTime = Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                                    input.getColdAndHotFerry().getLocationCoordinate().getX()) / input.getColdAndHotFerry().getEmptySpeed();

                            coldOpenTime = input.getTotalTime() + executionTime;
                            coldOpenStateChange = true;

                            eventTime = executionTime;
                        }

                        log.info("开箱cold：前往工岗装载" + coldOpenTime);

                        //没有待搬运的工岗
                    }else {
                        //判断4个工岗是否全部被占用，正等待工作完成，避免车辆在搬运冷热混合开箱点位的砂箱后整个开箱区堵塞（因为工岗被占用）
                        if (!tools.judgeColdOpenBoxWorkStationAllInWorking(input.getWorkStations(), input.getOpenBoxIndexInput())){
                            //判断热砂合箱点位是否有待搬运的产品
                            //如果有，前往热砂合箱点位
                            if (input.getColdAndHotOpenBoxPosition().getStatus().equals(PositionStatusEnum.OCCUPIED)){
                                //发生前往热砂合箱点位的装载事件
                                if (coldOpenStateChange){
                                    simulationOutput = coldOpenBoxEventFlow.ColdAndHotOpenBoxEmptyGoColdAndHotPosotionEventCreate(simulationOutput);

                                    coldOpenStateChange = false;
                                }else {
                                    double executionTime = Math.abs(input.getColdAndHotOpenBoxPosition().getCoordinate().getX() -
                                            input.getColdAndHotFerry().getLocationCoordinate().getX()) / input.getColdAndHotFerry().getEmptySpeed();

                                    coldOpenTime = input.getTotalTime() + executionTime;
                                    coldOpenStateChange = true;

                                    eventTime = executionTime;
                                }

//                            log.info("开箱cold：前往冷砂点位装载" + coldOpenTime);
                            }
                        }
                    }
                }

                //子车满载
            }else {
                //判断车辆装载的产品是否是合箱完成的砂箱产品
                if (input.getColdAndHotSubCar().getProduct().getProductType().equals(ProductTypeEnum.WHOLE_SAND_SHELL_BOX)){
                    int workStationIndex = tools.judgeColdAndHotOpenBoxCarGoWhichWorkStation(input.getWorkStations(), input.getOpenBoxIndexInput());
                    //判断全部工岗是否已满
                    if (workStationIndex != -1){
                        //发生前往工岗卸载砂箱的指令
                        if (coldOpenStateChange){
                            simulationOutput = coldOpenBoxEventFlow.ColdAndHotOpenBoxFullGoWorkStationEventCreate(simulationOutput, workStationIndex);

                            coldOpenStateChange = false;

                        }else {
                            double executionTime = (Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getX() -
                                    input.getColdAndHotFerry().getLocationCoordinate().getX()) / input.getColdAndHotFerry().getFullSpeed()) +
                                    (Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                                            input.getColdAndHotFerry().getLocationCoordinate().getY()) / input.getColdAndHotSubCar().getFullSpeed()) +
                                    (input.getColdAndHotSubCar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getWorkStations().get(workStationIndex).getWorkPositionCoordinate().getY() -
                                    input.getColdAndHotFerry().getLocationCoordinate().getY()) / input.getColdAndHotSubCar().getEmptySpeed());
                            coldOpenTime = input.getTotalTime() + executionTime;
                            coldOpenStateChange = true;

                            eventTime = executionTime;
                        }

                        log.info("开箱cold：前往工岗卸载" + coldOpenTime);
                    }

                    //前往砂箱回流
                }else {
                    //判断母车是否到达砂箱回流的点位处
                    if (input.getColdAndHotFerry().getLocationCoordinate().getX() ==
                            input.getColdAndHotRefluxPosition().getCoordinate().getX()){
                        //判断点位是否被占用
                        if (input.getColdAndHotRefluxPosition().getStatus().equals(PositionStatusEnum.UNOCCUPIED)){
                            //发生子车前往点位卸载砂箱的事件
                            if (coldOpenStateChange){
                                simulationOutput = coldOpenBoxEventFlow.ColdAndHotOpenBoxFullUnLoadPositionEventCreate(simulationOutput);

                                coldOpenStateChange = false;

                            }else {
                                double executionTime = (Math.abs(input.getColdAndHotFerry().getLocationCoordinate().getY() -
                                        input.getColdAndHotRefluxPosition().getCoordinate().getY()) / input.getColdAndHotSubCar().getFullSpeed()) +
                                        (input.getColdAndHotSubCar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getColdAndHotRefluxPosition().getCoordinate().getY() -
                                        input.getColdAndHotFerry().getLocationCoordinate().getY()) / input.getColdAndHotSubCar().getEmptySpeed());

                                coldOpenTime = input.getTotalTime() + executionTime;
                                coldOpenStateChange = true;

                                eventTime = executionTime;
                            }

                            log.info("开箱cold：在点位卸载" + coldOpenTime);
                        }

                        //前往热砂砂箱回流点位
                    }else {
                        //发生前往点位的事件
                        if (coldOpenStateChange){
                            simulationOutput = coldOpenBoxEventFlow.ColdAndHotOpenBoxFullGoPositionEventCreate(simulationOutput);

                            coldOpenStateChange = false;

                        }else {
                            double executionTime = Math.abs(input.getColdAndHotFerry().getLocationCoordinate().getX() -
                                    input.getColdAndHotRefluxPosition().getCoordinate().getX()) / input.getColdAndHotFerry().getFullSpeed();

                            coldOpenTime = input.getTotalTime() + executionTime;
                            coldOpenStateChange = true;

                            eventTime = executionTime;
                        }

                        log.info("开箱cold：前往点位卸载" + coldOpenTime);
                    }
                }
            }
        }

        if (eventTime != 0.0){
            timeList[47] = eventTime;
            eventTime = 0.0;
        }

        //判断铸件队列是否为空
        /*
        需要更改，更改为判断点位是否存在托盘，若没有则自动填充
         */
        if (!upProducts.isEmpty()){
            if (input.getCastingUpPosition().getStatus().equals(PositionStatusEnum.UNOCCUPIED)){
                Product product = upProducts.pop().productDeepCopy();
                input.getCastingUpPosition().setProduct(product);
                input.getCastingUpPosition().setStatus(PositionStatusEnum.OCCUPIED);
            }
        }

        if (!downProducts.isEmpty()){
            if (input.getCastingDownPosition().getStatus().equals(PositionStatusEnum.UNOCCUPIED)){
                Product product = downProducts.pop().productDeepCopy();
                input.getCastingDownPosition().setProduct(product);
                input.getCastingDownPosition().setStatus(PositionStatusEnum.OCCUPIED);
            }
        }

        //判断工岗是否可以发生事件
        if (input.getTotalTime() >= hotOpenWorkStationTime_1){
            //参考开箱
            //注意开箱后的产品问题和铸件问题
            if (input.getWorkStations().get(input.getOpenBoxIndexInput().getHotWorkStation_1()).getStatus().equals(WORKING)){
                if (hotOpenWorkStationStateChange_1){
                    simulationOutput = openBoxWorkStationEventFlow.OpenBoxWorkStationWorkingEventCreate(simulationOutput, input.getHotSubCar(), input.getHotFerry(),
                            input.getOpenBoxIndexInput().getHotWorkStation_1(), input.getOpenBoxIndexInput().getHotWorkStationInGanttIndex_1(), false);

                    hotOpenWorkStationStateChange_1 = false;

                }else {
                    double executionTime = input.getWorkStations().get(input.getOpenBoxIndexInput().getHotWorkStation_1()).getProduct().getFirst().getUnboxingTime();

                    hotOpenWorkStationTime_1 = input.getTotalTime() + executionTime;
                    hotOpenWorkStationStateChange_1 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[48] = eventTime;
            eventTime = 0.0;
        }

        if (input.getTotalTime() >= hotOpenWorkStationTime_2){
            if (input.getWorkStations().get(input.getOpenBoxIndexInput().getHotWorkStation_2()).getStatus().equals(WORKING)){
                if (hotOpenWorkStationStateChange_2){
                    simulationOutput = openBoxWorkStationEventFlow.OpenBoxWorkStationWorkingEventCreate(simulationOutput, input.getHotSubCar(), input.getHotFerry(),
                            input.getOpenBoxIndexInput().getHotWorkStation_2(), input.getOpenBoxIndexInput().getHotWorkStationInGanttIndex_2(), false);

                    hotOpenWorkStationStateChange_2 = false;

                }else {
                    double executionTime = input.getWorkStations().get(input.getOpenBoxIndexInput().getHotWorkStation_2()).getProduct().getFirst().getUnboxingTime();

                    hotOpenWorkStationTime_2 = input.getTotalTime() + executionTime;
                    hotOpenWorkStationStateChange_2 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[49] = eventTime;
            eventTime = 0.0;
        }

        if (input.getTotalTime() >= hotOpenWorkStationTime_3){
            if (input.getWorkStations().get(input.getOpenBoxIndexInput().getHotWorkStation_3()).getStatus().equals(WORKING)){
                if (hotOpenWorkStationStateChange_3){
                    simulationOutput = openBoxWorkStationEventFlow.OpenBoxWorkStationWorkingEventCreate(simulationOutput, input.getHotSubCar(), input.getHotFerry(),
                            input.getOpenBoxIndexInput().getHotWorkStation_3(), input.getOpenBoxIndexInput().getHotWorkStationInGanttIndex_3(), false);

                    hotOpenWorkStationStateChange_3 = false;

                }else {
                    double executionTime = input.getWorkStations().get(input.getOpenBoxIndexInput().getHotWorkStation_3()).getProduct().getFirst().getUnboxingTime();

                    hotOpenWorkStationTime_3 = input.getTotalTime() + executionTime;
                    hotOpenWorkStationStateChange_3 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[50] = eventTime;
            eventTime = 0.0;
        }

        if (input.getTotalTime() >= hotOpenWorkStationTime_4){
            if (input.getWorkStations().get(input.getOpenBoxIndexInput().getHotWorkStation_4()).getStatus().equals(WORKING)){
                if (hotOpenWorkStationStateChange_4){
                    simulationOutput = openBoxWorkStationEventFlow.OpenBoxWorkStationWorkingEventCreate(simulationOutput, input.getHotSubCar(), input.getHotFerry(),
                            input.getOpenBoxIndexInput().getHotWorkStation_4(), input.getOpenBoxIndexInput().getHotWorkStationInGanttIndex_4(), false);

                    hotOpenWorkStationStateChange_4 = false;

                }else {
                    double executionTime = input.getWorkStations().get(input.getOpenBoxIndexInput().getHotWorkStation_4()).getProduct().getFirst().getUnboxingTime();

                    hotOpenWorkStationTime_4 = input.getTotalTime() + executionTime;
                    hotOpenWorkStationStateChange_4 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[51] = eventTime;
            eventTime = 0.0;
        }

        if (input.getTotalTime() >= coldOpenWorkStationTime_1){
            if (input.getWorkStations().get(input.getOpenBoxIndexInput().getColdAndHotWorkStation_1()).getStatus().equals(WORKING)){
                if (coldOpenWorkStationStateChange_1){
                    simulationOutput = openBoxWorkStationEventFlow.OpenBoxWorkStationWorkingEventCreate(simulationOutput, input.getHotSubCar(), input.getHotFerry(),
                            input.getOpenBoxIndexInput().getColdAndHotWorkStation_1(), input.getOpenBoxIndexInput().getColdAndHotWorkStationInGanttIndex_1(), true);

                    coldOpenWorkStationStateChange_1 = false;

                }else {
                    double executionTime = input.getWorkStations().get(input.getOpenBoxIndexInput().getColdAndHotWorkStation_1()).getProduct().getFirst().getUnboxingTime();

                    coldOpenWorkStationTime_1 = input.getTotalTime() + executionTime;
                    coldOpenWorkStationStateChange_1 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[52] = eventTime;
            eventTime = 0.0;
        }

        if (input.getTotalTime() >= coldOpenWorkStationTime_2){
            if (input.getWorkStations().get(input.getOpenBoxIndexInput().getColdAndHotWorkStation_2()).getStatus().equals(WORKING)){
                if (coldOpenWorkStationStateChange_2){
                    simulationOutput = openBoxWorkStationEventFlow.OpenBoxWorkStationWorkingEventCreate(simulationOutput, input.getHotSubCar(), input.getHotFerry(),
                            input.getOpenBoxIndexInput().getColdAndHotWorkStation_2(), input.getOpenBoxIndexInput().getColdAndHotWorkStationInGanttIndex_2(), true);

                    coldOpenWorkStationStateChange_2 = false;

                }else {
                    double executionTime = input.getWorkStations().get(input.getOpenBoxIndexInput().getColdAndHotWorkStation_2()).getProduct().getFirst().getUnboxingTime();

                    coldOpenWorkStationTime_2 = input.getTotalTime() + executionTime;
                    coldOpenWorkStationStateChange_2 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[53] = eventTime;
            eventTime = 0.0;
        }

        if (input.getTotalTime() >= coldOpenWorkStationTime_3){
            if (input.getWorkStations().get(input.getOpenBoxIndexInput().getColdAndHotWorkStation_3()).getStatus().equals(WORKING)){
                if (coldOpenWorkStationStateChange_3){
                    simulationOutput = openBoxWorkStationEventFlow.OpenBoxWorkStationWorkingEventCreate(simulationOutput, input.getHotSubCar(), input.getHotFerry(),
                            input.getOpenBoxIndexInput().getColdAndHotWorkStation_3(), input.getOpenBoxIndexInput().getColdAndHotWorkStationInGanttIndex_3(), true);

                    coldOpenWorkStationStateChange_3 = false;

                }else {
                    double executionTime = input.getWorkStations().get(input.getOpenBoxIndexInput().getColdAndHotWorkStation_3()).getProduct().getFirst().getUnboxingTime();

                    coldOpenWorkStationTime_3 = input.getTotalTime() + executionTime;
                    coldOpenWorkStationStateChange_3 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[54] = eventTime;
            eventTime = 0.0;
        }

        if (input.getTotalTime() >= coldOpenWorkStationTime_4){
            if (input.getWorkStations().get(input.getOpenBoxIndexInput().getColdAndHotWorkStation_4()).getStatus().equals(WORKING)){
                if (coldOpenWorkStationStateChange_4){
                    simulationOutput = openBoxWorkStationEventFlow.OpenBoxWorkStationWorkingEventCreate(simulationOutput, input.getHotSubCar(), input.getHotFerry(),
                            input.getOpenBoxIndexInput().getColdAndHotWorkStation_4(), input.getOpenBoxIndexInput().getColdAndHotWorkStationInGanttIndex_4(), true);

                    coldOpenWorkStationStateChange_4 = false;

                }else {
                    double executionTime = input.getWorkStations().get(input.getOpenBoxIndexInput().getColdAndHotWorkStation_4()).getProduct().getFirst().getUnboxingTime();

                    coldOpenWorkStationTime_4 = input.getTotalTime() + executionTime;
                    coldOpenWorkStationStateChange_4 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[55] = eventTime;
            eventTime = 0.0;
        }

        //判断铸件上轨道是否可以发生事件
        //未考虑碰撞
        if (input.getTotalTime() >= castingUpTime){
            //判断铸件回流上轨道子车是否满载
            if (allTool.judgeSubCarIsEmpty(input.getCastingUpSubCar())){
                //判断铸件回流点位是否有产品占用
                if (input.getCastingUpPosition().getStatus().equals(PositionStatusEnum.OCCUPIED)){
                    //前往铸件回流点位，并进行装载
                    if (castingUpStateChange){
                        simulationOutput = castingUpEventFlow.EmptyCastingUpGoPositionEventCreate(simulationOutput);

                        castingUpStateChange = false;
                    }else {
                        double executionTime = (Math.abs(input.getCastingUpSubCar().getLocationCoordinate().getX() - input.getCastingUpPosition().getCoordinate().getX())
                                / input.getCastingUpSubCar().getEmptySpeed()) + input.getCastingUpSubCar().getTopRodRaiseOrFallTime();

                        castingUpTime = input.getTotalTime() + executionTime;

                        castingUpStateChange = true;

                        eventTime = executionTime;
                    }

                    log.info("castingUpStateChange: 子车发生前往铸件回流点位装载的事件" + castingUpTime);
                }else {
                    //判断子车是否在轨道中点位置，不在则前往中点位置
                    if (input.getCastingUpSubCar().getLocationCoordinate().getX() !=
                            (input.getCastingUpPosition().getCoordinate().getX() + input.getCastingVerticalPosition().getCoordinate().getX()) / 2){
                        Coordinate waitCoordinate = new Coordinate((input.getCastingUpPosition().getCoordinate().getX() + input.getCastingVerticalPosition().getCoordinate().getX()) / 2,
                                input.getCastingUpPosition().getCoordinate().getY());
                        //前往轨道中点位置
                        if (castingUpStateChange){
                            simulationOutput = castingUpEventFlow.EmptyCastingUpGoWaitPositionEventCreate(simulationOutput, waitCoordinate);

                            castingUpStateChange = false;
                        }else {
                            double executionTime = Math.abs(input.getCastingUpSubCar().getLocationCoordinate().getX() - waitCoordinate.getX())
                                    / input.getCastingUpSubCar().getEmptySpeed();

                            castingUpTime = input.getTotalTime() + executionTime;

                            castingUpStateChange = true;

                            eventTime = executionTime;
                        }

//                        log.info("castingUpStateChange: 前往轨道中点位置处" + castingUpTime);
                    }
                }
                //子车满载
            }else {
                Coordinate waitCoordinate = new Coordinate(input.getCastingVerticalPosition().getCoordinate().getX() -
                        (input.getColdAndHotOpenBoxPosition().getCoordinate().getY() - input.getColdAndHotOpenBoxPath().getStartCoordinate().getY()),
                        input.getCastingUpSubCar().getLocationCoordinate().getY());
                //判断子车是否到达等待点位
                if (input.getCastingUpSubCar().getLocationCoordinate().equals(waitCoordinate)){
                    //判断垂直轨道点位是否被占用
                    if (input.getCastingVerticalPosition().getStatus().equals(PositionStatusEnum.UNOCCUPIED)){
                        //发生前往垂直轨道点位放置铸件的事件
                        if (castingUpStateChange){
                            //前往回流点前的等待位置处
                            simulationOutput = castingUpEventFlow.FullCastingUpGoPositionEventCreate(simulationOutput);

                            castingUpStateChange = false;
                        }else {
                            double executionTime = (Math.abs(input.getCastingUpSubCar().getLocationCoordinate().getX() - input.getCastingVerticalPosition().getCoordinate().getX())
                                    / input.getCastingUpSubCar().getFullSpeed()) + input.getCastingUpSubCar().getTopRodRaiseOrFallTime();

                            castingUpTime = input.getTotalTime() + executionTime;

                            castingUpStateChange = true;

                            eventTime = executionTime;
                        }

                        log.info("castingUpStateChange: 发生前往垂直轨道点位放置铸件的事件" + castingUpTime);
                    }
                }else {
                    //发生前往等待点的事件
                    if (castingUpStateChange){
                        //前往回流点前的等待位置处
                        simulationOutput = castingUpEventFlow.FullCastingUpGoWaitPositionEventCreate(simulationOutput, waitCoordinate);

                        castingUpStateChange = false;
                    }else {
                        double executionTime = Math.abs(input.getCastingUpSubCar().getLocationCoordinate().getX() - waitCoordinate.getX())
                                / input.getCastingUpSubCar().getFullSpeed();

                        castingUpTime = input.getTotalTime() + executionTime;

                        castingUpStateChange = true;

                        eventTime = executionTime;
                    }

                    log.info("castingUpStateChange: 发生满载前往等待点的事件" + castingUpTime);
                }
            }
        }

        if (eventTime != 0.0){
            timeList[56] = eventTime;
            eventTime = 0.0;
        }

        //判断铸件下轨道是否可以发生事件
        //未考虑碰撞
        if (input.getTotalTime() >= castingDownTime){
            //判断铸件回流下轨道子车是否满载
            if (allTool.judgeSubCarIsEmpty(input.getCastingDownSubCar())){
                //判断铸件回流点位是否有产品占用
                if (input.getCastingDownPosition().getStatus().equals(PositionStatusEnum.OCCUPIED)){
                    //前往铸件回流点位，并进行装载
                    if (castingDownStateChange){
                        //前往开模点装载的事件
                        simulationOutput = castingDownEventFlow.EmptyCastingDownGoPositionEventCreate(simulationOutput);

                        castingDownStateChange = false;
                    }else {
                        double executionTime = (Math.abs(input.getCastingDownSubCar().getLocationCoordinate().getX() - input.getCastingDownPosition().getCoordinate().getX())
                                / input.getCastingDownSubCar().getEmptySpeed()) + input.getCastingDownSubCar().getTopRodRaiseOrFallTime();

                        castingDownTime = input.getTotalTime() + executionTime;

                        castingDownStateChange = true;

                        eventTime = executionTime;
                    }

                    log.info("castingDownStateChange: 前往铸件回流点位，并进行装载" + castingDownTime);
                }else {
                    //判断子车是否在轨道中点位置，不在则前往中点位置
                    if (input.getCastingDownSubCar().getLocationCoordinate().getX() !=
                            (input.getCastingDownPosition().getCoordinate().getX() + input.getCastingShippingPosition().getCoordinate().getX()) / 2){
                        //前往轨道中点位置
                        Coordinate waitCoordinate = new Coordinate((input.getCastingDownPosition().getCoordinate().getX() + input.getCastingShippingPosition().getCoordinate().getX()) / 2,
                                input.getCastingDownPosition().getCoordinate().getY());
                        //前往轨道中点位置
                        if (castingDownStateChange){
                            simulationOutput = castingDownEventFlow.EmptyCastingDownGoWaitPositionEventCreate(simulationOutput, waitCoordinate);

                            castingDownStateChange = false;
                        }else {
                            double executionTime = Math.abs(input.getCastingDownSubCar().getLocationCoordinate().getX() - waitCoordinate.getX())
                                    / input.getCastingDownSubCar().getEmptySpeed();

                            castingDownTime = input.getTotalTime() + executionTime;

                            castingDownStateChange = true;

                            eventTime = executionTime;
                        }

//                        log.info("castingDownStateChange: 前往轨道中点位置" + castingDownTime);
                    }
                }
                //子车满载
            }else {
                Coordinate waitCoordinate = new Coordinate(input.getCastingShippingPosition().getCoordinate().getX() -
                        (input.getColdAndHotOpenBoxPosition().getCoordinate().getY() - input.getColdAndHotOpenBoxPath().getStartCoordinate().getY()),
                        input.getCastingDownSubCar().getLocationCoordinate().getY());
                //判断子车是否到达等待点位
                if (input.getCastingDownSubCar().getLocationCoordinate().equals(waitCoordinate)){
                    //判断传输点位是否被占用
                    if (input.getCastingShippingPosition().getStatus().equals(PositionStatusEnum.UNOCCUPIED)){
                        //发生前往传输点位放置铸件的事件
                        if (castingDownStateChange){
                            //前往回流点前的等待位置处
                            simulationOutput = castingDownEventFlow.FullCastingDownGoPositionEventCreate(simulationOutput);

                            castingDownStateChange = false;
                        }else {
                            double executionTime = (Math.abs(input.getCastingDownSubCar().getLocationCoordinate().getX() - input.getCastingVerticalPosition().getCoordinate().getX())
                                    / input.getCastingDownSubCar().getFullSpeed()) + input.getCastingDownSubCar().getTopRodRaiseOrFallTime();

                            castingDownTime = input.getTotalTime() + executionTime;

                            castingDownStateChange = true;

                            eventTime = executionTime;
                        }

                        log.info("castingDownStateChange: 发生前往传输点位放置铸件的事件" + castingDownTime);
                    }
                }else {
                    //发生前往等待点的事件
                    if (castingDownStateChange){
                        //前往回流点前的等待位置处
                        simulationOutput = castingDownEventFlow.FullCastingDownGoWaitPositionEventCreate(simulationOutput, waitCoordinate);

                        castingDownStateChange = false;
                    }else {
                        double executionTime = Math.abs(input.getCastingDownSubCar().getLocationCoordinate().getX() - waitCoordinate.getX())
                                / input.getCastingDownSubCar().getFullSpeed();

                        castingDownTime = input.getTotalTime() + executionTime;

                        castingDownStateChange = true;

                        eventTime = executionTime;
                    }

                    log.info("castingDownStateChange: 发生满载前往等待点的事件" + castingDownTime);
                }
            }
        }

        if (eventTime != 0.0){
            timeList[57] = eventTime;
            eventTime = 0.0;
        }

        //判断铸件垂直轨道是否可以发生事件
        //未考虑碰撞
        if (input.getTotalTime() >= castingVerticalTime){
            //判断铸件回流下轨道子车是否满载
            if (allTool.judgeSubCarIsEmpty(input.getCastingVerticalSubCar())){
                //判断铸件回流点位是否有产品占用
                if (input.getCastingVerticalPosition().getStatus().equals(PositionStatusEnum.OCCUPIED)){
                    //前往铸件回流点位，并进行装载
                    //注意此为垂直轨道！！！
                    if (castingVerticalStateChange){
                        //前往开模点装载的事件
                        simulationOutput = castingVerticalEventFlow.EmptyCastingVerticalGoPositionEventCreate(simulationOutput);

                        castingVerticalStateChange = false;
                    }else {
                        double executionTime = (Math.abs(input.getCastingVerticalSubCar().getLocationCoordinate().getY() - input.getCastingVerticalPosition().getCoordinate().getY())
                                / input.getCastingVerticalSubCar().getEmptySpeed()) + input.getCastingVerticalSubCar().getTopRodRaiseOrFallTime();

                        castingVerticalTime = input.getTotalTime() + executionTime;

                        castingVerticalStateChange = true;

                        eventTime = executionTime;
                    }

                    log.info("castingVerticalStateChange: 前往铸件回流点位，并进行装载" + castingVerticalTime);
                }else {
                    //判断子车是否在轨道中点位置，不在则前往中点位置
                    if (input.getCastingVerticalSubCar().getLocationCoordinate().getY() !=
                            (input.getCastingVerticalPosition().getCoordinate().getY() + input.getCastingShippingPosition().getCoordinate().getY()) / 2){
                        //前往轨道中点位置
                        Coordinate waitCoordinate = new Coordinate(input.getCastingVerticalSubCar().getLocationCoordinate().getX(),
                                (input.getCastingVerticalPosition().getCoordinate().getY() + input.getCastingShippingPosition().getCoordinate().getY()) / 2);
                        //前往轨道中点位置
                        if (castingVerticalStateChange){
                            //前往回流点前的等待位置处
                            simulationOutput = castingVerticalEventFlow.EmptyCastingVerticalGoWaitPositionEventCreate(simulationOutput, waitCoordinate);

                            castingVerticalStateChange = false;
                        }else {
                            double executionTime = Math.abs(input.getCastingVerticalSubCar().getLocationCoordinate().getY() - waitCoordinate.getY())
                                    / input.getCastingVerticalSubCar().getEmptySpeed();

                            castingVerticalTime = input.getTotalTime() + executionTime;

                            castingVerticalStateChange = true;

                            eventTime = executionTime;
                        }

//                        log.info("castingVerticalStateChange: 前往轨道中点位置" + castingVerticalTime);
                    }
                }
                //子车满载
            }else {
                Coordinate waitCoordinate = new Coordinate(input.getCastingVerticalSubCar().getLocationCoordinate().getX(),
                        (input.getCastingVerticalPosition().getCoordinate().getY() + input.getCastingShippingPosition().getCoordinate().getY()) / 2);
                //判断子车是否到达等待点位
                if (input.getCastingVerticalSubCar().getLocationCoordinate().equals(waitCoordinate)){
                    //判断传输点位是否被占用
                    if (input.getCastingShippingPosition().getStatus().equals(PositionStatusEnum.UNOCCUPIED)){
                        //发生前往传输点位放置铸件的事件
                        if (castingVerticalStateChange){
                            //前往回流点前的等待位置处
                            simulationOutput = castingVerticalEventFlow.FullCastingVerticalGoPositionEventCreate(simulationOutput);

                            castingVerticalStateChange = false;
                        }else {
                            double executionTime = (Math.abs(input.getCastingVerticalSubCar().getLocationCoordinate().getY() - input.getCastingShippingPosition().getCoordinate().getY())
                                    / input.getCastingVerticalSubCar().getFullSpeed()) + input.getCastingVerticalSubCar().getTopRodRaiseOrFallTime();

                            castingVerticalTime = input.getTotalTime() + executionTime;

                            castingVerticalStateChange = true;

                            eventTime = executionTime;
                        }

                        log.info("castingVerticalStateChange: 发生前往传输点位放置铸件的事件" + castingVerticalTime);
                    }
                }else {
                    //发生前往等待点的事件
                    if (castingVerticalStateChange){
                        //前往回流点前的等待位置处
                        simulationOutput = castingVerticalEventFlow.FullCastingVerticalGoWaitPositionEventCreate(simulationOutput, waitCoordinate);

                        castingVerticalStateChange = false;
                    }else {
                        double executionTime = Math.abs(input.getCastingVerticalSubCar().getLocationCoordinate().getY() - waitCoordinate.getY())
                                / input.getCastingVerticalSubCar().getFullSpeed();

                        castingVerticalTime = input.getTotalTime() + executionTime;

                        castingVerticalStateChange = true;

                        eventTime = executionTime;
                    }

                    log.info("castingVerticalStateChange: 发生满载前往等待点的事件" + castingVerticalTime);
                }
            }
        }

        if (eventTime != 0.0){
            timeList[58] = eventTime;
            eventTime = 0.0;
        }

        //判断砂箱回流轨道是否可以发生事件
        if (input.getTotalTime() >= sandBoxRefluxTime){
            //判断铸件回流下轨道子车是否满载
            if (allTool.judgeSubCarIsEmpty(input.getSandBoxRefluxRightSubCar())){
                //判断铸件回流点位是否有产品占用
                if (input.getColdAndHotRefluxPosition().getStatus().equals(PositionStatusEnum.OCCUPIED)){
                    //前往铸件回流点位，并进行装载
                    //注意此为垂直轨道！！！
                    if (sandBoxRefluxStateChange){
                        //前往开模点装载的事件
                        simulationOutput = sandBoxRefluxEventFlow.EmptySandBoxRefluxGoPositionEventCreate(simulationOutput);

                        sandBoxRefluxStateChange = false;
                    }else {
                        double executionTime = (Math.abs(input.getSandBoxRefluxRightSubCar().getLocationCoordinate().getY() - input.getColdAndHotRefluxPosition().getCoordinate().getY())
                                / input.getSandBoxRefluxRightSubCar().getEmptySpeed()) + input.getSandBoxRefluxRightSubCar().getTopRodRaiseOrFallTime();

                        sandBoxRefluxTime = input.getTotalTime() + executionTime;

                        sandBoxRefluxStateChange = true;

                        eventTime = executionTime;
                    }

                    log.info("sandBoxRefluxStateChange: 前往铸件回流点位，并进行装载" + sandBoxRefluxTime);
                }else {
                    //判断子车是否在轨道中点位置，不在则前往中点位置
                    if (input.getSandBoxRefluxRightSubCar().getLocationCoordinate().getY() !=
                            (input.getColdAndHotRefluxPosition().getCoordinate().getY() +
                                    outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getY()) / 2){
                        //前往轨道中点位置
                        Coordinate waitCoordinate = new Coordinate(input.getSandBoxRefluxRightSubCar().getLocationCoordinate().getX(),
                                (input.getColdAndHotRefluxPosition().getCoordinate().getY() +
                                        outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getY()) / 2);
                        if (sandBoxRefluxStateChange){
                            //前往回流点前的等待位置处
                            simulationOutput = sandBoxRefluxEventFlow.EmptySandBoxRefluxGoWaitPositionEventCreate(simulationOutput, waitCoordinate);

                            sandBoxRefluxStateChange = false;
                        }else {
                            double executionTime = Math.abs(input.getSandBoxRefluxRightSubCar().getLocationCoordinate().getY() - waitCoordinate.getY())
                                    / input.getSandBoxRefluxRightSubCar().getEmptySpeed();

                            sandBoxRefluxTime = input.getTotalTime() + executionTime;

                            sandBoxRefluxStateChange = true;

                            eventTime = executionTime;
                        }

//                        log.info("sandBoxRefluxStateChange: 前往回流点前的等待位置处" + sandBoxRefluxTime);
                    }
                }
                //子车满载
            }else {
                Coordinate waitCoordinate = new Coordinate(input.getSandBoxRefluxRightSubCar().getLocationCoordinate().getX(),
                        outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getY() -
                                (outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getY() -
                                        input.getCastingDownPath().getStartCoordinate().getY()));
                //判断子车是否到达等待点位
                if (input.getSandBoxRefluxRightSubCar().getLocationCoordinate().equals(waitCoordinate)){
                    //判断传输点位是否被占用
                    if (outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getStatus().equals(PositionStatusEnum.UNOCCUPIED)){
                        //发生前往传输点位放置铸件的事件
                        if (sandBoxRefluxStateChange){
                            simulationOutput = sandBoxRefluxEventFlow.FullSandBoxRefluxGoPositionEventCreate(simulationOutput);

                            sandBoxRefluxStateChange = false;
                        }else {
                            double executionTime = (Math.abs(input.getSandBoxRefluxRightSubCar().getLocationCoordinate().getY() -
                                    outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getY())
                                    / input.getSandBoxRefluxRightSubCar().getFullSpeed()) + input.getSandBoxRefluxRightSubCar().getTopRodRaiseOrFallTime();

                            sandBoxRefluxTime = input.getTotalTime() + executionTime;

                            sandBoxRefluxStateChange = true;

                            eventTime = executionTime;
                        }

                        log.info("sandBoxRefluxStateChange: 发生前往传输点位放置铸件的事件" + sandBoxRefluxTime);
                    }
                }else {
                    //发生前往等待点的事件
                    if (sandBoxRefluxStateChange){
                        //前往回流点前的等待位置处
                        simulationOutput = sandBoxRefluxEventFlow.FullSandBoxRefluxGoWaitPositionEventCreate(simulationOutput, waitCoordinate);

                        sandBoxRefluxStateChange = false;
                    }else {
                        double executionTime = Math.abs(input.getSandBoxRefluxRightSubCar().getLocationCoordinate().getY() - waitCoordinate.getY())
                                / input.getSandBoxRefluxRightSubCar().getFullSpeed();

                        sandBoxRefluxTime = input.getTotalTime() + executionTime;

                        sandBoxRefluxStateChange = true;

                        eventTime = executionTime;
                    }

                    log.info("sandBoxRefluxStateChange: 发生满载前往等待点的事件" + sandBoxRefluxTime);
                }
            }
        }

        if (eventTime != 0.0){
            timeList[59] = eventTime;
            eventTime = 0.0;
        }

        return simulationOutput;
    }
}
