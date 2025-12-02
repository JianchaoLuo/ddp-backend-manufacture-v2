package cn.hex.ddp.manufacture.infrastructure.algorithm.enums;

import cn.hex.ddp.manufacture.domain.car.enums.CarOperationStatusEnum;
import cn.hex.ddp.manufacture.domain.simulator.enums.FerrySubCarStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassDescription: 描述母车状态的枚举类，用于算法对母车状态的记录，内容与算法文档对应
 * @Author: KangHong
 * @Created: 2024/5/23 17:43
 */
@AllArgsConstructor
@Getter
public enum FerryStatusEnum implements Serializable {
    /**
     * 母车搭载子车
     */
    CARRY_SUB_CAR(0),
    /**
     * 母车未搭载子车
     */
    NOT_CARRY_SUB_CAR(1),
    /**
     * 母车离线
     */
    FERRY_OFFLINE(2),
    /**
     * 母车损坏
     */
    FERRY_BROKEN(3);

    private final Integer code;

    public static List<FerryStatusEnum> fromStatus(CarOperationStatusEnum carOperationStatusEnum,
                                                   FerrySubCarStatus ferrySubCarStatus) {
        List<FerryStatusEnum> ferryStatusEnums = new ArrayList<>();
        if (carOperationStatusEnum == CarOperationStatusEnum.OFFLINE) {
            ferryStatusEnums.add(FERRY_OFFLINE);
        } else if (carOperationStatusEnum == CarOperationStatusEnum.BREAKDOWN) {
            ferryStatusEnums.add(FERRY_BROKEN);
        } else {
            if (ferrySubCarStatus == FerrySubCarStatus.CARRY_SUB_CAR) {
                ferryStatusEnums.add(CARRY_SUB_CAR);
            } else {
                ferryStatusEnums.add(NOT_CARRY_SUB_CAR);
            }
        }
        return ferryStatusEnums;
    }
}
