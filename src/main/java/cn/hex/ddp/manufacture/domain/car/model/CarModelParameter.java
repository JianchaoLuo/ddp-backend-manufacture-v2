package cn.hex.ddp.manufacture.domain.car.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车辆型号参数
 *
 * @author Huhaisen
 * @date 2024/06/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarModelParameter {
    /**
     * 参数名称
     */
    private String name;

    /**
     * 参数值
     */
    private Double value;

    /**
     * 是否默认参数
     */
    private Boolean isDefault;
}
