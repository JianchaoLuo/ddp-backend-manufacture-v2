package cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input;

import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.openBox.OpenBoxIndexInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/8/1 0:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenBoxSimulationInput implements Serializable {
    /**
     * 冷热混合开箱轨道母车
     */
    Ferry coldAndHotFerry;
    /**
     * 冷热混合开箱轨道子车
     */
    SubCar coldAndHotSubCar;
    /**
     * 热砂开箱轨道母车
     */
    Ferry hotFerry;
    /**
     * 热砂开箱轨道子车
     */
    SubCar hotSubCar;
    /**
     * 铸件回流上轨道子车
     */
    SubCar castingUpSubCar;
    /**
     * 铸件回流下轨道子车
     */
    SubCar castingDownSubCar;
    /**
     * 铸件回流垂直轨道子车
     */
    SubCar castingVerticalSubCar;
    /**
     * 砂箱回流右轨道子车
     */
    SubCar sandBoxRefluxRightSubCar;
    /**
     * 热砂冷砂混合开箱点位
     */
    Position coldAndHotOpenBoxPosition;
    /**
     * 热砂开箱点位
     */
    Position hotOpenBoxPosition;
    /**
     * 热砂冷砂混合开箱回流点位
     */
    Position coldAndHotRefluxPosition;
    /**
     * 铸件回流上点位
     */
    Position castingUpPosition;
    /**
     * 铸件回流下点位
     */
    Position castingDownPosition;
    /**
     * 铸件回流垂直点位
     */
    Position castingVerticalPosition;
    /**
     * 铸件回流传输点位
     */
    Position castingShippingPosition;
    /**
     * 开箱区工岗列表
     */
    List<WorkStation> workStations;
    /**
     * 冷热混合开箱轨道
     */
    Path coldAndHotOpenBoxPath;
    /**
     * 热砂开箱轨道
     */
    Path hotOpenBoxPath;
    /**
     * 铸件回流上轨道
     */
    Path castingUpPath;
    /**
     * 铸件回流下轨道
     */
    Path castingDownPath;
    /**
     * 铸件回流垂直轨道
     */
    Path castingVerticalPath;
    /**
     * 砂箱回流左轨道
     */
    Path sandBoxRefluxLeftPath;
    /**
     * 砂箱回流右轨道
     */
    Path sandBoxRefluxRightPath;
    /**
     * 开箱区的输入标识
     */
    OpenBoxIndexInput openBoxIndexInput;
    /**
     * 整体的总时间
     */
    double totalTime;
}
