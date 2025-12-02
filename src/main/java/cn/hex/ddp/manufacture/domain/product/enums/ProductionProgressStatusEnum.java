package cn.hex.ddp.manufacture.domain.product.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 计划排产-生产进度枚举类
 * @Author: FengZebang
 * @Date: 2025/5/27
 */
@Getter
@AllArgsConstructor
public enum ProductionProgressStatusEnum {
    /**
     * 未开始
     */
    NOT_STARTED(0,"未开始"),
    /**
     * 已完成出库
     */
    COMPLETED_OUTBOUND(1,"已出库"),
    /**
     * 已完成合模
     */
    COMPLETED_MOLDING(2,"已合模"),
    /**
     * 已完成射沙
     */
    COMPLETED_SAND(3,"射砂完毕"),
    /**
     * 已完成喷涂
     */
    COMPLETED_SPRAY(4,"喷涂完毕"),
    /**
     * 已完成合箱
     */
    COMPLETED_CLOSING_BOX(5,"已合箱"),
    /**
     * 已完成浇筑
     */
    COMPLETED_POURING(6,"已浇筑"),
    /**
     * 已冷却
     */
    COMPLETED_COOLING(7,"已冷却"),
    /**
     * 已开箱
     */
    COMPLETED_OPENING_BOX(8,"已开箱"),
    /**
     * 已完成
     */
    COMPLETED(9,"已完成");

    @EnumValue
    private final int code;

    private final String description;
}
