package cn.hex.ddp.manufacture.api.configuration.rest.req;

import cn.hex.ddp.manufacture.api.common.respond.page.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 获取坐标分页请求参数
 *
 * @author Huhaisen
 * @date 2024/05/13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetCoordinatePageReq extends PageReq {
    /**
     * 横坐标
     */
    private Float x;

    /**
     * 纵坐标
     */
    private Float y;
}
