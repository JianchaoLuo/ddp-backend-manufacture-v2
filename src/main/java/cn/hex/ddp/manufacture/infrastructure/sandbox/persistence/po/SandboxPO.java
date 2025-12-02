package cn.hex.ddp.manufacture.infrastructure.sandbox.persistence.po;

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
 * 砂箱 PO
 *
 * @author Huhaisen
 * @date 2024/05/03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@Table(schema = "ddp_manufacture_service_v2", value = "sandbox", comment = "砂箱表")
public class SandboxPO extends BaseDatabasePO {

    /**
     * 砂箱编号
     * 每个单体砂箱的唯一物理编码
     */
    @Column(comment = "砂箱编号", length = 32, notNull = true, defaultValue = "''")
    @UniqueIndex
    private String sandboxNo;

    /**
     * 砂箱名称
     */
    @ColumnComment("砂箱名称")
    @Column(comment = "砂箱名称", length = 32, notNull = true, defaultValue = "''")
    private String name;

    /**
     * 砂箱版本
     */
    @Column(comment = "砂箱版本", length = 20, notNull = true, defaultValue = "0.0.0")
    private String version;

    /**
     * 砂箱型号ID
     */
    @ColumnComment("砂箱型号ID")
    @ColumnNotNull
    @ColumnDefault(value = "0")
    private Long sandboxModelId;

    /**
     * 砂箱状态
     * 有没有被使用过之类的，是否可用...
     */
    @Column(comment = "砂箱状态", length = 512, notNull = true, defaultValue = "''")
    private String status;

}
