package cn.hex.ddp.manufacture.domain.scheduling.manager;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.scheduling.rest.req.GetSchedulingPageReq;
import cn.hex.ddp.manufacture.api.scheduling.rest.req.ProductsDetailReq;
import cn.hex.ddp.manufacture.domain.scheduling.enums.PLanType;
import cn.hex.ddp.manufacture.domain.scheduling.model.SchedulingOrderDetail;
import cn.hex.ddp.manufacture.domain.scheduling.model.SchedulingPlan;
import cn.hex.ddp.manufacture.infrastructure.scheduling.persistence.po.SchedulingOrderDetailPO;

import java.util.List;

/**
 * 排产管理接口
 *
 * @author 冯泽邦
 * @date 2025/11/25
 */
public interface SchedulingManager {
    /**
     * 保存排产计划
     *
     * @param plan 排产计划
     */
    Long saveSchedulingPlan(SchedulingPlan plan);

    void saveSchedulingOrderDetail(SchedulingOrderDetail detail);

    PageResult<SchedulingPlan> getSchedulingPage(GetSchedulingPageReq req);

    /**
     * 根据计划类型删除排产计划数据
     * 主表数据+排产订单详情表数据
     *
     * @param pLanType 排产计划类型
     */
    void deleteSchedulingPlan(PLanType pLanType);

    SchedulingPlan getSchedulingPlanById(Long scheduleId);

    /**
     * 获取排产订单ID列表
     * @param scheduleId 排产计划ID,id为null时返回所有排产订单ID
     * @return 排产订单ID列表
     */
    List<Long> getSchedulingOrderIds(Long scheduleId);

    PageResult<SchedulingOrderDetailPO> getSchedulingOrderDetailPage(Long scheduleId, ProductsDetailReq req);

    List<SchedulingPlan> deleteAndBackupSchedulingPlan(PLanType pLanType);

    List<SchedulingOrderDetail> deleteAndBackupSchedulingOrderDetail(PLanType pLanType);
}
