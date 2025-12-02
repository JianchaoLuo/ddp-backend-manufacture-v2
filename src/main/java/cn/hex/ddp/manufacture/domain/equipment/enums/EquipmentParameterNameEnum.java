package cn.hex.ddp.manufacture.domain.equipment.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备参数名称枚举
 * 枚举中包含了设备参数的枚举值，设备参数名和默认值，每个枚举就是一个设备参数的定义。
 *
 * @author Huhaisen
 * @date 2024/05/03
 */
@Getter
@AllArgsConstructor
public enum EquipmentParameterNameEnum {

    /** ################# 电炉参数 ################### */

    /**
     *  电炉功率
     */
    ELECTRIC_FURNACE_POWER(0, "电炉功率", 1000.0),

    /**
     *  电炉容量
     */
    ELECTRIC_FURNACE_CAPACITY(1, "电炉容量", 10000.0),

    /**
     *  电炉烧制时长
     */
    ELECTRIC_FURNACE_FIRING_TIME(2, "电炉烧制时长", 10.0),

    /**
     * 设备默认运行时间
     */
    PROCESS_TIME(3, "设备默认运行时间", 5.0),

    // TODO：待完善参数具体数据以及其他参数。
    ;

    @EnumValue
    private final int code;

    private final String name;

    private final Double defaultValue;

}
