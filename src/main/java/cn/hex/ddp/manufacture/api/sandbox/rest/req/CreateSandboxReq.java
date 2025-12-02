package cn.hex.ddp.manufacture.api.sandbox.rest.req;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CreateSandboxReq {
    /**
     * 砂箱编号
     * 每个单体砂箱的唯一物理编码
     */
    @Length(max = 32, message = "砂箱编号长度不能超过32")
    private String sandboxNo;

    /**
     * 砂箱名称
     */
    @NotEmpty(message = "砂箱名称不能为空")
    @Length(max = 32, message = "砂箱名称长度不能超过32")
    private String name;

    /**
     * 砂箱版本
     */
    @Length(max = 20, message = "砂箱版本长度不能超过20")
    private String version;

    /**
     * 砂箱型号ID
     */
    @NotNull(message = "砂箱型号ID不能为空")
    private Long sandboxModelId;

    /**
     * 砂箱状态
     * 有没有被使用过之类的，是否可用...
     */
    @Length(max = 512, message = "砂箱状态长度不能超过512")
    private String status;
}
