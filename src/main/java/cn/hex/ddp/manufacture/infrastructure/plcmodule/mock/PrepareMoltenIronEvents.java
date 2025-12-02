package cn.hex.ddp.manufacture.infrastructure.plcmodule.mock;

import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Instruction;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 *  模拟由工厂控制的区域中可能的事件，目的是为了给模拟器，让模拟器mock数据
 *  【准备铁水事件集合】
 *
 * @author Huhaisen
 * @date 2024/08/13
 */
@Data
public class PrepareMoltenIronEvents {
    /**
     * 浇筑电炉设备_1准备铁水事件集合
     */
    private List<Event> pouringElectricFurnace1PrepareEvents;
    /**
     * 浇筑电炉设备_2准备铁水事件集合
     */
    private List<Event> pouringElectricFurnace2PrepareEvents;

    public PrepareMoltenIronEvents() {
        initEvents();
    }

    private void initEvents() {
        this.pouringElectricFurnace1PrepareEvents = initPrepareMoltenIronEvents(
                1823262429266579458L, 15.0);
        this.pouringElectricFurnace2PrepareEvents = initPrepareMoltenIronEvents(
                1823262429782478849L, 15.0);
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

    private List<Event> initPrepareMoltenIronEvents(Long equipmentId, Double moltenIronQuantity) {
        ArrayList<Event> events = new ArrayList<>();
        Event event1 = getNewEvent();
        event1.setInstruction(new Instruction(
                equipmentId,
                ExecuteTypeEnum.EQUIPMENT,
                InstructionsEnum.PREPARE_MOLTEN_IRON,
                null,
                moltenIronQuantity));
        events.add(event1);
        return events;
    }
}
