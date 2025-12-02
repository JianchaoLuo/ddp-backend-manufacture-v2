package cn.hex.ddp.manufacture.domain.technique.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 配方明细类
 * 相当于参数类，跟车辆型号参数类似
 *
 * @author Huhaisen
 * @date 2024/09/26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FormulaDetail {
    /**
     * 配方名称
     */
    private String name;

    /**
     * 所占比例
     */
    private Float value;
}
