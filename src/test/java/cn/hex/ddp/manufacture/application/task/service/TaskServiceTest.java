//package cn.hex.ddp.manufacture.application.task.service;
//
//import cn.hex.ddp.manufacture.api.task.rest.req.CreateTaskByOrdersReq;
//import cn.hex.ddp.manufacture.application.task.converter.TaskAppConverter;
//import cn.hex.ddp.manufacture.domain.order.manager.OrderManager;
//import cn.hex.ddp.manufacture.domain.task.manager.TaskManager;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * @author 冯泽邦
// * @date 2025/7/24
// */
//@SpringBootTest
//public class TaskServiceTest {
//    @Autowired
//    private TaskService taskService;
//
//    @Autowired
//    private TaskAppConverter taskAppConverter;
//
//    @Autowired
//    private OrderManager orderManager;
//
//    @Autowired
//    private TaskManager taskManager;
//
//    @Test
//    public void testCreateTaskByOrders() {
//        List<Long> orderIdList = Arrays.asList(
//                1948033765144051714L,
//                1943509402957967362L,
//                1943509087462420482L,
//                1943508483134517249L,
//                1943508306311049217L,
//                1943508080007376897L
//        );
//        // 创建仿真任务
//         CreateTaskByOrdersReq req = new CreateTaskByOrdersReq();
//         req.setName("仿真测试接口测试任务");
//         req.setPlannedProductionDate(LocalDateTime.now().plusDays(1));
//         req.setExpectedStartTime(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0));
//         req.setExpectedEndTime(LocalDateTime.now().plusDays(1).withHour(17).withMinute(0));
//         req.setExpectedProductionTime(LocalTime.of(8, 0));
//         req.setOrderIds(orderIdList);
//
//         taskService.createTaskByOrders(req);
//    }
//}
