package cn.hex.ddp.manufacture.api.path.rest.req;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathNameEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathTypeEnum;
import cn.hex.ddp.manufacture.domain.path.enums.RailDirectionEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class UpdatePathReq {
    /**
     * 路径编号
     */
    @Length(max = 255, message = "路径编号长度不能超过255")
    private String pathNo;

    /**
     * 路径名称
     */
    @Length(max = 255, message = "路径名称长度不能超过255")
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
     * 轨道长度(单位：毫米)
     */
    private Float railLength;

    /**
     * 轨道宽度(单位：毫米)
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
    @Length(max = 512, message = "路径描述长度不能超过512")
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
