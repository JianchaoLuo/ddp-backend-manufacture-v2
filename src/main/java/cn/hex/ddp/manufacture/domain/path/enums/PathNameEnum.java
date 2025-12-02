package cn.hex.ddp.manufacture.domain.path.enums;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @ClassDescription: 轨道名称的枚举类
 * @Author: KangHong
 * @Created: 2024/6/8 0:02
 */
@AllArgsConstructor
@Getter
public enum PathNameEnum implements Serializable {
    /**
     * 默认轨道名称（默认值）
     */
    DEFAULT(0, AreaEnum.DEFAULT),

    /**
     * 出库轨道
     */
    OUT_BOUND_PATH(1, AreaEnum.SOUTH_MOULDING),
    /**
     * 砂箱回流总轨道
     */
    SAND_BOX_REFLUX_ALL_PATH(2, AreaEnum.SOUTH_MOULDING),
    /**
     * 冷砂回流轨道
     */
    COLD_SAND_BOX_REFLUX_PATH(3, AreaEnum.SOUTH_MOULDING),
    /**
     * 制芯轨道
     */
    CORE_MAKING_PATH(4, AreaEnum.SOUTH_MOULDING),
    /**
     * 模具回流轨道
     */
    MOLD_REFLUX_PATH(5, AreaEnum.SOUTH_MOULDING),
    /**
     * 热砂合模轨道
     */
    HOT_SAND_MOLDING_PATH(6, AreaEnum.SOUTH_MOULDING),
    /**
     * 冷砂合模轨道
     */
    COLD_SAND_MOLDING_PATH(7, AreaEnum.SOUTH_MOULDING),
    /**
     * 热砂和砂芯模具回流垂直轨道
     */
    HOT_MOLD_AND_CORE_MOLD_REFLUX_PATH(8, AreaEnum.SOUTH_MOULDING),
    /**
     * 冷砂模具回流垂直轨道
     */
    COLD_MOLD_REFLUX_PATH(9, AreaEnum.SOUTH_MOULDING),
    /**
     * 热砂砂箱前往喷涂的垂直轨道
     */
    HOT_SAND_BOX_GO_SPRAY_PATH(10, AreaEnum.SOUTH_MOULDING),
    /**
     * 冷砂砂箱前往喷涂的垂直轨道
     */
    COLD_SAND_BOX_GO_SPRAY_PATH(11, AreaEnum.SOUTH_MOULDING),
    /**
     * 热砂喷涂上轨道
     */
    HOT_SPRAY_UP_PATH(12, AreaEnum.SOUTH_MOULDING),
    /**
     * 热砂喷涂下轨道
     */
    HOT_SPRAY_DOWN_PATH(13, AreaEnum.SOUTH_MOULDING),
    /**
     * 冷砂喷涂上轨道
     */
    COLD_SPRAY_UP_PATH(14, AreaEnum.SOUTH_MOULDING),
    /**
     * 冷砂喷涂下轨道
     */
    COLD_SPRAY_DOWN_PATH(15, AreaEnum.SOUTH_MOULDING),
    /**
     * 热砂砂箱喷涂完成前往合箱轨道
     */
    HOT_SPRAY_GO_CLOSING_BOX_PATH(16, AreaEnum.SOUTH_MOULDING),
    /**
     * 冷砂砂箱喷涂完成前往合箱轨道
     */
    COLD_SPRAY_GO_CLOSING_BOX_PATH(17, AreaEnum.SOUTH_MOULDING),
    /**
     * 热砂合箱轨道
     */
    HOT_CLOSING_BOX_PATH(18, AreaEnum.SOUTH_COMBINE_BOX),
    /**
     * 冷砂合箱轨道
     */
    COLD_CLOSING_BOX_PATH(19, AreaEnum.SOUTH_COMBINE_BOX),
    /**
     * 合箱浇筑开箱混合轨道
     */
    CONSOLIDATION_POURING_UNBOXING_PATH(20, AreaEnum.SOUTH_WAITING_POURING),
    /**
     * 待浇筑轨道1
     */
    WAIT_POURING_PATH_1(21, AreaEnum.SOUTH_WAITING_POURING),
    /**
     * 待浇筑轨道2
     */
    WAIT_POURING_PATH_2(22, AreaEnum.SOUTH_WAITING_POURING),
    /**
     * 待浇筑轨道3
     */
    WAIT_POURING_PATH_3(23, AreaEnum.SOUTH_WAITING_POURING),
    /**
     * 待浇筑轨道4
     */
    WAIT_POURING_PATH_4(24, AreaEnum.SOUTH_WAITING_POURING),
    /**
     * 待浇筑轨道5
     */
    WAIT_POURING_PATH_5(25, AreaEnum.SOUTH_WAITING_POURING),
    /**
     * 待浇筑轨道6
     */
    WAIT_POURING_PATH_6(26, AreaEnum.SOUTH_WAITING_POURING),
    /**
     * 待浇筑轨道7
     */
    WAIT_POURING_PATH_7(27, AreaEnum.SOUTH_WAITING_POURING),
    /**
     * 待浇筑轨道8
     */
    WAIT_POURING_PATH_8(28, AreaEnum.SOUTH_WAITING_POURING),
    /**
     * 待浇筑轨道9
     */
    WAIT_POURING_PATH_9(29, AreaEnum.SOUTH_WAITING_POURING),
    /**
     * 待浇筑轨道10
     */
    WAIT_POURING_PATH_10(30, AreaEnum.SOUTH_WAITING_POURING),
    /**
     * 浇筑轨道
     */
    POURING_PATH(31, AreaEnum.SOUTH_POURING),
    /**
     * 冷却轨道_1
     */
    COOLING_PATH_1(32, AreaEnum.SOUTH_COOLING),
    /**
     * 冷却轨道_2
     */
    COOLING_PATH_2(33, AreaEnum.SOUTH_COOLING),
    /**
     * 冷却轨道_3
     */
    COOLING_PATH_3(34, AreaEnum.SOUTH_COOLING),
    /**
     * 冷却轨道_4
     */
    COOLING_PATH_4(35, AreaEnum.SOUTH_COOLING),
    /**
     * 冷却轨道_5
     */
    COOLING_PATH_5(36, AreaEnum.SOUTH_COOLING),
    /**
     * 冷却轨道_6
     */
    COOLING_PATH_6(37, AreaEnum.SOUTH_COOLING),
    /**
     * 冷却轨道_7
     */
    COOLING_PATH_7(38, AreaEnum.SOUTH_COOLING),
    /**
     * 冷却轨道_8
     */
    COOLING_PATH_8(39, AreaEnum.SOUTH_COOLING),
    /**
     * 冷却轨道_9
     */
    COOLING_PATH_9(40, AreaEnum.SOUTH_COOLING),
    /**
     * 冷却轨道_10
     */
    COOLING_PATH_10(41, AreaEnum.SOUTH_COOLING),

    /**
     * 冷热混合开箱轨道
     */
    COLD_AND_HOT_MIXED_UNBOXING_PATH(42, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 热砂开箱轨道
     */
    HOT_SAND_UNBOXING_PATH(43, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 铸件回流上轨道
     */
    CASTING_REFLUX_UP_PATH(44, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 铸件回流下轨道
     */
    CASTING_REFLUX_DOWN_PATH(45, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 铸件回流垂直轨道
     */
    CASTING_REFLUX_VERTICAL_PATH(46, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 砂箱回流左轨道
     */
    SAND_BOX_REFLUX_LEFT_PATH(47, AreaEnum.SOUTH_OPEN_BOX),
    /**
     * 砂箱回流右轨道
     */
    SAND_BOX_REFLUX_RIGHT_PATH(48, AreaEnum.SOUTH_OPEN_BOX),
    ;

    @EnumValue
    private final Integer code;

    /**
     * 所属区域
     */
    private final AreaEnum area;
}
