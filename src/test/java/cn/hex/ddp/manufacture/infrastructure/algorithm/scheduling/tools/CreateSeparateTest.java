package cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.tools;

import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule.ScheduleOrderInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.Separate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.testinput.SchedulingCreateSeparateTestInput;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author LiuYongkang
 * @date 2025/7/3
 * @Description
 */
class CreateSeparateTest {

    @Test
    void createSeparate_1() {
        double lastCompletionTime = 0;
        Map<Long, Double> orderProductFiTimeMap = SchedulingCreateSeparateTestInput.createOrderProductFiTimeMap();
        ScheduleOrderInput scheduleOrderInput = SchedulingCreateSeparateTestInput.createScheduleOrderInput();

        var separate = CreateSeparate.createSeparate_1(lastCompletionTime, orderProductFiTimeMap, scheduleOrderInput);

        assertNotNull(separate);
        assertNotNull(separate.getProductSeparates());
        assertTrue(separate.getProductSeparates().size() > 0);

        printSeparate(separate); // 打印排产计划
    }

    private void printSeparate(Separate separate) {
        if (separate == null || separate.getProductSeparates() == null) {
            System.out.println("Separate为空或无排产计划。");
            return;
        }
        int day = 1;
        for (var ps : separate.getProductSeparates()) {
            System.out.println("第" + day + "天排产计划：");
            System.out.println("  模拟完成时间: " + ps.getFinishTime());
            if (ps.getCurrentSeparate() != null) {
                for (var pfs : ps.getCurrentSeparate()) {
                    System.out.println("    产品id: " + pfs.getId()
                            + ", 产品型号id: " + pfs.getProductId()
                            + ", 订单id: " + pfs.getOrderId()
                            + ", 生产进度: " + pfs.getProductionProgress());
                }
            }
            day++;
        }
    }
}