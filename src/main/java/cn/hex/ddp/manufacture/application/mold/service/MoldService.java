package cn.hex.ddp.manufacture.application.mold.service;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.mold.rest.req.*;
import cn.hex.ddp.manufacture.application.mold.dto.out.MoldGroupSummaryDTO;
import cn.hex.ddp.manufacture.domain.mold.model.Mold;
import cn.hex.ddp.manufacture.domain.mold.model.MoldGroup;
import cn.hex.ddp.manufacture.domain.mold.model.MoldModel;

import java.util.List;

/**
 * 模具服务接口
 *
 * @author Huhaisen
 * @date 2024/05/21
 */
public interface MoldService {
    /**
     * 创建模具型号
     * @param createMoldModelReq 创建模具型号请求参数
     */
    void createMoldModel(CreateMoldModelReq createMoldModelReq);

    /**
     * 获取模具型号分页列表
     * @param getMoldModelPageReq 获取模具型号分页列表请求参数
     * @return {@link PageResult }<{@link MoldModel }>
     */
    PageResult<MoldModel> getMoldModelPage(GetMoldModelPageReq getMoldModelPageReq);

    /**
     * 更新模具型号
     * @param id 模具型号ID
     * @param updateMoldModelReq 更新模具型号请求参数
     */
    void updateMoldModel(Long id, UpdateMoldModelReq updateMoldModelReq);

    /**
     * 删除模具型号
     * @param id 模具型号ID
     */
    void deleteMoldModel(Long id);

    /**
     * 创建模具
     * @param createMoldReq 创建模具请求参数
     */
    void createMold(CreateMoldReq createMoldReq);

    /**
     * 获取模具分页列表
     * @param getMoldPageReq     获取模具分页列表请求参数
     * @return {@link PageResult }<{@link Mold }>
     */
    PageResult<Mold> getMoldPage(GetMoldPageReq getMoldPageReq);

    /**
     * 更新模具
     * @param id 模具ID
     * @param updateMoldReq 更新模具请求参数
     */
    void updateMold(Long id, UpdateMoldReq updateMoldReq);

    /**
     * 删除模具
     * @param id 模具ID
     */
    void deleteMold(Long id);

    /**
     * 创建模具组
     * @param createMoldGroupReq 创建模具组请求参数
     * 说明：对于砂芯模具组，创建时数量为1
     *      对于砂箱模具组，创建时数量大于等于2
     */
    void createMoldGroup(CreateMoldGroupReq createMoldGroupReq);

    /**
     * 更新模具组基本信息
     * @param id 模具组ID
     * @param updateMoldGroupReq 更新模具组请求参数
     * 说明：对于砂芯模具组，创建时数量为1
     *      对于砂箱模具组，创建时数量大于等于2
     */
    void updateMoldGroup(Long id, UpdateMoldGroupReq updateMoldGroupReq);

    /**
     * 添加模具到组
     *
     * @param groupId           模具组ID
     * @param moldId            模具ID
     * @param addMoldToGroupReq 添加模具到组请求参数
     */
    void addMoldToGroup(Long groupId, Long moldId, AddMoldToGroupReq addMoldToGroupReq);

    /**
     * 从组中删除模具
     * @param groupId 模具组ID
     * @param moldId 模具ID
     */
    void deleteMoldFromGroup(Long groupId, Long moldId);

    /**
     * 删除模具组
     * @param id 模具组ID
     */
    void deleteMoldGroup(Long id);

    /**
     * 获取模具组分页列表
     * @param getMoldGroupPageReq 获取模具组分页列表请求参数
     * @return {@link PageResult }<{@link MoldGroupSummaryDTO }>
     */
    PageResult<MoldGroupSummaryDTO> getMoldGroupPage(GetMoldGroupPageReq getMoldGroupPageReq);

    /**
     * 获取模具组详情
     * @param id 模具组ID
     * @return {@link MoldGroup }
     */
    MoldGroup getMoldGroup(Long id);

    /**
     * 批量添加或更新模具到模具组
     * @param groupId 模具组ID
     * @param reqList 批量添加或更新模具请求参数
     */
    void addOrUpdateMoldBatchToGroup(Long groupId, List<AddMoldBatchToGroupReq> reqList);
}
