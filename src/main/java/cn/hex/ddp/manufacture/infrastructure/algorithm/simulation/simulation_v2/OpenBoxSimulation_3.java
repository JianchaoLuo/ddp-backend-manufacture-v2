package cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.simulation_v2;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.openBox.*;
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
 * @Created: 2024/8/1 0:08
 */
@Slf4j
public class OpenBoxSimulation_3 {
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
    private final Deque<Integer> upWorkStationIndex = new ArrayDeque<>();
    private final Deque<Integer> downWorkStationIndex = new ArrayDeque<>();
    /**
     * 铸件区的铸件队列
     */

    /**
     * 枚举类，用于查找当前执行的事件，从而进行当前事件的资源状态的改变，避免出错
     */
    private HotOpenBoxEnum hotOpenBoxEnum;
    private ColdAndHotOpenBoxEnum coldAndHotOpenBoxEnum;
    private CastingUpEnum castingUpEnum;
    private CastingDownEnum castingDownEnum;
    private CastingVerticalEnum castingVerticalEnum;
    private SandBoxRefluxEnum sandBoxRefluxEnum;

    private double eventTime = 0.0;

    int hotWorkStationIndex = -1;
    int coldWorkStationIndex = -1;

    public AfterClosingBoxAllLink OpenBoxSimulationRun(AfterClosingBoxAllLink afterClosingBoxAllLink, Double[] timeList){
        AfterClosingBoxAllLink simulationOutput = SerializationUtils.clone(afterClosingBoxAllLink);
        OpenBoxSimulationInput input = simulationOutput.getOpenBoxInput().getInput();
        OutBoundSimulationInput outBoundInput = simulationOutput.getOutBoundInput().getInput();

        //判断热砂开箱是否可以发生事件
        if (input.getTotalTime() >= hotOpenTime){
            if (!hotOpenStateChange){
                //判断热砂开箱轨道子车是否满载
                if (allTool.judgeSubCarIsEmpty(input.getHotSubCar())){
                    //参考合箱，注意开箱后产品有多个，注意热砂是下轨道
                    //判断母车是否到达热砂开箱的交互点并判断点位状态是否被占用
                    if(input.getHotFerry().getLocationCoordinate().getX() == input.getHotOpenBoxPosition().getCoordinate().getX() &&
                            input.getHotOpenBoxPosition().getStatus().equals(PositionStatusEnum.OCCUPIED)){
                        //进行装载的事件流
                        //计算事件的时间
                        double executionTime = (Math.abs(input.getHotOpenBoxPosition().getCoordinate().getY() -
                                input.getHotFerry().getLocationCoordinate().getY()) / input.getHotSubCar().getEmptySpeed()) +
                                (input.getHotSubCar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getHotOpenBoxPosition().getCoordinate().getY() -
                                input.getHotFerry().getLocationCoordinate().getY()) / input.getHotSubCar().getFullSpeed());

                        hotOpenTime = input.getTotalTime() + executionTime;
                        hotOpenStateChange = true;
                        hotOpenBoxEnum = HotOpenBoxEnum.EMPTY_SUB_CAR_GO_HOT_OPEN_BOX_POSITION_LOAD;
                        eventTime = executionTime;

                    }else if (tools.judgeHotOpenBoxFerryInWorkstation(input.getHotFerry(), input.getWorkStations(), input.getOpenBoxIndexInput()) != -1){
                        hotWorkStationIndex = tools.judgeHotOpenBoxFerryInWorkstation(input.getHotFerry(), input.getWorkStations(), input.getOpenBoxIndexInput());
                        //发生前往工岗装载回流砂箱的事件
                        //计算事件的时间
                        double executionTime = (Math.abs(input.getWorkStations().get(hotWorkStationIndex).getWorkPositionCoordinate().getY() -
                                input.getHotFerry().getLocationCoordinate().getY()) / input.getHotSubCar().getEmptySpeed()) +
                                (input.getHotSubCar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getWorkStations().get(hotWorkStationIndex).getWorkPositionCoordinate().getY() -
                                input.getHotFerry().getLocationCoordinate().getY()) / input.getHotSubCar().getFullSpeed());

                        hotOpenTime = input.getTotalTime() + executionTime;
                        hotOpenStateChange = true;
                        hotOpenBoxEnum = HotOpenBoxEnum.EMPTY_SUB_CAR_GO_HOT_OPEN_WORK_STATION_LOAD;
                        eventTime = executionTime;

                    }else {
                        hotWorkStationIndex = tools.judgeHotOpenBoxWorkStationIsWaitingTransprot(input.getWorkStations(), input.getOpenBoxIndexInput());
                        //判断是否有工岗待搬运
                        if (hotWorkStationIndex != -1){
                            //发生母车前往工岗的事件
                            //计算事件的时间
                            double executionTime = Math.abs(input.getWorkStations().get(hotWorkStationIndex).getWorkPositionCoordinate().getX() -
                                    input.getHotFerry().getLocationCoordinate().getX()) / input.getHotFerry().getEmptySpeed();

                            hotOpenTime = input.getTotalTime() + executionTime;
                            hotOpenStateChange = true;
                            hotOpenBoxEnum = HotOpenBoxEnum.EMPTY_FERRY_GO_HOT_OPEN_WORK_STATION;
                            eventTime = executionTime;

                            //没有待搬运的工岗
                        }else {
                            //判断4个工岗是否全部被占用，正等待工作完成，避免车辆在搬运热砂开箱点位的砂箱后整个开箱区堵塞（因为工岗被占用）
                            if (!tools.judgeHotOpenBoxWorkStationAllInWorking(input.getWorkStations(), input.getOpenBoxIndexInput())){
                                //判断热砂合箱点位是否有待搬运的产品
                                //如果有，前往热砂开箱点位
                                if (input.getHotOpenBoxPosition().getStatus().equals(PositionStatusEnum.OCCUPIED)){
                                    //发生前往热砂合箱点位的装载事件
                                    //计算事件的时间
                                    double executionTime = Math.abs(input.getHotOpenBoxPosition().getCoordinate().getX() -
                                            input.getHotFerry().getLocationCoordinate().getX()) / input.getHotFerry().getEmptySpeed();

                                    hotOpenTime = input.getTotalTime() + executionTime;
                                    hotOpenStateChange = true;
                                    hotOpenBoxEnum = HotOpenBoxEnum.EMPTY_FERRY_GO_HOT_OPEN_POSITION;
                                    eventTime = executionTime;

                                }
                            }
                        }
                    }

                    //子车满载
                }else {
                    //判断车辆装载的产品是否是合箱完成的砂箱产品
                    if (input.getHotSubCar().getProduct().getProductType().equals(ProductTypeEnum.WHOLE_SAND_SHELL_BOX)){
                        hotWorkStationIndex = tools.judgeHotOpenBoxCarGoWhichWorkStation(input.getWorkStations(), input.getOpenBoxIndexInput());
                        //判断全部工岗是否已满
                        if (hotWorkStationIndex != -1){
                            //发生前往工岗卸载砂箱的指令
                            double executionTime = (Math.abs(input.getWorkStations().get(hotWorkStationIndex).getWorkPositionCoordinate().getX() -
                                    input.getHotFerry().getLocationCoordinate().getX()) / input.getHotFerry().getFullSpeed()) +
                                    (Math.abs(input.getWorkStations().get(hotWorkStationIndex).getWorkPositionCoordinate().getY() -
                                            input.getHotFerry().getLocationCoordinate().getY()) / input.getHotSubCar().getFullSpeed()) +
                                    (input.getHotSubCar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getWorkStations().get(hotWorkStationIndex).getWorkPositionCoordinate().getY() -
                                    input.getHotFerry().getLocationCoordinate().getY()) / input.getHotSubCar().getEmptySpeed());

                            hotOpenTime = input.getTotalTime() + executionTime;
                            hotOpenStateChange = true;
                            hotOpenBoxEnum = HotOpenBoxEnum.FULL_SUB_CAR_GO_HOT_OPEN_WORK_STATION_LAND;
                            eventTime = executionTime;

                        }
                        //前往砂箱回流
                    }else {
                        //判断母车是否到达砂箱回流的点位处
                        if (input.getHotFerry().getLocationCoordinate().getX() ==
                                outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getX()){
                            //判断点位是否被占用
                            if (outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getStatus().equals(PositionStatusEnum.UNOCCUPIED)){
                                //发生子车前往点位卸载砂箱的事件
                                double executionTime = (Math.abs(input.getHotFerry().getLocationCoordinate().getY() -
                                        outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getY()) /
                                        input.getHotSubCar().getFullSpeed()) + (input.getHotSubCar().getTopRodRaiseOrFallTime()) +
                                        (Math.abs(outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getY() -
                                                input.getHotFerry().getLocationCoordinate().getY()) / input.getHotSubCar().getEmptySpeed());

                                hotOpenTime = input.getTotalTime() + executionTime;
                                hotOpenStateChange = true;
                                hotOpenBoxEnum = HotOpenBoxEnum.FULL_SUB_CAR_GO_HOT_SAND_REFLUX_POSITION_LAND;
                                eventTime = executionTime;

                            }
                            //前往热砂砂箱回流点位
                        }else {
                            //发生热砂砂箱回流点位的事件
                            double executionTime = Math.abs(input.getHotFerry().getLocationCoordinate().getX() -
                                    outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getX()) /
                                    input.getHotFerry().getFullSpeed();

                            hotOpenTime = input.getTotalTime() + executionTime;
                            hotOpenStateChange = true;
                            hotOpenBoxEnum = HotOpenBoxEnum.FULL_FERRY_GO_HOT_SAND_REFLUX_POSITION;
                            eventTime = executionTime;

                        }
                    }
                }
            }else {
                switch (hotOpenBoxEnum){
                    case EMPTY_SUB_CAR_GO_HOT_OPEN_BOX_POSITION_LOAD:
                        //进行子车前往热砂开箱点位装载的事件流
                        simulationOutput = hotOpenBoxEventFlow.HotEmptyLoadInPositionEventCreate(simulationOutput);

                        hotOpenStateChange = false;
                        hotOpenBoxEnum = null;
                        log.info("开箱：在热砂点位装载产品" + hotOpenTime);
                        break;
                    case EMPTY_SUB_CAR_GO_HOT_OPEN_WORK_STATION_LOAD:
                        //发生前往工岗装载回流砂箱的事件
                        simulationOutput = hotOpenBoxEventFlow.HotOpenBoxEmptyLoadInWorkStationEventCreate_2(simulationOutput, hotWorkStationIndex, downProducts, downWorkStationIndex);

                        hotOpenStateChange = false;
                        hotOpenBoxEnum = null;
//                        log.info("开箱：在工岗装载产品" + hotOpenTime);
                        break;
                    case EMPTY_FERRY_GO_HOT_OPEN_WORK_STATION:
                        //发生母车前往工岗的事件
                        simulationOutput = hotOpenBoxEventFlow.HotOpenBoxEmptyGoWorkStationEventCreate(simulationOutput, hotWorkStationIndex);

                        hotOpenStateChange = false;
                        hotOpenBoxEnum = null;
                        log.info("开箱：前往工岗装载" + hotOpenTime);
                        break;
                    case EMPTY_FERRY_GO_HOT_OPEN_POSITION:
                        //发生前往热砂合箱点位的装载事件
                        simulationOutput = hotOpenBoxEventFlow.HotOpenBoxEmptyGoHotPosotionEventCreate(simulationOutput);

                        hotOpenStateChange = false;
                        hotOpenBoxEnum = null;
//                        log.info("开箱：前往热砂点位装载" + hotOpenTime);
                        break;
                    case FULL_SUB_CAR_GO_HOT_OPEN_WORK_STATION_LAND:
                        //发生子车前往热砂开箱工岗卸载砂箱的指令
                        simulationOutput = hotOpenBoxEventFlow.HotOpenBoxFullGoWorkStationEventCreate(simulationOutput, hotWorkStationIndex);

                        hotOpenStateChange = false;
                        hotOpenBoxEnum = null;
                        log.info("开箱：前往工岗卸载" + hotOpenTime);
                        break;
                    case FULL_SUB_CAR_GO_HOT_SAND_REFLUX_POSITION_LAND:
                        //发生子车前往热砂砂箱回流点位卸载砂箱的事件
                        simulationOutput = hotOpenBoxEventFlow.HotOpenBoxFullUnLoadPositionEventCreate(simulationOutput);

                        hotOpenStateChange = false;
                        hotOpenBoxEnum = null;
                        log.info("开箱：在点位卸载" + hotOpenTime);
                        break;
                    case FULL_FERRY_GO_HOT_SAND_REFLUX_POSITION:
                        //发生母车前往热砂砂箱回流点位的事件
                        simulationOutput = hotOpenBoxEventFlow.HotOpenBoxFullGoPositionEventCreate(simulationOutput);

                        hotOpenStateChange = false;
                        hotOpenBoxEnum = null;
                        log.info("开箱：前往点位卸载" + hotOpenTime);
                        break;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[46] = eventTime;
            eventTime = 0.0;
        }

        //参考热砂
        //判断冷砂开箱是否可以发生事件
        if (input.getTotalTime() >= coldOpenTime){
            if (!coldOpenStateChange){
                //判断冷砂开箱轨道子车是否满载
                if (allTool.judgeSubCarIsEmpty(input.getColdAndHotSubCar())){
                    //参考合箱，注意开箱后产品有多个，注意热砂是下轨道
                    //判断母车是否到达热砂开箱的交互点并判断点位状态是否被占用
                    if(input.getColdAndHotFerry().getLocationCoordinate().getX() == input.getColdAndHotOpenBoxPosition().getCoordinate().getX() &&
                            input.getColdAndHotOpenBoxPosition().getStatus().equals(PositionStatusEnum.OCCUPIED)){
                        //进行装载的事件流
                        double executionTime = (Math.abs(input.getColdAndHotOpenBoxPosition().getCoordinate().getY() -
                                input.getColdAndHotFerry().getLocationCoordinate().getY()) / input.getColdAndHotSubCar().getEmptySpeed()) +
                                (input.getColdAndHotSubCar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getColdAndHotOpenBoxPosition().getCoordinate().getY() -
                                input.getColdAndHotFerry().getLocationCoordinate().getY()) / input.getColdAndHotSubCar().getFullSpeed());
                        coldOpenTime = input.getTotalTime() + executionTime;
                        coldOpenStateChange = true;
                        coldAndHotOpenBoxEnum = ColdAndHotOpenBoxEnum.EMPTY_SUB_CAR_GO_COLD_OPEN_BOX_POSITION_LOAD;
                        eventTime = executionTime;

                    }else if (tools.judgeColdAndHotOpenBoxFerryInWorkstation(input.getColdAndHotFerry(), input.getWorkStations(), input.getOpenBoxIndexInput()) != -1){
                        coldWorkStationIndex = tools.judgeColdAndHotOpenBoxFerryInWorkstation(input.getColdAndHotFerry(), input.getWorkStations(), input.getOpenBoxIndexInput());
                        //发生装载事件
                        double executionTime = (Math.abs(input.getWorkStations().get(coldWorkStationIndex).getWorkPositionCoordinate().getY() -
                                input.getColdAndHotFerry().getLocationCoordinate().getY()) / input.getColdAndHotSubCar().getEmptySpeed()) +
                                (input.getColdAndHotSubCar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getWorkStations().get(coldWorkStationIndex).getWorkPositionCoordinate().getY() -
                                input.getColdAndHotFerry().getLocationCoordinate().getY()) / input.getColdAndHotSubCar().getFullSpeed());

                        coldOpenTime = input.getTotalTime() + executionTime;
                        coldOpenStateChange = true;
                        coldAndHotOpenBoxEnum = ColdAndHotOpenBoxEnum.EMPTY_SUB_CAR_GO_COLD_OPEN_WORK_STATION_LOAD;
                        eventTime = executionTime;

                    }else {
                        coldWorkStationIndex = tools.judgeColdAndHotOpenBoxWorkStationIsWaitingTransprot(input.getWorkStations(), input.getOpenBoxIndexInput());
                        //判断是否有工岗待搬运
                        if (coldWorkStationIndex != -1){
                            //发生前往工岗的事件
                            double executionTime = Math.abs(input.getWorkStations().get(coldWorkStationIndex).getWorkPositionCoordinate().getX() -
                                    input.getColdAndHotFerry().getLocationCoordinate().getX()) / input.getColdAndHotFerry().getEmptySpeed();

                            coldOpenTime = input.getTotalTime() + executionTime;
                            coldOpenStateChange = true;
                            coldAndHotOpenBoxEnum = ColdAndHotOpenBoxEnum.EMPTY_FERRY_GO_COLD_OPEN_WORK_STATION;
                            eventTime = executionTime;

                            //没有待搬运的工岗
                        }else {
                            //判断4个工岗是否全部被占用，正等待工作完成，避免车辆在搬运冷热混合开箱点位的砂箱后整个开箱区堵塞（因为工岗被占用）
                            if (!tools.judgeColdOpenBoxWorkStationAllInWorking(input.getWorkStations(), input.getOpenBoxIndexInput())){
                                //判断热砂合箱点位是否有待搬运的产品
                                //如果有，前往热砂合箱点位
                                if (input.getColdAndHotOpenBoxPosition().getStatus().equals(PositionStatusEnum.OCCUPIED)){
                                    //发生前往热砂合箱点位的装载事件
                                    double executionTime = Math.abs(input.getColdAndHotOpenBoxPosition().getCoordinate().getX() -
                                            input.getColdAndHotFerry().getLocationCoordinate().getX()) / input.getColdAndHotFerry().getEmptySpeed();

                                    coldOpenTime = input.getTotalTime() + executionTime;
                                    coldOpenStateChange = true;
                                    coldAndHotOpenBoxEnum = ColdAndHotOpenBoxEnum.EMPTY_FERRY_GO_COLD_OPEN_POSITION;
                                    eventTime = executionTime;

                                }
                            }
                        }
                    }

                    //子车满载
                }else {
                    //判断车辆装载的产品是否是合箱完成的砂箱产品
                    if (input.getColdAndHotSubCar().getProduct().getProductType().equals(ProductTypeEnum.WHOLE_SAND_SHELL_BOX)){
                        coldWorkStationIndex = tools.judgeColdAndHotOpenBoxCarGoWhichWorkStation(input.getWorkStations(), input.getOpenBoxIndexInput());
                        //判断全部工岗是否已满
                        if (coldWorkStationIndex != -1){
                            //发生前往工岗卸载砂箱的指令
                            double executionTime = (Math.abs(input.getWorkStations().get(coldWorkStationIndex).getWorkPositionCoordinate().getX() -
                                    input.getColdAndHotFerry().getLocationCoordinate().getX()) / input.getColdAndHotFerry().getFullSpeed()) +
                                    (Math.abs(input.getWorkStations().get(coldWorkStationIndex).getWorkPositionCoordinate().getY() -
                                            input.getColdAndHotFerry().getLocationCoordinate().getY()) / input.getColdAndHotSubCar().getFullSpeed()) +
                                    (input.getColdAndHotSubCar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getWorkStations().get(coldWorkStationIndex).getWorkPositionCoordinate().getY() -
                                    input.getColdAndHotFerry().getLocationCoordinate().getY()) / input.getColdAndHotSubCar().getEmptySpeed());
                            coldOpenTime = input.getTotalTime() + executionTime;
                            coldOpenStateChange = true;
                            coldAndHotOpenBoxEnum = ColdAndHotOpenBoxEnum.FULL_SUB_CAR_GO_COLD_OPEN_WORK_STATION_LAND;
                            eventTime = executionTime;

                        }
                        //前往砂箱回流
                    }else {
                        //判断母车是否到达砂箱回流的点位处
                        if (input.getColdAndHotFerry().getLocationCoordinate().getX() ==
                                input.getColdAndHotRefluxPosition().getCoordinate().getX()){
                            //判断点位是否被占用
                            if (input.getColdAndHotRefluxPosition().getStatus().equals(PositionStatusEnum.UNOCCUPIED)){
                                //发生子车前往点位卸载砂箱的事件
                                double executionTime = (Math.abs(input.getColdAndHotFerry().getLocationCoordinate().getY() -
                                        input.getColdAndHotRefluxPosition().getCoordinate().getY()) / input.getColdAndHotSubCar().getFullSpeed()) +
                                        (input.getColdAndHotSubCar().getTopRodRaiseOrFallTime()) + (Math.abs(input.getColdAndHotRefluxPosition().getCoordinate().getY() -
                                        input.getColdAndHotFerry().getLocationCoordinate().getY()) / input.getColdAndHotSubCar().getEmptySpeed());

                                coldOpenTime = input.getTotalTime() + executionTime;
                                coldOpenStateChange = true;
                                coldAndHotOpenBoxEnum = ColdAndHotOpenBoxEnum.FULL_SUB_CAR_GO_COLD_SAND_REFLUX_POSITION_LAND;
                                eventTime = executionTime;

                            }
                            //前往热砂砂箱回流点位
                        }else {
                            //发生前往点位的事件
                            double executionTime = Math.abs(input.getColdAndHotFerry().getLocationCoordinate().getX() -
                                    input.getColdAndHotRefluxPosition().getCoordinate().getX()) / input.getColdAndHotFerry().getFullSpeed();

                            coldOpenTime = input.getTotalTime() + executionTime;
                            coldOpenStateChange = true;
                            coldAndHotOpenBoxEnum = ColdAndHotOpenBoxEnum.FULL_FERRY_GO_COLD_SAND_REFLUX_POSITION;
                            eventTime = executionTime;

                        }
                    }
                }
            }else {
                switch (coldAndHotOpenBoxEnum){
                    case EMPTY_SUB_CAR_GO_COLD_OPEN_BOX_POSITION_LOAD:
                        //发生空载子车前往冷砂开箱点位装载砂箱事件
                        simulationOutput = coldOpenBoxEventFlow.ColdAndHotEmptyLoadInPositionEventCreate(simulationOutput);

                        coldOpenStateChange = false;
                        coldAndHotOpenBoxEnum = null;
                        log.info("开箱：在冷砂点位装载产品" + coldOpenTime);
                        break;
                    case EMPTY_SUB_CAR_GO_COLD_OPEN_WORK_STATION_LOAD:
                        //发生空载子车前往冷砂开箱工岗装载砂箱事件
                        simulationOutput = coldOpenBoxEventFlow.ColdAndHotOpenBoxEmptyLoadInWorkStationEventCreate_2(simulationOutput, coldWorkStationIndex, upProducts, upWorkStationIndex);

                        coldOpenStateChange = false;
                        coldAndHotOpenBoxEnum = null;
//                        log.info("开箱cold：在工岗装载产品" + coldOpenTime);
                        break;
                    case EMPTY_FERRY_GO_COLD_OPEN_WORK_STATION:
                        //发生空载母车前往冷砂开箱工岗事件
                        simulationOutput = coldOpenBoxEventFlow.ColdAndHotOpenBoxEmptyGoWorkStationEventCreate(simulationOutput, coldWorkStationIndex);

                        coldOpenStateChange = false;
                        coldAndHotOpenBoxEnum = null;
                        log.info("开箱cold：前往工岗装载" + coldOpenTime);
                        break;
                    case EMPTY_FERRY_GO_COLD_OPEN_POSITION:
                        //发生空载母车前往冷砂开箱点位事件
                        simulationOutput = coldOpenBoxEventFlow.ColdAndHotOpenBoxEmptyGoColdAndHotPosotionEventCreate(simulationOutput);

                        coldOpenStateChange = false;
                        coldAndHotOpenBoxEnum = null;
//                        log.info("开箱cold：前往冷砂点位装载" + coldOpenTime);
                        break;
                    case FULL_SUB_CAR_GO_COLD_OPEN_WORK_STATION_LAND:
                        //发生满载子车前往冷砂开箱工岗卸载砂箱的指令
                        simulationOutput = coldOpenBoxEventFlow.ColdAndHotOpenBoxFullGoWorkStationEventCreate(simulationOutput, coldWorkStationIndex);

                        coldOpenStateChange = false;
                        coldAndHotOpenBoxEnum = null;
                        log.info("开箱cold：前往工岗卸载" + coldOpenTime);
                        break;
                    case FULL_SUB_CAR_GO_COLD_SAND_REFLUX_POSITION_LAND:
                        //发生满载子车前往冷砂砂箱回流点位卸载砂箱的事件
                        simulationOutput = coldOpenBoxEventFlow.ColdAndHotOpenBoxFullUnLoadPositionEventCreate(simulationOutput);

                        coldOpenStateChange = false;
                        coldAndHotOpenBoxEnum = null;
                        log.info("开箱cold：在点位卸载" + coldOpenTime);
                        break;
                    case FULL_FERRY_GO_COLD_SAND_REFLUX_POSITION:
                        //发生满载母车前往冷砂砂箱回流点位的事件
                        simulationOutput = coldOpenBoxEventFlow.ColdAndHotOpenBoxFullGoPositionEventCreate(simulationOutput);

                        coldOpenStateChange = false;
                        coldAndHotOpenBoxEnum = null;
                        log.info("开箱cold：前往点位卸载" + coldOpenTime);
                        break;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[47] = eventTime;
            eventTime = 0.0;
        }

        //判断铸件队列是否为空
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

        Coordinate upMidWaitCoordinate = new Coordinate((input.getCastingUpPosition().getCoordinate().getX() + input.getCastingVerticalPosition().getCoordinate().getX()) / 2,
                input.getCastingUpPosition().getCoordinate().getY());

        Coordinate upLayWaitCoordinate = new Coordinate(input.getCastingVerticalPosition().getCoordinate().getX() -
                (input.getColdAndHotOpenBoxPosition().getCoordinate().getY() - input.getColdAndHotOpenBoxPath().getStartCoordinate().getY()),
                input.getCastingUpSubCar().getLocationCoordinate().getY());

        //判断铸件上轨道是否可以发生事件
        //未考虑碰撞
        if (input.getTotalTime() >= castingUpTime){
            if (!castingUpStateChange){
                //判断铸件回流上轨道子车是否满载
                if (allTool.judgeSubCarIsEmpty(input.getCastingUpSubCar())){
                    //判断铸件回流点位是否有产品占用
                    if (input.getCastingUpPosition().getStatus().equals(PositionStatusEnum.OCCUPIED)){
                        //前往上铸件托盘回流点位，并进行装载
                        double executionTime = (Math.abs(input.getCastingUpSubCar().getLocationCoordinate().getX() - input.getCastingUpPosition().getCoordinate().getX())
                                / input.getCastingUpSubCar().getEmptySpeed()) + input.getCastingUpSubCar().getTopRodRaiseOrFallTime();

                        castingUpTime = input.getTotalTime() + executionTime;
                        castingUpStateChange = true;
                        castingUpEnum = CastingUpEnum.EMPTY_SUB_CAR_GO_UP_TRAY_LAY_POSITION_LOAD;
                        eventTime = executionTime;

                    }else {
                        //判断子车是否在轨道中点位置，不在则前往中点位置
                        if (input.getCastingUpSubCar().getLocationCoordinate().getX() !=
                                (input.getCastingUpPosition().getCoordinate().getX() + input.getCastingVerticalPosition().getCoordinate().getX()) / 2){
                            //前往上轨道中点位置
                            double executionTime = Math.abs(input.getCastingUpSubCar().getLocationCoordinate().getX() - upMidWaitCoordinate.getX())
                                    / input.getCastingUpSubCar().getEmptySpeed();

                            castingUpTime = input.getTotalTime() + executionTime;
                            castingUpStateChange = true;
                            castingUpEnum = CastingUpEnum.EMPTY_SUB_CAR_GO_UP_MID_POSITION_WAIT;
                            eventTime = executionTime;

                        }
                    }

                    //子车满载
                }else {
                    //判断子车是否到达等待点位
                    if (input.getCastingUpSubCar().getLocationCoordinate().equals(upLayWaitCoordinate)){
                        //判断垂直轨道点位是否被占用
                        if (input.getCastingVerticalPosition().getStatus().equals(PositionStatusEnum.UNOCCUPIED)){
                            //发生前往垂直轨道点位放置铸件的事件
                            double executionTime = (Math.abs(input.getCastingUpSubCar().getLocationCoordinate().getX() - input.getCastingVerticalPosition().getCoordinate().getX())
                                    / input.getCastingUpSubCar().getFullSpeed()) + input.getCastingUpSubCar().getTopRodRaiseOrFallTime();

                            castingUpTime = input.getTotalTime() + executionTime;
                            castingUpStateChange = true;
                            castingUpEnum = CastingUpEnum.FULL_SUB_CAR_GO_UP_CASTING_LAY_POSITION_LAND;
                            eventTime = executionTime;

                        }
                    }else {
                        //发生前往等待点的事件
                        double executionTime = Math.abs(input.getCastingUpSubCar().getLocationCoordinate().getX() - upLayWaitCoordinate.getX())
                                / input.getCastingUpSubCar().getFullSpeed();

                        castingUpTime = input.getTotalTime() + executionTime;

                        castingUpStateChange = true;
                        castingUpEnum = CastingUpEnum.FULL_SUB_CAR_GO_UP_WAIT_CASTING_LAY_POSITION;
                        eventTime = executionTime;

                    }
                }
            }else {
                switch (castingUpEnum){
                    case EMPTY_SUB_CAR_GO_UP_TRAY_LAY_POSITION_LOAD:
                        //前往上铸件托盘回流点位，并进行装载
                        simulationOutput = castingUpEventFlow.EmptyCastingUpGoPositionEventCreate(simulationOutput);

                        castingUpStateChange = false;
                        castingUpEnum = null;
                        log.info("castingUpStateChange: 子车发生前往铸件回流点位装载的事件" + castingUpTime);
                        break;
                    case EMPTY_SUB_CAR_GO_UP_MID_POSITION_WAIT:
                        //前往上轨道中点位置
                        simulationOutput = castingUpEventFlow.EmptyCastingUpGoWaitPositionEventCreate(simulationOutput, upMidWaitCoordinate);

                        castingUpStateChange = false;
                        castingUpEnum = null;
//                        log.info("castingUpStateChange: 前往轨道中点位置处" + castingUpTime);
                        break;
                    case FULL_SUB_CAR_GO_UP_CASTING_LAY_POSITION_LAND:
                        //发生前往垂直轨道点位放置铸件的事件
                        simulationOutput = castingUpEventFlow.FullCastingUpGoPositionEventCreate(simulationOutput);

                        castingUpStateChange = false;
                        castingUpEnum = null;
                        log.info("castingUpStateChange: 发生前往垂直轨道点位放置铸件的事件" + castingUpTime);
                        break;
                    case FULL_SUB_CAR_GO_UP_WAIT_CASTING_LAY_POSITION:
                        //发生满载前往放置铸件的等待点的事件
                        simulationOutput = castingUpEventFlow.FullCastingUpGoWaitPositionEventCreate_2(simulationOutput, upWorkStationIndex.pop(), upLayWaitCoordinate);

                        castingUpStateChange = false;
                        castingUpEnum = null;
                        log.info("castingUpStateChange: 发生满载前往等待点的事件" + castingUpTime);
                        break;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[56] = eventTime;
            eventTime = 0.0;
        }

        Coordinate downMidWaitCoordinate = new Coordinate((input.getCastingDownPosition().getCoordinate().getX() + input.getCastingShippingPosition().getCoordinate().getX()) / 2,
                input.getCastingDownPosition().getCoordinate().getY());
        Coordinate downLayWaitCoordinate = new Coordinate(input.getCastingShippingPosition().getCoordinate().getX() -
                (input.getColdAndHotOpenBoxPosition().getCoordinate().getY() - input.getColdAndHotOpenBoxPath().getStartCoordinate().getY()),
                input.getCastingDownSubCar().getLocationCoordinate().getY());

        //判断铸件下轨道是否可以发生事件
        //未考虑碰撞
        if (input.getTotalTime() >= castingDownTime){
            if (!castingDownStateChange){
                //判断铸件回流下轨道子车是否满载
                if (allTool.judgeSubCarIsEmpty(input.getCastingDownSubCar())){
                    //判断铸件回流点位是否有产品占用
                    if (input.getCastingDownPosition().getStatus().equals(PositionStatusEnum.OCCUPIED)){
                        //前往铸件回流点位，并进行装载
                        double executionTime = (Math.abs(input.getCastingDownSubCar().getLocationCoordinate().getX() - input.getCastingDownPosition().getCoordinate().getX())
                                / input.getCastingDownSubCar().getEmptySpeed()) + input.getCastingDownSubCar().getTopRodRaiseOrFallTime();

                        castingDownTime = input.getTotalTime() + executionTime;
                        castingDownStateChange = true;
                        castingDownEnum = CastingDownEnum.EMPTY_SUB_CAR_GO_DOWN_TRAY_LAY_POSITION_LOAD;
                        eventTime = executionTime;

                    }else {
                        //判断子车是否在轨道中点位置，不在则前往中点位置
                        if (input.getCastingDownSubCar().getLocationCoordinate().getX() !=
                                (input.getCastingDownPosition().getCoordinate().getX() + input.getCastingShippingPosition().getCoordinate().getX()) / 2){
                            //前往轨道中点位置
                            double executionTime = Math.abs(input.getCastingDownSubCar().getLocationCoordinate().getX() - downMidWaitCoordinate.getX())
                                    / input.getCastingDownSubCar().getEmptySpeed();

                            castingDownTime = input.getTotalTime() + executionTime;
                            castingDownStateChange = true;
                            castingDownEnum = CastingDownEnum.EMPTY_SUB_CAR_GO_DOWN_MID_POSITION_WAIT;
                            eventTime = executionTime;

                        }
                    }
                    //子车满载
                }else {
                    //判断子车是否到达等待点位
                    if (input.getCastingDownSubCar().getLocationCoordinate().equals(downLayWaitCoordinate)){
                        //判断传输点位是否被占用
                        if (input.getCastingShippingPosition().getStatus().equals(PositionStatusEnum.UNOCCUPIED)){
                            //发生前往传输点位放置铸件的事件
                            double executionTime = (Math.abs(input.getCastingDownSubCar().getLocationCoordinate().getX() - input.getCastingVerticalPosition().getCoordinate().getX())
                                    / input.getCastingDownSubCar().getFullSpeed()) + input.getCastingDownSubCar().getTopRodRaiseOrFallTime();

                            castingDownTime = input.getTotalTime() + executionTime;
                            castingDownStateChange = true;
                            castingDownEnum = CastingDownEnum.FULL_SUB_CAR_GO_DOWN_CASTING_LAY_POSITION_LAND;
                            eventTime = executionTime;

                        }
                    }else {
                        //发生前往等待点的事件
                        double executionTime = Math.abs(input.getCastingDownSubCar().getLocationCoordinate().getX() - downLayWaitCoordinate.getX())
                                / input.getCastingDownSubCar().getFullSpeed();

                        castingDownTime = input.getTotalTime() + executionTime;
                        castingDownStateChange = true;
                        castingDownEnum = CastingDownEnum.FULL_SUB_CAR_GO_DOWN_WAIT_CASTING_LAY_POSITION;
                        eventTime = executionTime;

                    }
                }
            }else {
                switch (castingDownEnum){
                    case EMPTY_SUB_CAR_GO_DOWN_TRAY_LAY_POSITION_LOAD:
                        //前往下铸件托盘回流点位，并进行装载
                        simulationOutput = castingDownEventFlow.EmptyCastingDownGoPositionEventCreate(simulationOutput);

                        castingDownStateChange = false;
                        castingDownEnum = null;
                        log.info("castingDownStateChange: 前往铸件回流点位，并进行装载" + castingDownTime);
                        break;
                    case EMPTY_SUB_CAR_GO_DOWN_MID_POSITION_WAIT:
                        //前往下轨道中点位置
                        simulationOutput = castingDownEventFlow.EmptyCastingDownGoWaitPositionEventCreate(simulationOutput, downMidWaitCoordinate);

                        castingDownStateChange = false;
                        castingDownEnum = null;
//                        log.info("castingDownStateChange: 前往轨道中点位置" + castingDownTime);
                        break;
                    case FULL_SUB_CAR_GO_DOWN_CASTING_LAY_POSITION_LAND:
                        //发生前往下垂直轨道点位放置铸件的事件
                        simulationOutput = castingDownEventFlow.FullCastingDownGoPositionEventCreate(simulationOutput);

                        castingDownStateChange = false;
                        castingDownEnum = null;
                        log.info("castingDownStateChange: 发生前往传输点位放置铸件的事件" + castingDownTime);
                        break;
                    case FULL_SUB_CAR_GO_DOWN_WAIT_CASTING_LAY_POSITION:
                        //发生满载前往下放置铸件的等待点的事件
                        simulationOutput = castingDownEventFlow.FullCastingDownGoWaitPositionEventCreate_2(simulationOutput, downWorkStationIndex.pop(), downLayWaitCoordinate);

                        castingDownStateChange = false;
                        castingDownEnum = null;
                        log.info("castingDownStateChange: 发生满载前往等待点的事件" + castingDownTime);
                        break;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[57] = eventTime;
            eventTime = 0.0;
        }

        Coordinate verticalMidWaitCoordinate = new Coordinate(input.getCastingVerticalSubCar().getLocationCoordinate().getX(),
                (input.getCastingVerticalPosition().getCoordinate().getY() + input.getCastingShippingPosition().getCoordinate().getY()) / 2);
        Coordinate verticalLayWaitCoordinate = new Coordinate(input.getCastingVerticalSubCar().getLocationCoordinate().getX(),
                (input.getCastingVerticalPosition().getCoordinate().getY() + input.getCastingShippingPosition().getCoordinate().getY()) / 2);

        //判断铸件垂直轨道是否可以发生事件
        //未考虑碰撞
        if (input.getTotalTime() >= castingVerticalTime){
            if (!castingVerticalStateChange){
                //判断铸件回流下轨道子车是否满载
                if (allTool.judgeSubCarIsEmpty(input.getCastingVerticalSubCar())){
                    //判断铸件回流点位是否有产品占用
                    if (input.getCastingVerticalPosition().getStatus().equals(PositionStatusEnum.OCCUPIED)){
                        //前往铸件回流点位，并进行装载
                        //注意此为垂直轨道！！！
                        double executionTime = (Math.abs(input.getCastingVerticalSubCar().getLocationCoordinate().getY() - input.getCastingVerticalPosition().getCoordinate().getY())
                                / input.getCastingVerticalSubCar().getEmptySpeed()) + input.getCastingVerticalSubCar().getTopRodRaiseOrFallTime();

                        castingVerticalTime = input.getTotalTime() + executionTime;

                        castingVerticalStateChange = true;
                        castingVerticalEnum = CastingVerticalEnum.EMPTY_SUB_CAR_GO_VERTICAL_CASTING_POSITION_LOAD;
                        eventTime = executionTime;

                    }else {
                        //判断子车是否在轨道中点位置，不在则前往中点位置
                        if (input.getCastingVerticalSubCar().getLocationCoordinate().getY() !=
                                (input.getCastingVerticalPosition().getCoordinate().getY() + input.getCastingShippingPosition().getCoordinate().getY()) / 2){
                            //前往轨道中点位置
                            double executionTime = Math.abs(input.getCastingVerticalSubCar().getLocationCoordinate().getY() - verticalMidWaitCoordinate.getY())
                                    / input.getCastingVerticalSubCar().getEmptySpeed();

                            castingVerticalTime = input.getTotalTime() + executionTime;
                            castingVerticalStateChange = true;
                            castingVerticalEnum = CastingVerticalEnum.EMPTY_SUB_CAR_GO_VERTICAL_MID_POSITION_WAIT;
                            eventTime = executionTime;

                        }
                    }
                    //子车满载
                }else {
                    //判断子车是否到达等待点位
                    if (input.getCastingVerticalSubCar().getLocationCoordinate().equals(verticalLayWaitCoordinate)){
                        //判断传输点位是否被占用
                        if (input.getCastingShippingPosition().getStatus().equals(PositionStatusEnum.UNOCCUPIED)){
                            //发生前往传输点位放置铸件的事件
                            double executionTime = (Math.abs(input.getCastingVerticalSubCar().getLocationCoordinate().getY() -
                                    input.getCastingShippingPosition().getCoordinate().getY()) / input.getCastingVerticalSubCar().getFullSpeed()) +
                                    input.getCastingVerticalSubCar().getTopRodRaiseOrFallTime();

                            castingVerticalTime = input.getTotalTime() + executionTime;
                            castingVerticalStateChange = true;
                            castingVerticalEnum = CastingVerticalEnum.FULL_SUB_CAR_GO_VERTICAL_CASTING_LAY_POSITION_LAND;
                            eventTime = executionTime;

                        }
                    }else {
                        //发生前往等待点的事件
                        double executionTime = Math.abs(input.getCastingVerticalSubCar().getLocationCoordinate().getY() -
                                verticalLayWaitCoordinate.getY()) / input.getCastingVerticalSubCar().getFullSpeed();

                        castingVerticalTime = input.getTotalTime() + executionTime;

                        castingVerticalStateChange = true;
                        castingVerticalEnum = CastingVerticalEnum.FULL_SUB_CAR_GO_VERTICAL_WAIT_CASTING_LAY_POSITION;
                        eventTime = executionTime;

                    }
                }
            }else {
                switch (castingVerticalEnum){
                    case EMPTY_SUB_CAR_GO_VERTICAL_CASTING_POSITION_LOAD:
                        //前往铸件回流点位，并进行装载
                        simulationOutput = castingVerticalEventFlow.EmptyCastingVerticalGoPositionEventCreate(simulationOutput);

                        castingVerticalStateChange = false;
                        castingVerticalEnum = null;
                        log.info("castingVerticalStateChange: 前往铸件回流点位，并进行装载" + castingVerticalTime);
                        break;
                    case EMPTY_SUB_CAR_GO_VERTICAL_MID_POSITION_WAIT:
                        //前往回流点前的等待位置处
                        simulationOutput = castingVerticalEventFlow.EmptyCastingVerticalGoWaitPositionEventCreate(simulationOutput, verticalMidWaitCoordinate);

                        castingVerticalStateChange = false;
                        castingVerticalEnum = null;
//                        log.info("castingVerticalStateChange: 前往轨道中点位置" + castingVerticalTime);
                        break;
                    case FULL_SUB_CAR_GO_VERTICAL_CASTING_LAY_POSITION_LAND:
                        //前往回流点前的等待位置处
                        simulationOutput = castingVerticalEventFlow.FullCastingVerticalGoPositionEventCreate(simulationOutput);

                        castingVerticalStateChange = false;
                        log.info("castingVerticalStateChange: 发生前往传输点位放置铸件的事件" + castingVerticalTime);
                        break;
                    case FULL_SUB_CAR_GO_VERTICAL_WAIT_CASTING_LAY_POSITION:
                        //前往回流点前的等待位置处
                        simulationOutput = castingVerticalEventFlow.FullCastingVerticalGoWaitPositionEventCreate(simulationOutput, verticalLayWaitCoordinate);

                        castingVerticalStateChange = false;
                        log.info("castingVerticalStateChange: 发生满载前往等待点的事件" + castingVerticalTime);
                        break;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[58] = eventTime;
            eventTime = 0.0;
        }

        Coordinate sandRefluxMidWaitCoordinate = new Coordinate(input.getSandBoxRefluxRightSubCar().getLocationCoordinate().getX(),
                (input.getColdAndHotRefluxPosition().getCoordinate().getY() +
                        outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getY()) / 2);
        Coordinate sandRefluxLayWaitCoordinate = new Coordinate(input.getSandBoxRefluxRightSubCar().getLocationCoordinate().getX(),
                outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getY() -
                        (outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getY() -
                                input.getCastingDownPath().getStartCoordinate().getY()));

        //判断砂箱回流轨道是否可以发生事件
        if (input.getTotalTime() >= sandBoxRefluxTime){
            if (!sandBoxRefluxStateChange){
                //判断铸件回流下轨道子车是否满载
                if (allTool.judgeSubCarIsEmpty(input.getSandBoxRefluxRightSubCar())){
                    //判断铸件回流点位是否有产品占用
                    if (input.getColdAndHotRefluxPosition().getStatus().equals(PositionStatusEnum.OCCUPIED)){
                        //前往冷热混合砂箱回流点位，并进行装载
                        //注意此为垂直轨道！！！
                        double executionTime = (Math.abs(input.getSandBoxRefluxRightSubCar().getLocationCoordinate().getY() -
                                input.getColdAndHotRefluxPosition().getCoordinate().getY()) / input.getSandBoxRefluxRightSubCar().getEmptySpeed()) +
                                input.getSandBoxRefluxRightSubCar().getTopRodRaiseOrFallTime();

                        sandBoxRefluxTime = input.getTotalTime() + executionTime;
                        sandBoxRefluxStateChange = true;
                        sandBoxRefluxEnum = SandBoxRefluxEnum.EMPTY_SUB_CAR_GO_COLD_AND_HOT_SAND_REFLUX_POSITION_LOAD;
                        eventTime = executionTime;

                    }else {
                        //判断子车是否在轨道中点位置，不在则前往中点位置
                        if (input.getSandBoxRefluxRightSubCar().getLocationCoordinate().getY() !=
                                (input.getColdAndHotRefluxPosition().getCoordinate().getY() +
                                        outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getY()) / 2){
                            //空载前往砂箱回流轨道的中点进行等待事件
                            double executionTime = Math.abs(input.getSandBoxRefluxRightSubCar().getLocationCoordinate().getY() - sandRefluxMidWaitCoordinate.getY())
                                    / input.getSandBoxRefluxRightSubCar().getEmptySpeed();

                            sandBoxRefluxTime = input.getTotalTime() + executionTime;
                            sandBoxRefluxStateChange = true;
                            sandBoxRefluxEnum = SandBoxRefluxEnum.EMPTY_SUB_CAR_GO_SAND_REFLUX_MID_POSITION_WAIT;
                            eventTime = executionTime;

                        }
                    }
                    //子车满载
                }else {
                    //判断子车是否到达等待点位
                    if (input.getSandBoxRefluxRightSubCar().getLocationCoordinate().equals(sandRefluxLayWaitCoordinate)){
                        //判断传输点位是否被占用
                        if (outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getStatus().
                                equals(PositionStatusEnum.UNOCCUPIED)){
                            //发生满载前往砂箱回流轨道放置点卸载砂箱的事件
                            double executionTime = (Math.abs(input.getSandBoxRefluxRightSubCar().getLocationCoordinate().getY() -
                                    outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getY())
                                    / input.getSandBoxRefluxRightSubCar().getFullSpeed()) + input.getSandBoxRefluxRightSubCar().getTopRodRaiseOrFallTime();

                            sandBoxRefluxTime = input.getTotalTime() + executionTime;
                            sandBoxRefluxStateChange = true;
                            sandBoxRefluxEnum = SandBoxRefluxEnum.FULL_SUB_CAR_GO_SAND_REFLUX_LAY_POSITION_LAND;
                            eventTime = executionTime;

                        }
                    }else {
                        //发生满载前往砂箱回流轨道放置前等待点的事件
                        double executionTime = Math.abs(input.getSandBoxRefluxRightSubCar().getLocationCoordinate().getY() - sandRefluxLayWaitCoordinate.getY())
                                / input.getSandBoxRefluxRightSubCar().getFullSpeed();

                        sandBoxRefluxTime = input.getTotalTime() + executionTime;
                        sandBoxRefluxStateChange = true;
                        sandBoxRefluxEnum = SandBoxRefluxEnum.FULL_SUB_CAR_GO_SAND_REFLUX_WAIT_CASTING_LAY_POSITION;
                        eventTime = executionTime;

                    }
                }
            }else {
                switch (sandBoxRefluxEnum){
                    case EMPTY_SUB_CAR_GO_COLD_AND_HOT_SAND_REFLUX_POSITION_LOAD:
                        //前往冷热混合砂箱回流点位，并进行装载
                        simulationOutput = sandBoxRefluxEventFlow.EmptySandBoxRefluxGoPositionEventCreate(simulationOutput);

                        sandBoxRefluxStateChange = false;
                        sandBoxRefluxEnum = null;
                        log.info("sandBoxRefluxStateChange: 前往铸件回流点位，并进行装载" + sandBoxRefluxTime);
                        break;
                    case EMPTY_SUB_CAR_GO_SAND_REFLUX_MID_POSITION_WAIT:
                        //空载前往砂箱回流轨道的中点进行等待事件
                        simulationOutput = sandBoxRefluxEventFlow.EmptySandBoxRefluxGoWaitPositionEventCreate(simulationOutput, sandRefluxMidWaitCoordinate);

                        sandBoxRefluxStateChange = false;
                        sandBoxRefluxEnum = null;
//                        log.info("sandBoxRefluxStateChange: 前往回流点前的等待位置处" + sandBoxRefluxTime);
                        break;
                    case FULL_SUB_CAR_GO_SAND_REFLUX_LAY_POSITION_LAND:
                        //满载前往砂箱回流轨道放置点卸载砂箱的事件
                        simulationOutput = sandBoxRefluxEventFlow.FullSandBoxRefluxGoPositionEventCreate(simulationOutput);

                        sandBoxRefluxStateChange = false;
                        log.info("sandBoxRefluxStateChange: 发生前往传输点位放置铸件的事件" + sandBoxRefluxTime);
                        break;
                    case FULL_SUB_CAR_GO_SAND_REFLUX_WAIT_CASTING_LAY_POSITION:
                        //满载前往砂箱回流轨道放置前等待点的事件
                        simulationOutput = sandBoxRefluxEventFlow.FullSandBoxRefluxGoWaitPositionEventCreate(simulationOutput, sandRefluxLayWaitCoordinate);

                        sandBoxRefluxStateChange = false;
                        log.info("sandBoxRefluxStateChange: 发生满载前往等待点的事件" + sandBoxRefluxTime);
                        break;
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