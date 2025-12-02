package cn.hex.ddp.manufacture.api.plc.rest.vo;

import cn.hex.ddp.manufacture.domain.plc.enums.PLCTypeEnum;
import cn.hex.ddp.manufacture.domain.plc.model.PLC;
import lombok.Data;

@Data
public class PLCResourceVO {
    /**
     * 资源ID
     */
    private Long resourceId;

    /**
     * 资源类型
     */
    private String resourceType;

    /**
     * 资源名称
     */
    private String resourceName;
}

