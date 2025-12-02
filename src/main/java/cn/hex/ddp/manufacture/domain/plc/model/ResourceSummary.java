package cn.hex.ddp.manufacture.domain.plc.model;

import lombok.Data;

/**
 * 用于按 PLC 查询到的资源的最小返回信息（id + 类型 + 名称）
 */
@Data
public class ResourceSummary {

    /**
     * 资源ID（全局唯一）
     */
    private Long resourceId;

    /**
     * 资源类型：CAR/SUB_CAR/FERRY_CAR、WORKSTATION、POSITION、EQUIPMENT、PATH
     */
    private String resourceType;

    /**
     * 资源名称
     */
    private String resourceName;
}

