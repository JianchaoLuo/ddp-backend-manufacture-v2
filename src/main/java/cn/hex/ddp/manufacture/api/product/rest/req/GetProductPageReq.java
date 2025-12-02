package cn.hex.ddp.manufacture.api.product.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetProductPageReq extends PageReq {
    /**
     * 产品名称(模糊查询,不传不搜索)
     */
    private String name;
    /**
     * 产品型号(模糊查询,不传不搜索)
     */
    private String model;
    /**
     * 产品材质编号(模糊查询,不传不搜索)
     */
    private String materialNo;
}
