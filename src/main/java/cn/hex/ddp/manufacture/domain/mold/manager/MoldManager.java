package cn.hex.ddp.manufacture.domain.mold.manager;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.mold.rest.req.*;
import cn.hex.ddp.manufacture.api.mold.rest.vo.MoldGroupSummaryVO;
import cn.hex.ddp.manufacture.application.mold.dto.out.MoldGroupSummaryDTO;
import cn.hex.ddp.manufacture.domain.mold.model.Mold;
import cn.hex.ddp.manufacture.domain.mold.model.MoldGroup;
import cn.hex.ddp.manufacture.domain.mold.model.MoldGroupMold;
import cn.hex.ddp.manufacture.domain.mold.model.MoldModel;
import cn.hex.ddp.manufacture.infrastructure.mold.persistence.po.MoldGroupMoldPO;

import java.util.List;
import java.util.Map;

/**
 * 模具管理接口
 *
 * @author Huhaisen
 * @date 2024/05/21
 */
public interface MoldManager {
    /**
     * 创建模具型号
     * @param createMoldModelReq 模具型号创建请求
     */
    void createMoldModel(CreateMoldModelReq createMoldModelReq);

    /**
     * 获取模具型号分页数据
     * @param getMoldModelPageReq 模具型号分页请求
     * @return {@link PageResult }<{@link MoldModel }>
     */
    PageResult<MoldModel> getMoldModelPage(GetMoldModelPageReq getMoldModelPageReq);

    /**
     * 根据id获取模具型号
     * @param id 模具型号id
     * @return {@link MoldModel }
     */
    MoldModel getMoldModelById(Long id);

    /**
     * 更新模具型号
     * @param id 模具型号id
     * @param updateMoldModelReq 模具型号更新请求
     */
    void updateMoldModel(Long id, UpdateMoldModelReq updateMoldModelReq);

    /**
     * 根据id删除模具型号
     * @param id 模具型号id
     */
    void deleteMoldModelById(Long id);

    /**
     * 创建模具
     * @param createMoldReq 模具创建请求
     */
    void createMold(CreateMoldReq createMoldReq);

    /**
     * 获取模具分页数据
     * @param getMoldPageReq 模具分页请求
     * @return {@link PageResult }<{@link Mold }>
     */
    PageResult<Mold> getMoldPage(GetMoldPageReq getMoldPageReq);

    /**
     * 根据id获取模具
     * @param id 模具id
     * @return {@link Mold }
     */
    Mold getMoldById(Long id);

    /**
     * 根据模具编号获取模具
     * @param moldNo    模具编号`
     * @return {@link Mold }
     */
    Mold getMoldByNo(String moldNo);

    /**
     * 根据模具型号获取模具列表
     * @param modelId 模具型号
     * @return List<Mold>
     */
    List<Mold> getMoldsByModelId(Long modelId);

    /**
     * 根据id更新模具
     * @param id 模具id
     * @param updateMoldReq 模具更新请求
     */
    void updateMoldById(Long id, UpdateMoldReq updateMoldReq);

    /**
     * 根据id删除模具
     * @param id 模具id
     */
    void deleteMoldById(Long id);

    /**
     * 创建模具组
     * @param createMoldGroupReq 模具组创建请求
     */
    void createMoldGroup(CreateMoldGroupReq createMoldGroupReq);

    /**
     * 根据id获取模具组
     * @param id 模具组id
     */
    MoldGroup getMoldGroupById(Long id);

    /**
     * 根据模具id获取模具组模具表项
     * @param moldId 模具id
     */
    List<MoldGroupMold> getMoldGroupMoldsByMoldId(Long moldId);

    /**
     * 更新模具组基本信息
     * @param id                 模具组id
     * @param updateMoldGroupReq 模具组更新请求
     */
    void updateMoldGroup(Long id, UpdateMoldGroupReq updateMoldGroupReq);

    /**
     * 添加模具到模具组
     * @param groupId           模具组id
     * @param moldId            模具id
     * @param addMoldToGroupReq 添加模具到模具组请求
     */
    void addMoldToGroup(Long groupId, Long moldId, AddMoldToGroupReq addMoldToGroupReq);

    /**
     * 将模具从模具组中删除
     * @param groupId 模具组id
     * @param moldId 模具id
     */
    void deleteMoldFromGroup(Long groupId, Long moldId);

    /**
     * 根据id删除模具组
     * @param id 模具组id
     */
    void deleteMoldGroupById(Long id);

    /**
     * 获取模具组分页数据
     * @param getMoldGroupPageReq     模具组分页请求
     * @return {@link PageResult }<{@link MoldGroupSummaryDTO }>
     */
    PageResult<MoldGroupSummaryDTO> getMoldGroupPage(GetMoldGroupPageReq getMoldGroupPageReq);

    /**
     * 根据模具组id获取模具组详情
     * @param id 模具组id
     * @return {@link MoldGroup }
     */
    MoldGroup getMoldGroupDetailById(Long id);

    /**
     * 根据模具组id和模具id获取模具组模具关系
     * @param groupId 模具组id
     * @param moldId 模具id
     * @return {@link MoldGroupMold }
     */
    MoldGroupMold getMoldGroupMold(Long groupId, Long moldId);

    Map<Long, MoldGroupSummaryVO> getMoldGroupSummaryVOMap(List<Long> sandboxMoldGroupIds);

    void addOrUpdateMoldBatchToGroup(Long groupId, List<AddMoldBatchToGroupReq> reqList);
}
