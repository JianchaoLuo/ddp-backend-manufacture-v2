package cn.hex.ddp.manufacture.infrastructure.common.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.tangzc.autotable.annotation.Index;
import com.tangzc.autotable.annotation.pgsql.PgsqlTypeConstant;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.ColumnId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 仿真数据基础数据库PO
 * 这里将仿真数据的基础字段抽取出来，作为所有仿真数据的基类。
 *
 * @author Huhaisen
 * @date 2024/12/26
 */
@Data
public class BaseSimulatorDataPO {

    /**
     * 主键
     */
    @ColumnId(mode = IdType.ASSIGN_ID, comment = "主键ID")
    protected Long id;

    /**
     * 仿真任务编号
     */
    @Column(comment = "仿真任务编号", notNull = true, defaultValue = "0")
    private Long mission;

    /**
     * 当前帧
     */
    @Index
    @Column(comment = "当前帧", notNull = true, defaultValue = "0")
    private Long tick;

    /**
     * 仿真当前时间
     */
    @Index
    @Column(comment = "仿真当前时间", notNull = true, defaultValue = "CURRENT_TIMESTAMP")
    private LocalDateTime simulationTime;

    /**
     * 当前帧所有数据（JSON格式）
     */
    @Column(type = PgsqlTypeConstant.TEXT, comment = "当前帧所有数据", notNull = true, defaultValue = "'[]'")
    private String data;

}
