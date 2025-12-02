package cn.hex.ddp.manufacture.infrastructure.technique.managerimpl;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.technique.rest.req.SearchMoltenIronFormulaReq;
import cn.hex.ddp.manufacture.api.technique.rest.req.SearchSandFormulaReq;
import cn.hex.ddp.manufacture.api.technique.rest.req.SearchTechniqueReq;
import cn.hex.ddp.manufacture.api.technique.rest.vo.TechniqueSummaryVO;
import cn.hex.ddp.manufacture.domain.technique.manager.TechniqueManager;
import cn.hex.ddp.manufacture.domain.technique.model.MoltenIronFormula;
import cn.hex.ddp.manufacture.domain.technique.model.SandFormula;
import cn.hex.ddp.manufacture.domain.technique.model.Technique;
import cn.hex.ddp.manufacture.infrastructure.sandbox.persistence.po.SandboxGroupSandboxPO;
import cn.hex.ddp.manufacture.infrastructure.technique.managerimpl.converter.TechniqueInfrastructureConverter;
import cn.hex.ddp.manufacture.infrastructure.technique.persistence.po.MoltenIronFormulaPO;
import cn.hex.ddp.manufacture.infrastructure.technique.persistence.po.SandFormulaPO;
import cn.hex.ddp.manufacture.infrastructure.technique.persistence.po.TechniquePO;
import cn.hex.ddp.manufacture.infrastructure.technique.persistence.postgresql.repository.MoltenIronFormulaPORepository;
import cn.hex.ddp.manufacture.infrastructure.technique.persistence.postgresql.repository.SandFormulaPORepository;
import cn.hex.ddp.manufacture.infrastructure.technique.persistence.postgresql.repository.TechniquePORepository;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class TechniqueManagerImpl implements TechniqueManager {
    @Autowired
    private TechniquePORepository techniquePORepository;
    @Autowired
    private MoltenIronFormulaPORepository moltenIronFormulaPORepository;
    @Autowired
    private SandFormulaPORepository sandFormulaPORepository;
    @Autowired
    private TechniqueInfrastructureConverter converter;

    @Override
    public void createSandFormula(SandFormula sandFormula) {
        SandFormulaPO sandFormulaPO = converter.toSandFormulaPO(sandFormula);
        sandFormulaPORepository.save(sandFormulaPO);
    }

    @Override
    public void deleteSandFormula(Long id) {
        sandFormulaPORepository.removeById(id);
    }

    @Override
    public SandFormula getSandFormula(Long id) {
        SandFormulaPO sandFormulaPO = sandFormulaPORepository.getById(id);
        return converter.toSandFormula(sandFormulaPO);
    }

    @Override
    public SandFormula getSandFormulaByModel(String model) {
        SandFormulaPO sandFormulaPO = sandFormulaPORepository.getOne(
                new LambdaQueryWrapper<SandFormulaPO>().eq(SandFormulaPO::getModel, model)
        );
        return converter.toSandFormula(sandFormulaPO);
    }

    @Override
    public PageResult<SandFormula> searchSandFormula(SearchSandFormulaReq searchSandFormulaReq) {
        Page<SandFormulaPO> page = sandFormulaPORepository.page(
                searchSandFormulaReq.toPage(),
                new LambdaQueryWrapper<SandFormulaPO>()
                        .like(StrUtil.isNotEmpty(searchSandFormulaReq.getSearch()), SandFormulaPO::getName,
                                searchSandFormulaReq.getSearch())
                        .or()
                        .like(StrUtil.isNotEmpty(searchSandFormulaReq.getSearch()), SandFormulaPO::getModel,
                                searchSandFormulaReq.getSearch())
                        .orderByDesc(SandFormulaPO::getId)

        );
        return PageResult.of(page, converter::toSandFormula);
    }

    @Override
    public void updateSandFormula(SandFormula sandFormula) {
        SandFormulaPO sandFormulaPO = converter.toSandFormulaPO(sandFormula);
        sandFormulaPORepository.updateById(sandFormulaPO);
    }

    @Override
    public void createMoltenIronFormula(MoltenIronFormula moltenIronFormula) {
        MoltenIronFormulaPO moltenIronFormulaPO = converter.toMoltenIronFormulaPO(moltenIronFormula);
        moltenIronFormulaPORepository.save(moltenIronFormulaPO);
    }

    @Override
    public void deleteMoltenIronFormula(Long id) {
        moltenIronFormulaPORepository.removeById(id);

    }

    @Override
    public MoltenIronFormula getMoltenIronFormula(Long id) {
        MoltenIronFormulaPO moltenIronFormulaPO = moltenIronFormulaPORepository.getById(id);
        return converter.toMoltenIronFormula(moltenIronFormulaPO);
    }

    @Override
    public MoltenIronFormula getMoltenIronFormulaByModel(String model) {
        MoltenIronFormulaPO moltenIronFormulaPO = moltenIronFormulaPORepository.getOne(
                new LambdaQueryWrapper<MoltenIronFormulaPO>().eq(MoltenIronFormulaPO::getModel, model)
        );
        return converter.toMoltenIronFormula(moltenIronFormulaPO);
    }

    @Override
    public PageResult<MoltenIronFormula> searchMoltenIronFormula(SearchMoltenIronFormulaReq searchSandFormulaReq) {
        Page<MoltenIronFormulaPO> page = moltenIronFormulaPORepository.page(
                searchSandFormulaReq.toPage(),
                new LambdaQueryWrapper<MoltenIronFormulaPO>()
                        .like(StrUtil.isNotEmpty(searchSandFormulaReq.getSearch()), MoltenIronFormulaPO::getName,
                                searchSandFormulaReq.getSearch())
                        .or()
                        .like(StrUtil.isNotEmpty(searchSandFormulaReq.getSearch()), MoltenIronFormulaPO::getModel,
                                searchSandFormulaReq.getSearch())
                        .orderByDesc(MoltenIronFormulaPO::getId)

        );
        return PageResult.of(page, converter::toMoltenIronFormula);
    }

    @Override
    public void updateMoltenIronFormula(MoltenIronFormula moltenIronFormula) {
        MoltenIronFormulaPO moltenIronFormulaPO = converter.toMoltenIronFormulaPO(moltenIronFormula);
        moltenIronFormulaPORepository.updateById(moltenIronFormulaPO);
    }

    @Override
    public void deleteTechnique(Long id) {
        techniquePORepository.removeById(id);
    }

    @Override
    public void createTechnique(Technique technique) {
        techniquePORepository.save(converter.toTechniquePO(technique));
    }

    @Override
    public void updateTechnique(Technique technique) {
        techniquePORepository.updateById(converter.toTechniquePO(technique));
    }

    @Override
    public PageResult<Technique> searchTechnique(SearchTechniqueReq searchTechniqueReq) {
        Page<TechniquePO> page = techniquePORepository.page(
                searchTechniqueReq.toPage(),
                new LambdaQueryWrapper<TechniquePO>()
                        .like(StrUtil.isNotEmpty(searchTechniqueReq.getSearch()), TechniquePO::getName,
                                searchTechniqueReq.getSearch())
                        .or()
                        .like(StrUtil.isNotEmpty(searchTechniqueReq.getSearch()), TechniquePO::getTechniqueNo,
                                searchTechniqueReq.getSearch())
                        .eq(ObjectUtil.isNotNull(searchTechniqueReq.getSandType()),
                                TechniquePO::getSandType, searchTechniqueReq.getSandType())
                        .eq(ObjectUtil.isNotNull(searchTechniqueReq.getSandLineType()),
                                TechniquePO::getSandLineType, searchTechniqueReq.getSandLineType())
                        .orderByDesc(TechniquePO::getId)
        );
        if (CollUtil.isEmpty(page.getRecords())) {
            return PageResult.emptyResult();
        }
        List<Long> sandFormulaIds = page.getRecords().stream().map(TechniquePO::getSandFormulaId).toList();
        Map<Long, SandFormulaPO> sandFormulaPOMap =
                sandFormulaPORepository.listByIds(sandFormulaIds).stream().collect(Collectors.toMap(
                        SandFormulaPO::getId, Function.identity(), (a, b) -> a
                ));
        List<Long> moltenIronFormulaIds = page.getRecords().stream().map(TechniquePO::getMoltenIronFormulaId).toList();
        Map<Long, MoltenIronFormulaPO> moltenIronFormulaPOMap =
                moltenIronFormulaPORepository.listByIds(moltenIronFormulaIds).stream().collect(Collectors.toMap(
                        MoltenIronFormulaPO::getId, Function.identity(), (a, b) -> a
                ));

        return PageResult.of(page, converter.toTechnique(page.getRecords(), sandFormulaPOMap, moltenIronFormulaPOMap));
    }

    @Override
    public Technique getTechnique(Long id) {
        TechniquePO techniquePO = techniquePORepository.getById(id);
        if (ObjectUtil.isNull(techniquePO)) {
            return null;
        }

        SandFormulaPO sandFormulaPO = sandFormulaPORepository.getById(techniquePO.getSandFormulaId());
        MoltenIronFormulaPO moltenIronFormulaPO = moltenIronFormulaPORepository.getById(techniquePO.getMoltenIronFormulaId());
        Technique technique = converter.toTechnique(techniquePO);
        technique.setSandFormula(converter.toSandFormula(sandFormulaPO));
        technique.setMoltenIronFormula(converter.toMoltenIronFormula(moltenIronFormulaPO));
        return technique;
    }

    @Override
    public Technique getTechniqueByNo(String techniqueNo) {
        TechniquePO techniquePO = techniquePORepository.getOne(
                new LambdaQueryWrapper<TechniquePO>().eq(TechniquePO::getTechniqueNo, techniqueNo)
        );
        if (ObjectUtil.isNull(techniquePO)) {
            return null;
        }
        SandFormulaPO sandFormulaPO = sandFormulaPORepository.getById(techniquePO.getSandFormulaId());
        MoltenIronFormulaPO moltenIronFormulaPO = moltenIronFormulaPORepository.getById(techniquePO.getMoltenIronFormulaId());
        Technique technique = converter.toTechnique(techniquePO);
        technique.setSandFormula(converter.toSandFormula(sandFormulaPO));
        technique.setMoltenIronFormula(converter.toMoltenIronFormula(moltenIronFormulaPO));
        return technique;
    }

    @Override
    public List<Technique> getTechniquesBySandboxGroupId(Long sandboxGroupId) {
        List<TechniquePO> techniquePOList = techniquePORepository.lambdaQuery()
                .eq(ObjectUtils.isNotNull(sandboxGroupId), TechniquePO::getSandboxGroupId, sandboxGroupId).list();
        return converter.toTechniqueList(techniquePOList);
    }

    @Override
    public List<Technique> getTechniquesByMoldGroupId(Long moldGroupId) {
        List<TechniquePO> techniquePOList = techniquePORepository.lambdaQuery()
                .eq(ObjectUtils.isNotNull(moldGroupId), TechniquePO::getSandboxMoldGroupId, moldGroupId).list();
        return converter.toTechniqueList(techniquePOList);
    }

    @Override
    public List<Technique> getTechniquesBySandFormulaId(Long sandFormulaId) {
        List<TechniquePO> techniquePOList = techniquePORepository.lambdaQuery()
                .eq(ObjectUtils.isNotNull(sandFormulaId), TechniquePO::getSandFormulaId, sandFormulaId).list();
        return converter.toTechniqueList(techniquePOList);
    }

    @Override
    public List<Technique> getTechniquesByMoltenIronFormulaId(Long moltenIronFormulaId) {
        List<TechniquePO> techniquePOList = techniquePORepository.lambdaQuery()
                .eq(ObjectUtils.isNotNull(moltenIronFormulaId), TechniquePO::getMoltenIronFormulaId, moltenIronFormulaId).list();
        return converter.toTechniqueList(techniquePOList);
    }


    @Override
    public Map<Long, TechniqueSummaryVO> getTechniqueSummaryVOMap(List<Long> techniqueIds) {
        if (CollectionUtils.isEmpty(techniqueIds)) {
            return Map.of();
        }

        List<TechniquePO> techniquePOList = techniquePORepository.listByIds(techniqueIds);
        List<TechniqueSummaryVO> techniqueSummaryVOList = converter.toTechniqueSummaryVOList(techniquePOList);
        return techniqueSummaryVOList.stream().collect(Collectors.toMap(
                TechniqueSummaryVO::getId, Function.identity(), (a, b) -> a
        ));
    }
}
