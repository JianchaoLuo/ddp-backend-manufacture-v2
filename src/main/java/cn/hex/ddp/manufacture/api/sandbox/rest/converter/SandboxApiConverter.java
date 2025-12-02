package cn.hex.ddp.manufacture.api.sandbox.rest.converter;

import cn.hex.ddp.manufacture.api.sandbox.rest.vo.SandboxGroupSummaryVO;
import cn.hex.ddp.manufacture.application.sandbox.dto.out.SandboxGroupSummaryDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 砂箱API转换器接口
 *
 * @author Huhaisen
 * @date 2024/05/20
 */
@Mapper(componentModel = "spring")
public interface SandboxApiConverter {
    List<SandboxGroupSummaryVO> toSandboxGroupSummaryVOList(List<SandboxGroupSummaryDTO> sandboxGroupSummaryDTOList);
}
