package cn.hex.ddp.manufacture.infrastructure.algorithm.util;

import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ExecuteTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.InstructionsEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassDescription: 指令类，用于表示事件中执行的指令
 * @Author: KangHong
 * @Created: 2024/5/30 20:06
 */
@AllArgsConstructor
@Data
public class Instruction implements Serializable {
    /**
     * 子车id，母车id，设备id或工岗id
     * 其他存在是为null
     */
    Long id;
    /**
     * 指令执行者类型
     */
    ExecuteTypeEnum executeType;
    /**
     * 指令类型
     */
    InstructionsEnum instructions;
    /**
     * 如果是移动指令，表示移动的目标坐标
     * 不是移动指令时为null
     */
    Coordinate runAimCoordinate;

    /**
     * 铁水容量
     */
    Double moltenIronQuantity;

    public Instruction(Long id, ExecuteTypeEnum executeType, InstructionsEnum instructions,
                       Coordinate runAimCoordinate) {
        this.id = id;
        this.executeType = executeType;
        this.instructions = instructions;
        this.runAimCoordinate = runAimCoordinate;
    }

    //    /**
//     * 如果有产品，则有
//     * 没有产品为null
//     */
//    List<Product> products;
}
