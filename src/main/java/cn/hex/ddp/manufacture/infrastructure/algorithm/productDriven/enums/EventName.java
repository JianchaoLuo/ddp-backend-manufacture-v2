package cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @ClassDescription: 事件名称枚举
 * @Author: KangHong
 * @Created: 2024/11/9 16:27
 */
@AllArgsConstructor
@Getter
public enum EventName implements Serializable {
    /**
     * 出库事件
     * 我随便写的，具体的事件按照总结的事件名称写
     */
    GO_TO_OUTBOUND_POINT(0),

    //子母车在出库点装载砂箱
    FAA_LOAD_AT_OUTBOUND_POINT(1),

    //前往合模区交互点
    GO_TO_HOT_SAND_MOULDING_POINT(2),

    //子母车在合模区交互点装载砂箱
    FAA_LOAD_AT_HOT_SAND_MOULDING_POINT(3),

    //前往合模工岗
    GO_TO_HOT_SAND_MOLDING_WORK_POSITION(4),

    //进行合模
    CARRY_OUT_HOT_SAND_MOLDING(5),

    //子母车在合模工岗装载砂箱
    FAA_LOAD_AT_HOT_SAND_MOLDING_WORK_POSITION(6),

    //前往射砂垂直轨道口
    GO_TO_HOT_SAND_SHOOTING_VERTICAL_TRACK(7),

    //前往射砂区域
    GO_TO_HOT_SAND_SHOOTING_AREA(8),

    //进行射砂喷涂
    CARRY_OUT_HOT_SAND_SHOOTING_AND_SPRAYING(9),

    //子母车在喷涂设备装载砂箱
    FAA_LOAD_AT_HOT_SAND_SPRAYING_EQUIPMENT(10),

    //前往合箱区交互点
    GO_TO_HOT_SAND_CONSOLIDATION_POINT(11),

    //子母车在合箱区交互点装载砂箱
    FAA_LOAD_AT_HOT_SAND_CONSOLIDATION_POINT(12),

    //前往合箱工岗
    GO_TO_HOT_SAND_CONSOLIDATION_WORK_POSITION(13),

    //进行合箱
    CARRY_OUT_CONSOLIDATION(14),

    //子母车在合箱工岗装载砂箱
    FAA_LOAD_AT_HOT_SAND_CONSOLIDATION_WORK_POSITION(15),

    //前往待浇筑区交互点
    GO_TO_HOT_SAND_POURING_WAITING_POINT(16),

    //子母车在待浇筑区交互点装载砂箱
    FAA_LOAD_AT_HOT_SAND_POURING_WAITING_POINT(17),

    //前往待浇筑轨道口
    GO_TO_POURING_WAITING_TRACK(18),

    //前往待浇筑点
    GO_TO_POURING_WAITING_POINT(19),

    //前往浇筑中转点（需要用到上方8个浇筑工岗）
    GO_TO_POURING_TRANSIT_POINT(20),

    //子母车在浇筑中转点装载砂箱（需要用到上方8个浇筑工岗）
    FAA_LOAD_AT_POURING_TRANSIT_POINT(21),

    //从中转点前往浇筑工岗（需要用到上方8个浇筑工岗）
    GO_TO_POURING_WORK_POSITION_FROM_TRA(22),

    //前往浇筑工岗（需要用到下方4个浇筑工岗）
    GO_TO_POURING_WORK_POSITION(23),

    //进行浇筑
    CARRY_OUT_POURING(24),

    //前往冷却中转点（浇筑点为下4个）
    GO_TO_COOLING_TRANSIT_POINT(25),

    //子母车在冷却中转点装载砂箱（浇筑点为下4个）
    FAA_LOAD_AT_COOLING_TRANSIT_POINT(26),

    //从中转点前往冷却点（浇筑点为下4个）
    GO_TO_COOLING_POINT_FROM_TRA(27),

    //前往冷却点（浇筑点为上8个）
    GO_TO_COOLING_POINT(28),

    //前往开箱区交互点
    GO_TO_HOT_SAND_UNBOXING_POINT(29),

    //子母车在开箱区交互点装载砂箱
    FAA_LOAD_AT_HOTSAND_UNBOXING_POINT(30),

    //前往开箱工岗
    GO_TO_HOTSAND_UNBOXING_WORK_POSITION(31),

    //进行开箱
    CARRY_OUT_HOTSAND_UNBOXING(32),

    //子母车在开箱区工岗装载砂箱
    FAA_LOAD_AT_HOTSAND_UNBOXING_WORK_POSITION(33),

    //前进到上方回流点交互点（在上半开箱）
    GO_TO_REFLUX_TRACK_RIGHT(34),

    //子车在回流点交互点装载砂箱（在上半开箱）
    AGV_LOAD_AT_REFLUX_TRACK_RIGHT(35),

    //前进到右回流轨道中点（在上半开箱）
    GO_TO_REFLUX_TRACK_RIGHT_MIDPOINT(36),

    //子车在回流轨道中点装载砂箱（在上半开箱）
    AGV_LOAD_AT_REFLUX_TRACK_RIGHT_MIDPOINT(37),

    //前进到总回流轨道右交互点（在上半开箱）
    GO_TO_REFLUX_RIGHT_POINT(38),

    //前进到总回流轨道第一交互点（在下半开箱）
    GO_TO_REFLUX_LEFT_POINT(39),

    //子车在总回流轨道左交互点装载砂箱（在下半开箱）
    AGV_LOAD_AT_REFLUX_LEFT_POINT(40),

    //前进到温度监测点
    GO_TO_TEMPERATURE_DETECTION_POINT(41),

    //温度检测
    CARRY_OUT_TEMPERATURE_DETECTION(42),

    //子车在温度监测点装载砂箱
    AGV_LOAD_AT_TEMPERATURE_DETECTION_POINT(43),

    //回到合模工岗（温度较高）
    BACK_TO_HOT_SAND_MOLDING_WORK_POSITION(44),

    //前进到回流轨道暂存点（温度不符合要求）
    GO_TO_REFLUX_TEMP_POINT(45),

    //子母车在回流轨道暂存点装载砂箱（温度不符合要求）
    FAA_LOAD_AT_REFLUX_TEMP_POINT(46),

    //砂箱回库（温度不符合要求）
    BACK_TO_OUTBOUND_POINT(47),
    ;

//    //砂箱回库（温度不符合要求）
//    BACK_TO_OUTBOUND_POINT(47, (I_BACK_TO_OUTBOUND_POINT) (name, age) -> {
//        return  CollUtil.newArrayList(new Event(name,age));
//    });

    private final Integer code;
//    private BaseInterface fun;

//    public List<Event> genEvent(Object... objects) {
//        switch (this){
//            case CARRY_OUT_POURING -> {
//                return CollUtil.newArrayList(new Event());
//            }
//        }
//    }
//
//    static {
//        ((I_BACK_TO_OUTBOUND_POINT) NodeEventName.BACK_TO_OUTBOUND_POINT.getFun()).gen("123","123")
//    }
}
