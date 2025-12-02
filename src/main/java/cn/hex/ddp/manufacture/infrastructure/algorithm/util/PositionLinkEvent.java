package cn.hex.ddp.manufacture.infrastructure.algorithm.util;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription: 点位和事件的关联类，用于表示这个事件的参与者点位的相关状态
 * @Author: KangHong
 * @Created: 2024/5/30 16:47
 */
@AllArgsConstructor
@Data
public class PositionLinkEvent implements Serializable {
    /**
     * 点位id
     */
    private Long positionId;
    /**
     * 事件参与中，点位应该的状态集合
     * 对于点位的状态，不存在多个状态同时存在，所以在List中的状态是或者关系，即满足一个即可
     * 不存在A和B状态的情况
     */
    private List<PositionStatusEnum> positionStatus;
}
