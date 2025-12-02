package cn.hex.ddp.manufacture.infrastructure.algorithm.util;

import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassDescription: 轨道和事件的关联类，用于表示这个事件的参与者轨道的相关状态
 * @Author: KangHong
 * @Created: 2024/5/30 17:29
 */
@Data
@AllArgsConstructor
public class PathLinkEvent implements Serializable {
    /**
     * 轨道的id
     */
    private Long id;
    /**
     * 事件中轨道的状态
     */
    private PathStatusEnum status;
}
