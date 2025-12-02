package cn.hex.ddp.manufacture.api.configuration.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 分页获取点位信息请求参数
 *
 * @author Huhaisen
 * @date 2024/05/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetPositionPageReq extends PageReq {
    /**
     * 点位名称(模糊查询)
     */
    private String name;
    /**
     * 点位描述(模糊查询)
     */
    private String description;
}
