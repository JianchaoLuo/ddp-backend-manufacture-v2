package cn.hex.ddp.manufacture.api.mold.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import cn.hex.ddp.manufacture.domain.mold.enums.MoldTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetMoldModelPageReq extends PageReq {
    /**
     * 模具型号名(模糊查询)
     */
    private String modelName;

    /**
     * 模具类型(精确查询)
     */
    private MoldTypeEnum type;
}
