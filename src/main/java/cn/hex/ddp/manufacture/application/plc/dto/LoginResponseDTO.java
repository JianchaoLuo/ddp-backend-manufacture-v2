package cn.hex.ddp.manufacture.application.plc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String targetUrl;
    private String addr;
    private String sid;
    private String[] variableNames;

    public LoginResponseDTO(String addr, String sid) {
        this.addr = addr;
        this.sid = sid;
    }
}
