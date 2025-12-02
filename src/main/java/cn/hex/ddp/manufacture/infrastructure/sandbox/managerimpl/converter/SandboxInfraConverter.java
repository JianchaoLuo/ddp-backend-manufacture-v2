package cn.hex.ddp.manufacture.infrastructure.sandbox.managerimpl.converter;

import cn.hex.ddp.manufacture.api.sandbox.rest.req.*;
import cn.hex.ddp.manufacture.api.sandbox.rest.vo.SandboxGroupSummaryVO;
import cn.hex.ddp.manufacture.domain.sandbox.model.Sandbox;
import cn.hex.ddp.manufacture.domain.sandbox.model.SandboxGroup;
import cn.hex.ddp.manufacture.domain.sandbox.model.SandboxGroupSandbox;
import cn.hex.ddp.manufacture.domain.sandbox.model.SandboxModel;
import cn.hex.ddp.manufacture.infrastructure.sandbox.persistence.po.SandboxGroupPO;
import cn.hex.ddp.manufacture.infrastructure.sandbox.persistence.po.SandboxGroupSandboxPO;
import cn.hex.ddp.manufacture.infrastructure.sandbox.persistence.po.SandboxModelPO;
import cn.hex.ddp.manufacture.infrastructure.sandbox.persistence.po.SandboxPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * 砂箱数据转换器
 *
 * @author Huhaisen
 * @date 2024/05/20
 */
@Mapper(componentModel = "spring")
public interface SandboxInfraConverter {
    SandboxModelPO toSandboxModelPO(CreateSandboxModelReq createSandboxModelReq);

    SandboxModelPO toSandboxModelPO(Long id, UpdateSandboxModelReq updateSandboxModelReq);

    List<SandboxModel> toSandboxModelList(List<SandboxModelPO> sandboxModelPOList);

    SandboxModel toSandboxModel(SandboxModelPO sandboxModelPO);

    SandboxPO toSandboxPO(CreateSandboxReq createSandboxReq);

    SandboxPO toSandboxPO(Long id, UpdateSandboxReq updateSandboxReq);

    Sandbox toSandBox(SandboxPO sandboxPO);

    List<Sandbox> toSandboxList(List<SandboxPO> sandboxPOList);

    List<SandboxGroupSandbox> toSandboxGroupSandboxList(List<SandboxGroupSandboxPO> sandboxGroupSandboxPOList);

    SandboxGroupPO toSandboxGroupPO(CreateSandboxGroupReq createSandboxGroupReq);

    SandboxGroupPO toSandboxGroupPO(Long id, UpdateSandboxGroupReq updateSandboxGroupReq);

    SandboxGroupSandboxPO toSandboxGroupSandboxPO(Long sandboxGroupId, Long sandboxId, AddSandboxToGroupReq addSandboxToGroupReq);

    SandboxGroupSandboxPO toSandboxGroupSandboxPO(Long sandboxGroupId, AddSandboxBatchToGroupReq addSandboxBatchToGroupReq);

    List<SandboxGroup> toSandboxGroupList(List<SandboxGroupPO> sandboxGroupPOList);

    SandboxGroup toSandboxGroup(SandboxGroupPO sandboxGroupPO);

    @Mapping(target = "sandbox.id", source = "sandboxId")
    SandboxGroupSandbox toSandboxGroupSandbox(SandboxGroupSandboxPO sandboxGroupSandboxPO);

    SandboxGroupSummaryVO toSandboxGroupSummaryVO(SandboxGroupPO sandboxGroupPO);
}
