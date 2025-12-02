package cn.hex.ddp.manufacture.infrastructure.algorithm;

import cn.hex.ddp.manufacture.application.simulator.processor.CommandProcessor;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.domain.simulator.model.cache.SimulateCache;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateResult;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DeployThread extends Thread {
    private CommandProcessor commandProcessor; //注意更改
    private SimulateResult simulateResult;

    private boolean pouringFlag = false;

    @Override
    public void run() {
        List<GanttChart> ganttCharts = simulateResult.getGanttCharts();
        while (SimulateCache.INSTANCE.getRunning()) {
            List<Event> events = createEventList(ganttCharts);
            for (Event event : events) {
                if (event.getId().equals("!!!!!!") && event.getEventName().equals("FlagEvent")) {
                    pouringFlag = true;
                    event.setCompleteFlag(true);
                } else {
                    if (eventStart(event)) {
                        eventOccurring(event);
//                    flag = true;
                    }
                }
            }


            //判断所有事件是否执行完成，结束的判断
            if (allEventFinish(ganttCharts)) {
                commandProcessor.stopSimulate();
                break;
            }
        }
    }

    public void setSimulateThread(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }

    public void setSimulateResult(SimulateResult simulateResult) {
        this.simulateResult = simulateResult;
    }

    /**
     * 发生事件的函数
     *
     * @param event 要发生的事件
     */
    public void eventOccurring(Event event) {
        event.setOccurringFlag(true);
//        log.info("事件!!!：" + event);
        commandProcessor.issueEvent(event);
    }

    /**
     * 判断事件是否能开始发生
     *
     * @param event 需要判断的事件
     * @return true表示可以发生，false表示不可以发生
     */
    // TODO 修改
    public boolean eventStart(Event event) {
        //总体的开始判断标识
        boolean flag = false;
        //子车开始状态标识判断，true表示符合结束状态
        boolean subCarStartFlag = true;
        //母车开始状态标识判断，true表示符合结束状态
        boolean ferryStartFlag = true;
        //设备开始状态标识判断，true表示符合结束状态
        boolean equipmentFlag = true;
        //点位开始状态标识判断，true表示符合结束状态
        boolean positionFlag = true;
        //工岗开始状态标识判断，true表示符合结束状态
        boolean workStationFlag = true;
        //道路开始状态标识判断，true表示符合结束状态
        boolean pathFlag = true;
        //子车事件的开始状态判断
        if (event.getStartSubCarEvent().getSubCarStatus() != null) {
            // 获得子车的状态，子车状态至少有三个，车辆必须满足至少有事件中的每个状态
            List<SubCarStatusEnum> subCarStatus = commandProcessor.getSubCarStatus(event.getStartSubCarEvent().getId());
            if (!checkSubCarStatus(subCarStatus)) {
                subCarStartFlag = false;
            } else {
                //优化问题
                for (SubCarStatusEnum subCarStatusStart : event.getStartSubCarEvent().getSubCarStatus()) {
                    for (int i = 0; i < subCarStatus.size(); i++) {
                        if (subCarStatusStart.equals(subCarStatus.get(i))) {
                            break;
                        }

                        //判断子车是否符合事件要求的状态
                        if (i == subCarStatus.size() - 1) {
                            subCarStartFlag = false;
                        }
                    }
                }
            }

            //判断子车是否需要检查位置
            if (event.getStartSubCarEvent().isCpFlag()) {
                //获得子车的当前实际位置
                Coordinate actualCoordinate = commandProcessor.getCarCoordinate(event.getStartSubCarEvent().getId());
                if (!actualCoordinate.equals(event.getStartSubCarEvent().getTargetCoordinate()))
                    subCarStartFlag = false;
            }
            if ("100020".equals(event.getId())) {
                log.info("模拟器子车状态：" + subCarStatus.toString() + "\t" + "事件子车状态：" +
                        event.getStartSubCarEvent().getSubCarStatus().toString());
            }
        }
        //母车事件的开始状态判断
        if (event.getStartFerryEvent().getFerryStatus() != null) {
            //获得母车的状态
            List<FerryStatusEnum> ferryStatus = commandProcessor.getFerryStatus(event.getStartFerryEvent().getId());
            if (!checkFullStatus(ferryStatus)) {
                ferryStartFlag = false;
            } else {
                for (FerryStatusEnum ferryStatusStart : event.getStartFerryEvent().getFerryStatus()) {
                    for (int i = 0; i < ferryStatus.size(); i++) {
                        if (ferryStatusStart.equals(ferryStatus.get(i))) {
                            break;
                        }

                        //判断母车是否符合事件要求的状态
                        if (i == ferryStatus.size() - 1) {
                            ferryStartFlag = false;
                        }
                    }
                }
            }

            //判断母车是否需要检查位置
            if (event.getStartFerryEvent().isCpFlag()) {
                //获得母车的当前实际位置
                Coordinate actualCoordinate = commandProcessor.getCarCoordinate(event.getStartFerryEvent().getId());
                if (!actualCoordinate.equals(event.getStartFerryEvent().getTargetCoordinate()))
                    ferryStartFlag = false;
            }
        }
        //设备事件的开始状态判断，设备，点位，工岗的状态为，有事件list中的其中一个状态便可
        if (event.getStartEquipmentEvent().getEquipmentStatus() != null) {
            //先标识为false
            equipmentFlag = false;
            //获得设备的状态
            EquipmentOperationStatusEnum equipmentStatus = commandProcessor.getEquipmentStatus(event.getStartEquipmentEvent().getId());
//            log.info("【设备事件开始状态判断】" + "\n" + "模拟器设备状态：" + equipmentStatus + "\t" + "事件设备状态："
//                    + event.getStartEquipmentEvent().getEquipmentStatus());
            for (EquipmentOperationStatusEnum equipmentStatusStart : event.getStartEquipmentEvent().getEquipmentStatus()) {
                //判断设备是否符合事件要求的状态
                if (equipmentStatus.equals(equipmentStatusStart)) {
                    equipmentFlag = true;
                }
            }
        }
        //点位事件的开始状态判断
        if (event.getStartPositionEvent().getPositionStatus() != null) {
            //先标识为false
            positionFlag = false;
            //获得设备的状态
            PositionStatusEnum positionStatus = commandProcessor.getPositionStatus(event.getStartPositionEvent().getPositionId());
            for (PositionStatusEnum positionStatusStart : event.getStartPositionEvent().getPositionStatus()) {
                //判断设备是否符合事件要求的状态
                if (positionStatus.equals(positionStatusStart)) {
                    positionFlag = true;
                }
            }
        }
        //工岗事件的结束状态判断
        if (event.getStartWorkStationEvent().getWorkStationStatus() != null) {
            //先标识为false
            workStationFlag = false;
            //获得工岗的状态
            WorkstationStatusEnum workStationStatus = commandProcessor.getWorkstationStatus(event.getStartWorkStationEvent().getId());
//            log.info("【工岗事件开始状态判断】" + "\n" + "模拟器工岗状态：" + workStationStatus + "\t" + "事件工岗状态：" +
//                    event.getStartWorkStationEvent().getWorkStationStatus());
            for (WorkstationStatusEnum workStationStatusStart : event.getStartWorkStationEvent().getWorkStationStatus()) {
                //判断工岗是否符合事件要求的状态
                if (workStationStatus.equals(workStationStatusStart)) {
                    workStationFlag = true;
                }
            }
        }
        //道路事件的结束状态判断，道路的状态只有一种，所以对比满足即可
        if (event.getStartPathEvent() != null) {
            PathStatusEnum status;
            for (PathLinkEvent pathEventStart : event.getStartPathEvent()) {
                //获得轨道状态
                status = commandProcessor.getPathStatus(pathEventStart.getId());
//                log.info("【道路事件开始状态判断】" + "\n" + "模拟器轨道状态：" + status + "\t" + "事件轨道状态：" +
//                        pathEventStart.getStatus());
                //判断轨道状态是否满足，有一个不满足整体为false
                if (!status.equals(pathEventStart.getStatus())) {
                    pathFlag = false;
                }
            }
        }

        //判断事件中所以的开始前提是否满足
        if (subCarStartFlag && ferryStartFlag && equipmentFlag && positionFlag && workStationFlag && pathFlag) {
            flag = true;
        }

        return flag;
    }

    /**
     * 事件完成的回调
     *
     * @param event
     */
    public void eventFinish(Event event) {
        //子车结束状态标识判断，true表示符合结束状态
        boolean subCarEndFlag = true;
        //母车结束状态标识判断，true表示符合结束状态
        boolean ferryEndFlag = true;
        //设备结束状态标识判断，true表示符合结束状态
        boolean equipmentFlag = true;
        //点位结束状态标识判断，true表示符合结束状态
        boolean positionFlag = true;
        //工岗结束状态标识判断，true表示符合结束状态
        boolean workStationFlag = true;
        //道路结束状态标识判断，true表示符合结束状态
        boolean pathFlag = true;
        //子车事件的结束状态判断
        if (event.getEndSubCarEvent().getSubCarStatus() != null) {
            //获得子车的状态，子车状态至少有三个，车辆必须满足至少有事件中的每个状态
            List<SubCarStatusEnum> subCarStatus = commandProcessor.getSubCarStatus(event.getEndSubCarEvent().getId());
//            if ("100706".equals(event.getId())){
//                log.info("{{子车事件结束状态判断}}" + "\n" + "模拟器子车状态：" + subCarStatus + "\t" + "事件子车状态："
//                    + event.getEndSubCarEvent().getSubCarStatus());
//            }
//            if ("100766".equals(event.getId())){
//                log.info("{{子车事件结束状态判断}}" + "\n" + "模拟器子车状态：" + subCarStatus + "\t" + "事件子车状态："
//                        + event.getEndSubCarEvent().getSubCarStatus());
//            }
//            log.info("{{子车事件结束状态判断}}" + "\n" + "模拟器子车状态：" + subCarStatus + "\t" + "事件子车状态："
//                    + event.getEndSubCarEvent().getSubCarStatus());
            if (!checkSubCarStatus(subCarStatus)) {
                subCarEndFlag = false;
            } else {
                for (SubCarStatusEnum subCarStatusEnd : event.getEndSubCarEvent().getSubCarStatus()) {
                    for (int i = 0; i < subCarStatus.size(); i++) {
                        if (subCarStatusEnd.equals(subCarStatus.get(i))) {
                            break;
                        }

                        //判断子车是否符合事件要求的状态
                        if (i == subCarStatus.size() - 1) {
                            subCarEndFlag = false;
                        }
                    }
                }
            }
            //判断子车是否需要检查位置
            if (event.getEndSubCarEvent().isCpFlag()) {
                //获得子车的当前实际位置
                Coordinate actualCoordinate = commandProcessor.getCarCoordinate(event.getEndSubCarEvent().getId());
                if (actualCoordinate == null) {
                    System.out.println(event.getEndSubCarEvent().getId() + " " + event);
                }
//                if ("100766".equals(event.getId())){
//                    log.info("模拟器子车位置：" + actualCoordinate + "\t" + "事件子车位置：" +
//                        event.getEndSubCarEvent().getTargetCoordinate());
//                }
//                if ("100728".equals(event.getId())){
//                    log.info("模拟器子车位置：" + actualCoordinate + "\t" + "事件子车位置：" +
//                            event.getEndSubCarEvent().getTargetCoordinate());
//                }
//                log.info("模拟器子车位置：" + actualCoordinate + "\t" + "事件子车位置：" +
//                        event.getEndSubCarEvent().getTargetCoordinate());
                if (!actualCoordinate.equals(event.getEndSubCarEvent().getTargetCoordinate()))
                    subCarEndFlag = false;
            }
        }
        //母车事件的结束状态判断
        if (event.getEndFerryEvent().getFerryStatus() != null) {
            //获得母车的状态
            List<FerryStatusEnum> ferryStatus = commandProcessor.getFerryStatus(event.getEndFerryEvent().getId());
//            if ("100706".equals(event.getId())){
//                log.info("{{母车事件结束状态判断}}" + "\n" + "模拟器母车状态：" + ferryStatus + "\t" + "事件母车状态："
//                    + event.getEndFerryEvent().getFerryStatus());
//            }
//            if ("100728".equals(event.getId())){
//                log.info("{{母车事件结束状态判断}}" + "\n" + "模拟器母车状态：" + ferryStatus + "\t" + "事件母车状态："
//                        + event.getEndFerryEvent().getFerryStatus());
//            }
//            log.info("{{母车事件结束状态判断}}" + "\n" + "模拟器母车状态：" + ferryStatus + "\t" + "事件母车状态："
//                    + event.getEndFerryEvent().getFerryStatus());
            if (!checkFullStatus(ferryStatus)) {
                ferryEndFlag = false;
            } else {
                for (FerryStatusEnum ferryStatusEnd : event.getEndFerryEvent().getFerryStatus()) {
                    for (int i = 0; i < ferryStatus.size(); i++) {
                        if (ferryStatusEnd.equals(ferryStatus.get(i))) {
                            break;
                        }

                        //判断母车是否符合事件要求的状态
                        if (i == ferryStatus.size() - 1) {
                            ferryEndFlag = false;
                        }
                    }
                }
            }
            //判断母车是否需要检查位置
            if (event.getEndFerryEvent().isCpFlag()) {
                //获得母车的当前实际位置
                Coordinate actualCoordinate = commandProcessor.getCarCoordinate(event.getEndFerryEvent().getId());
//                if ("100706".equals(event.getId())){
//                    log.info("模拟器母车位置：" + actualCoordinate + "\t" + "事件母车位置：" +
//                        event.getEndFerryEvent().getTargetCoordinate());
//                }
//                if ("100728".equals(event.getId())){
//                    log.info("模拟器母车位置：" + actualCoordinate + "\t" + "事件母车位置：" +
//                            event.getEndFerryEvent().getTargetCoordinate());
//                }
//                log.info("模拟器母车位置：" + actualCoordinate + "\t" + "事件母车位置：" +
//                        event.getEndFerryEvent().getTargetCoordinate());
                if (!actualCoordinate.equals(event.getEndFerryEvent().getTargetCoordinate()))
                    ferryEndFlag = false;
            }
        }
        //设备事件的结束状态判断，设备，点位，工岗的状态为，有事件list中的其中一个状态便可
        if (event.getEndEquipmentEvent().getEquipmentStatus() != null) {
            //先标识为false
            equipmentFlag = false;
            //获得设备的状态
            EquipmentOperationStatusEnum equipmentStatus = commandProcessor.getEquipmentStatus(event.getEndEquipmentEvent().getId());
            for (EquipmentOperationStatusEnum equipmentStatusEnd : event.getEndEquipmentEvent().getEquipmentStatus()) {
                //判断设备是否符合事件要求的状态
                if (equipmentStatus.equals(equipmentStatusEnd)) {
                    equipmentFlag = true;
                }
            }
        }
        //点位事件的结束状态判断
        if (event.getEndPositionEvent().getPositionStatus() != null) {
            //先标识为false
            positionFlag = false;
            //获得设备的状态
            PositionStatusEnum positionStatus = commandProcessor.getPositionStatus(event.getEndPositionEvent().getPositionId());
            for (PositionStatusEnum positionStatusEnd : event.getEndPositionEvent().getPositionStatus()) {
                //判断设备是否符合事件要求的状态
                if (positionStatus.equals(positionStatusEnd)) {
                    positionFlag = true;
                }
            }
        }
        //工岗事件的结束状态判断
        if (event.getEndWorkStationEvent().getWorkStationStatus() != null) {
            //先标识为false
            workStationFlag = false;
            //获得工岗的状态
            WorkstationStatusEnum workStationStatus = commandProcessor.getWorkstationStatus(event.getEndWorkStationEvent().getId());
            for (WorkstationStatusEnum workStationStatusEnd : event.getEndWorkStationEvent().getWorkStationStatus()) {
                //判断工岗是否符合事件要求的状态
                if (workStationStatus.equals(workStationStatusEnd)) {
                    workStationFlag = true;
                }
            }
        }
        //道路事件的结束状态判断，道路的状态只有一种，所以对比满足即可
        if (event.getEndPathEvent() != null) {
            PathStatusEnum status;
            for (PathLinkEvent pathEventEnd : event.getEndPathEvent()) {
                //获得轨道状态
                status = commandProcessor.getPathStatus(pathEventEnd.getId());
                if (!status.equals(pathEventEnd.getStatus())) {
                    pathFlag = false;
                }
            }
        }

        if (subCarEndFlag && ferryEndFlag && equipmentFlag && positionFlag && workStationFlag && pathFlag) {
            //事件完成，设置事件完成标识
            event.setCompleteFlag(true);
            event.setOccurringFlag(false);
        } else {
            // 事件未完成，设置事件未完成标识
            event.setCompleteFlag(false);
            event.setOccurringFlag(false);
        }

        // todo  处理
    }

    /**
     * 事件全部完成标识，判断事件是否执行完成
     *
     * @return true表示事件全部执行完成
     */
    public boolean allEventFinish(List<GanttChart> ganttCharts) {
        boolean flag = true;
        for (GanttChart chart : ganttCharts) {
            for (EventLinkGantt eventLinkGantt : chart.getEventLinkGantts()) {
                for (Event event : eventLinkGantt.getEvents()) {
                    if (!event.isCompleteFlag())
                        flag = false;
                }
            }
        }

        return flag;
    }

    /**
     * 下发事件列表的生成函数
     */
    private List<Event> createEventList(List<GanttChart> ganttCharts) {
        List<Event> eventList = new ArrayList<>();
        for (GanttChart ganttChart : ganttCharts) {
            if (pouringFlag) {
                List<EventLinkGantt> eventLinkGantts = ganttChart.getEventLinkGantts();
                for (int i = 0; i < eventLinkGantts.size(); i++) {
                    //判断事件列表是否为空
                    if (!eventLinkGantts.get(i).getEvents().isEmpty()) {
                        //循环判断事件是否已经完成，找到第一个未完成的事件
                        for (int j = 0; j < eventLinkGantts.get(i).getEvents().size(); j++) {
                            Event event = eventLinkGantts.get(i).getEvents().get(j);
                            //判断事件是否完成
                            if (!event.isCompleteFlag()) {
                                //找到第一个未完成的事件，判断事件是否正在执行中
                                if (!event.isOccurringFlag()) {
                                    //判断是否可以发生事件
                                    eventList.add(event);
                                }
//                                else {
//                                    log.info("!!!!!!!!!!正在进行事件id：" + event.getId());
//                                }

                                //跳出循环，避免判断到剩余的事件
                                break;
                            }
                        }

                    }
                }
            } else {
                if (ganttChart.getRegion().equals(AreaEnum.SOUTH_STEREOSCOPIC_STOREHOUSE) ||
                        ganttChart.getRegion().equals(AreaEnum.SOUTH_MOULDING) ||
                        ganttChart.getRegion().equals(AreaEnum.SOUTH_COMBINE_BOX) || ganttChart.getRegion().equals(AreaEnum.SOUTH_WAITING_POURING)) {
                    List<EventLinkGantt> eventLinkGantts = ganttChart.getEventLinkGantts();
                    for (int i = 0; i < eventLinkGantts.size(); i++) {
                        //判断事件列表是否为空
                        if (!eventLinkGantts.get(i).getEvents().isEmpty()) {
                            //循环判断事件是否已经完成，找到第一个未完成的事件
                            for (int j = 0; j < eventLinkGantts.get(i).getEvents().size(); j++) {
                                Event event = eventLinkGantts.get(i).getEvents().get(j);
                                //判断事件是否完成
                                if (!event.isCompleteFlag()) {
                                    //找到第一个未完成的事件，判断事件是否正在执行中
                                    if (!event.isOccurringFlag()) {
                                        //判断是否可以发生事件
                                        eventList.add(event);
                                    }

                                    //跳出循环，避免判断到剩余的事件
                                    break;
                                } else {
                                    eventLinkGantts.get(i).getEvents().remove(event);
                                    j--;
                                    if (eventLinkGantts.get(i).getEvents().isEmpty()) {
                                        break;
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }

        return eventList;
    }

    private boolean checkFullStatus(List<FerryStatusEnum> actualStatus) {
        for (FerryStatusEnum status : actualStatus) {
            if (FerryStatusEnum.FERRY_BROKEN.equals(status)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkSubCarStatus(List<SubCarStatusEnum> actualStatus) {
        for (SubCarStatusEnum status : actualStatus) {
            if (SubCarStatusEnum.SUB_CAR_BROKEN.equals(status)) {
                return false;
            }
        }
        return true;
    }

    public void go() {
        assert commandProcessor != null;
    }
}
