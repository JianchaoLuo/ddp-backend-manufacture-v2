package cn.hex.ddp.manufacture.application.mold.converter;

import cn.hex.ddp.manufacture.application.mold.dto.out.MoldGroupSummaryDTO;
import cn.hex.ddp.manufacture.domain.mold.model.MoldGroup;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 模具应用转换器接口
 *
 * @author Huhaisen
 * @date 2024/05/21
 */
@Mapper(componentModel = "spring")
public interface MoldAppConverter {
    List<MoldGroupSummaryDTO> toMoldGroupSummaryDTO(List<MoldGroup> moldGroupList);
}
