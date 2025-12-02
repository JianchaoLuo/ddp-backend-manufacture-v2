package cn.hex.ddp.manufacture.infrastructure.algorithm.pojo;

import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.OrderProductTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductAfootEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductFinishEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription: 产品类，用于算法中描述产品相关信息
 * @Author: KangHong
 * @Created: 2024/5/23 16:22
 */
@Data
@AllArgsConstructor
public class Product implements Serializable {
    /**
     * 产品的id
     */
    private Long id;
    /**
     * 产品名称
     */
    private String name;
    /**
     * 产品生产的序列
     */
    private Integer level;
    /**
     * 产品的类型，区分不同类型的产品
     */
    private ProductTypeEnum productType;
    /**
     * 产品已经完成的加工
     */
    private ProductFinishEnum productFinish;
    /**
     * 产品正在进行的加工
     */
    private ProductAfootEnum productAfoot;
    /**
     * 热砂或冷砂
     */
    private OrderProductTypeEnum orderProductType;
    /**
     * 产品是否需要回流的标识
     * true标识需要回流，false标识不需要
     */
    private boolean refluxFlag;
    /**
     * 产品正在加工的已经完成的加工时间
     */
    private double finishTime;
    /**
     * 产品出库时间
     */
    private double outBoundTime;
    /**
     * 产品的合模时间
     */
    private double moldClosingTime;
    /**
     * 产品的射砂时间
     */
    private double sandblastingTime;
    /**
     * 产品的开模时间
     */
    private double moldOpeningTime;
    /**
     * 产品的喷涂时间
     */
    private double sprayTime;
    /**
     * 产品的合箱时间
     */
    private double closingBoxTime;
    /**
     * 产品的浇筑时间
     */
    private double pouringTime;
    /**
     * 产品的冷却时间
     */
    private double coolingTime;
    /**
     * 产品的开箱时间
     */
    private double unboxingTime;

    //新增加产品列表，标识产品是否存在子产品，比如：砂壳箱由上砂箱，模具，砂芯，下砂箱，模具组成。
    /**
     * 产品的子产品列表
     */
    private List<Product> products;

    public Product() {
    }

    /**
     * 产品类的深拷贝函数
     * @return 拷贝后的产品
     */
    public Product productDeepCopy() {
        Product copy = new Product(id, name, level, productType, productFinish, productAfoot, orderProductType, refluxFlag, finishTime, outBoundTime,
                moldClosingTime, sandblastingTime, moldOpeningTime, sprayTime, closingBoxTime, pouringTime, coolingTime, unboxingTime, products);

        return copy;
    }
}
