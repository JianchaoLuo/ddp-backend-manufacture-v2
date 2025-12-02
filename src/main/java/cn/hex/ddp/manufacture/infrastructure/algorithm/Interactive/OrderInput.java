package cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription: 仿真任务订单的输入
 * @Author: KangHong
 * @Created: 2024/6/2 1:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInput implements Serializable {
    /**
     * 订单id
     */
    private Long id;
    /**
     * 订单中的产品列表
     */
    private List<OrderProduct> orderProducts;
    /**
     * 订单交付时间
     */
    private double deliveryTime;
    /**
     * 订单优先级
     */
    private Integer priority;
}
