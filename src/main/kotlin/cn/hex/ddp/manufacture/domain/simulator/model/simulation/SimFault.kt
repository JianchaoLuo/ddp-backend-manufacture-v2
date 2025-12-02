//package cn.hex.ddp.manufacture.domain.simulator.model.simulation
//
//import cn.hex.ddp.manufacture.domain.task.enums.ResourceTypeEnum
//
///**
// * 模拟器内部使用的故障模型（简化）
// * resourceType: 目前只支持车辆类型(Sub car / Main car)，由 ResourceTypeEnum 指定
// * resourceId: 资源 id（车辆 id）
// * startTick / endTick: 使用与模拟器 currentTick 相同的时间单位（例如秒或仿真 tick）
// *
// * 注意：这里把时间用 tick 表示，转换逻辑在 SimulateService.kt 中实现（将前端传的 LocalDateTime -> tick）
// */
//data class SimFault(
//    val resourceType: ResourceTypeEnum,
//    val resourceId: Long,
//    val startTick: Long,
//    val endTick: Long
//)