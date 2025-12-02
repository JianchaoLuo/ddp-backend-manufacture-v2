package cn.hex.ddp.manufacture.domain.equipment.model;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentControlStatusEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentNameEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentTypeEnum;
import lombok.Data;
import cn.hex.ddp.manufacture.domain.configuration.model.Coordinate;

import java.util.List;

/**
 * 设备聚合模型
 *
 * @author Huhaisen
 * @date 2024/05/03
 */
@Data
public class Equipment {
    /**
     * 设备ID(数据库ID)
     */
    private Long id;

    /**
     * 设备唯一编号
     */
    private String equipmentNo;

    /**
     * 设备型号
     */
    private String model;

    /**
     * 所属区域
     */
    private AreaEnum area;
    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备运行状态
     */
    private EquipmentOperationStatusEnum operationStatus;

    /**
     * 设备控制状态
     */
    private EquipmentControlStatusEnum controlStatus;

    /**
     * 设备类型
     */
    private EquipmentTypeEnum type;

    /**
     * 设备所在坐标
     */
    private Long coordinateId;
    /**
     * 展示样式
     */
    private String displayStyle;

    /**
     * 关联的PLCId
     */
    private Long plcId;

    /**
     * 设备参数列表
     */
    private List<EquipmentParameter> parameters;

    /**
     * 设备名称枚举（供算法使用）
     */
    private EquipmentNameEnum nameEnum;

    /**
     * 设备所在坐标（完整对象）
     */
    private Coordinate coordinate;
}
