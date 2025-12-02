package cn.hex.ddp.manufacture.infrastructure.algorithm.simulation;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.AnalogInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.MouldingSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.OutBoundSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.mouldingLine.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.List;

import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.OCCUPIED;
import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.UNOCCUPIED;
import static cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum.PATH_OCCUPIED;
import static cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum.PATH_UNOCCUPIED;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/7 21:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class MouldingSimulation {
    /**
     * 模具回流时间
     */
    private double moldRefluxTime = 0.0;
    /**
     * 冷砂模具回流时间
     */
    private double coldMoldRefluxTime = 0.0;
    /**
     * 热砂喷涂搬运时间
     */
    private double hotSprayTime = 0.0;
    /**
     * 冷砂喷涂搬运时间
     */
    private double coldSprayTime = 0.0;
    /**
     * 热砂前往合箱时间
     */
    private double hotSprayGoClosingTime = 0.0;
    /**
     * 冷砂前往合箱时间
     */
    private double coldSprayGoClosingTime = 0.0;
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean moldRefluxStateChange = false;
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean coldMoldRefluxStateChange = false;
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean hotSprayStateChange = false;
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean coldSprayStateChange = false;
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean hotClosingStateChange = false;
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean coldClosingStateChange = false;
    /**
     * 判断模具回流子车是否进入回流点位放置产品的标识
     * true表示子车已进入放置点位放置产品，需改变对轨道的占用
     */
    private boolean moldRefluxSubCarLayProductFlag = false;
    /**
     * 造型区仿真工具类
     */
    private MouldingSimulationTools tools = new MouldingSimulationTools();
    /**
     * 仿真总工具类
     */
    private SimulationTool allTool = new SimulationTool();
    /**
     * 创建造型线的事件流类
     */
    private MouldingEventFlow mouldingEventFlow = new MouldingEventFlow();
    /**
     * 创建喷涂到合箱的事件流类
     */
    private SprayGoClosingEventFlow sprayGoClosingEventFlow = new SprayGoClosingEventFlow();
    /**
     * 喷涂设备仿真类
     */
    private SprayEquipmentSimulation hotSprayUp = new SprayEquipmentSimulation();
    private SprayEquipmentSimulation hotSprayDown = new SprayEquipmentSimulation();
    private SprayEquipmentSimulation coldSprayUp = new SprayEquipmentSimulation();
    private SprayEquipmentSimulation coldSprayDown = new SprayEquipmentSimulation();

    private double eventTime = 0.0;

    public OutBoundMouldingAndClosingBox MouldingSimulationRun(OutBoundMouldingAndClosingBox simulationInput, Double[] timeList, AnalogInput analogInput){
        OutBoundMouldingAndClosingBox simulationOutput = SerializationUtils.clone(simulationInput);
        MouldingSimulationInput input = simulationOutput.getMouldingInput().getInput();
        OutBoundSimulationInput outBoundInput = simulationOutput.getOutBoundInput().getInput();
        //模具回流轨道的热砂开模和砂芯开模的中点
        Coordinate moldRefluxWaitCoordinate = new Coordinate(input.getCoreMoldOpeningPosition().getCoordinate().getX(),
                (input.getHotMoldOpeningPosition().getCoordinate().getY() + input.getCoreMoldOpeningPosition().getCoordinate().getY()) / 2);
        //砂芯开模点和放置热砂点位的中点
        Coordinate waitLayCoordinate = new Coordinate(input.getCoreMoldOpeningPosition().getCoordinate().getX(),
                (input.getCoreMoldOpeningPosition().getCoordinate().getY() +
                        outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate().getY()) / 2);

        if (input.getTotalTime() >= moldRefluxTime){
            //判断子车是否装载
            if (allTool.judgeSubCarIsEmpty(input.getHotAndCoreMoldRefluxSubCar())){
                //判断回流子车是否到达等待点
                if (input.getHotAndCoreMoldRefluxSubCar().getLocationCoordinate().equals(moldRefluxWaitCoordinate)){
                    //判断制芯开模点位是否被占据
                    if (input.getCoreMoldOpeningPosition().getStatus().equals(OCCUPIED)){
                        if (moldRefluxStateChange){
                            simulationOutput = mouldingEventFlow.EmptyCoreMoldOpeningPositionEventCreate(simulationOutput);

//                            log.info("制芯开模轨道搬运");
                            moldRefluxStateChange = false;
                        }else {
                            double executionTime = (Math.abs(input.getHotAndCoreMoldRefluxSubCar().getLocationCoordinate().getY() - input.getCoreMoldOpeningPosition().getCoordinate().getY())
                                    / input.getHotAndCoreMoldRefluxSubCar().getEmptySpeed()) + input.getHotAndCoreMoldRefluxSubCar().getTopRodRaiseOrFallTime();
                            moldRefluxTime = input.getTotalTime() + executionTime;

//                            log.info("制芯开模轨道搬运?" + moldRefluxTime);
                            moldRefluxStateChange = true;

                            eventTime = executionTime;
                        }

                        //判断热砂开模点位是否被占据
                    }else if (input.getHotMoldOpeningPosition().getStatus().equals(OCCUPIED)){
                        if (moldRefluxStateChange){
                            simulationOutput = mouldingEventFlow.EmptyHotMoldOpeningPositionEventCreate(simulationOutput);

//                            log.info("热砂开模轨道搬运");
                            moldRefluxStateChange = false;
                        }else {
                            double executionTime = (Math.abs(input.getHotAndCoreMoldRefluxSubCar().getLocationCoordinate().getY() - input.getHotMoldOpeningPosition().getCoordinate().getY())
                                    / input.getHotAndCoreMoldRefluxSubCar().getEmptySpeed()) + input.getHotAndCoreMoldRefluxSubCar().getTopRodRaiseOrFallTime();

                            moldRefluxTime = input.getTotalTime() + executionTime;
//                            log.info("热砂开模轨道搬运?" + moldRefluxTime);
                            moldRefluxStateChange = true;

                            eventTime = executionTime;
                        }
                    }

                    //未到等待点
                }else {
                    if (moldRefluxStateChange){
                        simulationOutput = mouldingEventFlow.EmptyGoRefluxWaitPositionEventCreate(simulationOutput, moldRefluxWaitCoordinate);

//                        log.info("Go");
                        moldRefluxStateChange = false;
                    }else {
                        double executionTime = Math.abs(input.getHotAndCoreMoldRefluxSubCar().getLocationCoordinate().getY() - moldRefluxWaitCoordinate.getY())
                                / input.getHotAndCoreMoldRefluxSubCar().getEmptySpeed();
                        moldRefluxTime = input.getTotalTime() + executionTime;
//                        log.info("Go" + moldRefluxTime);
                        moldRefluxStateChange = true;

                        eventTime = executionTime;
                    }
                }
            }else {
                //判断车辆是否经过砂芯开模后的回流点位
//                if (tools.judgeMoldRefluxSubCarFullPosition(input.getHotAndCoreMoldRefluxSubCar(), simulationOutput)){
                    //判断车辆是否到达回流点前的等待位置处
                    if (input.getHotAndCoreMoldRefluxSubCar().getLocationCoordinate().equals(waitLayCoordinate)){
                        //判断回流模具的轨道是否被占用并且热砂和砂芯模具回流的点位是否被占用
                        if (outBoundInput.getPaths().get(outBoundInput.getOutBoundIndexInput().getMoldRefluxPathIndex()).getStatus().equals(PATH_UNOCCUPIED) &&
                                outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getStatus().equals(PositionStatusEnum.UNOCCUPIED)
                                && !moldRefluxSubCarLayProductFlag){

                            //前往点位放置砂箱
                            double executionTime = (Math.abs(outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate().getY()
                                    - input.getHotAndCoreMoldRefluxSubCar().getLocationCoordinate().getY()) / input.getHotAndCoreMoldRefluxSubCar().getFullSpeed())
                                    + input.getHotAndCoreMoldRefluxSubCar().getTopRodRaiseOrFallTime() +
                                    (Math.abs(outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate().getY() -
                                            waitLayCoordinate.getY()) / input.getHotAndCoreMoldRefluxSubCar().getEmptySpeed());
                            moldRefluxTime = input.getTotalTime() + executionTime;

                            //占用轨道，防止其他车辆进入
                            outBoundInput.getPaths().get(outBoundInput.getOutBoundIndexInput().getMoldRefluxPathIndex()).setStatus(PATH_OCCUPIED);
                            moldRefluxSubCarLayProductFlag = true;

                            eventTime = executionTime;

//                            log.info("模具放置轨道搬运准备");

                        }else if (moldRefluxSubCarLayProductFlag){
                            //发生前往点位放置砂箱的事件流
                            simulationOutput = mouldingEventFlow.FullMoldRefluxLayPositionEventCreate(simulationOutput, waitLayCoordinate);

//                            log.info("模具放置轨道搬运" + "?" + simulationOutput.getOutBoundInput().getInput());
                            outBoundInput.getPaths().get(outBoundInput.getOutBoundIndexInput().getMoldRefluxPathIndex()).setStatus(PATH_UNOCCUPIED);
                            moldRefluxSubCarLayProductFlag = false;
                        }

                        //没有到达回流点前的等待位置处
                    }else {
                        if (moldRefluxStateChange){
                            //前往回流点前的等待位置处
                            simulationOutput = mouldingEventFlow.FullGoRefluxWaitPositionEventCreate(simulationOutput, waitLayCoordinate);

//                            log.info("前往等待点");
                            moldRefluxStateChange = false;
                        }else {
                            double executionTime = Math.abs(input.getHotAndCoreMoldRefluxSubCar().getLocationCoordinate().getY() - waitLayCoordinate.getY())
                                    / input.getHotAndCoreMoldRefluxSubCar().getFullSpeed();
                            moldRefluxTime = input.getTotalTime() + executionTime;
                            moldRefluxStateChange = true;
//                            log.info("前往等待点？" + moldRefluxTime);

                            eventTime = executionTime;
                        }
                    }

                    //车辆没经过砂芯开模后的回流点位
//                }else {
//                    //此处写法有问题
//                    //先暂时简单处理
//                    //前往回流点前的等待位置处
//                    if (moldRefluxStateChange){
//                        //前往回流点前的等待位置处
//                        simulationOutput = mouldingEventFlow.FullGoRefluxWaitPositionEventCreate(simulationOutput, waitLayCoordinate);
//
//                        moldRefluxStateChange = false;
//                    }else {
//                        double executionTime = Math.abs(input.getHotAndCoreMoldRefluxSubCar().getLocationCoordinate().getY() - waitLayCoordinate.getY())
//                                / input.getHotAndCoreMoldRefluxSubCar().getFullSpeed();
//                        moldRefluxTime = input.getTotalTime() + executionTime;
//                        moldRefluxStateChange = true;
//                    }
//                }
            }
        }

        if (eventTime != 0.0){
            timeList[15] = eventTime;
            eventTime = 0.0;
        }

        Coordinate coldMoldRefluxWaitCoordinate = new Coordinate(input.getColdMoldOpeningPosition().getCoordinate().getX(),
                (input.getColdMoldOpeningPosition().getCoordinate().getY() + outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getColdSandMoldRefluxPositionIndex()).getCoordinate().getY()) / 2);

        if (input.getTotalTime() >= coldMoldRefluxTime){
            //判断子车是否装载
            if (allTool.judgeSubCarIsEmpty(input.getColdMoldRefluxSubCar())){
                //判断子车是否到达中间等待点位
                if (input.getColdMoldRefluxSubCar().getLocationCoordinate().equals(coldMoldRefluxWaitCoordinate)){
                    //判断点位是否有开模的冷模具
                    if (input.getColdMoldOpeningPosition().getStatus().equals(OCCUPIED)){
                        //子车发生前往开模点装载的事件
                        if (coldMoldRefluxStateChange){
                            //前往开模点装载的事件
                            simulationOutput = mouldingEventFlow.EmptyColdGoRefluxPositionEventCreate(simulationOutput);

//                            log.info("冷砂开模轨道搬运");
                            coldMoldRefluxStateChange = false;
                        }else {
                            double executionTime = (Math.abs(input.getColdMoldRefluxSubCar().getLocationCoordinate().getY() - input.getColdMoldOpeningPosition().getCoordinate().getY())
                                    / input.getColdMoldRefluxSubCar().getEmptySpeed()) + input.getColdMoldRefluxSubCar().getTopRodRaiseOrFallTime();
                            coldMoldRefluxTime = input.getTotalTime() + executionTime;

//                            log.info("冷砂开模轨道搬运？" + coldMoldRefluxTime);
                            coldMoldRefluxStateChange = true;

                            eventTime = executionTime;
                        }
                    }

                    //子车未到等待点
                }else {
                    //子车发前往等待点的事件
                    if (coldMoldRefluxStateChange){
                        //前往回流点前的等待位置处
                        simulationOutput = mouldingEventFlow.EmptyColdGoRefluxWaitPositionEventCreate(simulationOutput, coldMoldRefluxWaitCoordinate);

//                        log.info("冷砂go");
                        coldMoldRefluxStateChange = false;
                    }else {
                        double executionTime = Math.abs(input.getColdMoldRefluxSubCar().getLocationCoordinate().getY() - coldMoldRefluxWaitCoordinate.getY())
                                / input.getColdMoldRefluxSubCar().getEmptySpeed();
                        coldMoldRefluxTime = input.getTotalTime() + executionTime;

//                        log.info("冷砂go" + coldMoldRefluxTime);
                        coldMoldRefluxStateChange = true;

                        eventTime = executionTime;
                    }
                }

                //子车满载时
            }else {
                //判断子车是否到达中间等待点位
                if (input.getColdMoldRefluxSubCar().getLocationCoordinate().equals(coldMoldRefluxWaitCoordinate)){
                    //判断回流点位是否被占用
                    if (outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getColdSandMoldRefluxPositionIndex()).getStatus().equals(UNOCCUPIED)){
                        //前往开模点卸载的事件
                        if (coldMoldRefluxStateChange){
                            //前往回流点前的等待位置处
                            simulationOutput = mouldingEventFlow.FullColdGoRefluxPositionEventCreate(simulationOutput);

//                            log.info("冷砂模具放置");
                            coldMoldRefluxStateChange = false;
                        }else {
                            double executionTime = (Math.abs(input.getColdMoldRefluxSubCar().getLocationCoordinate().getY() - outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getColdSandMoldRefluxPositionIndex()).getCoordinate().getY())
                                    / input.getColdMoldRefluxSubCar().getFullSpeed()) + input.getColdMoldRefluxSubCar().getTopRodRaiseOrFallTime();
                            coldMoldRefluxTime = input.getTotalTime() + executionTime;

//                            log.info("冷砂模具放置？" + coldMoldRefluxTime);
                            coldMoldRefluxStateChange = true;

                            eventTime = executionTime;
                        }
                    }

                    //子车未到等待点
                }else {
                    //子车发前往等待点的事件
                    if (coldMoldRefluxStateChange){
                        //前往回流点前的等待位置处
                        simulationOutput = mouldingEventFlow.FullColdGoRefluxWaitPositionEventCreate(simulationOutput, coldMoldRefluxWaitCoordinate);

//                        log.info("冷砂前往等待点");
                        coldMoldRefluxStateChange = false;
                    }else {
                        double executionTime = Math.abs(input.getColdMoldRefluxSubCar().getLocationCoordinate().getY() - coldMoldRefluxWaitCoordinate.getY())
                                / input.getColdMoldRefluxSubCar().getFullSpeed();
                        coldMoldRefluxTime = input.getTotalTime() + executionTime;

//                        log.info("冷砂前往等待点？" + coldMoldRefluxTime);
                        coldMoldRefluxStateChange = true;

                        eventTime = executionTime;
                    }
                }
            }
        }

        if (eventTime != 0.0){
            timeList[16] = eventTime;
            eventTime = 0.0;
        }

        Coordinate hotSprayWaitCoordinate = new Coordinate(input.getHotSandBoxSprayDownPosition().getCoordinate().getX(),
                (input.getHotSandBoxSprayDownPosition().getCoordinate().getY() + input.getHotSandBoxOpeningPosition_2().getCoordinate().getY()) / 2);

//        if (input.getTotalTime() >= hotSprayTime){
//            //判断子车是否满载
//            if (allTool.judgeSubCarIsEmpty(input.getHotSpraySandBoxSubCar())){
//                //判断子车是否到达中点位置
//                if (input.getHotSpraySandBoxSubCar().getLocationCoordinate().equals(hotSprayWaitCoordinate)){
//                    //判断上方喷涂机器正在工作且开模砂箱放置点位被占用
//                    if ((input.getHotSpray_1().getEquipmentStatus().equals(EquipmentOperationStatusEnum.WORKING) ||
//                            input.getHotSpray_1().getEquipmentStatus().equals(EquipmentOperationStatusEnum.FINISHED)) &&
//                            input.getHotSandBoxOpeningPosition_2().getStatus().equals(OCCUPIED)){
//                        if (hotSprayStateChange){
//                            //前往热砂开模砂箱放置点搬运砂箱
//                            simulationOutput = mouldingEventFlow.EmptyHotSprayGoPositionEventCreate(simulationOutput);
//
////                            log.info("发生？");
//                            hotSprayStateChange = false;
//                        }else {
//                            double executionTime = (Math.abs(input.getHotSpraySandBoxSubCar().getLocationCoordinate().getY() - input.getHotSandBoxOpeningPosition_2().getCoordinate().getY())
//                                    / input.getHotSpraySandBoxSubCar().getEmptySpeed()) + input.getHotSpraySandBoxSubCar().getTopRodRaiseOrFallTime();
//                            hotSprayTime = input.getTotalTime() + executionTime;
//                            hotSprayStateChange = true;
//                            //这个地方存在一个问题，当车辆预计时间完成时，点位上的产品有可能会被搬走，此时，车辆不发生搬运的事件，而在下次满足搬运条件时，立刻发生搬运事件
//                            //！！！！！！
//                            eventTime = executionTime;
//                        }
//                    }
//                }else {
//                    //子车发前往等待点的事件
//                    if (hotSprayStateChange){
//                        //前往回流点前的等待位置处
//                        simulationOutput = mouldingEventFlow.EmptyHotSprayGoWaitPositionEventCreate(simulationOutput, hotSprayWaitCoordinate);
//
//                        hotSprayStateChange = false;
//                    }else {
//                        double executionTime = Math.abs(input.getHotSpraySandBoxSubCar().getLocationCoordinate().getY() - hotSprayWaitCoordinate.getY())
//                                / input.getHotSpraySandBoxSubCar().getEmptySpeed();
//                        hotSprayTime = input.getTotalTime() + executionTime;
//                        hotSprayStateChange = true;
//                        eventTime = executionTime;
//                    }
//                }
//
//                //子车满载
//            }else {
//                //发生前往下点放置砂箱的事件
//                if (hotSprayStateChange){
//                    //前往回流点前的等待位置处
//                    simulationOutput = mouldingEventFlow.FullHoSprayGoDownPositionEventCreate(simulationOutput);
//
//                    hotSprayStateChange = false;
//                }else {
//                    double executionTime = (Math.abs(input.getHotSpraySandBoxSubCar().getLocationCoordinate().getY() - input.getHotSandBoxSprayDownPosition().getCoordinate().getY())
//                            / input.getHotSpraySandBoxSubCar().getFullSpeed()) + input.getHotSpraySandBoxSubCar().getTopRodRaiseOrFallTime();
//                    hotSprayTime = input.getTotalTime() + executionTime;
//                    hotSprayStateChange = true;
//                    eventTime = executionTime;
//                }
//            }
//        }

//        if (eventTime != 0.0){
//            timeList[17] = eventTime;
//            eventTime = 0.0;
//        }

        Coordinate coldSprayWaitCoordinate = new Coordinate(input.getColdSandBoxOpeningPosition().getCoordinate().getX(),
                (input.getColdSandBoxOpeningPosition().getCoordinate().getY() + input.getColdSandBoxSprayPosition_1().getCoordinate().getY()) / 2);

        if (input.getTotalTime() >= coldSprayTime){
            //判断子车是否满载
            if (allTool.judgeSubCarIsEmpty(input.getColdSandBoxSubCar())){
                //判断车辆是否到达等待点位
                if (input.getColdSandBoxSubCar().getLocationCoordinate().equals(coldSprayWaitCoordinate)){
                    //判断冷砂开模砂箱放置点位是否被占用
                    if (input.getColdSandBoxOpeningPosition().getStatus().equals(OCCUPIED)){
                        //子车发前往等待点的事件
                        if (coldSprayStateChange){
                            //前往回流点前的等待位置处
                            simulationOutput = mouldingEventFlow.EmptyColdSprayGoPositionEventCreate(simulationOutput);

                            coldSprayStateChange = false;
                        }else {
                            double executionTime = (Math.abs(input.getColdSandBoxSubCar().getLocationCoordinate().getY() - input.getColdSandBoxOpeningPosition().getCoordinate().getY())
                                    / input.getColdSandBoxSubCar().getEmptySpeed()) + input.getColdSandBoxSubCar().getTopRodRaiseOrFallTime();
                            coldSprayTime = input.getTotalTime() + executionTime;
                            coldSprayStateChange = true;

                            eventTime = executionTime;
                        }
                    }
                }else {
                    //子车发前往等待点的事件
                    if (coldSprayStateChange){
                        //前往回流点前的等待位置处
                        simulationOutput = mouldingEventFlow.EmptyColdSprayGoWaitPositionEventCreate(simulationOutput, coldSprayWaitCoordinate);

                        coldSprayStateChange = false;
                    }else {
                        double executionTime = Math.abs(input.getColdSandBoxSubCar().getLocationCoordinate().getY() - coldSprayWaitCoordinate.getY())
                                / input.getColdSandBoxSubCar().getEmptySpeed();
                        coldSprayTime = input.getTotalTime() + executionTime;
                        coldSprayStateChange = true;

                        eventTime = executionTime;
                    }
                }

                //子车满载
            }else {
                //判断下方点位空闲且上方点位空闲
                if (input.getColdSandBoxSprayPosition_2().getStatus().equals(UNOCCUPIED) && input.getColdSandBoxSprayPosition_1().getStatus().equals(UNOCCUPIED)){
                    //子车发前往下放置点的事件
                    if (coldSprayStateChange){
                        //前往回流点前的等待位置处
                        simulationOutput = mouldingEventFlow.FullColdSprayGoDownPositionEventCreate(simulationOutput);

                        coldSprayStateChange = false;
                    }else {
                        double executionTime = (Math.abs(input.getColdSandBoxSubCar().getLocationCoordinate().getY() - input.getColdSandBoxSprayPosition_2().getCoordinate().getY())
                                / input.getColdSandBoxSubCar().getFullSpeed()) + input.getColdSandBoxSubCar().getTopRodRaiseOrFallTime();
                        coldSprayTime = input.getTotalTime() + executionTime;
                        coldSprayStateChange = true;

                        eventTime = executionTime;
                    }

                    //判断上方点位空闲
                }else if (input.getColdSandBoxSprayPosition_1().getStatus().equals(UNOCCUPIED)){
                    //子车发前往上放置点的事件
                    if (coldSprayStateChange){
                        //前往回流点前的等待位置处
                        simulationOutput = mouldingEventFlow.FullColdSprayGoPositionEventCreate(simulationOutput);

                        coldSprayStateChange = false;
                    }else {
                        double executionTime = (Math.abs(input.getColdSandBoxSubCar().getLocationCoordinate().getY() - input.getColdSandBoxSprayPosition_1().getCoordinate().getY())
                                / input.getColdSandBoxSubCar().getFullSpeed()) + input.getColdSandBoxSubCar().getTopRodRaiseOrFallTime();
                        coldSprayTime = input.getTotalTime() + executionTime;
                        coldSprayStateChange = true;

                        eventTime = executionTime;
                    }
                }
            }
        }

        if (eventTime != 0.0){
            timeList[18] = eventTime;
            eventTime = 0.0;
        }

        //热砂喷涂上设备仿真模拟
        if (hotSprayUp.isWorkFlag()){
            if (hotSprayUp.SprayEquipmentWorkSimulation(10)){
                //设备显示为工作完成
                input.getHotSpray_1().setEquipmentStatus(EquipmentOperationStatusEnum.FINISHED);
                if (input.getHotSpray_1().getProducts().getFirst().getProductType().equals(ProductTypeEnum.UPPER_BOX)){
                    input.getHotSpray_1().getProducts().getFirst().setProductType(ProductTypeEnum.UPPER_SAND_SHELL_BOX);
                }else if (input.getHotSpray_1().getProducts().getFirst().getProductType().equals(ProductTypeEnum.LOWER_BOX)){
                    input.getHotSpray_1().getProducts().getFirst().setProductType(ProductTypeEnum.LOWER_SAND_SHELL_BOX);
                }

                hotSprayUp.setWorkFlag(false);
            }
        }else {
            if (input.getHotSandBoxOpeningPosition_2().getStatus().equals(OCCUPIED)
                    && input.getHotSpray_1().getEquipmentStatus().equals(EquipmentOperationStatusEnum.STANDBY)){
                hotSprayUp.setWorkFlag(true);
                List<Product> products = new ArrayList<>();
                products.add(input.getHotSandBoxOpeningPosition_2().getProduct().productDeepCopy());
                input.getHotSandBoxOpeningPosition_2().setProduct(null);
                input.getHotSandBoxOpeningPosition_2().setStatus(UNOCCUPIED);
                input.getHotSpray_1().setProducts(products);
                input.getHotSpray_1().setEquipmentStatus(EquipmentOperationStatusEnum.WORKING);
            }
        }

        //热砂喷涂下设备仿真模拟
        if (hotSprayDown.isWorkFlag()){
            if (hotSprayDown.SprayEquipmentWorkSimulation(10)){
                //设备显示为工作完成
                input.getHotSpray_3().setEquipmentStatus(EquipmentOperationStatusEnum.FINISHED);
                if (input.getHotSpray_3().getProducts().getFirst().getProductType().equals(ProductTypeEnum.UPPER_BOX)){
                    input.getHotSpray_3().getProducts().getFirst().setProductType(ProductTypeEnum.UPPER_SAND_SHELL_BOX);
                }else if (input.getHotSpray_3().getProducts().getFirst().getProductType().equals(ProductTypeEnum.LOWER_BOX)){
                    input.getHotSpray_3().getProducts().getFirst().setProductType(ProductTypeEnum.LOWER_SAND_SHELL_BOX);
                }

                hotSprayDown.setWorkFlag(false);
            }
        }else {
            if (input.getHotSandBoxSprayDownPosition().getStatus().equals(OCCUPIED)
                    && input.getHotSpray_3().getEquipmentStatus().equals(EquipmentOperationStatusEnum.STANDBY)){
                hotSprayDown.setWorkFlag(true);
                List<Product> products = new ArrayList<>();
                products.add(input.getHotSandBoxSprayDownPosition().getProduct().productDeepCopy());
                input.getHotSandBoxSprayDownPosition().setProduct(null);
                input.getHotSandBoxSprayDownPosition().setStatus(UNOCCUPIED);
                input.getHotSpray_3().setProducts(products);
                input.getHotSpray_3().setEquipmentStatus(EquipmentOperationStatusEnum.WORKING);
            }
        }

        //冷砂喷涂上设备仿真模拟
        if (coldSprayUp.isWorkFlag()){
            if (coldSprayUp.SprayEquipmentWorkSimulation(10)){
                //设备显示为工作完成
                input.getColdSpray_1().setEquipmentStatus(EquipmentOperationStatusEnum.FINISHED);
                if (input.getColdSpray_1().getProducts().getFirst().getProductType().equals(ProductTypeEnum.UPPER_BOX)){
                    input.getColdSpray_1().getProducts().getFirst().setProductType(ProductTypeEnum.UPPER_SAND_SHELL_BOX);

                    log.info("上冷砂喷涂完成的上砂箱" + input.getColdSpray_1().getProducts().getFirst());
                }else if (input.getColdSpray_1().getProducts().getFirst().getProductType().equals(ProductTypeEnum.LOWER_BOX)){
                    input.getColdSpray_1().getProducts().getFirst().setProductType(ProductTypeEnum.LOWER_SAND_SHELL_BOX);

                    log.info("上冷砂喷涂完成的下砂箱" + input.getColdSpray_1().getProducts().getFirst());
                }

                simulationOutput = tools.createColdSparyUpMokeEventFlow(simulationOutput, analogInput);
                coldSprayUp.setWorkFlag(false);
            }
        }else {
            if (input.getColdSandBoxSprayPosition_1().getStatus().equals(OCCUPIED)
                    && input.getColdSpray_1().getEquipmentStatus().equals(EquipmentOperationStatusEnum.STANDBY)){
                coldSprayUp.setWorkFlag(true);
                List<Product> products = new ArrayList<>();
                products.add(input.getColdSandBoxSprayPosition_1().getProduct().productDeepCopy());
                input.getColdSandBoxSprayPosition_1().setProduct(null);
                input.getColdSandBoxSprayPosition_1().setStatus(UNOCCUPIED);
                input.getColdSpray_1().setProducts(products);
                input.getColdSpray_1().setEquipmentStatus(EquipmentOperationStatusEnum.WORKING);
            }
        }

        //冷砂喷涂下设备仿真模拟
        if (coldSprayDown.isWorkFlag()){
            if (coldSprayDown.SprayEquipmentWorkSimulation(10)){
                //设备显示为工作完成
                input.getColdSpray_3().setEquipmentStatus(EquipmentOperationStatusEnum.FINISHED);
                if (input.getColdSpray_3().getProducts().getFirst().getProductType().equals(ProductTypeEnum.UPPER_BOX)){
                    input.getColdSpray_3().getProducts().getFirst().setProductType(ProductTypeEnum.UPPER_SAND_SHELL_BOX);

                    log.info("下冷砂喷涂完成的上砂箱" + input.getColdSpray_3().getProducts().getFirst());
                }else if (input.getColdSpray_3().getProducts().getFirst().getProductType().equals(ProductTypeEnum.LOWER_BOX)){
                    input.getColdSpray_3().getProducts().getFirst().setProductType(ProductTypeEnum.LOWER_SAND_SHELL_BOX);

                    log.info("下冷砂喷涂完成的下砂箱" + input.getColdSpray_3().getProducts().getFirst());
                }

                simulationOutput = tools.createColdSparyDownMokeEventFlow(simulationOutput, analogInput);
                coldSprayDown.setWorkFlag(false);
            }
        }else {
            if (input.getColdSandBoxSprayPosition_1().getStatus().equals(OCCUPIED)
                    && input.getColdSpray_3().getEquipmentStatus().equals(EquipmentOperationStatusEnum.STANDBY)){
                coldSprayDown.setWorkFlag(true);
                List<Product> products = new ArrayList<>();
                products.add(input.getColdSandBoxSprayPosition_1().getProduct().productDeepCopy());
                input.getColdSandBoxSprayPosition_1().setProduct(null);
                input.getColdSandBoxSprayPosition_1().setStatus(UNOCCUPIED);
                input.getColdSpray_3().setProducts(products);
                input.getColdSpray_3().setEquipmentStatus(EquipmentOperationStatusEnum.WORKING);
            }
        }

        //热砂喷涂前往合箱
        if (input.getTotalTime() >= hotSprayGoClosingTime){
            //判断子车是否装载
            if (allTool.judgeSubCarIsEmpty(input.getHotSprayGoClosingBoxSubCar())){
                //判断上喷涂设备是否喷涂完成
                if (input.getHotSpray_1().getEquipmentStatus().equals(EquipmentOperationStatusEnum.FINISHED)){
                    //前往上喷涂点搬运砂箱事件
                    if (hotClosingStateChange){
                        //前往回流点前的等待位置处
                        simulationOutput = sprayGoClosingEventFlow.EmptyHotSprayGoClosingBoxEventCreate(simulationOutput);

                        hotClosingStateChange = false;
                    }else {
                        double executionTime = (Math.abs(input.getHotSprayGoClosingBoxferry().getLocationCoordinate().getY() -
                                input.getHotSpray_1().getEquipmentCoordinate().getY()) / input.getHotSprayGoClosingBoxferry().getEmptySpeed()) +
                                (Math.abs(input.getHotSprayGoClosingBoxSubCar().getLocationCoordinate().getX() - input.getHotSpray_1().getEquipmentCoordinate().getX())
                                        / input.getHotSprayGoClosingBoxSubCar().getEmptySpeed()) + input.getHotSprayGoClosingBoxSubCar().getTopRodRaiseOrFallTime() +
                                (Math.abs(input.getHotSprayGoClosingBoxSubCar().getLocationCoordinate().getX() - input.getHotSpray_1().getEquipmentCoordinate().getX())
                                        / input.getHotSprayGoClosingBoxSubCar().getFullSpeed());
                        hotSprayGoClosingTime = input.getTotalTime() + executionTime;
                        hotClosingStateChange = true;

                        eventTime = executionTime;
                    }

                    log.info("前往热砂喷涂设备上装载" + hotSprayGoClosingTime + input.getHotSpray_1().getProducts());
                    //判断下喷涂设备是否喷涂完成
                }else if (input.getHotSpray_3().getEquipmentStatus().equals(EquipmentOperationStatusEnum.FINISHED)){
                    //前往下喷涂点搬运砂箱事件
                    if (hotClosingStateChange){
                        //前往回流点前的等待位置处
                        simulationOutput = sprayGoClosingEventFlow.EmptyHotSprayGoClosingBoxDownEventCreate(simulationOutput);

                        hotClosingStateChange = false;
                    }else {
                        double executionTime = (Math.abs(input.getHotSprayGoClosingBoxferry().getLocationCoordinate().getY() -
                                input.getHotSpray_3().getEquipmentCoordinate().getY()) / input.getHotSprayGoClosingBoxferry().getEmptySpeed()) +
                                (Math.abs(input.getHotSprayGoClosingBoxSubCar().getLocationCoordinate().getX() - input.getHotSpray_3().getEquipmentCoordinate().getX())
                                        / input.getHotSprayGoClosingBoxSubCar().getEmptySpeed()) + input.getHotSprayGoClosingBoxSubCar().getTopRodRaiseOrFallTime() +
                                (Math.abs(input.getHotSprayGoClosingBoxSubCar().getLocationCoordinate().getX() - input.getHotSpray_3().getEquipmentCoordinate().getX())
                                        / input.getHotSprayGoClosingBoxSubCar().getFullSpeed());
                        hotSprayGoClosingTime = input.getTotalTime() + executionTime;
                        hotClosingStateChange = true;

                        eventTime = executionTime;
                    }

                    log.info("前往热砂喷涂设备下装载" + hotSprayGoClosingTime + input.getHotSpray_3().getProducts());

                }
            }else {
                //判断前往合箱的点位是否被占用
                if (simulationOutput.getClosingBoxInput().getInput().getHotClosingBoxPosition().getStatus().equals(UNOCCUPIED)){
                    //发生前往热砂合箱点位装载砂箱的事件
                    if (hotClosingStateChange){
                        //前往回流点前的等待位置处
                        simulationOutput = sprayGoClosingEventFlow.FullHotSprayGoClosingBoxEventCreate(simulationOutput);

                        hotClosingStateChange = false;
                    }else {
                        double executionTime = (Math.abs(input.getHotSprayGoClosingBoxferry().getLocationCoordinate().getY() -
                                simulationOutput.getClosingBoxInput().getInput().getHotClosingBoxPosition().getCoordinate().getY()) / input.getHotSprayGoClosingBoxferry().getFullSpeed()) +
                                (Math.abs(input.getHotSprayGoClosingBoxSubCar().getLocationCoordinate().getX() - simulationOutput.getClosingBoxInput().getInput().getHotClosingBoxPosition().getCoordinate().getX())
                                        / input.getHotSprayGoClosingBoxSubCar().getFullSpeed()) + input.getHotSprayGoClosingBoxSubCar().getTopRodRaiseOrFallTime() +
                                (Math.abs(input.getHotSprayGoClosingBoxSubCar().getLocationCoordinate().getX() - simulationOutput.getClosingBoxInput().getInput().getHotClosingBoxPosition().getCoordinate().getX())
                                        / input.getHotSprayGoClosingBoxSubCar().getEmptySpeed());
                        hotSprayGoClosingTime = input.getTotalTime() + executionTime;
                        hotClosingStateChange = true;

                        eventTime = executionTime;
                    }
                }
            }
        }

        if (eventTime != 0.0){
            timeList[19] = eventTime;
            eventTime = 0.0;
        }

        //冷砂喷涂前往合箱
        if (input.getTotalTime() >= coldSprayGoClosingTime){
            //判断子车是否装载
            if (allTool.judgeSubCarIsEmpty(input.getColdSprayGoClosingBoxSubCar())){
                //判断上喷涂设备是否喷涂完成
                if (input.getColdSpray_1().getEquipmentStatus().equals(EquipmentOperationStatusEnum.FINISHED)){
                    //前往上喷涂点搬运砂箱事件
                    if (coldClosingStateChange){
                        //前往回流点前的等待位置处
                        simulationOutput = sprayGoClosingEventFlow.EmptyColdSprayGoClosingBoxUpEventCreate(simulationOutput);

                        coldClosingStateChange = false;
                    }else {
                        double executionTime = (Math.abs(input.getColdSprayGoClosingBoxferry().getLocationCoordinate().getY() -
                                input.getColdSpray_1().getEquipmentCoordinate().getY()) / input.getColdSprayGoClosingBoxferry().getEmptySpeed()) +
                                (Math.abs(input.getColdSprayGoClosingBoxSubCar().getLocationCoordinate().getX() - input.getColdSpray_1().getEquipmentCoordinate().getX())
                                        / input.getColdSprayGoClosingBoxSubCar().getEmptySpeed()) + input.getColdSprayGoClosingBoxSubCar().getTopRodRaiseOrFallTime() +
                                (Math.abs(input.getColdSprayGoClosingBoxSubCar().getLocationCoordinate().getX() - input.getColdSpray_1().getEquipmentCoordinate().getX())
                                        / input.getColdSprayGoClosingBoxSubCar().getFullSpeed());
                        coldSprayGoClosingTime = input.getTotalTime() + executionTime;
                        coldClosingStateChange = true;

                        eventTime = executionTime;
                    }

                    log.info("前往冷砂喷涂设备上装载" + coldSprayGoClosingTime + input.getColdSpray_1().getProducts());
                    //判断下喷涂设备是否喷涂完成
                }else if (input.getColdSpray_3().getEquipmentStatus().equals(EquipmentOperationStatusEnum.FINISHED)){
                    //前往下喷涂点搬运砂箱事件
                    if (coldClosingStateChange){
                        //前往回流点前的等待位置处
                        simulationOutput = sprayGoClosingEventFlow.EmptyColdSprayGoClosingBoxDownEventCreate(simulationOutput);

                        coldClosingStateChange = false;
                    }else {
                        double executionTime = (Math.abs(input.getColdSprayGoClosingBoxferry().getLocationCoordinate().getY() -
                                input.getColdSpray_3().getEquipmentCoordinate().getY()) / input.getColdSprayGoClosingBoxferry().getEmptySpeed()) +
                                (Math.abs(input.getColdSprayGoClosingBoxSubCar().getLocationCoordinate().getX() - input.getColdSpray_3().getEquipmentCoordinate().getX())
                                        / input.getColdSprayGoClosingBoxSubCar().getEmptySpeed()) + input.getColdSprayGoClosingBoxSubCar().getTopRodRaiseOrFallTime() +
                                (Math.abs(input.getColdSprayGoClosingBoxSubCar().getLocationCoordinate().getX() - input.getColdSpray_3().getEquipmentCoordinate().getX())
                                        / input.getColdSprayGoClosingBoxSubCar().getFullSpeed());
                        coldSprayGoClosingTime = input.getTotalTime() + executionTime;
                        coldClosingStateChange = true;

                        eventTime = executionTime;
                    }

                    log.info("前往冷砂喷涂设备下装载" + coldSprayGoClosingTime + input.getColdSpray_3().getProducts());
                }
            }else {
                //判断前往合箱的点位是否被占用
                if (simulationOutput.getClosingBoxInput().getInput().getColdClosingBoxPosition().getStatus().equals(UNOCCUPIED)){
                    //发生前往冷砂合箱点位装载砂箱的事件
                    if (coldClosingStateChange){
                        //前往回流点前的等待位置处
                        simulationOutput = sprayGoClosingEventFlow.FullColdSprayGoClosingBoxEventCreate(simulationOutput);

                        coldClosingStateChange = false;
                    }else {
                        double executionTime = (Math.abs(input.getColdSprayGoClosingBoxferry().getLocationCoordinate().getY() -
                                simulationOutput.getClosingBoxInput().getInput().getColdClosingBoxPosition().getCoordinate().getY()) / input.getColdSprayGoClosingBoxferry().getFullSpeed()) +
                                (Math.abs(input.getColdSprayGoClosingBoxSubCar().getLocationCoordinate().getX() - simulationOutput.getClosingBoxInput().getInput().getColdClosingBoxPosition().getCoordinate().getX())
                                        / input.getColdSprayGoClosingBoxSubCar().getFullSpeed()) + input.getColdSprayGoClosingBoxSubCar().getTopRodRaiseOrFallTime() +
                                (Math.abs(input.getColdSprayGoClosingBoxSubCar().getLocationCoordinate().getX() - simulationOutput.getClosingBoxInput().getInput().getColdClosingBoxPosition().getCoordinate().getX())
                                        / input.getColdSprayGoClosingBoxSubCar().getEmptySpeed());
                        coldSprayGoClosingTime = input.getTotalTime() + executionTime;
                        coldClosingStateChange = true;

                        eventTime = executionTime;
                    }
                }
            }
        }

        if (eventTime != 0.0){
            timeList[20] = eventTime;
            eventTime = 0.0;
        }

        return simulationOutput;
    }
}
