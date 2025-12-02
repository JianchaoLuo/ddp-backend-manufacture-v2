package cn.hex.ddp.manufacture.application.path.converter;

import cn.hex.ddp.manufacture.api.path.rest.req.CreatePathReq;
import cn.hex.ddp.manufacture.api.path.rest.req.UpdatePathReq;
import cn.hex.ddp.manufacture.application.path.dto.out.PathDTO;
import cn.hex.ddp.manufacture.application.path.dto.out.PathSummaryDTO;
import cn.hex.ddp.manufacture.domain.path.model.Path;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * 路径转换器接口
 *
 * @author Huhaisen
 * @date 2024/05/15
 */
@Mapper(componentModel = "spring")
public interface PathAppConverter {
    Path toPath(CreatePathReq createPathReq);

    PathDTO toPathDTO(Path path);

    Path updatePath(@MappingTarget Path path, UpdatePathReq updatePathReq);

    List<PathSummaryDTO> toPathSummaryDTOList(List<Path> paths);
}
