package cn.hex.ddp.manufacture.domain.simulator.manager;

import cn.hex.ddp.manufacture.domain.simulator.model.ProductSimulatorData;

import java.util.List;

/**
 * 产品仿真管理器接口
 * @Author: FengZebang
 * @Date: 2025/3/12
 */
public interface ProductSimulatorManager {
    /**
     * 保存单条产品仿真数据
     * @param productSimulatorData
     */
    void saveProductSimulatorData(ProductSimulatorData productSimulatorData);

    /**
     * 获取产品仿真数据
     * @param mission
     * @param startTick
     * @param endTick
     * @return
     */
    List<ProductSimulatorData> getProductSimulatorData(Long mission, Long startTick, Long endTick);
}
