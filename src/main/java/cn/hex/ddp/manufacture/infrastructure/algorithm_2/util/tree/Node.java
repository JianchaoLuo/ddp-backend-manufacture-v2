package cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.tree;

import cn.hex.ddp.manufacture.infrastructure.algorithm_2.enums.NodeState;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.EventName;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.resource.ResEvents;
import cn.hex.ddp.manufacture.infrastructure.algorithm_2.util.resource.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @ClassDescription: 节点类
 * @Author: KangHong
 * @Created: 2024/12/7 21:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Node {
    /**
     * 资源状态列表
     */
    private Resource resource;
    /**
     * 可执行事件列表
     */
    private ResEvents events;
    /**
     * 节点类型：未被扩展/未被选取/已被选取
     */
    private NodeState state;
    /**
     * 子节点列表：可扩展的子节点列表
     */
    private List<Node> children;
    /**
     * 父节点，用于定位该节点的父亲节点
     */
    private Node parent;
    /**
     * 定位子节点的Map，key为事件名称，value为子节点
     */
    private Map<EventName, Node> childrenMap;

    /**
     * 启发值
     */
    private double inspireValue;
    /**
     * 层级
     */
    private int storey;

    /**
     * 生成启发值的函数
     * 通过当前节点的状态，生成当前节点的启发值
     * @return 启发值
     */
    public double generateInspireValue() {
        return 0.0;
    }
}
