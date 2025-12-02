package cn.hex.ddp.manufacture.api.car.rest.controller;

import cn.hex.ddp.manufacture.api.car.rest.converter.CarApiConverter;
import cn.hex.ddp.manufacture.api.car.rest.req.*;
import cn.hex.ddp.manufacture.api.car.rest.vo.CarSummaryVO;
import cn.hex.ddp.manufacture.api.car.rest.vo.CarVO;
import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.application.car.dto.out.CarDTO;
import cn.hex.ddp.manufacture.application.car.dto.out.CarSummaryDTO;
import cn.hex.ddp.manufacture.application.car.service.CarService;
import cn.hex.ddp.manufacture.domain.car.model.CarModel;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车辆Controller
 *
 * @author Huhaisen
 * @date 2024/05/16
 */
@Validated
@RestController
@RequestMapping("/api/v2/car")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private CarApiConverter carCvt;

    /**
     * 车辆型号/创建车辆型号
     *
     * @param createCarModelReq 车辆型号创建请求参数
     */
    @PostMapping("/model")
    public void createCarModel(@RequestBody @Validated CreateCarModelReq createCarModelReq) {
        carService.createCarModel(createCarModelReq);
    }

    /**
     * 车辆型号/获取车辆型号分页列表
     *
     * @param getCarModelPageReq 车辆型号分页请求参数
     * @return {@link PageResult }<{@link CarModel }>
     */
    @GetMapping("/model/page")
    public PageResult<CarModel> getCarModelPage(@Valid GetCarModelPageReq getCarModelPageReq) {
        return carService.getCarModelPage(getCarModelPageReq);
    }

    /**
     * 车辆型号/更新车辆型号
     *
     * @param id 车辆型号ID
     * @param updateCarModelReq 车辆型号更新请求参数
     */
    @PutMapping("/model/{id}")
    public void updateCarModel(@PathVariable("id") @Valid @NotNull(message = "id不能为空") Long id,
                               @RequestBody @Validated UpdateCarModelReq updateCarModelReq) {
        carService.updateCarModel(id, updateCarModelReq);
    }

    /**
     * 车辆型号/删除车辆型号
     *
     * @param id 车辆型号ID
     */
    @DeleteMapping("/model/{id}")
    public void deleteCarModel(@PathVariable("id") @Valid @NotNull(message = "id不能为空") Long id) {
        carService.deleteCarModel(id);
    }


    /**
     * 创建车辆
     *
     * @param createCarReq 车辆创建请求参数
     */
    @PostMapping("")
    public void createCar(@RequestBody @Valid CreateCarReq createCarReq) {
        carService.createCar(createCarReq);
    }

    /**
     * 获取单个车辆信息
     *
     * @param id 车辆ID
     * @return {@link CarVO }
     */
    @GetMapping("/{id}")
    public CarVO getCar(@PathVariable("id") @Valid @NotNull(message = "id不能为空") Long id) {
        return carCvt.toCarVo(carService.getCar(id));
    }

    /**
     * 获取车辆分页
     *
     * @param getCarPageReq 车辆分页请求参数
     * @return {@link PageResult }<{@link CarSummaryVO }>
     */
    @GetMapping("/page")
    public PageResult<CarVO> getCarPage(@Valid GetCarPageReq getCarPageReq) {
        PageResult<CarDTO> carPage = carService.getCarPage(getCarPageReq);
        return PageResult.of(carPage, carCvt.toCarVOList(carPage.getList()));
    }

    /**
     * 更新车辆信息
     *
     * @param id 车辆ID
     * @param updateCarReq 车辆更新请求参数
     */
    @PutMapping("/{id}")
    public void updateCar(@PathVariable("id") @Valid @NotNull(message = "id不能为空") Long id,
                          @RequestBody @Validated UpdateCarReq updateCarReq) {
        carService.updateCar(id, updateCarReq);
    }

    /**
     * 删除车辆信息
     *
     * @param id 车辆ID
     */
    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable("id") @Valid @NotNull(message = "id不能为空") Long id) {
        carService.deleteCar(id);
    }


    /**
     * 根据区域获取车辆列表
     *
     * @return List<CarSummaryVO>
     */
    @GetMapping("/list/{area}")
    public List<CarSummaryVO> getCarListByArea(@PathVariable("area") @Valid @NotNull(message = "area不能为空") AreaEnum area) {
        List<CarSummaryDTO> carSummaryDTOS = carService.getlistByArea(area);
        if (carSummaryDTOS.isEmpty()) {
            return List.of();
        }
        return carCvt.toCarSummaryVOList(carSummaryDTOS);
    }
}
