package cn.hex.ddp.manufacture;

import cn.hex.ddp.manufacture.domain.common.utils.JsonValidateUtils;
import org.junit.jupiter.api.Test;

public class JsonValidateTest {
    @Test
    public void jsonValidateTest() {
        String jsonStr = "{\"name\":\"ddp\",\"age\":25,\"gender\":\"male\"}";
        boolean isValid = JsonValidateUtils.isValidJson(jsonStr);
        System.out.println(isValid);

        String parameters = """
                        {"emptySpeed": 0.33,"fullSpeed": 0.25,"topRodRaiseOrFallTime": 30}
                        """;
        System.out.println(JsonValidateUtils.isValidJson(parameters));

        String parameters2 = """
                        {"emptySpeed": 0.25,"fullSpeed": 0.17}
                        """;
        System.out.println(JsonValidateUtils.isValidJson(parameters2));

        String parameters3 = """
                        ["a","b","c"]
                        """;
        System.out.println(JsonValidateUtils.isValidJson(parameters3));

        String parameters4 = """
                        [{"name": "emptySpeed","value": 0.33,"default": true},\
                         {"name": "fullSpeed","value": 0.25,"default": true},\
                         {"name": "topRodRaiseOrFallTime","value": 30,"default": true}]""";
        System.out.println("parameters4: " + JsonValidateUtils.isValidJson(parameters4));

        String parameters5 = """
                        [{"name": "emptySpeed","value": 0.25,"default": true},\
                         {"name": "fullSpeed","value": 0.17,"default": true}]""";
        System.out.println("parameters5: " + JsonValidateUtils.isValidJson(parameters5));

        String parameters6 = "";
        System.out.println("parameters6: " + JsonValidateUtils.isValidJson(parameters6));
    }
}
