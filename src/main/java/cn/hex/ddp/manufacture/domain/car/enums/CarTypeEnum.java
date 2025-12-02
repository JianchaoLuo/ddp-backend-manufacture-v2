package cn.hex.ddp.manufacture.domain.car.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 车辆类型枚举
 *
 * @author Huhaisen
 * @date 2024/04/30
 */
@Getter
@AllArgsConstructor
public enum CarTypeEnum {

    /**
     * 摆渡车（母车）
     */
    FERRY_CAR(0),

    /**
     * 小车
     */
    SUB_CAR(1),


    /**
     * 行车
     */
    TRAVELLING_CRANE(2),
    ;

    @EnumValue
    private final int code;



}
