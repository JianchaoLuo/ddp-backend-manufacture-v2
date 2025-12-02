package cn.hex.ddp.manufacture.api.mold.rest.controller;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.mold.rest.converter.MoldApiConverter;
import cn.hex.ddp.manufacture.api.mold.rest.req.*;
import cn.hex.ddp.manufacture.api.mold.rest.vo.MoldGroupSummaryVO;
import cn.hex.ddp.manufacture.application.mold.dto.out.MoldGroupSummaryDTO;
import cn.hex.ddp.manufacture.application.mold.service.MoldService;
import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.domain.mold.enums.MoldTypeEnum;
import cn.hex.ddp.manufacture.domain.mold.model.Mold;
import cn.hex.ddp.manufacture.domain.mold.model.MoldGroup;
import cn.hex.ddp.manufacture.domain.mold.model.MoldModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 模具Controller
 *
 * @author Huhaisen
 * @date 2024/05/21
 */
@Validated
@RestController
@RequestMapping("/api/v2/mold")
public class MoldController {

    @Autowired
    private MoldService moldService;

    @Autowired
    private MoldApiConverter moldConverter;

    /**
     * 模具型号/创建模具型号
     * @param createMoldModelReq 创建模具型号请求参数
     */
    @PostMapping("/model")
    public void createMoldModel(@RequestBody @Valid CreateMoldModelReq createMoldModelReq) {
        moldService.createMoldModel(createMoldModelReq);
    }

    /**
     * 模具型号/获取模具型号分页列表
     * @param getMoldModelPageReq 获取模具型号分页请求参数
     * @return {@link PageResult }<{@link MoldModel }>
     */
    @GetMapping("/model/page")
    public PageResult<MoldModel> getMoldModelPage(@Valid GetMoldModelPageReq getMoldModelPageReq) {
        return moldService.getMoldModelPage(getMoldModelPageReq);
    }

    /**
     * 模具型号/更新模具型号
     * @param id 模具型号id
     * @param updateMoldModelReq 更新模具型号请求参数
     */
    @PutMapping("/model/{id}")
    public void updateMoldModel(@PathVariable("id") @Valid @NotNull(message = "模具型号id不能为空") Long id,
                                @RequestBody @Valid UpdateMoldModelReq updateMoldModelReq) {
        moldService.updateMoldModel(id, updateMoldModelReq);
    }

    /**
     * 模具型号/删除模具型号
     * @param id 模具型号id
     */
    @DeleteMapping("/model/{id}")
    public void deleteMoldModel(@PathVariable("id") @Valid @NotNull(message = "模具型号id不能为空") Long id) {
        moldService.deleteMoldModel(id);
    }


    /**
     * 模具/创建模具
     * @param createMoldReq 创建模具请求参数
     */
    @PostMapping("")
    public void createMold(@RequestBody @Valid CreateMoldReq createMoldReq) {
        moldService.createMold(createMoldReq);
    }

    /**
     * 模具/获取模具分页列表
     * @param getMoldPageReq 获取模具分页请求参数
     * @return {@link PageResult }<{@link Mold }>
     */
    @GetMapping("/page")
    public PageResult<Mold> getMoldPage(@Valid GetMoldPageReq getMoldPageReq) {
        return moldService.getMoldPage(getMoldPageReq);
    }

    /**
     * 模具/更新模具
     * @param id 模具id
     * @param updateMoldReq 更新模具请求参数
     */
    @PutMapping("/{id}")
    public void updateMold(@PathVariable("id") @Valid @NotNull(message = "模具id不能为空") Long id,
                           @RequestBody @Valid UpdateMoldReq updateMoldReq) {
        moldService.updateMold(id, updateMoldReq);
    }

    /**
     * 模具/删除模具
     * @param id 模具id
     */
    @DeleteMapping("/{id}")
    public void deleteMold(@PathVariable("id") @Valid @NotNull(message = "模具id不能为空") Long id) {
        moldService.deleteMold(id);
    }


    /**
     * 模具组/创建模具组
     * @param createMoldGroupReq 创建模具组请求参数
     * 1. 创建模具组时，如果模具为砂箱，则数量需要大于2，如果模具为砂芯，则数量需要等于1
     */
    @PostMapping("/group")
    public void createMoldGroup(@RequestBody @Valid CreateMoldGroupReq createMoldGroupReq) {
        // 校验模具组参数
        validateMoldGroupParams(createMoldGroupReq.getType(), createMoldGroupReq.getGroupItems().size());
        moldService.createMoldGroup(createMoldGroupReq);
    }

    /**
     * 模具组/更新模具组信息
     * 说明：1. 如果模具组明细groupItems为空，认为模具组中的模具不变，尺寸等基本信息按前端传的更新
     * 2. 如果模具组明细groupItems不为空，认为模具组中的模具需要更新，尺寸等基本信息按后端计算的更新
     * 3. 更新模具组时，如果模具为砂箱，则数量需要大于2，如果模具为砂芯，则数量需要等于1
     * @param id 模具组id
     * @param updateMoldGroupReq 更新模具组请求参数
     */
    @PutMapping("/group/{id}")
    public void updateMoldGroup(@PathVariable("id") @Valid @NotNull(message = "模具组id不能为空") Long id,
                                @RequestBody @Valid UpdateMoldGroupReq updateMoldGroupReq) {
        // 校验模具组参数
        validateMoldGroupParams(updateMoldGroupReq.getType(), updateMoldGroupReq.getGroupItems().size());
        moldService.updateMoldGroup(id, updateMoldGroupReq);
    }

    /**
     * 模具组/批量添加或更新模具到模具组
     * @param groupId 模具组id
     * @param reqList 批量添加或更新模具到模具组请求参数
     */
    @PostMapping("/group/{groupId}/mold")
    public void addOrUpdateMoldBatchToGroup(@PathVariable("groupId") @Valid @NotNull(message = "模具组id不能为空") Long groupId,
                                    @RequestBody @Valid List<AddMoldBatchToGroupReq> reqList) {
        moldService.addOrUpdateMoldBatchToGroup(groupId, reqList);
    }

    /**
     * 模具组/添加模具到模具组
     *
     * @param groupId           模具组id
     * @param moldId            模具id
     * @param addMoldToGroupReq 添加模具到模具组请求参数
     */
    @PostMapping("/group/{groupId}/mold/{moldId}")
    public void addMoldToGroup(@PathVariable("groupId") @Valid @NotNull(message = "模具组id不能为空") Long groupId,
                               @PathVariable("moldId") @Valid @NotNull(message = "模具id不能为空") Long moldId,
                               @RequestBody @Valid AddMoldToGroupReq addMoldToGroupReq) {
        moldService.addMoldToGroup(groupId, moldId, addMoldToGroupReq);
    }

    /**
     * 模具组/将模具从模具组中移除
     * @param groupId 模具组id
     * @param moldId 模具id
     */
    @DeleteMapping("/group/{groupId}/mold/{moldId}")
    public void deleteMoldFromGroup(@PathVariable("groupId") @Valid @NotNull(message = "模具组id不能为空") Long groupId,
                                    @PathVariable("moldId") @Valid @NotNull(message = "模具id不能为空") Long moldId) {
        moldService.deleteMoldFromGroup(groupId, moldId);
    }

    /**
     * 模具组/删除模具组
     * @param id 模具组id
     */
    @DeleteMapping("/group/{id}")
    public void deleteMoldGroup(@PathVariable("id") @Valid @NotNull(message = "模具组id不能为空") Long id) {
        moldService.deleteMoldGroup(id);
    }

    /**
     * 模具组/获取模具组分页列表
     * @param getMoldGroupPageReq 获取模具组分页请求参数
     * @return {@link PageResult }<{@link MoldGroupSummaryVO }>
     */
    @GetMapping("/group/page")
    public PageResult<MoldGroupSummaryVO> getMoldGroupPage(@Valid GetMoldGroupPageReq getMoldGroupPageReq) {
        PageResult<MoldGroupSummaryDTO> moldGroupSummaryDTOPage = moldService.getMoldGroupPage(getMoldGroupPageReq);
        return PageResult.of(moldGroupSummaryDTOPage, moldConverter.toMoldGroupSummaryVOList(moldGroupSummaryDTOPage.getList()));
    }

    /**
     * 模具组/获取模具组详情
     * @param id 模具组id
     * @return {@link MoldGroup }
     */
    @GetMapping("/group/{id}")
    public MoldGroup getMoldGroup(@PathVariable("id") @Valid @NotNull(message = "模具组id不能为空") Long id) {
        return moldService.getMoldGroup(id);
    }


    /**
     * 【私有方法 非接口方法】模具组/校验模具组参数
     * @param type 模具组类型
     * @param groupItemsSize 模具组明细数量
     */
    private void validateMoldGroupParams(MoldTypeEnum type, Integer groupItemsSize) {
        // 校验数量的合法性
        // 砂芯模具组中砂芯模具基本数量
        int moldNum = 1;
        // 砂箱模具组中砂箱模具基本数量
        int minSandBoxNum = 2;
        // 如果请求为砂箱
        if(type.equals(MoldTypeEnum.SANDBOX_MOLD)){
            if(groupItemsSize < minSandBoxNum){
                throw new BusinessException(BssExType.SAND_BOX_MOLD_NUM_ERROR,"砂箱模具数量小于2");
            }
        }
        // 如果请求为砂芯
        if(type.equals(MoldTypeEnum.SAND_CORE_MOLD)){
            if(groupItemsSize != moldNum){
                throw new BusinessException(BssExType.SAND_CORE_MOLD_NUM_ERROR,"砂芯模具数量不等于1");
            }
        }
    }


}
