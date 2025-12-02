//package cn.hex.ddp.manufacture.infrastructure.simulator;
//
//import cn.hex.ddp.manufacture.domain.car.enums.CarControlStatusEnum;
//import cn.hex.ddp.manufacture.domain.car.enums.CarOperationStatusEnum;
//import cn.hex.ddp.manufacture.domain.simulator.manager.CarSimulatorManager;
//import cn.hex.ddp.manufacture.domain.simulator.model.CarSimulatorData;
//import cn.hex.ddp.manufacture.domain.simulator.model.CarVaryData;
//import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
///**
// * 车辆仿真数据测试类
// *
// * @author Huhaisen
// * @date 2024/12/26
// */
//@SpringBootTest
//public class CarSimulatorDataTest {
//
//    @Autowired
//    private CarSimulatorManager carSimulatorManager;
//
//    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
//
//    @Test
//    public void saveCarRealTimeTest() throws JsonProcessingException {
//        // 构造CarVaryData数据
//        ArrayList<CarVaryData> carVaryDatas = new ArrayList<>();
//        for (int i = 1; i < 5; i++) {
//            CarVaryData carVaryData = new CarVaryData();
//            carVaryData.setId((long) i);
//            carVaryData.setOperationStatus(CarOperationStatusEnum.STANDBY);
////            carVaryData.setControlStatus(CarControlStatusEnum.LOCAL_CONTROL);
////            carVaryData.setCurrentCoordinate(new Coordinate(1111.22, 2222.33));
//            carVaryData.setCurrentAction("当前操作");
//            carVaryDatas.add(carVaryData);
//        }
//
//        int n = 50; // 定义线程数量
//        ExecutorService executorService = Executors.newFixedThreadPool(n);
//        for (int threadId = 1; threadId <= n; threadId++) {
//            int finalThreadId = threadId;
//            executorService.submit(() -> {
//                try {
//                    long startAll = System.currentTimeMillis();
//                    for (int i = 1; i <= 300; i++) {
//                        CarSimulatoArData carSimulatorData = new CarSimulatorData();
//                        carSimulatorData.setMission((long) finalThreadId);
//                        carSimulatorData.setTick((long) i);
//                        carSimulatorData.setData(OBJECT_MAPPER.writeValueAsString(carVaryDatas));
//
//                        long start = System.currentTimeMillis();
//                        carSimulatorManager.saveCarRealTime(carSimulatorData);
//                        long end = System.currentTimeMillis();
//                        System.out.println("线程" + finalThreadId + " - 执行时间：" + (end - start) + "ms");
//
//                        Thread.sleep(500); // 每隔0.5秒执行一次
//                    }
//                    long endAll = System.currentTimeMillis();
//                    System.out.println("[存储数据]线程" + finalThreadId + " - 总执行时间：" + (endAll - startAll) + "ms");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//
//        executorService.shutdown();
//        while (!executorService.isTerminated()) {
//            // 等待所有线程执行完成
//        }
//    }
//
//    @Test
//    public void getCarRealTimeTest() {
//        int n = 50; // 定义线程数量
//        ExecutorService executorService = Executors.newFixedThreadPool(n);
//
//        for (int threadId = 1; threadId <= n; threadId++) {
//            int finalThreadId = threadId;
//            executorService.submit(() -> {
//                try {
//                    long startAll = System.currentTimeMillis();
//                    long tickStart = 1;
//                    long tickTotal = 200;
//                    long tickBatch = 50;
//
//                    while (tickStart <= tickTotal ) {
//                        long start = System.currentTimeMillis();
//                        List<CarSimulatorData> carSimulatorDatas = carSimulatorManager.getCarRealTimes(
//                                (long) finalThreadId, tickStart, tickStart + tickBatch);
//                        long end = System.currentTimeMillis();
//
//                        System.out.println("线程" + finalThreadId + " - 执行时间：" + (end - start) + "ms");
///*                        for (CarSimulatorData carSimulatorData : carSimulatorDatas) {
//                            System.out.println(carSimulatorData);
//                        }*/
//
//                        tickStart += tickBatch;
//
//                        Thread.sleep(500); // 每隔0.5秒执行一次
//                    }
//                    long endAll = System.currentTimeMillis();
//                    System.out.println("线程" + finalThreadId + " - 总执行时间：" + (endAll - startAll) + "ms");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//
//        executorService.shutdown();
//        while (!executorService.isTerminated()) {
//            // 等待所有线程执行完成
//        }
//    }
//}
