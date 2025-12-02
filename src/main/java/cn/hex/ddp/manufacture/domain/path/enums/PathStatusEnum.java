package cn.hex.ddp.manufacture.domain.path.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * @author kanghong
 * @description
 * @date 2024-05-28 14:38
 */
@AllArgsConstructor
@Getter
public enum PathStatusEnum implements Serializable {
    /**
     * 轨道空闲
     */
    PATH_UNOCCUPIED(0),
    /**
     * 轨道被占用
     */
    PATH_OCCUPIED(1),
    /**
     * 轨道不可用
     */
    PATH_UNAVAILABLE(2);

    @EnumValue
    private final int code;
}
