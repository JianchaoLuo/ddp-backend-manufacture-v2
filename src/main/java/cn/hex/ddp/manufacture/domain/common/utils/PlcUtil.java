package cn.hex.ddp.manufacture.domain.common.utils;

/**
 * PLC工具类
 * @author 冯泽邦
 * @date 2025/10/20
 */

import cn.hex.ddp.manufacture.domain.plc.model.PlcWriteParam;
import cn.hex.ddp.manufacture.domain.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.plc4x.java.api.PlcConnection;
import org.apache.plc4x.java.api.PlcDriverManager;
import org.apache.plc4x.java.api.exceptions.PlcConnectionException;
import org.apache.plc4x.java.api.messages.PlcReadRequest;
import org.apache.plc4x.java.api.messages.PlcReadResponse;
import org.apache.plc4x.java.api.messages.PlcWriteRequest;
import org.apache.plc4x.java.api.messages.PlcWriteResponse;
import org.apache.plc4x.java.api.types.PlcResponseCode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class PlcUtil {

    /**
     * PLC创建链接
     */
    public static PlcConnection createConnection(String plcUrl) {
        PlcConnection plcConnection;
        try {
            plcConnection = PlcDriverManager.getDefault().getConnectionManager().getConnection(plcUrl);
        } catch (PlcConnectionException e) {
            log.error("PLC建立连接异常：{}", e.getMessage());
            throw new ApiException("PLC建立连接异常");
        }
        return plcConnection;
    }

    /**
     * PLC读数据
     */
    public static Map<String, Object> readPlcDataBatch(PlcConnection plcConnection, Map<String, String> addressMap) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            PlcReadRequest.Builder builder = plcConnection.readRequestBuilder();

            for (String key : addressMap.keySet()) {
                builder.addTagAddress(key, addressMap.get(key));
            }

            PlcReadRequest readRequest = builder.build();
            PlcReadResponse response = readRequest.execute().get(5000, TimeUnit.MILLISECONDS);
            plcConnection.close();

            // 处理响应结果并将其存储到结果映射中
            for (String key : addressMap.keySet()) {
                String address = addressMap.get(key);
                if (response.getResponseCode(key) == PlcResponseCode.OK) {
                    Object value = null;
                    // 根据地址的类型选择相应的获取方法
                    if (address.contains(":BOOL")) {
                        value = response.getBoolean(key); // 布尔类型
                    } else if (address.contains(":BYTE")) {
                        value = response.getByte(key); // 字节类型
                    } else if (address.contains(":WORD")) {
                        value = response.getShort(key); // 16位整数 (WORD)
                    } else if (address.contains(":DWORD")) {
                        value = response.getInteger(key); // 32位整数 (DWORD)
                    } else if (address.contains(":REAL")) {
                        value = response.getFloat(key); // 实数 (REAL)
                    } else if (address.contains(":STRING")) {
                        value = response.getString(key); // 字符串类型
                    } else if (address.contains(":DINT")) {
                        value = response.getInteger(key); // 32位整数 (DINT)
                    } else {
                        value = response.getObject(key);
                        // 你可以在这里扩展更多类型支持
                        log.error("未知数据类型: {} - {}", key, address);
                    }
                    // 将读取的值存储到结果映射中
                    resultMap.put(key, value);
                } else {
                    log.error("读取失败，字段：{}，地址：{}，响应结果：{}", key, address, response.getResponseCode(key));
                    throw new ApiException("读取失败");
                }
            }
        } catch  (TimeoutException | InterruptedException | ExecutionException e)  {
            log.error("PLC读取异常：{}", e.getMessage());
            throw new ApiException("PLC读取异常");
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
        return resultMap;  // 返回包含所有读取结果的 Map
    }

    /**
     * PLC写数据
     */
    public static Boolean writePlcData(PlcConnection plcConnection, PlcWriteParam plcWriteParam) {
        String name = plcWriteParam.getName();
        String address = plcWriteParam.getAddress();
        Object value = plcWriteParam.getValue();

        try {
            PlcWriteRequest.Builder builder = plcConnection.writeRequestBuilder();
            builder.addTagAddress(name, address, value);
            PlcWriteRequest writeRequest = builder.build();
            PlcWriteResponse response = writeRequest.execute().get(5000, TimeUnit.MILLISECONDS);
            plcConnection.close();
            // 处理响应结果
            if (response.getResponseCode(name) != PlcResponseCode.OK) {
                log.error("写入失败，字段：{}，地址：{}，响应结果：{}", name, address, response.getResponseCode(name));
                return false;
            }
        } catch  ( TimeoutException | InterruptedException | ExecutionException e)  {
            log.error("PLC写入异常：{}", e.getMessage());
            throw new ApiException("PLC写入异常");
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
        return true;
    }
}
