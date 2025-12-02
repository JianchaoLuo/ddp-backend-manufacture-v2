//package cn.hex.ddp.manufacture.infrastructure.simulator;
//
//import cn.hex.ddp.manufacture.domain.simulator.model.PositionSimulatorData;
//import cn.hex.ddp.manufacture.domain.simulator.model.PositionVaryData;
//import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
//import cn.hex.ddp.manufacture.infrastructure.simulator.persistence.managerimpl.PositonSimulatorManagerImpl;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
///**
// * 点位模拟数据测试
// * @Author: FengZebang
// * @Date: 2025/3/12
// */
//@SpringBootTest
//public class PositionSimulatorDataTest {
//    @Autowired
//    private PositonSimulatorManagerImpl positonSimulatorManager;
//
//    private static final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Test
//    void savePositionSimulatorData(){
//        //PositionVaryData数据构造
//        List<PositionVaryData> positionVaryDataList = new ArrayList<>();
//        for (int i = 0; i < 10; i++){
//            PositionVaryData positionVaryData = new PositionVaryData();
//            positionVaryData.setId(Long.valueOf(i));
//            positionVaryData.setCurrentCoordinate(new Coordinate(1, 1));
//            positionVaryData.setCurrentAction("action");
//            positionVaryDataList.add(positionVaryData);
//        }
//
//        int threadNum=20;
//        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
//        for(int i=1; i<=threadNum ; i++){
//            int fianlI = i;
//            executorService.execute(() -> {
//                try {
//                    long startAll = System.currentTimeMillis();
//                    for (int j = 1; j <= 100; j++) {
//                        PositionSimulatorData positionSimulatorData = new PositionSimulatorData();
//                        positionSimulatorData.setMission(Long.valueOf(fianlI));
//                        positionSimulatorData.setTick(Long.valueOf(j));
//                        positionSimulatorData.setData(objectMapper.writeValueAsString(positionVaryDataList));
//
//                        long start = System.currentTimeMillis();
//                        positonSimulatorManager.savePositionRealTime(positionSimulatorData);
//                        long end = System.currentTimeMillis();
//                        System.out.println("线程" + fianlI + " - 执行时间：" + (end - start) + "ms");
//
//                        TimeUnit.MILLISECONDS.sleep(500);
//                    }
//                    long endAll = System.currentTimeMillis();
//                    System.out.println("[存储数据]线程" + fianlI + " - 总执行时间：" + (endAll - startAll) + "ms");
//                        System.out.println("第" + fianlI + "次插入100条数据耗时：" + (endAll - startAll) + "ms");
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            });
//        }
//        executorService.shutdown();
//        while (!executorService.isTerminated()){
//
//        }
//    }
//
//    @Test
//    public void getPositionRealTimeTest(){
//        int n=20;
//        ExecutorService executorService = Executors.newFixedThreadPool(n);
//
//        for(int i=1; i<=n ; i++){
//            int fianlI = i;
//            executorService.execute(() -> {
//                try {
//                    long startAll = System.currentTimeMillis();
//                    long TickStart = 1;
//                    long TickTotal = 100;
//                    long TickBatch = 10;
//
//                    while (TickStart <= TickTotal){
//                        long start = System.currentTimeMillis();
//                        List<PositionSimulatorData> positionSimulatorDataList = positonSimulatorManager.getPositionRealTimes(Long.valueOf(fianlI), TickStart, TickStart + TickBatch - 1);
//                        long end=System.currentTimeMillis();
//                        System.out.println("线程" + fianlI + " - 执行时间：" + (end - start) + "ms");
//                        TickStart += TickBatch;
//                        TimeUnit.MILLISECONDS.sleep(500);
//                    }
//                    long endAll = System.currentTimeMillis();
//                    System.out.println("[获取数据]线程" + fianlI + " - 总执行时间：" + (endAll - startAll) + "ms");
//                }catch (Exception e){
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
