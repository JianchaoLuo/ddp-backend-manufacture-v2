package cn.hex.ddp.manufacture.domain.technique.manager;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.technique.rest.req.SearchMoltenIronFormulaReq;
import cn.hex.ddp.manufacture.api.technique.rest.req.SearchSandFormulaReq;
import cn.hex.ddp.manufacture.api.technique.rest.req.SearchTechniqueReq;
import cn.hex.ddp.manufacture.api.technique.rest.vo.TechniqueSummaryVO;
import cn.hex.ddp.manufacture.domain.technique.model.MoltenIronFormula;
import cn.hex.ddp.manufacture.domain.technique.model.SandFormula;
import cn.hex.ddp.manufacture.domain.technique.model.Technique;

import java.util.List;
import java.util.Map;

public interface TechniqueManager {
    void createSandFormula(SandFormula sandFormula);

    void deleteSandFormula(Long id);

    SandFormula getSandFormula(Long id);

    SandFormula getSandFormulaByModel(String model);

    PageResult<SandFormula> searchSandFormula(SearchSandFormulaReq searchSandFormulaReq);

    void updateSandFormula(SandFormula sandFormula);

    void createMoltenIronFormula(MoltenIronFormula moltenIronFormula);

    void deleteMoltenIronFormula(Long id);

    MoltenIronFormula getMoltenIronFormula(Long id);

    MoltenIronFormula getMoltenIronFormulaByModel(String model);

    PageResult<MoltenIronFormula> searchMoltenIronFormula(SearchMoltenIronFormulaReq searchSandFormulaReq);

    void updateMoltenIronFormula(MoltenIronFormula moltenIronFormula);

    void deleteTechnique(Long id);

    void createTechnique(Technique technique);

    void updateTechnique(Technique technique);

    PageResult<Technique> searchTechnique(SearchTechniqueReq searchTechniqueReq);

    Technique getTechnique(Long id);

    Technique getTechniqueByNo(String techniqueNo);

    List<Technique> getTechniquesBySandboxGroupId(Long sandboxGroupId);

    List<Technique> getTechniquesByMoldGroupId(Long moldGroupId);

    List<Technique> getTechniquesBySandFormulaId(Long sandFormulaId);

    List<Technique> getTechniquesByMoltenIronFormulaId(Long moltenIronFormulaId);

    Map<Long, TechniqueSummaryVO> getTechniqueSummaryVOMap(List<Long> techniqueIds);

}
