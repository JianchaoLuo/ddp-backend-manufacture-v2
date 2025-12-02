package cn.hex.ddp.manufacture.domain.workstation.enums;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * 工岗名称枚举（供算法使用）
 *
 * @author Huhaisen
 * @date 2024/06/22
 */
@Getter
@AllArgsConstructor
public enum WorkstationNameEnum implements Serializable {

    /**
     * 默认工岗（默认值）
     */
    DEFAULT(0, AreaEnum.DEFAULT),
    /**
     * 热砂合模工岗1
     */
    HOT_MOLD_CLOSING_WORKSTATION_1(1, AreaEnum.SOUTH_MOULDING),
    /**
     * 热砂合模工岗2
     */
    HOT_MOLD_CLOSING_WORKSTATION_2(2, AreaEnum.SOUTH_MOULDING),
    /**
     * 热砂合模工岗3
     */
    HOT_MOLD_CLOSING_WORKSTATION_3(3, AreaEnum.SOUTH_MOULDING),
    /**
     * 热砂合模工岗4
     */
    HOT_MOLD_CLOSING_WORKSTATION_4(4, AreaEnum.SOUTH_MOULDING),
    /**
     * 冷砂合模工岗1
     */
    COLD_MOLD_CLOSING_WORKSTATION_1(5, AreaEnum.SOUTH_MOULDING),
    /**
     * 冷砂合模工岗2
     */
    COLD_MOLD_CLOSING_WORKSTATION_2(6, AreaEnum.SOUTH_MOULDING),
    /**
     * 冷砂合模工岗3
     */
    COLD_MOLD_CLOSING_WORKSTATION_3(7, AreaEnum.SOUTH_MOULDING),
    /**
     * 冷砂合模工岗4
     */
    COLD_MOLD_CLOSING_WORKSTATION_4(8, AreaEnum.SOUTH_MOULDING),
    /**
     * 热砂合箱工岗1
     */
    HOT_CLOSING_BOX_WORKSTATION_1(9, AreaEnum.SOUTH_COMBINE_BOX),
    /**
     * 热砂合箱工岗2
     */
    HOT_CLOSING_BOX_WORKSTATION_2(10, AreaEnum.SOUTH_COMBINE_BOX),
    /**
     * 热砂合箱工岗3
     */
    HOT_CLOSING_BOX_WORKSTATION_3(11, AreaEnum.SOUTH_COMBINE_BOX),
    /**
     * 热砂合箱工岗4
     */
    HOT_CLOSING_BOX_WORKSTATION_4(12, AreaEnum.SOUTH_COMBINE_BOX),
    /**
     * 冷砂合箱工岗1
     */
    COLD_CLOSING_BOX_WORKSTATION_1(13, AreaEnum.SOUTH_COMBINE_BOX),
    /**
     * 冷砂合箱工岗2
     */
    COLD_CLOSING_BOX_WORKSTATION_2(14, AreaEnum.SOUTH_COMBINE_BOX),
    /**
     * 冷砂合箱工岗3
     */
    COLD_CLOSING_BOX_WORKSTATION_3(15, AreaEnum.SOUTH_COMBINE_BOX),
    /**
     * 冷砂合箱工岗4
     */
    COLD_CLOSING_BOX_WORKSTATION_4(16, AreaEnum.SOUTH_COMBINE_BOX),
    /**
     * 浇筑工岗1
     */
    POURING_WORKSTATION_15T_1(17, AreaEnum.SOUTH_POURING),
    /**
     * 浇筑工岗2
     */
    POURING_WORKSTATION_15T_2(18, AreaEnum.SOUTH_POURING),
    /**
     * 浇筑工岗3
     */
    POURING_WORKSTATION_15T_3(19, AreaEnum.SOUTH_POURING),
    /**
     * 浇筑工岗4
     */
    POURING_WORKSTATION_15T_4(20, AreaEnum.SOUTH_POURING),
    /**
     * 浇筑工岗5
     */
    POURING_WORKSTATION_30T_1(21, AreaEnum.SOUTH_POURING),
    /**
     * 浇筑工岗6
     */
    POURING_WORKSTATION_30T_2(22, AreaEnum.SOUTH_POURING),
    /**
     * 浇筑工岗7
     */
    POURING_WORKSTATION_30T_3(23, AreaEnum.SOUTH_POURING),
    /**
     * 浇筑工岗8
     */
    POURING_WORKSTATION_30T_4(24, AreaEnum.SOUTH_POURING),
    /**
     * 浇筑工岗9
     */
    POURING_WORKSTATION_30T_5(25, AreaEnum.SOUTH_POURING),
    /**
     * 浇筑工岗10
     */
    POURING_WORKSTATION_30T_6(26, AreaEnum.SOUTH_POURING),
    /**
     * 浇筑工岗11
     */
    POURING_WORKSTATION_30T_7(27, AreaEnum.SOUTH_POURING),
    /**
     * 浇筑工岗12
     */
    POURING_WORKSTATION_30T_8(28, AreaEnum.SOUTH_POURING),
    /**
     * 冷热混合开箱工岗1
     */
    COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_1(29, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 冷热混合开箱工岗2
     */
    COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_2(30, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 冷热混合开箱工岗3
     */
    COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_3(31, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 冷热混合开箱工岗4
     */
    COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_4(32, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 热砂开箱工岗1
     */
    HOT_SAND_UNBOXING_WORKSTATION_1(33, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 热砂开箱工岗2
     */
    HOT_SAND_UNBOXING_WORKSTATION_2(34, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 热砂开箱工岗3
     */
    HOT_SAND_UNBOXING_WORKSTATION_3(35, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 热砂开箱工岗4
     */
    HOT_SAND_UNBOXING_WORKSTATION_4(36, AreaEnum.SOUTH_OPEN_BOX),

    //以下是2024.12.25以后修改新增的工岗枚举
    /**
     * 热砂开箱工岗5
     */
    HOT_SAND_UNBOXING_WORKSTATION_5(37, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 热砂开箱工岗6
     */
    HOT_SAND_UNBOXING_WORKSTATION_6(38, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 热砂开箱工岗7
     */
    HOT_SAND_UNBOXING_WORKSTATION_7(39, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 热砂开箱工岗8
     */
    HOT_SAND_UNBOXING_WORKSTATION_8(40, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 冷热混合开箱工岗5
     */
    COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_5(41, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 冷热混合开箱工岗6
     */
    COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_6(42, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 冷热混合开箱工岗7
     */
    COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_7(43, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 冷热混合开箱工岗8
     */
    COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_8(44, AreaEnum.SOUTH_OPEN_BOX),

    ;

    @EnumValue
    private final int code;

    /**
     * 所属区域
     */
    private final AreaEnum area;
}
