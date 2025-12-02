package cn.hex.ddp.manufacture.infrastructure.simulator.persistence.po;

import cn.hex.ddp.manufacture.infrastructure.common.po.BaseSimulatorDataPO;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 产品仿真持久化对象
 * @Author: FengZebang
 * @Date: 2025/3/12
 */
@Data
@AutoDefine
@AutoRepository
@EqualsAndHashCode(callSuper = true)
@Table(dsName = "simulation", schema = "ddp_simulation_service", value = "product_simulator_data", comment = "产品仿真数据")
public class ProductSimulatorDataPO extends BaseSimulatorDataPO {

}
