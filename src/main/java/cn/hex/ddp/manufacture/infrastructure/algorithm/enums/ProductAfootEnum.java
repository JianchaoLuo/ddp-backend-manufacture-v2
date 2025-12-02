package cn.hex.ddp.manufacture.infrastructure.algorithm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @ClassDescription: 产品正在进行的加工的枚举表示类
 * @Author: KangHong
 * @Created: 2024/5/27 16:09
 */
@AllArgsConstructor
@Getter
public enum ProductAfootEnum implements Serializable {
    /**
     * 产品前往合模设备
     */
    GOING_MOLD_CLOSING(0),
    /**
     * 产品进行合模加工
     */
    DOING_MOLD_CLOSING(1),
    /**
     * 产品前往射砂设备
     */
    GOING_SANDBLASTING(2),
    /**
     * 产品进行射砂加工
     */
    DOING_SANDBLASTING(3),
    /**
     * 产品前往开模设备
     */
    GOING_MOLD_OPENING(4),
    /**
     * 产品进行开模加工
     */
    DOING_MOLD_OPENING(5),
    /**
     * 产品前往喷涂设备
     */
    GOING_SPRAY(6),
    /**
     * 产品进行喷涂加工
     */
    DOING_SPRAY(7),
    /**
     * 产品前往合箱设备
     */
    GOING_CLOSING_BOX(8),
    /**
     * 产品进行合箱加工
     */
    DOING_CLOSING_BOX(9),
    /**
     * 产品前往浇筑设备
     */
    GOING_POURING(10),
    /**
     * 产品进行浇筑加工
     */
    DOING_POURING(11),
    /**
     * 产品前往冷却位置
     */
    GOING_COOLING(12),
    /**
     * 产品进行冷却处理
     */
    DOING_COOLING(13),
    /**
     * 产品前往开箱设备
     */
    GOING_UNBOXING(14),
    /**
     * 产品进行开箱加工
     */
    DOING_UNBOXING(15),

    /**
     * 产品正在回流
     */
    REFLUXING(16),
    ;

    private final Integer code;
}
