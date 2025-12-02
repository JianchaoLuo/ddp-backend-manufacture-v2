package cn.hex.ddp.manufacture.infrastructure.plcmodule.mock;

import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Instruction;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 模拟由工厂控制的区域中可能的事件，目的是为了给模拟器，让模拟器mock数据
 * 【工岗状态变化触发的事件集合】
 *
 * @author Huhaisen
 * @date 2024/08/13
 */
@Data
public class WorkstationTriggerEvents {
    /**
     * 浇筑电炉设备_1事件集合
     */
    private List<Event> pouringElectricFurnace1Events;
    /**
     * 浇筑电炉设备_2事件集合
     */
    private List<Event> pouringElectricFurnace2Events;
    /**
     * 浇筑电炉设备_3事件集合
     */
    private List<Event> pouringElectricFurnace3Events;
    /**
     * 浇筑电炉设备_4事件集合
     */
    private List<Event> pouringElectricFurnace4Events;
    /**
     * 浇筑电炉设备_5事件集合
     */
    private List<Event> pouringElectricFurnace5Events;
    /**
     * 浇筑电炉设备_6事件集合
     */
    private List<Event> pouringElectricFurnace6Events;

    public WorkstationTriggerEvents() {
        initEvents();
    }

    private void initEvents() {
        this.pouringElectricFurnace1Events = initPouringElectricFurnaceEvents(1823262429266579458L);
        this.pouringElectricFurnace2Events = initPouringElectricFurnaceEvents(1823262429782478849L);
        this.pouringElectricFurnace3Events = initPouringElectricFurnaceEvents(1823262430042525698L);
        this.pouringElectricFurnace4Events = initPouringElectricFurnaceEvents(1823262430231269377L);
        this.pouringElectricFurnace5Events = initPouringElectricFurnaceEvents(1823262430432595970L);
        this.pouringElectricFurnace6Events = initPouringElectricFurnaceEvents(1823262430591979522L);
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

    private List<Event> initPouringElectricFurnaceEvents(Long equipmentId) {
        ArrayList<Event> events = new ArrayList<>();
        Event event1 = getNewEvent();
        event1.setInstruction(new Instruction(
                equipmentId,
                ExecuteTypeEnum.EQUIPMENT,
                InstructionsEnum.USE_MOLTEN_IRON,
                null));
        events.add(event1);
        return events;
    }
}
