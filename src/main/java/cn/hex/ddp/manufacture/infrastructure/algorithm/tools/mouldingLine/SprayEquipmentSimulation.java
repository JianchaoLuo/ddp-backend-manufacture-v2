package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.mouldingLine;

import lombok.Data;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/11 16:35
 */
@Data
public class SprayEquipmentSimulation {
    private double SprayTime = 60 * 6;

    private double workTime = 0.0;

    private boolean workFlag = false;

    public boolean SprayEquipmentWorkSimulation(double dTime) {
        workTime = workTime + dTime;
        if (workTime > SprayTime) {
            workTime = 0.0;

            return true;
        }

        return false;
    }
}
