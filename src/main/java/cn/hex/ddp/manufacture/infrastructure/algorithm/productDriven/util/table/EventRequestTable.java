package cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.util.table;

import cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.util.ProductEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription: 事件请求表
 * @Author: KangHong
 * @Created: 2024/11/21 15:08
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestTable implements Serializable {
    /**
     * 正在执行的事件集合
     */
    private List<ProductEvent> events;
}
