package cn.hex.ddp.manufacture.api.configuration.rest.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 添加或修改全局配置请求参数
 *
 * @author Huhaisen
 * @date 2024/05/15
 */
@Data
public class AddOrUpdateGlobalConfigurationReq {
    /**
     * 车辆装载货物的时间(单位：秒)
     */
    @NotNull(message = "车辆装载货物的时间不能为空")
    private Float loadingGoodsTime;

    /**
     * 冷却区最大工件宽度(单位：毫米)
     */
    @NotNull(message = "冷却区最大工件宽度不能为空")
    private Float coolingAreaWorkpieceMaxWidth;

    /**
     * 冷却区间距(单位：毫米)
     */
    @NotNull(message = "冷却区间距不能为空")
    private Float coolingAreaSpacing;

    /**
     * 浇筑区每天工作开始时间(格式：HH:mm)
     */
    @NotEmpty(message = "浇筑区每天工作开始时间不能为空")
    private String pouringAreaWorkStartTime;

    /**
     * 浇筑区每天工作结束时间(格式：HH:mm)
     */
    @NotEmpty(message = "浇筑区每天工作结束时间不能为空")
    private String pouringAreaWorkEndTime;

    /**
     * 其他每个区域的工作开始时间(格式：HH:mm)
     * (这里考虑把每个区域都拆分出来，每个区域都可以单独配置)
     */
    @NotEmpty(message = "其他每个区域的工作开始时间不能为空")
    private String otherAreaWorkStartTime;

    /**
     * 其他每个区域的工作结束时间(格式：HH:mm)
     * (这里考虑把每个区域都拆分出来，每个区域都可以单独配置)
     */
    @NotEmpty(message = "其他每个区域的工作结束时间不能为空")
    private String otherAreaWorkEndTime;

    /**
     * 立体库收到出库指令后出库需要的出库时间(单位：秒)
     */
    @NotNull(message = "立体库收到出库指令后出库需要的出库时间不能为空")
    private Float stereoscopicStorehouseOutboundTime;

    /**
     * 小车在点位上搬运起砂箱或产品的时间(单位：秒)
     */
    @NotNull(message = "小车在点位上搬运起砂箱或产品的时间不能为空")
    private Float subCarLiftGoodsTime;

    /**
     * 摆渡车与小车交互砂箱或产品的时间(单位：秒)
     */
    @NotNull(message = "摆渡车与小车交互砂箱或产品的时间不能为空")
    private Float ferryCarWithSubCarInteractionTime;

    /**
     * 摆渡车与摆渡车交互砂箱或产品的时间(单位：秒)
     */
    @NotNull(message = "摆渡车与摆渡车交互砂箱或产品的时间不能为空")
    private Float ferryCarWithFerryCarInteractionTime;

    /**
     * 小车与小车的交互时间(单位：秒)
     */
    @NotNull(message = "小车与小车的交互时间不能为空")
    private Float subCarWithSubCarInteractionTime;

}
