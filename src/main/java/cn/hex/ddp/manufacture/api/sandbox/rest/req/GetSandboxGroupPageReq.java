package cn.hex.ddp.manufacture.api.sandbox.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetSandboxGroupPageReq extends PageReq {
    /**
     * 砂箱组型号名称(模糊查询)
     */
    private String name;
}
