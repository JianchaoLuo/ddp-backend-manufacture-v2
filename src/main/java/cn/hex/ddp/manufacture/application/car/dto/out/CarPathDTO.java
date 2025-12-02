package cn.hex.ddp.manufacture.application.car.dto.out;

import cn.hex.ddp.manufacture.api.path.rest.vo.PathSummaryVO;
import lombok.Data;

/**
 * 车辆路径DTO
 *
 * @author Huhaisen
 * @date 2024/06/03
 */
@Data
public class CarPathDTO {
    /**
     * 路径
     */
    private PathSummaryVO path;
}
