package cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.testinput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule.ScheduleOrderInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule.ScheduleOrderItem;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule.ScheduleProductInput;

import java.util.*;

/**
 * @author LiuYongkang
 * @date 2025/7/4
 * @Description
 */


public class SchedulingCreateSeparateTestInput {
    public static ScheduleOrderInput createScheduleOrderInput() {
        // 产品列表1
        ScheduleProductInput product1 = new ScheduleProductInput(1001L, 2001L, 3001L);
        ScheduleProductInput product2 = new ScheduleProductInput(1002L, 2002L, 3001L);
        List<ScheduleProductInput> productList1 = new ArrayList<>();
        productList1.add(product1);
        productList1.add(product2);

        // 产品列表2
        ScheduleProductInput product3 = new ScheduleProductInput(1003L, 2003L, 3002L);
        List<ScheduleProductInput> productList2 = new ArrayList<>();
        productList2.add(product3);

        // 订单项1
        ScheduleOrderItem orderItem1 = new ScheduleOrderItem(3001L, productList1, 120.0);
        // 订单项2
        ScheduleOrderItem orderItem2 = new ScheduleOrderItem(3002L, productList2, 180.0);

        List<ScheduleOrderItem> items = new ArrayList<>();
        items.add(orderItem1);
        items.add(orderItem2);

        return new ScheduleOrderInput(items);
    }

    public static Map<Long, Double> createOrderProductFiTimeMap() {
        Map<Long, Double> orderProductFiTimeMap = new HashMap<>();
        // 对应 createScheduleOrderInput 里的产品型号id
        orderProductFiTimeMap.put(2001L, 60.0); // product1
        orderProductFiTimeMap.put(2002L, 75.0); // product2
        orderProductFiTimeMap.put(2003L, 90.0); // product3
        return orderProductFiTimeMap;
    }

}
