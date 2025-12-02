package cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input;

import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.mouldingLine.MouldingIndexInput;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/7 21:04
 */
@Data
@AllArgsConstructor
public class MouldingSimulationInput implements Serializable {
    /**
     * 热砂模具和砂芯回流轨道子车
     */
    SubCar hotAndCoreMoldRefluxSubCar;
    /**
     * 砂箱回流轨道子车
     */
    SubCar hotSpraySandBoxSubCar;
    /**
     * 冷砂模具回流轨道子车
     */
    SubCar coldMoldRefluxSubCar;
    /**
     * 冷砂搬运轨道子车
     */
    SubCar coldSandBoxSubCar;
    /**
     * 热砂喷涂前往合箱的母车
     */
    Ferry hotSprayGoClosingBoxferry;
    /**
     * 热砂喷涂前往合箱的子车
     */
    SubCar hotSprayGoClosingBoxSubCar;
    /**
     * 冷砂喷涂前往合箱的母车
     */
    Ferry coldSprayGoClosingBoxferry;
    /**
     * 冷砂喷涂前往合箱的子车
     */
    SubCar coldSprayGoClosingBoxSubCar;
    /**
     * 热砂开模模具放置点位
     */
    Position hotMoldOpeningPosition;
    /**
     * 热砂开模砂箱放置点位1
     */
    Position hotSandBoxOpeningPosition_1;
    /**
     * 热砂开模砂箱放置点位2
     */
    Position hotSandBoxOpeningPosition_2;
    /**
     * 热砂砂箱回流放置点位
     * 可能不存在！！！
     */
    Position hotSandBoxRefluxLayPosition;
    /**
     * 砂芯开模模具放置点位
     */
    Position coreMoldOpeningPosition;
    /**
     * 冷砂开模模具放置点位
     */
    Position coldMoldOpeningPosition;
    /**
     * 冷砂开模砂箱放置点位
     */
    Position coldSandBoxOpeningPosition;
    /**
     * 热砂喷涂下方轨道放置点位
     */
    Position hotSandBoxSprayDownPosition;
    /**
     * 冷砂喷涂上方轨道放置点位
     */
    Position coldSandBoxSprayPosition_1;
    /**
     * 冷砂喷涂下方轨道放置点位
     */
    Position coldSandBoxSprayPosition_2;
    /**
     * 热砂喷涂上1号设备
     */
    Equipment hotSpray_1;
    /**
     * 热砂喷涂上2号设备
     */
    Equipment hotSpray_2;
    /**
     * 热砂喷涂下1号设备
     */
    Equipment hotSpray_3;
    /**
     * 冷砂喷涂上1号设备
     */
    Equipment coldSpray_1;
    /**
     * 冷砂喷涂上2号设备
     */
    Equipment coldSpray_2;
    /**
     * 冷砂喷涂下1号设备
     */
    Equipment coldSpray_3;

    /**
     * 模具回流轨道
     */
    Path moldRefluxPath;
    /**
     * 冷砂模具回流轨道
     */
    Path coldMoldRefluxPath;
    /**
     * 砂箱回流轨道
     */
    Path hotSpraySandBoxPath;
    /**
     * 冷砂前往喷涂轨道
     */
    Path coldSandBoxGoSprayPath;
    /**
     * 热砂喷涂上轨道
     */
    Path hotSprayUpPath;
    /**
     * 热砂喷涂下轨道
     */
    Path hotSprayDownPath;
    /**
     * 冷砂喷涂上轨道
     */
    Path coldSprayUpPath;
    /**
     * 冷砂喷涂下轨道
     */
    Path coldSprayDownPath;
    /**
     * 热砂喷涂结束前往合箱轨道
     */
    Path hotSparyGoClosingBoxPath;
    /**
     * 冷砂喷涂结束前往合箱轨道
     */
    Path coldSparyGoClosingBoxPath;
    /**
     * 造型线的输入标识
     */
    MouldingIndexInput mouldingIndexInput;
    /**
     * 整体的总时间
     */
    double totalTime;
}
