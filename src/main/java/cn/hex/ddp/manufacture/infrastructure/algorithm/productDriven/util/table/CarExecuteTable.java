package cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.util.table;

import cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassDescription: 车辆执行表
 * @Author: KangHong
 * @Created: 2024/11/21 15:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarExecuteTable implements Serializable {
    /**
     * 车辆正在执行的事件
     * Key为车辆名字枚举
     * Value为车辆正在发生的事件（正在执行的指令）
     */
    private Map<CarNameEnum, Event> carExecuteEvents;
    /**
     * Double用于记录事件剩余的执行时间
     * 记录车辆发生事件，已经执行的事件
     * 用于判断事件何时结束（执行完成）
     * 刚开始初始化时应同时维护两个属性
     * 并且保证时间为事件的发生时间
     * 在后续的时间迭代中更新Map中的时间
     */
    private Map<CarNameEnum, Double> carExecuteTime;
}
