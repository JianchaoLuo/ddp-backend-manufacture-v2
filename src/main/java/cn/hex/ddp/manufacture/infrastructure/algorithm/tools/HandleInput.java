package cn.hex.ddp.manufacture.infrastructure.algorithm.tools;

import cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentNameEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.ClosingBoxSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.MoldClosingSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.MouldingSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.OutBoundSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.closingBox.ClosingBoxGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.closingBox.ClosingBoxIndexInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.moldClosing.MoldClosingGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.moldClosing.MoldClosingIndexInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.mouldingLine.MouldingGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.mouldingLine.MouldingIndexInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound.OutBoundGanttChartLinkInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.outBound.OutBoundIndexInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.EventLinkGantt;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;
import org.apache.commons.lang3.SerializationUtils;

import java.util.*;

import static cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum.*;
import static cn.hex.ddp.manufacture.domain.common.enums.AreaEnum.SOUTH_MOULDING;
import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum.*;
import static cn.hex.ddp.manufacture.domain.path.enums.PathNameEnum.*;
import static cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationNameEnum.*;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum.*;

/**
 * @ClassDescription: 处理输入的初始化工具类
 * @Author: KangHong
 * @Created: 2024/6/6 19:32
 */
public class HandleInput {
    /**
     * 处理输入，生成出库队列
     * @param simulateInput 仿真输入
     * @return 出库队列
     */
    public Deque<Product> handleDeque(SimulateInput simulateInput, List<Double> doubles){
        SimulateInput input = SerializationUtils.clone(simulateInput);
        Deque<Product> deque = new LinkedList<>();
        if (!input.getOrderInputs().isEmpty()){
            Integer maxPriority;
            int maxIndex = 0;
            do {
                maxPriority = input.getOrderInputs().getFirst().getPriority();

                for (int i = 0; i < input.getOrderInputs().size(); i++) {
                    if (input.getOrderInputs().get(i).getPriority() >= maxPriority) {
                        maxPriority = input.getOrderInputs().get(i).getPriority();
                        maxIndex = i;
                    }
                }
                for (int i = 0; i < input.getOrderInputs().get(maxIndex).getOrderProducts().size(); i++) {

                    if (input.getOrderInputs().get(maxIndex).getOrderProducts().get(i).getAlreadyOutboundNum() %
                            input.getOrderInputs().get(maxIndex).getOrderProducts().get(i).getProducts().size() == 0) {

                        for (int j = 0; j < input.getOrderInputs().get(maxIndex).getOrderProducts().get(i).getNumber() -
                                (input.getOrderInputs().get(maxIndex).getOrderProducts().get(i).getAlreadyOutboundNum() /
                                        input.getOrderInputs().get(maxIndex).getOrderProducts().get(i).getProducts().size()); j++) {

                            for (int k = 0; k < input.getOrderInputs().get(maxIndex).getOrderProducts().get(i).getProducts().size(); k++) {

                                deque.offer(input.getOrderInputs().get(maxIndex).getOrderProducts().get(i).getProducts().get(k));
                            }
                        }
                    } else {
                        for (int j = input.getOrderInputs().get(maxIndex).getOrderProducts().get(i).getAlreadyOutboundNum() /
                                input.getOrderInputs().get(maxIndex).getOrderProducts().get(i).getProducts().size();
                             j < input.getOrderInputs().get(maxIndex).getOrderProducts().get(i).getAlreadyOutboundProducts().size(); j++) {

                            deque.offer(input.getOrderInputs().get(maxIndex).getOrderProducts().get(i).getProducts().get(j));
                        }

                        for (int j = 0; j < input.getOrderInputs().get(maxIndex).getOrderProducts().get(i).getNumber() -
                                (input.getOrderInputs().get(maxIndex).getOrderProducts().get(i).getAlreadyOutboundNum() /
                                        input.getOrderInputs().get(maxIndex).getOrderProducts().get(i).getProducts().size()) - 1; j++) {

                            for (int k = 0; k < input.getOrderInputs().get(maxIndex).getOrderProducts().get(i).getProducts().size(); k++) {

                                deque.offer(input.getOrderInputs().get(maxIndex).getOrderProducts().get(i).getProducts().get(k));
                            }
                        }
                    }

                    doubles.add(input.getOrderInputs().get(maxIndex).getOrderProducts().get(i).getUsage() * (
                            input.getOrderInputs().get(maxIndex).getOrderProducts().get(i).getNumber() -
                                    input.getOrderInputs().get(maxIndex).getOrderProducts().get(i).getFinishNum()
                    ));
                }

                input.getOrderInputs().remove(maxIndex);

            } while (!input.getOrderInputs().isEmpty());
        }

        return deque;
    }

    /**
     * 出库区的输入处理
     * @param input 仿真输入
     * @param totalTime 仿真开始的时间
     * @param productDeque 出库队列
     * @return 出库区的初始化甘特图和出库区输入
     */
    public OutBoundGanttChartLinkInput handleOutBoundInput(SimulateInput input, double totalTime, Deque<Product> productDeque){
        List<SubCar> subCars = new ArrayList<>();
        List<Ferry> ferries = new ArrayList<>();
        List<Position> positions = new ArrayList<>();
        List<WorkStation> workStations = new ArrayList<>();
        List<Path> paths = new ArrayList<>();

        for (int i = 0; i < input.getAllocationInputs().size(); i++) {
            if (input.getAllocationInputs().get(i).getRegion().equals(SOUTH_MOULDING)){
                subCars = input.getAllocationInputs().get(i).getSubCars();
                ferries = input.getAllocationInputs().get(i).getFerries();
                positions = input.getAllocationInputs().get(i).getPositions();
                workStations = input.getAllocationInputs().get(i).getWorkStations();
                paths = input.getAllocationInputs().get(i).getPaths();
            }
        }

        Ferry outboundFerry = new Ferry();
        SubCar sandBoxMoldingSubCar = new SubCar();
        List<SubCar> outboundSubCars = new ArrayList<>();
        List<Position> outboundPositions = new ArrayList<>();
        List<Path> outboundPaths = new ArrayList<>();
        OutBoundIndexInput outBoundIndexInput = new OutBoundIndexInput();
        int subCarIndex = 0;
        int positionIndex = 0;
        int pathIndex = 0;
        for (Ferry ferry : ferries) {
            if (ferry.getNameEnum().equals(OUT_BOUND_FERRY)) {
                outboundFerry = ferry;
            }
        }
        for (SubCar subCar : subCars) {
            if (subCar.getNameEnum().equals(OUT_BOUND_SUB_CAR)) {
                outboundSubCars.add(subCar);
                outBoundIndexInput.setOutBoundSubcarIndex(subCarIndex);
                subCarIndex++;
            } else if (subCar.getNameEnum().equals(SAND_BOX_REFLUX_ALL_SUB_CAR)) {
                outboundSubCars.add(subCar);
                outBoundIndexInput.setSandBoxRefluxSubcarIndex(subCarIndex);
                subCarIndex++;
            } else if (subCar.getNameEnum().equals(CORE_MAKING_SUB_CAR)) {
                outboundSubCars.add(subCar);
                outBoundIndexInput.setCoreMakingSubcarIndex(subCarIndex);
                subCarIndex++;
            } else if (subCar.getNameEnum().equals(MOLD_REFLUX_SUB_CAR)) {
                outboundSubCars.add(subCar);
                outBoundIndexInput.setMoldRefluxSubcarIndex(subCarIndex);
                subCarIndex++;
            } else if (subCar.getNameEnum().equals(COLD_SAND_BOX_REFLUX_SUB_CAR)) {
                outboundSubCars.add(subCar);
                outBoundIndexInput.setColdRefluxSubcarIndex(subCarIndex);
                subCarIndex++;
            } else if (subCar.getNameEnum().equals(HOT_SAND_BOX_REFLUX_AND_MOLDING_SUB_CAR)){
                sandBoxMoldingSubCar = subCar;
            }
        }
        for (Position position : positions) {
            if (position.getNameEnum().equals(HOT_SAND_MOLDING_POSITION)) {
                outboundPositions.add(position);
                outBoundIndexInput.setHotSandMoldingPositionIndex(positionIndex);
                positionIndex++;
            } else if (position.getNameEnum().equals(COLD_SAND_MOLDING_POSITION)) {
                outboundPositions.add(position);
                outBoundIndexInput.setColdSandMoldingPositionIndex(positionIndex);
                positionIndex++;
            } else if (position.getNameEnum().equals(SAND_BOX_REFLUX_ALL_POSITION)) {
                outboundPositions.add(position);
                outBoundIndexInput.setSandBoxRefluxPositionIndex(positionIndex);
                positionIndex++;
            } else if (position.getNameEnum().equals(CORE_MAKING_POSITION)) {
                outboundPositions.add(position);
                outBoundIndexInput.setCoreMakingPositionIndex(positionIndex);
                positionIndex++;
            } else if (position.getNameEnum().equals(CORE_MAKING_LAY_POSITION)) {
                outboundPositions.add(position);
                outBoundIndexInput.setCoreMakingLayPositionIndex(positionIndex);
                positionIndex++;
            } else if (position.getNameEnum().equals(MOLD_REFLUX_POSITION)) {
                outboundPositions.add(position);
                outBoundIndexInput.setMoldRefluxPositionIndex(positionIndex);
                positionIndex++;
            } else if (position.getNameEnum().equals(COLD_REFLUX_POSITION)) {
                outboundPositions.add(position);
                outBoundIndexInput.setColdRefluxPositionIndex(positionIndex);
                positionIndex++;
            } else if (position.getNameEnum().equals(HOT_SAND_UNBOXING_REFLUX_POSITION)) {
                outboundPositions.add(position);
                outBoundIndexInput.setHotSandBoxRefluxPositionIndex(positionIndex);
                positionIndex++;
            } else if (position.getNameEnum().equals(HOT_SAND_AND_COLD_SAND_MIXED_UNBOXING_REFLUX_POSITION)) {
                outboundPositions.add(position);
                outBoundIndexInput.setHotAndColdSandBoxRefluxPositionIndex(positionIndex);
                positionIndex++;
            } else if (position.getNameEnum().equals(HOT_SAND_VERTICAL_TRACK_REFLUX_INTERACTION_POSITION)) {
                outboundPositions.add(position);
                outBoundIndexInput.setHotSandVertiaclPositionIndex(positionIndex);
                positionIndex++;
            } else if (position.getNameEnum().equals(SAND_BOX_ROW_CAR_POSITION)) {
                outboundPositions.add(position);
                outBoundIndexInput.setSandBoxRowCarPositionIndex(positionIndex);
                positionIndex++;
            } else if (position.getNameEnum().equals(SAND_MOLD_REFLUX_RAIL_INTERACTION_POSITION_2)) {
                outboundPositions.add(position);
                outBoundIndexInput.setColdSandMoldRefluxPositionIndex(positionIndex);
                positionIndex++;
            } else if (position.getNameEnum().equals(SAND_MOLD_REFLUX_RAIL_INTERACTION_POSITION_1)) {
                outboundPositions.add(position);
                outBoundIndexInput.setHotSandMoldRefluxPositionIndex(positionIndex);
                positionIndex++;
            } else if (position.getNameEnum().equals(COLD_SAND_ROW_CAR_POSITION)) {
                outboundPositions.add(position);
                outBoundIndexInput.setColdSandRowCarPositionIndex(positionIndex);
                positionIndex++;
            }
        }
        for (Path path : paths) {
            if (path.getNameEnum().equals(OUT_BOUND_PATH)) {
                outboundPaths.add(path);
                outBoundIndexInput.setOutBoundPathIndex(pathIndex);
                pathIndex++;
            } else if (path.getNameEnum().equals(SAND_BOX_REFLUX_ALL_PATH)) {
                outboundPaths.add(path);
                outBoundIndexInput.setSandBoxRefluxPathIndex(pathIndex);
                pathIndex++;
            } else if (path.getNameEnum().equals(COLD_SAND_BOX_REFLUX_PATH)) {
                outboundPaths.add(path);
                outBoundIndexInput.setColdSandBoxRefluxPathIndex(pathIndex);
                pathIndex++;
            } else if (path.getNameEnum().equals(CORE_MAKING_PATH)) {
                outboundPaths.add(path);
                outBoundIndexInput.setCoreMakingPathIndex(pathIndex);
                pathIndex++;
            } else if (path.getNameEnum().equals(MOLD_REFLUX_PATH)) {
                outboundPaths.add(path);
                outBoundIndexInput.setMoldRefluxPathIndex(pathIndex);
                pathIndex++;
            }
        }

        List<Event> eventFerrys = new ArrayList<>();
        List<EventLinkGantt> eventLinkGantts = new ArrayList<>();
        int ganttIndex = 0;
        eventLinkGantts.add(new EventLinkGantt(outboundFerry.getId(), FERRY, eventFerrys));
        outBoundIndexInput.setOutBoundFerryInGanttIndex(ganttIndex);
        ganttIndex++;
        for (SubCar outboundSubCar : outboundSubCars) {
            List<Event> eventSubCars = new ArrayList<>();
            eventLinkGantts.add(new EventLinkGantt(outboundSubCar.getId(), SUB_CAR, eventSubCars));

            if (outboundSubCar.getNameEnum().equals(OUT_BOUND_SUB_CAR)) {
                outBoundIndexInput.setOutBoundSubCarInGanttIndex(ganttIndex);
                ganttIndex++;
            } else if (outboundSubCar.getNameEnum().equals(SAND_BOX_REFLUX_ALL_SUB_CAR)) {
                outBoundIndexInput.setSandBoxRefluxSubcarInGanttIndex(ganttIndex);
                ganttIndex++;
            } else if (outboundSubCar.getNameEnum().equals(CORE_MAKING_SUB_CAR)) {
                outBoundIndexInput.setCoreMakingSubcarInGanttIndex(ganttIndex);
                ganttIndex++;
            } else if (outboundSubCar.getNameEnum().equals(MOLD_REFLUX_SUB_CAR)) {
                outBoundIndexInput.setMoldRefluxSubcarInGanttIndex(ganttIndex);
                ganttIndex++;
            } else if (outboundSubCar.getNameEnum().equals(COLD_SAND_BOX_REFLUX_SUB_CAR)) {
                outBoundIndexInput.setColdRefluxSubcarInGanttIndex(ganttIndex);
                ganttIndex++;
            }
        }

        List<Event> eventPositions = new ArrayList<>();
        List<Event> eventSubCars = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(null, POSITION, eventPositions));
        outBoundIndexInput.setPositionInGanttIndex(ganttIndex);
        ganttIndex++;
        eventLinkGantts.add(new EventLinkGantt(sandBoxMoldingSubCar.getId(), SUB_CAR, eventSubCars));
        outBoundIndexInput.setHotSandMoldingSubcarInGanttIndex(ganttIndex);

        //新增的输入
        Map<PositionNameEnum, Position> positionMap = new HashMap<>();
        for (Position position : positions){
            if (position.getNameEnum().getCode() > PositionNameEnum.CASTING_REFLUX_SHIPPING_POSITION.getCode() &&
                    position.getNameEnum().getCode() <= PositionNameEnum.COLD_SAND_MOLD_CLOSING_WORKSTATION_ACQUISITION_POSITION_4.getCode()) {
                positionMap.put(position.getNameEnum(), position);
            }
        }

        GanttChart ganttChart = new GanttChart(1L, "OutBoundGanttChart", SOUTH_MOULDING, eventLinkGantts);
        OutBoundSimulationInput outBoundSimulationInput = new OutBoundSimulationInput(outboundFerry, outboundSubCars,
                workStations, outboundPositions, outboundPaths, outBoundIndexInput, totalTime, productDeque, positionMap, sandBoxMoldingSubCar);

        return new OutBoundGanttChartLinkInput(ganttChart, outBoundSimulationInput);
    }

    /**
     * 合模区的输入处理
     * @param input 仿真输入
     * @param totalTime 仿真总时间
     * @return 合模区输入
     */
    public MoldClosingGanttChartLinkInput handleMoldClosingInput(SimulateInput input, double totalTime){
        List<SubCar> subCars = new ArrayList<>();
        List<Ferry> ferries = new ArrayList<>();
        List<Position> positions = new ArrayList<>();
        List<WorkStation> workStations = new ArrayList<>();
        List<Path> paths = new ArrayList<>();

        for (int i = 0; i < input.getAllocationInputs().size(); i++) {
            if (input.getAllocationInputs().get(i).getRegion().equals(SOUTH_MOULDING)){
                subCars = input.getAllocationInputs().get(i).getSubCars();
                ferries = input.getAllocationInputs().get(i).getFerries();
                positions = input.getAllocationInputs().get(i).getPositions();
                workStations = input.getAllocationInputs().get(i).getWorkStations();
                paths = input.getAllocationInputs().get(i).getPaths();
            }
        }

        Ferry hotMoldClosingFerry = new Ferry();
        Ferry coldMoldClosingFerry = new Ferry();
        SubCar hotMoldClosingSubcar = new SubCar();
        SubCar coldMoldClosingSubcar = new SubCar();
        List<WorkStation> moldClosingWorkStations = new ArrayList<>();
        Position hotMoldClosingPosition = new Position();
        Position coldMoldClosingPosition = new Position();
        Position hotMoldClosingLayPosition = new Position();
        Position coldMoldClosingLayPosition = new Position();
        Path hotMoldClosingPath = new Path();
        Path coldMoldClosingPath = new Path();
        MoldClosingIndexInput moldClosingIndexInput = new MoldClosingIndexInput();
        int workStationIndex = 0;

        for (Ferry ferry : ferries) {
            if (ferry.getNameEnum().equals(HOT_MOLD_CLOSING_FERRY)) {
                hotMoldClosingFerry = ferry;
            }
            if (ferry.getNameEnum().equals(COLD_FERRY_CLOSING_FERRY)) {
                coldMoldClosingFerry = ferry;
            }
        }
        for (SubCar subCar : subCars) {
            if (subCar.getNameEnum().equals(HOT_MOLD_CLOSING_SUB_CAR)) {
                hotMoldClosingSubcar = subCar;
            }
            if (subCar.getNameEnum().equals(COLD_MOLD_CLOSING_SUB_CAR)) {
                coldMoldClosingSubcar = subCar;
            }
        }
        for (Position position : positions) {
            if (position.getNameEnum().equals(HOT_SAND_MOLDING_POSITION)) {
                hotMoldClosingPosition = position;
            }
            if (position.getNameEnum().equals(COLD_SAND_MOLDING_POSITION)) {
                coldMoldClosingPosition = position;
            }
            if (position.getNameEnum().equals(HOT_SAND_SHOOTING_POSITION)) {
                hotMoldClosingLayPosition = position;
            }
            if (position.getNameEnum().equals(COLD_SAND_SHOOTING_POSITION)) {
                coldMoldClosingLayPosition = position;
            }
        }
        for (WorkStation workStation : workStations) {
            if (workStation.getNameEnum().equals(HOT_MOLD_CLOSING_WORKSTATION_1)) {
                moldClosingWorkStations.add(workStation);
                moldClosingIndexInput.setHotMoldClosingWorkStationIndex_1(workStationIndex);
                workStationIndex++;
            }
            if (workStation.getNameEnum().equals(HOT_MOLD_CLOSING_WORKSTATION_2)) {
                moldClosingWorkStations.add(workStation);
                moldClosingIndexInput.setHotMoldClosingWorkStationIndex_2(workStationIndex);
                workStationIndex++;
            }
            if (workStation.getNameEnum().equals(HOT_MOLD_CLOSING_WORKSTATION_3)) {
                moldClosingWorkStations.add(workStation);
                moldClosingIndexInput.setHotMoldClosingWorkStationIndex_3(workStationIndex);
                workStationIndex++;
            }
            if (workStation.getNameEnum().equals(HOT_MOLD_CLOSING_WORKSTATION_4)) {
                moldClosingWorkStations.add(workStation);
                moldClosingIndexInput.setHotMoldClosingWorkStationIndex_4(workStationIndex);
                workStationIndex++;
            }
            if (workStation.getNameEnum().equals(COLD_MOLD_CLOSING_WORKSTATION_1)) {
                moldClosingWorkStations.add(workStation);
                moldClosingIndexInput.setColdMoldClosingWorkStationIndex_1(workStationIndex);
                workStationIndex++;
            }
            if (workStation.getNameEnum().equals(COLD_MOLD_CLOSING_WORKSTATION_2)) {
                moldClosingWorkStations.add(workStation);
                moldClosingIndexInput.setColdMoldClosingWorkStationIndex_2(workStationIndex);
                workStationIndex++;
            }
            if (workStation.getNameEnum().equals(COLD_MOLD_CLOSING_WORKSTATION_3)) {
                moldClosingWorkStations.add(workStation);
                moldClosingIndexInput.setColdMoldClosingWorkStationIndex_3(workStationIndex);
                workStationIndex++;
            }
            if (workStation.getNameEnum().equals(COLD_MOLD_CLOSING_WORKSTATION_4)) {
                moldClosingWorkStations.add(workStation);
                moldClosingIndexInput.setColdMoldClosingWorkStationIndex_4(workStationIndex);
                workStationIndex++;
            }
        }

        for (Path path : paths) {
            if (path.getNameEnum().equals(HOT_SAND_MOLDING_PATH)) {
                hotMoldClosingPath = path;
            }
            if (path.getNameEnum().equals(COLD_SAND_MOLDING_PATH)) {
                coldMoldClosingPath = path;
            }
        }

        List<Event> eventFerrys_1 = new ArrayList<>();
        List<EventLinkGantt> eventLinkGantts = new ArrayList<>();
        int ganttIndex = 0;
        eventLinkGantts.add(new EventLinkGantt(hotMoldClosingFerry.getId(), FERRY, eventFerrys_1));
        moldClosingIndexInput.setHotMoldClosingFerryInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventFerrys_2 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(coldMoldClosingFerry.getId(), FERRY, eventFerrys_2));
        moldClosingIndexInput.setColdMoldClosingFerryInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventSubCar_1 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(hotMoldClosingSubcar.getId(), SUB_CAR, eventSubCar_1));
        moldClosingIndexInput.setHotMoldClosingSubCarInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventSubCar_2 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(coldMoldClosingSubcar.getId(), SUB_CAR, eventSubCar_2));
        moldClosingIndexInput.setColdMoldClosingSubCarInGanttIndex(ganttIndex);
        ganttIndex++;

        for (WorkStation moldClosingWorkStation : moldClosingWorkStations) {

            List<Event> eventWorkSattions = new ArrayList<>();
            eventLinkGantts.add(new EventLinkGantt(moldClosingWorkStation.getId(), WORK_STATION, eventWorkSattions));

            if (moldClosingWorkStation.getNameEnum().equals(HOT_MOLD_CLOSING_WORKSTATION_1)) {
                moldClosingIndexInput.setHotMoldWorkStationInGanttIndex_1(ganttIndex);
                ganttIndex++;
            } else if (moldClosingWorkStation.getNameEnum().equals(HOT_MOLD_CLOSING_WORKSTATION_2)) {
                moldClosingIndexInput.setHotMoldWorkStationInGanttIndex_2(ganttIndex);
                ganttIndex++;
            } else if (moldClosingWorkStation.getNameEnum().equals(HOT_MOLD_CLOSING_WORKSTATION_3)) {
                moldClosingIndexInput.setHotMoldWorkStationInGanttIndex_3(ganttIndex);
                ganttIndex++;
            } else if (moldClosingWorkStation.getNameEnum().equals(HOT_MOLD_CLOSING_WORKSTATION_4)) {
                moldClosingIndexInput.setHotMoldWorkStationInGanttIndex_4(ganttIndex);
                ganttIndex++;
            } else if (moldClosingWorkStation.getNameEnum().equals(COLD_MOLD_CLOSING_WORKSTATION_1)) {
                moldClosingIndexInput.setColdMoldWorkStationInGanttIndex_1(ganttIndex);
                ganttIndex++;
            } else if (moldClosingWorkStation.getNameEnum().equals(COLD_MOLD_CLOSING_WORKSTATION_2)) {
                moldClosingIndexInput.setColdMoldWorkStationInGanttIndex_2(ganttIndex);
                ganttIndex++;
            } else if (moldClosingWorkStation.getNameEnum().equals(COLD_MOLD_CLOSING_WORKSTATION_3)) {
                moldClosingIndexInput.setColdMoldWorkStationInGanttIndex_3(ganttIndex);
                ganttIndex++;
            } else if (moldClosingWorkStation.getNameEnum().equals(COLD_MOLD_CLOSING_WORKSTATION_4)) {
                moldClosingIndexInput.setColdMoldWorkStationInGanttIndex_4(ganttIndex);
                ganttIndex++;
            }
        }

        GanttChart ganttChart = new GanttChart(2L, "MoldClosingGanttChart", SOUTH_MOULDING, eventLinkGantts);
        MoldClosingSimulationInput moldClosingSimulationInput = new MoldClosingSimulationInput(hotMoldClosingFerry, coldMoldClosingFerry, hotMoldClosingSubcar,
                coldMoldClosingSubcar, moldClosingWorkStations, hotMoldClosingPosition, coldMoldClosingPosition, hotMoldClosingLayPosition, coldMoldClosingLayPosition,
                hotMoldClosingPath, coldMoldClosingPath, moldClosingIndexInput, totalTime);

        return new MoldClosingGanttChartLinkInput(ganttChart, moldClosingSimulationInput);
    }

    /**
     * 造型线的输入处理
     * @param input 仿真输入
     * @param totalTime 仿真总时间
     * @return 合模区输入
     */
    public MouldingGanttChartLinkInput handleMouldingInput(SimulateInput input, double totalTime){
        List<SubCar> subCars = new ArrayList<>();
        List<Ferry> ferries = new ArrayList<>();
        List<Equipment> equipments = new ArrayList<>();
        List<Position> positions = new ArrayList<>();
        List<Path> paths = new ArrayList<>();

        for (int i = 0; i < input.getAllocationInputs().size(); i++) {
            if (input.getAllocationInputs().get(i).getRegion().equals(SOUTH_MOULDING)){
                subCars = input.getAllocationInputs().get(i).getSubCars();
                ferries = input.getAllocationInputs().get(i).getFerries();
                equipments = input.getAllocationInputs().get(i).getEquipments();
                positions = input.getAllocationInputs().get(i).getPositions();
                paths = input.getAllocationInputs().get(i).getPaths();
            }
        }

        Ferry hotSprayGoClosingBoxferry = new Ferry();
        Ferry coldSprayGoClosingBoxferry = new Ferry();
        SubCar hotSprayGoClosingBoxSubCar = new SubCar();
        SubCar coldSprayGoClosingBoxSubCar = new SubCar();
        SubCar hotAndCoreMoldRefluxSubCar = new SubCar();
        SubCar hotSpraySandBoxSubCar = new SubCar();
        SubCar coldMoldRefluxSubCar = new SubCar();
        SubCar coldSandBoxSubCar = new SubCar();
        Position hotMoldOpeningPosition = new Position();
        Position hotSandBoxOpeningPosition_1 = new Position();
        Position hotSandBoxOpeningPosition_2 = new Position();
        Position hotSandBoxRefluxLayPosition = new Position();
        Position coreMoldOpeningPosition = new Position();
        Position coldMoldOpeningPosition = new Position();
        Position coldSandBoxOpeningPosition = new Position();
        Position hotSandBoxSprayDownPosition = new Position();
        Position coldSandBoxSprayPosition_1 = new Position();
        Position coldSandBoxSprayPosition_2 = new Position();
        Equipment hotSpray_1 = new Equipment();
        Equipment hotSpray_2 = new Equipment();
        Equipment hotSpray_3 = new Equipment();
        Equipment coldSpray_1 = new Equipment();
        Equipment coldSpray_2 = new Equipment();
        Equipment coldSpray_3 = new Equipment();
        Path moldRefluxPath = new Path();
        Path coldMoldRefluxPath = new Path();
        Path hotSpraySandBoxPath = new Path();
        Path coldSandBoxGoSprayPath = new Path();
        Path hotSprayUpPath = new Path();
        Path hotSprayDownPath = new Path();
        Path coldSprayUpPath = new Path();
        Path coldSprayDownPath = new Path();
        Path hotSparyGoClosingBoxPath = new Path();
        Path coldSparyGoClosingBoxPath = new Path();
        MouldingIndexInput mouldingIndexInput = new MouldingIndexInput();

        for (Ferry ferry : ferries) {
            if (ferry.getNameEnum().equals(HOT_SPRAY_GO_CLOSING_BOX_FERRY)) {
                hotSprayGoClosingBoxferry = ferry;
            }
            if (ferry.getNameEnum().equals(COLD_SPRAY_GO_CLOSING_BOX_FERRY)) {
                coldSprayGoClosingBoxferry = ferry;
            }
        }
        for (SubCar subCar : subCars) {
            if (subCar.getNameEnum().equals(HOT_SPRAY_GO_CLOSING_BOX_SUB_CAR)) {
                hotSprayGoClosingBoxSubCar = subCar;
            }
            if (subCar.getNameEnum().equals(COLD_SPRAY_GO_CLOSING_BOX_SUB_CAR)) {
                coldSprayGoClosingBoxSubCar = subCar;
            }
            if (subCar.getNameEnum().equals(HOT_MOLD_AND_CORE_MOLD_REFLUX_SUB_CAR)) {
                hotAndCoreMoldRefluxSubCar = subCar;
            }
            if (subCar.getNameEnum().equals(HOT_SAND_BOX_GO_SPRAY_SUB_CAR)) {
                hotSpraySandBoxSubCar = subCar;
            }
            if (subCar.getNameEnum().equals(COLD_MOLD_REFLUX_SUB_CAR)) {
                coldMoldRefluxSubCar = subCar;
            }
            if (subCar.getNameEnum().equals(COLD_SAND_BOX_GO_SPRAY_SUB_CAR)) {
                coldSandBoxSubCar = subCar;
            }
        }
        for (Position position : positions) {
            if (position.getNameEnum().equals(HOT_MOLD_OPENING_MOLD_LAY_POSITION)) {
                hotMoldOpeningPosition = position;
            }
            if (position.getNameEnum().equals(HOT_MOLD_OPENING_SAND_BOX_LAY_POSITION_1)) {
                hotSandBoxOpeningPosition_1 = position;
            }
            if (position.getNameEnum().equals(HOT_MOLD_OPENING_SAND_BOX_LAY_POSITION_2)) {
                hotSandBoxOpeningPosition_2 = position;
            }
            if (position.getNameEnum().equals(HOT_MOLD_OPENING_SAND_BOX_REFLUX_LAY_POSITION)) {
                hotSandBoxRefluxLayPosition = position;
            }
            if (position.getNameEnum().equals(CORE_MOLD_OPENING_MOLD_LAY_POSITION)) {
                coreMoldOpeningPosition = position;
            }

            if (position.getNameEnum().equals(COLD_MOLD_OPENING_MOLD_LAY_POSITION)) {
                coldMoldOpeningPosition = position;
            }
            if (position.getNameEnum().equals(COLD_MOLD_OPENING_SAND_BOX_LAY_POSITION)) {
                coldSandBoxOpeningPosition = position;
            }
            if (position.getNameEnum().equals(HOT_SAND_BOX_GO_SPRAY_DOWN_POSITION)) {
                hotSandBoxSprayDownPosition = position;
            }
            if (position.getNameEnum().equals(COLD_SAND_BOX_GO_SPRAY_POSITION_1)) {
                coldSandBoxSprayPosition_1 = position;
            }
            if (position.getNameEnum().equals(COLD_SAND_BOX_GO_SPRAY_POSITION_2)) {
                coldSandBoxSprayPosition_2 = position;
            }
        }
        for (Equipment equipment : equipments) {
            if (equipment.getNameEnum().equals(EquipmentNameEnum.HOT_SAND_SPRAYING_1)) {
                hotSpray_1 = equipment;

            }
            if (equipment.getNameEnum().equals(EquipmentNameEnum.HOT_SAND_SPRAYING_2)) {
                hotSpray_2 = equipment;

            }
            if (equipment.getNameEnum().equals(EquipmentNameEnum.HOT_SAND_SPRAYING_3)) {
                hotSpray_3 = equipment;

            }

            if (equipment.getNameEnum().equals(EquipmentNameEnum.COLD_SAND_SPRAYING_1)) {
                coldSpray_1 = equipment;

            }
            if (equipment.getNameEnum().equals(EquipmentNameEnum.COLD_SAND_SPRAYING_2)) {
                coldSpray_2 = equipment;

            }
            if (equipment.getNameEnum().equals(EquipmentNameEnum.COLD_SAND_SPRAYING_3)) {
                coldSpray_3 = equipment;

            }

        }

        for (Path path : paths) {
            if (path.getNameEnum().equals(HOT_MOLD_AND_CORE_MOLD_REFLUX_PATH)) {
                moldRefluxPath = path;
            }
            if (path.getNameEnum().equals(COLD_MOLD_REFLUX_PATH)) {
                coldMoldRefluxPath = path;
            }
            if (path.getNameEnum().equals(HOT_SAND_BOX_GO_SPRAY_PATH)) {
                hotSpraySandBoxPath = path;
            }
            if (path.getNameEnum().equals(COLD_SAND_BOX_GO_SPRAY_PATH)) {
                coldSandBoxGoSprayPath = path;
            }
            if (path.getNameEnum().equals(HOT_SPRAY_UP_PATH)) {
                hotSprayUpPath = path;
            }
            if (path.getNameEnum().equals(HOT_SPRAY_DOWN_PATH)) {
                hotSprayDownPath = path;
            }
            if (path.getNameEnum().equals(COLD_SPRAY_UP_PATH)) {
                coldSprayUpPath = path;
            }
            if (path.getNameEnum().equals(COLD_SPRAY_DOWN_PATH)) {
                coldSprayDownPath = path;
            }
            if (path.getNameEnum().equals(HOT_SPRAY_GO_CLOSING_BOX_PATH)) {
                hotSparyGoClosingBoxPath = path;
            }
            if (path.getNameEnum().equals(COLD_SPRAY_GO_CLOSING_BOX_PATH)) {
                coldSparyGoClosingBoxPath = path;
            }
        }

        List<Event> eventFerrys_1 = new ArrayList<>();
        List<EventLinkGantt> eventLinkGantts = new ArrayList<>();
        int ganttIndex = 0;
        eventLinkGantts.add(new EventLinkGantt(hotSprayGoClosingBoxferry.getId(), FERRY, eventFerrys_1));
        mouldingIndexInput.setHotSprayGoClosingBoxFerryInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventFerrys_2 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(coldSprayGoClosingBoxferry.getId(), FERRY, eventFerrys_2));
        mouldingIndexInput.setColdSprayGoClosingBoxFerryInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventSubCar_1 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(hotSprayGoClosingBoxSubCar.getId(), SUB_CAR, eventSubCar_1));
        mouldingIndexInput.setHotSprayGoClosingBoxSubCarInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventSubCar_2 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(coldSprayGoClosingBoxSubCar.getId(), SUB_CAR, eventSubCar_2));
        mouldingIndexInput.setColdSprayGoClosingBoxSubCarInGanttIndex(ganttIndex);
        ganttIndex++;

        List<Event> eventSubCar_3 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(hotAndCoreMoldRefluxSubCar.getId(), SUB_CAR, eventSubCar_3));
        mouldingIndexInput.setMoldRefluxSubCarInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventSubCar_4 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(hotSpraySandBoxSubCar.getId(), SUB_CAR, eventSubCar_4));
        mouldingIndexInput.setHotSpraySandBoxSubCarInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventSubCar_5 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(coldMoldRefluxSubCar.getId(), SUB_CAR, eventSubCar_5));
        mouldingIndexInput.setColdMoldRefluxSubCarInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventSubCar_6 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(coldSandBoxSubCar.getId(), SUB_CAR, eventSubCar_6));
        mouldingIndexInput.setColdSandBoxSubCarInGanttIndex(ganttIndex);
        ganttIndex++;


        //moke造型区的甘特图
        List<Event> eventSubCar_7 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(null, SUB_CAR, eventSubCar_7));
        mouldingIndexInput.setHotMokeInputInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventSubCar_8 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(null, SUB_CAR, eventSubCar_8));
        mouldingIndexInput.setCoreMokeInputInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventSubCar_9 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(null, SUB_CAR, eventSubCar_9));
        mouldingIndexInput.setColdMokeInputInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventSubCar_10 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(null, SUB_CAR, eventSubCar_10));
        mouldingIndexInput.setHotUpMokeInputInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventSubCar_11 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(null, SUB_CAR, eventSubCar_11));
        mouldingIndexInput.setHotDownMokeInputInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventSubCar_12 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(null, SUB_CAR, eventSubCar_12));
        mouldingIndexInput.setColdUpMokeInputInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventSubCar_13 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(null, SUB_CAR, eventSubCar_13));
        mouldingIndexInput.setColdDownMokeInputInGanttIndex(ganttIndex);
        //


        GanttChart ganttChart = new GanttChart(3L, "MouldingLineGanttChart", SOUTH_MOULDING, eventLinkGantts);
        MouldingSimulationInput mouldingSimulationInput = new MouldingSimulationInput(hotAndCoreMoldRefluxSubCar, hotSpraySandBoxSubCar, coldMoldRefluxSubCar,
                coldSandBoxSubCar, hotSprayGoClosingBoxferry, hotSprayGoClosingBoxSubCar, coldSprayGoClosingBoxferry, coldSprayGoClosingBoxSubCar, hotMoldOpeningPosition,
                hotSandBoxOpeningPosition_1, hotSandBoxOpeningPosition_2, hotSandBoxRefluxLayPosition, coreMoldOpeningPosition, coldMoldOpeningPosition,
                coldSandBoxOpeningPosition, hotSandBoxSprayDownPosition, coldSandBoxSprayPosition_1, coldSandBoxSprayPosition_2, hotSpray_1, hotSpray_2,
                hotSpray_3, coldSpray_1, coldSpray_2, coldSpray_3, moldRefluxPath, coldMoldRefluxPath, hotSpraySandBoxPath, coldSandBoxGoSprayPath,
                hotSprayUpPath, hotSprayDownPath, coldSprayUpPath, coldSprayDownPath, hotSparyGoClosingBoxPath, coldSparyGoClosingBoxPath, mouldingIndexInput, totalTime);

        return new MouldingGanttChartLinkInput(ganttChart, mouldingSimulationInput);
    }

    /**
     * 合箱区的输入处理
     * @param input 仿真输入
     * @param totalTime 仿真总时间
     * @return 合模区输入
     */
    public ClosingBoxGanttChartLinkInput handleClosingBoxInput(SimulateInput input, double totalTime){
        List<SubCar> subCars = new ArrayList<>();
        List<Ferry> ferries = new ArrayList<>();
        List<WorkStation> workStations = new ArrayList<>();
        List<Position> positions = new ArrayList<>();
        List<Path> paths = new ArrayList<>();

        for (int i = 0; i < input.getAllocationInputs().size(); i++) {
            if (input.getAllocationInputs().get(i).getRegion().equals(AreaEnum.SOUTH_COMBINE_BOX)){
                subCars = input.getAllocationInputs().get(i).getSubCars();
                ferries = input.getAllocationInputs().get(i).getFerries();
                workStations = input.getAllocationInputs().get(i).getWorkStations();
                positions = input.getAllocationInputs().get(i).getPositions();
                paths = input.getAllocationInputs().get(i).getPaths();
            }
        }

        Ferry hotClosingBoxFerry = new Ferry();
        Ferry coldClosingBoxFerry = new Ferry();

        SubCar hotClosingBoxSubCar = new SubCar();
        SubCar coldClosingBoxSubCar = new SubCar();

        Position hotClosingBoxPosition = new Position();
        Position coldClosingBoxPosition = new Position();
        Position hotClosingBoxGoPourPosition = new Position();
        Position coldClosingBoxGoPourPosition = new Position();

        Path hotClosingBoxPath = new Path();
        Path coldClosingBoxPath = new Path();

        List<WorkStation> closingBoxWorkStations = new ArrayList<>();
        ClosingBoxIndexInput closingBoxIndexInput = new ClosingBoxIndexInput();
        int workStationIndex = 0;

        for (Ferry ferry : ferries) {
            if (ferry.getNameEnum().equals(CarNameEnum.HOT_CLOSING_BOX_FERRY)) {
                hotClosingBoxFerry = ferry;
            }
            if (ferry.getNameEnum().equals(CarNameEnum.COLD_CLOSING_BOX_FERRY)) {
                coldClosingBoxFerry = ferry;
            }
        }
        for (SubCar subCar : subCars) {
            if (subCar.getNameEnum().equals(CarNameEnum.HOT_CLOSING_BOX_SUB_CAR)) {
                hotClosingBoxSubCar = subCar;
            }
            if (subCar.getNameEnum().equals(CarNameEnum.COLD_CLOSING_BOX_SUB_CAR)) {
                coldClosingBoxSubCar = subCar;
            }
        }

        for (Position position : positions) {
            if (position.getNameEnum().equals(HOT_SAND_BOX_SPRAY_GO_CLOSING_BOX_POSITION)) {
                hotClosingBoxPosition = position;
            }
            if (position.getNameEnum().equals(COLD_SAND_BOX_SPRAY_GO_CLOSING_BOX_POSITION)) {
                coldClosingBoxPosition = position;
            }
            if (position.getNameEnum().equals(HOT_SAND_BOX_CLOSING_BOX_GO_POUR_POSITION)) {
                hotClosingBoxGoPourPosition = position;
            }
            if (position.getNameEnum().equals(COLD_SAND_BOX_CLOSING_BOX_GO_POUR_POSITION)) {
                coldClosingBoxGoPourPosition = position;
            }
        }

        for (Path path : paths) {
            if (path.getNameEnum().equals(HOT_CLOSING_BOX_PATH)) {
                hotClosingBoxPath = path;
            }
            if (path.getNameEnum().equals(COLD_CLOSING_BOX_PATH)) {
                coldClosingBoxPath = path;
            }
        }

        for (WorkStation workStation : workStations) {
            if (workStation.getNameEnum().equals(HOT_CLOSING_BOX_WORKSTATION_1)) {
                closingBoxWorkStations.add(workStation);
                closingBoxIndexInput.setHotClosingBoxWorkStationIndex_1(workStationIndex);
                workStationIndex++;
            }
            if (workStation.getNameEnum().equals(HOT_CLOSING_BOX_WORKSTATION_2)) {
                closingBoxWorkStations.add(workStation);
                closingBoxIndexInput.setHotClosingBoxWorkStationIndex_2(workStationIndex);
                workStationIndex++;
            }
            if (workStation.getNameEnum().equals(HOT_CLOSING_BOX_WORKSTATION_3)) {
                closingBoxWorkStations.add(workStation);
                closingBoxIndexInput.setHotClosingBoxWorkStationIndex_3(workStationIndex);
                workStationIndex++;
            }
            if (workStation.getNameEnum().equals(HOT_CLOSING_BOX_WORKSTATION_4)) {
                closingBoxWorkStations.add(workStation);
                closingBoxIndexInput.setHotClosingBoxWorkStationIndex_4(workStationIndex);
                workStationIndex++;
            }
            if (workStation.getNameEnum().equals(COLD_CLOSING_BOX_WORKSTATION_1)) {
                closingBoxWorkStations.add(workStation);
                closingBoxIndexInput.setColdClosingBoxWorkStationIndex_1(workStationIndex);
                workStationIndex++;
            }
            if (workStation.getNameEnum().equals(COLD_CLOSING_BOX_WORKSTATION_2)) {
                closingBoxWorkStations.add(workStation);
                closingBoxIndexInput.setColdClosingBoxWorkStationIndex_2(workStationIndex);
                workStationIndex++;
            }
            if (workStation.getNameEnum().equals(COLD_CLOSING_BOX_WORKSTATION_3)) {
                closingBoxWorkStations.add(workStation);
                closingBoxIndexInput.setColdClosingBoxWorkStationIndex_3(workStationIndex);
                workStationIndex++;
            }
            if (workStation.getNameEnum().equals(COLD_CLOSING_BOX_WORKSTATION_4)) {
                closingBoxWorkStations.add(workStation);
                closingBoxIndexInput.setColdClosingBoxWorkStationIndex_4(workStationIndex);
                workStationIndex++;
            }

        }

        List<Event> eventFerrys_1 = new ArrayList<>();
        List<EventLinkGantt> eventLinkGantts = new ArrayList<>();
        int ganttIndex = 0;

        eventLinkGantts.add(new EventLinkGantt(hotClosingBoxFerry.getId(), FERRY, eventFerrys_1));
        closingBoxIndexInput.setHotClosingBoxFerryInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventFerrys_2 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(coldClosingBoxFerry.getId(), FERRY, eventFerrys_2));
        closingBoxIndexInput.setColdClosingBoxFerryInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventSubCar_1 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(hotClosingBoxSubCar.getId(), SUB_CAR, eventSubCar_1));
        closingBoxIndexInput.setHotClosingBoxSubCarInGanttIndex(ganttIndex);
        ganttIndex++;
        List<Event> eventSubCar_2 = new ArrayList<>();
        eventLinkGantts.add(new EventLinkGantt(coldClosingBoxSubCar.getId(), SUB_CAR, eventSubCar_2));
        closingBoxIndexInput.setColdClosingBoxSubCarInGanttIndex(ganttIndex);
        ganttIndex++;

        for (WorkStation closingBoxWorkStation : closingBoxWorkStations) {

            List<Event> eventWorkSattions = new ArrayList<>();
            eventLinkGantts.add(new EventLinkGantt(closingBoxWorkStation.getId(), WORK_STATION, eventWorkSattions));

            if (closingBoxWorkStation.getNameEnum().equals(HOT_CLOSING_BOX_WORKSTATION_1)) {
                closingBoxIndexInput.setHotClosingBoxWorkStationInGanttIndex_1(ganttIndex);
                ganttIndex++;

            } else if (closingBoxWorkStation.getNameEnum().equals(HOT_CLOSING_BOX_WORKSTATION_2)) {
                closingBoxIndexInput.setHotClosingBoxWorkStationInGanttIndex_2(ganttIndex);
                ganttIndex++;

            } else if (closingBoxWorkStation.getNameEnum().equals(HOT_CLOSING_BOX_WORKSTATION_3)) {
                closingBoxIndexInput.setHotClosingBoxWorkStationInGanttIndex_3(ganttIndex);
                ganttIndex++;

            } else if (closingBoxWorkStation.getNameEnum().equals(HOT_CLOSING_BOX_WORKSTATION_4)) {
                closingBoxIndexInput.setHotClosingBoxWorkStationInGanttIndex_4(ganttIndex);
                ganttIndex++;

            } else if (closingBoxWorkStation.getNameEnum().equals(COLD_CLOSING_BOX_WORKSTATION_1)) {
                closingBoxIndexInput.setColdClosingBoxWorkStationInGanttIndex_1(ganttIndex);
                ganttIndex++;

            } else if (closingBoxWorkStation.getNameEnum().equals(COLD_CLOSING_BOX_WORKSTATION_2)) {
                closingBoxIndexInput.setColdClosingBoxWorkStationInGanttIndex_2(ganttIndex);
                ganttIndex++;

            } else if (closingBoxWorkStation.getNameEnum().equals(COLD_CLOSING_BOX_WORKSTATION_3)) {
                closingBoxIndexInput.setColdClosingBoxWorkStationInGanttIndex_3(ganttIndex);
                ganttIndex++;

            } else if (closingBoxWorkStation.getNameEnum().equals(COLD_CLOSING_BOX_WORKSTATION_4)) {
                closingBoxIndexInput.setColdClosingBoxWorkStationInGanttIndex_4(ganttIndex);
                ganttIndex++;

            }
        }

        GanttChart ganttChart = new GanttChart(4L, "ClosingGanttChart", AreaEnum.SOUTH_COMBINE_BOX, eventLinkGantts);
        ClosingBoxSimulationInput closingBoxSimulationInput = new ClosingBoxSimulationInput(hotClosingBoxPosition, coldClosingBoxPosition, hotClosingBoxGoPourPosition,
                coldClosingBoxGoPourPosition, hotClosingBoxFerry, hotClosingBoxSubCar, coldClosingBoxFerry, coldClosingBoxSubCar, closingBoxWorkStations,
                hotClosingBoxPath, coldClosingBoxPath, closingBoxIndexInput, totalTime);

        return new ClosingBoxGanttChartLinkInput(ganttChart, closingBoxSimulationInput);
    }

    /**
     * 判断结束的函数
     * @param productDeque 产品队列
     * @param input 仿真输入
     * @return true表示结束
     */
    public boolean judgeSimulationFinish(Deque<Product> productDeque, SimulateInput input){
        int num = 0;
        for (int i = 0; i < input.getOrderInputs().size(); i++) {
            for (int j = 0; j < input.getOrderInputs().get(i).getOrderProducts().size(); j++) {
                num = num + (input.getOrderInputs().get(i).getOrderProducts().get(j).getNumber() - input.getOrderInputs().get(i).getOrderProducts().get(j).getFinishNum());
            }
        }

        return productDeque.size() == num * 2;
    }

    public Deque<Product> updataProductDeque(Deque<Product> productDeque) {
        Deque<Product> result = new ArrayDeque<>();
        ArrayList<Product> sandAndCoreProducts = new ArrayList<>();
        ArrayList<Product> moldProducts = new ArrayList<>();
        Product newProduct = new Product();

        do {
            Product product = Objects.requireNonNull(productDeque.poll()).productDeepCopy();
            if (product.getProductType().equals(ProductTypeEnum.UPPER_BOX)) {
                sandAndCoreProducts.add(product);
            }else if (product.getProductType().equals(ProductTypeEnum.LOWER_BOX)){
                sandAndCoreProducts.add(product);
            }else if (product.getProductType().equals(ProductTypeEnum.MOULD)){
                moldProducts.add(product);
            }else {
                sandAndCoreProducts.add(product);
            }
        }while (!productDeque.isEmpty());

        for (int i = 0, j = 0; i < sandAndCoreProducts.size(); i++) {
            if (sandAndCoreProducts.get(i).getProductType().equals(ProductTypeEnum.UPPER_BOX)){
                newProduct = sandAndCoreProducts.get(i).productDeepCopy();
                newProduct.setProducts(new ArrayList<>());
                newProduct.setProductType(ProductTypeEnum.UPPER_SAND_SHELL_BOX);
                newProduct.getProducts().add(sandAndCoreProducts.get(i));
                newProduct.getProducts().add(moldProducts.get(j));
                j++;
                result.add(newProduct);
            }else if (sandAndCoreProducts.get(i).getProductType().equals(ProductTypeEnum.LOWER_BOX)){
                newProduct = sandAndCoreProducts.get(i).productDeepCopy();
                newProduct.setProducts(new ArrayList<>());
                newProduct.setProductType(ProductTypeEnum.LOWER_SAND_SHELL_BOX);
                newProduct.getProducts().add(sandAndCoreProducts.get(i));
                newProduct.getProducts().add(moldProducts.get(j));
                j++;
                result.add(newProduct);
            }else {
                newProduct = sandAndCoreProducts.get(i).productDeepCopy();
                newProduct.setProducts(new ArrayList<>());
                result.add(newProduct);
            }
        }

        return result;
    }
}
