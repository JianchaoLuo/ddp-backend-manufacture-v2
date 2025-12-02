package cn.hex.ddp.manufacture.api.product.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetProductModelPageReq extends PageReq {
    /**
     * 产品型号名称(模糊查询)
     */
    private String name;
}
