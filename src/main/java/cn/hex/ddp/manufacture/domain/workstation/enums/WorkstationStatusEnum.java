package cn.hex.ddp.manufacture.domain.workstation.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * 工位状态枚举
 *
 * @author Huhaisen
 * @date 2024/05/03
 */
@Getter
@AllArgsConstructor
public enum WorkstationStatusEnum implements Serializable {

    /**
     * 空闲
     */
    FREE(0),

    /**
     * 被占用
     */
    OCCUPANCY(1),

    /**
     * 作业中（作业工位占用时状态1）
     */
    WORKING(2),

    /**
     * 待搬运（作业工位占用时状态2）
     */
    WAIT_TRANSPORT(3),

    /**
     * 工岗待配对
     */
    WAIT_PAIR(4),

    /**
     * 工岗不可用
     */
    UNAVAILABLE(5),

    /**
     * 工岗准备中
     */
    PREPARING(6),
    ;

    @EnumValue
    private final int code;

}
