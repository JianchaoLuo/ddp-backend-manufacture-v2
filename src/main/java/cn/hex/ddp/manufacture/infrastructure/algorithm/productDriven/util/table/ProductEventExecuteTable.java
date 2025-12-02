package cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.util.table;

import cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.util.ProductEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ClassDescription: 事件执行表
 * @Author: KangHong
 * @Created: 2024/11/21 15:03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEventExecuteTable implements Serializable {
    /**
     * 正在执行的事件集合
     */
    private List<ProductEvent> events;
    /**
     * Double用于记录事件剩余的执行时间
     */
    private Map<CarNameEnum, Double> eventExecuteTime;
}
