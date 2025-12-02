package cn.hex.ddp.manufacture.domain.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON校验工具类
 *
 * @author Huhaisen
 * @date 2024/06/15
 */
public class JsonValidateUtils {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static boolean isValidJson(String jsonString) {
        try {
            OBJECT_MAPPER.readTree(jsonString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
