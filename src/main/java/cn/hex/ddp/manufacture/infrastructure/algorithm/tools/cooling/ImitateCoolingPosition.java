package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.cooling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/8/1 1:58
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImitateCoolingPosition implements Serializable {
    /**
     * 产品冷却时间
     */
    double coolingTime;
    /**
     * 仿真的总时间
     */
    double totalTime;
}
