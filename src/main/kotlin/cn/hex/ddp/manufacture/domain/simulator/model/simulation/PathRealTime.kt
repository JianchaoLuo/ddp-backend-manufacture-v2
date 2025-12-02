package cn.hex.ddp.manufacture.domain.simulator.model.simulation

import cn.hex.ddp.manufacture.domain.path.enums.RailDirectionEnum
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate

data class Path(
    val id: Long,
    val name: String,
    val length: Double = 100.0,
    val startCoordinate: Coordinate = Coordinate(0.0, 0.0),
    val endCoordinate: Coordinate = Coordinate(0.0, 0.0),
    var direction: RailDirectionEnum = RailDirectionEnum.TOP_TO_BOTTOM,
    val status: PathStatusEnum,
)
