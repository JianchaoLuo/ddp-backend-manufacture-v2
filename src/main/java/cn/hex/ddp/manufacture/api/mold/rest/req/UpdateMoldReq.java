package cn.hex.ddp.manufacture.api.mold.rest.req;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UpdateMoldReq {
    /**
     * 模具编号
     * 每个模具实体的唯一编号
     */
    @Length(max = 32, message = "模具编号长度不能超过32")
    private String moldNo;

    /**
     * 模具名称
     */
    @Length(max = 32, message = "模具名称长度不能超过32")
    private String name;

    /**
     * 模具型号ID
     */
    private Long moldModelId;

    /**
     * 模具状态
     * 有没有被使用过之类的，是否可用...
     */
    @Length(max = 512, message = "模具状态长度不能超过512")
    private String status;
}
