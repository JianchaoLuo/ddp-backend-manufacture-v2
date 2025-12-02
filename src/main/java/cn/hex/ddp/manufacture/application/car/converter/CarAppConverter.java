package cn.hex.ddp.manufacture.application.car.converter;

import cn.hex.ddp.manufacture.api.car.rest.req.CreateCarModelReq;
import cn.hex.ddp.manufacture.api.car.rest.req.CreateCarReq;
import cn.hex.ddp.manufacture.api.car.rest.req.UpdateCarModelReq;
import cn.hex.ddp.manufacture.api.path.rest.vo.PathSummaryVO;
import cn.hex.ddp.manufacture.application.car.dto.out.*;
import cn.hex.ddp.manufacture.application.path.dto.out.PathDTO;
import cn.hex.ddp.manufacture.domain.car.model.Car;
import cn.hex.ddp.manufacture.domain.car.model.CarModel;
import cn.hex.ddp.manufacture.domain.car.model.CarPath;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 车辆应用转换器接口
 *
 * @author Huhaisen
 * @date 2024/05/16
 */
@Mapper(componentModel = "spring")
public interface CarAppConverter {
    @Mapping(target = "parameters", ignore = true)
    CarModel toCarModel(CreateCarModelReq createCarModelReq);

    /**
     * 为了调整carModel.setParameters位置，放到最后。
     * 先carModel.setType才能保证参数为空的情况下成功设置默认参数。
     */
    @AfterMapping
    default void setParameters(CreateCarModelReq createCarModelReq, @MappingTarget CarModel carModel) {
        carModel.setParameters(createCarModelReq.getParameters());
    }

    CarModel toCarModel(Long id, UpdateCarModelReq updateCarModelReq);

    Car toCar(CreateCarReq createCarReq);

    CarDTO toCarDTO(Car car);

    CarSummaryDTO toCarSummaryDTO(Car car);

    CarPathDTO toCarPathDTO(CarPath carPath, PathSummaryVO path);

    default List<CarDTO> toCarDTOList(List<Car> list, Map<Long, PathSummaryVO> pathSummaryVOMap) {
        ArrayList<CarDTO> carDTOS = new ArrayList<>(list.size());
        for (Car car : list) {
            CarDTO carDTO = toCarDTO(car);
            carDTO.setCarPaths(car.getCarPaths().stream().map(carPath -> toCarPathDTO(carPath,
                    pathSummaryVOMap.getOrDefault(carPath.getPathId(), null))).toList());
            carDTOS.add(carDTO);
        }
        return carDTOS;
    }

    default List<CarDetailDTO> toCarDetailDTOList(List<Car> cars, Map<Long, PathDTO> pathDTOMap) {
        ArrayList<CarDetailDTO> carDetailDTOS = new ArrayList<>(cars.size());
        for (Car car : cars) {
            CarDetailDTO carDetailDTO = toCarDetailDTO(car);
            carDetailDTO.setCarPaths(car.getCarPaths().stream().map(carPath -> toCarPathDetailDTO(carPath,
                    pathDTOMap.get(carPath.getPathId()))).toList());
            carDetailDTOS.add(carDetailDTO);
        }
        return carDetailDTOS;
    }

    CarPathDetailDTO toCarPathDetailDTO(CarPath carPath, PathDTO path);

    CarDetailDTO toCarDetailDTO(Car car);

    List<CarSummaryDTO> toCarSummaryDTOList(List<Car> cars);
}
