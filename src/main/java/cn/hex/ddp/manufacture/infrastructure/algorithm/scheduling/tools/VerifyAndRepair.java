package cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.tools;

import cn.hex.ddp.manufacture.domain.product.enums.ProductionProgressStatusEnum;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule.ScheduleOrderInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule.ScheduleOrderItem;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule.ScheduleProductInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.Separate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.util.ProductForSeparate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.util.ProductSeparate;
import lombok.Getter;

import java.util.*;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2025/5/28 下午4:28
 */
public class VerifyAndRepair {

    // 和 createSeparate_1 中保持一致
    private static final int DAY_MINUTES = 24 * 60;

    /**
     * 用于封装校验结果的内部类。
     */
    @Getter
    public static class VerificationResult {
        private final boolean valid;
        private final List<Long> failedOrderIds;

        private VerificationResult(boolean valid, List<Long> failedOrderIds) {
            this.valid = valid;
            this.failedOrderIds = failedOrderIds;
        }

        public static VerificationResult success() {
            return new VerificationResult(true, Collections.emptyList());
        }

        public static VerificationResult failure(List<Long> failedOrderIds) {
            return new VerificationResult(false, failedOrderIds);
        }
    }


    /**
     * 校验解的可行性的函数。
     * 现在会找出所有不满足约束的订单。
     *
     * @param separate           排产问题的解
     * @param scheduleOrderInput 算法的排产订单输入
     * @return 包含校验结果（是否成功及所有失败订单的ID列表）的对象
     */
    public static VerificationResult verifySeparate(Separate separate, ScheduleOrderInput scheduleOrderInput) {
        // 如果解为空，所有订单都无法排产，全部标记为失败
        if (separate == null || separate.getProductSeparates().isEmpty()) {
            List<Long> allOrderIds = new ArrayList<>();
            for (ScheduleOrderItem item : scheduleOrderInput.getItems()) {
                allOrderIds.add(item.getId());
            }
            return VerificationResult.failure(allOrderIds);
        }

        List<ScheduleOrderItem> items = scheduleOrderInput.getItems();
        // 按照交付时间从小到大排序
        items.sort(Comparator.comparingDouble(ScheduleOrderItem::getDeliveryTime));

        List<Long> failedOrderIds = new ArrayList<>();
        Map<Long, Integer> productNumMap = new HashMap<>();
        List<ProductSeparate> productSeparates = separate.getProductSeparates();
        int maxScheduleDay = productSeparates.size();

        // 按时间顺序遍历所有订单，找出所有失败的订单
        for (ScheduleOrderItem orderItem : items) {
            // 标志位，用于判断订单是否在排产计划内被完成
            boolean orderCompleted = false;

            // 拿到产品列表和数量
            Map<Long, Integer> orderProductNumMap = new HashMap<>();
            addOrderProductToMap(orderItem.getSchesuleProductList(), orderProductNumMap);

            // 推进时间，为当前订单寻找完成点
            int scheduleDay = 0;
            while (scheduleDay < maxScheduleDay) {
                // 累加当天生产完成的产品
                ProductSeparate productSeparate = productSeparates.get(scheduleDay);
                for (ProductForSeparate productForSeparate : productSeparate.getCurrentSeparate()) {
                    addProductToMap(productForSeparate, productNumMap);
                }
                scheduleDay++;

                // 检查订单是否完成
                if (isOrderFinished(productNumMap, orderProductNumMap)) {
                    // 标记订单已完成（无论是否超时）
                    orderCompleted = true;
                    // 订单在第 scheduleDay\-1 天结束，该天的结束时间对应的“分钟刻度”
                    double finishTimeMinutes = (scheduleDay) * DAY_MINUTES;
                    double deliveryTimeMinutes = orderItem.getDeliveryTime();

                    // 若完成时刻晚于交期，则视为失败
                    if (finishTimeMinutes > deliveryTimeMinutes) {
                        failedOrderIds.add(orderItem.getId());
                    } else {
                        clearProductFromOrder(productNumMap, orderProductNumMap);
                    }
                    // 不论成功或失败，都跳出内层循环，开始检查下一个订单
                    break;
                }
            }

            // 如果遍历完所有排产天数，订单仍未完成，则标记为失败
            if (!orderCompleted) {
                failedOrderIds.add(orderItem.getId());
            }
        }

        if (failedOrderIds.isEmpty()) {
            return VerificationResult.success();
        } else {
            return VerificationResult.failure(failedOrderIds);
        }
    }

    /**
     * 统计一个订单中每种产品的数量
     *
     * @param orderProducts      订单中的产品表
     * @param orderProductNumMap key为产品类型id，value为数量
     */
    public static void addOrderProductToMap(List<ScheduleProductInput> orderProducts, Map<Long, Integer> orderProductNumMap) {
        // 遍历订单中的每种产品
        for (ScheduleProductInput scheduleProductInput : orderProducts) {
            // 拿到产品类型
            Long productId = scheduleProductInput.getProductId();
            // 如果该类型存在
            if (orderProductNumMap.containsKey(productId)) {
                Integer productNum = orderProductNumMap.get(productId) + 1;
                orderProductNumMap.put(productId, productNum);
            } else {
                orderProductNumMap.put(productId, 1);
            }
        }
    }

    /**
     * 将完成的产品加入productNumMap
     *
     * @param productForSeparate 产品
     * @param productNumMap      产品数量map
     */
    public static void addProductToMap(ProductForSeparate productForSeparate, Map<Long, Integer> productNumMap) {
        // 如果当前产品已完成生产
        if (productForSeparate.getProductionProgress().equals(ProductionProgressStatusEnum.COMPLETED)) {
            Long productId = productForSeparate.getProductId();
            if (productNumMap.containsKey(productId)) {
                int productNum = productNumMap.get(productId) + 1;
                productNumMap.put(productId, productNum);
            } else {
                productNumMap.put(productId, 1);
            }
        }
    }

    /**
     * 判断订单是否完成
     *
     * @param productNumMap      产品数量
     * @param orderProductNumMap 订单产品数量
     * @return true为完成，false为未完成
     */
    public static boolean isOrderFinished(Map<Long, Integer> productNumMap, Map<Long, Integer> orderProductNumMap) {
        // 遍历订单要求的每种产品
        for (Map.Entry<Long, Integer> entry : orderProductNumMap.entrySet()) {
            // 拿到产品的数量
            int productNum = entry.getValue();
            // 拿到产品的类型
            Long productId = entry.getKey();
            // 如果产品不存在，或者产品数量不够
            if (!productNumMap.containsKey(productId) || productNumMap.get(productId) < productNum) {
                return false;
            }
        }
        return true;
    }

    /**
     * 清除productNumMap中属于本订单的产品
     *
     * @param productNumMap      产品数量
     * @param orderProductNumMap 订单产品数量
     */
    public static void clearProductFromOrder(Map<Long, Integer> productNumMap, Map<Long, Integer> orderProductNumMap) {
        for (Map.Entry<Long, Integer> entry : orderProductNumMap.entrySet()) {
            Long productId = entry.getKey();
            int productNum = entry.getValue();
            int remainingNum = productNumMap.get(productId) - productNum;
            productNumMap.put(productId, remainingNum);
        }
    }

    /**
     * 修复生成的初始解
     *
     * @param separate           排产问题的解
     * @param scheduleOrderInput 排产算法的订单输入
     */
    public static void repairSeparate(Separate separate, ScheduleOrderInput scheduleOrderInput) {

    }
}
