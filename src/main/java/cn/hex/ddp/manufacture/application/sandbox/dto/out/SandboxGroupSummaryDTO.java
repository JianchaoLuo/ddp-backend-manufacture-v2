package cn.hex.ddp.manufacture.application.sandbox.dto.out;

import lombok.Data;

import java.util.List;

@Data
public class SandboxGroupSummaryDTO {
    /**
     * 砂箱组ID(数据库ID)
     */
    private Long id;

    /**
     * 砂箱组型号名称
     */
    private String name;

    /**
     * 砂箱组长度
     */
    private Float length;

    /**
     * 砂箱组宽度
     */
    private Float width;

    /**
     * 砂箱组高度
     */
    private Float height;

    /**
     * 砂箱组空重
     */
    private Float emptyWeight;

    /**
     * 砂箱组满重
     */
    private Float fullWeight;

    /**
     * 砂箱组总层数
     */
    private Integer layerCount;

    /**
     * 砂箱组砂箱列表
     */
    private List<SandboxGroupSandboxSummaryDTO> sandboxGroupSandboxList;
}
