package cn.hex.ddp.manufacture.api.sandbox.rest.req;

import cn.hex.ddp.manufacture.domain.sandbox.enums.SandboxTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CreateSandboxModelReq {
    /**
     * 砂箱型号名
     */
    @NotBlank(message = "砂箱型号名不能为空")
    @Length(max = 32, message = "砂箱型号名长度不能超过32")
    private String modelName;

    /**
     * 砂箱长度
     */
    @NotNull(message = "砂箱长度不能为空")
    private Float length;

    /**
     * 砂箱宽度
     */
    @NotNull(message = "砂箱宽度不能为空")
    private Float width;

    /**
     * 砂箱高度
     */
    @NotNull(message = "砂箱高度不能为空")
    private Float height;

    /**
     * 砂箱空重
     */
    @NotNull(message = "砂箱空重不能为空")
    private Float emptyWeight;

    /**
     * 砂箱满重
     */
    @NotNull(message = "砂箱满重不能为空")
    private Float fullWeight;

    /**
     * 砂箱位置
     * 1代表最上层，2代表第二层,...
     */
    @NotNull(message = "砂箱位置不能为空")
    private Integer layer;

    /**
     * 砂箱冷热类型
     */
    @NotNull(message = "砂箱冷热类型不能为空")
    private SandboxTypeEnum type;

}
