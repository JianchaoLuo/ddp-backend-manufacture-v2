package cn.hex.ddp.manufacture.application.equipment.dto;

import cn.hex.ddp.manufacture.api.plc.rest.vo.PLCSummaryVO;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
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
 * @author zhanganqi
 * @date 2025/06/10
 */
@Data
public class EquipmentSummaryDTO {
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

//    /**
//     * 所属区域
//     */
//    private AreaEnum area;

    /**
     * 设备运行状态
     */
    private EquipmentOperationStatusEnum operationStatus;

    /**
     * 设备类型
     */
    private EquipmentTypeEnum type;

    /**
     * 设备所在工位ID
     */
    private Coordinate coordinate;

    /**
     * 设备名称枚举（供算法使用）
     */
    private EquipmentNameEnum nameEnum;
}
