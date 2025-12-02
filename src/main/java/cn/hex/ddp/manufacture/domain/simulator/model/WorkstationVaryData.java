package cn.hex.ddp.manufacture.domain.simulator.model;
import cn.hex.ddp.manufacture.domain.common.enums.ProcessEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
import lombok.Data;
/**
 * 工位可能发生变化的数据，用于仿真展示
 * 属性？
 * @Author: FengZebang
 * @Date: 2025/3/5
 */
@Data
public class WorkstationVaryData {
    /**
     * 工位ID
     */
    private Long id;
    /**
     * 工位状态
     */
    private WorkstationStatusEnum status;
//    /**
//     * 所属工序
//     */
//    private ProcessEnum process;
//    /**
//     * 工位优先级
//     */
//    private Integer priority;
    /**
     * 工位名称
     */
    private String name;
    /**
     * 当前动作
     */
    private String currentAction;
}