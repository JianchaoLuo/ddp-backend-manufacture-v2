package cn.hex.ddp.manufacture.infrastructure.sandbox.persistence.po;

import cn.hex.ddp.manufacture.domain.sandbox.enums.SandboxTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.tangzc.autotable.annotation.ColumnComment;
import com.tangzc.autotable.annotation.ColumnDefault;
import com.tangzc.autotable.annotation.ColumnNotNull;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 砂箱型号PO
 *
 * @author Huhaisen
 * @date 2024/05/03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@Table(schema = "ddp_manufacture_service_v2", value = "sandbox_model", comment = "砂箱型号表")
public class SandboxModelPO extends BaseDatabasePO {

    /**
     * 砂箱型号名
     */
    @Column(comment = "砂箱型号名", length = 32, notNull = true, defaultValue = "''")
    private String modelName;

    /**
     * 砂箱长度
     */
    @ColumnComment("砂箱长度")
    @ColumnNotNull
    @ColumnDefault("0")
    private Float length;

    /**
     * 砂箱宽度
     */
    @ColumnComment("砂箱宽度")
    @ColumnNotNull
    @ColumnDefault("0")
    private Float width;

    /**
     * 砂箱高度
     */
    @ColumnComment("砂箱高度")
    @ColumnNotNull
    @ColumnDefault("0")
    private Float height;

    /**
     * 砂箱空重
     */
    @ColumnComment("砂箱空重")
    @ColumnNotNull
    @ColumnDefault("0")
    private Float emptyWeight;

    /**
     * 砂箱满重
     */
    @ColumnComment("砂箱满重")
    @ColumnNotNull
    @ColumnDefault("0")
    private Float fullWeight;

    /**
     * 砂箱位置
     * 1代表最上层，2代表第二层,...
     */
    @ColumnComment("砂箱位置")
    @ColumnNotNull
    @ColumnDefault("0")
    private Integer layer;

    /**
     * 砂箱冷热类型
     */
    @ColumnComment("砂箱冷热类型")
    @ColumnNotNull
    @ColumnDefault("0")
    private SandboxTypeEnum type;

}
