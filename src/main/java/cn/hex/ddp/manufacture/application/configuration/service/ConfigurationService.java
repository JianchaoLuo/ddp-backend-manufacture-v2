package cn.hex.ddp.manufacture.application.configuration.service;

import cn.hex.ddp.manufacture.api.configuration.rest.req.*;
import cn.hex.ddp.manufacture.api.configuration.rest.vo.NearPointsVO;
import cn.hex.ddp.manufacture.application.configuration.dto.in.UpdateCoordinateDTO;
import cn.hex.ddp.manufacture.application.configuration.dto.in.UpdatePositionDTO;
import cn.hex.ddp.manufacture.application.configuration.dto.out.CoordinateDTO;
import cn.hex.ddp.manufacture.application.configuration.dto.out.PositionDTO;
import cn.hex.ddp.manufacture.domain.configuration.model.GlobalConfiguration;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 工厂全局配置服务接口
 *
 * @author Huhaisen
 * @date 2024/05/12
 */
public interface ConfigurationService {

    /**
     * 添加坐标点, 返回坐标点id
     *
     * @param addCoordinateReq 坐标点信息
     * @return {@link CoordinateDTO }
     */
    CoordinateDTO addCoordinate(AddCoordinateReq addCoordinateReq);

    /**
     * 批量添加坐标点
     *
     * @param coordinateReqList 坐标点列表
     */
    void addCoordinateBatch(List<AddCoordinateReq> coordinateReqList);

    /**
     * 根据id获取坐标点信息
     *
     * @param id 坐标点id
     * @return {@link CoordinateDTO }
     */
    CoordinateDTO getCoordinate(Long id);

    /**
     * 根据id批量获取坐标点信息
     *
     * @param ids 坐标点id列表
     * @return {@link List }<{@link CoordinateDTO }>
     */
    List<CoordinateDTO> getCoordinateBatch(List<Long> ids);

    /**
     * 根据条件分页获取坐标点信息
     *
     * @param getCoordinatePageReq 分页查询条件
     * @return {@link Page }<{@link CoordinateDTO }>
     */
    Page<CoordinateDTO> getCoordinatePage(GetCoordinatePageReq getCoordinatePageReq);

    /**
     * 更新坐标点信息
     *
     * @param updateCoordinateDTO 更新坐标点信息
     */
    void updateCoordinate(UpdateCoordinateDTO updateCoordinateDTO);

    /**
     * 批量删除坐标点
     *
     * @param ids 坐标点id列表
     */
    void deleteCoordinateBatch(List<Long> ids);

    /**
     * 添加单个点位
     *
     * @param addPositionReq 点位信息
     */
    void addPosition(AddPositionReq addPositionReq);

    /**
     * 根据id获取点位信息
     *
     * @param id 点位id
     * @return {@link PositionDTO }
     */
    PositionDTO getPosition(Long id);

    /**
     * 根据id批量获取点位信息
     * @param ids 点位id列表
     * @return {@link List }<{@link PositionDTO }>
     */
    List<PositionDTO> getPositionBatch(List<Long> ids);

    /**
     * 根据条件分页获取点位信息
     *
     * @param getPositionPageReq 分页查询条件
     * @return {@link Page }<{@link PositionDTO }>
     */
    Page<PositionDTO> getPositionPage(GetPositionPageReq getPositionPageReq);

    /**
     * 更新点位信息
     *
     * @param updatePositionDTO 更新点位信息
     */
    void updatePosition(UpdatePositionDTO updatePositionDTO);

    /**
     * 批量删除点位
     *
     * @param ids 点位id列表
     */
    void deletePositionBatch(List<Long> ids);

    /**
     * 删除单个点位
     *
     * @param id 点位id
     */
    void deletePosition(Long id);

    /**
     * 添加或更新全局配置
     *
     * @param addOrUpdateGlobalConfigurationReq 全局配置信息
     */
    void addOrUpdateGlobalConfiguration(AddOrUpdateGlobalConfigurationReq addOrUpdateGlobalConfigurationReq);

    /**
     * 获取全局配置信息
     *
     * @return {@link GlobalConfiguration }
     */
    GlobalConfiguration getGlobalConfiguration();

    List<PositionDTO> getAllPosition();

    /**
     * 获取获取目标点位附近点位
     */
    NearPointsVO getNearByPoint(NearPointReq nearPointReq);
}
