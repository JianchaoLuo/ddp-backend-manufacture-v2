package cn.hex.ddp.manufacture.infrastructure.algorithm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @ClassDescription: 订单中产品的类型
 * @Author: KangHong
 * @Created: 2024/6/2 2:27
 */
@AllArgsConstructor
@Getter
public enum OrderProductTypeEnum implements Serializable {
    /**
     * 热砂
     */
    HOT_SAND(0),
    /**
     * 冷砂
     */
    COLD_SAND(1),
    /**
     * 砂芯
     */
    CORE_SAND(2);
    ;

    private final Integer code;
}
