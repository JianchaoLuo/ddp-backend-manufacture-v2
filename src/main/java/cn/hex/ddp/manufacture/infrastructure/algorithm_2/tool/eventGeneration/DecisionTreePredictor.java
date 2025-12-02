package cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.eventGeneration;

import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.eventGeneration.decisionTreeNode.DecisionTreeNode;
import lombok.Data;

/**
 * @Author: bobo
 * @Description: 用决策树进行决策的类
 * @DateTime: 2025/3/19 15:20
 **/
@Data
public class DecisionTreePredictor<R extends Enum<R>, F> {

    //决策树的根节点
    private final DecisionTreeNode<R, F> root;

    //构造时需要一个根节点
    public DecisionTreePredictor(DecisionTreeNode<R, F> root) {
        this.root = root;
    }

//    //预测方法
//    public DecisionResult predict(PredictInfo features) {
//        return predictRecursive(root, features);
//    }
//
//    //具体的预测
//    private <Resource extends Enum<Resource>, Feature> DecisionResult predictRecursive(
//            DecisionTreeNode<Resource, Feature> node,
//            PredictInfo predictInfo
//    ) {
//        //如果是叶子结点，返回预测结果
//        if (node.isLeaf()) {
//            return node.getDecisionResult();
//        }
//
//        // 获取当前节点的目标资源
//        Resource keyResource = node.getKeyResource(predictInfo);
//        // 获取当前节点的分裂特征类型
//        Class<Feature> splitFeatureType = node.getSplitFeatureType();
//        // 从预测信息中获取目标资源的状态列表
//        List<?> keyResourceFeatures = predictInfo.getKeyResourceFeatures(keyResource);
//        // 当前判断需要的特征
//        Feature feature = null;
//        // 根据分裂特征类型查找具体分裂特征
//        for (Object keyResourceFeature : keyResourceFeatures) {
//            if (splitFeatureType.isInstance(keyResourceFeature)) {
//                feature = (Feature) keyResourceFeature;
//                break;
//            }
//        }
//
//        //获取子节点
//        DecisionTreeNode<?, ?> child = node.getChild(feature);
//
//        // 如果节点进行了地点决策,将预测结果的地点信息传递给子节点,并继续决策
//        if(child != null){
//            // 如果当前节点为地点决策节点
//            if(node.getType().equals(DecisionTreeNodeType.LOCATION_DECISION_NODE)){
//                // 如果地点决策行为通过地点的状态
//                if(node.getLocationDecisionNodeType().equals(LocationDecisionNodeType.DECISION_BY_STATUS)){
//                    // 修改节点预测结果的地点枚举
//                    node.getDecisionResult().setLocationEnum(keyResource);
//                }
//                // 如果地点决策行为通过坐标
//                else {
//                    // 获得坐标和地点枚举的映射关系
//                    Map<Feature,Object> CoordinateToLocation = node.getLDNodeResourceInfo().getCoordinateToLocation();
//                    // 修改节点预测结果的地点枚举
//                    node.getDecisionResult().setLocationEnum(CoordinateToLocation.get(feature));
//                }
//            }
//            Object LocationEnum = node.getDecisionResult().getLocationEnum();
//            // 将预测结果的地点信息传递给子节点
//            child.getDecisionResult().setLocationEnum(LocationEnum);
//            // 递归预测
//            return predictRecursive(child, predictInfo);
//        }else {
//            // 当前状态下无法生成事件
//            return null;
//        }
//    }
}
