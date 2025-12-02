//package cn.hex.ddp.manufacture.application.order.service;
//
//import cn.hex.ddp.manufacture.infrastructure.algorithm.Interactive.OrderInput;
//import cn.hex.ddp.manufacture.infrastructure.common.converter.AlgorithmDataConverter;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Arrays;
//import java.util.List;
//
///**
// * @author 冯泽邦
// * @date 2025/7/24
// */
//@SpringBootTest
//public class OrderServiceTest {
//    @Autowired
//    private OrderService orderService;
//
//    List<Long> orderIdList = Arrays.asList(
//            1948033765144051714L,
//            1948033587141984257L,
//            1943509402957967362L,
//            1943509087462420482L,
//            1943508483134517249L,
//            1943508306311049217L,
//            1943508080007376897L
//    );
//
//    @Test
//    public void test1(){
//
//        for(Long orderId : orderIdList){
//            OrderInput orderInput = AlgorithmDataConverter.orderDTOToOrderInput(
//                    orderService.getOrderDetailById(orderId)
//            );
//            System.out.println("OrderId = " + orderId +orderInput);
//        }
//    }
//}
