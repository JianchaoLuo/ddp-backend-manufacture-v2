package cn.hex.ddp.manufacture.infrastructure.socketio


object OutEvents {
    const val META_POSITION = "META_POSITION"
    const val META_PATH = "META_PATH"
    const val META_EQUIPMENT = "META_EQUIPMENT"
    const val META_WORKSTATION = "META_WORKSTATION"
    const val META_CAR = "META_CAR"

    const val CAR_REAL_TIME = "CAR_REAL_TIME"
    const val EQUIPMENT_REAL_TIME = "EQUIPMENT_REAL_TIME"
    const val WORKSTATION_REAL_TIME = "WORKSTATION_REAL_TIME"
    const val POSITION_REAL_TIME = "POSITION_REAL_TIME"
    const val CHART_REAL_TIME = "CHART_REAL_TIME"
    const val PRODUCT_REAL_TIME = "PRODUCT_REAL_TIME"

    const val END_SHOW = "END_SHOW"
}

object InEvents {
    const val INIT_TIME = "INIT_TIME"
    const val READY_SHOW = "READY_SHOW"
    const val APPOINT_TASK = "APPOINT_TASK"

}
