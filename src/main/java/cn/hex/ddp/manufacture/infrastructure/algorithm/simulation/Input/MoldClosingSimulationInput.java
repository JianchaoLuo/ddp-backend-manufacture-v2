package cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input;

import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.moldClosing.MoldClosingIndexInput;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/6/23 14:44
 */
@Data
@AllArgsConstructor
public class MoldClosingSimulationInput implements Serializable {
    /**
     * 热砂合模轨道母车
     */
    Ferry hotMoldClosingFerry;
    /**
     * 冷砂合模轨道母车
     */
    Ferry coldMoldClosingFerry;
    /**
     * 热砂合模轨道子车
     */
    SubCar hotMoldClosingSubcar;
    /**
     * 冷砂合模轨道子车
     */
    SubCar coldMoldClosingSubcar;
    /**
     * 出库区的工岗
     */
    List<WorkStation> workStations;
    /**
     * 热砂合模点位
     */
    Position hotMoldClosingPosition;
    /**
     * 冷砂合模点位
     */
    Position coldMoldClosingPosition;
    /**
     * 热砂合模放置点位
     */
    Position hotMoldClosingLayPosition;
    /**
     * 冷砂合模放置点位
     */
    Position coldMoldClosingLayPosition;
    /**
     * 热砂合模轨道
     */
    Path hotMoldClosingPath;
    /**
     * 冷砂合模轨道
     */
    Path coldMoldClosingPath;
    /**
     * 合模区的输入标识
     */
    MoldClosingIndexInput moldClosingIndexInput;
    /**
     * 整体的总时间
     */
    double totalTime;
}
