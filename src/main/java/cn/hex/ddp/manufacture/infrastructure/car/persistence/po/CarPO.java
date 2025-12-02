package cn.hex.ddp.manufacture.infrastructure.car.persistence.po;

import cn.hex.ddp.manufacture.domain.car.enums.CarControlStatusEnum;
import cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum;
import cn.hex.ddp.manufacture.domain.car.enums.CarOperationStatusEnum;
import cn.hex.ddp.manufacture.domain.car.enums.CarTypeEnum;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.path.enums.RailDirectionEnum;
import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.tangzc.autotable.annotation.ColumnComment;
import com.tangzc.autotable.annotation.ColumnDefault;
import com.tangzc.autotable.annotation.ColumnNotNull;
import com.tangzc.mpe.autotable.annotation.Column;
import com.tangzc.mpe.autotable.annotation.Table;
import com.tangzc.mpe.autotable.annotation.UniqueIndex;
import com.tangzc.mpe.bind.metadata.annotation.BindEntity;
import com.tangzc.mpe.bind.metadata.annotation.JoinCondition;
import com.tangzc.mpe.processer.annotation.AutoDefine;
import com.tangzc.mpe.processer.annotation.AutoRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 车辆PO
 *
 * @author Huhaisen
 * @date 2024/04/30
 */
@EqualsAndHashCode(callSuper = true)
@AutoDefine
@AutoRepository
@Data
@Table(schema = "ddp_manufacture_service_v2", value = "car", comment = "车辆表")
public class CarPO extends BaseDatabasePO {

    /**
     * 车辆唯一编号
     */
    @Column(comment = "车辆唯一编号", length = 255, notNull = true, defaultValue = "''")
    @UniqueIndex
    private String carNo;

    /**
     * 车辆名称
     */
    @Column(comment = "车辆名称", length = 255, notNull = true, defaultValue = "''")
    private String name;

    /**
     * 车辆类型
     */
    @ColumnComment("车辆类型")
    @ColumnNotNull
    @ColumnDefault("0")
    private CarTypeEnum type;

    /**
     * 车辆运行状态
     */
    @ColumnComment("车辆运行状态")
    @ColumnNotNull
    @ColumnDefault("0")
    private CarOperationStatusEnum operationStatus;

    /**
     * 车辆控制状态
     */
    @ColumnComment("车辆控制状态")
    @ColumnNotNull
    @ColumnDefault("0")
    private CarControlStatusEnum controlStatus;

    /**
     * 所属区域
     */
    @ColumnComment("所属区域")
    @ColumnNotNull
    @ColumnDefault("0")
    private AreaEnum area;

    /**
     * 车辆型号ID
     */
    @ColumnComment("车辆型号ID")
    @ColumnNotNull
    @ColumnDefault("0")
    private Long carModelId;

    /**
     * 车头朝向
     */
    @ColumnComment("车头朝向")
    @ColumnNotNull
    @ColumnDefault("0")
    private RailDirectionEnum headDirection;

    /**
     * 车辆型号, 连表查询
     */
    @BindEntity(conditions = @JoinCondition(selfField = CarPODefine.carModelId))
    private CarModelPO model;

    /**
     * 车辆路径关联信息, 连表查询
     */
    @BindEntity(conditions = @JoinCondition(selfField = CarPODefine.id, joinField = CarPathPODefine.carId))
    private List<CarPathPO> carPaths;


    /**
     * 所绑定的车辆ID
     */
    @Column(comment = "所绑定的车辆ID", notNull = true, defaultValue = "0")
    private Long bindCarId;

    /**
     * 所绑定的车辆
     */
    @BindEntity(conditions = @JoinCondition(selfField = CarPODefine.bindCarId))
    private CarPO bindCar;

    /**
     * 车辆名字枚举（供算法使用）
     */
    @Column(comment = "车辆名字枚举（供算法使用）", notNull = true, defaultValue = "0")
    private CarNameEnum nameEnum;

}
