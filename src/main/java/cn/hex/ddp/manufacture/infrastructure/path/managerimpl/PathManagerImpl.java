package cn.hex.ddp.manufacture.infrastructure.path.managerimpl;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.path.rest.req.GetPathPageReq;
import cn.hex.ddp.manufacture.api.path.rest.vo.PathSummaryVO;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.path.manager.PathManager;
import cn.hex.ddp.manufacture.domain.path.model.Path;
import cn.hex.ddp.manufacture.infrastructure.car.persistence.po.CarPathPO;
import cn.hex.ddp.manufacture.infrastructure.car.persistence.postgresql.repository.CarPathPORepository;
import cn.hex.ddp.manufacture.infrastructure.path.managerimpl.converter.PathInfraConverter;
import cn.hex.ddp.manufacture.infrastructure.path.persistence.po.PathPO;
import cn.hex.ddp.manufacture.infrastructure.path.persistence.postgresql.repository.PathPORepository;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 路径管理实现类
 *
 * @author Huhaisen
 * @date 2024/05/15
 */
@Repository
public class PathManagerImpl implements PathManager {

    @Autowired
    private PathPORepository pathRepository;

    @Autowired
    private PathInfraConverter pathCvt;

    @Autowired
    private CarPathPORepository carPathRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createPath(Path path) {
        pathRepository.save(pathCvt.toPathPO(path));
    }

    @Override
    public Path getPathById(Long id) {
        PathPO pathPO = pathRepository.getById(id);
        return pathCvt.toPath(pathPO);
    }

    @Override
    public Path getPathByNo(String pathNo) {
        PathPO pathPO = pathRepository.getOne(Wrappers.lambdaQuery(PathPO.class).eq(PathPO::getPathNo, pathNo));
        return pathCvt.toPath(pathPO);
    }

    @Override
    public PageResult<Path> getPathPage(GetPathPageReq getPathPageReq) {
        Page<PathPO> page = pathRepository.page(
                new Page<>(getPathPageReq.getCurrent(), getPathPageReq.getPageSize()),
                Wrappers.lambdaQuery(PathPO.class)
                        .like(StringUtils.isNotEmpty(getPathPageReq.getPathNo()), PathPO::getPathNo,
                                getPathPageReq.getPathNo())
                        .like(StringUtils.isNotEmpty(getPathPageReq.getName()), PathPO::getName,
                                getPathPageReq.getName())
                        .eq(ObjectUtils.isNotNull(getPathPageReq.getRailDirection()), PathPO::getRailDirection,
                                getPathPageReq.getRailDirection())
                        .eq(ObjectUtils.isNotNull(getPathPageReq.getArea()), PathPO::getArea, getPathPageReq.getArea())
                        .eq(ObjectUtils.isNotNull(getPathPageReq.getPathType()), PathPO::getPathType,
                                getPathPageReq.getPathType())
                        .orderByDesc(PathPO::getId)
        );
        return PageResult.of(page, pathCvt.toPathList(page.getRecords()));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePathById(Long id) {
        //删除路径信息
        pathRepository.removeById(id);
        //删除车辆路径相关联的信息
        carPathRepository.remove(Wrappers.lambdaQuery(CarPathPO.class).eq(CarPathPO::getPathId, id));
    }

    @Override
    public Map<Long, PathSummaryVO> getPathSummaryVOMap(List<Long> pathIds) {
        if (CollectionUtils.isEmpty(pathIds)) {
            return Map.of();
        }

        List<PathPO> pathPOS = pathRepository.listByIds(pathIds);
        return pathPOS.stream().collect(Collectors.toMap(PathPO::getId, pathCvt::toPathSummaryVO, (a, b) -> a));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePathById(Path updatePath) {
        pathRepository.updateById(pathCvt.toPathPO(updatePath));
    }

    @Override
    public List<Path> getAllPath() {
        List<PathPO> pathPOS = pathRepository.list()
                .stream().sorted(Comparator.comparing(PathPO::getId, Comparator.reverseOrder())).toList();

        return pathCvt.toPathList(pathPOS);
    }

    @Override
    public List<Path> getPathByIds(List<Long> pathIds) {
        if (CollectionUtils.isEmpty(pathIds)) {
            return List.of();
        }
        List<PathPO> pathPOS = pathRepository.listByIds(pathIds)
                .stream().sorted(Comparator.comparing(PathPO::getId, Comparator.reverseOrder())).toList();
        return pathCvt.toPathList(pathPOS);
    }

    @Override
    public List<Path> getListByArea(AreaEnum area) {
        if (ObjectUtils.isNull(area)) {
            return List.of();
        }
        List<PathPO> pathPOS = pathRepository.list(Wrappers.lambdaQuery(PathPO.class).eq(PathPO::getArea, area));
        if (CollectionUtils.isEmpty(pathPOS)) {
            return List.of();
        }
        return pathCvt.toPathList(pathPOS);
    }
}
