package cn.hex.ddp.manufacture.domain.plc.model;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.plc.enums.PLCTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * PLC 基本信息 + 资源列表
 */
@Data
public class PlcWithResources {

    /**
     * PLC 主键 ID
     */
    private Long id;

    /**
     * PLC IP 地址
     */
    private String ip;

    /**
     * PLC 类型
     */
    private PLCTypeEnum type;

    /**
     * PLC 所属区域
     */
    private AreaEnum area;

    /**
     * 该 PLC 关联的资源列表
     */
    private List<ResourceSummary> resources;
}

