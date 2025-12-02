package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.waitingPour;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Position;
import lombok.Data;

import java.util.List;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/25 16:40
 */
@Data
public class WaitingPourSimulationTools {
    /**
     * 判断待浇筑区是否可占用
     * @param halfPositions 半区待浇筑区
     * @return true表示可以占用，false表示全被占用
     */
    public boolean judgeWaitingPourIsEmpty(List<List<Position>> halfPositions) {
        for (List<Position> positions : halfPositions) {
            //判断最右边的点位是否被占用，如果被占用则这条轨道的点位不可放置，否则可以放置
            if (positions.getFirst().getStatus().equals(PositionStatusEnum.UNOCCUPIED))
                return true;
        }

        return false;
    }

    /**
     * 判断前往待浇筑区的哪条轨道
     * @param halfPositions 半区待浇筑区
     * @return 轨道标识，-1表示出错
     */
    public int judgeCarGoWhichPath(List<List<Position>> halfPositions) {
        for (int i = 0; i < halfPositions.size(); i++) {
            //判断最右边的点位是否被占用，如果被占用则这条轨道的点位不可放置，否则可以放置
            if (halfPositions.get(i).getFirst().getStatus().equals(PositionStatusEnum.UNOCCUPIED))
                return i;
        }

        return -1;
    }

    /**
     * 判断前往浇筑区的哪个点位
     * @param halfPositions 半区待浇筑区
     * @return 点位标识，-1表示出错
     */
    public int judgeCarGoWhichPosition(List<List<Position>> halfPositions, int index) {
        if (index == -1){
            return -1;
        }

        //找到可以放置的点位，若点位被占用，则在其之后的点位不可用
        for (int i = 0; i < halfPositions.get(index).size(); i++) {
            if (halfPositions.get(index).get(i).getStatus().equals(PositionStatusEnum.OCCUPIED))
                return i - 1;
            if (i == halfPositions.get(index).size() - 1)
                return i;
        }

        return -1;
    }
}
