package cn.hex.ddp.manufacture.infrastructure.algorithm.util;

import cn.hex.ddp.manufacture.domain.common.exception.BusinessException;
import cn.hex.ddp.manufacture.infrastructure.common.converter.AlgorithmDataConverter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassDescription: 坐标类，用于定义坐标的表达
 * @Author: KangHong
 * @Created: 2024/5/22 17:52
 */
@NoArgsConstructor
@Data
public class Coordinate implements Serializable {
    /**
     * x轴坐标
     */
    private double x;
    /**
     * y轴坐标
     */
    private double y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Coordinate fromStr(String str) {
        if (str == null || str.isEmpty()) {
            throw new BusinessException("坐标字符串为空");
        }
        try {
            String[] split = str.split(",");
            return new Coordinate(Double.parseDouble(split[0]), Double.parseDouble(split[1]));
        } catch (Exception e) {
            throw new BusinessException("坐标字符串格式错误");
        }
    }

    @Override
    public String toString() {
        return x + "," + y;
    }

    /**
     * 经过比例尺转换后的坐标字符串
     */
    public String scaleConversionToString() {
        return (x / AlgorithmDataConverter.PLOTTING_SCALE) + "," + (y / AlgorithmDataConverter.PLOTTING_SCALE);
    }


    public Coordinate coordinateDeepCopy(){
        Coordinate c = new Coordinate(x, y);

        return c;
    }
}
