package cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.eventGeneration.decisionTreeNode;

import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.eventGeneration.DecisionResult;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: bobo
 * @Description: 决策树节点,泛型R表示当前节点判断的资源类型，F表示特征的类型
 * @DateTime: 2025/3/19 14:38
 **/
@Data
@NoArgsConstructor
public abstract class DecisionTreeNode<R extends Enum<R>, F> {

//    // 该节点的类型
//    private DecisionTreeNodeType type;

//    // 对于地点决策节点，节点存储的资源相关信息
//    private LocationDecisionNodeResourceInfo<R,F> lDNodeResourceInfo;
//
//    // 对于主体判断节点，节点存储的资源相关信息
//    private MainBodyJudgeNodeResourceInfo<R> mBJNodeResourceInfo;

    // 分裂特征（作为子节点映射键的类型，比如工岗状态枚举类）
    private Class<F> splitFeatureType;

    // 子节点映射（键值 -> 子节点）
    private Map<F, DecisionTreeNode<?, ?>> children;

    // 叶节点决策结果
    private DecisionResult decisionResult;

    // 各节点共通的构造函数
    public DecisionTreeNode(Class<F> splitFeatureType){
        this.splitFeatureType = splitFeatureType;
        children = new HashMap<>();
        this.decisionResult = new DecisionResult();
    }

//    // 构造主体判断节点
//    public DecisionTreeNode(R splitResource, Class<F> splitFeatureType,
//                            EventName decisionEvent) {
//        this.type = DecisionTreeNodeType.MAIN_BODY_JUDGE_NODE;
//        this.mBJNodeResourceInfo = new MainBodyJudgeResourceInfo<>(splitResource);
//        this.splitFeatureType = splitFeatureType;
//        this.children = new HashMap<>();
//        this.decisionResult = new DecisionResult();
//        this.decisionResult.setDecisionEvent(decisionEvent);
//    }
//
//    // 构造地点决策节点,根据坐标决策地点
//    public DecisionTreeNode(R splitResource, Map<F, Object> coordinateToLocation, Class<F> splitFeatureType,
//                            EventName decisionEvent) {
//        this.type = DecisionTreeNodeType.LOCATION_DECISION_NODE;
//        this.lDNodeResourceInfo = new LocationDecisionNodeResourceInfo<>(splitResource, coordinateToLocation);
//        this.splitFeatureType = splitFeatureType;
//        this.children = new HashMap<>();
//        this.decisionResult = new DecisionResult();
//        this.decisionResult.setDecisionEvent(decisionEvent);
//    }
//
//    // 构造地点决策节点,根据状态决策地点
//    public DecisionTreeNode(List<R> splitResources, F desiredFeature, Class<F> splitFeatureType,
//                            EventName decisionEvent) {
//        this.type = DecisionTreeNodeType.LOCATION_DECISION_NODE;
//        this.lDNodeResourceInfo = new LocationDecisionNodeResourceInfo<>(splitResources, desiredFeature);
//        this.splitFeatureType = splitFeatureType;
//        this.children = new HashMap<>();
//        this.decisionResult = new DecisionResult();
//        this.decisionResult.setDecisionEvent(decisionEvent);
//    }
//
//    // 构造地点判断节点
//    public DecisionTreeNode(Class<F> splitFeatureType, EventName decisionEvent) {
//        this.type = DecisionTreeNodeType.LOCATION_JUDGE_NODE;
//        this.splitFeatureType = splitFeatureType;
//        this.children = new HashMap<>();
//        this.decisionResult = new DecisionResult();
//        this.decisionResult.setDecisionEvent(decisionEvent);
//    }

    // 是否为叶节点
    public boolean isLeaf() {
        return children == null || children.isEmpty();
    }

//    // 获取地点决策节点的类型
//    public LocationDecisionNodeType getLocationDecisionNodeType() {
//        return lDNodeResourceInfo.getType();
//    }

    // 为节点添加子节点
    public void addChild(F feature, DecisionTreeNode<?, ?> child){
        this.children.put(feature, child);
    }

//    /*
//     * 获得需要进行特征判断的资源
//     * 在单目标模式下，直接返回单一的资源
//     * 在多目标模式下，返回一个有助于事件生成的资源，如果没有这样的资源，则随意返回一个
//     */
//    public R getKeyResource(PredictInfo predictInfo){
//        //如果该节点为主题判断节点，直接返回目标资源
//        if(type.equals(DecisionTreeNodeType.MAIN_BODY_JUDGE_NODE)){
//            return mBJNodeResourceInfo.getSplitResource();
//        }
//        //如果节点为地点决策节点
//        else if(type.equals(DecisionTreeNodeType.LOCATION_DECISION_NODE)) {
//            // 如果节点根据坐标决定地点，直接返回目标资源
//            if(lDNodeResourceInfo.getType().equals(LocationDecisionNodeType.DECISION_BY_COORDINATE)){
//                return lDNodeResourceInfo.getSplitResource();
//            }
//            // 如果节点根据地点状态决定地点
//            else{
//                //获取期望特征
//                F desiredFeature = lDNodeResourceInfo.getDesiredFeature();
//                //获取目标资源列表
//                List<R> splitResources = lDNodeResourceInfo.getSplitResources();
//                //遍历之前可以对分裂资源基于位置排序，后续可以优化
//                for (R resource : splitResources) {
//                    //分裂资源的特征表中存在期望特征
//                    if(predictInfo.getFeatures().get(resource).contains(desiredFeature)){
//                        //该资源为目标资源
//                        return resource;
//                    }
//                }
//                //如果for循环中没有找到符合期望的资源,随便返回一个
//                return splitResources.getFirst();
//            }
//        }
//        // 如果节点为地点判断节点
//        else {
//            // 从节点结果中获取目标资源,这里应该判断decisionResult中的地点枚举是否属于R类，但java的泛型比较残废做不到
//            if (decisionResult.getLocationEnum() instanceof Enum<?>){
//                return (R) decisionResult.getLocationEnum();
//            }
//        }
//        return null;
//    }

    //根据特征获取子节点
    public DecisionTreeNode<?,?> getChild(F feature) {
        return children.get(feature);
    }

//    @Override
//    public String toString() {
//        return "DecisionTreeNode{" +
//                "\ntype=" + type +
//                "\nLocationDecisionNodeResourceInfo=" + lDNodeResourceInfo +
//                "\nMainBodyJudgeNodeResourceInfo=" + mBJNodeResourceInfo +
//                "\nsplitFeatureType=" + splitFeatureType.getSimpleName() +
//                "\nchildren=" + children +
//                "\ndecisionResult=" + decisionResult +
//                "\n}";
//    }
}
