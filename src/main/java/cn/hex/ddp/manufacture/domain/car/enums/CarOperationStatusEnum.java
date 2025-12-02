package cn.hex.ddp.manufacture.domain.car.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 车辆运行状态枚举
 *
 * @author Huhaisen
 * @date 2024/04/30
 */
@Getter
@AllArgsConstructor
public enum CarOperationStatusEnum {

    /**
     * 待机
     */
    STANDBY(0),

    /**
     * 运行
     */
    WORKING(1),

    /**
     * 停止
     */
    STOPPING(2),

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
    ;

    @EnumValue
    private final int code;

}
