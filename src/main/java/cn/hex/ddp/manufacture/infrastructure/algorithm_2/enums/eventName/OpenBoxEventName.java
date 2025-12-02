package cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums.eventName;

import cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums.NodeEventType;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.EventName;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author LiuYongkang
 * @date 2025/2/24
 * @Description 开箱区事件名称枚举
 */
@Getter
@AllArgsConstructor
public enum OpenBoxEventName implements EventName {
    /**
     * 开箱区轨道母车空载前往冷却区点位事件
     */
    OPEN_BOX_FERRY_EMPTY_GO_COOLING_POSITION(0, NodeEventType.EXECUTION_EVENT),

    /**
     * 开箱区轨道母车空载前往冷却区点位完成事件
     */
    OPEN_BOX_FERRY_EMPTY_GO_COOLING_POSITION_FINISHED(1, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷却区点位母车卸载子车事件
     */
    COOLING_POSITION_FERRY_UN_LOAD_SUB_CAR(2, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷却区点位母车卸载子车完成事件
     */
    COOLING_POSITION_FERRY_UN_LOAD_SUB_CAR_FINISHED(3, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷却区点位子车前往冷却区右暂存点位事件
     */
    COOLING_POSITION_SUB_CAR_GO_COOLING_RIGHT_TEMPORARY_POSITION(4, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷却区点位子车前往冷却区右暂存点位完成事件
     */
    COOLING_POSITION_SUB_CAR_GO_COOLING_RIGHT_TEMPORARY_POSITION_FINISHED(5, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷却区子车空载前往冷却区事件
     */
    COOLING_SUB_CAR_EMPTY_GO_COOLING(6, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷却区子车空载前往冷却区完成事件
     */
    COOLING_SUB_CAR_EMPTY_GO_COOLING_FINISHED(7, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷却区子车装载砂箱事件
     */
    COOLING_SUB_CAR_LOAD_SAND_BOX(8, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷却区子车装载砂箱完成事件
     */
    COOLING_SUB_CAR_LOAD_SAND_BOX_FINISHED(9, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷却区子车满载前往冷却区右暂存点位事件
     */
    COOLING_SUB_CAR_FULL_GO_COOLING_RIGHT_TEMPORARY_POSITION(10, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷却区子车满载前往冷却区右暂存点位完成事件
     */
    COOLING_SUB_CAR_FULL_GO_COOLING_RIGHT_TEMPORARY_POSITION_FINISHED(11, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷却区子车卸载砂箱事件
     */
    COOLING_SUB_CAR_UN_LOAD_SAND_BOX(12, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷却区子车卸载砂箱完成事件
     */
    COOLING_SUB_CAR_UN_LOAD_SAND_BOX_FINISHED(13, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷却区点位母车满载前往冷热砂混合轨道点位事件
     */
    COOLING_FERRY_FULL_GO_COLD_AND_HOT_SAND_POSITION(14, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷却区点位母车满载前往冷热砂混合轨道点位完成事件
     */
    COOLING_FERRY_FULL_GO_COLD_AND_HOT_SAND_POSITION_FINISHED(15, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷却区点位母车满载前往热砂混合轨道点位事件
     */
    COOLING_FERRY_FULL_GO_HOT_SAND_POSITION(16, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷却区点位母车满载前往热砂混合轨道点位完成事件
     */
    COOLING_FERRY_FULL_GO_HOT_SAND_POSITION_FINISHED(17, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷热砂混合轨道子车满载前往冷热砂混合轨道点位事件
     */
    COLD_AND_HOT_SAND_SUB_CAR_FULL_GO_COLD_AND_HOT_SAND_POSITION(18, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷热砂混合轨道子车满载前往冷热砂混合轨道点位完成事件
     */
    COLD_AND_HOT_SAND_SUB_CAR_FULL_GO_COLD_AND_HOT_SAND_POSITION_FINISHED(19, NodeEventType.COMPLETE_EVENT),

    /**
     * 热砂混合轨道子车满载前往热砂混合轨道点位事件
     */
    HOT_SAND_SUB_CAR_FULL_GO_HOT_SAND_POSITION(20, NodeEventType.EXECUTION_EVENT),

    /**
     * 热砂混合轨道子车满载前往热砂混合轨道点位完成事件
     */
    HOT_SAND_SUB_CAR_FULL_GO_HOT_SAND_POSITION_FINISHED(21, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷热砂混合轨道点位子车卸载砂箱事件
     */
    COLD_AND_HOT_SAND_POSITION_SUB_CAR_UN_LOAD_SAND_BOX(22, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷热砂混合轨道点位子车卸载砂箱完成事件
     */
    COLD_AND_HOT_SAND_POSITION_SUB_CAR_UN_LOAD_SAND_BOX_FINISHED(23, NodeEventType.COMPLETE_EVENT),

    /**
     * 热砂混合轨道点位子车卸载砂箱事件
     */
    HOT_SAND_POSITION_SUB_CAR_UN_LOAD_SAND_BOX(24, NodeEventType.EXECUTION_EVENT),

    /**
     * 热砂混合轨道点位子车卸载砂箱完成事件
     */
    HOT_SAND_POSITION_SUB_CAR_UN_LOAD_SAND_BOX_FINISHED(25, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷热砂混合轨道点位子车空载返回冷热砂混合轨道事件
     */
    COLD_AND_HOT_SAND_POSITION_GO_COLD_AND_HOT_SAND(26, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷热砂混合轨道点位子车空载返回冷热砂混合轨道完成事件
     */
    COLD_AND_HOT_SAND_POSITION_GO_COLD_AND_HOT_SAND_FINISHED(27, NodeEventType.COMPLETE_EVENT),

    /**
     * 热砂混合轨道点位子车空载返回热砂混合轨道事件
     */
    HOT_SAND_POSITION_GO_HOT_SAND(28, NodeEventType.EXECUTION_EVENT),

    /**
     * 热砂混合轨道点位子车空载返回热砂混合轨道完成事件
     */
    HOT_SAND_POSITION_GO_HOT_SAND_FINISHED(29, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷热混合开箱轨道母车空载前往开箱区点位事件
     */
    COLD_AND_HOT_MIXED_UNBOXING_PATH_FERRY_EMPTY_GO_OPEN_BOX_POSITION(30, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷热混合开箱轨道母车空载前往开箱区点位完成事件
     */
    COLD_AND_HOT_MIXED_UNBOXING_PATH_FERRY_EMPTY_GO_OPEN_BOX_POSITION_FINISHED(31, NodeEventType.COMPLETE_EVENT),

    /**
     * 热砂开箱轨道母车空载前往开箱区点位事件
     */
    HOT_SAND_UNBOXING_PATH_FERRY_EMPTY_GO_OPEN_BOX_POSITION(32, NodeEventType.EXECUTION_EVENT),

    /**
     * 热砂开箱轨道母车空载前往开箱区点位完成事件
     */
    HOT_SAND_UNBOXING_PATH_FERRY_EMPTY_GO_OPEN_BOX_POSITION_FINISHED(33, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷热混合开箱轨道子车空载前往开箱区点位事件
     */
    COLD_AND_HOT_MIXED_UNBOXING_PATH_SUB_CAR_EMPTY_GO_OPEN_BOX_POSITION(34, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷热混合开箱轨道子车空载前往开箱区点位完成事件
     */
    COLD_AND_HOT_MIXED_UNBOXING_PATH_SUB_CAR_EMPTY_GO_OPEN_BOX_POSITION_FINISHED(35, NodeEventType.COMPLETE_EVENT),

    /**
     * 热砂开箱轨道子车空载前往开箱区点位事件
     */
    HOT_SAND_UNBOXING_PATH_SUB_CAR_EMPTY_GO_OPEN_BOX_POSITION(36, NodeEventType.EXECUTION_EVENT),

    /**
     * 热砂开箱轨道子车空载前往开箱区点位完成事件
     */
    HOT_SAND_UNBOXING_PATH_SUB_CAR_EMPTY_GO_OPEN_BOX_POSITION_FINISHED(37, NodeEventType.COMPLETE_EVENT),

    /**
     * 开箱轨道子车装载砂箱事件
     */
    OPEN_BOX_PATH_SUB_CAR_LOAD_SAND_BOX(38, NodeEventType.EXECUTION_EVENT),

    /**
     * 开箱轨道子车装载砂箱完成事件
     */
    OPEN_BOX_PATH_SUB_CAR_LOAD_SAND_BOX_FINISHED(39, NodeEventType.COMPLETE_EVENT),

    /**
     * 开箱区点位子车满载前往冷热混合开箱轨道事件
     */
    OPEN_BOX_POSITION_SUB_CAR_EMPTY_GO_COLD_AND_HOT_MIXED_UNBOXING_PATH(40, NodeEventType.EXECUTION_EVENT),

    /**
     * 开箱区点位子车满载前往冷热混合开箱轨道完成事件
     */
    OPEN_BOX_POSITION_SUB_CAR_EMPTY_GO_COLD_AND_HOT_MIXED_UNBOXING_PATH_FINISHED(41, NodeEventType.COMPLETE_EVENT),

    /**
     * 开箱区点位子车满载前往热砂开箱轨道事件
     */
    OPEN_BOX_POSITION_SUB_CAR_EMPTY_GO_HOT_SAND_UNBOXING_PATH(42, NodeEventType.EXECUTION_EVENT),

    /**
     * 开箱区点位子车满载前往热砂开箱轨道完成事件
     */
    OPEN_BOX_POSITION_SUB_CAR_EMPTY_GO_HOT_SAND_UNBOXING_PATH_FINISHED(43, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷热混合开箱轨道母车满载前往开箱工岗X事件
     */
    COLD_AND_HOT_MIXED_UNBOXING_PATH_FERRY_FULL_GO_OPEN_BOX_STATION_X(44, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷热混合开箱轨道母车满载前往开箱工岗X完成事件
     */
    COLD_AND_HOT_MIXED_UNBOXING_PATH_FERRY_FULL_GO_OPEN_BOX_STATION_X_FINISHED(45, NodeEventType.COMPLETE_EVENT),

    /**
     * 热砂开箱轨道母车满载前往开箱区工岗X事件
     */
    HOT_SAND_UNBOXING_PATH_FERRY_FULL_GO_OPEN_BOX_STATION_X(46, NodeEventType.EXECUTION_EVENT),

    /**
     * 热砂开箱轨道母车满载前往开箱区工岗X完成事件
     */
    HOT_SAND_UNBOXING_PATH_FERRY_FULL_GO_OPEN_BOX_STATION_X_FINISHED(47, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷热混合开箱轨道子车满载前往开箱工岗X事件
     */
    COLD_AND_HOT_MIXED_UNBOXING_PATH_SUB_CAR_FULL_GO_OPEN_BOX_STATION_X(48, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷热混合开箱轨道子车满载前往开箱工岗X完成事件
     */
    COLD_AND_HOT_MIXED_UNBOXING_PATH_SUB_CAR_FULL_GO_OPEN_BOX_STATION_X_FINISHED(49, NodeEventType.COMPLETE_EVENT),

    /**
     * 热砂开箱轨道子车满载前往开箱区工岗X事件
     */
    HOT_SAND_UNBOXING_PATH_SUB_CAR_FULL_GO_OPEN_BOX_STATION_X(50, NodeEventType.EXECUTION_EVENT),

    /**
     * 热砂开箱轨道子车满载前往开箱区工岗X完成事件
     */
    HOT_SAND_UNBOXING_PATH_SUB_CAR_FULL_GO_OPEN_BOX_STATION_X_FINISHED(51, NodeEventType.COMPLETE_EVENT),

    /**
     * 开箱工岗X子车卸载事件
     */
    OPEN_BOX_STATION_X_UN_LOAD(52, NodeEventType.EXECUTION_EVENT),

    /**
     * 开箱工岗X子车卸载完成事件
     */
    OPEN_BOX_STATION_X_UN_LOAD_FINISHED(53, NodeEventType.COMPLETE_EVENT),

    /**
     * 开箱工岗X子车空载前往冷热混合开箱轨道事件
     */
    OPEN_BOX_STATION_X_SUB_CAR_EMPTY_GO_COLD_AND_HOT_MIXED_UNBOXING_PATH(54, NodeEventType.EXECUTION_EVENT),

    /**
     * 开箱工岗X子车空载前往冷热混合开箱轨道完成事件
     */
    OPEN_BOX_STATION_X_SUB_CAR_EMPTY_GO_COLD_AND_HOT_MIXED_UNBOXING_PATH_FINISHED(55, NodeEventType.COMPLETE_EVENT),

    /**
     * 开箱工岗X子车空载前往热砂开箱轨道事件
     */
    OPEN_BOX_STATION_X_SUB_CAR_EMPTY_GO_HOT_SAND_UNBOXING_PATH(56, NodeEventType.EXECUTION_EVENT),

    /**
     * 开箱工岗X子车空载前往热砂开箱轨道完成事件
     */
    OPEN_BOX_STATION_X_SUB_CAR_EMPTY_GO_HOT_SAND_UNBOXING_PATH_FINISHED(57, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷热混合开箱轨道母车空载前往开箱工岗X事件
     */
    COLD_AND_HOT_MIXED_UNBOXING_PATH_FERRY_EMPTY_GO_OPEN_BOX_STATION_X(58, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷热混合开箱轨道母车空载前往开箱工岗X完成事件
     */
    COLD_AND_HOT_MIXED_UNBOXING_PATH_FERRY_EMPTY_GO_OPEN_BOX_STATION_X_FINISHED(59, NodeEventType.COMPLETE_EVENT),

    /**
     * 热砂开箱轨道母车空载前往开箱区工岗X事件
     */
    HOT_SAND_UNBOXING_PATH_FERRY_EMPTY_GO_OPEN_BOX_STATION_X(60, NodeEventType.EXECUTION_EVENT),

    /**
     * 热砂开箱轨道母车空载前往开箱区工岗X完成事件
     */
    HOT_SAND_UNBOXING_PATH_FERRY_EMPTY_GO_OPEN_BOX_STATION_X_FINISHED(61, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷热混合开箱轨道子车空载前往开箱工岗X事件
     */
    COLD_AND_HOT_MIXED_UNBOXING_PATH_SUB_CAR_EMPTY_GO_OPEN_BOX_STATION_X(62, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷热混合开箱轨道子车空载前往开箱工岗X完成事件
     */
    COLD_AND_HOT_MIXED_UNBOXING_PATH_SUB_CAR_EMPTY_GO_OPEN_BOX_STATION_X_FINISHED(63, NodeEventType.COMPLETE_EVENT),

    /**
     * 热砂开箱轨道子车空载前往开箱区工岗X事件
     */
    HOT_SAND_UNBOXING_PATH_SUB_CAR_EMPTY_GO_OPEN_BOX_STATION_X(64, NodeEventType.EXECUTION_EVENT),

    /**
     * 热砂开箱轨道子车空载前往开箱区工岗X完成事件
     */
    HOT_SAND_UNBOXING_PATH_SUB_CAR_EMPTY_GO_OPEN_BOX_STATION_X_FINISHED(65, NodeEventType.COMPLETE_EVENT),

    /**
     * 开箱工岗X子车装载事件
     */
    OPEN_BOX_STATION_X_SUB_CAR_LOAD(66, NodeEventType.EXECUTION_EVENT),

    /**
     * 开箱工岗X子车装载完成事件
     */
    OPEN_BOX_STATION_X_SUB_CAR_LOAD_FINISHED(67, NodeEventType.COMPLETE_EVENT),

    /**
     * 开箱工岗X子车满载前往冷热混合开箱轨道
     */
    OPEN_BOX_STATION_X_SUB_CAR_FULL_GO_COLD_AND_HOT_MIXED_UNBOXING_PATH(68, NodeEventType.EXECUTION_EVENT),

    /**
     * 开箱工岗X子车满载前往冷热混合开箱轨道完成事件
     */
    OPEN_BOX_STATION_X_SUB_CAR_FULL_GO_COLD_AND_HOT_MIXED_UNBOXING_PATH_FINISHED(69, NodeEventType.COMPLETE_EVENT),

    /**
     * 开箱工岗X子车满载前往热砂开箱轨道
     */
    OPEN_BOX_STATION_X_SUB_CAR_FULL_GO_HOT_SAND_UNBOXING_PATH(70, NodeEventType.EXECUTION_EVENT),

    /**
     * 开箱工岗X子车满载前往热砂开箱轨道完成事件
     */
    OPEN_BOX_STATION_X_SUB_CAR_FULL_GO_HOT_SAND_UNBOXING_PATH_FINISHED(71, NodeEventType.COMPLETE_EVENT),

    /**
     * 冷热混合开箱轨道母车满载前往热砂冷砂混合开箱回流点位
     */
    COLD_AND_HOT_MIXED_UNBOXING_PATH_FERRY_FULL_GO_COLD_AND_HOT_MIXED_POSITION(72, NodeEventType.EXECUTION_EVENT),

    /**
     * 冷热混合开箱轨道母车满载前往热砂冷砂混合开箱回流点位完成事件
     */
    COLD_AND_HOT_MIXED_UNBOXING_PATH_FERRY_FULL_GO_COLD_AND_HOT_MIXED_POSITION_FINISHED(73, NodeEventType.COMPLETE_EVENT),

    /**
     * 子车满载前往热砂冷砂混合开箱回流点位
     */
    SUB_CAR_FULL_GO_COLD_AND_HOT_MIXED_POSITION(74, NodeEventType.EXECUTION_EVENT),

    /**
     * 子车满载前往热砂冷砂混合开箱回流点位完成事件
     */
    SUB_CAR_FULL_GO_COLD_AND_HOT_MIXED_POSITION_FINISHED(75, NodeEventType.COMPLETE_EVENT),

    /**
     * 子车空载前往冷热混合开箱轨道
     */
    SUB_CAR_EMPTY_GO_COLD_AND_HOT_MIXED_UNBOXING_PATH(76, NodeEventType.EXECUTION_EVENT),

    /**
     * 子车空载前往冷热混合开箱轨道完成事件
     */
    SUB_CAR_EMPTY_GO_COLD_AND_HOT_MIXED_UNBOXING_PATH_FINISHED(77, NodeEventType.COMPLETE_EVENT),

    /**
     * 热砂冷砂混合开箱回流点位子车卸载事件
     */
    COLD_AND_HOT_MIXED_POSITION_SUB_CAR_UNLOAD(78, NodeEventType.EXECUTION_EVENT),

    /**
     * 热砂冷砂混合开箱回流点位子车卸载完成事件
     */
    COLD_AND_HOT_MIXED_POSITION_SUB_CAR_UNLOAD_FINISHED(79, NodeEventType.COMPLETE_EVENT),

    /**
     * 开箱工岗X工作事件
     */
    OPEN_BOX_STATION_X_SUB_WORKING(80, NodeEventType.EXECUTION_EVENT),

    /**
     * 开箱工岗X工作完成事件
     */
    OPEN_BOX_STATION_X_SUB_WORKING_FINISHED(81, NodeEventType.COMPLETE_EVENT),

    /**
     * 铸件回流上轨道子车空载前往左虚拟点位事件
     */
    CASTING_REFLUX_UP_PATH_SUB_CAR_EMPTY_GO_LEFT_VIRTUAL_POSITION(82, NodeEventType.EXECUTION_EVENT),

    /**
     * 铸件回流上轨道子车空载前往左虚拟点位完成事件
     */
    CASTING_REFLUX_UP_PATH_SUB_CAR_EMPTY_GO_LEFT_VIRTUAL_POSITION_FINISHED(83, NodeEventType.COMPLETE_EVENT),

    /**
     * 左虚拟点位子车进出事件
     */
    LEFT_VIRTUAL_POSITION_SUB_CAR_GO_WAITING(84, NodeEventType.EXECUTION_EVENT),

    /**
     * 左虚拟点位子车进出完成事件
     */
    LEFT_VIRTUAL_POSITION_SUB_CAR_GO_WAITING_FINISHED(85, NodeEventType.COMPLETE_EVENT),

    /**
     * 左铸件回流垂直轨道点位子车装载事件
     */
    LEFT_CASTING_REFLUX_VERTICAL_PATH_POSITION_SUB_CAR_LOAD(86, NodeEventType.EXECUTION_EVENT),

    /**
     * 左铸件回流垂直轨道点位子车装载完成事件
     */
    LEFT_CASTING_REFLUX_VERTICAL_PATH_POSITION_SUB_CAR_LOAD_FINISHED(87, NodeEventType.COMPLETE_EVENT),

    /**
     * 左铸件回流垂直轨道点位子车带托盘前往工岗X事件
     */
    LEFT_CASTING_REFLUX_VERTICAL_PATH_POSITION_SUB_CAR_TRAY_GO_POSITION_X(88, NodeEventType.EXECUTION_EVENT),

    /**
     * 左铸件回流垂直轨道点位子车带托盘前往工岗X完成事件
     */
    LEFT_CASTING_REFLUX_VERTICAL_PATH_POSITION_SUB_CAR_TRAY_GO_POSITION_X_FINISHED(89, NodeEventType.COMPLETE_EVENT),

    /**
     * 工岗X子车带托盘装载事件
     */
    POSITION_X_SUB_CAR_TRAY_LOAD(90, NodeEventType.EXECUTION_EVENT),

    /**
     * 工岗X子车带托盘装载完成事件
     */
    POSITION_X_SUB_CAR_TRAY_LOAD_FINISHED(91, NodeEventType.COMPLETE_EVENT),

    /**
     * 铸件回流上轨道子车满载前往右虚拟点位事件
     */
    CASTING_REFLUX_UP_PATH_SUB_CAR_FULL_GO_RIGHT_VIRTUAL_POSITION(92, NodeEventType.EXECUTION_EVENT),

    /**
     * 铸件回流上轨道子车满载前往右虚拟点位完成事件
     */
    CASTING_REFLUX_UP_PATH_SUB_CAR_FULL_GO_RIGHT_VIRTUAL_POSITION_FINISHED(93, NodeEventType.COMPLETE_EVENT),

    /**
     * 右虚拟点位子车进出事件
     */
    RIGHT_VIRTUAL_POSITION_SUB_CAR_GO_WAITING(94, NodeEventType.EXECUTION_EVENT),

    /**
     * 右虚拟点位子车进出完成事件
     */
    RIGHT_VIRTUAL_POSITION_SUB_CAR_GO_WAITING_FINISHED(95, NodeEventType.COMPLETE_EVENT),

    /**
     * 右铸件回流垂直轨道点位子车卸载事件
     */
    RIGHT_CASTING_REFLUX_VERTICAL_PATH_POSITION_SUB_CAR_UNLOAD(96, NodeEventType.EXECUTION_EVENT),

    /**
     * 右铸件回流垂直轨道点位子车卸载完成事件
     */
    RIGHT_CASTING_REFLUX_VERTICAL_PATH_POSITION_SUB_CAR_UNLOAD_FINISHED(97, NodeEventType.COMPLETE_EVENT),

    /**
     * 右铸件回流垂直轨道点位子车空载前往上虚拟点位事件
     */
    RIGHT_CASTING_REFLUX_VERTICAL_PATH_POSITION_SUB_CAR_GO_VIRTUAL_POSITION(98, NodeEventType.EXECUTION_EVENT),

    /**
     * 右铸件回流垂直轨道点位子车空载前往上虚拟点位完成事件
     */
    RIGHT_CASTING_REFLUX_VERTICAL_PATH_POSITION_SUB_CAR_GO_VIRTUAL_POSITION_FINISHED(99, NodeEventType.COMPLETE_EVENT),

    /**
     * 上虚拟点位子车进出事件
     */
    TOP_VIRTUAL_POSITION_GO_WAITING(100, NodeEventType.EXECUTION_EVENT),

    /**
     * 上虚拟点位子车进出完成事件
     */
    TOP_VIRTUAL_POSITION_GO_WAITING_FINISHED(101, NodeEventType.COMPLETE_EVENT),

    /**
     * 上虚拟点位子车装载事件
     */
    TOP_VIRTUAL_POSITION_LOAD(102, NodeEventType.EXECUTION_EVENT),

    /**
     * 上虚拟点位子车装载完成事件
     */
    TOP_VIRTUAL_POSITION_LOAD_FINISHED(103, NodeEventType.COMPLETE_EVENT),

    /**
     * 右铸件回流垂直轨道点位子车满载前往下虚拟点位事件
     */
    RIGHT_CASTING_REFLUX_VERTICAL_PATH_POSITION_SUB_CAR_FULL_GO_VIRTUAL_POSITION(104, NodeEventType.EXECUTION_EVENT),

    /**
     * 右铸件回流垂直轨道点位子车满载前往下虚拟点位完成事件
     */
    RIGHT_CASTING_REFLUX_VERTICAL_PATH_POSITION_SUB_CAR_FULL_GO_VIRTUAL_POSITION_FINISHED(105, NodeEventType.COMPLETE_EVENT),

    /**
     * 下虚拟点位子车进出事件
     */
    UNDER_VIRTUAL_POSITION_GO_WAITING(106, NodeEventType.EXECUTION_EVENT),

    /**
     * 下虚拟点位子车进出完成事件
     */
    UNDER_VIRTUAL_POSITION_GO_WAITING_FINISHED(107, NodeEventType.COMPLETE_EVENT),

    /**
     * 下虚拟点位子车装载事件
     */
    UNDER_VIRTUAL_POSITION_UNLOAD(108, NodeEventType.EXECUTION_EVENT),

    /**
     * 下虚拟点位子车装载完成事件
     */
    UNDER_VIRTUAL_POSITION_UNLOAD_FINISHED(109, NodeEventType.COMPLETE_EVENT),

    /**
     * 铸件回流下轨道子车空载前往托盘点位事件
     */
    CASTING_REFLUX_DOWN_PATH_SUB_CAR_EMPTY_GO_TRAY_POSITION(110, NodeEventType.EXECUTION_EVENT),

    /**
     * 铸件回流下轨道子车空载前往托盘点位完成事件
     */
    CASTING_REFLUX_DOWN_PATH_SUB_CAR_EMPTY_GO_TRAY_POSITION_FINISHED(111, NodeEventType.COMPLETE_EVENT),

    /**
     * 铸件回流下轨道子车托盘点位装载事件
     */
    CASTING_REFLUX_DOWN_PATH_SUB_CAR_TRAY_POSITION_LOAD(112, NodeEventType.EXECUTION_EVENT),

    /**
     * 铸件回流下轨道子车托盘点位装载完成事件
     */
    CASTING_REFLUX_DOWN_PATH_SUB_CAR_TRAY_POSITION_LOAD_FINISHED(113, NodeEventType.COMPLETE_EVENT),

    /**
     * 托盘点位子车前往左铸件回流垂直轨道左虚拟点位事件
     */
    TRAY_POSITION_SUB_CAR_GO_LEFT_CASTING_REFLUX_VERTICAL_PATH_VIRTUAL(114, NodeEventType.EXECUTION_EVENT),

    /**
     * 托盘点位子车前往左铸件回流垂直轨道左虚拟点位完成事件
     */
    TRAY_POSITION_SUB_CAR_GO_LEFT_CASTING_REFLUX_VERTICAL_PATH_VIRTUAL_FINISHED(115, NodeEventType.COMPLETE_EVENT),

    /**
     * 左铸件回流垂直轨道左虚拟点位子车进事件
     */
    LEFT_CASTING_REFLUX_VERTICAL_PATH_LEFT_VIRTUAL_ENTER(116, NodeEventType.EXECUTION_EVENT),

    /**
     * 左铸件回流垂直轨道左虚拟点位子车进完成事件
     */
    LEFT_CASTING_REFLUX_VERTICAL_PATH_LEFT_VIRTUAL_ENTER_FINISHED(117, NodeEventType.COMPLETE_EVENT),

    /**
     * 左铸件回流垂直轨道左虚拟点位子车出事件
     */
    LEFT_CASTING_REFLUX_VERTICAL_PATH_LEFT_VIRTUAL_GO(118, NodeEventType.EXECUTION_EVENT),

    /**
     * 左铸件回流垂直轨道左虚拟点位子车出完成事件
     */
    LEFT_CASTING_REFLUX_VERTICAL_PATH_LEFT_VIRTUAL_GO_FINISHED(119, NodeEventType.COMPLETE_EVENT),

    /**
     * 左铸件回流垂直轨道下交互点位子车卸载事件
     */
    LEFT_CASTING_REFLUX_VERTICAL_PATH_UNDER_POSITION_UNLOAD(120, NodeEventType.EXECUTION_EVENT),

    /**
     * 左铸件回流垂直轨道下交互点位子车卸载完成事件
     */
    LEFT_CASTING_REFLUX_VERTICAL_PATH_UNDER_POSITION_UNLOAD_FINISHED(121, NodeEventType.COMPLETE_EVENT),

    /**
     * 托盘点位子车前往工岗事件
     */
    TRAY_POSITION_SUB_CAR_GO_STATION(122, NodeEventType.EXECUTION_EVENT),

    /**
     * 托盘点位子车前往工岗完成事件
     */
    TRAY_POSITION_SUB_CAR_GO_STATION_FINISHED(123, NodeEventType.COMPLETE_EVENT),

    /**
     * 铸件回流下轨道子车进入工岗事件
     */
    CASTING_REFLUX_DOWN_PATH_SUB_CAR_GO_STATION(124, NodeEventType.EXECUTION_EVENT),

    /**
     * 铸件回流下轨道子车进入工岗完成事件
     */
    CASTING_REFLUX_DOWN_PATH_SUB_CAR_GO_STATION_FINISHED(125, NodeEventType.COMPLETE_EVENT),

    /**
     * 子车装载铸件事件
     */
    SUB_CAR_LOAD_CASTING_BODY(126, NodeEventType.EXECUTION_EVENT),

    /**
     * 子车装载铸件完成事件
     */
    SUB_CAR_LOAD_CASTING_BODY_FINISHED(127, NodeEventType.COMPLETE_EVENT),

    /**
     * 子车满载返回铸件回流下轨道
     */
    SUB_CAR_FULL_CASTING_REFLUX_DOWN_PATH(128, NodeEventType.EXECUTION_EVENT),

    /**
     * 子车满载返回铸件回流下轨道完成事件
     */
    SUB_CAR_FULL_CASTING_REFLUX_DOWN_PATH_FINISHED(129, NodeEventType.COMPLETE_EVENT),

    /**
     * 铸件回流下轨道子车满载前往铸件回流传输点位左虚拟点位
     */
    CASTING_REFLUX_DOWN_PATH_SUB_CAR_FULL_GO_TRANSMISSION_LEFT_VIRTUAL(130, NodeEventType.EXECUTION_EVENT),

    /**
     * 铸件回流下轨道子车满载前往铸件回流传输点位左虚拟点位完成事件
     */
    CASTING_REFLUX_DOWN_PATH_SUB_CAR_FULL_GO_TRANSMISSION_LEFT_VIRTUAL_FINISHED(131, NodeEventType.COMPLETE_EVENT),

    /**
     * 铸件回流传输点位左虚拟点位子车进入事件
     */
    TRANSMISSION_LEFT_VIRTUAL_SUB_CAR_ENTER(132, NodeEventType.EXECUTION_EVENT),

    /**
     * 铸件回流传输点位左虚拟点位子车进入完成事件
     */
    TRANSMISSION_LEFT_VIRTUAL_SUB_CAR_ENTER_FINISHED(133, NodeEventType.COMPLETE_EVENT),

    /**
     * 铸件回流传输点位左虚拟点位子车出事件
     */
    TRANSMISSION_LEFT_VIRTUAL_SUB_CAR_GO(134, NodeEventType.EXECUTION_EVENT),

    /**
     * 铸件回流传输点位左虚拟点位子车出完成事件
     */
    TRANSMISSION_LEFT_VIRTUAL_SUB_CAR_GO_FINISHED(135, NodeEventType.COMPLETE_EVENT),

    /**
     * 铸件回流传输点子车卸载事件
     */
    TRANSMISSION_LEFT_VIRTUAL_SUB_CAR_UNLOAD(136, NodeEventType.EXECUTION_EVENT),

    /**
     * 铸件回流传输点子车卸载完成事件
     */
    TRANSMISSION_LEFT_VIRTUAL_SUB_CAR_UNLOAD_FINISHED(137, NodeEventType.COMPLETE_EVENT),

    /**
     * 左铸件回流垂直轨道子车空载进入下交互点事件
     */
    LEFT_CASTING_REFLUX_VERTICAL_PATH_SUB_CAR_EMPTY_GO_UNDER_POSITION(138, NodeEventType.EXECUTION_EVENT),

    /**
     * 左铸件回流垂直轨道子车空载进入下交互点完成事件
     */
    LEFT_CASTING_REFLUX_VERTICAL_PATH_SUB_CAR_EMPTY_GO_UNDER_POSITION_FINISHED(139, NodeEventType.COMPLETE_EVENT),

    /**
     * 左铸件回流垂直轨道子车装载托盘事件
     */
    LEFT_CASTING_REFLUX_VERTICAL_PATH_SUB_CAR_LOAD_TRAY(140, NodeEventType.EXECUTION_EVENT),

    /**
     * 左铸件回流垂直轨道子车装载托盘完成事件
     */
    LEFT_CASTING_REFLUX_VERTICAL_PATH_SUB_CAR_LOAD_TRAY_FINISHED(141, NodeEventType.COMPLETE_EVENT),

    /**
     * 左铸件回流垂直轨道子车满载进入上交互点事件
     */
    LEFT_CASTING_REFLUX_VERTICAL_PATH_SUB_CAR_FULL_GO_TOP_POSITION(142, NodeEventType.EXECUTION_EVENT),

    /**
     * 左铸件回流垂直轨道子车满载进入上交互点完成事件
     */
    LEFT_CASTING_REFLUX_VERTICAL_PATH_SUB_CAR_FULL_GO_TOP_POSITION_FINISHED(143, NodeEventType.COMPLETE_EVENT),

    /**
     * 左铸件回流垂直轨道子车卸载托盘事件
     */
    LEFT_CASTING_REFLUX_VERTICAL_PATH_SUB_CAR_UN_LOAD_TRAY(144, NodeEventType.EXECUTION_EVENT),

    /**
     * 左铸件回流垂直轨道子车卸载托盘完成事件
     */
    LEFT_CASTING_REFLUX_VERTICAL_PATH_SUB_CAR_UN_LOAD_TRAY_FINISHED(145, NodeEventType.COMPLETE_EVENT),

    /**
     * 左铸件回流垂直轨道子车返回初始点位事件
     */
    LEFT_CASTING_REFLUX_VERTICAL_PATH_SUB_CAR_GO_INITIAL(146, NodeEventType.EXECUTION_EVENT),

    /**
     * 左铸件回流垂直轨道子车返回初始点位完成事件
     */
    LEFT_CASTING_REFLUX_VERTICAL_PATH_SUB_CAR_GO_INITIAL_FINISHED(147, NodeEventType.COMPLETE_EVENT),

    /**
     * 砂箱回流右轨道子车空载前往热砂冷砂混合开箱回流点位下虚拟点位事件
     */
    SAND_BOX_REFLUX_RIGHT_PATH_SUB_CAR_EMPTY_GO_UNDER_VIRTUAL(148, NodeEventType.EXECUTION_EVENT),

    /**
     * 砂箱回流右轨道子车空载前往热砂冷砂混合开箱回流点位下虚拟点位完成事件
     */
    SAND_BOX_REFLUX_RIGHT_PATH_SUB_CAR_EMPTY_GO_UNDER_VIRTUAL_FINISHED(149, NodeEventType.COMPLETE_EVENT),

    /**
     * 热砂冷砂混合开箱回流点位下虚拟点位子车进事件
     */
    COLD_AND_HOT_MIXED_POSITION_SUB_CAR_ENTER(150, NodeEventType.EXECUTION_EVENT),

    /**
     * 热砂冷砂混合开箱回流点位下虚拟点位子车进完成事件
     */
    COLD_AND_HOT_MIXED_POSITION_SUB_CAR_ENTER_FINISHED(151, NodeEventType.COMPLETE_EVENT),

    /**
     * 热砂冷砂混合开箱回流点位下虚拟点位子车出事件
     */
    COLD_AND_HOT_MIXED_POSITION_SUB_CAR_GO(152, NodeEventType.EXECUTION_EVENT),

    /**
     * 热砂冷砂混合开箱回流点位下虚拟点位子车出完成事件
     */
    COLD_AND_HOT_MIXED_POSITION_SUB_CAR_GO_FINISHED(153, NodeEventType.COMPLETE_EVENT),

    /**
     * 热砂冷砂混合开箱回流点位子车装载事件
     */
    COLD_AND_HOT_MIXED_POSITION_SUB_CAR_LOAD(154, NodeEventType.EXECUTION_EVENT),

    /**
     * 热砂冷砂混合开箱回流点位子车装载完成事件
     */
    COLD_AND_HOT_MIXED_POSITION_SUB_CAR_LOAD_FINISHED(155, NodeEventType.COMPLETE_EVENT),

    /**
     * 砂箱回流右轨道子车满载前往热砂冷砂混合轨道回流开箱交互点事件
     */
    SAND_BOX_REFLUX_RIGHT_PATH_SUB_CAR_FULL_GO_HOT_COLD_UNBOXING_POSITION(156, NodeEventType.EXECUTION_EVENT),

    /**
     * 砂箱回流右轨道子车满载前往热砂冷砂混合轨道回流开箱交互点完成事件
     */
    SAND_BOX_REFLUX_RIGHT_PATH_SUB_CAR_FULL_GO_HOT_COLD_UNBOXING_POSITION_FINISHED(157, NodeEventType.COMPLETE_EVENT),

    /**
     * 热砂合箱轨道母车满载前往右交互点事件
     */
    HOT_CLOSING_BOX_PATH_FERRY_FULL_GO_HOT_RIGHT_POSITION(158, NodeEventType.EXECUTION_EVENT),

    /**
     * 热砂合箱轨道母车满载前往右交互点完成事件
     */
    HOT_CLOSING_BOX_PATH_FERRY_FULL_GO_HOT_RIGHT_POSITION_FINISHED(159, NodeEventType.COMPLETE_EVENT),

    /**
     * 热砂合箱轨道右交互点子车满载前往热砂回流开箱交互点事件
     */
    HOT_CLOSING_BOX_PATH_RIGHT_POSITION_SUB_CAR_GO_HOT_UNBOXING(160, NodeEventType.EXECUTION_EVENT),

    /**
     * 热砂合箱轨道右交互点子车满载前往热砂回流开箱交互点完成事件
     */
    HOT_CLOSING_BOX_PATH_RIGHT_POSITION_SUB_CAR_GO_HOT_UNBOXING_FINISHED(161, NodeEventType.COMPLETE_EVENT),

    /**
     * 热砂回流开箱交互点子车卸载事件
     */
    HOT_UNBOXING_SUB_CAR_UN_LOAD(162, NodeEventType.EXECUTION_EVENT),

    /**
     * 热砂回流开箱交互点子车卸载完成事件
     */
    HOT_UNBOXING_SUB_CAR_UN_LOAD_FINISHED(163, NodeEventType.COMPLETE_EVENT),

    /**
     * 子车空载返回热砂合箱轨道右交互点子车事件
     */
    SUB_CAR_EMPTY_GO_HOT_CLOSING_BOX_PATH_RIGHT_POSITION(164, NodeEventType.EXECUTION_EVENT),

    /**
     * 子车空载返回热砂合箱轨道右交互点子车完成事件
     */
    SUB_CAR_GO_HOT_CLOSING_BOX_PATH_RIGHT_POSITION_FINISHED(165, NodeEventType.COMPLETE_EVENT),
    ;

    private final Integer code;
    private final NodeEventType type;

    @Override
    public void method_x() {
        // 这里可以加入实际的实现代码
    }
}
