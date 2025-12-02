package cn.hex.ddp.manufacture.domain.order.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 订单甘特图
 *
 * @author Huhaisen
 * @date 2024/08/11
 */
@Data
public class OrderGanttChart implements Serializable {
    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
