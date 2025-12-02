package cn.hex.ddp.manufacture.api.car.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import cn.hex.ddp.manufacture.domain.car.enums.CarTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获得车型分页请求参数
 *
 * @author Huhaisen
 * @date 2024/05/16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetCarModelPageReq extends PageReq {

    /**
     * 车辆型号唯一编号（模糊查询）
     */
    private String modelNo;

    /**
     * 车辆类型(条件查询)
     */
    private CarTypeEnum type;

}
