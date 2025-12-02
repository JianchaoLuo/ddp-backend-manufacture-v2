package cn.hex.ddp.manufacture.infrastructure.scheduling.managerimpl;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.scheduling.rest.req.GetSchedulingPageReq;
import cn.hex.ddp.manufacture.api.scheduling.rest.req.ProductsDetailReq;
import cn.hex.ddp.manufacture.domain.scheduling.enums.PLanType;
import cn.hex.ddp.manufacture.domain.scheduling.manager.SchedulingManager;
import cn.hex.ddp.manufacture.domain.scheduling.model.SchedulingOrderDetail;
import cn.hex.ddp.manufacture.domain.scheduling.model.SchedulingPlan;
import cn.hex.ddp.manufacture.infrastructure.scheduling.managerimpl.converter.SchedulingConverter;
import cn.hex.ddp.manufacture.infrastructure.scheduling.persistence.po.SchedulingOrderDetailPO;
import cn.hex.ddp.manufacture.infrastructure.scheduling.persistence.po.SchedulingPlanPO;
import cn.hex.ddp.manufacture.infrastructure.scheduling.persistence.postgresql.repository.SchedulingOrderDetailPORepository;
import cn.hex.ddp.manufacture.infrastructure.scheduling.persistence.postgresql.repository.SchedulingPlanPORepository;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.esotericsoftware.minlog.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 排产管理实现类
 * @author 冯泽邦
 * @date 2025/11/25
 */
@Repository
public class SchedulingManagerImpl implements SchedulingManager {
    @Autowired
    private SchedulingPlanPORepository schedulingPlanPORepository;

    @Autowired
    private SchedulingOrderDetailPORepository schedulingOrderDetailPORepository;

    @Autowired
    private SchedulingConverter schedulingConverter;

    @Override
    public Long saveSchedulingPlan(SchedulingPlan plan) {
        // 参数校验
        if(plan == null){
            Log.warn("排产计划为空，无法保存");
            return null;
        }
        // 转换为PO对象
        SchedulingPlanPO planPO = schedulingConverter.toSchedulingPlanPO(plan);

        // 保存到数据库
        schedulingPlanPORepository.save(planPO);

        // 返回保存记录的ID
        return planPO.getId();
    }

    @Override
    public void saveSchedulingOrderDetail(SchedulingOrderDetail detail) {
        // 参数校验
        if (detail == null) {
            Log.warn("排产订单详情为空，无法保存");
            return;
        }

        // 保存排产订单详情到数据库
        schedulingOrderDetailPORepository.save(
                schedulingConverter.toSchedulingOrderDetailPO( detail)
        );
    }


    @Override
    public PageResult<SchedulingPlan> getSchedulingPage(GetSchedulingPageReq req) {
        List<SchedulingOrderDetailPO> list = schedulingOrderDetailPORepository.lambdaQueryPlus()
                .eq(ObjectUtils.isNotNull(req.getOrderId()), SchedulingOrderDetailPO::getOrderId, req.getOrderId())
                .eq(ObjectUtils.isNotNull(req.getProductId()), SchedulingOrderDetailPO::getProductId, req.getProductId())
                .list();
        List<Long> planIds = list.stream()
                .map(SchedulingOrderDetailPO::getSchedulingPlanId)
                .distinct()
                .toList();
        if (CollectionUtils.isEmpty(planIds)) {
            return PageResult.emptyResult();
        }
        Page<SchedulingPlanPO> page = schedulingPlanPORepository.lambdaQueryPlus()
                .ge(ObjectUtils.isNotNull(req.getStartTime()), SchedulingPlanPO::getScheduleDate, req.getStartTime())
                .le(ObjectUtils.isNotNull(req.getEndTime()), SchedulingPlanPO::getScheduleDate, req.getEndTime())
                .in(SchedulingPlanPO::getId, planIds)
                .page(req.toPage());
        return PageResult.of(page, schedulingConverter::toSchedulingPlanList);
    }

    @Override
    public void deleteSchedulingPlan(PLanType pLanType) {
        // 参数校验
        if (pLanType == null) {
            Log.warn("计划类型为空，无法删除排产计划");
            return;
        }

        // 删除排产订单详情记录
        schedulingOrderDetailPORepository.lambdaUpdate()
                .eq(SchedulingOrderDetailPO::getPlanType, pLanType)
                .remove();

        // 删除排产计划主记录
        schedulingPlanPORepository.lambdaUpdate()
                .eq(SchedulingPlanPO::getPlanType, pLanType)
                .remove();
    }

    @Override
    public SchedulingPlan getSchedulingPlanById(Long scheduleId) {
        SchedulingPlanPO schedulingPlanPO = schedulingPlanPORepository.getById(scheduleId);
        return schedulingConverter.toSchedulingPlan(schedulingPlanPO);
    }


    @Override
    public List<Long> getSchedulingOrderIds(Long scheduleId ) {
        // 查询所有排产订单详情中计划类型为排产计划的订单ID，并去重
        return schedulingOrderDetailPORepository.lambdaQuery()
                .eq(SchedulingOrderDetailPO::getPlanType, PLanType.SCHEDULE_PLAN)
                .eq(scheduleId != null, SchedulingOrderDetailPO::getSchedulingPlanId, scheduleId)
                .orderByDesc(SchedulingOrderDetailPO::getUpdateTime)
                .orderByDesc(SchedulingOrderDetailPO::getCreateTime)
                .list()
                .stream()
                .map(SchedulingOrderDetailPO::getOrderId)
                .distinct()
                .toList();
    }

    @Override
    public PageResult<SchedulingOrderDetailPO> getSchedulingOrderDetailPage(Long scheduleId, ProductsDetailReq req) {
        // 查询指定排产计划ID下计划类型为排产计划的排产订单详情，并进行分页
        Page<SchedulingOrderDetailPO> page = schedulingOrderDetailPORepository.lambdaQuery()
                .eq(SchedulingOrderDetailPO::getSchedulingPlanId, scheduleId)
                .eq(SchedulingOrderDetailPO::getPlanType, PLanType.SCHEDULE_PLAN)
                .page(req.toPage());

        return PageResult.of(page);
    }

    @Override
    public List<SchedulingPlan> deleteAndBackupSchedulingPlan(PLanType pLanType) {
        // 先查询需要删除的排产计划主记录用于备份
        List<SchedulingPlanPO> backupPlans = schedulingPlanPORepository.lambdaQuery()
                .eq(SchedulingPlanPO::getPlanType, pLanType)
                .list();

        // 转换为领域模型用于返回
        List<SchedulingPlan> backupResult = backupPlans.stream()
                .map(schedulingConverter::toSchedulingPlan)
                .filter(Objects::nonNull)
                .toList();

        // 删除排产计划主记录
        schedulingPlanPORepository.lambdaUpdate()
                .eq(SchedulingPlanPO::getPlanType, pLanType)
                .remove();

        return backupResult;
    }

    @Override
    public List<SchedulingOrderDetail> deleteAndBackupSchedulingOrderDetail(PLanType pLanType) {
        // 先查询需要删除的排产订单详情记录用于备份
        List<SchedulingOrderDetailPO> backupDetails = schedulingOrderDetailPORepository.lambdaQuery()
                .eq(SchedulingOrderDetailPO::getPlanType, pLanType)
                .list();

        // 转换为领域模型用于返回
        List<SchedulingOrderDetail> backupResult = backupDetails.stream()
                .map(schedulingConverter::toSchedulingOrderDetail)
                .filter(Objects::nonNull)
                .toList();

        // 删除排产订单详情记录
        schedulingOrderDetailPORepository.lambdaUpdate()
                .eq(SchedulingOrderDetailPO::getPlanType, pLanType)
                .remove();

        return backupResult;
    }


}
