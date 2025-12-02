package cn.hex.ddp.manufacture.api.simulator.rest.vo;

import cn.hex.ddp.manufacture.domain.simulator.enums.SimulateStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimulateStatusVO {
    private SimulateStatusEnum status;
}
