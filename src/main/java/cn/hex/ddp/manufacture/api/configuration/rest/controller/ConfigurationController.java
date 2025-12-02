package cn.hex.ddp.manufacture.api.configuration.rest.controller;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.configuration.rest.converter.ConfigurationApiConverter;
import cn.hex.ddp.manufacture.api.configuration.rest.req.*;
import cn.hex.ddp.manufacture.api.configuration.rest.vo.CoordinateVO;
import cn.hex.ddp.manufacture.api.configuration.rest.vo.GlobalConfigurationVO;
import cn.hex.ddp.manufacture.api.configuration.rest.vo.NearPointsVO;
import cn.hex.ddp.manufacture.api.configuration.rest.vo.PositionVO;
import cn.hex.ddp.manufacture.application.configuration.dto.out.CoordinateDTO;
import cn.hex.ddp.manufacture.application.configuration.dto.out.PositionDTO;
import cn.hex.ddp.manufacture.application.configuration.service.ConfigurationService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 工厂全局配置 Controller
 *
 * @author Huhaisen
 * @date 2024/05/12
 */
@Validated
@RestController
@RequestMapping("/api/v2/configuration")
public class ConfigurationController {

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private ConfigurationApiConverter configurationApiConverter;

    /* ################## 坐标相关接口 ################## */

    /**
     * 坐标/添加单个坐标并返回坐标信息
     *
     * @param addCoordinateReq 坐标参数
     * @return {@link String }
     */
    @PostMapping("/coordinate")
    public CoordinateVO addCoordinate(@RequestBody @Valid AddCoordinateReq addCoordinateReq) {
        CoordinateDTO coordinateDTO = configurationService.addCoordinate(addCoordinateReq);
        return configurationApiConverter.toCoordinateVO(coordinateDTO);
    }

    /**
     * 坐标/批量添加坐标
     *
     * @param addCoordinateReqList 坐标列表
     */
    @PostMapping("/coordinate/batch")
    public void addCoordinateBatch(@RequestBody @Valid @NotEmpty(message = "坐标列表不能为空")
                                       List<AddCoordinateReq> addCoordinateReqList) {
        configurationService.addCoordinateBatch(addCoordinateReqList);
    }

    /**
     * 坐标/获取单个坐标
     *
     * @param id 坐标ID
     * @return {@link CoordinateVO }
     */
    @GetMapping("/coordinate/{id}")
    public CoordinateVO getCoordinate(@PathVariable("id") @Valid @NotNull(message = "id不能为空") Long id) {
        CoordinateDTO coordinateDTO = configurationService.getCoordinate(id);
        return configurationApiConverter.toCoordinateVO(coordinateDTO);
    }

    /**
     * 坐标/根据ID列表批量获取坐标
     *
     * @param ids 坐标ID列表
     * @return {@link List }<{@link CoordinateVO }>
     */
    @GetMapping("/coordinate/batch")
    public List<CoordinateVO> getCoordinateBatch(@RequestParam("ids") @Valid @NotEmpty(message = "id列表不能为空") List<Long> ids) {
        List<CoordinateDTO> coordinateDTOList = configurationService.getCoordinateBatch(ids);
        return configurationApiConverter.toCoordinateVOList(coordinateDTOList);
    }

    /**
     * 坐标/分页获取坐标
     *
     * @param getCoordinatePageReq 分页查询条件
     * @return {@link PageResult }<{@link CoordinateVO }>
     */
    @GetMapping("/coordinate/page")
    public PageResult<CoordinateVO> getCoordinatePage(@Valid GetCoordinatePageReq getCoordinatePageReq) {
        Page<CoordinateDTO> coordinateDTOPage = configurationService.getCoordinatePage(getCoordinatePageReq);
        return PageResult.of(configurationApiConverter.toCoordinateVOPage(coordinateDTOPage));
    }

    /**
     * 坐标/更新单个坐标
     *
     * @param id 坐标ID
     * @param updateCoordinateReq 更新坐标参数
     */
    @PutMapping("/coordinate/{id}")
    public void updateCoordinate(@PathVariable("id") @Valid @NotNull(message = "id不能为空") Long id,
                                 @RequestBody @Valid UpdateCoordinateReq updateCoordinateReq) {
        configurationService.updateCoordinate(configurationApiConverter.toUpdateCoordinateDTO(id, updateCoordinateReq));
    }

    /**
     * 坐标/批量删除坐标
     *
     * @param ids 坐标ID列表
     */
    @DeleteMapping("/coordinate/batch")
    public void deleteCoordinateBatch(@RequestParam("ids") @Valid @NotEmpty(message = "id列表不能为空") List<Long> ids) {
        configurationService.deleteCoordinateBatch(ids);
    }


    /** ################## 点位相关接口 ################## */

    /**
     * 点位/添加单个点位
     *
     * @param addPositionReq 点位参数
     */
    @PostMapping("/position")
    public void addPosition(@RequestBody @Valid AddPositionReq addPositionReq) {
        configurationService.addPosition(addPositionReq);
    }

    /**
     * 点位/获取单个点位
     *
     * @param id 点位ID
     * @return {@link PositionVO }
     */
    @GetMapping("/position/{id}")
    public PositionVO getPosition(@PathVariable("id") @Valid @NotNull(message = "id不能为空") Long id) {
        PositionDTO positionDTO = configurationService.getPosition(id);
        return configurationApiConverter.toPositionVO(positionDTO);
    }

    /**
     * 点位/根据ID列表批量获取点位
     *
     * @param ids 点位ID列表
     * @return {@link List }<{@link PositionVO }>
     */
    @GetMapping("/position/batch")
    public List<PositionVO> getPositionBatch(@RequestParam("ids") @Valid @NotEmpty(message = "id列表不能为空") List<Long> ids) {
        List<PositionDTO> positionDTOList = configurationService.getPositionBatch(ids);
        return configurationApiConverter.toPositionVOList(positionDTOList);
    }

    /**
     * 点位/分页获取点位
     *
     * @param getPositionPageReq 分页查询条件
     * @return {@link PageResult }<{@link PositionVO }>
     */
    @GetMapping("/position/page")
    public PageResult<PositionVO> getPositionPage(@Valid GetPositionPageReq getPositionPageReq) {
        Page<PositionDTO> positionDTOPage = configurationService.getPositionPage(getPositionPageReq);
        return PageResult.of(configurationApiConverter.toPositionVOPage(positionDTOPage));
    }

    /**
     * 点位/更新单个点位
     *
     * @param id                点位ID
     * @param updatePositionReq 更新点位参数
     */
    @PutMapping("/position/{id}")
    public void updatePosition(@PathVariable("id") @Valid @NotNull(message = "id不能为空") Long id,
                               @RequestBody @Valid  UpdatePositionReq updatePositionReq) {
        configurationService.updatePosition(configurationApiConverter.toUpdatePositionDTO(id, updatePositionReq));
    }

    /**
     * 点位/删除单个点位
     *
     * @param id 点位ID
     */
    @DeleteMapping("/position/{id}")
    public void deletePosition(@PathVariable("id") @Valid @NotNull(message = "id不能为空") Long id) {
        configurationService.deletePosition(id);
    }

    /**
     * 点位/批量删除点位
     *
     * @param ids 点位ID列表
     */
    @DeleteMapping("/position/batch")
    public void deletePositionBatch(@RequestParam("ids") @Valid @NotEmpty(message = "id列表不能为空") List<Long> ids) {
        configurationService.deletePositionBatch(ids);
    }


    /** ################## 工厂全局配置相关接口 ################## */

    /**
     * 添加或更新工厂全局配置
     *
     * @param addOrUpdateGlobalConfigurationReq 工厂全局配置参数
     */
    @PostMapping("/global-configuration")
    public void addOrUpdateGlobalConfiguration(@RequestBody @Valid AddOrUpdateGlobalConfigurationReq addOrUpdateGlobalConfigurationReq) {
        configurationService.addOrUpdateGlobalConfiguration(addOrUpdateGlobalConfigurationReq);
    }

    /**
     * 获取工厂全局配置
     *
     * @return {@link GlobalConfigurationVO }
     */
    @GetMapping("/global-configuration")
    public GlobalConfigurationVO getGlobalConfiguration() {
        return configurationApiConverter.toGlobalConfigurationVO(configurationService.getGlobalConfiguration());
    }

    /**################## 手动控制部分接口 ##################*/

    /**
     * 获取目标点位附近的点位信息
     */
    @GetMapping("/coordinate/points")
    public NearPointsVO getNearByPoint(@Valid @RequestBody NearPointReq nearPointReq){
        return configurationService.getNearByPoint(nearPointReq);
    }
}
