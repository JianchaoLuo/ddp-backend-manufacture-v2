package cn.hex.ddp.manufacture.infrastructure.algorithm.pojo;

import cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription: 小车类，用于在算法中描述小车的详细信息
 * @Author: KangHong
 * @Created: 2024/5/23 17:03
 */
@Data
@AllArgsConstructor
public class SubCar implements Serializable {
    /**
     * 子车id
     */
    private Long id;
    /**
     * 子车名称
     */
    private String name;
    /**
     * 子车名字枚举类
     */
    private CarNameEnum nameEnum;
    /**
     * 子车空载速度
     */
    private double emptySpeed;
    /**
     * 子车满载速度
     */
    private double fullSpeed;
    /**
     * 顶杆升起或降落时间
     */
    private double topRodRaiseOrFallTime;
    /**
     * 子车状态信息
     */
    private List<SubCarStatusEnum> subCarState;
    /**
     * 子车所处的绑定的轨道
     */
    private Path bindPath;
    /**
     * 子车所处的绑定的母车
     */
    private Long bindFerryId;
    /**
     * 子车的当前位置坐标
     */
    private Coordinate locationCoordinate;
    /**
     * 子车上搭载的产品
     */
    private Product product;

    /**
     * 子车上搭载的托盘
     */
    private Tray tray;

    public SubCar() {
    }
}
