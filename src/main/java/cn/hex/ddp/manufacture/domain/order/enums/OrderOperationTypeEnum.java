package cn.hex.ddp.manufacture.domain.order.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单操作类型枚举
 *
 * @author Huhaisen
 * @date 2024/05/05
 */
@Getter
@AllArgsConstructor
public enum OrderOperationTypeEnum {

    /**
     * 创建订单
     */
    CREATE_ORDER(0),

    /**
     * 变更产品
     */
    CHANGE_PRODUCT(1),

    /**
     * 取消订单
     */
    CANCEL_ORDER(2),

    /**
     * 修改订单
     */
    MODIFY_ORDER(3),
    ;

    @EnumValue
    private final int code;

}
