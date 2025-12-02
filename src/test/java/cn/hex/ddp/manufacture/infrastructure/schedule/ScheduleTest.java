package cn.hex.ddp.manufacture.infrastructure.schedule;

import cn.hex.ddp.manufacture.application.task.converter.TaskAppConverter;
import cn.hex.ddp.manufacture.application.task.service.TaskService;
import cn.hex.ddp.manufacture.domain.order.enums.OrderStatusEnum;
import cn.hex.ddp.manufacture.domain.order.manager.OrderManager;
import cn.hex.ddp.manufacture.domain.product.manager.ProductManager;
import cn.hex.ddp.manufacture.infrastructure.task.managerimpl.converter.TaskInfraConverter;
import cn.hex.ddp.manufacture.infrastructure.task.persistence.postgresql.repository.TaskPORepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: FengZebang
 * @Date: 2025/6/11
 */
@SpringBootTest
public class ScheduleTest {

    @Autowired
    private ProductManager productManager;

    @Autowired
    private TaskPORepository taskPORepository;

    @Autowired
    private TaskInfraConverter taskInfraConverter;

    @Autowired
    private TaskAppConverter taskAppConverter;
    @Autowired
    private OrderManager orderManager;

    @Autowired
    private TaskService taskService;


    List<Long> orderIdList = Arrays.asList(
            1993969021911379970L,
            1993968929733160962L,
            1993969120775319553L,
            1995413472634548226L
    );

    @Test
    public void orderstatus(){
        orderManager.updateOrderStatusBatch(orderIdList, OrderStatusEnum.PENDING_PRODUCTION);

    }


}


