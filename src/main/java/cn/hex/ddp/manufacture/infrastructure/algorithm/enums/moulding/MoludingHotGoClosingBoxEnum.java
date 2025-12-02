package cn.hex.ddp.manufacture.infrastructure.algorithm.enums.moulding;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/12/26 18:08
 */
@AllArgsConstructor
@Getter
public enum MoludingHotGoClosingBoxEnum {
    /**
     * 前往热砂喷涂点搬运砂箱事件
     */
    GO_HOT_SPARY_EQUIPMENT_LOAD(0),
    /**
     * 前往热砂合箱点位卸载砂箱
     */
    GO_HOT_CLOSING_BOX_LAND(1),

    ;

    private final Integer code;
}
