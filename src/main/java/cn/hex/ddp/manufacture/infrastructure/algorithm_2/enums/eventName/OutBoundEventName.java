package cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums.eventName;

import cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums.NodeEventType;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.EventName;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2025/2/21 15:41
 */
@Getter
@AllArgsConstructor
public enum OutBoundEventName implements EventName {
    /**
     * 出库区母车前往出库点位事件
     */
    OUT_BOUND_FERRY_EMPTY_GO_POSITION(0, NodeEventType.EXECUTION_EVENT),
    /**
     * 出库区母车前往出库点位完成事件
     */
    OUT_BOUND_FERRY_EMPTY_GO_POSITION_FINISHED(1, NodeEventType.COMPLETE_EVENT),
    /**
     * 出库区子车空载进入出库点位事件
     */
    OUT_BOUND_SUB_CAR_EMPTY_INTO_OUT_POSITION(2, NodeEventType.EXECUTION_EVENT),
    /**
     * 出库区子车空载进入出库点位完成事件
     */
    OUT_BOUND_SUB_CAR_EMPTY_INTO_OUT_POSITION_FINISHED(3, NodeEventType.COMPLETE_EVENT),
    /**
     * 出库区子车在出库点装载事件
     */
    OUT_BOUND_SUB_CAR_IN_OUT_POSITION_LOAD(4, NodeEventType.EXECUTION_EVENT),
    /**
     * 出库区子车在出库点装载完成事件
     */
    OUT_BOUND_SUB_CAR_IN_OUT_POSITION_LOAD_FINISHED(5, NodeEventType.COMPLETE_EVENT),

    ;

    private final Integer code;

    private final NodeEventType type;

    @Override
    public void method_x() {
    }
}
