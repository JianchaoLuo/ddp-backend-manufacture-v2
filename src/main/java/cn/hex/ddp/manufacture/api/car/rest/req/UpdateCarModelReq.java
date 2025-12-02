package cn.hex.ddp.manufacture.api.car.rest.req;

import cn.hex.ddp.manufacture.domain.car.enums.CarTypeEnum;
import cn.hex.ddp.manufacture.domain.car.model.CarModelParameter;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * 修改车辆型号请求参数
 *
 * @author Huhaisen
 * @date 2024/05/16
 */
@Data
public class UpdateCarModelReq {
    /**
     * 车辆型号唯一编号
     */
    @Length(max = 255, message = "型号编号长度不能超过255个字符")
    private String modelNo;

    /**
     * 车辆类型
     */
    private CarTypeEnum type;

    /**
     * 型号参数
     */
    private List<CarModelParameter> parameters;
}
