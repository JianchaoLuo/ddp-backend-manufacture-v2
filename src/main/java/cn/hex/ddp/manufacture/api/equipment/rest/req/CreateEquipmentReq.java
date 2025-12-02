package cn.hex.ddp.manufacture.api.equipment.rest.req;

import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentControlStatusEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentNameEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * @author zhaolin
 * @date 2024/5/16
 */
@Data
public class CreateEquipmentReq {
    /**
     * 设备唯一编号
     */
    private String equipmentNo;

    /**
     * 设备型号
     */
    private String model;

    /**
     * 设备名称
     */
    private String name;

    /**
     * 设备控制状态
     */
    private EquipmentControlStatusEnum controlStatus;

    /**
     * 设备类型
     */
    private EquipmentTypeEnum type;

    /**
     * 设备所在位置id
     */
    private Long coordinateId;
    /**
     * 展示样式
     */
    private Object displayStyle;

    /**
     * 关联的PLCId
     */
    private Long plcId;

    /**
     * 设备参数列表
     */
    private List<CreateEquipmentParameterReq> parameters;

    /**
     * 设备名称枚举（供算法使用）
     */
    private EquipmentNameEnum nameEnum;
}
