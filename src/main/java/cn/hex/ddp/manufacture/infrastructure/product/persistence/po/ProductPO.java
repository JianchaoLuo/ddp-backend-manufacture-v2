package cn.hex.ddp.manufacture.infrastructure.product.persistence.po;

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
 * 产品 PO
 *
 * @author Huhaisen
 * @date 2024/05/05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@Table(schema = "ddp_manufacture_service_v2", value = "product", comment = "产品表")
public class ProductPO extends BaseDatabasePO {

    /**
     * 产品名称
     */
    @ColumnComment(value = "产品名称")
    @Column(length = 63, notNull = true, defaultValue = "''")
    @UniqueIndex
    private String name;

    /**
     * 产品型号
     */
    @ColumnComment(value = "产品型号")
    @Column(length = 63, notNull = true, defaultValue = "''")
    @UniqueIndex
    private String model;

    /**
     * 长
     */
    @ColumnComment(value = "长")
    @ColumnNotNull
    @ColumnDefault("0")
    private Float length;

    /**
     * 宽
     */
    @ColumnComment(value = "宽")
    @ColumnNotNull
    @ColumnDefault("0")
    private Float width;

    /**
     * 高
     */
    @ColumnComment(value = "高")
    @ColumnNotNull
    @ColumnDefault("0")
    private Float height;

    /**
     * 直径
     */
    @ColumnComment(value = "直径")
    @ColumnNotNull
    @ColumnDefault("0")
    private Float diameter;

    /**
     * 内径
     */
    @ColumnComment(value = "内径")
    @ColumnNotNull
    @ColumnDefault("0")
    private Float innerDiameter;

    /**
     * 产品重量
     */
    @ColumnComment(value = "产品重量")
    @ColumnNotNull
    @ColumnDefault("0")
    private Float weight;

    /**
     * 产品材质编号
     */
    @ColumnComment(value = "产品材质编号")
    @Column(length = 63, notNull = true, defaultValue = "''")
    private String materialNo;

    /**
     * 工艺ID
     */
    @ColumnComment(value = "工艺ID")
    @ColumnNotNull
    @ColumnDefault("0")
    private Long techniqueId;

}
