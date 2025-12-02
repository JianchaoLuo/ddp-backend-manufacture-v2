package cn.hex.ddp.manufacture.api.equipment.rest.vo;

import cn.hex.ddp.manufacture.api.plc.rest.vo.PLCSummaryVO;
import cn.hex.ddp.manufacture.domain.configuration.model.Coordinate;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentControlStatusEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentNameEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentTypeEnum;
import cn.hex.ddp.manufacture.domain.equipment.model.EquipmentParameter;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Data;

import java.util.List;

/**
 * @author zhaolin
 * @date 2024/5/16
 */
@Data
public class EquipmentVO {
    /**
     * 设备ID
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
     * 设备所在工位ID
     */
    private Coordinate coordinate;
    /**
     * 展示样式
     */
    @JsonRawValue
    private String displayStyle;
    /**
     * 关联的PLC
     */
    private PLCSummaryVO plc;

    /**
     * 设备参数列表
     */
    private List<EquipmentParameter> parameters;

    /**
     * 设备名称枚举（供算法使用）
     */
    private EquipmentNameEnum nameEnum;
}
