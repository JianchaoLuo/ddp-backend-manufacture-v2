package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.mouldingLine;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.AnalogInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.SubCar;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;

import java.util.ArrayList;
import java.util.List;

import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum.SUB_CAR;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum.LANDING;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum.RUNNING;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/8 16:50
 */
public class MouldingSimulationTools {

    private final SimulationTool allTools = new SimulationTool();
    /**
     * 判断模具回流轨道子车满载时是否经过制芯开模点位
     * @param subCar
     * @param input
     * @return true表示已经经过点位，false表示未经过点位
     */
    public boolean judgeMoldRefluxSubCarFullPosition(SubCar subCar, OutBoundMouldingAndClosingBox input){
        if (subCar.getLocationCoordinate().getY() >= input.getMouldingInput().getInput().getCoreMoldOpeningPosition().getCoordinate().getY()){
            return true;
        }

        return false;
    }

    public OutBoundMouldingAndClosingBox createColdSparyUpMokeEventFlow(OutBoundMouldingAndClosingBox outBoundMouldingAndClosingBox, AnalogInput input){
        MouldingGanttChartLinkInput mouldingInput = outBoundMouldingAndClosingBox.getMouldingInput();

        Coordinate coldUpSubCarCoordinate = input.getColdUpSpray().getLocationCoordinate().coordinateDeepCopy();

        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>();
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        startSubCarStatus.add(SubCarStatusEnum.EMPTY_LOAD);
        startSubCarStatus.add(SubCarStatusEnum.TOP_ROD_FALL_DOWN);
        startSubCarStatus.add(SubCarStatusEnum.SUB_CAR_NOT_IN_FERRY);
        endSubCarStatus.add(SubCarStatusEnum.FULL_LOAD);
        endSubCarStatus.add(SubCarStatusEnum.TOP_ROD_RAISED);
        endSubCarStatus.add(SubCarStatusEnum.SUB_CAR_NOT_IN_FERRY);
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        startPositionStatus.add(PositionStatusEnum.OCCUPIED);
        endPositionStatus.add(PositionStatusEnum.UNOCCUPIED);
        List<EquipmentOperationStatusEnum> startEquipmentStatus = new ArrayList<>();
        startEquipmentStatus.add(EquipmentOperationStatusEnum.WORKING);

        Event event = allTools.createEvent("002018", "moke", 0.0, 0.0, 0.0,
                input.getColdUpSpray().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, mouldingInput.getInput().getColdSandBoxSprayPosition_1().getCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                mouldingInput.getInput().getColdSandBoxSprayPosition_1().getId(), startPositionStatus, null, null, null,
                null, null, null, null, input.getColdUpSpray().getId(),
                SUB_CAR, RUNNING, mouldingInput.getInput().getColdSandBoxSprayPosition_1().getCoordinate(), null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdUpMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002019", "moke", 0.0, 0.0, 0.0,
                input.getColdUpSpray().getId(), startSubCarStatus, false, null,
                endSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, null, null, null,
                mouldingInput.getInput().getColdSandBoxSprayPosition_1().getId(), startPositionStatus, endPositionStatus, null, null,
                null, null, null, null, input.getColdUpSpray().getId(),
                SUB_CAR, InstructionsEnum.RISING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdUpMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002020", "moke", 0.0, 0.0, 0.0,
                input.getColdUpSpray().getId(), endSubCarStatus, false, null,
                endSubCarStatus, true, mouldingInput.getInput().getColdSpray_1().getEquipmentCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdUpSpray().getId(),
                SUB_CAR, RUNNING, mouldingInput.getInput().getColdSpray_1().getEquipmentCoordinate(), null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdUpMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002021", "moke", 0.0, 0.0, 0.0,
                input.getColdUpSpray().getId(), endSubCarStatus, false, null,
                startSubCarStatus, true, mouldingInput.getInput().getColdSpray_1().getEquipmentCoordinate(),
                null, null, false, null, null,
                false, null, mouldingInput.getInput().getColdSpray_1().getId(), null, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdUpSpray().getId(),
                SUB_CAR, LANDING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdUpMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002022", "moke", 0.0, 0.0, 0.0,
                null, null, false, null,
                null, false, null,
                null, null, false, null, null,
                false, null, mouldingInput.getInput().getColdSpray_1().getId(), startEquipmentStatus, null,
                null, null, null, null, null,
                null, null, null, null, mouldingInput.getInput().getColdSpray_1().getId(),
                ExecuteTypeEnum.EQUIPMENT, InstructionsEnum.NOTICE, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdUpMokeInputInGanttIndex()).getEvents().add(event);

        /*
        新增moke
         */
        event = allTools.createEvent("002059", "moke", 0.0, 0.0, 0.0,
                input.getColdUpSpray().getId(), startSubCarStatus, false, null,
                endSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, mouldingInput.getInput().getColdSpray_1().getId(), null, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdUpSpray().getId(),
                SUB_CAR, InstructionsEnum.RISING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdUpMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002060", "moke", 0.0, 0.0, 0.0,
                input.getColdUpSpray().getId(), endSubCarStatus, false, null,
                endSubCarStatus, true, mouldingInput.getInput().getColdSpray_2().getEquipmentCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdUpSpray().getId(),
                SUB_CAR, RUNNING, mouldingInput.getInput().getColdSpray_2().getEquipmentCoordinate(), null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdUpMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002061", "moke", 0.0, 0.0, 0.0,
                input.getColdUpSpray().getId(), endSubCarStatus, false, null,
                startSubCarStatus, true, mouldingInput.getInput().getColdSpray_2().getEquipmentCoordinate(),
                null, null, false, null, null,
                false, null, mouldingInput.getInput().getColdSpray_2().getId(), null, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdUpSpray().getId(),
                SUB_CAR, LANDING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdUpMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002062", "moke", 0.0, 0.0, 0.0,
                null, null, false, null,
                null, false, null,
                null, null, false, null, null,
                false, null, mouldingInput.getInput().getColdSpray_2().getId(), startEquipmentStatus, null,
                null, null, null, null, null,
                null, null, null, null, mouldingInput.getInput().getColdSpray_2().getId(),
                ExecuteTypeEnum.EQUIPMENT, InstructionsEnum.NOTICE, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdUpMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002063", "moke", 0.0, 0.0, 0.0,
                input.getColdUpSpray().getId(), startSubCarStatus, false, null,
                endSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, mouldingInput.getInput().getColdSpray_2().getId(), null, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdUpSpray().getId(),
                SUB_CAR, InstructionsEnum.RISING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdUpMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002064", "moke", 0.0, 0.0, 0.0,
                input.getColdUpSpray().getId(), endSubCarStatus, false, null,
                endSubCarStatus, true, mouldingInput.getInput().getColdSpray_3().getEquipmentCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdUpSpray().getId(),
                SUB_CAR, RUNNING, mouldingInput.getInput().getColdSpray_3().getEquipmentCoordinate(), null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdUpMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002065", "moke", 0.0, 0.0, 0.0,
                input.getColdUpSpray().getId(), endSubCarStatus, false, null,
                startSubCarStatus, true, mouldingInput.getInput().getColdSpray_3().getEquipmentCoordinate(),
                null, null, false, null, null,
                false, null, mouldingInput.getInput().getColdSpray_3().getId(), null, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdUpSpray().getId(),
                SUB_CAR, LANDING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdUpMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002066", "moke", 0.0, 0.0, 0.0,
                null, null, false, null,
                null, false, null,
                null, null, false, null, null,
                false, null, mouldingInput.getInput().getColdSpray_3().getId(), startEquipmentStatus, null,
                null, null, null, null, null,
                null, null, null, null, mouldingInput.getInput().getColdSpray_3().getId(),
                ExecuteTypeEnum.EQUIPMENT, InstructionsEnum.NOTICE, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdUpMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002023", "moke", 0.0, 0.0, 0.0,
                input.getColdUpSpray().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, coldUpSubCarCoordinate,
                null, null, false, null, null,
                false, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdUpSpray().getId(),
                SUB_CAR, RUNNING, coldUpSubCarCoordinate, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdUpMokeInputInGanttIndex()).getEvents().add(event);

        outBoundMouldingAndClosingBox.setMouldingInput(mouldingInput);

        return outBoundMouldingAndClosingBox;
    }

    public OutBoundMouldingAndClosingBox createColdSparyDownMokeEventFlow(OutBoundMouldingAndClosingBox outBoundMouldingAndClosingBox, AnalogInput input){
        MouldingGanttChartLinkInput mouldingInput = outBoundMouldingAndClosingBox.getMouldingInput();

        Coordinate coldDownSubCarCoordinate = input.getColdDownSpray().getLocationCoordinate().coordinateDeepCopy();

        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>();
        List<SubCarStatusEnum> endSubCarStatus = new ArrayList<>();
        startSubCarStatus.add(SubCarStatusEnum.EMPTY_LOAD);
        startSubCarStatus.add(SubCarStatusEnum.TOP_ROD_FALL_DOWN);
        startSubCarStatus.add(SubCarStatusEnum.SUB_CAR_NOT_IN_FERRY);
        endSubCarStatus.add(SubCarStatusEnum.FULL_LOAD);
        endSubCarStatus.add(SubCarStatusEnum.TOP_ROD_RAISED);
        endSubCarStatus.add(SubCarStatusEnum.SUB_CAR_NOT_IN_FERRY);
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        startPositionStatus.add(PositionStatusEnum.OCCUPIED);
        endPositionStatus.add(PositionStatusEnum.UNOCCUPIED);
        List<EquipmentOperationStatusEnum> startEquipmentStatus = new ArrayList<>();
        startEquipmentStatus.add(EquipmentOperationStatusEnum.WORKING);
        List<EquipmentOperationStatusEnum> endEquipmentStatus = new ArrayList<>();
        endEquipmentStatus.add(EquipmentOperationStatusEnum.FINISHED);

        Event event = allTools.createEvent("002018", "moke", 0.0, 0.0, 0.0,
                input.getColdDownSpray().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, mouldingInput.getInput().getColdSandBoxSprayPosition_1().getCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                mouldingInput.getInput().getColdSandBoxSprayPosition_1().getId(), startPositionStatus, null, null, null,
                null, null, null, null, input.getColdDownSpray().getId(),
                SUB_CAR, RUNNING, mouldingInput.getInput().getColdSandBoxSprayPosition_1().getCoordinate(), null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdDownMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002019", "moke", 0.0, 0.0, 0.0,
                input.getColdDownSpray().getId(), startSubCarStatus, false, null,
                endSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, null, null, null,
                mouldingInput.getInput().getColdSandBoxSprayPosition_1().getId(), startPositionStatus, endPositionStatus, null, null,
                null, null, null, null, input.getColdDownSpray().getId(),
                SUB_CAR, InstructionsEnum.RISING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdDownMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002020", "moke", 0.0, 0.0, 0.0,
                input.getColdDownSpray().getId(), endSubCarStatus, false, null,
                endSubCarStatus, true, mouldingInput.getInput().getColdSpray_1().getEquipmentCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdDownSpray().getId(),
                SUB_CAR, RUNNING, mouldingInput.getInput().getColdSpray_1().getEquipmentCoordinate(), null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdDownMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002021", "moke", 0.0, 0.0, 0.0,
                input.getColdDownSpray().getId(), endSubCarStatus, false, null,
                startSubCarStatus, true, mouldingInput.getInput().getColdSpray_1().getEquipmentCoordinate(),
                null, null, false, null, null,
                false, null, mouldingInput.getInput().getColdSpray_1().getId(), null, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdDownSpray().getId(),
                SUB_CAR, LANDING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdDownMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002021", "moke", 0.0, 0.0, 0.0,
                null, null, false, null,
                null, false, null,
                null, null, false, null, null,
                false, null, mouldingInput.getInput().getColdSpray_1().getId(), startEquipmentStatus, endEquipmentStatus,
                null, null, null, null, null,
                null, null, null, null, mouldingInput.getInput().getColdSpray_1().getId(),
                ExecuteTypeEnum.EQUIPMENT, InstructionsEnum.NOTICE, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdDownMokeInputInGanttIndex()).getEvents().add(event);

        /*
        新增moke
         */
        event = allTools.createEvent("002079", "moke", 0.0, 0.0, 0.0,
                input.getColdDownSpray().getId(), startSubCarStatus, false, null,
                endSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, mouldingInput.getInput().getColdSpray_1().getId(), endEquipmentStatus, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdDownSpray().getId(),
                SUB_CAR, InstructionsEnum.RISING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdDownMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002080", "moke", 0.0, 0.0, 0.0,
                input.getColdDownSpray().getId(), endSubCarStatus, false, null,
                endSubCarStatus, true, mouldingInput.getInput().getColdSpray_2().getEquipmentCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdDownSpray().getId(),
                SUB_CAR, RUNNING, mouldingInput.getInput().getColdSpray_2().getEquipmentCoordinate(), null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdDownMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002081", "moke", 0.0, 0.0, 0.0,
                input.getColdDownSpray().getId(), endSubCarStatus, false, null,
                startSubCarStatus, true, mouldingInput.getInput().getColdSpray_2().getEquipmentCoordinate(),
                null, null, false, null, null,
                false, null, mouldingInput.getInput().getColdSpray_2().getId(), null, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdDownSpray().getId(),
                SUB_CAR, LANDING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdDownMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002082", "moke", 0.0, 0.0, 0.0,
                null, null, false, null,
                null, false, null,
                null, null, false, null, null,
                false, null, mouldingInput.getInput().getColdSpray_2().getId(), startEquipmentStatus, endEquipmentStatus,
                null, null, null, null, null,
                null, null, null, null, mouldingInput.getInput().getColdSpray_2().getId(),
                ExecuteTypeEnum.EQUIPMENT, InstructionsEnum.NOTICE, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdDownMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002083", "moke", 0.0, 0.0, 0.0,
                input.getColdDownSpray().getId(), startSubCarStatus, false, null,
                endSubCarStatus, false, null,
                null, null, false, null, null,
                false, null, mouldingInput.getInput().getColdSpray_2().getId(), endEquipmentStatus, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdDownSpray().getId(),
                SUB_CAR, InstructionsEnum.RISING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdDownMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002084", "moke", 0.0, 0.0, 0.0,
                input.getColdDownSpray().getId(), endSubCarStatus, false, null,
                endSubCarStatus, true, mouldingInput.getInput().getColdSpray_3().getEquipmentCoordinate(),
                null, null, false, null, null,
                false, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdDownSpray().getId(),
                SUB_CAR, RUNNING, mouldingInput.getInput().getColdSpray_3().getEquipmentCoordinate(), null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdDownMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002085", "moke", 0.0, 0.0, 0.0,
                input.getColdDownSpray().getId(), endSubCarStatus, false, null,
                startSubCarStatus, true, mouldingInput.getInput().getColdSpray_3().getEquipmentCoordinate(),
                null, null, false, null, null,
                false, null, mouldingInput.getInput().getColdSpray_3().getId(), null, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdDownSpray().getId(),
                SUB_CAR, LANDING, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdDownMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002086", "moke", 0.0, 0.0, 0.0,
                null, null, false, null,
                null, false, null,
                null, null, false, null, null,
                false, null, mouldingInput.getInput().getColdSpray_3().getId(), startEquipmentStatus, endEquipmentStatus,
                null, null, null, null, null,
                null, null, null, null, mouldingInput.getInput().getColdSpray_3().getId(),
                ExecuteTypeEnum.EQUIPMENT, InstructionsEnum.NOTICE, null, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdDownMokeInputInGanttIndex()).getEvents().add(event);

        event = allTools.createEvent("002087", "moke", 0.0, 0.0, 0.0,
                input.getColdDownSpray().getId(), startSubCarStatus, false, null,
                startSubCarStatus, true, coldDownSubCarCoordinate,
                null, null, false, null, null,
                false, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, input.getColdDownSpray().getId(),
                SUB_CAR, RUNNING, coldDownSubCarCoordinate, null);
        mouldingInput.getGanttChart().getEventLinkGantts().get(mouldingInput.getInput().getMouldingIndexInput().getColdDownMokeInputInGanttIndex()).getEvents().add(event);

        outBoundMouldingAndClosingBox.setMouldingInput(mouldingInput);

        return outBoundMouldingAndClosingBox;
    }
}
