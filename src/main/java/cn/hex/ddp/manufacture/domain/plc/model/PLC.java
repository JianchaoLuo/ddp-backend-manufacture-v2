package cn.hex.ddp.manufacture.domain.plc.model;

import cn.hex.ddp.manufacture.domain.common.enums.AreaEnum;
import cn.hex.ddp.manufacture.domain.plc.enums.PLCTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * PLC聚合模型
 * 说明：这个类是将设备PLC和区域PLC合并到一起了，通过PLC类型这个字段来区分。
 *
 * @author Huhaisen
 * @date 2024/05/03
 */
@Data
public class PLC {
    /**
     * PLC的ID(数据库ID)
     */
    private Long id;

    /**
     * ip地址(PLC的IP地址)
     */
    private String ip;

    /**
     * PLC类型
     */
    private PLCTypeEnum type;

    /**
     * 关联的设备ID列表（对应的设备ID）
     */
    private List<Long> equipmentIdList;

    /**
     * 所属区域(一个PLC对应一个区域)
     */
    private AreaEnum area;

}
