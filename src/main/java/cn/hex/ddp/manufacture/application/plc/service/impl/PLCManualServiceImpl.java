package cn.hex.ddp.manufacture.application.plc.service.impl;

import cn.hex.ddp.manufacture.api.plc.rest.vo.PLCResourceVO;
import cn.hex.ddp.manufacture.api.plc.rest.vo.PLCWithResourcesVO;
import cn.hex.ddp.manufacture.application.plc.dto.LoginResponseDTO;
import cn.hex.ddp.manufacture.application.plc.service.PLCManualService;
import cn.hex.ddp.manufacture.application.plc.service.PLCService;
import cn.hex.ddp.manufacture.domain.car.manager.CarManager;
import cn.hex.ddp.manufacture.domain.car.model.Car;
import cn.hex.ddp.manufacture.domain.car.model.CarFlatDTO;
import cn.hex.ddp.manufacture.domain.configuration.manager.ConfigurationManager;
import cn.hex.ddp.manufacture.domain.path.enums.RailDirectionEnum;
import cn.hex.ddp.manufacture.domain.path.manager.PathManager;
import cn.hex.ddp.manufacture.domain.path.model.Path;
import cn.hex.ddp.manufacture.domain.plc.model.PLCVariable;
import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class PLCManualServiceImpl implements PLCManualService {

    @Autowired
    private PLCService plcService;

    @Autowired
    private CarManager carManager;

    @Autowired
    private PathManager pathManager;

    @Autowired
    private ConfigurationManager configurationManager;

    /**
     * 登录接口
     */
    @Override
    public LoginResponseDTO login(String targetUrl, String grm, String pass) {
        try {
            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String postData = "GRM=" + grm + "&PASS=" + pass;

            try (OutputStream os = connection.getOutputStream()) {
                os.write(postData.getBytes(StandardCharsets.UTF_8));
            }

            int code = connection.getResponseCode();
            if (code == 200) {
                String addr = null;
                String sid = null;
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        if (line.startsWith("ADDR=")) addr = line.substring(5);
                        if (line.startsWith("SID=")) sid = line.substring(4);
                    }
                }
                if (addr != null && sid != null) {
                    return new LoginResponseDTO(addr, sid);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // 登录失败
    }

    /**
     * 读取变量接口
     */
    @Override
    public List<PLCVariable> readVariables(String addr, String sid, String[] variableNames) {
        List<PLCVariable> result = new ArrayList<>();
        try {
            String targetUrl = "http://" + addr + "/exdata?SID=" + sid + "&OP=R";
            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");

            StringBuilder requestBody = new StringBuilder(variableNames.length + "\n");
            for (String var : variableNames) requestBody.append(var).append("\n");

            try (OutputStream os = connection.getOutputStream()) {
                os.write(requestBody.toString().getBytes(StandardCharsets.UTF_8));
            }

            if (connection.getResponseCode() == 200) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    int index = -1;
                    while ((line = in.readLine()) != null) {
                        if (!line.trim().isEmpty() && !line.equals("OK")) {
                            // 跳过第一行OK
                            if (index == -1) {
                                index++;
                                continue;
                            }
                            result.add(new PLCVariable(variableNames[index], line));
                            index++;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 枚举所有变量接口
     */
    @Override
    public List<PLCVariable> enumerateVariables(String addr, String sid) {
        List<PLCVariable> result = new ArrayList<>();
        try {
            String targetUrl = "http://" + addr + "/exdata?SID=" + sid + "&OP=E";
            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");

            try (OutputStream os = connection.getOutputStream()) {
                os.write("NTRP\n".getBytes(StandardCharsets.UTF_8));
            }

            System.out.println("connection.getResponseCode() = " + connection.getResponseCode());

            if (connection.getResponseCode() == 200) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = in.readLine()) != null) {
                        line = line.trim();
                        if (line.isEmpty() || line.equals("OK")) continue;

                        System.out.println("line == " + line);

                        // 处理 key=value 格式
                        if (line.contains("=")) {
                            String[] parts = line.split("=", 2);
                            String key = parts[0].trim();
                            String value = parts.length > 1 ? parts[1].trim() : null;
                            result.add(new PLCVariable(key, value));
                            continue;
                        }

                        // 处理 name,type,access,value 格式
                        String[] parts = line.split(",", -1);
                        if (parts.length >= 4) {
                            String name = parts[0].trim();
                            String type = parts[1].trim();
                            String access = parts[2].trim();
                            String value = parts[3].trim();
                            result.add(new PLCVariable(name, value));
                        } else if (parts.length > 0) {
                            result.add(new PLCVariable(parts[0].trim(), null));
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // ✅ 写变量
    public List<PLCVariable> writeVariables(String addr, String sid, List<PLCVariable> variables) {
        List<PLCVariable> resultList = new ArrayList<>();
        try {
            String targetUrl = "http://" + addr + "/exdata?SID=" + sid + "&OP=W";
            URL url = new URL(targetUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");

            // 组装请求体：第一行变量个数，后续依次是变量名和变量值
            StringBuilder body = new StringBuilder();
            body.append(variables.size()).append("\r\n");
            for (PLCVariable var : variables) {
                body.append(var.getName()).append("\r\n");
                body.append(var.getValue()).append("\r\n");
            }

            byte[] data = body.toString().getBytes(StandardCharsets.UTF_8);
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));

            try (OutputStream os = connection.getOutputStream()) {
                os.write(data);
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        response.append(line).append("\r\n");
                    }
                    // 解析响应
                    return parseWriteResponse(response.toString(), variables);
                }
            } else {
                return variables; // 返回原始列表
            }

        } catch (Exception e) {
            e.printStackTrace();
            return variables; // 异常时返回原始变量列表
        }
    }

    // ✅ 写变量响应解析，返回修改成功后的变量列表
    private List<PLCVariable> parseWriteResponse(String response, List<PLCVariable> originalVariables) {
        if (response == null || response.isEmpty()) {
            return originalVariables;
        }

        String[] lines = response.split("\r\n");
        String statusCode = lines[0].trim();
        List<PLCVariable> updatedList = new ArrayList<>();

        if ("OK".equals(statusCode)) {
            int variableCount = Integer.parseInt(lines[1].trim());
            for (int i = 0; i < variableCount && i < originalVariables.size(); i++) {
                String status = (2 + i) < lines.length ? lines[2 + i].trim() : "1"; // 1 表示错误
                PLCVariable originalVar = originalVariables.get(i);

                PLCVariable updatedVar = new PLCVariable();
                updatedVar.setName(originalVar.getName());


                // 如果返回0表示成功，保持当前value；否则不修改value
                if ("0".equals(status)) {
                    updatedVar.setValue(originalVar.getValue());
                } else {
                    updatedVar.setValue(originalVar.getValue()); // 失败不改值，但也可选择标记错误
                }
                updatedList.add(updatedVar);
            }
        } else if ("ERROR".equals(statusCode)) {
            return originalVariables;
        } else {
            return originalVariables;
        }

        return updatedList;
    }

    @Override
    public List<CarFlatDTO> getSimulatorCars() {
        List<PLCWithResourcesVO> plcWithResources = plcService.listAllPlcWithResources();
        if (plcWithResources == null || plcWithResources.isEmpty()) {
            return List.of();
        }

        Set<Long> carIds = new LinkedHashSet<>();
        Map<Long, String> carIdToPlcIp = new HashMap<>();
        for (PLCWithResourcesVO vo : plcWithResources) {
            List<PLCResourceVO> resources = vo.getResources();
            if (resources == null || resources.isEmpty()) continue;
            for (PLCResourceVO res : resources) {
                String rt = res.getResourceType();
                if (isCarResourceType(rt)) {
                    Long carId = res.getResourceId();
                    carIds.add(carId);
                    carIdToPlcIp.putIfAbsent(carId, vo.getIp());
                }
            }
        }
        if (carIds.isEmpty()) {
            return List.of();
        }

        List<CarFlatDTO> result = new ArrayList<>();
        for (Long id : carIds) {
            Car car = carManager.getCarById(id);
            if (car == null) continue;
            CarFlatDTO dto = new CarFlatDTO();
            dto.setId(car.getId());
            dto.setCarNo(car.getCarNo());
            dto.setName(car.getName());
            dto.setType(car.getType());
            dto.setOperationStatus(car.getOperationStatus());
            dto.setControlStatus(car.getControlStatus());
            dto.setArea(car.getArea());
            dto.setModel(car.getModel());
            dto.setCarPaths(car.getCarPaths() == null ? List.of() : car.getCarPaths());
            RailDirectionEnum dir = car.getHeadDirection();
            dto.setHeadDirection(dir == null ? null : dir.name());
            dto.setBindCar(car.getBindCar() == null ? null : car.getBindCar().getCarNo());
            dto.setNameEnum(car.getNameEnum() == null ? null : car.getNameEnum().name());
            dto.setPlcIp(carIdToPlcIp.get(id));

            dto.setTick(null);
            dto.setMission(null);
            dto.setLoadStatus(null);
            dto.setTopRodStatus(null);
            dto.setFerryStatus(null);

            Coordinate current = buildCurrentCoordinateFromMiddle(car);
            dto.setCurrentCoordinate(current);
            dto.setTargetCoordinate(new Coordinate(0, 0));

            dto.setLocationStatus(null);
            dto.setConnectStatus(null);
            dto.setCargos(null);
            dto.setCurrentEvent(null);
            dto.setCurrentRelativePosition(0);
            dto.setCurrentAction(null);

            result.add(dto);
        }
        return result;
    }

    private boolean isCarResourceType(String rt) {
        if (rt == null) return false;
        String t = rt.trim().toUpperCase(Locale.ROOT);
        return "CAR".equals(t) || "SUB_CAR".equals(t) || "FERRY_CAR".equals(t);
    }

    private Coordinate buildCurrentCoordinateFromMiddle(Car car) {
        try {
            if (car.getCarPaths() == null || car.getCarPaths().isEmpty()) {
                return new Coordinate(0, 0);
            }
            Long pathId = car.getCarPaths().get(0).getPathId();
            if (pathId == null) {
                return new Coordinate(0, 0);
            }
            Path path = pathManager.getPathById(pathId);
            if (path == null || path.getMiddleCoordinateId() == null) {
                return new Coordinate(0, 0);
            }

            var mid = configurationManager.getCoordinateById(path.getMiddleCoordinateId());
            if (mid == null) {
                return new Coordinate(0, 0);
            }
            return new Coordinate(
                    mid.getX() == null ? 0 : mid.getX(),
                    mid.getY() == null ? 0 : mid.getY()
            );
        } catch (Exception e) {
            return new Coordinate(0, 0);
        }
    }
}
