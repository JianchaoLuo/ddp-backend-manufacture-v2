package cn.hex.ddp.manufacture.infrastructure.algorithm.util;

import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription: 设备和事件的关联类，用于表示这个事件的参与者设备的相关状态
 * @Author: KangHong
 * @Created: 2024/5/30 16:47
 */
@AllArgsConstructor
@Data
public class EquipmentLinkEvent implements Serializable {
    /**
     * 设备id
     */
    private Long id;
    /**
     * 事件参与中，设备应该的状态集合
     * 对于设备的状态，不存在多个状态同时存在，所以在List中的状态是或者关系，即满足一个即可
     * 不存在A和B状态的情况
     */
    private List<EquipmentOperationStatusEnum> equipmentStatus;
}
