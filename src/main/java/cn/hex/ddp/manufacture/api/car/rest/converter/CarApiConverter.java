package cn.hex.ddp.manufacture.api.car.rest.converter;

import cn.hex.ddp.manufacture.api.car.rest.vo.CarSummaryVO;
import cn.hex.ddp.manufacture.api.car.rest.vo.CarVO;
import cn.hex.ddp.manufacture.application.car.dto.out.CarDTO;
import cn.hex.ddp.manufacture.application.car.dto.out.CarSummaryDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 车辆API转换器接口
 *
 * @author Huhaisen
 * @date 2024/05/16
 */
@Mapper(componentModel = "spring")
public interface CarApiConverter {
    CarVO toCarVo(CarDTO car);

    List<CarVO> toCarVOList(List<CarDTO> list);

    List<CarSummaryVO> toCarSummaryVOList(List<CarSummaryDTO> carSummaryDTOS);
}
