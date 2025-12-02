package cn.hex.ddp.manufacture.api.technique.rest.controller;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.technique.rest.req.*;
import cn.hex.ddp.manufacture.api.technique.rest.vo.TechniquePageVO;
import cn.hex.ddp.manufacture.api.technique.rest.vo.TechniqueVO;
import cn.hex.ddp.manufacture.application.technique.service.TechniqueService;
import cn.hex.ddp.manufacture.domain.technique.model.MoltenIronFormula;
import cn.hex.ddp.manufacture.domain.technique.model.SandFormula;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 工艺管理
 */
@Validated
@RestController
@RequestMapping("/api/v2/technique")
public class TechniqueController {
    @Autowired
    private TechniqueService techniqueService;

    /**
     * 砂配方/创建砂配方
     */
    @PostMapping("/sand-formula")
    public void createSandFormula(@Valid @RequestBody CreateSandFormulaReq request) {
        techniqueService.createSandFormula(request);
    }

    /**
     * 砂配方/删除砂配方
     *
     * @param id
     */
    @DeleteMapping("/sand-formula/{id}")
    public void deleteSandFormula(@PathVariable Long id) {
        techniqueService.deleteSandFormula(id);
    }

    /**
     * 砂配方/搜索砂配方
     *
     * @param searchSandFormulaReq
     * @return
     */
    @GetMapping("/sand-formula/search")
    public PageResult<SandFormula> searchSandFormula(@Valid SearchSandFormulaReq searchSandFormulaReq) {
        return techniqueService.searchSandFormula(searchSandFormulaReq);
    }

    /**
     * 砂配方/更新砂配方
     *
     * @param id
     * @param request
     */
    @PutMapping("/sand-formula/{id}")
    public void updateSandFormula(@PathVariable Long id, @Valid @RequestBody CreateSandFormulaReq request) {
        techniqueService.updateSandFormula(id, request);
    }

    /**
     * 铁水配方/创建铁水配方
     */
    @PostMapping("/molten-iron-formula")
    public void createMoltenIronFormula(@Valid @RequestBody CreateMoltenIronFormulaReq createMoltenIronFormulaReq) {
        techniqueService.createMoltenIronFormula(createMoltenIronFormulaReq);
    }

    /**
     * 铁水配方/删除铁水配方
     *
     * @param id
     */
    @DeleteMapping("/molten-iron-formula/{id}")
    public void deleteMoltenIronFormula(@PathVariable Long id) {
        techniqueService.deleteMoltenIronFormula(id);
    }

    /**
     * 铁水配方/搜索铁水配方
     *
     * @param searchSandFormulaReq
     * @return
     */
    @GetMapping("/molten-iron-formula/search")
    public PageResult<MoltenIronFormula> searchMoltenIronFormula(@Valid SearchMoltenIronFormulaReq searchSandFormulaReq) {
        return techniqueService.searchMoltenIronFormula(searchSandFormulaReq);
    }

    /**
     * 铁水配方/更新铁水配方
     *
     * @param id
     * @param createMoltenIronFormulaReq
     */
    @PutMapping("/molten-iron-formula/{id}")
    public void updateMoltenIronFormula(@PathVariable Long id,
                                        @Valid @RequestBody CreateMoltenIronFormulaReq createMoltenIronFormulaReq) {
        techniqueService.updateMoltenIronFormula(id, createMoltenIronFormulaReq);
    }

    /**
     * 创建工艺
     */
    @PostMapping("/technique")
    public void createTechnique(@Valid @RequestBody CreateTechniqueReq createTechniqueReq) {
        techniqueService.createTechnique(createTechniqueReq);
    }

    /**
     * 删除工艺
     *
     * @param id
     */
    @DeleteMapping("/technique/{id}")
    public void deleteTechnique(@PathVariable Long id) {
        techniqueService.deleteTechnique(id);
    }

    /**
     * 更新工艺
     *
     * @param id
     * @param createTechniqueReq
     */
    @PutMapping("/technique/{id}")
    public void updateTechnique(@PathVariable Long id, @Valid @RequestBody CreateTechniqueReq createTechniqueReq) {
        techniqueService.updateTechnique(id, createTechniqueReq);
    }

    /**
     * 搜索工艺
     * 部分非必要字段不返回
     *
     * @param searchTechniqueReq
     * @return
     */
    @GetMapping("/technique/search")
    public PageResult<TechniquePageVO> searchTechnique(@Valid SearchTechniqueReq searchTechniqueReq) {
        return techniqueService.searchTechnique(searchTechniqueReq);
    }

    /**
     * 获取工艺详情
     */
    @GetMapping("/technique/{id}")
    public TechniqueVO getTechnique(@PathVariable Long id) {
        return techniqueService.getTechnique(id);
    }
}
