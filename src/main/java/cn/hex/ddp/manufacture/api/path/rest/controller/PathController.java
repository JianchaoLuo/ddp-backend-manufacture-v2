package cn.hex.ddp.manufacture.api.path.rest.controller;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.path.rest.converter.PathApiConverter;
import cn.hex.ddp.manufacture.api.path.rest.req.CreatePathReq;
import cn.hex.ddp.manufacture.api.path.rest.req.GetPathPageReq;
import cn.hex.ddp.manufacture.api.path.rest.req.UpdatePathReq;
import cn.hex.ddp.manufacture.api.path.rest.vo.PathSummaryVO;
import cn.hex.ddp.manufacture.api.path.rest.vo.PathVO;
import cn.hex.ddp.manufacture.application.path.dto.out.PathDTO;
import cn.hex.ddp.manufacture.application.path.dto.out.PathSummaryDTO;
import cn.hex.ddp.manufacture.application.path.service.PathService;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 路径Controller
 *
 * @author Huhaisen
 * @date 2024/05/15
 */
@Validated
@RestController
@RequestMapping("/api/v2/path")
public class PathController {

    @Autowired
    private PathService pathService;

    @Autowired
    private PathApiConverter pathCvt;

    /**
     * 创建路径
     *
     * @param createPathReq 创建路径请求参数
     */
    @PostMapping("")
    public void createPath(@RequestBody @Valid CreatePathReq createPathReq) {
        pathService.createPath(createPathReq);
    }

    /**
     * 获取单条路径详情
     *
     * @param id 路径ID
     * @return {@link PathVO }
     */
    @GetMapping("/{id}")
    public PathVO getPath(@PathVariable("id") @Valid @NotNull(message = "路径ID不能为空") Long id) {
        PathDTO pathDTO = pathService.getPath(id);
        return pathCvt.toPathVO(pathDTO);
    }

    /**
     * 获取路径分页列表
     *
     * @param getPathPageReq 分页请求参数
     * @return {@link PageResult }<{@link PathVO }>
     */
    @GetMapping("/page")
    public PageResult<PathVO> getPathPage(@Valid GetPathPageReq getPathPageReq) {
        PageResult<PathDTO> pageResult = pathService.getPathPage(getPathPageReq);
        return PageResult.of(pageResult, pathCvt.toPathVOList(pageResult.getList()));
    }

    /**
     * 更新路径
     *
     * @param id            路径ID
     * @param updatePathReq 更新路径请求参数
     */
    @PutMapping("/{id}")
    public void updatePath(@PathVariable("id") @Valid @NotNull(message = "路径ID不能为空") Long id,
                           @RequestBody @Valid UpdatePathReq updatePathReq) {
        pathService.updatePath(id, updatePathReq);
    }

    /**
     * 删除路径
     *
     * @param id 路径ID
     */
    @DeleteMapping("/{id}")
    public void deletePath(@PathVariable("id") @Valid @NotNull(message = "路径ID不能为空") Long id) {
        pathService.deletePath(id);
    }

    /**
     * 根据区域获取路径列表
     *
     * @return List<PathSummaryVO>
     */
    @GetMapping("/list/{area}")
    public List<PathSummaryVO> getPathListByArea(@PathVariable("area") @Valid @NotNull(message = "area不能为空") AreaEnum area){
        List<PathSummaryDTO> pathSummaryDTOS = pathService.getListByArea(area);
        if (pathSummaryDTOS == null || pathSummaryDTOS.isEmpty()) {
            return List.of();
        }
        return pathCvt.toPathSummaryVOList(pathSummaryDTOS);
    }
}
