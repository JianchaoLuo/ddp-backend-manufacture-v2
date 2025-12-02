package cn.hex.ddp.manufacture.application.car.service;

import cn.hex.ddp.manufacture.api.car.rest.req.*;
import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.application.car.dto.out.CarDTO;
import cn.hex.ddp.manufacture.application.car.dto.out.CarDetailDTO;
import cn.hex.ddp.manufacture.application.car.dto.out.CarSummaryDTO;
import cn.hex.ddp.manufacture.domain.car.model.CarModel;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;

import java.util.List;

/**
 * 车辆服务接口
 *
 * @author Huhaisen
 * @date 2024/05/16
 */
public interface CarService {
    /**
     * 创建车辆型号
     *
     * @param createCarModelReq 车辆型号创建请求
     */
    void createCarModel(CreateCarModelReq createCarModelReq);

    /**
     * 分页查询车辆型号
     *
     * @param getCarModelPageReq 分页查询请求
     * @return {@link PageResult }<{@link CarModel }>
     */
    PageResult<CarModel> getCarModelPage(GetCarModelPageReq getCarModelPageReq);

    /**
     * 更新车辆型号
     *
     * @param id                车辆型号ID
     * @param updateCarModelReq 车辆型号更新请求
     */
    void updateCarModel(Long id, UpdateCarModelReq updateCarModelReq);

    /**
     * 删除车辆型号
     *
     * @param id 车辆型号ID
     */
    void deleteCarModel(Long id);

    /**
     * 创建车辆
     *
     * @param createCarReq 车辆创建请求
     */
    void createCar(CreateCarReq createCarReq);

    /**
     * 获取车辆信息
     *
     * @param id 车辆ID
     * @return {@link CarDTO }
     */
    CarDTO getCar(Long id);

    /**
     * 分页查询车辆
     *
     * @param getCarPageReq 分页查询请求
     * @return {@link PageResult }<{@link CarDTO }>
     */
    PageResult<CarDTO> getCarPage(GetCarPageReq getCarPageReq);

    /**
     * 更新车辆信息
     *
     * @param id           车辆ID
     * @param updateCarReq 车辆更新请求
     */
    void updateCar(Long id, UpdateCarReq updateCarReq);

    /**
     * 删除车辆
     *
     * @param id 车辆ID
     */
    void deleteCar(Long id);

    List<CarDetailDTO> getAllCar();

    List<CarSummaryDTO> getlistByArea(AreaEnum area);
}
