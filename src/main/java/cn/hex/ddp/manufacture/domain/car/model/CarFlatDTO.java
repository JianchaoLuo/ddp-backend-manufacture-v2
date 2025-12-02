package cn.hex.ddp.manufacture.domain.car.model;

import cn.hex.ddp.manufacture.domain.car.enums.CarControlStatusEnum;
import cn.hex.ddp.manufacture.domain.car.enums.CarOperationStatusEnum;
import cn.hex.ddp.manufacture.domain.car.enums.CarTypeEnum;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarFlatDTO {
    private Long id;
    private String carNo;
    private String name;
    private CarTypeEnum type;
    private CarOperationStatusEnum operationStatus;
    private CarControlStatusEnum controlStatus;
    private AreaEnum area;
    private CarModel model;
    private List<CarPath> carPaths;
    private String headDirection;
    private String bindCar;
    private String nameEnum;
    private String plcIp;

    private String tick;
    private String mission;
    private String loadStatus;
    private String topRodStatus;
    private String ferryStatus;

    private Coordinate currentCoordinate;
    private Coordinate targetCoordinate;
    private String locationStatus;
    private String connectStatus;
    private List<Object> cargos;
    private Object currentEvent;
    private int currentRelativePosition;
    private String currentAction;
}
