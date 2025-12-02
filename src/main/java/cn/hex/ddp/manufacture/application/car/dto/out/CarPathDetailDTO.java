package cn.hex.ddp.manufacture.application.car.dto.out;

import cn.hex.ddp.manufacture.application.path.dto.out.PathDTO;
import lombok.Data;

/**
 * 车辆路径详情DTO
 *
 * @author Huhaisen
 * @date 2024/06/28
 */
@Data
public class CarPathDetailDTO {
    /**
     * 路径详情
     */
    private PathDTO path;
}
