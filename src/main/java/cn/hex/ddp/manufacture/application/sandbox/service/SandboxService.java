package cn.hex.ddp.manufacture.application.sandbox.service;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.sandbox.rest.req.*;
import cn.hex.ddp.manufacture.application.sandbox.dto.out.SandboxGroupSummaryDTO;
import cn.hex.ddp.manufacture.domain.sandbox.model.Sandbox;
import cn.hex.ddp.manufacture.domain.sandbox.model.SandboxGroup;
import cn.hex.ddp.manufacture.domain.sandbox.model.SandboxModel;

import java.util.List;

/**
 * 砂箱服务接口
 *
 * @author Huhaisen
 * @date 2024/05/20
 */
public interface SandboxService {
    /**
     * 创建砂箱模型
     * @param createSandboxModelReq 创建砂箱模型请求参数
     */
    void createSandboxModel(CreateSandboxModelReq createSandboxModelReq);

    /**
     * 获取砂箱模型分页数据
     * @param getSandboxModelPageReq 获取砂箱模型分页请求参数
     * @return {@link PageResult }<{@link SandboxModel }>
     */
    PageResult<SandboxModel> getSandboxModelPage(GetSandboxModelPageReq getSandboxModelPageReq);

    /**
     * 更新砂箱模型
     * @param id 砂箱模型ID
     * @param updateSandboxModelReq 更新砂箱模型请求参数
     */
    void updateSandboxModel(Long id, UpdateSandboxModelReq updateSandboxModelReq);

    /**
     * 删除砂箱模型
     * @param id 砂箱模型ID
     */
    void deleteSandboxModel(Long id);

    /**
     * 创建砂箱
     * @param createSandboxReq 创建砂箱请求参数
     */
    void createSandbox(CreateSandboxReq createSandboxReq);

    /**
     * 获取砂箱分页数据
     * @param getSandboxPageReq 获取砂箱分页请求参数
     * @return {@link PageResult }<{@link Sandbox }>
     */
    PageResult<Sandbox> getSandboxPage(GetSandboxPageReq getSandboxPageReq);

    /**
     * 更新砂箱
     * @param id 砂箱ID
     * @param updateSandboxReq 更新砂箱请求参数
     */
    void updateSandbox(Long id, UpdateSandboxReq updateSandboxReq);

    /**
     * 删除砂箱
     * @param id 砂箱ID
     */
    void deleteSandbox(Long id);

    /**
     * 创建砂箱组
     * @param createSandboxGroupReq 创建砂箱组请求参数
     */
    void createSandboxGroup(CreateSandboxGroupReq createSandboxGroupReq);

    /**
     * 更新砂箱组
     * @param id 砂箱组ID
     * @param updateSandboxGroupReq 更新砂箱组请求参数
     */
    void updateSandboxGroup(Long id, UpdateSandboxGroupReq updateSandboxGroupReq);

    /**
     * 添加砂箱到组
     *
     * @param groupId              砂箱组ID
     * @param sandboxId            砂箱ID
     * @param addSandboxToGroupReq 添加砂箱到组请求参数
     */
    void addSandboxToGroup(Long groupId, Long sandboxId, AddSandboxToGroupReq addSandboxToGroupReq);

    /**
     * 从组中删除砂箱
     * @param groupId   砂箱组ID
     * @param sandboxId 砂箱ID
     */
    void deleteSandboxFromGroup(Long groupId, Long sandboxId);

    /**
     * 删除砂箱组
     * @param id 砂箱组ID
     */
    void deleteSandboxGroup(Long id);

    /**
     * 获取砂箱组摘要信息分页数据
     * @param getSandboxGroupPageReq 获取砂箱组摘要信息分页请求参数
     * @return {@link PageResult }<{@link SandboxGroupSummaryDTO }>
     */
    PageResult<SandboxGroupSummaryDTO> getSandboxGroupPage(GetSandboxGroupPageReq getSandboxGroupPageReq);

    /**
     * 获取砂箱组详情
     * @param id     砂箱组ID
     * @return {@link SandboxGroup }
     */
    SandboxGroup getSandboxGroup(Long id);

    /**
     * 向砂箱组中批量添加或更新砂箱
     * @param groupId   砂箱组ID
     * @param reqList   批量添加或更新砂箱请求参数列表
     */
    void addOrUpdateSandboxBatchToGroup(Long groupId, List<AddSandboxBatchToGroupReq> reqList);
}
