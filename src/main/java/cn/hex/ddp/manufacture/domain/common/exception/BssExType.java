package cn.hex.ddp.manufacture.domain.common.exception;

import cn.hex.ddp.manufacture.domain.technique.model.MoltenIronFormula;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 自定义业务异常的异常种类
 *
 * @author Huhaisen
 * @date 2024/05/07
 */
@Getter
@AllArgsConstructor
public enum BssExType {

    /**
     * 参数校验失败
     */
    PARAM_VALIDATE_FAILED(10001, "参数校验失败"),

    /**
     * 服务器繁忙异常
     */
    SERVER_BUSY_ERROR(10002, "服务器繁忙, 请稍后再试"),

    /**
     * 用户名或密码错误
     */
    USERNAME_OR_PASSWORD_ERROR(10003, "用户名或密码错误"),

    /**
     * 手机号已存在
     */
    PHONE_ALREADY_EXIST(10004, "手机号已存在"),

    /**
     * 工号已存在
     */
    WORKER_NO_ALREADY_EXIST(10005, "工号已存在"),
    /**
     * 模具已在模具组中
     */
    MOLD_GROUP_MOLD_EXIST(10006, "模具已在模具组中"),
    /**
     * 模具已从模具组中移除
     */
    MOLD_GROUP_MOLD_NOT_EXIST(10007, "模具已从模具组中移除"),
    /**
     * 沙箱已在沙箱组中
     */
    SANDBOX_EXIST_IN_GROUP(10008, "沙箱已在沙箱组中"),
    /**
     * 沙箱已从沙箱组中移除
     */
    SANDBOX_NOT_EXIST_IN_GROUP(10009, "沙箱已从沙箱组中移除"),
    /**
     * 产品型号不存在或已被删除
     */
    PRODUCT_MODEL_NOT_FOUND(10010, "产品型号不存在或已被删除"),

    /**
     * 工艺不存在或已被删除
     */
    TECHNIQUE_NOT_FOUND(10011, "工艺不存在或已被删除"),
    /**
     * 产品已在订单项中, 不能重复添加
     */
    PRODUCT_ALREADY_IN_ORDER_ITEM(10012, "产品已在订单项中, 不能重复添加"),
    /**
     * 产品不在订单项中
     */
    PRODUCT_NOT_IN_ORDER_ITEM(10013, "产品不在订单项中"),
    /**
     * 订单不存在或已被删除
     */
    ORDER_NOT_EXIST(10014, "订单不存在或已被删除"),
    /**
     * 订单项不存在或已被删除
     */
    ORDER_ITEM_NOT_EXIST(10015, "订单项不存在或已被删除"),

    /**
     * JSON处理异常
     */
    JSON_PROCESSING_ERROR(10016, "JSON处理异常"),
    /**
     * JSON格式校验异常
     */
    JSON_VALIDATE_ERROR(10017, "JSON格式校验异常"),

    /**
     *  坐标重复错误
     */
    COORDINATE_REPEAT_ERROR(10018, "坐标已存在"),
    /**
     * 工位编号已存在
     */
    WORKSTATION_ALREADY_EXISTS(10019, "工位编号已存在"),
    /**
     * 点位名称重复
     */
    POSITION_NAME_REPEAT_ERROR(10020, "点位名已存在"),
    /**
     * 设备编号重复
     */
    EQUIPMENT_NO_REPEAT_ERROR(10021, "设备编号已存在"),
    /**
     * 车辆型号唯一编号重复
     */
    CAR_MODEL_NO_REPEAT_ERROR(10022, "车辆型号编号已存在"),
    /**
     * 车辆编号重复
     */
    CAR_NO_REPEAT_ERROR(10023, "车辆编号已存在"),
    /**
     * 路径编号重复
     */
    PATH_NO_REPEAT_ERROR(10024, "路径编号已存在"),
    /**
     * 沙配方型号已存在
     */
    SAND_FORMULA_MODEL_REPEAT(10025, "沙配方型号已存在"),
    /**
     * 铁水配方型号已存在
     */
    MOLTEN_IRON_FORMULA_MODEL_REPEAT(10026, "铁水配方型号已存在"),
    /**
     * 工艺编号已存在
     */
    TECHNIQUE_NO_REPEAT(10027, "工艺编号已存在"),
    /**
     * 沙箱编号已存在
     */
    SANDBOX_NO_REPEAT(10028, "沙箱编号已存在"),
    /**
     * 模具编号已存在
     */
    MOLD_NO_REPEAT(10029, "模具编号已存在"),
    /**
     * 产品名称已存在
     */
    PRODUCT_NAME_REPEAT(10030, "产品名称已存在"),
    /**
     * 产品型号已存在
     */
    PRODUCT_MODEL_REPEAT(10031, "产品型号已存在"),
    /**
     * 订单编号已存在
     */
    ORDER_NO_REPEAT(10032, "订单编号已存在"),

    /**
     * 车辆型号在删除时被已存在的车辆占用，无法删除
     */
    CAR_MODEL_BE_OCCUPIED(10033, "车辆型号被现有车辆占用"),

    /**
     * 砂箱型号在删除时被已存在的砂箱占用，无法删除
     */
    SANDBOX_MODEL_BE_OCCUPIED(10034, "砂箱型号被现有砂箱占用"),

    /**
     * 砂箱在删除时被已存在的砂箱组占用，无法删除
     */
    SANDBOX_BE_OCCUPIED(10035, "砂箱被现有砂箱组占用"),

    /**
     * 砂箱组在删除时被已存在的工艺占用，无法删除
     */
    SANDBOX_GROUP_BE_OCCUPIED(10036, "砂箱组被现有工艺占用"),

    /**
     * 模具组在删除时被已存在的工艺占用，无法删除
     */
    MOLD_GROUP_BE_OCCUPIED(10037, "模具组被现有工艺占用"),

    /**
     * 模具型号在删除时被已存在的模具占用，无法删除
     */
    MOLD_MODEL_BE_OCCUPIED(10038, "模具型号被模具占用"),

    /**
     * 模具在删除时被已存在的模具组占用，无法删除
     */
    MOLD_BE_OCCUPIED(10039, "模具被现有模具组占用"),

    /**
     * 车辆型号类型与车辆类型不一致
     */
    CAR_MODEL_INCONSISTENT(10040, "车辆型号类型不一致"),

    /**
     * 产品在删除时被订单占用，无法删除
     */
    PRODUCT_BE_OCCUPIED(10041, "产品被订单组占用"),

    /**
     * PLC在删除时被已存在的设备占用，无法删除
     */
    PLC_BE_OCCUPIED(10042, "PLC被设备占用"),

    /**
     * 订单在删除时被已存在的任务项占用，无法删除
     */
    ORDER_BE_OCCUPIED(10043, "订单被仿真任务项占用"),

    /**
     * 产品在删除时被已存在的任务项占用，无法删除
     */
    PRODUCT_BE_OCCUPIED_BY_TASK(10044, "产品被仿真任务项占用"),

    /**
     * 工艺在删除时被已存在的产品占用，无法删除
     */
    TECHNIQUE_BE_OCCUPIED(10045, "工艺被产品占用"),

    /**
     * 砂配方删除时被工艺占用，无法删除
     */
    SAND_FORMULA_BE_OCCUPIED(10046,"砂配方被工艺占用"),

    /**
     * 铁水删除时被工艺占用，无法删除
     */
    MOLTEN_IRON_FORMULA_BE_OCCUPIED(10047,"铁水配方被工艺占用"),

    /**
     * Tick数据范围校验异常
     */
    TICK_VALIDATE_ERROR(10048, "Tick数据范围不合法"),
    /**
     * 仿真数据中MissionId校验异常
     */
    SIMULATOR_MISSION_VALIDATE_ERROR(10049, "仿真数据中MissionId不合法"),
    /**
     * 砂箱模具数量异常
     */
    SAND_BOX_MOLD_NUM_ERROR(10050, "砂箱模具的数量不合法"),
    /**
     * 砂芯模具数量异常
     */
    SAND_CORE_MOLD_NUM_ERROR(10051, "砂芯模具的数量不合法"),

    /**
     * 时间转换异常
     */
    TIME_CONVERSION_ERROR(10052, "时间转换异常"),

    /**
     * 无效资源类型
     */
    INVALID_RESOURCE_TYPE(10053, "无效资源类型");

    /**
     * 异常状态码
     * 通用场景业务异常状态码 0 - 10000
     * 特定场景业务异常状态码从 10001 开始
     */
    private final int code;

    /**
     * 异常信息
     */
    private final String msg;

}
