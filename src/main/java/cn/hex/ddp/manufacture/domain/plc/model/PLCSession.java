package cn.hex.ddp.manufacture.domain.plc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

/**
 * @author Fan Wenbo
 * @data 2025/10/15 15:43
 * @desc 如果您读取GRM设备数据的间隔比较长（1分钟或者更长)，建议可以在每次读取数据之前登录一次，
 * 这样您的程序内部不需要记录任何状态。但如果数据读取间隔小于1分钟，推荐您只登录一次然后保存着登
 * 录得到的SID，下次就不用再登录了。
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PLCSession {
    /**
     *  客户端能够访问这个GRM设备的唯一凭据
     */
    private String sid;

    private String sessionId;
}
