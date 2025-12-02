package cn.hex.ddp.manufacture.application.sandbox.converter;

import cn.hex.ddp.manufacture.application.sandbox.dto.out.SandboxGroupSandboxSummaryDTO;
import cn.hex.ddp.manufacture.application.sandbox.dto.out.SandboxGroupSummaryDTO;
import cn.hex.ddp.manufacture.domain.sandbox.model.SandboxGroup;
import cn.hex.ddp.manufacture.domain.sandbox.model.SandboxGroupSandbox;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * 砂箱应用转换器接口
 *
 * @author Huhaisen
 * @date 2024/05/20
 */
@Mapper(componentModel = "spring")
public interface SandboxAppConverter {
    List<SandboxGroupSummaryDTO> toSandboxGroupSummaryDTOList(List<SandboxGroup> sandboxGroupList);

    @Mapping(source = "sandbox.id", target = "sandboxId")
    SandboxGroupSandboxSummaryDTO sandboxGroupSandboxToSandboxGroupSandboxSummaryDTO(SandboxGroupSandbox sandboxGroupSandbox);
}
