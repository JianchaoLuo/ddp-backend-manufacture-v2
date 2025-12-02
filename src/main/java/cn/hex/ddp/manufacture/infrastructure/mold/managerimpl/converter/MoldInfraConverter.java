package cn.hex.ddp.manufacture.infrastructure.mold.managerimpl.converter;

import cn.hex.ddp.manufacture.api.mold.rest.req.*;
import cn.hex.ddp.manufacture.api.mold.rest.vo.MoldGroupSummaryVO;
import cn.hex.ddp.manufacture.api.sandbox.rest.vo.SandboxGroupSummaryVO;
import cn.hex.ddp.manufacture.application.mold.dto.out.MoldGroupSummaryDTO;
import cn.hex.ddp.manufacture.domain.mold.model.Mold;
import cn.hex.ddp.manufacture.domain.mold.model.MoldGroup;
import cn.hex.ddp.manufacture.domain.mold.model.MoldGroupMold;
import cn.hex.ddp.manufacture.domain.mold.model.MoldModel;
import cn.hex.ddp.manufacture.infrastructure.mold.persistence.po.MoldGroupMoldPO;
import cn.hex.ddp.manufacture.infrastructure.mold.persistence.po.MoldGroupPO;
import cn.hex.ddp.manufacture.infrastructure.mold.persistence.po.MoldModelPO;
import cn.hex.ddp.manufacture.infrastructure.mold.persistence.po.MoldPO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 模具基础设施转换器接口
 *
 * @author Huhaisen
 * @date 2024/05/21
 */
@Mapper(componentModel = "spring")
public interface MoldInfraConverter {
    MoldModelPO toMoldModelPO(CreateMoldModelReq createMoldModelReq);

    MoldModelPO toMoldModelPO(Long id, UpdateMoldModelReq updateMoldModelReq);

    List<MoldModel> toMoldModelList(List<MoldModelPO> moldModelPOList);

    MoldModel toMoldModel(MoldModelPO moldModelPO);

    MoldPO toMoldPO(CreateMoldReq createMoldReq);

    MoldPO toMoldPO(Long id, UpdateMoldReq updateMoldReq);

    List<Mold> toMoldList(List<MoldPO> moldPOList);

    Mold toMold(MoldPO moldPO);

    MoldGroupPO toMoldGroupPO(CreateMoldGroupReq createMoldGroupReq);

    MoldGroupPO toMoldGroupPO(Long id, UpdateMoldGroupReq updateMoldGroupReq);

    MoldGroup toMoldGroup(MoldGroupPO moldGroupPO);

    MoldGroupMoldPO toMoldGroupMoldPO(Long moldGroupId, Long moldId, AddMoldToGroupReq addMoldToGroupReq);

    MoldGroupMoldPO toMoldGroupMoldPO(Long moldGroupId, AddMoldBatchToGroupReq addMoldBatchToGroupReq);

    MoldGroupMold toMoldGroupMold(MoldGroupMoldPO moldGroupMoldPO);

    List<MoldGroupMold> toMoldGroupMoldList(List<MoldGroupMoldPO> moldGroupMoldPOList);

    MoldGroupSummaryVO toMoldGroupSummaryVO(MoldGroupPO moldGroupPO);

    List<MoldGroupSummaryDTO> toMoldGroupSummaryDTOList(List<MoldGroupPO> moldGroupPOList);
}
