package cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.eventGeneration.resourceInfo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @Author: bobo
 * @Description: 对于根据坐标决策地点的节点，存储的资源相关信息
 * 例如：需要决策的事件是子车进入哪个工岗，由子车的坐标决定，即子车当前在哪个工岗的路口，就进入哪个工岗
 * @DateTime: 2025/3/21 12:41
 **/
@Data
@NoArgsConstructor
public class LocationDecisionByCoordinateResourceInfo<R extends Enum<R>, F> {

    //分裂目标资源,根据坐标决策地点时使用
    private R splitResource;

    //坐标和地点的映射关系,根据坐标决策地点时使用
    private Map<F,Object> coordinateToLocation;

    // 构造根据坐标决策地点的决策节点
    public LocationDecisionByCoordinateResourceInfo(R splitResource, Map<F, Object> coordinateToLocation) {
        this.splitResource = splitResource;
        this.coordinateToLocation = coordinateToLocation;
    }

    @Override
    public String toString() {
        return "LocationDecisionByCoordinateResourceInfo{" +
                "\nsplitResource=" + splitResource +
                "\ncoordinateToLocation=" + coordinateToLocation +
                "\n}";
    }
}
