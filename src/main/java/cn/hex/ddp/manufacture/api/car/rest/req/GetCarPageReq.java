package cn.hex.ddp.manufacture.api.car.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import cn.hex.ddp.manufacture.domain.car.enums.CarTypeEnum;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.path.enums.RailDirectionEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获得车辆分页请求参数
 *
 * @author Huhaisen
 * @date 2024/05/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetCarPageReq extends PageReq {
    /**
     * 车辆唯一编号(模糊查询)
     */
    private String carNo;

    /**
     * 车辆名称（模糊查询）
     */
    private String name;

    /**
     * 车辆类型（条件查询）
     */
    private CarTypeEnum type;

    /**
     * 所属区域（条件查询）
     */
    private AreaEnum area;

    /**
     * 车头朝向（条件查询）
     */
    private RailDirectionEnum headDirection;
}
