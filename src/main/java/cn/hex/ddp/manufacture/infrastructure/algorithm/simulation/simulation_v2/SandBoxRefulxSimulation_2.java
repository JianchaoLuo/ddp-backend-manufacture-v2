package cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.simulation_v2;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.OrderProductTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.outbound.HotSandMoldingEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.outbound.SandBoxRefulxEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.MoldClosingSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.OutBoundSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.moldClosing.MoldClosingGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound.OutBoundGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound.OutBoundSimulationTools;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound.SandBoxRefluxEventFlow;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.OCCUPIED;
import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.UNOCCUPIED;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2025/1/3 21:50
 */
@Data
@Slf4j
public class SandBoxRefulxSimulation_2 {
    /**
     * 砂箱回流轨道车辆时间
     */
    private double sandBoxRefluxTime = 0.0;
    private double hotsandMoldingTime = 0.0;
    /**
     * 出库区仿真的工具类
     */
    private final OutBoundSimulationTools tools = new OutBoundSimulationTools();
    /**
     * 用于判断事件完成时，是否满足可以改变状态的条件
     * 当区域时间小于总时间时，表示事件执行完成，true为可以改变状态
     */
    private boolean sandBoxRefluxStateChange = false;
    private boolean hotSandMoldingStateChange = false;
    /**
     * 仿真总工具类
     */
    private final SimulationTool allTool = new SimulationTool();
    /**
     * 创建事件的类
     */
    private final SandBoxRefluxEventFlow eventFlow = new SandBoxRefluxEventFlow();
    /**
     * 枚举类，用于查找当前执行的事件，从而进行当前事件的资源状态的改变，避免出错
     */
    private SandBoxRefulxEnum sandBoxRefulxEnum;
    private HotSandMoldingEnum hotSandMoldingEnum;

    private double eventTime = 0.0;

    PositionNameEnum transferEnum = null;

    public OutBoundGanttChartLinkInput SandBoxRefulxSimulationRun(OutBoundSimulationInput input, MoldClosingGanttChartLinkInput moldInput,
                                                                  GanttChart outBoundGanttChart, Double[] timeList){
        OutBoundGanttChartLinkInput simulationOutput = new OutBoundGanttChartLinkInput(outBoundGanttChart, input);
        //轨道的回流等待点位坐标坐标
        Coordinate refluxWaitCoordinate = new Coordinate((input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getX() -
                input.getPositions().get(input.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getX()) +
                input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getX(),
                input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getY());
        //轨道的放置等待点位坐标
        Coordinate layWaitCoordinate = new Coordinate(input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).getCoordinate().getX() -
                (input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getX() -
                        input.getPositions().get(input.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getX()),
                input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getY());

        if (input.getTotalTime() >= sandBoxRefluxTime){
            if (!sandBoxRefluxStateChange){
                //判断子车是否满载
                if (allTool.judgeSubCarIsEmpty(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()))){
                    //判断子车是否到达回流等待点位
                    if (input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getLocationCoordinate().equals(refluxWaitCoordinate)){
                        //判断冷热砂混合点位是否被占用
                        if (input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getStatus().equals(PositionStatusEnum.OCCUPIED)){
                            //前往冷热砂混合点位装载事件,回到等待点位
                            double executionTime = (Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getLocationCoordinate().getX() -
                                    input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getX())
                                    / input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getEmptySpeed()) +
                                    input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getTopRodRaiseOrFallTime() +
                                    (Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getHotAndColdSandBoxRefluxPositionIndex()).getCoordinate().getX() -
                                            refluxWaitCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getFullSpeed());

                            sandBoxRefluxTime = input.getTotalTime() + executionTime;
                            sandBoxRefluxStateChange = true;
                            sandBoxRefulxEnum = SandBoxRefulxEnum.EMPTY_SUB_CAR_GO_HOT_AND_COLD_REFLUX_POSITION_LOAD;
                            eventTime = executionTime;

                        }else if (input.getPositions().get(input.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getStatus().equals(PositionStatusEnum.OCCUPIED)){
                            //前往热砂回流点位装载事件,回到等待点位
                            double executionTime = (Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getLocationCoordinate().getX() -
                                    input.getPositions().get(input.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getX())
                                    / input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getEmptySpeed()) +
                                    input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getTopRodRaiseOrFallTime() +
                                    (Math.abs(input.getPositions().get(input.getOutBoundIndexInput().getHotSandBoxRefluxPositionIndex()).getCoordinate().getX() -
                                            refluxWaitCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getFullSpeed());

                            sandBoxRefluxTime = input.getTotalTime() + executionTime;
                            sandBoxRefluxStateChange = true;
                            sandBoxRefulxEnum = SandBoxRefulxEnum.EMPTY_SUB_CAR_GO_HOT_REFLUX_POSITION_LOAD;
                            eventTime = executionTime;

                        }
                    }else {
                        //前往等待点位事件
                        double executionTime = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getLocationCoordinate().getX() -
                                refluxWaitCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getEmptySpeed();

                        sandBoxRefluxTime = input.getTotalTime() + executionTime;
                        sandBoxRefluxStateChange = true;
                        sandBoxRefulxEnum = SandBoxRefulxEnum.EMPTY_SUB_CAR_GO_WAIT_POSITION_LOAD;
                        eventTime = executionTime;

                    }

                    //子车满载
                }else {
                    if (input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getLocationCoordinate().equals(layWaitCoordinate)){
                        //判断放置点位是否被占用
                        if (input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).getStatus().equals(PositionStatusEnum.UNOCCUPIED)){
                            //前往点位放置产品
                            double executionTime = (Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getLocationCoordinate().getX() -
                                    input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).getCoordinate().getX())
                                    / input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getFullSpeed()) +
                                    input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getTopRodRaiseOrFallTime();

                            sandBoxRefluxTime = input.getTotalTime() + executionTime;
                            sandBoxRefluxStateChange = true;
                            sandBoxRefulxEnum = SandBoxRefulxEnum.FULL_SUB_CAR_GO_LAY_POSITION_LAND;
                            eventTime = executionTime;

                        }
                    }else {
                        //前往放置等待点位
                        double executionTime = Math.abs(input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getLocationCoordinate().getX() -
                                layWaitCoordinate.getX()) / input.getSubCars().get(input.getOutBoundIndexInput().getSandBoxRefluxSubcarIndex()).getFullSpeed();

                        sandBoxRefluxTime = input.getTotalTime() + executionTime;
                        sandBoxRefluxStateChange = true;
                        sandBoxRefulxEnum = SandBoxRefulxEnum.FULL_SUB_CAR_GO_LAY_WAIT_POSITION_LOAD;
                        eventTime = executionTime;

                    }
                }
            }else {
                switch (sandBoxRefulxEnum){
                    case EMPTY_SUB_CAR_GO_HOT_AND_COLD_REFLUX_POSITION_LOAD:
                        //前往冷热混合砂箱回流装载的事件
                        simulationOutput = eventFlow.EmptySandBoxRefluxGoHACPositionEventCreate(simulationOutput, refluxWaitCoordinate);

                        sandBoxRefluxStateChange = false;
                        sandBoxRefulxEnum = null;
                        break;
                    case EMPTY_SUB_CAR_GO_HOT_REFLUX_POSITION_LOAD:
                        //前往热砂砂箱回流装载的事件
                        simulationOutput = eventFlow.EmptySandBoxRefluxGoHotPositionEventCreate(simulationOutput, refluxWaitCoordinate);

                        sandBoxRefluxStateChange = false;
                        sandBoxRefulxEnum = null;
                        break;
                    case EMPTY_SUB_CAR_GO_WAIT_POSITION_LOAD:
                        //前往回流点前的等待位置处
                        simulationOutput = eventFlow.EmptySandBoxRefluxGoWaitPositionEventCreate(simulationOutput, refluxWaitCoordinate);

                        sandBoxRefluxStateChange = false;
                        sandBoxRefulxEnum = null;
                        break;
                    case FULL_SUB_CAR_GO_LAY_WAIT_POSITION_LOAD:
                        //前往放置等待点位
                        simulationOutput = eventFlow.FullSandBoxRefluxGoWaitPositionEventCreate(simulationOutput, layWaitCoordinate);

                        sandBoxRefluxStateChange = false;
                        sandBoxRefulxEnum = null;
                        break;
                    case FULL_SUB_CAR_GO_LAY_POSITION_LAND:
                        //前往回流点前的等待位置处
                        simulationOutput = eventFlow.FullSandBoxRefluxGoHotPositionEventCreate(simulationOutput);

                        sandBoxRefluxStateChange = false;
                        sandBoxRefulxEnum = null;
                        break;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[2] = eventTime;
            eventTime = 0.0;
        }

        Coordinate hotsandMoldingMidCoordinate = new Coordinate((input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).getCoordinate().getX() +
                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate().getX()) / 2,
                input.getSandBoxMoldingSubCar().getLocationCoordinate().getY());

        if (input.getTotalTime() >= hotsandMoldingTime){
            if (!hotSandMoldingStateChange){
                //判断子车是否满载
                if (allTool.judgeSubCarIsEmpty(input.getSandBoxMoldingSubCar())){
                    //判断热砂回流点位（热砂砂箱合模放置点位）是否被占用，且点位占用的砂箱不是需要回流的砂箱
                    if (input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getStatus().equals(OCCUPIED) &&
                            !input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getProduct().isRefluxFlag()){
                        //发生子车前往热砂砂箱合模放置点位搬运要合模的砂箱事件，并且搬运后子车向前移动到等待点，防止后续发生车辆堵塞
                        //计算事件流的时间
                        double executionTime = (Math.abs(input.getSandBoxMoldingSubCar().getLocationCoordinate().getX() -
                                input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate().getX())
                                / input.getSandBoxMoldingSubCar().getEmptySpeed()) + input.getSandBoxMoldingSubCar().getTopRodRaiseOrFallTime() +
                                (Math.abs(input.getSandBoxMoldingSubCar().getLocationCoordinate().getX() -
                                        input.getNewPositionMap().get(PositionNameEnum.HOT_SAND_MOLD_CLOSING_WORKSTATION_ACQUISITION_POSITION_5).getCoordinate().getX())
                                        / input.getSandBoxMoldingSubCar().getFullSpeed());

                        hotsandMoldingTime = input.getTotalTime() + executionTime;
                        hotSandMoldingStateChange = true;
                        hotSandMoldingEnum = HotSandMoldingEnum.EMPTY_SUB_CAR_GO_HOT_SAND_MOLDING_POSITION_LOAD;
                        eventTime = executionTime;

                        //判断砂箱回流放置点位是否被占用
                    }else if (input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).getStatus().equals(PositionStatusEnum.OCCUPIED)){
                        //发生子车前往砂箱回流点位搬运回流砂箱的事件
                        //计算事件流的时间
                        double executionTime = (Math.abs(input.getSandBoxMoldingSubCar().getLocationCoordinate().getX() -
                                input.getNewPositionMap().get(PositionNameEnum.SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9).getCoordinate().getX())
                                / input.getSandBoxMoldingSubCar().getEmptySpeed()) + input.getSandBoxMoldingSubCar().getTopRodRaiseOrFallTime();

                        hotsandMoldingTime = input.getTotalTime() + executionTime;
                        hotSandMoldingStateChange = true;
                        hotSandMoldingEnum = HotSandMoldingEnum.EMPTY_SUB_CAR_GO_SAND_BOX_REFLUX_LAY_POSITION_LOAD;
                        eventTime = executionTime;

                        //前往中间等待点位等待事件
                    }else {
                        if (!input.getSandBoxMoldingSubCar().getLocationCoordinate().equals(hotsandMoldingMidCoordinate)){
                            //发生子车空载前往中点等待的事件
                            //计算事件流的时间
                            double executionTime = (Math.abs(input.getSandBoxMoldingSubCar().getLocationCoordinate().getX() - hotsandMoldingMidCoordinate.getX())
                                    / input.getSandBoxMoldingSubCar().getEmptySpeed());

                            hotsandMoldingTime = input.getTotalTime() + executionTime;
                            hotSandMoldingStateChange = true;
                            hotSandMoldingEnum = HotSandMoldingEnum.EMPTY_SUB_CAR_GO_MID_POSITION_WAIT;
                            eventTime = executionTime;
                        }
                    }

                    //子车满载
                }else {
                    //判断装载的砂箱是否是需要回流的砂箱(如果不是)
                    if (!input.getSandBoxMoldingSubCar().getProduct().isRefluxFlag()){
                        //选择应该前往的工岗相应位置
                        PositionNameEnum goPositionEnum = selectGoWhichHotSandMoldingPosition(moldInput);
                        if (goPositionEnum != null){
                            //发生子车满载前往相应热砂砂箱合模位置的事件
                            //计算事件流的时间
                            double executionTime = (Math.abs(input.getSandBoxMoldingSubCar().getLocationCoordinate().getX() -
                                    input.getNewPositionMap().get(goPositionEnum).getCoordinate().getX()) /
                                    input.getSandBoxMoldingSubCar().getFullSpeed()) + input.getSandBoxMoldingSubCar().getTopRodRaiseOrFallTime();

                            hotsandMoldingTime = input.getTotalTime() + executionTime;
                            hotSandMoldingStateChange = true;
                            transferEnum = goPositionEnum;
                            hotSandMoldingEnum = HotSandMoldingEnum.FULL_SUB_CAR_GO_HOT_MOLDING_POSITION_LAND;
                            eventTime = executionTime;
                        }
                    }else {
                        //判断回流的砂箱是热砂还是冷砂类型（如果是热砂类型）
                        if (input.getSandBoxMoldingSubCar().getProduct().getOrderProductType().equals(OrderProductTypeEnum.HOT_SAND)){
                            //判断是否到达放置等待点位（该等待点位可以是：热砂合模工岗砂箱获取点位_5）
                            if (input.getSandBoxMoldingSubCar().getLocationCoordinate().equals
                                    (input.getNewPositionMap().get(PositionNameEnum.HOT_SAND_MOLD_CLOSING_WORKSTATION_ACQUISITION_POSITION_5).getCoordinate())){
                                //判断砂箱回流点位是否被占用
                                if (input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getStatus().equals(PositionStatusEnum.UNOCCUPIED)){
                                    //发生子车前往砂箱回流点位放置砂箱的事件
                                    //计算事件流的时间
                                    double executionTime = (Math.abs(input.getSandBoxMoldingSubCar().getLocationCoordinate().getX() -
                                            input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getCoordinate().getX()) /
                                            input.getSandBoxMoldingSubCar().getFullSpeed()) + input.getSandBoxMoldingSubCar().getTopRodRaiseOrFallTime();

                                    hotsandMoldingTime = input.getTotalTime() + executionTime;
                                    hotSandMoldingStateChange = true;
                                    hotSandMoldingEnum = HotSandMoldingEnum.FULL_SUB_CAR_GO_HOT_SAND_REFLUX_POSITION_LAND;
                                    eventTime = executionTime;
                                }
                            }else {
                                //发生子车前往回流砂箱放置等待点位的事件
                                //计算事件流的时间
                                double executionTime = (Math.abs(input.getSandBoxMoldingSubCar().getLocationCoordinate().getX() -
                                        input.getNewPositionMap().get(PositionNameEnum.HOT_SAND_MOLD_CLOSING_WORKSTATION_ACQUISITION_POSITION_5).getCoordinate().getX()) /
                                        input.getSandBoxMoldingSubCar().getFullSpeed());

                                hotsandMoldingTime = input.getTotalTime() + executionTime;
                                hotSandMoldingStateChange = true;
                                hotSandMoldingEnum = HotSandMoldingEnum.FULL_SUB_CAR_GO_HOT_SAND_WAIT_REFLUX_POSITION;
                                eventTime = executionTime;
                            }
                            //冷砂类型
                        }else {
                            //判断运往冷砂回流轨道的行车交互点位置是否被占用
                            if (input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).getStatus().equals(PositionStatusEnum.UNOCCUPIED)){
                                //发生前往冷砂回流轨道的行车交互点放置冷砂回流砂箱的事件
                                //计算事件发生时间
                                double executionTime = (Math.abs(input.getSandBoxMoldingSubCar().getLocationCoordinate().getX() -
                                        input.getPositions().get(input.getOutBoundIndexInput().getSandBoxRowCarPositionIndex()).getCoordinate().getX())
                                        / input.getSandBoxMoldingSubCar().getFullSpeed()) + input.getSandBoxMoldingSubCar().getTopRodRaiseOrFallTime();

                                sandBoxRefluxTime = input.getTotalTime() + executionTime;
                                hotSandMoldingStateChange = true;
                                hotSandMoldingEnum = HotSandMoldingEnum.FULL_SUB_CAR_GO_COLD_SAND_REFLUX_POSITION_LAND;
                                eventTime = executionTime;
                            }
                        }
                    }
                }
            }else {
                switch (hotSandMoldingEnum){
                    case EMPTY_SUB_CAR_GO_HOT_SAND_MOLDING_POSITION_LOAD:
                        //发生子车前往热砂砂箱合模放置点位搬运要合模的砂箱事件，并且搬运后子车向前移动到等待点，防止后续发生车辆堵塞
                        eventFlow.emptySubCarGoHotMoldingPositionLoad(simulationOutput);

                        hotSandMoldingStateChange = false;
                        hotSandMoldingEnum = null;
                        break;
                    case EMPTY_SUB_CAR_GO_SAND_BOX_REFLUX_LAY_POSITION_LOAD:
                        //发生子车前往砂箱回流点位搬运回流砂箱的事件
                        eventFlow.emptySubCarGoSandBoxRefluxLayPositionLoad(simulationOutput);

                        hotSandMoldingStateChange = false;
                        hotSandMoldingEnum = null;
                        break;
                    case EMPTY_SUB_CAR_GO_MID_POSITION_WAIT:
                        //发生子车空载前往中点等待的事件
                        eventFlow.emptySubCarGoMidPositionWait(simulationOutput, hotsandMoldingMidCoordinate);

                        hotSandMoldingStateChange = false;
                        hotSandMoldingEnum = null;
                        break;
                    case FULL_SUB_CAR_GO_HOT_MOLDING_POSITION_LAND:
                        //发生子车满载前往相应热砂砂箱合模位置的事件
                        if (transferEnum != null){
                            eventFlow.fullSubCarGoHotMoldingPositionLand(simulationOutput, transferEnum);
                        }

                        hotSandMoldingStateChange = false;
                        hotSandMoldingEnum = null;
                        break;
                    case FULL_SUB_CAR_GO_HOT_SAND_REFLUX_POSITION_LAND:
                        //发生子车前往砂箱回流点位放置砂箱的事件
                        eventFlow.fullSubCarGoHotSandRefluxPositionLand(simulationOutput);

                        hotSandMoldingStateChange = false;
                        hotSandMoldingEnum = null;
                        break;
                    case FULL_SUB_CAR_GO_HOT_SAND_WAIT_REFLUX_POSITION:
                        //发生子车前往回流砂箱放置等待点位的事件
                        eventFlow.fullSubCarGoHotSandWaitRefluxPosition(simulationOutput);

                        hotSandMoldingStateChange = false;
                        hotSandMoldingEnum = null;
                        break;
                    case FULL_SUB_CAR_GO_COLD_SAND_REFLUX_POSITION_LAND:
                        //发生前往冷砂回流轨道的行车交互点放置冷砂回流砂箱的事件
                        eventFlow.fullSubCarGoColdSandRefluxPositionLand(simulationOutput);

                        hotSandMoldingStateChange = false;
                        hotSandMoldingEnum = null;
                        break;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[100] = eventTime;
            eventTime = 0.0;
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

        return simulationOutput;
    }

    public static PositionNameEnum selectGoWhichHotSandMoldingPosition(MoldClosingGanttChartLinkInput moldInput){
        MoldClosingSimulationInput input = moldInput.getInput();
        if (!(input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_4()).getStatus().equals(WorkstationStatusEnum.WORKING) ||
                input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_4()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT))){
            if (input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_4()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){
                List<Product> products = input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_4()).getProduct();

                for (Product product : products){
                    if (product.getProductType().equals(ProductTypeEnum.MOULD)){

                        return PositionNameEnum.HOT_SAND_MOLD_CLOSING_WORKSTATION_ACQUISITION_POSITION_1;
                    }
                }
            }else {

                return PositionNameEnum.HOT_SAND_MOLD_CLOSING_WORKSTATION_ACQUISITION_POSITION_1;
            }
        }
        if (!(input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_3()).getStatus().equals(WorkstationStatusEnum.WORKING) ||
                input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_3()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT))){
            if (input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_3()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){
                List<Product> products = input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_3()).getProduct();

                for (Product product : products){
                    if (product.getProductType().equals(ProductTypeEnum.MOULD)){

                        return PositionNameEnum.HOT_SAND_MOLD_CLOSING_WORKSTATION_ACQUISITION_POSITION_2;
                    }
                }
            }else {

                return PositionNameEnum.HOT_SAND_MOLD_CLOSING_WORKSTATION_ACQUISITION_POSITION_2;
            }
        }
        if (!(input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_2()).getStatus().equals(WorkstationStatusEnum.WORKING) ||
                input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_2()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT))){
            if (input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_2()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){
                List<Product> products = input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_2()).getProduct();

                for (Product product : products){
                    if (product.getProductType().equals(ProductTypeEnum.MOULD)){

                        return PositionNameEnum.HOT_SAND_MOLD_CLOSING_WORKSTATION_ACQUISITION_POSITION_3;
                    }
                }
            }else {

                return PositionNameEnum.HOT_SAND_MOLD_CLOSING_WORKSTATION_ACQUISITION_POSITION_3;
            }
        }
        if (!(input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_1()).getStatus().equals(WorkstationStatusEnum.WORKING) ||
                input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_1()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT))){
            if (input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_1()).getStatus().equals(WorkstationStatusEnum.WAIT_PAIR)){
                List<Product> products = input.getWorkStations().get(input.getMoldClosingIndexInput().getHotMoldClosingWorkStationIndex_1()).getProduct();

                for (Product product : products){
                    if (product.getProductType().equals(ProductTypeEnum.MOULD)){

                        return PositionNameEnum.HOT_SAND_MOLD_CLOSING_WORKSTATION_ACQUISITION_POSITION_4;
                    }
                }
            }else {

                return PositionNameEnum.HOT_SAND_MOLD_CLOSING_WORKSTATION_ACQUISITION_POSITION_4;
            }
        }

        return null;
    }
}
