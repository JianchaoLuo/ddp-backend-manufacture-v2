package cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.eventGeneration.decisionTreeNode;

import cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums.decisionTree.DecisionTreeNodeType;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.eventGeneration.DecisionResult;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.eventGeneration.resourceInfo.LocationDecisionByCoordinateResourceInfo;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.eventGeneration.resourceInfo.LocationDecisionByStatusResourceInfo;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.EventName;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: bobo
 * @Description: 根据状态做地点决策节点。比如决策的事件为子母车前往合箱工岗路口，工岗的状态需要
 * @DateTime: 2025/5/22 22:17
 **/
public class LocationDecisionByStatusNode<R extends Enum<R>, F> extends DecisionTreeNode<R, F> {

    /**
     * 对于根据状态做地点决策的节点，节点存储的资源相关信息
     */
    private LocationDecisionByStatusResourceInfo<R, F> locationDecisionByStatusResourceInfo;

    // 构造地点决策节点,根据状态决策地点
    public LocationDecisionByStatusNode(List<R> splitResources, F desiredFeature, Class<F> splitFeatureType) {
        super(splitFeatureType);
        this.locationDecisionByStatusResourceInfo = new LocationDecisionByStatusResourceInfo<>(splitResources, desiredFeature);
    }

}
