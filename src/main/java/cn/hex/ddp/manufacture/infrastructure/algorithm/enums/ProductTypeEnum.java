package cn.hex.ddp.manufacture.infrastructure.algorithm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @ClassDescription: 产品类型的枚举表示类
 * @Author: KangHong
 * @Created: 2024/5/27 15:37
 */
@AllArgsConstructor
@Getter
public enum ProductTypeEnum implements Serializable {
    /**
     * 上砂箱
     */
    UPPER_BOX(0),
    /**
     * 中砂箱
     */
    MIDDLE_BOX(1),
    /**
     * 下砂箱
     */
    LOWER_BOX(2),
    /**
     * 模具
     */
    MOULD(3),
    /**
     * 砂芯模具
     */
    SAND_MOULD(4),
    /**
     * 砂芯
     */
    SAND_CORE(5),
    /**
     * 上砂壳箱
     */
    UPPER_SAND_SHELL_BOX(6),
    /**
     * 中砂壳箱
     * 待确定是否存在
     */
    MIDDLE_SAND_SHELL_BOX(7),
    /**
     * 下砂壳箱
     */
    LOWER_SAND_SHELL_BOX(8),
    /**
     * 合箱完成的整体砂壳箱
     */
    WHOLE_SAND_SHELL_BOX(9),
    /**
     * 铸件
     */
    CASTING(10),
    /**
     * 无物品，在事件生成时使用
     */
    EMPTY(11);


    private final Integer code;
}
