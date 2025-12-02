package cn.hex.ddp.manufacture.domain.equipment.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备控制状态枚举
 *
 * @author Huhaisen
 * @date 2024/05/03
 */
@Getter
@AllArgsConstructor
public enum EquipmentControlStatusEnum {

    /**
     * 本地控制
     */
    LOCAL_CONTROL(0),

    /**
     * 远程控制
     */
    REMOTE_CONTROL(1),
    ;

    @EnumValue
    private final int code;

}
