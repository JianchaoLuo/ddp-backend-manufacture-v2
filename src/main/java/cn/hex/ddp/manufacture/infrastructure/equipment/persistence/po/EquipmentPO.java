package cn.hex.ddp.manufacture.infrastructure.equipment.persistence.po;

import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentControlStatusEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentNameEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.tangzc.autotable.annotation.ColumnComment;
import com.tangzc.autotable.annotation.ColumnDefault;
import com.tangzc.autotable.annotation.Index;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.autotable.annotation.UniqueIndex;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 设备PO
 *
 * @author Huhaisen
 * @date 2024/05/03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "ddp_manufacture_service_v2", value = "equipment", comment = "设备表")
public class EquipmentPO extends BaseDatabasePO {
    /**
     * 设备唯一编号
     */
    @Column(comment = "设备唯一编号", length = 255, notNull = true, defaultValue = "''")
    @UniqueIndex
    private String equipmentNo;

    /**
     * 设备型号
     */
    @Column(comment = "设备型号", length = 512, notNull = true, defaultValue = "''")
    private String model;

    /**
     * 设备名称
     */
    @Column(length = 255, notNull = true, defaultValue = "''", comment = "设备名称")
    private String name;

    /**
     * 设备运行状态
     */
    @ColumnComment("设备运行状态")
    @NotNull
    @ColumnDefault("0")
    private EquipmentOperationStatusEnum operationStatus;

    /**
     * 设备控制状态
     */
    @ColumnComment("设备控制状态")
    @NotNull
    @ColumnDefault("0")
    private EquipmentControlStatusEnum controlStatus;

    /**
     * 设备类型
     */
    @ColumnComment("设备类型")
    @NotNull
    @ColumnDefault("0")
    private EquipmentTypeEnum type;

    /**
     * 设备所在坐标
     */
    @Index
    @Column(notNull = true, defaultValue = "0", comment = "设备所在坐标")
    private Long coordinateId;


    /**
     * 展示样式
     */
    @Column(length = 256, notNull = true, defaultValue = "''", comment = "展示样式")
    private String displayStyle;

    /**
     * 关联的PLCId
     */
    @ColumnComment("关联的PLCId")
    @Index
    @Column(notNull = true, defaultValue = "0", comment = "关联的PLCId")
    private Long plcId;

    /**
     * 设备名称枚举（供算法使用）
     */
    @Column(comment = "设备名称枚举（供算法使用）", notNull = true, defaultValue = "0")
    private EquipmentNameEnum nameEnum;
}
