package cn.hex.ddp.manufacture.api.technique.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchMoltenIronFormulaReq extends PageReq {
    /**
     * 搜索铁水配方型号、铁水配方名称 不传不搜索
     */
    private String search;
}
