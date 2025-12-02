package cn.hex.ddp.manufacture.infrastructure.algorithm.pojo;

import cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription: 母车类，用于在算法中描述母车的详细信息
 * @Author: KangHong
 * @Created: 2024/5/23 17:04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ferry implements Serializable {
    /**
     * 母车的id
     */
    private Long id;
    /**
     * 母车的名称
     */
    private String ferryName;
    /**
     * 母车名字枚举
     */
    private CarNameEnum nameEnum;
    /**
     * 母车空载速度
     */
    private double emptySpeed;
    /**
     * 母车的满载速度
     */
    private double fullSpeed;
    /**
     * 母车的状态信息
     */
    private List<FerryStatusEnum> ferryState;
    /**
     * 母车所处的绑定的轨道
     */
    private Path bindPath;
    /**
     * 母车所处的绑定的子车
     */
    private Long bindSubCarId;
    /**
     * 母车的当前位置坐标
     */
    private Coordinate locationCoordinate;

}
