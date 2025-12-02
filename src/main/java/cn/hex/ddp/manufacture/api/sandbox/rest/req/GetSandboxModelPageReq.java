package cn.hex.ddp.manufacture.api.sandbox.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import cn.hex.ddp.manufacture.domain.sandbox.enums.SandboxTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetSandboxModelPageReq extends PageReq {
    /**
     * 砂箱型号名(模糊查询)
     */
    private String modelName;

    /**
     * 砂箱冷热类型(条件查询)
     */
    private SandboxTypeEnum type;
}
