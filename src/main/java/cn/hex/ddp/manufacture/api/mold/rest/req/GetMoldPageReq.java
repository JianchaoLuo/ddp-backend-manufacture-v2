package cn.hex.ddp.manufacture.api.mold.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetMoldPageReq extends PageReq {
    /**
     * 模具编号(模糊查询)
     */
    private String moldNo;
    /**
     * 模具名称(模糊查询)
     */
    private String name;
}
