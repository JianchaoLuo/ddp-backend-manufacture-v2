package cn.hex.ddp.manufacture.infrastructure.algorithm.data;

import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.AllocationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.OrderInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimulateInput implements Serializable {
    /**
     * 工厂的基础配置信息列表
     * 包括车辆，点位，设备，工岗，轨道等
     */
    private List<AllocationInput> allocationInputs;
    /**
     * 仿真任务中订单的输入
     */
    private List<OrderInput> orderInputs;

    private AnalogInput analogInput;
}
