package cn.hex.ddp.manufacture.infrastructure.algorithm.simulation;

import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductFinishEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.CoolingSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.PouringSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.WaitingPourSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.pouring.PouringEventFlow;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.pouring.PouringSimulationTools;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.pouring.PouringWorkStationEventFlow;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.waitingPour.AfterClosingBoxAllLink;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/31 14:25
 */
@Slf4j
public class PouringSimulation {
    /**
     * 前往浇筑区时间
     */
    private double pouringTime = 0.0;
    /**
     * 前往浇筑工岗时间
     */
    private double workstationPouringTime_1 = 0.0;
    private double workstationPouringTime_2 = 0.0;
    private double workstationPouringTime_3 = 0.0;
    private double workstationPouringTime_4 = 0.0;
    private double workstationPouringTime_5 = 0.0;
    private double workstationPouringTime_6 = 0.0;
    private double workstationPouringTime_7 = 0.0;
    private double workstationPouringTime_8 = 0.0;
    private double workstationPouringTime_9 = 0.0;
    private double workstationPouringTime_10 = 0.0;
    private double workstationPouringTime_11 = 0.0;
    private double workstationPouringTime_12 = 0.0;
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean pouringStateChange = false;
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean workstationStateChange_1 = false;
    private boolean workstationStateChange_2 = false;
    private boolean workstationStateChange_3 = false;
    private boolean workstationStateChange_4 = false;
    private boolean workstationStateChange_5 = false;
    private boolean workstationStateChange_6 = false;
    private boolean workstationStateChange_7 = false;
    private boolean workstationStateChange_8 = false;
    private boolean workstationStateChange_9 = false;
    private boolean workstationStateChange_10 = false;
    private boolean workstationStateChange_11 = false;
    private boolean workstationStateChange_12 = false;
    /**
     * 保证逻辑判断不会重复出现的加锁
     */
    private boolean carEventLock = false;
    /**
     * 仿真总工具类
     */
    private final SimulationTool allTool = new SimulationTool();
    /**
     * 浇筑区的工具类
     */
    private final PouringSimulationTools tools = new PouringSimulationTools();
    /**
     * 创建事件的类
     */
    private final PouringEventFlow pouringEventFlow = new PouringEventFlow();
    /**
     * 创建事件的类
     */
    private final PouringWorkStationEventFlow pouringWorkStationEventFlow = new PouringWorkStationEventFlow();

    private double eventTime = 0.0;

    public AfterClosingBoxAllLink PouringSimulationRun(AfterClosingBoxAllLink afterClosingBoxAllLink, Double[] timeList){
        AfterClosingBoxAllLink simulationOutput = SerializationUtils.clone(afterClosingBoxAllLink);
        //获取浇筑区和待浇筑区的放置输入
        PouringSimulationInput input = simulationOutput.getPouringInput().getInput();
        WaitingPourSimulationInput waitingPourInput = simulationOutput.getWaitingPourInput().getInput();
        CoolingSimulationInput coolingInput = simulationOutput.getCoolingInput().getInput();

        //判断浇筑区是否可以发生事件
        if (input.getTotalTime() >= pouringTime){
            //判断子车是否空载
            if (allTool.judgeSubCarIsEmpty(input.getPouringSubCar())){
                //判断是否有工岗处于待搬运的状态
                if (tools.judgeIsGoWhichWorkStation(input.getWorkingStations()) && !carEventLock){
                    //获得要去的工岗的位置标识
                    int workStationIndex = tools.judgeGoWhichWorkStation(input.getWorkingStations(), input.getPouringIndexInput());
                    if (workStationIndex != -1){
                        //为保证车辆去其中一个工岗搬运待搬运的砂箱不会出现重复判断的错误，这里的事件执行不用加锁，不用防止状态提前改变影响其他区域，因为这个事件对其他区域没有影响
                        double executionTime = (Math.abs(input.getPouringFerry().getLocationCoordinate().getY() -
                                input.getWorkingStations().get(workStationIndex).getWorkPositionCoordinate().getY()) /
                                input.getPouringFerry().getEmptySpeed()) + (Math.abs(input.getPouringSubCar().getLocationCoordinate().getX() -
                                input.getWorkingStations().get(workStationIndex).getWorkPositionCoordinate().getX()) / input.getPouringSubCar().getEmptySpeed()) +
                                input.getPouringSubCar().getTopRodRaiseOrFallTime() + (Math.abs(input.getPouringSubCar().getLocationCoordinate().getX() -
                                input.getWorkingStations().get(workStationIndex).getWorkPositionCoordinate().getX()) / input.getPouringSubCar().getFullSpeed());

                        pouringTime = input.getTotalTime() + executionTime;

                        simulationOutput = pouringEventFlow.emptyGoWorkStationLoadEventCreate(simulationOutput, workStationIndex);

                        eventTime = executionTime;

                        log.info("前往工岗，装载产品");
                    }else {
                        log.info("ERROR: 空载浇筑区前往工岗判断出错!!!");
                    }

                    //判断是否存在空闲的工岗
                }else if (tools.judgeIsExistFreeWorkStation(input.getWorkingStations())){
                    //判断待浇筑上半区是否有产品待浇筑
                    if (tools.judgeWaitingPourIsEmpty(waitingPourInput.getUpPositions())){
                        //判断车辆前往的轨道
                        int pathIndex = tools.judgePouringCarGoWhichPath(waitingPourInput.getUpPositions());
                        int positionIndex = tools.judgePouringCarGoWhichPosition(waitingPourInput.getUpPositions(), pathIndex);
                        if (pathIndex != -1 && positionIndex != -1){
                            if (pouringStateChange){
                                simulationOutput = pouringEventFlow.emptyGoUpWaitingPourPositionEventCreate(simulationOutput, pathIndex, positionIndex);

                                pouringStateChange = false;
                                carEventLock = false;
                            }else {
                                double executionTime = (Math.abs(input.getPouringFerry().getLocationCoordinate().getY() -
                                        waitingPourInput.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate().getY()) /
                                        input.getPouringFerry().getEmptySpeed()) + (Math.abs(input.getPouringSubCar().getLocationCoordinate().getX() -
                                        waitingPourInput.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getPouringSubCar().getEmptySpeed()) +
                                        input.getPouringSubCar().getTopRodRaiseOrFallTime() + (Math.abs(input.getPouringSubCar().getLocationCoordinate().getX() -
                                        waitingPourInput.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getPouringSubCar().getFullSpeed());

                                pouringTime = input.getTotalTime() + executionTime;
                                pouringStateChange = true;
                                carEventLock = true;

                                eventTime = executionTime;
                            }

                            log.info("前往上待浇筑轨道，装载产品");
                        }

                        //判断待浇筑下半区是否有产品待浇筑
                    }else if (tools.judgeWaitingPourIsEmpty(waitingPourInput.getDownPositions())){
                        //判断车辆前往的轨道
                        int pathIndex = tools.judgePouringCarGoWhichPath(waitingPourInput.getDownPositions());
                        int positionIndex = tools.judgePouringCarGoWhichPosition(waitingPourInput.getDownPositions(), pathIndex);
                        if (pathIndex != -1 && positionIndex != -1){
                            if (pouringStateChange){
                                simulationOutput = pouringEventFlow.emptyGoDownWaitingPourPositionEventCreate(simulationOutput, pathIndex, positionIndex);

                                pouringStateChange = false;
                                carEventLock = false;
                            }else {
                                double executionTime = (Math.abs(input.getPouringFerry().getLocationCoordinate().getY() -
                                        waitingPourInput.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate().getY()) /
                                        input.getPouringFerry().getEmptySpeed()) + (Math.abs(input.getPouringSubCar().getLocationCoordinate().getX() -
                                        waitingPourInput.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getPouringSubCar().getEmptySpeed()) +
                                        input.getPouringSubCar().getTopRodRaiseOrFallTime() + (Math.abs(input.getPouringSubCar().getLocationCoordinate().getX() -
                                        waitingPourInput.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getPouringSubCar().getFullSpeed());

                                pouringTime = input.getTotalTime() + executionTime;
                                pouringStateChange = true;
                                carEventLock = true;

                                eventTime = executionTime;
                            }

                            log.info("前往下待浇筑轨道，装载产品");
                        }
                    }
                }

                //子车满载
            }else {
                //判断子车装载的产品是否完成浇筑
                if (input.getPouringSubCar().getProduct().getProductFinish().equals(ProductFinishEnum.FINISH_POURING)){
                    //判断冷却区上半区是否全空
                    if (tools.judgeCoolingIsEmpty(coolingInput.getUpPositions())){
                        //判断车辆前往的轨道
                        int pathIndex = tools.judgeCoolingCarGoWhichPath(coolingInput.getUpPositions());
                        int positionIndex = tools.judgeCoolingCarGoWhichPosition(coolingInput.getUpPositions(), pathIndex);
                        if (pathIndex != -1 && positionIndex != -1){
                            if (pouringStateChange){
                                simulationOutput = pouringEventFlow.fullGoUpCoolingPositionEventCreate(simulationOutput, pathIndex, positionIndex);

                                pouringStateChange = false;
                            }else {
                                double executionTime = (Math.abs(input.getPouringFerry().getLocationCoordinate().getY() -
                                        coolingInput.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate().getY()) /
                                        input.getPouringFerry().getFullSpeed()) + (Math.abs(input.getPouringSubCar().getLocationCoordinate().getX() -
                                        coolingInput.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getPouringSubCar().getFullSpeed()) +
                                        input.getPouringSubCar().getTopRodRaiseOrFallTime() + (Math.abs(input.getPouringSubCar().getLocationCoordinate().getX() -
                                        coolingInput.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getPouringSubCar().getEmptySpeed());

                                pouringTime = input.getTotalTime() + executionTime;
                                pouringStateChange = true;

                                eventTime = executionTime;
                            }

                            log.info("前往上冷却轨道，卸载产品");
                        }

                    }else if (tools.judgeCoolingIsEmpty(coolingInput.getDownPositions())){
                        //判断车辆前往的轨道
                        int pathIndex = tools.judgeCoolingCarGoWhichPath(coolingInput.getDownPositions());
                        int positionIndex = tools.judgeCoolingCarGoWhichPosition(coolingInput.getDownPositions(), pathIndex);
                        if (pathIndex != -1 && positionIndex != -1){
                            if (pouringStateChange){
                                simulationOutput = pouringEventFlow.fullGoDownCoolingPositionEventCreate(simulationOutput, pathIndex, positionIndex);

                                pouringStateChange = false;
                            }else {
                                double executionTime = (Math.abs(input.getPouringFerry().getLocationCoordinate().getY() -
                                        coolingInput.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate().getY()) /
                                        input.getPouringFerry().getFullSpeed()) + (Math.abs(input.getPouringSubCar().getLocationCoordinate().getX() -
                                        coolingInput.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getPouringSubCar().getFullSpeed()) +
                                        input.getPouringSubCar().getTopRodRaiseOrFallTime() + (Math.abs(input.getPouringSubCar().getLocationCoordinate().getX() -
                                        coolingInput.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getPouringSubCar().getEmptySpeed());

                                pouringTime = input.getTotalTime() + executionTime;
                                pouringStateChange = true;

                                eventTime = executionTime;
                            }

                            log.info("前往下冷却轨道，卸载产品");
                        }

                    }

                    //未完成浇筑，前往浇筑工岗
                }else {
                    int workStationIndex = tools.judgeFullGoWhichWorkStation(input.getWorkingStations(), input.getPouringIndexInput());
                    if (workStationIndex != -1){
                        //为保证车辆去其中一个工岗搬运待搬运的砂箱不会出现重复判断的错误，这里的事件执行不用加锁，不用防止状态提前改变影响其他区域，因为这个事件对其他区域没有影响
                        double executionTime = (Math.abs(input.getPouringFerry().getLocationCoordinate().getY() -
                                input.getWorkingStations().get(workStationIndex).getWorkPositionCoordinate().getY()) /
                                input.getPouringFerry().getFullSpeed()) + (Math.abs(input.getPouringSubCar().getLocationCoordinate().getX() -
                                input.getWorkingStations().get(workStationIndex).getWorkPositionCoordinate().getX()) / input.getPouringSubCar().getFullSpeed()) +
                                input.getPouringSubCar().getTopRodRaiseOrFallTime() + (Math.abs(input.getPouringSubCar().getLocationCoordinate().getX() -
                                input.getWorkingStations().get(workStationIndex).getWorkPositionCoordinate().getX()) / input.getPouringSubCar().getEmptySpeed());

                        pouringTime = input.getTotalTime() + executionTime;

                        simulationOutput = pouringEventFlow.fullGoWorkStationUnLoadEventCreate(simulationOutput, workStationIndex);

                        eventTime = executionTime;
                        log.info("前往工岗，卸载产品");
                    }else {
                        log.info("ERROR: 满载浇筑区前往工岗判断出错!!!");
                    }
                }
            }
        }

        if (eventTime != 0.0){
            timeList[32] = eventTime;
            eventTime = 0.0;
        }

        //判断浇筑工岗1是否可以发生事件
        if (input.getTotalTime() >= workstationPouringTime_1){
            if (input.getWorkingStations().get(input.getPouringIndexInput().getPouringWorkStationIndex_1()).getStatus().equals(WorkstationStatusEnum.WORKING)){
                if (workstationStateChange_1){
                    simulationOutput = pouringWorkStationEventFlow.pouringWorkStationWorkingEventCreate(simulationOutput, input.getPouringSubCar(), input.getPouringFerry(),
                            input.getPouringIndexInput().getPouringWorkStationIndex_1(), input.getPouringIndexInput().getPouringWorkStationInGanttIndex_1());

                    workstationStateChange_1 = false;

                }else {
                    double executionTime = input.getWorkingStations().get(input.getPouringIndexInput().getPouringWorkStationIndex_1()).getProduct().getFirst().getPouringTime();

                    workstationPouringTime_1 = input.getTotalTime() + executionTime;
                    workstationStateChange_1 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[33] = eventTime;
            eventTime = 0.0;
        }

        //判断浇筑工岗2是否可以发生事件
        if (input.getTotalTime() >= workstationPouringTime_2){
            if (input.getWorkingStations().get(input.getPouringIndexInput().getPouringWorkStationIndex_2()).getStatus().equals(WorkstationStatusEnum.WORKING)){
                if (workstationStateChange_2){
                    simulationOutput = pouringWorkStationEventFlow.pouringWorkStationWorkingEventCreate(simulationOutput, input.getPouringSubCar(), input.getPouringFerry(),
                            input.getPouringIndexInput().getPouringWorkStationIndex_2(), input.getPouringIndexInput().getPouringWorkStationInGanttIndex_2());

                    workstationStateChange_2 = false;

                }else {
                    double executionTime = input.getWorkingStations().get(input.getPouringIndexInput().getPouringWorkStationIndex_2()).getProduct().getFirst().getPouringTime();

                    workstationPouringTime_2 = input.getTotalTime() + executionTime;
                    workstationStateChange_2 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[34] = eventTime;
            eventTime = 0.0;
        }

        //判断浇筑工岗3是否可以发生事件
        if (input.getTotalTime() >= workstationPouringTime_3){
            if (input.getWorkingStations().get(input.getPouringIndexInput().getPouringWorkStationIndex_3()).getStatus().equals(WorkstationStatusEnum.WORKING)){
                if (workstationStateChange_3){
                    simulationOutput = pouringWorkStationEventFlow.pouringWorkStationWorkingEventCreate(simulationOutput, input.getPouringSubCar(), input.getPouringFerry(),
                            input.getPouringIndexInput().getPouringWorkStationIndex_3(), input.getPouringIndexInput().getPouringWorkStationInGanttIndex_3());

                    workstationStateChange_3 = false;

                }else {
                    double executionTime = input.getWorkingStations().get(input.getPouringIndexInput().getPouringWorkStationIndex_3()).getProduct().getFirst().getPouringTime();

                    workstationPouringTime_3 = input.getTotalTime() + executionTime;
                    workstationStateChange_3 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[35] = eventTime;
            eventTime = 0.0;
        }

        //判断浇筑工岗4是否可以发生事件
        if (input.getTotalTime() >= workstationPouringTime_4){
            if (input.getWorkingStations().get(input.getPouringIndexInput().getPouringWorkStationIndex_4()).getStatus().equals(WorkstationStatusEnum.WORKING)){
                if (workstationStateChange_4){
                    simulationOutput = pouringWorkStationEventFlow.pouringWorkStationWorkingEventCreate(simulationOutput, input.getPouringSubCar(), input.getPouringFerry(),
                            input.getPouringIndexInput().getPouringWorkStationIndex_4(), input.getPouringIndexInput().getPouringWorkStationInGanttIndex_4());

                    workstationStateChange_4 = false;

                }else {
                    double executionTime = input.getWorkingStations().get(input.getPouringIndexInput().getPouringWorkStationIndex_4()).getProduct().getFirst().getPouringTime();

                    workstationPouringTime_4 = input.getTotalTime() + executionTime;
                    workstationStateChange_4 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[36] = eventTime;
            eventTime = 0.0;
        }

        //判断浇筑工岗5是否可以发生事件
        if (input.getTotalTime() >= workstationPouringTime_5){
            if (input.getWorkingStations().get(input.getPouringIndexInput().getPouringWorkStationIndex_5()).getStatus().equals(WorkstationStatusEnum.WORKING)){
                if (workstationStateChange_5){
                    simulationOutput = pouringWorkStationEventFlow.pouringWorkStationWorkingEventCreate(simulationOutput, input.getPouringSubCar(), input.getPouringFerry(),
                            input.getPouringIndexInput().getPouringWorkStationIndex_5(), input.getPouringIndexInput().getPouringWorkStationInGanttIndex_5());

                    workstationStateChange_5 = false;

                }else {
                    double executionTime = input.getWorkingStations().get(input.getPouringIndexInput().getPouringWorkStationIndex_5()).getProduct().getFirst().getPouringTime();

                    workstationPouringTime_5 = input.getTotalTime() + executionTime;
                    workstationStateChange_5 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[37] = eventTime;
            eventTime = 0.0;
        }

        //判断浇筑工岗6是否可以发生事件
        if (input.getTotalTime() >= workstationPouringTime_6){
            if (input.getWorkingStations().get(input.getPouringIndexInput().getPouringWorkStationIndex_6()).getStatus().equals(WorkstationStatusEnum.WORKING)){
                if (workstationStateChange_6){
                    simulationOutput = pouringWorkStationEventFlow.pouringWorkStationWorkingEventCreate(simulationOutput, input.getPouringSubCar(), input.getPouringFerry(),
                            input.getPouringIndexInput().getPouringWorkStationIndex_6(), input.getPouringIndexInput().getPouringWorkStationInGanttIndex_6());

                    workstationStateChange_6 = false;

                }else {
                    double executionTime = input.getWorkingStations().get(input.getPouringIndexInput().getPouringWorkStationIndex_6()).getProduct().getFirst().getPouringTime();

                    workstationPouringTime_6 = input.getTotalTime() + executionTime;
                    workstationStateChange_6 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[38] = eventTime;
            eventTime = 0.0;
        }

        //判断浇筑工岗7是否可以发生事件
        if (input.getTotalTime() >= workstationPouringTime_7){
            if (input.getWorkingStations().get(input.getPouringIndexInput().getPouringWorkStationIndex_7()).getStatus().equals(WorkstationStatusEnum.WORKING)){
                if (workstationStateChange_7){
                    simulationOutput = pouringWorkStationEventFlow.pouringWorkStationWorkingEventCreate(simulationOutput, input.getPouringSubCar(), input.getPouringFerry(),
                            input.getPouringIndexInput().getPouringWorkStationIndex_7(), input.getPouringIndexInput().getPouringWorkStationInGanttIndex_7());

                    workstationStateChange_7 = false;

                }else {
                    double executionTime = input.getWorkingStations().get(input.getPouringIndexInput().getPouringWorkStationIndex_7()).getProduct().getFirst().getPouringTime();

                    workstationPouringTime_7 = input.getTotalTime() + executionTime;
                    workstationStateChange_7 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[39] = eventTime;
            eventTime = 0.0;
        }

        //判断浇筑工岗8是否可以发生事件
        if (input.getTotalTime() >= workstationPouringTime_8){
            if (input.getWorkingStations().get(input.getPouringIndexInput().getPouringWorkStationIndex_8()).getStatus().equals(WorkstationStatusEnum.WORKING)){
                if (workstationStateChange_8){
                    simulationOutput = pouringWorkStationEventFlow.pouringWorkStationWorkingEventCreate(simulationOutput, input.getPouringSubCar(), input.getPouringFerry(),
                            input.getPouringIndexInput().getPouringWorkStationIndex_8(), input.getPouringIndexInput().getPouringWorkStationInGanttIndex_8());

                    workstationStateChange_8 = false;

                }else {
                    double executionTime = input.getWorkingStations().get(input.getPouringIndexInput().getPouringWorkStationIndex_8()).getProduct().getFirst().getPouringTime();

                    workstationPouringTime_8 = input.getTotalTime() + executionTime;
                    workstationStateChange_8 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[40] = eventTime;
            eventTime = 0.0;
        }

        //判断浇筑工岗9是否可以发生事件
        if (input.getTotalTime() >= workstationPouringTime_9){
            if (input.getWorkingStations().get(input.getPouringIndexInput().getPouringWorkStationIndex_9()).getStatus().equals(WorkstationStatusEnum.WORKING)){
                if (workstationStateChange_9){
                    simulationOutput = pouringWorkStationEventFlow.pouringWorkStationWorkingEventCreate(simulationOutput, input.getPouringSubCar(), input.getPouringFerry(),
                            input.getPouringIndexInput().getPouringWorkStationIndex_9(), input.getPouringIndexInput().getPouringWorkStationInGanttIndex_9());

                    workstationStateChange_9 = false;

                }else {
                    double executionTime = input.getWorkingStations().get(input.getPouringIndexInput().getPouringWorkStationIndex_9()).getProduct().getFirst().getPouringTime();

                    workstationPouringTime_9 = input.getTotalTime() + executionTime;
                    workstationStateChange_9 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[41] = eventTime;
            eventTime = 0.0;
        }

        //判断浇筑工岗10是否可以发生事件
        if (input.getTotalTime() >= workstationPouringTime_10){
            if (input.getWorkingStations().get(input.getPouringIndexInput().getPouringWorkStationIndex_10()).getStatus().equals(WorkstationStatusEnum.WORKING)){
                if (workstationStateChange_10){
                    simulationOutput = pouringWorkStationEventFlow.pouringWorkStationWorkingEventCreate(simulationOutput, input.getPouringSubCar(), input.getPouringFerry(),
                            input.getPouringIndexInput().getPouringWorkStationIndex_10(), input.getPouringIndexInput().getPouringWorkStationInGanttIndex_10());

                    workstationStateChange_10 = false;

                }else {
                    double executionTime = input.getWorkingStations().get(input.getPouringIndexInput().getPouringWorkStationIndex_10()).getProduct().getFirst().getPouringTime();

                    workstationPouringTime_10 = input.getTotalTime() + executionTime;
                    workstationStateChange_10 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[42] = eventTime;
            eventTime = 0.0;
        }

        //判断浇筑工岗11是否可以发生事件
        if (input.getTotalTime() >= workstationPouringTime_11){
            if (input.getWorkingStations().get(input.getPouringIndexInput().getPouringWorkStationIndex_11()).getStatus().equals(WorkstationStatusEnum.WORKING)){
                if (workstationStateChange_11){
                    simulationOutput = pouringWorkStationEventFlow.pouringWorkStationWorkingEventCreate(simulationOutput, input.getPouringSubCar(), input.getPouringFerry(),
                            input.getPouringIndexInput().getPouringWorkStationIndex_11(), input.getPouringIndexInput().getPouringWorkStationInGanttIndex_11());

                    workstationStateChange_11 = false;

                }else {
                    double executionTime = input.getWorkingStations().get(input.getPouringIndexInput().getPouringWorkStationIndex_11()).getProduct().getFirst().getPouringTime();

                    workstationPouringTime_11 = input.getTotalTime() + executionTime;
                    workstationStateChange_11 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[43] = eventTime;
            eventTime = 0.0;
        }

        //判断浇筑工岗12是否可以发生事件
        if (input.getTotalTime() >= workstationPouringTime_12){
            if (input.getWorkingStations().get(input.getPouringIndexInput().getPouringWorkStationIndex_12()).getStatus().equals(WorkstationStatusEnum.WORKING)){
                if (workstationStateChange_12){
                    simulationOutput = pouringWorkStationEventFlow.pouringWorkStationWorkingEventCreate(simulationOutput, input.getPouringSubCar(), input.getPouringFerry(),
                            input.getPouringIndexInput().getPouringWorkStationIndex_12(), input.getPouringIndexInput().getPouringWorkStationInGanttIndex_12());

                    workstationStateChange_12 = false;

                }else {
                    double executionTime = input.getWorkingStations().get(input.getPouringIndexInput().getPouringWorkStationIndex_12()).getProduct().getFirst().getPouringTime();

                    workstationPouringTime_12 = input.getTotalTime() + executionTime;
                    workstationStateChange_12 = true;

                    eventTime = executionTime;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[44] = eventTime;
            eventTime = 0.0;
        }

        return simulationOutput;
    }
}
