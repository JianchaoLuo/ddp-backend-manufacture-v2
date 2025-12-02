package cn.hex.ddp.manufacture.application.technique.service.impl;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.mold.rest.vo.MoldGroupSummaryVO;
import cn.hex.ddp.manufacture.api.sandbox.rest.vo.SandboxGroupSummaryVO;
import cn.hex.ddp.manufacture.api.technique.rest.req.*;
import cn.hex.ddp.manufacture.api.technique.rest.vo.TechniquePageVO;
import cn.hex.ddp.manufacture.api.technique.rest.vo.TechniqueVO;
import cn.hex.ddp.manufacture.application.technique.converter.TechniqueServiceConverter;
import cn.hex.ddp.manufacture.application.technique.service.TechniqueService;
import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.domain.common.exception.NotFoundException;
import cn.hex.ddp.manufacture.domain.mold.manager.MoldManager;
import cn.hex.ddp.manufacture.domain.product.manager.ProductManager;
import cn.hex.ddp.manufacture.domain.sandbox.manager.SandboxManager;
import cn.hex.ddp.manufacture.domain.technique.manager.TechniqueManager;
import cn.hex.ddp.manufacture.domain.technique.model.MoltenIronFormula;
import cn.hex.ddp.manufacture.domain.technique.model.SandFormula;
import cn.hex.ddp.manufacture.domain.technique.model.Technique;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class TechniqueServiceImpl implements TechniqueService {
    @Autowired
    private TechniqueManager techniqueManager;

    @Autowired
    private SandboxManager sandboxManager;

    @Autowired
    private MoldManager moldManager;

    @Autowired
    private ProductManager productManager;

    @Autowired
    private TechniqueServiceConverter converter;

    @Override
    public void createSandFormula(CreateSandFormulaReq request) {
        Optional.ofNullable(techniqueManager.getSandFormulaByModel(request.getModel())).ifPresent(sandFormula -> {
            log.warn("[TechniqueServiceImpl.createSandFormula] 砂配方型号已存在, model: {}", request.getModel());
            throw new BusinessException(BssExType.SAND_FORMULA_MODEL_REPEAT, request.getModel());
        });
        SandFormula sandFormula = converter.toSandFormula(request);
        techniqueManager.createSandFormula(sandFormula);
    }

    @Override
    public void deleteSandFormula(Long id) {
        // todo 判断是否还有工艺使用了这个砂配方
        if (CollectionUtils.isNotEmpty(techniqueManager.getTechniquesBySandFormulaId(id))) {
            log.warn("[TechniqueServiceImpl.deleteSandFormula] 砂配方被工艺占用，id={}", id);
            throw new BusinessException(BssExType.SAND_FORMULA_BE_OCCUPIED);
        }
        techniqueManager.deleteSandFormula(id);
    }

    @Override
    public PageResult<SandFormula> searchSandFormula(SearchSandFormulaReq searchSandFormulaReq) {
        return techniqueManager.searchSandFormula(searchSandFormulaReq);
    }

    @Override
    public void updateSandFormula(Long id, CreateSandFormulaReq request) {
        Optional.ofNullable(techniqueManager.getSandFormulaByModel(request.getModel())).ifPresent(sandFormula -> {
            if (!sandFormula.getId().equals(id)) {
                log.warn("[TechniqueServiceImpl.updateSandFormula] 砂配方型号已存在, model: {}", request.getModel());
                throw new BusinessException(BssExType.SAND_FORMULA_MODEL_REPEAT, request.getModel());
            }
        });
        SandFormula sandFormula = converter.toSandFormula(request);
        sandFormula.setId(id);
        techniqueManager.updateSandFormula(sandFormula);
    }

    @Override
    public void createMoltenIronFormula(CreateMoltenIronFormulaReq createMoltenIronFormulaReq) {
        Optional.ofNullable(techniqueManager.getMoltenIronFormulaByModel(createMoltenIronFormulaReq.getModel()))
                .ifPresent(moltenIronFormula -> {
            log.warn("[TechniqueServiceImpl.createMoltenIronFormula] 铁水配方型号已存在, model: {}",
                    createMoltenIronFormulaReq.getModel());
            throw new BusinessException(BssExType.MOLTEN_IRON_FORMULA_MODEL_REPEAT,
                    createMoltenIronFormulaReq.getModel());
        });
        MoltenIronFormula moltenIronFormula = converter.toMoltenIronFormula(createMoltenIronFormulaReq);
        techniqueManager.createMoltenIronFormula(moltenIronFormula);
    }

    @Override
    public void deleteMoltenIronFormula(Long id) {
        // todo 判断是否还有工艺使用了这个铁水配方
        if (CollectionUtils.isNotEmpty(techniqueManager.getTechniquesByMoltenIronFormulaId(id))) {
            log.warn("[TechniqueServiceImpl.deleteMoltenIronFormula] 铁水配方被工艺占用，id={}", id);
            throw new BusinessException(BssExType.MOLTEN_IRON_FORMULA_BE_OCCUPIED);
        }

        techniqueManager.deleteMoltenIronFormula(id);
    }

    @Override
    public PageResult<MoltenIronFormula> searchMoltenIronFormula(SearchMoltenIronFormulaReq searchSandFormulaReq) {
        return techniqueManager.searchMoltenIronFormula(searchSandFormulaReq);
    }

    @Override
    public void updateMoltenIronFormula(Long id, CreateMoltenIronFormulaReq createMoltenIronFormulaReq) {
        Optional.ofNullable(techniqueManager.getMoltenIronFormulaByModel(createMoltenIronFormulaReq.getModel()))
                .ifPresent(moltenIronFormula -> {
            if (!moltenIronFormula.getId().equals(id)) {
                log.warn("[TechniqueServiceImpl.updateMoltenIronFormula] 铁水配方型号已存在, model: {}",
                        createMoltenIronFormulaReq.getModel());
                throw new BusinessException(BssExType.MOLTEN_IRON_FORMULA_MODEL_REPEAT,
                        createMoltenIronFormulaReq.getModel());
            }
        });
        MoltenIronFormula moltenIronFormula = converter.toMoltenIronFormula(createMoltenIronFormulaReq);
        moltenIronFormula.setId(id);
        techniqueManager.updateMoltenIronFormula(moltenIronFormula);
    }

    @Override
    public void createTechnique(CreateTechniqueReq createTechniqueReq) {
        Optional.ofNullable(techniqueManager.getTechniqueByNo(createTechniqueReq.getTechniqueNo())).ifPresent(
                technique -> {
            log.warn("[TechniqueServiceImpl.createTechnique] 工艺编号已存在, techniqueNo: {}",
                    createTechniqueReq.getTechniqueNo());
            throw new BusinessException(BssExType.TECHNIQUE_NO_REPEAT, createTechniqueReq.getTechniqueNo());
        });
        Technique technique = converter.toTechnique(createTechniqueReq);
        // 校验关联的对象是否都存在
        validateRelatedObject(createTechniqueReq);
        techniqueManager.createTechnique(technique);
    }

    @Override
    public void deleteTechnique(Long id) {
        Technique technique = techniqueManager.getTechnique(id);
        if (technique == null) {
            throw new BusinessException(BssExType.TECHNIQUE_NOT_FOUND);
        }
        if(CollectionUtils.isNotEmpty(productManager.getProductsByTechniqueId(id))){
            log.warn("[TechniqueServiceImpl.deleteTechnique] 工艺被占用");
            throw new BusinessException(BssExType.TECHNIQUE_BE_OCCUPIED);
        }

        techniqueManager.deleteTechnique(id);
    }

    @Override
    public void updateTechnique(Long id, CreateTechniqueReq createTechniqueReq) {
        Optional.ofNullable(techniqueManager.getTechniqueByNo(createTechniqueReq.getTechniqueNo())).ifPresent(
                technique -> {
            if (!technique.getId().equals(id)) {
                log.warn("[TechniqueServiceImpl.updateTechnique] 工艺编号已存在, techniqueNo: {}",
                        createTechniqueReq.getTechniqueNo());
                throw new BusinessException(BssExType.TECHNIQUE_NO_REPEAT, createTechniqueReq.getTechniqueNo());
            }
        });
        Technique technique = converter.toTechnique(createTechniqueReq);
        technique.setId(id);
        // 校验关联的对象是否都存在
        validateRelatedObject(createTechniqueReq);
        techniqueManager.updateTechnique(technique);
    }

    @Override
    public PageResult<TechniquePageVO> searchTechnique(SearchTechniqueReq searchTechniqueReq) {
        PageResult<Technique> techniquePageResult = techniqueManager.searchTechnique(searchTechniqueReq);
        // 获得砂箱组对应Map
        List<Long> sandboxGroupIds = techniquePageResult.getList().stream()
                .map(Technique::getSandboxGroupId).toList();
        Map<Long, SandboxGroupSummaryVO> sandboxGroupSummaryVOMap = sandboxManager.getSandboxGroupSummaryVOMap(
                sandboxGroupIds);
        // 获得砂箱模具组对应Map
        List<Long> sandboxMoldGroupIds = techniquePageResult.getList().stream()
                .map(Technique::getSandboxMoldGroupId).toList();
        Map<Long, MoldGroupSummaryVO> sandboxMoldGroupSummaryVOMap = moldManager.getMoldGroupSummaryVOMap(
                sandboxMoldGroupIds);
        // 获得砂芯模具组对应Map
        List<Long> sandCoreMoldGroupIds = techniquePageResult.getList().stream()
                .map(Technique::getSandCoreMoldGroupId).toList();
        Map<Long, MoldGroupSummaryVO> sandCoreMoldGroupSummaryVOMap = moldManager.getMoldGroupSummaryVOMap(
                sandCoreMoldGroupIds);

        return PageResult.of(techniquePageResult, converter.toTechniquePageVOList(techniquePageResult.getList(),
                sandboxGroupSummaryVOMap, sandboxMoldGroupSummaryVOMap, sandCoreMoldGroupSummaryVOMap));
    }

    @Override
    public TechniqueVO getTechnique(Long id) {
        Technique technique = techniqueManager.getTechnique(id);
        TechniqueVO techniqueVO = converter.toTechniqueVO(technique);
        // 组装 砂箱组 砂箱模具组 砂芯模具组
        techniqueVO.setSandboxGroup(sandboxManager.getSandboxGroupById(technique.getSandboxGroupId()));
        techniqueVO.setSandboxMoldGroup(moldManager.getMoldGroupDetailById(technique.getSandboxMoldGroupId()));
        techniqueVO.setSandCoreMoldGroup(moldManager.getMoldGroupDetailById(technique.getSandCoreMoldGroupId()));
        return techniqueVO;
    }

    /**
     * 校验工艺中关联的对象是否都存在
     */
    private void validateRelatedObject(CreateTechniqueReq createTechniqueReq) {
        Optional.ofNullable(sandboxManager.getSandboxGroupById(createTechniqueReq.getSandboxGroupId())).orElseThrow(
                () -> new NotFoundException("砂箱组不存在, id: " + createTechniqueReq.getSandboxGroupId()));
        Optional.ofNullable(moldManager.getMoldGroupById(createTechniqueReq.getSandboxMoldGroupId())).orElseThrow(
                () -> new NotFoundException("砂箱模具组不存在, id: " + createTechniqueReq.getSandboxMoldGroupId()));
        Optional.ofNullable(moldManager.getMoldGroupById(createTechniqueReq.getSandCoreMoldGroupId())).orElseThrow(
                () -> new NotFoundException("砂芯模具组不存在, id: " + createTechniqueReq.getSandCoreMoldGroupId()));
        Optional.ofNullable(techniqueManager.getSandFormula(createTechniqueReq.getSandFormulaId())).orElseThrow(
                () -> new NotFoundException("砂配方不存在, id: " + createTechniqueReq.getSandFormulaId()));
        Optional.ofNullable(techniqueManager.getMoltenIronFormula(createTechniqueReq.getMoltenIronFormulaId())).orElseThrow(
                () -> new NotFoundException("铁水配方不存在, id: " + createTechniqueReq.getMoltenIronFormulaId()));
    }
}
