package cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.util;

import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription: 产品驱动中，产品的描述类
 * @Author: KangHong
 * @Created: 2024/11/9 17:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriveProduct implements Serializable {
    /**
     * 产品事件列表
     */
    private List<ProductEvent> productEvents;
    /**
     * 产品详情
     */
    private Product product;
    /**
     * 产品完成所有事件时间
     * 用于计算保存订单的仿真时间
     */
    private Double productFinishTime;
    /**
     * 产品所属的订单id
     * 用于定位产品所归属的订单
     * 可用于判断订单的完成时间
     */
    private Long orderId;
}
