package cn.hex.ddp.manufacture.api.productionflow.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryProductionFlowByPageReq extends PageReq {
    /**
     * 生产流程名称(模糊查询)
     */
    private String name;
    /**
     * 生产流程描述(模糊查询)
     */
    private String description;
}
