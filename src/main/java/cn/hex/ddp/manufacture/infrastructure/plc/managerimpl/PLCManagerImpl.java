package cn.hex.ddp.manufacture.infrastructure.plc.managerimpl;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.plc.rest.req.ListPLCReq;
import cn.hex.ddp.manufacture.api.plc.rest.vo.PLCSummaryVO;
import cn.hex.ddp.manufacture.domain.car.enums.CarTypeEnum;
import cn.hex.ddp.manufacture.domain.plc.manager.PLCManager;
import cn.hex.ddp.manufacture.domain.plc.model.PLC;
import cn.hex.ddp.manufacture.domain.plc.model.PlcWithResources;
import cn.hex.ddp.manufacture.domain.plc.model.ResourceSummary;
import cn.hex.ddp.manufacture.infrastructure.car.persistence.po.CarPO;
import cn.hex.ddp.manufacture.infrastructure.car.persistence.postgresql.repository.CarPORepository;
import cn.hex.ddp.manufacture.infrastructure.configuration.persistence.po.PositionPO;
import cn.hex.ddp.manufacture.infrastructure.configuration.persistence.postgresql.repository.PositionPORepository;
import cn.hex.ddp.manufacture.infrastructure.equipment.persistence.po.EquipmentPO;
import cn.hex.ddp.manufacture.infrastructure.equipment.persistence.postgresql.repository.EquipmentPORepository;
import cn.hex.ddp.manufacture.infrastructure.path.persistence.po.PathPO;
import cn.hex.ddp.manufacture.infrastructure.path.persistence.postgresql.repository.PathPORepository;
import cn.hex.ddp.manufacture.infrastructure.plc.managerimpl.converter.PLCInfrastructureConverter;
import cn.hex.ddp.manufacture.infrastructure.plc.persistence.po.PLCPO;
import cn.hex.ddp.manufacture.infrastructure.plc.persistence.po.PLCResourcePO;
import cn.hex.ddp.manufacture.infrastructure.plc.persistence.postgresql.repository.PLCPORepository;
import cn.hex.ddp.manufacture.infrastructure.plc.persistence.postgresql.repository.PLCResourcePORepository;
import cn.hex.ddp.manufacture.infrastructure.workstation.persistence.po.WorkstationPO;
import cn.hex.ddp.manufacture.infrastructure.workstation.persistence.postgresql.repository.WorkstationPORepository;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
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
public class PLCManagerImpl implements PLCManager {

    @Autowired
    private PLCPORepository plcPORepository;

    @Autowired
    private PLCInfrastructureConverter converter;

    @Autowired
    private CarPORepository carPORepository;

    @Autowired
    private PLCResourcePORepository plcResourcePORepository;

    @Autowired
    private WorkstationPORepository workstationPORepository;

    @Autowired
    private EquipmentPORepository equipmentPORepository;

    @Autowired
    private PositionPORepository positionPORepository;

    @Autowired
    private PathPORepository pathPORepository;

    @Override
    public PageResult<PLC> listPLC(ListPLCReq listPLCReq) {
        Page<PLCPO> page = plcPORepository.page(
                listPLCReq.toPage(),
                Wrappers.lambdaQuery(PLCPO.class)
                        .like(StringUtils.isNotEmpty(listPLCReq.getIp()), PLCPO::getIp, listPLCReq.getIp())
                        .eq(ObjectUtils.isNotNull(listPLCReq.getType()), PLCPO::getType, listPLCReq.getType())
                        .eq(ObjectUtils.isNotNull(listPLCReq.getArea()), PLCPO::getArea, listPLCReq.getArea())
                        .orderByDesc(PLCPO::getId)
        );
        if (CollUtil.isEmpty(page.getRecords())) {
            return PageResult.emptyResult();
        }
        return PageResult.of(page,
                converter.poToPLC(page.getRecords()));
    }

    @Override
    public void createPLC(PLC plc) {
        PLCPO plcPO = converter.toPLCPO(plc);
        plcPORepository.save(plcPO);
        plc.setId(plcPO.getId());
    }

    @Override
    public void updatePLC(Long id, PLC plc) {
        PLCPO plcPO = converter.toPLCPO(plc);
        plcPO.setId(id);
        plcPORepository.updateById(plcPO);
    }

    @Override
    public void deletePLC(Long id) {
        plcPORepository.removeById(id);

        // todo 删除关联的Equipment和EquipmentParameter
    }

    @Override
    public Map<Long, PLCSummaryVO> getPLCSummaryVOMap(List<Long> plcIdList) {
        if (CollectionUtils.isEmpty(plcIdList)) {
            return Collections.emptyMap();
        }
        List<PLCPO> plcPOs = plcPORepository.listByIds(plcIdList);
        return plcPOs.stream().collect(Collectors.toMap(PLCPO::getId, converter::toPLCSummaryVO, (k1, k2) -> k1));
    }

    @Override
    public PLC getPLCbyId(Long plcId) {
        return converter.poToPLC(plcPORepository.getById(plcId));
    }

    // 插入 PLC 关联资源记录
    @Override
    public void insertPLCResource(Long plcId, Long resourceId) {
        PLCResourcePO plcResourcePO = new PLCResourcePO();
        plcResourcePO.setPLCID(plcId);
        plcResourcePO.setResourceId(resourceId);
        plcResourcePORepository.save(plcResourcePO);
    }

    @Override
    public void updatePLCResource(Long plcId, Long resourceId) {
        PLCResourcePO plcResourcePO = plcResourcePORepository.getOne(
                Wrappers.lambdaQuery(PLCResourcePO.class)
                        .eq(PLCResourcePO::getPLCID, plcId)
        );
        if (plcResourcePO != null) {
            plcResourcePO.setResourceId(resourceId);
            plcResourcePORepository.updateById(plcResourcePO);
        }
    }

    @Override
    public PLCResourcePO getPLCResourceByPlcId(Long plcId) {
        return plcResourcePORepository.getOne(
                Wrappers.lambdaQuery(PLCResourcePO.class)
                        .eq(PLCResourcePO::getPLCID, plcId)
        );
    }


    // 按 PLC 获取资源摘要信息
    @Override
    public List<ResourceSummary> getResourcesByPlcId(Long id) {
        List<PLCResourcePO> bindings = plcResourcePORepository.list(
                Wrappers.lambdaQuery(PLCResourcePO.class)
                        .eq(PLCResourcePO::getPLCID, id)
        );
        if (CollectionUtils.isEmpty(bindings)) {
            return Collections.emptyList();
        }
        Set<Long> resourceIds = bindings.stream().map(PLCResourcePO::getResourceId).collect(Collectors.toSet());
        return buildResourceSummaries(resourceIds);
    }

    // 查询全部 PLC，并返回按 PLCID 分组的资源摘要信息（仅资源，不含PLC基础信息）v1
//    @Override
//    public Map<Long, List<ResourceSummary>> getResourcesGroupedByPlc() {
//        List<PLCResourcePO> all = plcResourcePORepository.list();
//        if (CollectionUtils.isEmpty(all)) {
//            return Collections.emptyMap();
//        }
//        Map<Long, Set<Long>> plcToIds = all.stream().collect(
//                Collectors.groupingBy(PLCResourcePO::getPLCID,
//                        Collectors.mapping(PLCResourcePO::getResourceId, Collectors.toSet()))
//        );
//        // 减少多次 DB 调用，先构建全集映射
//        Set<Long> allIds = all.stream().map(PLCResourcePO::getResourceId).collect(Collectors.toSet());
//        Map<Long, ResourceSummary> idToSummary = indexById(buildResourceSummaries(allIds));
//        Map<Long, List<ResourceSummary>> result = new HashMap<>();
//        for (Map.Entry<Long, Set<Long>> e : plcToIds.entrySet()) {
//            List<ResourceSummary> list = e.getValue().stream()
//                    .map(idToSummary::get)
//                    .filter(Objects::nonNull)
//                    .collect(Collectors.toList());
//            result.put(e.getKey(), list);
//        }
//        return result;
//    }

    // 查询全部 PLC 的聚合信息 v2
    @Override
    public List<PlcWithResources> listPlcWithResources() {
        // 1) 全量 PLC
        List<PLCPO> plcList = plcPORepository.list();
        if (CollectionUtils.isEmpty(plcList)) {
            return Collections.emptyList();
        }
        // 2) 全量绑定
        List<PLCResourcePO> all = plcResourcePORepository.list();
        Map<Long, Set<Long>> plcToIds = all.stream().collect(
                Collectors.groupingBy(PLCResourcePO::getPLCID,
                        Collectors.mapping(PLCResourcePO::getResourceId, Collectors.toSet()))
        );
        // 3) 统一批量查询资源并索引
        Set<Long> allIds = all.stream().map(PLCResourcePO::getResourceId).collect(Collectors.toSet());
        Map<Long, ResourceSummary> idToSummary = indexById(buildResourceSummaries(allIds));
        // 4) 组装返回
        List<cn.hex.ddp.manufacture.domain.plc.model.PlcWithResources> result = new ArrayList<>();
        for (PLCPO plcPO : plcList) {
            cn.hex.ddp.manufacture.domain.plc.model.PlcWithResources agg = new cn.hex.ddp.manufacture.domain.plc.model.PlcWithResources();
            agg.setId(plcPO.getId());
            agg.setIp(plcPO.getIp());
            agg.setType(plcPO.getType());
            agg.setArea(plcPO.getArea());
            List<ResourceSummary> resources = plcToIds.getOrDefault(plcPO.getId(), Collections.emptySet())
                    .stream().map(idToSummary::get).filter(Objects::nonNull).collect(Collectors.toList());
            agg.setResources(resources);
            result.add(agg);
        }
        return result;
    }

    // 新增：分页查询 PLC 及资源
    @Override
    public PageResult<PlcWithResources> listPlcWithResources(ListPLCReq req) {
        Page<PLCPO> page = plcPORepository.page(
                req.toPage(),
                Wrappers.lambdaQuery(PLCPO.class)
                        .like(StringUtils.isNotEmpty(req.getIp()), PLCPO::getIp, req.getIp())
                        .eq(ObjectUtils.isNotNull(req.getType()), PLCPO::getType, req.getType())
                        .eq(ObjectUtils.isNotNull(req.getArea()), PLCPO::getArea, req.getArea())
                        .orderByDesc(PLCPO::getId)
        );
        if (CollUtil.isEmpty(page.getRecords())) {
            return PageResult.emptyResult();
        }

        List<PLCPO> plcPageList = page.getRecords();
        Set<Long> plcIds = plcPageList.stream().map(PLCPO::getId).collect(Collectors.toSet());

        List<PLCResourcePO> bindings = plcResourcePORepository.list(
                Wrappers.lambdaQuery(PLCResourcePO.class)
                        .in(PLCResourcePO::getPLCID, plcIds)
        );
        Map<Long, Set<Long>> plcToIds = bindings.stream().collect(
                Collectors.groupingBy(PLCResourcePO::getPLCID,
                        Collectors.mapping(PLCResourcePO::getResourceId, Collectors.toSet()))
        );

        Set<Long> allResourceIds = bindings.stream().map(PLCResourcePO::getResourceId).collect(Collectors.toSet());
        Map<Long, ResourceSummary> idToSummary = indexById(buildResourceSummaries(allResourceIds));

        List<PlcWithResources> aggs = new ArrayList<>();
        for (PLCPO plcPO : plcPageList) {
            PlcWithResources agg = new PlcWithResources();
            agg.setId(plcPO.getId());
            agg.setIp(plcPO.getIp());
            agg.setType(plcPO.getType());
            agg.setArea(plcPO.getArea());
            List<ResourceSummary> resources = plcToIds.getOrDefault(plcPO.getId(), Collections.emptySet())
                    .stream().map(idToSummary::get).filter(Objects::nonNull).collect(Collectors.toList());
            agg.setResources(resources);
            aggs.add(agg);
        }
        return PageResult.of(page, aggs);
    }

    private Map<Long, ResourceSummary> indexById(List<ResourceSummary> list) {
        return list.stream().collect(Collectors.toMap(ResourceSummary::getResourceId, Function.identity(), (a, b) -> a));
    }

    private List<ResourceSummary> buildResourceSummaries(Set<Long> resourceIds) {
        if (CollectionUtils.isEmpty(resourceIds)) return Collections.emptyList();
        // 各类型批量查询（不会命中则返回空列表）
        List<CarPO> cars = carPORepository.listByIds(resourceIds);
        List<WorkstationPO> wss = workstationPORepository.listByIds(resourceIds);
        List<PositionPO> poss = positionPORepository.listByIds(resourceIds);
        List<EquipmentPO> eqs = equipmentPORepository.listByIds(resourceIds);
        List<PathPO> paths = pathPORepository.listByIds(resourceIds);

        List<ResourceSummary> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(cars)) {
            for (CarPO po : cars) {
                ResourceSummary s = new ResourceSummary();
                s.setResourceId(po.getId());
                // 根据车辆类型细分子车/母车
                CarTypeEnum type = po.getType();
                String rt = (type == CarTypeEnum.SUB_CAR) ? "SUB_CAR" : (type == CarTypeEnum.FERRY_CAR ? "FERRY_CAR" : "CAR");
                s.setResourceType(rt);
                s.setResourceName(po.getName());
                result.add(s);
            }
        }
        if (CollectionUtils.isNotEmpty(wss)) {
            for (WorkstationPO po : wss) {
                ResourceSummary s = new ResourceSummary();
                s.setResourceId(po.getId());
                s.setResourceType("WORKSTATION");
                s.setResourceName(po.getName());
                result.add(s);
            }
        }
        if (CollectionUtils.isNotEmpty(poss)) {
            for (PositionPO po : poss) {
                ResourceSummary s = new ResourceSummary();
                s.setResourceId(po.getId());
                s.setResourceType("POSITION");
                s.setResourceName(po.getName());
                result.add(s);
            }
        }
        if (CollectionUtils.isNotEmpty(eqs)) {
            for (EquipmentPO po : eqs) {
                ResourceSummary s = new ResourceSummary();
                s.setResourceId(po.getId());
                s.setResourceType("EQUIPMENT");
                s.setResourceName(po.getName());
                result.add(s);
            }
        }
        if (CollectionUtils.isNotEmpty(paths)) {
            for (PathPO po : paths) {
                ResourceSummary s = new ResourceSummary();
                s.setResourceId(po.getId());
                s.setResourceType("PATH");
                s.setResourceName(po.getName());
                result.add(s);
            }
        }
        return result;
    }

    @Override
    // 删除某个PLC关联的所有资源
    public void deletePLCResourcesByPLCId(Long plcId) {
        plcResourcePORepository.remove(
                Wrappers.lambdaQuery(PLCResourcePO.class)
                        .eq(PLCResourcePO::getPLCID, plcId)
        );
    }
}
