package cn.hex.ddp.manufacture.api.workstation.rest.vo;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.common.enums.ProcessEnum;
import cn.hex.ddp.manufacture.domain.configuration.model.Coordinate;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationNameEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
import lombok.Data;

@Data
public class WorkstationVO {
    /**
     * 工位ID(数据库ID)
     */
    private Long id;

    /**
     * 工位编号
     */
    private String stationNo;

    /**
     * 工位名称
     */
    private String name;

    /**
     * 工位位置
     */
    private Coordinate coordinate;

    /**
     * 工位描述
     */
    private String description;

    /**
     * 所属分区
     */
    private AreaEnum area;

    /**
     * 所属工序
     */
    private ProcessEnum process;

    /**
     * 工位优先级
     */
    private Integer priority;

    /**
     * 工位状态
     */
    private WorkstationStatusEnum status;

    /**
     * 工岗名称枚举（供算法使用）
     */
    private WorkstationNameEnum nameEnum;
}
