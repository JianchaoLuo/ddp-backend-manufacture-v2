package cn.hex.ddp.manufacture.api.path.rest.converter;

import cn.hex.ddp.manufacture.api.path.rest.vo.PathSummaryVO;
import cn.hex.ddp.manufacture.api.path.rest.vo.PathVO;
import cn.hex.ddp.manufacture.application.path.dto.out.PathDTO;
import cn.hex.ddp.manufacture.application.path.dto.out.PathSummaryDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 路径API转换器接口
 *
 * @author Huhaisen
 * @date 2024/05/15
 */
@Mapper(componentModel = "spring")
public interface PathApiConverter {
    PathVO toPathVO(PathDTO pathDTO);

    List<PathVO> toPathVOList(List<PathDTO> list);

    List<PathSummaryVO> toPathSummaryVOList(List<PathSummaryDTO> pathSummaryDTOS);
}
