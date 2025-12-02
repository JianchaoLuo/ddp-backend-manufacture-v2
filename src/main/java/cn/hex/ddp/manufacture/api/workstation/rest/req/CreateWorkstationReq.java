package cn.hex.ddp.manufacture.api.workstation.rest.req;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.common.enums.ProcessEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationNameEnum;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CreateWorkstationReq {

    /**
     * 工位编号
     */
    @NotEmpty(message = "工位编号不能为空")
    @Length(max = 255, message = "工位编号长度不能超过255")
    private String stationNo;

    /**
     * 工位名称
     */
    @NotEmpty(message = "工位名称不能为空")
    @Length(max = 255, message = "工位名称长度不能超过255")
    private String name;

    /**
     * 工位位置id
     */
    private Long coordinateId;

    /**
     * 工位描述
     */
    @Length(max = 512, message = "工位描述长度不能超过512")
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
     * 工岗名称枚举（供算法使用）
     */
    private WorkstationNameEnum nameEnum;

}
