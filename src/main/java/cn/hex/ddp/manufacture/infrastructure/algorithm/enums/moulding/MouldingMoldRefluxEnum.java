package cn.hex.ddp.manufacture.infrastructure.algorithm.enums.moulding;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/12/24 20:02
 */
@AllArgsConstructor
@Getter
public enum MouldingMoldRefluxEnum {
    /**
     * 前往制芯开模点位装载
     */
    GO_CORE_MOLD_OPENING_POSITION_LOAD(0),
    /**
     * 前往热砂开模点位装载
     */
    GO_HOT_MOLD_OPENING_POSITION_LOAD(1),
    /**
     * 前往冷砂开模点位装载
     */
    GO_COLD_MOLD_OPENING_POSITION_LOAD(2),
    /**
     * 空载前往等待点位
     */
    EMPTY_GO_WAIT_POSITION(3),
    /**
     * 满载前往等待点位
     */
    FULL_GO_WAIT_POSITION(4),
    /**
     * 满载前往放置模具点位卸载
     */
    FULL_GO_MOLD_LAY_POSITION(5),
    /**
     * 满载前往冷砂等待点
     */
    FULL_GO_COLD_SAND_WAIT_POSITION(6),
    ;

    private final Integer code;
}
