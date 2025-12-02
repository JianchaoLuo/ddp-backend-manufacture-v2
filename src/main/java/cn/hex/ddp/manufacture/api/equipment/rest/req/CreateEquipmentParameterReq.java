package cn.hex.ddp.manufacture.api.equipment.rest.req;

import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentParameterNameEnum;
import lombok.Data;

/**
 * @author zhaolin
 * @date 2024/5/16
 */
@Data
public class CreateEquipmentParameterReq {
    /**
     * 设备参数名
     * 枚举包含中文名，默认值
     */
    private EquipmentParameterNameEnum name;

    /**
     * 设备参数值
     */
    private Double value = 0.0;

    /**
     * 设备参数值范围
     * 例如：[110:114]在这个闭区间内，
     * [xxx,xxy,xxz]在里面选,
     * [xxy:xxz,xxxx:xxxy,abc]
     */
    private String valueRange = "";
}
