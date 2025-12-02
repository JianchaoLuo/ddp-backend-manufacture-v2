package cn.hex.ddp.manufacture.application.scheduling.service;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.scheduling.rest.req.*;
import cn.hex.ddp.manufacture.api.scheduling.rest.vo.*;
import jakarta.validation.Valid;

/**
 * 排产服务接口
 * @author 冯泽邦
 * @date 2025/11/25
 */
public interface SchedulingService {
    /**
     * 更新排产计划
     */
    SchedulingResultVO updateSchedule(UpdateSchedulingReq updateSchedulingReq);

    /**
     * 分页查询排产计划
     */
    PageResult<SchedulingSummaryVO> getSchedulingPage( GetSchedulingPageReq req);

    /**
     * 排产详情
     */
    PageResult<SchedulingOrderVO> getSchedulingOrdersPage(SchedulingDetailReq  req);

    /**
     * 决策
     * @param req 临时订单列表
     * @return 决策结果
     */
    SchedulingResultVO makeSchedulingDecision( DecisionReq req);

    /**
     * 产品排产详情/分页查询产品详情
     * @param scheduleId 排产计划ID
     * @return 当天生产的所有排产产品信息
     */
    PageResult<SchedulingProductVO> getSchedulingProductsPage(Long scheduleId, ProductsDetailReq  req);

    /**
     * 订单完成情况/分页查询订单的完成情况
     * @param scheduleId 排产计划ID
     * @return 当天生产所有订单的完成情况
     */
    PageResult<SchedulingOrderVO> getSchedulingOrderPage(Long scheduleId, OrdersDetailReq  req);
}
