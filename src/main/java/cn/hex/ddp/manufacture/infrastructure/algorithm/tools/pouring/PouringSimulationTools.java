package cn.hex.ddp.manufacture.infrastructure.algorithm.tools.pouring;

import cn.hex.ddp.manufacture.domain.configuration.enums.PositionStatusEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Position;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.WorkStation;

import java.util.List;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/31 15:18
 */
public class PouringSimulationTools {
    /**
     * 判断待浇筑区是否被占用
     * @param halfPositions 半区待浇筑区
     * @return true表示有产品占用，false表示上半区全空
     */
    public boolean judgeWaitingPourIsEmpty(List<List<Position>> halfPositions) {
        for (List<Position> positions : halfPositions) {
            for (Position position : positions) {
                if (position.getStatus().equals(PositionStatusEnum.OCCUPIED))
                    return true;
            }
        }

        return false;
    }

    /**
     * 判断前往待浇筑区的哪条轨道
     * @param halfPositions 半区待浇筑区
     * @return 轨道标识，-1表示出错
     */
    public int judgePouringCarGoWhichPath(List<List<Position>> halfPositions) {
        for (int i = 0; i < halfPositions.size(); i++) {
            for (Position position : halfPositions.get(i)) {
                if (position.getStatus().equals(PositionStatusEnum.OCCUPIED))
                    return i;
            }
        }

        return -1;
    }

    /**
     * 判断前往浇筑区的哪个点位
     * @param halfPositions 半区待浇筑区
     * @return 点位标识，-1表示出错
     */
    public int judgePouringCarGoWhichPosition(List<List<Position>> halfPositions, int index) {
        if (index == -1){
            return -1;
        }

        for (int i = halfPositions.get(index).size() - 1; i >= 0; i--) {
            if (halfPositions.get(index).get(i).getStatus().equals(PositionStatusEnum.OCCUPIED))
                return i;
        }

        return -1;
    }

    /**
     * 判断空载车辆是否需要前往工岗
     * @param workingStations 工岗列表
     * @return true表示需要前往工岗，false表示不需要
     */
    public boolean judgeIsGoWhichWorkStation(List<WorkStation> workingStations) {
        for (WorkStation workStation : workingStations) {
            if (workStation.getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT))
                return true;
        }

        return false;
    }

    /**
     * 判断空载车辆需要前往哪个工岗
     * @param workingStations 工岗列表
     * @param input 浇筑区的仿真输入标识
     * @return 工岗的位置标识，-1标识所有工岗均不存在待搬运状态
     */
    public Integer judgeGoWhichWorkStation(List<WorkStation> workingStations, PouringIndexInput input) {
        if (workingStations.get(input.getPouringWorkStationIndex_1()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT)){

            return input.getPouringWorkStationIndex_1();
        }else if (workingStations.get(input.getPouringWorkStationIndex_2()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT)){

            return input.getPouringWorkStationIndex_2();
        }else if (workingStations.get(input.getPouringWorkStationIndex_3()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT)){

            return input.getPouringWorkStationIndex_3();
        }else if (workingStations.get(input.getPouringWorkStationIndex_4()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT)){

            return input.getPouringWorkStationIndex_4();
        }else if (workingStations.get(input.getPouringWorkStationIndex_5()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT)){

            return input.getPouringWorkStationIndex_5();
        }else if (workingStations.get(input.getPouringWorkStationIndex_6()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT)){

            return input.getPouringWorkStationIndex_6();
        }else if (workingStations.get(input.getPouringWorkStationIndex_7()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT)){

            return input.getPouringWorkStationIndex_7();
        }else if (workingStations.get(input.getPouringWorkStationIndex_8()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT)){

            return input.getPouringWorkStationIndex_8();
        }else if (workingStations.get(input.getPouringWorkStationIndex_9()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT)){

            return input.getPouringWorkStationIndex_9();
        }else if (workingStations.get(input.getPouringWorkStationIndex_10()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT)){

            return input.getPouringWorkStationIndex_10();
        }else if (workingStations.get(input.getPouringWorkStationIndex_11()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT)){

            return input.getPouringWorkStationIndex_11();
        }else if (workingStations.get(input.getPouringWorkStationIndex_12()).getStatus().equals(WorkstationStatusEnum.WAIT_TRANSPORT)){

            return input.getPouringWorkStationIndex_12();
        }

        return -1;
    }

    /**
     * 判断是否存在空闲的工岗
     * @param workingStations 工岗列表
     * @return true表示存在空闲的工岗，false表示不存在
     */
    public boolean judgeIsExistFreeWorkStation(List<WorkStation> workingStations) {
        for (WorkStation workStation : workingStations) {
            if (workStation.getStatus().equals(WorkstationStatusEnum.FREE))
                return true;
        }

        return false;
    }

    /**
     * 判断冷却区是否可占用
     * @param halfPositions 半区冷却区
     * @return true表示可以占用，false表示全被占用
     */
    public boolean judgeCoolingIsEmpty(List<List<Position>> halfPositions) {
        for (List<Position> positions : halfPositions) {
            //判断最左侧的点位是否被占用，若所有左侧点位被占用，则半区被占满
            if (positions.getFirst().getStatus().equals(PositionStatusEnum.UNOCCUPIED))
                return true;
        }

        return false;
    }

    /**
     * 判断前往冷却区的哪条轨道
     * @param halfPositions 半区冷却区
     * @return 轨道标识，-1表示出错
     */
    public int judgeCoolingCarGoWhichPath(List<List<Position>> halfPositions) {
        for (int i = 0; i < halfPositions.size(); i++) {
            //判断最左边的点位是否被占用，如果被占用则这条轨道的点位不可放置，否则可以放置
            if (halfPositions.get(i).getFirst().getStatus().equals(PositionStatusEnum.UNOCCUPIED))
                return i;
        }

        return -1;
    }

    /**
     * 判断前往冷却区的哪个点位
     * @param halfPositions 半区冷却区
     * @return 点位标识，-1表示出错
     */
    public int judgeCoolingCarGoWhichPosition(List<List<Position>> halfPositions, int index) {
        if (index == -1){
            return -1;
        }

        //找到可以放置的点位，若点位被占用，则在其之后的点位不可用
        for (int i = 0; i < halfPositions.get(index).size(); i++) {
            if (halfPositions.get(index).get(i).getStatus().equals((PositionStatusEnum.OCCUPIED)) ||
                    halfPositions.get(index).get(i).getStatus().equals(PositionStatusEnum.FINISH_COOLING))
                return i - 1;

            if (i == halfPositions.get(index).size() - 1)
                return i;
        }

        return -1;
    }

    /**
     * 判断满载车辆应前往哪个工岗
     * @param workingStations 工岗列表
     * @param input 浇筑区的仿真输入标识
     * @return 工岗的位置标识，-1标识所有工岗均不存在空闲状态
     */
    public Integer judgeFullGoWhichWorkStation(List<WorkStation> workingStations, PouringIndexInput input) {
        if (workingStations.get(input.getPouringWorkStationIndex_1()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getPouringWorkStationIndex_1();
        }else if (workingStations.get(input.getPouringWorkStationIndex_2()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getPouringWorkStationIndex_2();
        }else if (workingStations.get(input.getPouringWorkStationIndex_3()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getPouringWorkStationIndex_3();
        }else if (workingStations.get(input.getPouringWorkStationIndex_4()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getPouringWorkStationIndex_4();
        }else if (workingStations.get(input.getPouringWorkStationIndex_5()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getPouringWorkStationIndex_5();
        }else if (workingStations.get(input.getPouringWorkStationIndex_6()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getPouringWorkStationIndex_6();
        }else if (workingStations.get(input.getPouringWorkStationIndex_7()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getPouringWorkStationIndex_7();
        }else if (workingStations.get(input.getPouringWorkStationIndex_8()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getPouringWorkStationIndex_8();
        }else if (workingStations.get(input.getPouringWorkStationIndex_9()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getPouringWorkStationIndex_9();
        }else if (workingStations.get(input.getPouringWorkStationIndex_10()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getPouringWorkStationIndex_10();
        }else if (workingStations.get(input.getPouringWorkStationIndex_11()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getPouringWorkStationIndex_11();
        }else if (workingStations.get(input.getPouringWorkStationIndex_12()).getStatus().equals(WorkstationStatusEnum.FREE)){

            return input.getPouringWorkStationIndex_12();
        }

        return -1;
    }
}
