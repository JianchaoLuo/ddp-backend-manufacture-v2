package cn.hex.ddp.manufacture.infrastructure.workstation.persistence.po;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.common.enums.ProcessEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationNameEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
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
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 工位PO
 *
 * @author Huhaisen
 * @date 2024/05/03
 */
@EqualsAndHashCode(callSuper = true)
@AutoDefine
@AutoRepository
@Data
@Table(schema = "ddp_manufacture_service_v2", value = "workstation", comment = "工位表")
public class WorkstationPO extends BaseDatabasePO {

    /**
     * 工位编号
     */
    @Column(comment = "工位编号", length = 255, notNull = true, defaultValue = "''")
    @UniqueIndex
    private String stationNo;

    /**
     * 工位名称
     */
    @Column(length = 255, notNull = true, defaultValue = "''", comment = "工位名称")
    private String name;

    /**
     * 工位位置ID（坐标ID）
     */
    @Index
    @Column(notNull = true, comment = "工位位置ID", defaultValue = "0")
    private Long coordinateId;

    /**
     * 工位描述
     */
    @Column(comment = "工位描述", length = 512, notNull = true, defaultValue = "''")
    private String description;

    /**
     * 所属分区
     */
    @ColumnComment("所属分区")
    @NotNull
    @ColumnDefault("0")
    private AreaEnum area;

    /**
     * 所属工序
     */
    @ColumnComment("所属工序")
    @NotNull
    @ColumnDefault("0")
    private ProcessEnum process;

    /**
     * 工位优先级
     */
    @ColumnComment("工位优先级")
    @NotNull
    @ColumnDefault("0")
    private Integer priority;

    /**
     * 工位状态
     */
    @ColumnComment("工位状态")
    @NotNull
    @ColumnDefault("0")
    private WorkstationStatusEnum status;

    /**
     * 工岗名称枚举（供算法使用）
     */
    @Column(comment = "工岗名称枚举", notNull = true, defaultValue = "0")
    private WorkstationNameEnum nameEnum;
}
