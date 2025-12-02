package cn.hex.ddp.manufacture.api.sandbox.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetSandboxPageReq extends PageReq {
    /**
     * 砂箱编号(模糊查询)
     */
    private String sandboxNo;
    /**
     * 砂箱名称
     */
    private String name;
}
