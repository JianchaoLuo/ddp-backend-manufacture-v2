package cn.hex.ddp.manufacture.infrastructure.car.managerimpl.converter;

import cn.hex.ddp.manufacture.api.car.rest.req.CreateCarReq;
import cn.hex.ddp.manufacture.api.car.rest.req.UpdateCarReq;
import cn.hex.ddp.manufacture.domain.car.model.Car;
import cn.hex.ddp.manufacture.domain.car.model.CarModel;
import cn.hex.ddp.manufacture.domain.car.model.CarPath;
import cn.hex.ddp.manufacture.infrastructure.car.persistence.po.CarModelPO;
import cn.hex.ddp.manufacture.infrastructure.car.persistence.po.CarPO;
import cn.hex.ddp.manufacture.infrastructure.car.persistence.po.CarPathPO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * 车辆转换器
 *
 * @author Huhaisen
 * @date 2024/05/16
 */
@Mapper(componentModel = "spring")
public interface CarInfraConverter {
    CarModelPO carModelToPO(CarModel carModel);

    List<CarModel> toCarModelList(List<CarModelPO> carModelPOList);

    List<CarPath> carPathPOListToCarPathList(List<CarPathPO> list);

    @Mapping(target = "parameters", ignore = true)
    CarModel toCarModel(CarModelPO carModelPO);

    /**
     * 为了调整carModel.setParameters位置，放到最后。
     * 先carModel.setType才能保证参数为空的情况下成功设置默认参数。
     */
    @AfterMapping
    default void setParameters(CarModelPO carModelPO, @MappingTarget CarModel carModel) {
        carModel.setParameters(carModelPO.getParameters());
    }

    @Mapping(target = "carPaths", expression = "java(carPO.getCarPaths()==null?new java.util.ArrayList<>()" +
            ":carPathPOListToCarPathList(carPO.getCarPaths()))")
    Car toCar(CarPO carPO);

    List<Car> toCarList(List<CarPO> carPOS);

    CarPO toCarPO(Long id, UpdateCarReq updateCarReq);

    CarPO toCarPO(CreateCarReq createCarReq);

    CarPathPO toCarPathPO(Long carId, Long pathId);
}
