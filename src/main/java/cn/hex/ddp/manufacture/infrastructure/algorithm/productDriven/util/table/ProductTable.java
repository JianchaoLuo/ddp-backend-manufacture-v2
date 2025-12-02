package cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.util.table;

import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.enums.EventType;
import cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.util.DriveProduct;
import cn.hex.ddp.manufacture.infrastructure.algorithm.productDriven.util.ProductEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription: 系统维护的产品表，用于存储目前订单要生产的所有产品
 * @Author: KangHong
 * @Created: 2024/11/17 20:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductTable implements Serializable {
    /**
     * 要生产的产品列表
     */
    private List<DriveProduct> products;

    /**
     * 获取当前产品表中，应执行的所有事件列表
     * （每个产品中应执行的事件，即第一个未开始或正在执行的事件）
     * 当一个产品的所有事件执行完成时，列表中不应包括null，应不包含完成事件的产品的事件位置
     * @return List<ProductEvent></>
     */
    public List<ProductEvent> getExecuteEventLists() {

        return null;
    }

    /**
     * 当一个事件执行完成时，根据对应的产品，更新对应的应执行的下一个事件
     * 不需要重新生产列表，从而提高效率;
     * 注意，当一个产品的所有事件执行完成时，应调用getExecuteEventLists()方法，
     * 生成一个新的不包含对应产品列的列表
     * @param product 完成事件的对应产品
     * @return 更新后的List<ProductEvent></>
     */
    public List<ProductEvent> updateExecuteEventLists(Product product) {

        return null;
    }

    /**
     * 更新产品事件表中事件执行状态的方法，调用此方法，将事件从未开始变为正在执行或者已完成、
     * 在开始执行事件时，或事件完成后调用此方法
     * @param product 更改事件状态对应的产品
     * @param eventType 更改后的事件状态枚举
     */
    public void setProductEventType(Product product, EventType eventType){

    }
}
