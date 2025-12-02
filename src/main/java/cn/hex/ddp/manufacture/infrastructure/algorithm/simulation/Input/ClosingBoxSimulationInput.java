package cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input;

import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.closingBox.ClosingBoxIndexInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/12 11:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClosingBoxSimulationInput implements Serializable {
    /**
     * 热砂合箱交互点位
     */
    Position hotClosingBoxPosition;
    /**
     * 冷砂合箱交互点位
     */
    Position coldClosingBoxPosition;
    /**
     * 热砂合箱完成前往浇筑点位
     */
    Position hotClosingBoxGoPourPosition;
    /**
     * 冷砂合箱完成前往浇筑点位
     */
    Position coldClosingBoxGoPourPosition;
    /**
     * 热砂合箱轨道母车
     */
    Ferry hotClosingBoxFerry;
    /**
     * 热砂合箱轨道子车
     */
    SubCar hotClosingBoxSubCar;
    /**
     * 冷砂合箱轨道母车
     */
    Ferry coldClosingBoxFerry;
    /**
     * 冷砂合箱轨道子车
     */
    SubCar coldClosingBoxSubCar;
    /**
     * 合箱区工岗列表
     */
    List<WorkStation> workStations;
    /**
     * 热砂合箱轨道
     */
    Path hotClosingBoxPath;
    /**
     * 冷砂合箱轨道
     */
    Path coldClosingBoxPath;
    /**
     * 合箱区的输入标识
     */
    ClosingBoxIndexInput closingBoxIndexInput;
    /**
     * 整体的总时间
     */
    double totalTime;
}
