package cn.hex.ddp.manufacture.application.technique.service;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.technique.rest.req.*;
import cn.hex.ddp.manufacture.api.technique.rest.vo.TechniquePageVO;
import cn.hex.ddp.manufacture.api.technique.rest.vo.TechniqueVO;
import cn.hex.ddp.manufacture.domain.technique.model.MoltenIronFormula;
import cn.hex.ddp.manufacture.domain.technique.model.SandFormula;

public interface TechniqueService {
    void createSandFormula(CreateSandFormulaReq request);

    void deleteSandFormula(Long id);

    PageResult<SandFormula> searchSandFormula(SearchSandFormulaReq searchSandFormulaReq);

    void updateSandFormula(Long id, CreateSandFormulaReq request);

    void createMoltenIronFormula(CreateMoltenIronFormulaReq createMoltenIronFormulaReq);

    void deleteMoltenIronFormula(Long id);

    PageResult<MoltenIronFormula> searchMoltenIronFormula(SearchMoltenIronFormulaReq searchSandFormulaReq);

    void updateMoltenIronFormula(Long id, CreateMoltenIronFormulaReq createMoltenIronFormulaReq);

    void deleteTechnique(Long id);

    void createTechnique(CreateTechniqueReq createTechniqueReq);

    void updateTechnique(Long id, CreateTechniqueReq createTechniqueReq);

    PageResult<TechniquePageVO> searchTechnique(SearchTechniqueReq searchTechniqueReq);

    TechniqueVO getTechnique(Long id);
}
