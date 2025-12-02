package cn.hex.ddp.manufacture.infrastructure.algorithm.tools;

import cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathNameEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.CoolingSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.OpenBoxSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.PouringSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.WaitingPourSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.cooling.CoolingGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.cooling.CoolingIndexInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.cooling.ImitateCoolingPosition;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.openBox.OpenBoxGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.openBox.OpenBoxIndexInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.pouring.PouringGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.pouring.PouringIndexInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.waitingPour.WaitingPourGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.waitingPour.WaitingPourIndexInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.EventLinkGantt;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;

import java.util.ArrayList;
import java.util.List;

import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum.*;
import static cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationNameEnum.*;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum.*;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/29 15:07
 */
public class PourHandleInput {
    /**
     * 待浇筑区的输入处理
     * @param input 仿真输入
     * @param totalTime 仿真总时间
     * @return 待浇筑区输入
     */
    public WaitingPourGanttChartLinkInput handleWaitingPourInput(SimulateInput input, double totalTime) {
        List<SubCar> subCars = new ArrayList<>();
        List<Ferry> ferries = new ArrayList<>();
        List<Equipment> equipments = new ArrayList<>();
        List<Position> positions = new ArrayList<>();
        List<WorkStation> workStations = new ArrayList<>();
        List<Path> paths = new ArrayList<>();

        for (int i = 0; i < input.getAllocationInputs().size(); i++) {
            if (input.getAllocationInputs().get(i).getRegion().equals(AreaEnum.SOUTH_WAITING_POURING)){
                subCars = input.getAllocationInputs().get(i).getSubCars();
                ferries = input.getAllocationInputs().get(i).getFerries();
                equipments = input.getAllocationInputs().get(i).getEquipments();
                positions = input.getAllocationInputs().get(i).getPositions();
                workStations = input.getAllocationInputs().get(i).getWorkStations();
                paths = input.getAllocationInputs().get(i).getPaths();
            }
        }

        Ferry closingGoPourFerry = new Ferry();
        SubCar closingGoPourSubCar = new SubCar();
        List<List<Position>> upPositions = new ArrayList<>();
        List<List<Position>> downPositions = new ArrayList<>();
        List<Position> upPosition_1 = new ArrayList<>();
        List<Position> upPosition_2 = new ArrayList<>();
        List<Position> upPosition_3 = new ArrayList<>();
        List<Position> upPosition_4 = new ArrayList<>();
        List<Position> upPosition_5 = new ArrayList<>();
        List<Position> downPosition_1 = new ArrayList<>();
        List<Position> downPosition_2 = new ArrayList<>();
        List<Position> downPosition_3 = new ArrayList<>();
        List<Position> downPosition_4 = new ArrayList<>();
        List<Position> downPosition_5 = new ArrayList<>();
        Integer positionCode = PositionNameEnum.POURING_UP_FIRST_POSITION_1.getCode();
        Path closingGoPourPath = new Path();
        Path waitingPourUpPath_1 = new Path();
        Path waitingPourUpPath_2 = new Path();
        Path waitingPourUpPath_3 = new Path();
        Path waitingPourUpPath_4 = new Path();
        Path waitingPourUpPath_5 = new Path();
        Path waitingPourDownPath_1 = new Path();
        Path waitingPourDownPath_2 = new Path();
        Path waitingPourDownPath_3 = new Path();
        Path waitingPourDownPath_4 = new Path();
        Path waitingPourDownPath_5 = new Path();
        WaitingPourIndexInput waitingPourIndexInput = new WaitingPourIndexInput();

        //查询待浇筑的车辆
        for (int i = 0; i < ferries.size(); i++) {
            if (ferries.get(i).getNameEnum().equals(CarNameEnum.CONSOLIDATION_POURING_FERRY)){
                closingGoPourFerry = ferries.get(i);
            }
        }
        for (int i = 0; i < subCars.size(); i++) {
            if (subCars.get(i).getNameEnum().equals(CarNameEnum.CONSOLIDATION_POURING_SUB_CAR)){
                closingGoPourSubCar = subCars.get(i);
            }
        }

        //查询待浇筑的点位
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getNameEnum().getCode().equals(positionCode)){
                upPosition_1.add(positions.get(i));
                positionCode++;
                i = 0;
                if (positionCode.equals(PositionNameEnum.POURING_UP_SECOND_POSITION_1.getCode()))
                    break;
            }
        }
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getNameEnum().getCode().equals(positionCode)){
                upPosition_2.add(positions.get(i));
                positionCode++;
                i = 0;
                if (positionCode.equals(PositionNameEnum.POURING_UP_THIRD_POSITION_1.getCode()))
                    break;
            }
        }
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getNameEnum().getCode().equals(positionCode)){
                upPosition_3.add(positions.get(i));
                positionCode++;
                i = 0;
                if (positionCode.equals(PositionNameEnum.POURING_UP_FOURTH_POSITION_1.getCode()))
                    break;
            }
        }
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getNameEnum().getCode().equals(positionCode)){
                upPosition_4.add(positions.get(i));
                positionCode++;
                i = 0;
                if (positionCode.equals(PositionNameEnum.POURING_UP_FIFTH_POSITION_1.getCode()))
                    break;
            }
        }
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getNameEnum().getCode().equals(positionCode)){
                upPosition_5.add(positions.get(i));
                positionCode++;
                i = 0;
                if (positionCode.equals(PositionNameEnum.POURING_DOWN_FIFTH_POSITION_1.getCode()))
                    break;
            }
        }
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getNameEnum().getCode().equals(positionCode)){
                downPosition_1.add(positions.get(i));
                positionCode++;
                i = 0;
                if (positionCode.equals(PositionNameEnum.POURING_DOWN_SECOND_POSITION_1.getCode()))
                    break;
            }
        }
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getNameEnum().getCode().equals(positionCode)){
                downPosition_2.add(positions.get(i));
                positionCode++;
                i = 0;
                if (positionCode.equals(PositionNameEnum.POURING_DOWN_THIRD_POSITION_1.getCode()))
                    break;
            }
        }
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getNameEnum().getCode().equals(positionCode)){
                downPosition_3.add(positions.get(i));
                positionCode++;
                i = 0;
                if (positionCode.equals(PositionNameEnum.POURING_DOWN_FOURTH_POSITION_1.getCode()))
                    break;
            }
        }
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getNameEnum().getCode().equals(positionCode)){
                downPosition_4.add(positions.get(i));
                positionCode++;
                i = 0;
                if (positionCode.equals(PositionNameEnum.POURING_DOWN_FIFTH_POSITION_1.getCode()))
                    break;
            }
        }
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getNameEnum().getCode().equals(positionCode)){
                downPosition_5.add(positions.get(i));
                positionCode++;
                i = 0;
                if (positionCode > PositionNameEnum.POURING_DOWN_FIFTH_POSITION_10.getCode())
                    break;
            }
        }

        //生成待浇筑区的仿真输入的点位二维列表
        upPositions.add(upPosition_1);
        upPositions.add(upPosition_2);
        upPositions.add(upPosition_3);
        upPositions.add(upPosition_4);
        upPositions.add(upPosition_5);
        downPositions.add(downPosition_1);
        downPositions.add(downPosition_2);
        downPositions.add(downPosition_3);
        downPositions.add(downPosition_4);
        downPositions.add(downPosition_5);

        //查询待浇筑的轨道
        for (int i = 0; i < paths.size(); i++) {
            if (paths.get(i).getNameEnum().equals(PathNameEnum.CONSOLIDATION_POURING_UNBOXING_PATH)){
                closingGoPourPath = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.WAIT_POURING_PATH_1)){
                waitingPourDownPath_1 = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.WAIT_POURING_PATH_2)){
                waitingPourDownPath_2 = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.WAIT_POURING_PATH_3)){
                waitingPourDownPath_3 = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.WAIT_POURING_PATH_4)){
                waitingPourDownPath_4 = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.WAIT_POURING_PATH_5)){
                waitingPourDownPath_5 = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.WAIT_POURING_PATH_6)){
                waitingPourUpPath_1 = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.WAIT_POURING_PATH_7)){
                waitingPourUpPath_2 = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.WAIT_POURING_PATH_8)){
                waitingPourUpPath_3 = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.WAIT_POURING_PATH_9)){
                waitingPourUpPath_4 = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.WAIT_POURING_PATH_10)){
                waitingPourUpPath_5 = paths.get(i);
            }
        }

        List<EventLinkGantt> eventLinkGantts = new ArrayList<>();
        int ganttIndex = 0;
        List<Event> eventFerrys_1 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(closingGoPourFerry.getId(), FERRY, eventFerrys_1));
        waitingPourIndexInput.setClosingBoxGoPourFerryInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventSubCar_1 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(closingGoPourSubCar.getId(), SUB_CAR, eventSubCar_1));
        waitingPourIndexInput.setClosingBoxGoPourSubCarInGanttIndex(ganttIndex);

        GanttChart ganttChart = new GanttChart(5L, "WaitingPourGanttChart", AreaEnum.SOUTH_WAITING_POURING, eventLinkGantts);
        WaitingPourSimulationInput waitingPourSimulationInput = new WaitingPourSimulationInput(closingGoPourFerry, closingGoPourSubCar,
                upPositions, downPositions, closingGoPourPath, waitingPourUpPath_1, waitingPourUpPath_2, waitingPourUpPath_3, waitingPourUpPath_4,
                waitingPourUpPath_5, waitingPourDownPath_1, waitingPourDownPath_2, waitingPourDownPath_3, waitingPourDownPath_4, waitingPourDownPath_5,
                waitingPourIndexInput, totalTime);

        return new WaitingPourGanttChartLinkInput(ganttChart, waitingPourSimulationInput);
    }

    /**
     * 生成冷却区的模拟列表
     * @return 模拟冷却区的半区列表
     */
    public List<List<ImitateCoolingPosition>> createImitates() {
        List<List<ImitateCoolingPosition>> result = new ArrayList<>();
        List<ImitateCoolingPosition> first = new ArrayList<>();
        List<ImitateCoolingPosition> second = new ArrayList<>();
        List<ImitateCoolingPosition> third = new ArrayList<>();
        List<ImitateCoolingPosition> fourth = new ArrayList<>();
        List<ImitateCoolingPosition> fifth = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            first.add(new ImitateCoolingPosition(0.0, 0.0));
        }
        for (int i = 0; i < 10; i++) {
            second.add(new ImitateCoolingPosition(0.0, 0.0));
        }
        for (int i = 0; i < 10; i++) {
            third.add(new ImitateCoolingPosition(0.0, 0.0));
        }
        for (int i = 0; i < 10; i++) {
            fourth.add(new ImitateCoolingPosition(0.0, 0.0));
        }
        for (int i = 0; i < 10; i++) {
            fifth.add(new ImitateCoolingPosition(0.0, 0.0));
        }
        result.add(first);
        result.add(second);
        result.add(third);
        result.add(fourth);
        result.add(fifth);

        return result;
    }

    public PouringGanttChartLinkInput handlePouringInput(SimulateInput input, double totalTime) {
        List<SubCar> subCars = new ArrayList<>();
        List<Ferry> ferries = new ArrayList<>();
        List<Equipment> equipments = new ArrayList<>();
        List<Position> positions = new ArrayList<>();
        List<WorkStation> workStations = new ArrayList<>();
        List<Path> paths = new ArrayList<>();

        for (int i = 0; i < input.getAllocationInputs().size(); i++) {
            if (input.getAllocationInputs().get(i).getRegion().equals(AreaEnum.SOUTH_POURING)){
                subCars = input.getAllocationInputs().get(i).getSubCars();
                ferries = input.getAllocationInputs().get(i).getFerries();
                equipments = input.getAllocationInputs().get(i).getEquipments();
                positions = input.getAllocationInputs().get(i).getPositions();
                workStations = input.getAllocationInputs().get(i).getWorkStations();
                paths = input.getAllocationInputs().get(i).getPaths();
            }
        }

        Ferry pouringFerry = new Ferry();
        SubCar pouringSubCar = new SubCar();
        List<WorkStation> workingStations = new ArrayList<>();
        Path pouringPath = new Path();
        PouringIndexInput pouringIndexInput = new PouringIndexInput();
        int workStationIndex = 0;

        for (int i = 0; i < ferries.size(); i++) {
            if (ferries.get(i).getNameEnum().equals(CarNameEnum.POURING_DOWN_FERRY)){
                pouringFerry = ferries.get(i);
            }
        }
        for (int i = 0; i < subCars.size(); i++) {
            if (subCars.get(i).getNameEnum().equals(CarNameEnum.POURING_DOWN_SUB_CAR)){
                pouringSubCar = subCars.get(i);
            }
        }

        for (int i = 0; i < paths.size(); i++) {
            if (paths.get(i).getNameEnum().equals(PathNameEnum.POURING_PATH)){
                pouringPath = paths.get(i);
            }
        }

        for (int i = 0; i < workStations.size(); i++) {
            if (workStations.get(i).getNameEnum().equals(POURING_WORKSTATION_15T_1)){
                workingStations.add(workStations.get(i));
                pouringIndexInput.setPouringWorkStationIndex_1(workStationIndex);
                workStationIndex++;
            }
            if (workStations.get(i).getNameEnum().equals(POURING_WORKSTATION_15T_2)){
                workingStations.add(workStations.get(i));
                pouringIndexInput.setPouringWorkStationIndex_2(workStationIndex);
                workStationIndex++;
            }
            if (workStations.get(i).getNameEnum().equals(POURING_WORKSTATION_15T_3)){
                workingStations.add(workStations.get(i));
                pouringIndexInput.setPouringWorkStationIndex_3(workStationIndex);
                workStationIndex++;
            }
            if (workStations.get(i).getNameEnum().equals(POURING_WORKSTATION_15T_4)){
                workingStations.add(workStations.get(i));
                pouringIndexInput.setPouringWorkStationIndex_4(workStationIndex);
                workStationIndex++;
            }
            if (workStations.get(i).getNameEnum().equals(POURING_WORKSTATION_30T_1)){
                workingStations.add(workStations.get(i));
                pouringIndexInput.setPouringWorkStationIndex_5(workStationIndex);
                workStationIndex++;
            }
            if (workStations.get(i).getNameEnum().equals(POURING_WORKSTATION_30T_2)){
                workingStations.add(workStations.get(i));
                pouringIndexInput.setPouringWorkStationIndex_6(workStationIndex);
                workStationIndex++;
            }
            if (workStations.get(i).getNameEnum().equals(POURING_WORKSTATION_30T_3)){
                workingStations.add(workStations.get(i));
                pouringIndexInput.setPouringWorkStationIndex_7(workStationIndex);
                workStationIndex++;
            }
            if (workStations.get(i).getNameEnum().equals(POURING_WORKSTATION_30T_4)){
                workingStations.add(workStations.get(i));
                pouringIndexInput.setPouringWorkStationIndex_8(workStationIndex);
                workStationIndex++;
            }
            if (workStations.get(i).getNameEnum().equals(POURING_WORKSTATION_30T_5)){
                workingStations.add(workStations.get(i));
                pouringIndexInput.setPouringWorkStationIndex_9(workStationIndex);
                workStationIndex++;
            }
            if (workStations.get(i).getNameEnum().equals(POURING_WORKSTATION_30T_6)){
                workingStations.add(workStations.get(i));
                pouringIndexInput.setPouringWorkStationIndex_10(workStationIndex);
                workStationIndex++;
            }
            if (workStations.get(i).getNameEnum().equals(POURING_WORKSTATION_30T_7)){
                workingStations.add(workStations.get(i));
                pouringIndexInput.setPouringWorkStationIndex_11(workStationIndex);
                workStationIndex++;
            }
            if (workStations.get(i).getNameEnum().equals(POURING_WORKSTATION_30T_8)){
                workingStations.add(workStations.get(i));
                pouringIndexInput.setPouringWorkStationIndex_12(workStationIndex);
                workStationIndex++;
            }

        }

        List<Event> eventFerrys_1 = new ArrayList<>();
        List<EventLinkGantt> eventLinkGantts = new ArrayList<>();
        int ganttIndex = 0;

        eventLinkGantts.add(new EventLinkGantt(pouringFerry.getId(), FERRY, eventFerrys_1));
        pouringIndexInput.setPouringFerryInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventSubCar_1 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(pouringSubCar.getId(), SUB_CAR, eventSubCar_1));
        pouringIndexInput.setPouringSubCarInGanttIndex(ganttIndex);
        ganttIndex++;

        for (int i = 0; i < workStations.size(); i++) {

            List<Event> eventWorkSattions = new ArrayList<>();
            eventLinkGantts.add(new EventLinkGantt(workStations.get(i).getId(), WORK_STATION, eventWorkSattions));

            if (workStations.get(i).getNameEnum().equals(POURING_WORKSTATION_15T_1)){
                pouringIndexInput.setPouringWorkStationInGanttIndex_1(ganttIndex);
                ganttIndex++;

            }else if (workStations.get(i).getNameEnum().equals(POURING_WORKSTATION_15T_2)){
                pouringIndexInput.setPouringWorkStationInGanttIndex_2(ganttIndex);
                ganttIndex++;

            }else if (workStations.get(i).getNameEnum().equals(POURING_WORKSTATION_15T_3)){
                pouringIndexInput.setPouringWorkStationInGanttIndex_3(ganttIndex);
                ganttIndex++;

            }else if (workStations.get(i).getNameEnum().equals(POURING_WORKSTATION_15T_4)){
                pouringIndexInput.setPouringWorkStationInGanttIndex_4(ganttIndex);
                ganttIndex++;

            }else if (workStations.get(i).getNameEnum().equals(POURING_WORKSTATION_30T_1)){
                pouringIndexInput.setPouringWorkStationInGanttIndex_5(ganttIndex);
                ganttIndex++;

            }else if (workStations.get(i).getNameEnum().equals(POURING_WORKSTATION_30T_2)){
                pouringIndexInput.setPouringWorkStationInGanttIndex_6(ganttIndex);
                ganttIndex++;

            }else if (workStations.get(i).getNameEnum().equals(POURING_WORKSTATION_30T_3)){
                pouringIndexInput.setPouringWorkStationInGanttIndex_7(ganttIndex);
                ganttIndex++;

            }else if (workStations.get(i).getNameEnum().equals(POURING_WORKSTATION_30T_4)){
                pouringIndexInput.setPouringWorkStationInGanttIndex_8(ganttIndex);
                ganttIndex++;

            }else if (workStations.get(i).getNameEnum().equals(POURING_WORKSTATION_30T_5)){
                pouringIndexInput.setPouringWorkStationInGanttIndex_9(ganttIndex);
                ganttIndex++;

            }else if (workStations.get(i).getNameEnum().equals(POURING_WORKSTATION_30T_6)){
                pouringIndexInput.setPouringWorkStationInGanttIndex_10(ganttIndex);
                ganttIndex++;

            }else if (workStations.get(i).getNameEnum().equals(POURING_WORKSTATION_30T_7)){
                pouringIndexInput.setPouringWorkStationInGanttIndex_11(ganttIndex);
                ganttIndex++;

            }else if (workStations.get(i).getNameEnum().equals(POURING_WORKSTATION_30T_8)){
                pouringIndexInput.setPouringWorkStationInGanttIndex_12(ganttIndex);
                ganttIndex++;

            }
        }

        GanttChart ganttChart = new GanttChart(6L, "PouringGanttChart", AreaEnum.SOUTH_POURING, eventLinkGantts);
        PouringSimulationInput pouringSimulationInput = new PouringSimulationInput(pouringFerry, pouringSubCar, workingStations,
                pouringPath, pouringIndexInput, totalTime);

        return new PouringGanttChartLinkInput(ganttChart, pouringSimulationInput);
    }

    public CoolingGanttChartLinkInput handleCoolingInput(SimulateInput input, double totalTime, Path coolingGoOpenBoxPath) {
        List<SubCar> subCars = new ArrayList<>();
        List<Ferry> ferries = new ArrayList<>();
        List<Equipment> equipments = new ArrayList<>();
        List<Position> positions = new ArrayList<>();
        List<WorkStation> workStations = new ArrayList<>();
        List<Path> paths = new ArrayList<>();

        for (int i = 0; i < input.getAllocationInputs().size(); i++) {
            if (input.getAllocationInputs().get(i).getRegion().equals(AreaEnum.SOUTH_COOLING)){
                subCars = input.getAllocationInputs().get(i).getSubCars();
                ferries = input.getAllocationInputs().get(i).getFerries();
                equipments = input.getAllocationInputs().get(i).getEquipments();
                positions = input.getAllocationInputs().get(i).getPositions();
                workStations = input.getAllocationInputs().get(i).getWorkStations();
                paths = input.getAllocationInputs().get(i).getPaths();
            }
        }

        Ferry coolingFerry = new Ferry();
        SubCar coolingSubCar = new SubCar();
        List<List<Position>> upPositions = new ArrayList<>();
        List<List<Position>> downPositions = new ArrayList<>();
        List<Position> upPosition_1 = new ArrayList<>();
        List<Position> upPosition_2 = new ArrayList<>();
        List<Position> upPosition_3 = new ArrayList<>();
        List<Position> upPosition_4 = new ArrayList<>();
        List<Position> upPosition_5 = new ArrayList<>();
        List<Position> downPosition_1 = new ArrayList<>();
        List<Position> downPosition_2 = new ArrayList<>();
        List<Position> downPosition_3 = new ArrayList<>();
        List<Position> downPosition_4 = new ArrayList<>();
        List<Position> downPosition_5 = new ArrayList<>();
        Integer positionCode = PositionNameEnum.COOLING_UP_FIRST_POSITION_1.getCode();
        Path coolingUpPath_1 = new Path();
        Path coolingUpPath_2 = new Path();
        Path coolingUpPath_3 = new Path();
        Path coolingUpPath_4 = new Path();
        Path coolingUpPath_5 = new Path();
        Path coolingDownPath_1 = new Path();
        Path coolingDownPath_2 = new Path();
        Path coolingDownPath_3 = new Path();
        Path coolingDownPath_4 = new Path();
        Path coolingDownPath_5 = new Path();
        CoolingIndexInput coolingIndexInput = new CoolingIndexInput();

        //查询冷却的车辆
        for (int i = 0; i < ferries.size(); i++) {
            if (ferries.get(i).getNameEnum().equals(CarNameEnum.COOLING_UNBOXING_FERRY)){
                coolingFerry = ferries.get(i);
            }
        }
        for (int i = 0; i < subCars.size(); i++) {
            if (subCars.get(i).getNameEnum().equals(CarNameEnum.COOLING_UNBOXING_SUB_CAR)){
                coolingSubCar = subCars.get(i);
            }
        }

        //查询冷却的点位
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getNameEnum().getCode().equals(positionCode)){
                upPosition_1.add(positions.get(i));
                positionCode++;
                i = 0;
                if (positionCode.equals(PositionNameEnum.COOLING_UP_SECOND_POSITION_1.getCode()))
                    break;
            }
        }
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getNameEnum().getCode().equals(positionCode)){
                upPosition_2.add(positions.get(i));
                positionCode++;
                i = 0;
                if (positionCode.equals(PositionNameEnum.COOLING_UP_THIRD_POSITION_1.getCode()))
                    break;
            }
        }
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getNameEnum().getCode().equals(positionCode)){
                upPosition_3.add(positions.get(i));
                positionCode++;
                i = 0;
                if (positionCode.equals(PositionNameEnum.COOLING_UP_FOURTH_POSITION_1.getCode()))
                    break;
            }
        }
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getNameEnum().getCode().equals(positionCode)){
                upPosition_4.add(positions.get(i));
                positionCode++;
                i = 0;
                if (positionCode.equals(PositionNameEnum.COOLING_UP_FIFTH_POSITION_1.getCode()))
                    break;
            }
        }
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getNameEnum().getCode().equals(positionCode)){
                upPosition_5.add(positions.get(i));
                positionCode++;
                i = 0;
                if (positionCode.equals(PositionNameEnum.COOLING_DOWN_FIFTH_POSITION_1.getCode()))
                    break;
            }
        }
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getNameEnum().getCode().equals(positionCode)){
                downPosition_1.add(positions.get(i));
                positionCode++;
                i = 0;
                if (positionCode.equals(PositionNameEnum.COOLING_DOWN_SECOND_POSITION_1.getCode()))
                    break;
            }
        }
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getNameEnum().getCode().equals(positionCode)){
                downPosition_2.add(positions.get(i));
                positionCode++;
                i = 0;
                if (positionCode.equals(PositionNameEnum.COOLING_DOWN_THIRD_POSITION_1.getCode()))
                    break;
            }
        }
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getNameEnum().getCode().equals(positionCode)){
                downPosition_3.add(positions.get(i));
                positionCode++;
                i = 0;
                if (positionCode.equals(PositionNameEnum.COOLING_DOWN_FOURTH_POSITION_1.getCode()))
                    break;
            }
        }
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getNameEnum().getCode().equals(positionCode)){
                downPosition_4.add(positions.get(i));
                positionCode++;
                i = 0;
                if (positionCode.equals(PositionNameEnum.COOLING_DOWN_FIFTH_POSITION_1.getCode()))
                    break;
            }
        }
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getNameEnum().getCode().equals(positionCode)){
                downPosition_5.add(positions.get(i));
                positionCode++;
                i = 0;
                if (positionCode > PositionNameEnum.COOLING_DOWN_FIFTH_POSITION_10.getCode())
                    break;
            }
        }

        //生成待浇筑区的仿真输入的点位二维列表
        upPositions.add(upPosition_1);
        upPositions.add(upPosition_2);
        upPositions.add(upPosition_3);
        upPositions.add(upPosition_4);
        upPositions.add(upPosition_5);
        downPositions.add(downPosition_1);
        downPositions.add(downPosition_2);
        downPositions.add(downPosition_3);
        downPositions.add(downPosition_4);
        downPositions.add(downPosition_5);

        for (int i = 0; i < paths.size(); i++) {
            if (paths.get(i).getNameEnum().equals(PathNameEnum.COOLING_PATH_1)){
                coolingDownPath_1 = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.COOLING_PATH_2)){
                coolingDownPath_2 = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.COOLING_PATH_3)){
                coolingDownPath_3 = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.COOLING_PATH_4)){
                coolingDownPath_4 = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.COOLING_PATH_5)){
                coolingDownPath_5 = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.COOLING_PATH_6)){
                coolingUpPath_1 = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.COOLING_PATH_7)){
                coolingUpPath_2 = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.COOLING_PATH_8)){
                coolingUpPath_3 = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.COOLING_PATH_9)){
                coolingUpPath_4 = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.COOLING_PATH_10)){
                coolingUpPath_5 = paths.get(i);
            }
        }

        List<EventLinkGantt> eventLinkGantts = new ArrayList<>();
        int ganttIndex = 0;
        List<Event> eventFerrys_1 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(coolingFerry.getId(), FERRY, eventFerrys_1));
        coolingIndexInput.setCoolingGoOpenBoxFerryInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventSubCar_1 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(coolingSubCar.getId(), SUB_CAR, eventSubCar_1));
        coolingIndexInput.setCoolingGoOpenBoxSubCarInGanttIndex(ganttIndex);

        for (List<Position> positions1: upPositions){
            for (Position position: positions1){
                List<Event> positionEvents = new ArrayList<>();
                eventLinkGantts.add(new EventLinkGantt(position.getId(), POSITION, positionEvents));
            }
        }

        for (List<Position> positions1: downPositions){
            for (Position position: positions1){
                List<Event> positionEvents = new ArrayList<>();
                eventLinkGantts.add(new EventLinkGantt(position.getId(), POSITION, positionEvents));
            }
        }

        GanttChart ganttChart = new GanttChart(7L, "CoolingGanttChart", AreaEnum.SOUTH_COOLING, eventLinkGantts);
        CoolingSimulationInput coolingSimulationInput = new CoolingSimulationInput(coolingFerry, coolingSubCar,
                upPositions, downPositions, coolingGoOpenBoxPath, coolingUpPath_1, coolingUpPath_2, coolingUpPath_3, coolingUpPath_4,
                coolingUpPath_5, coolingDownPath_1, coolingDownPath_2, coolingDownPath_3, coolingDownPath_4, coolingDownPath_5, coolingIndexInput, totalTime);

        return new CoolingGanttChartLinkInput(ganttChart, coolingSimulationInput);
    }

    public OpenBoxGanttChartLinkInput handleOpenBoxInput(SimulateInput input, double totalTime) {
        List<SubCar> subCars = new ArrayList<>();
        List<Ferry> ferries = new ArrayList<>();
        List<Equipment> equipments = new ArrayList<>();
        List<Position> positions = new ArrayList<>();
        List<WorkStation> workStations_1 = new ArrayList<>();
        List<Path> paths = new ArrayList<>();

        for (int i = 0; i < input.getAllocationInputs().size(); i++) {
            if (input.getAllocationInputs().get(i).getRegion().equals(AreaEnum.SOUTH_OPEN_BOX)){
                subCars = input.getAllocationInputs().get(i).getSubCars();
                ferries = input.getAllocationInputs().get(i).getFerries();
                equipments = input.getAllocationInputs().get(i).getEquipments();
                positions = input.getAllocationInputs().get(i).getPositions();
                workStations_1 = input.getAllocationInputs().get(i).getWorkStations();
                paths = input.getAllocationInputs().get(i).getPaths();
            }
        }

        Ferry coldAndHotFerry = new Ferry();
        Ferry hotFerry = new Ferry();
        SubCar coldAndHotSubCar = new SubCar();
        SubCar hotSubCar = new SubCar();
        SubCar castingUpSubCar = new SubCar();
        SubCar castingDownSubCar = new SubCar();
        SubCar castingVerticalSubCar = new SubCar();
        SubCar sandBoxRefluxRightSubCar = new SubCar();
        Position coldAndHotOpenBoxPosition = new Position();
        Position hotOpenBoxPosition = new Position();
        Position coldAndHotRefluxPosition = new Position();
        Position castingUpPosition = new Position();
        Position castingDownPosition = new Position();
        Position castingVerticalPosition = new Position();
        Position castingShippingPosition = new Position();
        List<WorkStation> workStations = new ArrayList<>();
        Path coldAndHotOpenBoxPath = new Path();
        Path hotOpenBoxPath = new Path();
        Path castingUpPath = new Path();
        Path castingDownPath = new Path();
        Path castingVerticalPath = new Path();
        Path sandBoxRefluxLeftPath = new Path();
        Path sandBoxRefluxRightPath = new Path();
        OpenBoxIndexInput openBoxIndexInput = new OpenBoxIndexInput();
        int workStationIndex = 0;

        for (int i = 0; i < ferries.size(); i++) {
            if (ferries.get(i).getNameEnum().equals(CarNameEnum.COLD_AND_HOT_MIXED_UNBOXING_FERRY)){
                coldAndHotFerry = ferries.get(i);
            }
            if (ferries.get(i).getNameEnum().equals(CarNameEnum.HOT_SAND_UNBOXING_FERRY)){
                hotFerry = ferries.get(i);
            }
        }
        for (int i = 0; i < subCars.size(); i++) {
            if (subCars.get(i).getNameEnum().equals(CarNameEnum.COLD_AND_HOT_MIXED_UNBOXING_SUB_CAR)){
                coldAndHotSubCar = subCars.get(i);
            }
            if (subCars.get(i).getNameEnum().equals(CarNameEnum.HOT_SAND_UNBOXING_SUB_CAR)){
                hotSubCar = subCars.get(i);
            }
            if (subCars.get(i).getNameEnum().equals(CarNameEnum.CASTING_REFLUX_UP_SUB_CAR)){
                castingUpSubCar = subCars.get(i);
            }
            if (subCars.get(i).getNameEnum().equals(CarNameEnum.CASTING_REFLUX_DOWN_SUB_CAR)){
                castingDownSubCar = subCars.get(i);
            }
            if (subCars.get(i).getNameEnum().equals(CarNameEnum.CASTING_REFLUX_VERTICAL_SUB_CAR)){
                castingVerticalSubCar = subCars.get(i);
            }
            if (subCars.get(i).getNameEnum().equals(CarNameEnum.SAND_BOX_REFLUX_RIGHT_SUB_CAR)){
                sandBoxRefluxRightSubCar = subCars.get(i);
            }
        }

        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).getNameEnum().equals(HOT_SAND_AND_COLD_SAND_MIXING_UNBOXING_POSITION)){
                coldAndHotOpenBoxPosition = positions.get(i);
            }
            if (positions.get(i).getNameEnum().equals(HOT_SAND_UNBOXING_POSITION)){
                hotOpenBoxPosition = positions.get(i);
            }
            if (positions.get(i).getNameEnum().equals(HOT_SAND_AND_COLD_SAND_MIXED_UNBOXING_REFLUX_POSITION_2)){
                coldAndHotRefluxPosition = positions.get(i);
            }
            if (positions.get(i).getNameEnum().equals(CASTING_REFLUX_UP_POSITION)){
                castingUpPosition = positions.get(i);
            }

            if (positions.get(i).getNameEnum().equals(CASTING_REFLUX_DOWN_POSITION)){
                castingDownPosition = positions.get(i);
            }
            if (positions.get(i).getNameEnum().equals(CASTING_REFLUX_VERTICALINTERACTION_POSITION)){
                castingVerticalPosition = positions.get(i);
            }
            if (positions.get(i).getNameEnum().equals(CASTING_REFLUX_SHIPPING_POSITION)){
                castingShippingPosition = positions.get(i);
            }
        }

        for (int i = 0; i < paths.size(); i++) {
            if (paths.get(i).getNameEnum().equals(PathNameEnum.COLD_AND_HOT_MIXED_UNBOXING_PATH)){
                coldAndHotOpenBoxPath = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.HOT_SAND_UNBOXING_PATH)){
                hotOpenBoxPath = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.CASTING_REFLUX_UP_PATH)){
                castingUpPath = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.CASTING_REFLUX_DOWN_PATH)){
                castingDownPath = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.CASTING_REFLUX_VERTICAL_PATH)){
                castingVerticalPath = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.SAND_BOX_REFLUX_LEFT_PATH)){
                sandBoxRefluxLeftPath = paths.get(i);
            }
            if (paths.get(i).getNameEnum().equals(PathNameEnum.SAND_BOX_REFLUX_RIGHT_PATH)){
                sandBoxRefluxRightPath = paths.get(i);
            }
        }

        for (int i = 0; i < workStations_1.size(); i++) {
            if (workStations_1.get(i).getNameEnum().equals(HOT_SAND_UNBOXING_WORKSTATION_1)){
                workStations.add(workStations_1.get(i));
                openBoxIndexInput.setHotWorkStation_1(workStationIndex);
                workStationIndex++;
            }
            if (workStations_1.get(i).getNameEnum().equals(HOT_SAND_UNBOXING_WORKSTATION_2)){
                workStations.add(workStations_1.get(i));
                openBoxIndexInput.setHotWorkStation_2(workStationIndex);
                workStationIndex++;
            }
            if (workStations_1.get(i).getNameEnum().equals(HOT_SAND_UNBOXING_WORKSTATION_3)){
                workStations.add(workStations_1.get(i));
                openBoxIndexInput.setHotWorkStation_3(workStationIndex);
                workStationIndex++;
            }
            if (workStations_1.get(i).getNameEnum().equals(HOT_SAND_UNBOXING_WORKSTATION_4)){
                workStations.add(workStations_1.get(i));
                openBoxIndexInput.setHotWorkStation_4(workStationIndex);
                workStationIndex++;
            }
            if (workStations_1.get(i).getNameEnum().equals(COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_1)){
                workStations.add(workStations_1.get(i));
                openBoxIndexInput.setColdAndHotWorkStation_1(workStationIndex);
                workStationIndex++;
            }
            if (workStations_1.get(i).getNameEnum().equals(COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_2)){
                workStations.add(workStations_1.get(i));
                openBoxIndexInput.setColdAndHotWorkStation_2(workStationIndex);
                workStationIndex++;
            }
            if (workStations_1.get(i).getNameEnum().equals(COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_3)){
                workStations.add(workStations_1.get(i));
                openBoxIndexInput.setColdAndHotWorkStation_3(workStationIndex);
                workStationIndex++;
            }
            if (workStations_1.get(i).getNameEnum().equals(COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_4)){
                workStations.add(workStations_1.get(i));
                openBoxIndexInput.setColdAndHotWorkStation_4(workStationIndex);
                workStationIndex++;
            }

        }

        List<Event> eventFerrys_1 = new ArrayList<>();
        List<EventLinkGantt> eventLinkGantts = new ArrayList<>();
        int ganttIndex = 0;

        eventLinkGantts.add(new EventLinkGantt(coldAndHotFerry.getId(), FERRY, eventFerrys_1));
        openBoxIndexInput.setColdAndHotFerryInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventFerrys_2 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(hotFerry.getId(), FERRY, eventFerrys_2));
        openBoxIndexInput.setHotFerryInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventSubCar_1 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(coldAndHotSubCar.getId(), SUB_CAR, eventSubCar_1));
        openBoxIndexInput.setColdAndHotSubCarInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventSubCar_2 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(hotSubCar.getId(), SUB_CAR, eventSubCar_2));
        openBoxIndexInput.setHotSubCarInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventSubCar_3 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(castingUpSubCar.getId(), SUB_CAR, eventSubCar_3));
        openBoxIndexInput.setCastingUpSubCarInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventSubCar_4 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(castingDownSubCar.getId(), SUB_CAR, eventSubCar_4));
        openBoxIndexInput.setCastingDownSubCarInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventSubCar_5 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(castingVerticalSubCar.getId(), SUB_CAR, eventSubCar_5));
        openBoxIndexInput.setCastingVerticalSubCarInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventSubCar_6 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(sandBoxRefluxRightSubCar.getId(), SUB_CAR, eventSubCar_6));
        openBoxIndexInput.setSandBoxRefluxRightSubCarInGanttIndex(ganttIndex);
        ganttIndex++;

        for (int i = 0; i < workStations_1.size(); i++) {

            List<Event> eventWorkSattions = new ArrayList<>();
            eventLinkGantts.add(new EventLinkGantt(workStations_1.get(i).getId(), WORK_STATION, eventWorkSattions));

            if (workStations_1.get(i).getNameEnum().equals(HOT_SAND_UNBOXING_WORKSTATION_1)){
                openBoxIndexInput.setHotWorkStationInGanttIndex_1(ganttIndex);
                ganttIndex++;

            }else if (workStations_1.get(i).getNameEnum().equals(HOT_SAND_UNBOXING_WORKSTATION_2)){
                openBoxIndexInput.setHotWorkStationInGanttIndex_2(ganttIndex);
                ganttIndex++;

            }else if (workStations_1.get(i).getNameEnum().equals(HOT_SAND_UNBOXING_WORKSTATION_3)){
                openBoxIndexInput.setHotWorkStationInGanttIndex_3(ganttIndex);
                ganttIndex++;

            }else if (workStations_1.get(i).getNameEnum().equals(HOT_SAND_UNBOXING_WORKSTATION_4)){
                openBoxIndexInput.setHotWorkStationInGanttIndex_4(ganttIndex);
                ganttIndex++;

            }else if (workStations_1.get(i).getNameEnum().equals(COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_1)){
                openBoxIndexInput.setColdAndHotWorkStationInGanttIndex_1(ganttIndex);
                ganttIndex++;

            }else if (workStations_1.get(i).getNameEnum().equals(COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_2)){
                openBoxIndexInput.setColdAndHotWorkStationInGanttIndex_2(ganttIndex);
                ganttIndex++;

            }else if (workStations_1.get(i).getNameEnum().equals(COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_3)){
                openBoxIndexInput.setColdAndHotWorkStationInGanttIndex_3(ganttIndex);
                ganttIndex++;

            }else if (workStations_1.get(i).getNameEnum().equals(COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_4)){
                openBoxIndexInput.setColdAndHotWorkStationInGanttIndex_4(ganttIndex);
                ganttIndex++;

            }else if (workStations_1.get(i).getNameEnum().equals(HOT_SAND_UNBOXING_WORKSTATION_5)) {
                openBoxIndexInput.setHotWorkStationInGanttIndex_5(ganttIndex);
                ganttIndex++;
            }else if (workStations_1.get(i).getNameEnum().equals(HOT_SAND_UNBOXING_WORKSTATION_6)) {
                openBoxIndexInput.setHotWorkStationInGanttIndex_6(ganttIndex);
                ganttIndex++;
            }else if (workStations_1.get(i).getNameEnum().equals(HOT_SAND_UNBOXING_WORKSTATION_7)) {
                openBoxIndexInput.setHotWorkStationInGanttIndex_7(ganttIndex);
                ganttIndex++;
            }else if (workStations_1.get(i).getNameEnum().equals(HOT_SAND_UNBOXING_WORKSTATION_8)) {
                openBoxIndexInput.setHotWorkStationInGanttIndex_8(ganttIndex);
                ganttIndex++;
            }else if (workStations_1.get(i).getNameEnum().equals(COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_5)){
                openBoxIndexInput.setColdAndHotWorkStationInGanttIndex_5(ganttIndex);
                ganttIndex++;

            }else if (workStations_1.get(i).getNameEnum().equals(COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_6)){
                openBoxIndexInput.setColdAndHotWorkStationInGanttIndex_6(ganttIndex);
                ganttIndex++;

            }else if (workStations_1.get(i).getNameEnum().equals(COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_7)){
                openBoxIndexInput.setColdAndHotWorkStationInGanttIndex_7(ganttIndex);
                ganttIndex++;

            }else if (workStations_1.get(i).getNameEnum().equals(COLD_AND_HOT_MIXED_UNBOXING_WORKSTATION_8)){
                openBoxIndexInput.setColdAndHotWorkStationInGanttIndex_8(ganttIndex);
                ganttIndex++;

            }
        }

        List<Event> eventPosition = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(null, POSITION, eventPosition));
        openBoxIndexInput.setPositionInGanttIndex(ganttIndex);

        GanttChart ganttChart = new GanttChart(8L, "OpenBoxGanttChart", AreaEnum.SOUTH_OPEN_BOX, eventLinkGantts);
        OpenBoxSimulationInput openBoxSimulationInput = new OpenBoxSimulationInput(coldAndHotFerry, coldAndHotSubCar, hotFerry,
                hotSubCar, castingUpSubCar, castingDownSubCar, castingVerticalSubCar, sandBoxRefluxRightSubCar, coldAndHotOpenBoxPosition,
                hotOpenBoxPosition, coldAndHotRefluxPosition, castingUpPosition, castingDownPosition, castingVerticalPosition, castingShippingPosition,
                workStations, coldAndHotOpenBoxPath, hotOpenBoxPath, castingUpPath, castingDownPath, castingVerticalPath, sandBoxRefluxLeftPath,
                sandBoxRefluxRightPath, openBoxIndexInput, totalTime);

        return new OpenBoxGanttChartLinkInput(ganttChart, openBoxSimulationInput);
    }
}
