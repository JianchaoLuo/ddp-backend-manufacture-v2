package cn.hex.ddp.manufacture.api.mold.rest.req;

import cn.hex.ddp.manufacture.domain.mold.enums.MoldTypeEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CreateMoldModelReq {
    /**
     * 模具型号名
     */
    @NotEmpty(message = "模具型号名不能为空")
    @Length(max = 32, message = "模具型号名长度不能超过32")
    private String modelName;

    /**
     * 模具类型
     */
    @NotNull(message = "模具类型不能为空")
    private MoldTypeEnum type;

    /**
     * 模具位置
     * 1代表最上层，2代表第二层,....
     */
    @NotNull(message = "模具位置不能为空")
    private Integer layer;

    /**
     * 模具长度
     */
    @NotNull(message = "模具长度不能为空")
    private Float length;

    /**
     * 模具宽度
     */
    @NotNull(message = "模具宽度不能为空")
    private Float width;

    /**
     * 模具高度
     */
    @NotNull(message = "模具高度不能为空")
    private Float height;

    /**
     * 模具重量
     */
    @NotNull(message = "模具重量不能为空")
    private Float weight;

}
