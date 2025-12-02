package cn.hex.ddp.manufacture.domain.car.model;

import cn.hex.ddp.manufacture.domain.car.enums.CarTypeEnum;
import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * 车辆型号（含行车）
 *
 * @author Huhaisen
 * @date 2024/04/30
 */
@Data
public class CarModel {
    /**
     * 车辆型号ID(数据库ID)
     */
    private Long id;

    /**
     * 车辆型号唯一编号
     */
    private String modelNo;

    /**
     * 车辆类型
     */
    private CarTypeEnum type;

    /**
     * 型号参数
     */
    private List<CarModelParameter> parameters;


    /**
     * 自定义设置车辆型号参数信息
     * 判断参数是否为空，为空则根据车辆类型设置不同的默认参数
     *
     * @param parameters 车辆型号参数
     */
    public void setParameters(List<CarModelParameter> parameters) {
        if (parameters == null || parameters.isEmpty()) {
            if (Objects.equals(type, CarTypeEnum.SUB_CAR)) {
                parameters = List.of(
                        new CarModelParameter("emptySpeed", 0.33, true),
                        new CarModelParameter("fullSpeed", 0.25, true),
                        new CarModelParameter("topRodRaiseOrFallTime", 30D, true)
                );
            } else if (Objects.equals(type, CarTypeEnum.FERRY_CAR)) {
                parameters = List.of(
                        new CarModelParameter("emptySpeed", 0.25, true),
                        new CarModelParameter("fullSpeed", 0.17, true)
                );
            } else {
                parameters = List.of();
            }
        }
        this.parameters = parameters;
    }
}
