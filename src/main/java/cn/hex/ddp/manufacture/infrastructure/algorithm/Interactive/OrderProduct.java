package cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive;


import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.OrderProductTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription: 订单中的产品
 * @Author: KangHong
 * @Created: 2024/6/2 2:16
 */
@Data
@AllArgsConstructor
public class OrderProduct implements Serializable {
    /**
     * 订单产品的构成列表
     * 由上砂箱，下砂箱，模具，砂芯等
     */
    private List<Product> products;
    /**
     * 订单产品的要生产的数目
     */
    private Integer number;
    /**
     * 订单产品所需要的材质枚举
     */
    private String materialType;
    /**
     * 订单中产品的类型
     * 热砂或冷砂
     */
    private OrderProductTypeEnum productType;
    /**
     * 订单产品所需要的材质的用量
     */
    private double usage;
    /**
     * 订单产品所需要的单位量的材质所需要的准备烧铁水的时间
     */
    private double materialTime;
    /**
     * 已经生产完成的产品数目
     */
    private Integer finishNum;
    /**
     * 已经出库的产品的数目
     */
    private Integer alreadyOutboundNum;
    /**
     * 已经出库的产品的列表
     * 注：这个是指立体库可以出库的产品，不包括成型或半成型的产品
     */
    private List<Product> alreadyOutboundProducts;

    /**
     * 产品型号id(新增字段)
     */
    private Long productId;
}
