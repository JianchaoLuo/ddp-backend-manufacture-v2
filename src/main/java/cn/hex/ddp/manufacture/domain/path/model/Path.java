package cn.hex.ddp.manufacture.domain.path.model;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathNameEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathTypeEnum;
import cn.hex.ddp.manufacture.domain.path.enums.RailDirectionEnum;
import lombok.Data;

import java.util.List;

/**
 * 路径聚合模型
 * (不允许拐弯)
 *
 * @author Huhaisen
 * @date 2024/04/28
 */
@Data
public class Path {
    /**
     * 路径ID(数据库ID)
     */
    private Long id;

    /**
     * 路径编号
     */
    private String pathNo;

    /**
     * 路径名称
     */
    private String name;

    /**
     * 开始坐标ID
     */
    private Long startCoordinateId;

    /**
     * 结束坐标ID
     */
    private Long endCoordinateId;

    /**
     * 中点坐标ID
     */
    private Long middleCoordinateId;

    /**
     * 轨道长度
     */
    private Float railLength;

    /**
     * 轨道宽度
     */
    private Float railWidth;

    /**
     * 轨道方向
     */
    private RailDirectionEnum railDirection;

    /**
     * 所属分区
     */
    private AreaEnum area;

    /**
     * 路径类型
     */
    private PathTypeEnum pathType;

    /**
     * 路径描述
     */
    private String description;

    /**
     * 路径状态
     */
    private PathStatusEnum status;


    /**
     * 轨道的节点坐标ID列表
     * 可以有，可以无（主要用于记录立体库出库点位）
     */
    private List<Long> nodeCoordinateIds;

    /**
     * 路径名称枚举(供算法使用)
     */
    private PathNameEnum nameEnum;
}
