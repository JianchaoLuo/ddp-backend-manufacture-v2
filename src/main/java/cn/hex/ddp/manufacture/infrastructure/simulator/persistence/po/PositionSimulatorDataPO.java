package cn.hex.ddp.manufacture.infrastructure.simulator.persistence.po;

import cn.hex.ddp.manufacture.infrastructure.common.po.BaseSimulatorDataPO;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;

/**
 * 点位仿真数据持久化对象
 * @Author: FengZebang
 * @Date: 2025/3/11
 */
@Data
@AutoRepository
@AutoDefine
@Table(dsName = "simulation",schema = "ddp_simulation_service", value = "position_simulator_data", comment = "点位仿真数据")
public class PositionSimulatorDataPO extends BaseSimulatorDataPO {
}
