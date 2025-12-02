package cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.eventGeneration.decisionTreeNode;

import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.eventGeneration.resourceInfo.LocationDecisionByCoordinateResourceInfo;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.eventGeneration.resourceInfo.MainBodyJudgeResourceInfo;

import java.util.Map;

/**
 * @Author: bobo
 * @Description: 根据坐标做地点决策节点。比如决策的事件为子车前往合箱工岗，子车的坐标在那个合箱工岗路口则去哪个工岗
 * @DateTime: 2025/5/22 21:04
 **/
public class LocationDecisionByCoordinateNode<R extends Enum<R>, F> extends DecisionTreeNode<R, F> {

    /**
     * 对于根据坐标做地点决策的节点，节点存储的资源相关信息
     */
    private LocationDecisionByCoordinateResourceInfo<R, F> locationDecisionByCoordinateResourceInfo;

    // 构造根据坐标做地点决策的节点
    public LocationDecisionByCoordinateNode(R splitResource, Class<F> splitFeatureType, Map<F, Object> coordinateToLocation) {
        super(splitFeatureType);
        this.locationDecisionByCoordinateResourceInfo = new LocationDecisionByCoordinateResourceInfo<>(splitResource,coordinateToLocation);
    }
}
