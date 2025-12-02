package cn.hex.ddp.manufacture.domain.equipment.model;

import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentParameterNameEnum;
import lombok.Data;

/**
 * 设备参数模型
 *
 * @author Huhaisen
 * @date 2024/05/03
 */
@Data
public class EquipmentParameter {
    /**
     * 设备参数ID(数据库ID)
     */
    private Long id;

    /**
     * 设备ID
     */
    private Long equipmentId;

    /**
     * 设备参数名
     * 枚举包含中文名，默认值
     */
    private EquipmentParameterNameEnum name;

    /**
     * 设备参数值
     */
    private Double value;

    /**
     * 设备参数值范围
     * 例如：[110:114]在这个闭区间内，
     * [xxx,xxy,xxz]在里面选,
     * [xxy:xxz,xxxx:xxxy,abc]
     */
    private String valueRange;

}
