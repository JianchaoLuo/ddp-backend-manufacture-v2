package cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.tools;

import cn.hex.ddp.manufacture.domain.product.enums.ProductionProgressStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule.ScheduleOrderInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule.ScheduleOrderItem;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule.ScheduleProductInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.Separate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.util.ProductForSeparate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.util.ProductSeparate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2025/5/28 下午4:21
 */
public class CreateSeparate {
    /**
     * 生成初始解的函数
     * 考虑每个订单的截止时间，根据订单的完成时间生成优先级，先将优先级高的订单尽量满足排产，依次类推，尝试生成可行的初始解。
     * 考虑优先级最高的订单的产品，按照订单的完成时间进行排序，依次访问订单中的产品List添加到当日计划中。
     *
     * @param lastCompletionTime    最晚完成时间
     * @param orderProductFiTimeMap 每个产品i单独仿真的完成时间fi
     * @param scheduleOrderInput    排产订单输入
     * @return 生成的初始解
     */
    public static Separate createSeparate_1(double lastCompletionTime, Map<Long, Double> orderProductFiTimeMap, ScheduleOrderInput scheduleOrderInput) {
        List<ProductSeparate> productSeparates = new ArrayList<>();
        // 按照订单的交付时间进行排序
        List<ScheduleOrderItem> sortedOrders = new ArrayList<>(scheduleOrderInput.getItems());
        sortedOrders.sort(Comparator.comparing(ScheduleOrderItem::getDeliveryTime));
        // 时间以分钟为单位刻度
        int dayMinutes = 24 * 60;
        List<ProductForSeparate> buffer = new ArrayList<>();
        double currentDayTime = 0;

        //遍历优先级订单
        for (ScheduleOrderItem order : sortedOrders) {
            //遍历订单中的产品
            for (ScheduleProductInput product : order.getSchesuleProductList()) {
                Long productId = product.getProductId();
                //拿到每个产品的单独仿真完成时间
                Double fiTime = orderProductFiTimeMap.get(productId);
                if (fiTime == null || fiTime <= 0) continue;
                // 如果加上当前产品后超出一天最大时间，则当天计划完成
                if (currentDayTime + fiTime > dayMinutes) {
                    if (!buffer.isEmpty()) {
                        productSeparates.add(new ProductSeparate(new ArrayList<>(buffer), currentDayTime));
                        buffer.clear();
                        currentDayTime = 0;
                    }
                }
                //将当前产品添加到当天计划中(产品生产流程默认为COMPLETED)
                buffer.add(new ProductForSeparate(product.getId(), productId, product.getOrderId(), ProductionProgressStatusEnum.COMPLETED));
                currentDayTime += fiTime;
            }
        }
        // 处理最后一天未满的情况
        if (!buffer.isEmpty()) {
            productSeparates.add(new ProductSeparate(new ArrayList<>(buffer), currentDayTime));
        }
        return new Separate(productSeparates);
    }


    /**
     * 生成初始解的函数
     * 类比方法1，按照订单的同时，考虑后面的订单的产品向前插入（部分）
     *
     * @param lastCompletionTime    最晚完成时间
     * @param orderProductFiTimeMap 每个产品i单独仿真的完成时间fi
     * @param input                 仿真算法输入
     * @return 生成的初始解
     */
    public static Separate createSeparate_2(double lastCompletionTime, Map<String, Double> orderProductFiTimeMap, SimulateInput input) {

        return null;
    }

    /**
     * 生成初始解的函数
     * 将相邻截止时间（或者按照优先级排序后的相邻）的订单，按照同材同安排的策略进行安排。
     *
     * @param lastCompletionTime    最晚完成时间
     * @param orderProductFiTimeMap 每个产品i单独仿真的完成时间fi
     * @param input                 仿真算法输入
     * @return 生成的初始解
     */
    public static Separate createSeparate_3(double lastCompletionTime, Map<String, Double> orderProductFiTimeMap, SimulateInput input) {

        return null;
    }

    /**
     * 生成初始解的函数
     * 4.在同材的基础上，将优先级高的订单的产品的材质放在前面（优先级通过订单完成时间决定）。
     *
     * @param lastCompletionTime    最晚完成时间
     * @param orderProductFiTimeMap 每个产品i单独仿真的完成时间fi
     * @param input                 仿真算法输入
     * @return 生成的初始解
     */
    public static Separate createSeparate_4(double lastCompletionTime, Map<String, Double> orderProductFiTimeMap, SimulateInput input) {

        return null;
    }
}
