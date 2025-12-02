//package cn.hex.ddp.manufacture.infrastructure.simulator;
//import cn.hex.ddp.manufacture.domain.common.enums.ProcessEnum;
//import cn.hex.ddp.manufacture.domain.simulator.manager.WorkStationSimilatorManager;
//import cn.hex.ddp.manufacture.domain.simulator.model.WorkStationSimulatorData;
//import cn.hex.ddp.manufacture.domain.simulator.model.WorkstationVaryData;
//import cn.hex.ddp.manufacture.domain.workstation.enums.WorkstationStatusEnum;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
///**
// * 工位仿真数据测试类
// * @Author: FengZebang
// * @Date: 2025/3/5
// */
//@SpringBootTest
//public class WorkStationSimilatorDataTest {
//    @Autowired
//    private WorkStationSimilatorManager workStationSimilatorManager;
//    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
//    /**
//     * 测试工位仿真数据存储
//     * @throws JsonProcessingException
//     */
//    @Test
//    public void saveWorkStationSimilatorData() throws JsonProcessingException {
//        //构造数据
//        List<WorkstationVaryData> workstationVaryDatas = new ArrayList<>();
//        for(int i = 1; i < 10; i++){
//            WorkstationVaryData workstationVaryData = new WorkstationVaryData();
//            workstationVaryData.setId(Long.valueOf(i));
//            workstationVaryData.setStatus(WorkstationStatusEnum.FREE);
//            workstationVaryData.setProcess(ProcessEnum.COOLING);
//            workstationVaryData.setPriority(i);
//            workstationVaryData.setCurrentAction("当前操作： 空闲");
//        }
//        //线程池
//        int threadNum = 30;
//        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
//        //提交任务
//        for(int threadId = 1 ; threadId <= threadNum ; threadId++){
//            int finalThreadId = threadId;
//            executorService.submit(() -> {
//                try {
//                    long startAll = System.currentTimeMillis();
//                    for(int i=1 ; i <= 30 ; i++){
//                        WorkStationSimulatorData workStationSimulatorData = new WorkStationSimulatorData();
//                        workStationSimulatorData.setMission(Long.valueOf(finalThreadId));
//                        workStationSimulatorData.setTick(Long.valueOf(i));
//                        workStationSimulatorData.setSimulationTime(LocalDateTime.now());
//                        workStationSimulatorData.setData(OBJECT_MAPPER.writeValueAsString(workstationVaryDatas));
//                        long start = System.currentTimeMillis();
//                        workStationSimilatorManager.saveWorkStationRealTime(workStationSimulatorData);
//                        long end = System.currentTimeMillis();
//                        System.out.println("线程：" + finalThreadId + "，第" + i + "次提交，耗时：" + (end - start) + "ms");
//                    }
//                    long endAll = System.currentTimeMillis();
//                    System.out.println("线程：" + finalThreadId + "，提交完成，耗时：" + (endAll - startAll) + "ms");
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            });
//        }
//        executorService.shutdown();
//        while (!executorService.isTerminated()){
//        }
//    }
//    /**
//     * 测试工位仿真数据查询
//     */
//    @Test
//    public void getWorkStationRealTimeTest(){
//        int ThreadNum = 30;
//        ExecutorService executorService = Executors.newFixedThreadPool(ThreadNum);
//        for(int threadId = 1 ; threadId <= ThreadNum ; threadId++){
//            int finalThreadId = threadId;
//            executorService.submit(() -> {
//                try {
//                    long startAll = System.currentTimeMillis();
//                    Long tickStart = 1L;
//                    Long ticktotal = 20L;
//                    Long tickBatch = 5L;
//                    while (tickStart <= ticktotal){
//                        long start = System.currentTimeMillis();
//                        List<WorkStationSimulatorData> workStationSimulatorDatas = workStationSimilatorManager.getWorkStationRealTimes(
//                                Long.valueOf(finalThreadId), tickStart, tickStart + tickBatch);
//                        long end = System.currentTimeMillis();
//                        System.out.println("线程：" + finalThreadId + "，查询第" + tickStart + "到" + (tickStart + tickBatch) + "条，耗时：" + (end - start) + "ms");
//                        tickStart +=tickBatch;
//                        TimeUnit.MILLISECONDS.sleep(500L);
//                    }
//                    long endAll = System.currentTimeMillis();
//                    System.out.println("线程：" + finalThreadId + "，查询完成，耗时：" + (endAll - startAll) + "ms");
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            });
//        }
//        executorService.shutdown();
//        while (!executorService.isTerminated()){
//        }
//    }
//}