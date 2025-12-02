package cn.hex.ddp.manufacture.api.scheduling.rest.converter;

import cn.hex.ddp.manufacture.api.scheduling.rest.vo.FailedOrderInfoVO;
import cn.hex.ddp.manufacture.api.scheduling.rest.vo.SchedulingResultVO;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.output.SchedulingResult;
import org.mapstruct.Mapper;

/**
 * 排产API转换器接口
 * @author 冯泽邦
 * @date 2025/11/25
 */
@Mapper(componentModel = "spring")
public interface ScheduliungApiConverter {
    SchedulingResultVO schedulingResultToSchedulingResultVO(SchedulingResult schedulingResult);

    FailedOrderInfoVO failedOrderInfoToFailedOrderInfoVO(SchedulingResult.FailedOrderInfo failedOrderInfo);
}
