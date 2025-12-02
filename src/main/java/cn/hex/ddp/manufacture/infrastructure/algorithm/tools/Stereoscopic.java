package cn.hex.ddp.manufacture.infrastructure.algorithm.tools;


import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;
import lombok.Data;

import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductAfootEnum.GOING_MOLD_CLOSING;

/**
 * @ClassDescription: 立体库模拟出库的工具类
 * @Author: KangHong
 * @Created: 2024/6/3 23:06
 */
@Data
public class Stereoscopic {
    /**
     * 雪花函数生成id
     */
    private SnowflakeIdGenerator idWorker = new SnowflakeIdGenerator(1, 1);
    /**
     * 立体库出库模拟
     * @param product 产品信息
     * @return 出库的产品
     */
    public Product outBoundProduct(Product product){
        //生成产品id
        product.setId(idWorker.nextId());
        /**
         * 更新产品的状态为前往合模
         */
        product.setProductAfoot(GOING_MOLD_CLOSING);
        /**
         * 更新产品状态为不需要回流
         */
        product.setRefluxFlag(false);

        Product product1 = product;

        return product1;
    }
}
