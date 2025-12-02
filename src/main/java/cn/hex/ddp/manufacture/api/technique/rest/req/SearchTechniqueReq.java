package cn.hex.ddp.manufacture.api.technique.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import cn.hex.ddp.manufacture.domain.technique.enums.SandLineTypeEnum;
import cn.hex.ddp.manufacture.domain.technique.enums.SandTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchTechniqueReq extends PageReq {
    /**
     * 搜索工艺编号、工艺名称，不传不搜索
     */
    private String search;

    /**
     * 砂类型，不传不搜索
     */
    private SandTypeEnum sandType;

    /**
     * 砂线类型，不传不搜索
     */
    private SandLineTypeEnum sandLineType;

}
