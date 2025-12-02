package cn.hex.ddp.manufacture.api.common.respond.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.*;
import org.hibernate.validator.constraints.Range;

/**
 * 分页请求公共参数
 *
 * @author Huhaisen
 * @date 2024/05/07
 */
@Data
@NoArgsConstructor
public class PageReq {

    public static final Integer DEFAULT_CURRENT = 1;
    public static final Integer DEFAULT_PAGE_SIZE = 20;

    @Range(min = 1, max = Integer.MAX_VALUE, message = "页码数不合法，需要在正整型范围内")
    private Integer current = DEFAULT_CURRENT;

    @Range(min = 1, max = 200, message = "数据量大小不合法，需要在1-200范围内")
    private Integer pageSize = DEFAULT_PAGE_SIZE;

    public Integer getCurrent() {
        if (current == null) {
            return DEFAULT_CURRENT;
        }
        return current;
    }

    public Integer getPageSize() {
        if (pageSize == null) {
            return DEFAULT_PAGE_SIZE;
        }
        return pageSize;
    }
    public <T> Page<T> toPage() {
        return Page.of(this.getCurrent(), this.getPageSize());
    }
}
