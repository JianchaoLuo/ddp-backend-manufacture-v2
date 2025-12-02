package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.cooling;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Position;

import java.util.List;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/8/1 0:40
 */
public class CoolingSimulationTools {
    /**
     * 判断冷却区的半区是否有冷却完成的产品
     * @param halfPositions 冷却区的半区点位列表
     * @return true表示存在冷却完成可搬运的点位，false表示不存在
     */
    public boolean judgeCoolingHalfIsFinish(List<List<Position>> halfPositions) {
        for (List<Position> upPosition : halfPositions) {
            for (int i = upPosition.size() - 1; i >= 0; i--) {
                if (upPosition.get(i).getStatus().equals(PositionStatusEnum.FINISH_COOLING))
                    return true;

                if (upPosition.get(i).getStatus().equals(PositionStatusEnum.OCCUPIED))
                    break;
            }
        }

        return false;
    }

    /**
     * 判断冷却区的母车应该前往哪条冷却轨道
     * @param halfPositions 冷却区的半区点位列表
     * @return 应前往冷却区半区的轨道标识，-1表示半区所有轨道不可去
     */
    public int judgeCoolingCarGoWhichPath(List<List<Position>> halfPositions) {
        for (int i = 0; i < halfPositions.size(); i++) {
            for (int j = halfPositions.get(i).size() - 1; j >= 0; j--) {
                if (halfPositions.get(i).get(j).getStatus().equals(PositionStatusEnum.FINISH_COOLING))
                    return i;

                if (halfPositions.get(i).get(j).getStatus().equals(PositionStatusEnum.OCCUPIED))
                    break;
            }
        }

        return -1;
    }

    /**
     * 判断冷却区的母车应该前往哪个冷却点位
     * @param halfositions 冷却区的半区点位列表
     * @param pathIndex 车辆所在的半区轨道标识
     * @return 应前往冷却点位标识，-1表示轨道上所有的冷却点位不可去
     */
    public int judgeCoolingCarGoWhichPosition(List<List<Position>> halfositions, int pathIndex) {
        if (pathIndex == -1){
            return -1;
        }

        for (int i = halfositions.get(pathIndex).size() - 1; i >= 0; i--) {
            if (halfositions.get(pathIndex).get(i).getStatus().equals(PositionStatusEnum.FINISH_COOLING))
                return i;
        }

        return -1;
    }
}
