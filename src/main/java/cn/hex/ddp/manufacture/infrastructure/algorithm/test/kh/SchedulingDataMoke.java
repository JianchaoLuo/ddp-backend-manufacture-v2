package cn.hex.ddp.manufacture.infrastructure.algorithm.test.kh;

import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.OrderInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.OrderProduct;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule.ScheduleOrderInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule.ScheduleOrderItem;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule.ScheduleProductInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.pojo.Product;

import java.util.ArrayList;
import java.util.List;

import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.OrderProductTypeEnum.*;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum.*;
import static cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum.SAND_MOULD;

public class SchedulingDataMoke {
    private SimulateInput input = new SimulateInput();
    private ScheduleOrderInput scheduleOrderInput = new ScheduleOrderInput();

    public SimulateInput createSimulateInput() {
        input.setAllocationInputs(null);
        input.setAnalogInput(null);

        List<Product> hotProducts = new ArrayList<>();
        Product hotSandUp = new Product(null, "上砂箱", 1, UPPER_BOX, null, null, HOT_SAND, false, 0.0, 60,
                600, 60, 60, 60, 60, 60, 60, 60, null);
        Product hotSandMold_1 = new Product(null, "模具", 2, MOULD, null, null, HOT_SAND, false, 0.0, 60,
                600, 60, 60, 60, 60, 60, 60, 60, null);
        Product hotSandDown = new Product(null, "下砂箱", 3, LOWER_BOX, null, null, HOT_SAND, false, 0.0, 60,
                600, 60, 60, 60, 60, 60, 60, 60, null);
        Product hotSandMold_2 = new Product(null, "模具", 4, MOULD, null, null, HOT_SAND, false, 0.0, 60,
                600, 60, 60, 60, 60, 60, 60, 60, null);
        Product coreHotSand = new Product(null, "砂芯", null, SAND_MOULD, null, null, CORE_SAND, false, 0.0, 60,
                600, 60, 60, 60, 60, 60, 60, 60, null);

        List<Product> coldProducts = new ArrayList<>();
        Product coldSandUp = new Product(null, "上砂箱", 1, UPPER_BOX, null, null, COLD_SAND, false, 0.0, 60,
                600, 60, 60, 60, 60, 60, 60, 60, null);
        Product coldSandMold_1 = new Product(null, "模具", 2, MOULD, null, null, COLD_SAND, false, 0.0, 60,
                600, 60, 60, 60, 60, 60, 60, 60, null);
        Product coldSandDown = new Product(null, "下砂箱", 3, LOWER_BOX, null, null, COLD_SAND, false, 0.0, 60,
                600, 60, 60, 60, 60, 60, 60, 60, null);
        Product coldSandMold_2 = new Product(null, "模具", 4, MOULD, null, null, COLD_SAND, false, 0.0, 60,
                600, 60, 60, 60, 60, 60, 60, 60, null);
        Product coreColdSand = new Product(null, "砂芯", null, SAND_MOULD, null, null, CORE_SAND, false, 0.0, 60,
                600, 60, 60, 60, 60, 60, 60, 60, null);

        hotProducts.add(hotSandUp);
        hotProducts.add(hotSandMold_1);
        hotProducts.add(hotSandDown);
        hotProducts.add(hotSandMold_2);
        hotProducts.add(coreHotSand);
        coldProducts.add(coldSandUp);
        coldProducts.add(coldSandMold_1);
        coldProducts.add(coldSandDown);
        coldProducts.add(coldSandMold_2);
        coldProducts.add(coreColdSand);

        OrderProduct orderProduct_1 = new OrderProduct(hotProducts, 2, "", HOT_SAND, 0.0,
                0.0, 0, 0, null, 1L);
        OrderProduct orderProduct_2 = new OrderProduct(coldProducts, 2, "", COLD_SAND, 0.0,
                0.0, 0, 0, null, 2L);
        OrderProduct orderProduct_3 = new OrderProduct(hotProducts, 2, "", HOT_SAND, 0.0,
                0.0, 0, 0, null, 3L);
        OrderProduct orderProduct_4 = new OrderProduct(coldProducts, 2, "", COLD_SAND, 0.0,
                0.0, 0, 0, null, 4L);
        OrderProduct orderProduct_5 = new OrderProduct(coldProducts, 2, "", COLD_SAND, 0.0,
                0.0, 0, 0, null, 5L);

        List<OrderInput> orderInputs = new ArrayList<>();
        orderInputs.add(new OrderInput(10L, List.of(orderProduct_1, orderProduct_2, orderProduct_3), 0.0, 1));
        orderInputs.add(new OrderInput(11L, List.of(orderProduct_4, orderProduct_5), 0.0, 1));

        input.setOrderInputs(orderInputs);
        return input;
    }

    public ScheduleOrderInput createScheduleOrderInput(){
        ScheduleProductInput scheduleProductInput_1 = new ScheduleProductInput(111L, 1L, 10L);
        ScheduleProductInput scheduleProductInput_11 = new ScheduleProductInput(1111L, 1L, 10L);
        ScheduleProductInput scheduleProductInput_2 = new ScheduleProductInput(112L, 2L, 10L);
        ScheduleProductInput scheduleProductInput_22 = new ScheduleProductInput(1122L, 2L, 10L);
        ScheduleProductInput scheduleProductInput_3 = new ScheduleProductInput(113L, 3L, 10L);
        ScheduleProductInput scheduleProductInput_33 = new ScheduleProductInput(1133L, 3L, 10L);
        ScheduleProductInput scheduleProductInput_4 = new ScheduleProductInput(114L, 4L, 11L);
        ScheduleProductInput scheduleProductInput_44 = new ScheduleProductInput(1144L, 4L, 11L);
        ScheduleProductInput scheduleProductInput_5 = new ScheduleProductInput(115L, 5L, 11L);
        ScheduleProductInput scheduleProductInput_55 = new ScheduleProductInput(1155L, 5L, 11L);


        List<ScheduleOrderItem> items = new ArrayList<>();
        items.add(new ScheduleOrderItem(10L, List.of(scheduleProductInput_1, scheduleProductInput_11,
                scheduleProductInput_2, scheduleProductInput_22, scheduleProductInput_3, scheduleProductInput_33), 0.0));
        items.add(new ScheduleOrderItem(12L, List.of(scheduleProductInput_4, scheduleProductInput_44,
                scheduleProductInput_5, scheduleProductInput_55), 0.0));

        scheduleOrderInput.setItems(items);

        return scheduleOrderInput;
    }
}
