package cn.hex.ddp.manufacture.api.scheduling.rest.controller;

import cn.hex.ddp.manufacture.api.common.respond.CommonResult;
import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.scheduling.rest.req.*;
import cn.hex.ddp.manufacture.api.scheduling.rest.vo.*;
import cn.hex.ddp.manufacture.application.scheduling.service.SchedulingService;
import cn.hex.ddp.manufacture.domain.common.utils.DateConverterUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 排产Controller
 * @author 冯泽邦
 * @date 2025/11/25
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/v2/scheduling")
public class SchedulingController {
    @Autowired
    private SchedulingService schedulingService;

    /**
     * 更新排产计划
     */
    @PutMapping("/orders")
    public CommonResult updateSchedule(@Validated @RequestBody UpdateSchedulingReq updateSchedulingReq) {
        long startTime = System.currentTimeMillis();
        log.info("开始更新排产计划，开始时间：{}", DateConverterUtils.formatTime(startTime));

        // 调用服务层执行排产计划更新
        SchedulingResultVO schedulingResultVO = schedulingService.updateSchedule(updateSchedulingReq);

        long endTime = System.currentTimeMillis();
        Long durationMs = endTime - startTime;
        log.info("结束更新排产计划，结束时间：{}，开始时间：{}，算法用时：{}毫秒",
                DateConverterUtils.formatTime(endTime),
                DateConverterUtils.formatTime(startTime),
                durationMs);

        return CommonResult.success(schedulingResultVO);
    }

    /**
     * 排产计划/分页查询排产计划（完成）
     */
    @GetMapping("/page")
    public PageResult<SchedulingSummaryVO> getSchedulingPage(@Valid GetSchedulingPageReq  req){
        return schedulingService.getSchedulingPage(req);
    }

    /**
     * 产品排产详情/分页查询排产品详情（完成）
     */
    @GetMapping("/{scheduleId}/products")
    public PageResult<SchedulingProductVO> getSchedulingProductsPage(@PathVariable Long scheduleId, @Valid ProductsDetailReq  req){
        return schedulingService.getSchedulingProductsPage(scheduleId, req);
    }

    /**
     * 订单完成情况/分页查询订单的完成情况（完成）
     */
    @GetMapping("/{scheduleId}/orders")
    public PageResult<SchedulingOrderVO> getSchedulingOrderPage(@PathVariable Long scheduleId, @Valid OrdersDetailReq req){
        return schedulingService.getSchedulingOrderPage(scheduleId, req);
    }

    /**
     * 排产详情（完成）
     * 分页查询所有处于排产计划中的订单
     */
    @GetMapping("/orders/page")
    public PageResult<SchedulingOrderVO> getSchedulingOrdersPage(@Valid SchedulingDetailReq  req ) {
        return schedulingService.getSchedulingOrdersPage(req);
    }

    /**
     * 决策
     * @param req 传入需要插入排产计划的临时订单
     * return 决策结果
     */
    @PostMapping("/decision")
    public SchedulingResultVO  makeSchedulingDecision(@Valid @RequestBody DecisionReq req) {
        return schedulingService.makeSchedulingDecision(req);
    }
}
