package cn.hex.ddp.manufacture.infrastructure.schedule;

import cn.hex.ddp.manufacture.domain.product.enums.ProductionProgressStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.output.SchedulingOutPut;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.output.SchedulingProduct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

/**
 * @author:bobo
 * @description: 测试排产算法转换方法
 * @date:2025/6/4 16:29
 */
@SpringBootTest
public class Algorithm2InfraScheduleTest {


    // 固定订单ID列表（需与输入一致）
    private static final List<Long> VALID_ORDER_IDS = Arrays.asList(
            1838136590343442434L, 1822549369557192705L, 1838136728684171266L,
            1806549499227209729L, 1822898184333082625L, 1796430539718234114L,
            1796140833251065858L, 1823590261250387970L, 1876963030868226049L,
            1876962438267596801L, 1822677576592195585L, 1855864868892110849L,
            1909507159028199425L, 1909530321665191937L, 1913216386635710466L
    );

    // 固定产品ID列表（需与输入一致）
    private static final List<Long> VALID_PRODUCT_IDS = Arrays.asList(
            1855864379030958081L,
            1876962143399636993L,
            1876962060671184897L,
            1894630614768861186L,
            1793271069671546881L,
            1927640202804785154L
    );

    public static SchedulingOutPut generate20DaysSchedulingPlan() {
        // 获取当前时间
        LocalDateTime currentDate = LocalDateTime.now();

        // 用于存储生产计划
        Map<LocalDateTime, List<SchedulingProduct>> productionPlan = new HashMap<>();
        // 用于存储订单完成时间
        Map<Long, LocalDateTime> orderFinishTime = new HashMap<>();

        Random random = new Random();

        // 生成20天的排产计划
        for (int i = 0; i < 20; i++) {
            LocalDateTime date = currentDate.plusDays(i);
            List<SchedulingProduct> products = new ArrayList<>();

            // 每天随机生产3-8个产品
            int dailyProductCount = 3 + random.nextInt(6);
            for (int j = 0; j < dailyProductCount; j++) {
                // 从固定列表中随机选择订单ID和产品ID
                Long orderId = VALID_ORDER_IDS.get(random.nextInt(VALID_ORDER_IDS.size()));
                Long productId = VALID_PRODUCT_IDS.get(random.nextInt(VALID_PRODUCT_IDS.size()));
                Long id = System.currentTimeMillis() + j; // 使用时间戳+序号生成唯一ID

                // 完全随机选择生产进度状态
                ProductionProgressStatusEnum[] statuses = ProductionProgressStatusEnum.values();
                ProductionProgressStatusEnum productionProgress = statuses[random.nextInt(statuses.length)];

                SchedulingProduct product = new SchedulingProduct(id, productId, orderId, productionProgress);
                products.add(product);

                // 如果产品状态是已完成，记录订单完成时间
                if (productionProgress == ProductionProgressStatusEnum.COMPLETED) {
                    orderFinishTime.put(orderId, date);
                }
            }

            productionPlan.put(date, products);
        }

        return new SchedulingOutPut(productionPlan, orderFinishTime);
    }

//    @Test
//    public void testInsertSchedulePlan() {
//        SchedulingOutPut output = generate20DaysSchedulingPlan();
//        algorithm2InfraSchedule.updateOrderFinishTime(output.getOrderFinishTime());
//        algorithm2InfraSchedule.insertSchedulePlan(output.getProductionPlan());
//    }
}



