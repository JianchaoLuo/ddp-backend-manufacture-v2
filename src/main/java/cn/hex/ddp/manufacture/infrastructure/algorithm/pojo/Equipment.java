package cn.hex.ddp.manufacture.infrastructure.algorithm.pojo;

import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentNameEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription: 设备类，用于描述设备的具体情况
 * @Author: KangHong
 * @Created: 2024/5/23 14:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Equipment implements Serializable {
    /**
     * 设备的id
     */
    private Long id;
    /**
     * 设备的名称
     */
    private String equipmentName;
    /**
     * 设备的坐标
     */
    private Coordinate equipmentCoordinate;
    /**
     * 设备的状态
     */
    private EquipmentOperationStatusEnum equipmentStatus;
    /**
     * 设备中加工的产品列表
     */
    private List<Product> products;
    /**
     * 设备已经完成的工作时间，设备必须在工作中才有意义
     */
    private double finishWorkTime;
    /**
     * 设备剩余的工作时间，设备必须在工作中才有意义
     */
    private double remainWorkTime;
    /**
     * 设备的名字枚举
     */
    private EquipmentNameEnum nameEnum;
}
