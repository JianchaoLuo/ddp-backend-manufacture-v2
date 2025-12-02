package cn.hex.ddp.manufacture.domain.simulator.enums

enum class SubCarLoadStatus {
    /**
     * 车辆空载
     */
    EMPTY_LOAD,

    /**
     * 车辆满载
     */
    FULL_LOAD,
}

enum class SubCarTopRodStatus {

    /**
     * 车辆顶杆完全落下
     */
    TOP_ROD_FALL_DOWN,

    /**
     * 车辆顶杆完全升起
     */
    TOP_ROD_RAISED,

    /**
     * 车辆顶杆正在落下
     */
    TOP_ROD_IN_FALL_DOWN,

    /**
     * 车辆顶杆正在升起
     */
    TOP_ROD_IN_RAISING
}

enum class SubCarFerryStatus {

    /**
     * 子车正在进入母车
     */
    ENTRY_FERRY,

    /**
     * 子车正在脱离母车
     */
    GET_OFF_FERRY,

    /**
     * 子车完全在母车上
     */
    SUB_CAR_IN_FERRY,

    /**
     * 子车不在母车上
     */
    SUB_CAR_NOT_IN_FERRY,
}

enum class CarLocationStatus {
    /**
     * 在轨
     */
    ON_TRACK,

    /**
     * 不在轨
     */
    OFF_TRACK,
}

enum class CarConnectStatus {
    /**
     * 在线
     */
    ONLINE,

    /**
     * 离线
     */
    OFFLINE,
}

enum class FerrySubCarStatus {
    /**
     * 母车搭载子车
     */
    CARRY_SUB_CAR,

    /**
     * 母车未搭载子车
     */
    NOT_CARRY_SUB_CAR,

    /**
     * 母车正在搭载子车
     */
    ENTRY_FERRY,

    /**
     * 子车正在离开母车
     */
    GET_OFF_FERRY,

}
