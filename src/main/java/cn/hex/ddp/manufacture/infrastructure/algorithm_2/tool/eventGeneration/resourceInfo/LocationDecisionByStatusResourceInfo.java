package cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.eventGeneration.resourceInfo;

import java.util.List;

/**
 * @Author: bobo
 * @Description: 对于根据资源状态决定地点的节点，存储的资源相关信息
 * 例如：需要决策的事件是母车搭载一个带着下砂箱的子车前往哪个工岗，由可选工岗的状态决定，即哪个工岗处于空闲状态，就前往哪个工岗
 * @DateTime: 2025/5/22 21:10
 **/
public class LocationDecisionByStatusResourceInfo<R extends Enum<R>, F> {

    //分裂目标资源列表,根据地点状态决定地点时使用
    private List<R> splitResources;

    // 期望特征，根据地点状态决定地点时使用
    private F desiredFeature;

    // 构造根据状态决策地点的决策节点
    public LocationDecisionByStatusResourceInfo(List<R> splitResources, F desiredFeature) {
        this.splitResources = splitResources;
        this.desiredFeature = desiredFeature;
    }

    @Override
    public String toString(){
        return "LocationDecisionByStatusResourceInfo{" +
                "\nsplitResources=" + splitResources +
                "\ndesiredFeature=" + desiredFeature +
                "\n}";
    }
}
