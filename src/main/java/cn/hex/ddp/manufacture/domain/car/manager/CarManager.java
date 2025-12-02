package cn.hex.ddp.manufacture.domain.car.manager;

import cn.hex.ddp.manufacture.api.car.rest.req.CreateCarReq;
import cn.hex.ddp.manufacture.api.car.rest.req.GetCarModelPageReq;
import cn.hex.ddp.manufacture.api.car.rest.req.GetCarPageReq;
import cn.hex.ddp.manufacture.api.car.rest.req.UpdateCarReq;
import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.domain.car.model.Car;
import cn.hex.ddp.manufacture.domain.car.model.CarModel;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;

import java.util.List;

/**
 * 车辆管理接口
 *
 * @author Huhaisen
 * @date 2024/05/16
 */
public interface CarManager {
    /**
     * 创建车辆型号
     *
     * @param carModel 车辆型号
     */
    void createCarModel(CarModel carModel);

    /**
     * 获得车辆型号分页数据
     *
     * @param getCarModelPageReq 分页请求参数
     * @return {@link PageResult }<{@link CarModel }>
     */
    PageResult<CarModel> getCarModelPage(GetCarModelPageReq getCarModelPageReq);

    /**
     * 更新车辆型号
     *
     * @param carModel  车辆型号
     */
    void updateCarModel(CarModel carModel);

    /**
     * 根据ID获取车辆型号
     *
     * @param id 车辆型号ID
     * @return {@link CarModel }
     */
    CarModel getCarModelById(Long id);

    /**
     * 根据车辆型号编号获取车辆型号
     *
     * @param modelNo 车辆型号编号
     * @return {@link CarModel }
     */
    CarModel getCarModelByNo(String modelNo);

    /**
     * 删除车辆型号
     *
     * @param id 车辆型号ID
     */
    void deleteCarModel(Long id);

    /**
     * 创建车辆
     *
     * @param createCarReq 车辆创建请求参数
     */
    void createCar(CreateCarReq createCarReq);

    /**
     * 根据ID获取车辆
     *
     * @param id    车辆ID
     * @return {@link Car }
     */
    Car getCarById(Long id);

    /**
     * 根据车辆编号获取车辆
     *
     * @param carNo 车辆编号
     * @return {@link Car }
     */
    Car getCarByNo(String carNo);

    /**
     * 根据车辆型号获取车辆列表
     *
     * @param carModelId carModelId
     * @return List<Car>
     */
    List<Car> getCarsByCarModelId(Long carModelId);

    /**
     * 获得车辆分页数据
     *
     * @param getCarPageReq 分页请求参数
     * @return {@link PageResult }<{@link Car }>
     */
    PageResult<Car> getCarPage(GetCarPageReq getCarPageReq);

    /**
     * 更新车辆
     *
     * @param id 车辆ID
     * @param updateCarReq 车辆更新请求参数
     */
    void updateCar(Long id, UpdateCarReq updateCarReq);

    /**
     * 删除车辆
     * @param id 车辆ID
     */
    void deleteCar(Long id);

    List<Car> getAllCar();


    /**
     * @param area
     * @return {@link List }<{@link Car }>
     */
    List<Car> getlistByArea(AreaEnum area);

    /**
     * 插入
     */
}
