//package cn.hex.ddp.manufacture.infrastructure.simulator;
//
//import cn.hex.ddp.manufacture.domain.path.enums.PathStatusEnum;
//import cn.hex.ddp.manufacture.domain.path.enums.RailDirectionEnum;
//import cn.hex.ddp.manufacture.domain.simulator.manager.PathSimulatorManager;
//import cn.hex.ddp.manufacture.domain.simulator.model.PathSimulatorData;
//import cn.hex.ddp.manufacture.domain.simulator.model.PathVaryData;
//import cn.hex.ddp.manufacture.domain.simulator.model.PositionSimulatorData;
//import cn.hex.ddp.manufacture.infrastructure.algorithm.util.Coordinate;
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
// * 路径摸你数据测试
// * @Author: FengZebang
// * @Date: 2025/3/13
// */
//@SpringBootTest
//public class PathSimulatorDataTest {
//    @Autowired
//    private PathSimulatorManager pathSimulatorManager;
//
//    private static final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Test
//    void savePathSimulatorData(){
//        List<PathVaryData> pathVaryDatas = new ArrayList<>();
//        for(int i = 0; i < 10; i++){
//            PathVaryData pathVaryData = new PathVaryData();
//            pathVaryData.setId(Long.valueOf(i));
//            pathVaryData.setName("path" + i);
//            pathVaryData.setLength(Float.valueOf(i));
//            pathVaryData.setStartCoordinate(new Coordinate(i, i));
//            pathVaryData.setEndCoordinate(new Coordinate(i+3, i+3));
//            pathVaryData.setRailDirection(RailDirectionEnum.LEFT_TO_RIGHT);
//            pathVaryData.setStatus(PathStatusEnum.PATH_UNAVAILABLE);
//        }
//
//        int threadNum = 20;
//        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
//        for(int i=1; i<=threadNum; i++){
//            int finalI = i;
//            executorService.execute(() -> {
//                try {
//                    long startTime = System.currentTimeMillis();
//                    for(int j = 1; j <= 50; j++){
//                        PathSimulatorData pathSimulatorData = new PathSimulatorData();
//                        pathSimulatorData.setMission(Long.valueOf(finalI));
//                        pathSimulatorData.setTick(Long.valueOf(j));
//                        pathSimulatorData.setData(objectMapper.writeValueAsString(pathVaryDatas));
//
//                        long s= System.currentTimeMillis();
//                        pathSimulatorManager.savePathRealTime(pathSimulatorData);
//                        long e = System.currentTimeMillis();
//                        System.out.println("线程" + finalI + "执行" + j + "次耗时：" + (e - s));
//
//                        Thread.sleep(500);
//                    }
//                    long endtime = System.currentTimeMillis();
//                    System.out.println("线程" + finalI + "执行完毕，耗时：" + (endtime - startTime));
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
//    public void getPathRealTimeTest(){
//        int threadNum = 20;
//        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
//
//        for(int i=1; i<=threadNum; i++){
//            int finalI = i;
//            executorService.execute(() -> {
//                try {
//                    long startTime = System.currentTimeMillis();
//                    long tickStart = 1;
//                    long tickTotal = 50;
//                    long tickBatch = 10;
//                    while (tickStart <= tickTotal){
//                        long s = System.currentTimeMillis();
//                        List<PathSimulatorData> pathSimulatorDataList = pathSimulatorManager.getPathRealTimes(Long.valueOf(finalI), tickStart, tickStart + tickBatch);
//                        long e = System.currentTimeMillis();
//                        System.out.println("线程" + finalI + "执行" + tickStart + "到" + (tickStart + tickBatch - 1) + "次耗时：" + (e - s));
//                        tickStart += tickBatch;
//                        Thread.sleep(500);
//                    }
//                    long endtime = System.currentTimeMillis();
//                    System.out.println("线程" + finalI + "执行完毕，耗时：" + (endtime - startTime));
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
