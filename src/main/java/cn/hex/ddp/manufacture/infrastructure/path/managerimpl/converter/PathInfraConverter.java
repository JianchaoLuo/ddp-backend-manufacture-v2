package cn.hex.ddp.manufacture.infrastructure.path.managerimpl.converter;

import cn.hex.ddp.manufacture.api.path.rest.vo.PathSummaryVO;
import cn.hex.ddp.manufacture.domain.path.model.Path;
import cn.hex.ddp.manufacture.infrastructure.path.persistence.po.PathPO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 路径转换器接口
 *
 * @author Huhaisen
 * @date 2024/05/15
 */
@Mapper(componentModel = "spring")
public interface PathInfraConverter {
    PathPO toPathPO(Path path);

    Path toPath(PathPO pathPO);

    List<Path> toPathList(List<PathPO> pathPOList);

    PathSummaryVO toPathSummaryVO(PathPO pathPO);
}
