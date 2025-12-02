package cn.hex.ddp.manufacture.domain.car.model;

import lombok.Data;

/**
 * 车辆路径关联
 *
 * @author Huhaisen
 * @date 2024/06/03
 */
@Data
public class CarPath {

    /**
     * 车辆ID
     */
    private Long carId;

    /**
     * 路径ID
     */
    private Long pathId;

}
