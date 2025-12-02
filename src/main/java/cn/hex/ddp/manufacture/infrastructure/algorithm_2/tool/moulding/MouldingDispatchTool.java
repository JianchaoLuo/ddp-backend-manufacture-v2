package cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.moulding;

import cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentNameEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationNameEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Ferry;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.SubCar;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.outbound.OutBoundDispatchTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.EventName;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.input.ResourceInput;

import java.util.List;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2025/3/14 11:16
 */
public class MouldingDispatchTool {
    OutBoundDispatchTool outBoundDispatchTool = new OutBoundDispatchTool();

    public void generateResEventByCar(CarNameEnum carNameEnum, ResourceInput resourceInput, List<EventName> events) {
        // 根据车辆名称枚举查找对应的子车信息
        SubCar subCar = resourceInput.getSubCarByName(carNameEnum);
        if (subCar != null) {
            if (carNameEnum.getCode() > 0 && carNameEnum.getCode() <= 7){
                outBoundDispatchTool.generateResEventBySubCar(subCar, resourceInput, events);
            }
        }else {
            Ferry ferry = resourceInput.getFerryByName(carNameEnum);
            if (carNameEnum.getCode() >= 100 && carNameEnum.getCode() <= 102){
                outBoundDispatchTool.generateResEventByFerry(ferry, resourceInput, events);
            }
        }
    }

    public void generateResEventByWorkstation(WorkstationNameEnum workstationNameEnum, ResourceInput resourceInput, List<EventName> events) {

    }

    public void generateResEventByEquipment(EquipmentNameEnum equipmentNameEnum, ResourceInput resourceInput, List<EventName> events) {
    }
}
