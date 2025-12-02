package cn.hex.ddp.manufacture.infrastructure.algorithm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @ClassDescription: 产品已经完成的加工的枚举表示类
 * @Author: KangHong
 * @Created: 2024/5/27 16:06
 */
@AllArgsConstructor
@Getter
public enum ProductFinishEnum implements Serializable {
    /**
     * 完成合模加工
     */
    FINISH_MOLD_CLOSING(0),
    /**
     * 完成射砂加工
     */
    FINISH_SANDBLASTING(1),
    /**
     * 完成开模加工
     */
    FINISH_MOLD_OPENING(2),
    /**
     * 完成喷涂加工
     */
    FINISH_SPRAY(3),
    /**
     * 完成合箱加工
     */
    FINISH_CLOSING_BOX(4),
    /**
     * 完成浇筑加工
     */
    FINISH_POURING(5),
    /**
     * 完成冷却加工
     */
    FINISH_COOLING(6),
    /**
     * 完成开箱加工
     */
    FINISH_UNBOXING(7),
    /**
     * 完成产品回流
     */
    FINISH_REFLUXING(8),
    ;

    private final Integer code;
}
