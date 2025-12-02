package cn.hex.ddp.manufacture.infrastructure.algorithm.pojo;

import cn.hex.ddp.manufacture.domain.path.enums.PathNameEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription: 轨道类，用于在算法中描述轨道的相关信息
 * @Author: KangHong
 * @Created: 2024/5/23 14:36
 */
@Data
@AllArgsConstructor
public class Path implements Serializable {
    /**
     * 轨道id
     */
    private Long id;
    /**
     * 轨道名称
     */
    private String trackName;
    /**
     * 轨道名称的枚举
     */
    private PathNameEnum nameEnum;
    /**
     * 轨道的起点坐标
     */
    private Coordinate startCoordinate;
    /**
     * 轨道的终点坐标
     */
    private Coordinate endCoordinate;
    /**
     * 轨道的节点坐标
     * 可以有，可以无
     */
    private List<Coordinate> nodeCoordinate;
    /**
     * 轨道状态
     */
    private PathStatusEnum status;

    public Path(Long id, String trackName, Coordinate startCoordinate, Coordinate endCoordinate, PathStatusEnum status) {
        this.id = id;
        this.trackName = trackName;
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;
        this.status = status;
    }

    public Path() {
    }
}
