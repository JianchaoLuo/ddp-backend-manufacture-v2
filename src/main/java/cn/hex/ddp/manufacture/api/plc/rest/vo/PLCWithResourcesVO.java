package cn.hex.ddp.manufacture.api.plc.rest.vo;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.plc.enums.PLCTypeEnum;
import lombok.Data;

import java.util.List;

@Data
public class PLCWithResourcesVO {
    /**
     * PLC的ID(数据库ID)
     */
    private Long id;

    /**
     * PLC的IP地址
     */
    private String ip;

    /**
     * PLC的类型
     */
    private PLCTypeEnum type;

    /**
     * PLC所属区域
     */
    private AreaEnum area;

    /**
     * PLC关联的资源列表
     * 包含该PLC下的所有资源信息
     */
    private List<PLCResourceVO> resources;
}
