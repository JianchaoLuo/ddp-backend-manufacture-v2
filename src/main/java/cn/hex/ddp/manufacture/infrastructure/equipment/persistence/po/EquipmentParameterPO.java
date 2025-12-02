package cn.hex.ddp.manufacture.infrastructure.equipment.persistence.po;

import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentParameterNameEnum;
import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.tangzc.autotable.annotation.ColumnComment;
import com.tangzc.autotable.annotation.ColumnDefault;
import com.tangzc.autotable.annotation.Index;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 设备参数PO
 *
 * @author Huhaisen
 * @date 2024/05/03
 */
@EqualsAndHashCode(callSuper = true)
@AutoDefine
@AutoRepository
@Data
@Table(schema = "ddp_manufacture_service_v2", value = "equipment_parameter", comment = "设备参数表")
public class EquipmentParameterPO extends BaseDatabasePO {

    /**
     * 设备ID
     */
    @Index
    @Column(notNull = true, comment = "设备ID", defaultValue = "0")
    private Long equipmentId;

    /**
     * 设备参数名
     */
    @ColumnComment("设备参数名")
    @NotNull
    @ColumnDefault("0")
    private EquipmentParameterNameEnum name;

    /**
     * 设备参数值
     */
    @ColumnComment("设备参数值")
    @NotNull
    @ColumnDefault("0")
    private Double value;

    /**
     * 设备参数值范围
     */
    @Column(length = 512, notNull = true, defaultValue = "''", comment = "设备参数值范围")
    private String valueRange;

}
