package cn.hex.ddp.manufacture.api.sandbox.rest.controller;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.sandbox.rest.converter.SandboxApiConverter;
import cn.hex.ddp.manufacture.api.sandbox.rest.req.*;
import cn.hex.ddp.manufacture.api.sandbox.rest.vo.SandboxGroupSummaryVO;
import cn.hex.ddp.manufacture.application.sandbox.dto.out.SandboxGroupSummaryDTO;
import cn.hex.ddp.manufacture.application.sandbox.service.SandboxService;
import cn.hex.ddp.manufacture.domain.sandbox.model.Sandbox;
import cn.hex.ddp.manufacture.domain.sandbox.model.SandboxGroup;
import cn.hex.ddp.manufacture.domain.sandbox.model.SandboxModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 砂箱Controller
 *
 * @author Huhaisen
 * @date 2024/05/20
 */
@Validated
@RestController
@RequestMapping("/api/v2/sandbox")
public class SandboxController {

    @Autowired
    private SandboxService sandboxService;

    @Autowired
    private SandboxApiConverter sandboxConverter;

    /**
     * 砂箱型号/创建砂箱型号
     * @param createSandboxModelReq 创建砂箱型号请求参数
     */
    @PostMapping("/model")
    public void createSandboxModel(@RequestBody @Valid CreateSandboxModelReq createSandboxModelReq) {
        sandboxService.createSandboxModel(createSandboxModelReq);
    }

    /**
     * 砂箱型号/获取砂箱型号分页列表
     * @param getSandboxModelPageReq 获取砂箱型号分页请求参数
     * @return {@link PageResult }<{@link SandboxModel }>
     */
    @GetMapping("/model/page")
    public PageResult<SandboxModel> getSandboxModelPage(@Valid GetSandboxModelPageReq getSandboxModelPageReq) {
        return sandboxService.getSandboxModelPage(getSandboxModelPageReq);
    }

    /**
     * 砂箱型号/更新砂箱型号
     * @param id 砂箱型号ID
     * @param updateSandboxModelReq 更新砂箱型号请求参数
     */
    @PutMapping("/model/{id}")
    public void updateSandboxModel(@PathVariable("id") @Valid @NotNull(message = "id不能为空") Long id,
                                   @RequestBody @Valid UpdateSandboxModelReq updateSandboxModelReq) {
        sandboxService.updateSandboxModel(id, updateSandboxModelReq);
    }

    /**
     * 砂箱型号/删除砂箱型号
     * @param id 砂箱型号ID
     */
    @DeleteMapping("/model/{id}")
    public void deleteSandboxModel(@PathVariable("id") @Valid @NotNull(message = "id不能为空") Long id) {
        sandboxService.deleteSandboxModel(id);
    }


    /**
     * 砂箱/创建砂箱
     * @param createSandboxReq 创建砂箱请求参数
     */
    @PostMapping("")
    public void createSandbox(@RequestBody @Valid CreateSandboxReq createSandboxReq) {
        sandboxService.createSandbox(createSandboxReq);
    }

    /**
     * 砂箱/获取砂箱分页列表
     * @param getSandboxPageReq 获取砂箱分页请求参数
     * @return {@link PageResult }<{@link Sandbox }>
     */
    @GetMapping("/page")
    public PageResult<Sandbox> getSandboxPage(@Valid GetSandboxPageReq getSandboxPageReq) {
        return sandboxService.getSandboxPage(getSandboxPageReq);
    }

    /**
     * 砂箱/更新砂箱
     * @param id 砂箱ID
     * @param updateSandboxReq 更新砂箱请求参数
     */
    @PutMapping("/{id}")
    public void updateSandbox(@PathVariable("id") @Valid @NotNull(message = "id不能为空") Long id,
                             @RequestBody @Valid UpdateSandboxReq updateSandboxReq) {
        sandboxService.updateSandbox(id, updateSandboxReq);
    }

    /**
     * 砂箱/删除砂箱
     * @param id 砂箱ID
     */
    @DeleteMapping("/{id}")
    public void deleteSandbox(@PathVariable("id") @Valid @NotNull(message = "id不能为空") Long id) {
        sandboxService.deleteSandbox(id);
    }


    /**
     * 砂箱组/创建砂箱组
     * @param createSandboxGroupReq 创建砂箱组请求参数
     * 2025-4-8，目前算法仅支持计算两层，需要增加创建砂箱组的层数校验，层数必须大于等于2
     */
    @PostMapping("/group")
    public void createSandboxGroup(@RequestBody @Valid CreateSandboxGroupReq createSandboxGroupReq) {
        sandboxService.createSandboxGroup(createSandboxGroupReq);
    }

    /**
     * 砂箱组/更新砂箱组信息
     * 说明：1. 如果砂箱组明细groupItems为空，认为砂箱组中的砂箱不变，尺寸等基本信息按前端传的更新
     * 2. 如果砂箱组明细groupItems不为空，认为砂箱组中的砂箱需要更新，尺寸等基本信息按后端计算的更新
     * 3. 2025-4-8，目前算法仅支持计算两层，需要增加更新砂箱组的层数校验，层数必须大于等于2
     * @param id 砂箱组ID
     * @param updateSandboxGroupReq 更新砂箱组请求参数
     */
    @PutMapping("/group/{id}")
    public void updateSandboxGroup(@PathVariable("id") @Valid @NotNull(message = "id不能为空") Long id,
                             @RequestBody @Valid UpdateSandboxGroupReq updateSandboxGroupReq) {
        sandboxService.updateSandboxGroup(id, updateSandboxGroupReq);
    }

    /**
     * 砂箱组/向砂箱组中批量添加或更新砂箱
     *
     * @param groupId 砂箱组ID
     * @param reqList 向砂箱组中批量添加或更新砂箱请求参数
     */
    @PostMapping("/group/{groupId}/sandbox")
    public void addOrUpdateSandboxBatchToGroup(@PathVariable("groupId") @Valid @NotNull(message = "砂箱组ID不能为空") Long groupId,
                                          @RequestBody @Valid List<AddSandboxBatchToGroupReq> reqList) {
        sandboxService.addOrUpdateSandboxBatchToGroup(groupId, reqList);
    }

    /**
     * 砂箱组/向砂箱组中添加砂箱
     *
     * @param groupId              砂箱组ID
     * @param sandboxId            砂箱ID
     * @param addSandboxToGroupReq 添加砂箱到组请求参数
     */
    @PostMapping("/group/{groupId}/sandbox/{sandboxId}")
    public void addSandboxToGroup(@PathVariable("groupId") @Valid @NotNull(message = "砂箱组ID不能为空") Long groupId,
                                       @PathVariable("sandboxId") @Valid @NotNull(message = "砂箱ID不能为空") Long sandboxId,
                                       @RequestBody @Valid AddSandboxToGroupReq addSandboxToGroupReq) {
        sandboxService.addSandboxToGroup(groupId, sandboxId, addSandboxToGroupReq);
    }

    /**
     * 砂箱组/从砂箱组中删除砂箱
     * @param groupId 砂箱组ID
     * @param sandboxId 砂箱ID
     */
    @DeleteMapping("/group/{groupId}/sandbox/{sandboxId}")
    public void deleteSandboxFromGroup(@PathVariable("groupId") @Valid @NotNull(message = "砂箱组ID不能为空") Long groupId,
            @PathVariable("sandboxId") @Valid @NotNull(message = "砂箱ID不能为空") Long sandboxId) {
        sandboxService.deleteSandboxFromGroup(groupId, sandboxId);
    }

    /**
     * 砂箱组/删除砂箱组
     * @param id 砂箱组ID
     */
    @DeleteMapping("/group/{id}")
    public void deleteSandboxGroup(@PathVariable("id") @Valid @NotNull(message = "id不能为空") Long id) {
        sandboxService.deleteSandboxGroup(id);
    }

    /**
     * 砂箱组/获取砂箱组分页列表
     * @param getSandboxGroupPageReq 获取砂箱组分页请求参数
     * @return {@link PageResult }<{@link SandboxGroupSummaryVO }>
     */
    @GetMapping("/group/page")
    public PageResult<SandboxGroupSummaryVO> getSandboxGroupPage(@Valid GetSandboxGroupPageReq getSandboxGroupPageReq) {
        PageResult<SandboxGroupSummaryDTO> pageResult = sandboxService.getSandboxGroupPage(getSandboxGroupPageReq);
        return PageResult.of(pageResult, sandboxConverter.toSandboxGroupSummaryVOList(pageResult.getList()));
    }

    /**
     * 砂箱组/获取砂箱组详情
     * @param id 砂箱组ID
     * @return {@link SandboxGroup }
     */
    @GetMapping("/group/{id}")
    public SandboxGroup getSandboxGroup(@PathVariable("id") @Valid @NotNull(message = "id不能为空") Long id) {
        return sandboxService.getSandboxGroup(id);
    }
}
