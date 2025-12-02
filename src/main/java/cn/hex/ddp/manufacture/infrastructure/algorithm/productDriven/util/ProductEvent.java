package cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.util;

import cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.enums.EventName;
import cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.enums.EventState;
import cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassDescription: 产品事件类
 * @Author: KangHong
 * @Created: 2024/11/8 16:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductEvent implements Serializable {
    /**
     * 事件状态
     */
    private EventState eventState;
    /**
     * 事件类型
     */
    private EventType eventType;
    /**
     * 事件名称
     */
    private EventName eventName;
    /**
     * 开始资源集合
     */
    private ResourceCollection resourceCollection;

    /**
     * 事件结束时资源的状态
     */
    private Resource finishResource;
}
