package cn.hex.ddp.manufacture.domain.simulator.model;

import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import lombok.Data;

/**
 * 点位可能变化的数据
 * @Author: FengZebang
 * @Date: 2025/3/11
 */
@Data
public class PositionVaryData {
    /**
     * 点位ID
     */
    private Long id;
    /**
     * 当前状态
     */
    private String currentStatus;
    /**
     * 当前操作
     */
    private String currentAction;
}
