package cn.hex.ddp.manufacture.infrastructure.path.persistence.po;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathNameEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathTypeEnum;
import cn.hex.ddp.manufacture.domain.path.enums.RailDirectionEnum;
import cn.hex.ddp.manufacture.infrastructure.common.handlers.JsonListLongTypeHandler;
import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangzc.autotable.annotation.ColumnComment;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.autotable.annotation.UniqueIndex;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 路径PO
 *
 * @author Huhaisen
 * @date 2024/04/30
 */
@EqualsAndHashCode(callSuper = true)
@AutoDefine
@AutoRepository
@Data
@TableName(autoResultMap = true)
@Table(schema = "ddp_manufacture_service_v2", value = "path", comment = "路径表")
public class PathPO extends BaseDatabasePO {

    /**
     * 路径编号
     */
    @ColumnComment("路径编号")
    @Column(length = 255, notNull = true, defaultValue = "''")
    @UniqueIndex
    private String pathNo;

    /**
     * 路径名称
     */
    @Column(comment = "路径名称", length = 255, notNull = true, defaultValue = "''")
    private String name;

    /**
     * 开始坐标ID
     */
    @Column(comment = "开始坐标ID", notNull = true, defaultValue = "0")
    private Long startCoordinateId;

    /**
     * 结束坐标ID
     */
    @Column(comment = "结束坐标ID", notNull = true, defaultValue = "0")
    private Long endCoordinateId;

    /**
     * 中点坐标ID
     */
    @Column(comment = "中点坐标ID", notNull = true, defaultValue = "0")
    private Long middleCoordinateId;

    /**
     * 轨道长度
     */
    @Column(comment = "轨道长度", notNull = true, defaultValue = "0")
    private Float railLength;

    /**
     * 轨道宽度
     */
    @Column(comment = "轨道宽度", notNull = true, defaultValue = "0")
    private Float railWidth;

    /**
     * 轨道方向
     */
    @Column(comment = "轨道方向", notNull = true, defaultValue = "0")
    private RailDirectionEnum railDirection;

    /**
     * 所属分区
     */
    @Column(comment = "所属分区", notNull = true, defaultValue = "0")
    private AreaEnum area;

    /**
     * 路径类型
     */
    @Column(comment = "路径类型", notNull = true, defaultValue = "0")
    private PathTypeEnum pathType;

    /**
     * 路径描述
     */
    @Column(length = 512, comment = "路径描述", notNull = true, defaultValue = "''")
    private String description;

    /**
     * 路径状态
     */
    @Column(comment = "路径状态", notNull = true, defaultValue = "0")
    private PathStatusEnum status;

    /**
     * 轨道的节点坐标ID列表
     * 可以有，可以无（主要用于记录立体库出库点位）
     */
    @TableField(typeHandler = JsonListLongTypeHandler.class)
    @Column(type = "JSONB", comment = "轨道的节点坐标ID列表", notNull = true, defaultValue = "'[]'")
    private List<Long> nodeCoordinateIds;

    /**
     * 路径名称枚举(供算法使用)
     */
    @Column(comment = "路径名称枚举(供算法使用)", notNull = true, defaultValue = "0")
    private PathNameEnum nameEnum;

//    /**
//     * 关键点位
//     */
//    private List<Long> keyPositionIds;
}
