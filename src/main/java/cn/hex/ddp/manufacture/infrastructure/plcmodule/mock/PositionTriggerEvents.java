package cn.hex.ddp.manufacture.infrastructure.plcmodule.mock;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import cn.hex.ddp.manufacture.domain.configuration.manager.ConfigurationManager;
import cn.hex.ddp.manufacture.domain.configuration.model.Position;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentNameEnum;
import cn.hex.ddp.manufacture.domain.equipment.manager.EquipmentManager;
import cn.hex.ddp.manufacture.domain.equipment.model.Equipment;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.*;
import cn.hex.ddp.manufacture.infrastructure.common.converter.AlgorithmDataConverter;
import cn.hex.ddp.manufacture.infrastructure.common.spring.SpringContextUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟由工厂控制的区域中可能的事件，目的是为了给模拟器，让模拟器mock数据
 * 【点位状态变化触发的事件集合】
 */
@Data
public class PositionTriggerEvents {

    private final EquipmentManager equipmentManager;

    private final ConfigurationManager configurationManager;

    /**
     * 热射砂事件集合
     */
    private List<Event> hotShootingEvents;
    /**
     * 射芯事件集合
     */
    private List<Event> coreShootingEvents;
    /**
     * 冷射砂事件集合
     */
    private List<Event> coldShootingEvents;
    /**
     * 热砂喷涂上事件集合
     */
    private List<Event> hotSprayUpEvents;
    /**
     * 热砂喷涂下事件集合
     */
    private List<Event> hotSprayDownEvents;
    /**
     * 冷砂喷涂上事件集合
     */
    private List<Event> coldSprayUpEvents;
    /**
     * 冷砂喷涂下事件集合
     */
    private List<Event> coldSprayDownEvents;
    /**
     * 铸件回流传输点位事件集合
     */
    private List<Event> castingRefluxPositionEvents;
    /**
     * 冷砂行车放置交互点位事件集合
     */
    private List<Event> coldSandRowCarPositionEvents;

    // 热砂射砂放置点位
    private Coordinate hotSandShootingPosition;
    // 热射砂机位置
    private Coordinate hotSandBlasting;
    // 热砂开模设备位置
    private Coordinate hotSandMoldOpening;
    // 热砂模具开模模具放置点位
    private Coordinate hotMoldOpeningMoldLayPosition;
    // 热砂模具开模砂箱放置点位
    private Coordinate hotMoldOpeningSandBoxLayPosition;

    // 制芯轨道放置的交互点
    private Coordinate coreMakingLayPosition;
    // 射芯机位置
    private Coordinate sandboxSandblasting;
    // 砂芯开模设备位置
    private Coordinate sandCoreMoldOpening;
    // 砂芯模具开模砂箱放置点位
    private Coordinate coreMoldOpeningMoldLayPosition;

    // 冷砂射砂放置点位
    private Coordinate coldSandShootingPosition;
    // 冷射砂机位置
    private Coordinate coldSandBlasting;
    // 冷砂开模设备位置
    private Coordinate coldSandMoldOpening;
    // 冷砂模具开模模具放置点位
    private Coordinate coldMoldOpeningMoldLayPosition;
    // 冷砂模具开模砂箱放置点位
    private Coordinate coldMoldOpeningSandBoxLayPosition;

    // 热砂喷涂机1位置
    private Coordinate hotSandSprayingUp1;

    // 热砂砂箱前往喷涂的下方点位
    private Coordinate hotSandBoxGoSprayDownPosition;
    // 热砂喷涂机3位置
    private Coordinate hotSandSprayingDown1;

    // 冷砂砂箱前往喷涂的上方点位
    private Coordinate coldSandBoxGoSprayUpPosition;
    // 冷砂喷涂机1位置
    private Coordinate coldSandSprayingUp1;

    // 冷砂砂箱前往喷涂的下方点位
    private Coordinate coldSandBoxGoSprayDownPosition;
    // 冷砂喷涂机3位置
    private Coordinate coldSandSprayingDown1;

    // 铸件回流传输点位
    private Position castingRefluxShippingPosition;

    // 冷砂行车放置交互点
    private Position coldSandRowCarPosition;
    // 砂箱回流行车放置点
    private Position sandBoxRowCarPosition;

    public PositionTriggerEvents() {
        this.equipmentManager = SpringContextUtil.getBean(EquipmentManager.class);
        this.configurationManager = SpringContextUtil.getBean(ConfigurationManager.class);
        initEvents();
    }

    public void initEvents() {
        initConfigInfo();
        this.hotShootingEvents = initHotShootingEvents();
        this.coreShootingEvents = initCoreShootingEvents();
        this.coldShootingEvents = initColdShootingEvents();
        this.hotSprayUpEvents = initHotSprayUpEvents();
        this.hotSprayDownEvents = initHotSprayDownEvents();
        this.coldSprayUpEvents = initColdSprayUpEvents();
        this.coldSprayDownEvents = initColdSprayDownEvents();
        this.castingRefluxPositionEvents = initCastingRefluxPositionEvents();
        this.coldSandRowCarPositionEvents = initColdSandRowCarPositionEvents();
    }

    // 初始化位置坐标配置信息，从数据库中查询
    // 注意：这里如果有坐标数据，需要进行比例尺转换
    private void initConfigInfo() {
        this.hotSandShootingPosition = AlgorithmDataConverter.toCoordinate(
                configurationManager.getPositionByNameEnum(PositionNameEnum.HOT_SAND_SHOOTING_POSITION).getCoordinate());
        this.hotSandBlasting = AlgorithmDataConverter.toCoordinate(
                getEquipmentCoordinate(EquipmentNameEnum.HOT_SAND_BLASTING));
        this.hotSandMoldOpening = AlgorithmDataConverter.toCoordinate(
                getEquipmentCoordinate(EquipmentNameEnum.HOT_SAND_MOLD_OPENING));
        this.hotMoldOpeningMoldLayPosition = AlgorithmDataConverter.toCoordinate(configurationManager
                .getPositionByNameEnum(PositionNameEnum.HOT_MOLD_OPENING_MOLD_LAY_POSITION).getCoordinate());
        this.hotMoldOpeningSandBoxLayPosition = AlgorithmDataConverter.toCoordinate(configurationManager
                .getPositionByNameEnum(PositionNameEnum.HOT_MOLD_OPENING_SAND_BOX_LAY_POSITION).getCoordinate());

        this.coreMakingLayPosition = AlgorithmDataConverter.toCoordinate(
                configurationManager.getPositionByNameEnum(PositionNameEnum.CORE_MAKING_LAY_POSITION).getCoordinate());
        this.sandboxSandblasting = AlgorithmDataConverter.toCoordinate(
                getEquipmentCoordinate(EquipmentNameEnum.SANDBOX_SANDBLASTING));
        this.sandCoreMoldOpening = AlgorithmDataConverter.toCoordinate(
                getEquipmentCoordinate(EquipmentNameEnum.SAND_CORE_MOLD_OPENING));
        this.coreMoldOpeningMoldLayPosition = AlgorithmDataConverter.toCoordinate(configurationManager
                .getPositionByNameEnum(PositionNameEnum.CORE_MOLD_OPENING_MOLD_LAY_POSITION).getCoordinate());

        this.coldSandShootingPosition = AlgorithmDataConverter.toCoordinate(configurationManager
                .getPositionByNameEnum(PositionNameEnum.COLD_SAND_SHOOTING_POSITION).getCoordinate());
        this.coldSandBlasting = AlgorithmDataConverter.toCoordinate(
                getEquipmentCoordinate(EquipmentNameEnum.COLD_SAND_BLASTING));
        this.coldSandMoldOpening = AlgorithmDataConverter.toCoordinate(
                getEquipmentCoordinate(EquipmentNameEnum.COLD_SAND_MOLD_OPENING));
        this.coldMoldOpeningMoldLayPosition = AlgorithmDataConverter.toCoordinate(configurationManager
                .getPositionByNameEnum(PositionNameEnum.COLD_MOLD_OPENING_MOLD_LAY_POSITION).getCoordinate());
        this.coldMoldOpeningSandBoxLayPosition = AlgorithmDataConverter.toCoordinate(configurationManager
                .getPositionByNameEnum(PositionNameEnum.COLD_MOLD_OPENING_SAND_BOX_LAY_POSITION).getCoordinate());

        this.hotSandSprayingUp1 = AlgorithmDataConverter.toCoordinate(
                getEquipmentCoordinate(EquipmentNameEnum.HOT_SAND_SPRAYING_1));

        this.hotSandBoxGoSprayDownPosition = AlgorithmDataConverter.toCoordinate(configurationManager
                .getPositionByNameEnum(PositionNameEnum.HOT_SAND_BOX_GO_SPRAY_DOWN_POSITION).getCoordinate());
        this.hotSandSprayingDown1 = AlgorithmDataConverter.toCoordinate(
                getEquipmentCoordinate(EquipmentNameEnum.HOT_SAND_SPRAYING_3));

        this.coldSandBoxGoSprayUpPosition = AlgorithmDataConverter.toCoordinate(configurationManager
                .getPositionByNameEnum(PositionNameEnum.COLD_SAND_BOX_GO_SPRAY_POSITION_1).getCoordinate());
        this.coldSandSprayingUp1 = AlgorithmDataConverter.toCoordinate(
                getEquipmentCoordinate(EquipmentNameEnum.COLD_SAND_SPRAYING_1));

        this.coldSandBoxGoSprayDownPosition = AlgorithmDataConverter.toCoordinate(configurationManager
                .getPositionByNameEnum(PositionNameEnum.COLD_SAND_BOX_GO_SPRAY_POSITION_2).getCoordinate());
        this.coldSandSprayingDown1 = AlgorithmDataConverter.toCoordinate(
                getEquipmentCoordinate(EquipmentNameEnum.COLD_SAND_SPRAYING_3));

        this.castingRefluxShippingPosition = configurationManager.getPositionByNameEnum(
                PositionNameEnum.CASTING_REFLUX_SHIPPING_POSITION);

        this.coldSandRowCarPosition = configurationManager.getPositionByNameEnum(
                PositionNameEnum.COLD_SAND_ROW_CAR_POSITION);
        this.sandBoxRowCarPosition = configurationManager.getPositionByNameEnum(
                PositionNameEnum.SAND_BOX_ROW_CAR_POSITION);
    }

    // 根据设备名字枚举，获取设备的坐标
    private cn.hex.ddp.manufacture.domain.configuration.model.Coordinate getEquipmentCoordinate(
            EquipmentNameEnum equipmentName) {
        Equipment equipment = equipmentManager.getEquipmentByNameEnum(equipmentName);
        return configurationManager.getCoordinateById(equipment.getCoordinateId());
    }

    /**
     * 获取新的事件对象，把一些通用的属性设置好
     */
    private Event getNewEvent() {
        Event event = new Event();
        event.setCompleteFlag(false);
        event.setOccurringFlag(false);
        return event;
    }

    /**
     * 初始化铸件回流传输点位事件集合
     */
    private List<Event> initCastingRefluxPositionEvents() {
        ArrayList<Event> events = new ArrayList<>();
        // 铸件回流传输点位
        Event event1 = getNewEvent();
        event1.setInstruction(new Instruction(castingRefluxShippingPosition.getId(), ExecuteTypeEnum.POSITION,
                InstructionsEnum.NOTICE, null));
        events.add(event1);
        return events;
    }

    /**
     * 初始化冷砂行车放置交互点位事件集合
     */
    private List<Event> initColdSandRowCarPositionEvents() {
        ArrayList<Event> events = new ArrayList<>();
        // 砂箱回流行车放置点
        Event event1 = getNewEvent();
        event1.setInstruction(new Instruction(sandBoxRowCarPosition.getId(), ExecuteTypeEnum.POSITION,
                InstructionsEnum.NOTICE, null));
        events.add(event1);
        // 冷砂行车放置交互点位
        Event event2 = getNewEvent();
        event2.setInstruction(new Instruction(coldSandRowCarPosition.getId(), ExecuteTypeEnum.POSITION,
                InstructionsEnum.NOTICE, null));
        events.add(event2);
        return events;
    }

    /**
     * 初始化热砂射砂放置点位事件集合
     * 注意：这里如果有坐标数据，需要进行比例尺转换
     */
    private List<Event> initHotShootingEvents() {
        ArrayList<Event> events = new ArrayList<>();
        // 车辆到热砂射砂放置点位去
        Event event1 = getNewEvent();
        event1.setInstruction(new Instruction(1806898891212197890L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(hotSandShootingPosition.getX(),
                hotSandShootingPosition.getY())));
        events.add(event1);
        // 车辆执行装载货物事件
        Event event2 = getNewEvent();
        event2.setStartPositionEvent(new PositionLinkEvent(1796864626629672962L, null));
        event2.setInstruction(new Instruction(1806898891212197890L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RISING, null));
        events.add(event2);

        // 车辆将东西运走，运到热射砂机位置
        Event event3 = getNewEvent();
        event3.setInstruction(new Instruction(1806898891212197890L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(hotSandBlasting.getX(), hotSandBlasting.getY())));
        events.add(event3);
        // 车辆执行卸载货物事件
        Event event4 = getNewEvent();
        event4.setStartEquipmentEvent(new EquipmentLinkEvent(1796862790497665025L, null));
        event4.setInstruction(new Instruction(1806898891212197890L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.LANDING, null));
        events.add(event4);
        // 车辆卸完货让他离开热射砂机位置
        Event event5 = getNewEvent();
        event5.setInstruction(new Instruction(1806898891212197890L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(hotSandBlasting.getX() +
                60 * AlgorithmDataConverter.PLOTTING_SCALE, hotSandBlasting.getY())));
        events.add(event5);

        // 给热射砂机发送通知事件
        Event event6 = getNewEvent();
        event6.setInstruction(new Instruction(1796862790497665025L, ExecuteTypeEnum.EQUIPMENT,
                InstructionsEnum.NOTICE, null));
        events.add(event6);

        // 通知车辆移动到热射砂机位置
        Event event7 = getNewEvent();
        event7.setInstruction(new Instruction(1806898891212197890L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(hotSandBlasting.getX(), hotSandBlasting.getY())));
        events.add(event7);
        // 车辆执行装载货物事件
        Event event8 = getNewEvent();
        event8.setStartEquipmentEvent(new EquipmentLinkEvent(1796862790497665025L, null));
        event8.setInstruction(new Instruction(1806898891212197890L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RISING, null));
        events.add(event8);

        // 车辆移动到开模设备位置
        Event event9 = getNewEvent();
        event9.setInstruction(new Instruction(1806898891212197890L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(hotSandMoldOpening.getX(), hotSandMoldOpening.getY())));
        events.add(event9);
        // 车辆卸载货物事件
        Event event10 = getNewEvent();
        event10.setStartEquipmentEvent(new EquipmentLinkEvent(1796862790678020098L, null));
        event10.setInstruction(new Instruction(1806898891212197890L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.LANDING, null));
        events.add(event10);
        // 车辆离开开模设备位置
        Event event11 = getNewEvent();
        event11.setInstruction(new Instruction(1806898891212197890L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(hotSandMoldOpening.getX() +
                60 * AlgorithmDataConverter.PLOTTING_SCALE, hotSandMoldOpening.getY())));
        events.add(event11);

        // 给开模设备发送通知事件
        Event event12 = getNewEvent();
        event12.setInstruction(new Instruction(1796862790678020098L, ExecuteTypeEnum.EQUIPMENT,
                InstructionsEnum.NOTICE, null));
        events.add(event12);

        // 通知车辆移动到开模设备位置
        Event event13 = getNewEvent();
        event13.setInstruction(new Instruction(1806898891212197890L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(hotSandMoldOpening.getX(), hotSandMoldOpening.getY())));
        events.add(event13);
        // 车辆执行装载货物事件
        Event event14 = getNewEvent();
        event14.setStartEquipmentEvent(new EquipmentLinkEvent(1796862790678020098L, null));
        event14.setInstruction(new Instruction(1806898891212197890L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RISING, null));
        events.add(event14);

        // 车辆移动到热砂模具开模砂箱放置点位
        Event event15 = getNewEvent();
        event15.setInstruction(new Instruction(1806898891212197890L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(hotMoldOpeningSandBoxLayPosition.getX(),
                hotMoldOpeningSandBoxLayPosition.getY())));
        events.add(event15);
        // 车辆卸载货物事件
        Event event16 = getNewEvent();
        event16.setStartPositionEvent(new PositionLinkEvent(1796864627162349570L, null));
        event16.setInstruction(new Instruction(1806898891212197890L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.LANDING, null));
        events.add(event16);

        // 车辆移动到开模设备位置
        Event event17 = getNewEvent();
        event17.setInstruction(new Instruction(1806898891212197890L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(hotSandMoldOpening.getX(), hotSandMoldOpening.getY())));
        events.add(event17);
        // 车辆执行装载货物事件 (可以没有)
        Event event18 = getNewEvent();
        event18.setStartEquipmentEvent(new EquipmentLinkEvent(1796862790678020098L, null));
        event18.setInstruction(new Instruction(1806898891212197890L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RISING, null));
        events.add(event18);

        // 车辆移动到热砂模具开模模具放置点位
        Event event19 = getNewEvent();
        event19.setInstruction(new Instruction(1806898891212197890L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(hotMoldOpeningMoldLayPosition.getX(),
                hotMoldOpeningMoldLayPosition.getY())));
        events.add(event19);
        // 车辆卸载货物事件
        Event event20 = getNewEvent();
        event20.setStartPositionEvent(new PositionLinkEvent(1796864626763890689L, null));
        event20.setInstruction(new Instruction(1806898891212197890L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.LANDING, null));
        events.add(event20);
        // 车辆卸完货让他离开热砂模具开模模具放置点
        Event event21 = getNewEvent();
        event21.setInstruction(new Instruction(1806898891212197890L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(hotSandMoldOpening.getX() +
                60 * AlgorithmDataConverter.PLOTTING_SCALE, hotSandMoldOpening.getY())));
        events.add(event21);
        return events;
    }

    /**
     * 初始化射芯事件集合
     * 注意：这里如果有坐标数据，需要进行比例尺转换
     */
    private List<Event> initCoreShootingEvents() {
        ArrayList<Event> events = new ArrayList<>();
        // 车辆到制芯轨道放置的交互点位去
        // 注意：这里制芯轨道放置的交互点位跟车辆所在路径不在同一水平位置，因此指令的目的地坐标y不能直接使用交互点位坐标
        Event event1 = getNewEvent();
        event1.setInstruction(new Instruction(1817456665160683522L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(coreMakingLayPosition.getX(), sandboxSandblasting.getY())));
        events.add(event1);
        // 车辆执行装载货物事件
        Event event2 = getNewEvent();
        event2.setStartPositionEvent(new PositionLinkEvent(1806223074272018433L, null));
        event2.setInstruction(new Instruction(1817456665160683522L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RISING, null));
        events.add(event2);

        // 车辆将东西运走，运到射芯机位置
        Event event3 = getNewEvent();
        event3.setInstruction(new Instruction(1817456665160683522L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(sandboxSandblasting.getX(), sandboxSandblasting.getY())));
        events.add(event3);
        // 车辆执行卸载货物事件
        Event event4 = getNewEvent();
        event4.setStartEquipmentEvent(new EquipmentLinkEvent(1796862790610911234L, null));
        event4.setInstruction(new Instruction(1817456665160683522L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.LANDING, null));
        events.add(event4);
        // 车辆卸完货让他离开射芯机位置
        Event event5 = getNewEvent();
        event5.setInstruction(new Instruction(1817456665160683522L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(sandboxSandblasting.getX() -
                60 * AlgorithmDataConverter.PLOTTING_SCALE, sandboxSandblasting.getY())));
        events.add(event5);

        // 给射芯机发送通知事件
        Event event6 = getNewEvent();
        event6.setInstruction(new Instruction(1796862790610911234L, ExecuteTypeEnum.EQUIPMENT,
                InstructionsEnum.NOTICE, null));
        events.add(event6);

        // 通知车辆移动到射芯机位置
        Event event7 = getNewEvent();
        event7.setInstruction(new Instruction(1817456665160683522L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(sandboxSandblasting.getX(), sandboxSandblasting.getY())));
        events.add(event7);
        // 车辆执行装载货物事件
        Event event8 = getNewEvent();
        event8.setStartEquipmentEvent(new EquipmentLinkEvent(1796862790610911234L, null));
        event8.setInstruction(new Instruction(1817456665160683522L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RISING, null));
        events.add(event8);

        // 车辆移动到开模设备位置
        Event event9 = getNewEvent();
        event9.setInstruction(new Instruction(1817456665160683522L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(sandCoreMoldOpening.getX(), sandCoreMoldOpening.getY())));
        events.add(event9);
        // 车辆卸载货物事件
        Event event10 = getNewEvent();
        event10.setStartEquipmentEvent(new EquipmentLinkEvent(1796862790812237825L, null));
        event10.setInstruction(new Instruction(1817456665160683522L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.LANDING, null));
        events.add(event10);
        // 车辆离开开模设备位置
        Event event11 = getNewEvent();
        event11.setInstruction(new Instruction(1817456665160683522L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(sandCoreMoldOpening.getX() -
                60 * AlgorithmDataConverter.PLOTTING_SCALE , sandCoreMoldOpening.getY())));
        events.add(event11);

        // 给开模设备发送通知事件
        Event event12 = getNewEvent();
        event12.setInstruction(new Instruction(1796862790812237825L, ExecuteTypeEnum.EQUIPMENT,
                InstructionsEnum.NOTICE, null));
        events.add(event12);

        // 通知车辆移动到开模设备位置
        Event event13 = getNewEvent();
        event13.setInstruction(new Instruction(1817456665160683522L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(sandCoreMoldOpening.getX(), sandCoreMoldOpening.getY())));
        events.add(event13);
        // 车辆执行装载货物事件
        Event event14 = getNewEvent();
        event14.setStartEquipmentEvent(new EquipmentLinkEvent(1796862790812237825L, null));
        event14.setInstruction(new Instruction(1817456665160683522L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RISING, null));
        events.add(event14);

        // 车辆移动到砂芯模具开模砂箱放置点位
        Event event15 = getNewEvent();
        event15.setInstruction(new Instruction(1817456665160683522L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(coreMoldOpeningMoldLayPosition.getX(),
                coreMoldOpeningMoldLayPosition.getY())));
        events.add(event15);
        // 车辆卸载货物事件
        Event event16 = getNewEvent();
        event16.setStartPositionEvent(new PositionLinkEvent(1796864626826805250L, null));
        event16.setInstruction(new Instruction(1817456665160683522L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.LANDING, null));
        events.add(event16);
        // 车辆卸完货让他离开砂芯模具开模砂箱放置点位
        Event event17 = getNewEvent();
        event17.setInstruction(new Instruction(1817456665160683522L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(sandCoreMoldOpening.getX() -
                60 * AlgorithmDataConverter.PLOTTING_SCALE , sandCoreMoldOpening.getY())));
        events.add(event17);
        return events;
    }

    /**
     * 初始化冷砂射砂放置点位事件集合
     * 注意：这里如果有坐标数据，需要进行比例尺转换
     */
    private List<Event> initColdShootingEvents() {
        ArrayList<Event> events = new ArrayList<>();
        // 车辆到冷砂射砂放置点位去
        Event event1 = getNewEvent();
        event1.setInstruction(new Instruction(1817456988612825089L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(coldSandShootingPosition.getX(),
                coldSandShootingPosition.getY())));
        events.add(event1);
        // 车辆执行装载货物事件
        Event event2 = getNewEvent();
        event2.setStartPositionEvent(new PositionLinkEvent(1796864626696781826L, null));
        event2.setInstruction(new Instruction(1817456988612825089L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RISING, null));
        events.add(event2);

        // 车辆将东西运走，运到冷射砂机位置
        Event event3 = getNewEvent();
        event3.setInstruction(new Instruction(1817456988612825089L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(coldSandBlasting.getX(), coldSandBlasting.getY())));
        events.add(event3);
        // 车辆执行卸载货物事件
        Event event4 = getNewEvent();
        event4.setStartEquipmentEvent(new EquipmentLinkEvent(1796862790547996675L, null));
        event4.setInstruction(new Instruction(1817456988612825089L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.LANDING, null));
        events.add(event4);
        // 车辆卸完货让他离开冷射砂机位置
        Event event5 = getNewEvent();
        event5.setInstruction(new Instruction(1817456988612825089L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(coldSandBlasting.getX() +
                60 * AlgorithmDataConverter.PLOTTING_SCALE, coldSandBlasting.getY())));
        events.add(event5);

        // 给冷射砂机发送通知事件
        Event event6 = getNewEvent();
        event6.setInstruction(new Instruction(1796862790547996675L, ExecuteTypeEnum.EQUIPMENT,
                InstructionsEnum.NOTICE, null));
        events.add(event6);

        // 通知车辆移动到冷射砂机位置
        Event event7 = getNewEvent();
        event7.setInstruction(new Instruction(1817456988612825089L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(coldSandBlasting.getX(), coldSandBlasting.getY())));
        events.add(event7);
        // 车辆执行装载货物事件
        Event event8 = getNewEvent();
        event8.setStartEquipmentEvent(new EquipmentLinkEvent(1796862790547996675L, null));
        event8.setInstruction(new Instruction(1817456988612825089L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RISING, null));
        events.add(event8);

        // 车辆移动到开模设备位置
        Event event9 = getNewEvent();
        event9.setInstruction(new Instruction(1817456988612825089L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(coldSandMoldOpening.getX(), coldSandMoldOpening.getY())));
        events.add(event9);
        // 车辆卸载货物事件
        Event event10 = getNewEvent();
        event10.setStartEquipmentEvent(new EquipmentLinkEvent(1796862790745128961L, null));
        event10.setInstruction(new Instruction(1817456988612825089L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.LANDING, null));
        events.add(event10);
        // 车辆离开开模设备位置
        Event event11 = getNewEvent();
        event11.setInstruction(new Instruction(1817456988612825089L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(coldSandMoldOpening.getX() -
                60 * AlgorithmDataConverter.PLOTTING_SCALE, coldSandMoldOpening.getY())));
        events.add(event11);

        // 给开模设备发送通知事件
        Event event12 = getNewEvent();
        event12.setInstruction(new Instruction(1796862790745128961L, ExecuteTypeEnum.EQUIPMENT,
                InstructionsEnum.NOTICE, null));
        events.add(event12);

        // 通知车辆移动到开模设备位置
        Event event13 = getNewEvent();
        event13.setInstruction(new Instruction(1817456988612825089L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(coldSandMoldOpening.getX(), coldSandMoldOpening.getY())));
        events.add(event13);
        // 车辆执行装载货物事件
        Event event14 = getNewEvent();
        event14.setStartEquipmentEvent(new EquipmentLinkEvent(1796862790745128961L, null));
        event14.setInstruction(new Instruction(1817456988612825089L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RISING, null));
        events.add(event14);

        // 车辆移动到冷砂模具开模砂箱放置点位
        Event event15 = getNewEvent();
        event15.setInstruction(new Instruction(1817456988612825089L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(coldMoldOpeningSandBoxLayPosition.getX(),
                coldMoldOpeningSandBoxLayPosition.getY())));
        events.add(event15);
        // 车辆卸载货物事件
        Event event16 = getNewEvent();
        event16.setStartPositionEvent(new PositionLinkEvent(1796864627296567297L, null));
        event16.setInstruction(new Instruction(1817456988612825089L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.LANDING, null));
        events.add(event16);

        // 车辆移动到开模设备位置
        Event event17 = getNewEvent();
        event17.setInstruction(new Instruction(1817456988612825089L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(coldSandMoldOpening.getX(), coldSandMoldOpening.getY())));
        events.add(event17);
        // 车辆执行装载货物事件 (可以没有)
        Event event18 = getNewEvent();
        event18.setStartEquipmentEvent(new EquipmentLinkEvent(1796862790745128961L, null));
        event18.setInstruction(new Instruction(1817456988612825089L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RISING, null));
        events.add(event18);

        // 车辆移动到冷砂模具开模模具放置点位
        Event event19 = getNewEvent();
        event19.setInstruction(new Instruction(1817456988612825089L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(coldMoldOpeningMoldLayPosition.getX(),
                coldMoldOpeningMoldLayPosition.getY())));
        events.add(event19);
        // 车辆卸载货物事件
        Event event20 = getNewEvent();
        event20.setStartPositionEvent(new PositionLinkEvent(1796864626961022977L, null));
        event20.setInstruction(new Instruction(1817456988612825089L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.LANDING, null));
        events.add(event20);
        // 车辆卸完货让他离开冷砂模具开模模具放置点
        Event event21 = getNewEvent();
        event21.setInstruction(new Instruction(1817456988612825089L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(coldSandMoldOpening.getX() -
                60 * AlgorithmDataConverter.PLOTTING_SCALE, coldSandMoldOpening.getY())));
        events.add(event21);
        return events;
    }

    /**
     * 初始化热砂喷涂上事件集合
     * 注意：这里如果有坐标数据，需要进行比例尺转换
     */
    private List<Event> initHotSprayUpEvents() {
        ArrayList<Event> events = new ArrayList<>();
        // 车辆到热砂模具开模砂箱放置点位
        Event event1 = getNewEvent();
        event1.setInstruction(new Instruction(1817886619438518273L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(hotMoldOpeningSandBoxLayPosition.getX(),
                hotMoldOpeningSandBoxLayPosition.getY())));
        events.add(event1);
        // 车辆执行装载货物事件
        Event event2 = getNewEvent();
        event2.setStartPositionEvent(new PositionLinkEvent(1796864627162349570L, null));
        event2.setInstruction(new Instruction(1817886619438518273L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RISING, null));
        events.add(event2);

        // 车辆将东西运走，运到热砂喷涂机1位置
        Event event3 = getNewEvent();
        event3.setInstruction(new Instruction(1817886619438518273L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(hotSandSprayingUp1.getX(), hotSandSprayingUp1.getY())));
        events.add(event3);
        // 车辆执行卸载货物事件
        Event event4 = getNewEvent();
        event4.setStartEquipmentEvent(new EquipmentLinkEvent(1796862790879346689L, null));
        event4.setInstruction(new Instruction(1817886619438518273L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.LANDING, null));
        events.add(event4);
        // 车辆卸完货让他离开热砂喷涂机1位置
        Event event5 = getNewEvent();
        event5.setInstruction(new Instruction(1817886619438518273L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(hotSandSprayingUp1.getX() +
                120 * AlgorithmDataConverter.PLOTTING_SCALE, hotSandSprayingUp1.getY())));
        events.add(event5);

        // 给热砂喷涂机1发送通知事件
        Event event6 = getNewEvent();
        event6.setInstruction(new Instruction(1796862790879346689L, ExecuteTypeEnum.EQUIPMENT,
                InstructionsEnum.NOTICE, null));
        events.add(event6);
        return events;
    }

    /**
     * 初始化热砂喷涂下事件集合
     * 注意：这里如果有坐标数据，需要进行比例尺转换
     */
    private List<Event> initHotSprayDownEvents() {
        ArrayList<Event> events = new ArrayList<>();
        // 车辆到热砂砂箱前往喷涂的下方点位
        Event event1 = getNewEvent();
        event1.setInstruction(new Instruction(1806899642168774658L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(hotSandBoxGoSprayDownPosition.getX(),
                hotSandBoxGoSprayDownPosition.getY())));
        events.add(event1);
        // 车辆执行装载货物事件
        Event event2 = getNewEvent();
        event2.setStartPositionEvent(new PositionLinkEvent(1796864627162349572L, null));
        event2.setInstruction(new Instruction(1806899642168774658L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RISING, null));
        events.add(event2);

        // 车辆将东西运走，运到热砂喷涂机3位置
        Event event3 = getNewEvent();
        event3.setInstruction(new Instruction(1806899642168774658L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(hotSandSprayingDown1.getX(), hotSandSprayingDown1.getY())));
        events.add(event3);
        // 车辆执行卸载货物事件
        Event event4 = getNewEvent();
        event4.setStartEquipmentEvent(new EquipmentLinkEvent(1796862791013564418L, null));
        event4.setInstruction(new Instruction(1806899642168774658L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.LANDING, null));
        events.add(event4);
        // 车辆卸完货让他离开热砂喷涂机3位置
        Event event5 = getNewEvent();
        event5.setInstruction(new Instruction(1806899642168774658L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(hotSandSprayingDown1.getX() +
                120 * AlgorithmDataConverter.PLOTTING_SCALE, hotSandSprayingDown1.getY())));
        events.add(event5);

        // 给热砂喷涂机3发送通知事件
        Event event6 = getNewEvent();
        event6.setInstruction(new Instruction(1796862791013564418L, ExecuteTypeEnum.EQUIPMENT,
                InstructionsEnum.NOTICE, null));
        events.add(event6);
        return events;
    }

    /**
     * 初始化冷砂喷涂上事件集合
     * 注意：这里如果有坐标数据，需要进行比例尺转换
     */
    private List<Event> initColdSprayUpEvents() {
        ArrayList<Event> events = new ArrayList<>();
        // 车辆到冷砂砂箱前往喷涂的上方点位
        Event event1 = getNewEvent();
        event1.setInstruction(new Instruction(1806898464978636801L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(coldSandBoxGoSprayUpPosition.getX(),
                coldSandBoxGoSprayUpPosition.getY())));
        events.add(event1);
        // 车辆执行装载货物事件
        Event event2 = getNewEvent();
        event2.setStartPositionEvent(new PositionLinkEvent(1796864627359481857L, null));
        event2.setInstruction(new Instruction(1806898464978636801L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RISING, null));
        events.add(event2);

        // 车辆将东西运走，运到冷砂喷涂机1位置
        Event event3 = getNewEvent();
        event3.setInstruction(new Instruction(1806898464978636801L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(coldSandSprayingUp1.getX(), coldSandSprayingUp1.getY())));
        events.add(event3);
        // 车辆执行卸载货物事件
        Event event4 = getNewEvent();
        event4.setStartEquipmentEvent(new EquipmentLinkEvent(1796862791147782147L, null));
        event4.setInstruction(new Instruction(1806898464978636801L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.LANDING, null));
        events.add(event4);
        // 车辆卸完货让他离开冷砂喷涂机1位置
        Event event5 = getNewEvent();
        event5.setInstruction(new Instruction(1806898464978636801L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(coldSandSprayingUp1.getX() +
                120 * AlgorithmDataConverter.PLOTTING_SCALE, coldSandSprayingUp1.getY())));
        events.add(event5);

        // 给冷砂喷涂机1发送通知事件
        Event event6 = getNewEvent();
        event6.setInstruction(new Instruction(1796862791147782147L, ExecuteTypeEnum.EQUIPMENT,
                InstructionsEnum.NOTICE, null));
        events.add(event6);
        return events;
    }

    /**
     * 初始化冷砂喷涂下事件集合
     * 注意：这里如果有坐标数据，需要进行比例尺转换
     */
    private List<Event> initColdSprayDownEvents() {
        ArrayList<Event> events = new ArrayList<>();
        // 车辆到冷砂砂箱前往喷涂的下方点位
        Event event1 = getNewEvent();
        event1.setInstruction(new Instruction(1813500565670334466L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(coldSandBoxGoSprayDownPosition.getX(),
                coldSandBoxGoSprayDownPosition.getY())));
        events.add(event1);
        // 车辆执行装载货物事件
        Event event2 = getNewEvent();
        event2.setStartPositionEvent(new PositionLinkEvent(1796864627422396417L, null));
        event2.setInstruction(new Instruction(1813500565670334466L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RISING, null));
        events.add(event2);

        // 车辆将东西运走，运到冷砂喷涂机3位置
        Event event3 = getNewEvent();
        event3.setInstruction(new Instruction(1813500565670334466L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(coldSandSprayingDown1.getX(), coldSandSprayingDown1.getY())));
        events.add(event3);
        // 车辆执行卸载货物事件
        Event event4 = getNewEvent();
        event4.setStartEquipmentEvent(new EquipmentLinkEvent(1796862791277805570L, null));
        event4.setInstruction(new Instruction(1813500565670334466L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.LANDING, null));
        events.add(event4);
        // 车辆卸完货让他离开冷砂喷涂机3位置
        Event event5 = getNewEvent();
        event5.setInstruction(new Instruction(1813500565670334466L, ExecuteTypeEnum.SUB_CAR,
                InstructionsEnum.RUNNING, new Coordinate(coldSandSprayingDown1.getX() +
                120 * AlgorithmDataConverter.PLOTTING_SCALE, coldSandSprayingDown1.getY())));
        events.add(event5);

        // 给冷砂喷涂机3发送通知事件
        Event event6 = getNewEvent();
        event6.setInstruction(new Instruction(1796862791277805570L, ExecuteTypeEnum.EQUIPMENT,
                InstructionsEnum.NOTICE, null));
        events.add(event6);
        return events;
    }
}
