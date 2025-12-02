package cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/12/7 21:56
 */
@Getter
@AllArgsConstructor
public enum NodeEventName {
    /**
     * 出库区母车前往出库点位事件
     */
    OUT_BOUND_FERRY_EMPTY_GO_POSITION(0, NodeEventType.EXECUTION_EVENT),
    /**
     * 出库区母车前往出库点位完成事件
     */
    OUT_BOUND_FERRY_EMPTY_GO_POSITION_FINISHED(1, NodeEventType.COMPLETE_EVENT),
    ;

    private final Integer code;

    private final NodeEventType type;

}
