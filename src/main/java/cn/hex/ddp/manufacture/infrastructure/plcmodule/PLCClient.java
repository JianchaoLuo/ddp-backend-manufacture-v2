package cn.hex.ddp.manufacture.infrastructure.plcmodule;

import cn.hex.ddp.manufacture.application.simulator.processor.CommandProcessor;
import cn.hex.ddp.manufacture.domain.simulator.model.simulation.Car;
import cn.hex.ddp.manufacture.domain.simulator.model.simulation.Equipment;
import cn.hex.ddp.manufacture.domain.simulator.model.simulation.PositionRealTime;
import cn.hex.ddp.manufacture.domain.simulator.model.simulation.WorkstationRealTime;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * PLC通信客户端
 * 主要实现模拟器与PLC部分的交互方法
 */
@Setter
public class PLCClient {
    private CommandProcessor commandProcessor;

    public PLCClient(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }

    /**
     * 车辆下发移动指令
     */
    public void carMove(@NotNull Car car, @NotNull Event event) {

    }

    /**
     * 车辆下发顶杆升指令
     */
    public void carRising(@NotNull Car car, @NotNull Event event) {

    }

    /**
     * 车辆下发顶杆降指令
     */
    public void carLanding(@NotNull Car car, @NotNull Event event) {

    }

    /**
     * 车辆下发等待指令
     */
    public void carWaiting(@NotNull Car car, @NotNull Event event) {

    }

    /**
     * 设备开启
     */
    public void equipmentOpen(@NotNull Equipment equipment, @NotNull Event event) {

    }

    /**
     * 设备关闭
     */
    public void equipmentOff(@NotNull Equipment equipment, @NotNull Event event) {

    }

    /**
     * 设备下发通知指令
     */
    public void equipmentNotice(@NotNull Equipment equipment, @NotNull Event event) {

    }

    /**
     * 设备使用铁水
     */
    public void equipmentUseMoltenIron(@NotNull Equipment equipment, @NotNull Event event) {

    }

    /**
     * 准备铁水
     */
    public void equipmentPrepareMoltenIron(@NotNull Equipment equipment, @NotNull Event event) {

    }

    /**
     * 工岗下发通知指令
     */
    public void workstationNotice(@NotNull WorkstationRealTime workstation, @NotNull Event event) {

    }

    /**
     * 点位下发通知指令
     */
    public void positionNotice(@NotNull PositionRealTime position, @NotNull Event event) {

    }
}
