package cn.hex.ddp.manufacture.api.plc.rest.vo;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.plc.enums.PLCTypeEnum;
import lombok.Data;

@Data
public class PLCSummaryVO {

    /**
     * PLC的ID(数据库ID)
     */
    private Long id;

    /**
     * ip地址(PLC的IP地址)
     */
    private String ip;

    /**
     * PLC类型
     */
    private PLCTypeEnum type;

    /**
     * 所属区域(一个PLC对应一个区域)
     */
    private AreaEnum area;
}
