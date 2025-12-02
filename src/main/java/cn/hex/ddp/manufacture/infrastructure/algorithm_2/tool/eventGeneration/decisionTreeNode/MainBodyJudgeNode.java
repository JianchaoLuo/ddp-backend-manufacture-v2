package cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.eventGeneration.decisionTreeNode;

import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.eventGeneration.resourceInfo.MainBodyJudgeResourceInfo;

/**
 * @Author: bobo
 * @Description: 主体判断节点。比如决策的事件为子母车前往合箱工岗，判断子母车相关特征时的节点属于主体判断节点
 * @DateTime: 2025/5/22 20:55
 **/
public class MainBodyJudgeNode<R extends Enum<R>, F> extends DecisionTreeNode<R, F>{

    /**
     * 对于主体判断节点，节点存储的资源相关信息
     */
    private MainBodyJudgeResourceInfo<R> mainBodyJudgeResourceInfo;

    // 构造主体判断节点
    public MainBodyJudgeNode(R splitResource, Class<F> splitFeatureType) {
        super(splitFeatureType);
        this.mainBodyJudgeResourceInfo = new MainBodyJudgeResourceInfo<>(splitResource);
    }

//    @Override
//    //获得需要进行特征判断的资源
//    public R getKeyResource(){
//        return mainBodyJudgeResourceInfo.getSplitResource();
//    }

    @Override
    public String toString() {
        return "MainBodyJudgeNode{" +
                "\nmainBodyJudgeResourceInfo=" + mainBodyJudgeResourceInfo +
                "\nsplitFeatureType=" + this.getSplitFeatureType().getSimpleName() +
                "\nchildren=" + this.getChildren() +
                "\ndecisionResult=" + this.getDecisionResult() +
                "\n}";
    }

}
