package cn.hex.ddp.manufacture.infrastructure.algorithm.enums.moulding;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/12/26 20:15
 */
@AllArgsConstructor
@Getter
public enum MouldingColdGoClosingBoxEnum {
    /**
     * 前往冷砂喷涂点搬运砂箱事件
     */
    GO_COLD_SPARY_EQUIPMENT_LOAD(0),
    /**
     * 前往冷砂合箱点位卸载砂箱
     */
    GO_COLD_CLOSING_BOX_LAND(1),

    ;

    private final Integer code;
}
