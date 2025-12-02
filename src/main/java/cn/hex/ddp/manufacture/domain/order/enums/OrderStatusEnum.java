package cn.hex.ddp.manufacture.domain.order.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态枚举
 *
 * @author Huhaisen
 * @date 2024/05/05
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    /**
     * 待处理（计划删除）
     */
    @Deprecated
    PENDING(0),

    /**
     * 进行中
     */
    PROGRESSING(1),

    /**
     * 已完成
     */
    FINISHED(2),

    /**
     * 已取消
     */
    CANCELLED(3),

    /**
     * 待排产
     */
    PENDING_SCHEDULING(4),

    /**
     * 待生产
     */
    PENDING_PRODUCTION(5),
    ;

    @EnumValue
    private final int code;

}
