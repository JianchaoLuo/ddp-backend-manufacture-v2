package cn.hex.ddp.manufacture.api.mold.rest.converter;

import cn.hex.ddp.manufacture.api.mold.rest.vo.MoldGroupSummaryVO;
import cn.hex.ddp.manufacture.application.mold.dto.out.MoldGroupSummaryDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 模具API转换器
 *
 * @author Huhaisen
 * @date 2024/05/21
 */
@Mapper(componentModel = "spring")
public interface MoldApiConverter {
    List<MoldGroupSummaryVO> toMoldGroupSummaryVOList(List<MoldGroupSummaryDTO> moldGroupSummaryDTOList);
}
