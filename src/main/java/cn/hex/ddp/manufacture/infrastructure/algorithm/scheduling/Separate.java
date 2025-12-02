package cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling;

import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.util.ProductSeparate;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2025/5/28 下午4:12
 */
@Data
@AllArgsConstructor
public class Separate {
    /**
     * 每天的排产计划列表，列表的元素顺序严格按日期顺序从小到大排列
     */
    private List<ProductSeparate> productSeparates;
}
