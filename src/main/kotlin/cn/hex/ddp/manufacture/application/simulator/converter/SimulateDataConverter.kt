package cn.hex.ddp.manufacture.application.simulator.converter

import cn.hex.ddp.manufacture.api.equipment.rest.vo.EquipmentVO
import cn.hex.ddp.manufacture.api.workstation.rest.vo.WorkstationVO
import cn.hex.ddp.manufacture.application.car.dto.out.CarDetailDTO
import cn.hex.ddp.manufacture.application.car.dto.out.CarPathDetailDTO
import cn.hex.ddp.manufacture.application.configuration.dto.out.CoordinateDTO
import cn.hex.ddp.manufacture.application.configuration.dto.out.PositionDTO
import cn.hex.ddp.manufacture.application.path.dto.out.PathDTO
import cn.hex.ddp.manufacture.domain.car.enums.CarOperationStatusEnum
import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentOperationStatusEnum
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentParameterNameEnum
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum
import cn.hex.ddp.manufacture.domain.simulator.enums.FerrySubCarStatus
import cn.hex.ddp.manufacture.domain.simulator.enums.SubCarFerryStatus
import cn.hex.ddp.manufacture.domain.simulator.enums.SubCarLoadStatus
import cn.hex.ddp.manufacture.domain.simulator.enums.SubCarTopRodStatus
import cn.hex.ddp.manufacture.domain.simulator.model.simulation.*
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate
import cn.hex.ddp.manufacture.infrastructure.common.converter.AlgorithmDataConverter
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

/**
 * 模拟器数据转换器
 * 用于将后端的数据转换成模拟器中的数据格式，由于涉及的类较多，因此抽取到单独的转换类中
 *
 * @author Huhaisen
 * @date 2024/07/12
 * @constructor 创建[SimulateDataConverter]
 */
object SimulateDataConverter {

    private fun mapPath(carPathDetailDTO: CarPathDetailDTO): Path {
        return Path(
            id = carPathDetailDTO.path.id,
            name = carPathDetailDTO.path.name,
            length = carPathDetailDTO.path.railLength?.toDouble() ?: 0.0,
            startCoordinate = mapCoordinate(carPathDetailDTO.path.startCoordinate),
            endCoordinate = mapCoordinate(carPathDetailDTO.path.endCoordinate),
            direction = carPathDetailDTO.path.railDirection,
            status = carPathDetailDTO.path.status
        )
    }

    private fun mapCoordinate(coordinateDTO: CoordinateDTO): Coordinate {
        // 需要使用比例尺进行转换
        return Coordinate(
            coordinateDTO.x?.toDouble()?.times(AlgorithmDataConverter.PLOTTING_SCALE) ?: 0.0,
            coordinateDTO.y?.toDouble()?.times(AlgorithmDataConverter.PLOTTING_SCALE) ?: 0.0
        )
    }

    fun carDetailDTOToFerry(carDetailDTO: CarDetailDTO): Ferry {
        // 车所绑定路径的开始坐标
        val startCoordinate = carDetailDTO.carPaths.firstOrNull()?.path?.startCoordinate?.let { mapCoordinate(it) }
            ?: Coordinate(0.0, 0.0)
        // 车所绑定路径的结束坐标
        val endCoordinate = carDetailDTO.carPaths.firstOrNull()?.path?.endCoordinate?.let { mapCoordinate(it) }
            ?: Coordinate(0.0, 0.0)
        // TODO: 待优化，避免使用固定ID做判断
        val currentCoordinate = when (carDetailDTO.id) {
            // id等于1820792585089773569，合箱浇筑混合轨道母车(CONSOLIDATION_POURING_FERRY), 调整一下位置，避免与合箱浇筑开箱混合
            // 轨道母车(COOLING_UNBOXING_FERRY)重合
            1820792585089773569 -> {
                Coordinate(endCoordinate.x, endCoordinate.y + 2000 * AlgorithmDataConverter.PLOTTING_SCALE)
            }
            else -> {
                Coordinate((startCoordinate.x + endCoordinate.x) / 2, (startCoordinate.y + endCoordinate.y) / 2)
            }
        }

        // 车辆参数Map
        val parametersMap = carDetailDTO.model?.parameters?.associate { it.name to it.value }?: emptyMap()

        return Ferry(
            id = carDetailDTO.id,
            meta = CarMeta(
                path = carDetailDTO.carPaths.map(::mapPath),
                type = carDetailDTO.type,
                nameEnum = carDetailDTO.nameEnum,
                name = carDetailDTO.name,
                emptySpeed = parametersMap["emptySpeed"]?: CarMeta(carDetailDTO.type).emptySpeed,
                fullSpeed = parametersMap["fullSpeed"]?: CarMeta(carDetailDTO.type).fullSpeed,
                // TODO: 车辆Meta的其他信息
            ),
            realTime = FerryRealTime(
                // 将车辆随机放置在所绑定的路径上的任意点位
/*                currentCoordinate = Coordinate(
                    if (startCoordinate.x == endCoordinate.x) startCoordinate.x else Random.nextDouble(
                        min(startCoordinate.x, endCoordinate.x), max(startCoordinate.x, endCoordinate.x)),
                    if (startCoordinate.y == endCoordinate.y) startCoordinate.y else Random.nextDouble(
                        min(startCoordinate.y, endCoordinate.y), max(startCoordinate.y, endCoordinate.y))
                )*/
                currentCoordinate = currentCoordinate,
                bindCar = carDetailDTO.bindCar?.id,
                subCarStatus = if (carDetailDTO.bindCar != null) FerrySubCarStatus.CARRY_SUB_CAR else
                    FerrySubCarStatus.NOT_CARRY_SUB_CAR,
                operationStatus = CarOperationStatusEnum.STANDBY
            )
        )
    }

    fun carDetailDTOToSubCar(carDetailDTO: CarDetailDTO): SubCar {
        // 车所绑定路径的开始坐标
        val startCoordinate = carDetailDTO.carPaths.firstOrNull()?.path?.startCoordinate?.let { mapCoordinate(it) }
            ?: Coordinate(0.0, 0.0)
        // 车所绑定路径的结束坐标
        val endCoordinate = carDetailDTO.carPaths.firstOrNull()?.path?.endCoordinate?.let { mapCoordinate(it) }
            ?: Coordinate(0.0, 0.0)

        // currentCoordinate车辆初始位置  TODO: 待优化，避免使用固定ID做判断
        val currentCoordinate = when (carDetailDTO.id) {
            // id等于1817886619438518273：如果车辆是“【工厂】热砂喷涂上轨道子车-2”，调整一下初始位置，避免与“【工厂】热砂喷涂上轨道子车”重合
            1817886619438518273 -> {
                Coordinate(startCoordinate.x - 650 * AlgorithmDataConverter.PLOTTING_SCALE, startCoordinate.y)
            }
            // ID等于1820792323386175490，合箱浇筑混合轨道子车(CONSOLIDATION_POURING_SUB_CAR)，调整一下位置，避免与合箱浇筑开箱混合
            // 轨道子车(COOLING_UNBOXING_SUB_CAR) 重合
            1820792323386175490 -> {
                Coordinate(endCoordinate.x, endCoordinate.y + 2000 * AlgorithmDataConverter.PLOTTING_SCALE)
            }
            else -> {
                Coordinate((startCoordinate.x + endCoordinate.x) / 2, (startCoordinate.y + endCoordinate.y) / 2)
            }
        }

        // 车辆参数Map
        val parametersMap = carDetailDTO.model?.parameters?.associate { it.name to it.value }?: emptyMap()

        return SubCar(
            id = carDetailDTO.id,
            meta = CarMeta(
                path = carDetailDTO.carPaths.map(::mapPath),
                type = carDetailDTO.type,
                nameEnum = carDetailDTO.nameEnum,
                name = carDetailDTO.name,
                emptySpeed = parametersMap["emptySpeed"]?: CarMeta(carDetailDTO.type).emptySpeed,
                fullSpeed = parametersMap["fullSpeed"]?: CarMeta(carDetailDTO.type).fullSpeed,
                raiseTopRodTime = parametersMap["topRodRaiseOrFallTime"]?: CarMeta(carDetailDTO.type).raiseTopRodTime,
                fallTopRodTime = parametersMap["topRodRaiseOrFallTime"]?: CarMeta(carDetailDTO.type).fallTopRodTime,
                // TODO: 车辆Meta的其他信息
            ),
            realTime = SubCarRealTime(
                // 将车辆随机放置在所绑定的路径上的任意点位
/*                currentCoordinate = Coordinate(
                    if (startCoordinate.x == endCoordinate.x) startCoordinate.x else Random.nextDouble(
                        min(startCoordinate.x, endCoordinate.x), max(startCoordinate.x, endCoordinate.x)),
                    if (startCoordinate.y == endCoordinate.y) startCoordinate.y else Random.nextDouble(
                        min(startCoordinate.y, endCoordinate.y), max(startCoordinate.y, endCoordinate.y))
                )*/
                currentCoordinate = currentCoordinate,
                bindFerry = carDetailDTO.bindCar?.id,
                ferryStatus = if (carDetailDTO.bindCar != null) SubCarFerryStatus.SUB_CAR_IN_FERRY else
                    SubCarFerryStatus.SUB_CAR_NOT_IN_FERRY,
                operationStatus = CarOperationStatusEnum.STANDBY,
                loadStatus = SubCarLoadStatus.EMPTY_LOAD,
                topRodStatus = SubCarTopRodStatus.TOP_ROD_FALL_DOWN
            )
        )
    }

    fun equipmentVOToEquipment(equipmentVO: EquipmentVO): Equipment {
        // 设备参数Map
        val parametersMap = equipmentVO.parameters?.associate { it.name to it.value }?: emptyMap()
        return Equipment(
            id = equipmentVO.id,
            meta = EquipmentMeta(
                nameEnum = equipmentVO.nameEnum,
                name = equipmentVO.name,
                config = EquipmentConfig(
                    processTime = parametersMap[EquipmentParameterNameEnum.PROCESS_TIME]?: EquipmentConfig().processTime,
                    electricFurnaceFiringTime = parametersMap[EquipmentParameterNameEnum.ELECTRIC_FURNACE_FIRING_TIME]
                        ?: EquipmentConfig().electricFurnaceFiringTime
                )
            ),
            realTime = EquipmentRealTime(
                operationStatus = equipmentVO.operationStatus?: EquipmentOperationStatusEnum.STANDBY,
            )
        )
    }

    fun pathDTOToPath(pathDTO: PathDTO): Path {
        return Path(
            id = pathDTO.id,
            name = pathDTO.name,
            status = pathDTO.status?: PathStatusEnum.PATH_UNOCCUPIED,
        )
    }

    fun positionDTOToPosition(positionDTO: PositionDTO): PositionRealTime {
        return PositionRealTime(
            id = positionDTO.id,
            status = PositionStatusEnum.UNOCCUPIED,
            nameEnum = positionDTO.nameEnum
        )
    }

    fun workstationVOToWorkstation(workstationVO: WorkstationVO): WorkstationRealTime {
        return WorkstationRealTime(
            id = workstationVO.id,
            nameEnum = workstationVO.nameEnum,
            name = workstationVO.name,
            status = workstationVO.status?: WorkstationStatusEnum.FREE
        )
    }
}