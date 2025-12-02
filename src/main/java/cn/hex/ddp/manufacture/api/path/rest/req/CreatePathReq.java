package cn.hex.ddp.manufacture.api.path.rest.req;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathNameEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathTypeEnum;
import cn.hex.ddp.manufacture.domain.path.enums.RailDirectionEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * 创建路径请求参数
 *
 * @author Huhaisen
 * @date 2024/05/15
 */
@Data
public class CreatePathReq {
    /**
     * 路径编号
     */
    @NotEmpty(message = "路径编号不能为空")
    @Length(max = 255, message = "路径编号长度不能超过255")
    private String pathNo;

    /**
     * 路径名称
     */
    @NotEmpty(message = "路径名称不能为空")
    @Length(max = 255, message = "路径名称长度不能超过255")
    private String name;

    /**
     * 开始坐标ID
     */
    @NotNull(message = "开始坐标ID不能为空")
    private Long startCoordinateId;

    /**
     * 结束坐标ID
     */
    @NotNull(message = "结束坐标ID不能为空")
    private Long endCoordinateId;

    /**
     * 中点坐标ID
     */
    @NotNull(message = "中点坐标ID不能为空")
    private Long middleCoordinateId;

    /**
     * 轨道长度(单位：毫米)
     */
    @NotNull(message = "轨道长度不能为空")
    private Float railLength;

    /**
     * 轨道宽度(单位：毫米)
     */
    @NotNull(message = "轨道宽度不能为空")
    private Float railWidth;

    /**
     * 轨道方向
     */
    @NotNull(message = "轨道方向不能为空")
    private RailDirectionEnum railDirection;

    /**
     * 所属分区
     */
    @NotNull(message = "所属分区不能为空")
    private AreaEnum area;

    /**
     * 路径类型
     */
    @NotNull(message = "路径类型不能为空")
    private PathTypeEnum pathType;

    /**
     * 路径描述
     */
    @NotEmpty(message = "路径描述不能为空")
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
