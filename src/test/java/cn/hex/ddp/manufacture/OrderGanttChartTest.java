package cn.hex.ddp.manufacture;

import cn.hex.ddp.manufacture.domain.order.manager.OrderManager;
import cn.hex.ddp.manufacture.domain.order.model.OrderGanttChart;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单甘特图测试类
 *
 * @author Huhaisen
 * @date 2024/08/11
 */
@SpringBootTest
public class OrderGanttChartTest {

    @Autowired
    private OrderManager orderManager;

    @Test
    public void addGanttChartTest() {
        Map<Long, Double> orderTimeMap = new HashMap<>();
        orderTimeMap.put(1111L, 60.0 * 60 * 24);
        orderTimeMap.put(2222L, 60.0 * 60 * 24 * 2);
        orderManager.addOrderGanttChart(1L, LocalDateTime.of(2024, 8, 11, 10, 0), orderTimeMap);
    }

    @Test
    public void getGanttChartTest() {
        List<OrderGanttChart> orderGanttChart = orderManager.getOrderGanttChart(1L);
        for (OrderGanttChart ganttChart : orderGanttChart) {
            System.out.println(ganttChart);
        }
    }
}
