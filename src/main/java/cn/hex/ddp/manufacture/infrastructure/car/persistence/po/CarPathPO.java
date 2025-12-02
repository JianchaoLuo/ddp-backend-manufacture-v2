package cn.hex.ddp.manufacture.infrastructure.car.persistence.po;

import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.tangzc.autotable.annotation.TableIndex;
import com.tangzc.autotable.annotation.TableIndexes;
import com.tangzc.autotable.annotation.enums.IndexTypeEnum;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 车辆路径关联
 *
 * @author Huhaisen
 * @date 2024/06/03
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AutoDefine
@AutoRepository
@AllArgsConstructor
@TableIndexes({
        @TableIndex(name = "carId_pathId_uni_index",
                fields = {CarPathPODefine.carId, CarPathPODefine.pathId},
                type = IndexTypeEnum.UNIQUE)
})
@Table(schema = "ddp_manufacture_service_v2", value = "car_path", comment = "车辆路径关联表")
public class CarPathPO extends BaseDatabasePO {

    /**
     * 车辆ID
     */
    @Column(comment = "车辆ID", notNull = true, defaultValue = "0")
    private Long carId;

    /**
     * 路径ID
     */
    @Column(comment = "路径ID", notNull = true, defaultValue = "0")
    private Long pathId;
}
