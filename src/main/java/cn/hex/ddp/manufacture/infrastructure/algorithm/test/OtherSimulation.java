package cn.hex.ddp.manufacture.infrastructure.algorithm.test;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.AnalogInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.OtherSimulationLink;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.closingBox.ClosingBoxGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.moldClosing.MoldClosingGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.mouldingLine.MouldingGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound.OutBoundGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.List;

import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.OCCUPIED;
import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.UNOCCUPIED;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum.SUB_CAR;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum.LANDING;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum.RUNNING;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum.MOULD;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum.UPPER_BOX;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/6/7 20:22
 */
@Data
@Slf4j
public class OtherSimulation {
    private double hotSandTime = 0.0;
    private int hotCount = 0;
    private double coldSandTime = 0.0;
    private int coldCount = 0;
    private double coreMakeTime = 0.0;
    private int coreCount = 0;
    private int refluxCount = 0;
    private int productCount = 0;
    private List<Product> hotSandProducts = new ArrayList<>();
    private List<Product> hotMoleProducts = new ArrayList<>();
    private List<Product> coldSandProducts = new ArrayList<>();
    private List<Product> coldMoleProducts = new ArrayList<>();
    private List<Product> coreProducts = new ArrayList<>();
    private double hotMoleRefluxTime = 0.0;
    private double hotSandRefluxTime = 0.0;
    private double coldMoleRefluxTime = 0.0;
    private double coldSandRefluxTime = 0.0;
    private double coreAndMoleRefluxTime = 0.0;
    private double hotSprayTime_1 = 0.0;
    private double hotSprayTime_2 = 0.0;
    private double coldSprayTime_1 = 0.0;
    private double coldSprayTime_2 = 0.0;

    private static SimulationTool allTools = new SimulationTool();

    public OtherSimulationLink otherSimulationRun(OutBoundGanttChartLinkInput input, MoldClosingGanttChartLinkInput moldInput, MouldingGanttChartLinkInput mouldingInput,
                                                  ClosingBoxGanttChartLinkInput closingBoxInput, double dtime, int finishedCount, AnalogInput analogInput){
        //热砂造型模拟
        if (moldInput.getInput().getHotMoldClosingLayPosition().getStatus().equals(OCCUPIED)){
            hotSandTime = hotSandTime + dtime;
            if (hotSandTime >= 6 * 60){
                //模拟开模
                if (mouldingInput.getInput().getHotMoldOpeningPosition().getStatus().equals(UNOCCUPIED) &&
                        mouldingInput.getInput().getHotSandBoxOpeningPosition_2().getStatus().equals(UNOCCUPIED)) {
                    moldInput.getInput().getHotMoldClosingLayPosition().setStatus(UNOCCUPIED);
                    Product product = moldInput.getInput().getHotMoldClosingLayPosition().getProduct().productDeepCopy();
                    moldInput.getInput().getHotMoldClosingLayPosition().setProduct(null);
                    for (Product product1 : product.getProducts()) {
                        if (product1.getProductType().equals(MOULD)){
                            mouldingInput.getInput().getHotMoldOpeningPosition().setStatus(OCCUPIED);
                            Product product2 = product1.productDeepCopy();
                            product2.setRefluxFlag(true);
                            mouldingInput.getInput().getHotMoldOpeningPosition().setProduct(product2);
//                            log.info("！！！" + " " + mouldingInput.getInput().getHotMoldOpeningPosition().getProduct());
                        }else {
                            mouldingInput.getInput().getHotSandBoxOpeningPosition_2().setStatus(OCCUPIED);
                            mouldingInput.getInput().getHotSandBoxOpeningPosition_2().setProduct(product1.productDeepCopy());
//                            log.info("???" + " " + mouldingInput.getInput().getHotSandBoxOpeningPosition_2().getProduct());
                        }
                    }

                    log.info("?Hot!!!:" + product.getProducts().size() + "???" + product);
                    product.setProductType(MOULD);
                    hotMoleProducts.add(product);
                    product.setProductType(UPPER_BOX);
                    hotSandProducts.add(product);
                    hotSandTime = 0.0;
                    hotCount++;

                    createHotMouldEvent(input, mouldingInput, moldInput, analogInput);
                }
            }
        }

        //冷砂造型模拟
        if (moldInput.getInput().getColdMoldClosingLayPosition().getStatus().equals(OCCUPIED)){
            coldSandTime = coldSandTime + dtime;
            if (coldSandTime >= 6 * 60){
                if (mouldingInput.getInput().getColdMoldOpeningPosition().getStatus().equals(UNOCCUPIED) &&
                        mouldingInput.getInput().getColdSandBoxOpeningPosition().getStatus().equals(UNOCCUPIED)){
                    moldInput.getInput().getColdMoldClosingLayPosition().setStatus(UNOCCUPIED);
                    Product product = moldInput.getInput().getColdMoldClosingLayPosition().getProduct().productDeepCopy();
                    moldInput.getInput().getColdMoldClosingLayPosition().setProduct(null);
                    //模拟开模
                    for (Product product1 : product.getProducts()) {
                        if (product1.getProductType().equals(MOULD)){
                            mouldingInput.getInput().getColdMoldOpeningPosition().setStatus(OCCUPIED);
                            Product product2 = product1.productDeepCopy();
                            product2.setRefluxFlag(true);
                            mouldingInput.getInput().getColdMoldOpeningPosition().setProduct(product2);
                        }else {
                            mouldingInput.getInput().getColdSandBoxOpeningPosition().setStatus(OCCUPIED);
                            mouldingInput.getInput().getColdSandBoxOpeningPosition().setProduct(product1.productDeepCopy());
                        }
                    }

                    log.info("?cold!!!:" + product.getProducts().size() + "???" + product);
                    product.setProductType(MOULD);
                    coldMoleProducts.add(product);
                    product.setProductType(UPPER_BOX);
                    coldSandProducts.add(product);
                    coldSandTime = 0.0;
                    coldCount++;

                    createColdMouldEvent(input, mouldingInput, moldInput, analogInput);
                }
            }
        }

        //砂芯造型模拟
        if (input.getInput().getPositions().get(input.getInput().getOutBoundIndexInput().getCoreMakingLayPositionIndex()).getStatus().equals(OCCUPIED)){
            coreMakeTime = coreMakeTime + dtime;
            if (coreMakeTime >= 6 * 60){
                if (mouldingInput.getInput().getCoreMoldOpeningPosition().getStatus().equals(UNOCCUPIED)){
                    input.getInput().getPositions().get(input.getInput().getOutBoundIndexInput().getCoreMakingLayPositionIndex()).setStatus(UNOCCUPIED);
                    Product product = input.getInput().getPositions().get(input.getInput().getOutBoundIndexInput().getCoreMakingLayPositionIndex()).getProduct().productDeepCopy();
                    input.getInput().getPositions().get(input.getInput().getOutBoundIndexInput().getCoreMakingLayPositionIndex()).setProduct(null);
                    mouldingInput.getInput().getCoreMoldOpeningPosition().setStatus(OCCUPIED);
                    product.setRefluxFlag(true);
                    mouldingInput.getInput().getCoreMoldOpeningPosition().setProduct(product);
                    coreProducts.add(product);
                    coreMakeTime = 0.0;
                    coreCount++;

                    createCoreMouldEvent(input, mouldingInput, moldInput, analogInput);
//                    log.info("?mold");
                }
//                log.info("?");
            }
        }

//        //设备工作模拟
//        if (mouldingInput.getInput().getHotSpray_1().getEquipmentStatus().equals(EquipmentOperationStatusEnum.WORKING)){
//            hotSprayTime_1 = hotSprayTime_1 + dtime;
//            if (hotSprayTime_1 >= 6 * 60){
//                mouldingInput.getInput().getHotSpray_1().setEquipmentStatus(EquipmentOperationStatusEnum.FINISHED);
//                hotSprayTime_1 = 0.0;
//            }
//        }
//
//        if (mouldingInput.getInput().getHotSpray_3().getEquipmentStatus().equals(EquipmentOperationStatusEnum.WORKING)){
//            hotSprayTime_2 = hotSprayTime_2 + dtime;
//            if (hotSprayTime_2 >= 6 * 60){
//                mouldingInput.getInput().getHotSpray_3().setEquipmentStatus(EquipmentOperationStatusEnum.FINISHED);
//                hotSprayTime_2 = 0.0;
//            }
//        }
//
//        if (mouldingInput.getInput().getColdSpray_1().getEquipmentStatus().equals(EquipmentOperationStatusEnum.WORKING)){
//            coldSprayTime_1 = coldSprayTime_1 + dtime;
//            if (coldSprayTime_1 >= 6 * 60){
//                mouldingInput.getInput().getColdSpray_1().setEquipmentStatus(EquipmentOperationStatusEnum.FINISHED);
//                coldSprayTime_1 = 0.0;
//            }
//        }
//
//        if (mouldingInput.getInput().getColdSpray_3().getEquipmentStatus().equals(EquipmentOperationStatusEnum.WORKING)){
//            coldSprayTime_2 = coldSprayTime_2 + dtime;
//            if (coldSprayTime_2 >= 6 * 60){
//                mouldingInput.getInput().getColdSpray_3().setEquipmentStatus(EquipmentOperationStatusEnum.FINISHED);
//                coldSprayTime_2 = 0.0;
//            }
//        }
//
//        //设备搬运模拟
//        if (mouldingInput.getInput().getHotSandBoxOpeningPosition_2().getStatus().equals(OCCUPIED)){
//            if (mouldingInput.getInput().getHotSpray_1().getEquipmentStatus().equals(EquipmentOperationStatusEnum.STANDBY)){
//                Product product = mouldingInput.getInput().getHotSandBoxOpeningPosition_2().getProduct().productDeepCopy();
//                mouldingInput.getInput().getHotSandBoxOpeningPosition_2().setProduct(null);
//                mouldingInput.getInput().getHotSpray_1().setEquipmentStatus(EquipmentOperationStatusEnum.WORKING);
//                List<Product> products = new ArrayList<>();
//                products.add(product);
//                mouldingInput.getInput().getHotSpray_1().setProducts(products);
//            }
//        }
//
//        if (mouldingInput.getInput().getHotSandBoxSprayDownPosition().getStatus().equals(OCCUPIED)){
//            if (mouldingInput.getInput().getHotSpray_3().getEquipmentStatus().equals(EquipmentOperationStatusEnum.STANDBY)){
//                Product product = mouldingInput.getInput().getHotSandBoxSprayDownPosition().getProduct().productDeepCopy();
//                mouldingInput.getInput().getHotSandBoxSprayDownPosition().setProduct(null);
//                mouldingInput.getInput().getHotSpray_3().setEquipmentStatus(EquipmentOperationStatusEnum.WORKING);
//                List<Product> products = new ArrayList<>();
//                products.add(product);
//                mouldingInput.getInput().getHotSpray_3().setProducts(products);
//            }
//        }
//
//        if (mouldingInput.getInput().getColdSandBoxSprayPosition_1().getStatus().equals(OCCUPIED)){
//            if (mouldingInput.getInput().getColdSpray_1().getEquipmentStatus().equals(EquipmentOperationStatusEnum.STANDBY)){
//                Product product = mouldingInput.getInput().getColdSandBoxSprayPosition_1().getProduct().productDeepCopy();
//                mouldingInput.getInput().getColdSandBoxSprayPosition_1().setProduct(null);
//                mouldingInput.getInput().getColdSpray_1().setEquipmentStatus(EquipmentOperationStatusEnum.WORKING);
//                List<Product> products = new ArrayList<>();
//                products.add(product);
//                mouldingInput.getInput().getColdSpray_1().setProducts(products);
//            }
//        }
//
//        if (mouldingInput.getInput().getColdSandBoxSprayPosition_2().getStatus().equals(OCCUPIED)){
//            if (mouldingInput.getInput().getColdSpray_3().getEquipmentStatus().equals(EquipmentOperationStatusEnum.STANDBY)){
//                Product product = mouldingInput.getInput().getColdSandBoxSprayPosition_2().getProduct().productDeepCopy();
//                mouldingInput.getInput().getColdSandBoxSprayPosition_2().setProduct(null);
//                mouldingInput.getInput().getColdSpray_3().setEquipmentStatus(EquipmentOperationStatusEnum.WORKING);
//                List<Product> products = new ArrayList<>();
//                products.add(product);
//                mouldingInput.getInput().getColdSpray_3().setProducts(products);
//            }
//        }

//        //合箱点位模拟
//        if (closingBoxInput.getInput().getHotClosingBoxGoPourPosition().getStatus().equals(OCCUPIED)){
//            closingBoxInput.getInput().getHotClosingBoxGoPourPosition().setStatus(UNOCCUPIED);
//            productCount++;
//            log.info("Hot:" + productCount);
//        }
//
//        if (closingBoxInput.getInput().getColdClosingBoxGoPourPosition().getStatus().equals(OCCUPIED)){
//            closingBoxInput.getInput().getColdClosingBoxGoPourPosition().setStatus(UNOCCUPIED);
//            productCount++;
//            log.info("Cold:" + productCount);
//        }

//        if (!hotMoleProducts.isEmpty()){
//            hotMoleRefluxTime = hotMoleRefluxTime + dtime;
//            if (hotMoleRefluxTime >= 15 * 60){
//                if (input.getInput().getPositions().get(input.getInput().getOutBoundIndexInput().getMoldRefluxPositionIndex()).getStatus().equals(UNOCCUPIED)){
//                    input.getInput().getPositions().get(input.getInput().getOutBoundIndexInput().getMoldRefluxPositionIndex()).setStatus(OCCUPIED);
//                    Product product = hotMoleProducts.getFirst();
//                    product.setRefluxFlag(true);
//                    input.getInput().getPositions().get(input.getInput().getOutBoundIndexInput().getMoldRefluxPositionIndex()).setProduct(product);
//                    hotMoleProducts.remove(0);
//                    hotMoleRefluxTime = 0.0;
//                }
//            }
//        }
//
//        if (!coldMoleProducts.isEmpty()){
//            coldMoleRefluxTime = coldMoleRefluxTime + dtime;
//            if (coldMoleRefluxTime >= 18 * 60){
//                if (input.getInput().getPositions().get(input.getInput().getOutBoundIndexInput().getMoldRefluxPositionIndex()).getStatus().equals(UNOCCUPIED)){
//                    input.getInput().getPositions().get(input.getInput().getOutBoundIndexInput().getMoldRefluxPositionIndex()).setStatus(OCCUPIED);
//                    Product product = coldMoleProducts.getFirst();
//                    product.setRefluxFlag(true);
//                    input.getInput().getPositions().get(input.getInput().getOutBoundIndexInput().getMoldRefluxPositionIndex()).setProduct(product);
//                    coldMoleProducts.remove(0);
//                    coldMoleRefluxTime = 0.0;
//                }
//            }
//        }
//
//        if (!coreProducts.isEmpty()){
//            coreAndMoleRefluxTime = coreAndMoleRefluxTime +dtime;
//            if (coreAndMoleRefluxTime >= 12 * 60){
//                if (input.getInput().getPositions().get(input.getInput().getOutBoundIndexInput().getMoldRefluxPositionIndex()).getStatus().equals(UNOCCUPIED)){
//                    input.getInput().getPositions().get(input.getInput().getOutBoundIndexInput().getMoldRefluxPositionIndex()).setStatus(OCCUPIED);
//                    Product product = coreProducts.getFirst();
//                    product.setRefluxFlag(true);
//                    product.setProductType(SAND_MOULD);
//                    input.getInput().getPositions().get(input.getInput().getOutBoundIndexInput().getMoldRefluxPositionIndex()).setProduct(product);
//                    coreProducts.remove(0);
//                    coreAndMoleRefluxTime = 0.0;
//                }
//            }
//        }
//
//        if (!hotSandProducts.isEmpty()){
//            hotSandRefluxTime = hotSandRefluxTime +dtime;
//            if (hotSandRefluxTime >= 30 * 60){
//                if (input.getInput().getPositions().get(input.getInput().getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).getStatus().equals(UNOCCUPIED)){
//                    input.getInput().getPositions().get(input.getInput().getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).setStatus(OCCUPIED);
//                    Product product = hotSandProducts.getFirst();
//                    product.setRefluxFlag(true);
//                    input.getInput().getPositions().get(input.getInput().getOutBoundIndexInput().getSandBoxRefluxPositionIndex()).setProduct(product);
//                    hotSandProducts.remove(0);
//                    hotSandRefluxTime = 0.0;
//                }
//            }
//        }
//
//        if (!coldSandProducts.isEmpty()){
//            coldSandRefluxTime = coldSandRefluxTime +dtime;
//            if (coldSandRefluxTime >= 35 * 60){
//                if (input.getInput().getPositions().get(input.getInput().getOutBoundIndexInput().getColdRefluxPositionIndex()).getStatus().equals(UNOCCUPIED)){
//                    input.getInput().getPositions().get(input.getInput().getOutBoundIndexInput().getColdRefluxPositionIndex()).setStatus(OCCUPIED);
//                    Product product = coldSandProducts.getFirst();
//                    product.setRefluxFlag(true);
//                    input.getInput().getPositions().get(input.getInput().getOutBoundIndexInput().getColdRefluxPositionIndex()).setProduct(product);
//                    coldSandProducts.remove(0);
//                    coldSandRefluxTime = 0.0;
//                }
//            }
//        }

        refluxCount = hotCount + coldCount + coreCount;

        OutBoundGanttChartLinkInput outboundGanttChartLinkInput = new OutBoundGanttChartLinkInput();
        outboundGanttChartLinkInput = input;
        MoldClosingGanttChartLinkInput moldClosingGanttChartLinkInput = new MoldClosingGanttChartLinkInput();
        moldClosingGanttChartLinkInput = moldInput;

        OutBoundANDMoldClosing outBoundANDMoldClosing = new OutBoundANDMoldClosing(outboundGanttChartLinkInput, moldClosingGanttChartLinkInput, finishedCount);

        return new OtherSimulationLink(outBoundANDMoldClosing, SerializationUtils.clone(mouldingInput), SerializationUtils.clone(closingBoxInput));
    }

    public static void createHotMouldEvent(OutBoundGanttChartLinkInput outBoundInput, MouldingGanttChartLinkInput mouldingInput,
                                           MoldClosingGanttChartLinkInput moldInput, AnalogInput input){
        Coordinate hotMouldSubCarCoordinate = input.getHotMouldSubCar().getLocationCoordinate().coordinateDeepCopy();
        Coordinate hotUpSubCarCoordinate = input.getHotUpSpray().getLocationCoordinate().coordinateDeepCopy();
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>();
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        startSubCarStatus.add(SubCarStatusEnum.EMPTY_LOAD);
        startSubCarStatus.add(SubCarStatusEnum.TOP_ROD_FALL_DOWN);
        startSubCarStatus.add(SubCarStatusEnum.SUB_CAR_NOT_IN_FERRY);
        endSubCarStatus.add(SubCarStatusEnum.FULL_LOAD);
        endSubCarStatus.add(SubCarStatusEnum.TOP_ROD_RAISED);
        endSubCarStatus.add(SubCarStatusEnum.SUB_CAR_NOT_IN_FERRY);
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        startPositionStatus.add(OCCUPIED);
        endPositionStatus.add(UNOCCUPIED);
        List<EquipmentOperationStatusEnum> startEquipmentStatus = new ArrayList<>();
        startEquipmentStatus.add(EquipmentOperationStatusEnum.WORKING);
        List<EquipmentOperationStatusEnum> endEquipmentStatus = new ArrayList<>();
        endEquipmentStatus.add(EquipmentOperationStatusEnum.FINISHED);
        Event event = allTools.createEvent("001001", "moke", 0.0, 0.0, 0.0,
                input.getHotMouldSubCar().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, moldInput.getInput().getHotMoldClosingLayPosition().getCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                moldInput.getInput().getHotMoldClosingLayPosition().getId(), startPositionStatus, startPositionStatus, null, null,
                null, null, null, null, input.getHotMouldSubCar().getId(),
                SUB_CAR, RUNNING, moldInput.getInput().getHotMoldClosingLayPosition().getCoordinate(), null);

        //事件加入甘特图
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getHotMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("001002", "moke", 0.0, 0.0, 0.0,
                input.getHotMouldSubCar().getId(), startSubCarStatus, true, moldInput.getInput().getHotMoldClosingLayPosition().getCoordinate(),
                endSubCarStatus, true, moldInput.getInput().getHotMoldClosingLayPosition().getCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                moldInput.getInput().getHotMoldClosingLayPosition().getId(), startPositionStatus, endPositionStatus, null, null,
                null, null, null, null, input.getHotMouldSubCar().getId(),
                SUB_CAR, InstructionsEnum.RISING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getHotMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("001003", "moke", 0.0, 0.0, 0.0,
                input.getHotMouldSubCar().getId(), endSubCarStatus, false, null,
                endSubCarStatus, true, input.getHotSandShooting().getEquipmentCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, input.getHotMouldSubCar().getId(),
                SUB_CAR, RUNNING, input.getHotSandShooting().getEquipmentCoordinate(), null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getHotMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("001004", "moke", 0.0, 0.0, 0.0,
                input.getHotMouldSubCar().getId(), endSubCarStatus, false, null,
                startSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, input.getHotSandShooting().getId(), null, null,
                null, null, null, null, null,
                null, null, null, null, input.getHotMouldSubCar().getId(),
                SUB_CAR, InstructionsEnum.LANDING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getHotMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("001005", "moke", 0.0, 0.0, 0.0,
                null, null, false, null,
                null, false, null,
                null, null, false, null, null,
                false, null, input.getHotSandShooting().getId(), startEquipmentStatus, endEquipmentStatus,
                null, null, null, null, null,
                null, null, null, null, input.getHotSandShooting().getId(),
                ExecuteTypeEnum.EQUIPMENT, InstructionsEnum.NOTICE, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getHotMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("001006", "moke", 0.0, 0.0, 0.0,
                input.getHotMouldSubCar().getId(), startSubCarStatus, false, null,
                endSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, input.getHotSandShooting().getId(), endEquipmentStatus, null,
                null, null, null, null, null,
                null, null, null, null, input.getHotMouldSubCar().getId(),
                SUB_CAR, InstructionsEnum.RISING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getHotMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("001007", "moke", 0.0, 0.0, 0.0,
                input.getHotMouldSubCar().getId(), endSubCarStatus, false, null,
                endSubCarStatus, true, input.getHotOpenMold().getEquipmentCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, input.getHotMouldSubCar().getId(),
                SUB_CAR, RUNNING, input.getHotOpenMold().getEquipmentCoordinate(), null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getHotMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("001008", "moke", 0.0, 0.0, 0.0,
                input.getHotMouldSubCar().getId(), endSubCarStatus, false, null,
                startSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, input.getHotOpenMold().getId(), null, null,
                null, null, null, null, null,
                null, null, null, null, input.getHotMouldSubCar().getId(),
                SUB_CAR, InstructionsEnum.LANDING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getHotMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("001009", "moke", 0.0, 0.0, 0.0,
                null, null, false, null,
                null, false, null,
                null, null, false, null, null,
                false, null, input.getHotOpenMold().getId(), startEquipmentStatus, endEquipmentStatus,
                null, null, null, null, null,
                null, null, null, null, input.getHotOpenMold().getId(),
                ExecuteTypeEnum.EQUIPMENT, InstructionsEnum.NOTICE, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getHotMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("001010", "moke", 0.0, 0.0, 0.0,
                input.getHotMouldSubCar().getId(), startSubCarStatus, false, null,
                endSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, input.getHotOpenMold().getId(), null, null,
                null, null, null, null, null,
                null, null, null, null, input.getHotMouldSubCar().getId(),
                SUB_CAR, InstructionsEnum.RISING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getHotMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("001011", "moke", 0.0, 0.0, 0.0,
                input.getHotMouldSubCar().getId(), endSubCarStatus, false, null,
                endSubCarStatus, true, mouldingInput.getInput().getHotSandBoxOpeningPosition_2().getCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                mouldingInput.getInput().getHotSandBoxOpeningPosition_2().getId(), endPositionStatus, endPositionStatus,
                null, null, null, null, null,
                null, input.getHotMouldSubCar().getId(), SUB_CAR, RUNNING,
                mouldingInput.getInput().getHotSandBoxOpeningPosition_2().getCoordinate(), null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getHotMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("001012", "moke", 0.0, 0.0, 0.0,
                input.getHotMouldSubCar().getId(), endSubCarStatus, false, null,
                startSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, null, null, null,
                mouldingInput.getInput().getHotSandBoxOpeningPosition_2().getId(), endPositionStatus, startPositionStatus, null, null,
                null, null, null, null, input.getHotMouldSubCar().getId(),
                SUB_CAR, InstructionsEnum.LANDING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getHotMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("001013", "moke", 0.0, 0.0, 0.0,
                input.getHotMouldSubCar().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, input.getHotOpenMold().getEquipmentCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, input.getHotMouldSubCar().getId(),
                SUB_CAR, RUNNING, input.getHotOpenMold().getEquipmentCoordinate(), null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getHotMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("001014", "moke", 0.0, 0.0, 0.0,
                input.getHotMouldSubCar().getId(), startSubCarStatus, false, null,
                endSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, input.getHotOpenMold().getId(), null, null,
                null, null, null, null, null,
                null, null, null, null, input.getHotMouldSubCar().getId(),
                SUB_CAR, InstructionsEnum.RISING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getHotMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("001015", "moke", 0.0, 0.0, 0.0,
                input.getHotMouldSubCar().getId(), endSubCarStatus, false, null,
                endSubCarStatus, true, mouldingInput.getInput().getHotMoldOpeningPosition().getCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                mouldingInput.getInput().getHotMoldOpeningPosition().getId(), endPositionStatus, endPositionStatus,
                null, null, null, null, null,
                null, input.getHotMouldSubCar().getId(), SUB_CAR, RUNNING,
                mouldingInput.getInput().getHotMoldOpeningPosition().getCoordinate(), null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getHotMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("001016", "moke", 0.0, 0.0, 0.0,
                input.getHotMouldSubCar().getId(), endSubCarStatus, false, null,
                startSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, null, null, null,
                mouldingInput.getInput().getHotMoldOpeningPosition().getId(), endPositionStatus, startPositionStatus, null, null,
                null, null, null, null, input.getHotMouldSubCar().getId(),
                SUB_CAR, InstructionsEnum.LANDING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getHotMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("001017", "moke", 0.0, 0.0, 0.0,
                input.getHotMouldSubCar().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, hotMouldSubCarCoordinate,
                null, null, false, null, null,
                false, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, input.getHotMouldSubCar().getId(),
                SUB_CAR, RUNNING, hotMouldSubCarCoordinate, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getHotMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("001018", "moke", 0.0, 0.0, 0.0,
                input.getHotUpSpray().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, mouldingInput.getInput().getHotSandBoxOpeningPosition_2().getCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                mouldingInput.getInput().getHotSandBoxOpeningPosition_2().getId(), startPositionStatus, null, null, null,
                null, null, null, null, input.getHotUpSpray().getId(),
                SUB_CAR, RUNNING, mouldingInput.getInput().getHotSandBoxOpeningPosition_2().getCoordinate(), null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getHotUpMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("001019", "moke", 0.0, 0.0, 0.0,
                input.getHotUpSpray().getId(), startSubCarStatus, false, null,
                endSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, null, null, null,
                mouldingInput.getInput().getHotSandBoxOpeningPosition_2().getId(), startPositionStatus, endPositionStatus, null, null,
                null, null, null, null, input.getHotUpSpray().getId(),
                SUB_CAR, InstructionsEnum.RISING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getHotUpMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("001020", "moke", 0.0, 0.0, 0.0,
                input.getHotUpSpray().getId(), endSubCarStatus, false, null,
                endSubCarStatus, true, mouldingInput.getInput().getHotSpray_1().getEquipmentCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, input.getHotUpSpray().getId(),
                SUB_CAR, RUNNING, mouldingInput.getInput().getHotSpray_1().getEquipmentCoordinate(), null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getHotUpMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002021", "moke", 0.0, 0.0, 0.0,
                input.getHotUpSpray().getId(), endSubCarStatus, false, null,
                startSubCarStatus, true, mouldingInput.getInput().getHotSpray_1().getEquipmentCoordinate(),
                null, null, false, null, null,
                false, null, mouldingInput.getInput().getHotSpray_1().getId(), null, null,
                null, null, null, null, null,
                null, null, null, null, input.getHotUpSpray().getId(),
                SUB_CAR, LANDING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getHotUpMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("001021", "moke", 0.0, 0.0, 0.0,
                null, null, false, null,
                null, false, null,
                null, null, false, null, null,
                false, null, mouldingInput.getInput().getHotSpray_1().getId(), startEquipmentStatus, null,
                null, null, null, null, null,
                null, null, null, null, mouldingInput.getInput().getHotSpray_1().getId(),
                ExecuteTypeEnum.EQUIPMENT, InstructionsEnum.NOTICE, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getHotUpMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("001022", "moke", 0.0, 0.0, 0.0,
                input.getHotUpSpray().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, hotUpSubCarCoordinate,
                null, null, false, null, null,
                false, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, input.getHotUpSpray().getId(),
                SUB_CAR, RUNNING, hotUpSubCarCoordinate, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getHotUpMokeInputInGanttIndex()).getEvents().add(event);
    }

    public static void createCoreMouldEvent(OutBoundGanttChartLinkInput outBoundInput, MouldingGanttChartLinkInput mouldingInput,
                                            MoldClosingGanttChartLinkInput moldInput, AnalogInput input){
        Coordinate coreMouldSubCarCoordinate = input.getCoreMouldSubCar().getLocationCoordinate().coordinateDeepCopy();
        Coordinate coreLayCoordinate = new Coordinate(outBoundInput.getInput().getPositions().get(outBoundInput.getInput().getOutBoundIndexInput().getCoreMakingLayPositionIndex()).getCoordinate().getX(),
                input.getCoreSandShooting().getEquipmentCoordinate().getY());
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>();
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        startSubCarStatus.add(SubCarStatusEnum.EMPTY_LOAD);
        startSubCarStatus.add(SubCarStatusEnum.TOP_ROD_FALL_DOWN);
        startSubCarStatus.add(SubCarStatusEnum.SUB_CAR_NOT_IN_FERRY);
        endSubCarStatus.add(SubCarStatusEnum.FULL_LOAD);
        endSubCarStatus.add(SubCarStatusEnum.TOP_ROD_RAISED);
        endSubCarStatus.add(SubCarStatusEnum.SUB_CAR_NOT_IN_FERRY);
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        startPositionStatus.add(OCCUPIED);
        endPositionStatus.add(UNOCCUPIED);
        List<EquipmentOperationStatusEnum> startEquipmentStatus = new ArrayList<>();
        startEquipmentStatus.add(EquipmentOperationStatusEnum.WORKING);
        List<EquipmentOperationStatusEnum> endEquipmentStatus = new ArrayList<>();
        endEquipmentStatus.add(EquipmentOperationStatusEnum.FINISHED);
        Event event = allTools.createEvent("002001", "moke", 0.0, 0.0, 0.0,
                input.getCoreMouldSubCar().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, coreLayCoordinate,
                null, null, false, null, null,
                false, null, null, null, null,
                outBoundInput.getInput().getPositions().get(outBoundInput.getInput().getOutBoundIndexInput().getCoreMakingLayPositionIndex()).getId(),
                startPositionStatus, startPositionStatus, null, null,
                null, null, null, null, input.getCoreMouldSubCar().getId(),
                SUB_CAR, RUNNING, coreLayCoordinate, null);

        //事件加入甘特图
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getCoreMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002002", "moke", 0.0, 0.0, 0.0,
                input.getCoreMouldSubCar().getId(), startSubCarStatus, true, coreLayCoordinate,
                endSubCarStatus, true, coreLayCoordinate,
                null, null, false, null, null,
                false, null, null, null, null,
                outBoundInput.getInput().getPositions().get(outBoundInput.getInput().getOutBoundIndexInput().getCoreMakingLayPositionIndex()).getId(),
                startPositionStatus, endPositionStatus, null, null,
                null, null, null, null, input.getCoreMouldSubCar().getId(),
                SUB_CAR, InstructionsEnum.RISING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getCoreMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002003", "moke", 0.0, 0.0, 0.0,
                input.getCoreMouldSubCar().getId(), endSubCarStatus, false, null,
                endSubCarStatus, true, input.getCoreSandShooting().getEquipmentCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, input.getCoreMouldSubCar().getId(),
                SUB_CAR, RUNNING, input.getCoreSandShooting().getEquipmentCoordinate(), null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getCoreMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002004", "moke", 0.0, 0.0, 0.0,
                input.getCoreMouldSubCar().getId(), endSubCarStatus, false, null,
                startSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, input.getCoreSandShooting().getId(), null, null,
                null, null, null, null, null,
                null, null, null, null, input.getCoreMouldSubCar().getId(),
                SUB_CAR, InstructionsEnum.LANDING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getCoreMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002005", "moke", 0.0, 0.0, 0.0,
                null, null, false, null,
                null, false, null,
                null, null, false, null, null,
                false, null, input.getCoreSandShooting().getId(), startEquipmentStatus, endEquipmentStatus,
                null, null, null, null, null,
                null, null, null, null, input.getCoreSandShooting().getId(),
                ExecuteTypeEnum.EQUIPMENT, InstructionsEnum.NOTICE, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getCoreMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002006", "moke", 0.0, 0.0, 0.0,
                input.getCoreMouldSubCar().getId(), startSubCarStatus, false, null,
                endSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, input.getCoreSandShooting().getId(), endEquipmentStatus, null,
                null, null, null, null, null,
                null, null, null, null, input.getCoreMouldSubCar().getId(),
                SUB_CAR, InstructionsEnum.RISING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getCoreMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002007", "moke", 0.0, 0.0, 0.0,
                input.getCoreMouldSubCar().getId(), endSubCarStatus, false, null,
                endSubCarStatus, true, input.getCoreOpenMold().getEquipmentCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, input.getCoreMouldSubCar().getId(),
                SUB_CAR, RUNNING, input.getCoreOpenMold().getEquipmentCoordinate(), null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getCoreMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002008", "moke", 0.0, 0.0, 0.0,
                input.getCoreMouldSubCar().getId(), endSubCarStatus, false, null,
                startSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, input.getCoreOpenMold().getId(), null, null,
                null, null, null, null, null,
                null, null, null, null, input.getCoreMouldSubCar().getId(),
                SUB_CAR, InstructionsEnum.LANDING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getCoreMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002009", "moke", 0.0, 0.0, 0.0,
                null, null, false, null,
                null, false, null,
                null, null, false, null, null,
                false, null, input.getCoreOpenMold().getId(), startEquipmentStatus, endEquipmentStatus,
                null, null, null, null, null,
                null, null, null, null, input.getCoreOpenMold().getId(),
                ExecuteTypeEnum.EQUIPMENT, InstructionsEnum.NOTICE, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getCoreMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002010", "moke", 0.0, 0.0, 0.0,
                input.getCoreMouldSubCar().getId(), startSubCarStatus, false, null,
                endSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, input.getCoreOpenMold().getId(), null, null,
                null, null, null, null, null,
                null, null, null, null, input.getCoreMouldSubCar().getId(),
                SUB_CAR, InstructionsEnum.RISING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getCoreMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002011", "moke", 0.0, 0.0, 0.0,
                input.getCoreMouldSubCar().getId(), endSubCarStatus, false, null,
                endSubCarStatus, true, mouldingInput.getInput().getCoreMoldOpeningPosition().getCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                mouldingInput.getInput().getCoreMoldOpeningPosition().getId(), endPositionStatus, endPositionStatus,
                null, null, null, null, null,
                null, input.getCoreMouldSubCar().getId(), SUB_CAR, RUNNING ,
                mouldingInput.getInput().getCoreMoldOpeningPosition().getCoordinate(), null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getCoreMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002012", "moke", 0.0, 0.0, 0.0,
                input.getCoreMouldSubCar().getId(), endSubCarStatus, false, null,
                startSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, null, null, null,
                mouldingInput.getInput().getCoreMoldOpeningPosition().getId(), endPositionStatus, startPositionStatus, null, null,
                null, null, null, null, input.getCoreMouldSubCar().getId(),
                SUB_CAR, InstructionsEnum.LANDING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getCoreMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002013", "moke", 0.0, 0.0, 0.0,
                input.getCoreMouldSubCar().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, coreMouldSubCarCoordinate,
                null, null, false, null, null,
                false, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, input.getCoreMouldSubCar().getId(),
                SUB_CAR, RUNNING, coreMouldSubCarCoordinate, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getCoreMokeInputInGanttIndex()).getEvents().add(event);
    }

    public static void createColdMouldEvent(OutBoundGanttChartLinkInput outBoundInput, MouldingGanttChartLinkInput mouldingInput,
                                            MoldClosingGanttChartLinkInput moldInput, AnalogInput input){
        Coordinate coldMouldSubCarCoordinate = input.getColdMouldSubCar().getLocationCoordinate().coordinateDeepCopy();
        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>();
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        startSubCarStatus.add(SubCarStatusEnum.EMPTY_LOAD);
        startSubCarStatus.add(SubCarStatusEnum.TOP_ROD_FALL_DOWN);
        startSubCarStatus.add(SubCarStatusEnum.SUB_CAR_NOT_IN_FERRY);
        endSubCarStatus.add(SubCarStatusEnum.FULL_LOAD);
        endSubCarStatus.add(SubCarStatusEnum.TOP_ROD_RAISED);
        endSubCarStatus.add(SubCarStatusEnum.SUB_CAR_NOT_IN_FERRY);
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        startPositionStatus.add(OCCUPIED);
        endPositionStatus.add(UNOCCUPIED);
        List<EquipmentOperationStatusEnum> startEquipmentStatus = new ArrayList<>();
        startEquipmentStatus.add(EquipmentOperationStatusEnum.WORKING);
        List<EquipmentOperationStatusEnum> endEquipmentStatus = new ArrayList<>();
        endEquipmentStatus.add(EquipmentOperationStatusEnum.FINISHED);
        Event event = allTools.createEvent("003001", "moke", 0.0, 0.0, 0.0,
                input.getColdMouldSubCar().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, moldInput.getInput().getColdMoldClosingLayPosition().getCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                moldInput.getInput().getColdMoldClosingLayPosition().getId(), startPositionStatus, startPositionStatus, null, null,
                null, null, null, null, input.getColdMouldSubCar().getId(),
                SUB_CAR, RUNNING, moldInput.getInput().getColdMoldClosingLayPosition().getCoordinate(), null);

        //事件加入甘特图
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("003002", "moke", 0.0, 0.0, 0.0,
                input.getColdMouldSubCar().getId(), startSubCarStatus, true, moldInput.getInput().getColdMoldClosingLayPosition().getCoordinate(),
                endSubCarStatus, true, moldInput.getInput().getColdMoldClosingLayPosition().getCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                moldInput.getInput().getColdMoldClosingLayPosition().getId(), startPositionStatus, endPositionStatus, null, null,
                null, null, null, null, input.getColdMouldSubCar().getId(),
                SUB_CAR, InstructionsEnum.RISING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("003003", "moke", 0.0, 0.0, 0.0,
                input.getColdMouldSubCar().getId(), endSubCarStatus, false, null,
                endSubCarStatus, true, input.getColdSandShooting().getEquipmentCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdMouldSubCar().getId(),
                SUB_CAR, RUNNING, input.getColdSandShooting().getEquipmentCoordinate(), null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("003004", "moke", 0.0, 0.0, 0.0,
                input.getColdMouldSubCar().getId(), endSubCarStatus, false, null,
                startSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, input.getColdSandShooting().getId(), null, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdMouldSubCar().getId(),
                SUB_CAR, InstructionsEnum.LANDING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("003005", "moke", 0.0, 0.0, 0.0,
                null, null, false, null,
                null, false, null,
                null, null, false, null, null,
                false, null, input.getColdSandShooting().getId(), startEquipmentStatus, endEquipmentStatus,
                null, null, null, null, null,
                null, null, null, null, input.getColdSandShooting().getId(),
                ExecuteTypeEnum.EQUIPMENT, InstructionsEnum.NOTICE, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("003006", "moke", 0.0, 0.0, 0.0,
                input.getColdMouldSubCar().getId(), startSubCarStatus, false, null,
                endSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, input.getColdSandShooting().getId(), endEquipmentStatus, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdMouldSubCar().getId(),
                SUB_CAR, InstructionsEnum.RISING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("003007", "moke", 0.0, 0.0, 0.0,
                input.getColdMouldSubCar().getId(), endSubCarStatus, false, null,
                endSubCarStatus, true, input.getColdOpenMold().getEquipmentCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdMouldSubCar().getId(),
                SUB_CAR, RUNNING, input.getColdOpenMold().getEquipmentCoordinate(), null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("003008", "moke", 0.0, 0.0, 0.0,
                input.getColdMouldSubCar().getId(), endSubCarStatus, false, null,
                startSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, input.getColdOpenMold().getId(), null, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdMouldSubCar().getId(),
                SUB_CAR, InstructionsEnum.LANDING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("003009", "moke", 0.0, 0.0, 0.0,
                null, null, false, null,
                null, false, null,
                null, null, false, null, null,
                false, null, input.getColdOpenMold().getId(), startEquipmentStatus, endEquipmentStatus,
                null, null, null, null, null,
                null, null, null, null, input.getColdOpenMold().getId(),
                ExecuteTypeEnum.EQUIPMENT, InstructionsEnum.NOTICE, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("003010", "moke", 0.0, 0.0, 0.0,
                input.getColdMouldSubCar().getId(), startSubCarStatus, false, null,
                endSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, input.getColdOpenMold().getId(), null, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdMouldSubCar().getId(),
                SUB_CAR, InstructionsEnum.RISING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("003011", "moke", 0.0, 0.0, 0.0,
                input.getColdMouldSubCar().getId(), endSubCarStatus, false, null,
                endSubCarStatus, true, mouldingInput.getInput().getColdSandBoxOpeningPosition().getCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                mouldingInput.getInput().getColdSandBoxOpeningPosition().getId(), endPositionStatus, endPositionStatus,
                null, null, null, null, null,
                null, input.getColdMouldSubCar().getId(), SUB_CAR, RUNNING,
                mouldingInput.getInput().getColdSandBoxOpeningPosition().getCoordinate(), null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("003012", "moke", 0.0, 0.0, 0.0,
                input.getColdMouldSubCar().getId(), endSubCarStatus, false, null,
                startSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, null, null, null,
                mouldingInput.getInput().getColdSandBoxOpeningPosition().getId(), endPositionStatus, startPositionStatus, null, null,
                null, null, null, null, input.getColdMouldSubCar().getId(),
                SUB_CAR, InstructionsEnum.LANDING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("003013", "moke", 0.0, 0.0, 0.0,
                input.getColdMouldSubCar().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, input.getColdOpenMold().getEquipmentCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdMouldSubCar().getId(),
                SUB_CAR, RUNNING, input.getColdOpenMold().getEquipmentCoordinate(), null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("003014", "moke", 0.0, 0.0, 0.0,
                input.getColdMouldSubCar().getId(), startSubCarStatus, false, null,
                endSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, input.getColdOpenMold().getId(), null, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdMouldSubCar().getId(),
                SUB_CAR, InstructionsEnum.RISING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("003015", "moke", 0.0, 0.0, 0.0,
                input.getColdMouldSubCar().getId(), endSubCarStatus, false, null,
                endSubCarStatus, true, mouldingInput.getInput().getColdMoldOpeningPosition().getCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                mouldingInput.getInput().getColdMoldOpeningPosition().getId(), endPositionStatus, endPositionStatus,
                null, null, null, null, null,
                null, input.getColdMouldSubCar().getId(), SUB_CAR, RUNNING,
                mouldingInput.getInput().getColdMoldOpeningPosition().getCoordinate(), null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("003016", "moke", 0.0, 0.0, 0.0,
                input.getColdMouldSubCar().getId(), endSubCarStatus, false, null,
                startSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, null, null, null,
                mouldingInput.getInput().getColdMoldOpeningPosition().getId(), endPositionStatus, startPositionStatus, null, null,
                null, null, null, null, input.getColdMouldSubCar().getId(),
                SUB_CAR, InstructionsEnum.LANDING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("003017", "moke", 0.0, 0.0, 0.0,
                input.getColdMouldSubCar().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, coldMouldSubCarCoordinate,
                null, null, false, null, null,
                false, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdMouldSubCar().getId(),
                SUB_CAR, RUNNING, coldMouldSubCarCoordinate, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdMokeInputInGanttIndex()).getEvents().add(event);
    }
}
