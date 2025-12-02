package cn.hex.ddp.manufacture.infrastructure.simulator;

import cn.hex.ddp.manufacture.application.simulator.service.SimulatorService;
import cn.hex.ddp.manufacture.domain.car.enums.CarOperationStatusEnum;
import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.RailDirectionEnum;
import cn.hex.ddp.manufacture.domain.simulator.model.simulation.*;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductAfootEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


/**
 * @Author: FengZebang
 * @Date: 2025/4/22
 */
@SpringBootTest
public class TickRecordTest {
    @Resource
    private SimulatorService simulatorService;

    @Test
    public void TickRecordInPostgreSQL() {
        // 创建模拟对象
        Long currentTick = 1L;
        Long missionId = 1L;
        List<Car> cars = createMockCars();
        List<Equipment> equipments = createMockEquipments();
        Collection<PositionRealTime> positions = createMockPositions();
        Collection<WorkstationRealTime> workstations = createMockWorkstations();
        Collection<Path> paths = createMockPaths();
        Collection<Product> products = createMockProducts();

        Long astart = System.currentTimeMillis();
        for(int i = 0; i < 50; i++){
            // 调用被测方法
            Long start = System.currentTimeMillis();
            simulatorService.saveSimulatorData(currentTick, missionId, cars, equipments, positions, workstations, paths, products);
            Long end = System.currentTimeMillis();
            System.out.println("存储第" + i + "帧耗时：" + (end - start) + "ms");
            currentTick++;
        }
        Long aend = System.currentTimeMillis();
        System.out.println("总耗时：" + (aend - astart) + "ms");

    }
    private List<Car> createMockCars() {
        List<Car> cars = new ArrayList<>();
        Car car1 = mock(Car.class);
        Car car2 = mock(Car.class);

        CarRealTime realTime1 = mock(CarRealTime.class);
        CarRealTime realTime2 = mock(CarRealTime.class);
        Coordinate currentCoordinate1 = mock(Coordinate.class);
        Coordinate targetCoordinate1 = mock(Coordinate.class);
        Coordinate currentCoordinate2 = mock(Coordinate.class);
        Coordinate targetCoordinate2 = mock(Coordinate.class);

        when(car1.getId()).thenReturn(100L);
        when(car1.getRealTime()).thenReturn(realTime1);
        when(realTime1.getOperationStatus()).thenReturn(CarOperationStatusEnum.STANDBY);
        when(realTime1.getCurrentCoordinate()).thenReturn(currentCoordinate1);
        when(realTime1.getTargetCoordinate()).thenReturn(targetCoordinate1);
        when(currentCoordinate1.scaleConversionToString()).thenReturn("car1CurrentCoordinate");
        when(targetCoordinate1.scaleConversionToString()).thenReturn("car1TargetCoordinate");
        when(realTime1.getCurrentAction()).thenReturn("car1Action");

        when(car2.getId()).thenReturn(110L);
        when(car2.getRealTime()).thenReturn(realTime2);
        when(realTime2.getOperationStatus()).thenReturn(CarOperationStatusEnum.OFFLINE);
        when(realTime2.getCurrentCoordinate()).thenReturn(currentCoordinate2);
        when(realTime2.getTargetCoordinate()).thenReturn(targetCoordinate2);
        when(currentCoordinate2.scaleConversionToString()).thenReturn("car2CurrentCoordinate");
        when(targetCoordinate2.scaleConversionToString()).thenReturn("car2TargetCoordinate");
        when(realTime2.getCurrentAction()).thenReturn("car2Action");

        cars.add(car1);
        cars.add(car2);
        return cars;
    }

    private List<Equipment> createMockEquipments() {
        List<Equipment> equipments = new ArrayList<>();
        Equipment equipment1 = mock(Equipment.class);
        Equipment equipment2 = mock(Equipment.class);

        EquipmentRealTime realTime1 = mock(EquipmentRealTime.class);
        EquipmentRealTime realTime2 = mock(EquipmentRealTime.class);

        when(equipment1.getId()).thenReturn(123L);
        when(equipment1.getRealTime()).thenReturn(realTime1);
        when(realTime1.getOperationStatus()).thenReturn(EquipmentOperationStatusEnum.BREAKDOWN);
        when(realTime1.getMoltenIronQuantity()).thenReturn(10.234);

        when(equipment2.getId()).thenReturn(456L);
        when(equipment2.getRealTime()).thenReturn(realTime2);
        when(realTime2.getOperationStatus()).thenReturn(EquipmentOperationStatusEnum.FINISHED);
        when(realTime2.getMoltenIronQuantity()).thenReturn(56.564);

        equipments.add(equipment1);
        equipments.add(equipment2);
        return equipments;
    }

    private Collection<PositionRealTime> createMockPositions() {
        Collection<PositionRealTime> positions = new ArrayList<>();
        PositionRealTime position1 = mock(PositionRealTime.class);
        PositionRealTime position2 = mock(PositionRealTime.class);

        PositionStatusEnum status1 = mock(PositionStatusEnum.class);
        PositionStatusEnum status2 = mock(PositionStatusEnum.class);

        when(position1.getId()).thenReturn(789L);
        when(position1.getStatus()).thenReturn(status1);
        when(status1.name()).thenReturn("position1Status");
        when(position1.getCurrentAction()).thenReturn("position1Action");

        when(position2.getId()).thenReturn(585L);
        when(position2.getStatus()).thenReturn(status2);
        when(status2.name()).thenReturn("position2Status");
        when(position2.getCurrentAction()).thenReturn("position2Action");

        positions.add(position1);
        positions.add(position2);
        return positions;
    }

    private Collection<WorkstationRealTime> createMockWorkstations() {
        Collection<WorkstationRealTime> workstations = new ArrayList<>();
        WorkstationRealTime workstation1 = mock(WorkstationRealTime.class);
        WorkstationRealTime workstation2 = mock(WorkstationRealTime.class);

        when(workstation1.getId()).thenReturn(741L);
        when(workstation1.getName()).thenReturn("workstation1Name");
        when(workstation1.getStatus()).thenReturn(WorkstationStatusEnum.WAIT_TRANSPORT);
        when(workstation1.getCurrentAction()).thenReturn("workstation1Action");

        when(workstation2.getId()).thenReturn(963L);
        when(workstation2.getName()).thenReturn("workstation2Name");
        when(workstation2.getStatus()).thenReturn(WorkstationStatusEnum.FREE);
        when(workstation2.getCurrentAction()).thenReturn("workstation2Action");

        workstations.add(workstation1);
        workstations.add(workstation2);
        return workstations;
    }

    private Collection<Path> createMockPaths() {
        Collection<Path> paths = new ArrayList<>();
        Path path1 = mock(Path.class);
        Path path2 = mock(Path.class);

        Coordinate startCoordinate1 = mock(Coordinate.class);
        Coordinate endCoordinate1 = mock(Coordinate.class);
        Coordinate startCoordinate2 = mock(Coordinate.class);
        Coordinate endCoordinate2 = mock(Coordinate.class);

        when(path1.getId()).thenReturn(7532L);
        when(path1.getName()).thenReturn("path1Name");
        when(path1.getLength()).thenReturn(10.3);
        when(path1.getStartCoordinate()).thenReturn(startCoordinate1);
        when(path1.getEndCoordinate()).thenReturn(endCoordinate1);
        when(startCoordinate1.scaleConversionToString()).thenReturn("path1StartCoordinate");
        when(endCoordinate1.scaleConversionToString()).thenReturn("path1EndCoordinate");
        when(path1.getDirection()).thenReturn(RailDirectionEnum.LEFT_TO_RIGHT);
        when(path1.getStatus()).thenReturn(PathStatusEnum.PATH_UNOCCUPIED);

        when(path2.getId()).thenReturn(862L);
        when(path2.getName()).thenReturn("path2Name");
        when(path2.getLength()).thenReturn(20.2);
        when(path2.getStartCoordinate()).thenReturn(startCoordinate2);
        when(path2.getEndCoordinate()).thenReturn(endCoordinate2);
        when(startCoordinate2.scaleConversionToString()).thenReturn("path2StartCoordinate");
        when(endCoordinate2.scaleConversionToString()).thenReturn("path2EndCoordinate");
        when(path2.getDirection()).thenReturn(RailDirectionEnum.BOTTOM_TO_TOP);
        when(path2.getStatus()).thenReturn(PathStatusEnum.PATH_UNAVAILABLE);

        paths.add(path1);
        paths.add(path2);
        return paths;
    }

    private Collection<Product> createMockProducts() {
        Collection<Product> products = new ArrayList<>();
        Product product1 = mock(Product.class);
        Product product2 = mock(Product.class);

        when(product1.getId()).thenReturn(951L);
        when(product1.getName()).thenReturn("product1Name");
        when(product1.getProductType()).thenReturn(ProductTypeEnum.MOULD);
        when(product1.getProductAfoot()).thenReturn(ProductAfootEnum.REFLUXING);

        when(product2.getId()).thenReturn(845L);
        when(product2.getName()).thenReturn("product2Name");
        when(product2.getProductType()).thenReturn(ProductTypeEnum.UPPER_BOX);
        when(product2.getProductAfoot()).thenReturn(ProductAfootEnum.GOING_SANDBLASTING);

        products.add(product1);
        products.add(product2);
        return products;
    }


}


