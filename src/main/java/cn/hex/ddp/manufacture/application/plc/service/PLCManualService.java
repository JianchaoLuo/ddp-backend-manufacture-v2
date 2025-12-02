package cn.hex.ddp.manufacture.application.plc.service;

import cn.hex.ddp.manufacture.application.plc.dto.LoginResponseDTO;
import cn.hex.ddp.manufacture.domain.car.model.CarFlatDTO;
import cn.hex.ddp.manufacture.domain.plc.model.PLCVariable;

import java.util.List;

public interface PLCManualService {

    LoginResponseDTO login(String url, String grm, String pass);

    List<PLCVariable> readVariables(String addr, String sid, String[] variableNames);

    List<PLCVariable> enumerateVariables(String addr, String sid);

    List<PLCVariable> writeVariables(String addr, String sid, List<PLCVariable> variables);

    List<CarFlatDTO> getSimulatorCars();
}
