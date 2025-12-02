package cn.hex.ddp.manufacture.infrastructure.simulator.persistence.po;

import cn.hex.ddp.manufacture.infrastructure.common.po.BaseSimulatorDataPO;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备仿真持久化对象
 * @Author: FengZebang
 * @Date: 2025/3/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoDefine
@AutoRepository
@Table(dsName = "simulation", schema = "ddp_simulation_service", value = "equipment_simulator_data", comment = "设备仿真数据")
public class EquipmentSimulatorPO extends BaseSimulatorDataPO {

}
