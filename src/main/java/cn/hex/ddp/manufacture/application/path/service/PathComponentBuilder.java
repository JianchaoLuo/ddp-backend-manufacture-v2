package cn.hex.ddp.manufacture.application.path.service;

import cn.hex.ddp.manufacture.application.path.dto.out.PathDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 路径组件构建器
 *
 * @author Huhaisen
 * @date 2024/06/28
 */
@Component
public class PathComponentBuilder {

    @Autowired
    private PathService pathService;

    /**
     * 构建路径DTO Map
     * @param pathIds 路径ID列表
     * @return {@link Map }<{@link Long }, {@link PathDTO }>
     */
    public Map<Long, PathDTO> buildPathDTOMap(List<Long> pathIds) {
        List<PathDTO> pathDTOS = pathService.getPathByIds(pathIds);
        return pathDTOS.stream().collect(Collectors.toMap(PathDTO::getId, pathDTO -> pathDTO, (k1, k2) -> k1));
    }

}
