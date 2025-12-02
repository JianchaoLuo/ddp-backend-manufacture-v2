package cn.hex.ddp.manufacture.application.path.service;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.path.rest.req.CreatePathReq;
import cn.hex.ddp.manufacture.api.path.rest.req.GetPathPageReq;
import cn.hex.ddp.manufacture.api.path.rest.req.UpdatePathReq;
import cn.hex.ddp.manufacture.application.path.dto.out.PathDTO;
import cn.hex.ddp.manufacture.application.path.dto.out.PathSummaryDTO;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;

import java.util.List;

/**
 * 路径服务接口
 *
 * @author Huhaisen
 * @date 2024/05/15
 */
public interface PathService {
    /**
     * 创建路径
     *
     * @param createPathReq 创建路径请求参数
     */
    void createPath(CreatePathReq createPathReq);

    /**
     * 获取路径详情
     *
     * @param id 路径ID
     * @return {@link PathDTO }
     */
    PathDTO getPath(Long id);

    /**
     * 获取路径列表
     *
     * @param getPathPageReq 分页请求参数
     * @return {@link PageResult }<{@link PathDTO }>
     */
    PageResult<PathDTO> getPathPage(GetPathPageReq getPathPageReq);

    /**
     * 删除路径
     *
     * @param id 路径ID
     */
    void deletePath(Long id);

    /**
     * 更新路径
     * @param id 路径ID
     * @param updatePathReq 更新路径请求参数
     */
    void updatePath(Long id, UpdatePathReq updatePathReq);

    List<PathDTO> getAllPath();

    /**
     * 根据路径ID获取路径信息
     * @param pathIds 路径ID列表
     * @return {@link List }<{@link PathDTO }>
     */
    List<PathDTO> getPathByIds(List<Long> pathIds);

    List<PathSummaryDTO> getListByArea(AreaEnum area);
}
