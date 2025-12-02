package cn.hex.ddp.manufacture.api.path.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathTypeEnum;
import cn.hex.ddp.manufacture.domain.path.enums.RailDirectionEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取路径分页请求参数
 *
 * @author Huhaisen
 * @date 2024/05/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetPathPageReq extends PageReq {
    /**
     * 路径编号(模糊查询)
     */
    private String pathNo;

    /**
     * 路径名称(模糊查询)
     */
    private String name;

    /**
     * 轨道方向(条件查询)
     */
    private RailDirectionEnum railDirection;

    /**
     * 所属分区(条件查询)
     */
    private AreaEnum area;

    /**
     * 路径类型(条件查询)
     */
    private PathTypeEnum pathType;
}
