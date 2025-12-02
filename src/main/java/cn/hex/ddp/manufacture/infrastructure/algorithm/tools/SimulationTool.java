package cn.hex.ddp.manufacture.infrastructure.algorithm.tools;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.SubCar;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.*;

import java.util.ArrayList;
import java.util.List;

import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum.EMPTY_LOAD;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum.SUB_CAR_IN_FERRY;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/6/26 15:00
 */
public class SimulationTool {
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

    /**
     * 判断当前子车是否是空载状态
     * @param subCar 需要判断的子车
     * @return true表示空载，false表示满载
     */
    public boolean judgeSubCarIsEmpty(SubCar subCar){
        for (int i = 0; i < subCar.getSubCarState().size(); i++) {
            if (subCar.getSubCarState().get(i).equals(EMPTY_LOAD)){

                return true;
            }
        }

        return false;
    }

    /**
     * 判断当前子车是否是完全在母车上
     * @param subCar 需要判断的子车
     * @return true表示子车完全在母车上，false相反
     */
    public boolean judgeSubCarIsInFerry(SubCar subCar){
        for (int i = 0; i < subCar.getSubCarState().size(); i++) {
            if (subCar.getSubCarState().get(i).equals(SUB_CAR_IN_FERRY)){

                return true;
            }
        }

        return false;
    }

    public double findMinEventTimeAndUpdata(Double[] timeList) {
        double minTime = 0.0;
        for (int i = 0; i < timeList.length; i++) {
            if (timeList[i] > 0.0)
                minTime = timeList[i];
        }

        if (minTime != 0.0){
            for (int i = 1; i < timeList.length; i++) {
                if ((timeList[i] < minTime) && timeList[i] != 0.0){
                    minTime = timeList[i];
                }
            }
        }

        if (minTime != 0.0){
            for (int i = 1; i < timeList.length; i++) {
                timeList[i] -= minTime;
                if (timeList[i] < 0.0){
                    timeList[i] = 0.0;
                }
            }
        }

        return minTime;
    }

    public Event createFlagEvent(){

        Event event = new Event("!!!!!!", "FlagEvent", 0.0, 0.0, 0.0, null, null,
                null, null, null, null, null, null,
                null, null, null, null, null, false, false);

        return event;
    }

    public boolean judgeSubCarIsHalfEmpty(SubCar subCar) {
        for (int i = 0; i < subCar.getSubCarState().size(); i++) {
            if (subCar.getSubCarState().get(i).equals(SubCarStatusEnum.HALF_LOAD)){

                return true;
            }
        }

        return false;
    }
}
