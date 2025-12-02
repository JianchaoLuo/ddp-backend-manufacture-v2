package cn.hex.ddp.manufacture.api.scheduling.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 获取排产计划分页请求
 * @author 冯泽邦
 * @date 2025/11/25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetSchedulingPageReq  extends PageReq {
    /**
     * 订单id(不传不搜索)
     */
    private Long orderId;

    /**
     * 产品id(不传不搜索)
     */
    private Long productId;

    /**
     * 开始时间(不传不搜索)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 结束时间(不传不搜索)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}