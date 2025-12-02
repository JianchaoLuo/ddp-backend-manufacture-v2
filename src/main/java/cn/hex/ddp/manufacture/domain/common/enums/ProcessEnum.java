package cn.hex.ddp.manufacture.domain.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 工序枚举
 *
 * @author Huhaisen
 * @date 2024/05/03
 */
@Getter
@AllArgsConstructor
public enum ProcessEnum {

    /**
     * 合模
     */
    COMBINE_MOLD(0),

    /**
     * 热射砂
     */
    HOT_SHOT_SAND(1),

    /**
     * 冷射砂
     */
    COLD_SHOT_SAND(2),

    /**
     * 开模
     */
    OPEN_MOLD(3),

    /**
     * 砂壳造型检查
     */
    SHELL_MOLDING_CHECK(4),

    /**
     * 喷涂
     */
    SPRAY(5),

    /**
     * 合箱
     */
    COMBINE_BOX(6),

    /**
     * 待浇筑
     */
    WAITING_POURING(7),

    /**
     * 浇筑
     */
    POURING(8),

    /**
     * 冷却
     */
    COOLING(9),

    /**
     * 热砂开箱
     */
    HOT_SAND_OPEN_BOX(10),

    /**
     * 冷砂开箱
     */
    COLD_SAND_OPEN_BOX(11),

    /**
     * 续冷
     */
    AFTER_COOLING(12),

    /**
     * 打磨
     */
    POLISH(13),

    /**
     * 制作砂芯
     */
    CORE_MAKING(14),

    /**
     * 取砂芯
     */
    CORE_FETCH(15),

    /**
     * 砂芯入库
     */
    CORE_STORAGE(16),
    ;

    @EnumValue
    private final int code;

}
