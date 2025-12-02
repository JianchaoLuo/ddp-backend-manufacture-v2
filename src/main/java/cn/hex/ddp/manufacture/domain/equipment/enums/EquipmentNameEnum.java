package cn.hex.ddp.manufacture.domain.equipment.enums;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备名称枚举（供算法使用）
 *
 * @author Huhaisen
 * @date 2024/06/22
 */
@AllArgsConstructor
@Getter
public enum EquipmentNameEnum {
    /**
     * 默认设备名称（默认值）
     */
    DEFAULT(0, AreaEnum.DEFAULT),
    /**
     * 热射砂机
     */
    HOT_SAND_BLASTING(1, AreaEnum.SOUTH_MOULDING),
    /**
     * 冷射砂机
     */
    COLD_SAND_BLASTING(2, AreaEnum.SOUTH_MOULDING),
    /**
     * 射芯机
     */
    SANDBOX_SANDBLASTING(3, AreaEnum.SOUTH_MOULDING),
    /**
     * 热砂开模设备
     */
    HOT_SAND_MOLD_OPENING(4, AreaEnum.SOUTH_MOULDING),
    /**
     * 冷砂开模设备
     */
    COLD_SAND_MOLD_OPENING(5, AreaEnum.SOUTH_MOULDING),
    /**
     * 砂芯开模设备
     */
    SAND_CORE_MOLD_OPENING(6, AreaEnum.SOUTH_MOULDING),
    /**
     * 热砂喷涂机1
     */
    HOT_SAND_SPRAYING_1(7, AreaEnum.SOUTH_MOULDING),
    /**
     * 热砂喷涂机2
     */
    HOT_SAND_SPRAYING_2(8, AreaEnum.SOUTH_MOULDING),
    /**
     * 热砂喷涂机3
     */
    HOT_SAND_SPRAYING_3(9, AreaEnum.SOUTH_MOULDING),
    /**
     * 冷砂喷涂机1
     */
    COLD_SAND_SPRAYING_1(11, AreaEnum.SOUTH_MOULDING),
    /**
     * 冷砂喷涂机2
     */
    COLD_SAND_SPRAYING_2(12, AreaEnum.SOUTH_MOULDING),
    /**
     * 冷砂喷涂机3
     */
    COLD_SAND_SPRAYING_3(13, AreaEnum.SOUTH_MOULDING),

    /**
     * 浇筑电炉设备_1
     */
    POURING_ELECTRIC_FURNACE_EQUIPMENT_1(15, AreaEnum.SOUTH_POURING),
    /**
     * 浇筑电炉设备_2
     */
    POURING_ELECTRIC_FURNACE_EQUIPMENT_2(16, AreaEnum.SOUTH_POURING),
    /**
     * 浇筑电炉设备_3
     */
    POURING_ELECTRIC_FURNACE_EQUIPMENT_3(17, AreaEnum.SOUTH_POURING),
    /**
     * 浇筑电炉设备_4
     */
    POURING_ELECTRIC_FURNACE_EQUIPMENT_4(18, AreaEnum.SOUTH_POURING),
    /**
     * 浇筑电炉设备_5
     */
    POURING_ELECTRIC_FURNACE_EQUIPMENT_5(19, AreaEnum.SOUTH_POURING),
    /**
     * 浇筑电炉设备_6
     */
    POURING_ELECTRIC_FURNACE_EQUIPMENT_6(20, AreaEnum.SOUTH_POURING),
    ;

    @EnumValue
    private final int code;

    /**
     * 所属区域
     */
    private final AreaEnum area;

}
