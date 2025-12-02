package cn.hex.ddp.manufacture.infrastructure.algorithm.test;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentNameEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.AllocationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.OrderInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.OrderProduct;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.AnalogInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;

import java.util.ArrayList;
import java.util.List;

import static cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum.*;
import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum.*;
import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.UNOCCUPIED;
import static cn.hex.ddp.manufacture.domain.path.enums.PathNameEnum.*;
import static cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum.PATH_UNOCCUPIED;
import static cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationNameEnum.*;
import static cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum.FREE;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum.CARRY_SUB_CAR;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.OrderProductTypeEnum.*;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum.*;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum.*;

/**
 * @ClassDescription: 模拟数据测试
 * @Author: KangHong
 * @Created: 2024/6/7 23:38
 */
public class DataTest {

    /**
     * 初始化数据模拟仿真输入
     * @return 仿真输入
     */
    public SimulateInput createSimulateInput() {
        //初始化轨道
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(3410, 900));
        coordinates.add(new Coordinate(3410, 1100));
        Path outBoundPath = new Path(1796862786970255361L, "OutBoundPath", OUT_BOUND_PATH, new Coordinate(3410, 1404), new Coordinate(3410, 758), coordinates, PATH_UNOCCUPIED);
        Path sandBoxRefluxPath = new Path(1796862786169143298L, "SandBoxRefluxPath", SAND_BOX_REFLUX_ALL_PATH, new Coordinate(2044, 777), new Coordinate(3432,777), null, PATH_UNOCCUPIED);
        Path coldSandBoxRefluxPath = new Path(1796862786307555330L, "ColdSandBoxRefluxPath", COLD_SAND_BOX_REFLUX_PATH, new Coordinate(2900, 1384), new Coordinate(3432, 1384), null, PATH_UNOCCUPIED);
        Path coreMakingPath = new Path(1796862785179287554L, "CoreMakingPath", CORE_MAKING_PATH, new Coordinate(3432, 1012), new Coordinate(2422, 1012), null, PATH_UNOCCUPIED);
        Path moldRefluxPath = new Path(1796862785309310978L, "MoldRefluxPath", MOLD_REFLUX_PATH, new Coordinate(2360, 1133), new Coordinate(3432, 1133), null, PATH_UNOCCUPIED);
        Path hotMoldClosingPath = new Path(1796862784780828673L, "HotMoldClosingPath", HOT_SAND_MOLDING_PATH, new Coordinate(3431, 955), new Coordinate(2915, 955), null, PATH_UNOCCUPIED);
        Path coldMoldClosingPath = new Path(1796862784982155265L, "ColdMoldClosingPath", COLD_SAND_MOLDING_PATH, new Coordinate(3431, 1193), new Coordinate(2915, 1193), null, PATH_UNOCCUPIED);
        //7.16新添
        Path hotMoldAndMoldRefluxPath = new Path(100L, "HotMoldAndMoldRefluxPath", HOT_MOLD_AND_CORE_MOLD_REFLUX_PATH, new Coordinate(670, 701), new Coordinate(670, 677), null, PATH_UNOCCUPIED);
        Path coldMoldRefluxPath = new Path(101L, "ColdMoldRefluxPath", COLD_MOLD_REFLUX_PATH, new Coordinate(662, 677), new Coordinate(662, 666), null, PATH_UNOCCUPIED);
        Path hotSandBoxGoSprayPath = new Path(102L, "HotSandBoxGoSprayPath", HOT_SAND_BOX_GO_SPRAY_PATH, new Coordinate(659, 701), new Coordinate(659, 693), null, PATH_UNOCCUPIED);
        Path coldSandBoxGoSprayPath = new Path(103L, "ColdSandBoxGoSprayPath", COLD_SAND_BOX_GO_SPRAY_PATH, new Coordinate(657, 666), new Coordinate(657, 655), null, PATH_UNOCCUPIED);
        Path hotSprayUpPath = new Path(104L, "HotSprayUpPath", HOT_SPRAY_UP_PATH, new Coordinate(670, 701), new Coordinate(638, 701), null, PATH_UNOCCUPIED);
        Path hotSprayDownPath = new Path(105L, "HotSprayDownPath", HOT_SPRAY_DOWN_PATH, new Coordinate(670, 693), new Coordinate(638, 693), null, PATH_UNOCCUPIED);
        Path coldSprayUpPath = new Path(106L, "ColdSprayUpPath", COLD_SPRAY_UP_PATH, new Coordinate(670, 661), new Coordinate(638, 661), null, PATH_UNOCCUPIED);
        Path coldSprayDownPath = new Path(107L, "ColdSprayDownPath", COLD_SPRAY_DOWN_PATH, new Coordinate(670, 655), new Coordinate(638, 655), null, PATH_UNOCCUPIED);
        Path hotSprayGoClosingBoxPath = new Path(108L, "HotSprayGoClosingBoxPath", HOT_SPRAY_GO_CLOSING_BOX_PATH, new Coordinate(635, 710), new Coordinate(635, 693), null, PATH_UNOCCUPIED);
        Path coldSprayGoClosingBoxPath = new Path(109L, "ColdSprayGoClosingBoxPath", COLD_SPRAY_GO_CLOSING_BOX_PATH, new Coordinate(635, 661), new Coordinate(635, 665), null, PATH_UNOCCUPIED);
        List<Path> paths = new ArrayList<>();
        paths.add(outBoundPath);
        paths.add(sandBoxRefluxPath);
        paths.add(coldSandBoxRefluxPath);
        paths.add(coreMakingPath);
        paths.add(moldRefluxPath);
        paths.add(hotMoldClosingPath);
        paths.add(coldMoldClosingPath);
        paths.add(hotMoldAndMoldRefluxPath);
        paths.add(coldMoldRefluxPath);
        paths.add(hotSandBoxGoSprayPath);
        paths.add(coldSandBoxGoSprayPath);
        paths.add(hotSprayUpPath);
        paths.add(hotSprayDownPath);
        paths.add(coldSprayUpPath);
        paths.add(coldSprayDownPath);
        paths.add(hotSprayGoClosingBoxPath);
        paths.add(coldSprayGoClosingBoxPath);

        //初始化母车
        List<FerryStatusEnum> ferryStatus = new ArrayList<>();
        ferryStatus.add(CARRY_SUB_CAR);
        Ferry outBoundFerry = new Ferry(1L, "OutBoundFerry", OUT_BOUND_FERRY, 0.25,
                0.167, ferryStatus, outBoundPath, null, new Coordinate(3410,1000));
        Ferry hotMoldClosingFerry = new Ferry(2L, "HotMoldClosingFerry", HOT_MOLD_CLOSING_FERRY, 0.25,
                0.167, ferryStatus, hotMoldClosingPath, null, new Coordinate(3000,955));
        Ferry coldMoldClosingFerry = new Ferry(3L, "ColdMoldClosingFerry", COLD_FERRY_CLOSING_FERRY, 0.25,
                0.167, ferryStatus, coldMoldClosingPath, null, new Coordinate(3000,1193));
        //7.16新添
        Ferry hotSprayGoClosingBoxFerry = new Ferry(7L, "HotSprayGoClosingBoxFerry", HOT_SPRAY_GO_CLOSING_BOX_FERRY, 0.25,
                0.167, ferryStatus, hotSprayGoClosingBoxPath, null, new Coordinate(635,697));
        Ferry coldSprayGoClosingBoxFerry = new Ferry(8L, "ColdSprayGoClosingBoxFerry", COLD_SPRAY_GO_CLOSING_BOX_FERRY, 0.25,
                0.167, ferryStatus, coldSprayGoClosingBoxPath, null, new Coordinate(635,659));
        List<Ferry> ferries = new ArrayList<>();

        //初始化小车
        List<SubCarStatusEnum> subCarStatus = new ArrayList<>();
        subCarStatus.add(EMPTY_LOAD);
        subCarStatus.add(TOP_ROD_FALL_DOWN);
        subCarStatus.add(SUB_CAR_IN_FERRY);
        SubCar outBoundSubcar = new SubCar(4L, "OutBoundSubcar", OUT_BOUND_SUB_CAR, 0.33, 0.25,
                30, subCarStatus, outBoundPath, outBoundFerry.getId(), new Coordinate(3410,1000), null, null);
        SubCar hotMoldClosingSubcar = new SubCar(5L, "HotMoldClosingSubcar", HOT_MOLD_CLOSING_SUB_CAR, 0.33, 0.25,
                30, subCarStatus, hotMoldClosingPath, hotMoldClosingFerry.getId(), new Coordinate(3000,955), null, null);
        SubCar coldMoldClosingSubcar = new SubCar(6L, "ColdMoldClosingSubcar", COLD_MOLD_CLOSING_SUB_CAR, 0.33, 0.25,
                30, subCarStatus, coldMoldClosingPath, coldMoldClosingFerry.getId(), new Coordinate(3000,1193), null, null);
        //7.16新添
        SubCar hotSprayGoClosingBoxSubcar = new SubCar(9L, "HotSprayGoClosingBoxSubcar", HOT_SPRAY_GO_CLOSING_BOX_SUB_CAR, 0.33, 0.25,
                30, subCarStatus, hotSprayGoClosingBoxPath, hotSprayGoClosingBoxFerry.getId(),new Coordinate(635,697), null, null);
        SubCar coldSprayGoClosingBoxSubcar = new SubCar(10L, "ColdSprayGoClosingBoxSubcar", COLD_SPRAY_GO_CLOSING_BOX_SUB_CAR, 0.33, 0.25,
                30, subCarStatus, coldSprayGoClosingBoxPath, coldSprayGoClosingBoxFerry.getId(),new Coordinate(635,659), null, null);

        //更新摆渡车，形成双向绑定
        outBoundFerry.setBindSubCarId(outBoundSubcar.getId());
        hotMoldClosingFerry.setBindSubCarId(hotMoldClosingSubcar.getId());
        coldMoldClosingFerry.setBindSubCarId(coldMoldClosingSubcar.getId());
        hotSprayGoClosingBoxFerry.setBindSubCarId(hotSprayGoClosingBoxSubcar.getId());
        coldSprayGoClosingBoxFerry.setBindSubCarId(coldSprayGoClosingBoxSubcar.getId());
        ferries.add(outBoundFerry);
        ferries.add(hotMoldClosingFerry);
        ferries.add(coldMoldClosingFerry);
        ferries.add(hotSprayGoClosingBoxFerry);
        ferries.add(coldSprayGoClosingBoxFerry);

        List<SubCarStatusEnum> otherSubCarStatus = new ArrayList<>();
        otherSubCarStatus.add(EMPTY_LOAD);
        otherSubCarStatus.add(TOP_ROD_FALL_DOWN);
        otherSubCarStatus.add(SUB_CAR_NOT_IN_FERRY);
        SubCar sandBoxRefluxAllSubcar = new SubCar(1806221088667533313L, "SandBoxRefluxAllSubcar", SAND_BOX_REFLUX_ALL_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, sandBoxRefluxPath, null, new Coordinate(2500, 777), null, null);
        SubCar coreMakingSubcar = new SubCar(1806222141957931009L, "CoreMakingSubcar", CORE_MAKING_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, coreMakingPath, null, new Coordinate(3000, 1012), null, null);
        SubCar moldRefluxSubcar = new SubCar(1806222498566045698L, "MoldRefluxSubcar", MOLD_REFLUX_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, moldRefluxPath, null, new Coordinate(3000, 1133), null, null);
        SubCar coldSandBoxRefluxSubcar = new SubCar(1806222953400565761L, "ColdSandBoxRefluxSubcar", COLD_SAND_BOX_REFLUX_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, coldSandBoxRefluxPath, null, new Coordinate(3100, 1384), null, null);
        //7.16新添
        SubCar hotMoldAndCoreMoldRefluxSubCar = new SubCar(11L, "HotMoldAndCoreMoldRefluxSubCar", HOT_MOLD_AND_CORE_MOLD_REFLUX_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, hotMoldAndMoldRefluxPath, null, new Coordinate(670, 695), null, null);
        SubCar hotSandBoxGoSpraySubCar = new SubCar(12L, "ColdSandBoxGoSpraySubCar", HOT_SAND_BOX_GO_SPRAY_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, hotSandBoxGoSprayPath, null, new Coordinate(659, 697), null, null);
        SubCar coldSandBoxGoSpraySubCar = new SubCar(13L, "ColdSandBoxGoSpraySubCar", COLD_SAND_BOX_GO_SPRAY_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, coldSandBoxGoSprayPath, null, new Coordinate(657, 657), null, null);
        SubCar coldMoldRefluxSubCar = new SubCar(14L, "ColdMoldRefluxSubCar", COLD_MOLD_REFLUX_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, coldMoldRefluxPath, null, new Coordinate(662, 671), null, null);

        SubCar sandBoxMoldingSubCar = new SubCar(14L, "sandBoxMoldingSubCar", HOT_SAND_BOX_REFLUX_AND_MOLDING_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, coldMoldRefluxPath, null, new Coordinate(662, 671), null, null);

        List<SubCar> subCars = new ArrayList<>();
        subCars.add(outBoundSubcar);
        subCars.add(sandBoxRefluxAllSubcar);
        subCars.add(coreMakingSubcar);
        subCars.add(moldRefluxSubcar);
        subCars.add(coldSandBoxRefluxSubcar);
        subCars.add(hotMoldClosingSubcar);
        subCars.add(coldMoldClosingSubcar);
        subCars.add(hotSprayGoClosingBoxSubcar);
        subCars.add(coldSprayGoClosingBoxSubcar);
        subCars.add(hotMoldAndCoreMoldRefluxSubCar);
        subCars.add(hotSandBoxGoSpraySubCar);
        subCars.add(coldSandBoxGoSpraySubCar);
        subCars.add(coldMoldRefluxSubCar);

        subCars.add(sandBoxMoldingSubCar);

        Position hotSandMoldingPosition = new Position(1796864626461900801L, "HotSandMoldingPosition", HOT_SAND_MOLDING_POSITION, new Coordinate(3358, 902), UNOCCUPIED, null, null);
        Position coldSandMoldingPosition = new Position(1796864626499649538L, "ColdSandMoldingPosition", COLD_SAND_MOLDING_POSITION, new Coordinate(3358, 1247), UNOCCUPIED, null, null);
        Position sandBoxRefluxAllPosition = new Position(3L, "SandBoxRefluxAllPosition", SAND_BOX_REFLUX_ALL_POSITION, new Coordinate(747, 710), UNOCCUPIED, null, null);
        Position coreMakingPosition = new Position(1806222687347474434L, "CoreMakingPosition", CORE_MAKING_POSITION, new Coordinate(3361, 1013), UNOCCUPIED, null, null);
        Position coreMakingLayPosition = new Position(1806223074272018433L, "CoreMakingLayPosition", CORE_MAKING_LAY_POSITION, new Coordinate(2896, 1013), UNOCCUPIED, null, null);
        Position moldRefluxPosition = new Position(1806222939316092929L, "MoldRefluxPosition", MOLD_REFLUX_POSITION, new Coordinate(3343, 1133), UNOCCUPIED, null, null);
        Position coldRefluxPosition = new Position(7L, "ColdRefluxPosition", COLD_REFLUX_POSITION, new Coordinate(753, 655), UNOCCUPIED, null, null);
        Position hotSandUnboxingRefluxPosition = new Position(8L, "HotSandUnboxingRefluxPosition", HOT_SAND_UNBOXING_REFLUX_POSITION, new Coordinate(633, 710), UNOCCUPIED, null, null);
        Position hotSandandColdSandMixedUnboxingRefluxPosition = new Position(9L, "HotSandandColdSandMixedUnboxingRefluxPosition", HOT_SAND_AND_COLD_SAND_MIXED_UNBOXING_REFLUX_POSITION, new Coordinate(648, 710), UNOCCUPIED, null, null);
        Position hotSandVerticalTrackRefluxInteractionPosition = new Position(10L, "HotSandVerticalTrackRefluxInteractionPosition", HOT_SAND_VERTICAL_TRACK_REFLUX_INTERACTION_POSITION, new Coordinate(654, 710), UNOCCUPIED, null, null);
        Position sandBoxRowCarPosition = new Position(11L, "SandBoxRowCarPosition", SAND_BOX_ROW_CAR_POSITION, new Coordinate(714, 710), UNOCCUPIED, null, null);
        Position coldSandMoldRefluxRailInteractionPosition = new Position(1796864626961022979L, "ColdSandMoldRefluxRailInteractionPosition", SAND_MOLD_REFLUX_RAIL_INTERACTION_POSITION_2, new Coordinate(2380, 1132), UNOCCUPIED, null, null);
        Position hotSandMoldRefluxRailInteractionPosition = new Position(1796864626893914113L, "HotSandMoldRefluxRailInteractionPosition", SAND_MOLD_REFLUX_RAIL_INTERACTION_POSITION_1, new Coordinate(2443, 1132), UNOCCUPIED, null, null);
        Position coldSandRowCarPosition = new Position(14L, "ColdSandRowCarPosition", COLD_SAND_ROW_CAR_POSITION, new Coordinate(714, 655), UNOCCUPIED, null, null);
        Position hotMoldClosingLayPosition = new Position(1796864626629672962L, "HotSandShootingPosition", HOT_SAND_SHOOTING_POSITION, new Coordinate(2929, 865), UNOCCUPIED, null, null);
        Position coldMoldClosingLayPosition = new Position(1796864626696781826L, "ColdSandShootingPosition", COLD_SAND_SHOOTING_POSITION, new Coordinate(2929, 1248), UNOCCUPIED, null, null);
        //7.16新添
        Position hotMoldOpeningMoldLayPosition = new Position(101L, "HotMoldOpeningMoldLayPosition", HOT_MOLD_OPENING_MOLD_LAY_POSITION, new Coordinate(670, 701), UNOCCUPIED, null, null);
        Position hotMoldOpeningSandBoxLayPosition = new Position(102L, "HotMoldOpeningSandBoxLayPosition", HOT_MOLD_OPENING_SAND_BOX_LAY_POSITION_2, new Coordinate(659, 701), UNOCCUPIED, null, null);
        Position hotMoldOpeningSandBoxRefluxLayPosition = new Position(103L, "hotMoldOpeningSandBoxRefluxLayPosition", HOT_MOLD_OPENING_SAND_BOX_REFLUX_LAY_POSITION, new Coordinate(659, 710), UNOCCUPIED, null, null);
        Position coreMoldOpeningMoldLayPosition = new Position(104L, "CoreMoldOpeningMoldLayPosition", CORE_MOLD_OPENING_MOLD_LAY_POSITION, new Coordinate(670, 688), UNOCCUPIED, null, null);
        Position coldMoldOpeningMoldLayPosition = new Position(105L, "ColdMoldOpeningMoldLayPosition", COLD_MOLD_OPENING_MOLD_LAY_POSITION, new Coordinate(662, 666), UNOCCUPIED, null, null);
        Position coldMoldOpeningSandBoxLayPosition = new Position(106L, "ColdMoldOpeningSandBoxLayPosition", COLD_MOLD_OPENING_SAND_BOX_LAY_POSITION, new Coordinate(657, 666), UNOCCUPIED, null, null);
        Position hotSandBoxGoSprayDownPosition = new Position(107L, "HotSandBoxGoSprayDownPosition", HOT_SAND_BOX_GO_SPRAY_DOWN_POSITION, new Coordinate(659, 693), UNOCCUPIED, null, null);
        Position coldSandBoxGoSprayUpPosition = new Position(108L, "ColdSandBoxGoSprayUpPosition", COLD_SAND_BOX_GO_SPRAY_POSITION_1, new Coordinate(657, 661), UNOCCUPIED, null, null);
        Position coldSandBoxGoSprayDownPosition = new Position(109L, "ColdSandBoxGoSprayDownPosition", COLD_SAND_BOX_GO_SPRAY_POSITION_2, new Coordinate(657, 655), UNOCCUPIED, null, null);

        //新增点位
        Position position1  = new Position(109L, "SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_5", SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_5, new Coordinate(657, 655), UNOCCUPIED, null, null);
        Position position2  = new Position(109L, "SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9", SAND_BOX_REFLUX_BLOCKAGE_LAY_POSITION_9, new Coordinate(657, 655), UNOCCUPIED, null, null);
        Position position3  = new Position(109L, "HOT_SAND_MOLD_CLOSING_WORKSTATION_ACQUISITION_POSITION_5", HOT_SAND_MOLD_CLOSING_WORKSTATION_ACQUISITION_POSITION_5, new Coordinate(657, 655), UNOCCUPIED, null, null);

        List<Position> positions = new ArrayList<>();
        positions.add(hotSandMoldingPosition);
        positions.add(coldSandMoldingPosition);
        positions.add(sandBoxRefluxAllPosition);
        positions.add(coreMakingPosition);
        positions.add(coreMakingLayPosition);
        positions.add(moldRefluxPosition);
        positions.add(coldRefluxPosition);
        positions.add(hotSandUnboxingRefluxPosition);
        positions.add(hotSandandColdSandMixedUnboxingRefluxPosition);
        positions.add(hotSandVerticalTrackRefluxInteractionPosition);
        positions.add(sandBoxRowCarPosition);
        positions.add(coldSandMoldRefluxRailInteractionPosition);
        positions.add(hotSandMoldRefluxRailInteractionPosition);
        positions.add(coldSandRowCarPosition);
        positions.add(hotMoldClosingLayPosition);
        positions.add(coldMoldClosingLayPosition);
        positions.add(hotMoldOpeningMoldLayPosition);
        positions.add(hotMoldOpeningSandBoxLayPosition);
        positions.add(hotMoldOpeningSandBoxRefluxLayPosition);
        positions.add(coreMoldOpeningMoldLayPosition);
        positions.add(coldMoldOpeningMoldLayPosition);
        positions.add(coldMoldOpeningSandBoxLayPosition);
        positions.add(hotSandBoxGoSprayDownPosition);
        positions.add(coldSandBoxGoSprayUpPosition);
        positions.add(coldSandBoxGoSprayDownPosition);

        positions.add(position1);
        positions.add(position2);
        positions.add(position3);

        WorkStation hotMoldClosingWorkStation_1 = new WorkStation(1796862791412023297L, "HotMoldClosingWorkStation_1", HOT_MOLD_CLOSING_WORKSTATION_1, new Coordinate(3002, 902), FREE, null, null);
        WorkStation hotMoldClosingWorkStation_2 = new WorkStation(1796862791474937858L, "HotMoldClosingWorkStation_2", HOT_MOLD_CLOSING_WORKSTATION_2, new Coordinate(3097, 902), FREE, null, null);
        WorkStation hotMoldClosingWorkStation_3 = new WorkStation(1796862791537852419L, "HotMoldClosingWorkStation_3", HOT_MOLD_CLOSING_WORKSTATION_3, new Coordinate(3192, 902), FREE, null, null);
        WorkStation hotMoldClosingWorkStation_4 = new WorkStation(1796862791604961283L, "HotMoldClosingWorkStation_4", HOT_MOLD_CLOSING_WORKSTATION_4, new Coordinate(3286, 902), FREE, null, null);
        WorkStation coldMoldClosingWorkStation_1 = new WorkStation(1796862791672070147L, "ColdMoldClosingWorkStation_1", COLD_MOLD_CLOSING_WORKSTATION_1, new Coordinate(3002, 1246), FREE, null, null);
        WorkStation coldMoldClosingWorkStation_2 = new WorkStation(1796862791760150529L, "ColdMoldClosingWorkStation_2", COLD_MOLD_CLOSING_WORKSTATION_2, new Coordinate(3097, 1246), FREE, null, null);
        WorkStation coldMoldClosingWorkStation_3 = new WorkStation(1796862791802093571L, "ColdMoldClosingWorkStation_3", COLD_MOLD_CLOSING_WORKSTATION_3, new Coordinate(3192, 1246), FREE, null, null);
        WorkStation coldMoldClosingWorkStation_4 = new WorkStation(1796862791869202433L, "ColdMoldClosingWorkStation_4", COLD_MOLD_CLOSING_WORKSTATION_4, new Coordinate(3286, 1246), FREE, null, null);
        List<WorkStation> workStations = new ArrayList<>();
        workStations.add(hotMoldClosingWorkStation_1);
        workStations.add(hotMoldClosingWorkStation_2);
        workStations.add(hotMoldClosingWorkStation_3);
        workStations.add(hotMoldClosingWorkStation_4);
        workStations.add(coldMoldClosingWorkStation_1);
        workStations.add(coldMoldClosingWorkStation_2);
        workStations.add(coldMoldClosingWorkStation_3);
        workStations.add(coldMoldClosingWorkStation_4);

        Equipment hotSandSprayingUp_1 = new Equipment(101L, "HotSandSprayingUp_1", new Coordinate(641, 701), EquipmentOperationStatusEnum.STANDBY,
                null, 0.0, 0.0, EquipmentNameEnum.HOT_SAND_SPRAYING_1);
        Equipment hotSandSprayingUp_2 = new Equipment(102L, "HotSandSprayingUp_2", new Coordinate(648, 701), EquipmentOperationStatusEnum.STANDBY,
                null, 0.0, 0.0, EquipmentNameEnum.HOT_SAND_SPRAYING_2);
        Equipment hotSandSprayingDown_1 = new Equipment(103L, "HotSandSprayingDown_1", new Coordinate(641, 693), EquipmentOperationStatusEnum.STANDBY,
                null, 0.0, 0.0, EquipmentNameEnum.HOT_SAND_SPRAYING_3);
        Equipment coldSandSprayingUp_1 = new Equipment(105L, "ColdSandSprayingUp_1", new Coordinate(639, 661), EquipmentOperationStatusEnum.STANDBY,
                null, 0.0, 0.0, EquipmentNameEnum.COLD_SAND_SPRAYING_1);
        Equipment coldSandSprayingUp_2 = new Equipment(106L, "ColdSandSprayingUp_2", new Coordinate(645, 661), EquipmentOperationStatusEnum.STANDBY,
                null, 0.0, 0.0, EquipmentNameEnum.COLD_SAND_SPRAYING_2);
        Equipment coldSandSprayingDown_1 = new Equipment(107L, "ColdSandSprayingDown_1", new Coordinate(639, 655), EquipmentOperationStatusEnum.STANDBY,
                null, 0.0, 0.0, EquipmentNameEnum.COLD_SAND_SPRAYING_3);
        List<Equipment> equipments = new ArrayList<>();
        equipments.add(hotSandSprayingUp_1);
        equipments.add(hotSandSprayingUp_2);
        equipments.add(hotSandSprayingDown_1);
        equipments.add(coldSandSprayingUp_1);
        equipments.add(coldSandSprayingUp_2);
        equipments.add(coldSandSprayingDown_1);

        List<AllocationInput> allocationInputs = new ArrayList<>();
        AllocationInput allocationInput = new AllocationInput(subCars, ferries, equipments, positions, workStations, paths, AreaEnum.SOUTH_MOULDING);
        allocationInputs.add(allocationInput);

        //合箱区
        //7.24
        Path hotClosingBoxPath = new Path(110L, "HotClosingBoxPath", HOT_CLOSING_BOX_PATH, new Coordinate(630, 710), new Coordinate(569, 710), null, PATH_UNOCCUPIED);
        Path coldClosingBoxPath = new Path(111L, "ColdClosingBoxPath", COLD_CLOSING_BOX_PATH, new Coordinate(630, 655), new Coordinate(569, 655), null, PATH_UNOCCUPIED);
        List<Path> paths_1 = new ArrayList<>();
        paths_1.add(hotClosingBoxPath);
        paths_1.add(coldClosingBoxPath);

        //7.24新增，合箱区
        Ferry hotClosingBoxFerry = new Ferry(9L, "HotClosingBoxFerry", HOT_CLOSING_BOX_FERRY, 0.25,
                0.167, ferryStatus, hotClosingBoxPath, null, new Coordinate(618,710));
        Ferry coldClosingBoxFerry = new Ferry(10L, "ColdClosingBoxFerry", COLD_CLOSING_BOX_FERRY, 0.25,
                0.167, ferryStatus, coldClosingBoxPath, null, new Coordinate(618,655));
        List<Ferry> ferries_2 = new ArrayList<>();
        SubCar hotClosingBoxSubCar = new SubCar(13L, "HotClosingBoxSubCar", HOT_CLOSING_BOX_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, hotClosingBoxPath, null, new Coordinate(618,710), null, null);
        SubCar coldClosingBoxSubCar = new SubCar(14L, "ColdClosingBoxSubCar", COLD_CLOSING_BOX_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, coldClosingBoxPath, null, new Coordinate(618,655), null, null);
        List<SubCar> subCars_1 = new ArrayList<>();
        hotClosingBoxFerry.setBindSubCarId(hotClosingBoxSubCar.getId());
        coldClosingBoxFerry.setBindSubCarId(coldClosingBoxSubCar.getId());
        hotClosingBoxSubCar.setBindFerryId(hotClosingBoxFerry.getId());
        coldClosingBoxSubCar.setBindFerryId(coldClosingBoxFerry.getId());
        ferries_2.add(hotClosingBoxFerry);
        ferries_2.add(coldClosingBoxFerry);
        subCars_1.add(hotClosingBoxSubCar);
        subCars_1.add(coldClosingBoxSubCar);

        Position hotClosingBoxPosition = new Position(110L, "HotClosingBoxPosition", HOT_SAND_BOX_SPRAY_GO_CLOSING_BOX_POSITION, new Coordinate(630, 704), UNOCCUPIED, null, null);
        Position coldClosingBoxPosition = new Position(111L, "ColdClosingBoxPosition", COLD_SAND_BOX_SPRAY_GO_CLOSING_BOX_POSITION, new Coordinate(628, 657), UNOCCUPIED, null, null);
        Position hotClosingBoxGoPourPosition = new Position(112L, "HotClosingBoxGoPourPosition", HOT_SAND_BOX_CLOSING_BOX_GO_POUR_POSITION, new Coordinate(569, 704), UNOCCUPIED, null, null);
        Position coldClosingBoxGoPourPosition = new Position(113L, "ColdClosingBoxGoPourPosition", COLD_SAND_BOX_CLOSING_BOX_GO_POUR_POSITION, new Coordinate(569, 704), UNOCCUPIED, null, null);
        List<Position> positions_2 = new ArrayList<>();
        positions_2.add(hotClosingBoxPosition);
        positions_2.add(coldClosingBoxPosition);
        positions_2.add(hotClosingBoxGoPourPosition);
        positions_2.add(coldClosingBoxGoPourPosition);

        WorkStation hotClosingBoxWorkStation_1 = new WorkStation(1796862791412023297L, "HotMoldClosingWorkStation_1", HOT_CLOSING_BOX_WORKSTATION_1, new Coordinate(585, 703), FREE, null, null);
        WorkStation hotClosingBoxWorkStation_2 = new WorkStation(1796862791474937858L, "HotMoldClosingWorkStation_2", HOT_CLOSING_BOX_WORKSTATION_2, new Coordinate(595, 703), FREE, null, null);
        WorkStation hotClosingBoxWorkStation_3 = new WorkStation(1796862791537852419L, "HotMoldClosingWorkStation_3", HOT_CLOSING_BOX_WORKSTATION_3, new Coordinate(605, 703), FREE, null, null);
        WorkStation hotClosingBoxWorkStation_4 = new WorkStation(1796862791604961283L, "HotMoldClosingWorkStation_4", HOT_CLOSING_BOX_WORKSTATION_4, new Coordinate(615, 703), FREE, null, null);
        WorkStation coldClosingBoxWorkStation_1 = new WorkStation(1796862791672070149L, "ColdMoldClosingWorkStation_1", COLD_CLOSING_BOX_WORKSTATION_1, new Coordinate(583, 658), FREE, null, null);
        WorkStation coldClosingBoxWorkStation_2 = new WorkStation(1796762791760150529L, "ColdMoldClosingWorkStation_2", COLD_CLOSING_BOX_WORKSTATION_2, new Coordinate(593, 658), FREE, null, null);
        WorkStation coldClosingBoxWorkStation_3 = new WorkStation(1796862791802093571L, "ColdMoldClosingWorkStation_3", COLD_CLOSING_BOX_WORKSTATION_3, new Coordinate(603, 658), FREE, null, null);
        WorkStation coldClosingBoxWorkStation_4 = new WorkStation(1796862791869202433L, "ColdMoldClosingWorkStation_4", COLD_CLOSING_BOX_WORKSTATION_4, new Coordinate(613, 658), FREE, null, null);
        List<WorkStation> workStations_1 = new ArrayList<>();
        workStations_1.add(hotClosingBoxWorkStation_1);
        workStations_1.add(hotClosingBoxWorkStation_2);
        workStations_1.add(hotClosingBoxWorkStation_3);
        workStations_1.add(hotClosingBoxWorkStation_4);
        workStations_1.add(coldClosingBoxWorkStation_1);
        workStations_1.add(coldClosingBoxWorkStation_2);
        workStations_1.add(coldClosingBoxWorkStation_3);
        workStations_1.add(coldClosingBoxWorkStation_4);

        AllocationInput allocationInput_2 = new AllocationInput(subCars_1, ferries_2, null, positions_2, workStations_1, paths_1, AreaEnum.SOUTH_COMBINE_BOX);
        allocationInputs.add(allocationInput_2);

        //待浇筑区
        //7.29
        Path closingGoPourPath = new Path(112L, "ClosingGoPourPath", CONSOLIDATION_POURING_UNBOXING_PATH, new Coordinate(561, 655), new Coordinate(561, 710), null, PATH_UNOCCUPIED);
        Path waitingPourDownPath_1 = new Path(113L, "WaitingPourDownPath_1", WAIT_POURING_PATH_1, new Coordinate(558, 659), new Coordinate(502, 659), null, PATH_UNOCCUPIED);
        Path waitingPourDownPath_2 = new Path(114L, "WaitingPourDownPath_2", WAIT_POURING_PATH_2, new Coordinate(558, 664), new Coordinate(502, 664), null, PATH_UNOCCUPIED);
        Path waitingPourDownPath_3 = new Path(115L, "WaitingPourDownPath_3", WAIT_POURING_PATH_3, new Coordinate(558, 669), new Coordinate(502, 669), null, PATH_UNOCCUPIED);
        Path waitingPourDownPath_4 = new Path(116L, "WaitingPourDownPath_4", WAIT_POURING_PATH_4, new Coordinate(558, 674), new Coordinate(502, 674), null, PATH_UNOCCUPIED);
        Path waitingPourDownPath_5 = new Path(117L, "WaitingPourDownPath_5", WAIT_POURING_PATH_5, new Coordinate(558, 679), new Coordinate(502, 679), null, PATH_UNOCCUPIED);
        Path waitingPourUpPath_1 = new Path(118L, "WaitingPourUpPath_1", WAIT_POURING_PATH_6, new Coordinate(558, 686), new Coordinate(502, 686), null, PATH_UNOCCUPIED);
        Path waitingPourUpPath_2 = new Path(119L, "WaitingPourUpPath_2", WAIT_POURING_PATH_7, new Coordinate(558, 691), new Coordinate(502, 691), null, PATH_UNOCCUPIED);
        Path waitingPourUpPath_3 = new Path(120L, "WaitingPourUpPath_3", WAIT_POURING_PATH_8, new Coordinate(558, 696), new Coordinate(502, 696), null, PATH_UNOCCUPIED);
        Path waitingPourUpPath_4 = new Path(121L, "WaitingPourUpPath_4", WAIT_POURING_PATH_9, new Coordinate(558, 701), new Coordinate(502, 701), null, PATH_UNOCCUPIED);
        Path waitingPourUpPath_5 = new Path(122L, "WaitingPourUpPath_5", WAIT_POURING_PATH_10, new Coordinate(558, 706), new Coordinate(502, 706), null, PATH_UNOCCUPIED);
        List<Path> paths_3 = new ArrayList<>();
        paths_3.add(closingGoPourPath);
        paths_3.add(waitingPourDownPath_1);
        paths_3.add(waitingPourDownPath_2);
        paths_3.add(waitingPourDownPath_3);
        paths_3.add(waitingPourDownPath_4);
        paths_3.add(waitingPourDownPath_5);
        paths_3.add(waitingPourUpPath_1);
        paths_3.add(waitingPourUpPath_2);
        paths_3.add(waitingPourUpPath_3);
        paths_3.add(waitingPourUpPath_4);
        paths_3.add(waitingPourUpPath_5);

        //7.29新增，待浇筑区
        Ferry closingGoPourFerry = new Ferry(15L, "ClosingGoPourFerry", CONSOLIDATION_POURING_FERRY, 0.25,
                0.167, ferryStatus, closingGoPourPath, null, new Coordinate(561,681));
        List<Ferry> ferries_3 = new ArrayList<>();
        SubCar closingGoPourSubCar = new SubCar(16L, "ClosingGoPourSubCar", CONSOLIDATION_POURING_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, closingGoPourPath, null, new Coordinate(561,681), null, null);
        List<SubCar> subCars_3 = new ArrayList<>();
        closingGoPourFerry.setBindSubCarId(closingGoPourSubCar.getId());
        closingGoPourSubCar.setBindFerryId(closingGoPourFerry.getId());
        ferries_3.add(closingGoPourFerry);
        subCars_3.add(closingGoPourSubCar);

        List<Position> positions_3 = new ArrayList<>();
        int positionCode = POURING_UP_FIRST_POSITION_1.getCode();
        int x, y = 655;
        Long positiongId = 114L;
        for (int i = 0; i < 5; i++) {
            x = 507;
            for (int j = 0; j < 10; j++) {
                Position position = new Position(positiongId, "Pouring_Position", PositionNameEnum.values()[positionCode], new Coordinate(x, y), UNOCCUPIED, null, null);
                positions_3.add(position);
                positiongId++;
                positionCode++;
                x = x + 5;
            }
            y = y + 5;
        }

        y = 686;
        for (int i = 0; i < 5; i++) {
            x = 507;
            for (int j = 0; j < 10; j++) {
                Position position = new Position(positiongId, "Pouring_Position", PositionNameEnum.values()[positionCode], new Coordinate(x, y), UNOCCUPIED, null, null);
                positions_3.add(position);
                positiongId++;
                positionCode++;
                x = x + 5;
            }
            y = y + 5;
        }

        List<WorkStation> workStations_3 = new ArrayList<>();

        AllocationInput allocationInput_3 = new AllocationInput(subCars_3, ferries_3, null, positions_3, workStations_3, paths_3, AreaEnum.SOUTH_WAITING_POURING);
        allocationInputs.add(allocationInput_3);

        //8.6 浇筑区
        Path pouringPath = new Path(123L, "PouringPath", POURING_PATH, new Coordinate(498, 655), new Coordinate(498, 775), null, PATH_UNOCCUPIED);
        List<Path> paths_4 = new ArrayList<>();
        paths_4.add(pouringPath);

        Ferry pouringFerry = new Ferry(17L, "PouringFerry", POURING_DOWN_FERRY, 0.25,
                0.167, ferryStatus, pouringPath, null, new Coordinate(561,655));
        List<Ferry> ferries_4 = new ArrayList<>();
        SubCar pouringSubCar = new SubCar(18L, "PouringSubCar", POURING_DOWN_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, pouringPath, null, new Coordinate(561,655), null, null);
        List<SubCar> subCars_4 = new ArrayList<>();
        pouringFerry.setBindSubCarId(pouringSubCar.getId());
        pouringSubCar.setBindFerryId(pouringFerry.getId());
        ferries_4.add(pouringFerry);
        subCars_4.add(pouringSubCar);

        WorkStation pouringWorkStation_1 = new WorkStation(100L, "PouringWorkStation_1", POURING_WORKSTATION_15T_1, new Coordinate(485, 686), FREE, null, null);
        WorkStation pouringWorkStation_2 = new WorkStation(101L, "PouringWorkStation_2", POURING_WORKSTATION_15T_2, new Coordinate(485, 694), FREE, null, null);
        WorkStation pouringWorkStation_3 = new WorkStation(102L, "PouringWorkStation_3", POURING_WORKSTATION_15T_3, new Coordinate(485, 702), FREE, null, null);
        WorkStation pouringWorkStation_4 = new WorkStation(103L, "PouringWorkStation_4", POURING_WORKSTATION_15T_4, new Coordinate(485, 710), FREE, null, null);
        WorkStation pouringWorkStation_5 = new WorkStation(104L, "PouringWorkStation_5", POURING_WORKSTATION_30T_1, new Coordinate(485, 718), FREE, null, null);
        WorkStation pouringWorkStation_6 = new WorkStation(105L, "PouringWorkStation_6", POURING_WORKSTATION_30T_2, new Coordinate(485, 726), FREE, null, null);
        WorkStation pouringWorkStation_7 = new WorkStation(106L, "PouringWorkStation_7", POURING_WORKSTATION_30T_3, new Coordinate(485, 734), FREE, null, null);
        WorkStation pouringWorkStation_8 = new WorkStation(107L, "PouringWorkStation_8", POURING_WORKSTATION_30T_4, new Coordinate(485, 742), FREE, null, null);
        WorkStation pouringWorkStation_9 = new WorkStation(108L, "PouringWorkStation_9", POURING_WORKSTATION_30T_5, new Coordinate(485, 750), FREE, null, null);
        WorkStation pouringWorkStation_10 = new WorkStation(109L, "PouringWorkStation_10", POURING_WORKSTATION_30T_6, new Coordinate(485, 758), FREE, null, null);
        WorkStation pouringWorkStation_11 = new WorkStation(110L, "PouringWorkStation_11", POURING_WORKSTATION_30T_7, new Coordinate(485, 766), FREE, null, null);
        WorkStation pouringWorkStation_12 = new WorkStation(111L, "PouringWorkStation_12", POURING_WORKSTATION_30T_8, new Coordinate(485, 774), FREE, null, null);
        List<WorkStation> workStations_4 = new ArrayList<>();
        workStations_4.add(pouringWorkStation_1);
        workStations_4.add(pouringWorkStation_2);
        workStations_4.add(pouringWorkStation_3);
        workStations_4.add(pouringWorkStation_4);
        workStations_4.add(pouringWorkStation_5);
        workStations_4.add(pouringWorkStation_6);
        workStations_4.add(pouringWorkStation_7);
        workStations_4.add(pouringWorkStation_8);
        workStations_4.add(pouringWorkStation_9);
        workStations_4.add(pouringWorkStation_10);
        workStations_4.add(pouringWorkStation_11);
        workStations_4.add(pouringWorkStation_12);

        AllocationInput allocationInput_4 = new AllocationInput(subCars_4, ferries_4, null, new ArrayList<>(), workStations_4, paths_4, AreaEnum.SOUTH_POURING);
        allocationInputs.add(allocationInput_4);

        //8.6 冷却区
        Path coolingDownPath_1 = new Path(124L, "CoolingDownPath_1", COOLING_PATH_1, new Coordinate(558, 717), new Coordinate(502, 717), null, PATH_UNOCCUPIED);
        Path coolingDownPath_2 = new Path(125L, "CoolingDownPath_2", COOLING_PATH_2, new Coordinate(558, 722), new Coordinate(502, 722), null, PATH_UNOCCUPIED);
        Path coolingDownPath_3 = new Path(126L, "CoolingDownPath_3", COOLING_PATH_3, new Coordinate(558, 727), new Coordinate(502, 727), null, PATH_UNOCCUPIED);
        Path coolingDownPath_4 = new Path(127L, "CoolingDownPath_4", COOLING_PATH_4, new Coordinate(558, 732), new Coordinate(502, 732), null, PATH_UNOCCUPIED);
        Path coolingDownPath_5 = new Path(128L, "CoolingDownPath_5", COOLING_PATH_5, new Coordinate(558, 735), new Coordinate(502, 735), null, PATH_UNOCCUPIED);
        Path coolingUpPath_1 = new Path(129L, "CoolingUpPath_1", COOLING_PATH_6, new Coordinate(558, 746), new Coordinate(502, 746), null, PATH_UNOCCUPIED);
        Path coolingUpPath_2 = new Path(130L, "CoolingUpPath_2", COOLING_PATH_7, new Coordinate(558, 751), new Coordinate(502, 751), null, PATH_UNOCCUPIED);
        Path coolingUpPath_3 = new Path(131L, "CoolingUpPath_3", COOLING_PATH_8, new Coordinate(558, 756), new Coordinate(502, 756), null, PATH_UNOCCUPIED);
        Path coolingUpPath_4 = new Path(132L, "CoolingUpPath_4", COOLING_PATH_9, new Coordinate(558, 761), new Coordinate(502, 761), null, PATH_UNOCCUPIED);
        Path coolingUpPath_5 = new Path(133L, "CoolingUpPath_5", COOLING_PATH_10, new Coordinate(558, 766), new Coordinate(502, 766), null, PATH_UNOCCUPIED);
        List<Path> paths_5 = new ArrayList<>();
        paths_5.add(coolingDownPath_1);
        paths_5.add(coolingDownPath_2);
        paths_5.add(coolingDownPath_3);
        paths_5.add(coolingDownPath_4);
        paths_5.add(coolingDownPath_5);
        paths_5.add(coolingUpPath_1);
        paths_5.add(coolingUpPath_2);
        paths_5.add(coolingUpPath_3);
        paths_5.add(coolingUpPath_4);
        paths_5.add(coolingUpPath_5);

        //8.6新增，待浇筑区
        Ferry coolingFerry = new Ferry(19L, "CoolingFerry", COOLING_UNBOXING_FERRY, 0.25,
                0.167, ferryStatus, closingGoPourPath, null, new Coordinate(561,727));
        List<Ferry> ferries_5 = new ArrayList<>();
        SubCar coolingSubCar = new SubCar(20L, "CoolingSubCar", COOLING_UNBOXING_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, closingGoPourPath, null, new Coordinate(561,681), null, null);
        List<SubCar> subCars_5 = new ArrayList<>();
        coolingFerry.setBindSubCarId(coolingSubCar.getId());
        coolingSubCar.setBindFerryId(coolingFerry.getId());
        ferries_5.add(coolingFerry);
        subCars_5.add(coolingSubCar);

        List<Position> positions_5 = new ArrayList<>();
        int positionCode_1 = COOLING_UP_FIRST_POSITION_1.getCode();
        int x_1, y_1 = 720;
        Long positiongId_1 = 215L;
        for (int i = 0; i < 5; i++) {
            x_1 = 507;
            for (int j = 0; j < 10; j++) {
                Position position = new Position(positiongId_1, "Cooling_Position", PositionNameEnum.values()[positionCode_1], new Coordinate(x_1, y_1), UNOCCUPIED, null, null);
                positions_5.add(position);
                positiongId_1++;
                positionCode_1++;
                x_1 = x_1 + 5;
            }
            y_1 = y_1 + 5;
        }

        y_1 = 753;
        for (int i = 0; i < 5; i++) {
            x_1 = 507;
            for (int j = 0; j < 10; j++) {
                Position position = new Position(positiongId_1, "Cooling_Position", PositionNameEnum.values()[positionCode_1], new Coordinate(x_1, y_1), UNOCCUPIED, null, null);
                positions_5.add(position);
                positiongId_1++;
                positionCode_1++;
                x_1 = x_1 + 5;
            }
            y_1 = y_1 + 5;
        }

        List<WorkStation> workStations_5 = new ArrayList<>();

        AllocationInput allocationInput_5 = new AllocationInput(subCars_5, ferries_5, null, positions_5, workStations_5, paths_5, AreaEnum.SOUTH_COOLING);
        allocationInputs.add(allocationInput_5);

        //8.6开箱区
        Path coldAndHotOpenBoxPath = new Path(134L, "ColdAndHotOpenBoxPath", COLD_AND_HOT_MIXED_UNBOXING_PATH, new Coordinate(564, 776), new Coordinate(648, 776), null, PATH_UNOCCUPIED);
        Path hotOpenBoxPath = new Path(135L, "HotOpenBoxPath", HOT_SAND_UNBOXING_PATH, new Coordinate(564, 718), new Coordinate(641, 718), null, PATH_UNOCCUPIED);
        Path castingUpPath = new Path(136L, "CastingUpPath", CASTING_REFLUX_UP_PATH, new Coordinate(564, 753), new Coordinate(641, 753), null, PATH_UNOCCUPIED);
        Path castingDownPath = new Path(137L, "CastingDownPath", CASTING_REFLUX_DOWN_PATH, new Coordinate(564, 742), new Coordinate(641, 742), null, PATH_UNOCCUPIED);
        Path castingVerticalPath = new Path(138L, "CastingVerticalPath", CASTING_REFLUX_VERTICAL_PATH, new Coordinate(641, 753), new Coordinate(641, 742), null, PATH_UNOCCUPIED);
        Path sandBoxRefluxLeftPath = new Path(139L, "SandBoxRefluxLeftPath", SAND_BOX_REFLUX_LEFT_PATH, new Coordinate(641, 719), new Coordinate(641, 710), null, PATH_UNOCCUPIED);
        Path sandBoxRefluxRightPath = new Path(140L, "SandBoxRefluxRightPath", SAND_BOX_REFLUX_RIGHT_PATH, new Coordinate(648, 776), new Coordinate(648, 710), null, PATH_UNOCCUPIED);

        List<Path> paths_6 = new ArrayList<>();
        paths_6.add(coldAndHotOpenBoxPath);
        paths_6.add(hotOpenBoxPath);
        paths_6.add(castingUpPath);
        paths_6.add(castingDownPath);
        paths_6.add(castingVerticalPath);
        paths_6.add(sandBoxRefluxLeftPath);
        paths_6.add(sandBoxRefluxRightPath);

        Ferry coldAndHotFerry = new Ferry(21L, "ColdAndHotFerry", COLD_AND_HOT_MIXED_UNBOXING_FERRY, 0.25,
                0.167, ferryStatus, coldAndHotOpenBoxPath, null, new Coordinate(581,776));
        Ferry hotFerry = new Ferry(22L, "HotFerry", HOT_SAND_UNBOXING_FERRY, 0.25,
                0.167, ferryStatus, hotOpenBoxPath, null, new Coordinate(581,718));
        List<Ferry> ferries_6 = new ArrayList<>();
        SubCar coldAndHotSubCar = new SubCar(23L, "ColdAndHotSubCar", COLD_AND_HOT_MIXED_UNBOXING_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, coldAndHotOpenBoxPath, null, new Coordinate(581,776), null, null);
        SubCar hotSubCar = new SubCar(24L, "HotSubCar", HOT_SAND_UNBOXING_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, hotOpenBoxPath, null, new Coordinate(581,718), null, null);
        List<SubCar> subCars_6 = new ArrayList<>();
        coldAndHotFerry.setBindSubCarId(coldAndHotSubCar.getId());
        hotFerry.setBindSubCarId(hotSubCar.getId());
        coldAndHotSubCar.setBindFerryId(coldAndHotFerry.getId());
        hotSubCar.setBindFerryId(hotFerry.getId());
        ferries_6.add(coldAndHotFerry);
        ferries_6.add(hotFerry);
        subCars_6.add(coldAndHotSubCar);
        subCars_6.add(hotSubCar);

        SubCar castingUpSubCar = new SubCar(25L, "CastingUpSubCar", CASTING_REFLUX_UP_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, castingUpPath, null, new Coordinate(581,753), null, null);
        SubCar castingDownSubCar = new SubCar(26L, "CastingDownSubCar", CASTING_REFLUX_DOWN_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, castingDownPath, null, new Coordinate(581,742), null, null);
        SubCar castingVerticalSubCar = new SubCar(27L, "CastingVerticalSubCar", CASTING_REFLUX_VERTICAL_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, castingVerticalPath, null, new Coordinate(641,746), null, null);
        SubCar sandBoxRefluxRightSubCar = new SubCar(28L, "SandBoxRefluxRightSubCar", SAND_BOX_REFLUX_RIGHT_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, sandBoxRefluxRightPath, null, new Coordinate(648,750), null, null);

        subCars_6.add(castingUpSubCar);
        subCars_6.add(castingDownSubCar);
        subCars_6.add(castingVerticalSubCar);
        subCars_6.add(sandBoxRefluxRightSubCar);

        WorkStation openBoxWorkStation_1 = new WorkStation(113L, "openBoxColdAndHotWorkStation_1", COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_1, new Coordinate(582, 768), FREE, null, null);
        WorkStation openBoxWorkStation_2 = new WorkStation(114L, "openBoxColdAndHotWorkStation_2", COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_2, new Coordinate(595, 768), FREE, null, null);
        WorkStation openBoxWorkStation_3 = new WorkStation(115L, "openBoxColdAndHotWorkStation_3", COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_3, new Coordinate(608, 768), FREE, null, null);
        WorkStation openBoxWorkStation_4 = new WorkStation(116L, "openBoxColdAndHotWorkStation_4", COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_4, new Coordinate(621, 768), FREE, null, null);
        WorkStation openBoxWorkStation_5 = new WorkStation(117L, "openBoxHotWorkStation_5", HOT_SAND_UNBOXING_WORKSTATION_1, new Coordinate(582, 726), FREE, null, null);
        WorkStation openBoxWorkStation_6 = new WorkStation(118L, "openBoxHotWorkStation_6", HOT_SAND_UNBOXING_WORKSTATION_2, new Coordinate(595, 726), FREE, null, null);
        WorkStation openBoxWorkStation_7 = new WorkStation(119L, "openBoxHotWorkStation_7", HOT_SAND_UNBOXING_WORKSTATION_3, new Coordinate(608, 726), FREE, null, null);
        WorkStation openBoxWorkStation_8 = new WorkStation(120L, "openBoxHotWorkStation_8", HOT_SAND_UNBOXING_WORKSTATION_4, new Coordinate(621, 726), FREE, null, null);
        WorkStation openBoxWorkStation_9 = new WorkStation(113L, "openBoxColdAndHotWorkStation_9", COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_5, new Coordinate(582, 768), FREE, null, null);
        WorkStation openBoxWorkStation_10 = new WorkStation(114L, "openBoxColdAndHotWorkStation_10", COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_6, new Coordinate(595, 768), FREE, null, null);
        WorkStation openBoxWorkStation_11 = new WorkStation(115L, "openBoxColdAndHotWorkStation_11", COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_7, new Coordinate(608, 768), FREE, null, null);
        WorkStation openBoxWorkStation_12 = new WorkStation(116L, "openBoxColdAndHotWorkStation_12", COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_8, new Coordinate(621, 768), FREE, null, null);
        WorkStation openBoxWorkStation_13 = new WorkStation(117L, "openBoxHotWorkStation_13", HOT_SAND_UNBOXING_WORKSTATION_5, new Coordinate(582, 726), FREE, null, null);
        WorkStation openBoxWorkStation_14 = new WorkStation(118L, "openBoxHotWorkStation_14", HOT_SAND_UNBOXING_WORKSTATION_6, new Coordinate(595, 726), FREE, null, null);
        WorkStation openBoxWorkStation_15 = new WorkStation(119L, "openBoxHotWorkStation_15", HOT_SAND_UNBOXING_WORKSTATION_7, new Coordinate(608, 726), FREE, null, null);
        WorkStation openBoxWorkStation_16 = new WorkStation(120L, "openBoxHotWorkStation_16", HOT_SAND_UNBOXING_WORKSTATION_8, new Coordinate(621, 726), FREE, null, null);

        List<WorkStation> workStations_6 = new ArrayList<>();
        workStations_6.add(openBoxWorkStation_1);
        workStations_6.add(openBoxWorkStation_2);
        workStations_6.add(openBoxWorkStation_3);
        workStations_6.add(openBoxWorkStation_4);
        workStations_6.add(openBoxWorkStation_5);
        workStations_6.add(openBoxWorkStation_6);
        workStations_6.add(openBoxWorkStation_7);
        workStations_6.add(openBoxWorkStation_8);
        workStations_6.add(openBoxWorkStation_9);
        workStations_6.add(openBoxWorkStation_10);
        workStations_6.add(openBoxWorkStation_11);
        workStations_6.add(openBoxWorkStation_12);
        workStations_6.add(openBoxWorkStation_13);
        workStations_6.add(openBoxWorkStation_14);
        workStations_6.add(openBoxWorkStation_15);
        workStations_6.add(openBoxWorkStation_16);

        Position coldAndHotOpenBoxPosition = new Position(120L, "ColdAndHotOpenBoxPosition", HOT_SAND_AND_COLD_SAND_MIXING_UNBOXING_POSITION, new Coordinate(570, 771), UNOCCUPIED, null, null);
        Position hotOpenBoxPosition = new Position(121L, "HotOpenBoxPosition", HOT_SAND_UNBOXING_POSITION, new Coordinate(570, 723), UNOCCUPIED, null, null);
        Position coldAndHotRefluxPosition = new Position(122L, "ColdAndHotRefluxPosition", HOT_SAND_AND_COLD_SAND_MIXED_UNBOXING_REFLUX_POSITION_2, new Coordinate(649, 771), UNOCCUPIED, null, null);
        Position castingUpPosition = new Position(123L, "CastingUpPosition", CASTING_REFLUX_UP_POSITION, new Coordinate(569, 753), UNOCCUPIED, null, null);
        Position castingDownPosition = new Position(124L, "CastingDownPosition", CASTING_REFLUX_DOWN_POSITION, new Coordinate(569, 742), UNOCCUPIED, null, null);
        Position castingVerticalPosition = new Position(125L, "CastingVerticalPosition", CASTING_REFLUX_VERTICALINTERACTION_POSITION, new Coordinate(641, 753), UNOCCUPIED, null, null);
        Position castingShippingPosition = new Position(126L, "CastingShippingPosition", CASTING_REFLUX_SHIPPING_POSITION, new Coordinate(641, 742), UNOCCUPIED, null, null);
        List<Position> positions_6 = new ArrayList<>();
        positions_6.add(coldAndHotOpenBoxPosition);
        positions_6.add(hotOpenBoxPosition);
        positions_6.add(coldAndHotRefluxPosition);
        positions_6.add(castingUpPosition);
        positions_6.add(castingDownPosition);
        positions_6.add(castingVerticalPosition);
        positions_6.add(castingShippingPosition);

        AllocationInput allocationInput_6 = new AllocationInput(subCars_6, ferries_6, null, positions_6, workStations_6, paths_6, AreaEnum.SOUTH_OPEN_BOX);
        allocationInputs.add(allocationInput_6);

        //模拟订单产品
        List<Product> hotProducts = new ArrayList<>();
        Product hotSandUp = new Product(null, "上砂箱", 1, UPPER_BOX, null, null, HOT_SAND, false, 0.0, 60,
                600, 60, 60, 60, 60, 60, 60, 60, null);
        Product hotSandMold_1 = new Product(null, "模具", 2, MOULD, null, null, HOT_SAND, false, 0.0, 60,
                600, 60, 60, 60, 60, 60, 60, 60, null);
        Product hotSandDown = new Product(null, "下砂箱", 3, LOWER_BOX, null, null, HOT_SAND, false, 0.0, 60,
                600, 60, 60, 60, 60, 60, 60, 60, null);
        Product hotSandMold_2 = new Product(null, "模具", 4, MOULD, null, null, HOT_SAND, false, 0.0, 60,
                600, 60, 60, 60, 60, 60, 60, 60, null);
        Product coreHotSand = new Product(null, "砂芯", null, SAND_MOULD, null, null, CORE_SAND, false, 0.0, 60,
                600, 60, 60, 60, 60, 60, 60, 60, null);

        List<Product> coldProducts = new ArrayList<>();
        Product coldSandUp = new Product(null, "上砂箱", 1, UPPER_BOX, null, null, COLD_SAND, false, 0.0, 60,
                600, 60, 60, 60, 60, 60, 60, 60, null);
        Product coldSandMold_1 = new Product(null, "模具", 2, MOULD, null, null, COLD_SAND, false, 0.0, 60,
                600, 60, 60, 60, 60, 60, 60, 60, null);
        Product coldSandDown = new Product(null, "下砂箱", 3, LOWER_BOX, null, null, COLD_SAND, false, 0.0, 60,
                600, 60, 60, 60, 60, 60, 60, 60, null);
        Product coldSandMold_2 = new Product(null, "模具", 4, MOULD, null, null, COLD_SAND, false, 0.0, 60,
                600, 60, 60, 60, 60, 60, 60, 60, null);
        Product coreColdSand = new Product(null, "砂芯", null, SAND_MOULD, null, null, CORE_SAND, false, 0.0, 60,
                600, 60, 60, 60, 60, 60, 60, 60, null);
        hotProducts.add(hotSandUp);
        hotProducts.add(hotSandMold_1);
        hotProducts.add(hotSandDown);
        hotProducts.add(hotSandMold_2);
        hotProducts.add(coreHotSand);
        coldProducts.add(coldSandUp);
        coldProducts.add(coldSandMold_1);
        coldProducts.add(coldSandDown);
        coldProducts.add(coldSandMold_2);
        coldProducts.add(coreColdSand);

        OrderProduct orderProduct_1 = new OrderProduct(hotProducts, 1, "ABC", HOT_SAND, 10.0, 60, 0, 0, null, 1L);
        OrderProduct orderProduct_2 = new OrderProduct(coldProducts, 1, "BC", COLD_SAND, 10.0, 60, 0, 0, null,2L);
        List<OrderProduct> orderProducts = new ArrayList<>();
        orderProducts.add(orderProduct_1);
        orderProducts.add(orderProduct_2);
        List<OrderInput> orderInputs = new ArrayList<>();
        OrderInput orderInput = new OrderInput(1L, orderProducts, 100000.0, 100);
        orderInputs.add(orderInput);

        SimulateInput input = new SimulateInput();
        input.setAllocationInputs(allocationInputs);
        input.setOrderInputs(orderInputs);

        SubCar hotMouldSubCar = new SubCar(30L, "hotMouldSubCar", CASTING_REFLUX_UP_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, castingUpPath, null, new Coordinate(581,753), null, null);
        SubCar coreMouldSubCar = new SubCar(30L, "coreMouldSubCar", CASTING_REFLUX_UP_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, castingUpPath, null, new Coordinate(581,753), null, null);
        SubCar coldMouldSubCar = new SubCar(30L, "coldMouldSubCar", CASTING_REFLUX_UP_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, castingUpPath, null, new Coordinate(581,753), null, null);
        SubCar hotUpSpray = new SubCar(30L, "hotUpSpray", CASTING_REFLUX_UP_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, castingUpPath, null, new Coordinate(581,753), null, null);
        SubCar hotDownSpray = new SubCar(30L, "hotDownSpray", CASTING_REFLUX_UP_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, castingUpPath, null, new Coordinate(581,753), null, null);
        SubCar coldUpSpray = new SubCar(30L, "coldUpSpray", CASTING_REFLUX_UP_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, castingUpPath, null, new Coordinate(581,753), null, null);
        SubCar coldDownSpray = new SubCar(30L, "coldDownSpray", CASTING_REFLUX_UP_SUB_CAR, 0.33,
                0.25, 30, otherSubCarStatus, castingUpPath, null, new Coordinate(581,753), null, null);
        Equipment hotSandShooting = new Equipment(108L, "hotSandShooting", new Coordinate(645, 655), EquipmentOperationStatusEnum.STANDBY,
                null, 0.0, 0.0, EquipmentNameEnum.COLD_SAND_SPRAYING_3);
        Equipment hotOpenMold = new Equipment(108L, "hotOpenMold", new Coordinate(645, 655), EquipmentOperationStatusEnum.STANDBY,
                null, 0.0, 0.0, EquipmentNameEnum.COLD_SAND_SPRAYING_3);
        Equipment coreSandShooting = new Equipment(108L, "coreSandShooting", new Coordinate(645, 655), EquipmentOperationStatusEnum.STANDBY,
                null, 0.0, 0.0, EquipmentNameEnum.COLD_SAND_SPRAYING_3);
        Equipment coreOpenMold = new Equipment(108L, "coreOpenMold", new Coordinate(645, 655), EquipmentOperationStatusEnum.STANDBY,
                null, 0.0, 0.0, EquipmentNameEnum.COLD_SAND_SPRAYING_3);
        Equipment coldSandShooting = new Equipment(108L, "coldSandShooting", new Coordinate(645, 655), EquipmentOperationStatusEnum.STANDBY,
                null, 0.0, 0.0, EquipmentNameEnum.COLD_SAND_SPRAYING_3);
        Equipment coldOpenMold = new Equipment(108L, "coldOpenMold", new Coordinate(645, 655), EquipmentOperationStatusEnum.STANDBY,
                null, 0.0, 0.0, EquipmentNameEnum.COLD_SAND_SPRAYING_3);

        input.setAnalogInput(new AnalogInput(hotMouldSubCar, coreMouldSubCar, coldMouldSubCar, hotUpSpray, hotDownSpray, coldUpSpray, coldDownSpray,
                hotSandShooting, hotOpenMold, coreSandShooting, coreOpenMold, coldSandShooting, coldOpenMold));

        return input;
    }
}
