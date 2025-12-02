package cn.hex.ddp.manufacture.infrastructure.algorithm.pojo;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassDescription: 点位类，用于算法中描述点位的信息
 * @Author: KangHong
 * @Created: 2024/5/23 16:36
 */
@Data
@AllArgsConstructor
public class Position implements Serializable {
    /**
     * 点位id
     */
    private Long id;
    /**
     * 点位名称
     */
    private String name;
    /**
     * 点位名称枚举
     */
    private PositionNameEnum nameEnum;
    /**
     * 点位坐标
     */
    private Coordinate coordinate;
    /**
     * 点位状态
     */
    private PositionStatusEnum status;
    /**
     * 点位中的产品
     */
    private Product product;

    /**
     * 点位中的托盘
     */
    private Tray tray;

    public Position() {
    }
}
