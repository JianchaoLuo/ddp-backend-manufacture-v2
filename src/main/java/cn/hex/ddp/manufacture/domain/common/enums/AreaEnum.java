package cn.hex.ddp.manufacture.domain.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * 工厂分区枚举
 *
 * @author Huhaisen
 * @date 2024/04/28
 */
@Getter
@AllArgsConstructor
public enum AreaEnum implements Serializable {
    /**
     * 默认分区
     */
    DEFAULT(0),

    /** ###################  南线分区  ###################### */

    /**
     * 南线立体库
     */
    SOUTH_STEREOSCOPIC_STOREHOUSE(1),

    /**
     * 南线造型区
     */
    SOUTH_MOULDING(2),

    /**
     * 南线合箱区
     */
    SOUTH_COMBINE_BOX(3),

    /**
     * 南线待浇筑区
     */
    SOUTH_WAITING_POURING(4),

    /**
     * 南线浇筑区
     */
    SOUTH_POURING(5),

    /**
     * 南线冷却区
     */
    SOUTH_COOLING(6),

    /**
     * 南线开箱区
     */
    SOUTH_OPEN_BOX(7),

    /**
     * 南线续冷区
     */
    SOUTH_AFTER_COOLING(8),

    /**
     * 南线打磨区
     */
    SOUTH_POLISH(9),
    ;

    /** ###################  北线分区  ###################### */

    @EnumValue
    private final int code;

}
