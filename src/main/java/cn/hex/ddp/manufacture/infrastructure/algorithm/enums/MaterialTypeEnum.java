package cn.hex.ddp.manufacture.infrastructure.algorithm.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @ClassDescription: 材质类型
 * @Author: KangHong
 * @Created: 2024/6/2 2:19
 */
@AllArgsConstructor
@Getter
public enum MaterialTypeEnum implements Serializable {
    /**
     * 材质A
     */
    A(0),
    /**
     * 材质B
     */
    B(1),
    /**
     * 材质C
     */
    C(2),
    ;

    private final Integer code;
}
