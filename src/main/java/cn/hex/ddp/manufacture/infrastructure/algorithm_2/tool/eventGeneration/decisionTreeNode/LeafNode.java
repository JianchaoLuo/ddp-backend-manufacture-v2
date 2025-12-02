package cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.eventGeneration.decisionTreeNode;

import cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums.decisionTree.NoUseEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.eventGeneration.DecisionResult;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.EventName;

/**
 * @Author: bobo
 * @Description: 叶节点，表示决策结果
 * @DateTime: 2025/5/23 14:10
 **/
public class LeafNode extends DecisionTreeNode<NoUseEnum,Void> {

    /**
     * 叶节点的构造函数，只需要决策结果的事件枚举
     * @param decisionEvent 叶节点携带的决策节点信息
     */
    public LeafNode(EventName decisionEvent) {
        super(null);
        DecisionResult decisionResult = this.getDecisionResult();
        if(decisionResult != null) {
            decisionResult.setDecisionEvent(decisionEvent);
        }
    }

    @Override
    public String toString() {
        return "LeafNode{" +
                "\ndecisionResult=" + this.getDecisionResult() +
                "\n}";
    }

}
