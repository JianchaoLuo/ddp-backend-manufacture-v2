package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.OrderProductTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.OutBoundSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;

import java.util.ArrayList;
import java.util.List;

import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.OCCUPIED;
import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.UNOCCUPIED;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/6/21 21:37
 */
public class SandBoxRefulxSimulation {
    /**
     * 砂箱回流轨道车辆时间
     */
    private double sandBoxRefluxTime = 0.0;
    /**
     * 出库区仿真的工具类
     */
    private final OutBoundSimulationTools tools = new OutBoundSimulationTools();
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean sandBoxRefluxStateChange = false;
    /**
     * 仿真总工具类
     */
    private final SimulationTool allTool = new SimulationTool();
    /**
     * 创建事件的类
     */
    private final SandBoxRefluxEventFlow eventFlow = new SandBoxRefluxEventFlow();

    private double eventTime = 0.0;


    public OutBoundGanttChartLinkInput SandBoxRefulxSimulationRun(OutBoundSimulationInput input, GanttChart outBoundGanttChart, Double[] timeList){
        OutBoundGanttChartLinkInput simulationOutput = new OutBoundGanttChartLinkInput(outBoundGanttChart, input);
        //轨道的回流等待点位坐标坐标
        Coordinate refluxWaitCoordinate = new Coordinate((input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getX()) +
                input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getX(),
                input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getY());
        //轨道的放置等待点位坐标
        Coordinate layWaitCoordinate = new Coordinate(input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate().getX() -
                (input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getX()),
                input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getY());

        if (input.getTotalTime() >= sandBoxRefluxTime){
            //判断子车是否满载
            if (allTool.judgeSubCarIsEmpty(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()))){
                //判断子车是否到达回流等待点位
                if (input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getLocationCoordinate().equals(refluxWaitCoordinate)){
                    //判断冷热砂混合点位是否被占用
                    if (input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getStatus().equals(PositionStatusEnum.OCCUPIED)){
                        //前往冷热砂混合点位装载事件,回到等待点位
                        if (sandBoxRefluxStateChange){
                            //前往开模点装载的事件
                            simulationOutput = eventFlow.EmptySandBoxRefluxGoHACPositionEventCreate(simulationOutput, refluxWaitCoordinate);

                            sandBoxRefluxStateChange = false;
                        }else {
                            double executionTime = (Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getLocationCoordinate().getX() -
                                    input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getX())
                                    / input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getEmptySpeed()) +
                                    input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getTopRodRaiseOrFallTime() +
                                    (Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getX() -
                                            refluxWaitCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getFullSpeed());

                            sandBoxRefluxTime = input.getTotalTime() + executionTime;

                            sandBoxRefluxStateChange = true;

                            eventTime = executionTime;
                        }
                    }else if (input.getPositions().get(input.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getStatus().equals(PositionStatusEnum.OCCUPIED)){
                        //前往热砂回流点位装载事件,回到等待点位
                        if (sandBoxRefluxStateChange){
                            //前往开模点装载的事件
                            simulationOutput = eventFlow.EmptySandBoxRefluxGoHotPositionEventCreate(simulationOutput, refluxWaitCoordinate);

                            sandBoxRefluxStateChange = false;
                        }else {
                            double executionTime = (Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getLocationCoordinate().getX() -
                                    input.getPositions().get(input.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getX())
                                    / input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getEmptySpeed()) +
                                    input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getTopRodRaiseOrFallTime() +
                                    (Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getX() -
                                            refluxWaitCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getFullSpeed());

                            sandBoxRefluxTime = input.getTotalTime() + executionTime;

                            sandBoxRefluxStateChange = true;

                            eventTime = executionTime;
                        }
                    }
                }else {
                    //前往等待点位事件
                    if (sandBoxRefluxStateChange){
                        //前往回流点前的等待位置处
                        simulationOutput = eventFlow.EmptySandBoxRefluxGoWaitPositionEventCreate(simulationOutput, refluxWaitCoordinate);

                        sandBoxRefluxStateChange = false;
                    }else {
                        double executionTime = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getLocationCoordinate().getX() -
                                refluxWaitCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getEmptySpeed();

                        sandBoxRefluxTime = input.getTotalTime() + executionTime;

                        sandBoxRefluxStateChange = true;

                        eventTime = executionTime;
                    }
                }

                //子车满载
            }else {
                //判断产品是冷砂或者热砂
                if (input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getProduct().getOrderProductType().equals(OrderProductTypeEnum.HOT_SAND)){
                    if (input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getLocationCoordinate().equals(layWaitCoordinate)){
                        //判断放置点位是否被占用
                        if (input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getStatus().equals(PositionStatusEnum.UNOCCUPIED)){
                            //前往点位放置产品
                            if (sandBoxRefluxStateChange){
                                //前往回流点前的等待位置处
                                simulationOutput = eventFlow.FullSandBoxRefluxGoHotPositionEventCreate(simulationOutput);

                                sandBoxRefluxStateChange = false;
                            }else {
                                double executionTime = (Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getLocationCoordinate().getX() -
                                        input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate().getX())
                                        / input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getFullSpeed()) +
                                        input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getTopRodRaiseOrFallTime();

                                sandBoxRefluxTime = input.getTotalTime() + executionTime;

                                sandBoxRefluxStateChange = true;

                                eventTime = executionTime;
                            }
                        }
                    }else {
                        //前往放置等待点位
                        if (sandBoxRefluxStateChange){
                            simulationOutput = eventFlow.FullSandBoxRefluxGoWaitPositionEventCreate(simulationOutput, layWaitCoordinate);

                            sandBoxRefluxStateChange = false;
                        }else {
                            double executionTime = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getLocationCoordinate().getX() -
                                    layWaitCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getFullSpeed();

                            sandBoxRefluxTime = input.getTotalTime() + executionTime;

                            sandBoxRefluxStateChange = true;

                            eventTime = executionTime;
                        }
                    }
                    //如果是冷砂
                }else {
                    //判断冷砂放置点位是否被占用
                    if (input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).getStatus().equals(PositionStatusEnum.UNOCCUPIED)){
                        //前往点位放置产品
                        if (sandBoxRefluxStateChange){
                            //前往回流点前的等待位置处
                            simulationOutput = eventFlow.FullSandBoxRefluxGoColdPositionEventCreate(simulationOutput);

                            sandBoxRefluxStateChange = false;
                        }else {
                            double executionTime = (Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getLocationCoordinate().getX() -
                                    input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).getCoordinate().getX())
                                    / input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getFullSpeed()) +
                                    input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getTopRodRaiseOrFallTime();

                            sandBoxRefluxTime = input.getTotalTime() + executionTime;

                            sandBoxRefluxStateChange = true;

                            eventTime = executionTime;
                        }
                    }
                }
            }
        }

        if (input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).getStatus().equals(PositionStatusEnum.OCCUPIED)){
            if (input.getPositions().get(input.getOutBoundIndexInput().getColdSandRowCarPositionIndex()).getStatus().equals(PositionStatusEnum.UNOCCUPIED)){
                input.getPositions().get(input.getOutBoundIndexInput().getColdSandRowCarPositionIndex()).setStatus(PositionStatusEnum.OCCUPIED);
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).setStatus(PositionStatusEnum.UNOCCUPIED);
                Product product = input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).getProduct().productDeepCopy();
                input.getPositions().get(input.getOutBoundIndexInput().getColdSandRowCarPositionIndex()).setProduct(product);
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).setProduct(null);

                List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
                List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
                startPositionStatus.add(UNOCCUPIED);
                endPositionStatus.add(OCCUPIED);

                Event event_1 = allTool.createEvent("100998", "positionUp", 0.0, 0.0, 0.0, null, null,
                        false, null, null, false, null, null, null,
                        false, null, null, false, null, null, null,
                        null, input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).getId(), endPositionStatus, startPositionStatus,
                        null, null, null, null, null, null,
                        input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).getId(), ExecuteTypeEnum.POSITION, InstructionsEnum.NOTICE,
                        null, product);

                //事件加入甘特图
                simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getPositionInGanttIndex()).getEvents().add(event_1);

                Event event_2 = allTool.createEvent("100999", "positionDown", 0.0, 0.0, 0.0, null, null,
                        false, null, null, false, null, null, null,
                        false, null, null, false, null, null, null,
                        null, input.getPositions().get(input.getOutBoundIndexInput().getColdSandRowCarPositionIndex()).getId(), startPositionStatus,
                        endPositionStatus, null, null, null, null, null, null,
                        input.getPositions().get(input.getOutBoundIndexInput().getColdSandRowCarPositionIndex()).getId(), ExecuteTypeEnum.POSITION, InstructionsEnum.NOTICE,
                        null, product);

                //事件加入甘特图
                simulationOutput.getGanttChart().getEventLinkGantts().get(input.getOutBoundIndexInput().getPositionInGanttIndex()).getEvents().add(event_2);
            }
        }

        if (eventTime != 0.0){
            timeList[2] = eventTime;
            eventTime = 0.0;
        }

        return simulationOutput;
    }
}
