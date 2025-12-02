package cn.hex.ddp.manufacture.api.equipment.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhaolin
 * @date 2024/5/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ListEquipmentReq extends PageReq {
    /**
     * 设备唯一编号(模糊查询)
     */
    private String equipmentNo;

    /**
     * 设备型号(模糊查询)
     */
    private String model;

    /**
     * 设备名称(模糊查询)
     */
    private String name;

    /**
     * 设备类型(条件查询)
     */
    private EquipmentTypeEnum type;
}
