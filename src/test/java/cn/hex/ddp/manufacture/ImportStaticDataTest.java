package cn.hex.ddp.manufacture;

import cn.hex.ddp.manufacture.application.simulator.SimulateService;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.common.enums.ProcessEnum;
import cn.hex.ddp.manufacture.domain.configuration.model.Coordinate;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentControlStatusEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentTypeEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathTypeEnum;
import cn.hex.ddp.manufacture.domain.path.enums.RailDirectionEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.configuration.persistence.po.CoordinatePO;
import cn.hex.ddp.manufacture.infrastructure.configuration.persistence.po.PositionPO;
import cn.hex.ddp.manufacture.infrastructure.configuration.persistence.postgresql.repository.CoordinatePORepository;
import cn.hex.ddp.manufacture.infrastructure.configuration.persistence.postgresql.repository.PositionPORepository;
import cn.hex.ddp.manufacture.infrastructure.equipment.persistence.po.EquipmentPO;
import cn.hex.ddp.manufacture.infrastructure.equipment.persistence.postgresql.repository.EquipmentPORepository;
import cn.hex.ddp.manufacture.infrastructure.path.persistence.po.PathPO;
import cn.hex.ddp.manufacture.infrastructure.path.persistence.postgresql.repository.PathPORepository;
import cn.hex.ddp.manufacture.infrastructure.workstation.persistence.po.WorkstationPO;
import cn.hex.ddp.manufacture.infrastructure.workstation.persistence.postgresql.repository.WorkstationPORepository;
import cn.hutool.core.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ImportStaticDataTest {

    @Autowired
    private SimulateService simulateService;
    @Autowired
    private EquipmentPORepository equipmentPORepository;
    @Autowired
    private CoordinatePORepository coordinatePORepository;
    @Autowired
    private PositionPORepository positionPORepository;
    @Autowired
    private WorkstationPORepository workstationPORepository;
    @Autowired
    private PathPORepository pathPORepository;
//    @Test
    void saveAll(){
        savePath();
        saveEquipment();
        saveEquipment();
        saveWorkStation();
        savePosition();
    }
    void savePath() {
        savePathPO(3173, 955, 3431, 955, 2915, 955, 516, 40, "热砂合模轨道", "HotSandMoldingTrack");
        savePathPO(3173, 1193, 3431, 1193, 2915, 1193, 516, 40, "冷砂合模轨道", "ColdSandMoldingTrack");
        savePathPO(2927, 1012, 3432, 1012, 2422, 1012, 1010, 40, "制芯轨道", "CoreMakingTrack");
        savePathPO(2896, 1133, 2360, 1133, 3432, 1133, 1072, 40, "模具回流轨道", "MoldRefluxTrack");
        savePathPO(2491, 866, 2955, 866, 2027, 866, 928, 40, "热砂射砂轨道", "HotSandBlastingTrack");
        savePathPO(2620, 1248, 2955, 1248, 2285, 1248, 670, 40, "冷砂射砂轨道", "ColdSandShootingTrack");
        savePathPO(2174, 949, 2319, 949, 2029, 949, 290, 40, "热砂喷涂轨道", "HotSandSprayingTrack");
        savePathPO(2177, 1295, 2326, 1295, 2028, 1295, 298, 40, "冷砂喷涂上半轨道", "ColdSandSprayingTrackUp");
        savePathPO(2177, 1373, 2326, 1373, 2028, 1373, 298, 40, "冷砂喷涂下半轨道", "ColdSandSprayingTrackDown");
        savePathPO(2738, 777, 2044, 777, 3432, 777, 1388, 40, "总体砂箱回流轨道", "SandBoxRefluxTrackAll");
        savePathPO(3166, 1384, 2900, 1384, 3432, 1384, 532, 40, "冷砂砂箱回流轨道", "ColdSandBoxRefluxTrack");
        savePathPO(3389, 903, 3431, 903, 3347, 903, 84, 25, "辅路1", "辅路1");
        savePathPO(3389, 1245, 3431, 1245, 3347, 1245, 84, 25, "辅路7", "辅路7");
        savePathPO(2025, 1335, 2068, 1335, 1982, 1335, 86, 25, "辅路15", "辅路15");
        savePathPO(2025, 839, 2068, 839, 1982, 839, 86, 25, "辅路16", "辅路16");
        savePathPO(3410, 1081, 3410, 1404, 3410, 758, 646, 40, "出库轨道", "OutboundTrack");
        savePathPO(2048, 898, 2048, 969, 2048, 827, 142, 40, "热砂合箱垂直轨道", "HotSandConsolidationVerticalTrack");
        savePathPO(2048, 1334, 2048, 1275, 2048, 1393, 118, 40, "冷砂合箱垂直轨道", "ColdSandConsolidationVerticalTrack");
        savePathPO(2300, 863, 2300, 969, 2300, 757, 212, 40, "热砂垂直轨道", "HotSandVerticalTrack");
        savePathPO(2306, 1311, 2306, 1229, 2306, 1393, 164, 40, "冷砂垂直左半轨道", "ColdSandVerticalTrackLeft");
        savePathPO(2380, 1191, 2380, 1114, 2380, 1268, 154, 40, "冷砂垂直右半轨道", "ColdSandVerticalTrackRight");
        savePathPO(2443, 1000, 2443, 847, 2443, 1153, 306, 40, "制芯模具回流垂直轨道", "MoldRefluxVerticalTrack");
        savePathPO(3358, 933, 3358, 892, 3358, 974, 82, 25, "辅路2", "辅路2");
        savePathPO(3286, 933, 3286, 974, 3286, 892, 82, 25, "辅路3", "辅路3");
        savePathPO(3192, 933, 3192, 974, 3192, 892, 82, 25, "辅路4", "辅路4");
        savePathPO(3096, 933, 3096, 974, 3096, 892, 82, 25, "辅路5", "辅路5");
        savePathPO(3002, 933, 3002, 974, 3002, 892, 82, 25, "辅路6", "辅路6");
        savePathPO(3358, 1214, 3358, 1255, 3358, 1173, 82, 25, "辅路8", "辅路8");
        savePathPO(3286, 1214, 3286, 1173, 3286, 1255, 82, 25, "辅路9", "辅路9");
        savePathPO(3192, 1214, 3192, 1173, 3192, 1255, 82, 25, "辅路10", "辅路10");
        savePathPO(3098, 1214, 3098, 1173, 3098, 1255, 82, 25, "辅路11", "辅路11");
        savePathPO(3002, 1214, 3002, 1173, 3002, 1255, 82, 25, "辅路12", "辅路12");
        savePathPO(2929, 913, 2929, 973, 2929, 853, 120, 25, "辅路13", "辅路13");
        savePathPO(2929, 1215, 2929, 1172, 2929, 1258, 86, 25, "辅路14", "辅路14");
    }

    void savePathPO(int positionX, int positionY,
                    int startX, int startY,
                    int endX, int endY,
                    int length,
                    int width,
                    String content,
                    String enContent
    ) {
        CoordinatePO coordinatePO = new CoordinatePO(startX, startY);
        coordinatePORepository.save(coordinatePO);
        CoordinatePO coordinatePO1 = new CoordinatePO(endX, endY);
        coordinatePORepository.save(coordinatePO1);
        CoordinatePO middleCoordinate = new CoordinatePO(positionX, positionY);
        coordinatePORepository.save(middleCoordinate);
        PathPO pathPO = new PathPO();
        pathPO.setPathNo(enContent);
        pathPO.setName(content);
        pathPO.setStartCoordinateId(coordinatePO.getId());
        pathPO.setEndCoordinateId(coordinatePO1.getId());
        pathPO.setMiddleCoordinateId(middleCoordinate.getId());
        pathPO.setRailLength((float) length);
        pathPO.setRailWidth((float) width);
        pathPO.setRailDirection(toRailDirection(coordinatePO, coordinatePO1));
        pathPO.setArea(AreaEnum.SOUTH_MOULDING);
        pathPO.setPathType(PathTypeEnum.ONLY_FERRY_CAR);
        pathPO.setDescription(content);
        pathPORepository.save(pathPO);
    }

    private RailDirectionEnum toRailDirection(CoordinatePO start, CoordinatePO end) {
        if (start.getX().equals(end.getX())) {
            if (start.getY() < end.getY())
                return RailDirectionEnum.TOP_TO_BOTTOM;
            else
                return RailDirectionEnum.BOTTOM_TO_TOP;
        }
        if (start.getX() < end.getX())
            return RailDirectionEnum.LEFT_TO_RIGHT;
        else
            return RailDirectionEnum.RIGHT_TO_LEFT;
    }

    void saveWorkStation() {
        saveWorkStationPO(3002, 902, "热砂合模 1");
        saveWorkStationPO(3097, 902, "热砂合模 2");
        saveWorkStationPO(3192, 902, "热砂合模 3");
        saveWorkStationPO(3286, 902, "热砂合模 4");
        saveWorkStationPO(3002, 1246, "冷砂合模 1");
        saveWorkStationPO(3097, 1246, "冷砂合模 2");
        saveWorkStationPO(3192, 1246, "冷砂合模 3");
        saveWorkStationPO(3286, 1246, "冷砂合模 4");
    }

    private void saveWorkStationPO(int x, int y, String name) {
        CoordinatePO coordinatePO = new CoordinatePO();
//        coordinatePO.setId(coordinateId++);
        coordinatePO.setX((float) x);
        coordinatePO.setY((float) y);
        coordinatePORepository.save(coordinatePO);
        WorkstationPO workstationPO = new WorkstationPO();
        workstationPO.setName(name);
        workstationPO.setCoordinateId(coordinatePO.getId());
        workstationPO.setStationNo(name);
        workstationPO.setDescription(name);
        workstationPO.setArea(AreaEnum.SOUTH_AFTER_COOLING);
        workstationPO.setProcess(ProcessEnum.AFTER_COOLING);
        workstationPO.setPriority(1);
        workstationPO.setStatus(WorkstationStatusEnum.FREE);
        workstationPORepository.save(workstationPO);
    }
//    @Test
    void savePosition() {
        savePositionPO(1, 3358, 902, "HotSandMoldingPoint", "热砂合模交互点");
        savePositionPO(2, 3358, 1247, "ColdSandMoldingPoint", "冷砂合模交互点");
        savePositionPO(3, 2929, 865, "HotSandShootingPoint", "热砂射砂交互点");
        savePositionPO(4, 2929, 1248, "ClodSandShootingPoint", "冷砂射砂交互点");
        savePositionPO(5, 2443, 866, "HotSandMoldRefluxPoint", "热砂模具回流交互点");
        savePositionPO(6, 2443, 1011, "SandCoreMoldRefluxPoint", "砂芯磨具回流交互点");
        savePositionPO(7, 2443, 1132, "HotSandMoldRefluxRailInteractionPoint", "热砂模具回流交叉交互点");
        savePositionPO(8, 2380, 1247, "ColdSandMoldRefluxPoint", "冷砂模具回流交互点");
        savePositionPO(9, 2380, 1132, "ColdSandMoldRefluxRailInteractionPoint", "冷砂模具回流交叉交互点");
        savePositionPO(10, 2300, 777, "HotSandVerticalTrackRefluxInteractionPoint", "热砂垂直回流交叉交互点");
        savePositionPO(11, 2300, 866, "HotSandVerticalRailInteractionPoint", "热砂垂直交叉交互点");
        savePositionPO(12, 2300, 949, "HotSandVerticalTrackSprayingPoint", "热砂垂直喷涂交互点");
        savePositionPO(13, 2306, 1247, "ColdSandVerticalRailInteractionPoint", "冷砂垂直交叉交互点");
        savePositionPO(14, 2306, 1295, "ColdSandSprayingPoints_1", "冷砂垂直喷涂交互点_1");
        savePositionPO(15, 2306, 1373, "ColdSandSprayingPoints_2", "冷砂垂直喷涂交互点_2");
    }

    private void savePositionPO(int no, int x, int y, String positionNo, String name) {
        CoordinatePO coordinatePO = new CoordinatePO();
//        coordinatePO.setId(coordinateId++);
        coordinatePO.setX((float) x);
        coordinatePO.setY((float) y);
        coordinatePORepository.save(coordinatePO);
        PositionPO positionPO = new PositionPO();
        positionPO.setCoordinateId(coordinatePO.getId());
//        positionPO.setPositionNo((long) no);
        positionPO.setName(positionNo);
        positionPO.setDescription(name);
        positionPORepository.save(positionPO);

    }


    void saveEquipment() {
        saveEquipmentPO(EquipmentTypeEnum.HOT_SAND_SHOOTER, new Coordinate(2792f, 865f), 80, 60,
                "HotSandBlasting");
        saveEquipmentPO(EquipmentTypeEnum.COLD_SAND_SHOOTER, new Coordinate(2795f, 1248f), 80, 60,
                "ColdSandBlasting");
        saveEquipmentPO(EquipmentTypeEnum.CORE_SHOOTER, new Coordinate(2797f, 1010f), 80, 60,
                "SandboxSandblasting");
        saveEquipmentPO(EquipmentTypeEnum.OPEN_MOLD_EQUIPMENT, new Coordinate(2512f, 865f), 80, 60,
                "HotSandMoldOpening");
        saveEquipmentPO(EquipmentTypeEnum.OPEN_MOLD_EQUIPMENT, new Coordinate(2483f, 1248f), 80, 60,
                "ColdSandMoldOpening");
        saveEquipmentPO(EquipmentTypeEnum.OPEN_MOLD_EQUIPMENT, new Coordinate(2512f, 1010f), 80, 60,
                "SandCoreMoldOpening");
        saveEquipmentPO(EquipmentTypeEnum.SPRAYING_MACHINE, new Coordinate(2112f, 865f), 55, 50,
                "HotSandSpraying_1");
        saveEquipmentPO(EquipmentTypeEnum.SPRAYING_MACHINE, new Coordinate(2176f, 865f), 55, 50,
                "HotSandSpraying_2");
        saveEquipmentPO(EquipmentTypeEnum.SPRAYING_MACHINE, new Coordinate(2112f, 948f), 55, 50,
                "HotSandSpraying_3");
        saveEquipmentPO(EquipmentTypeEnum.SPRAYING_MACHINE, new Coordinate(2176f, 948f), 55, 50,
                "HotSandSpraying_4");
        saveEquipmentPO(EquipmentTypeEnum.SPRAYING_MACHINE, new Coordinate(2176f, 1295f), 55, 50,
                "ColdSandSpraying_1");
        saveEquipmentPO(EquipmentTypeEnum.SPRAYING_MACHINE, new Coordinate(2112f, 1295f), 55, 50,
                "ColdSandSpraying_2");
        saveEquipmentPO(EquipmentTypeEnum.SPRAYING_MACHINE, new Coordinate(2112f, 1372f), 55, 50,
                "ColdSandSpraying_3");
        saveEquipmentPO(EquipmentTypeEnum.SPRAYING_MACHINE, new Coordinate(2176f, 1372f), 55, 50,
                "ColdSandSpraying_4");

    }

    private void saveEquipmentPO(EquipmentTypeEnum type,
                                 Coordinate coordinate,
                                 int width,
                                 int height,
                                 String content
    ) {
        CoordinatePO coordinatePO = new CoordinatePO();
//        coordinatePO.setId(coordinateId++);
        coordinatePO.setX(coordinate.getX());
        coordinatePO.setY(coordinate.getY());
        coordinatePORepository.save(coordinatePO);
        EquipmentPO equipmentPO = new EquipmentPO();
        equipmentPO.setCoordinateId(coordinatePO.getId());
        equipmentPO.setEquipmentNo(RandomUtil.randomString(10));
        equipmentPO.setModel(content);
        equipmentPO.setName(content);
        equipmentPO.setOperationStatus(EquipmentOperationStatusEnum.OFFLINE);
        equipmentPO.setControlStatus(EquipmentControlStatusEnum.REMOTE_CONTROL);
        equipmentPO.setType(type);
        equipmentPO.setDisplayStyle("{\"width\":" + width + ",\"height\":" + height + "}");
        equipmentPORepository.save(equipmentPO);
    }

}
