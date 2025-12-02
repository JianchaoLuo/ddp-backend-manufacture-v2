package cn.hex.ddp.manufacture.domain.equipment.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * 设备运行状态枚举
 *
 * @author Huhaisen
 * @date 2024/05/03
 */
@Getter
@AllArgsConstructor
public enum EquipmentOperationStatusEnum implements Serializable {

    /**
     * 待机，空闲
     */
    STANDBY(0),

    /**
     * 工作中
     */
    WORKING(1),
    /**
     * 工作完成
     */
    FINISHED(2),

    /**
     * 急停
     */
    EMERGENCY_STOP(3),

    /**
     * 故障
     */
    BREAKDOWN(4),

    /**
     * 离线
     */
    OFFLINE(5),
    /**
     * 不可用
     */
    UNAVAILABLE(6),
    ;

    @EnumValue
    private final int code;
}
