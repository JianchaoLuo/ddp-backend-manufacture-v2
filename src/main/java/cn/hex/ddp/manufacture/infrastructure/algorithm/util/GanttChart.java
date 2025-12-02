package cn.hex.ddp.manufacture.infrastructure.algorithm.util;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassDescription: 甘特图，用于记录每一个车辆，设备的甘特图
 * @Author: KangHong
 * @Created: 2024/5/29 2:11
 */
@AllArgsConstructor
@Data
public class GanttChart implements Serializable {
    /**
     * 甘特图的id
     */
    private Long id;
    /**
     * 甘特图名称
     * 以区域名+甘特图取名，例：OutBoundGanttChart
     */
    private String name;
    /**
     * 甘特图所属的区域
     */
    private AreaEnum region;
    /**
     * 区域中，甘特图发生的所有事件，为二维数组
     */
    private List<EventLinkGantt> eventLinkGantts;
}
