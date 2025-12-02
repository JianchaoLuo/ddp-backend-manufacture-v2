package cn.hex.ddp.manufacture.domain.equipment.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备类型枚举
 *
 * @author Huhaisen
 * @date 2024/05/03
 */
@Getter
@AllArgsConstructor
public enum EquipmentTypeEnum {

    /**
     * 合模设备
     */
    COMBINE_MOLD_EQUIPMENT(0),

    /**
     * 热射砂机
     */
    HOT_SAND_SHOOTER(1),

    /**
     * 冷射砂机
     */
    COLD_SAND_SHOOTER(2),

    /**
     * 开模设备
     */
    OPEN_MOLD_EQUIPMENT(3),

    /**
     * 射芯机
     */
    CORE_SHOOTER(4),

    /**
     * 喷涂机
     */
    SPRAYING_MACHINE(5),

    /**
     * 合箱机
     */
    COMBINE_BOX_MACHINE(6),

    /**
     * 浇筑机
     */
    POURING_MACHINE(7),

    /**
     * 电炉
     */
    ELECTRIC_FURNACE(8),

    /**
     * 开箱机
     */
    OPEN_BOX_MACHINE(9),
    ;

    @EnumValue
    private final int code;

}
