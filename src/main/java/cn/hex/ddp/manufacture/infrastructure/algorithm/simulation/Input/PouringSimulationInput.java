package cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.Input;

import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Ferry;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Path;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.SubCar;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.WorkStation;
import cn.hex.ddp.manufacture.infrastructure.algorithm.tools.pouring.PouringIndexInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/31 14:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PouringSimulationInput implements Serializable {
    /**
     * 浇筑区母车
     */
    Ferry pouringFerry;
    /**
     * 浇筑区子车
     */
    SubCar pouringSubCar;
    /**
     * 浇筑区工岗的列表
     */
    List<WorkStation> workingStations;
    /**
     * 浇筑区浇筑轨道
     */
    Path pouringPath;
    /**
     * 浇筑区的输入标识
     */
    PouringIndexInput pouringIndexInput;
    /**
     * 整体的总时间
     */
    double totalTime;
}
