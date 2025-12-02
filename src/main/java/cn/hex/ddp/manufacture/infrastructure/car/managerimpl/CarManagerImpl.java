package cn.hex.ddp.manufacture.infrastructure.car.managerimpl;

import cn.hex.ddp.manufacture.api.car.rest.req.CreateCarReq;
import cn.hex.ddp.manufacture.api.car.rest.req.GetCarModelPageReq;
import cn.hex.ddp.manufacture.api.car.rest.req.GetCarPageReq;
import cn.hex.ddp.manufacture.api.car.rest.req.UpdateCarReq;
import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.domain.car.manager.CarManager;
import cn.hex.ddp.manufacture.domain.car.model.Car;
import cn.hex.ddp.manufacture.domain.car.model.CarModel;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.infrastructure.car.managerimpl.converter.CarInfraConverter;
import cn.hex.ddp.manufacture.infrastructure.car.persistence.po.CarModelPO;
import cn.hex.ddp.manufacture.infrastructure.car.persistence.po.CarPO;
import cn.hex.ddp.manufacture.infrastructure.car.persistence.po.CarPathPO;
import cn.hex.ddp.manufacture.infrastructure.car.persistence.postgresql.repository.CarModelPORepository;
import cn.hex.ddp.manufacture.infrastructure.car.persistence.postgresql.repository.CarPORepository;
import cn.hex.ddp.manufacture.infrastructure.car.persistence.postgresql.repository.CarPathPORepository;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 车辆管理实现类
 *
 * @author Huhaisen
 * @date 2024/05/16
 */
@Slf4j
@Repository
public class CarManagerImpl implements CarManager {

    @Autowired
    private CarModelPORepository carModelRepository;

    @Autowired
    private CarPORepository carRepository;

    @Autowired
    private CarPathPORepository carPathRepository;

    @Autowired
    private CarInfraConverter carCvt;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCarModel(CarModel carModel) {
        carModelRepository.save(carCvt.carModelToPO(carModel));
    }

    @Override
    public PageResult<CarModel> getCarModelPage(GetCarModelPageReq getCarModelPageReq) {
        Page<CarModelPO> page = carModelRepository.page(
                new Page<>(getCarModelPageReq.getCurrent(), getCarModelPageReq.getPageSize()),
                Wrappers.lambdaQuery(CarModelPO.class)
                        .eq(ObjectUtils.isNotNull(getCarModelPageReq.getType()), CarModelPO::getType,
                                getCarModelPageReq.getType())
                        .like(StringUtils.isNotEmpty(getCarModelPageReq.getModelNo()), CarModelPO::getModelNo,
                                getCarModelPageReq.getModelNo())
                        .orderByDesc(CarModelPO::getId)
        );
        return PageResult.of(page, carCvt.toCarModelList(page.getRecords()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCarModel(CarModel carModel) {
        carModelRepository.updateById(carCvt.carModelToPO(carModel));
    }

    @Override
    public CarModel getCarModelById(Long id) {
        CarModelPO carModelPO = carModelRepository.getById(id);
        return carCvt.toCarModel(carModelPO);
    }

    @Override
    public CarModel getCarModelByNo(String modelNo) {
        CarModelPO carModelPO = carModelRepository.getOne(
                Wrappers.lambdaQuery(CarModelPO.class).eq(CarModelPO::getModelNo, modelNo)
        );
        return carCvt.toCarModel(carModelPO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCarModel(Long id) {
        carModelRepository.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCar(CreateCarReq createCarReq) {
        // 保存车辆信息
        CarPO carPO = carCvt.toCarPO(createCarReq);
        carRepository.save(carPO);
        // 保存车辆路径关联信息
        if (CollectionUtils.isNotEmpty(createCarReq.getPathIds())) {
            List<CarPathPO> carPathPOS = createCarReq.getPathIds().stream().map(pathId ->
                    carCvt.toCarPathPO(carPO.getId(), pathId)).toList();
            carPathRepository.saveBatch(carPathPOS);
        }
    }

    @Override
    public Car getCarById(Long id) {
        CarPO carPO = carRepository.lambdaQueryPlus().eq(CarPO::getId, id)
                .bindOne(CarPO::getModel, CarPO::getCarPaths, CarPO::getBindCar);
        return carCvt.toCar(carPO);
    }

    @Override
    public Car getCarByNo(String carNo) {
        CarPO carPO = carRepository.lambdaQueryPlus().eq(CarPO::getCarNo, carNo)
                .bindOne(CarPO::getModel, CarPO::getCarPaths, CarPO::getBindCar);
        return carCvt.toCar(carPO);
    }

    @Override
    public PageResult<Car> getCarPage(GetCarPageReq getCarPageReq) {
        Page<CarPO> carPOPage = carRepository.lambdaQueryPlus()
                .like(StringUtils.isNotEmpty(getCarPageReq.getCarNo()), CarPO::getCarNo, getCarPageReq.getCarNo())
                .like(StringUtils.isNotEmpty(getCarPageReq.getName()), CarPO::getName, getCarPageReq.getName())
                .eq(ObjectUtils.isNotNull(getCarPageReq.getType()), CarPO::getType, getCarPageReq.getType())
                .eq(ObjectUtils.isNotNull(getCarPageReq.getArea()), CarPO::getArea, getCarPageReq.getArea())
                .eq(ObjectUtils.isNotNull(getCarPageReq.getHeadDirection()), CarPO::getHeadDirection,
                        getCarPageReq.getHeadDirection())
                .orderByDesc(CarPO::getId)
                .bindPage(getCarPageReq.toPage(), CarPO::getModel, CarPO::getCarPaths, CarPO::getBindCar);
        if (carPOPage.getRecords().isEmpty()) {
            return PageResult.emptyResult();
        }

        return PageResult.of(carPOPage, carCvt.toCarList(carPOPage.getRecords()));
    }

    @Override
    public List<Car> getAllCar() {
        List<CarPO> carPOList = carRepository.lambdaQueryPlus()
                .orderByDesc(CarPO::getId)
                .bindList(CarPO::getModel, CarPO::getCarPaths, CarPO::getBindCar);
        return carCvt.toCarList(carPOList);
    }

    @Override
    public List<Car> getlistByArea(AreaEnum area) {
        List<CarPO> carPOS = carRepository.list(
                Wrappers.lambdaQuery(CarPO.class).eq(CarPO::getArea, area)
        );
        return carCvt.toCarList(carPOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCar(Long id, UpdateCarReq updateCarReq) {
        // 更新车辆信息
        carRepository.updateById(carCvt.toCarPO(id, updateCarReq));
        // 更新车辆路径关联信息
        carPathRepository.remove(Wrappers.lambdaQuery(CarPathPO.class).eq(CarPathPO::getCarId, id));
        if (CollectionUtils.isNotEmpty(updateCarReq.getPathIds())) {
            List<CarPathPO> carPathPOS = updateCarReq.getPathIds().stream().map(pathId ->
                    carCvt.toCarPathPO(id, pathId)).toList();
            carPathRepository.saveBatch(carPathPOS);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCar(Long id) {
        // 删除车辆信息
        carRepository.removeById(id);
        // 删除车辆路径关联信息
        carPathRepository.remove(Wrappers.lambdaQuery(CarPathPO.class).eq(CarPathPO::getCarId, id));
    }

    @Override
    public List<Car> getCarsByCarModelId(Long carModelId){
        List<CarPO> carPOList = carRepository.lambdaQueryPlus()
                .eq(ObjectUtils.isNotNull(carModelId), CarPO::getCarModelId, carModelId)
                .bindList(CarPO::getModel, CarPO::getCarPaths, CarPO::getBindCar);

        return carCvt.toCarList(carPOList);
    }
}
