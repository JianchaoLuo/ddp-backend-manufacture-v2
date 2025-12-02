package cn.hex.ddp.manufacture.infrastructure.simulator.persistence.managerimpl;

import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.domain.simulator.manager.ProductSimulatorManager;
import cn.hex.ddp.manufacture.domain.simulator.model.ProductSimulatorData;
import cn.hex.ddp.manufacture.infrastructure.common.dynamic.DynamicTableNameContext;
import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.managerimpl.converter.ProductSimulatorInfraConverter;
import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.po.ProductSimulatorDataPO;
import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.postgresql.repository.ProductSimulatorDataPORepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 产品仿真管理器实现类
 * @Author: FengZebang
 * @Date: 2025/3/12
 */
@Slf4j
@Repository
public class ProductSimulatorManagerImpl implements ProductSimulatorManager {

    @Autowired
    private ProductSimulatorDataPORepository productSimulatorDataPORepository;

    @Autowired
    private ProductSimulatorInfraConverter productSimulatorInfraConverter;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProductSimulatorData(ProductSimulatorData productSimulatorData) {
        if(Optional.ofNullable(productSimulatorData.getMission()).isEmpty()){
            log.warn("[ProductSimulatorManagerImpl.saveProductSimulatorData] 任务编号不能为空");
            throw new BusinessException(BssExType.SIMULATOR_MISSION_VALIDATE_ERROR, "任务编号不能为空");
        }

        ProductSimulatorDataPO productSimulatorDataPO = productSimulatorInfraConverter.toProductSimulatorDataPO(productSimulatorData);
        try {
            DynamicTableNameContext.setMissionId(productSimulatorDataPO.getMission());
            productSimulatorDataPORepository.save(productSimulatorDataPO);
        }finally {
            DynamicTableNameContext.clear();
        }
    }

    @Override
    public List<ProductSimulatorData> getProductSimulatorData(Long mission, Long startTick, Long endTick) {
        if(Optional.ofNullable(mission).isEmpty()){
            log.warn("[ProductSimulatorManagerImpl.getProductSimulatorData] 任务编号不能为空");
            throw new BusinessException(BssExType.SIMULATOR_MISSION_VALIDATE_ERROR, "任务编号不能为空");
        }
        if(startTick == null || endTick == null || startTick > endTick){
            log.warn("[ProductSimulatorManagerImpl.getProductSimulatorData] 帧不能为空，且开始帧不能大于结束帧");
            throw new BusinessException(BssExType.TICK_VALIDATE_ERROR, "帧不能为空，且开始帧不能大于结束帧");
        }
        try {
            DynamicTableNameContext.setMissionId(mission);
            List<ProductSimulatorDataPO> productSimulatorDataPOList = productSimulatorDataPORepository.lambdaQuery()
                    .eq(ProductSimulatorDataPO::getMission, mission)
                    .ge(ProductSimulatorDataPO::getTick, startTick)
                    .le(ProductSimulatorDataPO::getTick, endTick)
                    .list();
            return productSimulatorInfraConverter.toProductSimulatorDataList(productSimulatorDataPOList);
        }finally {
            DynamicTableNameContext.clear();
        }
    }
}
