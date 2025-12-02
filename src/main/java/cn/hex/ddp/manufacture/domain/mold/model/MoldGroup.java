package cn.hex.ddp.manufacture.domain.mold.model;

import cn.hex.ddp.manufacture.domain.mold.enums.MoldTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * 模具组聚合类
 *
 * @author Huhaisen
 * @date 2024/05/04
 */
@Data
public class MoldGroup {
    /**
     * 模具组ID(数据库ID)
     */
    private Long id;

    /**
     * 模具组合型号名称
     */
    private String name;

    /**
     * 模具组合类型
     */
    private MoldTypeEnum type;

    /**
     * 模具组合长
     */
    private Float length;

    /**
     * 模具组合宽
     */
    private Float width;

    /**
     * 模具组合高
     */
    private Float height;

    /**
     * 模具组合重量
     */
    private Float weight;

    /**
     * 模具组合总层数
     */
    private Integer layerCount;

    /**
     * 模具组模具列表
     */
    private List<MoldGroupMold> moldGroupMoldList;

}
