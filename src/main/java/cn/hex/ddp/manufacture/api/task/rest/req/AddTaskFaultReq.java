package cn.hex.ddp.manufacture.api.task.rest.req;


import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.task.enums.ResourceTypeEnum;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 添加仿真故障模拟列表请求参数
 *
 * @author zhanganqi
 * @date 2025/06/12
 */
@Data
public class AddTaskFaultReq {
    /**
     * 故障区域
     */
    @NotNull(message = "故障区域不能为空")
    private AreaEnum area;

    /**
     * 故障资源类型
     */
    @NotNull(message = "故障资源类型不能为空")
    private ResourceTypeEnum resourceType;

    /**
     * 故障资源ID
     */
    @NotNull(message = "故障资源ID不能为空")
    private Long resourceId;

    /**
     * 故障开始时间
     * 校验故障时间不能早是过去时间
     */
    @FutureOrPresent(message = "故障开始时间不能早是过去时间")
    @NotNull(message = "故障开始时间不能为空")
    private LocalDateTime startTime;

    /**
     * 故障持续时间，单位是秒
     */
    @NotNull(message = "故障持续时间不能为空")
    private Long duration;
}