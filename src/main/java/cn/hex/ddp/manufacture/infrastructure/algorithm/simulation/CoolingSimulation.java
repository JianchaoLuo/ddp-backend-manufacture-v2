package cn.hex.ddp.manufacture.infrastructure.algorithm.simulation;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.OrderProductTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.CoolingSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.OpenBoxSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.cooling.CoolingEventFlow;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.cooling.CoolingSimulationTools;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.waitingPour.AfterClosingBoxAllLink;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/31 17:26
 */
@Slf4j
public class CoolingSimulation {
    /**
     * 冷却完前往开箱时间
     */
    private double goOpenBoxTime = 0.0;
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean goOpenBoxStateChange = false;
    /**
     * 仿真总工具类
     */
    private final SimulationTool allTool = new SimulationTool();
    /**
     * 冷却区的工具类
     */
    private final CoolingSimulationTools tools = new CoolingSimulationTools();
    /**
     * 创建事件的类
     */
    private final CoolingEventFlow eventFlow = new CoolingEventFlow();

    private double eventTime = 0.0;

    public AfterClosingBoxAllLink CoolingSimulationRun(AfterClosingBoxAllLink afterClosingBoxAllLink, Double[] timeList){
        AfterClosingBoxAllLink simulationOutput = SerializationUtils.clone(afterClosingBoxAllLink);
        //获取冷却区和开箱区的放置输入
        CoolingSimulationInput input = simulationOutput.getCoolingInput().getInput();
        OpenBoxSimulationInput openBoxInput = simulationOutput.getOpenBoxInput().getInput();

        //判断冷却区是否可以发生事件
        if (input.getTotalTime() >= goOpenBoxTime){
            //判断子车是否空载
            if (allTool.judgeSubCarIsEmpty(input.getCoolingSubCar())){
                //判断冷却区上半区是否有可以搬运的产品
                if (tools.judgeCoolingHalfIsFinish(input.getUpPositions())){
                    //判断车辆前往的轨道
                    int pathIndex = tools.judgeCoolingCarGoWhichPath(input.getUpPositions());
                    int positionIndex = tools.judgeCoolingCarGoWhichPosition(input.getUpPositions(), pathIndex);
                    if (pathIndex != -1 && positionIndex != -1){
                        if (goOpenBoxStateChange){
                            simulationOutput = eventFlow.emptyGoUpCoolingPositionEventCreate(simulationOutput, pathIndex, positionIndex);

                            goOpenBoxStateChange = false;

                            log.info("冷却区：前往上轨道装载砂箱 " + goOpenBoxTime + "pathIndex: " + pathIndex + " positionIndex: " + positionIndex);
                        }else {
                            double executionTime = (Math.abs(input.getCoolingFerry().getLocationCoordinate().getY() -
                                    input.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate().getY()) /
                                    input.getCoolingFerry().getEmptySpeed()) + (Math.abs(input.getCoolingSubCar().getLocationCoordinate().getX() -
                                    input.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getCoolingSubCar().getEmptySpeed()) +
                                    input.getCoolingSubCar().getTopRodRaiseOrFallTime() + (Math.abs(input.getCoolingSubCar().getLocationCoordinate().getX() -
                                    input.getUpPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getCoolingSubCar().getFullSpeed());

                            goOpenBoxTime = input.getTotalTime() + executionTime;
                            goOpenBoxStateChange = true;

                            eventTime = executionTime;
                        }

                        log.info("冷却区：前往上轨道装载砂箱 " + goOpenBoxTime);
                    }

                }else if (tools.judgeCoolingHalfIsFinish(input.getDownPositions())){
                    //判断车辆前往的轨道
                    int pathIndex = tools.judgeCoolingCarGoWhichPath(input.getDownPositions());
                    int positionIndex = tools.judgeCoolingCarGoWhichPosition(input.getDownPositions(), pathIndex);
                    if (pathIndex != -1 && positionIndex != -1){
                        if (goOpenBoxStateChange){
                            simulationOutput = eventFlow.emptyGoDownCoolingPositionEventCreate(simulationOutput, pathIndex, positionIndex);

                            goOpenBoxStateChange = false;
                        }else {
                            double executionTime = (Math.abs(input.getCoolingFerry().getLocationCoordinate().getY() -
                                    input.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate().getY()) /
                                    input.getCoolingFerry().getEmptySpeed()) + (Math.abs(input.getCoolingSubCar().getLocationCoordinate().getX() -
                                    input.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getCoolingSubCar().getEmptySpeed()) +
                                    input.getCoolingSubCar().getTopRodRaiseOrFallTime() + (Math.abs(input.getCoolingSubCar().getLocationCoordinate().getX() -
                                    input.getDownPositions().get(pathIndex).get(positionIndex).getCoordinate().getX()) / input.getCoolingSubCar().getFullSpeed());

                            goOpenBoxTime = input.getTotalTime() + executionTime;
                            goOpenBoxStateChange = true;

                            eventTime = executionTime;
                        }

                        log.info("冷却区：前往下轨道装载砂箱 " + goOpenBoxTime);
                    }
                }

                //子车满载
            }else {
                //判断子车上的产品是热砂还是冷砂
                if (input.getCoolingSubCar().getProduct().getOrderProductType().equals(OrderProductTypeEnum.HOT_SAND)){
                    //判断母车是否到达热砂开箱点位
                    if (input.getCoolingFerry().getLocationCoordinate().getY() == openBoxInput.getHotOpenBoxPosition().getCoordinate().getY()){
                        //判断点位是否未占用
                        if (openBoxInput.getHotOpenBoxPosition().getStatus().equals(PositionStatusEnum.UNOCCUPIED)){
                            //发生子车前往热砂开箱点卸载产品的事件
                            if (goOpenBoxStateChange){
                                simulationOutput = eventFlow.fullSubCarGoHotOpenBoxPositionEventCreate(simulationOutput);

                                goOpenBoxStateChange = false;
                            }else {
                                double executionTime = (Math.abs(input.getCoolingSubCar().getLocationCoordinate().getX() -
                                        openBoxInput.getHotOpenBoxPosition().getCoordinate().getX()) / input.getCoolingSubCar().getFullSpeed()) +
                                        input.getCoolingSubCar().getTopRodRaiseOrFallTime() + (Math.abs(input.getCoolingSubCar().getLocationCoordinate().getX() -
                                        openBoxInput.getHotOpenBoxPosition().getCoordinate().getX()) / input.getCoolingSubCar().getEmptySpeed());

                                goOpenBoxTime = input.getTotalTime() + executionTime;
                                goOpenBoxStateChange = true;

                                eventTime = executionTime;
                            }

                            log.info("冷却区：在热砂点位卸载砂箱 " + goOpenBoxTime);
                        }
                    }else {
                        //发生母车前往热砂开箱点的事件
                        if (goOpenBoxStateChange){
                            simulationOutput = eventFlow.fullGoHotOpenBoxPositionEventCreate(simulationOutput);

                            goOpenBoxStateChange = false;
                        }else {
                            double executionTime = (Math.abs(input.getCoolingFerry().getLocationCoordinate().getY() -
                                    openBoxInput.getHotOpenBoxPosition().getCoordinate().getY()) / input.getCoolingFerry().getFullSpeed());

                            goOpenBoxTime = input.getTotalTime() + executionTime;
                            goOpenBoxStateChange = true;

                            eventTime = executionTime;
                        }

//                        log.info("冷却区：前往热砂点位 " + goOpenBoxTime);
                    }
                }else {
                    //判断母车是否到达冷热砂开箱点位
                    if (input.getCoolingFerry().getLocationCoordinate().getY() == openBoxInput.getColdAndHotOpenBoxPosition().getCoordinate().getY()){
                        //判断点位是否未占用
                        if (openBoxInput.getColdAndHotOpenBoxPosition().getStatus().equals(PositionStatusEnum.UNOCCUPIED)){
                            //发生子车前往冷热砂开箱点卸载产品的事件
                            if (goOpenBoxStateChange){
                                simulationOutput = eventFlow.fullSubCarGoColdAndHotOpenBoxPositionEventCreate(simulationOutput);

                                goOpenBoxStateChange = false;
                            }else {
                                double executionTime = (Math.abs(input.getCoolingSubCar().getLocationCoordinate().getX() -
                                        openBoxInput.getColdAndHotOpenBoxPosition().getCoordinate().getX()) / input.getCoolingSubCar().getFullSpeed()) +
                                        input.getCoolingSubCar().getTopRodRaiseOrFallTime() + (Math.abs(input.getCoolingSubCar().getLocationCoordinate().getX() -
                                        openBoxInput.getColdAndHotOpenBoxPosition().getCoordinate().getX()) / input.getCoolingSubCar().getEmptySpeed());

                                goOpenBoxTime = input.getTotalTime() + executionTime;
                                goOpenBoxStateChange = true;

                                eventTime = executionTime;
                            }

                            log.info("冷却区：在冷热砂点位卸载砂箱 " + goOpenBoxTime);
                        }
                    }else {
                        //发生母车前往冷热砂开箱点的事件
                        if (goOpenBoxStateChange){
                            simulationOutput = eventFlow.fullGoColdAndHotOpenBoxPositionEventCreate(simulationOutput);

                            goOpenBoxStateChange = false;
                        }else {
                            double executionTime = (Math.abs(input.getCoolingFerry().getLocationCoordinate().getY() -
                                    openBoxInput.getColdAndHotOpenBoxPosition().getCoordinate().getY()) / input.getCoolingFerry().getFullSpeed());

                            goOpenBoxTime = input.getTotalTime() + executionTime;
                            goOpenBoxStateChange = true;

                            eventTime = executionTime;
                        }

//                        log.info("冷却区：前往热砂点位 " + goOpenBoxTime);
                    }
                }
            }
        }

        if (eventTime != 0.0){
            timeList[45] = eventTime;
            eventTime = 0.0;
        }

        int currentTimeIndex = 60;

        for (int i = 0; i < simulationOutput.getUpImitates().size(); i++) {
            for (int j = 0; j < simulationOutput.getUpImitates().get(i).size(); j++) {
                if (simulationOutput.getUpImitates().get(i).get(j).getCoolingTime() != 0.0){
//                    log.info("Up!!! " + simulationOutput.getUpImitates().get(i).get(j).getCoolingTime());
                    if (input.getTotalTime() >= (simulationOutput.getUpImitates().get(i).get(j).getCoolingTime() +
                            simulationOutput.getUpImitates().get(i).get(j).getTotalTime())){
                        simulationOutput.getCoolingInput().getInput().getUpPositions().get(i).get(j).setStatus(PositionStatusEnum.FINISH_COOLING);
                        simulationOutput.getUpImitates().get(i).get(j).setCoolingTime(0.0);

                        log.info("Up!!!一个产品冷却完成 " + "i = " + i + ", j = " + j);
                    }else {
                        timeList[currentTimeIndex] = (simulationOutput.getUpImitates().get(i).get(j).getCoolingTime() +
                                simulationOutput.getUpImitates().get(i).get(j).getTotalTime()) - input.getTotalTime();
                    }
                }

                currentTimeIndex++;
            }
        }

        for (int i = 0; i < simulationOutput.getDownImitates().size(); i++) {
            for (int j = 0; j < simulationOutput.getDownImitates().get(i).size(); j++) {
                if (simulationOutput.getDownImitates().get(i).get(j).getCoolingTime() != 0.0){
//                    log.info("Down!!! " + simulationOutput.getDownImitates().get(i).get(j).getCoolingTime());
                    if (input.getTotalTime() >= (simulationOutput.getDownImitates().get(i).get(j).getCoolingTime() +
                            simulationOutput.getDownImitates().get(i).get(j).getTotalTime())){
                        simulationOutput.getCoolingInput().getInput().getDownPositions().get(i).get(j).setStatus(PositionStatusEnum.FINISH_COOLING);
                        simulationOutput.getDownImitates().get(i).get(j).setCoolingTime(0.0);

                        log.info("Down!!!一个产品冷却完成 " + "i = " + i + ", j = " + j);
                    }else {
                        timeList[currentTimeIndex] = (simulationOutput.getDownImitates().get(i).get(j).getCoolingTime() +
                                simulationOutput.getDownImitates().get(i).get(j).getTotalTime()) - input.getTotalTime();
                    }
                }

                currentTimeIndex++;
            }
        }

        return simulationOutput;
    }
}
