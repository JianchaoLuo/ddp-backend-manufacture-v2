package cn.hex.ddp.manufacture.infrastructure.sandbox.managerimpl;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.sandbox.rest.req.*;
import cn.hex.ddp.manufacture.api.sandbox.rest.vo.SandboxGroupSummaryVO;
import cn.hex.ddp.manufacture.domain.sandbox.manager.SandboxManager;
import cn.hex.ddp.manufacture.domain.sandbox.model.Sandbox;
import cn.hex.ddp.manufacture.domain.sandbox.model.SandboxGroup;
import cn.hex.ddp.manufacture.domain.sandbox.model.SandboxGroupSandbox;
import cn.hex.ddp.manufacture.domain.sandbox.model.SandboxModel;
import cn.hex.ddp.manufacture.infrastructure.car.persistence.po.CarPO;
import cn.hex.ddp.manufacture.infrastructure.sandbox.managerimpl.converter.SandboxInfraConverter;
import cn.hex.ddp.manufacture.infrastructure.sandbox.persistence.po.SandboxGroupPO;
import cn.hex.ddp.manufacture.infrastructure.sandbox.persistence.po.SandboxGroupSandboxPO;
import cn.hex.ddp.manufacture.infrastructure.sandbox.persistence.po.SandboxModelPO;
import cn.hex.ddp.manufacture.infrastructure.sandbox.persistence.po.SandboxPO;
import cn.hex.ddp.manufacture.infrastructure.sandbox.persistence.postgresql.repository.SandboxGroupPORepository;
import cn.hex.ddp.manufacture.infrastructure.sandbox.persistence.postgresql.repository.SandboxGroupSandboxPORepository;
import cn.hex.ddp.manufacture.infrastructure.sandbox.persistence.postgresql.repository.SandboxModelPORepository;
import cn.hex.ddp.manufacture.infrastructure.sandbox.persistence.postgresql.repository.SandboxPORepository;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 砂箱管理实现类
 *
 * @author Huhaisen
 * @date 2024/05/20
 */
@Repository
public class SandboxManagerImpl implements SandboxManager {

    @Autowired
    private SandboxPORepository sandboxRepository;

    @Autowired
    private SandboxModelPORepository sandboxModelRepository;

    @Autowired
    private SandboxGroupPORepository sandboxGroupRepository;

    @Autowired
    private SandboxGroupSandboxPORepository sandboxGroupSandboxRepository;

    @Autowired
    private SandboxInfraConverter sandboxConverter;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSandboxModel(CreateSandboxModelReq createSandboxModelReq) {
        sandboxModelRepository.save(sandboxConverter.toSandboxModelPO(createSandboxModelReq));
    }

    @Override
    public PageResult<SandboxModel> getSandboxModelPage(GetSandboxModelPageReq getSandboxModelPageReq) {
        Page<SandboxModelPO> sandboxModelPOPage = sandboxModelRepository.page(
                getSandboxModelPageReq.toPage(),
                Wrappers.lambdaQuery(SandboxModelPO.class)
                        .like(ObjectUtils.isNotEmpty(getSandboxModelPageReq.getModelName()), SandboxModelPO::getModelName, getSandboxModelPageReq.getModelName())
                        .eq(ObjectUtils.isNotNull(getSandboxModelPageReq.getType()), SandboxModelPO::getType, getSandboxModelPageReq.getType())
                        .orderByDesc(SandboxModelPO::getId)
        );
        if (sandboxModelPOPage.getRecords().isEmpty()) {
            return PageResult.emptyResult();
        }

        return PageResult.of(sandboxModelPOPage, sandboxConverter.toSandboxModelList(sandboxModelPOPage.getRecords()));
    }

    @Override
    public SandboxModel getSandboxModelById(Long id) {
        SandboxModelPO sandboxModelPO = sandboxModelRepository.getById(id);
        return sandboxConverter.toSandboxModel(sandboxModelPO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSandboxModel(Long id, UpdateSandboxModelReq updateSandboxModelReq) {
        sandboxModelRepository.updateById(sandboxConverter.toSandboxModelPO(id, updateSandboxModelReq));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSandboxModel(Long id) {
        sandboxModelRepository.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSandbox(CreateSandboxReq createSandboxReq) {
        sandboxRepository.save(sandboxConverter.toSandboxPO(createSandboxReq));
    }

    @Override
    public PageResult<Sandbox> getSandboxPage(GetSandboxPageReq getSandboxPageReq) {
        Page<SandboxPO> sandboxPOPage = sandboxRepository.page(
                getSandboxPageReq.toPage(),
                Wrappers.lambdaQuery(SandboxPO.class)
                        .like(StringUtils.isNotEmpty(getSandboxPageReq.getSandboxNo()), SandboxPO::getSandboxNo,
                                getSandboxPageReq.getSandboxNo())
                        .like(ObjectUtils.isNotEmpty(getSandboxPageReq.getName()), SandboxPO::getName,
                                getSandboxPageReq.getName())
                        .orderByDesc(SandboxPO::getId)
        );
        if (sandboxPOPage.getRecords().isEmpty()) {
            return PageResult.emptyResult();
        }
        // 获取砂箱型号Map
        List<Long> sandboxModelIds = sandboxPOPage.getRecords().stream().map(SandboxPO::getSandboxModelId).toList();
        Map<Long, SandboxModel> sandboxModelsMap = getSandboxModelsMap(sandboxModelIds);
        ArrayList<Sandbox> sandboxList = new ArrayList<>();
        // 组装返回数据, 并添加砂箱型号
        sandboxPOPage.getRecords().stream().forEach(sandboxPO -> {
            Sandbox sandBox = sandboxConverter.toSandBox(sandboxPO);
            sandBox.setSandboxModel(sandboxModelsMap.getOrDefault(sandboxPO.getSandboxModelId(), null));
            sandboxList.add(sandBox);
        });
        return PageResult.of(sandboxPOPage, sandboxList);
    }

    @Override
    public Sandbox getSandboxById(Long id) {
        return sandboxConverter.toSandBox(sandboxRepository.getById(id));
    }

    @Override
    public List<Sandbox> getSandboxsByModelId(Long modelId){
        List<SandboxPO> sandboxPOList = sandboxRepository.lambdaQueryPlus()
                .eq(ObjectUtils.isNotNull(modelId), SandboxPO::getSandboxModelId, modelId)
                .bindList(SandboxPO::getSandboxNo, SandboxPO::getName, SandboxPO::getId);
        return sandboxConverter.toSandboxList(sandboxPOList);
    }

    @Override
    public List<SandboxGroupSandbox> getSandboxGroupSandboxsBySandboxId(Long sandboxId) {
        List<SandboxGroupSandboxPO> sandboxGroupSandboxPOList = sandboxGroupSandboxRepository.lambdaQueryPlus()
                .eq(ObjectUtils.isNotNull(sandboxId), SandboxGroupSandboxPO::getSandboxId, sandboxId)
                .bindList(SandboxGroupSandboxPO::getSandboxGroupId, SandboxGroupSandboxPO::getLayer);
        return sandboxConverter.toSandboxGroupSandboxList(sandboxGroupSandboxPOList);
    }

    @Override
    public Sandbox getSandboxByNo(String sandboxNo) {
        SandboxPO sandboxPO = sandboxRepository.getOne(
                Wrappers.lambdaQuery(SandboxPO.class).eq(SandboxPO::getSandboxNo, sandboxNo));
        return sandboxConverter.toSandBox(sandboxPO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSandbox(Long id, UpdateSandboxReq updateSandboxReq) {
        sandboxRepository.updateById(sandboxConverter.toSandboxPO(id, updateSandboxReq));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSandbox(Long id) {
        sandboxRepository.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void
    createSandboxGroup(CreateSandboxGroupReq createSandboxGroupReq) {
        SandboxGroupPO sandboxGroupPO = sandboxConverter.toSandboxGroupPO(createSandboxGroupReq);
        List<Long> sandboxIds = createSandboxGroupReq.getGroupItems().stream()
                .map(AddSandboxBatchToGroupReq::getSandboxId).toList();
        // 计算砂箱组尺寸参数
        calculateGroupParameters(sandboxIds, sandboxGroupPO);
        sandboxGroupRepository.save(sandboxGroupPO);
        // 保存砂箱组与砂箱关联信息
        List<SandboxGroupSandboxPO> sandboxGroupSandboxPOs = createSandboxGroupReq.getGroupItems().stream()
                .map(item -> sandboxConverter.toSandboxGroupSandboxPO(sandboxGroupPO.getId(), item)).toList();
        sandboxGroupSandboxRepository.saveBatch(sandboxGroupSandboxPOs);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSandboxGroup(Long id, UpdateSandboxGroupReq updateSandboxGroupReq) {
        SandboxGroupPO sandboxGroupPO = sandboxConverter.toSandboxGroupPO(id, updateSandboxGroupReq);
        // 如果砂箱组中的砂箱有变动，则重新计算砂箱组尺寸参数并且维护砂箱组与砂箱关联信息
        if (CollectionUtils.isNotEmpty(updateSandboxGroupReq.getGroupItems())) {
            List<Long> sandboxIds = updateSandboxGroupReq.getGroupItems().stream()
                    .map(AddSandboxBatchToGroupReq::getSandboxId).toList();
            // 计算砂箱组尺寸参数
            calculateGroupParameters(sandboxIds, sandboxGroupPO);
            // 删除原有砂箱组与砂箱关联信息
            sandboxGroupSandboxRepository.remove(
                    Wrappers.lambdaQuery(SandboxGroupSandboxPO.class)
                            .eq(SandboxGroupSandboxPO::getSandboxGroupId, id)
            );
            // 保存新的砂箱组与砂箱关联信息
            List<SandboxGroupSandboxPO> sandboxGroupSandboxPOs = updateSandboxGroupReq.getGroupItems().stream()
                    .map(item -> sandboxConverter.toSandboxGroupSandboxPO(id, item)).toList();
            sandboxGroupSandboxRepository.saveBatch(sandboxGroupSandboxPOs);
        }
        sandboxGroupRepository.updateById(sandboxGroupPO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addSandboxToGroup(Long groupId, Long sandboxId, AddSandboxToGroupReq addSandboxToGroupReq) {
        SandboxGroupSandboxPO sandboxGroupSandboxPO = sandboxConverter.toSandboxGroupSandboxPO(groupId, sandboxId, addSandboxToGroupReq);
        sandboxGroupSandboxRepository.save(sandboxGroupSandboxPO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSandboxFromGroup(Long groupId, Long sandboxId) {
        sandboxGroupSandboxRepository.remove(
                Wrappers.lambdaQuery(SandboxGroupSandboxPO.class)
                        .eq(SandboxGroupSandboxPO::getSandboxGroupId, groupId)
                        .eq(SandboxGroupSandboxPO::getSandboxId, sandboxId));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSandboxGroupById(Long id) {
        // 删除砂箱组信息
        sandboxGroupRepository.removeById(id);
        // 删除砂箱组与砂箱关联信息
        sandboxGroupSandboxRepository.remove(
                Wrappers.lambdaQuery(SandboxGroupSandboxPO.class)
                        .eq(SandboxGroupSandboxPO::getSandboxGroupId, id)
        );
    }

    @Override
    public PageResult<SandboxGroup> getSandboxGroupPage(GetSandboxGroupPageReq getSandboxGroupPageReq) {
        // 连表查询获取砂箱组与砂箱关联信息
        Page<SandboxGroupPO> sandboxGroupPOPage = sandboxGroupRepository.lambdaQueryPlus()
                .like(StringUtils.isNotEmpty(getSandboxGroupPageReq.getName()), SandboxGroupPO::getName,
                        getSandboxGroupPageReq.getName())
                .orderByDesc(SandboxGroupPO::getId)
                .bindPage(getSandboxGroupPageReq.toPage(), SandboxGroupPO::getSandboxGroupSandboxList);
        if (sandboxGroupPOPage.getRecords().isEmpty()) {
            return PageResult.emptyResult();
        }
        return PageResult.of(sandboxGroupPOPage, sandboxConverter.toSandboxGroupList(sandboxGroupPOPage.getRecords()));
    }

    @Override
    public SandboxGroup getSandboxGroupById(Long id) {
        // 获取砂箱组PO
        SandboxGroupPO sandboxGroupPO = sandboxGroupRepository.getById(id);
        if (sandboxGroupPO == null) {
            return null;
        }
        // 获取砂箱组与砂箱关联信息
        List<SandboxGroupSandboxPO> sandboxGroupSandboxPOList = sandboxGroupSandboxRepository.list(
                Wrappers.lambdaQuery(SandboxGroupSandboxPO.class)
                        .eq(SandboxGroupSandboxPO::getSandboxGroupId, id)
        );
        // 获取砂箱Map
        List<Long> sandboxIdList = sandboxGroupSandboxPOList.stream().map(SandboxGroupSandboxPO::getSandboxId).toList();
        Map<Long, Sandbox> sandboxMap = getSandboxMap(sandboxIdList);
        // 将砂箱数据拼接到砂箱组砂箱关联信息中
        ArrayList<SandboxGroupSandbox> sandboxGroupSandboxes = new ArrayList<>(sandboxGroupSandboxPOList.size());
        for (SandboxGroupSandboxPO sandboxGroupSandboxPO : sandboxGroupSandboxPOList) {
            SandboxGroupSandbox sandboxGroupSandbox = sandboxConverter.toSandboxGroupSandbox(sandboxGroupSandboxPO);
            sandboxGroupSandbox.setSandbox(sandboxMap.getOrDefault(sandboxGroupSandboxPO.getSandboxId(), null));
            sandboxGroupSandboxes.add(sandboxGroupSandbox);
        }
        // 组装返回数据
        SandboxGroup sandboxGroup = sandboxConverter.toSandboxGroup(sandboxGroupPO);
        sandboxGroup.setSandboxGroupSandboxList(sandboxGroupSandboxes);
        return sandboxGroup;
    }

    @Override
    public SandboxGroupSandbox getSandboxGroupSandbox(Long groupId, Long sandboxId) {
        SandboxGroupSandboxPO sandboxGroupSandboxPO = sandboxGroupSandboxRepository.lambdaQuery()
                .eq(SandboxGroupSandboxPO::getSandboxGroupId, groupId)
                .eq(SandboxGroupSandboxPO::getSandboxId, sandboxId)
                .one();
        return sandboxConverter.toSandboxGroupSandbox(sandboxGroupSandboxPO);
    }

    @Override
    public Map<Long, SandboxGroupSummaryVO> getSandboxGroupSummaryVOMap(List<Long> sandboxGroupIds) {
        if (CollectionUtils.isEmpty(sandboxGroupIds)) {
            return Map.of();
        }
        List<SandboxGroupPO> sandboxGroupPOS = sandboxGroupRepository.listByIds(sandboxGroupIds);
        return sandboxGroupPOS.stream().collect(
                Collectors.toMap(SandboxGroupPO::getId, sandboxConverter::toSandboxGroupSummaryVO, (k1, k2) -> k1)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrUpdateSandboxBatchToGroup(Long groupId, List<AddSandboxBatchToGroupReq> reqList) {
        // 先删除原有关联关系
        sandboxGroupSandboxRepository.remove(
                Wrappers.lambdaQuery(SandboxGroupSandboxPO.class)
                        .eq(SandboxGroupSandboxPO::getSandboxGroupId, groupId)
        );
        // 批量新增关联关系
        List<SandboxGroupSandboxPO> sandboxGroupSandboxPOList = reqList.stream()
               .map(req -> sandboxConverter.toSandboxGroupSandboxPO(groupId, req))
               .toList();
        sandboxGroupSandboxRepository.saveBatch(sandboxGroupSandboxPOList);
    }

    /**
     * 根据砂箱型号ID列表，获取砂箱型号Map
     */
    private Map<Long, SandboxModel> getSandboxModelsMap(List<Long> sandboxModelIds) {
        if (CollectionUtils.isEmpty(sandboxModelIds)) {
            return Map.of();
        }

        List<SandboxModelPO> sandboxModelPOS = sandboxModelRepository.listByIds(sandboxModelIds);
        List<SandboxModel> sandboxModelList = sandboxConverter.toSandboxModelList(sandboxModelPOS);
        return sandboxModelList.stream().collect(
                Collectors.toMap(SandboxModel::getId, Function.identity(), (k1, k2) -> k1)
        );
    }

    /**
     * 根据砂箱ID列表，获取砂箱Map
     */
    private Map<Long, Sandbox> getSandboxMap(List<Long> sandboxIds) {
        if (CollectionUtils.isEmpty(sandboxIds)) {
            return Map.of();
        }
        // 获取砂箱PO列表
        List<SandboxPO> sandboxPOS = sandboxRepository.listByIds(sandboxIds);
        // 根据砂箱PO列表中的砂箱型号ID列表，获取砂箱型号Map
        List<Long> sandboxModelIds = sandboxPOS.stream().map(SandboxPO::getSandboxModelId).toList();
        Map<Long, SandboxModel> sandboxModelsMap = getSandboxModelsMap(sandboxModelIds);
        // 组装返回数据，拼接砂箱型号
        List<Sandbox> sandboxList = new ArrayList<>(sandboxPOS.size());
        for (SandboxPO sandboxPO : sandboxPOS) {
            Sandbox sandbox = sandboxConverter.toSandBox(sandboxPO);
            sandbox.setSandboxModel(sandboxModelsMap.getOrDefault(sandboxPO.getSandboxModelId(), null));
            sandboxList.add(sandbox);
        }
        // 返回砂箱Map
        return sandboxList.stream().collect(
                Collectors.toMap(Sandbox::getId, Function.identity(), (k1, k2) -> k1)
        );
    }

    /**
     * 计算砂箱组参数
     * 原理：根据当前砂箱组中有哪些砂箱，分别计算砂箱组的总层数、长度、宽度、高度、空重、满重。具体计算规则如下：
     * 1. 砂箱组的总层数为砂箱组中砂箱的数量，即sandboxIds的大小。
     * 2. 砂箱组的长度为砂箱组中砂箱的最大长度。
     * 3. 砂箱组的宽度为砂箱组中砂箱的最大宽度。
     * 4. 砂箱组的高度为所有砂箱的高度之和。
     * 5. 砂箱组的空重为所有砂箱的空重之和。
     * 6. 砂箱组的满重为所有砂箱的满重之和。
     */
    private void calculateGroupParameters(List<Long> sandboxIds, SandboxGroupPO sandboxGroupPO) {
        Map<Long, Sandbox> sandboxMap = getSandboxMap(sandboxIds);
        List<Float> lengths = new ArrayList<>(sandboxIds.size());
        List<Float> widths = new ArrayList<>(sandboxIds.size());
        List<Float> heights = new ArrayList<>(sandboxIds.size());
        List<Float> emptyWeights = new ArrayList<>(sandboxIds.size());
        List<Float> fullWeights = new ArrayList<>(sandboxIds.size());
        for (Long sandboxId : sandboxMap.keySet()) {
            Sandbox sandbox = sandboxMap.get(sandboxId);
            lengths.add(sandbox.getSandboxModel().getLength());
            widths.add(sandbox.getSandboxModel().getWidth());
            heights.add(sandbox.getSandboxModel().getHeight());
            emptyWeights.add(sandbox.getSandboxModel().getEmptyWeight());
            fullWeights.add(sandbox.getSandboxModel().getFullWeight());
        }
        sandboxGroupPO.setLayerCount(sandboxIds.size());
        sandboxGroupPO.setLength(Collections.max(lengths));
        sandboxGroupPO.setWidth(Collections.max(widths));
        sandboxGroupPO.setHeight(heights.stream().reduce(0F, Float::sum));
        sandboxGroupPO.setEmptyWeight(emptyWeights.stream().reduce(0F, Float::sum));
        sandboxGroupPO.setFullWeight(fullWeights.stream().reduce(0F, Float::sum));
    }
}
