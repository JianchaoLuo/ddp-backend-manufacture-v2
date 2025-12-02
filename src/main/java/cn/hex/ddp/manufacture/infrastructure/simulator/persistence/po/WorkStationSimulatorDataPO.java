package cn.hex.ddp.manufacture.infrastructure.simulator.persistence.po;
import cn.hex.ddp.manufacture.infrastructure.common.po.BaseSimulatorDataPO;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * 工位仿真数据持久化对象
 * @Author: FengZebang
 * @Date: 2025/3/5
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@Table(dsName = "simulation", schema = "ddp_simulation_service", value = "workstation_simulator_data", comment = "工位仿真数据")
public class WorkStationSimulatorDataPO extends BaseSimulatorDataPO {
}