package cn.hex.ddp.manufacture.api.plc.rest.req;

import cn.hex.ddp.manufacture.domain.plc.model.PLCVariable;
import lombok.Data;

import java.util.List;


@Data
public  class WriteVariableReq {

    private String addr;

    private String sid;

    private List<PLCVariable> variables; // 包含变量名和对应值
}
