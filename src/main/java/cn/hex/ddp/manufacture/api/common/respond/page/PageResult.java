package cn.hex.ddp.manufacture.api.common.respond.page;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * 分页结果返回模板
 *
 * @author Huhaisen
 * @date 2024/05/07
 */
@Data
public class PageResult<T> {
    private static final PageResult<?> EMPTY_RESULT = new PageResult<>(0, -1, -1, Collections.emptyList());
    /**
     * 一共有多少条数据
     */
    private Integer total = 0;

    /**
     * 上一页的页码（如果当前页为首页，则返回-1，表示没有上一页）
     */
    private Integer prev = -1;

    /**
     * 下一页的页码（如果当前页为尾页，则返回-1，表示没有下一页）
     */
    private Integer next = -1;

    /**
     * 数据列表
     */
    private List<T> list;

    /**
     * 禁止使用构造函数构造，请使用of方法
     */
    @Deprecated
    public PageResult() {
    }

    @SuppressWarnings("unchecked")
    public static <T> PageResult<T> emptyResult() {
        return (PageResult<T>) EMPTY_RESULT;
    }

    private PageResult(Integer total, Integer prev, Integer next, List<T> list) {
        this.total = total;
        this.prev = prev <= 0 ? -1 : prev;
        this.next = next <= 0 ? -1 : next;
        this.list = list;
    }

    public static <T> PageResult<T> of(Page<T> page) {
        return PageResult.of(
                Long.valueOf(page.getTotal()).intValue(),
                Long.valueOf(page.getCurrent()).intValue(),
                Long.valueOf(page.getSize()).intValue(),
                page.getRecords());
    }

    public static <T, R> PageResult<R> of(Page<T> page, List<R> data) {
        return PageResult.of(
                Long.valueOf(page.getTotal()).intValue(),
                Long.valueOf(page.getCurrent()).intValue(),
                Long.valueOf(page.getSize()).intValue(),
                data);
    }

    public static <T, S> PageResult<S> of(PageResult<T> page, List<S> data) {
        return new PageResult<>(
                page.getTotal(),
                page.getPrev(),
                page.getNext(),
                data
        );
    }

    public static <T> PageResult<T> of(Integer total, Integer current, Integer pageSize, List<T> list) {
        return new PageResult<>(
                total,
                current - 1,
                current < (1.0 * total / pageSize) ? current + 1 : -1,
                list
        );
    }



    public static <T, R> PageResult<T> of(Page<R> page, Function<List<R>, List<T>> converter) {
        return PageResult.of(Long.valueOf(page.getTotal()).intValue(),
                Long.valueOf(page.getCurrent()).intValue(),
                Long.valueOf(page.getSize()).intValue(),
                converter.apply(page.getRecords()));
    }

    public static <T, R> PageResult<T> of(PageResult<R> page, Function<List<R>, List<T>> converter) {
        return new PageResult<>(
                page.getTotal(),
                page.getPrev(),
                page.getNext(),
                converter.apply(page.getList()));
    }

    public static <T> PageResult<T> of(Integer current, Integer pageSize, List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return PageResult.emptyResult();
        }
        return new PageResult<>(
                list.size(),
                current - 1,
                current < (1.0 * list.size() / pageSize) ? current + 1 : -1,
                list.subList((current - 1) * pageSize, Math.min(current * pageSize, list.size()))
        );
    }




    public boolean isEmpty() {
        return list == null || list.isEmpty();
    }

    public void setPrev(Integer prev) {
        this.prev = prev <= 0 ? -1 : prev;
    }

    public void setNext(Integer next) {
        this.next = next <= 0 ? -1 : next;
    }


}
