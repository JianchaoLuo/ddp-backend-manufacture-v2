package cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling;

import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.AllocationInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.OrderInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.OrderProduct;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule.ScheduleOrderInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule.ScheduleOrderItem;
import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.schedule.ScheduleProductInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.AnalogInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateInput;
import cn.hex.ddp.manufacture.infrastructure.algorithm.data.SimulateResult;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.output.SchedulingOutPut;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.output.SchedulingProduct;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.output.SchedulingResult;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.tools.CreateSeparate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.tools.VerifyAndRepair;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.util.ProductForSeparate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.util.ProductSeparate;
import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.util.ScheduleDateConverter;
import cn.hex.ddp.manufacture.infrastructure.algorithm.simulation.simulation_v2.WholeSimulation_3;
import cn.hex.ddp.manufacture.infrastructure.common.util.Slf4j;
import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassDescription:
 * @Author: KangHong
 * @Created: 2025/5/28 下午1:06
 */
@lombok.extern.slf4j.Slf4j
@Slf4j
public class ProductionScheduling {
    /**
     * 排产算法入口函数
     *
     * @param input              输入仿真需要的数据，包括订单和地图资源
     * @param scheduleOrderInput 排产订单输入
     * @return 算法排产出的结果
     */
    // 仿真时间缩放因子：>1 表示缩短工时，<1 表示拉长工时
    private static final double SIMULATION_TIME_SCALE = 1.0 / 5.0;

    public static SchedulingResult preDoScheduling(SimulateInput input, ScheduleOrderInput scheduleOrderInput) {
        // 一、首先考虑当天状态多久能清空
        // 1. 通过PLC获取当天状态，利用仿真模拟算法计算，当前未和PLC联动，当前算法也需要修改部分以满足能以任意时刻状态进行仿真模拟
        double currentTime = calculateCurrentState(input);
        //2.算出每个产品i单独仿真的完成时间fi
        Map<Long, Double> orderProductFiTimeMap = calculateProductTime(input, scheduleOrderInput);
        //3.计算最晚完成时间，包括当天状态清零，不同的订单剩余的小数相加
        double lastCompletionTime = getLatestCompletionTime(orderProductFiTimeMap, currentTime, input);
        // 二、其次随机生成初始解
        // 根据方法1生成初始解
        Separate initialSeparate = CreateSeparate.createSeparate_1(lastCompletionTime, orderProductFiTimeMap, scheduleOrderInput);
        // 判断生成的解是否可行
        VerifyAndRepair.VerificationResult verificationResult = VerifyAndRepair.verifySeparate(initialSeparate, scheduleOrderInput);
        if (!verificationResult.isValid()) {
            // 解不可行，返回失败结果，并附上所有导致失败的订单ID
            List<SchedulingResult.FailedOrderInfo> failedOrders = new ArrayList<>();
            for (Long failedOrderId : verificationResult.getFailedOrderIds()) {
                String reason = "订单 (ID: " + failedOrderId + ") 无法在交期内完成";
                failedOrders.add(new SchedulingResult.FailedOrderInfo(failedOrderId, reason));
            }
            return SchedulingResult.failure(failedOrders);
        }

        // 若生成的解可行
        // 进行变领域优化
//        OptimizeSeparate.changeSeparate(initialSeparate, scheduleOrderInput);

        // 返回排产算法结果
        return transformSuccessResult(initialSeparate, scheduleOrderInput);
    }

    /**
     * 计算当天状态多久能清空
     *
     * @param input 仿真算法输入
     * @return 计算后的时间
     */
    public static double calculateCurrentState(SimulateInput input) {
        /*
        //利用仿真算法模拟
        WholeSimulation_3 wholeSimulation_3 = new WholeSimulation_3();
        //仿真模拟
        SimulateResult result = wholeSimulation_3.simulationRun(input);
        //计算剩余时间，可以先根据订单id查询仿真完成的时间
        double time = result.getOrderTimes().get(id);
        */

        return 0;
    }

    /**
     * 计算每个类型产品的仿真的完成时间fi
     *
     * @param input              仿真算法输入
     * @param scheduleOrderInput 仿真算法输入
     * @return 计算后的时间
     */
    public static Map<Long, Double> calculateProductTime(SimulateInput input, ScheduleOrderInput scheduleOrderInput) {
        // 创建一个map作为返回参数
        Map<Long, Double> productTimeMap = new HashMap<Long, Double>();
        // 拆分input中的每个订单
        // 组合成一个新的input，这个input中只一个订单，订单中只有一种类型的产品，要生成的数量设置为1
        Map<Long, SimulateInput> productInputMap = new HashMap<>();
        splitOrderInput(input, scheduleOrderInput, productInputMap);

        // 将新的input作为参数输入到算法模块中得到仿真结果

        // 循环执行订单中所有类型产品的单个数量的仿真，从而获得每个类型产品的生产仿真模拟时间
        productInputMap.forEach((productId, simulateInput) -> {
            WholeSimulation_3 wholeSimulation_3 = new WholeSimulation_3();
            SimulateResult result = wholeSimulation_3.simulationRun(simulateInput);
            List<Double> valueList = new ArrayList<>(result.getOrderTimes().values());
            if (!valueList.isEmpty()) {
                Double rawTime = valueList.getFirst();
                // 在这里统一缩短仿真时间
                double scaledTime = rawTime * SIMULATION_TIME_SCALE;
                productTimeMap.put(productId, scaledTime);
            } else {
                log.warn("产品ID {} 的仿真模拟未能返回有效的生产时间。", productId);
            }
        });

        System.out.println("===00=====" + productTimeMap);
        // 返回对应仿真时间
        return productTimeMap;
    }

    /**
     * 测试每个类型产品的仿真的完成时间fi
     *
     * @param input              仿真算法输入
     * @param scheduleOrderInput 排产订单输入
     * @return 计算后的时间
     */
    public static Map<Long, Double> calculateProductTime1(SimulateInput input, ScheduleOrderInput scheduleOrderInput) {
        Map<Long, Double> productTimeMap = new HashMap<>();
        for (ScheduleOrderItem orderItem : scheduleOrderInput.getItems()) {
            for (ScheduleProductInput productInput : orderItem.getSchesuleProductList()) {
                productTimeMap.put(productInput.getProductId(), 100.0);
            }
        }
        return productTimeMap;
    }

    /**
     * 拆分仿真输入的订单函数
     *
     * @param input              仿真算法输入
     * @param scheduleOrderInput 排产订单输入
     * @param productInputMap    存储拆分后的订单输入列表
     */
    private static void splitOrderInput(SimulateInput input, ScheduleOrderInput scheduleOrderInput, Map<Long, SimulateInput> productInputMap) {
        // 获取仿真的地图信息（此信息唯一，不用修改）
        List<AllocationInput> allocationInputs = input.getAllocationInputs();
        // 获取仿真的moke地图信息（此信息唯一，不用修改）
        AnalogInput analogInput = input.getAnalogInput();

        // 遍历排产订单输入中的每个订单项
        for (ScheduleOrderItem orderItem : scheduleOrderInput.getItems()) {
            for (ScheduleProductInput productInput : orderItem.getSchesuleProductList()) {
                // 检查productInputMap中是否已经包含该产品的输入
                if (!productInputMap.containsKey(productInput.getProductId())) {
                    // 创建一个新的SimulateInput对象, 用于产品记录参数信息
                    OrderProduct orderProduct_simulation = null;
                    // 遍历原始输入中的订单，找到对应的订单产品
                    for (OrderInput orderInput : input.getOrderInputs()) {
                        for (OrderProduct product : orderInput.getOrderProducts()) {
                            if (product.getProductId().equals(productInput.getProductId())) {
                                // 找到对应的订单产品
                                orderProduct_simulation = product;
                                break;
                            }
                        }
                    }

                    // 将订单拆分存入map中
                    List<OrderProduct> orderProducts = new ArrayList<>();
                    orderProducts.add(new OrderProduct(
                            orderProduct_simulation.getProducts(),
                            1,
                            orderProduct_simulation.getMaterialType(),
                            orderProduct_simulation.getProductType(),
                            orderProduct_simulation.getUsage(),
                            orderProduct_simulation.getMaterialTime(),
                            0,
                            0,
                            new ArrayList<>(),
                            orderProduct_simulation.getProductId()
                    ));

                    List<OrderInput> orderInputs = new ArrayList<>();
                    orderInputs.add(new OrderInput(
                            orderItem.getId(),
                            orderProducts,
                            orderItem.getDeliveryTime(),
                            1
                    ));

//                    productInputMap.put(productInput.getProductId(), new SimulateInput(
//                            allocationInputs,
//                            orderInputs,
//                            analogInput
//                    ));
                    productInputMap.put(productInput.getProductId(), new SimulateInput(
                            (List<AllocationInput>) SerializationUtils.clone((Serializable) allocationInputs),
                            (List<OrderInput>) SerializationUtils.clone((Serializable) orderInputs),
                            SerializationUtils.clone(analogInput)
                    ));
                }
            }
        }

    }

    /**
     * 计算最晚完成时间，包括当天状态清零，不同的订单剩余的小数相加
     *
     * @param orderProductFiTimeMap 每个产品i单独仿真的完成时间fi
     * @param currentTime           当天的剩余时间
     * @param input                 仿真算法输入
     * @return 所有订单的最晚完成时间
     */
    public static double getLatestCompletionTime(Map<Long, Double> orderProductFiTimeMap, double currentTime, SimulateInput input) {
        // 统计每种产品型号的总数量
        Map<Long, Integer> productIdToCount = new HashMap<>();
        for (OrderInput orderInput : input.getOrderInputs()) {
            for (OrderProduct orderProduct : orderInput.getOrderProducts()) {
                Long productId = orderProduct.getProductId();
                int number = orderProduct.getNumber() != null ? orderProduct.getNumber() : 0;
                productIdToCount.put(productId, productIdToCount.getOrDefault(productId, 0) + number);
            }
        }
        // 计算所有产品的总时间
        double totalTime = 0;
        for (Map.Entry<Long, Integer> entry : productIdToCount.entrySet()) {
            Long productId = entry.getKey();
            int count = entry.getValue();
            Double fiTime = orderProductFiTimeMap.get(productId);
            if (fiTime != null) {
                totalTime += fiTime * count;
            }
        }

        return totalTime;
    }

    /**
     * 解转化为结果的函数
     *
     * @param separate 排产问题的解
     * @return 返回给后端的数据结构
     */
    public static SchedulingOutPut transformResult(Separate separate, ScheduleOrderInput scheduleOrderInput) {
        // 构造的解对象
        SchedulingOutPut schedulingOutPut = new SchedulingOutPut();
        Map<LocalDateTime, List<SchedulingProduct>> productionPlan = new HashMap<>();
        Map<Long, LocalDateTime> orderFinishTime = new HashMap<>();

        // 拿到解中的排产计划列表
        List<ProductSeparate> productSeparates = separate.getProductSeparates();
        // 遍历解中的计划
        for (int i = 0; i < productSeparates.size(); i++) {
            // 拿到当天的解
            ProductSeparate productSeparate = productSeparates.get(i);
            // 拿到当天的时间
            LocalDateTime date = ScheduleDateConverter.days2LocalDateTime(i);
            // 拿到当天的产品列表
            List<ProductForSeparate> currentSeparate = productSeparate.getCurrentSeparate();
            // 构造当天的输出
            List<SchedulingProduct> schedulingProductList = new ArrayList<>();
            // 遍历当天的产品列表
            for (ProductForSeparate productForSeparate : currentSeparate) {
                // 拿到产品的订单id
                Long orderId = productForSeparate.getOrderId();
                // 检查该订单的完成时间是否已经拿到,没拿到的话拿一下
                if (!orderFinishTime.containsKey(orderId)) {
                    LocalDateTime finishTime = getFinishTime(orderId, productSeparates);
                    orderFinishTime.put(orderId, finishTime);
                }
                // 构造输出对象
                SchedulingProduct schedulingProduct = new SchedulingProduct();
                schedulingProduct.setId(productForSeparate.getId());
                schedulingProduct.setProductId(productForSeparate.getProductId());
                schedulingProduct.setOrderId(orderId);
                schedulingProduct.setProductionProgress(productForSeparate.getProductionProgress());
                // 将对象加入list
                schedulingProductList.add(schedulingProduct);
            }
            // 将当日计划加入map
            productionPlan.put(date, schedulingProductList);
        }
        schedulingOutPut.setProductionPlan(productionPlan);
        schedulingOutPut.setOrderFinishTime(orderFinishTime);
        return schedulingOutPut;
    }

    /**
     * 将成功的解转化为SchedulingResult结果对象
     *
     * @param separate 排产问题的解
     * @return 返回给后端的数据结构
     */
    public static SchedulingResult transformSuccessResult(Separate separate, ScheduleOrderInput scheduleOrderInput) {
        // 构造的解对象
        SchedulingOutPut schedulingOutPut = new SchedulingOutPut();
        Map<LocalDateTime, List<SchedulingProduct>> productionPlan = new HashMap<>();
        Map<Long, LocalDateTime> orderFinishTime = new HashMap<>();

        // 拿到解中的排产计划列表
        List<ProductSeparate> productSeparates = separate.getProductSeparates();
        // 遍历解中的计划
        for (int i = 0; i < productSeparates.size(); i++) {
            // 拿到当天的解
            ProductSeparate productSeparate = productSeparates.get(i);
            // 拿到当天的时间
            LocalDateTime date = ScheduleDateConverter.days2LocalDateTime(i);
            // 拿到当天的产品列表
            List<ProductForSeparate> currentSeparate = productSeparate.getCurrentSeparate();
            // 构造当天的输出
            List<SchedulingProduct> schedulingProductList = new ArrayList<>();
            // 遍历当天的产品列表
            for (ProductForSeparate productForSeparate : currentSeparate) {
                // 拿到产品的订单id
                Long orderId = productForSeparate.getOrderId();
                // 检查该订单的完成时间是否已经拿到,没拿到的话拿一下
                if (!orderFinishTime.containsKey(orderId)) {
                    LocalDateTime finishTime = getFinishTime(orderId, productSeparates);
                    orderFinishTime.put(orderId, finishTime);
                }
                // 构造输出对象
                SchedulingProduct schedulingProduct = new SchedulingProduct();
                schedulingProduct.setId(productForSeparate.getId());
                schedulingProduct.setProductId(productForSeparate.getProductId());
                schedulingProduct.setOrderId(orderId);
                schedulingProduct.setProductionProgress(productForSeparate.getProductionProgress());
                // 将对象加入list
                schedulingProductList.add(schedulingProduct);
            }
            // 将当日计划加入map
            productionPlan.put(date, schedulingProductList);
        }
        schedulingOutPut.setProductionPlan(productionPlan);
        schedulingOutPut.setOrderFinishTime(orderFinishTime);

        // 使用静态工厂方法创建并返回一个表示成功的SchedulingResult对象
        return SchedulingResult.success(schedulingOutPut);
    }

    /**
     * 从算法的解中拿到订单的完成时间
     *
     * @param orderId          需要查找完成时间的订单id
     * @param productSeparates 排产计划表
     * @return 时间对象
     */
    public static LocalDateTime getFinishTime(Long orderId, List<ProductSeparate> productSeparates) {
        int size = productSeparates.size();
        LocalDateTime finishTime = null;
        // 逆序查找订单的完成时间
        for (int i = size - 1; i >= 0; i--) {
            ProductSeparate productSeparate = productSeparates.get(i);
            List<ProductForSeparate> currentSeparate = productSeparate.getCurrentSeparate();
            for (ProductForSeparate productForSeparate : currentSeparate) {
                Long productOrderId = productForSeparate.getOrderId();
                // 如果当日产品表中包括该订单的产品
                if (orderId.equals(productOrderId)) {
                    finishTime = ScheduleDateConverter.days2LocalDateTime(i);
                    return finishTime;
                }
            }
        }
        log.error("从算法的解中提取订单完成时间失败！");
        return finishTime;
    }


}
