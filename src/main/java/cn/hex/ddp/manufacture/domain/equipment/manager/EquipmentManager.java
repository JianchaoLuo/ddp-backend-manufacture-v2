package cn.hex.ddp.manufacture.domain.equipment.manager;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.equipment.rest.req.ListEquipmentReq;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentNameEnum;
import cn.hex.ddp.manufacture.domain.equipment.model.Equipment;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

/**
 * @author zhaolin
 * @date 2024/5/15
 */
public interface EquipmentManager {
    PageResult<Equipment> listEquipment(ListEquipmentReq listEquipmentReq);

    void createEquipment(Equipment equipment);

    void deleteEquipment(Long id);

    void updateEquipment(Long id, Equipment equipment);

    Equipment getEquipment(Long id);

    List<Equipment> getEquipmentsByPlcId(long plcID);

    Equipment getEquipmentByNo(String equipmentNo);

    Equipment getEquipmentByNameEnum(EquipmentNameEnum nameEnum);

    Map<Long, List<Long>> getEquipmentIdListByPLCIds(List<Long> plcIds);

    Map<Long, List<Equipment>> getEquipmentListByPLCIds(List<Long> plcIds);

    List<Equipment> getAllEquipment();

    List<Equipment> getListByArea(AreaEnum area);

    List<Equipment> getNearEquipment(@NotNull(message = "横坐标不能为空") Float x, @NotNull(message = "纵坐标不能为空") Float y);
}
