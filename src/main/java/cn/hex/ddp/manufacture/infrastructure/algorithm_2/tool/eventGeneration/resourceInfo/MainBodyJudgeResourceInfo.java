package cn.hex.ddp.manufacture.infrastructure.algorithm_2.tool.eventGeneration.resourceInfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: bobo
 * @Description: 对于主体判断节点，节点存储的资源相关信息
 * @DateTime: 2025/3/21 12:43
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MainBodyJudgeResourceInfo<R extends Enum<R>> {

    //分裂目标资源
    private R splitResource;

    @Override
    public String toString(){
        return "MainBodyJudgeResourceInfo{" +
                "\nsplitResources=" + splitResource +
                "\n}";
    }
}
