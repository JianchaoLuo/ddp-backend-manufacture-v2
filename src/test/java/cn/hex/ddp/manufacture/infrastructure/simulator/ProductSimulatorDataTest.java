//package cn.hex.ddp.manufacture.infrastructure.simulator;
//
//import cn.hex.ddp.manufacture.domain.simulator.manager.ProductSimulatorManager;
//import cn.hex.ddp.manufacture.domain.simulator.model.ProductSimulatorData;
//import cn.hex.ddp.manufacture.domain.simulator.model.ProductVaryData;
//import cn.hex.ddp.manufacture.infrastructure.algorithm.enums.ProductTypeEnum;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
///**
// * 产品模拟数据测试类
// * @Author: FengZebang
// * @Date: 2025/3/13
// */
//@SpringBootTest
//public class ProductSimulatorDataTest {
//    @Autowired
//    private ProductSimulatorManager productSimulatorManager;
//
//    private static final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Test
//    void saveProductSimulatorData() throws Exception {
//        List<ProductVaryData> productVaryDatas = new ArrayList<>();
//        for(int i = 0; i < 10; i++){
//            ProductVaryData productVaryData = new ProductVaryData();
//            productVaryData.setId(Long.valueOf(i));
//            productVaryData.setName("产品名称" + i);
//            productVaryData.setProductType(ProductTypeEnum.MIDDLE_BOX);
//            productVaryDatas.add(productVaryData);
//        }
//
//        int threadNum = 20;
//        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
//        for(int i = 1; i <= threadNum; i++){
//            int finalI = i;
//            executorService.execute(() -> {
//                try{
//                    long startTime = System.currentTimeMillis();
//                    for(int j = 0; j < 100; j++) {
//                            ProductSimulatorData productSimulatorData = new ProductSimulatorData();
//                            productSimulatorData.setMission(Long.valueOf(finalI));
//                            productSimulatorData.setTick(Long.valueOf(j));
//                            productSimulatorData.setSimulationTime(LocalDateTime.now());
//                            productSimulatorData.setData(objectMapper.writeValueAsString(productVaryDatas));
//
//                            long s = System.currentTimeMillis();
//                            productSimulatorManager.saveProductSimulatorData(productSimulatorData);
//                            long e = System.currentTimeMillis();
//                            System.out.println("线程" + finalI + "第" + j + "次插入数据耗时：" + (e - s) + "ms");
//                            TimeUnit.MILLISECONDS.sleep(500);
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//           });
//        }
//        executorService.shutdown();
//        while (!executorService.isTerminated()){
//
//        }
//    }
//
//    @Test
//    public void getProductRealTimeTest(){
//        int threadNum = 20;
//        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
//
//        for(int i = 1; i <= threadNum; i++){
//            int finalI = i;
//            executorService.execute(() -> {
//                try{
//                    long startTime = System.currentTimeMillis();
//                    long tickStart = 1;
//                    long tickTotal = 50;
//                    long tickBatch = 10;
//                    while (tickStart <= tickTotal){
//                        long s = System.currentTimeMillis();
//                        List<ProductSimulatorData> productSimulatorDataList = productSimulatorManager.getProductSimulatorData(Long.valueOf(finalI), tickStart, tickStart + tickBatch - 1);
//                        long e = System.currentTimeMillis();
//                        System.out.println("线程" + finalI + "查询数据耗时：" + (e - s) + "ms");
//                        tickStart += tickBatch;
//                        TimeUnit.MILLISECONDS.sleep(500);
//                    }
//                    long endTime = System.currentTimeMillis();
//                    System.out.println("线程" + finalI + "查询总耗时：" + (endTime - startTime) + "ms");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//        executorService.shutdown();
//        while (!executorService.isTerminated()){
//
//        }
//    }
//}
