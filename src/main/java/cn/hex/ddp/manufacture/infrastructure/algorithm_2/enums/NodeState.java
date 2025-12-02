package cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassDescription: 节点状态枚举：用于表示节点的状态：未被扩展，未被选取，已被选取
 * @Author: KangHong
 * @Created: 2024/12/7 22:42
 */
@Getter
@AllArgsConstructor
public enum NodeState {
    /**
     * 未被扩展
     */
    NOT_EXPANDED(0),
    /**
     * 未被选取
     */
    NOT_SELECTED(1),
    /**
     * 已被选取
     */
    SELECTED(2),
    ;

    private final Integer code;
}
