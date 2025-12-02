package cn.hex.ddp.manufacture.infrastructure.mold.persistence.po;

import cn.hex.ddp.manufacture.domain.mold.enums.MoldTypeEnum;
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
 * 模具型号 PO
 *
 * @author Huhaisen
 * @date 2024/05/04
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@Table(schema = "ddp_manufacture_service_v2", value = "mold_model", comment = "模具型号")
public class MoldModelPO extends BaseDatabasePO {

    /**
     * 模具型号名
     */
    @Column(comment = "模具型号名", length = 32, notNull = true, defaultValue = "''")
    private String modelName;

    /**
     * 模具类型
     */
    @ColumnComment(value = "模具类型")
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private MoldTypeEnum type;

    /**
     * 模具位置
     * 1代表最上层，2代表第二层,....
     */
    @ColumnComment(value = "模具位置")
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private Integer layer;

    /**
     * 模具长度
     */
    @ColumnComment(value = "模具长度")
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private Float length;

    /**
     * 模具宽度
     */
    @ColumnComment(value = "模具宽度")
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private Float width;

    /**
     * 模具高度
     */
    @ColumnComment(value = "模具高度")
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private Float height;

    /**
     * 模具重量
     */
    @ColumnComment(value = "模具重量")
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private Float weight;

}
