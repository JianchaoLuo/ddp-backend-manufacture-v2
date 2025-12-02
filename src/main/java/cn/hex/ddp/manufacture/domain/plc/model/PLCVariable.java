package cn.hex.ddp.manufacture.domain.plc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Fan Wenbo
 * @data 2025/10/15 15:47
 * @desc
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PLCVariable {
    private String name;
    private String value;
}
