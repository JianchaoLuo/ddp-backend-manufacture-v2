package cn.hex.ddp.manufacture.infrastructure.algorithm.tools;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2024/7/7 17:25
 */
public class SimulationException extends Exception {
    // 无参构造方法
    public SimulationException() {
        super();
    }

    // 用详细信息指定的构造方法
    public SimulationException(String message) {
        super(message);
    }

    // 用详细信息和原因指定的构造方法
    public SimulationException(String message, Throwable cause) {
        super(message, cause);
    }

    // 用原因指定的构造方法
    public SimulationException(Throwable cause) {
        super(cause);
    }
}
