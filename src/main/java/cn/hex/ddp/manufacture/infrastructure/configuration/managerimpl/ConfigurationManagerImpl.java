package cn.hex.ddp.manufacture.infrastructure.configuration.managerimpl;

import cn.hex.ddp.manufacture.api.configuration.rest.req.AddCoordinateReq;
import cn.hex.ddp.manufacture.api.configuration.rest.req.AddPositionReq;
import cn.hex.ddp.manufacture.api.configuration.rest.req.GetCoordinatePageReq;
import cn.hex.ddp.manufacture.api.configuration.rest.req.GetPositionPageReq;
import cn.hex.ddp.manufacture.application.configuration.dto.in.UpdateCoordinateDTO;
import cn.hex.ddp.manufacture.application.configuration.dto.in.UpdatePositionDTO;
import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import cn.hex.ddp.manufacture.domain.configuration.manager.ConfigurationManager;
import cn.hex.ddp.manufacture.domain.configuration.model.Coordinate;
import cn.hex.ddp.manufacture.domain.configuration.model.GlobalConfiguration;
import cn.hex.ddp.manufacture.domain.configuration.model.Position;
import cn.hex.ddp.manufacture.infrastructure.common.redis.RedisUtil;
import cn.hex.ddp.manufacture.infrastructure.configuration.managerimpl.converter.ConfigurationInfraConverter;
import cn.hex.ddp.manufacture.infrastructure.configuration.persistence.po.CoordinatePO;
import cn.hex.ddp.manufacture.infrastructure.configuration.persistence.po.PositionPO;
import cn.hex.ddp.manufacture.infrastructure.configuration.persistence.postgresql.repository.CoordinatePORepository;
import cn.hex.ddp.manufacture.infrastructure.configuration.persistence.postgresql.repository.PositionPORepository;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 工厂全局配置管理器实现类
 *
 * @author Huhaisen
 * @date 2024/05/12
 */
@Slf4j
@Repository
public class ConfigurationManagerImpl implements ConfigurationManager {

    private static final String REDIS_KEY_GLOBAL_CONFIGURATION = "ddp:manufacture:configuration:globalConfiguration";

    @Autowired
    private CoordinatePORepository coordinateRepository;

    @Autowired
    private PositionPORepository positionRepository;

    @Autowired
    private ConfigurationInfraConverter configurationCvt;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCoordinate(Coordinate coordinate) {
        coordinateRepository.save(configurationCvt.toCoordinatePO(coordinate));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCoordinateBatch(List<AddCoordinateReq> coordinateReqList) {
        coordinateRepository.saveBatch(configurationCvt.toCoordinatePOList(coordinateReqList));
    }

    @Override
    public Coordinate getCoordinateById(Long id) {
        CoordinatePO coordinatePO = coordinateRepository.getById(id);
        return configurationCvt.toCoordinate(coordinatePO);
    }

    @Override
    public Coordinate getCoordinateByXAndY(Float x, Float y) {
        CoordinatePO coordinatePO = coordinateRepository.getOne(Wrappers.lambdaQuery(CoordinatePO.class)
                .eq(CoordinatePO::getX, x)
                .eq(CoordinatePO::getY, y));
        return configurationCvt.toCoordinate(coordinatePO);
    }

    @Override
    public List<Coordinate> getCoordinateByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return List.of();
        }

        List<CoordinatePO> coordinatePOList = coordinateRepository.listByIds(ids)
                .stream().sorted(Comparator.comparing(CoordinatePO::getId, Comparator.reverseOrder())).toList();
        return configurationCvt.toCoordinateList(coordinatePOList);
    }

    @Override
    public Map<Long, Coordinate> getCoordinateMapByIds(List<Long> ids) {
        return getCoordinateByIds(ids)
                .stream()
                .collect(Collectors.toMap(Coordinate::getId, Function.identity(), (k1, k2) -> k1));
    }

    @Override
    public Page<Coordinate> getCoordinatePage(GetCoordinatePageReq getCoordinatePageReq) {
        Page<CoordinatePO> poPage = coordinateRepository.page(
                getCoordinatePageReq.toPage(),
                Wrappers.lambdaQuery(CoordinatePO.class)
                        .eq(Objects.nonNull(getCoordinatePageReq.getX()), CoordinatePO::getX,
                                getCoordinatePageReq.getX())
                        .eq(Objects.nonNull(getCoordinatePageReq.getY()), CoordinatePO::getY,
                                getCoordinatePageReq.getY())
                        .orderByDesc(CoordinatePO::getId)
        );
        return configurationCvt.toCoordinatePage(poPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCoordinateById(UpdateCoordinateDTO updateCoordinateDTO) {
        coordinateRepository.updateById(configurationCvt.toCoordinatePO(updateCoordinateDTO));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCoordinateBatch(List<Long> ids) {
        coordinateRepository.removeBatchByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addPosition(AddPositionReq addPositionReq) {
        positionRepository.save(configurationCvt.toPositionPO(addPositionReq));
    }

    @Override
    public Position getPositionById(Long id) {
        PositionPO positionPO = positionRepository.getById(id);
        if (positionPO == null) {
            return null;
        }

        Position position = configurationCvt.toPosition(positionPO);
        position.setCoordinate(configurationCvt.toCoordinate(
                coordinateRepository.getById(positionPO.getCoordinateId())
        ));
        return position;
    }

    @Override
    public Position getPositionByName(String name) {
        PositionPO positionPO = positionRepository.getOne(Wrappers.lambdaQuery(PositionPO.class)
                .eq(PositionPO::getName, name));
        if (positionPO == null) {
            return null;
        }
        Position position = configurationCvt.toPosition(positionPO);
        position.setCoordinate(configurationCvt.toCoordinate(
                coordinateRepository.getById(positionPO.getCoordinateId())
        ));
        return position;
    }

    @Override
    public Position getPositionByNameEnum(PositionNameEnum nameEnum) {
        PositionPO positionPO = positionRepository.getOne(Wrappers.lambdaQuery(PositionPO.class)
                .eq(PositionPO::getNameEnum, nameEnum));
        if (positionPO == null) {
            return null;
        }

        Position position = configurationCvt.toPosition(positionPO);
        position.setCoordinate(configurationCvt.toCoordinate(
                coordinateRepository.getById(positionPO.getCoordinateId())
        ));
        return position;
    }

    @Override
    public List<Position> getPositionByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return List.of();
        }

        List<PositionPO> positionPOList = positionRepository.listByIds(ids)
                .stream().sorted(Comparator.comparing(PositionPO::getId, Comparator.reverseOrder())).toList();
        return toPositionList(positionPOList);
    }

    @Override
    public Page<Position> getPositionPage(GetPositionPageReq getPositionPageReq) {
        Page<PositionPO> poPage = positionRepository.page(
                new Page<>(getPositionPageReq.getCurrent(), getPositionPageReq.getPageSize()),
                Wrappers.lambdaQuery(PositionPO.class)
                        .like(StringUtils.isNotEmpty(getPositionPageReq.getName()), PositionPO::getName,
                                getPositionPageReq.getName())
                        .like(StringUtils.isNotEmpty(getPositionPageReq.getDescription()), PositionPO::getDescription,
                                getPositionPageReq.getDescription())
                        .orderByDesc(PositionPO::getId)
        );
        Page<Position> positionPage = configurationCvt.toPositionPage(poPage);
        positionPage.setRecords(toPositionList(poPage.getRecords()));
        return positionPage;
    }

    @Override
    public List<Position> getAllPosition() {
        List<PositionPO> positionPOList = positionRepository.list(
                Wrappers.lambdaQuery(PositionPO.class).orderByDesc(PositionPO::getId)
        );
        return toPositionList(positionPOList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePositionById(UpdatePositionDTO updatePositionDTO) {
        positionRepository.updateById(configurationCvt.toPositionPO(updatePositionDTO));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePositionByIds(List<Long> ids) {
        positionRepository.removeBatchByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePositionById(Long id) {
        positionRepository.removeById(id);
    }

    @Override
    public void addOrUpdateGlobalConfiguration(GlobalConfiguration globalConfiguration) {
        if (RedisUtil.KeyOps.hasKey(REDIS_KEY_GLOBAL_CONFIGURATION)) {
            RedisUtil.KeyOps.delete(REDIS_KEY_GLOBAL_CONFIGURATION);
        }
        RedisUtil.StringOps.set(REDIS_KEY_GLOBAL_CONFIGURATION, JSONUtil.toJsonStr(globalConfiguration));
    }

    @Override
    public GlobalConfiguration getGlobalConfiguration() {
        if (RedisUtil.KeyOps.hasKey(REDIS_KEY_GLOBAL_CONFIGURATION)) {
            String globalConfigurationStr = RedisUtil.StringOps.get(REDIS_KEY_GLOBAL_CONFIGURATION);
            return JSONUtil.toBean(globalConfigurationStr, GlobalConfiguration.class);
        } else {
            return null;
        }
    }

    /**
     * 转换点位列表, 并添加坐标信息
     *
     * @param positionPOList 点位列表
     * @return {@link List }<{@link Position }>
     */
    private List<Position> toPositionList(List<PositionPO> positionPOList) {
        if (CollectionUtils.isEmpty(positionPOList)) {
            return List.of();
        }

        // 获取点位列表中的坐标ID列表
        List<Long> coordinateIds = positionPOList.stream().map(PositionPO::getCoordinateId).toList();
        // 查询坐标列表
        List<CoordinatePO> coordinatePOList = coordinateRepository.listByIds(coordinateIds);
        List<Coordinate> coordinateList = configurationCvt.toCoordinateList(coordinatePOList);
        // 转换坐标列表为Map, 方便根据坐标ID查询坐标
        Map<Long, Coordinate> coordinateMap = coordinateList.stream().collect(
                Collectors.toMap(Coordinate::getId, entity -> entity)
        );

        // 转换点位列表, 添加坐标信息
        ArrayList<Position> positionList = new ArrayList<>();
        for (PositionPO positionPO : positionPOList) {
            Position position = configurationCvt.toPosition(positionPO);
            position.setCoordinate(coordinateMap.getOrDefault(positionPO.getCoordinateId(), null));
            positionList.add(position);
        }
        return positionList;
    }

    /**
     * 获取最近的三个点位
     */
    @Override
    public List<Position> getNearPosition(Float x, Float y) {
        // 获取所有点位
        List<PositionPO> positionPOList = positionRepository.list();

        // 获取所有相关的坐标ID
        List<Long> coordinateIds = positionPOList.stream()
                .map(PositionPO::getCoordinateId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // 批量查询坐标信息
        List<CoordinatePO> coordinatePOList = coordinateRepository.listByIds(coordinateIds);
        Map<Long, CoordinatePO> coordinateMap = coordinatePOList.stream()
                .collect(Collectors.toMap(CoordinatePO::getId, Function.identity()));

        // 遍历所有点位计算距离并排序，获取距离最近前三的点位信息
        return positionPOList.stream()
                .map(positionPO -> {
                    CoordinatePO coordinatePO = coordinateMap.get(positionPO.getCoordinateId());
                    if (coordinatePO == null) {
                        // 如果找不到坐标信息，返回一个具有最大距离的PositionDistance对象
                        return new PositionDistance(positionPO, Float.MAX_VALUE);
                    }

                    Float distance = (float) Math.sqrt(
                            Math.pow(coordinatePO.getX() - x, 2) +
                                    Math.pow(coordinatePO.getY() - y, 2)
                    );
                    return new PositionDistance(positionPO, distance);
                })
                .sorted(Comparator.comparing(PositionDistance::getDistance))
                .limit(3) // 只取前三个最近的点位
                .map(positionDistance -> {
                    PositionPO positionPO = positionDistance.getPositionPO();
                    CoordinatePO coordinatePO = coordinateMap.get(positionPO.getCoordinateId());
                    Position position = configurationCvt.toPosition(positionPO);
                    if (coordinatePO != null) {
                        position.setCoordinate(configurationCvt.toCoordinate(coordinatePO));
                    }
                    return position;
                })
                .collect(Collectors.toList());
    }

    public Float getDistance(Float x, Float y, Long coordinateId){
        CoordinatePO coordinatePO = coordinateRepository.getOne(Wrappers.lambdaQuery(CoordinatePO.class)
                .eq(CoordinatePO::getId, coordinateId));

        // 判空处理
        if (coordinatePO == null) {
            return Float.MAX_VALUE; // 如果找不到坐标，返回最大距离
        }

        // 计算欧几里得距离
        Float distance = (float) Math.sqrt(
                Math.pow(coordinatePO.getX() - x, 2) +
                        Math.pow(coordinatePO.getY() - y, 2)
        );
        return distance;
    }

    // 辅助类用于存储点位和距离
    private static class PositionDistance {
        private PositionPO positionPO;
        private Float distance;

        public PositionDistance(PositionPO positionPO, Float distance) {
            this.positionPO = positionPO;
            this.distance = distance;
        }

        public PositionPO getPositionPO() {
            return positionPO;
        }

        public Float getDistance() {
            return distance;
        }
    }
}
