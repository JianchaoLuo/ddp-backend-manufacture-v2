package cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2025/6/5 下午4:35
 */
@Data
@AllArgsConstructor
public class ProductSeparate {
    /**
     * 当天产品的解
     */
    private List<ProductForSeparate> currentSeparate;
    /**
     * 当天排产产品的模拟时间
     */
    private Double finishTime;
}
