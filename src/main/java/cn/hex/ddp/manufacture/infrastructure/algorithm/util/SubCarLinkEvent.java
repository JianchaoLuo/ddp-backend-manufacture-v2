package cn.hex.ddp.manufacture.infrastructure.algorithm.util;

import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription: 子车和事件的关联类，用于表示这个事件的参与者子车的相关状态
 * @Author: KangHong
 * @Created: 2024/5/30 12:34
 */
@AllArgsConstructor
@Data
public class SubCarLinkEvent implements Serializable {
    /**
     * 事件参与，子车的id
     */
    private Long id;
    /**
     * 事件参与中，子车应该的状态集合
     * 对于子车的状态，存在多个状态同时存在，所以在List中的所有状态都必须满足
     * 不存在A或者B状态的情况
     */
    private List<SubCarStatusEnum> subCarStatus;
    /**
     * 检查小车当前坐标位置的标识符
     * true表示应该检查子车坐标
     * false表示不用检查子车坐标
     */
    private boolean cpFlag;
    /**
     * 如果要检查子车坐标，则子车应该到达的目标点位
     */
    private Coordinate targetCoordinate;
}
