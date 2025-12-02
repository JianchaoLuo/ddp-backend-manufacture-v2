package cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.simulation_v2;

import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.outbound.OutBoundCarEventEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.OutBoundSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.test.OutBoundANDMoldClosing;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.Stereoscopic;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.moldClosing.MoldClosingGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Deque;
import java.util.LinkedList;

import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.OCCUPIED;
import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.UNOCCUPIED;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.OrderProductTypeEnum.COLD_SAND;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.OrderProductTypeEnum.HOT_SAND;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/12/28 19:57
 */
@Data
@Slf4j
public class OutBoundSimulation_2 {
    /**
     * 区域所处的时间
     * 出库轨道车辆的时间
     */
    private double outBoundTime = 0.0;
    /**
     * 出库区仿真的工具类
     */
    private OutBoundSimulationTools tools = new OutBoundSimulationTools();
    /**
     * 出库区模拟的立体库
     */
    private Stereoscopic stereoscopic = new Stereoscopic();

    /**
     * 制芯轨道仿真
     */
    private CoreMakingSimulation coreMakingSimulation = new CoreMakingSimulation();
    /**
     * 模具回流轨道仿真
     */
    private MoldRefulxSimulation moldRefulxSimulation = new MoldRefulxSimulation();
    /**
     * 砂箱回流轨道仿真
     */
    private SandBoxRefulxSimulation_2 sandBoxRefulxSimulation_2 = new SandBoxRefulxSimulation_2();
    /**
     * 冷砂回流轨道仿真
     */
    private ColdSandBoxRefluxSimulation_2 coldSandBoxRefluxSimulation_2 =  new ColdSandBoxRefluxSimulation_2();

    /**
     * 回流产品列表，简单测试使用，后期会更改
     */
    private Deque<Product> refluxProducts = new LinkedList<>();

    private SimulationTool simulationTool = new SimulationTool();

    /**
     * 创建出库区的事件流类
     */
    private OutBoundEventFlow outBoundEventFlow = new OutBoundEventFlow();
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean outBoundStateChange = false;
    /**
     * 枚举类，用于查找当前执行的事件，从而进行当前事件的资源状态的改变，避免出错
     */
    private OutBoundCarEventEnum carEventEnum;

    private double eventTime = 0.0;

    public OutBoundANDMoldClosing OutBoundSimulationRun(OutBoundSimulationInput input, MoldClosingGanttChartLinkInput moldInput,
                                                        GanttChart outBoundGanttChart, int finishedCount, Double[] timeList) {
        //判断时间总时间线是否到达
        //总时间线未到区域时间，故此时间内正在进行事件，不发生其他事件
        if (input.getTotalTime() >= outBoundTime){
            if (!outBoundStateChange){
                //判断子车是否空载
                if (tools.judgeSubCarIsEmpty(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()))){
                    //判断子车是否完全在母车上
                    if (tools.judgeSubCarIsInFerry(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()))){
                        //判断砂箱回流轨道上的点位是否存在需要回流的砂箱
                        if (input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getStatus().equals(OCCUPIED)){
                            //判断母车是否到达回流点
                            if (input.getOutboundFerry().getLocationCoordinate().getY() ==
                                    input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate().getY()){
                                //发生子车前往回流点位的事件
                                //计算事件发生事件的时间
                                double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                                        input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate().getX()) /
                                        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();

                                outBoundTime = input.getTotalTime() + executionTime;
                                outBoundStateChange = true;
                                carEventEnum = OutBoundCarEventEnum.EMPTY_SUB_CAR_GO_HOT_SAND_REFLUX_POSITION;
                                eventTime = executionTime;

                            }else {
                                //发生母车空载前往回流点位的事件
                                //计算发生事件的时间
                                double executionTime = Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate().getY() -
                                        input.getOutboundFerry().getLocationCoordinate().getY()) / input.getOutboundFerry().getEmptySpeed();

                                outBoundTime = input.getTotalTime() + executionTime;
                                outBoundStateChange = true;
                                carEventEnum = OutBoundCarEventEnum.EMPTY_FERRY_GO_HOT_SAND_REFLUX_POSITION;
                                eventTime = executionTime;

                            }

                            //判断模具回流轨道上的点位是否存在需要回流的砂箱
                        }else if (input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getStatus().equals(OCCUPIED)){
                            //判断母车是否到达模具回流点
                            if (input.getOutboundFerry().getLocationCoordinate().getY() ==
                                    input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getCoordinate().getY()){
                                //发生子车前往模具回流点位的事件
                                //计算事件发生事件的时间
                                double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                                        input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getCoordinate().getX()) /
                                        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();

                                outBoundTime = input.getTotalTime() + executionTime;
                                outBoundStateChange = true;
                                carEventEnum = OutBoundCarEventEnum.EMPTY_SUB_CAR_GO_MOLD_REFLUX_POSITION;
                                eventTime = executionTime;

                            }else {
                                //发生母车空载前往模具回流点位的事件
                                //计算发生事件的时间
                                double executionTime = Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getCoordinate().getY() -
                                        input.getOutboundFerry().getLocationCoordinate().getY()) / input.getOutboundFerry().getEmptySpeed();

                                outBoundTime = input.getTotalTime() + executionTime;
                                outBoundStateChange = true;
                                carEventEnum = OutBoundCarEventEnum.EMPTY_FERRY_GO_MOLD_REFLUX_POSITION;
                                eventTime = executionTime;

                            }

                            //判断冷砂回流轨道上的点位是否存在需要回流的砂箱
                        }else if (input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getStatus().equals(OCCUPIED)){
                            //判断母车是否到达冷砂回流点
                            if (input.getOutboundFerry().getLocationCoordinate().getY() ==
                                    input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate().getY()){
                                //发生子车前往冷砂回流点位的事件
                                //计算事件发生事件的时间
                                double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                                        input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate().getX()) /
                                        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();

                                outBoundTime = input.getTotalTime() + executionTime;
                                outBoundStateChange = true;
                                carEventEnum = OutBoundCarEventEnum.EMPTY_SUB_CAR_GO_COLD_SAND_REFLUX_POSITION;
                                eventTime = executionTime;

                            }else {
                                //发生母车空载前往冷砂回流点位的事件
                                //计算发生事件的时间
                                double executionTime = Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate().getY() -
                                        input.getOutboundFerry().getLocationCoordinate().getY()) / input.getOutboundFerry().getEmptySpeed();

                                outBoundTime = input.getTotalTime() + executionTime;
                                outBoundStateChange = true;
                                carEventEnum = OutBoundCarEventEnum.EMPTY_FERRY_GO_COLD_SAND_REFLUX_POSITION;
                                eventTime = executionTime;

                            }

                            //此时表示没有需要回流的产品，考虑是否需要出库
                        }else {
                            //判断出库队列是否为空，为空表示所有订单均已出库
                            if (!input.getProductDeque().isEmpty()){
                                //判断需要出库的产品是热砂还是冷砂类型，从而决定去哪个出库点，就近原则，热砂上，冷砂下
                                if (input.getProductDeque().peek().getOrderProductType().equals(HOT_SAND)){
                                    //判断母车是否到达出库点1，上方
                                    if (input.getOutboundFerry().getLocationCoordinate().getY() ==
                                            input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getNodeCoordinate().getFirst().getY()){
                                        //发生子车在出库点1进行出库的事件，由于不确定完全的出库流程，考虑让子车向出库点移动一段距离，然后进行出库，先暂时使用交互点和轨道之间的距离替代
                                        //计算事件发生事件的时间
                                        double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                                                input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getCoordinate().getX()) /
                                                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();

                                        outBoundTime = input.getTotalTime() + executionTime;
                                        outBoundStateChange = true;
                                        carEventEnum = OutBoundCarEventEnum.EMPTY_SUB_CAR_OUT_BOUND_IN_OUT_POSITION_1;
                                        eventTime = executionTime;

                                    }else {
                                        //发生母车空载前往出库点1，上方的事件
                                        //计算发生事件的时间
                                        double executionTime = Math.abs(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getNodeCoordinate().getFirst().getY() -
                                                input.getOutboundFerry().getLocationCoordinate().getY()) / input.getOutboundFerry().getEmptySpeed();

                                        outBoundTime = input.getTotalTime() + executionTime;
                                        outBoundStateChange = true;
                                        carEventEnum = OutBoundCarEventEnum.EMPTY_FERRY_OUT_BOUND_GO_OUT_POSITION_1;
                                        eventTime = executionTime;

                                    }

                                    //判断冷砂，前往下方的二号出库点
                                }else {
                                    //判断母车是否到达出库点2，下方
                                    if (input.getOutboundFerry().getLocationCoordinate().getY() ==
                                            input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getNodeCoordinate().get(1).getY()){
                                        //发生子车在出库点2进行出库的事件，由于不确定完全的出库流程，考虑让子车向出库点移动一段距离，然后进行出库，先暂时使用交互点和轨道之间的距离替代
                                        //计算事件发生事件的时间
                                        double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                                                input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getCoordinate().getX()) /
                                                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();

                                        outBoundTime = input.getTotalTime() + executionTime;
                                        outBoundStateChange = true;
                                        carEventEnum = OutBoundCarEventEnum.EMPTY_SUB_CAR_OUT_BOUND_IN_OUT_POSITION_2;
                                        eventTime = executionTime;

                                    }else {
                                        //发生母车空载前往出库点2，下方的事件
                                        //计算发生事件的时间
                                        double executionTime = Math.abs(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getNodeCoordinate().get(1).getY() -
                                                input.getOutboundFerry().getLocationCoordinate().getY()) / input.getOutboundFerry().getEmptySpeed();

                                        outBoundTime = input.getTotalTime() + executionTime;
                                        outBoundStateChange = true;
                                        carEventEnum = OutBoundCarEventEnum.EMPTY_FERRY_OUT_BOUND_GO_OUT_POSITION_2;
                                        eventTime = executionTime;

                                    }
                                }
                            }
                        }
                    }else {      //子车不在母车上时，考虑特殊情况：当子车没在母车的同时也没到点位时
                        //判断母车目前在哪里
                        switch (tools.judgeFerryLocation(input)){
                            //母车在砂箱回流点位
                            case 1:
                                //判断子车是否到达点位
                                if (input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate().equals(
                                        input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate())){
                                    //发生子车在回流砂箱轨道交互点进行装载产品的事件
                                    //计算事件发生事件的时间
                                    double executionTime = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getTopRodRaiseOrFallTime();

                                    outBoundTime = input.getTotalTime() + executionTime;
                                    outBoundStateChange = true;
                                    carEventEnum = OutBoundCarEventEnum.EMPTY_SUB_CAR_IN_HOT_SAND_REFLUX_POSITION_LOAD;
                                    eventTime = executionTime;

                                    //特殊情况未到达时
                                }else {
                                    //发生子车前往热砂砂箱回流点位的事件
                                    //计算事件发生事件的时间
                                    double executionTime = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate().getX() -
                                            input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate().getX()) /
                                            input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();

                                    outBoundTime = input.getTotalTime() + executionTime;
                                    outBoundStateChange = true;
                                    carEventEnum = OutBoundCarEventEnum.EMPTY_SUB_CAR_GO_HOT_SAND_REFLUX_POSITION;
                                    eventTime = executionTime;

                                }
                                break;
                            //母车在模具回流点位
                            case 2:
                                //判断子车是否到达点位
                                if (input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate().equals(
                                        input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getCoordinate())){
                                    //发生子车在模具回流轨道交互点进行装载产品的事件
                                    //计算事件发生事件的时间
                                    double executionTime = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getTopRodRaiseOrFallTime();

                                    outBoundTime = input.getTotalTime() + executionTime;
                                    outBoundStateChange = true;
                                    carEventEnum = OutBoundCarEventEnum.EMPTY_SUB_CAR_IN_MOLD_REFLUX_POSITION_LOAD;
                                    eventTime = executionTime;

                                    //特殊情况未到达时
                                }else {
                                    //发生子车前往模具回流点位的事件
                                    //计算事件发生事件的时间
                                    double executionTime = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate().getX() -
                                            input.getPositions().get(input.getOutBoundIndexInput().getMoldRefluxPositionIndex()).getCoordinate().getX()) /
                                            input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();

                                    outBoundTime = input.getTotalTime() + executionTime;
                                    outBoundStateChange = true;
                                    carEventEnum = OutBoundCarEventEnum.EMPTY_SUB_CAR_GO_MOLD_REFLUX_POSITION;
                                    eventTime = executionTime;

                                }
                                break;
                            //母车在冷砂回流点位
                            case 3:
                                //判断子车是否到达点位
                                if (input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate().equals(
                                        input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate())){
                                    //发生子车在冷砂回流轨道交互点进行装载产品的事件
                                    //计算事件发生事件的时间
                                    double executionTime = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getTopRodRaiseOrFallTime();

                                    outBoundTime = input.getTotalTime() + executionTime;
                                    outBoundStateChange = true;
                                    carEventEnum = OutBoundCarEventEnum.EMPTY_SUB_CAR_IN_COLD_SAND_REFLUX_POSITION_LOAD;
                                    eventTime = executionTime;

                                    //特殊情况未到达时
                                }else {
                                    //发生子车前往冷砂回流点位的事件
                                    //计算事件发生事件的时间
                                    double executionTime = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate().getX() -
                                            input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate().getX()) /
                                            input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();

                                    outBoundTime = input.getTotalTime() + executionTime;
                                    outBoundStateChange = true;
                                    carEventEnum = OutBoundCarEventEnum.EMPTY_SUB_CAR_GO_COLD_SAND_REFLUX_POSITION;
                                    eventTime = executionTime;

                                }
                                break;
                            //母车在出库点1处
                            case 4:
                                Coordinate subCarIsCoordinate_1 = new Coordinate(Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                                        input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getCoordinate().getX()) +
                                        input.getOutboundFerry().getLocationCoordinate().getX(), input.getOutboundFerry().getLocationCoordinate().getY());
                                //为了区分子车在出库点进行出库和回流完成返回母车事件，设计一个点位用于出库砂箱，一个点位用于返回母车
                                if (input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate().equals(subCarIsCoordinate_1)){
                                    //发生子车在出库点1进行出库产品的事件
                                    //计算事件发生事件的时间
                                    assert input.getProductDeque().peek() != null;
                                    double executionTime = input.getProductDeque().peek().getOutBoundTime();

                                    outBoundTime = input.getTotalTime() + executionTime;
                                    outBoundStateChange = true;
                                    carEventEnum = OutBoundCarEventEnum.EMPTY_SUB_CAR_IN_OUT_BOUND_POSITION_LOAD_1;
                                    eventTime = executionTime;

                                }else {
                                    //发生子车在出库点1回流砂箱完成，返回母车的事件
                                    //计算事件发生事件的时间
                                    double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                                            input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate().getX()) /
                                            input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();

                                    outBoundTime = input.getTotalTime() + executionTime;
                                    outBoundStateChange = true;
                                    carEventEnum = OutBoundCarEventEnum.EMPTY_SUB_CAR_BACK_FERRY_IN_OUT_BOUND_POSITION_1;
                                    eventTime = executionTime;

                                }
                                break;
                            //母车在出库点2处
                            case 5:
                                Coordinate subCarIsCoordinate_2 = new Coordinate(Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                                        input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getCoordinate().getX()) +
                                        input.getOutboundFerry().getLocationCoordinate().getX(), input.getOutboundFerry().getLocationCoordinate().getY());
                                if (input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate().equals(subCarIsCoordinate_2)){
                                    //发生子车在出库点1进行出库产品的事件
                                    //计算事件发生事件的时间
                                    assert input.getProductDeque().peek() != null;
                                    double executionTime_2 = input.getProductDeque().peek().getOutBoundTime();

                                    outBoundTime = input.getTotalTime() + executionTime_2;
                                    outBoundStateChange = true;
                                    carEventEnum = OutBoundCarEventEnum.EMPTY_SUB_CAR_IN_OUT_BOUND_POSITION_LOAD_2;
                                    eventTime = executionTime_2;

                                }else {
                                    //发生子车在出库点1回流砂箱完成，返回母车的事件
                                    //计算事件发生事件的时间
                                    double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                                            input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate().getX()) /
                                            input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();

                                    outBoundTime = input.getTotalTime() + executionTime;
                                    outBoundStateChange = true;
                                    carEventEnum = OutBoundCarEventEnum.EMPTY_SUB_CAR_BACK_FERRY_IN_OUT_BOUND_POSITION_2;
                                    eventTime = executionTime;

                                }
                                break;
                            //母车在热砂合模交互点
                            case 6:
                                //母车在冷砂合模交互点
                            case 7:
                                //子车在热砂或冷砂合模交互点，放置砂箱后返回母车的事件
                                //计算事件发生事件的时间
                                double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                                        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate().getX()) /
                                        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();

                                outBoundTime = input.getTotalTime() + executionTime;
                                outBoundStateChange = true;
                                carEventEnum = OutBoundCarEventEnum.EMPTY_SUB_CAR_BACK_FERRY_IN_SAND_REFLUX_POSITION;
                                eventTime = executionTime;

                                break;
                            //母车在制芯轨道处
                            case 8:
                                //子车在热砂或冷砂合模交互点，放置砂箱后返回母车的事件
                                //计算事件发生事件的时间
                                double executionTime_2 = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                                        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate().getX()) /
                                        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();

                                outBoundTime = input.getTotalTime() + executionTime_2;
                                outBoundStateChange = true;
                                carEventEnum = OutBoundCarEventEnum.EMPTY_SUB_CAR_BACK_FERRY_IN_MOLD_REFLUX_POSITION;
                                eventTime = executionTime_2;

                                break;
                            //位置出错
                            case 0:
                                //异常情况
                                log.info("EMPTY_LOAD_ERROR!");
                                break;
                        }
                    }
                }else {     //子车满载

                    //判断子车是否在母车上
                    if (tools.judgeSubCarIsInFerry(input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()))){
                        //判断子车上搭载的产品是否需要回流，当不需要回流时
                        if (!input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().isRefluxFlag()){
                            //判断当前子车搭载的产品类型，是热砂类型时
                            if (input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().getOrderProductType().equals(HOT_SAND)){
                                //判断产品是模具或是砂箱
                                if (input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().getProductType().equals(ProductTypeEnum.MOULD)){
                                    //判断母车是否到达热砂模具交互点
                                    if (input.getOutboundFerry().getLocationCoordinate().getY() ==
                                            input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getCoordinate().getY()){
                                        if (input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getStatus().equals(UNOCCUPIED)){
                                            //发生子车前往热砂模具交互点位的事件
                                            //计算事件发生事件的时间
                                            double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                                                    input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getCoordinate().getX()) /
                                                    input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getFullSpeed();

                                            outBoundTime = input.getTotalTime() + executionTime;
                                            outBoundStateChange = true;
                                            carEventEnum = OutBoundCarEventEnum.FULL_SUB_CAR_GO_HOT_MOLDING_POSITION;
                                            eventTime = executionTime;

                                        }
                                    }else {
                                        //发生母车满载前往热砂模具交互点的事件
                                        //计算发生事件的时间
                                        double executionTime = Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getCoordinate().getY() -
                                                input.getOutboundFerry().getLocationCoordinate().getY()) / input.getOutboundFerry().getFullSpeed();

                                        outBoundTime = input.getTotalTime() + executionTime;
                                        outBoundStateChange = true;
                                        carEventEnum = OutBoundCarEventEnum.FULL_FERRY_GO_HOT_MOLDING_POSITION;
                                        eventTime = executionTime;

                                    }
                                    //搬运的产品是砂箱的时候，应该前往砂箱回流轨道
                                }else {
                                    //前往热砂回流轨道，将砂箱放置到热砂回流轨道上
                                    //判断母车是否到达热砂模具交互点
                                    if (input.getOutboundFerry().getLocationCoordinate().getY() ==
                                            input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate().getY()){
                                        if (input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getStatus().equals(UNOCCUPIED)){
                                            //子车满载前往热砂砂箱回流交互点卸载砂箱（热砂砂箱合模点位）的事件
                                            //发生子车前往热砂模具交互点位的事件
                                            //计算事件发生事件的时间
                                            double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                                                    input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate().getX()) /
                                                    input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getFullSpeed() +
                                                    input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getTopRodRaiseOrFallTime() +
                                                    Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                                                            input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate().getX()) /
                                                            input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();

                                            outBoundTime = input.getTotalTime() + executionTime;
                                            outBoundStateChange = true;
                                            carEventEnum = OutBoundCarEventEnum.FULL_SUB_CAR_GO_HOT_SAND_MOLDING_POSITION_LAND;
                                            eventTime = executionTime;

                                        }
                                    }else {
                                        //发生母车满载前往热砂砂箱回流交互点（热砂砂箱合模点位）的事件
                                        //计算发生事件的时间
                                        double executionTime = Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate().getY() -
                                                input.getOutboundFerry().getLocationCoordinate().getY()) / input.getOutboundFerry().getFullSpeed();

                                        outBoundTime = input.getTotalTime() + executionTime;
                                        outBoundStateChange = true;
                                        carEventEnum = OutBoundCarEventEnum.FULL_FERRY_GO_HOT_SAND_MOLDING_POSITION;
                                        eventTime = executionTime;

                                    }
                                }

                                //是冷砂类型时
                            }else if (input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().getOrderProductType().equals(COLD_SAND)){
                                //判断产品是模具或是砂箱
                                if (input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().getProductType().equals(ProductTypeEnum.MOULD)){
                                    //判断母车是否到达冷砂模具交互点
                                    if (input.getOutboundFerry().getLocationCoordinate().getY() ==
                                            input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldingPositionIndex()).getCoordinate().getY()){
                                        if (input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldingPositionIndex()).getStatus().equals(UNOCCUPIED)){
                                            //发生子车前往冷砂模具交互点位的事件
                                            //计算事件发生事件的时间
                                            double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                                                    input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldingPositionIndex()).getCoordinate().getX()) /
                                                    input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();

                                            outBoundTime = input.getTotalTime() + executionTime;
                                            outBoundStateChange = true;
                                            carEventEnum = OutBoundCarEventEnum.FULL_SUB_CAR_GO_COLD_MOLDING_POSITION;
                                            eventTime = executionTime;

                                        }
                                    }else {
                                        //发生母车满载前往冷砂模具交互点的事件
                                        //计算发生事件的时间
                                        double executionTime = Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getColdSandMoldingPositionIndex()).getCoordinate().getY() -
                                                input.getOutboundFerry().getLocationCoordinate().getY()) / input.getOutboundFerry().getEmptySpeed();

                                        outBoundTime = input.getTotalTime() + executionTime;
                                        outBoundStateChange = true;
                                        carEventEnum = OutBoundCarEventEnum.FULL_FERRY_GO_COLD_MOLDING_POSITION;
                                        eventTime = executionTime;

                                    }
                                }else {
                                    //前往热砂回流轨道，将砂箱放置到热砂回流轨道上
                                    //判断母车是否到达热砂模具交互点
                                    if (input.getOutboundFerry().getLocationCoordinate().getY() ==
                                            input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate().getY()){
                                        if (input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getStatus().equals(UNOCCUPIED)){
                                            //子车满载前往热砂砂箱回流交互点卸载砂箱（热砂砂箱合模点位）的事件
                                            //发生子车前往热砂模具交互点位的事件
                                            //计算事件发生事件的时间
                                            double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                                                    input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate().getX()) /
                                                    input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getFullSpeed() +
                                                    input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getTopRodRaiseOrFallTime() +
                                                    Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                                                            input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate().getX()) /
                                                            input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();

                                            outBoundTime = input.getTotalTime() + executionTime;
                                            outBoundStateChange = true;
                                            carEventEnum = OutBoundCarEventEnum.FULL_SUB_CAR_GO_COLD_SAND_MOLDING_POSITION_LAND;
                                            eventTime = executionTime;

                                        }
                                    }else {
                                        //发生母车满载前往热砂砂箱回流交互点（热砂砂箱合模点位）的事件
                                        //计算发生事件的时间
                                        double executionTime = Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getColdRefluxPositionIndex()).getCoordinate().getY() -
                                                input.getOutboundFerry().getLocationCoordinate().getY()) / input.getOutboundFerry().getFullSpeed();

                                        outBoundTime = input.getTotalTime() + executionTime;
                                        outBoundStateChange = true;
                                        carEventEnum = OutBoundCarEventEnum.FULL_FERRY_GO_COLD_SAND_MOLDING_POSITION;
                                        eventTime = executionTime;

                                    }
                                }

                                //是砂芯类型时
                            }else {
                                //判断母车是否到达制芯交互点
                                if (input.getOutboundFerry().getLocationCoordinate().getY() ==
                                        input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getCoordinate().getY()){
                                    if (input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getStatus().equals(UNOCCUPIED)){
                                        //发生子车前往制芯点的事件
                                        //计算事件发生事件的时间
                                        double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                                                input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getCoordinate().getX()) /
                                                input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();

                                        outBoundTime = input.getTotalTime() + executionTime;
                                        outBoundStateChange = true;
                                        carEventEnum = OutBoundCarEventEnum.FULL_SUB_CAR_GO_CORE_MAKING_POSITION;
                                        eventTime = executionTime;

                                    }
                                }else {
                                    //发生母车满载前往冷砂模具交互点的事件
                                    //计算发生事件的时间
                                    double executionTime = Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getCoreMakingPositionIndex()).getCoordinate().getY() -
                                            input.getOutboundFerry().getLocationCoordinate().getY()) / input.getOutboundFerry().getEmptySpeed();

                                    outBoundTime = input.getTotalTime() + executionTime;
                                    outBoundStateChange = true;
                                    carEventEnum = OutBoundCarEventEnum.FULL_FERRY_GO_CORE_MAKING_POSITION;
                                    eventTime = executionTime;

                                }
                            }
                        }else {      //产品需要回流
                            //判断母车是否到达出库点，若已经到达
                            if ((input.getOutboundFerry().getLocationCoordinate().getY() == input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getNodeCoordinate().get(0).getY()) ||
                                    (input.getOutboundFerry().getLocationCoordinate().getY() == input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getNodeCoordinate().get(1).getY())){
                                //发生子车前往出库点的事件，同理进入出库点一段距离，思路同上空载情况
                                //计算事件发生事件的时间
                                double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                                        input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getCoordinate().getX()) * 2 /
                                        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();

                                outBoundTime = input.getTotalTime() + executionTime;
                                outBoundStateChange = true;
                                carEventEnum = OutBoundCarEventEnum.FULL_SUB_CAR_GO_OUT_BOUND_POSITION_LAND;
                                eventTime = executionTime;

                            }else {      //母车没到出库点
                                //发生母车满载前往出库点的事件，根据就近原则决定去哪个出库点
                                int obIndex = tools.judgeGoWhichOutBound(input.getOutboundFerry(), input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()));
                                //计算发生事件的时间
                                double executionTime = Math.abs(input.getPaths().get(input.getOutBoundIndexInput().getOutBoundPathIndex()).getNodeCoordinate().get(obIndex).getY() -
                                        input.getOutboundFerry().getLocationCoordinate().getY()) / input.getOutboundFerry().getEmptySpeed();

                                outBoundTime = input.getTotalTime() + executionTime;
                                outBoundStateChange = true;
                                carEventEnum = OutBoundCarEventEnum.FULL_FERRY_GO_OUT_BOUND_POSITION_LAND;
                                eventTime = executionTime;

                            }
                        }
                    }else {     //子车不在母车上
                        double executionTime_1;
                        //判断母车目前在哪里
                        switch (tools.judgeFerryLocation(input)){
                            //母车在砂箱回流轨道处
                            case 1:
                                //母车在模具回流轨道处
                            case 2:
                                //母车在冷砂回流轨道处
                            case 3:
                                //子车装载砂箱后返回母车的事件
                                //计算事件发生事件的时间
                                double executionTime = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                                        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate().getX()) /
                                        input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();

                                outBoundTime = input.getTotalTime() + executionTime;
                                outBoundStateChange = true;
                                carEventEnum = OutBoundCarEventEnum.FULL_SUB_CAR_GO_BACK_FERRY;
                                eventTime = executionTime;

                                break;
                            //母车在出库点1处
                            case 4:
                                //母车在出库点2处
                            case 5:
                                Coordinate subCarIsCoordinate_1 = new Coordinate(Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                                        input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getCoordinate().getX()) * 2 +
                                        input.getOutboundFerry().getLocationCoordinate().getX(), input.getOutboundFerry().getLocationCoordinate().getY());
                                //为了区分子车在出库点进行出库和回流完成返回母车事件，设计一个点位用于出库砂箱，一个点位用于返回母车
                                if (input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate().equals(subCarIsCoordinate_1)){
                                    //发生子车在出库点1进行回流产品的事件
                                    //计算事件发生事件的时间
                                    executionTime_1 = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getProduct().getOutBoundTime();

                                    outBoundTime = input.getTotalTime() + executionTime_1;
                                    outBoundStateChange = true;
                                    carEventEnum = OutBoundCarEventEnum.FULL_SUB_CAR_IN_OUT_BOUND_POSITION_LAND;
                                    eventTime = executionTime_1;

                                }else {
                                    //发生子车在出库点1出库产品完成，返回母车的事件
                                    //计算事件发生事件的时间
                                    double executionTime_2 = Math.abs(input.getOutboundFerry().getLocationCoordinate().getX() -
                                            input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getLocationCoordinate().getX()) /
                                            input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getEmptySpeed();

                                    outBoundTime = input.getTotalTime() + executionTime_2;
                                    outBoundStateChange = true;
                                    carEventEnum = OutBoundCarEventEnum.FULL_SUB_CAR_BACK_FERRY_IN_OUT_BOUND_POSITION;
                                    eventTime = executionTime_2;

                                }
                                break;
                            //母车在热砂合模交互点
                            case 6:
                                //发生子车在热砂合模交互点进行卸载产品的事件
                                //计算事件发生事件的时间
                                executionTime_1 = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getTopRodRaiseOrFallTime();

                                outBoundTime = input.getTotalTime() + executionTime_1;
                                outBoundStateChange = true;
                                carEventEnum = OutBoundCarEventEnum.FULL_SUB_CAR_LAND_IN_HOT_MOLDING_POSITION;
                                eventTime = executionTime_1;

                                break;
                            //母车在冷砂合模交互点
                            case 7:
                                //发生子车在冷砂合模交互点进行卸载产品的事件
                                //计算事件发生事件的时间
                                executionTime_1 = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getTopRodRaiseOrFallTime();

                                outBoundTime = input.getTotalTime() + executionTime_1;
                                outBoundStateChange = true;
                                carEventEnum = OutBoundCarEventEnum.FULL_SUB_CAR_LAND_IN_COLD_MOLDING_POSITION;
                                eventTime = executionTime_1;

                                break;
                            //母车在制芯轨道处
                            case 8:
                                //发生子车在制芯交互点进行卸载产品的事件
                                //计算事件发生事件的时间
                                executionTime_1 = input.getSubCars().get(input.getOutBoundIndexInput().getOutBoundSubcarIndex()).getTopRodRaiseOrFallTime();

                                outBoundTime = input.getTotalTime() + executionTime_1;
                                outBoundStateChange = true;
                                carEventEnum = OutBoundCarEventEnum.FULL_SUB_CAR_LAND_IN_CORE_MAKING_POSITION;
                                eventTime = executionTime_1;

                                break;
                            //位置出错
                            case 0:
                                //异常情况
                                log.info("FULL_LOAD_ERROR!");
                                break;
                        }
                    }
                }
            }else {
                switch (carEventEnum){
                    case OutBoundCarEventEnum.EMPTY_SUB_CAR_GO_HOT_SAND_REFLUX_POSITION:
                        //发生子车前往回流点位的事件
                        outBoundEventFlow.EmptySubCarGoHotSandRefluxPosition(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("子车空载前往回流点位");
                        break;
                    case OutBoundCarEventEnum.EMPTY_FERRY_GO_HOT_SAND_REFLUX_POSITION:
                        //发生母车空载前往回流点位的事件
                        outBoundEventFlow.EmptyFerryGoHotSandRefluxPosition(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("母车空载前往回流点位");
                        break;
                    case OutBoundCarEventEnum.EMPTY_SUB_CAR_GO_MOLD_REFLUX_POSITION:
                        //发生子车前往模具回流点位的事件
                        outBoundEventFlow.EmptySubCarGoMoldRefluxPosition(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("子车空载前往模具回流点位");
                        break;
                    case OutBoundCarEventEnum.EMPTY_FERRY_GO_MOLD_REFLUX_POSITION:
                        //发生母车空载前往模具回流点位的事件
                        outBoundEventFlow.EmptyFerryGoMoldRefluxPosition(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("母车空载前往模具回流点位");
                        break;
                    case OutBoundCarEventEnum.EMPTY_SUB_CAR_GO_COLD_SAND_REFLUX_POSITION:
                        //发生子车前往冷砂回流点位的事件
                        outBoundEventFlow.EmptySubCarGoColdSandRefluxPosition(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("子车空载前往冷砂回流点位");
                        break;
                    case OutBoundCarEventEnum.EMPTY_FERRY_GO_COLD_SAND_REFLUX_POSITION:
                        //发生母车空载前往冷砂回流点位的事件
                        outBoundEventFlow.EmptyFerryGoColdSandRefluxPosition(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("母车空载前往冷砂回流点位");
                        break;
                    case OutBoundCarEventEnum.EMPTY_SUB_CAR_OUT_BOUND_IN_OUT_POSITION_1:
                        //发生子车在出库点1进行出库的事件，由于不确定完全的出库流程，考虑让子车向出库点移动一段距离，然后进行出库，先暂时使用交互点和轨道之间的距离替代
                        outBoundEventFlow.EmptySubCarOutBoundInOutPosition_1(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("子车空载在出库点1进行出库");
                        break;
                    case OutBoundCarEventEnum.EMPTY_FERRY_OUT_BOUND_GO_OUT_POSITION_1:
                        //发生母车空载前往出库点1，上方的事件
                        outBoundEventFlow.EmptyFerryOutBoundGoOutPosition_1(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("母车空载前往出库点1");
                        break;
                    case OutBoundCarEventEnum.EMPTY_SUB_CAR_OUT_BOUND_IN_OUT_POSITION_2:
                        //发生子车在出库点2进行出库的事件，由于不确定完全的出库流程，考虑让子车向出库点移动一段距离，然后进行出库，先暂时使用交互点和轨道之间的距离替代
                        outBoundEventFlow.EmptySubCarOutBoundInOutPosition_2(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("子车空载在出库点2进行出库");
                        break;
                    case OutBoundCarEventEnum.EMPTY_FERRY_OUT_BOUND_GO_OUT_POSITION_2:
                        //发生母车空载前往出库点2，下方的事件
                        outBoundEventFlow.EmptyFerryOutBoundGoOutPosition_2(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("母车空载前往出库点2");
                        break;
                    case OutBoundCarEventEnum.EMPTY_SUB_CAR_IN_HOT_SAND_REFLUX_POSITION_LOAD:
                        //发生子车在回流砂箱轨道交互点进行装载产品的事件
                        outBoundEventFlow.EmptySubCarInHotSandRefluxPositionLoad(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("子车在回流砂箱轨道交互点进行装载产品");
                        break;
                    case OutBoundCarEventEnum.EMPTY_SUB_CAR_IN_MOLD_REFLUX_POSITION_LOAD:
                        //发生子车在模具回流轨道交互点进行装载产品的事件
                        outBoundEventFlow.EmptySubCarInMoldRefluxPositionLoad(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("子车在模具回流轨道交互点进行装载产品");
                        break;
                    case OutBoundCarEventEnum.EMPTY_SUB_CAR_IN_COLD_SAND_REFLUX_POSITION_LOAD:
                        //发生子车在冷砂回流轨道交互点进行装载产品的事件
                        outBoundEventFlow.EmptySubCarInColdSandRefluxPositionLoad(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("子车在冷砂回流轨道交互点进行装载产品");
                        break;
                    case OutBoundCarEventEnum.EMPTY_SUB_CAR_IN_OUT_BOUND_POSITION_LOAD_1:
                        //发生子车在出库点1进行出库产品的事件
                        outBoundEventFlow.EmptySubCarInOutBoundPositionLoad_1(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("子车在出库点1进行出库产品");
                        break;
                    case OutBoundCarEventEnum.EMPTY_SUB_CAR_BACK_FERRY_IN_OUT_BOUND_POSITION_1:
                        //发生子车在出库点1回流砂箱完成，返回母车的事件
                        outBoundEventFlow.EmptySubCarBackFerryInOutBoundPosition_1(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("子车在出库点1回流砂箱完成，返回母车");
                        break;
                    case OutBoundCarEventEnum.EMPTY_SUB_CAR_IN_OUT_BOUND_POSITION_LOAD_2:
                        //发生子车在出库点1进行出库产品的事件
                        outBoundEventFlow.EmptySubCarInOutBoundPositionLoad_2(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("子车在出库点2进行出库产品");
                        break;
                    case OutBoundCarEventEnum.EMPTY_SUB_CAR_BACK_FERRY_IN_OUT_BOUND_POSITION_2:
                        //发生子车在出库点1回流砂箱完成，返回母车的事件
                        outBoundEventFlow.EmptySubCarBackFerryInOutBoundPosition_2(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("子车在出库点2回流砂箱完成，返回母车");
                        break;
                    case OutBoundCarEventEnum.EMPTY_SUB_CAR_BACK_FERRY_IN_SAND_REFLUX_POSITION:
                        //子车在热砂或冷砂合模交互点，放置砂箱后返回母车的事件
                        outBoundEventFlow.EmptySubCarBackFerryInSandRefluxPosition(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("子车在热砂或冷砂合模交互点，放置砂箱后返回母车");
                        break;
                    case OutBoundCarEventEnum.EMPTY_SUB_CAR_BACK_FERRY_IN_MOLD_REFLUX_POSITION:
                        //子车在热砂或冷砂合模交互点，放置砂箱后返回母车的事件
                        outBoundEventFlow.EmptySubCarBackFerryInMoldRefluxPosition(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("子车在热砂或冷砂合模交互点，放置砂箱后返回母车");
                        break;
                    case OutBoundCarEventEnum.FULL_SUB_CAR_GO_HOT_MOLDING_POSITION:
                        //发生子车前往热砂模具交互点位的事件
                        outBoundEventFlow.FullSubCarGoHotMoldingPosition(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("子车在热砂模具交互点位，放置砂箱后返回母车");
                        break;
                    case OutBoundCarEventEnum.FULL_FERRY_GO_HOT_MOLDING_POSITION:
                        //发生母车满载前往热砂模具交互点的事件
                        outBoundEventFlow.FullFerryGoHotMoldingPosition(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("母车满载前往热砂模具交互点位");
                        break;
                    case OutBoundCarEventEnum.FULL_SUB_CAR_GO_HOT_SAND_MOLDING_POSITION_LAND:
                        //子车满载前往热砂砂箱回流交互点卸载砂箱（热砂砂箱合模点位）的事件
                        //发生子车前往热砂模具交互点位的事件
                        outBoundEventFlow.FullSubCarGoHotSandMoldingPositionLand(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
                        break;
                    case OutBoundCarEventEnum.FULL_FERRY_GO_HOT_SAND_MOLDING_POSITION:
                        //发生母车满载前往热砂砂箱回流交互点（热砂砂箱合模点位）的事件
                        outBoundEventFlow.FullFerryGoHotSandMoldingPosition(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
                        break;
                    case OutBoundCarEventEnum.FULL_SUB_CAR_GO_COLD_MOLDING_POSITION:
                        //发生子车前往冷砂模具交互点位的事件
                        outBoundEventFlow.FullSubCarGoColdMoldingPosition(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("子车在冷砂模具交互点位，放置砂箱后返回母车");
                        break;
                    case OutBoundCarEventEnum.FULL_FERRY_GO_COLD_MOLDING_POSITION:
                        //发生母车满载前往冷砂模具交互点的事件
                        outBoundEventFlow.FullFerryGoColdMoldingPosition(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("母车满载前往冷砂模具交互点位");
                        break;
                    case OutBoundCarEventEnum.FULL_SUB_CAR_GO_COLD_SAND_MOLDING_POSITION_LAND:
                        //子车满载前往热砂砂箱回流交互点卸载砂箱（热砂砂箱合模点位）的事件
                        //发生子车前往热砂模具交互点位的事件
                        outBoundEventFlow.FullSubCarGoColdSandMoldingPositionLand(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
                        break;
                    case OutBoundCarEventEnum.FULL_FERRY_GO_COLD_SAND_MOLDING_POSITION:
                        //发生母车满载前往热砂砂箱回流交互点（热砂砂箱合模点位）的事件
                        outBoundEventFlow.FullFerryGoColdSandMoldingPosition(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
                        break;
                    case OutBoundCarEventEnum.FULL_SUB_CAR_GO_CORE_MAKING_POSITION:
                        //发生子车前往制芯点的事件
                        outBoundEventFlow.FullSubCarGoCoreMakingPosition(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("子车在制芯点位，放置砂箱后返回母车");
                        break;
                    case OutBoundCarEventEnum.FULL_FERRY_GO_CORE_MAKING_POSITION:
                        //发生母车满载前往冷砂模具交互点的事件
                        outBoundEventFlow.FullFerryGoCoreMakingPosition(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("母车满载前往制芯点位");
                        break;
                    case OutBoundCarEventEnum.FULL_SUB_CAR_GO_OUT_BOUND_POSITION_LAND:
                        //发生子车前往出库点的事件，同理进入出库点一段距离，思路同上空载情况
                        outBoundEventFlow.FullSubCarGoOutBoundPositionLand(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("子车在出库点位，放置砂箱后返回母车");
                        break;
                    case OutBoundCarEventEnum.FULL_FERRY_GO_OUT_BOUND_POSITION_LAND:
                        //发生母车满载前往出库点的事件，根据就近原则决定去哪个出库点
                        outBoundEventFlow.FullFerryGoOutBoundPositionLand(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("母车满载前往出库点位");
                        break;
                    case OutBoundCarEventEnum.FULL_SUB_CAR_GO_BACK_FERRY:
                        //子车装载砂箱后返回母车的事件
                        outBoundEventFlow.FullSubCarGoBackFerry(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("子车装载砂箱后返回母车");
                        break;
                    case OutBoundCarEventEnum.FULL_SUB_CAR_IN_OUT_BOUND_POSITION_LAND:
                        //发生子车在出库点1进行回流产品的事件
                        outBoundEventFlow.FullSubCarInOutBoundPositionLand(input, outBoundGanttChart, refluxProducts);
                        finishedCount++;

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("子车在出库点1卸载产品完成 " + finishedCount);
                        break;
                    case OutBoundCarEventEnum.FULL_SUB_CAR_BACK_FERRY_IN_OUT_BOUND_POSITION:
                        //发生子车在出库点1出库产品完成，返回母车的事件
                        outBoundEventFlow.FullSubCarBackFerryInOutBoundPosition(input, outBoundGanttChart);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("子车在出库点1装载砂箱后返回母车");
                        break;
                    case OutBoundCarEventEnum.FULL_SUB_CAR_LAND_IN_HOT_MOLDING_POSITION:
                        //发生子车在热砂合模交互点进行卸载产品的事件
                        outBoundEventFlow.FullSubCarLandInHotMoldingPosition(input, outBoundGanttChart, moldInput);

                        outBoundStateChange = false;
                        carEventEnum = null;
                        break;
                    case OutBoundCarEventEnum.FULL_SUB_CAR_LAND_IN_COLD_MOLDING_POSITION:
                        //发生子车在冷砂合模交互点进行卸载产品的事件
                        outBoundEventFlow.FullSubCarLandInColdMoldingPosition(input, outBoundGanttChart, moldInput);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("子车在冷砂合模交互点卸载产品完成");
                        break;
                    case OutBoundCarEventEnum.FULL_SUB_CAR_LAND_IN_CORE_MAKING_POSITION:
                        //发生子车在制芯交互点进行卸载产品的事件
                        outBoundEventFlow.FullSubCarLandInCoreMakingPosition(input, outBoundGanttChart, moldInput);

                        outBoundStateChange = false;
                        carEventEnum = null;
//                        log.info("子车在制芯交互点卸载产品完成");
                        break;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[0] = eventTime;
            eventTime = 0.0;
        }

//        log.info("is null4?" + (input.getPositions().get(input.getOutBoundIndexInput().getHotSandMoldingPositionIndex()).getProduct() == null));

        OutBoundGanttChartLinkInput outBoundGanttChartLinkInput = new OutBoundGanttChartLinkInput(outBoundGanttChart, input);

        //制芯轨道事件
        outBoundGanttChartLinkInput = coreMakingSimulation.CoreMakingSimulationRun(outBoundGanttChartLinkInput.getInput(), outBoundGanttChartLinkInput.getGanttChart(), timeList);

        //砂箱回流轨道事件
        outBoundGanttChartLinkInput = sandBoxRefulxSimulation_2.SandBoxRefulxSimulationRun(outBoundGanttChartLinkInput.getInput(), moldInput, outBoundGanttChartLinkInput.getGanttChart(), timeList);

        //冷砂回流轨道事件
        outBoundGanttChartLinkInput = coldSandBoxRefluxSimulation_2.ColdSandBoxRefluxSimulationRun(outBoundGanttChartLinkInput.getInput(), outBoundGanttChartLinkInput.getGanttChart(), timeList);

        //模具回流轨道事件
        outBoundGanttChartLinkInput = moldRefulxSimulation.MoldRefulxSimulationRun(outBoundGanttChartLinkInput.getInput(), outBoundGanttChartLinkInput.getGanttChart(), timeList);

        return new OutBoundANDMoldClosing(outBoundGanttChartLinkInput, moldInput, finishedCount);
    }
}
