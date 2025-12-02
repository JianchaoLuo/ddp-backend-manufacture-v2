package cn.hex.ddp.manufacture.domain.plc.model;

import lombok.Data;

import java.io.Serializable;

/**
 * PLC写参数
 * @author 冯泽邦
 * @date 2025/10/20
 */
@Data
public class PlcWriteParam implements Serializable {

    /**
     * 字段名
     */
    private String name;

    /**
     * 数据地址
     */
    private String address;

    /**
     * 写入值
     */
    private Object value;
}