package cn.hex.ddp.manufacture.infrastructure.technique.managerimpl.converter;

import cn.hex.ddp.manufacture.api.technique.rest.vo.TechniqueSummaryVO;
import cn.hex.ddp.manufacture.domain.technique.model.MoltenIronFormula;
import cn.hex.ddp.manufacture.domain.technique.model.SandFormula;
import cn.hex.ddp.manufacture.domain.technique.model.Technique;
import cn.hex.ddp.manufacture.infrastructure.technique.persistence.po.MoltenIronFormulaPO;
import cn.hex.ddp.manufacture.infrastructure.technique.persistence.po.SandFormulaPO;
import cn.hex.ddp.manufacture.infrastructure.technique.persistence.po.TechniquePO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface TechniqueInfrastructureConverter {
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    SandFormulaPO toSandFormulaPO(SandFormula sandFormula);

    SandFormula toSandFormula(SandFormulaPO sandFormulaPO);

    List<SandFormula> toSandFormula(List<SandFormulaPO> sandFormulaPOS);

    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    MoltenIronFormulaPO toMoltenIronFormulaPO(MoltenIronFormula moltenIronFormula);

    MoltenIronFormula toMoltenIronFormula(MoltenIronFormulaPO moltenIronFormulaPO);

    List<MoltenIronFormula> toMoltenIronFormula(List<MoltenIronFormulaPO> moltenIronFormulaPOS);

    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateBy", ignore = true)
    @Mapping(target = "sandFormulaId", source = "sandFormula.id")
    @Mapping(target = "moltenIronFormulaId", source = "moltenIronFormula.id")
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createBy", ignore = true)
    TechniquePO toTechniquePO(Technique technique);



    default List<Technique> toTechnique(List<TechniquePO> records, Map<Long, SandFormulaPO> sandFormulaPOMap, Map<Long,
            MoltenIronFormulaPO> moltenIronFormulaPOMap) {
        return records.stream().map(record -> {
            Technique technique = toTechnique(record);
            technique.setSandFormula(toSandFormula(sandFormulaPOMap.get(record.getSandFormulaId())));
            technique.setMoltenIronFormula(toMoltenIronFormula(moltenIronFormulaPOMap.get(record.getMoltenIronFormulaId())));
            return technique;
        }).toList();
    }

    @Mapping(target = "sandFormula", ignore = true)
    @Mapping(target = "moltenIronFormula", ignore = true)
    Technique toTechnique(TechniquePO record);

    List<Technique> toTechniqueList(List<TechniquePO> techniquePOList);

    List<TechniqueSummaryVO> toTechniqueSummaryVOList(List<TechniquePO> techniquePOList);
}
