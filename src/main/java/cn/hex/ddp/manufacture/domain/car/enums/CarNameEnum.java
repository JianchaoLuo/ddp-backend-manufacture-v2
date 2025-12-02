package cn.hex.ddp.manufacture.domain.car.enums;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/6/8 0:43
 */
@AllArgsConstructor
@Getter
public enum CarNameEnum implements Serializable {
    /**
     * 默认车辆名称（默认值）
     */
    DEFAULT(0, AreaEnum.DEFAULT),

    /**
     * 出库轨道子车
     */
    OUT_BOUND_SUB_CAR(1, AreaEnum.SOUTH_MOULDING),
    /**
     * 砂箱回流总轨道子车
     */
    SAND_BOX_REFLUX_ALL_SUB_CAR(2, AreaEnum.SOUTH_MOULDING),
    /**
     * 制芯轨道子车
     */
    CORE_MAKING_SUB_CAR(3, AreaEnum.SOUTH_MOULDING),
    /**
     * 模具回流轨道子车
     */
    MOLD_REFLUX_SUB_CAR(4, AreaEnum.SOUTH_MOULDING),
    /**
     * 冷砂回流轨道子车
     */
    COLD_SAND_BOX_REFLUX_SUB_CAR(5, AreaEnum.SOUTH_MOULDING),
    /**
     * 热砂合模轨道子车
     */
    HOT_MOLD_CLOSING_SUB_CAR(6, AreaEnum.SOUTH_MOULDING),
    /**
     * 冷砂合模轨道子车
     */
    COLD_MOLD_CLOSING_SUB_CAR(7, AreaEnum.SOUTH_MOULDING),
    /**
     * 热砂砂箱喷涂完前往合箱子车
     */
    HOT_SPRAY_GO_CLOSING_BOX_SUB_CAR(8, AreaEnum.SOUTH_MOULDING),
    /**
     * 冷砂砂箱喷涂完前往合箱子车
     */
    COLD_SPRAY_GO_CLOSING_BOX_SUB_CAR(9, AreaEnum.SOUTH_MOULDING),
    /**
     * 热砂模具和砂芯模具回流垂直轨道子车
     */
    HOT_MOLD_AND_CORE_MOLD_REFLUX_SUB_CAR(10, AreaEnum.SOUTH_MOULDING),
    /**
     * 热砂砂箱前往喷涂的垂直轨道子车
     */
    HOT_SAND_BOX_GO_SPRAY_SUB_CAR(11, AreaEnum.SOUTH_MOULDING),
    /**
     * 冷砂模具回流垂直轨道
     */
    COLD_MOLD_REFLUX_SUB_CAR(12, AreaEnum.SOUTH_MOULDING),
    /**
     * 冷砂砂箱前往喷涂的垂直轨道子车
     */
    COLD_SAND_BOX_GO_SPRAY_SUB_CAR(13, AreaEnum.SOUTH_MOULDING),
    /**
     * 热砂砂箱合箱轨道子车
     */
    HOT_CLOSING_BOX_SUB_CAR(14, AreaEnum.SOUTH_COMBINE_BOX),
    /**
     * 冷砂砂箱合箱轨道子车
     */
    COLD_CLOSING_BOX_SUB_CAR(15, AreaEnum.SOUTH_COMBINE_BOX),
    /**
     * 合箱浇筑混合轨道子车
     */
    CONSOLIDATION_POURING_SUB_CAR(16, AreaEnum.SOUTH_WAITING_POURING),
    /**
     * 浇筑区轨道下子车
     */
    POURING_DOWN_SUB_CAR(17, AreaEnum.SOUTH_POURING),
    /**
     * 合箱浇筑开箱混合轨道子车
     */
    COOLING_UNBOXING_SUB_CAR(18, AreaEnum.SOUTH_COOLING),
    /**
     * 冷热混合开箱轨道子车
     */
    COLD_AND_HOT_MIXED_UNBOXING_SUB_CAR(19, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 热砂开箱轨道子车
     */
    HOT_SAND_UNBOXING_SUB_CAR(20, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 铸件回流上轨道子车
     */
    CASTING_REFLUX_UP_SUB_CAR(21, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 铸件回流下轨道子车
     */
    CASTING_REFLUX_DOWN_SUB_CAR(22, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 铸件回流垂直轨道子车
     */
    CASTING_REFLUX_VERTICAL_SUB_CAR(23, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 砂箱回流右轨道子车
     */
    SAND_BOX_REFLUX_RIGHT_SUB_CAR(24, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 热砂砂箱回流和砂箱合模子车
     */
    HOT_SAND_BOX_REFLUX_AND_MOLDING_SUB_CAR(25, AreaEnum.SOUTH_MOULDING),
    /**
     * 待浇筑等待下轨道子车
     */
    WAIT_POURING_DOWN_SUB_CAR(26, AreaEnum.SOUTH_WAITING_POURING),
    /**
     * 待浇筑等待上轨道子车
     */
    WAIT_POURING_UP_SUB_CAR(27, AreaEnum.SOUTH_WAITING_POURING),
    /**
     * 冷却区下轨道子车
     */
    COOLING_DOWN_SUB_CAR(28, AreaEnum.SOUTH_COOLING),
    /**
     * 冷却区上轨道子车
     */
    COOLING_UP_SUB_CAR(29, AreaEnum.SOUTH_COOLING),
    /**
     * 浇筑区轨道上子车
     */
    POURING_UP_SUB_CAR(30, AreaEnum.SOUTH_POURING),


    /**
     * 出库轨道母车
     */
    OUT_BOUND_FERRY(100, AreaEnum.SOUTH_MOULDING),
    /**
     * 热砂合模轨道母车
     */
    HOT_MOLD_CLOSING_FERRY(101, AreaEnum.SOUTH_MOULDING),
    /**
     * 冷砂合模轨道母车
     */
    COLD_FERRY_CLOSING_FERRY(102, AreaEnum.SOUTH_MOULDING),
    /**
     * 热砂砂箱喷涂完前往合箱母车
     */
    HOT_SPRAY_GO_CLOSING_BOX_FERRY(103, AreaEnum.SOUTH_MOULDING),
    /**
     * 冷砂砂箱喷涂完前往合箱母车
     */
    COLD_SPRAY_GO_CLOSING_BOX_FERRY(104, AreaEnum.SOUTH_MOULDING),
    /**
     * 热砂砂箱合箱轨道母车
     */
    HOT_CLOSING_BOX_FERRY(105, AreaEnum.SOUTH_COMBINE_BOX),
    /**
     * 冷砂砂箱合箱轨道母车
     */
    COLD_CLOSING_BOX_FERRY(106, AreaEnum.SOUTH_COMBINE_BOX),
    /**
     * 合箱浇筑混合轨道母车
     */
    CONSOLIDATION_POURING_FERRY(107, AreaEnum.SOUTH_WAITING_POURING),
    /**
     * 浇筑区轨道下母车
     */
    POURING_DOWN_FERRY(108, AreaEnum.SOUTH_POURING),
    /**
     * 合箱浇筑开箱混合轨道母车
     */
    COOLING_UNBOXING_FERRY(109, AreaEnum.SOUTH_COOLING),
    /**
     * 冷热混合开箱轨道母车
     */
    COLD_AND_HOT_MIXED_UNBOXING_FERRY(110, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 热砂开箱轨道母车
     */
    HOT_SAND_UNBOXING_FERRY(111, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 浇筑区轨道上母车
     */
    POURING_UP_FERRY(112, AreaEnum.SOUTH_POURING),

    ;

    @EnumValue
    private final Integer code;

    /**
     * 所属区域
     */
    private final AreaEnum area;
}
