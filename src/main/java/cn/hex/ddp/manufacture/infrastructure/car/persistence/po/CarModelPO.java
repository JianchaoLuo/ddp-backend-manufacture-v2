package cn.hex.ddp.manufacture.infrastructure.car.persistence.po;

import cn.hex.ddp.manufacture.domain.car.enums.CarTypeEnum;
import cn.hex.ddp.manufacture.domain.car.model.CarModelParameter;
import cn.hex.ddp.manufacture.infrastructure.common.handlers.JsonListCarModelParameterTypeHandler;
import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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

import java.util.List;

/**
 * 车辆型号PO
 *
 * @author Huhaisen
 * @date 2024/04/30
 */
@EqualsAndHashCode(callSuper = true)
@AutoDefine
@AutoRepository
@Data
@TableName(autoResultMap = true)
@Table(schema = "ddp_manufacture_service_v2", value = "car_model", comment = "车辆型号表")
public class CarModelPO extends BaseDatabasePO {

    /**
     * 车辆型号唯一编号
     */
    @Column(comment = "车辆型号唯一编号", length = 255, notNull = true, defaultValue = "''")
    @UniqueIndex
    private String modelNo;

    /**
     * 车辆类型
     */
    @ColumnComment("车辆类型")
    @ColumnNotNull
    @ColumnDefault("0")
    private CarTypeEnum type;

    /**
     * 型号参数（JSON格式）
     */
    @TableField(typeHandler = JsonListCarModelParameterTypeHandler.class)
    @Column(type = "JSONB", comment = "型号参数（JSON格式）", notNull = true, defaultValue = "'[]'")
    private List<CarModelParameter> parameters;

}
