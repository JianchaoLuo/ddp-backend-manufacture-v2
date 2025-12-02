package cn.hex.ddp.manufacture.domain.car.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 车辆控制状态枚举
 *
 * @author Huhaisen
 * @date 2024/04/30
 */
@Getter
@AllArgsConstructor
public enum CarControlStatusEnum {

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
