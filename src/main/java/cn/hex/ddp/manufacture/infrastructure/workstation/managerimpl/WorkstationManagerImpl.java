package cn.hex.ddp.manufacture.infrastructure.workstation.managerimpl;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.workstation.rest.req.ListWorkstationReq;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.configuration.model.Coordinate;
import cn.hex.ddp.manufacture.domain.workstation.manager.WorkstationManager;
import cn.hex.ddp.manufacture.domain.workstation.model.Workstation;
import cn.hex.ddp.manufacture.infrastructure.configuration.persistence.po.CoordinatePO;
import cn.hex.ddp.manufacture.infrastructure.configuration.persistence.postgresql.repository.CoordinatePORepository;
import cn.hex.ddp.manufacture.infrastructure.workstation.managerimpl.converter.WorkstationConverter;
import cn.hex.ddp.manufacture.infrastructure.workstation.persistence.po.WorkstationPO;
import cn.hex.ddp.manufacture.infrastructure.workstation.persistence.postgresql.repository.WorkstationPORepository;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class WorkstationManagerImpl implements WorkstationManager {

    @Autowired
    private WorkstationPORepository workstationPORepository;

    @Autowired
    private CoordinatePORepository coordinatePORepository;

    @Autowired
    private WorkstationConverter workstationConverter;

    @Override
    public PageResult<Workstation> listWorkstation(ListWorkstationReq listWorkstationReq) {
        Page<WorkstationPO> page = workstationPORepository.page(
                listWorkstationReq.toPage(),
                Wrappers.lambdaQuery(WorkstationPO.class)
                        .like(StringUtils.isNotEmpty(listWorkstationReq.getStationNo()), WorkstationPO::getStationNo,
                                listWorkstationReq.getStationNo())
                        .like(StringUtils.isNotEmpty(listWorkstationReq.getName()), WorkstationPO::getName,
                                listWorkstationReq.getName())
                        .eq(ObjectUtils.isNotNull(listWorkstationReq.getArea()), WorkstationPO::getArea,
                                listWorkstationReq.getArea())
                        .eq(ObjectUtils.isNotNull(listWorkstationReq.getProcess()), WorkstationPO::getProcess,
                                listWorkstationReq.getProcess())
                        .orderByDesc(WorkstationPO::getId)
        );
        return PageResult.of(page, workstationConverter::toWorkstation);
    }

    @Override
    public void addWorkstation(Workstation workstation) {
        workstationPORepository.save(workstationConverter.toWorkstationPO(workstation));
    }

    @Override
    public void deleteWorkstation(Long id) {
        workstationPORepository.removeById(id);
    }

    @Override
    public void updateWorkstation(Workstation workstation) {
        workstationPORepository.updateById(workstationConverter.toWorkstationPO(workstation));
    }

    @Override
    public Map<Long, Workstation> listWorkstationMapByIds(List<Long> workstationIds) {
        if (CollUtil.isEmpty(workstationIds)) {
            return Collections.emptyMap();
        }
        return workstationPORepository.listByIds(workstationIds).stream()
                .map(workstationConverter::toWorkstation)
                .collect(Collectors.toMap(Workstation::getId, workstation -> workstation, (a, b) -> a));
    }

    @Override
    public Workstation getWorkstationById(Long workstationId) {
        WorkstationPO byId = workstationPORepository.getById(workstationId);
        return workstationConverter.toWorkstation(byId);
    }

    @Override
    public List<Workstation> getAllWorkstation() {
        List<WorkstationPO> all = workstationPORepository.list()
                .stream().sorted(Comparator.comparing(WorkstationPO::getId, Comparator.reverseOrder())).toList();
        return all.stream().map(workstationConverter::toWorkstation).collect(Collectors.toList());
    }

    @Override
    public Workstation getWorkstationByNo(String stationNo) {
        WorkstationPO workstationPO = workstationPORepository.getOne(Wrappers.lambdaQuery(WorkstationPO.class)
                .eq(WorkstationPO::getStationNo, stationNo));
        return workstationConverter.toWorkstation(workstationPO);
    }

    @Override
    public List<Workstation> getListByArea(AreaEnum area) {
        List<WorkstationPO> workstationPOS = workstationPORepository.list(
                Wrappers.lambdaQuery(WorkstationPO.class).eq(WorkstationPO::getArea, area));
        return workstationConverter.toWorkstationList(workstationPOS);
    }

    @Override
    public List<Workstation> getNearWorkStation(Float x, Float y) {
        // 参数校验
        if (x == null || y == null) {
            return List.of();
        }

        // 获取所有工位
        List<WorkstationPO> workstationPOS = workstationPORepository.list();
        if (CollUtil.isEmpty(workstationPOS)) {
            return List.of();
        }

        // 获取所有相关的坐标ID
        List<Long> coordinateIds = workstationPOS.stream()
                .map(WorkstationPO::getCoordinateId)
                .filter(ObjectUtils::isNotNull)
                .distinct()
                .collect(Collectors.toList());

        if (CollUtil.isEmpty(coordinateIds)) {
            // 如果没有坐标信息，返回所有工位中的前3个
            return workstationPOS.stream()
                    .limit(3)
                    .map(workstationConverter::toWorkstation)
                    .collect(Collectors.toList());
        }

        // 批量查询坐标信息
        List<CoordinatePO> coordinatePOList = coordinatePORepository.listByIds(coordinateIds);
        Map<Long, CoordinatePO> coordinateMap = coordinatePOList.stream()
                .collect(Collectors.toMap(CoordinatePO::getId, Function.identity()));

        // 计算距离并排序
        return workstationPOS.stream()
                .map(workstationConverter::toWorkstation)
                .map(workstation -> {
                    Float distance = Float.MAX_VALUE;
                    if (workstation.getCoordinateId() != null) {
                        CoordinatePO coordinatePO = coordinateMap.get(workstation.getCoordinateId());
                        if (coordinatePO != null) {
                            distance = (float) Math.sqrt(
                                    Math.pow(coordinatePO.getX() - x, 2) +
                                            Math.pow(coordinatePO.getY() - y, 2)
                            );
                        }
                    }
                    return new AbstractMap.SimpleEntry<Workstation, Float>(workstation, distance);
                })
                .sorted(Map.Entry.comparingByValue())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
