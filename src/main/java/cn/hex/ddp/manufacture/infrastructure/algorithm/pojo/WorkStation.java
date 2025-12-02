package cn.hex.ddp.manufacture.infrastructure.algorithm.pojo;

import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationNameEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription: 工岗类，用于算法中描述工岗的信息
 * @Author: KangHong
 * @Created: 2024/5/23 16:57
 */
@Data
@AllArgsConstructor
public class WorkStation implements Serializable {
    /**
     * 工岗的id；
     */
    private Long id;
    /**
     * 工岗的名称
     */
    private String workPositionName;
    /**
     * 工岗的名字枚举
     */
    private WorkstationNameEnum nameEnum;
    /**
     * 工岗的坐标
     */
    private Coordinate workPositionCoordinate;
    /**
     * 工岗的状态
     */
    private WorkstationStatusEnum status;
    /**
     * 工岗中的产品
     */
    private List<Product> product;

    /**
     * 工岗中的零件
     */
    private Product castingProduct;
}
