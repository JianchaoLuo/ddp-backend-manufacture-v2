package cn.hex.ddp.manufacture.infrastructure.mold.managerimpl;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.mold.rest.req.*;
import cn.hex.ddp.manufacture.api.mold.rest.vo.MoldGroupSummaryVO;
import cn.hex.ddp.manufacture.application.mold.dto.out.MoldGroupSummaryDTO;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.domain.mold.manager.MoldManager;
import cn.hex.ddp.manufacture.domain.mold.model.Mold;
import cn.hex.ddp.manufacture.domain.mold.model.MoldGroup;
import cn.hex.ddp.manufacture.domain.mold.model.MoldGroupMold;
import cn.hex.ddp.manufacture.domain.mold.model.MoldModel;
import cn.hex.ddp.manufacture.infrastructure.mold.managerimpl.converter.MoldInfraConverter;
import cn.hex.ddp.manufacture.infrastructure.mold.persistence.po.MoldGroupMoldPO;
import cn.hex.ddp.manufacture.infrastructure.mold.persistence.po.MoldGroupPO;
import cn.hex.ddp.manufacture.infrastructure.mold.persistence.po.MoldModelPO;
import cn.hex.ddp.manufacture.infrastructure.mold.persistence.po.MoldPO;
import cn.hex.ddp.manufacture.infrastructure.mold.persistence.postgresql.repository.MoldGroupMoldPORepository;
import cn.hex.ddp.manufacture.infrastructure.mold.persistence.postgresql.repository.MoldGroupPORepository;
import cn.hex.ddp.manufacture.infrastructure.mold.persistence.postgresql.repository.MoldModelPORepository;
import cn.hex.ddp.manufacture.infrastructure.mold.persistence.postgresql.repository.MoldPORepository;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangzc.mpe.bind.Binder;
import com.tangzc.mpe.bind.Deeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 模具管理实现类
 *
 * @author Huhaisen
 * @date 2024/05/21
 */
@Repository
public class MoldManagerImpl implements MoldManager {

    @Autowired
    private MoldModelPORepository moldModelRepository;

    @Autowired
    private MoldPORepository moldRepository;

    @Autowired
    private MoldGroupPORepository moldGroupRepository;

    @Autowired
    private MoldGroupMoldPORepository moldGroupMoldRepository;

    @Autowired
    private MoldInfraConverter moldConverter;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMoldModel(CreateMoldModelReq createMoldModelReq) {
        moldModelRepository.save(moldConverter.toMoldModelPO(createMoldModelReq));
    }

    @Override
    public PageResult<MoldModel> getMoldModelPage(GetMoldModelPageReq getMoldModelPageReq) {
        Page<MoldModelPO> moldModelPOPage = moldModelRepository.page(
                getMoldModelPageReq.toPage(),
                Wrappers.lambdaQuery(MoldModelPO.class)
                        .like(StringUtils.isNotEmpty(getMoldModelPageReq.getModelName()), MoldModelPO::getModelName,
                                getMoldModelPageReq.getModelName())
                        .eq(ObjectUtils.isNotNull(getMoldModelPageReq.getType()), MoldModelPO::getType,
                                getMoldModelPageReq.getType())
                        .orderByDesc(MoldModelPO::getId)
        );
        if (moldModelPOPage.getRecords().isEmpty()) {
            return PageResult.emptyResult();
        }
        return PageResult.of(moldModelPOPage, moldConverter.toMoldModelList(moldModelPOPage.getRecords()));
    }

    @Override
    public MoldModel getMoldModelById(Long id) {
        MoldModelPO moldModelPO = moldModelRepository.getById(id);
        return moldConverter.toMoldModel(moldModelPO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMoldModel(Long id, UpdateMoldModelReq updateMoldModelReq) {
        moldModelRepository.updateById(moldConverter.toMoldModelPO(id, updateMoldModelReq));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMoldModelById(Long id) {
        moldModelRepository.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMold(CreateMoldReq createMoldReq) {
        moldRepository.save(moldConverter.toMoldPO(createMoldReq));
    }

    @Override
    public PageResult<Mold> getMoldPage(GetMoldPageReq getMoldPageReq) {
        // 连表查询的方式，获得模具分页数据，以及对应的模具型号信息
        Page<MoldPO> moldPOPage = moldRepository.lambdaQueryPlus()
                .like(StringUtils.isNotEmpty(getMoldPageReq.getMoldNo()), MoldPO::getMoldNo, getMoldPageReq.getMoldNo())
                .like(StringUtils.isNotEmpty(getMoldPageReq.getName()), MoldPO::getName, getMoldPageReq.getName())
                .orderByDesc(MoldPO::getId)
                .bindPage(getMoldPageReq.toPage(), MoldPO::getMoldModel);
        if (moldPOPage.getRecords().isEmpty()) {
            return PageResult.emptyResult();
        }
        return PageResult.of(moldPOPage, moldConverter.toMoldList(moldPOPage.getRecords()));
    }

    @Override
    public Mold getMoldById(Long id) {
        MoldPO moldPO = moldRepository.getById(id);
        return moldConverter.toMold(moldPO);
    }

    @Override
    public Mold getMoldByNo(String moldNo) {
        MoldPO moldPO = moldRepository.lambdaQueryPlus()
                .eq(MoldPO::getMoldNo, moldNo)
                .one();
        return moldConverter.toMold(moldPO);
    }

    @Override
    public List<Mold> getMoldsByModelId(Long modelId) {
        List<MoldPO> moldPOS = moldRepository.lambdaQueryPlus()
                .eq(ObjectUtils.isNotNull(modelId), MoldPO::getMoldModelId, modelId)
                .bindList(MoldPO::getMoldModel);
        return moldConverter.toMoldList(moldPOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMoldById(Long id, UpdateMoldReq updateMoldReq) {
        moldRepository.updateById(moldConverter.toMoldPO(id, updateMoldReq));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMoldById(Long id) {
        moldRepository.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMoldGroup(CreateMoldGroupReq createMoldGroupReq) {
        MoldGroupPO moldGroupPO = moldConverter.toMoldGroupPO(createMoldGroupReq);
        List<Long> moldIds = createMoldGroupReq.getGroupItems().stream().map(AddMoldBatchToGroupReq::getMoldId).toList();
        // 计算模具组尺寸参数
        calculateGroupParameters(moldIds, moldGroupPO);
        moldGroupRepository.save(moldGroupPO);
        // 批量新增模具组与模具的关联关系
        List<MoldGroupMoldPO> moldGroupMoldPOList = createMoldGroupReq.getGroupItems().stream()
                .map(req -> moldConverter.toMoldGroupMoldPO(moldGroupPO.getId(), req))
                .collect(Collectors.toList());
        moldGroupMoldRepository.saveBatch(moldGroupMoldPOList);
    }

    @Override
    public MoldGroup getMoldGroupById(Long id) {
        MoldGroupPO moldGroupPO = moldGroupRepository.getById(id);
        return moldConverter.toMoldGroup(moldGroupPO);
    }

    @Override
    public List<MoldGroupMold> getMoldGroupMoldsByMoldId(Long moldId) {
        List<MoldGroupMoldPO> moldGroupMoldPOs = moldGroupMoldRepository.lambdaQueryPlus()
                .eq(ObjectUtils.isNotNull(moldId), MoldGroupMoldPO::getMoldId, moldId)
                .bindList(MoldGroupMoldPO::getMold);
        return moldConverter.toMoldGroupMoldList(moldGroupMoldPOs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMoldGroup(Long id, UpdateMoldGroupReq updateMoldGroupReq) {
        MoldGroupPO moldGroupPO = moldConverter.toMoldGroupPO(id, updateMoldGroupReq);
        // 如果模具组中的模具有变动，则重新计算模具组尺寸参数并且维护模具组与模具关联信息
        if (CollectionUtils.isNotEmpty(updateMoldGroupReq.getGroupItems())) {
            List<Long> moldIds = updateMoldGroupReq.getGroupItems().stream().map(AddMoldBatchToGroupReq::getMoldId)
                    .toList();
            // 计算模具组尺寸参数
            calculateGroupParameters(moldIds, moldGroupPO);
            // 先删除原有关联关系
            moldGroupMoldRepository.remove(
                    Wrappers.lambdaQuery(MoldGroupMoldPO.class)
                           .eq(MoldGroupMoldPO::getMoldGroupId, id)
            );
            // 批量新增关联关系
            List<MoldGroupMoldPO> moldGroupMoldPOList = updateMoldGroupReq.getGroupItems().stream()
                    .map(req -> moldConverter.toMoldGroupMoldPO(id, req))
                    .collect(Collectors.toList());
            moldGroupMoldRepository.saveBatch(moldGroupMoldPOList);
        }
        moldGroupRepository.updateById(moldGroupPO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMoldToGroup(Long groupId, Long moldId, AddMoldToGroupReq addMoldToGroupReq) {
        MoldGroupMoldPO moldGroupMoldPO = moldConverter.toMoldGroupMoldPO(groupId, moldId, addMoldToGroupReq);
        moldGroupMoldRepository.save(moldGroupMoldPO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMoldFromGroup(Long groupId, Long moldId) {
        moldGroupMoldRepository.remove(
                Wrappers.lambdaQuery(MoldGroupMoldPO.class)
                       .eq(MoldGroupMoldPO::getMoldGroupId, groupId)
                       .eq(MoldGroupMoldPO::getMoldId, moldId)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMoldGroupById(Long id) {
        // 删除模具组信息
        moldGroupRepository.removeById(id);
        // 删除模具组与模具的关联信息
        moldGroupMoldRepository.remove(
                Wrappers.lambdaQuery(MoldGroupMoldPO.class)
                       .eq(MoldGroupMoldPO::getMoldGroupId, id)
        );
    }

    @Override
    public PageResult<MoldGroupSummaryDTO> getMoldGroupPage(GetMoldGroupPageReq getMoldGroupPageReq) {
        Page<MoldGroupPO> moldGroupPOPage = moldGroupRepository.lambdaQueryPlus()
                .like(StringUtils.isNotEmpty(getMoldGroupPageReq.getName()), MoldGroupPO::getName,
                        getMoldGroupPageReq.getName())
                .eq(ObjectUtils.isNotNull(getMoldGroupPageReq.getType()), MoldGroupPO::getType,
                        getMoldGroupPageReq.getType())
                .orderByDesc(MoldGroupPO::getId)
                .bindPage(getMoldGroupPageReq.toPage(), MoldGroupPO::getMoldGroupMoldList);
        if (moldGroupPOPage.getRecords().isEmpty()) {
            return PageResult.emptyResult();
        }
        return PageResult.of(moldGroupPOPage, moldConverter::toMoldGroupSummaryDTOList);
    }

    @Override
    public MoldGroup getMoldGroupDetailById(Long id) {
        // 连表查询模具组信息，以及模具组与模具的关联信息
        MoldGroupPO moldGroupPO = moldGroupRepository.lambdaQueryPlus()
                .eq(MoldGroupPO::getId, id)
                .bindOne(MoldGroupPO::getMoldGroupMoldList);
        // 关联查询模具信息
        Binder.bindOn(Deeper.with(moldGroupPO).inList(MoldGroupPO::getMoldGroupMoldList), MoldGroupMoldPO::getMold);
        // 关联查询模具型号信息
        Binder.bindOn(Deeper.with(moldGroupPO).inList(MoldGroupPO::getMoldGroupMoldList).in(MoldGroupMoldPO::getMold),
                MoldPO::getMoldModel);
        return moldConverter.toMoldGroup(moldGroupPO);
    }

    @Override
    public MoldGroupMold getMoldGroupMold(Long groupId, Long moldId) {
        MoldGroupMoldPO moldGroupMoldPO = moldGroupMoldRepository.lambdaQuery()
                .eq(MoldGroupMoldPO::getMoldGroupId, groupId)
                .eq(MoldGroupMoldPO::getMoldId, moldId)
                .one();
        return moldConverter.toMoldGroupMold(moldGroupMoldPO);
    }

    @Override
    public Map<Long, MoldGroupSummaryVO> getMoldGroupSummaryVOMap(List<Long> sandboxMoldGroupIds) {
        if (CollectionUtils.isEmpty(sandboxMoldGroupIds)) {
            return Map.of();
        }

        List<MoldGroupPO> moldGroupPOS = moldGroupRepository.listByIds(sandboxMoldGroupIds);
        return moldGroupPOS.stream().collect(Collectors.toMap(
                MoldGroupPO::getId, moldConverter::toMoldGroupSummaryVO, (v1, v2) -> v1
        ));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdateMoldBatchToGroup(Long groupId, List<AddMoldBatchToGroupReq> reqList) {
        // 先删除原有关联关系
        moldGroupMoldRepository.remove(
                Wrappers.lambdaQuery(MoldGroupMoldPO.class)
                       .eq(MoldGroupMoldPO::getMoldGroupId, groupId)
        );
        // 批量新增关联关系
        List<MoldGroupMoldPO> moldGroupMoldPOList = reqList.stream()
               .map(req -> moldConverter.toMoldGroupMoldPO(groupId, req))
               .collect(Collectors.toList());
        moldGroupMoldRepository.saveBatch(moldGroupMoldPOList);
    }

    /**
     * 计算模具组参数
     * 原理：根据当前模具组中有哪些模具，分别计算模具组的总层数、长度、宽度、高度、重量。具体计算规则如下：
     * 1. 模具组的总层数为模具组中模具的数量，即moldIds的大小。
     * 2. 模具组的长度为模具组中模具的最大长度。
     * 3. 模具组的宽度为模具组中模具的最大宽度。
     * 4. 模具组的高度为所有模具的高度之和。
     * 5. 模具组的重量为所有模具的重量之和。
     */
    private void calculateGroupParameters(List<Long> moldIds, MoldGroupPO moldGroupPO) {
        // 查询模具信息，并过滤目标模具
        List<MoldPO> moldPOS = moldRepository.lambdaQueryPlus()
                .in(MoldPO::getId, moldIds) // 仅查询目标模具
                .bindList(MoldPO::getMoldModel);

        // 校验模具列表是否为空
        if (CollectionUtils.isEmpty(moldPOS)) {
            throw new BusinessException("目标模具数据为空，无法计算尺寸");
        }

        // 计算模具组尺寸参数
        List<Float> lengths = moldPOS.stream()
                .map(moldPO -> Optional.ofNullable(moldPO.getMoldModel().getLength()).orElse(0F))
                .toList();
        List<Float> widths = moldPOS.stream()
                .map(moldPO -> Optional.ofNullable(moldPO.getMoldModel().getWidth()).orElse(0F))
                .toList();
        List<Float> heights = moldPOS.stream()
                .map(moldPO -> Optional.ofNullable(moldPO.getMoldModel().getHeight()).orElse(0F))
                .toList();
        List<Float> weights = moldPOS.stream()
                .map(moldPO -> Optional.ofNullable(moldPO.getMoldModel().getWeight()).orElse(0F))
                .toList();

        // 更新模具组信息
        moldGroupPO.setLayerCount(moldIds.size());
        moldGroupPO.setLength(Collections.max(lengths));
        moldGroupPO.setWidth(Collections.max(widths));
        moldGroupPO.setHeight(heights.stream().reduce(Float::sum).orElse(0F)); // 叠加高度
        moldGroupPO.setWeight(weights.stream().reduce(Float::sum).orElse(0F)); // 总重量
    }
}
