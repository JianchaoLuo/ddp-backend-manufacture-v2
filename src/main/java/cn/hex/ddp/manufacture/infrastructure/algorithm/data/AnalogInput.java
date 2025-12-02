package cn.hex.ddp.manufacture.infrastructure.algorithm.data;

import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Equipment;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.SubCar;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/8/14 10:49
 */
@Data
@AllArgsConstructor
public class AnalogInput implements Serializable {
    SubCar hotMouldSubCar;
    SubCar coreMouldSubCar;
    SubCar coldMouldSubCar;
    SubCar hotUpSpray;
    SubCar hotDownSpray;
    SubCar coldUpSpray;
    SubCar coldDownSpray;
    Equipment hotSandShooting;
    Equipment hotOpenMold;
    Equipment coreSandShooting;
    Equipment coreOpenMold;
    Equipment coldSandShooting;
    Equipment coldOpenMold;
}
