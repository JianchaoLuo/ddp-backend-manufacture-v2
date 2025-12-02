package cn.hex.ddp.manufacture.infrastructure.algorithm.enums.moulding;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/12/26 16:47
 */
@AllArgsConstructor
@Getter
public enum MouldingColdGoSprayEnum {
    /**
     * 前往冷砂开模砂箱点位装载
     */
    GO_COLD_SAND_OPENING_POSITION_LOAD(0),
    /**
     * 空载前往等待点位事件
     */
    EMPTY_GO_WAIT_POSITION(1),
    /**
     * 满载前往冷砂喷涂点卸载
     */
    GO_COLD_SPARY_POSITION_LANDING(3),
    /**
     * 满载前往等待点位事件
     */
    FULL_GO_WAIT_POSITION(4),

    ;

    private final Integer code;
}
