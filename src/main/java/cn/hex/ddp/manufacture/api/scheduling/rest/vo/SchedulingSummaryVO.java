package cn.hex.ddp.manufacture.api.scheduling.rest.vo;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import lombok.Data;

/**
 * 排产概要VO
 * @author 冯泽邦
 * @date 2025/11/26
 */
@Data
public class SchedulingSummaryVO {
    /**
     * 排产计划ID
     */
    private Long schedulingId;

    /**
     * 排产计划日期
     */
    private String schedulingDate;

    /**
     * 排产产品总量
     */
    private Integer totalProductCount;
}
