package cn.hex.ddp.manufacture.infrastructure.algorithm.simulation;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.ClosingBoxSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.WaitingPourSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.waitingPour.AfterClosingBoxAllLink;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.waitingPour.WaitingPourEventFlow;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.waitingPour.WaitingPourSimulationTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/25 6:32
 */
@Slf4j
public class WaitingPourSimulation {
    /**
     * 前往待浇筑区时间
     */
    private double goWaitingPourTime = 0.0;
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean stateChange = false;
    /**
     * 仿真总工具类
     */
    private final SimulationTool allTool = new SimulationTool();
    /**
     * 待浇筑区放置的工具类
     */
    private final WaitingPourSimulationTools tools = new WaitingPourSimulationTools();
    /**
     * 创建事件的类
     */
    private final WaitingPourEventFlow eventFlow = new WaitingPourEventFlow();

    private double eventTime = 0.0;

    public AfterClosingBoxAllLink WaitingPourSimulationRun(AfterClosingBoxAllLink afterClosingBoxAllLink, Double[] timeList){
        AfterClosingBoxAllLink simulationOutput = SerializationUtils.clone(afterClosingBoxAllLink);
        //获取合箱区的仿真输入
        ClosingBoxSimulationInput closingBoxInput = simulationOutput.getClosingBoxInput().getInput();
        WaitingPourSimulationInput input = simulationOutput.getWaitingPourInput().getInput();

        //判断前往待浇筑区是否可以发生事件
        if (input.getTotalTime() >= goWaitingPourTime){
            //判断子车是否空载
            if (allTool.judgeSubCarIsEmpty(input.getClosingGoPourSubCar())){
                //判断热砂合箱完成点位是否被占用
                if (closingBoxInput.getHotClosingBoxGoPourPosition().getStatus().equals(PositionStatusEnum.OCCUPIED)){
                    //发生前往点位装载砂箱的事件流
                    if (stateChange){
                        simulationOutput = eventFlow.emptyGoHotClosingPositionEventCreate(simulationOutput);

                        stateChange = false;
                    }else {
                        double executionTime = (Math.abs(input.getClosingGoPourFerry().getLocationCoordinate().getY() -
                                closingBoxInput.getHotClosingBoxGoPourPosition().getCoordinate().getY()) / input.getClosingGoPourFerry().getEmptySpeed()) +
                                (Math.abs(input.getClosingGoPourSubCar().getLocationCoordinate().getX() -
                                        closingBoxInput.getHotClosingBoxGoPourPosition().getCoordinate().getX()) / input.getClosingGoPourSubCar().getEmptySpeed()) +
                                input.getClosingGoPourSubCar().getTopRodRaiseOrFallTime() + (Math.abs(input.getClosingGoPourSubCar().getLocationCoordinate().getX() -
                                closingBoxInput.getHotClosingBoxGoPourPosition().getCoordinate().getX()) / input.getClosingGoPourSubCar().getFullSpeed());

                        goWaitingPourTime = input.getTotalTime() + executionTime;
                        stateChange = true;

                        eventTime = executionTime;
                    }

                    log.info("待浇筑：热砂子车前往Hot点位装载砂箱 " + goWaitingPourTime);
                    //判断冷砂合箱完成点位是否被占用
                }else if (closingBoxInput.getColdClosingBoxGoPourPosition().getStatus().equals(PositionStatusEnum.OCCUPIED)){
                    //发生前往点位装载砂箱的事件流
                    if (stateChange){
                        simulationOutput = eventFlow.emptyGoColdClosingPositionEventCreate(simulationOutput);

                        stateChange = false;
                    }else {
                        double executionTime = (Math.abs(input.getClosingGoPourFerry().getLocationCoordinate().getY() -
                                closingBoxInput.getColdClosingBoxGoPourPosition().getCoordinate().getY()) / input.getClosingGoPourFerry().getEmptySpeed()) +
                                (Math.abs(input.getClosingGoPourSubCar().getLocationCoordinate().getX() -
                                        closingBoxInput.getColdClosingBoxGoPourPosition().getCoordinate().getX()) / input.getClosingGoPourSubCar().getEmptySpeed()) +
                                input.getClosingGoPourSubCar().getTopRodRaiseOrFallTime() + (Math.abs(input.getClosingGoPourSubCar().getLocationCoordinate().getX() -
                                closingBoxInput.getColdClosingBoxGoPourPosition().getCoordinate().getX()) / input.getClosingGoPourSubCar().getFullSpeed());

                        goWaitingPourTime = input.getTotalTime() + executionTime;
                        stateChange = true;

                        eventTime = executionTime;
                    }

                    log.info("待浇筑：冷砂子车前往Cold点位装载砂箱 " + goWaitingPourTime);
                }

                //子车满载
            }else {
                //判断待浇筑上半区是否全空
                if (tools.judgeWaitingPourIsEmpty(input.getUpPositions())){
                    //判断车辆前往的轨道
                    int pathIndex = tools.judgeCarGoWhichPath(input.getUpPositions());
                    int positionIndex = tools.judgeCarGoWhichPosition(input.getUpPositions(), pathIndex);
//                    log.info("???????" + pathIndex + " : " + positionIndex);
                    if (pathIndex != -1 && positionIndex != -1){
                        if (stateChange){
                            simulationOutput = eventFlow.fullGoUpWaitingPourPositionEventCreate(simulationOutput, pathIndex, positionIndex);

                            stateChange = false;
                        }else {
                            double executionTime = (Math.abs(input.getClosingGoPourFerry().getLocationCoordinate().getY() -
                                    input.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate().getY()) /
                                    input.getClosingGoPourFerry().getFullSpeed()) + (Math.abs(input.getClosingGoPourSubCar().getLocationCoordinate().getX() -
                                    input.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getClosingGoPourSubCar().getFullSpeed()) +
                                    input.getClosingGoPourSubCar().getTopRodRaiseOrFallTime() + (Math.abs(input.getClosingGoPourSubCar().getLocationCoordinate().getX() -
                                    input.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getClosingGoPourSubCar().getEmptySpeed());

                            goWaitingPourTime = input.getTotalTime() + executionTime;
                            stateChange = true;

                            eventTime = executionTime;
                        }

                        log.info("???" + " " + simulationOutput.getWaitingPourCount() + " " + goWaitingPourTime);
                    }

                }else if (tools.judgeWaitingPourIsEmpty(input.getDownPositions())){
                    //判断车辆前往的轨道
                    int pathIndex = tools.judgeCarGoWhichPath(input.getDownPositions());
                    int positionIndex = tools.judgeCarGoWhichPosition(input.getDownPositions(), pathIndex);
                    if (pathIndex != -1 && positionIndex != -1){
                        if (stateChange){
                            simulationOutput = eventFlow.fullGoDownWaitingPourPositionEventCreate(simulationOutput, pathIndex, positionIndex);

                            stateChange = false;
                        }else {
                            double executionTime = (Math.abs(input.getClosingGoPourFerry().getLocationCoordinate().getY() -
                                    input.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate().getY()) /
                                    input.getClosingGoPourFerry().getFullSpeed()) + (Math.abs(input.getClosingGoPourSubCar().getLocationCoordinate().getX() -
                                    input.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getClosingGoPourSubCar().getFullSpeed()) +
                                    input.getClosingGoPourSubCar().getTopRodRaiseOrFallTime() + (Math.abs(input.getClosingGoPourSubCar().getLocationCoordinate().getX() -
                                    input.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getClosingGoPourSubCar().getEmptySpeed());
                            goWaitingPourTime = input.getTotalTime() + executionTime;
                            stateChange = true;

                            eventTime = executionTime;
                        }

                        log.info("!!!" + " " + simulationOutput.getWaitingPourCount() + " " + goWaitingPourTime);
                    }
                }
            }
        }

        if (eventTime != 0.0){
            timeList[31] = eventTime;
            eventTime = 0.0;
        }

        return simulationOutput;
    }
}
