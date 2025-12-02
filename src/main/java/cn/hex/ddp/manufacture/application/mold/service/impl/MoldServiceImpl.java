package cn.hex.ddp.manufacture.application.mold.service.impl;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.mold.rest.req.*;
import cn.hex.ddp.manufacture.application.mold.converter.MoldAppConverter;
import cn.hex.ddp.manufacture.application.mold.dto.out.MoldGroupSummaryDTO;
import cn.hex.ddp.manufacture.application.mold.service.MoldService;
import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.domain.common.exception.NotFoundException;
import cn.hex.ddp.manufacture.domain.mold.manager.MoldManager;
import cn.hex.ddp.manufacture.domain.mold.model.Mold;
import cn.hex.ddp.manufacture.domain.mold.model.MoldGroup;
import cn.hex.ddp.manufacture.domain.mold.model.MoldGroupMold;
import cn.hex.ddp.manufacture.domain.mold.model.MoldModel;
import cn.hex.ddp.manufacture.domain.technique.manager.TechniqueManager;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * 模具服务实现类
 *
 * @author Huhaisen
 * @date 2024/05/21
 */
@Slf4j
@Service
public class MoldServiceImpl implements MoldService {

    @Autowired
    private MoldManager moldManager;

    @Autowired
    private MoldAppConverter moldConverter;

    @Autowired
    private TechniqueManager techniqueManager;

    @Override
    public void createMoldModel(CreateMoldModelReq createMoldModelReq) {
        moldManager.createMoldModel(createMoldModelReq);
    }

    @Override
    public PageResult<MoldModel> getMoldModelPage(GetMoldModelPageReq getMoldModelPageReq) {
        return moldManager.getMoldModelPage(getMoldModelPageReq);
    }

    @Override
    public void updateMoldModel(Long id, UpdateMoldModelReq updateMoldModelReq) {
        MoldModel moldModel = moldManager.getMoldModelById(id);
        if (moldModel == null) {
            log.warn("[MoldServiceImpl.updateMoldModel] 模具型号不存在，id={}", id);
            throw new NotFoundException("模具型号不存在");
        }
        moldManager.updateMoldModel(id, updateMoldModelReq);
    }

    @Override
    public void deleteMoldModel(Long id) {
        MoldModel moldModel = moldManager.getMoldModelById(id);
        if (moldModel == null) {
            log.warn("[MoldServiceImpl.deleteMoldModel] 模具型号不存在，id={}", id);
            throw new NotFoundException("模具型号不存在");
        }
        if(CollectionUtils.isNotEmpty(moldManager.getMoldsByModelId(id))){
            log.warn("[MoldServiceImpl.deleteMoldModel] 模具型号被模具占用");
            throw new BusinessException(BssExType.MOLD_MODEL_BE_OCCUPIED);
        }
        moldManager.deleteMoldModelById(id);
    }

    @Override
    public void createMold(CreateMoldReq createMoldReq) {
        Optional.ofNullable(moldManager.getMoldByNo(createMoldReq.getMoldNo())).ifPresent(mold -> {
            log.warn("[MoldServiceImpl.createMold] 模具编号已存在，moldNo={}", createMoldReq.getMoldNo());
            throw new BusinessException(BssExType.MOLD_NO_REPEAT, createMoldReq.getMoldNo());
        });
        MoldModel moldModel = moldManager.getMoldModelById(createMoldReq.getMoldModelId());
        if (moldModel == null) {
            log.warn("[MoldServiceImpl.createMold] 模具型号不存在，moldModelId={}", createMoldReq.getMoldModelId());
            throw new NotFoundException("模具型号不存在");
        }
        moldManager.createMold(createMoldReq);
    }

    @Override
    public PageResult<Mold> getMoldPage(GetMoldPageReq getMoldPageReq) {
        return moldManager.getMoldPage(getMoldPageReq);
    }

    @Override
    public void updateMold(Long id, UpdateMoldReq updateMoldReq) {
        Optional.ofNullable(moldManager.getMoldByNo(updateMoldReq.getMoldNo())).ifPresent(mold -> {
            if (!mold.getId().equals(id)) {
                log.warn("[MoldServiceImpl.updateMold] 模具编号已存在，moldNo={}", updateMoldReq.getMoldNo());
                throw new BusinessException(BssExType.MOLD_NO_REPEAT, updateMoldReq.getMoldNo());
            }
        });
        Mold mold = moldManager.getMoldById(id);
        if (mold == null) {
            log.warn("[MoldServiceImpl.updateMold] 模具不存在，id={}", id);
            throw new NotFoundException("模具不存在");
        }
        if (updateMoldReq.getMoldModelId() != null) {
            MoldModel moldModel = moldManager.getMoldModelById(updateMoldReq.getMoldModelId());
            if (moldModel == null) {
                log.warn("[MoldServiceImpl.updateMold] 模具型号不存在，moldModelId={}", updateMoldReq.getMoldModelId());
                throw new NotFoundException("模具型号不存在");
            }
        }
        moldManager.updateMoldById(id, updateMoldReq);
    }

    @Override
    public void deleteMold(Long id) {
        Mold mold = moldManager.getMoldById(id);
        if (mold == null) {
            log.warn("[MoldServiceImpl.deleteMold] 模具不存在，id={}", id);
            throw new NotFoundException("模具不存在");
        }
        if (CollectionUtils.isNotEmpty(moldManager.getMoldGroupMoldsByMoldId(id))) {
            log.warn("[MoldServiceImpl.deleteMold] 模具被模具组占用");
            throw new BusinessException(BssExType.MOLD_BE_OCCUPIED);
        }
        moldManager.deleteMoldById(id);
    }

    @Override
    public void createMoldGroup(CreateMoldGroupReq createMoldGroupReq) {
        moldManager.createMoldGroup(createMoldGroupReq);
    }

    @Override
    public void updateMoldGroup(Long id, UpdateMoldGroupReq updateMoldGroupReq) {
        MoldGroup moldGroup = moldManager.getMoldGroupById(id);
        if (moldGroup == null) {
            log.warn("[MoldServiceImpl.updateMoldGroup] 模具组不存在，id={}", id);
            throw new NotFoundException("模具组不存在");
        }
        moldManager.updateMoldGroup(id, updateMoldGroupReq);
    }

    @Override
    public void addMoldToGroup(Long groupId, Long moldId, AddMoldToGroupReq addMoldToGroupReq) {
        MoldGroup moldGroup = moldManager.getMoldGroupById(groupId);
        if (moldGroup == null) {
            log.warn("[MoldServiceImpl.addMoldToGroup] 模具组不存在，groupId={}", groupId);
            throw new NotFoundException("模具组不存在");
        }

        Mold mold = moldManager.getMoldById(moldId);
        if (mold == null) {
            log.warn("[MoldServiceImpl.addMoldToGroup] 模具不存在，moldId={}", moldId);
            throw new NotFoundException("模具不存在");
        }

        MoldGroupMold moldGroupMold = moldManager.getMoldGroupMold(groupId, moldId);
        if (moldGroupMold != null) {
            throw new BusinessException(BssExType.MOLD_GROUP_MOLD_EXIST);
        }
        moldManager.addMoldToGroup(groupId, moldId, addMoldToGroupReq);
    }

    @Override
    public void deleteMoldFromGroup(Long groupId, Long moldId) {
        MoldGroupMold moldGroupMold = moldManager.getMoldGroupMold(groupId, moldId);
        if (moldGroupMold == null) {
            log.warn("[MoldServiceImpl.deleteMoldFromGroup] 模具组模具关系不存在，groupId={}, moldId={}", groupId, moldId);
            throw new BusinessException(BssExType.MOLD_GROUP_MOLD_NOT_EXIST);
        }
        moldManager.deleteMoldFromGroup(groupId, moldId);
    }

    @Override
    public void deleteMoldGroup(Long id) {
        MoldGroup moldGroup = moldManager.getMoldGroupById(id);
        if (moldGroup == null) {
            log.warn("[MoldServiceImpl.deleteMoldGroup] 模具组不存在，id={}", id);
            throw new NotFoundException("模具组不存在");
        }
        if(CollectionUtils.isNotEmpty(techniqueManager.getTechniquesByMoldGroupId(id))) {
            log.warn("[MoldServiceImpl.deleteMoldGroup] 模具组被工艺占用");
            throw new BusinessException(BssExType.MOLD_GROUP_BE_OCCUPIED);
        }
        moldManager.deleteMoldGroupById(id);
    }

    @Override
    public PageResult<MoldGroupSummaryDTO> getMoldGroupPage(GetMoldGroupPageReq getMoldGroupPageReq) {
        return moldManager.getMoldGroupPage(getMoldGroupPageReq);
    }

    @Override
    public MoldGroup getMoldGroup(Long id) {
        MoldGroup moldGroup = moldManager.getMoldGroupById(id);
        if (moldGroup == null) {
            log.warn("[MoldServiceImpl.getMoldGroup] 模具组不存在，id={}", id);
            throw new NotFoundException("模具组不存在");
        }
        return moldManager.getMoldGroupDetailById(id);
    }

    @Override
    public void addOrUpdateMoldBatchToGroup(Long groupId, List<AddMoldBatchToGroupReq> reqList) {
        moldManager.addOrUpdateMoldBatchToGroup(groupId, reqList);
    }
}
