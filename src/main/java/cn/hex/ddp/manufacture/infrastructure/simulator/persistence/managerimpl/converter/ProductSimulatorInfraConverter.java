package cn.hex.ddp.manufacture.infrastructure.simulator.persistence.managerimpl.converter;

import cn.hex.ddp.manufacture.domain.simulator.model.ProductSimulatorData;
import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.po.ProductSimulatorDataPO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 产品仿真数据基础转换器
 * @Author: FengZebang
 * @Date: 2025/3/12
 */
@Mapper(componentModel = "spring")
public interface ProductSimulatorInfraConverter {
    ProductSimulatorDataPO toProductSimulatorDataPO(ProductSimulatorData productSimulatorData);

    List<ProductSimulatorData> toProductSimulatorDataList(List<ProductSimulatorDataPO> productSimulatorDataPOList);
}
