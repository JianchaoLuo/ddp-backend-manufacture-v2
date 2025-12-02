package cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author bobo
 * @description 排产订单项
 * @date 2025/6/6 11:18
 */
@Data
@AllArgsConstructor
public class ScheduleOrderItem {
    /**
     * 订单id（来自订单模块，不是排产订单id）
     */
    private Long id;
    /**
     * 订单中的产品列表
     */
    private List<ScheduleProductInput> schesuleProductList;
    /**
     * 交付时间（订单中与客户确定的交付时间）,我这里以分钟作为单位
     */
    private double deliveryTime;
}
