package cn.hex.ddp.manufacture.api.plc.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.plc.enums.PLCTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ListPLCReq extends PageReq {
    /**
     * PLC的IP地址(模糊查询)
     */
    private String ip;

    /**
     * PLC类型(条件查询)
     */
    private PLCTypeEnum type;

    /**
     * 所属区域(条件查询)
     */
    private AreaEnum area;
}
