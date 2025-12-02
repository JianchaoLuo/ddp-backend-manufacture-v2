package cn.hex.ddp.manufacture.api.plc.rest.controller;

import cn.hex.ddp.manufacture.api.common.respond.CommonResult;
import cn.hex.ddp.manufacture.api.common.respond.page.PageResult;
import cn.hex.ddp.manufacture.api.plc.rest.req.*;
import cn.hex.ddp.manufacture.api.plc.rest.vo.PLCVO;
import cn.hex.ddp.manufacture.api.plc.rest.vo.PLCWithResourcesVO;
import cn.hex.ddp.manufacture.application.plc.dto.LoginResponseDTO;
import cn.hex.ddp.manufacture.application.plc.service.PLCService;
import cn.hex.ddp.manufacture.application.plc.service.impl.PLCManualServiceImpl;
import cn.hex.ddp.manufacture.domain.car.model.CarFlatDTO;
import cn.hex.ddp.manufacture.domain.common.exception.ApiException;
import cn.hex.ddp.manufacture.domain.common.utils.PlcUtil;
import cn.hex.ddp.manufacture.domain.plc.model.CarInfoParam;
import cn.hex.ddp.manufacture.domain.plc.model.PLCVariable;
import cn.hex.ddp.manufacture.domain.plc.model.PlcWriteParam;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.plc4x.java.api.PlcConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PLC管理
 *
 * @author fanwenbo
 * @date 2024/5/19
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/api/v2/plc")
public class PLCController {

    /**
     * PLC连接地址
     */
    private static final String PLC_URL = "s7://192.168.10.2";
//    private static final String PLC_URL = "s7://192.168.1.2";
    private static final Boolean isMock = false;

    // 🔹 静态线程与运行标识
    private static Thread motionThread;
    private static volatile boolean running = false;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PLCService plcService;

    @Autowired
    private PLCManualServiceImpl plcManualService;

    /**
     * 获取PLC列表
     *
     * @param listPlcReq
     * @return
     */
    @GetMapping("/page")
    public PageResult<PLCVO> listPlc(@Valid ListPLCReq listPlcReq) {
        return plcService.listPLC(listPlcReq);
    }

    /**
     * 创建PLC
     */
    @PostMapping("")
    public void createPLC(@Valid @RequestBody UpdatePLCReq updatePLCReq) {
        plcService.createPLC(updatePLCReq);
    }

    /**
     * 全量更新PLC
     */
    @PutMapping("/{id}")
    public void updatePLC(@PathVariable Long id, @Valid @RequestBody UpdatePLCReq updatePLCReq) {
        plcService.updatePLC(id, updatePLCReq);
    }

    /**
     * 删除PLC
     */
    @DeleteMapping("/{id}")
    public void deletePLC(@PathVariable("id") Long id) {
        plcService.deletePLC(id);
    }

    /**
     * 获取车辆信息
     */
    @RequestMapping(value = "/getCarInfo", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult getCarInfo(String plcIp) {
        Map<String, String> addressMap = new HashMap<>();
        addressMap.put("isEmergency","%DB2.DBX0.0:BOOL");
        addressMap.put("isRemote","%DB2.DBX0.1:BOOL");
        addressMap.put("isServoError","%DB2.DBX0.2:BOOL");
        addressMap.put("isReadHeadError","%DB2.DBX0.3:BOOL");
        addressMap.put("isNeedClean","%DB2.DBX0.4:BOOL");
        addressMap.put("isInPosition","%DB2.DBX0.5:BOOL");
        addressMap.put("currentPosition","%DB2.DBD2:DINT");
        addressMap.put("currentSpeed","%DB2.DBD6:REAL");

        addressMap.put("remoteEmergency","%DB3.DBX0.0:BOOL");
        addressMap.put("remoteRest","%DB3.DBX0.1:BOOL");
        addressMap.put("remoteStart","%DB3.DBX0.2:BOOL");
        addressMap.put("targetPosition","%DB3.DBD2:DINT");
        addressMap.put("targetSpeed","%DB3.DBD6:REAL");

        PlcConnection plcConnection = PlcUtil.createConnection(plcIp);
        if (!plcConnection.getMetadata().isReadSupported()) {
            log.error("当前PLC连接不支持读数据");
            throw new ApiException("当前PLC连接不支持读数据");
        }

        Map<String, Object> resultMap = PlcUtil.readPlcDataBatch(plcConnection, addressMap);
        //使用ObjectMapper将Map转换为CarInfoParam对象
        CarInfoParam carInfoParam = objectMapper.convertValue(resultMap, CarInfoParam.class);
        return CommonResult.success(carInfoParam);
    }

    /**
     * 远程急停
     */
    @RequestMapping(value = "/remoteEmergencyStop", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult remoteEmergencyStop(String plcIp) {
        if(isMock) {
            running = false;
            if (motionThread != null) {
                motionThread.interrupt();
                motionThread = null;
            }
            // ⚙️ 模拟环境下立即写速度为 0
            PlcConnection plcConn = PlcUtil.createConnection(plcIp);
            PlcWriteParam speedParam = new PlcWriteParam();
            speedParam.setName("currentSpeed");
            speedParam.setAddress("%DB2.DBD6:REAL");
            speedParam.setValue(0.0);
            PlcUtil.writePlcData(plcConn, speedParam);
        }

        // 实际PLC控制逻辑
        PlcWriteParam writeParam = new PlcWriteParam();
        writeParam.setName("remoteEmergency");
        writeParam.setAddress("%DB3.DBX0.0:BOOL");
        writeParam.setValue(true);

        PlcConnection plcConnection = PlcUtil.createConnection(plcIp);
        if (!plcConnection.getMetadata().isWriteSupported()) {
            log.error("当前PLC连接不支持写数据");
            throw new ApiException("当前PLC连接不支持写数据");
        }

        if (PlcUtil.writePlcData(plcConnection, writeParam)) {
            return CommonResult.success();
        } else {
            return CommonResult.failure("数据写入失败！");
        }
    }

    /**
     * 远程急停复位
     */
    @RequestMapping(value = "/remoteEmergencyReset", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult remoteEmergencyReset(String plcIp) {
        if(isMock) {
            running = false;
            if (motionThread != null) {
                motionThread.interrupt();
                motionThread = null;
            }
            // ② 设置目标位置为 0
            PlcConnection initPlcConnection = PlcUtil.createConnection(plcIp);
            PlcWriteParam writeParam = new PlcWriteParam();
            writeParam.setName("targetPosition");
            writeParam.setAddress("%DB3.DBD2:DINT");
            writeParam.setValue(0);
            PlcUtil.writePlcData(initPlcConnection, writeParam);

            // 创建PLC连接
            PlcConnection plcConnection = PlcUtil.createConnection(plcIp);
            // 获取初始状态
            Map<String, String> addressMap = new HashMap<>();
            addressMap.put("currentPosition","%DB2.DBD2:DINT");
            addressMap.put("currentSpeed","%DB2.DBD6:REAL");
            addressMap.put("targetPosition","%DB3.DBD2:DINT");
            addressMap.put("targetSpeed","%DB3.DBD6:REAL");
            Map<String, Object> plcData = PlcUtil.readPlcDataBatch(plcConnection, addressMap);

            CarStatus status = new CarStatus();
            status.currentPosition = ((Number) plcData.get("currentPosition")).doubleValue();
            status.currentSpeed = ((Number) plcData.get("currentSpeed")).doubleValue();
            double targetPosition = ((Number) plcData.get("targetPosition")).doubleValue();
            double targetSpeed = ((Number) plcData.get("targetSpeed")).doubleValue();

            double startPosition = status.currentPosition;
            boolean forward = targetPosition > startPosition;

            // 启动线程
            running = true;
            motionThread = new Thread(() -> {
                try {
                    while (running) {

                        PlcConnection plcConnection1 = PlcUtil.createConnection(plcIp);
                        status.currentSpeed = targetSpeed;

                        if (forward) {
                            status.currentPosition += status.currentSpeed;
                            if (status.currentPosition >= targetPosition) {
                                status.currentPosition = targetPosition;
                                status.currentSpeed = 0;
                            }
                        } else {
                            status.currentPosition -= status.currentSpeed;
                            if (status.currentPosition <= targetPosition) {
                                status.currentPosition = targetPosition;
                                status.currentSpeed = 0;
                            }
                        }

                        // 写回PLC
                        PlcWriteParam positionParam = new PlcWriteParam();
                        positionParam.setName("currentPosition");
                        positionParam.setAddress("%DB2.DBD2:DINT");
                        positionParam.setValue((int) status.currentPosition);
                        PlcUtil.writePlcData(plcConnection1, positionParam);
                        PlcConnection plcConnection2 = PlcUtil.createConnection(plcIp);

                        PlcWriteParam speedParam = new PlcWriteParam();
                        speedParam.setName("currentSpeed");
                        speedParam.setAddress("%DB2.DBD6:REAL");
                        speedParam.setValue(status.currentSpeed);
                        PlcUtil.writePlcData(plcConnection2, speedParam);

                        Thread.sleep(500);
                        if (status.currentSpeed == 0) break;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    running = false;
                    motionThread = null;
                    log.info("远程复位线程已停止");
                }
            });
            motionThread.start();
        }

        // 实际PLC控制逻辑
        PlcWriteParam writeParam = new PlcWriteParam();
        writeParam.setName("remoteEmergency");
        writeParam.setAddress("%DB3.DBX0.0:BOOL");
        writeParam.setValue(false);

        PlcConnection plcConnection = PlcUtil.createConnection(plcIp);
        if (!plcConnection.getMetadata().isWriteSupported()) {
            log.error("当前PLC连接不支持写数据");
            throw new ApiException("当前PLC连接不支持写数据");
        }

        if (PlcUtil.writePlcData(plcConnection, writeParam)) {
            return CommonResult.success();
        } else {
            return CommonResult.failure("数据写入失败！");
        }
    }

    class CarStatus {
        double currentPosition;
        double currentSpeed;
    }

    @RequestMapping(value = "/remoteStart", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult remoteStart(String plcIp) {
        if(isMock) {
            if (running) {
                return CommonResult.failure("当前已有运动线程在运行");
            }
            // 创建PLC连接
            PlcConnection plcConnection = PlcUtil.createConnection(plcIp);
            if (!plcConnection.getMetadata().isWriteSupported()) {
                log.error("当前PLC连接不支持写数据");
                throw new ApiException("当前PLC连接不支持写数据");
            }

            // 获取初始状态
            Map<String, String> addressMap = new HashMap<>();
            addressMap.put("currentPosition","%DB2.DBD2:DINT");
            addressMap.put("currentSpeed","%DB2.DBD6:REAL");
            addressMap.put("targetPosition","%DB3.DBD2:DINT");
            addressMap.put("targetSpeed","%DB3.DBD6:REAL");
            Map<String, Object> plcData = PlcUtil.readPlcDataBatch(plcConnection, addressMap);

            CarStatus status = new CarStatus();
            status.currentPosition = ((Number) plcData.get("currentPosition")).doubleValue();
            status.currentSpeed = ((Number) plcData.get("currentSpeed")).doubleValue();
            double targetPosition = ((Number) plcData.get("targetPosition")).doubleValue();
            double targetSpeed = ((Number) plcData.get("targetSpeed")).doubleValue();

            double startPosition = status.currentPosition;
            boolean forward = targetPosition > startPosition;

            // 启动线程
            running = true;
            motionThread = new Thread(() -> {
                try {
                    while (running) {

                        PlcConnection plcConnection1 = PlcUtil.createConnection(plcIp);
                        status.currentSpeed = targetSpeed;

                        if (forward) {
                            status.currentPosition += status.currentSpeed;
                            if (status.currentPosition >= targetPosition) {
                                status.currentPosition = targetPosition;
                                status.currentSpeed = 0;
                            }
                        } else {
                            status.currentPosition -= status.currentSpeed;
                            if (status.currentPosition <= targetPosition) {
                                status.currentPosition = targetPosition;
                                status.currentSpeed = 0;
                            }
                        }

                        // 写回PLC
                        PlcWriteParam positionParam = new PlcWriteParam();
                        positionParam.setName("currentPosition");
                        positionParam.setAddress("%DB2.DBD2:DINT");
                        positionParam.setValue((int) status.currentPosition);
                        PlcUtil.writePlcData(plcConnection1, positionParam);
                        PlcConnection plcConnection2 = PlcUtil.createConnection(plcIp);

                        PlcWriteParam speedParam = new PlcWriteParam();
                        speedParam.setName("currentSpeed");
                        speedParam.setAddress("%DB2.DBD6:REAL");
                        speedParam.setValue(status.currentSpeed);
                        PlcUtil.writePlcData(plcConnection2, speedParam);

                        Thread.sleep(500);
                        if (status.currentSpeed == 0) break;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    running = false;
                    motionThread = null;
                    log.info("运动线程已停止");
                }
            });

            motionThread.start();
            return CommonResult.success();
        }

        // 实际PLC控制逻辑
        PlcWriteParam writeParam = new PlcWriteParam();
        writeParam.setName("remoteStart");
        writeParam.setAddress("%DB3.DBX0.2:BOOL");
        writeParam.setValue(true);

        PlcConnection plcConnection = PlcUtil.createConnection(plcIp);
        if (!plcConnection.getMetadata().isWriteSupported()) {
            log.error("当前PLC连接不支持写数据");
            throw new ApiException("当前PLC连接不支持写数据");
        }

        if (PlcUtil.writePlcData(plcConnection, writeParam)) {
            return CommonResult.success();
        } else {
            return CommonResult.failure("数据写入失败！");
        }
    }


    /**
     * 停止
     */
    @RequestMapping(value = "/remoteStop", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult remoteStop(String plcIp) {
        if(isMock) {
            running = false;
            if (motionThread != null) {
                motionThread.interrupt();
                motionThread = null;
            }
            // ⚙️ 模拟环境下立即写速度为 0
            PlcConnection plcConn = PlcUtil.createConnection(plcIp);
            PlcWriteParam speedParam = new PlcWriteParam();
            speedParam.setName("currentSpeed");
            speedParam.setAddress("%DB2.DBD6:REAL");
            speedParam.setValue(0.0);
            PlcUtil.writePlcData(plcConn, speedParam);
        }

        // 实际PLC控制逻辑
        PlcWriteParam writeParam = new PlcWriteParam();
        writeParam.setName("remoteStop");
        writeParam.setAddress("%DB3.DBX0.2:BOOL");
        writeParam.setValue(false);

        PlcConnection plcConnection = PlcUtil.createConnection(plcIp);
        if (!plcConnection.getMetadata().isWriteSupported()) {
            log.error("当前PLC连接不支持写数据");
            throw new ApiException("当前PLC连接不支持写数据");
        }

        if (PlcUtil.writePlcData(plcConnection, writeParam)) {
            return CommonResult.success();
        } else {
            return CommonResult.failure("数据写入失败！");
        }
    }

    /**
     * 远程复位
     */
    @RequestMapping(value = "/remoteRest", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult remoteRest(String plcIp) {
        if(isMock) {
            running = false;
            if (motionThread != null) {
                motionThread.interrupt();
                motionThread = null;
            }
            // ② 设置目标位置为 0
            PlcConnection initPlcConnection = PlcUtil.createConnection(plcIp);
            PlcWriteParam writeParam = new PlcWriteParam();
            writeParam.setName("targetPosition");
            writeParam.setAddress("%DB3.DBD2:DINT");
            writeParam.setValue(0);
            PlcUtil.writePlcData(initPlcConnection, writeParam);

            // 创建PLC连接
            PlcConnection plcConnection = PlcUtil.createConnection(plcIp);
            // 获取初始状态
            Map<String, String> addressMap = new HashMap<>();
            addressMap.put("currentPosition","%DB2.DBD2:DINT");
            addressMap.put("currentSpeed","%DB2.DBD6:REAL");
            addressMap.put("targetPosition","%DB3.DBD2:DINT");
            addressMap.put("targetSpeed","%DB3.DBD6:REAL");
            Map<String, Object> plcData = PlcUtil.readPlcDataBatch(plcConnection, addressMap);

            CarStatus status = new CarStatus();
            status.currentPosition = ((Number) plcData.get("currentPosition")).doubleValue();
            status.currentSpeed = ((Number) plcData.get("currentSpeed")).doubleValue();
            double targetPosition = ((Number) plcData.get("targetPosition")).doubleValue();
            double targetSpeed = ((Number) plcData.get("targetSpeed")).doubleValue();

            double startPosition = status.currentPosition;
            boolean forward = targetPosition > startPosition;

            // 启动线程
            running = true;
            motionThread = new Thread(() -> {
                try {
                    while (running) {

                        PlcConnection plcConnection1 = PlcUtil.createConnection(plcIp);
                        status.currentSpeed = targetSpeed;

                        if (forward) {
                            status.currentPosition += status.currentSpeed;
                            if (status.currentPosition >= targetPosition) {
                                status.currentPosition = targetPosition;
                                status.currentSpeed = 0;
                            }
                        } else {
                            status.currentPosition -= status.currentSpeed;
                            if (status.currentPosition <= targetPosition) {
                                status.currentPosition = targetPosition;
                                status.currentSpeed = 0;
                            }
                        }

                        // 写回PLC
                        PlcWriteParam positionParam = new PlcWriteParam();
                        positionParam.setName("currentPosition");
                        positionParam.setAddress("%DB2.DBD2:DINT");
                        positionParam.setValue((int) status.currentPosition);
                        PlcUtil.writePlcData(plcConnection1, positionParam);
                        PlcConnection plcConnection2 = PlcUtil.createConnection(plcIp);

                        PlcWriteParam speedParam = new PlcWriteParam();
                        speedParam.setName("currentSpeed");
                        speedParam.setAddress("%DB2.DBD6:REAL");
                        speedParam.setValue(status.currentSpeed);
                        PlcUtil.writePlcData(plcConnection2, speedParam);

                        Thread.sleep(500);
                        if (status.currentSpeed == 0) break;
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    running = false;
                    motionThread = null;
                    log.info("远程复位线程已停止");
                }
            });
            motionThread.start();
        }

        // 实际PLC控制逻辑
        PlcWriteParam writeParam = new PlcWriteParam();
        writeParam.setName("remoteRest");
        writeParam.setAddress("%DB3.DBX0.1:BOOL");
        writeParam.setValue(false);

        PlcConnection plcConnection = PlcUtil.createConnection(plcIp);
        if (!plcConnection.getMetadata().isWriteSupported()) {
            log.error("当前PLC连接不支持写数据");
            throw new ApiException("当前PLC连接不支持写数据");
        }

        if (PlcUtil.writePlcData(plcConnection, writeParam)) {
            return CommonResult.success();
        } else {
            return CommonResult.failure("数据写入失败！");
        }
    }

    /**
     * 设定目标位置
     */
    @RequestMapping(value = "/setTargetPosition", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult setTargetPosition(String plcIp, Integer position) {
        PlcWriteParam writeParam = new PlcWriteParam();
        writeParam.setName("targetPosition");
        writeParam.setAddress("%DB3.DBD2:DINT");
        writeParam.setValue(position);

        PlcConnection plcConnection = PlcUtil.createConnection(plcIp);
        if (!plcConnection.getMetadata().isWriteSupported()) {
            log.error("当前PLC连接不支持写数据");
            throw new ApiException("当前PLC连接不支持写数据");
        }

        if (PlcUtil.writePlcData(plcConnection, writeParam)) {
            return CommonResult.success();
        } else {
            return CommonResult.failure("数据写入失败！");
        }
    }

    /**
     * 设定目标速度
     */
    @RequestMapping(value = "/setTargetSpeed", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult setTargetSpeed(String plcIp, Double speed) {
        PlcWriteParam writeParam = new PlcWriteParam();
        writeParam.setName("targetPosition");
        writeParam.setAddress("%DB3.DBD6:REAL");
        writeParam.setValue(speed);

        PlcConnection plcConnection = PlcUtil.createConnection(plcIp);
        if (!plcConnection.getMetadata().isWriteSupported()) {
            log.error("当前PLC连接不支持写数据");
            throw new ApiException("当前PLC连接不支持写数据");
        }

        if (PlcUtil.writePlcData(plcConnection, writeParam)) {
            return CommonResult.success();
        } else {
            return CommonResult.failure("数据写入失败！");
        }
    }


    /**
     * PLC登录
     * @return
     */
    @GetMapping("/login")
    public LoginResponseDTO login() {
        String url = "http://127.0.0.1:7080/exlog"; // 登录地址
        String grm = "40414070104"; // 用户名或设备编号
        String pass = "123";         // 密码

        return plcManualService.login(url, grm, pass);
    }

    /**
     * 读取PLC变量
     * @param loginResponseDTO
     * @return
     */
    @PostMapping("/read")
    public List<PLCVariable> read(@RequestBody LoginResponseDTO loginResponseDTO) {
        return plcManualService.readVariables(loginResponseDTO.getAddr(), loginResponseDTO.getSid(), loginResponseDTO.getVariableNames());
    }

    /**
     * 枚举变量
     * @param loginResponseDTO
     * @return
     */
    @PostMapping("/enumerate")
    public List<PLCVariable> enumerate(@RequestBody LoginResponseDTO loginResponseDTO) {
        return plcManualService.enumerateVariables(loginResponseDTO.getAddr(), loginResponseDTO.getSid());
    }

    /**
     * 写变量
     * @param writeRequest
     * @return
     */
    @PostMapping("/write")
    public List<PLCVariable> write(@RequestBody WriteVariableReq writeRequest) {
        return plcManualService.writeVariables(
                writeRequest.getAddr(),
                writeRequest.getSid(),
                writeRequest.getVariables()
        );
    }

    /**
     * 获取仿真数据（车辆）
     */
    @GetMapping("/simulator")
    public List<CarFlatDTO> getSimulatorCars() {
        return plcManualService.getSimulatorCars();
    }


    /**
     * 新增PLC绑定资源
     */
    @PostMapping("/resource")
    public void AddPLCResource(@RequestBody @Validated AddPLCResourceReq  addPLCResourceReq){
        plcService.addPLCResource(addPLCResourceReq.getPlcId(), addPLCResourceReq.getResourceId());
    }


    /**
     *  修改 PLC 绑定资源
     */
    @PostMapping("/update/{plcId}")
    public void updatePLCResource(@PathVariable("plcId") Long plcId, @RequestBody @Validated UpdatePLCResourceReq  updatePLCResourceReq){
        plcService.updatePLCResource(plcId, updatePLCResourceReq.getResourceId());
    }

    /**
     * 查询 PLC 绑定资源
     */
    @GetMapping("/{id}/resources")
    public CommonResult listResourcesByPlc(@PathVariable("id") Long id) {
        return CommonResult.success(plcService.getResourcesByPlcId(id));
    }

    /**
     * 查询全部 PLC 及其绑定资源
     */
    @GetMapping("/with-resources")
    public CommonResult listAllPlcWithResources() {
        return CommonResult.success(plcService.listAllPlcWithResources());
    }

    /**
     * 分页查询 PLC 及其绑定资源
     */
    @GetMapping("/with-resources/page")
    public PageResult<PLCWithResourcesVO> pagePlcWithResources(@Valid ListPLCReq req) {
        return plcService.pagePlcWithResources(req);
    }

    /**
     * 删除 PLC 绑定资源
     */
    @DeleteMapping("/{plcId}/resources")
    public void deletePLCResourcesByPLCId(@PathVariable("plcId") Long plcId) {
        plcService.deletePLCResourcesByPLCId(plcId);
    }


}
