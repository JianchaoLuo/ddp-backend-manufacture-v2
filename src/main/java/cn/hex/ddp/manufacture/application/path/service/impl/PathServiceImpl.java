package cn.hex.ddp.manufacture.application.path.service.impl;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.path.rest.req.CreatePathReq;
import cn.hex.ddp.manufacture.api.path.rest.req.GetPathPageReq;
import cn.hex.ddp.manufacture.api.path.rest.req.UpdatePathReq;
import cn.hex.ddp.manufacture.application.configuration.converter.ConfigurationAppConverter;
import cn.hex.ddp.manufacture.application.configuration.dto.out.CoordinateDTO;
import cn.hex.ddp.manufacture.application.path.converter.PathAppConverter;
import cn.hex.ddp.manufacture.application.path.dto.out.PathDTO;
import cn.hex.ddp.manufacture.application.path.dto.out.PathSummaryDTO;
import cn.hex.ddp.manufacture.application.path.service.PathService;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.common.exception.BssExType;
import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.domain.common.exception.NotFoundException;
import cn.hex.ddp.manufacture.domain.configuration.manager.ConfigurationManager;
import cn.hex.ddp.manufacture.domain.configuration.model.Coordinate;
import cn.hex.ddp.manufacture.domain.path.manager.PathManager;
import cn.hex.ddp.manufacture.domain.path.model.Path;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 路径服务实现类
 *
 * @author Huhaisen
 * @date 2024/05/15
 */
@Slf4j
@Service
public class PathServiceImpl implements PathService {

    @Autowired
    private PathManager pathManager;

    @Autowired
    private PathAppConverter pathCvt;

    @Autowired
    private ConfigurationManager configurationManager;

    @Autowired
    private ConfigurationAppConverter configurationCvt;

    @Override
    public void createPath(CreatePathReq createPathReq) {
        Coordinate startCoordinate = configurationManager.getCoordinateById(createPathReq.getStartCoordinateId());
        if (startCoordinate == null) {
            log.warn("[PathServiceImpl.createPath] 坐标不存在，id={}", createPathReq.getStartCoordinateId());
            throw new NotFoundException("坐标不存在");
        }
        Coordinate endCoordinate = configurationManager.getCoordinateById(createPathReq.getEndCoordinateId());
        if (endCoordinate == null) {
            log.warn("[PathServiceImpl.createPath] 坐标不存在，id={}", createPathReq.getEndCoordinateId());
            throw new NotFoundException("坐标不存在");
        }
        Coordinate middleCoordinate = configurationManager.getCoordinateById(createPathReq.getMiddleCoordinateId());
        if (middleCoordinate == null) {
            log.warn("[PathServiceImpl.createPath] 坐标不存在，id={}", createPathReq.getMiddleCoordinateId());
            throw new NotFoundException("坐标不存在");
        }

        Optional.ofNullable(pathManager.getPathByNo(createPathReq.getPathNo())).ifPresent(p -> {
            log.warn("[PathServiceImpl.createPath] 路径编号已存在，pathNo={}", createPathReq.getPathNo());
            throw new BusinessException(BssExType.PATH_NO_REPEAT_ERROR, createPathReq.getPathNo());
        });
        pathManager.createPath(pathCvt.toPath(createPathReq));
    }

    @Override
    public PathDTO getPath(Long id) {
        Path path = pathManager.getPathById(id);
        if (path == null) {
            log.warn("[PathServiceImpl.getPath] 路径不存在，id={}", id);
            throw new NotFoundException("路径不存在");
        }
        PathDTO pathDTO = pathCvt.toPathDTO(path);
        // 获得路径中涉及的坐标信息
        List<Long> coordinateIdList = new ArrayList<>(List.of(path.getStartCoordinateId(), path.getEndCoordinateId(),
                path.getMiddleCoordinateId()));
        // 添加节点坐标ID
        coordinateIdList.addAll(path.getNodeCoordinateIds());
        List<Coordinate> coordinates = configurationManager.getCoordinateByIds(coordinateIdList);
        List<CoordinateDTO> coordinateDTOS = configurationCvt.coordinateListToDTOList(coordinates);
        Map<Long, CoordinateDTO> coordinateDTOMap =
                coordinateDTOS.stream().collect(Collectors.toMap(CoordinateDTO::getId, c -> c, (a, b) -> a));
        // 填充坐标信息
        pathDTO.setStartCoordinate(coordinateDTOMap.getOrDefault(path.getStartCoordinateId(), null));
        pathDTO.setEndCoordinate(coordinateDTOMap.getOrDefault(path.getEndCoordinateId(), null));
        pathDTO.setMiddleCoordinate(coordinateDTOMap.getOrDefault(path.getMiddleCoordinateId(), null));
        // 填充节点坐标信息
        List<CoordinateDTO> nodeCoordinateDTOS = path.getNodeCoordinateIds().stream().map(coordinateDTOMap::get)
                .toList();
        pathDTO.setNodeCoordinates(nodeCoordinateDTOS);
        return pathDTO;
    }

    @Override
    public PageResult<PathDTO> getPathPage(GetPathPageReq getPathPageReq) {
        PageResult<Path> pathPage = pathManager.getPathPage(getPathPageReq);
        // 路径信息转换并补充坐标信息
        List<PathDTO> pathDTOS = pathsToPathDTOS(pathPage.getList());
        return PageResult.of(pathPage, pathDTOS);
    }

    @Override
    public List<PathDTO> getAllPath() {
        List<Path> paths = pathManager.getAllPath();
        // 路径信息转换并补充坐标信息
        return pathsToPathDTOS(paths);
    }

    @Override
    public void deletePath(Long id) {
        Path path = pathManager.getPathById(id);
        if (path == null) {
            log.warn("[PathServiceImpl.deletePath] 路径不存在，id={}", id);
            throw new NotFoundException("路径不存在");
        }
        pathManager.deletePathById(id);
    }

    @Override
    public void updatePath(Long id, UpdatePathReq updatePathReq) {
        Path path = pathManager.getPathById(id);
        if (path == null) {
            log.warn("[PathServiceImpl.updatePath] 路径不存在，id={}", id);
            throw new NotFoundException("路径不存在");
        }
        Path pathByNo = pathManager.getPathByNo(updatePathReq.getPathNo());
        if (pathByNo != null && !pathByNo.getId().equals(id)) {
            log.warn("[PathServiceImpl.updatePath] 路径编号已存在，pathNo={}", updatePathReq.getPathNo());
            throw new BusinessException(BssExType.PATH_NO_REPEAT_ERROR, updatePathReq.getPathNo());
        }
        Path updatePath = pathCvt.updatePath(path, updatePathReq);
        pathManager.updatePathById(updatePath);
    }

    @Override
    public List<PathDTO> getPathByIds(List<Long> pathIds) {
        List<Path> paths = pathManager.getPathByIds(pathIds);
        return pathsToPathDTOS(paths);
    }

    @Override
    public List<PathSummaryDTO> getListByArea(AreaEnum area) {
        List<Path> paths = pathManager.getListByArea(area);
        if (CollectionUtils.isEmpty(paths)) {
            return List.of();
        }
        // 路径信息转换为摘要DTO
        return pathCvt.toPathSummaryDTOList(paths);
    }

    /**
     * 路径信息转换并补充坐标信息
     *
     * @param paths 路径列表
     * @return {@link List }<{@link PathDTO }>
     */
    private List<PathDTO> pathsToPathDTOS(List<Path> paths) {
        if (CollectionUtils.isEmpty(paths)) {
            return List.of();
        }

        // 收集所有的坐标ID，包括startCoordinateId, endCoordinateId, middleCoordinateId以及nodeCoordinateIds中的坐标ID
        List<Long> coordinateIds = paths.stream()
                .flatMap(p -> Stream.concat(
                        Stream.of(p.getStartCoordinateId(), p.getEndCoordinateId(), p.getMiddleCoordinateId()),
                        p.getNodeCoordinateIds().stream()))
                .toList();
        List<Coordinate> coordinates = configurationManager.getCoordinateByIds(coordinateIds);
        List<CoordinateDTO> coordinateDTOS = configurationCvt.coordinateListToDTOList(coordinates);
        Map<Long, CoordinateDTO> coordinateDTOMap =
                coordinateDTOS.stream().collect(Collectors.toMap(CoordinateDTO::getId, c -> c, (a, b) -> a));
        // 填充坐标信息
        List<PathDTO> pathDTOS = paths.stream().map(p -> {
            PathDTO pathDTO = pathCvt.toPathDTO(p);
            pathDTO.setStartCoordinate(coordinateDTOMap.getOrDefault(p.getStartCoordinateId(), null));
            pathDTO.setEndCoordinate(coordinateDTOMap.getOrDefault(p.getEndCoordinateId(), null));
            pathDTO.setMiddleCoordinate(coordinateDTOMap.getOrDefault(p.getMiddleCoordinateId(), null));
            // 填充节点坐标信息
            List<CoordinateDTO> nodeCoordinateDTOS = p.getNodeCoordinateIds().stream().map(coordinateDTOMap::get)
                    .toList();
            pathDTO.setNodeCoordinates(nodeCoordinateDTOS);
            return pathDTO;
        }).toList();
        return pathDTOS;
    }

}
