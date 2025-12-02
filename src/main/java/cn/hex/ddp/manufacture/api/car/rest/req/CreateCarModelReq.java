package cn.hex.ddp.manufacture.api.car.rest.req;

import cn.hex.ddp.manufacture.domain.car.enums.CarTypeEnum;
import cn.hex.ddp.manufacture.domain.car.model.CarModelParameter;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * 添加车型请求参数
 *
 * @author Huhaisen
 * @date 2024/05/16
 */
@Data
public class CreateCarModelReq {

    /**
     * 车辆型号唯一编号
     */
    @NotEmpty(message = "型号编号不能为空")
    @Length(max = 255, message = "型号编号长度不能超过255个字符")
    private String modelNo;

    /**
     * 车辆类型
     */
    @NotNull(message = "车辆类型不能为空")
    private CarTypeEnum type;

    /**
     * 型号参数
     */
    private List<CarModelParameter> parameters;
}
