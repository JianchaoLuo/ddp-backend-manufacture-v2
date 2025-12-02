package cn.hex.ddp.manufacture.infrastructure.algorithm.scalable.tool.allRegion;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.test.OtherSimulation;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

/**
 * @ClassDescription: 仿真的工具类，所有仿真用到的属性存放在工具类中
 * @Author: KangHong
 * @Created: 2024/11/27 20:15
 */
@Data
@AllArgsConstructor
public class AttributeTool {
    /**
     * 处理仿真输入的工具类
     */
    private HandleInputTool handleInputTool;
    /**
     * 调度需要用到的工具类
     */
    private DispatchTool dispatchTool;
    /**
     * 仿真总时间
     */
    private double totalTime;
    /**
     * 事件最小完成时间
     */
    private double eventMinTime;
    /**
     * 热砂出库队列
     */
    private Deque<Product> hotProducts;
    /**
     * 冷砂出库队列
     */
    private Deque<Product> coldProducts;
    /**
     * 待浇筑区是否放置完砂箱，可以开始浇筑的标识
     * false表示不可以开始浇筑，true表示可以
     * 目前暂时用当订单中所有需要生成的产品全部到达待浇筑区时，可以准备开始浇筑
     */
    private boolean waitingPourFlag;
    /**
     * 待浇筑区的产品计数，用于判断是否达到所有需要的产品数目
     */
    private int waitingPourCount;
    /**
     * 铸件计数判断
     */
    private int castingCount;
    /**
     * 仿真结束判断，用于判断何时结束仿真
     */
    private int finishedCount;
    /**
     * 订单数量列表
     */
    private Map<Long, Integer> orderNums;
    /**
     * 浇筑铁水用量
     */
    private List<Double> usageDoubleList;
    /**
     * 测试的模拟其他区域类
     */
    private OtherSimulation otherSimulation;

    /**
     * 创造事件的函数
     * @param id 事件id
     * @param eventName 事件名称
     * @param executionTime 事件执行预计需要用到的时间
     * @param startTime 事件发生的时间
     * @param endTime 事件结束的时间
     * @param subCarId 事件开始前，参与子车的id
     * @param startSubCarStatus 事件开始前，参与子车的状态
     * @param startSubCarCpFlag 事件开始前，参与子车的状态，表示是否判断位置
     * @param startSubCarTargetCoordinate 若判断位置，目标位置
     * @param endSubCarStatus 事件结束后，参与子车的状态
     * @param endSubCarCpFlag 事件结束后，参与子车的状态，表示是否判断位置
     * @param endSubCarTargetCoordinate 若判断位置，目标位置
     * @param ferryId 事件开始前，参与母车的id
     * @param startFerryStatus 事件开始前，参与母车的状态
     * @param startFerryCpFlag 事件开始前，参与母车的状态，表示是否判断位置
     * @param startFerryTargetCoordinate 若判断位置，目标位置
     * @param endFerryStatus 事件结束后，参与母车的状态
     * @param endFerryCpFlag 事件结束后，参与母车的状态，表示是否判断位置
     * @param endFerryTargetCoordinate 若判断位置，目标位置
     * @param equipmentId 事件开始前，参与设备的id
     * @param startEquipmentStatus 事件开始前，参与设备的状态
     * @param endEquipmentStatus 事件结束后，参与设备的状态
     * @param positionId 事件开始前，参与点位的id
     * @param startPositionStatus 事件开始前，参与点位的状态
     * @param endPositionStatus 事件结束后，参与点位的状态
     * @param workStationId 事件开始前，参与工岗的id
     * @param startWorkStationStatus 事件开始前，参与工岗的状态
     * @param endWorkStationStatus 事件结束后，参与工岗的状态
     * @param pathIds 事件开始前，参与道路的id
     * @param startPathStatus 事件开始前，参与道路的状态
     * @param endPathstatus 事件结束后，参与道路的状态
     * @param instructionExecuteId 事件中指令的id
     * @param executeType 事件中指令执行者的类型
     * @param instructions 事件中执行的指令
     * @param runAimCoordinate 如有需要，目标点位
     * @return
     */
    public Event createEvent(String id, String eventName, double executionTime, double startTime, double endTime, Long subCarId,
                             List<SubCarStatusEnum> startSubCarStatus, boolean startSubCarCpFlag, Coordinate startSubCarTargetCoordinate,
                             List<SubCarStatusEnum> endSubCarStatus, boolean endSubCarCpFlag, Coordinate endSubCarTargetCoordinate,
                             Long ferryId, List<FerryStatusEnum> startFerryStatus, boolean startFerryCpFlag, Coordinate startFerryTargetCoordinate,
                             List<FerryStatusEnum> endFerryStatus, boolean endFerryCpFlag, Coordinate endFerryTargetCoordinate,
                             Long equipmentId, List<EquipmentOperationStatusEnum> startEquipmentStatus, List<EquipmentOperationStatusEnum> endEquipmentStatus,
                             Long positionId, List<PositionStatusEnum> startPositionStatus, List<PositionStatusEnum> endPositionStatus,
                             Long workStationId, List<WorkstationStatusEnum> startWorkStationStatus, List<WorkstationStatusEnum> endWorkStationStatus,
                             List<Long> pathIds, List<PathStatusEnum> startPathStatus, List<PathStatusEnum> endPathstatus, Long instructionExecuteId,
                             ExecuteTypeEnum executeType, InstructionsEnum instructions, Coordinate runAimCoordinate, Product product){

        SubCarLinkEvent startSubCarEvent = new SubCarLinkEvent(subCarId, startSubCarStatus, startSubCarCpFlag, startSubCarTargetCoordinate);
        SubCarLinkEvent endSubCarEvent = new SubCarLinkEvent(subCarId, endSubCarStatus, endSubCarCpFlag, endSubCarTargetCoordinate);
        FerryLinkEvent startFerryEvent = new FerryLinkEvent(ferryId, startFerryStatus, startFerryCpFlag, startFerryTargetCoordinate);
        FerryLinkEvent endFerryEvent = new FerryLinkEvent(ferryId, endFerryStatus, endFerryCpFlag, endFerryTargetCoordinate);
        EquipmentLinkEvent startEquipmentEvent = new EquipmentLinkEvent(equipmentId, startEquipmentStatus);
        EquipmentLinkEvent endEquipmentEvent = new EquipmentLinkEvent(equipmentId, endEquipmentStatus);
        PositionLinkEvent startPositionEvent = new PositionLinkEvent(positionId, startPositionStatus);
        PositionLinkEvent endPositionEvent = new PositionLinkEvent(positionId, endPositionStatus);
        WorkStationLinkEvent startWorkStationEvent = new WorkStationLinkEvent(workStationId, startWorkStationStatus);
        WorkStationLinkEvent endWorkStationEvent = new WorkStationLinkEvent(workStationId, endWorkStationStatus);

        List<PathLinkEvent> startPathEvents = new ArrayList<>();
        List<PathLinkEvent> endPathLinkEvent = new ArrayList<>();
        int startPathStatusSize = startPathStatus == null ? 0 : startPathStatus.size();
        for (int i = 0; i < startPathStatusSize; i++) {
            startPathEvents.add(new PathLinkEvent(pathIds.get(i), startPathStatus.get(i)));
            endPathLinkEvent.add(new PathLinkEvent(pathIds.get(i), endPathstatus.get(i)));
        }

        Instruction instruction = new Instruction(instructionExecuteId, executeType, instructions, runAimCoordinate);
        Event event = new Event(id, eventName, executionTime, startTime, endTime, startSubCarEvent, startFerryEvent,
                startEquipmentEvent, startPositionEvent, startWorkStationEvent, startPathEvents, endSubCarEvent, endFerryEvent,
                endEquipmentEvent, endPositionEvent, endWorkStationEvent, endPathLinkEvent, instruction, false, false, product);

        return event;
    }
}
