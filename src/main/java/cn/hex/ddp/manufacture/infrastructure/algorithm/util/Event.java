package cn.hex.ddp.manufacture.infrastructure.algorithm.util;

import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription: 事件类，用于描述算法中发生的具体事件
 * @Author: KangHong
 * @Created: 2024/5/29 2:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event implements Serializable {
    /**
     * 事件id
     */
    private String id;
    /**
     * 事件名称
     */
    private String eventName;
    /**
     * 事件执行预计需要用到的时间
     */
    private double executionTime;
    /**
     * 事件发生的时间
     */
    private double startTime;
    /**
     * 事件结束的时间
     */
    private double endTime;
    /**
     * 事件开始前，参与子车的事件关联
     * 主要包括子车的状态信息
     */
    private SubCarLinkEvent startSubCarEvent;
    /**
     * 事件开始前，参与母车的事件关联
     * 主要包括母车的状态信息
     */
    private FerryLinkEvent startFerryEvent;
    /**
     * 事件开始前，参与设备的事件关联
     * 主要包括设备的状态信息
     */
    private EquipmentLinkEvent startEquipmentEvent;
    /**
     * 事件开始前，参与点位的事件关联
     * 主要包括点位的状态信息
     */
    private PositionLinkEvent startPositionEvent;
    /**
     * 事件开始前，参与工岗的事件关联
     * 主要包括工岗的状态信息
     */
    private WorkStationLinkEvent startWorkStationEvent;
    /**
     * 事件开始前，参与轨道的事件关联
     * 主要包括轨道的状态信息
     */
    private List<PathLinkEvent> startPathEvent;
    /**
     * 事件结束后，参与子车的事件关联
     * 主要包括子车的状态信息
     */
    private SubCarLinkEvent endSubCarEvent;
    /**
     * 事件结束后，参与母车的事件关联
     * 主要包括母车的状态信息
     */
    private FerryLinkEvent endFerryEvent;
    /**
     * 事件结束后，参与设备的事件关联
     * 主要包括设备的状态信息
     */
    private EquipmentLinkEvent endEquipmentEvent;
    /**
     * 事件结束后，参与点位的事件关联
     * 主要包括点位的状态信息
     */
    private PositionLinkEvent endPositionEvent;
    /**
     * 事件结束后，参与工岗的事件关联
     * 主要包括工岗的状态信息
     */
    private WorkStationLinkEvent endWorkStationEvent;
    /**
     * 事件结束后，参与轨道的事件关联
     * 主要包括轨道的状态信息
     */
    private List<PathLinkEvent> endPathEvent;
    /**
     * 事件执行的指令
     */
    private Instruction instruction;
    /**
     * 事件是否完成的标识
     * true表示完成，false表示未完成
     */
    private boolean completeFlag;
    /**
     * 事件是否正在发生的标识
     * true表示事件正在发生，false表示事件未发生
     */
    private boolean occurringFlag;
    /**
     * 事件涉及的产品
     */
    private Product product;

    public Event(String id, String eventName, double executionTime, double startTime, double endTime, SubCarLinkEvent startSubCarEvent,
                 FerryLinkEvent startFerryEvent, EquipmentLinkEvent startEquipmentEvent, PositionLinkEvent startPositionEvent,
                 List<PathLinkEvent> startPathEvent, WorkStationLinkEvent startWorkStationEvent, SubCarLinkEvent endSubCarEvent,
                 FerryLinkEvent endFerryEvent, EquipmentLinkEvent endEquipmentEvent, PositionLinkEvent endPositionEvent,
                 WorkStationLinkEvent endWorkStationEvent, List<PathLinkEvent> endPathEvent, Instruction instruction,
                 boolean completeFlag, boolean occurringFlag) {
        this.id = id;
        this.eventName = eventName;
        this.executionTime = executionTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startSubCarEvent = startSubCarEvent;
        this.startFerryEvent = startFerryEvent;
        this.startEquipmentEvent = startEquipmentEvent;
        this.startPositionEvent = startPositionEvent;
        this.startPathEvent = startPathEvent;
        this.startWorkStationEvent = startWorkStationEvent;
        this.endSubCarEvent = endSubCarEvent;
        this.endFerryEvent = endFerryEvent;
        this.endEquipmentEvent = endEquipmentEvent;
        this.endPositionEvent = endPositionEvent;
        this.endWorkStationEvent = endWorkStationEvent;
        this.endPathEvent = endPathEvent;
        this.instruction = instruction;
        this.completeFlag = completeFlag;
        this.occurringFlag = occurringFlag;
    }

    public String toString(){
        String str = "id：" + id + " name：" + eventName + " instruction：" + instruction;

        return str;
    }
}
