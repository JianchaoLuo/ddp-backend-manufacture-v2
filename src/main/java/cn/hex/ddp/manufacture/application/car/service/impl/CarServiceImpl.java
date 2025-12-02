package cn.hex.ddp.manufacture.application.car.service.impl;

import cn.hex.ddp.manufacture.api.car.rest.req.*;
import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.path.rest.vo.PathSummaryVO;
import cn.hex.ddp.manufacture.application.car.converter.CarAppConverter;
import cn.hex.ddp.manufacture.application.car.dto.out.CarDTO;
import cn.hex.ddp.manufacture.application.car.dto.out.CarDetailDTO;
import cn.hex.ddp.manufacture.application.car.dto.out.CarSummaryDTO;
import cn.hex.ddp.manufacture.application.car.service.CarService;
import cn.hex.ddp.manufacture.application.path.converter.PathAppConverter;
import cn.hex.ddp.manufacture.application.path.dto.out.PathDTO;
import cn.hex.ddp.manufacture.application.path.service.PathComponentBuilder;
import cn.hex.ddp.manufacture.domain.car.manager.CarManager;
import cn.hex.ddp.manufacture.domain.car.model.Car;
import cn.hex.ddp.manufacture.domain.car.model.CarModel;
import cn.hex.ddp.manufacture.domain.car.model.CarPath;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.domain.common.exception.NotFoundException;
import cn.hex.ddp.manufacture.domain.path.manager.PathManager;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 车辆服务实现类
 *
 * @author Huhaisen
 * @date 2024/05/16
 */
@Slf4j
@Service
public class CarServiceImpl implements CarService {

    @Autowired
    private CarManager carManager;

    @Autowired
    private CarAppConverter carCvt;

    @Autowired
    private PathManager pathManager;

    @Autowired
    private PathAppConverter pathCvt;
    @Autowired
    private PathComponentBuilder pathComponentBuilder;

    @Override
    public void createCarModel(CreateCarModelReq createCarModelReq) {
        Optional.ofNullable(carManager.getCarModelByNo(createCarModelReq.getModelNo())).ifPresent(carModel -> {
            log.warn("[CarServiceImpl.createCarModel] 车辆型号编号已存在，modelNo={}", createCarModelReq.getModelNo());
            throw new BusinessException(BssExType.CAR_MODEL_NO_REPEAT_ERROR, createCarModelReq.getModelNo());
        });
        carManager.createCarModel(carCvt.toCarModel(createCarModelReq));
    }

    @Override
    public PageResult<CarModel> getCarModelPage(GetCarModelPageReq getCarModelPageReq) {
        return carManager.getCarModelPage(getCarModelPageReq);
    }

    @Override
    public void updateCarModel(Long id, UpdateCarModelReq updateCarModelReq) {
        CarModel carModel = carManager.getCarModelById(id);
        if (carModel == null) {
            log.warn("[CarServiceImpl.updateCarModel] 车辆型号不存在，id={}", id);
            throw new NotFoundException("车辆型号不存在");
        }
        CarModel carModelByNo = carManager.getCarModelByNo(updateCarModelReq.getModelNo());
        if (carModelByNo != null && !carModelByNo.getId().equals(id)) {
            log.warn("[CarServiceImpl.updateCarModel] 车辆型号编号已存在，modelNo={}", updateCarModelReq.getModelNo());
            throw new BusinessException(BssExType.CAR_MODEL_NO_REPEAT_ERROR, updateCarModelReq.getModelNo());
        }
        carManager.updateCarModel(carCvt.toCarModel(id, updateCarModelReq));
    }

    @Override
    public void deleteCarModel(Long id) {
        CarModel carModel = carManager.getCarModelById(id);
        if (carModel == null) {
            log.warn("[CarServiceImpl.deleteCarModel] 车辆型号不存在，id={}", id);
            throw new NotFoundException("车辆型号不存在");
        }
        if(CollectionUtils.isNotEmpty(carManager.getCarsByCarModelId(id))){
           log.warn("[CarServiceImpl.deleteCarModel] 车辆型号被占用");
           throw new BusinessException(BssExType.CAR_MODEL_BE_OCCUPIED);
        }

        carManager.deleteCarModel(id);
    }

    @Override
    public void createCar(CreateCarReq createCarReq) {
        CarModel carModel = carManager.getCarModelById(createCarReq.getCarModelId());
        if (carModel == null) {
            log.warn("[CarServiceImpl.createCar] 车辆型号不存在，id={}", createCarReq.getCarModelId());
            throw new NotFoundException("车辆型号不存在");
        }
        Optional.ofNullable(carManager.getCarByNo(createCarReq.getCarNo())).ifPresent(car -> {
            log.warn("[CarServiceImpl.createCar] 车辆编号已存在，carNo={}", createCarReq.getCarNo());
            throw new BusinessException(BssExType.CAR_NO_REPEAT_ERROR, createCarReq.getCarNo());
        });
        if (!carModel.getType().equals(createCarReq.getType())) {
            log.warn("[CarServiceImpl.createCar] 车辆型号类型不一致，车辆类型={}, 车辆型号类型={}", createCarReq.getType(), carModel.getType());
            throw new BusinessException(BssExType.CAR_MODEL_INCONSISTENT);
        }

        carManager.createCar(createCarReq);
    }

    @Override
    public CarDTO getCar(Long id) {
        Car car = carManager.getCarById(id);
        if (car == null) {
            log.warn("[CarServiceImpl.getCar] 车辆不存在，id={}", id);
            throw new NotFoundException("车辆不存在");
        }

        CarDTO carDTO = carCvt.toCarDTO(car);
        // 补充路径信息
        if (CollectionUtils.isEmpty(car.getCarPaths())) {
            carDTO.setCarPaths(List.of());
        } else {
            Map<Long, PathSummaryVO> pathSummaryVOMap = pathManager.getPathSummaryVOMap(
                    car.getCarPaths().stream().map(CarPath::getPathId).toList()
            );
            carDTO.setCarPaths(car.getCarPaths().stream().map(carPath -> carCvt.toCarPathDTO(carPath,
                    pathSummaryVOMap.getOrDefault(carPath.getPathId(), null))).toList());
        }
        return carDTO;
    }

    @Override
    public PageResult<CarDTO> getCarPage(GetCarPageReq getCarPageReq) {
        PageResult<Car> carPage = carManager.getCarPage(getCarPageReq);
        // 补充路径信息
        List<Long> pathIds = carPage.getList().stream().map(Car::getCarPaths).toList()
                .stream().flatMap(List::stream).map(CarPath::getPathId).toList();
        Map<Long, PathSummaryVO> pathSummaryVOMap = pathManager.getPathSummaryVOMap(pathIds);

        return PageResult.of(carPage, carCvt.toCarDTOList(carPage.getList(), pathSummaryVOMap));
    }

    @Override
    public List<CarDetailDTO> getAllCar() {
        List<Car> cars = carManager.getAllCar();
        // 补充路径信息
        List<Long> pathIds = cars.stream().map(Car::getCarPaths).flatMap(List::stream).map(CarPath::getPathId).toList();
        Map<Long, PathDTO> pathDTOMap = pathComponentBuilder.buildPathDTOMap(pathIds);

        return carCvt.toCarDetailDTOList(cars, pathDTOMap);
    }

    @Override
    public List<CarSummaryDTO> getlistByArea(AreaEnum area) {
        List<Car> cars = carManager.getlistByArea(area);
        if (CollectionUtils.isEmpty(cars)) {
            return List.of();
        }
        return carCvt.toCarSummaryDTOList(cars);
    }

    @Override
    public void updateCar(Long id, UpdateCarReq updateCarReq) {
        Car car = carManager.getCarById(id);
        if (car == null) {
            log.warn("[CarServiceImpl.updateCar] 车辆不存在，id={}", id);
            throw new NotFoundException("车辆不存在");
        }

        if (updateCarReq.getCarModelId() != null) {
            CarModel carModel = carManager.getCarModelById(updateCarReq.getCarModelId());
            if (carModel == null) {
                log.warn("[CarServiceImpl.updateCar] 车辆型号不存在，id={}", updateCarReq.getCarModelId());
                throw new NotFoundException("车辆型号不存在");
            }
            if (!car.getType().equals(carModel.getType())) {
                log.warn("[CarServiceImpl.updateCar] 车辆型号类型不一致，车辆类型={}, 车辆型号类型={}", car.getType(), carModel.getType());
                throw new BusinessException(BssExType.CAR_MODEL_INCONSISTENT);
            }
        }

        Car carByNo = carManager.getCarByNo(updateCarReq.getCarNo());
        if (carByNo != null && !carByNo.getId().equals(id)) {
            log.warn("[CarServiceImpl.updateCar] 车辆编号已存在，carNo={}", updateCarReq.getCarNo());
            throw new BusinessException(BssExType.CAR_NO_REPEAT_ERROR, updateCarReq.getCarNo());
        }
        carManager.updateCar(id, updateCarReq);
    }

    @Override
    public void deleteCar(Long id) {
        Car car = carManager.getCarById(id);
        if (car == null) {
            log.warn("[CarServiceImpl.deleteCar] 车辆不存在，id={}", id);
            throw new NotFoundException("车辆不存在");
        }

        carManager.deleteCar(id);
    }
}
