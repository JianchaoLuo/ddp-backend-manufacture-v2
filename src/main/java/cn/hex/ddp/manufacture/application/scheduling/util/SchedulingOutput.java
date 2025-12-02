package cn.hex.ddp.manufacture.application.scheduling.util;

import cn.hex.ddp.manufacture.domain.order.manager.OrderManager;
import cn.hex.ddp.manufacture.domain.product.enums.ProductionProgressStatusEnum;
import cn.hex.ddp.manufacture.domain.scheduling.enums.PLanType;
import cn.hex.ddp.manufacture.domain.scheduling.manager.SchedulingManager;
import cn.hex.ddp.manufacture.domain.scheduling.model.SchedulingOrderDetail;
import cn.hex.ddp.manufacture.domain.scheduling.model.SchedulingPlan;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.output.SchedulingProduct;
import cn.hex.ddp.manufacture.infrastructure.scheduling.persistence.po.SchedulingOrderDetailPO;
import cn.hex.ddp.manufacture.infrastructure.scheduling.persistence.po.SchedulingPlanPO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 排产算法输出处理工具类
 * @author 冯泽邦
 * @date 2025/11/25
 */
@Slf4j
@Service
public class SchedulingOutput  {

    @Autowired
    private SchedulingManager schedulingManager;

    @Autowired
    private OrderManager orderManager;

    /**
     * 更新排产计划订单预计完成时间
     */
    public void updateOrderExpectedCompletionTime(Map<Long, LocalDateTime> orderFinishTime) {
        orderManager.updateOrderExpectedDeliveryTimeBatch(orderFinishTime);
    }

    /**
     * 更新排产计划
     */
    public void updatePlan(Map<LocalDateTime, List<SchedulingProduct>> productionPlan, PLanType pLanType){
        // 参数校验
        if (productionPlan == null || productionPlan.isEmpty()) {
            log.warn("排产计划为空，无法更新");
            return;
        }

        // 1. 处理排产计划主表
        for (Map.Entry<LocalDateTime, List<SchedulingProduct>> entry : productionPlan.entrySet()) {
            LocalDateTime scheduleDate = entry.getKey();
            List<SchedulingProduct> products = entry.getValue();

            // 创建 SchedulingPlanPO
            SchedulingPlan plan = new SchedulingPlan();
            plan.setScheduleDate(scheduleDate);
            plan.setTotalProductCount(calculateTotalCount(products));
            //设置计划类型
            plan.setPlanType(pLanType);
            Long schedulingPlanId = schedulingManager.saveSchedulingPlan(plan);

            // 2. 处理排产订单详情，以天为单位批次处理
            for (SchedulingProduct product : products) {
                SchedulingOrderDetail detail = new SchedulingOrderDetail();
                detail.setSchedulingPlanId(schedulingPlanId);
                detail.setOrderId(product.getOrderId());
                detail.setProductId(product.getProductId());
                detail.setProductionItemId(product.getId());
                detail.setProductionCount(
                        orderManager.getTotalNnmByOrderIdAndProductId(product.getOrderId(), product.getProductId())
                );
                detail.setProductionProgress(product.getProductionProgress());
                //设置计划类型
                detail.setPlanType(pLanType);
                schedulingManager.saveSchedulingOrderDetail(detail);
            }
        }
    }

    /**
     * 计算产品总数量
     * @param products 产品列表
     * @return 总数量
     */
    private Integer calculateTotalCount(List<SchedulingProduct> products) {
        if (products == null || products.isEmpty()) {
            return 0;
        }

        //返回当天计划中生产产品数量
        return products.size();
    }

}
