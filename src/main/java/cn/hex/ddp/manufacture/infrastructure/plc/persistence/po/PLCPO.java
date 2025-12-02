package cn.hex.ddp.manufacture.infrastructure.plc.persistence.po;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.plc.enums.PLCTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.tangzc.autotable.annotation.ColumnComment;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * PLC PO
 *
 * @author Huhaisen
 * @date 2024/05/03
 */
@EqualsAndHashCode(callSuper = true)
@AutoDefine
@AutoRepository
@Data
@Table(schema = "ddp_manufacture_service_v2", value = "plc", comment = "PLC数据表")
public class PLCPO extends BaseDatabasePO {

    /**
     * ip地址(PLC的IP地址)
     */
    @ColumnComment("ip地址(PLC的IP地址)")
    private String ip;

    /**
     * PLC类型
     */
    @ColumnComment("PLC类型")
    private PLCTypeEnum type;

    /**
     * 所属区域(一个PLC对应一个区域)
     */
    @ColumnComment("所属区域(一个PLC对应一个区域)")
    private AreaEnum area;

}
