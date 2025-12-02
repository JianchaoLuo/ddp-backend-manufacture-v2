package cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.eventGeneration;

import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.EventName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: bobo
 * @Description: 决策函数最终返回的结果
 * @DateTime: 2025/3/21 10:56
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecisionResult {

    // 决策指向的事件
    private EventName decisionEvent;

    // 事件具体的地点枚举，比如前往工岗事件具体工岗的枚举
    Object locationEnum;
}
