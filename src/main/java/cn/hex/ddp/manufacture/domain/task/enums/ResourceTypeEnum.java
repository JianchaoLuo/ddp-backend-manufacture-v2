package cn.hex.ddp.manufacture.domain.task.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 资源类型枚举
 *
 * @author zhanganqi
 * @date 2025/06/12
 */
@Getter
@AllArgsConstructor
public enum ResourceTypeEnum {
    CAR(1, "车辆资源"),
    EQUIPMENT(2, "设备资源"),
    POSITION(3, "点位资源"),
    WORKSTATION(4, "工岗资源"),
    PATH(5, "路径资源");

    @EnumValue
    private final int code;
    private final String desc;
}