package cn.hex.ddp.manufacture.domain.plc.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * PLC类型枚举
 *
 * @author Huhaisen
 * @date 2024/05/03
 */
@Getter
@AllArgsConstructor
public enum PLCTypeEnum {

    /**
     * 设备PLC
     */
    EQUIPMENT_PLC(0),

    /**
     * 区域PLC
     */
    AREA_PLC(1),

    /**
     * 车辆PLC
     */
    CAR_PLC(2),

    /**
     * 工岗PLC
     */
    WORKSTATION_PLC(3),
    ;

    @EnumValue
    private final int code;

}
