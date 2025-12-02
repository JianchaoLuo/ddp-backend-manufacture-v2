package cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.dispatch;

import cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.NodeAttributeTool;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.tree.NodeTree;
import lombok.Data;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/12/8 15:47
 */
@Data
public class NodeDispatchTool {

    /**
     * 选择算法，选择树中当前节点的E中的一个事件，更新到当前执行事件Event（在属性工具类中）
     * @param tree 当前树
     * @param nodeAttributeTool 属性工具类中
     */
    public void selectEvent(NodeTree tree, NodeAttributeTool nodeAttributeTool) {
    }
}
