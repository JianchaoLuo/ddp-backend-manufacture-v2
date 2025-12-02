package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.moldClosing;

import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Ferry;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.SubCar;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input.MoldClosingSimulationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.GanttChart;

import java.util.ArrayList;
import java.util.List;

import static cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum.WAIT_TRANSPORT;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum.WORK_STATION;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum.NOTICE;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/6/25 21:08
 */
public class WorkStationEventFlow {
    /**
     * 合模区仿真
     */
    private MoldClosingSimulationTools tools = new MoldClosingSimulationTools();

    /**
     * 工岗通知事件的生成
     * @param input
     * @param moldClosingGanttChart
     * @return
     */
    public MoldClosingGanttChartLinkInput MoleClosingWorkStationWorkingEventCreate(MoldClosingSimulationInput input, SubCar subCar, Ferry ferry,
                                                                                   GanttChart moldClosingGanttChart, Integer workStationId, Integer workStationGanttIndex){
        //发生子车装载砂箱的事件
        //计算事件发生事件的时间
        double executionTime = input.getWorkStations().get(workStationId).getProduct().getFirst().getMoldClosingTime();

        List<SubCarStatusEnum> startSubCarStatus = new ArrayList<>(subCar.getSubCarState());
        List<FerryStatusEnum> startFerryStatus = new ArrayList<>(ferry.getFerryState());
        Coordinate ferryCoordinate = new Coordinate(ferry.getLocationCoordinate().getX(), ferry.getLocationCoordinate().getY());
        List<WorkstationStatusEnum> startWorkStationStatus = new ArrayList<>();
        startWorkStationStatus.add(WorkstationStatusEnum.WORKING);
        List<WorkstationStatusEnum> endWorkStationStatus = new ArrayList<>();
        endWorkStationStatus.add(WorkstationStatusEnum.WAIT_TRANSPORT);

        Product product = input.getWorkStations().get(workStationId).getProduct().getFirst().productDeepCopy();
        ArrayList<Product> products = new ArrayList<>();
        for (int i = 1; i < input.getWorkStations().get(workStationId).getProduct().size(); i++) {
            products.add(input.getWorkStations().get(workStationId).getProduct().get(i).productDeepCopy());
            //product.getProducts().add(input.getWorkStations().get(workStationId).getProduct().get(i).productDeepCopy());
        }

        product.setProducts(products);

        //生成事件
        Event event = tools.createEvent("100141", "HMWSW", executionTime, input.getTotalTime() - executionTime, input.getTotalTime(),
                null, null,false, null, null, false,
                null, null, null,false, null, null,
                false, null, null,null, null, null,
                null, null, input.getWorkStations().get(workStationId).getId(), startWorkStationStatus,
                endWorkStationStatus, null, null, null, input.getWorkStations().get(workStationId).getId(),
                WORK_STATION, NOTICE, null, product);

        //事件加入甘特图
        moldClosingGanttChart.getEventLinkGantts().get(workStationGanttIndex).getEvents().add(event);

        //改变车辆状态和时间
        input.getWorkStations().get(workStationId).setStatus(WAIT_TRANSPORT);

        MoldClosingGanttChartLinkInput moldClosingGanttChartLinkInput = new MoldClosingGanttChartLinkInput(moldClosingGanttChart, input);

        return moldClosingGanttChartLinkInput;
    }
}
