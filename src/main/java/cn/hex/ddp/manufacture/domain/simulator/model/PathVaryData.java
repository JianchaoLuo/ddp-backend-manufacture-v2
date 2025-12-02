package cn.hex.ddp.manufacture.domain.simulator.model;

import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.RailDirectionEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import lombok.Data;

/**
 * 路径中可能发生变换的数据，用于仿真
 * @Author: FengZebang
 * @Date: 2025/3/12
 */
@Data
public class    PathVaryData {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 路径名称
     */
    private String name;

    /**
     * 路径长度
     */
    private Double length;

    /**
     * 开始坐标
     */
    private String startCoordinate;

    /**
     * 结束坐标
     */
    private String endCoordinate;

    /**
     * 路径朝向
     */
    private RailDirectionEnum railDirection;

    /**
     * 路径状态
     */
    private PathStatusEnum status;
}
