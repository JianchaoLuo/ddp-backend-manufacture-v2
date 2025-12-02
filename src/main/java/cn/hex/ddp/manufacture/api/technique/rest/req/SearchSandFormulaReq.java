package cn.hex.ddp.manufacture.api.technique.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchSandFormulaReq extends PageReq {
    /**
     * 根据砂配方型号和砂配方名称模糊搜索 不传不搜索
     */
    private String search;
}
