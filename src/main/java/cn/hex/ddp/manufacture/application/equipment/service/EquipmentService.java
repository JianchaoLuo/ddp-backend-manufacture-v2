package cn.hex.ddp.manufacture.application.equipment.service;

import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.equipment.rest.req.CreateEquipmentReq;
import cn.hex.ddp.manufacture.api.equipment.rest.req.ListEquipmentReq;
import cn.hex.ddp.manufacture.api.equipment.rest.vo.EquipmentVO;
import cn.hex.ddp.manufacture.application.equipment.dto.EquipmentSummaryDTO;
import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;

import java.util.List;

/**
 * @author zhaolin
 * @date 2024/5/15
 */
public interface EquipmentService {
    PageResult<EquipmentVO> listEquipment(ListEquipmentReq listEquipmentReq);

    void createEquipment(CreateEquipmentReq createEquipmentReq);

    void deleteEquipment(Long id);

    void updateEquipment(Long id, CreateEquipmentReq createEquipmentReq);

    EquipmentVO getEquipment(Long id);

    List<EquipmentVO> getAllEquipment();

    List<EquipmentSummaryDTO> getListByArea(AreaEnum area);
}
