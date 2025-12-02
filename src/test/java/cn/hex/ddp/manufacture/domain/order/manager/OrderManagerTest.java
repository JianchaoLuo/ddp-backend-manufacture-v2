//package cn.hex.ddp.manufacture.domain.order.manager;
//
//import cn.hex.ddp.manufacture.infrastructure.algorithm.scheduling.output.SchedulingOutPut;
//import cn.hex.ddp.manufacture.infrastructure.order.managerimpl.OrderManagerImpl;
//import cn.hex.ddp.manufacture.infrastructure.order.persistence.po.ScheduledOrderPO;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author:bobo
// * @description: TODO
// * @date:2025/6/5 12:05
// */
//@SpringBootTest
//public class OrderManagerTest {
//
//    @Autowired
//    OrderManagerImpl orderManagerImpl;
//
//    // 获得当天的时间
//    LocalDateTime today = LocalDateTime.now();
//
//    List<ScheduledOrderPO> scheduledOrderPOList;
//    {
//        ScheduledOrderPO order1 = new ScheduledOrderPO();
//        order1.setId(1L);
//        order1.setOrderId(1L);
//        order1.setOrderNo("1");
//        order1.setDeliveryTime(today.plusDays(10));
//        order1.setProductionProgress("0%");
//        ScheduledOrderPO order2 = new ScheduledOrderPO();
//        order2.setId(2L);
//        order2.setOrderId(2L);
//        order2.setOrderNo("2");
//        order2.setDeliveryTime(today.plusDays(10));
//        order2.setProductionProgress("0%");
//        scheduledOrderPOList = List.of(order1, order2);
//    }
//
//    SchedulingOutPut schedulingOutPut = new SchedulingOutPut();
//    {
//        Map<String, LocalDateTime> orderFinishTime = new HashMap<>();
//        orderFinishTime.put("1", today.plusDays(1));
//        orderFinishTime.put("2", today.plusDays(3));
//        schedulingOutPut.setOrderFinishTime(orderFinishTime);
//    }
//
//    @Test
//    void completeScheduledOrderByAlgorithmTest(){
//        orderManagerImpl.completeScheduledOrderByAlgorithm(scheduledOrderPOList,schedulingOutPut);
//        System.out.println(scheduledOrderPOList);
//    }
//}
