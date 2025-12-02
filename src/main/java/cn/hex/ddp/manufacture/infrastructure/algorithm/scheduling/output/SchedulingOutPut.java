package cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.output;

import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule.ScheduleOrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @ClassDescription:算法的输出结果类
 * @Author: KangHong
 * @Created: 2025/5/28 下午1:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchedulingOutPut {

    /**
     * key为计划的日期，value为每天生产的产品列表
     */
    private Map<LocalDateTime, List<SchedulingProduct>> productionPlan;

    /**
     * key为订单的id，value为订单按照排产算法的完成时间
     */
    private Map<Long, LocalDateTime> orderFinishTime;

}
