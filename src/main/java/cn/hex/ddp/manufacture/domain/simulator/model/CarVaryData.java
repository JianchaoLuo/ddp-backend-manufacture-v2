package cn.hex.ddp.manufacture.domain.simulator.model;

import cn.hex.ddp.manufacture.domain.car.enums.CarControlStatusEnum;
import cn.hex.ddp.manufacture.domain.car.enums.CarOperationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import lombok.Data;

/**
 * 车辆中可能变化的数据
 * 用于仿真展示
 *
 * @author Huhaisen
 * @date 2024/12/26
 */
@Data
public class CarVaryData {
    /**
     * 车辆ID(数据库ID)
     */
    private Long id;

    /**
     * 车辆运行状态
     */
    private CarOperationStatusEnum carOperationStatus;

//    /**
//     * 车辆类型
//     */
//    private String CarType;

//    /**
//     * 车辆名称
//     */
//    private String carName;

//    /**
//     * 车辆所属区域
//     */
//    private String area;

//    /**
//     * 车辆控制状态
//     */
//    private CarControlStatusEnum controlStatus;

    /**
     * 当前点位
     */
    private String currentCoordinate;

    /**
     * 目标点位
     */
    private String targetCoordinate;

/*    *//**
     * 车辆运行状态
     *//*
    private String operationstatus;*/

    /**
     * 当前操作
     */
    private String currentAction;
}
