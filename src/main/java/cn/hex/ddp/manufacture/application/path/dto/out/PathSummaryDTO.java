package cn.hex.ddp.manufacture.application.path.dto.out;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathNameEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathTypeEnum;
import lombok.Data;

@Data
public class PathSummaryDTO {
    /**
     * 路径ID(数据库ID)
     */
    private String id;

    /**
     * 路径编号
     */
    private String pathNo;

    /**
     * 路径名称
     */
    private String name;

    /**
     * 所属分区
     */
    private AreaEnum area;

    /**
     * 路径类型
     */
    private PathTypeEnum pathType;

    /**
     * 路径描述
     */
    private String description;

    /**
     * 路径状态
     */
    private PathStatusEnum status;


    /**
     * 路径名称枚举(供算法使用)
     */
    private PathNameEnum nameEnum;
}
