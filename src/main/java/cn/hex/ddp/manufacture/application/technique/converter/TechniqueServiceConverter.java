package cn.hex.ddp.manufacture.application.technique.converter;

import cn.hex.ddp.manufacture.api.mold.rest.vo.MoldGroupSummaryVO;
import cn.hex.ddp.manufacture.api.sandbox.rest.vo.SandboxGroupSummaryVO;
import cn.hex.ddp.manufacture.api.technique.rest.req.CreateMoltenIronFormulaReq;
import cn.hex.ddp.manufacture.api.technique.rest.req.CreateSandFormulaReq;
import cn.hex.ddp.manufacture.api.technique.rest.req.CreateTechniqueReq;
import cn.hex.ddp.manufacture.api.technique.rest.vo.TechniquePageVO;
import cn.hex.ddp.manufacture.api.technique.rest.vo.TechniqueVO;
import cn.hex.ddp.manufacture.domain.technique.model.MoltenIronFormula;
import cn.hex.ddp.manufacture.domain.technique.model.SandFormula;
import cn.hex.ddp.manufacture.domain.technique.model.Technique;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface TechniqueServiceConverter {
    @Mapping(target = "id", ignore = true)
    SandFormula toSandFormula(CreateSandFormulaReq request);

    @Mapping(target = "id", ignore = true)
    MoltenIronFormula toMoltenIronFormula(CreateMoltenIronFormulaReq createMoltenIronFormulaReq);

    @Mapping(target = "sandFormula", expression = "java(toSandFormula(createTechniqueReq.getSandFormulaId()))")
    @Mapping(target = "moltenIronFormula", expression = "java(toMoltenIronFormula(createTechniqueReq" +
            ".getMoltenIronFormulaId()))")
    @Mapping(target = "id", ignore = true)
    Technique toTechnique(CreateTechniqueReq createTechniqueReq);

    default SandFormula toSandFormula(Long id) {
        SandFormula sandFormula = new SandFormula();
        sandFormula.setId(id);
        return sandFormula;
    }

    default MoltenIronFormula toMoltenIronFormula(Long id) {
        MoltenIronFormula moltenIronFormula = new MoltenIronFormula();
        moltenIronFormula.setId(id);
        return moltenIronFormula;
    }

    TechniqueVO toTechniqueVO(Technique technique);

    List<TechniqueVO> toTechniqueVO(List<Technique> techniques);

    default List<TechniquePageVO> toTechniquePageVOList(List<Technique> techniqueList,
                                                    Map<Long, SandboxGroupSummaryVO> sandboxGroupSummaryVOMap,
                                                    Map<Long, MoldGroupSummaryVO> sandboxMoldGroupSummaryVOMap,
                                                    Map<Long, MoldGroupSummaryVO> sandCoreMoldGroupSummaryVOMap) {
        ArrayList<TechniquePageVO> techniquePageVOS = new ArrayList<>(techniqueList.size());
        for (Technique technique : techniqueList) {
            TechniquePageVO techniquePageVO = toTechniquePageVO(technique);
            techniquePageVO.setSandboxGroup(
                    sandboxGroupSummaryVOMap.getOrDefault(technique.getSandboxGroupId(), null)
            );
            techniquePageVO.setSandboxMoldGroup(
                    sandboxMoldGroupSummaryVOMap.getOrDefault(technique.getSandboxMoldGroupId(), null)
            );
            techniquePageVO.setSandCoreMoldGroup(
                    sandCoreMoldGroupSummaryVOMap.getOrDefault(technique.getSandCoreMoldGroupId(), null)
            );
            techniquePageVOS.add(techniquePageVO);
        }
        return techniquePageVOS;
    }

    TechniquePageVO toTechniquePageVO(Technique technique);
}
