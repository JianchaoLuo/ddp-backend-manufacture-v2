package cn.hex.ddp.manufacture.infrastructure.configuration.persistence.po;

import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.tangzc.autotable.annotation.ColumnComment;
import com.tangzc.autotable.annotation.ColumnDefault;
import com.tangzc.autotable.annotation.ColumnNotNull;
import com.tangzc.autotable.annotation.TableIndex;
import com.tangzc.autotable.annotation.enums.IndexTypeEnum;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 坐标 PO
 *
 * @author Huhaisen
 * @date 2024/04/28
 */
@EqualsAndHashCode(callSuper = true)
@AutoDefine
@AutoRepository
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableIndex(name = "unique_x_y", fields = {CoordinatePODefine.x, CoordinatePODefine.y}, type = IndexTypeEnum.UNIQUE)
@Table(schema = "ddp_manufacture_service_v2", value = "coordinate", comment = "坐标")
public class CoordinatePO extends BaseDatabasePO {

    /**
     * 横坐标
     */
    @ColumnComment(value = "横坐标")
    @ColumnNotNull
    @ColumnDefault("0")
    private Float x;

    /**
     * 纵坐标
     */
    @ColumnComment(value = "纵坐标")
    @ColumnNotNull
    @ColumnDefault("0")
    private Float y;

    public CoordinatePO(int x, int y) {
        this.x = (float) x;
        this.y = (float) y;
    }
}
