package cn.hex.ddp.manufacture.api.mold.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import cn.hex.ddp.manufacture.domain.mold.enums.MoldTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetMoldGroupPageReq extends PageReq {
    /**
     * 模具组合型号名称(模糊查询)
     */
    private String name;

    /**
     * 模具组合类型(条件查询)
     */
    private MoldTypeEnum type;
}
