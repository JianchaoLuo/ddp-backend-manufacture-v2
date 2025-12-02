package cn.hex.ddp.manufacture.infrastructure.algorithm.enums;

import cn.hex.ddp.manufacture.domain.car.enums.CarOperationStatusEnum;
import cn.hex.ddp.manufacture.domain.simulator.enums.SubCarFerryStatus;
import cn.hex.ddp.manufacture.domain.simulator.enums.SubCarLoadStatus;
import cn.hex.ddp.manufacture.domain.simulator.enums.SubCarTopRodStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassDescription: 描述子车状态的枚举类，用于算法对子车状态的记录，内容与算法文档对应
 * @Author: KangHong
 * @Created: 2024/5/23 17:36
 */
@AllArgsConstructor
@Getter
public enum SubCarStatusEnum implements Serializable {
    /**
     * 子车空载
     */
    EMPTY_LOAD(0),
    /**
     * 子车满载
     */
    FULL_LOAD(1),
    /**
     * 子车顶杆完全落下
     */
    TOP_ROD_FALL_DOWN(2),
    /**
     * 子车顶杆完全升起
     */
    TOP_ROD_RAISED(3),
    /**
     * 子车顶杆正在落下
     */
    TOP_ROD_IN_FALL_DOWN(4),
    /**
     * 子车顶杆正在升起
     */
    TOP_ROD_IN_RAISING(5),
    /**
     * 子车完全在母车上
     */
    SUB_CAR_IN_FERRY(6),
    /**
     * 子车不在母车上
     */
    SUB_CAR_NOT_IN_FERRY(7),
    /**
     * 子车离线
     */
    SUB_CAR_OFFLINE(8),
    /**
     * 子车故障
     */
    SUB_CAR_BROKEN(9),
    /**
     * 半满载状态，现只有小车装载托盘的时候
     */
    HALF_LOAD(10),
    ;

    private final Integer code;


    public static List<SubCarStatusEnum> fromStatus(SubCarLoadStatus loadStatus,
                                             SubCarTopRodStatus topRodStatus,
                                             SubCarFerryStatus subCarFerryStatus,
                                             CarOperationStatusEnum carOperationStatusEnum) {
        List<SubCarStatusEnum> result = new ArrayList<>();
        if (!(loadStatus == null)) {
            switch (loadStatus) {
                case FULL_LOAD -> result.add(FULL_LOAD);
                case EMPTY_LOAD -> result.add(EMPTY_LOAD);
            }
        }
        if (!(topRodStatus == null)) {
            switch (topRodStatus) {
                case TOP_ROD_FALL_DOWN -> result.add(TOP_ROD_FALL_DOWN);
                case TOP_ROD_RAISED -> result.add(TOP_ROD_RAISED);
                case TOP_ROD_IN_FALL_DOWN -> result.add(TOP_ROD_IN_FALL_DOWN);
                case TOP_ROD_IN_RAISING -> result.add(TOP_ROD_IN_RAISING);
            }
        }
        if (!(subCarFerryStatus == null)) {
            switch (subCarFerryStatus) {
                case SUB_CAR_IN_FERRY -> result.add(SUB_CAR_IN_FERRY);
                case SUB_CAR_NOT_IN_FERRY -> result.add(SUB_CAR_NOT_IN_FERRY);
            }
        }
        if (!(carOperationStatusEnum == null)) {
            switch (carOperationStatusEnum) {
                case OFFLINE -> result.add(SUB_CAR_OFFLINE);
                case BREAKDOWN -> result.add(SUB_CAR_BROKEN);
            }
        }
        return result;

    }
}
