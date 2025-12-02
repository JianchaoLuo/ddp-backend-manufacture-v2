package cn.hex.ddp.manufacture.api.configuration.rest.vo;

import cn.hex.ddp.manufacture.api.equipment.rest.vo.EquipmentVO;
import cn.hex.ddp.manufacture.api.workstation.rest.vo.WorkstationVO;
import cn.hex.ddp.manufacture.domain.configuration.model.Position;
import cn.hex.ddp.manufacture.domain.equipment.model.Equipment;
import cn.hex.ddp.manufacture.domain.workstation.model.Workstation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 获取目标附近点位返回结果
 *
 * @author 冯泽邦
 * @date 2025/7/28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NearPointsVO {
    /**
     * 工岗岗位列表
     */
    private List<Workstation> workStationList;

    /**
     * 设备点位列表
     */
    private List<Equipment> equipmentList;

    /**
     * 交互点位列表
     */
    private List<Position> positionList;
}
