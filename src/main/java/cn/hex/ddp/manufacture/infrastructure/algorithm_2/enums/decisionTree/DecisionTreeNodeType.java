package cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums.decisionTree;

/**
 * @Author: bobo
 * @Description: 决策树节点的类型
 * @DateTime: 2025/3/22 16:25
 **/
public enum DecisionTreeNodeType {

    // 主体判断节点。
    // 比如决策的事件为子母车前往合箱工岗，判断子母车相关特征时的节点属于主体判断节点
    MAIN_BODY_JUDGE_NODE,

    // 地点决策节点。
    // 比如决策的事件为子母车前往合箱工岗，判断子母车具体去哪个工岗的节点属于地点决策节点
    LOCATION_DECISION_NODE,

    // 地点判断节点。
    // 比如决策的事件为子母车前往合箱工岗，对具体合箱工岗是否满足条件的节点判断属于地点判断节点
    LOCATION_JUDGE_NODE;
}
