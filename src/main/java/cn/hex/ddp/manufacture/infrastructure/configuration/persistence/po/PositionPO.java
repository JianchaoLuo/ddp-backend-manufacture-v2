package cn.hex.ddp.manufacture.infrastructure.configuration.persistence.po;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.tangzc.autotable.annotation.ColumnComment;
import com.tangzc.autotable.annotation.ColumnDefault;
import com.tangzc.autotable.annotation.ColumnNotNull;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.autotable.annotation.UniqueIndex;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 点位 PO
 *
 * @author Huhaisen
 * @date 2024/04/28
 */
@EqualsAndHashCode(callSuper = true)
@AutoDefine
@AutoRepository
@Data
@Table(schema = "ddp_manufacture_service_v2", value = "position", comment = "点位表")
public class PositionPO extends BaseDatabasePO {

    /**
     * 坐标ID
     */
    @ColumnComment(value = "坐标ID")
    @ColumnNotNull
    @ColumnDefault("0")
    private Long coordinateId;

    /**
     * 点位名称
     */
    @Column(comment = "点位名称", length = 255, notNull = true, defaultValue = "''")
    @UniqueIndex
    private String name;
    /**
     * 点位描述
     */
    @Column(comment = "点位描述", length = 255, notNull = true, defaultValue = "''")
    private String description;

    /**
     * 点位名称枚举(供算法使用)
     */
    @Column(comment = "点位名称枚举(供算法使用)", notNull = true, defaultValue = "0")
    private PositionNameEnum nameEnum;

}
