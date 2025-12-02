package cn.hex.ddp.manufacture.infrastructure.scheduling.managerimpl.converter;

import cn.hex.ddp.manufacture.domain.scheduling.model.SchedulingOrderDetail;
import cn.hex.ddp.manufacture.domain.scheduling.model.SchedulingPlan;
import cn.hex.ddp.manufacture.infrastructure.scheduling.persistence.po.SchedulingOrderDetailPO;
import cn.hex.ddp.manufacture.infrastructure.scheduling.persistence.po.SchedulingPlanPO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 排产数据转换器
 *
 * @author 冯泽邦
 * @date 2025/11/25
 */
@Mapper(componentModel = "spring")
public interface SchedulingConverter {

    SchedulingPlanPO toSchedulingPlanPO(SchedulingPlan schedulingPlan);

    SchedulingPlan toSchedulingPlan(SchedulingPlanPO schedulingPlanPO);

    SchedulingOrderDetailPO toSchedulingOrderDetailPO(SchedulingOrderDetail schedulingOrderDetail);

    SchedulingOrderDetail toSchedulingOrderDetail(SchedulingOrderDetailPO schedulingOrderDetailPO);

    List<SchedulingPlan> toSchedulingPlanList(List<SchedulingPlanPO> schedulingPlanPOS);
}
