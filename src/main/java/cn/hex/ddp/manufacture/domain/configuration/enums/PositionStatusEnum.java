package cn.hex.ddp.manufacture.domain.configuration.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public enum PositionStatusEnum implements Serializable {
    /**
     * 未占用
     */
    UNOCCUPIED,
    /**
     * 已占有
     */
    OCCUPIED,
    /**
     * 不可用
     */
    UNAVAILABLE,
    /**
     * 完成冷却
     */
    FINISH_COOLING,
}
