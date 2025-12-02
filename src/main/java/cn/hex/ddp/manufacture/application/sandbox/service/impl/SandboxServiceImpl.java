package cn.hex.ddp.manufacture.application.sandbox.service.impl;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.sandbox.rest.req.*;
import cn.hex.ddp.manufacture.application.sandbox.converter.SandboxAppConverter;
import cn.hex.ddp.manufacture.application.sandbox.dto.out.SandboxGroupSummaryDTO;
import cn.hex.ddp.manufacture.application.sandbox.service.SandboxService;
import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.domain.common.exception.NotFoundException;
import cn.hex.ddp.manufacture.domain.sandbox.manager.SandboxManager;
import cn.hex.ddp.manufacture.domain.sandbox.model.Sandbox;
import cn.hex.ddp.manufacture.domain.sandbox.model.SandboxGroup;
import cn.hex.ddp.manufacture.domain.sandbox.model.SandboxGroupSandbox;
import cn.hex.ddp.manufacture.domain.sandbox.model.SandboxModel;
import cn.hex.ddp.manufacture.domain.technique.manager.TechniqueManager;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 砂箱服务实现类
 *
 * @author Huhaisen
 * @date 2024/05/20
 */
@Slf4j
@Service
public class SandboxServiceImpl implements SandboxService {

    @Autowired
    private SandboxManager sandboxManager;

    @Autowired
    private TechniqueManager techniqueManager;

    @Autowired
    private SandboxAppConverter sandboxConverter;

    @Override
    public void createSandboxModel(CreateSandboxModelReq createSandboxModelReq) {
        sandboxManager.createSandboxModel(createSandboxModelReq);
    }

    @Override
    public PageResult<SandboxModel> getSandboxModelPage(GetSandboxModelPageReq getSandboxModelPageReq) {
        return sandboxManager.getSandboxModelPage(getSandboxModelPageReq);
    }

    @Override
    public void updateSandboxModel(Long id, UpdateSandboxModelReq updateSandboxModelReq) {
        SandboxModel sandboxModel = sandboxManager.getSandboxModelById(id);
        if (sandboxModel == null) {
            log.warn("[SandboxServiceImpl.updateSandboxModel] 砂箱型号不存在, id: {}", id);
            throw new NotFoundException("砂箱型号不存在");
        }

        sandboxManager.updateSandboxModel(id, updateSandboxModelReq);
    }

    @Override
    public void deleteSandboxModel(Long id) {
        SandboxModel sandboxModel = sandboxManager.getSandboxModelById(id);
        if (sandboxModel == null) {
            log.warn("[SandboxServiceImpl.deleteSandboxModel] 砂箱型号不存在, id: {}", id);
            throw new NotFoundException("砂箱型号不存在");
        }
        //判断当前砂箱型号是否有砂箱在使用
        if(CollectionUtils.isNotEmpty(sandboxManager.getSandboxsByModelId(id))){
            log.warn("[SandboxServiceImpl.deleteSandboxModel] 砂箱型号被占用");
            throw new BusinessException(BssExType.SANDBOX_MODEL_BE_OCCUPIED);
        }

        sandboxManager.deleteSandboxModel(id);
    }

    @Override
    public void createSandbox(CreateSandboxReq createSandboxReq) {
        Optional.ofNullable(sandboxManager.getSandboxByNo(createSandboxReq.getSandboxNo())).ifPresent(sandbox -> {
            log.warn("[SandboxServiceImpl.createSandbox] 砂箱编号已存在, sandboxNo: {}", createSandboxReq.getSandboxNo());
            throw new BusinessException(BssExType.SANDBOX_NO_REPEAT, createSandboxReq.getSandboxNo());
        });
        SandboxModel sandboxModel = sandboxManager.getSandboxModelById(createSandboxReq.getSandboxModelId());
        if (sandboxModel == null) {
            log.warn("[SandboxServiceImpl.createSandbox] 砂箱型号不存在, id: {}", createSandboxReq.getSandboxModelId());
            throw new NotFoundException("砂箱型号不存在");
        }
        sandboxManager.createSandbox(createSandboxReq);
    }

    @Override
    public PageResult<Sandbox> getSandboxPage(GetSandboxPageReq getSandboxPageReq) {
        return sandboxManager.getSandboxPage(getSandboxPageReq);
    }

    @Override
    public void updateSandbox(Long id, UpdateSandboxReq updateSandboxReq) {
        Optional.ofNullable(sandboxManager.getSandboxByNo(updateSandboxReq.getSandboxNo())).ifPresent(sandbox -> {
            if (!sandbox.getId().equals(id)) {
                log.warn("[SandboxServiceImpl.updateSandbox] 砂箱编号已存在, sandboxNo: {}",
                        updateSandboxReq.getSandboxNo());
                throw new BusinessException(BssExType.SANDBOX_NO_REPEAT, updateSandboxReq.getSandboxNo());
            }
        });
        Sandbox sandbox = sandboxManager.getSandboxById(id);
        if (sandbox == null) {
            log.warn("[SandboxServiceImpl.updateSandbox] 砂箱不存在, id: {}", id);
            throw new NotFoundException("砂箱不存在");
        }
        if (updateSandboxReq.getSandboxModelId() != null) {
            SandboxModel sandboxModel = sandboxManager.getSandboxModelById(updateSandboxReq.getSandboxModelId());
            if (sandboxModel == null) {
                log.warn("[SandboxServiceImpl.updateSandbox] 砂箱型号不存在, id: {}", updateSandboxReq.getSandboxModelId());
                throw new NotFoundException("砂箱型号不存在");
            }
        }
        sandboxManager.updateSandbox(id, updateSandboxReq);
    }

    @Override
    public void deleteSandbox(Long id) {
        Sandbox sandbox = sandboxManager.getSandboxById(id);
        if (sandbox == null) {
            log.warn("[SandboxServiceImpl.deleteSandbox] 砂箱不存在, id: {}", id);
            throw new NotFoundException("砂箱不存在");
        }
        //判断当前砂箱是否有砂箱组在使用
        if(CollectionUtils.isNotEmpty(sandboxManager.getSandboxGroupSandboxsBySandboxId(id))){
            log.warn("[SandboxServiceImpl.deleteSandbox] 砂箱被占用");
            throw new BusinessException(BssExType.SANDBOX_BE_OCCUPIED);
        }

        sandboxManager.deleteSandbox(id);
    }

    @Override
    public void createSandboxGroup(CreateSandboxGroupReq createSandboxGroupReq) {
        sandboxManager.createSandboxGroup(createSandboxGroupReq);
    }

    @Override
    public void updateSandboxGroup(Long id, UpdateSandboxGroupReq updateSandboxGroupReq) {
        SandboxGroup sandboxGroup = sandboxManager.getSandboxGroupById(id);
        if (sandboxGroup == null) {
            log.warn("[SandboxServiceImpl.updateSandboxGroup] 砂箱组不存在, id: {}", id);
            throw new NotFoundException("砂箱组不存在");
        }
        sandboxManager.updateSandboxGroup(id, updateSandboxGroupReq);
    }

    @Override
    public void addSandboxToGroup(Long groupId, Long sandboxId, AddSandboxToGroupReq addSandboxToGroupReq) {
        SandboxGroup sandboxGroup = sandboxManager.getSandboxGroupById(groupId);
        if (sandboxGroup == null) {
            log.warn("[SandboxServiceImpl.addSandboxToGroup] 砂箱组不存在, sandboxGroupId: {}", groupId);
            throw new NotFoundException("砂箱组不存在");
        }

        Sandbox sandbox = sandboxManager.getSandboxById(sandboxId);
        if (sandbox == null) {
            log.warn("[SandboxServiceImpl.addSandboxToGroup] 砂箱不存在, sandboxId: {}", sandboxId);
            throw new NotFoundException("砂箱不存在");
        }

        SandboxGroupSandbox sandboxGroupSandbox = sandboxManager.getSandboxGroupSandbox(groupId, sandboxId);
        if (sandboxGroupSandbox != null) {
            log.warn("[SandboxServiceImpl.addSandboxToGroup] 砂箱已存在于组中, sandboxGroupId: {}, sandboxId: {}", groupId, sandboxId);
            throw new BusinessException(BssExType.SANDBOX_EXIST_IN_GROUP);
        }
        sandboxManager.addSandboxToGroup(groupId, sandboxId, addSandboxToGroupReq);
    }

    @Override
    public void deleteSandboxFromGroup(Long groupId, Long sandboxId) {
        SandboxGroupSandbox sandboxGroupSandbox = sandboxManager.getSandboxGroupSandbox(groupId, sandboxId);
        if (sandboxGroupSandbox == null) {
            log.warn("[SandboxServiceImpl.deleteSandboxFromGroup] 砂箱不存在于组中, sandboxGroupId: {}, sandboxId: {}", groupId, sandboxId);
            throw new BusinessException(BssExType.SANDBOX_NOT_EXIST_IN_GROUP);
        }
        sandboxManager.deleteSandboxFromGroup(groupId, sandboxId);
    }

    @Override
    public void deleteSandboxGroup(Long id) {
        SandboxGroup sandboxGroup = sandboxManager.getSandboxGroupById(id);
        if (sandboxGroup == null) {
            log.warn("[SandboxServiceImpl.deleteSandboxGroup] 砂箱组不存在, id: {}", id);
            throw new NotFoundException("砂箱组不存在");
        }
        if (CollectionUtils.isNotEmpty(techniqueManager.getTechniquesBySandboxGroupId(id))){
            log.warn("[SandboxServiceImpl.deleteSandboxGroup] 砂箱组被工艺占用, id: {}", id);
            throw new BusinessException(BssExType.SANDBOX_GROUP_BE_OCCUPIED);
        }
        sandboxManager.deleteSandboxGroupById(id);
    }

    @Override
    public PageResult<SandboxGroupSummaryDTO> getSandboxGroupPage(GetSandboxGroupPageReq getSandboxGroupPageReq) {
        PageResult<SandboxGroup> sandboxGroupPage = sandboxManager.getSandboxGroupPage(getSandboxGroupPageReq);
        return PageResult.of(sandboxGroupPage, sandboxConverter.toSandboxGroupSummaryDTOList(sandboxGroupPage.getList()));
    }

    @Override
    public SandboxGroup getSandboxGroup(Long id) {
        SandboxGroup sandboxGroup = sandboxManager.getSandboxGroupById(id);
        if (sandboxGroup == null) {
            log.warn("[SandboxServiceImpl.getSandboxGroup] 砂箱组不存在, id: {}", id);
            throw new NotFoundException("砂箱组不存在");
        }
        return sandboxGroup;
    }

    @Override
    public void addOrUpdateSandboxBatchToGroup(Long groupId, List<AddSandboxBatchToGroupReq> reqList) {
        sandboxManager.addOrUpdateSandboxBatchToGroup(groupId, reqList);
    }
}
