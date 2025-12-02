package cn.hex.ddp.manufacture.api.task.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import cn.hex.ddp.manufacture.domain.task.enums.SimulateStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取仿真任务分页请求参数
 *
 * @author Huhaisen
 * @date 2024/09/21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetTaskPageReq extends PageReq {
    /**
     * 仿真任务名称(模糊查询，不传不搜索)
     */
    private String name;
    /**
     * 仿真状态（条件查询,不传不搜索）
     */
    private SimulateStatusEnum simulateStatus;
}
