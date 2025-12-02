package cn.hex.ddp.manufacture.domain.plc.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 大车信息参数
 * 大车读取参数
 * @author 冯泽邦
 * @date 2025/10/20
 */
@Data
public class CarInfoParam implements Serializable {

    /**
     * 急停状态
     */
    private Boolean isEmergency;

    /**
     * 本地远程状态
     */
    private Boolean isRemote;

    /**
     * 车辆伺服故障
     */
    private Boolean isServoError;

    /**
     * 读码头故障
     */
    private Boolean isReadHeadError;

    /**
     * 读码头需清理
     */
    private Boolean isNeedClean;

    /**
     * 车辆移动到位
     */
    private Boolean isInPosition;

    /**
     * 车辆当前位置
     */
    private Integer currentPosition;

    /**
     * 车辆当前速度
     */
    private Double currentSpeed;

    /**
     * 远程急停
     */
    private Boolean remoteEmergency;

    /**
     * 远程复位
     */
    private Boolean remoteRest;

    /**
     * 远程车辆移动启停
     */
    private Boolean remoteStart;

    /**
     * 车辆目标位置
     */
    private Integer targetPosition;

    /**
     * 车辆目标速度
     */
    private Double targetSpeed;

}
