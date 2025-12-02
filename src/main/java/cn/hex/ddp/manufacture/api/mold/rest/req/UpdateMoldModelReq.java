package cn.hex.ddp.manufacture.api.mold.rest.req;

import cn.hex.ddp.manufacture.domain.mold.enums.MoldTypeEnum;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UpdateMoldModelReq {
    /**
     * 模具型号名
     */
    @Length(max = 32, message = "模具型号名长度不能超过32")
    private String modelName;

    /**
     * 模具类型
     */
    private MoldTypeEnum type;

    /**
     * 模具位置
     * 1代表最上层，2代表第二层,....
     */
    private Integer layer;

    /**
     * 模具长度
     */
    private Float length;

    /**
     * 模具宽度
     */
    private Float width;

    /**
     * 模具高度
     */
    private Float height;

    /**
     * 模具重量
     */
    private Float weight;
}
