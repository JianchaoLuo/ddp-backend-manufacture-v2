package cn.hex.ddp.manufacture.domain.simulator.model;

import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum;
import lombok.Data;

/**
 * 设备变化数据
 * @Author: FengZebang
 * @Date: 2025/3/10
 */
@Data
public class EquipmentVaryData {
    /**
     * 设备编号
     */
    private Long id;
    /**
     * 设备操作状态
     */
    private EquipmentOperationStatusEnum operationStatus;
    /**
     * 当前操作
     */
    private String currentAction;
    /**
     * 铁水状态
     */
    private Double moltenIronPouringQuantity;
}
