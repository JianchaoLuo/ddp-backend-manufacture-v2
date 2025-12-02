package cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums.decisionTree;

/**
 * @Author: bobo
 * @Description: 地点决策节点的类型
 * @DateTime: 2025/3/23 12:46
 **/
public enum LocationDecisionNodeType {

    //根据坐标决策地点
    //比如，需要决策的事件是子车进入哪个工岗，由子车的坐标决定，即子车当前在哪个工岗的路口，就进入哪个工岗
    DECISION_BY_COORDINATE,

    //根据状态决策地点
    //比如，需要决策的事件是母车搭载一个带着下砂箱的子车前往哪个工岗，由可选工岗的状态决定，即哪个工岗处于空闲状态，就前往哪个工岗
    DECISION_BY_STATUS;
}
