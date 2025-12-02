package cn.hex.ddp.manufacture.infrastructure.plc.persistence.po;

import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.tangzc.autotable.annotation.AutoTable;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 冯泽邦
 * @date 2025/11/3
 * PLC与资源ID关联表
 */
@EqualsAndHashCode(callSuper = true)
@AutoTable
@AutoRepository
@Data
@Table(schema = "ddp_manufacture_service_v2", value = "plc_resource", comment = "PLC资源表")
public class PLCResourcePO extends BaseDatabasePO {
    /**
     * 资源id
     */
    private Long resourceId;

    /**
     * PLC_ID
     */
    private Long PLCID;
}
