package cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author:bobo
 * @description: 排产算法订单相关的输入类
 * @date:2025/6/6 11:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleOrderInput {
    List<ScheduleOrderItem> items;
}
