package cn.hex.ddp.manufacture.infrastructure.algorithm.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/12/23 19:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tray implements Serializable {
    /**
     * 托盘id
     */
    private Long id;
    /**
     * 托盘编号
     */
    private String code;
    /**
     * 托盘搭载的产品
     */
    private Product product;
}
