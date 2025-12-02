package cn.hex.ddp.manufacture.api.workstation.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.common.enums.ProcessEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ListWorkstationReq extends PageReq {
    /**
     * 工位编号(模糊查询, 不传不搜索)
     */
    private String stationNo;

    /**
     * 工位名称(模糊查询, 不传不搜索)
     */
    private String name;

    /**
     * 所属分区(精确查询, 不传不搜索)
     */
    private AreaEnum area;

    /**
     * 所属工序(精确查询, 不传不搜索)
     */
    private ProcessEnum process;
}
