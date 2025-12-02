package cn.hex.ddp.manufacture;

import cn.hex.ddp.manufacture.domain.car.enums.CarNameEnum;
import cn.hex.ddp.manufacture.domain.configuration.enums.PositionNameEnum;
import cn.hex.ddp.manufacture.domain.equipment.enums.EquipmentNameEnum;
import cn.hex.ddp.manufacture.domain.path.enums.PathNameEnum;
import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationNameEnum;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;

/**
 * 将枚举类的属性按指定格式输出
 *
 * @author Huhaisen
 * @date 2024/07/31
 */
public class OutPutEnumsTest {
    @Test
    public void enums2Json() {
        // 创建JSONObject
        JSONObject jsonObject = new JSONObject();

        // 遍历CarNameEnum枚举
        for (CarNameEnum carName : CarNameEnum.values()) {
            jsonObject.put(carName.name(), carName.getCode());
        }
        // 遍历PathNameEnum枚举
        for (PathNameEnum pathNameEnum : PathNameEnum.values()) {
            jsonObject.put(pathNameEnum.name(), pathNameEnum.getCode());
        }
        // 遍历PositionNameEnum枚举
        for (PositionNameEnum positionNameEnum : PositionNameEnum.values()) {
            jsonObject.put(positionNameEnum.name(), positionNameEnum.getCode());
        }
        // 遍历WorkstationNameEnum枚举
        for (WorkstationNameEnum workstationNameEnum : WorkstationNameEnum.values()) {
            jsonObject.put(workstationNameEnum.name(), workstationNameEnum.getCode());
        }
        // 遍历EquipmentNameEnum枚举
        for (EquipmentNameEnum equipmentNameEnum : EquipmentNameEnum.values()) {
            jsonObject.put(equipmentNameEnum.name(), equipmentNameEnum.getCode());
        }

        // 将JSONObject写入JSON文件
        try (FileWriter file = new FileWriter("nameEnums.json")) {
            file.write(jsonObject.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("JSON文件已生成");
    }
}
