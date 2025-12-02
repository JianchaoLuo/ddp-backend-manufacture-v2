package cn.hex.ddp.manufacture.domain.configuration.manager;

import cn.hex.ddp.manufacture.api.configuration.rest.req.*;
import cn.hex.ddp.manufacture.application.configuration.dto.in.UpdateCoordinateDTO;
import cn.hex.ddp.manufacture.application.configuration.dto.in.UpdatePositionDTO;
import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import cn.hex.ddp.manufacture.domain.configuration.model.Coordinate;
import cn.hex.ddp.manufacture.domain.configuration.model.GlobalConfiguration;
import cn.hex.ddp.manufacture.domain.configuration.model.Position;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

/**
 * 工厂配置管理器接口
 *
 * @author Huhaisen
 * @date 2024/05/12
 */
public interface ConfigurationManager {

    /**
     * 插入单个坐标点
     *
     * @param coordinate 坐标点信息
     */
    void addCoordinate(Coordinate coordinate);

    /**
     * 批量添加坐标点
     *
     * @param coordinateReqList 坐标点列表
     */
    void addCoordinateBatch(List<AddCoordinateReq> coordinateReqList);

    /**
     * 根据坐标点ID获取坐标点信息
     *
     * @param id 坐标点ID
     * @return {@link Coordinate }
     */
    Coordinate getCoordinateById(Long id);

    /**
     * 根据坐标点X、Y获取坐标点信息
     */
    Coordinate getCoordinateByXAndY(Float x, Float y);

    /**
     * 根据坐标点ID列表获取坐标点信息
     *
     * @param ids 坐标点ID列表
     * @return {@link List }<{@link Coordinate }>
     */
    List<Coordinate> getCoordinateByIds(List<Long> ids);

    /**
     *
     * @param ids
     * @return
     */
    Map<Long,Coordinate> getCoordinateMapByIds(List<Long> ids);

    /**
     * 根据分页条件获取坐标点列表
     *
     * @param getCoordinatePageReq 分页条件
     * @return {@link Page }<{@link Coordinate }>
     */
    Page<Coordinate> getCoordinatePage(GetCoordinatePageReq getCoordinatePageReq);

    /**
     * 根据坐标点ID更新坐标点信息
     *
     * @param updateCoordinateDTO 坐标点更新信息
     */
    void updateCoordinateById(UpdateCoordinateDTO updateCoordinateDTO);

    /**
     * 根据坐标点ID列表批量删除坐标点信息
     *
     * @param ids 坐标点ID列表
     */
    void deleteCoordinateBatch(List<Long> ids);

    /**
     * 添加单个点位
     * @param addPositionReq 点位信息
     */
    void addPosition(AddPositionReq addPositionReq);

    /**
     * 根据点位ID获取点位信息
     *
     * @param id 点位ID
     * @return {@link Position }
     */
    Position getPositionById(Long id);

    /**
     * 根据点位名称获取点位信息
     *
     * @param name 点位名称
     * @return {@link Position }
     */
    Position getPositionByName(String name);

    /**
     * 根据点位名称枚举获取点位信息
     *
     * @param nameEnum 点位名称枚举
     * @return {@link Position }
     */
    Position getPositionByNameEnum(PositionNameEnum nameEnum);

    /**
     * 根据点位ID列表获取点位信息
     * @param ids 点位ID列表
     * @return {@link List }<{@link Position }>
     */
    List<Position> getPositionByIds(List<Long> ids);

    /**
     * 根据分页条件获取点位列表
     *
     * @param getPositionPageReq 分页条件
     * @return {@link Page }<{@link Position }>
     */
    Page<Position> getPositionPage(GetPositionPageReq getPositionPageReq);

    /**
     * 根据点位ID更新点位信息
     *
     * @param updatePositionDTO 点位更新信息
     */
    void updatePositionById(UpdatePositionDTO updatePositionDTO);

    /**
     * 根据点位ID列表批量删除点位信息
     *
     * @param ids 点位ID列表
     */
    void deletePositionByIds(List<Long> ids);

    /**
     * 根据点位ID删除点位信息
     *
     * @param id 点位ID
     */
    void deletePositionById(Long id);

    /**
     * 添加或更新全局配置信息
     *
     * @param globalConfiguration 全局配置信息
     */
    void addOrUpdateGlobalConfiguration(GlobalConfiguration globalConfiguration);

    /**
     * 获取全局配置信息
     *
     * @return {@link GlobalConfiguration }
     */
    GlobalConfiguration getGlobalConfiguration();

    List<Position> getAllPosition();

    /**
     * 获取目标点位附近距离最近的的；两个点位
     */
    List<Position> getNearPosition(@NotNull(message = "横坐标不能为空") Float x, @NotNull(message = "纵坐标不能为空") Float y);
}
