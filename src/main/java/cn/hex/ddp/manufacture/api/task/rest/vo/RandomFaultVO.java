package cn.hex.ddp.manufacture.api.task.rest.vo;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.task.enums.ResourceTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 随机故障VO
 * @Author: FengZebang
 * @Date: 2025/6/13
 */
@Data
public class RandomFaultVO {
    /**
     * 区域
     */
    private AreaEnum area;

    /**
     * 故障资源
     */
    private ResourceTypeEnum  resourceType;

    /**
     * 故障开始时间
     */
    private LocalDateTime startTime;

    /**
     * 故障结束时间
     */
    private LocalDateTime endTime;
}
