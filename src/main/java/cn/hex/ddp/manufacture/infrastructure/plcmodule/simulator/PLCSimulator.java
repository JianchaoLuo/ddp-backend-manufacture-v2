package cn.hex.ddp.manufacture.infrastructure.plcmodule.simulator;

import cn.hex.ddp.manufacture.application.simulator.processor.CommandProcessor;
import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import cn.hex.ddp.manufacture.domain.simulator.model.simulation.PositionRealTime;
import cn.hex.ddp.manufacture.domain.simulator.model.simulation.WorkstationRealTime;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationNameEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Instruction;
import cn.hex.ddp.manufacture.infrastructure.common.redis.RedisUtil;
import cn.hex.ddp.manufacture.infrastructure.plcmodule.mock.PositionTriggerEvents;
import cn.hex.ddp.manufacture.infrastructure.plcmodule.mock.PrepareMoltenIronEvents;
import cn.hex.ddp.manufacture.infrastructure.plcmodule.mock.WorkstationTriggerEvents;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * PLC模拟器
 *
 * @author Huhaisen
 * @date 2024/07/25
 */
public class PLCSimulator {
    // 后端模拟器对象
    @Setter
    private CommandProcessor commandProcessor;
    // 模拟工厂控制部分产生的事件序列，目的是让后端模拟器生成仿真数据
//    private final PositionTriggerEvents positionTriggerEvents;
    private final WorkstationTriggerEvents workstationTriggerEvents;
    private final PrepareMoltenIronEvents prepareMoltenIronEvents;
    // 调度器，用于实现定时任务
    private final ScheduledExecutorService scheduler;
    private boolean shouldStop = false;

    // 各部分模拟事件集合是否第一次运行
    private final AtomicBoolean hotShootingFirstRun = new AtomicBoolean(true);
    private final AtomicBoolean coreShootingFirstRun = new AtomicBoolean(true);
    private final AtomicBoolean coldShootingFirstRun = new AtomicBoolean(true);
    private final AtomicBoolean hotSprayUpFirstRun = new AtomicBoolean(true);
    private final AtomicBoolean hotSprayDownFirstRun = new AtomicBoolean(true);
    private final AtomicBoolean coldSprayUpFirstRun = new AtomicBoolean(true);
    private final AtomicBoolean coldSprayDownFirstRun = new AtomicBoolean(true);
    private final AtomicBoolean castingRefluxPositionFirstRun = new AtomicBoolean(true);
    private final AtomicBoolean coldSandRowCarPositionFirstRun = new AtomicBoolean(true);
    // 浇筑电炉设备_1-6的事件集合是否第一次运行
    private final AtomicBoolean pouringElectricFurnace1FirstRun = new AtomicBoolean(true);
    private final AtomicBoolean pouringElectricFurnace2FirstRun = new AtomicBoolean(true);
    private final AtomicBoolean pouringElectricFurnace3FirstRun = new AtomicBoolean(true);
    private final AtomicBoolean pouringElectricFurnace4FirstRun = new AtomicBoolean(true);
    private final AtomicBoolean pouringElectricFurnace5FirstRun = new AtomicBoolean(true);
    private final AtomicBoolean pouringElectricFurnace6FirstRun = new AtomicBoolean(true);

    public PLCSimulator() {
//        this.positionTriggerEvents = new PositionTriggerEvents();
        this.workstationTriggerEvents = new WorkstationTriggerEvents();
        this.prepareMoltenIronEvents = new PrepareMoltenIronEvents();
        this.scheduler = Executors.newScheduledThreadPool(7);
    }

    /**
     * 开始调度任务
     */
    public void startScheduling() {
        issuePrepareMoltenIronEvents();
        // 每500ms执行一次processEvents方法, 判断是否达到结束条件
        scheduler.scheduleAtFixedRate(this::processEvents, 0, 100, TimeUnit.MILLISECONDS);
    }

    public void stopPlcSimulate() {
        shouldStop = true;
        scheduler.shutdown();
    }

    public void plcEventFinish(Event event) {
        event.setCompleteFlag(true);
        event.setOccurringFlag(false);
    }

    /**
     * 下发准备铁水事件（模拟器开始即下发，只模拟执行一次）
     */
    private void issuePrepareMoltenIronEvents() {
        processEvents(prepareMoltenIronEvents.getPouringElectricFurnace1PrepareEvents(),
                new AtomicBoolean(true));
        processEvents(prepareMoltenIronEvents.getPouringElectricFurnace2PrepareEvents(),
                new AtomicBoolean(true));
    }

    /**
     * 工厂控制的部分涉及的工位状态若改变，会调用该函数
     * 实现内容为：让后端模拟器发送事件序列，模拟工厂控制部分的运行情况
     */
    public void triggerByWorkstation(WorkstationRealTime workstation) {
        CompletableFuture.runAsync(() -> {
            switch (workstation.getNameEnum()) {
                case WorkstationNameEnum.POURING_WORKSTATION_15T_1, WorkstationNameEnum.POURING_WORKSTATION_15T_2:
                    processEvents(workstationTriggerEvents.getPouringElectricFurnace1Events(),
                            pouringElectricFurnace1FirstRun);
                    break;
                case WorkstationNameEnum.POURING_WORKSTATION_15T_3, WorkstationNameEnum.POURING_WORKSTATION_15T_4:
                    processEvents(workstationTriggerEvents.getPouringElectricFurnace2Events(),
                            pouringElectricFurnace2FirstRun);
                    break;
                case WorkstationNameEnum.POURING_WORKSTATION_30T_1, WorkstationNameEnum.POURING_WORKSTATION_30T_2:
                    processEvents(workstationTriggerEvents.getPouringElectricFurnace3Events(),
                            pouringElectricFurnace3FirstRun);
                    break;
                case WorkstationNameEnum.POURING_WORKSTATION_30T_3, WorkstationNameEnum.POURING_WORKSTATION_30T_4:
                    processEvents(workstationTriggerEvents.getPouringElectricFurnace4Events(),
                            pouringElectricFurnace4FirstRun);
                    break;
                case WorkstationNameEnum.POURING_WORKSTATION_30T_5, WorkstationNameEnum.POURING_WORKSTATION_30T_6:
                    processEvents(workstationTriggerEvents.getPouringElectricFurnace5Events(),
                            pouringElectricFurnace5FirstRun);
                    break;
                case WorkstationNameEnum.POURING_WORKSTATION_30T_7, WorkstationNameEnum.POURING_WORKSTATION_30T_8:
                    processEvents(workstationTriggerEvents.getPouringElectricFurnace6Events(),
                            pouringElectricFurnace6FirstRun);
                    break;
                default:
                    break;
            }
        }, scheduler);
    }

    /**
     * 工厂控制的部分涉及的点位状态若改变，会调用该函数
     * 实现内容为：让后端模拟器发送事件序列，模拟工厂控制部分的运行情况
     */
    public void triggerByPosition(PositionRealTime position) {
        CompletableFuture.runAsync(() -> {
//            log.info("====$$$$==== PLC模拟器接收到点位状态更新请求，点位信息：" + position);
            switch (position.getNameEnum()) {
                // 热砂射砂放置点位
//                case PositionNameEnum.HOT_SAND_SHOOTING_POSITION:
//                    processEvents(positionTriggerEvents.getHotShootingEvents(), hotShootingFirstRun);
//                    break;
//                // 制芯轨道放置的交互点
//                case PositionNameEnum.CORE_MAKING_LAY_POSITION:
//                    processEvents(positionTriggerEvents.getCoreShootingEvents(), coreShootingFirstRun);
//                    break;
//                // 冷砂射砂放置点位
//                case PositionNameEnum.COLD_SAND_SHOOTING_POSITION:
//                    processEvents(positionTriggerEvents.getColdShootingEvents(), coldShootingFirstRun);
//                    break;
//                // 热砂模具开模砂箱放置点位
//                case PositionNameEnum.HOT_MOLD_OPENING_SAND_BOX_LAY_POSITION:
//                    processEvents(positionTriggerEvents.getHotSprayUpEvents(), hotSprayUpFirstRun);
//                    break;
//                // 热砂砂箱前往喷涂的下方点位
//                case PositionNameEnum.HOT_SAND_BOX_GO_SPRAY_DOWN_POSITION:
//                    processEvents(positionTriggerEvents.getHotSprayDownEvents(), hotSprayDownFirstRun);
//                    break;
//                // 冷砂砂箱前往喷涂的上方点位
//                case PositionNameEnum.COLD_SAND_BOX_GO_SPRAY_POSITION_1:
//                    processEvents(positionTriggerEvents.getColdSprayUpEvents(), coldSprayUpFirstRun);
//                    break;
//                // 冷砂砂箱前往喷涂的下方点位
//                case PositionNameEnum.COLD_SAND_BOX_GO_SPRAY_POSITION_2:
//                    processEvents(positionTriggerEvents.getColdSprayDownEvents(), coldSprayDownFirstRun);
//                    break;
//                case PositionNameEnum.CASTING_REFLUX_SHIPPING_POSITION:
//                    // 铸件回流传输点位。
//                    processEvents(positionTriggerEvents.getCastingRefluxPositionEvents(), castingRefluxPositionFirstRun);
//                    break;
//                case PositionNameEnum.SAND_BOX_ROW_CAR_POSITION:
//                    // 冷砂行车放置交互点(砂箱回流行车放置点)
//                    processEvents(positionTriggerEvents.getColdSandRowCarPositionEvents(), coldSandRowCarPositionFirstRun);
//                    break;
                default:
                    break;
            }
        }, scheduler);
    }

    private void processEvents() {
        if (shouldStop) {
            scheduler.shutdown();
        }
    }

    /**
     * 遍历事件集合，依次通过模拟器下发事件，并设置事件的发生标志位
     */
    private void processEvents(List<Event> events, AtomicBoolean isFirstRun) {
        int eventIndex = 0;
        while (eventIndex < events.size()) {
            if (eventCanIssue(events.get(eventIndex))) {
                if (eventIndex == 0 && !events.getFirst().isOccurringFlag()) {
                    // 如果是第一次运行该事件集合，则直接发送第一个事件
                    if (isFirstRun.get()) {
                        isFirstRun.set(false);
                        if (issueEventWithLock(events.getFirst())) {
                            eventIndex++;
                        }
                    } else if (events.getLast().isCompleteFlag()){
                        // 如果不是第一次运行该事件集合，则需要判断上一次执行的这批事件是否已经完成
                        if (issueEventWithLock(events.getFirst())) {
                            eventIndex++;
                            // 为了保证并发情况能够正常执行，这里需要将上一个事件的完成标志设置为false
                            events.getLast().setCompleteFlag(false);
                        }
                    }
                } else if (events.get(eventIndex - 1).isCompleteFlag() && !events.get(eventIndex).isOccurringFlag()) {
                    // 对于非第一个事件，依次判断如果上一个事件已经完成，则发送当前事件
                    if (issueEventWithLock(events.get(eventIndex))) {
                        eventIndex++;
                        // 为了保证并发情况能够正常执行，这里需要将上一个事件的完成标志设置为false
                        events.get(eventIndex - 1).setCompleteFlag(false);
                    }
                }
            }
        }
    }

    /**
     * 尝试获取锁，并下发事件。确保并发情况下，只有一个线程能够下发同一个事件。
     */
    private boolean issueEventWithLock(Event event) {
        boolean lock = RedisUtil.LockOps.getLock(event.getInstruction().getId().toString(),
                event.getInstruction().getId().toString(), 5, TimeUnit.SECONDS);
        if (lock) {
            try {
                event.setOccurringFlag(true);
                commandProcessor.issueEvent(event);
                return true;
            } catch (Exception e) {
                event.setOccurringFlag(false);
                return false;
            } finally {
                RedisUtil.LockOps.releaseLock(event.getInstruction().getId().toString(),
                        event.getInstruction().getId().toString());
            }
        } else {
            return false;
        }
    }

    /**
     * 判断事件是否可以下发
     */
    private boolean eventCanIssue(Event event) {
        Instruction instruction = event.getInstruction();
        return switch (instruction.getExecuteType()) {
            case ExecuteTypeEnum.SUB_CAR, ExecuteTypeEnum.FERRY ->
                    !commandProcessor.carHasCurrentEvent(instruction.getId());
            default -> true;
        };
    }
}
