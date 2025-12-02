package cn.hex.ddp.manufacture.infrastructure.common.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.tangzc.mpe.autotable.annotation.ColumnId;
import com.tangzc.mpe.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Base database po
 *
 * @author Huhaisen
 * @date 2024/04/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseDatabasePO extends BaseEntity<Long, LocalDateTime> {

    /**
     * 主键
     */
    @ColumnId(mode = IdType.ASSIGN_ID, comment = "主键ID")
    protected Long id;

}
