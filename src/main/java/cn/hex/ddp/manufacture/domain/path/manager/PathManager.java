package cn.hex.ddp.manufacture.domain.path.manager;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.path.rest.req.GetPathPageReq;
import cn.hex.ddp.manufacture.api.path.rest.vo.PathSummaryVO;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.path.model.Path;

import java.util.List;
import java.util.Map;

/**
 * 路径管理器接口
 *
 * @author Huhaisen
 * @date 2024/05/15
 */
public interface PathManager {
    /**
     * 创建路径
     *
     * @param path 路径对象
     */
    void createPath(Path path);

    /**
     * 根据路径ID获取路径对象
     *
     * @param id 路径ID
     * @return {@link Path }
     */
    Path getPathById(Long id);

    /**
     * 根据路径编号获取路径对象
     *
     * @param pathNo 路径编号
     * @return {@link Path }
     */
    Path getPathByNo(String pathNo);

    /**
     * 分页获取路径列表
     *
     * @param getPathPageReq 分页查询条件
     * @return {@link PageResult }<{@link Path }>
     */
    PageResult<Path> getPathPage(GetPathPageReq getPathPageReq);

    /**
     * 根据路径ID删除路径
     *
     * @param id 路径ID
     */
    void deletePathById(Long id);

    Map<Long, PathSummaryVO> getPathSummaryVOMap(List<Long> pathIds);

    void updatePathById(Path updatePath);

    List<Path> getAllPath();

    /**
     * 根据路径ID列表获取路径信息
     * @param pathIds 路径ID列表
     * @return {@link List }<{@link Path }>
     */
    List<Path> getPathByIds(List<Long> pathIds);

    List<Path> getListByArea(AreaEnum area);
}
