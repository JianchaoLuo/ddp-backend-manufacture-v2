package cn.hex.ddp.manufacture.infrastructure.algorithm.multitruck.multiCarTools.outBound;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.multitruck.input.OutBoundSimulationMultiCarInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.SimulationTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;

import java.util.ArrayList;
import java.util.List;

import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.OCCUPIED;
import static cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum.UNOCCUPIED;
import static cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum.PATH_UNOCCUPIED;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum.FERRY;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum.SUB_CAR;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum.CARRY_SUB_CAR;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum.NOT_CARRY_SUB_CAR;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum.RISING;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum.RUNNING;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum.*;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/11/8 15:04
 */
public class OutBoundMuktiCarEventFlow {
    /**
     * 仿真总工具类
     */
    private SimulationTool allTool = new SimulationTool();


    public OutBoundANDMoldClosingMultiCar HotoutBoundEmptyGoRefluxEventCreate(OutBoundANDMoldClosingMultiCar input, int finishedCount) {
        //分离出库区的仿真输入
        OutBoundSimulationMultiCarInput outBoundInput = input.getOutBoundMultiCarGanttLinkInput().getInput();
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime_1 = Math.abs(outBoundInput.getOutboundFerryHot().getLocationCoordinate().getY() -
                outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInputMultiCar().getSandBoxRefluxPositionIndex()).getCoordinate().getY())
                / outBoundInput.getOutboundFerryHot().getEmptySpeed();
        double executionTime_2 = Math.abs(outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInputMultiCar().getSandBoxRefluxPositionIndex()).getCoordinate().getX() -
                        outBoundInput.getOutboundFerryHot().getLocationCoordinate().getX()) /
                        outBoundInput.getSubCars().get(outBoundInput.getOutBoundIndexInputMultiCar().getOutBoundSubcarHotIndex()).getEmptySpeed();
        double executionTime_3 = outBoundInput.getSubCars().get(outBoundInput.getOutBoundIndexInputMultiCar().getOutBoundSubcarHotIndex()).getTopRodRaiseOrFallTime();
        double executionTime_4 = Math.abs(outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInputMultiCar().getSandBoxRefluxPositionIndex()).getCoordinate().getX() -
                        outBoundInput.getOutboundFerryHot().getLocationCoordinate().getX()) /
                        outBoundInput.getSubCars().get(outBoundInput.getOutBoundIndexInputMultiCar().getOutBoundSubcarHotIndex()).getFullSpeed();
        //前往的目标点位
        Coordinate aimCoordinate = new Coordinate(outBoundInput.getOutboundFerryHot().getLocationCoordinate().getX(),
                outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInputMultiCar().getSandBoxRefluxPositionIndex()).getCoordinate().getY());
        //子母车开始状态
        List<SubCarStatusEnum> startSubCarStatus_1 = new ArrayList<>(outBoundInput.getSubCars().get(outBoundInput.getOutBoundIndexInputMultiCar().getOutBoundSubcarHotIndex()).getSubCarState());
        List<FerryStatusEnum> startFerryStatus_1 = new ArrayList<>(outBoundInput.getOutboundFerryHot().getFerryState());
        //子车结束的状态
        List<SubCarStatusEnum> endSubCarStatus_1 = new ArrayList<>();
        List<SubCarStatusEnum> endSubCarStatus_2 = new ArrayList<>();
        List<SubCarStatusEnum> endSubCarStatus_3 = new ArrayList<>();
        endSubCarStatus_1.add(EMPTY_LOAD);
        endSubCarStatus_1.add(TOP_ROD_FALL_DOWN);
        endSubCarStatus_1.add(SUB_CAR_NOT_IN_FERRY);
        endSubCarStatus_2.add(FULL_LOAD);
        endSubCarStatus_2.add(TOP_ROD_RAISED);
        endSubCarStatus_2.add(SUB_CAR_NOT_IN_FERRY);
        endSubCarStatus_3.add(FULL_LOAD);
        endSubCarStatus_3.add(TOP_ROD_RAISED);
        endSubCarStatus_3.add(SUB_CAR_IN_FERRY);
        //母车结束状态
        List<FerryStatusEnum> endFerryStatus_1 = new ArrayList<>();
        List<FerryStatusEnum> endFerryStatus_2 = new ArrayList<>();
        endFerryStatus_1.add(NOT_CARRY_SUB_CAR);
        endFerryStatus_2.add(CARRY_SUB_CAR);
        //点位状态列表
        List<PositionStatusEnum> startPositionStatus = new ArrayList<>();
        List<PositionStatusEnum> endPositionStatus = new ArrayList<>();
        startPositionStatus.add(OCCUPIED);
        endPositionStatus.add(UNOCCUPIED);
        //道路写入事件的列表
        List<Long> pathIds = new ArrayList<>();
        List<PathStatusEnum> pathStatusEnums = new ArrayList<>();
        pathIds.add(outBoundInput.getPaths().get(outBoundInput.getOutBoundIndexInputMultiCar().getOutBoundPathIndex()).getId());
        pathStatusEnums.add(PATH_UNOCCUPIED);

        Product product = outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInputMultiCar().getSandBoxRefluxPositionIndex()).getProduct().productDeepCopy();

        //生成事件
        Event event_1 = allTool.createEvent("101001", "ELCSSCGIHCSP", executionTime_1,
                outBoundInput.getTotalTime() - executionTime_1 - executionTime_2 - executionTime_3 - executionTime_4,
                outBoundInput.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                outBoundInput.getSubCars().get(outBoundInput.getOutBoundIndexInputMultiCar().getOutBoundSubcarHotIndex()).getId(),
                startSubCarStatus_1, false, null, startSubCarStatus_1, true,
                aimCoordinate, outBoundInput.getOutboundFerryHot().getId(), startFerryStatus_1, false, null,
                startFerryStatus_1, true, aimCoordinate, null, null, null,
                outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInputMultiCar().getSandBoxRefluxPositionIndex()).getId(),
                startPositionStatus, startPositionStatus, null,null, null, pathIds,
                pathStatusEnums, pathStatusEnums, outBoundInput.getOutboundFerryHot().getId(), FERRY, RUNNING, aimCoordinate, null);

        //事件加入甘特图
        input.getOutBoundMultiCarGanttLinkInput().getGanttChart().getEventLinkGantts().
                get(outBoundInput.getOutBoundIndexInputMultiCar().getOutBoundSubCarHotInGanttIndex()).getEvents().add(event_1);

        //生成事件
        Event event_2 = allTool.createEvent("101002", "ELCSSCGIHCSP", executionTime_2,
                outBoundInput.getTotalTime() - executionTime_2 - executionTime_3 - executionTime_4,
                outBoundInput.getTotalTime() - executionTime_3 - executionTime_4,
                outBoundInput.getSubCars().get(outBoundInput.getOutBoundIndexInputMultiCar().getOutBoundSubcarHotIndex()).getId(),
                startSubCarStatus_1, true, outBoundInput.getOutboundFerryHot().getLocationCoordinate().coordinateDeepCopy(), endSubCarStatus_1,
                true, outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInputMultiCar().getSandBoxRefluxPositionIndex()).getCoordinate(),
                outBoundInput.getOutboundFerryHot().getId(), startFerryStatus_1, true, aimCoordinate, endFerryStatus_1,
                true, aimCoordinate, null,null, null,
                outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInputMultiCar().getSandBoxRefluxPositionIndex()).getId(),
                startPositionStatus, startPositionStatus, null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                outBoundInput.getSubCars().get(outBoundInput.getOutBoundIndexInputMultiCar().getOutBoundSubcarHotIndex()).getId(), SUB_CAR, RUNNING,
                outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInputMultiCar().getSandBoxRefluxPositionIndex()).getCoordinate(), null);

        //事件加入甘特图
        input.getOutBoundMultiCarGanttLinkInput().getGanttChart().getEventLinkGantts().
                get(outBoundInput.getOutBoundIndexInputMultiCar().getOutBoundSubCarHotInGanttIndex()).getEvents().add(event_2);

        //生成事件
        Event event_3 = allTool.createEvent("101003", "ELCSCSCIHCSP", executionTime_3,
                outBoundInput.getTotalTime() - executionTime_3 - executionTime_4, outBoundInput.getTotalTime() - executionTime_4,
                outBoundInput.getSubCars().get(outBoundInput.getOutBoundIndexInputMultiCar().getOutBoundSubcarHotIndex()).getId(), endSubCarStatus_1, true,
                outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInputMultiCar().getSandBoxRefluxPositionIndex()).getCoordinate(), endSubCarStatus_2,
                true, outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInputMultiCar().getSandBoxRefluxPositionIndex()).getCoordinate(),
                outBoundInput.getOutboundFerryHot().getId(), endFerryStatus_1,true, aimCoordinate, endFerryStatus_1,
                true, aimCoordinate, null,null, null,
                outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInputMultiCar().getSandBoxRefluxPositionIndex()).getId(), startPositionStatus,
                endPositionStatus, null,null, null, pathIds, pathStatusEnums, pathStatusEnums,
                outBoundInput.getSubCars().get(outBoundInput.getOutBoundIndexInputMultiCar().getOutBoundSubcarHotIndex()).getId(), SUB_CAR, RISING, null, product);

        //事件加入甘特图
        input.getOutBoundMultiCarGanttLinkInput().getGanttChart().getEventLinkGantts().
                get(outBoundInput.getOutBoundIndexInputMultiCar().getOutBoundSubCarHotInGanttIndex()).getEvents().add(event_3);
        //生成事件
        Event event_4 = allTool.createEvent("101004", "FLCSCSCGBTF", executionTime_4,
                outBoundInput.getTotalTime() - executionTime_4, outBoundInput.getTotalTime(),
                outBoundInput.getSubCars().get(outBoundInput.getOutBoundIndexInputMultiCar().getOutBoundSubcarHotIndex()).getId(), endSubCarStatus_2,
                true, outBoundInput.getPositions().get(outBoundInput.getOutBoundIndexInputMultiCar().getSandBoxRefluxPositionIndex()).getCoordinate(),
                endSubCarStatus_3, true, aimCoordinate, outBoundInput.getOutboundFerryHot().getId(), endFerryStatus_1, true,
                aimCoordinate, endFerryStatus_2, true, aimCoordinate, null,null, null,
                null, null, null, null,null, null, pathIds,
                pathStatusEnums, pathStatusEnums, outBoundInput.getSubCars().get(outBoundInput.getOutBoundIndexInputMultiCar().getOutBoundSubcarHotIndex()).getId(),
                SUB_CAR, RUNNING, aimCoordinate, product);

        //事件加入甘特图
        input.getOutBoundMultiCarGanttLinkInput().getGanttChart().getEventLinkGantts().
                get(outBoundInput.getOutBoundIndexInputMultiCar().getOutBoundSubCarHotInGanttIndex()).getEvents().add(event_4);

        //改变车辆状态和时间
        input.getOutBoundMultiCarGanttLinkInput().getInput().getSubCars().
                get(outBoundInput.getOutBoundIndexInputMultiCar().getOutBoundSubcarHotIndex()).setSubCarState(endSubCarStatus_3);
        input.getOutBoundMultiCarGanttLinkInput().getInput().getSubCars().
                get(outBoundInput.getOutBoundIndexInputMultiCar().getOutBoundSubcarHotIndex()).setLocationCoordinate(aimCoordinate);

        input.getOutBoundMultiCarGanttLinkInput().getInput().getOutboundFerryHot().setFerryState(endFerryStatus_2);
        input.getOutBoundMultiCarGanttLinkInput().getInput().getOutboundFerryHot().setLocationCoordinate(aimCoordinate);

        input.getOutBoundMultiCarGanttLinkInput().getInput().getPositions().
                get(outBoundInput.getOutBoundIndexInputMultiCar().getSandBoxRefluxPositionIndex()).setStatus(UNOCCUPIED);


        input.getOutBoundMultiCarGanttLinkInput().getInput().getSubCars().
                get(outBoundInput.getOutBoundIndexInputMultiCar().getOutBoundSubcarHotIndex()).setProduct(product);

        input.getOutBoundMultiCarGanttLinkInput().getInput().getPositions().
                get(outBoundInput.getOutBoundIndexInputMultiCar().getSandBoxRefluxPositionIndex()).setProduct(null);


        return input;
    }
}
