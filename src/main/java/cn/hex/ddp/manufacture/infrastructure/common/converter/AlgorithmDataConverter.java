package cn.hex.ddp.manufacture.infrastructure.common.converter;

import cn.hex.ddp.manufacture.api.equipment.rest.vo.EquipmentVO;
import cn.hex.ddp.manufacture.api.workstation.rest.vo.WorkstationVO;
import cn.hex.ddp.manufacture.application.car.dto.out.CarDetailDTO;
import cn.hex.ddp.manufacture.application.car.dto.out.CarPathDetailDTO;
import cn.hex.ddp.manufacture.application.car.dto.out.CarSummaryDTO;
import cn.hex.ddp.manufacture.application.configuration.dto.out.CoordinateDTO;
import cn.hex.ddp.manufacture.application.configuration.dto.out.PositionDTO;
import cn.hex.ddp.manufacture.application.order.dto.out.OrderDTO;
import cn.hex.ddp.manufacture.application.order.dto.out.OrderItemDTO;
import cn.hex.ddp.manufacture.application.path.dto.out.PathDTO;
import cn.hex.ddp.manufacture.application.task.dto.out.TaskItemDTO;
import cn.hex.ddp.manufacture.application.technique.dto.out.TechniqueDTO;
import cn.hex.ddp.manufacture.domain.car.enums.CarTypeEnum;
import cn.hex.ddp.manufacture.domain.car.model.CarModelParameter;
import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.mold.model.Mold;
import cn.hex.ddp.manufacture.domain.mold.model.MoldGroup;
import cn.hex.ddp.manufacture.domain.order.model.Order;
import cn.hex.ddp.manufacture.domain.sandbox.enums.SandboxTypeEnum;
import cn.hex.ddp.manufacture.domain.sandbox.model.Sandbox;
import cn.hex.ddp.manufacture.domain.sandbox.model.SandboxGroup;
import cn.hex.ddp.manufacture.domain.sandbox.model.SandboxGroupSandbox;
import cn.hex.ddp.manufacture.domain.technique.enums.SandLineTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.OrderInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.OrderProduct;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.FerryStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.OrderProductTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.SubCarStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.*;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 算法数据转换器
 * 后端调用算法时需要按规定格式给入参，算法跟后端对数据模型的定义不完全一致，因此写一个转换器专门用来转换
 *
 * @author Huhaisen
 * @date 2024/06/22
 */
public class AlgorithmDataConverter {

    /**
     * 比例尺，用于坐标转换（前端坐标系跟工厂实际尺寸的比例，工厂实际长度单位为米）
     */
    public final static double PLOTTING_SCALE = (double) 1 /10;

    /**
     * 后端车辆列表转换为算法子车列表
     *
     * @param cars 车辆列表
     * @return {@link List }<{@link SubCar }>
     */
    public static List<SubCar> carDetailDTOsToSubCars(List<CarDetailDTO> cars) {
        if (CollectionUtils.isEmpty(cars)) {
            return Collections.emptyList();
        }
        ArrayList<SubCar> subCars = new ArrayList<>(cars.size());
        for (CarDetailDTO car : cars) {
            // 只有子车才需要转换
            if (car.getType() == CarTypeEnum.SUB_CAR) {
                // 车辆型号参数转换为Map，后续获得默认参数
                Map<String, Double> parametersMap = carModelParameterToMap(car.getModel().getParameters());
                // 初始化子车状态信息
                List<SubCarStatusEnum> subCarStatus;
                if (car.getBindCar() != null) {
                    subCarStatus = List.of(SubCarStatusEnum.EMPTY_LOAD,
                            SubCarStatusEnum.TOP_ROD_FALL_DOWN, SubCarStatusEnum.SUB_CAR_IN_FERRY);
                } else {
                    subCarStatus = List.of(SubCarStatusEnum.EMPTY_LOAD,
                            SubCarStatusEnum.TOP_ROD_FALL_DOWN, SubCarStatusEnum.SUB_CAR_NOT_IN_FERRY);
                }

                // 转换绑定的摆渡车
                CarSummaryDTO bindCar = car.getBindCar();
                Ferry bindFerry = null;
                if (bindCar != null) {
                    bindFerry = new Ferry(
                            bindCar.getId(),
                            bindCar.getName(),
                            bindCar.getNameEnum(),
                            0,
                            0,
                            List.of(FerryStatusEnum.CARRY_SUB_CAR),
                            null,
                            null,
                            null
                    );
                }
                // 转换路径信息
                List<Path> paths = pathDTOsToPaths(car.getCarPaths().stream().map(CarPathDetailDTO::getPath).toList());

                // 车辆初始位置，初始设置为车辆所在道路中点位置。若车辆绑定路径为空，则初始位置设为(0，0)
                Coordinate initCoordinate = new Coordinate(0, 0);
                if (!paths.isEmpty()) {
                    Coordinate startCoordinate = paths.getFirst().getStartCoordinate();
                    Coordinate endCoordinate = paths.getFirst().getEndCoordinate();
                    // TODO: 临时处理，后续需要优化
                    if (car.getId() == 1817886619438518273L) {
                        // id等于1817886619438518273：如果车辆是“【工厂】热砂喷涂上轨道子车-2”，
                        // 调整一下初始位置，避免与“【工厂】热砂喷涂上轨道子车”重合
                        initCoordinate = new Coordinate(startCoordinate.getX() - 650 * PLOTTING_SCALE,
                                (startCoordinate.getY() + endCoordinate.getY()) / 2);
                    }else if (car.getId() == 1820792323386175490L) {
                        // ID等于1820792323386175490，合箱浇筑混合轨道子车(CONSOLIDATION_POURING_SUB_CAR)，
                        // 调整一下位置，避免与合箱浇筑开箱混合轨道子车(COOLING_UNBOXING_SUB_CAR) 重合
                        initCoordinate = new Coordinate(endCoordinate.getX(), endCoordinate.getY() + 2000 * PLOTTING_SCALE);
                    }
                    else {
                        initCoordinate = new Coordinate((startCoordinate.getX() + endCoordinate.getX()) / 2,
                                (startCoordinate.getY() + endCoordinate.getY()) / 2);
                    }
                }

                SubCar subCar = new SubCar(
                        car.getId(),
                        car.getName(),
                        car.getNameEnum(),
                        parametersMap.get("emptySpeed"),
                        parametersMap.get("fullSpeed"),
                        parametersMap.get("topRodRaiseOrFallTime"),
                        subCarStatus,
                        paths.isEmpty() ? null : paths.getFirst(),
                        bindFerry == null ? null : bindFerry.getId(),
                        initCoordinate,
                        null,
                        null
                );
                subCars.add(subCar);
            }
        }
        return subCars;
    }

    /**
     * 后端车辆列表转换为算法摆渡车列表
     *
     * @param cars 车辆列表
     * @return {@link List }<{@link Ferry }>
     */
    public static List<Ferry> carDetailDTOsToFerries(List<CarDetailDTO> cars) {
        if (CollectionUtils.isEmpty(cars)) {
            return Collections.emptyList();
        }
        ArrayList<Ferry> ferries = new ArrayList<>(cars.size());
        for (CarDetailDTO car : cars) {
            // 只有摆渡车才需要转换
            if (car.getType() == CarTypeEnum.FERRY_CAR) {
                // 车辆型号参数转换为Map，后续获得默认参数
                Map<String, Double> parametersMap = carModelParameterToMap(car.getModel().getParameters());
                // 初始化母车状态信息
                List<FerryStatusEnum> ferryStatus = List.of(FerryStatusEnum.CARRY_SUB_CAR);
                if (car.getBindCar() != null) {
                    ferryStatus = List.of(FerryStatusEnum.CARRY_SUB_CAR);
                } else {
                    ferryStatus = List.of(FerryStatusEnum.NOT_CARRY_SUB_CAR);
                }

                // 转换绑定的子车
                CarSummaryDTO bindCar = car.getBindCar();
                SubCar bindSubCar = null;
                if (bindCar != null) {
                    bindSubCar = new SubCar(
                            bindCar.getId(),
                            bindCar.getName(),
                            bindCar.getNameEnum(),
                            0,
                            0,
                            0,
                            List.of(SubCarStatusEnum.EMPTY_LOAD,
                                    SubCarStatusEnum.TOP_ROD_FALL_DOWN, SubCarStatusEnum.SUB_CAR_IN_FERRY),
                            null,
                            null,
                            null,
                            null,
                            null
                    );
                }
                // 转换路径信息
                List<Path> paths = pathDTOsToPaths(car.getCarPaths().stream().map(CarPathDetailDTO::getPath).toList());

                // 车辆初始位置，初始设置为车辆所在道路中点位置。若车辆绑定路径为空，则初始位置设为(0，0)
                Coordinate initCoordinate = new Coordinate(0, 0);
                if (!paths.isEmpty()) {
                    Coordinate startCoordinate = paths.getFirst().getStartCoordinate();
                    Coordinate endCoordinate = paths.getFirst().getEndCoordinate();
                    if (car.getId() == 1820792585089773569L) {
                        initCoordinate = new Coordinate(endCoordinate.getX(), endCoordinate.getY() + 2000 * PLOTTING_SCALE);
                    } else {
                        initCoordinate = new Coordinate((startCoordinate.getX() + endCoordinate.getX()) / 2,
                                (startCoordinate.getY() + endCoordinate.getY()) / 2);
                    }
                }

                Ferry ferry = new Ferry(
                        car.getId(),
                        car.getName(),
                        car.getNameEnum(),
                        parametersMap.get("emptySpeed"),
                        parametersMap.get("fullSpeed"),
                        ferryStatus,
                        paths.isEmpty() ? null : paths.getFirst(),
                        bindSubCar == null ? null : bindSubCar.getId(),
                        initCoordinate
                );
                ferries.add(ferry);
            }
        }
        return ferries;
    }

    /**
     * 后端设备列表转换为算法设备列表
     *
     * @param equipmentVOs 设备列表
     * @return {@link List }<{@link Equipment }>
     */
    public static List<Equipment> equipmentVOToEquipments(List<EquipmentVO> equipmentVOs) {
        if (CollectionUtils.isEmpty(equipmentVOs)) {
            return Collections.emptyList();
        }
        return equipmentVOs.stream().map(equipmentVO -> new Equipment(
                equipmentVO.getId(),
                equipmentVO.getName(),
                toCoordinate(equipmentVO.getCoordinate()),
                equipmentVO.getOperationStatus(),
                List.of(),
                0,
                0,
                equipmentVO.getNameEnum()
        )).toList();
    }

    /**
     * 后端点位列表转换为算法点位列表
     *
     * @param positionDTOs 点位列表
     * @return {@link List }<{@link Position }>
     */
    public static List<Position> positionDTOsToPositions(List<PositionDTO> positionDTOs) {
        if (CollectionUtils.isEmpty(positionDTOs)) {
            return Collections.emptyList();
        }
        return positionDTOs.stream().map(positionDTO -> new Position(
                positionDTO.getId(),
                positionDTO.getName(),
                positionDTO.getNameEnum(),
                toCoordinate(positionDTO.getCoordinate()),
                PositionStatusEnum.UNOCCUPIED,
                null,
                null
        )).toList();
    }

    /**
     * 后端工岗列表转换为算法工岗列表
     *
     * @param workstationVOs 工岗列表
     * @return {@link List }<{@link WorkStation }>
     */
    public static List<WorkStation> workstationVOsToWorkStations(List<WorkstationVO> workstationVOs) {
        if (CollectionUtils.isEmpty(workstationVOs)) {
            return Collections.emptyList();
        }
        return workstationVOs.stream().map(workstationVO -> new WorkStation(
                workstationVO.getId(),
                workstationVO.getName(),
                workstationVO.getNameEnum(),
                toCoordinate(workstationVO.getCoordinate()),
                workstationVO.getStatus(),
                List.of(),
                null
        )).toList();
    }

    /**
     * 后端路径列表转换为算法路径列表
     *
     * @param pathDTOs 路径列表
     * @return {@link List }<{@link Path }>
     */
    public static List<Path> pathDTOsToPaths(List<PathDTO> pathDTOs) {
        if (CollectionUtils.isEmpty(pathDTOs)) {
            return Collections.emptyList();
        }

        return pathDTOs.stream().map(pathDTO -> new Path(
                pathDTO.getId(),
                pathDTO.getName(),
                pathDTO.getNameEnum(),
                toCoordinate(pathDTO.getStartCoordinate()),
                toCoordinate(pathDTO.getEndCoordinate()),
                pathDTO.getNodeCoordinates().stream().map(AlgorithmDataConverter::toCoordinate).toList(),
                pathDTO.getStatus()
        )).toList();
    }

    /**
     * 后端订单详情转换为算法订单输入
     *
     * @param orderDTO 订单详情
     * @return {@link OrderInput }
     */
    public static OrderInput orderDTOToOrderInput(OrderDTO orderDTO) {
        if (orderDTO == null) {
            return null;
        }
        ArrayList<OrderProduct> orderProducts = new ArrayList<>();
        // 循环将每个产品项转换为算法中的OrderProduct
        for (OrderItemDTO orderItem : orderDTO.getOrderItems()) {
            // 获得工艺信息
            TechniqueDTO techniqueDTO = orderItem.getProduct().getTechnique();
            // 获得砂箱组信息
            SandboxGroup sandboxGroup = techniqueDTO.getSandboxGroup();
            // 获得砂箱模具组信息
            MoldGroup sandboxMoldGroup = techniqueDTO.getSandboxMoldGroup();
            // 获得砂芯模具组信息
            MoldGroup sandCoreMoldGroup = techniqueDTO.getSandCoreMoldGroup();

            // 构造算法中的订单产品的构成列表，由上砂箱，下砂箱，模具，砂芯等组成，且需要满足一定的顺序
            ArrayList<Product> products = new ArrayList<>();
            // 转换砂箱信息
            List<Product> sandboxProducts = sandboxGroup.getSandboxGroupSandboxList().stream()
                    .map(item -> sandboxToProduct(item, sandboxGroup, techniqueDTO)).toList();
            products.addAll(sandboxProducts);
            // 转换砂箱模具组信息
            List<Product> sandboxMoldProducts = sandboxMoldGroup.getMoldGroupMoldList().stream().map(item -> {
                Mold mold = item.getMold();
                return new Product(mold.getId(), mold.getName(), item.getLayer(), ProductTypeEnum.MOULD,
                        null, null, techniqueDTO.getSandboxMoldNeedHeat() ?
                        OrderProductTypeEnum.HOT_SAND : OrderProductTypeEnum.COLD_SAND, false, 0,
                        techniqueDTO.getOutBoundTime(), techniqueDTO.getMoldClosingTime(),
                        techniqueDTO.getSandblastingTime(), techniqueDTO.getMoldOpeningTime(),
                        techniqueDTO.getSprayTime(), techniqueDTO.getClosingBoxTime(), techniqueDTO.getPouringTime(),
                        techniqueDTO.getCoolingTime(), techniqueDTO.getUnboxingTime(), List.of()
                );
            }).toList();
            products.addAll(sandboxMoldProducts);
            // 转换砂芯模具组信息
            List<Product> sandCoreMoldProducts = sandCoreMoldGroup.getMoldGroupMoldList().stream().map(item -> {
                Mold mold = item.getMold();
                return new Product(
                        mold.getId(), mold.getName(), item.getLayer(), ProductTypeEnum.SAND_MOULD, null,
                        null, OrderProductTypeEnum.CORE_SAND, false, 0,
                        techniqueDTO.getOutBoundTime(), techniqueDTO.getMoldClosingTime(),
                        techniqueDTO.getSandblastingTime(), techniqueDTO.getMoldOpeningTime(),
                        techniqueDTO.getSprayTime(), techniqueDTO.getClosingBoxTime(), techniqueDTO.getPouringTime(),
                        techniqueDTO.getCoolingTime(), techniqueDTO.getUnboxingTime(), List.of()
                );
            }).toList();
            products.addAll(sandCoreMoldProducts);

            // todo 这里的产品id是产品型号id嘛
            OrderProduct orderProduct = new OrderProduct(
                    products,
                    orderItem.getCount(),
                    orderItem.getProduct().getMaterialNo(),
                    techniqueDTO.getSandLineType() == SandLineTypeEnum.COLD_LINE ? OrderProductTypeEnum.COLD_SAND
                            : OrderProductTypeEnum.HOT_SAND,
                    techniqueDTO.getMoltenIronPouringQuantity(),
                    techniqueDTO.getMoltenIronFormula().getMaterialTime(),
                    0,
                    0,
                    List.of(),
                    orderItem.getProduct().getId()
            );
            orderProducts.add(orderProduct);
        }

        return new OrderInput(
                orderDTO.getId(),
                orderProducts,
                //Duration.between(orderDTO.getExpectedProductionTime(), orderDTO.getDeliveryDeadline()).getSeconds(),
                Duration.between(LocalDateTime.now(), orderDTO.getDeliveryDeadline()).getSeconds(),
                orderDTO.getPriority()
        );
    }

    /**
     * 根据订单和仿真任务列表，转换成算法订单输入
     *
     * @param order 订单
     * @param taskItems 任务项列表
     * @return {@link OrderInput }
     */
    public static OrderInput toOrderInput(Order order, List<TaskItemDTO> taskItems) {
        ArrayList<OrderProduct> orderProducts = new ArrayList<>();
        for (TaskItemDTO taskItem : taskItems) {
            // 获得工艺信息
            TechniqueDTO techniqueDTO = taskItem.getProduct().getTechnique();
            // 获得砂箱组信息
            SandboxGroup sandboxGroup = techniqueDTO.getSandboxGroup();
            // 获得砂箱模具组信息
            MoldGroup sandboxMoldGroup = techniqueDTO.getSandboxMoldGroup();
            // 获得砂芯模具组信息
            MoldGroup sandCoreMoldGroup = techniqueDTO.getSandCoreMoldGroup();

            // 构造算法中的订单产品的构成列表，由上砂箱，下砂箱，模具，砂芯等组成，且需要满足一定的顺序
            ArrayList<Product> products = new ArrayList<>();
            // 转换砂箱信息
            List<Product> sandboxProducts = sandboxGroup.getSandboxGroupSandboxList().stream()
                    .map(item -> sandboxToProduct(item, sandboxGroup, techniqueDTO)).toList();
            products.addAll(sandboxProducts);
            // 转换砂箱模具组信息
            List<Product> sandboxMoldProducts = sandboxMoldGroup.getMoldGroupMoldList().stream().map(item -> {
                Mold mold = item.getMold();
                return new Product(mold.getId(), mold.getName(), item.getLayer(), ProductTypeEnum.MOULD, null,
                        null, techniqueDTO.getSandboxMoldNeedHeat() ? OrderProductTypeEnum.HOT_SAND
                        : OrderProductTypeEnum.COLD_SAND, false, 0, techniqueDTO.getOutBoundTime(),
                        techniqueDTO.getMoldClosingTime(), techniqueDTO.getSandblastingTime(),
                        techniqueDTO.getMoldOpeningTime(), techniqueDTO.getSprayTime(),
                        techniqueDTO.getClosingBoxTime(), techniqueDTO.getPouringTime(),
                        techniqueDTO.getCoolingTime(), techniqueDTO.getUnboxingTime(), List.of()
                );
            }).toList();
            products.addAll(sandboxMoldProducts);
            // 转换砂芯模具组信息
            List<Product> sandCoreMoldProducts = sandCoreMoldGroup.getMoldGroupMoldList().stream().map(item -> {
                Mold mold = item.getMold();
                return new Product(
                        mold.getId(), mold.getName(), item.getLayer(), ProductTypeEnum.SAND_MOULD, null,
                        null, OrderProductTypeEnum.CORE_SAND, false, 0,
                        techniqueDTO.getOutBoundTime(), techniqueDTO.getMoldClosingTime(),
                        techniqueDTO.getSandblastingTime(), techniqueDTO.getMoldOpeningTime(),
                        techniqueDTO.getSprayTime(), techniqueDTO.getClosingBoxTime(), techniqueDTO.getPouringTime(),
                        techniqueDTO.getCoolingTime(), techniqueDTO.getUnboxingTime(), List.of()
                );
            }).toList();
            products.addAll(sandCoreMoldProducts);

            // todo 这里的产品id是产品型号id嘛
            OrderProduct orderProduct = new OrderProduct(
                    products,
                    taskItem.getCount(),
                    taskItem.getProduct().getMaterialNo(),
                    techniqueDTO.getSandLineType() == SandLineTypeEnum.COLD_LINE ? OrderProductTypeEnum.COLD_SAND
                            : OrderProductTypeEnum.HOT_SAND,
                    techniqueDTO.getMoltenIronPouringQuantity(),
                    techniqueDTO.getMoltenIronFormula().getMaterialTime(),
                    0,
                    0,
                    List.of(),
                    taskItem.getProduct().getId()
            );
            orderProducts.add(orderProduct);
        }

        return new OrderInput(
                order.getId(),
                orderProducts,
                Duration.between(order.getExpectedProductionTime(), order.getDeliveryDeadline()).getSeconds(),
                order.getPriority()
        );
    }

    /**
     * 砂箱组砂箱转换为算法Product
     */
    private static @NotNull Product sandboxToProduct(SandboxGroupSandbox item, SandboxGroup sandboxGroup,
                                                     TechniqueDTO techniqueDTO) {
        Sandbox sandbox = item.getSandbox();
        // 算法中砂箱类型。这里依据后端砂箱所处的层级来判断
        ProductTypeEnum productType = null;
        // 1. 如果该砂箱组中砂箱数量小于2，则1为上砂箱，2为下砂箱
        if (sandboxGroup.getSandboxGroupSandboxList().size() <= 2) {
            if (item.getLayer() == 1) {
                productType = ProductTypeEnum.UPPER_BOX;
            } else if (item.getLayer() == 2) {
                productType = ProductTypeEnum.LOWER_BOX;
            }
        } else { // 2. 如果该砂箱组中砂箱数量大于2，则1为上砂箱，2为中砂箱，3及3以后为下砂箱
            if (item.getLayer() == 1) {
                productType = ProductTypeEnum.UPPER_BOX;
            } else if (item.getLayer() == 2) {
                productType = ProductTypeEnum.MIDDLE_BOX;
            } else {
                productType = ProductTypeEnum.LOWER_BOX;
            }
        }

        return new Product(
                sandbox.getId(),
                sandbox.getName(),
                item.getLayer(),
                productType,
                null,
                null,
                sandbox.getSandboxModel().getType() == SandboxTypeEnum.COLD_SAND ?
                        OrderProductTypeEnum.COLD_SAND : OrderProductTypeEnum.HOT_SAND,
                false,
                0,
                techniqueDTO.getOutBoundTime(),
                techniqueDTO.getMoldClosingTime(),
                techniqueDTO.getSandblastingTime(),
                techniqueDTO.getMoldOpeningTime(),
                techniqueDTO.getSprayTime(),
                techniqueDTO.getClosingBoxTime(),
                techniqueDTO.getPouringTime(),
                techniqueDTO.getCoolingTime(),
                techniqueDTO.getUnboxingTime(),
                List.of()
        );
    }


    /**
     * 车辆型号参数列表转换为Map
     *
     * @param parameters 车辆型号参数列表
     * @return {@link Map }<{@link String }, {@link Double }>
     */
    private static Map<String, Double> carModelParameterToMap(List<CarModelParameter> parameters) {
        if (CollectionUtils.isEmpty(parameters)) {
            return Collections.emptyMap();
        }
        return parameters.stream().collect(Collectors.toMap(
                CarModelParameter::getName, CarModelParameter::getValue, (k1, k2) -> k1));
    }

    /**
     * 后端坐标转换为算法坐标
     *
     * @param coordinate 坐标
     * @return {@link Coordinate }
     */
    public static Coordinate toCoordinate(cn.hex.ddp.manufacture.domain.configuration.model.Coordinate coordinate) {
        if (coordinate == null) {
            return new Coordinate(0, 0);
        }
        // 比例尺转换
        return new Coordinate(coordinate.getX() * PLOTTING_SCALE, coordinate.getY() * PLOTTING_SCALE);
    }

    private static Coordinate toCoordinate(CoordinateDTO coordinateDTO) {
        if (coordinateDTO == null) {
            return new Coordinate(0, 0);
        }
        // 比例尺转换
        return new Coordinate(coordinateDTO.getX() * PLOTTING_SCALE, coordinateDTO.getY() * PLOTTING_SCALE);
    }

}
