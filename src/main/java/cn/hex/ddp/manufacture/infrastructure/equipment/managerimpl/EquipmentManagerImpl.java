package cn.hex.ddp.manufacture.infrastructure.equipment.managerimpl;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.equipment.rest.req.ListEquipmentReq;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.configuration.manager.ConfigurationManager;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentNameEnum;
import cn.hex.ddp.manufacture.domain.equipment.manager.EquipmentManager;
import cn.hex.ddp.manufacture.domain.equipment.model.Equipment;
import cn.hex.ddp.manufacture.infrastructure.car.persistence.po.CarPO;
import cn.hex.ddp.manufacture.infrastructure.common.po.BaseDatabasePO;
import cn.hex.ddp.manufacture.infrastructure.equipment.managerimpl.converter.EquipmentInfrastructureConverter;
import cn.hex.ddp.manufacture.infrastructure.equipment.persistence.po.EquipmentPO;
import cn.hex.ddp.manufacture.infrastructure.equipment.persistence.po.EquipmentParameterPO;
import cn.hex.ddp.manufacture.infrastructure.equipment.persistence.postgresql.repository.EquipmentPORepository;
import cn.hex.ddp.manufacture.infrastructure.equipment.persistence.postgresql.repository.EquipmentParameterPORepository;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import cn.hex.ddp.manufacture.domain.configuration.model.Coordinate;


import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhaolin
 * @date 2024/5/15
 */
@Repository
public class EquipmentManagerImpl implements EquipmentManager {
    @Autowired
    private EquipmentPORepository equipmentPORepository;
    @Autowired
    private EquipmentParameterPORepository equipmentParameterPORepository;
    @Autowired
    private EquipmentInfrastructureConverter converter;
    @Autowired
    private ConfigurationManager configurationManager;

    @Override
    public PageResult<Equipment> listEquipment(ListEquipmentReq listEquipmentReq) {
        Page<EquipmentPO> page = equipmentPORepository.page(
                listEquipmentReq.toPage(),
                Wrappers.lambdaQuery(EquipmentPO.class)
                        .like(StringUtils.isNotEmpty(listEquipmentReq.getEquipmentNo()), EquipmentPO::getEquipmentNo,
                                listEquipmentReq.getEquipmentNo())
                        .like(StringUtils.isNotEmpty(listEquipmentReq.getModel()), EquipmentPO::getModel,
                                listEquipmentReq.getModel())
                        .like(StringUtils.isNotEmpty(listEquipmentReq.getName()), EquipmentPO::getName,
                                listEquipmentReq.getName())
                        .eq(ObjectUtils.isNotNull(listEquipmentReq.getType()), EquipmentPO::getType,
                                listEquipmentReq.getType())
                        .orderByDesc(EquipmentPO::getId)
        );
        if (CollUtil.isEmpty(page.getRecords())) {
            return PageResult.emptyResult();
        }
        List<Long> equipmentIds = page.getRecords().stream().map(BaseDatabasePO::getId).toList();
        Map<Long, List<EquipmentParameterPO>> equipmentParameterMap =
                equipmentParameterPORepository.list(new QueryWrapper<EquipmentParameterPO>().lambda().in(EquipmentParameterPO::getEquipmentId, equipmentIds))
                        .stream()
                        .collect(Collectors.groupingBy(EquipmentParameterPO::getEquipmentId));
        return PageResult.of(page,
                converter.toEquipment(page.getRecords(), equipmentParameterMap));
    }

    @Override
    public void createEquipment(Equipment equipment) {
        EquipmentPO equipmentPO = converter.toEquipmentPO(equipment);
        equipmentPORepository.save(equipmentPO);
        List<EquipmentParameterPO> equipmentParameterPOS = converter.toEquipmentParameterPO(equipment.getParameters()
                , equipmentPO.getId());
        if (CollUtil.isEmpty(equipmentParameterPOS)) {
            return;
        }
        equipmentParameterPORepository.saveBatch(equipmentParameterPOS);
    }

    @Override
    public void deleteEquipment(Long id) {
        equipmentPORepository.removeById(id);
        equipmentParameterPORepository.remove(new QueryWrapper<EquipmentParameterPO>().lambda().eq(EquipmentParameterPO::getEquipmentId, id));
    }

    @Override
    public void updateEquipment(Long id, Equipment equipment) {
        EquipmentPO equipmentPO = converter.toEquipmentPO(equipment);
        equipmentPO.setId(id);
        equipmentPORepository.updateById(equipmentPO);
        equipmentParameterPORepository.remove(new QueryWrapper<EquipmentParameterPO>().lambda().eq(EquipmentParameterPO::getEquipmentId, id));
        List<EquipmentParameterPO> equipmentParameterPOS = converter.toEquipmentParameterPO(equipment.getParameters()
                , id);
        if (CollUtil.isEmpty(equipmentParameterPOS)) {
            return;
        }
        equipmentParameterPORepository.saveBatch(equipmentParameterPOS);
    }

    @Override
    public Equipment getEquipment(Long id) {
        EquipmentPO equipmentPO = equipmentPORepository.getById(id);
        List<EquipmentParameterPO> equipmentParameterPOS =
                equipmentParameterPORepository.list(new QueryWrapper<EquipmentParameterPO>().lambda().eq(EquipmentParameterPO::getEquipmentId, id));
        return converter.toEquipment(equipmentPO, equipmentParameterPOS);
    }

    @Override
    public List<Equipment> getEquipmentsByPlcId(long plcID) {
        List<EquipmentPO> EquipmentPOList = equipmentPORepository.lambdaQuery()
                .eq(ObjectUtils.isNotNull(plcID), EquipmentPO::getPlcId, plcID)
                .list();

        return converter.toEquipments(EquipmentPOList);
    }

    @Override
    public Equipment getEquipmentByNo(String equipmentNo) {
        EquipmentPO equipmentPO = equipmentPORepository.getOne(Wrappers.lambdaQuery(EquipmentPO.class)
                .eq(EquipmentPO::getEquipmentNo, equipmentNo));
        if (equipmentPO == null) {
            return null;
        }
        List<EquipmentParameterPO> equipmentParameterPOS = equipmentParameterPORepository.list(
                new QueryWrapper<EquipmentParameterPO>().lambda()
                        .eq(EquipmentParameterPO::getEquipmentId, equipmentPO.getId()));
        return converter.toEquipment(equipmentPO, equipmentParameterPOS);
    }

    @Override
    public Equipment getEquipmentByNameEnum(EquipmentNameEnum nameEnum) {
        EquipmentPO equipmentPO = equipmentPORepository.getOne(Wrappers.lambdaQuery(EquipmentPO.class)
                .eq(EquipmentPO::getNameEnum, nameEnum));
        if (equipmentPO == null) {
            return null;
        }

        List<EquipmentParameterPO> equipmentParameterPOS = equipmentParameterPORepository.list(
                new QueryWrapper<EquipmentParameterPO>().lambda()
                        .eq(EquipmentParameterPO::getEquipmentId, equipmentPO.getId()));
        return converter.toEquipment(equipmentPO, equipmentParameterPOS);
    }

    @Override
    public Map<Long, List<Long>> getEquipmentIdListByPLCIds(List<Long> plcIds) {
        if (CollectionUtil.isEmpty(plcIds)) {
            return Map.of();
        }
        List<EquipmentPO> equipmentList =
                equipmentPORepository.list(Wrappers.lambdaQuery(EquipmentPO.class).in(EquipmentPO::getPlcId, plcIds));
        // 使用Stream API进行分组，并将id收集到List<Long>中
        Map<Long, List<Long>> groupedByPlcId = equipmentList.stream()
                .collect(Collectors.groupingBy(
                        EquipmentPO::getPlcId,
                        Collectors.mapping(EquipmentPO::getId, Collectors.toList())
                ));
        return groupedByPlcId;
    }

    @Override
    public Map<Long, List<Equipment>> getEquipmentListByPLCIds(List<Long> plcIds) {
        if (CollectionUtil.isEmpty(plcIds)) {
            return Map.of();
        }
        List<EquipmentPO> equipmentList =
                equipmentPORepository.list(Wrappers.lambdaQuery(EquipmentPO.class).in(EquipmentPO::getPlcId, plcIds));
        List<Equipment> equipments = converter.toEquipments(equipmentList);
        Map<Long, List<Equipment>> groupedByPlcId = equipments.stream()
                .collect(Collectors.groupingBy(Equipment::getPlcId));
        return groupedByPlcId;
    }

    @Override
    public List<Equipment> getAllEquipment() {

        List<EquipmentPO> equipmentPOList = equipmentPORepository.list()
                .stream().sorted(Comparator.comparing(EquipmentPO::getId, Comparator.reverseOrder())).toList();
        if (CollUtil.isEmpty(equipmentPOList)) {
            return List.of();
        }
        List<Long> equipmentIds = equipmentPOList.stream().map(BaseDatabasePO::getId).toList();
        Map<Long, List<EquipmentParameterPO>> equipmentParameterMap =
                equipmentParameterPORepository.list(new QueryWrapper<EquipmentParameterPO>().lambda().in(EquipmentParameterPO::getEquipmentId, equipmentIds))
                        .stream()
                        .collect(Collectors.groupingBy(EquipmentParameterPO::getEquipmentId));
        return converter.toEquipment(equipmentPOList, equipmentParameterMap);
    }

    @Override
    public List<Equipment> getListByArea(AreaEnum area) {
        List<EquipmentPO> equipmentPOS = equipmentPORepository.list();
        if (CollUtil.isEmpty(equipmentPOS)) {
            return List.of();
        }

        List<Long> ids = equipmentPOS.stream()
                .map(EquipmentPO::getCoordinateId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        Map<Long, Coordinate> coordinateMap = configurationManager.getCoordinateMapByIds(ids);

        return equipmentPOS.stream()
                .map(po -> {
                    Equipment equipment = converter.toEquipment(po, null);

                    if (po.getCoordinateId() != null) {
                        equipment.setCoordinate(coordinateMap.get(po.getCoordinateId()));
                    }

                    return equipment;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Equipment> getNearEquipment(Float x, Float y) {
        // 参数校验
        if (x == null || y == null) {
            return List.of();
        }

        // 获取所有设备
        List<EquipmentPO> equipmentPOS = equipmentPORepository.list();
        if (CollUtil.isEmpty(equipmentPOS)) {
            return List.of();
        }

        // 获取所有相关的坐标ID
        List<Long> ids = equipmentPOS.stream()
                .map(EquipmentPO::getCoordinateId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        // 批量查询坐标信息
        Map<Long, Coordinate> coordinateMap = configurationManager.getCoordinateMapByIds(ids);

        // 创建一个临时的设备和距离的映射关系
        return equipmentPOS.stream()
                .map(po -> {
                    Equipment equipment = converter.toEquipment(po, null);

                    if (po.getCoordinateId() != null) {
                        equipment.setCoordinate(coordinateMap.get(po.getCoordinateId()));
                    }

                    return equipment;
                })
                .map(equipment -> new AbstractMap.SimpleEntry<Equipment, Float>(equipment,
                        calculateDistance(equipment.getCoordinate(), x, y)))
                .sorted(Map.Entry.comparingByValue())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // 辅助方法：计算设备与目标点的距离
    private Float calculateDistance(Coordinate coordinate, Float x, Float y) {
        if (coordinate == null) {
            return Float.MAX_VALUE;
        }

        return (float) Math.sqrt(
                Math.pow(coordinate.getX() - x, 2) +
                        Math.pow(coordinate.getY() - y, 2)
        );
    }
}
