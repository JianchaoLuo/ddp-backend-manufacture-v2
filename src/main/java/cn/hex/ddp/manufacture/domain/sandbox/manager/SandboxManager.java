package cn.hex.ddp.manufacture.domain.sandbox.manager;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.sandbox.rest.req.*;
import cn.hex.ddp.manufacture.api.sandbox.rest.vo.SandboxGroupSummaryVO;
import cn.hex.ddp.manufacture.domain.sandbox.model.Sandbox;
import cn.hex.ddp.manufacture.domain.sandbox.model.SandboxGroup;
import cn.hex.ddp.manufacture.domain.sandbox.model.SandboxGroupSandbox;
import cn.hex.ddp.manufacture.domain.sandbox.model.SandboxModel;

import java.util.List;
import java.util.Map;

/**
 * 砂箱管理接口
 *
 * @author Huhaisen
 * @date 2024/05/20
 */
public interface SandboxManager {
    /**
     * 创建砂箱模型
     *
     * @param createSandboxModelReq 创建砂箱模型请求参数
     */
    void createSandboxModel(CreateSandboxModelReq createSandboxModelReq);

    /**
     * 获取砂箱模型分页列表
     *
     * @param getSandboxModelPageReq 获取砂箱模型分页请求参数
     * @return {@link PageResult }<{@link SandboxModel }>
     */
    PageResult<SandboxModel> getSandboxModelPage(GetSandboxModelPageReq getSandboxModelPageReq);

    /**
     * 获取砂箱模型
     * @param id 砂箱模型ID
     * @return {@link SandboxModel }
     */
    SandboxModel getSandboxModelById(Long id);

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
     * 获取砂箱分页列表
     * @param getSandboxPageReq 获取砂箱分页请求参数
     * @return {@link PageResult }<{@link Sandbox }>
     */
    PageResult<Sandbox> getSandboxPage(GetSandboxPageReq getSandboxPageReq);

    /**
     * 获取砂箱
     * @param id 砂箱ID
     * @return {@link Sandbox }
     */
    Sandbox getSandboxById(Long id);

    /**
     * 根据砂箱型号id在砂箱表中获取砂箱列表
     * @param modelId 砂箱型号ID
     * @return List<Sandbox>
     */
    List<Sandbox> getSandboxsByModelId(Long modelId);

    /**
     * 根据砂箱id在砂箱组-砂箱关联表中获取砂箱砂箱组-砂箱关联项列表
     * @param sandboxId 砂箱型号ID
     * @return List<SandboxGroupSandbox>
     */
    List<SandboxGroupSandbox> getSandboxGroupSandboxsBySandboxId(Long sandboxId);

    /**
     * 根据砂箱编号获取砂箱
     * @param sandboxNo 砂箱编号
     * @return {@link Sandbox }
     */
    Sandbox getSandboxByNo(String sandboxNo);

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
     * @param sandboxId 砂箱ID
     * @param groupId 砂箱组ID
     */
    void deleteSandboxFromGroup(Long groupId, Long sandboxId);

    /**
     * 根据ID删除砂箱组
     * @param id  砂箱组ID
     */
    void deleteSandboxGroupById(Long id);

    /**
     * 获取砂箱组分页列表
     * @param getSandboxGroupPageReq 获取砂箱组分页请求参数
     * @return {@link PageResult }<{@link SandboxGroup }>
     */
    PageResult<SandboxGroup> getSandboxGroupPage(GetSandboxGroupPageReq getSandboxGroupPageReq);

    /**
     * 根据ID获取砂箱组
     * @param id 砂箱组ID
     * @return {@link SandboxGroup }
     */
    SandboxGroup getSandboxGroupById(Long id);

    /**
     * 获取砂箱组砂箱关联
     * @param groupId 砂箱组ID
     * @param sandboxId 砂箱ID
     * @return {@link SandboxGroupSandbox }
     */
    SandboxGroupSandbox getSandboxGroupSandbox(Long groupId, Long sandboxId);

    Map<Long, SandboxGroupSummaryVO> getSandboxGroupSummaryVOMap(List<Long> sandboxGroupIds);

    void addOrUpdateSandboxBatchToGroup(Long groupId, List<AddSandboxBatchToGroupReq> reqList);
}
