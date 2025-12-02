package cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.simulation_v2;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.AnalogInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.OrderProductTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.moulding.MoludingHotGoClosingBoxEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.moulding.MouldingColdGoClosingBoxEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.moulding.MouldingColdGoSprayEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.moulding.MouldingMoldRefluxEnum;
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
 * @Created: 2024/12/18 16:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class MouldingSimulation_2 {
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
    /**
     * 枚举类，用于查找当前执行的事件，从而进行当前事件的资源状态的改变，避免出错
     */
    private MouldingMoldRefluxEnum moldRefluxEnum;
    private MouldingColdGoSprayEnum coldGoSprayEnum;
    private MoludingHotGoClosingBoxEnum hotGoClosingBoxEnum;
    private MouldingColdGoClosingBoxEnum coldGoClosingBoxEnum;

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
        //冷砂开模点和模具回流放置点位的中点
        Coordinate coldMoldRefluxWaitCoordinate = new Coordinate(input.getColdMoldOpeningPosition().getCoordinate().getX(),
                (input.getColdMoldOpeningPosition().getCoordinate().getY() +
                        outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate().getY()) / 2);



        if (input.getTotalTime() >= moldRefluxTime){
            //判断当前是否应该进行事件判断，或者执行上一次的事件，更改资源状态
            if (!moldRefluxStateChange){
                //判断子车是否装载
                if (allTool.judgeSubCarIsEmpty(input.getHotAndCoreMoldRefluxSubCar())){
                    //判断回流子车是否到达等待点
                    if (input.getHotAndCoreMoldRefluxSubCar().getLocationCoordinate().equals(moldRefluxWaitCoordinate)){
                        //判断制芯开模点位是否被占据
                        if (input.getCoreMoldOpeningPosition().getStatus().equals(OCCUPIED)){
                            //计算前往制芯开模点位装载时间
                            double executionTime = (Math.abs(input.getHotAndCoreMoldRefluxSubCar().getLocationCoordinate().getY() -
                                    input.getCoreMoldOpeningPosition().getCoordinate().getY()) / input.getHotAndCoreMoldRefluxSubCar().getEmptySpeed()) +
                                    input.getHotAndCoreMoldRefluxSubCar().getTopRodRaiseOrFallTime();

                            moldRefluxTime = input.getTotalTime() + executionTime;
                            moldRefluxEnum = MouldingMoldRefluxEnum.GO_CORE_MOLD_OPENING_POSITION_LOAD;
                            moldRefluxStateChange = true;
                            eventTime = executionTime;

                            //判断热砂开模点位是否被占据
                        }else if (input.getHotMoldOpeningPosition().getStatus().equals(OCCUPIED)){
                            //计算前往热砂开模点位装载时间
                            double executionTime = (Math.abs(input.getHotAndCoreMoldRefluxSubCar().getLocationCoordinate().getY() -
                                    input.getHotMoldOpeningPosition().getCoordinate().getY()) / input.getHotAndCoreMoldRefluxSubCar().getEmptySpeed()) +
                                    input.getHotAndCoreMoldRefluxSubCar().getTopRodRaiseOrFallTime();

                            moldRefluxTime = input.getTotalTime() + executionTime;
                            moldRefluxEnum = MouldingMoldRefluxEnum.GO_HOT_MOLD_OPENING_POSITION_LOAD;
                            moldRefluxStateChange = true;
                            eventTime = executionTime;

                            //判断冷砂开模点位是否被占据
                        }else if (input.getColdMoldOpeningPosition().getStatus().equals(OCCUPIED)) {
                            //计算前往冷砂开模点位装载时间
                            double executionTime = (Math.abs(input.getHotAndCoreMoldRefluxSubCar().getLocationCoordinate().getY() -
                                    input.getColdMoldOpeningPosition().getCoordinate().getY()) / input.getHotAndCoreMoldRefluxSubCar().getEmptySpeed()) +
                                    input.getHotAndCoreMoldRefluxSubCar().getTopRodRaiseOrFallTime();

                            moldRefluxTime = input.getTotalTime() + executionTime;
                            moldRefluxEnum = MouldingMoldRefluxEnum.GO_COLD_MOLD_OPENING_POSITION_LOAD;
                            moldRefluxStateChange = true;
                            eventTime = executionTime;

                        }

                        //未到等待点
                    }else {
                        //计算前往等待点的时间
                        double executionTime = Math.abs(input.getHotAndCoreMoldRefluxSubCar().getLocationCoordinate().getY() -
                                moldRefluxWaitCoordinate.getY()) / input.getHotAndCoreMoldRefluxSubCar().getEmptySpeed();

                        moldRefluxTime = input.getTotalTime() + executionTime;
                        moldRefluxEnum = MouldingMoldRefluxEnum.EMPTY_GO_WAIT_POSITION;
                        moldRefluxStateChange = true;
                        eventTime = executionTime;

                    }

                    //子车满载
                }else {
                    //判断车辆装载的是否为冷砂
                    if (!input.getHotAndCoreMoldRefluxSubCar().getProduct().getOrderProductType().equals(OrderProductTypeEnum.COLD_SAND)){
                        //判断车辆是否到达回流点前的等待位置处
                        if (input.getHotAndCoreMoldRefluxSubCar().getLocationCoordinate().equals(waitLayCoordinate)){
                            //判断回流模具的轨道是否被占用并且热砂和砂芯模具回流的点位是否被占用
                            if (outBoundInput.getPaths().get(outBoundInput.getOutBoundIndexInput().getMoldRefluxPathIndex()).getStatus().equals(PATH_UNOCCUPIED) &&
                                    outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getStatus().equals(PositionStatusEnum.UNOCCUPIED)
                                    && !moldRefluxSubCarLayProductFlag){

                                //计算前往点位放置砂箱的时间
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


                            }else if (moldRefluxSubCarLayProductFlag){
                                //发生前往点位放置砂箱的事件流
                                simulationOutput = mouldingEventFlow.FullMoldRefluxLayPositionEventCreate(simulationOutput, waitLayCoordinate);
                                outBoundInput.getPaths().get(outBoundInput.getOutBoundIndexInput().getMoldRefluxPathIndex()).setStatus(PATH_UNOCCUPIED);
                                moldRefluxSubCarLayProductFlag = false;
                            }

                            //没有到达回流点前的等待位置处
                        }else {
                            //计算前往回流点前的等待点的时间
                            double executionTime = Math.abs(input.getHotAndCoreMoldRefluxSubCar().getLocationCoordinate().getY() - waitLayCoordinate.getY())
                                    / input.getHotAndCoreMoldRefluxSubCar().getFullSpeed();

                            moldRefluxTime = input.getTotalTime() + executionTime;
                            moldRefluxEnum = MouldingMoldRefluxEnum.FULL_GO_WAIT_POSITION;
                            moldRefluxStateChange = true;
                            eventTime = executionTime;

                        }

                        //车辆装载的为冷砂
                    }else {
                        //判断子车是否到达中间等待点位
                        if (input.getHotAndCoreMoldRefluxSubCar().getLocationCoordinate().equals(coldMoldRefluxWaitCoordinate)){
                            //判断回流点位是否被占用
                            if (outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getStatus().equals(UNOCCUPIED)){
                                //计算前往点位放置砂箱的时间
                                double executionTime = (Math.abs(input.getColdMoldRefluxSubCar().getLocationCoordinate().getY() -
                                        outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInput().getHotSandMoldRefluxPositionIndex()).getCoordinate().getY())
                                        / input.getColdMoldRefluxSubCar().getFullSpeed()) + input.getColdMoldRefluxSubCar().getTopRodRaiseOrFallTime();

                                moldRefluxTime = input.getTotalTime() + executionTime;
                                moldRefluxEnum = MouldingMoldRefluxEnum.FULL_GO_MOLD_LAY_POSITION;
                                moldRefluxStateChange = true;
                                eventTime = executionTime;

                            }

                            //子车未到等待点
                        }else {
                            //计算前往冷砂等待点位的时间
                            double executionTime = Math.abs(input.getColdMoldRefluxSubCar().getLocationCoordinate().getY() -
                                    coldMoldRefluxWaitCoordinate.getY()) / input.getColdMoldRefluxSubCar().getFullSpeed();

                            coldMoldRefluxTime = input.getTotalTime() + executionTime;
                            moldRefluxEnum = MouldingMoldRefluxEnum.FULL_GO_COLD_SAND_WAIT_POSITION;
                            moldRefluxStateChange = true;
                            eventTime = executionTime;

                        }
                    }

                }
            }else {
                switch (moldRefluxEnum){
                    case MouldingMoldRefluxEnum.GO_CORE_MOLD_OPENING_POSITION_LOAD:
                        //发生前往制芯开模点位装载的事件流
                        simulationOutput = mouldingEventFlow.EmptyCoreMoldOpeningPositionEventCreate(simulationOutput);

                        moldRefluxStateChange = false;
                        moldRefluxEnum = null;
                        break;
                    case MouldingMoldRefluxEnum.GO_HOT_MOLD_OPENING_POSITION_LOAD:
                        //发生前往热砂开模点位装载的事件流
                        simulationOutput = mouldingEventFlow.EmptyHotMoldOpeningPositionEventCreate(simulationOutput);

                        moldRefluxStateChange = false;
                        moldRefluxEnum = null;
                        break;
                    case MouldingMoldRefluxEnum.GO_COLD_MOLD_OPENING_POSITION_LOAD:
                        //发生前往冷砂开模点位装载的事件流
                        simulationOutput = mouldingEventFlow.EmptyColdMoldOpeningPositionEventCreate(simulationOutput);

                        moldRefluxStateChange = false;
                        moldRefluxEnum = null;
                        break;
                    case MouldingMoldRefluxEnum.EMPTY_GO_WAIT_POSITION:
                        //发生空载前往回流点前的等待位置事件流
                        simulationOutput = mouldingEventFlow.EmptyGoRefluxWaitPositionEventCreate(simulationOutput, moldRefluxWaitCoordinate);

                        moldRefluxStateChange = false;
                        moldRefluxEnum = null;
                        break;
                    case MouldingMoldRefluxEnum.FULL_GO_WAIT_POSITION:
                        //发生满载前往回流点前的等待位置事件流
                        simulationOutput = mouldingEventFlow.FullGoRefluxWaitPositionEventCreate(simulationOutput, waitLayCoordinate);

                        moldRefluxStateChange = false;
                        moldRefluxEnum = null;
                        break;
                    case MouldingMoldRefluxEnum.FULL_GO_MOLD_LAY_POSITION:
                        //发生满载前往冷砂模具回流点事件
                        simulationOutput = mouldingEventFlow.FullColdGoRefluxPositionEventCreate(simulationOutput);
                        moldRefluxStateChange = false;
                        moldRefluxEnum = null;
                        break;
                    case MouldingMoldRefluxEnum.FULL_GO_COLD_SAND_WAIT_POSITION:
                        //发生满载前往冷砂模具回流点前的等待位置事件流
                        simulationOutput = mouldingEventFlow.FullColdGoRefluxWaitPositionEventCreate(simulationOutput, coldMoldRefluxWaitCoordinate);
                        moldRefluxStateChange = false;
                        moldRefluxEnum = null;
                        break;
                }
            }

        }

        if (eventTime != 0.0){
            timeList[15] = eventTime;
            eventTime = 0.0;
        }


        Coordinate coldSprayWaitCoordinate = new Coordinate(input.getColdSandBoxOpeningPosition().getCoordinate().getX(),
                (input.getColdSandBoxOpeningPosition().getCoordinate().getY() + input.getColdSandBoxSprayPosition_1().getCoordinate().getY()) / 2);

        if (input.getTotalTime() >= coldSprayTime){
            if (!coldSprayStateChange){
                //判断子车是否满载
                if (allTool.judgeSubCarIsEmpty(input.getColdSandBoxSubCar())){
                    //判断车辆是否到达等待点位
                    if (input.getColdSandBoxSubCar().getLocationCoordinate().equals(coldSprayWaitCoordinate)){
                        //判断冷砂开模砂箱放置点位是否被占用
                        if (input.getColdSandBoxOpeningPosition().getStatus().equals(OCCUPIED)){
                            //计算子车发前往冷砂开模砂箱点位装载的时间
                            double executionTime = (Math.abs(input.getColdSandBoxSubCar().getLocationCoordinate().getY() -
                                    input.getColdSandBoxOpeningPosition().getCoordinate().getY()) / input.getColdSandBoxSubCar().getEmptySpeed()) +
                                    input.getColdSandBoxSubCar().getTopRodRaiseOrFallTime();

                            coldSprayTime = input.getTotalTime() + executionTime;
                            coldSprayStateChange = true;
                            coldGoSprayEnum = MouldingColdGoSprayEnum.GO_COLD_SAND_OPENING_POSITION_LOAD;
                            eventTime = executionTime;

                        }
                    }else {
                        //计算子车空载前往等待点的事件
                        double executionTime = Math.abs(input.getColdSandBoxSubCar().getLocationCoordinate().getY() - coldSprayWaitCoordinate.getY())
                                / input.getColdSandBoxSubCar().getEmptySpeed();

                        coldSprayTime = input.getTotalTime() + executionTime;
                        coldSprayStateChange = true;
                        coldGoSprayEnum = MouldingColdGoSprayEnum.EMPTY_GO_WAIT_POSITION;
                        eventTime = executionTime;

                    }

                    //子车满载
                }else {
                    //判断车辆是否到达等待点位
                    if (input.getColdSandBoxSubCar().getLocationCoordinate().equals(coldSprayWaitCoordinate)){
                        if (input.getColdSandBoxSprayPosition_1().getStatus().equals(UNOCCUPIED)){
                            //计算子车满载前往冷砂喷涂点的时间
                            double executionTime = (Math.abs(input.getColdSandBoxSubCar().getLocationCoordinate().getY() -
                                    input.getColdSandBoxSprayPosition_1().getCoordinate().getY()) / input.getColdSandBoxSubCar().getFullSpeed()) +
                                    input.getColdSandBoxSubCar().getTopRodRaiseOrFallTime();
                            coldSprayTime = input.getTotalTime() + executionTime;
                            coldGoSprayEnum = MouldingColdGoSprayEnum.GO_COLD_SPARY_POSITION_LANDING;
                            coldSprayStateChange = true;

                            eventTime = executionTime;
                        }
                    }else {
                        //计算子车满载前往等待点的事件
                        double executionTime = Math.abs(input.getColdSandBoxSubCar().getLocationCoordinate().getY() - coldSprayWaitCoordinate.getY())
                                / input.getColdSandBoxSubCar().getFullSpeed();

                        coldSprayTime = input.getTotalTime() + executionTime;
                        coldSprayStateChange = true;
                        coldGoSprayEnum = MouldingColdGoSprayEnum.FULL_GO_WAIT_POSITION;
                        eventTime = executionTime;
                    }
                }
            }else {
                switch (coldGoSprayEnum){
                    case MouldingColdGoSprayEnum.GO_COLD_SAND_OPENING_POSITION_LOAD:
                        //发生子车发前往冷砂开模砂箱点位装载的事件
                        simulationOutput = mouldingEventFlow.EmptyColdSprayGoPositionEventCreate(simulationOutput);

                        coldSprayStateChange = false;
                        coldGoSprayEnum = null;
                        break;
                    case MouldingColdGoSprayEnum.EMPTY_GO_WAIT_POSITION:
                        //发生空载前往等待点前的事件
                        simulationOutput = mouldingEventFlow.EmptyColdSprayGoWaitPositionEventCreate(simulationOutput, coldSprayWaitCoordinate);

                        coldSprayStateChange = false;
                        coldGoSprayEnum = null;
                        break;
                    case MouldingColdGoSprayEnum.GO_COLD_SPARY_POSITION_LANDING:
                        //发生满载前往喷涂点位的事件
                        simulationOutput = mouldingEventFlow.FullColdSprayGoPositionEventCreate(simulationOutput);

                        coldSprayStateChange = false;
                        coldGoSprayEnum = null;
                        break;
                    case MouldingColdGoSprayEnum.FULL_GO_WAIT_POSITION:
                        //发生满载前往等待点前的事件
                        simulationOutput = mouldingEventFlow.FullColdSprayGoWaitPositionEventCreate(simulationOutput, coldSprayWaitCoordinate);

                        coldSprayStateChange = false;
                        coldGoSprayEnum = null;
                        break;
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
                input.getHotSpray_3().setEquipmentStatus(EquipmentOperationStatusEnum.FINISHED);
                if (input.getHotSpray_3().getProducts().getFirst().getProductType().equals(ProductTypeEnum.UPPER_BOX)){
                    input.getHotSpray_3().getProducts().getFirst().setProductType(ProductTypeEnum.UPPER_SAND_SHELL_BOX);
                }else if (input.getHotSpray_3().getProducts().getFirst().getProductType().equals(ProductTypeEnum.LOWER_BOX)){
                    input.getHotSpray_3().getProducts().getFirst().setProductType(ProductTypeEnum.LOWER_SAND_SHELL_BOX);
                }

                hotSprayUp.setWorkFlag(false);
            }
        }else {
            if (input.getHotSandBoxOpeningPosition_2().getStatus().equals(OCCUPIED)
                    && input.getHotSpray_3().getEquipmentStatus().equals(EquipmentOperationStatusEnum.STANDBY)){
                hotSprayUp.setWorkFlag(true);
                List<Product> products = new ArrayList<>();
                products.add(input.getHotSandBoxOpeningPosition_2().getProduct().productDeepCopy());
                input.getHotSandBoxOpeningPosition_2().setProduct(null);
                input.getHotSandBoxOpeningPosition_2().setStatus(UNOCCUPIED);
                input.getHotSpray_3().setProducts(products);
                input.getHotSpray_3().setEquipmentStatus(EquipmentOperationStatusEnum.WORKING);
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
            if (!hotClosingStateChange){
                //判断子车是否装载
                if (allTool.judgeSubCarIsEmpty(input.getHotSprayGoClosingBoxSubCar())){
                    //判断上喷涂设备是否喷涂完成
                    if (input.getHotSpray_3().getEquipmentStatus().equals(EquipmentOperationStatusEnum.FINISHED)){
                        //计算前往热砂喷涂设备装载喷涂完成砂箱时间
                        double executionTime = (Math.abs(input.getHotSprayGoClosingBoxferry().getLocationCoordinate().getY() -
                                input.getHotSpray_3().getEquipmentCoordinate().getY()) / input.getHotSprayGoClosingBoxferry().getEmptySpeed()) +
                                (Math.abs(input.getHotSprayGoClosingBoxSubCar().getLocationCoordinate().getX() - input.getHotSpray_3().getEquipmentCoordinate().getX())
                                        / input.getHotSprayGoClosingBoxSubCar().getEmptySpeed()) + input.getHotSprayGoClosingBoxSubCar().getTopRodRaiseOrFallTime() +
                                (Math.abs(input.getHotSprayGoClosingBoxSubCar().getLocationCoordinate().getX() - input.getHotSpray_3().getEquipmentCoordinate().getX())
                                        / input.getHotSprayGoClosingBoxSubCar().getFullSpeed());

                        hotSprayGoClosingTime = input.getTotalTime() + executionTime;
                        hotClosingStateChange = true;
                        hotGoClosingBoxEnum = MoludingHotGoClosingBoxEnum.GO_HOT_SPARY_EQUIPMENT_LOAD;
                        eventTime = executionTime;

                        log.info("前往热砂喷涂设备上装载" + hotSprayGoClosingTime + input.getHotSpray_1().getProducts());
                    }
                }else {
                    //判断前往合箱的点位是否被占用
                    if (simulationOutput.getClosingBoxInput().getInput().getHotClosingBoxPosition().getStatus().equals(UNOCCUPIED)){
                        //计算前往热砂合箱点位卸载砂箱的时间
                        double executionTime = (Math.abs(input.getHotSprayGoClosingBoxferry().getLocationCoordinate().getY() -
                                simulationOutput.getClosingBoxInput().getInput().getHotClosingBoxPosition().getCoordinate().getY()) /
                                input.getHotSprayGoClosingBoxferry().getFullSpeed()) + (Math.abs(input.getHotSprayGoClosingBoxSubCar().getLocationCoordinate().getX() -
                                simulationOutput.getClosingBoxInput().getInput().getHotClosingBoxPosition().getCoordinate().getX())
                                        / input.getHotSprayGoClosingBoxSubCar().getFullSpeed()) + input.getHotSprayGoClosingBoxSubCar().getTopRodRaiseOrFallTime() +
                                (Math.abs(input.getHotSprayGoClosingBoxSubCar().getLocationCoordinate().getX() -
                                        simulationOutput.getClosingBoxInput().getInput().getHotClosingBoxPosition().getCoordinate().getX()) /
                                        input.getHotSprayGoClosingBoxSubCar().getEmptySpeed());

                        hotSprayGoClosingTime = input.getTotalTime() + executionTime;
                        hotClosingStateChange = true;
                        hotGoClosingBoxEnum = MoludingHotGoClosingBoxEnum.GO_HOT_CLOSING_BOX_LAND;
                        eventTime = executionTime;

                    }
                }
            }else {
                switch (hotGoClosingBoxEnum){
                    case MoludingHotGoClosingBoxEnum.GO_HOT_SPARY_EQUIPMENT_LOAD:
                        //发生前往热砂喷涂设备装载喷涂完成砂箱的事件
                        simulationOutput = sprayGoClosingEventFlow.EmptyHotSprayGoClosingBoxEventCreate(simulationOutput);

                        hotClosingStateChange = false;
                        hotGoClosingBoxEnum = null;
                        break;
                    case MoludingHotGoClosingBoxEnum.GO_HOT_CLOSING_BOX_LAND:
                        //发生前往热砂合箱点位卸载砂箱的事件
                        simulationOutput = sprayGoClosingEventFlow.FullHotSprayGoClosingBoxEventCreate(simulationOutput);

                        hotClosingStateChange = false;
                        hotGoClosingBoxEnum = null;
                        break;
                }
            }
        }

        if (eventTime != 0.0){
            timeList[19] = eventTime;
            eventTime = 0.0;
        }

        //冷砂喷涂前往合箱
        if (input.getTotalTime() >= coldSprayGoClosingTime){
            if (!coldClosingStateChange){
                //判断子车是否装载
                if (allTool.judgeSubCarIsEmpty(input.getColdSprayGoClosingBoxSubCar())){
                    //判断上喷涂设备是否喷涂完成
                    if (input.getColdSpray_3().getEquipmentStatus().equals(EquipmentOperationStatusEnum.FINISHED)){
                        //计算前往冷砂喷涂点搬运砂箱时间
                        double executionTime = (Math.abs(input.getColdSprayGoClosingBoxferry().getLocationCoordinate().getY() -
                                input.getColdSpray_3().getEquipmentCoordinate().getY()) / input.getColdSprayGoClosingBoxferry().getEmptySpeed()) +
                                (Math.abs(input.getColdSprayGoClosingBoxSubCar().getLocationCoordinate().getX() - input.getColdSpray_3().getEquipmentCoordinate().getX())
                                        / input.getColdSprayGoClosingBoxSubCar().getEmptySpeed()) + input.getColdSprayGoClosingBoxSubCar().getTopRodRaiseOrFallTime() +
                                (Math.abs(input.getColdSprayGoClosingBoxSubCar().getLocationCoordinate().getX() - input.getColdSpray_3().getEquipmentCoordinate().getX())
                                        / input.getColdSprayGoClosingBoxSubCar().getFullSpeed());

                        coldSprayGoClosingTime = input.getTotalTime() + executionTime;
                        coldClosingStateChange = true;
                        coldGoClosingBoxEnum = MouldingColdGoClosingBoxEnum.GO_COLD_SPARY_EQUIPMENT_LOAD;
                        eventTime = executionTime;

                        log.info("前往冷砂喷涂设备上装载" + coldSprayGoClosingTime + input.getColdSpray_1().getProducts());
                        //判断下喷涂设备是否喷涂完成
                    }
                }else {
                    //判断前往合箱的点位是否被占用
                    if (simulationOutput.getClosingBoxInput().getInput().getColdClosingBoxPosition().getStatus().equals(UNOCCUPIED)){
                        //计算前往冷砂合箱点位装载卸箱的时间
                        double executionTime = (Math.abs(input.getColdSprayGoClosingBoxferry().getLocationCoordinate().getY() -
                                simulationOutput.getClosingBoxInput().getInput().getColdClosingBoxPosition().getCoordinate().getY()) /
                                input.getColdSprayGoClosingBoxferry().getFullSpeed()) + (Math.abs(input.getColdSprayGoClosingBoxSubCar().getLocationCoordinate().getX() -
                                simulationOutput.getClosingBoxInput().getInput().getColdClosingBoxPosition().getCoordinate().getX())
                                        / input.getColdSprayGoClosingBoxSubCar().getFullSpeed()) + input.getColdSprayGoClosingBoxSubCar().getTopRodRaiseOrFallTime() +
                                (Math.abs(input.getColdSprayGoClosingBoxSubCar().getLocationCoordinate().getX() -
                                        simulationOutput.getClosingBoxInput().getInput().getColdClosingBoxPosition().getCoordinate().getX()) /
                                        input.getColdSprayGoClosingBoxSubCar().getEmptySpeed());

                        coldSprayGoClosingTime = input.getTotalTime() + executionTime;
                        coldClosingStateChange = true;
                        coldGoClosingBoxEnum = MouldingColdGoClosingBoxEnum.GO_COLD_CLOSING_BOX_LAND;
                        eventTime = executionTime;

                    }
                }
            }else {
                switch (coldGoClosingBoxEnum){
                    case MouldingColdGoClosingBoxEnum.GO_COLD_SPARY_EQUIPMENT_LOAD:
                        //发生前往冷砂喷涂点搬运砂箱的事件
                        simulationOutput = sprayGoClosingEventFlow.EmptyColdSprayGoClosingBoxUpEventCreate(simulationOutput);

                        coldClosingStateChange = false;
                        coldGoClosingBoxEnum = null;
                        break;
                    case MouldingColdGoClosingBoxEnum.GO_COLD_CLOSING_BOX_LAND:
                        //发生前往冷砂合箱点位装载卸箱的事件
                        simulationOutput = sprayGoClosingEventFlow.FullColdSprayGoClosingBoxEventCreate(simulationOutput);

                        coldClosingStateChange = false;
                        coldGoClosingBoxEnum = null;
                        break;
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
